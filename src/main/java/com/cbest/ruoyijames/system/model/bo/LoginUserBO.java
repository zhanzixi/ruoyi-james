package com.cbest.ruoyijames.system.model.bo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

/**
 * @author James
 * @date 2021/4/29 17:01
 */
@Data
public class LoginUserBO {

    private Integer id;
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;

    private Set<String> roleCodeSet;
    private Set<String> permissionSet;
}
