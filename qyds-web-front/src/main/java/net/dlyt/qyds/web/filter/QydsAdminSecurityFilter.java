package net.dlyt.qyds.web.filter;

import com.alibaba.fastjson.JSON;
import net.dlyt.qyds.common.dto.SysUser;
import net.dlyt.qyds.web.common.EncryptUtil;
import net.dlyt.qyds.web.context.IPUtils;
import net.dlyt.qyds.web.context.PamsDataContext;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class QydsAdminSecurityFilter implements Filter {
    private static final Logger logger = Logger.getLogger(QydsAdminSecurityFilter.class);

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

            // TODO: 2017/12/15 圆通调用该url 返回运单号接口
            if(servletPath.equals("/resultFromYTO.json")) {
                chain.doFilter(req, resp);
                return;
            }

            // TODO: 2017/12/15 取消圆通订单
            if(servletPath.equals("/cancelYto.json")) {
                chain.doFilter(req, resp);
                return;
            }

            // TODO: 2018/1/4  批量计算累计消费金额到会员表中
            if(servletPath.equals("/testPointAll/test.json")) {
                chain.doFilter(req, resp);
                return;
            }

            // TODO: 2018/1/29 发送退货未成功的订单
            if(servletPath.equals("/erp_send/sendReturnOrderByOrderId.json")) {
                chain.doFilter(req, resp);
                return;
            }

            // TODO: 2018/1/29 发送指定新订单
            if(servletPath.equals("/erp_send/sendOrderById.json")) {
                chain.doFilter(req, resp);
                return;
            }

            // TODO: 2018/1/29 发送指定子订单
            if(servletPath.equals("/erp_send/sendReturnOrderBySubOrderId.json")) {
                chain.doFilter(req, resp);
                return;
            }

            // TODO: 2018/1/29 发送指定新订单
            if(servletPath.equals("/erp_send/sendGoodsTypeById.json")) {
                chain.doFilter(req, resp);
                return;
            }

            // TODO: 2018/1/29 发送指定新订单
            if(servletPath.equals("/erp_send/sendMemberById.json")) {
                chain.doFilter(req, resp);
                return;
            }

            // TODO: 2018/1/29 发送指定新订单
            if(servletPath.equals("/erp_send/sendCouponById.json")) {
                chain.doFilter(req, resp);
                return;
            }

            // TODO: 2018/1/29 发送指定新订单
            if(servletPath.equals("/erp_send/sendCouponMemberById.json")) {
                chain.doFilter(req, resp);
                return;
            }

            // TODO: 2018/1/29 发送指定新订单
            if(servletPath.equals("/erp_send/sendCouponUsedById.json")) {
                chain.doFilter(req, resp);
                return;
            }

            // TODO: 2018/1/29 发送指定新订单
            if(servletPath.equals("/erp_send/sendPointRecordById.json")) {
                chain.doFilter(req, resp);
                return;
            }

            // TODO: 2018/1/29 发送指定新订单
            if(servletPath.equals("/mmb_saler/getQRcode.json")) {
                chain.doFilter(req, resp);
                return;
            }


            Cookie[] cookies = request.getCookies();
            if(cookies != null && cookies.length > 0) {
                for(int idx = 0; idx < cookies.length; ++idx) {
                    Cookie ck = cookies[idx];
                    if(ck != null && "qyds_admin_token".equals(ck.getName())) {
                        String token = EncryptUtil.decrypt((String)ck.getValue());
                        SysUser sysUser = JSON.parseObject(token, SysUser.class);
                        PamsDataContext.set("userId", sysUser.getUserId());
                        PamsDataContext.set("userName", sysUser.getUserName());
                        PamsDataContext.set("loginId", sysUser.getLoginId());
                        PamsDataContext.set("orgId",sysUser.getOrgId());
                        PamsDataContext.set("sysUser", sysUser);
                        // 设置cookie信息
                        ck.setMaxAge(30 * 60);
                        response.addCookie(ck);
                        isCookieFinded = true;
                        continue;
                    }

                    if(ck != null && "the_user_info".equals(ck.getName())) {
                        // 设置cookie信息
                        ck.setMaxAge(30 * 60);
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
