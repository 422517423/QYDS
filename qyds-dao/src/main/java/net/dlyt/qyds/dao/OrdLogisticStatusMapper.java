package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.ActGoods;
import net.dlyt.qyds.common.dto.OrdLogisticStatus;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdLogisticStatusMapper {
    int deleteByPrimaryKey(String ordLogisticId);

    int insert(OrdLogisticStatus record);

    int insertSelective(OrdLogisticStatus record);

    OrdLogisticStatus selectByPrimaryKey(String ordLogisticId);

    int updateByPrimaryKeySelective(OrdLogisticStatus record);

    int updateByPrimaryKey(OrdLogisticStatus record);
}