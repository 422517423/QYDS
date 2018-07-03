package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.ActGoods;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActGoodsMapper {
    int deleteByPrimaryKey(String actGoodsId);

    int insert(ActGoods record);

    int insertSelective(ActGoods record);

    ActGoods selectByPrimaryKey(String actGoodsId);

    int updateByPrimaryKeySelective(ActGoods record);

    int updateByPrimaryKey(ActGoods record);

    List<ActGoods> selectByActivityIds(@Param("activityIds")List<String> activityId);

    List<ActGoods> selectByGoodsActivityIds(@Param("activityIds")List<String> activityId,@Param("goodsId")String goodsId);
}