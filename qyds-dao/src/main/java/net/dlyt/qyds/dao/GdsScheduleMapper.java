package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.GdsSchedule;
import org.springframework.stereotype.Repository;

@Repository
public interface GdsScheduleMapper {
    int deleteByPrimaryKey(String scheduleId);

    int insert(GdsSchedule record);

    int insertSelective(GdsSchedule record);

    GdsSchedule selectByPrimaryKey(String scheduleId);

    int updateByPrimaryKeySelective(GdsSchedule record);

    int updateByPrimaryKey(GdsSchedule record);
}