package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.ErpProvince;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ErpProvinceMapperExt {
    
    List<ErpProvince> queryAllProvince();
}