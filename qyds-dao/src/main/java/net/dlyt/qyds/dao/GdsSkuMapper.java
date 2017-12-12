package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.GdsSku;
import org.springframework.stereotype.Repository;

@Repository
public interface GdsSkuMapper {
    int deleteByPrimaryKey(String goodsSkuId);

    int insert(GdsSku record);

    int insertSelective(GdsSku record);

    GdsSku selectByPrimaryKey(String goodsSkuId);

    int updateByPrimaryKeySelective(GdsSku record);

    int updateByPrimaryKey(GdsSku record);
}