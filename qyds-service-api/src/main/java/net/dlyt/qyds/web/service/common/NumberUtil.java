package net.dlyt.qyds.web.service.common;

import java.math.BigDecimal;

/**
 * Created by YiLian on 16/9/26.
 */
public class NumberUtil {

    /**
     * 四舍五入
     *
     * @param bd
     * @return
     */
    public static Integer formatInteger(BigDecimal bd) {
        return bd.add(new BigDecimal(0.5)).intValue();
    }

    /**
     * 四舍五入
     *
     * @param db
     * @return
     */
    public static Integer formatInteger(double db) {
        return ((Double) (db + 0.5)).intValue();
    }

}
