package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.GdsColoreimage;
import org.springframework.stereotype.Repository;

@Repository
public interface GdsColoreimageMapper {
    int deleteByPrimaryKey(String goodsColoreId);

    int insert(GdsColoreimage record);

    int insertSelective(GdsColoreimage record);

    GdsColoreimage selectByPrimaryKey(String goodsColoreId);

    int updateByPrimaryKeySelective(GdsColoreimage record);

    int updateByPrimaryKey(GdsColoreimage record);
}