package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.MmbPointRule;
import org.springframework.stereotype.Repository;

@Repository
public interface MmbPointRuleMapper {
    int deleteByPrimaryKey(String ruleId);

    int insert(MmbPointRule record);

    int insertSelective(MmbPointRule record);

    MmbPointRule selectByPrimaryKey(String ruleId);

    int updateByPrimaryKeySelective(MmbPointRule record);

    int updateByPrimaryKey(MmbPointRule record);
}