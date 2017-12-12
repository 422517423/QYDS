package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.GdsBrandType;

public interface GdsBrandTypeMapper {
    int deleteByPrimaryKey(String goodsTypeId);

    int insert(GdsBrandType record);

    int insertSelective(GdsBrandType record);

    GdsBrandType selectByPrimaryKey(String goodsTypeId);

    int updateByPrimaryKeySelective(GdsBrandType record);

    int updateByPrimaryKey(GdsBrandType record);
}