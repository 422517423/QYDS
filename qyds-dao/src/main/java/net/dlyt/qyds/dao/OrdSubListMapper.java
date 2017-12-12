package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.OrdSubList;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdSubListMapper {
    int deleteByPrimaryKey(String subOrderId);

    int insert(OrdSubList record);

    int insertSelective(OrdSubList record);

    OrdSubList selectByPrimaryKey(String subOrderId);

    int updateByPrimaryKeySelective(OrdSubList record);

    int updateByPrimaryKey(OrdSubList record);
}