package com.learn.mall.member.controller;

import java.util.Arrays;
import java.util.Map;

import com.learn.common.exception.StatusCodeEnum;
import com.learn.mall.member.entity.vo.MemberLoginVo;
import com.learn.mall.member.entity.vo.MemberRegisterVo;
import com.learn.mall.member.entity.vo.SocialUser;
import com.learn.mall.member.exception.PhoneExistException;
import com.learn.mall.member.exception.UsernameExistException;
import com.learn.mall.member.feign.CouponFeignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.learn.mall.member.entity.MemberEntity;
import com.learn.mall.member.service.MemberService;
import com.learn.common.utils.PageUtils;
import com.learn.common.utils.R;


/**
 * 会员
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 20:55:02
 */
@Api(tags = "会员模块")
@RestController
@RequestMapping("member/member")
@SuppressWarnings("all")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private CouponFeignService couponFeignService;

    /**
     * 社交登录业务
     */
    @ApiOperation(value = "社交登录业务")
    @PostMapping("/oauth2/login")
    public R oauth2LoginAndRegister(@RequestBody SocialUser socialUser) throws Exception {
        MemberEntity memberEntity = memberService.oauth2LoginAndRegister(socialUser);
        if (memberEntity != null) {
            return R.ok().setData(memberEntity);
        } else {
            return R.error(StatusCodeEnum.LOGIN_EXCEPTION.getCode(),
                    StatusCodeEnum.LOGIN_EXCEPTION.getMsg());
        }
    }

    /**
     * 会员登录业务
     */
    @ApiOperation(value = "普通会员登录")
    @PostMapping("/login")
    public R login(@RequestBody MemberLoginVo vo) {
        MemberEntity memberEntity = memberService.login(vo);
        if (memberEntity != null) {
            return R.ok().setData(memberEntity);
        } else {
            return R.error(StatusCodeEnum.LOGIN_EXCEPTION.getCode(),
                    StatusCodeEnum.LOGIN_EXCEPTION.getMsg());
        }
    }

    /**
     * 会员注册业务
     * http + json
     * 传过来的请求体里的json数据要通过@RequestBody注解来转为我们的接收的对象
     */
    @ApiOperation(value = "会员注册")
    @PostMapping("/register")
    public R register(@RequestBody MemberRegisterVo vo) {
        try {
            memberService.register(vo);
        } catch (PhoneExistException e) {
            return R.error(StatusCodeEnum.PHONE_EXIST_EXCEPTION.getCode(),
                    StatusCodeEnum.PHONE_EXIST_EXCEPTION.getMsg());
        } catch (UsernameExistException e) {
            return R.error(StatusCodeEnum.USER_EXIST_EXCEPTION.getCode(),
                    StatusCodeEnum.USER_EXIST_EXCEPTION.getMsg());
        }
        return R.ok();
    }

    /**
     * 测试 优惠券服务和会员服务的远程调用 （openFeign）
     *
     * @return 会员信息和该会员所对应的优惠券信息
     */
    @ApiOperation(value = "测试 优惠券服务和会员服务的远程调用 （openFeign）")
    @RequestMapping(value = "/coupons", method = RequestMethod.POST)
    public R test() {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setNickname("李佳乐");
        R r = couponFeignService.memberCoupon();
        return R.ok().put("member", memberEntity).put("coupons", r.get("coupons"));
    }

    /**
     * 列表
     */
    @ApiOperation(value = "列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = memberService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @ApiOperation(value = "信息")
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    public R info(@PathVariable("id") Long id) {
        MemberEntity member = memberService.getById(id);
        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @ApiOperation(value = "保存")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public R save(@RequestBody MemberEntity member) {
        memberService.save(member);
        return R.ok();
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public R update(@RequestBody MemberEntity member) {
        memberService.updateById(member);
        return R.ok();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public R delete(@RequestBody Long[] ids) {
        memberService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
