package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.GdsType;
import org.springframework.stereotype.Repository;

@Repository
public interface GdsTypeMapper {
    int deleteByPrimaryKey(String goodsTypeId);

    int insert(GdsType record);

    int insertSelective(GdsType record);

    GdsType selectByPrimaryKey(String goodsTypeId);

    int updateByPrimaryKeySelective(GdsType record);

    int updateByPrimaryKey(GdsType record);
}