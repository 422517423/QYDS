package net.dlyt.qyds.web.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by C_Nagai on 2016/7/6.
 */
public class StringUtil {
    //ERP加密
    static final String ERP_TOKEN_KEY = "bb5683c5-7916-4e79-9dd6-d2292530871a";

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
    public static String getErpData(String data,String key) throws Exception {
        try {
            data = EncryptUtil.decrypt(data);
            String key1 = getErpKey(data);
            if (!key1.equals(key)) {
                throw new Exception("密钥错误");
            }
            return data;
        } catch (Exception e) {
            throw e;
        }
    }
    public static String getErpKey(String data) throws Exception{
        return EncryptUtil.encrypt(EncryptUtil.encodeMD5(ERP_TOKEN_KEY + data.toLowerCase()).toString());
    }
    public static void main(String aaa[]){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c  = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MILLISECOND,0);
        System.out.println(sdf.format(c.getTime()));
    }
}

