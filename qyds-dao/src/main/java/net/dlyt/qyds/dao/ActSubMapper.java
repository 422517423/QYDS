package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.ActSub;
import org.springframework.stereotype.Repository;

@Repository
public interface ActSubMapper {
    int deleteByPrimaryKey(String actSubId);

    int insert(ActSub record);

    int insertSelective(ActSub record);

    ActSub selectByPrimaryKey(String actSubId);

    int updateByPrimaryKeySelective(ActSub record);

    int updateByPrimaryKey(ActSub record);
}