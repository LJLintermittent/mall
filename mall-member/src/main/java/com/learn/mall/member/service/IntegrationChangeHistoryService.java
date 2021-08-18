package com.learn.mall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.common.utils.PageUtils;
import com.learn.mall.member.entity.IntegrationChangeHistoryEntity;

import java.util.Map;

/**
 * 积分变化历史记录
 *
 * @author LJL
 * @email 18066550996@163.com
 * @date 2021-04-09 20:55:02
 */
@SuppressWarnings("all")
public interface IntegrationChangeHistoryService extends IService<IntegrationChangeHistoryEntity> {

    /**
     * 基础分页查询
     */
    PageUtils queryPage(Map<String, Object> params);

}

