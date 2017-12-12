package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.ComDistrict;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComDistrictMapperExt {

    List<ComDistrict> selectByParentId(@Param(value = "parentId") String parentId);

}