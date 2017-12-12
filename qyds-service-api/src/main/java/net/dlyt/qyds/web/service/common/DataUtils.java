package net.dlyt.qyds.web.service.common;

import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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


    /**
     * 时间戳转化成年月日时分秒形式
     * @param dt
     * @return
     */
    public static String formatTimeStampToYMDHM(Date dt) {
        String rs = "";
        if (!StringUtils.isEmpty(dt)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            rs = sdf.format(dt);
        }
        return rs;
    }

    /**
     * 时间戳转化成年月日时分秒形式
     * @param dt
     * @return
     */
    public static String formatTimeStampToYMDHMS(Date dt) {
        String rs = "";
        if (!StringUtils.isEmpty(dt)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            rs = sdf.format(dt);
        }
        return rs;
    }

    /**
     * 指定日期到现在的天数
     * @param dt
     * @return
     */
    public static int daysToNow(Date dt) throws ParseException
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        long time1 = cal.getTimeInMillis();
        cal.setTime(new Date());
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/1000/3600/24;

        return Integer.parseInt(String.valueOf(between_days));
    }
}
