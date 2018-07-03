package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.ActMaster;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActMasterMapper {
    int deleteByPrimaryKey(String activityId);

    int insert(ActMaster record);

    int insertSelective(ActMaster record);

    ActMaster selectByPrimaryKey(String activityId);

    int updateByPrimaryKeySelective(ActMaster record);

    int updateByPrimaryKey(ActMaster record);

    List<ActMaster> selectByActivityIds(@Param("activityIds")List<String> activityIds);
}