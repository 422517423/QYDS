package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.ErpAction;
import org.springframework.stereotype.Repository;

@Repository
public interface ErpActionMapper {
    int deleteByPrimaryKey(String actionCode);

    int insert(ErpAction record);

    int insertSelective(ErpAction record);

    ErpAction selectByPrimaryKey(String actionCode);

    int updateByPrimaryKeySelective(ErpAction record);

    int updateByPrimaryKey(ErpAction record);
}