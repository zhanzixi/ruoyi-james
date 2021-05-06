package com.cbest.ruoyijames.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cbest.ruoyijames.common.util.ValidatorUtils;
import com.cbest.ruoyijames.system.entity.SysRole;
import com.cbest.ruoyijames.system.entity.SysRoleDept;
import com.cbest.ruoyijames.system.entity.SysRoleMenu;
import com.cbest.ruoyijames.system.mapper.SysRoleMapper;
import com.cbest.ruoyijames.system.service.ISysRoleDeptService;
import com.cbest.ruoyijames.system.service.ISysRoleMenuService;
import com.cbest.ruoyijames.system.service.ISysRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色信息表 服务实现类
 * </p>
 *
 * @author James
 * @since 2021-04-27
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    private final ValidatorUtils validatorUtils;
    private final ISysRoleMenuService iSysRoleMenuService;
    private final ISysRoleDeptService iSysRoleDeptService;

    public SysRoleServiceImpl(ValidatorUtils validatorUtils, ISysRoleMenuService iSysRoleMenuService, ISysRoleDeptService iSysRoleDeptService) {
        this.validatorUtils = validatorUtils;
        this.iSysRoleMenuService = iSysRoleMenuService;
        this.iSysRoleDeptService = iSysRoleDeptService;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveWithMenus(SysRole role) {
        validatorUtils.validate(role);
        this.save(role);
        if (!CollectionUtils.isEmpty(role.getMenuIdList())) {
            List<SysRoleMenu> roleMenuList = role.getMenuIdList().stream().map(menuId -> {
                SysRoleMenu roleMenu = new SysRoleMenu();
                roleMenu.setRoleId(role.getId());
                roleMenu.setMenuId(menuId);
                return roleMenu;
            }).collect(Collectors.toList());
            iSysRoleMenuService.saveBatch(roleMenuList);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateByIdWithMenus(SysRole role) {
        validatorUtils.validate(role);
        this.updateById(role);

        iSysRoleMenuService.lambdaUpdate()
                .eq(SysRoleMenu::getRoleId, role.getId())
                .remove();

        if (!CollectionUtils.isEmpty(role.getMenuIdList())) {
            List<SysRoleMenu> roleMenuList = role.getMenuIdList().stream().map(menuId -> {
                SysRoleMenu roleMenu = new SysRoleMenu();
                roleMenu.setRoleId(role.getId());
                roleMenu.setMenuId(menuId);
                return roleMenu;
            }).collect(Collectors.toList());
            iSysRoleMenuService.saveBatch(roleMenuList);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateByIdWithDept(SysRole role) {
        this.lambdaUpdate()
                .eq(SysRole::getId, role.getId())
                .set(SysRole::getDataScope, role.getDataScope())
                .update();
        if (role.getDataScope() == SysRole.DataScopeEnum.CUSTOM && !CollectionUtils.isEmpty(role.getDeptIdList())) {
            iSysRoleDeptService.lambdaUpdate()
                    .eq(SysRoleDept::getRoleId, role.getId())
                    .remove();
            List<SysRoleDept> roleDeptList = role.getDeptIdList().stream().map(deptId -> {
                SysRoleDept roleDept = new SysRoleDept();
                roleDept.setRoleId(role.getId());
                roleDept.setDeptId(deptId);
                return roleDept;
            }).collect(Collectors.toList());
            iSysRoleDeptService.saveBatch(roleDeptList);
        }
    }
}
