package com.learn.mall.seckill.interceptor;

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
        String uri = request.getRequestURI();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        boolean match = antPathMatcher.match("/kill", uri);
        //如果是秒杀请求，需要进行拦截判断，设置登录信息，然后放行
        if (match) {
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
        //如果不是秒杀请求，全部放行
        return true;
    }
}
