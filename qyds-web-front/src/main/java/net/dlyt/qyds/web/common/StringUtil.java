package net.dlyt.qyds.web.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by C_Nagai on 2016/7/6.
 */
public class StringUtil {

    /**
     * 生成数字随机数
     * @param count 随机数长度
     * @return
     */
    public static String getRadionNumber(int count) {
        Random rd = new Random();
        String rsb = "";
        for (int i = 0; i < count; i ++) {
            rsb += rd.nextInt(10);
        }
        return rsb;
    }

    /**
     * 当前时间编码取得
     *
     * @return 当前时间编码
     * @author zlh
     */
    public static String getNowCode() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmssSSS");
        return format.format(date);
    }
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }
}
