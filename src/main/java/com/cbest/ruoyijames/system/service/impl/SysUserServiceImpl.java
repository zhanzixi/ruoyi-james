package com.cbest.ruoyijames.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cbest.ruoyijames.common.constant.Constants;
import com.cbest.ruoyijames.common.constant.StatusEnum;
import com.cbest.ruoyijames.common.util.ValidatorUtils;
import com.cbest.ruoyijames.system.entity.*;
import com.cbest.ruoyijames.system.mapper.SysUserMapper;
import com.cbest.ruoyijames.system.model.bo.LoginUserBO;
import com.cbest.ruoyijames.system.model.vo.UserInfoVO;
import com.cbest.ruoyijames.system.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author James
 * @since 2021-04-23
 */
@AllArgsConstructor
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    private final ValidatorUtils validatorUtils;
    private final ISysUserPostService iSysUserPostService;
    private final ISysUserRoleService iSysUserRoleService;
    private final ISysMenuService iSysMenuService;
    private final ISysRoleService iSysRoleService;
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveWithPostRole(SysUser user) {
        validatorUtils.validate(user);
        user.setPassword(MD5.create().digestHex(user.getPassword()));
        this.save(user);
        this.savePostAndRole(user);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateByIdWithPostRole(SysUser user) {
        validatorUtils.validate(user);
        // 不更新用户名称和密码
        this.lambdaUpdate().eq(SysUser::getId, user.getId())
                .set(SysUser::getNickname, user.getNickname())
                .set(SysUser::getDeptId, user.getDeptId())
                .set(SysUser::getPhoneNumber, user.getPhoneNumber())
                .set(SysUser::getEmail, user.getEmail())
                .set(SysUser::getSex, user.getSex())
                .set(SysUser::getStatus, user.getStatus())
                .set(SysUser::getRemark, user.getRemark())
                .update();
        iSysUserPostService.lambdaUpdate().eq(SysUserPost::getUserId, user.getId()).remove();
        iSysUserRoleService.lambdaUpdate().eq(SysUserRole::getUserId, user.getId()).remove();
        this.savePostAndRole(user);
    }

    private void savePostAndRole(SysUser user) {
        if (!CollectionUtils.isEmpty(user.getPostIdList())) {
            List<SysUserPost> userPostList = user.getPostIdList().stream().map(postId -> {
                SysUserPost userPost = new SysUserPost();
                userPost.setUserId(user.getId());
                userPost.setPostId(postId);
                return userPost;
            }).collect(Collectors.toList());
            iSysUserPostService.saveBatch(userPostList);
        }
        if (!CollectionUtils.isEmpty(user.getRoleIdList())) {
            List<SysUserRole> userRoleList = user.getRoleIdList().stream().map(roleId -> {
                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(user.getId());
                userRole.setRoleId(roleId);
                return userRole;
            }).collect(Collectors.toList());
            iSysUserRoleService.saveBatch(userRoleList);
        }
    }

    @Override
    public String login(LoginUserBO loginUserBO) {
        validatorUtils.validate(loginUserBO);
        SysUser user = this.lambdaQuery()
                .eq(SysUser::getName, loginUserBO.getUsername())
                .eq(SysUser::getStatus, StatusEnum.NORMAL)
                .one();
        if (user == null) {
            throw new RuntimeException("用户名不存在");
        }
        if (!StringUtils.equals(user.getPassword(), MD5.create().digestHex(loginUserBO.getPassword()))) {
            throw new RuntimeException("密码不正确");
        }

        LoginUserBO loginUser = new LoginUserBO();
        loginUser.setId(user.getId());
        List<SysMenu> menuList = this.getBaseMapper().getMenuListByUserId(user.getId());
        Set<String> permissionSet = menuList.stream()
                .filter(e -> e.getType() == SysMenu.Type.BUTTON)
                .filter(e -> StringUtils.isNoneEmpty(e.getPerms()))
                .map(SysMenu::getPerms)
                .collect(Collectors.toSet());
        loginUser.setPermissionSet(permissionSet);

        List<Integer> roleIdList = iSysUserRoleService.lambdaQuery()
                .eq(SysUserRole::getUserId, user.getId()).list()
                .stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(roleIdList)) {
            Set<String> roleCodeSet = iSysRoleService.lambdaQuery()
                    .in(SysRole::getId, roleIdList).select(SysRole::getCode)
                    .list().stream().map(SysRole::getCode).collect(Collectors.toSet());
            loginUser.setRoleCodeSet(roleCodeSet);
        }

        // 生成token
        String uuid = UUID.randomUUID().toString();
        String jws = Jwts.builder()
                .setClaims(Collections.singletonMap(Constants.LOGIN_USER_KEY, uuid))
                .signWith(SignatureAlgorithm.HS256, Constants.SECRET_KEY)
                .compact();

        // 存入redis
        BoundValueOperations<String, String> valueOps = stringRedisTemplate.boundValueOps(Constants.LOGIN_TOKEN_KEY + uuid);
        try {
            valueOps.set(objectMapper.writeValueAsString(loginUser), 30, TimeUnit.MINUTES);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        return jws;
    }

    @Override
    public LoginUserBO getLoginUser() {
        HttpServletRequest request = getRequest();
        String token = request.getHeader(Constants.TOKEN_HEADER);
        if (StringUtils.isNotEmpty(token) && token.startsWith(Constants.TOKEN_PREFIX)) {
            token = token.replace(Constants.TOKEN_PREFIX, "");
            Claims claims = Jwts.parser()
                    .setSigningKey(Constants.SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            String uuid = (String) claims.get(Constants.LOGIN_USER_KEY);
            BoundValueOperations<String, String> valueOps = stringRedisTemplate.boundValueOps(Constants.LOGIN_TOKEN_KEY + uuid);
            String value = valueOps.get();
            if (StringUtils.isNoneEmpty(value)) {
                valueOps.expire(30, TimeUnit.MINUTES);
                try {
                    return objectMapper.readValue(value, LoginUserBO.class);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public UserInfoVO getUserInfo() {
        LoginUserBO loginUser = this.getLoginUser();
        if (loginUser != null) {
            UserInfoVO userInfoVO = new UserInfoVO();
            SysUser user = this.getById(loginUser.getId());
            if (user != null) {
                userInfoVO.setNickname(user.getNickname());
            }

            List<SysMenu> menuList;
            if (CollectionUtil.contains(loginUser.getRoleCodeSet(), "admin")) {// 管理员
                menuList = iSysMenuService.lambdaQuery().orderByAsc(SysMenu::getParentId, SysMenu::getSort).list();
            } else {
                menuList = this.getBaseMapper().getMenuListByUserId(loginUser.getId());
            }
            Set<String> permissionSet = menuList.stream()
                    .filter(e -> e.getType() == SysMenu.Type.BUTTON)
                    .filter(e -> StringUtils.isNoneEmpty(e.getPerms()))
                    .map(SysMenu::getPerms)
                    .collect(Collectors.toSet());
            userInfoVO.setPermissionSet(permissionSet);
            List<SysMenu> menus = menuList.stream()
                    .filter(e -> e.getType() != SysMenu.Type.BUTTON)
                    .collect(Collectors.toList());
            userInfoVO.setMenuList(menus);
            return userInfoVO;
        }
        return null;
    }

    private ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }

    private HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }
}
