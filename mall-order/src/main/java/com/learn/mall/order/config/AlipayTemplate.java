package com.learn.mall.order.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.learn.mall.order.vo.PayVo;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "alipay")
@Component
@Data
public class AlipayTemplate {

    //在支付宝创建的应用的id
    private String app_id = "2021000117660636";
    // 商户私钥，您的PKCS8格式RSA2私钥
    private String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCI9q3Q8nmXkJz839jqUcU7VSZkBj+aw//NdB7DRUjTpF0uazb4YGJhtdsOPuuOqV8nRhf+DRTq8mrZnDDai8DdUDFNhiqEU6gleYHljxvS+VgQ5rz5pu1Gyz6i4z60yNoRMAqlKm5W/FJE1QxBl9b1W3IZOA37vixU4VIOlm9JRtotD4thqGIP5Icp9BkHSFTUDXFizjRoLn8d/Pj19q8SDVH7WKO+c13/ZwThOaaCFkS8m2J63lSVkaKeGPwoCD5nGPKABiqMJA/BQK6m6/xBn9tMdDMUL9p7tSjqAst1mFOdtt+0wXS0FWqakjHbmSZYkkGEc1t+0JigMdKccGqrAgMBAAECggEBAIOnw5tHiXQU8aLuNd+/7HxFNJsPmpJVd43x4iRIdGHk7CFTtEp6s/dCzSfbeajvzGS/Lw7pWkMh9W3qgteBYUob7NidcD80/zvOmfulXdSmFG5iriK4v/q+Ih6HnF5Bd8TvzjvIFrqM3zQFKzveDiZZ2cq+JErqGkkZMjyNhLoE9lS2jlSnSCmeDZxYC1V/u9PbScVy76OGcqMHjQL/IVJZ3PGzAiXuU+PdSkG1ONfoUGuJT1qlIgUy6xBK5aoxl09/jNy0U8TaXDmGE50JJqnn8MLVAWLSfDNXMPIjTUU9vX2R7RXgIU3c3NxMexgjLuYUuXFypxcyXrz/fBBXhfkCgYEAwDjv8S9fDI+23T7c5OhXZWnZPSIU8fZ5HIJubkV9xf3NsDCSaQ+s1Ahwsk4VkKtl2lQaxL+zJ2TNSlf5M+nyezC3FAtqaitQQ+DvL17IpWZuZOjmllrZ9bJOUrtYnY0dfkiVyJ2lhTyb38kG8iIb8xd14c197ylmHI6SZrvPOrUCgYEAtmglbOfi0fSeS8X3WYRKCMUVbmvmyenToGC2wHMeQrKZ/KIsmzUq9iGPowT9xUieM21Z+qOsrOP1odePPsWQLMpqMJ9HyBgtHaqgsZ04azkikgXaiXFGU1w9y9vzlYadMWC4pIcUO+eXV6bqHCkmXw06K7cOSm5SBoyz5vFci98CgYEArHBnj84XToV+XmwObaug+3FBWP5CG+4oJ9M6jeH5Oqy84Nox/d36BRytPMefwPCBpqLcgLQYW7aqE49oF5I/3ROOqYS0KhpBHyOibrRw+zAwyIXGlgtajHcwnDdE/9GgK7Gh7R6/Zg9BJjiI6p5+xEy/i5l06CATYnrkCxVgiiECgYB6xn7qXup9B3nE5usmhbSPUpiqSlZ9cbXQLUjVIKctnamoMkBz98WDBfP4dlOZhtd1JhV6btC7TnT3CnCQf8obW+M99vgJ5rv3S/H9myVuhnaOcwI1oNkFIwE1RkceTg1gPwoJNCKab7eed+r8KXZ6ZBzTb9NepgLwBaTRvYPdYQKBgDL4NkIVWHLUzwgbpnv8LDhSt9/+q/p7nrU8qD98XnfKGZ61u+lvPW1NbVTya/JMM6fCF9ekQ+D4CeDSWKI0x+BpJ6fnvoEkNh0Zv+BPIihi/FAg4Qi70Q9uHfFVKKdWMW7/Dt11AJ51HiSpLYqEzLDND5ZER8bU6BueK5lHtm1s";
    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    private String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoZA9t9D+UfLPYHzxhO7ozvHCPpT94vI4Q7LwnAr1kfuqL7oHSRml2oxBe7AtDk3qUf0SiVxylfho0HgpC7aDSXysTQ6TaV9HEt+68sRcb7vE9inzbh47ZRiTeJEI8spzk9aRLqsbBgeNumdBfM666WggQqnCtSYhADmGvUSLOGcL9pDzZH3ufd43KVsCIvAQ0FRawF2P+z/og3zVoFQMji64gOXGiGk8d3qqsDhEHIZvBUol0bCFztf9Oek59Kqi6fFVBHHry+5aLkGLhOe/hp9AfB3kPRtTWWz70kHfbyqEO43UF8vGYMyGdaNLrxNBqR7mGrqzQN5NFkNQpI2KqwIDAQAB";
    // 服务器[异步通知]页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    // 支付宝会悄悄的给我们发送一个请求，告诉我们支付成功的信息
    private String notify_url = "http://fner440b4r.bjhttp.cn/payed/notify";
    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    //同步通知，支付成功，一般跳转到成功页
    private String return_url = "http://order.mall.com/list.html";
    // 签名方式
    private String sign_type = "RSA2";
    // 字符编码格式
    private String charset = "utf-8";
    // 支付宝网关； https://openapi.alipaydev.com/gateway.do
    private String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";
    //支付宝自动收单
    private String timeout = "1m";

    public String pay(PayVo vo) throws AlipayApiException {

        //AlipayClient alipayClient = new DefaultAlipayClient(AlipayTemplate.gatewayUrl, AlipayTemplate.app_id, AlipayTemplate.merchant_private_key, "json", AlipayTemplate.charset, AlipayTemplate.alipay_public_key, AlipayTemplate.sign_type);
        //1、根据支付宝的配置生成一个支付客户端
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl,
                app_id, merchant_private_key, "json",
                charset, alipay_public_key, sign_type);

        //2、创建一个支付请求 //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(return_url);
        alipayRequest.setNotifyUrl(notify_url);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = vo.getOut_trade_no();
        //付款金额，必填
        String total_amount = vo.getTotal_amount();
        //订单名称，必填
        String subject = vo.getSubject();
        //商品描述，可空
        String body = vo.getBody();

        alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
                + "\"total_amount\":\"" + total_amount + "\","
                + "\"subject\":\"" + subject + "\","
                + "\"body\":\"" + body + "\","
                + "\"timeout_express\":\""+timeout+"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        String result = alipayClient.pageExecute(alipayRequest).getBody();

        //会收到支付宝的响应，响应的是一个页面，只要浏览器显示这个页面，就会自动来到支付宝的收银台页面
        System.out.println("支付宝的响应：" + result);

        return result;

    }
}
