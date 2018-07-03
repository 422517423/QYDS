package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.SysCode;
import org.springframework.stereotype.Repository;

@Repository
public interface SysCodeMapper {
    int deleteByPrimaryKey(Integer seq);

    int insert(SysCode record);

    int insertSelective(SysCode record);

    SysCode selectByPrimaryKey(Integer seq);

    int updateByPrimaryKeySelective(SysCode record);

    int updateByPrimaryKey(SysCode record);
}