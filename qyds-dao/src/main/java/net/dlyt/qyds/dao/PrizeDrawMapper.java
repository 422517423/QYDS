package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.PrizeDraw;
import org.springframework.stereotype.Repository;

@Repository
public interface PrizeDrawMapper {
    int deleteByPrimaryKey(String prizeDrawId);

    int insert(PrizeDraw record);

    int insertSelective(PrizeDraw record);

    PrizeDraw selectByPrimaryKey(String prizeDrawId);

    int updateByPrimaryKeySelective(PrizeDraw record);

    int updateByPrimaryKey(PrizeDraw record);
}