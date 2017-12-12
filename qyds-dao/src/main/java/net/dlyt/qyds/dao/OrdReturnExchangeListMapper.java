package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.OrdReturnExchangeList;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdReturnExchangeListMapper {
    int deleteByPrimaryKey(String detailId);

    int insert(OrdReturnExchangeList record);

    int insertSelective(OrdReturnExchangeList record);

    OrdReturnExchangeList selectByPrimaryKey(String detailId);

    int updateByPrimaryKeySelective(OrdReturnExchangeList record);

    int updateByPrimaryKey(OrdReturnExchangeList record);
}