package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.ComConfig;
import net.dlyt.qyds.common.dto.ComConfigKey;
import org.springframework.stereotype.Repository;

@Repository
public interface ComConfigMapper {
    int deleteByPrimaryKey(ComConfigKey key);

    int insert(ComConfig record);

    int insertSelective(ComConfig record);

    ComConfig selectByPrimaryKey(ComConfigKey key);

    int updateByPrimaryKeySelective(ComConfig record);

    int updateByPrimaryKey(ComConfig record);
}