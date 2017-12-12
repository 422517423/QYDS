package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.PrizeGoods;
import org.springframework.stereotype.Repository;

@Repository
public interface PrizeGoodsMapper {
    int deleteByPrimaryKey(String prizeGoodsId);

    int insert(PrizeGoods record);

    int insertSelective(PrizeGoods record);

    PrizeGoods selectByPrimaryKey(String prizeGoodsId);

    int updateByPrimaryKeySelective(PrizeGoods record);

    int updateByPrimaryKey(PrizeGoods record);
}