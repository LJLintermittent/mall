package com.learn.mall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.common.utils.PageUtils;
import com.learn.mall.member.entity.MemberReceiveAddressEntity;

import java.util.List;
import java.util.Map;

/**
 * 会员收货地址
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 20:55:02
 */
@SuppressWarnings("all")
public interface MemberReceiveAddressService extends IService<MemberReceiveAddressEntity> {

    /**
     * 基础分页查询
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 返回这个会员的所有收货地址
     */
    List<MemberReceiveAddressEntity> getAddress(Long memberId);

}

