package com.learn.mall.order.web;

import com.learn.mall.order.exception.NoStockException;
import com.learn.mall.order.service.OrderService;
import com.learn.mall.order.vo.OrderConfirmVo;
import com.learn.mall.order.vo.OrderSubmitVo;
import com.learn.mall.order.vo.SubmitOrderResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;

/**
 * Description:
 * date: 2021/5/12 17:14
 * Package: com.learn.mall.order.web
 *
 * @author 李佳乐
 * @version 1.0
 */
@Api(tags = "提交订单，订单确认页面信息展示模块")
@Controller
@SuppressWarnings("all")
public class OrderWebController {

    @Autowired
    OrderService orderService;

    /**
     * 订单确认页面信息展示接口
     * confirm.html
     */
    @ApiOperation(value = "订单确认页面信息展示接口")
    @GetMapping("/toTrade")
    public String toTrade(Model model, HttpServletRequest request) throws ExecutionException, InterruptedException {
        OrderConfirmVo confirmVo = orderService.confirmOrder();
        model.addAttribute("orderConfirmData", confirmVo);
        //展示订单确认的数据
        return "confirm";
    }

    /**
     * 提交订单接口（真正创建订单/下单功能）
     * 下单：创建订单，验证防重令牌，验证价格，锁库存
     */
    @ApiOperation(value = "提交订单接口")
    @PostMapping("/submitOrder")
    public String submitOrder(OrderSubmitVo vo, Model model, RedirectAttributes redirectAttributes) {
        try {
            SubmitOrderResponseVo responseVo = orderService.submitOrder(vo);
            if (responseVo.getCode() == 0) {
                // 下单成功来到支付选择页面
                model.addAttribute("submitOrderResp", responseVo);
                return "pay";
            } else {
                // 下单失败重新回到订单确认页面 再次确认订单信息
                String msg = "下单失败,原因是:";
                StringBuffer stringBuffer = new StringBuffer(msg);
                switch (responseVo.getCode()) {
                    case 1:
                        stringBuffer.append("订单信息过期,请刷新后再次提交");
                        break;
                    case 2:
                        stringBuffer.append("订单价格发生变化,请确认后再次提交");
                        break;
                    case 3:
                        stringBuffer.append("库存锁定失败,商品库存不足");
                        break;
                }
                redirectAttributes.addFlashAttribute("message", stringBuffer);
                return "redirect:http://order.mall.com/toTrade";
            }
        } catch (Exception e) {
            if (e instanceof NoStockException) {
                String message = e.getMessage();
                StringBuffer stringBuffer = new StringBuffer("下单失败，异常原因是：");
                stringBuffer.append(message);
                redirectAttributes.addFlashAttribute("message", stringBuffer);
            }
            return "redirect:http://order.mall.com/toTrade";
        }
    }
}
