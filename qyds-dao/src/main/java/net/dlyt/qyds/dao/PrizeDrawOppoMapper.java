package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.PrizeDrawOppo;
import org.springframework.stereotype.Repository;

@Repository
public interface PrizeDrawOppoMapper {
    int deleteByPrimaryKey(String prizeDrawOppoId);

    int insert(PrizeDrawOppo record);

    int insertSelective(PrizeDrawOppo record);

    PrizeDrawOppo selectByPrimaryKey(String prizeDrawOppoId);

    int updateByPrimaryKeySelective(PrizeDrawOppo record);

    int updateByPrimaryKey(PrizeDrawOppo record);
}