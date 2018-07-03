package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.SysCode;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysCodeMapperExt {

    // 根据区分获取code
    List<SysCode> selectSysCodeByCategory(String category);

}