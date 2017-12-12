package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.SysLogLogon;
import org.springframework.stereotype.Repository;

@Repository
public interface SysLogLogonMapperExt {

    int insertSelective(SysLogLogon record);
    
}