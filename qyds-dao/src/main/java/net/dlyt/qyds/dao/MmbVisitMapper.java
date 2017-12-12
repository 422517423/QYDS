package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.MmbVisit;
import org.springframework.stereotype.Repository;

@Repository
public interface MmbVisitMapper {
    int deleteByPrimaryKey(Integer visitNo);

    int insert(MmbVisit record);

    int insertSelective(MmbVisit record);

    MmbVisit selectByPrimaryKey(Integer visitNo);

    int updateByPrimaryKeySelective(MmbVisit record);

    int updateByPrimaryKey(MmbVisit record);
}