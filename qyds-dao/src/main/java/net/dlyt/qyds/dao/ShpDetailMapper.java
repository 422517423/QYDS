package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.ShpDetail;
import org.springframework.stereotype.Repository;

@Repository
public interface ShpDetailMapper {
    int deleteByPrimaryKey(String shopId);

    int insert(ShpDetail record);

    int insertSelective(ShpDetail record);

    ShpDetail selectByPrimaryKey(String shopId);

    int updateByPrimaryKeySelective(ShpDetail record);

    int updateByPrimaryKey(ShpDetail record);
}