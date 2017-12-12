package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.ErpProvince;
import org.springframework.stereotype.Repository;

@Repository
public interface ErpProvinceMapper {
    int deleteByPrimaryKey(String pcode);

    int insert(ErpProvince record);

    int insertSelective(ErpProvince record);

    ErpProvince selectByPrimaryKey(String pcode);

    int updateByPrimaryKeySelective(ErpProvince record);

    int updateByPrimaryKey(ErpProvince record);
}