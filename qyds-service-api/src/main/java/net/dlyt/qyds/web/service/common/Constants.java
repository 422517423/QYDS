package net.dlyt.qyds.web.service.common;

/**
 * Created by panda on 16/6/26.
 */
public class Constants {
    //成功
    public static final String NORMAL = "00";
    //失败
    public static final String FAIL = "99";

    // 用户不存在
    public static final String FAIL_NO_DATA = "95";

    // 库存不足
    public static final String NO_STORE = "90";

    //数据错误
    public static final String ERROR_DATA = "10";
    //参数错误
    public static final String ERROR_PARAM = "20";
    //没权限
    public static final String NO_POWER = "30";
    //未删除
    public static final String DELETED_NO = "0";
    //删除
    public static final String DELETED_YES = "1";

    //成功
    public static final String SUCCESS = "200";
    //成功
    public static final String SUCCESS_MSG = "success";

    // 组织编码
    public static final String ORGID = "00000000";

    //新建操作标识符
    public static final String INSERT = "insert";
    //编辑操作标识符
    public static final String UPDATE = "update";

    public static final String FAIL_MESSAGE = "system error!";

    // 10分钟有效
    public static final long VALIDED_MILLISECONDS = 10 * 60 * 1000;

    /**
     * 商品区分
     */
    // ERP单品
    public static final String GDS_ERP = "10";
    // 商品
    public static final String GDS_GOODS = "20";
    // 套装
    public static final String GDS_SUIT = "30";


    /**
     * cms形式
     */
    // 商品
    public static final String CMS_MASTER_GDS = "41";
    // 商品分类
    public static final String CMS_MASTER_ADS = "42";
    // 活动
    public static final String CMS_MASTER_ACT = "43";

    /**
     * 商品单选多选
     */
    // 单选
    public static final String GOODS_RADIO = "0";
    // 多选
    public static final String GOODS_MULTIPLE  = "1";

    // 获取售后服务code
    public static final String GOODS_SERVE  = "goods_serve";

    // 生日优惠券发放时间间隔(天)
    public static final Integer DISTRIBUTE_BIRTHDAY_COUPON_INTERVAL  = 5;

    // 库存不足报警下限值
    public static final Integer INVENTORY_WARNING_COUNT  = 5;

    // 远程调用URL 微信和PC
    public static final String WX_SOAP_URL  = "https://www.dealuna.com/qyds-wx-front/catcheRemove/";
    public static final String PC_SOAP_URL  = "https://www.dealuna.com/qyds-web-pc/catcheRemove/";
}
