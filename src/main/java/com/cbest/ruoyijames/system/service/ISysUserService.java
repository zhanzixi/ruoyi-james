package com.cbest.ruoyijames.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cbest.ruoyijames.system.entity.SysUser;
import com.cbest.ruoyijames.system.model.bo.LoginUserBO;
import com.cbest.ruoyijames.system.model.vo.UserInfoVO;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author James
 * @since 2021-04-23
 */
public interface ISysUserService extends IService<SysUser> {
    void saveWithPostRole(SysUser user);

    void updateByIdWithPostRole(SysUser user);

    String login(LoginUserBO loginUserBO);

    LoginUserBO getLoginUser();

    UserInfoVO getUserInfo();
}
