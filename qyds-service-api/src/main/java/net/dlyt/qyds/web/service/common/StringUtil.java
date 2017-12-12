package net.dlyt.qyds.web.service.common;

import com.alibaba.druid.support.spring.stat.annotation.Stat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cjk on 16/7/28.
 */
public class StringUtil {
    public static boolean isEmpty(String str) {
        if (str == null || str.trim().length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 业务编码取得
     *
     * @return 业务编码
     * @author wenxc
     */
    public static String getBusinessCode() {

        String code = "";
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmssSSS");
        code += format.format(date);

        Double random = Math.random();
        Long lStr = Math.round(random * 1000000);

        return code + String.valueOf(lStr).substring(0, 3);
    }

    /**
     * 验证是否是手机号码
     *
     * @param telephone
     * @return
     */
    public static boolean isTelephone(String telephone) {
        if (isEmpty(telephone)) {
            return false;
        }

        //String mobile = "^1(3[0-9]|4[57]|5[0-35-9]|7[01678]|8[0-9])\\d{8}$";
        String mobile = "^1(3|4|5|7|8)\\d{9}$";
        Pattern p = Pattern.compile(mobile);
        Matcher m = p.matcher(telephone);

        return m.matches();
    }

    public static String getRandomNum(int length) {
        String RANDOM_STR = "0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(RANDOM_STR.charAt(new java.util.Random().nextInt(RANDOM_STR.length())));
        }
        return sb.toString();
    }
}
