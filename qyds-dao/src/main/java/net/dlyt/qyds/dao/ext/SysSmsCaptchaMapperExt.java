package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.SysSmsCaptcha;
import org.springframework.stereotype.Repository;

@Repository
public interface SysSmsCaptchaMapperExt {
    int insertSelective(SysSmsCaptcha record);

    SysSmsCaptcha selectBySelective(SysSmsCaptcha record);
}