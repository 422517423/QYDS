package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.OrdDispatch;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdDispatchMapper {
    int deleteByPrimaryKey(String dispatchId);

    int insert(OrdDispatch record);

    int insertSelective(OrdDispatch record);

    OrdDispatch selectByPrimaryKey(String dispatchId);

    int updateByPrimaryKeySelective(OrdDispatch record);

    int updateByPrimaryKey(OrdDispatch record);
}