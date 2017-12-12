package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.ActTemplate;
import org.springframework.stereotype.Repository;

@Repository
public interface ActTemplateMapper {
    int deleteByPrimaryKey(String tempId);

    int insert(ActTemplate record);

    int insertSelective(ActTemplate record);

    ActTemplate selectByPrimaryKey(String tempId);

    int updateByPrimaryKeySelective(ActTemplate record);

    int updateByPrimaryKey(ActTemplate record);
}