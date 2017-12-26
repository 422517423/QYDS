package net.dlyt.qyds.web.quartz;

/**
 * Created by zlh on 16/11/4.
 */
public class TaskJobConstants {
    //批处理执行开关
    //ALL
    public static final  boolean RUNJOB_SWITCH_ALL = true;

    //DistributeBirthdayCouponTaskJob
    public static final  boolean RUNJOB_SWITCH_DISTRIBUTE_BIRTHDAY_COUPON = true;
    //MmbAddPointTaskJob
    public static final  boolean RUNJOB_SWITCH_MMB_ADD_POINT = true;
    //MmbBonusPointCleanTaskJob
    public static final  boolean RUNJOB_SWITCH_MMB_BONUS_POINT_CLEAN = true;
    //MmbMasterDownGradeTaskJob
    public static final  boolean RUNJOB_SWITCH_MMB_MASTER_DOWN_GRADE = true;
    //MmbMasterUpgradeTaskJob
    public static final  boolean RUNJOB_SWITCH_MMB_MASTER_UPGRADE = false;
    //ThumbnailTaskJob
    public static final  boolean RUNJOB_SWITCH_THUMBNAIL = true;
    //UnpayOrderTaskJob
    public static final  boolean RUNJOB_SWITCH_UNPAY_ORDER = true;

    //ErpSendTaskJob
    public static final  boolean RUNJOB_SWITCH_ERP_SEND = true;
    //sendFailGoodsType
    public static final  boolean RUNJOB_SWITCH_ERP_SEND_GOODS = true;
    //sendFailMember
    public static final  boolean RUNJOB_SWITCH_ERP_SEND_MEMBER = true;
    //sendFailPointRecord
    public static final  boolean RUNJOB_SWITCH_ERP_SEND_POINT = true;
    //sendFailOrder
    public static final  boolean RUNJOB_SWITCH_ERP_SEND_ORDER = true;
    //sendFailReturnOrder
    public static final  boolean RUNJOB_SWITCH_ERP_SEND_RETURN = true;
    //sendFailCoupon
    public static final  boolean RUNJOB_SWITCH_ERP_SEND_COUPON = true;
    //sendCouponSku
    public static final  boolean RUNJOB_SWITCH_ERP_SEND_COUPON_SKU = false;
    //sendFailCouponMember
    public static final  boolean RUNJOB_SWITCH_ERP_SEND_COUPON_MEMBER = true;
    //sendFailCouponUsed
    public static final  boolean RUNJOB_SWITCH_ERP_SEND_COUPON_USED = true;
    //sendFailBankUpdate
    public static final  boolean RUNJOB_SWITCH_ERP_SEND_BANK_UPDATE = true;
    //sendFailMemberUsed
    public static final  boolean RUNJOB_SWITCH_ERP_SEND_MEMBER_USED = true;
    // TODO: 2017/12/25
    //sendExpress
    public static final  boolean RUNJOB_SWITCH_ERP_SEND_EXPRESS = true;
}
