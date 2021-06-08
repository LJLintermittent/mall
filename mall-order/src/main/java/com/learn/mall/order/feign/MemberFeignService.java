package com.learn.mall.order.feign;

import com.learn.mall.order.vo.MemberAddressVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Description:
 * date: 2021/5/12 20:49
 * Package: com.learn.mall.order.feign
 *
 * @author 李佳乐
 * @version 1.0
 */
@FeignClient("mall-member")
public interface MemberFeignService {

    /**
     * 返回这个会员的所有收货地址
     */
    @GetMapping("/member/memberreceiveaddress/{memberId}/addresses")
    List<MemberAddressVo> getAddress(@PathVariable("memberId") Long memberId);

}
