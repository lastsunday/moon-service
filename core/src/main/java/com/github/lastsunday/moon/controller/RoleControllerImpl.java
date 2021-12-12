package com.github.lastsunday.moon.controller;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import com.github.lastsunday.moon.data.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.lastsunday.moon.config.log.OperationLog;
import com.github.lastsunday.moon.config.log.emun.FunctionModule;
import com.github.lastsunday.moon.config.log.emun.Operation;
import com.github.lastsunday.moon.controller.dto.PageResultDTO;
import com.github.lastsunday.moon.controller.dto.RoleCreateParamDTO;
import com.github.lastsunday.moon.controller.dto.RoleCreateResultDTO;
import com.github.lastsunday.moon.controller.dto.RoleDeleteParamDTO;
import com.github.lastsunday.moon.controller.dto.RoleGetParamDTO;
import com.github.lastsunday.moon.controller.dto.RoleListParamDTO;
import com.github.lastsunday.moon.controller.dto.RoleUpdateParamDTO;
import com.github.lastsunday.moon.data.domain.RoleDO;
import com.github.lastsunday.moon.data.domain.RolePermissionDO;
import com.github.lastsunday.moon.data.domain.dto.RoleDTO;
import com.github.lastsunday.moon.data.mapper.RoleMapper;
import com.github.lastsunday.moon.data.mapper.RolePermissionMapper;
import com.github.lastsunday.moon.util.IdGenerator;
import com.github.lastsunday.service.core.CommonException;

@RestController
@RequestMapping("/api/system/role")
public class RoleControllerImpl implements RoleController {

    @Autowired
    protected RoleMapper roleMapper;
    @Autowired
    protected RolePermissionMapper rolePermissionMapper;
    @Autowired
    protected UserRoleMapper userRoleMapper;

    @Override
    @RequestMapping(path = "get", method = RequestMethod.POST)
    public RoleDTO get(@Valid @RequestBody RoleGetParamDTO param) {
        RoleDO entity = roleMapper.selectById(param.getId());
        if (entity != null) {
            RoleDTO result = copyAndReturn(entity, RoleDTO::new);
            List<RolePermissionDO> rolePermissionList = rolePermissionMapper
                    .selectRolePermissionByRoleId(param.getId());
            Set<String> permissonSet = new LinkedHashSet<String>();
            for (RolePermissionDO rolePermissionDo : rolePermissionList) {
                permissonSet.add(rolePermissionDo.getPermission());
            }
            result.setPermissions(permissonSet);
            return result;
        } else {
            throw new CommonException(MESSAGE_ENTITY_NOT_EXISTS);
        }
    }

    @Override
    @RequestMapping(path = "list", method = RequestMethod.POST)
    public PageResultDTO<RoleDTO> list(@Valid @RequestBody RoleListParamDTO param) {
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<RoleDO> list = roleMapper.select(param.getName());
        PageResultDTO<RoleDTO> result = copyAndReturn(list, RoleDTO::new);
        for (int i = 0; i < list.size(); i++) {
            RoleDO roleDo = list.get(i);
            List<RolePermissionDO> rolePermissionList = rolePermissionMapper
                    .selectRolePermissionByRoleId(roleDo.getId());
            Set<String> permissonSet = new LinkedHashSet<String>();
            for (RolePermissionDO rolePermissionDo : rolePermissionList) {
                permissonSet.add(rolePermissionDo.getPermission());
            }
            result.getItems().get(i).setPermissions(permissonSet);
        }
        return result;
    }

    @Override
    @RequestMapping(path = "create", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    @OperationLog(functionModule = FunctionModule.ROLE, operation = Operation.ADD)
    public RoleCreateResultDTO create(@Valid @RequestBody RoleCreateParamDTO param) {
        long existsEntityCount = roleMapper.selectCountByName(param.getName());
        if (existsEntityCount == 0) {
            RoleDO entity = copyAndReturn(param, RoleDO::new);
            entity.setId(IdGenerator.genUniqueStringId());
            fillBaseInfoForCreateDefaultDto(entity);
            roleMapper.insert(entity);
            // TODO 批量插入权限信息
            if (param.getPermissions() != null) {
                for (String permission : param.getPermissions()) {
                    RolePermissionDO rolePermissionDo = new RolePermissionDO();
                    rolePermissionDo.setId(IdGenerator.genUniqueStringId());
                    rolePermissionDo.setRoleId(entity.getId());
                    rolePermissionDo.setPermission(permission);
                    fillBaseInfoForCreateDefaultDto(rolePermissionDo);
                    rolePermissionMapper.insert(rolePermissionDo);
                }
            }
            return copyAndReturn(entity, RoleCreateResultDTO::new);
        } else {
            throw new CommonException(MESSAGE_ENTITY_EXISTS);
        }
    }

    @RequestMapping(path = "update", method = RequestMethod.POST)
    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationLog(functionModule = FunctionModule.ROLE, operation = Operation.UPDATE)
    public void update(@Valid @RequestBody RoleUpdateParamDTO param) {
        RoleDO entity = copyAndReturn(param, RoleDO::new);
        fillBaseInfoForUpdateDefaultDto(entity);
        RoleDO selectRoleDO = roleMapper.selectById(entity.getId());
        if (selectRoleDO == null) {
            throw new CommonException(MESSAGE_ENTITY_NOT_EXISTS);
        }
        if (!selectRoleDO.getCanModify()) {
            throw new CommonException(MESSAGE_ENTITY_UPDATE_FAILURE);
        }
        int result = roleMapper.updateById(entity);
        if (result > 0) {
            // skip
        } else {
            throw new CommonException(MESSAGE_ENTITY_UPDATE_FAILURE);
        }
        rolePermissionMapper.deleteRolePermissionByRoleId(param.getId());
        for (String permission : param.getPermissions()) {
            RolePermissionDO rolePermissionDo = new RolePermissionDO();
            rolePermissionDo.setId(IdGenerator.genUniqueStringId());
            rolePermissionDo.setRoleId(entity.getId());
            rolePermissionDo.setPermission(permission);
            fillBaseInfoForCreateDefaultDto(rolePermissionDo);
            rolePermissionMapper.insert(rolePermissionDo);
        }
    }

    @RequestMapping(path = "delete", method = RequestMethod.POST)
    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationLog(functionModule = FunctionModule.ROLE, operation = Operation.DELETE)
    public void delete(@Valid @RequestBody RoleDeleteParamDTO param) {
        RoleDO selectRoleDO = roleMapper.selectById(param.getId());
        if (selectRoleDO == null) {
            throw new CommonException(MESSAGE_ENTITY_NOT_EXISTS);
        }
        if (!selectRoleDO.getCanModify()) {
            throw new CommonException(MESSAGE_ENTITY_DELETE_FAILURE);
        }
        Long userCount = userRoleMapper.selectCountUserByRoleId(param.getId());
        if (userCount > 0) {
            throw new CommonException(MESSAGE_ROLE_DELETE_FAILURE_BY_OTHER_USER_REF);
        }
        rolePermissionMapper.deleteRolePermissionByRoleId(param.getId());
    }

    @Override
    @RequestMapping(path = "options", method = RequestMethod.POST)
    public List<Map<String, Object>> options() {
        List<RoleDO> roleDoList = roleMapper.selectList(new QueryWrapper<>());
        List<Map<String, Object>> list = new LinkedList<>();
        roleDoList.forEach(item -> {
            Map<String, Object> map = new HashMap<>();
            map.put("value", item.getId());
            map.put("label", item.getName());
            list.add(map);
        });
        return list;
    }

}
