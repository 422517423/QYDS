package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.ErpMember;
import org.springframework.stereotype.Repository;

@Repository
public interface ErpMemberMapper {
    int deleteByPrimaryKey(String memberCode);

    int insert(ErpMember record);

    int insertSelective(ErpMember record);

    ErpMember selectByPrimaryKey(String memberCode);

    int updateByPrimaryKeySelective(ErpMember record);

    int updateByPrimaryKey(ErpMember record);
}