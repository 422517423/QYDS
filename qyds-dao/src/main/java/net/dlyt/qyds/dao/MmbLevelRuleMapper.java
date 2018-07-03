package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.MmbLevelRule;
import org.springframework.stereotype.Repository;

@Repository
public interface MmbLevelRuleMapper {
    int deleteByPrimaryKey(String memberLevelId);

    int insert(MmbLevelRule record);

    int insertSelective(MmbLevelRule record);

    MmbLevelRule selectByPrimaryKey(String memberLevelId);

    int updateByPrimaryKeySelective(MmbLevelRule record);

    int updateByPrimaryKey(MmbLevelRule record);
}