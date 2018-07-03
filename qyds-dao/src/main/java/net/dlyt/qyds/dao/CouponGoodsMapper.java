package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.CouponGoods;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponGoodsMapper {
    int deleteByPrimaryKey(String couponGoodsId);

    int insert(CouponGoods record);

    int insertSelective(CouponGoods record);

    CouponGoods selectByPrimaryKey(String couponGoodsId);

    int updateByPrimaryKeySelective(CouponGoods record);

    int updateByPrimaryKey(CouponGoods record);
}