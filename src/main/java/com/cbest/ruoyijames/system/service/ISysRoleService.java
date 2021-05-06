package com.cbest.ruoyijames.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cbest.ruoyijames.system.entity.SysRole;

/**
 * <p>
 * 角色信息表 服务类
 * </p>
 *
 * @author James
 * @since 2021-04-27
 */
public interface ISysRoleService extends IService<SysRole> {
    void saveWithMenus(SysRole role);

    void updateByIdWithMenus(SysRole role);

    void updateByIdWithDept(SysRole role);
}
