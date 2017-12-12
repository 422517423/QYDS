package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.GdsDetail;
import org.springframework.stereotype.Repository;

@Repository
public interface GdsDetailMapper {
    int deleteByPrimaryKey(String goodsId);

    int insert(GdsDetail record);

    int insertSelective(GdsDetail record);

    GdsDetail selectByPrimaryKey(String goodsId);

    int updateByPrimaryKeySelective(GdsDetail record);

    int updateByPrimaryKey(GdsDetail record);
}