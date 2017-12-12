package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.ComDistrict;
import org.springframework.stereotype.Repository;

@Repository
public interface ComDistrictMapper {
    int deleteByPrimaryKey(String districtId);

    int insert(ComDistrict record);

    int insertSelective(ComDistrict record);

    ComDistrict selectByPrimaryKey(String districtId);

    int updateByPrimaryKeySelective(ComDistrict record);

    int updateByPrimaryKey(ComDistrict record);
}