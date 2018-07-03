package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.GdsMaster;
import org.springframework.stereotype.Repository;

@Repository
public interface GdsMasterMapper {
    int deleteByPrimaryKey(String goodsId);

    int insert(GdsMaster record);

    int insertSelective(GdsMaster record);

    GdsMaster selectByPrimaryKey(String goodsId);

    int updateByPrimaryKeySelective(GdsMaster record);

    int updateByPrimaryKey(GdsMaster record);
}