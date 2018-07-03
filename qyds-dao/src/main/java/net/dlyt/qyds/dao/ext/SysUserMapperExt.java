package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.SysUser;
import net.dlyt.qyds.common.dto.SysUserExt;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysUserMapperExt {

   List<SysUserExt> selectAll(SysUserExt form);

   int selectCountAll(SysUserExt form);

   int insertSelective(SysUser sysUser);

   int updateByPrimaryKeySelective(SysUser sysUser);

   SysUserExt selectByPrimaryKey(Integer userId);

   int deleteByPrimaryKey(SysUser sysUser);

   SysUser selectByLoginIdAndPassword(SysUser sysUser);

   SysUser selectByLoginId(String loginId);

   int updatePassword(SysUser sysUser);

   int deleteErpStorer();

   int deleteByLoginId(String loginId);

//   void resetPassword(SysUser sysUser);
}