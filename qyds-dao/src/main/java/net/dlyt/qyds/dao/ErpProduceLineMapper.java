package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.ErpProduceLine;
import org.springframework.stereotype.Repository;

@Repository
public interface ErpProduceLineMapper {
    int deleteByPrimaryKey(String lineCode);

    int insert(ErpProduceLine record);

    int insertSelective(ErpProduceLine record);

    ErpProduceLine selectByPrimaryKey(String lineCode);

    int updateByPrimaryKeySelective(ErpProduceLine record);

    int updateByPrimaryKey(ErpProduceLine record);
}