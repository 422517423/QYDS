package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.ErpPointRecord;
import net.dlyt.qyds.common.dto.MmbPointRecord;
import net.dlyt.qyds.common.dto.ext.MmbPointRecordExt;
import net.dlyt.qyds.common.form.ErpPointRecordForm;
import net.dlyt.qyds.common.form.MmbPointRecordForm;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by YiLian on 16/8/1.
 */
@Repository
public interface MmbPointRecordMapperExt {

    /**
     * 查询会员的积分履历
     *
     * @param form
     * @return
     */
    List<MmbPointRecordExt> queryRecordList(MmbPointRecordForm form);

    /**
     * 年度(自然年)累计积分查询
     *
     * @param form memberId
     *             pointTime:积分时间
     * @return
     */
    Integer selectPointOfYear(MmbPointRecordForm form);

    /**
     * 检索36个月内的可使用积分列表
     *
     * @param form memberId:会员ID
     *             pointTime:积分时间
     * @return
     */
    List<MmbPointRecordExt> selectSubPointList(MmbPointRecordForm form);

    /**
     * 检索第36个月需要清空的积分
     *
     * @param pointTime:积分时间
     * @return
     */
    List<MmbPointRecordExt> countSubPoint(Date pointTime);

    /**
     * 检索一个月天后将清除的积分
     *
     * @return
     */
    List<MmbPointRecordExt> selectNotifyPoint();

    /**
     * 清空第36个月的积分
     *
     * @param pointTime:积分时间
     * @return
     */
    int clearSubPoint(Date pointTime);

    /**
     * 取得指定会员的ERP积分记录
     *
     * @param memberId:会员ID
     * @return
     */
    List<MmbPointRecord> selectErpPointByMemberd(String memberId);

    /**
     * 通过ID更新可用积分
     *
     * @param record:积分记录
     * @return
     */
    int updateSurplusPointById(MmbPointRecord record);

    List<MmbPointRecord> selectPointRecordByScoreSource(String scoreSource);

    List<MmbPointRecordExt> selectSendFail();

    MmbPointRecordExt selectSendFailById(Integer recordNo);

    MmbPointRecordExt selectSendById(Integer recordNo);

    /**
     * 会员积分累计
     * 满足条件:
     * 1-消费积分及生日积分(积分不生效状态)
     * 2.1-购物订单为完成状态
     * 2.2-ERP数据为积分时间超过三十天
     *
     * @return
     */
    List<MmbPointRecord> queryRecordOfSuccess();

    int clearByOrderId(MmbPointRecord record);
    //根据条件取得分页信息
    List<MmbPointRecordExt> selectRecordByPage(MmbPointRecordForm form);
    //根据条件取得件数信息
    int getRecordCountByPage(MmbPointRecordForm form);
}
