package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.CouponMember;
import net.dlyt.qyds.common.dto.MmbMaster;
import net.dlyt.qyds.common.dto.ext.CouponMemberExt;
import net.dlyt.qyds.common.dto.ext.MmbPointRecordExt;
import net.dlyt.qyds.common.form.MmbPointRecordForm;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by cjk on 16/8/17.
 */
@Repository
public interface CouponMemberMapperExt {

    int checkCouponGeted(CouponMemberExt form);

    List<String> getUserForDistribute(String couponId);

    int updateStatusUsedById(CouponMember record);

    List<CouponMember> selectSendFail();

    CouponMember selectSendFailById(String id);

    List<CouponMember> selectSendUsedFail();

    CouponMember selectSendUsedFailById(String id);
    //根据条件取得分页信息
    List<CouponMemberExt> selectRecordByPage(CouponMemberExt form);
    //根据条件取得件数信息
    int getRecordCountByPage(CouponMemberExt form);
}
