package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.ErpCity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ErpCityMapperExt {

    List<ErpCity> queryCityOfProvince(String pcode);
}