package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.CmsMasterGoods;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsMasterGoodsMapper {
    int deleteByPrimaryKey(String cmsGdsId);

    int insert(CmsMasterGoods record);

    int insertSelective(CmsMasterGoods record);

    CmsMasterGoods selectByPrimaryKey(String cmsGdsId);

    int updateByPrimaryKeySelective(CmsMasterGoods record);

    int updateByPrimaryKey(CmsMasterGoods record);
}