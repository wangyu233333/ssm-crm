package com.bjpowernode.crm.settings.interceptor;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.settings.domain.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {
    //  请求到达目标之前执行
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        //  登录验证，如果没有登录成功跳转到登录页面
        HttpSession session = httpServletRequest.getSession();
        User user = (User) session.getAttribute(Contants.SESSION_USER);

        if (user == null) {
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath());   //  重定向时，url必须家项目的名字
            return false;
        }
        return true;
    }

    //  请求执行完目标资源之后执行
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    //  响应回去之后最后执行
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
