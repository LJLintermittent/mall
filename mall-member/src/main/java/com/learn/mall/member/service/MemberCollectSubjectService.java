package com.learn.mall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.common.utils.PageUtils;
import com.learn.mall.member.entity.MemberCollectSubjectEntity;

import java.util.Map;

/**
 * 会员收藏的专题活动
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 20:55:02
 */
@SuppressWarnings("all")
public interface MemberCollectSubjectService extends IService<MemberCollectSubjectEntity> {

    /**
     * 基础分页查询
     */
    PageUtils queryPage(Map<String, Object> params);
}

