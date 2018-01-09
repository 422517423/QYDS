package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.CouponMaster;
import net.dlyt.qyds.common.dto.GdsMaster;
import net.dlyt.qyds.common.dto.MmbMaster;
import net.dlyt.qyds.common.dto.ext.MmbMasterExt;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by C_Nagai on 2016/7/27.
 */
@Repository
public interface MmbMasterMapperExt {
    // 根据memberid获取会员折扣
    BigDecimal selectMemberDiscount(String memberId);

    //根据memberPhone获取会员折扣
    BigDecimal selectMemberDiscountByPhone(String memberPhone);

    // 获取会员列表
    List<MmbMasterExt> selectAll(MmbMasterExt ext);

    int getAllDataCount(MmbMasterExt ext);

    MmbMasterExt selectBySelective(MmbMaster sysUser);

    int updateByCode(MmbMaster record);

    int deleteByCode(String code);

    int getCountBySelective(MmbMaster ext);

    MmbMaster getPointByCode(String code);

    int updatePointById(MmbMaster record);

    // 根据手机号获取会员列表
    List<MmbMasterExt> selectAllByPhone(MmbMasterExt ext);

    int getAllDataCountByPhone(MmbMasterExt ext);

    // 获取ERP发送失败的会员列表
    List<MmbMaster> selectSendFail();

    // 获取将要发送生日券的会员列表
    List<MmbMaster> queryForDistributeBirthdayCoupon(MmbMasterExt birthdayStart);

    List<MmbMasterExt> selectAllByTel(MmbMasterExt ext);

    int deleteBySelective(MmbMaster mmbMaster);

    int clearOldphoneById(String memberId);

    // 获取会员对账单列表
    List<MmbMasterExt> selectReport();

    int countByTelephone(String countByTelphone);

    List<MmbMasterExt> export(MmbMasterExt ext);

    // 获取ERP发送未成功的禁用会员列表
    List<MmbMaster> selectSendUsedFail();
}
