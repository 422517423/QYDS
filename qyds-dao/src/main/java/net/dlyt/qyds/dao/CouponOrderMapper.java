package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.CouponOrder;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponOrderMapper {
    int deleteByPrimaryKey(String orderId);

    int insert(CouponOrder record);

    int insertSelective(CouponOrder record);

    CouponOrder selectByPrimaryKey(String orderId);

    int updateByPrimaryKeySelective(CouponOrder record);

    int updateByPrimaryKey(CouponOrder record);
}