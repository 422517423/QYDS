package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.MmbContact;

public interface MmbContactMapper {
    int deleteByPrimaryKey(String contactId);

    int insert(MmbContact record);

    int insertSelective(MmbContact record);

    MmbContact selectByPrimaryKey(String contactId);

    int updateByPrimaryKeySelective(MmbContact record);

    int updateByPrimaryKey(MmbContact record);
}