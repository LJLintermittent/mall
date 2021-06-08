package com.learn.mall.seckill.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Description:
 * date: 2021/5/23 21:28
 * Package: com.learn.mall.seckill.vo
 *
 * @author 李佳乐
 * @version 1.0
 */
@Data
public class SecKillSessionsWithSkus {

    private Long id;
    /**
     * 场次名称
     */
    private String name;
    /**
     * 每日开始时间
     */
    private Date startTime;
    /**
     * 每日结束时间
     */
    private Date endTime;
    /**
     * 启用状态
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;

    private List<SecKillSkuVo> relationEntities;

}
