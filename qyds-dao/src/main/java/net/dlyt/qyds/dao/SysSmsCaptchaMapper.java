package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.SysSmsCaptcha;
import org.springframework.stereotype.Repository;

@Repository
public interface SysSmsCaptchaMapper {
    int deleteByPrimaryKey(Integer seq);

    int insert(SysSmsCaptcha record);

    int insertSelective(SysSmsCaptcha record);

    SysSmsCaptcha selectByPrimaryKey(Integer seq);

    int updateByPrimaryKeySelective(SysSmsCaptcha record);

    int updateByPrimaryKey(SysSmsCaptcha record);
}