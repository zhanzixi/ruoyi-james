package com.cbest.ruoyijames.system.model.vo;

import com.cbest.ruoyijames.system.entity.SysMenu;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @author James
 * @date 2021/4/29 17:12
 */
@Data
public class UserInfoVO {
    private String nickname;
    private List<SysMenu> menuList;
    private Set<String> permissionSet;
}
