package net.dlyt.qyds.web.service.context;

import java.util.LinkedHashMap;
import java.util.Map;

@Deprecated
public final class PamsDataContext {

    /**
     * userId   = 用户ID  ProfileId
     * userName = 用户名称 Name
     * orgId  = 组织ID
     * ip       = IP地址
     * uri      = 请求URI
     * type     = 请求项目
     */
    private static final ThreadLocal<Map<String, Object>> local = new ThreadLocal<Map<String, Object>>();

    public static Map<String, Object> get(){
        return local.get();
    }

    public static Object get(String key) {
        if (local.get() == null) {
            return null;
        }
        return local.get().get(key);
    }

    public static void set(String key, Object value) {
        if (local.get() == null) {
            Map<String, Object> m = new LinkedHashMap<String, Object>();
            m.put(key, value);
            local.set(m);
        } else
            local.get().put(key, value);
    }

    public static Object getUserId() {
        if (local.get() == null) {
            return null;
        }
        return local.get().get("userId");
    }

    public static Object getLoginId() {
        if (local.get() == null) {
            return null;
        }
        return local.get().get("loginId");
    }

    public static Object getUserName() {
        if (local.get() == null) {
            return null;
        }
        return local.get().get("userName");
    }

    public static Object getSysUser() {
        if (local.get() == null) {
            return null;
        }
        return local.get().get("sysUser");
    }
}

