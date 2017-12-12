package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.ErpDistrict;
import org.springframework.stereotype.Repository;

@Repository
public interface ErpDistrictMapper {
    int deleteByPrimaryKey(String dcode);

    int insert(ErpDistrict record);

    int insertSelective(ErpDistrict record);

    ErpDistrict selectByPrimaryKey(String dcode);

    int updateByPrimaryKeySelective(ErpDistrict record);

    int updateByPrimaryKey(ErpDistrict record);
}