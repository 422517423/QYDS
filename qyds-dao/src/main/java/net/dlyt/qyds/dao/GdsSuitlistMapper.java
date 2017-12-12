package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.GdsSuitlist;
import org.springframework.stereotype.Repository;


@Repository
public interface GdsSuitlistMapper {
    int deleteByPrimaryKey(String suitId);

    int insert(GdsSuitlist record);

    int insertSelective(GdsSuitlist record);

    GdsSuitlist selectByPrimaryKey(String suitId);

    int updateByPrimaryKeySelective(GdsSuitlist record);

    int updateByPrimaryKey(GdsSuitlist record);
}