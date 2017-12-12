package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.ErpDistrict;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ErpDistrictMapperExt {

    List<ErpDistrict> querypDistrictOfCity(String ccode);
}