package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.SysLogLogon;
import org.springframework.stereotype.Repository;

@Repository
public interface SysLogLogonMapper {
    int deleteByPrimaryKey(Integer seq);

    int insert(SysLogLogon record);

    int insertSelective(SysLogLogon record);

    SysLogLogon selectByPrimaryKey(Integer seq);

    int updateByPrimaryKeySelective(SysLogLogon record);

    int updateByPrimaryKey(SysLogLogon record);
}