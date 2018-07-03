package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.MmbGroup;
import org.springframework.stereotype.Repository;

@Repository
public interface MmbGroupMapper {
    int deleteByPrimaryKey(String groupId);

    int insert(MmbGroup record);

    int insertSelective(MmbGroup record);

    MmbGroup selectByPrimaryKey(String groupId);

    int updateByPrimaryKeySelective(MmbGroup record);

    int updateByPrimaryKey(MmbGroup record);
}