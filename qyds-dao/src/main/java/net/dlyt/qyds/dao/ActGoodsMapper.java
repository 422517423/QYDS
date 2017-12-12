package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.ActGoods;
import org.springframework.stereotype.Repository;

@Repository
public interface ActGoodsMapper {
    int deleteByPrimaryKey(String actGoodsId);

    int insert(ActGoods record);

    int insertSelective(ActGoods record);

    ActGoods selectByPrimaryKey(String actGoodsId);

    int updateByPrimaryKeySelective(ActGoods record);

    int updateByPrimaryKey(ActGoods record);
}