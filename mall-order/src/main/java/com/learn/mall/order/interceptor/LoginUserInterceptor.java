package com.learn.mall.order.interceptor;

import com.learn.common.constant.AuthConstant;
import com.learn.common.vo.MemberRespVo;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Description:
 * date: 2021/5/12 17:21
 * Package: com.learn.mall.order.interceptor
 *
 * @author 李佳乐
 * @version 1.0
 */
@SuppressWarnings("all")
@Component
public class LoginUserInterceptor implements HandlerInterceptor {

    public static ThreadLocal<MemberRespVo> threadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /**
         * 库存服务远程调用这个订单服务的接口，会出现登录拦截问题
         */
        String uri = request.getRequestURI();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        boolean match1 = antPathMatcher.match("/order/order/status/**", uri);
        boolean match2 = antPathMatcher.match("/payed/notify", uri);
        if (match1 || match2) {
            return true;
        }
        MemberRespVo memberRespVo = (MemberRespVo) request.getSession().getAttribute(AuthConstant.LOGIN_USER);
        if (memberRespVo != null) {
            threadLocal.set(memberRespVo);
            return true;
        } else {
            //没登录就去登录
            request.getSession().setAttribute("msg", "请先进行登录");
            response.sendRedirect("http://auth.mall.com/auth/login.html");
            return false;
        }
    }
}
