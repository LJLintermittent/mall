package com.learn.mall.member.controller;

import java.util.Arrays;
import java.util.Map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.learn.mall.member.entity.MemberCollectSpuEntity;
import com.learn.mall.member.service.MemberCollectSpuService;
import com.learn.common.utils.PageUtils;
import com.learn.common.utils.R;


/**
 * 会员收藏的商品
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 20:55:02
 */
@Api(tags = "会员收藏的商品模块")
@RestController
@RequestMapping("member/membercollectspu")
@SuppressWarnings("all")
public class MemberCollectSpuController {

    @Autowired
    private MemberCollectSpuService memberCollectSpuService;

    /**
     * 列表
     */
    @ApiOperation(value = "列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = memberCollectSpuService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @ApiOperation(value = "信息")
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    public R info(@PathVariable("id") Long id) {
        MemberCollectSpuEntity memberCollectSpu = memberCollectSpuService.getById(id);
        return R.ok().put("memberCollectSpu", memberCollectSpu);
    }

    /**
     * 保存
     */
    @ApiOperation(value = "保存")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public R save(@RequestBody MemberCollectSpuEntity memberCollectSpu) {
        memberCollectSpuService.save(memberCollectSpu);
        return R.ok();
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public R update(@RequestBody MemberCollectSpuEntity memberCollectSpu) {
        memberCollectSpuService.updateById(memberCollectSpu);
        return R.ok();
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public R delete(@RequestBody Long[] ids) {
        memberCollectSpuService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
