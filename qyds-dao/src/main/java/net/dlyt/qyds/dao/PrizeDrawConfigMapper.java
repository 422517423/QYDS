package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.PrizeDrawConfig;
import org.springframework.stereotype.Repository;

@Repository
public interface PrizeDrawConfigMapper {
    int deleteByPrimaryKey(String prizeDrawId);

    int insert(PrizeDrawConfig record);

    int insertSelective(PrizeDrawConfig record);

    PrizeDrawConfig selectByPrimaryKey(String prizeDrawId);

    int updateByPrimaryKeySelective(PrizeDrawConfig record);

    int updateByPrimaryKey(PrizeDrawConfig record);
}