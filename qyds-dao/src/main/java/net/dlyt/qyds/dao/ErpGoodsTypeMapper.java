package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.ErpGoodsType;
import org.springframework.stereotype.Repository;

@Repository
public interface ErpGoodsTypeMapper {
    int deleteByPrimaryKey(String typeCode);

    int insert(ErpGoodsType record);

    int insertSelective(ErpGoodsType record);

    ErpGoodsType selectByPrimaryKey(String typeCode);

    int updateByPrimaryKeySelective(ErpGoodsType record);

    int updateByPrimaryKey(ErpGoodsType record);
}