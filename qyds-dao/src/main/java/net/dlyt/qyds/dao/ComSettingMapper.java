package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.ComSetting;
import org.springframework.stereotype.Repository;

@Repository
public interface ComSettingMapper {
    int deleteByPrimaryKey(String comSettingId);

    int insert(ComSetting record);

    int insertSelective(ComSetting record);

    ComSetting selectByPrimaryKey(String comSettingId);

    int updateByPrimaryKeySelective(ComSetting record);

    int updateByPrimaryKey(ComSetting record);
}