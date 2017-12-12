package net.dlyt.qyds.web.intercept;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PAMSIntercept implements HandlerInterceptor {

    private static final String PROJECT_PREFIX = "/qyds-web-";
    private Logger logger = Logger.getLogger(PAMSIntercept.class);

    public PAMSIntercept() {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object arg2, Exception
            arg3) throws Exception {

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object arg2, ModelAndView arg3)
            throws Exception {

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
        //登陆验证
        //监控


        return true;
    }

}
