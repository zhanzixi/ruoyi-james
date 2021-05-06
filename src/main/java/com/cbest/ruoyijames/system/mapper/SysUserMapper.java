package com.cbest.ruoyijames.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cbest.ruoyijames.system.entity.SysMenu;
import com.cbest.ruoyijames.system.entity.SysUser;

import java.util.List;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author James
 * @since 2021-04-23
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
    List<SysMenu> getMenuListByUserId(Integer userId);
}
