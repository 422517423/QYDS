package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.PamsTaskReport;
import org.springframework.stereotype.Repository;

@Repository
public interface PamsTaskReportMapper {
    int deleteByPrimaryKey(Integer seq);

    int insert(PamsTaskReport record);

    int insertSelective(PamsTaskReport record);

    PamsTaskReport selectByPrimaryKey(Integer seq);

    int updateByPrimaryKeySelective(PamsTaskReport record);

    int updateByPrimaryKey(PamsTaskReport record);
}