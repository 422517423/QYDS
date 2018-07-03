package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.CouponMaster;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponMasterMapper {
    int deleteByPrimaryKey(String couponId);

    int insert(CouponMaster record);

    int insertSelective(CouponMaster record);

    CouponMaster selectByPrimaryKey(String couponId);

    int updateByPrimaryKeySelective(CouponMaster record);

    int updateByPrimaryKey(CouponMaster record);
}