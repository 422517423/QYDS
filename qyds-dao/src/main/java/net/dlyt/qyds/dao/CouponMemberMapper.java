package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.CouponMember;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponMemberMapper {
    int deleteByPrimaryKey(String couponMemberId);

    int insert(CouponMember record);

    int insertSelective(CouponMember record);

    CouponMember selectByPrimaryKey(String couponMemberId);

    int updateByPrimaryKeySelective(CouponMember record);

    int updateByPrimaryKey(CouponMember record);
}