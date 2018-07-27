package net.dlyt.qyds.web.service.common;

/**
 * Created by cjk on 16/7/28.
 */
public class
ComCode {
    public static class ApproveStatus {
        // "10";"审批中";"20";"审批通过";"30";"审批驳回";"40";"未申请"
        public static final String APPROVE_STATUS_APPROVING = "10";
        public static final String APPROVE_STATUS_APPROVED = "20";
        public static final String APPROVE_STATUS_REJECT = "30";
        public static final String APPROVE_STATUS_NOT_APPLY = "40";
    }

    public static class ActivityType {
        // "10";"单品特价";'11'：'秒杀'
        // "20";"单品折扣";"21";"一二件折";"40";"订单满送优惠劵";"41";"订单满减";"42";"订单满送货品"
        // "43";"订单满送积分";"44";"订单满折"
        public static final String SPECIAL_PRICE = "10";
        public static final String SECOND_KILL = "11";
        public static final String DISCOUNT = "20";
        public static final String DISCOUNT_TWO = "21";
        public static final String INTEGRAL_EXCHANGE = "30";
        public static final String FULL_SEND = "40";
        public static final String FULL_CUT = "41";
        public static final String FULL_SEND_GOODS = "42";
        public static final String FULL_SEND_POINT = "43";
        public static final String FULL_DISCOUNT = "44";
    }

    /**
     * 优惠商品对象类型
     */
    public static class ActivityGoodsType {
        // "10";"全部"
        // "20";"按分类"
        // "30";"按品牌"
        // "40";"商品"
        // "50";"SKU"
        public static final String ALL = "10";
        public static final String TYPE = "20";
        public static final String BRAND = "30";
        public static final String GOODS = "40";
        public static final String SKU = "50";
    }

    public static class ActivityMemberType {
        //"10";"全部"
        //"20";"会员组"
        public static final String ALL = "10";
        public static final String MEMBER_GROUP = "20";
        public static final String MEMBER_LEVEL = "30";
    }

    public class CouponStatus {
        //"10";"未使用"
        //"20";"已使用"
        //"30";"已过期"
        public static final String UNUSE = "10";
        public static final String USED = "20";
    }

    public class CouponType {
        //"10";"代金券"
        //"20";"生日劵"
        //"30";"注册劵"
        //"40":"红包"
        public static final String NORMAL = "10";
        public static final String BIRTHDAY_SEND = "20";
        public static final String REGIST_SEND = "30";
        public static final String RED_PACKET = "40";
    }

    public class CouponDistributeType {
        //"10";"平台推送"
        //"20";"满购赠送"
        //"30";"用户手动领取"
        //"40";"系统自动发放"
        //"50";"积分兑换"
        public static final String PLATFORM_SEND = "10";
        public static final String ACTIVITY_SEND = "20";
        public static final String MANUAL_COLLECTION = "30";
        public static final String SYSTEM_SEND = "40";
        public static final String POINT_EXCHANGE = "50";
        public static final String BUY = "60";
    }

    public class CouponOriginPriceType {
        //"10";"未使用"
        //"20";"已使用"
        //"30";"已过期"
        public static final String ALL = "0";
        public static final String MUST_ORIGIN_PRICE = "1";
        public static final String MUST_DISCOUNT_PRICE = "2";
    }

    public class CouponStyle {
        //"0";"抵值"
        //"1";"折扣"
        public static final String WORTH = "0";
        public static final String DISCOUNT = "1";
    }

    public class PrizeDrawOppoType {
        // "0" 积分兑换
        // "1" 注册赠送
        // "2" 订单满赠
        public static final String EXCHANGE = "0";
        public static final String REGISTER = "1";
        public static final String ORDER = "2";
    }

}
