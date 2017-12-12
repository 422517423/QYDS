package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.CouponMaster;
import net.dlyt.qyds.common.dto.ext.CouponMasterExt;
import net.dlyt.qyds.common.dto.ext.CouponMemberExt;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by cjk on 16/8/6.
 */
@Repository
public interface CouponMasterMapperExt {

    List<CouponMaster> select(CouponMaster form);

    int selectCount(CouponMaster form);

    int checkExistByCouponName(CouponMaster form);

    List<CouponMaster> selectApproveList(CouponMasterExt form);

    int selectApproveCount(CouponMasterExt form);

    CouponMasterExt selectById(CouponMasterExt tempId);

    List<CouponMasterExt> getAllCoupons(CouponMemberExt form);

    List<CouponMasterExt> getPointExchangeCoupons(CouponMemberExt form);

    List<CouponMasterExt> getMyCoupons(CouponMemberExt form);

    int getMyCouponsCount(CouponMemberExt form);

    List<CouponMasterExt> getOrderCoupons(CouponMemberExt form);

    List<CouponMasterExt> getMyCouponsForPc(CouponMemberExt form);

    int getMyCouponsCountForPc(CouponMemberExt form);

    int addOneDistributedById(String couponId);

    //add by zlh
    List<CouponMaster> selectSendFail();
    CouponMaster selectSendFailById(String couponId);
    int updateSendById(CouponMaster record);


    CouponMasterExt selectBirthdayCoupon(String memberLevel);

    CouponMasterExt selectRegisterCoupon();


    void setOnlyCoupon(CouponMaster coupon);

    List<CouponMaster> selectBirthdayCouponList();

    List<CouponMasterExt> getSendList();
}
