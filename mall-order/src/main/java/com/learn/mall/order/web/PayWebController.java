package com.learn.mall.order.web;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.learn.common.utils.PageUtils;
import com.learn.mall.order.config.AlipayTemplate;
import com.learn.mall.order.service.OrderService;
import com.learn.mall.order.vo.PayVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


/**
 * Description:
 * date: 2021/5/20 15:44
 * Package: com.learn.mall.order.web
 *
 * @author 李佳乐
 * @version 1.0
 */
@Controller
public class PayWebController {

    @Autowired
    private AlipayTemplate alipayTemplate;

    @Autowired
    private OrderService orderService;

    /**
     * 点击支付宝支付后跳转到支付宝支付页面 提交支付页面需要的数据
     */
    @ResponseBody
    @GetMapping(value = "/payOrder", produces = "text/html")
    public String payOrder(@RequestParam("orderSn") String orderSn) throws AlipayApiException {
        PayVo payVo = orderService.getOrderPay(orderSn);
        String result = alipayTemplate.pay(payVo);
        System.out.println(result);
        return result;
    }

    /**
     * 支付完成以后跳转到当前用户的所有订单列表页
     */
    @GetMapping("/list.html")
    public String listWithItem(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum
            , Model model, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("page", pageNum.toString());
        PageUtils page = orderService.queryPageWithItem(map);
        System.out.println(JSON.toJSONString(page));
        model.addAttribute("orders", page);
        return "list";
    }

}
