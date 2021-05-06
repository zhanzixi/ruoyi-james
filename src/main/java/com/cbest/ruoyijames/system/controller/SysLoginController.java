package com.cbest.ruoyijames.system.controller;

import com.cbest.ruoyijames.common.model.AjaxResult;
import com.cbest.ruoyijames.system.model.bo.LoginUserBO;
import com.cbest.ruoyijames.system.model.vo.UserInfoVO;
import com.cbest.ruoyijames.system.service.ISysUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.UUID;

/**
 * @author James
 * @date 2021/4/29 15:30
 */
@AllArgsConstructor
@Api(tags = "登录管理")
@RestController
public class SysLoginController {

    private final ISysUserService iSysUserService;

    @ApiOperation("登录")
    @PostMapping("/system/login")
    public AjaxResult<String> login(@RequestBody LoginUserBO loginUserBO) {
        return AjaxResult.success(iSysUserService.login(loginUserBO));
    }

    @ApiOperation("得到用户信息（菜单、权限等）")
    @GetMapping("/system/user-info")
    public AjaxResult<UserInfoVO> getUserInfo() {
        return AjaxResult.success(iSysUserService.getUserInfo());
    }

    public static void main(String[] args) {
        String uuid = UUID.randomUUID().toString();
        String secretKey = "abcdefghijklmnopqrstuvwxyz";
        System.out.println(uuid + "|" + secretKey);
        String jws = Jwts.builder()
                .setClaims(Collections.singletonMap("login_user_key", uuid))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        System.out.println("jws:" + jws);
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jws)
                .getBody();
        String login_user_key = (String) claims.get("login_user_key");
        System.out.println(login_user_key);
    }
}
