package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.OrdReturnExchange;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdReturnExchangeMapper {
    int deleteByPrimaryKey(String rexOrderId);

    int insert(OrdReturnExchange record);

    int insertSelective(OrdReturnExchange record);

    OrdReturnExchange selectByPrimaryKey(String rexOrderId);

    int updateByPrimaryKeySelective(OrdReturnExchange record);

    int updateByPrimaryKey(OrdReturnExchange record);
}