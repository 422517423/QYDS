package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.ErpGoodsSize;
import net.dlyt.qyds.common.dto.ErpGoodsSizeKey;
import org.springframework.stereotype.Repository;

@Repository
public interface ErpGoodsSizeMapper {
    int deleteByPrimaryKey(ErpGoodsSizeKey key);

    int insert(ErpGoodsSize record);

    int insertSelective(ErpGoodsSize record);

    ErpGoodsSize selectByPrimaryKey(ErpGoodsSizeKey key);

    int updateByPrimaryKeySelective(ErpGoodsSize record);

    int updateByPrimaryKey(ErpGoodsSize record);
}