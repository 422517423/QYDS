package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.MmbPointRecord;
import org.springframework.stereotype.Repository;

@Repository
public interface MmbPointRecordMapper {
    int deleteByPrimaryKey(Integer recordNo);

    int insert(MmbPointRecord record);

    int insertSelective(MmbPointRecord record);

    MmbPointRecord selectByPrimaryKey(Integer recordNo);

    int updateByPrimaryKeySelective(MmbPointRecord record);

    int updateByPrimaryKey(MmbPointRecord record);
}