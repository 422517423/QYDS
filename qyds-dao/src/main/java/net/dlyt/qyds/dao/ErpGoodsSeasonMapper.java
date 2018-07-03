package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.ErpGoodsSeason;
import org.springframework.stereotype.Repository;

@Repository
public interface ErpGoodsSeasonMapper {
    int deleteByPrimaryKey(String seasonCode);

    int insert(ErpGoodsSeason record);

    int insertSelective(ErpGoodsSeason record);

    ErpGoodsSeason selectByPrimaryKey(String seasonCode);

    int updateByPrimaryKeySelective(ErpGoodsSeason record);

    int updateByPrimaryKey(ErpGoodsSeason record);
}