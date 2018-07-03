package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.CmsItems;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsItemsMapper {
    int deleteByPrimaryKey(String itemId);

    int insert(CmsItems record);

    int insertSelective(CmsItems record);

    CmsItems selectByPrimaryKey(String itemId);

    int updateByPrimaryKeySelective(CmsItems record);

    int updateByPrimaryKey(CmsItems record);
}