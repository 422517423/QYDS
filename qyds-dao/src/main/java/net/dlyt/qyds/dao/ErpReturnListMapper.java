package net.dlyt.qyds.dao;

import java.math.BigDecimal;
import net.dlyt.qyds.common.dto.ErpReturnList;
import org.springframework.stereotype.Repository;

@Repository
public interface ErpReturnListMapper {
    int deleteByPrimaryKey(BigDecimal amount);

    int insert(ErpReturnList record);

    int insertSelective(ErpReturnList record);

    ErpReturnList selectByPrimaryKey(BigDecimal amount);

    int updateByPrimaryKeySelective(ErpReturnList record);

    int updateByPrimaryKey(ErpReturnList record);
}