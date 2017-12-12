package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.GdsBrand;
import org.springframework.stereotype.Repository;

@Repository
public interface GdsBrandMapper {
    int deleteByPrimaryKey(String brandId);

    int insert(GdsBrand record);

    int insertSelective(GdsBrand record);

    GdsBrand selectByPrimaryKey(String brandId);

    int updateByPrimaryKeySelective(GdsBrand record);

    int updateByPrimaryKey(GdsBrand record);
}