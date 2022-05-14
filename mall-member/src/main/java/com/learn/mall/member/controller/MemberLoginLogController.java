package com.learn.mall.member.controller;

import java.util.Arrays;
import java.util.Map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.learn.mall.member.entity.MemberLoginLogEntity;
import com.learn.mall.member.service.MemberLoginLogService;
import com.learn.common.utils.PageUtils;
import com.learn.common.utils.R;

/**
 * 会员登录记录
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 20:55:02
 */
@Api(tags = "会员登录记录模块")
@RestController
@RequestMapping("member/memberloginlog")
@SuppressWarnings("all")
public class MemberLoginLogController {

    @Autowired
    private MemberLoginLogService memberLoginLogService;

    /**
     * 列表
     */
    @ApiOperation(value = "列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = memberLoginLogService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @ApiOperation(value = "列表")
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    public R info(@PathVariable("id") Long id) {
        MemberLoginLogEntity memberLoginLog = memberLoginLogService.getById(id);
        return R.ok().put("memberLoginLog", memberLoginLog);
    }

    /**
     * 保存
     */
    @ApiOperation(value = "列表")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public R save(@RequestBody MemberLoginLogEntity memberLoginLog) {
        memberLoginLogService.save(memberLoginLog);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public R update(@RequestBody MemberLoginLogEntity memberLoginLog) {
        memberLoginLogService.updateById(memberLoginLog);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public R delete(@RequestBody Long[] ids) {
        memberLoginLogService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
