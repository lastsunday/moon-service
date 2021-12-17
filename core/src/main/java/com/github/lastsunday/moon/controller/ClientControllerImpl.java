package com.github.lastsunday.moon.controller;

import com.github.lastsunday.moon.config.AppConfig;
import com.github.lastsunday.moon.config.log.OperationLog;
import com.github.lastsunday.moon.config.log.emun.FunctionModule;
import com.github.lastsunday.moon.config.log.emun.Operation;
import com.github.lastsunday.moon.constant.Constants;
import com.github.lastsunday.moon.controller.dto.ClientInfoResultDTO;
import com.github.lastsunday.moon.controller.dto.ClientLoginParamDTO;
import com.github.lastsunday.moon.controller.dto.ClientLoginResultDTO;
import com.github.lastsunday.moon.controller.dto.ClientResetPasswordParamDTO;
import com.github.lastsunday.moon.data.component.CacheComponent;
import com.github.lastsunday.moon.data.domain.RoleDO;
import com.github.lastsunday.moon.data.domain.UserDO;
import com.github.lastsunday.moon.data.domain.UserRoleDO;
import com.github.lastsunday.moon.data.mapper.RoleMapper;
import com.github.lastsunday.moon.data.mapper.UserMapper;
import com.github.lastsunday.moon.data.mapper.UserRoleMapper;
import com.github.lastsunday.moon.security.LoginUser;
import com.github.lastsunday.moon.security.TokenService;
import com.github.lastsunday.moon.util.SecurityUtils;
import com.github.lastsunday.service.core.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/client")
public class ClientControllerImpl implements ClientController {

    public static final Logger log = LoggerFactory.getLogger(ClientControllerImpl.class);

    @Autowired
    protected AppConfig appConfig;

    @Autowired
    protected TokenService tokenService;

    @Resource
    protected AuthenticationManager authenticationManager;

    @Autowired
    protected CacheComponent redisCache;

    @Autowired
    protected UserRoleMapper userRoleMapper;

    @Autowired
    protected RoleMapper roleMapper;

    @Autowired
    protected UserMapper userMapper;

    @Override
    @RequestMapping(path = "login", method = RequestMethod.POST)
    @OperationLog(functionModule = FunctionModule.CLIENT, operation = Operation.CLIENT_LOGIN)
    public ClientLoginResultDTO login(@Valid @RequestBody ClientLoginParamDTO param) {
        if (appConfig.getModule().getClient().isLoginCaptchaCheckingEnable()) {
            // 验证码校验
            String verifyKey = Constants.CAPTCHA_CODE_KEY + param.getUuid();
            String captcha = redisCache.getRaw(verifyKey);
            if (captcha == null) {
                throw new CommonException(MESSAGE_CLIENT_VERIFY_CODE_NOT_SENT_OR_EXPIRED);
            }
            if (!captcha.equalsIgnoreCase(param.getVerifyCode())) {
                throw new CommonException(MESSAGE_CLIENT_VERIFY_CODE_NOT_CORRECT);
            }
            redisCache.del(verifyKey);
        } else {
            log.debug("skip checking captcha code for: account=" + param.getAccount() + ",verifyCode="
                    + param.getVerifyCode() + ",uuid=" + param.getUuid());
        }
        ClientLoginResultDTO result = new ClientLoginResultDTO();
        // 用户验证
        Authentication authentication = null;
        try {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(param.getAccount(), param.getPassword()));
        } catch (BadCredentialsException e) {
            throw new CommonException(MESSAGE_CLIENT_NAME_OR_PASSWORD_NOT_MATCH);
        }
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // 生成token
        result.setToken(tokenService.createToken(loginUser));
        return result;
    }

    @Override
    @RequestMapping(path = "info", method = RequestMethod.POST)
    public ClientInfoResultDTO info() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        UserDO userDo = userMapper.selectById(loginUser.getId());
        ClientInfoResultDTO result = new ClientInfoResultDTO();
        Set<String> userRoles = new HashSet<String>();
        List<UserRoleDO> userRoleDoList = userRoleMapper.selectUserRoleByUserId(userDo.getId());
        for (int i = 0; i < userRoleDoList.size(); i++) {
            userRoles.add(userRoleDoList.get(i).getRoleId());
        }
        Set<String> roles = new HashSet<String>();
        if (userRoles.size() > 0) {
            List<RoleDO> roleDoList = roleMapper.selectBatchIds(userRoles);
            for (RoleDO roleDo : roleDoList) {
                roles.add(roleDo.getName());
            }
        } else {
            // skip
        }
        result.setRoles(roles);
        result.setPermissions(loginUser.getPermissions());
        result.setName(userDo.getName());
        // TODO
//		result.setIntroduction();
        if (userDo.getAvatar() != null) {
            result.setAvatar(userDo.getAvatar());
        } else {
            result.setAvatar("");
        }
        return result;
    }

    @Override
    @RequestMapping(path = "resetPassword", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    @OperationLog(functionModule = FunctionModule.CLIENT, operation = Operation.CLIENT_RESET_PASSWORD)
    public void resetPassword(@RequestBody @Valid ClientResetPasswordParamDTO param) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        UserDO userDo = userMapper.selectById(loginUser.getId());
        if (userDo != null) {
            if (SecurityUtils.matchesPassword(param.getCurrentPassword(), userDo.getPassword())) {
                if (!SecurityUtils.matchesPassword(param.getNewPassword(), userDo.getPassword())) {
                    UserDO resultUserDo = new UserDO();
                    resultUserDo.setId(userDo.getId());
                    resultUserDo.setPassword(SecurityUtils.encryptPassword(param.getNewPassword()));
                    userMapper.updateById(resultUserDo);
                } else {
                    throw new CommonException(MESSAGE_CLIENT_NEW_PASSWORD_CANT_EQUAL_CURRENT_PASSWORD);
                }
            } else {
                throw new CommonException(MESSAGE_CLIENT_CURRENT_PASSWORD_INCORRECT);
            }
        } else {
            throw new CommonException(MESSAGE_CLIENT_NOT_EXISTS);
        }
    }

    @Override
    @RequestMapping(path = "logout", method = RequestMethod.POST)
    @OperationLog(functionModule = FunctionModule.CLIENT, operation = Operation.CLIENT_LOGOUT)
    public void logout() {
        redisCache.del(Constants.LOGIN_TOKEN_KEY + SecurityUtils.getLoginUser().getToken());
    }

}
