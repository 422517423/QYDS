package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.SysLogOperation;
import org.springframework.stereotype.Repository;

@Repository
public interface SysLogOperationMapperExt {

    int insertSelective(SysLogOperation record);

}