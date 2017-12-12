package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.GdsTypeDetail;
import org.springframework.stereotype.Repository;

@Repository
public interface GdsTypeDetailMapper {
    int deleteByPrimaryKey(String goodsTypeId);

    int insert(GdsTypeDetail record);

    int insertSelective(GdsTypeDetail record);

    GdsTypeDetail selectByPrimaryKey(String goodsTypeId);

    int updateByPrimaryKeySelective(GdsTypeDetail record);

    int updateByPrimaryKey(GdsTypeDetail record);
}