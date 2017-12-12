package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.BnkMaster;
import org.springframework.stereotype.Repository;

@Repository
public interface BnkMasterMapper {
    int deleteByPrimaryKey(Integer bankId);

    int insert(BnkMaster record);

    int insertSelective(BnkMaster record);

    BnkMaster selectByPrimaryKey(Integer bankId);

    int updateByPrimaryKeySelective(BnkMaster record);

    int updateByPrimaryKey(BnkMaster record);
}