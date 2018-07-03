package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.ErpCity;
import org.springframework.stereotype.Repository;

@Repository
public interface ErpCityMapper {
    int deleteByPrimaryKey(String ccode);

    int insert(ErpCity record);

    int insertSelective(ErpCity record);

    ErpCity selectByPrimaryKey(String ccode);

    int updateByPrimaryKeySelective(ErpCity record);

    int updateByPrimaryKey(ErpCity record);
}