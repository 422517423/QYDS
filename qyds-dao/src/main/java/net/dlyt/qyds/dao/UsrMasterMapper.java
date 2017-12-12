package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.UsrMaster;
import org.springframework.stereotype.Repository;

@Repository
public interface UsrMasterMapper {
    int deleteByPrimaryKey(String userId);

    int insert(UsrMaster record);

    int insertSelective(UsrMaster record);

    UsrMaster selectByPrimaryKey(String userId);

    int updateByPrimaryKeySelective(UsrMaster record);

    int updateByPrimaryKey(UsrMaster record);
}