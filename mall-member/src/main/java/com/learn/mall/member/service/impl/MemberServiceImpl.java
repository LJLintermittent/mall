package com.learn.mall.member.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.learn.common.utils.HttpUtils;
import com.learn.mall.member.entity.MemberLevelEntity;
import com.learn.mall.member.entity.vo.MemberLoginVo;
import com.learn.mall.member.entity.vo.MemberRegisterVo;
import com.learn.mall.member.entity.vo.SocialUser;
import com.learn.mall.member.exception.PhoneExistException;
import com.learn.mall.member.exception.UsernameExistException;
import com.learn.mall.member.service.MemberLevelService;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learn.common.utils.PageUtils;
import com.learn.common.utils.Query;

import com.learn.mall.member.dao.MemberDao;
import com.learn.mall.member.entity.MemberEntity;
import com.learn.mall.member.service.MemberService;

@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Autowired
    private MemberLevelService memberLevelService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 用户注册功能
     */
    @Override
    public void register(MemberRegisterVo vo) {
        MemberEntity memberEntity = new MemberEntity();
        //设置用户默认等级
        MemberLevelEntity levelEntity = memberLevelService.getDefaultLevelInfo();
        memberEntity.setLevelId(levelEntity.getId());
        //检查用户名和手机号是否唯一(要让controller感知异常，如果有不唯一的输入，要往上层抛出异常信息)
        checkPhoneUnique(vo.getPhone());
        checkUsernameUnique(vo.getUsername());
        memberEntity.setMobile(vo.getPhone());
        memberEntity.setUsername(vo.getUsername());
        memberEntity.setNickname(vo.getUsername());
        //密码加密处理
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passwordWithSalt = passwordEncoder.encode(vo.getPassword());
        memberEntity.setPassword(passwordWithSalt);
        baseMapper.insert(memberEntity);
    }

    /**
     * 手机号已存在 异常
     */
    @Override
    public void checkPhoneUnique(String phone) throws PhoneExistException {
        MemberEntity memberEntity = baseMapper.selectOne(new QueryWrapper<MemberEntity>()
                .eq("mobile", phone));
        if (memberEntity != null) {
            throw new PhoneExistException();
        }
    }

    /**
     * 用户名已存在 异常
     */
    @Override
    public void checkUsernameUnique(String username) throws UsernameExistException {
        MemberEntity memberEntity = baseMapper.selectOne(new QueryWrapper<MemberEntity>()
                .eq("username", username));
        if (memberEntity != null) {
            throw new UsernameExistException();
        }
    }

    /**
     * 用户普通登录功能
     */
    @Override
    public MemberEntity login(MemberLoginVo vo) {
        //这个account有可能是用户名或者手机号
        //存在一个人的手机号刚好是另一个人的用户名这种情况
        String loginAccount = vo.getLoginAccount();
        String password = vo.getPassword();
        List<MemberEntity> memberEntities = baseMapper.selectList(new QueryWrapper<MemberEntity>()
                .eq("username", loginAccount).or()
                .eq("mobile", loginAccount));
        if (memberEntities == null) {
            //根本无法通过用户名或手机号查到这个用户
            return null;
        } else {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            for (MemberEntity memberEntity : memberEntities) {
                String passwordDB = memberEntity.getPassword();
                boolean isSuccess = passwordEncoder.matches(password, passwordDB);
                if (isSuccess) {
                    return memberEntity;
                }
            }
        }
        return null;
    }

    /**
     * 社交登录业务
     * 此方法具有登录 和 注册 的合并逻辑
     * 当用户第一次使用社交登录时，创建一个本系统的用户信息
     */
    @Override
    public MemberEntity oauth2LoginAndRegister(SocialUser socialUser) throws Exception {
        String uid = socialUser.getUid();
        //判断当前用户是否已经登录过本系统（社交登录即相当于在本系统注册）
        MemberEntity memberEntity = baseMapper.selectOne(new QueryWrapper<MemberEntity>()
                .eq("social_uid", uid));
        if (memberEntity != null) {
            //这个用户已经注册过,所以此接口为登录逻辑
            MemberEntity updateMemberEntity = new MemberEntity();
            updateMemberEntity.setId(memberEntity.getId());
            updateMemberEntity.setAccessToken(socialUser.getAccess_token());
            updateMemberEntity.setExpiresIn(socialUser.getExpires_in());

            baseMapper.updateById(updateMemberEntity);

            memberEntity.setAccessToken(socialUser.getAccess_token());
            memberEntity.setExpiresIn(socialUser.getExpires_in());
            return memberEntity;
        } else {
            //没有查到当前社交用户,我们需要创建注册一个用户信息到我们的系统中
            MemberEntity registerMemberEntity = new MemberEntity();
            //远程查询社交用户在微博平台的个人信息，但是这个接口如果失败，也不应该影响我们的注册业务，只需要往里面插入记录即可
            try {
                //查询当前社交用户的社交账号开放的信息（昵称，性别，头像等）
                Map<String, String> queryMap = new HashMap<>();
                queryMap.put("access_token", socialUser.getAccess_token());
                queryMap.put("uid", socialUser.getUid());
                HttpResponse response = HttpUtils.doGet("http://api.weibo.com"
                        , "/2/users/show.json", "get",
                        new HashMap<String, String>(), queryMap);
                if (response.getStatusLine().getStatusCode() == 200) {
                    //查询成功
                    String json = EntityUtils.toString(response.getEntity());
                    JSONObject jsonObject = JSON.parseObject(json);
                    //昵称
                    String nickName = jsonObject.getString("name");
                    //性别
                    String gender = jsonObject.getString("gender");
                    registerMemberEntity.setNickname(nickName);
                    //返回的json数据中 男为m 本系统数据库中gender字段存 1为男，0为女
                    registerMemberEntity.setGender("m".equals(gender) ? 1 : 0);
                }
            } catch (Exception e) {
            }
            registerMemberEntity.setSocialUid(socialUser.getUid());
            registerMemberEntity.setAccessToken(socialUser.getAccess_token());
            registerMemberEntity.setExpiresIn(socialUser.getExpires_in());
            baseMapper.insert(registerMemberEntity);
            return registerMemberEntity;
        }
    }
}