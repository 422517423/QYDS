package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.OrdMaster;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdMasterMapper {
    int deleteByPrimaryKey(String orderId);

    int insert(OrdMaster record);

    int insertSelective(OrdMaster record);

    OrdMaster selectByPrimaryKey(String orderId);

    int updateByPrimaryKeySelective(OrdMaster record);

    int updateByPrimaryKey(OrdMaster record);
}