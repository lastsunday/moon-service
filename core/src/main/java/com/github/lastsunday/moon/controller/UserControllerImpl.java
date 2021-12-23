package com.github.lastsunday.moon.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.github.lastsunday.moon.data.component.DataKeyBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.lastsunday.moon.config.log.OperationLog;
import com.github.lastsunday.moon.config.log.emun.FunctionModule;
import com.github.lastsunday.moon.config.log.emun.Operation;
import com.github.lastsunday.moon.constant.Constants;
import com.github.lastsunday.moon.controller.dto.PageResultDTO;
import com.github.lastsunday.moon.controller.dto.UserCreateParamDTO;
import com.github.lastsunday.moon.controller.dto.UserCreateResultDTO;
import com.github.lastsunday.moon.controller.dto.UserDeleteParamDTO;
import com.github.lastsunday.moon.controller.dto.UserGetParamDTO;
import com.github.lastsunday.moon.controller.dto.UserListParamDTO;
import com.github.lastsunday.moon.controller.dto.UserOnlineDTO;
import com.github.lastsunday.moon.controller.dto.UserOnlineForceLogoutParamDTO;
import com.github.lastsunday.moon.controller.dto.UserOnlineListParamDTO;
import com.github.lastsunday.moon.controller.dto.UserOnlineListParamDTO.SortField;
import com.github.lastsunday.moon.controller.dto.UserResetPasswordParamDTO;
import com.github.lastsunday.moon.controller.dto.UserUpdateParamDTO;
import com.github.lastsunday.moon.data.component.CacheComponent;
import com.github.lastsunday.moon.data.domain.RoleDO;
import com.github.lastsunday.moon.data.domain.UserDO;
import com.github.lastsunday.moon.data.domain.UserRoleDO;
import com.github.lastsunday.moon.data.domain.dto.UserDTO;
import com.github.lastsunday.moon.data.enums.UserStatusType;
import com.github.lastsunday.moon.data.mapper.RoleMapper;
import com.github.lastsunday.moon.data.mapper.UserMapper;
import com.github.lastsunday.moon.data.mapper.UserRoleMapper;
import com.github.lastsunday.moon.data.mapper.user.SelectUserDetailParam;
import com.github.lastsunday.moon.dto.SortOrder;
import com.github.lastsunday.moon.security.LoginUser;
import com.github.lastsunday.moon.util.IdGenerator;
import com.github.lastsunday.moon.util.SecurityUtils;
import com.github.lastsunday.moon.util.StringUtils;
import com.github.lastsunday.service.core.CommonException;

@RestController
@RequestMapping("/api/system/user")
public class UserControllerImpl implements UserController {

    public static final int INDEX_DATE_START = 0;
    public static final int INDEX_DATE_END = 1;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private CacheComponent redisCache;
    @Autowired
    private DataKeyBuilder dataKeyBuilder;

    @Override
    @RequestMapping(path = "get", method = RequestMethod.POST)
    @Transactional
    public UserDTO get(@RequestBody @Valid UserGetParamDTO param) {
        UserDO entity = userMapper.selectById(param.getId());
        if (entity != null) {
            UserDTO result = copyAndReturn(entity, UserDTO::new);
            result.setPassword(null);
            return result;
        } else {
            throw new CommonException(MESSAGE_ENTITY_NOT_EXISTS);
        }
    }

    @Override
    @RequestMapping(path = "list", method = RequestMethod.POST)
    public PageResultDTO<UserDTO> list(@RequestBody @Valid UserListParamDTO param) {
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        return copyAndReturn(userMapper.selectUserDetail(copyAndReturn(param, SelectUserDetailParam::new)),
                UserDTO::new);
    }

    @Override
    @RequestMapping(path = "create", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    @OperationLog(functionModule = FunctionModule.USER, operation = Operation.ADD)
    public UserCreateResultDTO create(@RequestBody @Valid UserCreateParamDTO param) {
        UserCreateResultDTO result = new UserCreateResultDTO();
        UserDO existsUser = userMapper.select(param.getName());
        if (existsUser == null) {
            UserDO entity = new UserDO();
            BeanUtils.copyProperties(param, entity);
            entity.setId(IdGenerator.genUniqueStringId());
            entity.setPassword(SecurityUtils.encryptPassword(param.getPassword()));
            entity.setRemark(param.getRemark());
            entity.setStatus(UserStatusType.getByCode(param.getStatus()).getCode());
            fillBaseInfoForCreateDefaultDto(entity);
            userMapper.insert(entity);
            if (param.getRoleIds() != null) {
                result.setRoleIds(new ArrayList<>());
                for (int i = 0; i < param.getRoleIds().size(); i++) {
                    RoleDO roleDo = roleMapper.selectById(param.getRoleIds().get(i));
                    if (roleDo == null) {
                        throw new CommonException(MESSAGE_ARGUMENT_NOT_VALID);
                    } else {
                        UserRoleDO userRoleDo = new UserRoleDO();
                        userRoleDo.setId(IdGenerator.genUniqueStringId());
                        userRoleDo.setRoleId(roleDo.getId());
                        userRoleDo.setUserId(entity.getId());
                        userRoleMapper.insert(userRoleDo);
                        result.getRoleIds().add(userRoleDo.getRoleId());
                    }
                }
            } else {
                // skip
            }
            BeanUtils.copyProperties(entity, result);
            return result;
        } else {
            throw new CommonException(MESSAGE_ENTITY_EXISTS);
        }
    }

    @Override
    @RequestMapping(path = "update", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    @PreAuthorize("@ss.hasPermission('system:user:update')")
    public void update(@RequestBody @Valid UserUpdateParamDTO param) {
        UserDO entity = new UserDO();
        BeanUtils.copyProperties(param, entity);
        entity.setStatus(UserStatusType.getByCode(param.getStatus()).getCode());
        fillBaseInfoForUpdateDefaultDto(entity);
        int result = userMapper.updateById(entity);
        if (result > 0) {
            // skip
        } else {
            throw new CommonException(MESSAGE_ENTITY_UPDATE_FAILURE);
        }
        userRoleMapper.deleteUserRoleByUserId(param.getId());
        if (param.getRoleIds() != null) {
            for (int i = 0; i < param.getRoleIds().size(); i++) {
                RoleDO roleDo = roleMapper.selectById(param.getRoleIds().get(i));
                if (roleDo == null) {
                    throw new CommonException(MESSAGE_ARGUMENT_NOT_VALID);
                } else {
                    UserRoleDO userRoleDo = new UserRoleDO();
                    userRoleDo.setId(IdGenerator.genUniqueStringId());
                    userRoleDo.setRoleId(roleDo.getId());
                    userRoleDo.setUserId(entity.getId());
                    fillBaseInfoForCreateDefaultDto(userRoleDo);
                    userRoleMapper.insert(userRoleDo);
                }
            }
        } else {
            // skip
        }
    }

    @Override
    @RequestMapping(path = "delete", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    @OperationLog(functionModule = FunctionModule.USER, operation = Operation.DELETE)
    public void delete(@RequestBody @Valid UserDeleteParamDTO param) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser.getId().equals(param.getId())) {
            throw new CommonException(MESSAGE_USER_DELETE_FAILURE_CANT_DELETE_SELF);
        } else {
            int result = userMapper.deleteById(param.getId());
            if (result > 0) {
                // success
                // skip
            } else {
                // failure
                throw new CommonException(MESSAGE_ENTITY_DELETE_FAILURE);
            }
        }
    }

    @Override
    @RequestMapping(path = "resetPassword", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    @OperationLog(functionModule = FunctionModule.USER, operation = Operation.USER_RESET_PASSWORD)
    public void resetPassword(@RequestBody @Valid UserResetPasswordParamDTO param) {
        UserDO userDo = userMapper.selectById(param.getUserId());
        if (userDo != null) {
            UserDO resultUserDo = new UserDO();
            resultUserDo.setId(userDo.getId());
            resultUserDo.setPassword(SecurityUtils.encryptPassword(param.getNewPassword()));
            userMapper.updateById(resultUserDo);
        } else {
            throw new CommonException(MESSAGE_USER_NOT_EXISTS);
        }
    }

    @Override
    @RequestMapping(path = "online/list", method = RequestMethod.POST)
    public PageResultDTO<UserOnlineDTO> list(@RequestBody @Valid UserOnlineListParamDTO param) {
        Collection<String> keys = redisCache.getPrefixKeySet(dataKeyBuilder.getKeyWithPrefix(Constants.USER_LOGIN_TOKEN_CACHE_KEY));
        List<UserOnlineDTO> userOnlineList = new ArrayList<UserOnlineDTO>();
        String ipaddr = param.getIpaddr();
        String userName = param.getUserName();
        for (String key : keys) {
            LoginUser user = redisCache.getObj(key, LoginUser.class);
            if (StringUtils.isNotEmpty(ipaddr) && StringUtils.isNotEmpty(userName)) {
                if (StringUtils.equals(ipaddr, user.getIpaddr()) && StringUtils.equals(userName, user.getUsername())) {
                    userOnlineList.add(selectOnlineByInfo(ipaddr, userName, user));
                }
            } else if (StringUtils.isNotEmpty(ipaddr)) {
                if (StringUtils.equals(ipaddr, user.getIpaddr())) {
                    userOnlineList.add(selectOnlineByIpaddr(ipaddr, user));
                }
            } else if (StringUtils.isNotEmpty(userName) && StringUtils.isNotNull(user.getId())) {
                if (StringUtils.equals(userName, user.getUsername())) {
                    userOnlineList.add(selectOnlineByUserName(userName, user));
                }
            } else {
                userOnlineList.add(loginUserToUserOnline(user));
            }
        }
        if (param.getLoginRangeDate() != null) {
            Date startDate = param.getLoginRangeDate().get(INDEX_DATE_START);
            Date endDate = param.getLoginRangeDate().get(INDEX_DATE_END);
            userOnlineList = userOnlineList.stream().filter(new Predicate<UserOnlineDTO>() {

                @Override
                public boolean test(UserOnlineDTO t) {
                    if (startDate != null && endDate != null) {
                        return t.getLoginTime() >= startDate.getTime() && t.getLoginTime() <= endDate.getTime();
                    } else if (startDate != null) {
                        return t.getLoginTime() >= startDate.getTime();
                    } else if (endDate != null) {
                        return t.getLoginTime() <= endDate.getTime();
                    } else {
                        return true;
                    }
                }
            }).collect(Collectors.toList());
        } else {
            // skip
        }
        long total = userOnlineList.size();
        int pageSize = param.getPageSize();
        int pageNum = param.getPageNum();
        int pages = (int) (total / pageSize);
        long startRow = pageNum > 0 ? (pageNum - 1) * pageSize : 0;
        long endRow = startRow + pageSize * (pageNum > 0 ? 1 : 0);
        if (endRow > total) {
            endRow = total;
        } else {
            // skip
        }
        if (total % pageSize > 0) {
            pages += 1;
        } else {
            // skip
        }
        if (param.getSortField() != null && SortField.LOGIN_TIME.equals(param.getSortField())
                && SortOrder.ASC.equals(param.getSortOrder())) {
            Collections.sort(userOnlineList, new Comparator<UserOnlineDTO>() {

                @Override
                public int compare(UserOnlineDTO o1, UserOnlineDTO o2) {
                    return (int) (o1.getLoginTime() - o2.getLoginTime());
                }
            });
        } else {
            Collections.sort(userOnlineList, new Comparator<UserOnlineDTO>() {

                @Override
                public int compare(UserOnlineDTO o1, UserOnlineDTO o2) {
                    if ((o1 == null || o1.getLoginTime() == null) && (o2 == null || o2.getLoginTime() == null)) {
                        return 0;
                    }
                    if (o1 == null || o1.getLoginTime() == null) {
                        return 1;
                    }
                    if (o2 == null || o2.getLoginTime() == null) {
                        return -1;
                    }
                    return o2.getLoginTime().compareTo(o1.getLoginTime());
                }
            });
        }
        userOnlineList = userOnlineList.subList((int) startRow, (int) endRow);
        PageResultDTO<UserOnlineDTO> result = copyAndReturn(userOnlineList, UserOnlineDTO::new);
        result.setPageNum(param.getPageNum());
        result.setPageSize(param.getPageSize());
        result.setTotal(total);
        result.setPages(pages);
        result.setStartRow(startRow);
        result.setEndRow(endRow);
        return result;
    }

    @Override
    @RequestMapping(path = "online/forceLogout", method = RequestMethod.POST)
    @OperationLog(functionModule = FunctionModule.USER, operation = Operation.USER_FORCE_LOGOUT)
    public void forceLogout(@RequestBody @Valid UserOnlineForceLogoutParamDTO param) {
        redisCache.del(dataKeyBuilder.getKeyWithPrefix(Constants.USER_LOGIN_TOKEN_CACHE_KEY) + param.getTokenId());
    }

    private UserOnlineDTO selectOnlineByInfo(String ipaddr, String userName, LoginUser user) {
        if (StringUtils.equals(ipaddr, user.getIpaddr()) && StringUtils.equals(userName, user.getUsername())) {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    private UserOnlineDTO selectOnlineByIpaddr(String ipaddr, LoginUser user) {
        if (StringUtils.equals(ipaddr, user.getIpaddr())) {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    private UserOnlineDTO selectOnlineByUserName(String userName, LoginUser user) {
        if (StringUtils.equals(userName, user.getUsername())) {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    private UserOnlineDTO loginUserToUserOnline(LoginUser user) {
        if (StringUtils.isNull(user) && StringUtils.isNull(user.getId())) {
            return null;
        }
        UserOnlineDTO userOnline = new UserOnlineDTO();
        userOnline.setTokenId(user.getToken());
        userOnline.setUserName(user.getUsername());
        userOnline.setIpaddr(user.getIpaddr());
//		userOnline.setLoginLocation(user.getLoginLocation());
        userOnline.setBrowser(user.getBrowser());
        userOnline.setOs(user.getOs());
        userOnline.setLoginTime(user.getLoginTime());
        return userOnline;
    }

}
