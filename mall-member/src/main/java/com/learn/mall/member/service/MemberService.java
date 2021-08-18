package com.learn.mall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.common.utils.PageUtils;
import com.learn.mall.member.entity.MemberEntity;
import com.learn.mall.member.entity.vo.MemberLoginVo;
import com.learn.mall.member.entity.vo.MemberRegisterVo;
import com.learn.mall.member.entity.vo.SocialUser;
import com.learn.mall.member.exception.PhoneExistException;
import com.learn.mall.member.exception.UsernameExistException;

import java.util.Map;

/**
 * 会员
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 20:55:02
 */
@SuppressWarnings("all")
public interface MemberService extends IService<MemberEntity> {

    /**
     * 基础分页查询
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 用户注册
     */
    void register(MemberRegisterVo vo);

    /**
     * 在注册接口中检查手机号是否唯一
     */
    void checkPhoneUnique(String phone) throws PhoneExistException;

    /**
     * 在注册接口中检查用户名是否唯一
     */
    void checkUsernameUnique(String username) throws UsernameExistException;

    /**
     * 用户登录
     */
    MemberEntity login(MemberLoginVo vo);

    /**
     * 社交登录，此接口包含注册与登录逻辑，当第一次登录时，系统会直接注册
     */
    MemberEntity oauth2LoginAndRegister(SocialUser socialUser) throws Exception;

}

