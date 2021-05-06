package com.cbest.ruoyijames.config;

import cn.hutool.http.HttpStatus;
import com.cbest.ruoyijames.common.BusinessException;
import com.cbest.ruoyijames.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author James
 * @date 2021/4/29 20:38
 */
@Component
@Slf4j
public class LoginInterceptor extends HandlerInterceptorAdapter {


    private final StringRedisTemplate stringRedisTemplate;
    private final ISysUserService iSysUserService;

    public LoginInterceptor(StringRedisTemplate stringRedisTemplate, ISysUserService iSysUserService) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.iSysUserService = iSysUserService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (iSysUserService.getLoginUser() != null) {
            return true;
        }
        throw new BusinessException(HttpStatus.HTTP_UNAUTHORIZED, "请登录");
    }
}
