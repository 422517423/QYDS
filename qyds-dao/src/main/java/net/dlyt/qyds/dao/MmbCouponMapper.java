package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.MmbCoupon;
import org.springframework.stereotype.Repository;

@Repository
public interface MmbCouponMapper {
    int deleteByPrimaryKey(Integer recordNo);

    int insert(MmbCoupon record);

    int insertSelective(MmbCoupon record);

    MmbCoupon selectByPrimaryKey(Integer recordNo);

    int updateByPrimaryKeySelective(MmbCoupon record);

    int updateByPrimaryKey(MmbCoupon record);
}