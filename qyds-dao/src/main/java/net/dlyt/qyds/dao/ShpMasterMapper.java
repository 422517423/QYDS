package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.ShpMaster;
import org.springframework.stereotype.Repository;

@Repository
public interface ShpMasterMapper {
    int deleteByPrimaryKey(String shopId);

    int insert(ShpMaster record);

    int insertSelective(ShpMaster record);

    ShpMaster selectByPrimaryKey(String shopId);

    int updateByPrimaryKeySelective(ShpMaster record);

    int updateByPrimaryKey(ShpMaster record);
}