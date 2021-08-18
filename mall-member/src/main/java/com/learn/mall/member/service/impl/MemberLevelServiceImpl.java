package com.learn.mall.member.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learn.common.utils.PageUtils;
import com.learn.common.utils.Query;

import com.learn.mall.member.dao.MemberLevelDao;
import com.learn.mall.member.entity.MemberLevelEntity;
import com.learn.mall.member.service.MemberLevelService;


@Service("memberLevelService")
@SuppressWarnings("all")
public class MemberLevelServiceImpl extends ServiceImpl<MemberLevelDao, MemberLevelEntity> implements MemberLevelService {

    @Autowired
    private MemberLevelDao memberLevelDao;

    /**
     * 基础分页查询
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberLevelEntity> page = this.page(
                new Query<MemberLevelEntity>().getPage(params),
                new QueryWrapper<MemberLevelEntity>()
        );
        return new PageUtils(page);
    }

    /**
     * 获取默认用户等级
     */
    @Override
    public MemberLevelEntity getDefaultLevelInfo() {
        MemberLevelEntity levelEntity = memberLevelDao.selectOne(new QueryWrapper<MemberLevelEntity>()
                .eq("default_status", 1));
        return levelEntity;
    }

}