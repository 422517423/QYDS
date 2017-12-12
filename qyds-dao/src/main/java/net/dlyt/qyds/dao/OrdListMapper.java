package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.OrdList;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdListMapper {
    int deleteByPrimaryKey(String detailId);

    int insert(OrdList record);

    int insertSelective(OrdList record);

    OrdList selectByPrimaryKey(String detailId);

    int updateByPrimaryKeySelective(OrdList record);

    int updateByPrimaryKey(OrdList record);
}