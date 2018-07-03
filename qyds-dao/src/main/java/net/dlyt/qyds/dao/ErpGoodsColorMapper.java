package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.ErpGoodsColor;
import org.springframework.stereotype.Repository;

@Repository
public interface ErpGoodsColorMapper {
    int deleteByPrimaryKey(String colorCode);

    int insert(ErpGoodsColor record);

    int insertSelective(ErpGoodsColor record);

    ErpGoodsColor selectByPrimaryKey(String colorCode);

    int updateByPrimaryKeySelective(ErpGoodsColor record);

    int updateByPrimaryKey(ErpGoodsColor record);
}