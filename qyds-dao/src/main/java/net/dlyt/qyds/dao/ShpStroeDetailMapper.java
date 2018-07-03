package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.ShpStroeDetail;

public interface ShpStroeDetailMapper {
    int deleteByPrimaryKey(String orgId);

    int insert(ShpStroeDetail record);

    int insertSelective(ShpStroeDetail record);

    ShpStroeDetail selectByPrimaryKey(String orgId);

    int updateByPrimaryKeySelective(ShpStroeDetail record);

    int updateByPrimaryKey(ShpStroeDetail record);
}