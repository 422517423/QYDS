package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.GdsSell;
import org.springframework.stereotype.Repository;

@Repository
public interface GdsSellMapper {
    int deleteByPrimaryKey(String goodsId);

    int insert(GdsSell record);

    int insertSelective(GdsSell record);

    GdsSell selectByPrimaryKey(String goodsId);

    int updateByPrimaryKeySelective(GdsSell record);

    int updateByPrimaryKey(GdsSell record);
}