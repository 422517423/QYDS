package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.GdsTypeProperty;
import org.springframework.stereotype.Repository;

@Repository
public interface GdsTypePropertyMapper {
    int deleteByPrimaryKey(String goodsTypePropertyId);

    int insert(GdsTypeProperty record);

    int insertSelective(GdsTypeProperty record);

    GdsTypeProperty selectByPrimaryKey(String goodsTypePropertyId);

    int updateByPrimaryKeySelective(GdsTypeProperty record);

    int updateByPrimaryKey(GdsTypeProperty record);
}