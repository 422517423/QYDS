package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.MmbGroupMember;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MmbGroupMemberMapper {
    int deleteByPrimaryKey(String groupMemberId);

    int insert(MmbGroupMember record);

    int insertSelective(MmbGroupMember record);

    MmbGroupMember selectByPrimaryKey(String groupMemberId);

    int updateByPrimaryKeySelective(MmbGroupMember record);

    int updateByPrimaryKey(MmbGroupMember record);

    List<MmbGroupMember> selectByMemberId(String MemberId);
}