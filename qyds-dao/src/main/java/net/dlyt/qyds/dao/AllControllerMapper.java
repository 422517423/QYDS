package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.AllController;

public interface AllControllerMapper {
    int insert(AllController record);

    int insertSelective(AllController record);

    int updateByPrimaryKeySelective(AllController record);
}