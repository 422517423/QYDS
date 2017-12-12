package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.ErpReturn;
import net.dlyt.qyds.common.dto.ErpReturnKey;
import org.springframework.stereotype.Repository;

@Repository
public interface ErpReturnMapper {
    int deleteByPrimaryKey(ErpReturnKey key);

    int insert(ErpReturn record);

    int insertSelective(ErpReturn record);

    ErpReturn selectByPrimaryKey(ErpReturnKey key);

    int updateByPrimaryKeySelective(ErpReturn record);

    int updateByPrimaryKey(ErpReturn record);
}