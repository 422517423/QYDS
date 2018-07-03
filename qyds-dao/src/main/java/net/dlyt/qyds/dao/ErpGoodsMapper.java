package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.ErpGoods;
import org.springframework.stereotype.Repository;

@Repository
public interface ErpGoodsMapper {
    int deleteByPrimaryKey(String sku);

    int insert(ErpGoods record);

    int insertSelective(ErpGoods record);

    ErpGoods selectByPrimaryKey(String sku);

    int updateByPrimaryKeySelective(ErpGoods record);

    int updateByPrimaryKey(ErpGoods record);
}