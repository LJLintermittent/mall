package com.learn.mall.order.listener;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.learn.mall.order.config.AlipayTemplate;
import com.learn.mall.order.service.OrderService;
import com.learn.mall.order.vo.PayAsyncVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Description:
 * date: 2021/5/21 0:04
 * Package: com.learn.mall.order.listener
 *
 * @author 李佳乐
 * @version 1.0
 */
@SuppressWarnings("all")
@RestController
public class OrderPayedListener {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AlipayTemplate alipayTemplate;

    /**
     * 支付宝成功异步通知
     * 收到支付宝给本系统的异步通知，告诉订单系统 订单支付成功，返回success
     * 返回了success后支付宝就不会再通知了
     */
    @PostMapping("/payed/notify")
    public String handleAliPay(PayAsyncVo payAsyncVo, HttpServletRequest request) throws AlipayApiException {
        /**
         *支付宝返回的数据
         *         Map<String, String[]> map = request.getParameterMap();
         *         for (String key : map.keySet()) {
         *             String value = request.getParameter(key);
         *             System.out.println("参数名：" + key + "--参数值：" + value);
         *         }
         */
        //验签，验证是否是支付宝发过来的请求，防止是postman恶意模拟支付宝的成功回调请求
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            /**
             * 乱码解决，这段代码在出现乱码时使用
             * valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
             */
            params.put(name, valueStr);
        }
        boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayTemplate.getAlipay_public_key()
                , alipayTemplate.getCharset(), alipayTemplate.getSign_type()); //调用SDK验证签名
        if (signVerified) {
            System.out.println("签名验签成功");
            String result = orderService.handlePayResult(payAsyncVo);
            return result;
        } else {
            System.out.println("签名验签失败");
            return "error";
        }
    }
}
