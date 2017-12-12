package net.dlyt.qyds.web.common;

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
}
