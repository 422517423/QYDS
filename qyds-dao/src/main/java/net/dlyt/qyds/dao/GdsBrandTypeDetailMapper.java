package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.GdsBrandTypeDetail;

public interface GdsBrandTypeDetailMapper {
    int deleteByPrimaryKey(String goodsTypeId);

    int insert(GdsBrandTypeDetail record);

    int insertSelective(GdsBrandTypeDetail record);

    GdsBrandTypeDetail selectByPrimaryKey(String goodsTypeId);

    int updateByPrimaryKeySelective(GdsBrandTypeDetail record);

    int updateByPrimaryKey(GdsBrandTypeDetail record);
}