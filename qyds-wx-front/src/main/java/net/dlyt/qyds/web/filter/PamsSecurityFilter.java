package net.dlyt.qyds.web.filter;

import com.alibaba.fastjson.JSON;
import net.dlyt.qyds.common.dto.ext.MmbMasterExt;
import net.dlyt.qyds.web.common.EncryptUtil;
import net.dlyt.qyds.web.context.IPUtils;
import net.dlyt.qyds.web.context.PamsDataContext;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Deprecated
public class PamsSecurityFilter implements Filter {
    private static final Logger logger = Logger.getLogger(PamsSecurityFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        try {
            boolean isCookieFinded = false;
            HttpServletRequest request = (HttpServletRequest)req;
            HttpServletResponse response = (HttpServletResponse)resp;
            PamsDataContext.set("ip", IPUtils.getRemoteIp(request));

            String uri = request.getRequestURI();
            String contextPath = request.getContextPath();
            String servletPath = request.getServletPath();
            String basePath = request.getScheme() + "://"
                    + request.getServerName() + ":" + request.getServerPort()
                    + contextPath;

            if(!servletPath.matches(".*\\.json")) {
                chain.doFilter(req, resp);
                return;
            }

            if(servletPath.matches("/auth/.*\\.json")) {
                chain.doFilter(req, resp);
                return;
            }

            Cookie[] cookies = request.getCookies();
            if(cookies != null && cookies.length > 0) {
                for(int idx = 0; idx < cookies.length; ++idx) {
                    Cookie ck = cookies[idx];
                    if(ck != null && "qyds_user_token".equals(ck.getName())) {
                        String token = EncryptUtil.decrypt((String)ck.getValue());
                        MmbMasterExt sysUser = JSON.parseObject(token, MmbMasterExt.class);
                        PamsDataContext.set("userId", sysUser.getMemberId());
                        PamsDataContext.set("userName", sysUser.getMemberName());
                        PamsDataContext.set("loginId", sysUser.getOpenId());
                        PamsDataContext.set("sysUser", sysUser);
                        // 设置cookie信息
                        ck.setMaxAge(30 * 60 * 1000);
                        response.addCookie(ck);
                        isCookieFinded = true;
                        continue;
                    }

                    if(ck != null && "the_user_info".equals(ck.getName())) {
                        // 设置cookie信息
                        ck.setMaxAge(30 * 60 * 1000);
                        response.addCookie(ck);
                        isCookieFinded = true;
                        continue;
                    }
                }
            }

            if(isCookieFinded) {
                chain.doFilter(req, resp);
            }else{
                response.sendRedirect(basePath + "/auth/401.json");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void destroy() {

    }
}
