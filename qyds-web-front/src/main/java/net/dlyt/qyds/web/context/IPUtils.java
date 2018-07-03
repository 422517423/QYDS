package net.dlyt.qyds.web.context;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by dkzhang on 16/7/11.
 */
public class IPUtils {
    public IPUtils() {
    }

    public static String getRemoteIp(HttpServletRequest request) {
        String remoteIp = null;
        remoteIp = request.getHeader("X-Forwarded-For");
        if(remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getHeader("X-Real-IP");
        }

        if(remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getHeader("Proxy-Client-IP");
        }

        if(remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getHeader("WL-Proxy-Client-IP");
        }

        if(remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getHeader("HTTP_CLIENT_IP");
        }

        if(remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if(remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getRemoteAddr();
        }

        if(remoteIp == null || remoteIp.isEmpty() || "unknown".equalsIgnoreCase(remoteIp)) {
            remoteIp = request.getRemoteHost();
        }

        if(remoteIp != null && remoteIp.indexOf(",") != -1) {
            remoteIp = remoteIp.substring(0, remoteIp.indexOf(",")).trim();
        }

        return remoteIp;
    }
}
