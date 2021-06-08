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
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void register(MemberRegisterVo vo);

    void checkPhoneUnique(String phone) throws PhoneExistException;

    void checkUsernameUnique(String username) throws UsernameExistException;

    MemberEntity login(MemberLoginVo vo);

    MemberEntity oauth2LoginAndRegister(SocialUser socialUser) throws Exception;

}

