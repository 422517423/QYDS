package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.form.MmbPointRecordForm;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by YiLian on 16/7/28.
 */
public interface MmbPointRecordService {

    /**
     * 取得会员积分履历一览(有分页)
     *
     * @param form memberId:会员ID
     *             lastUpdateTime:最后一条更新时间
     * @return
     */
    JSONObject getRecordList(MmbPointRecordForm form);

    /**
     * 会员积分记录添加(积分增加)
     *
     * @param form memberId:会员ID
     *             rule_id:积分规则ID(累计)
     *             cash:实际现金数额
     *             worth_id:积分规则ID(消费)
     *             worthCash:积分抵值金额
     *             worthPoint:抵值积分
     *             exchangeId:积分规则ID(换购)
     *             exchangePoint:换购积分
     *             score_source:积分来源(订单号)
     *             update_user_id: 如果是手工添加需要传值,默认为会员自己
     * @return
     */
    JSONObject add(MmbPointRecordForm form);

    /**
     * 清除36个月前的消费积分
     * 每月末执行一次
     *
     * @return
     */
    JSONObject clearSurplusPoint();

    /**
     * 通知一个月后清除消费的积分
     * 每月末执行一次
     *
     * @return
     */
    JSONObject notifyClearPoint();

    /**
     *
     * 处理会员积分累计
     * [每日执行一次]
     * 条件:
     * 1-消费积分及生日积分(积分不生效状态)
     * 2.1-购物订单为完成状态
     * 2.2-ERP数据为积分时间超过三十天
     *
     * @return
     */
    JSONObject addPoint();

    JSONObject selectRecordByPage(MmbPointRecordForm form);
}


