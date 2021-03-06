package com.learn.mall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.common.utils.PageUtils;
import com.learn.mall.member.entity.MemberLevelEntity;

import java.util.Map;

/**
 * 会员等级
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 20:55:02
 */
@SuppressWarnings("all")
public interface MemberLevelService extends IService<MemberLevelEntity> {

    /**
     * 基础分页查询
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取默认用户等级
     */
    MemberLevelEntity getDefaultLevelInfo();

}

