package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.ErpBrand;
import org.springframework.stereotype.Repository;

@Repository
public interface ErpBrandMapper {
    int deleteByPrimaryKey(String brandCode);

    int insert(ErpBrand record);

    int insertSelective(ErpBrand record);

    ErpBrand selectByPrimaryKey(String brandCode);

    int updateByPrimaryKeySelective(ErpBrand record);

    int updateByPrimaryKey(ErpBrand record);
}