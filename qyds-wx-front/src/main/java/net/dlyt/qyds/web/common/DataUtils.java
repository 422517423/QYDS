package net.dlyt.qyds.web.common;

import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by C_Nagai on 2016/7/6.
 */
public class DataUtils {

    /**
     * 时间戳转化成年月日形式
     * @param dt
     * @return
     */
    public static String formatTimeStampToYMD(Date dt) {
        String rs = "";
        if (!StringUtils.isEmpty(dt)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            rs = sdf.format(dt);
        }
        return rs;
    }
}
