package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.SysLogOperation;
import org.springframework.stereotype.Repository;

@Repository
public interface SysLogOperationMapper {
    int deleteByPrimaryKey(Integer seq);

    int insert(SysLogOperation record);

    int insertSelective(SysLogOperation record);

    SysLogOperation selectByPrimaryKey(Integer seq);

    int updateByPrimaryKeySelective(SysLogOperation record);

    int updateByPrimaryKey(SysLogOperation record);
}