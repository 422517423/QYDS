package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.MmbMaster;
import org.springframework.stereotype.Repository;

@Repository
public interface MmbMasterMapper {
    int deleteByPrimaryKey(String memberId);

    int insert(MmbMaster record);

    int insertSelective(MmbMaster record);

    MmbMaster selectByPrimaryKey(String memberId);

    int updateByPrimaryKeySelective(MmbMaster record);

    int updateByPrimaryKey(MmbMaster record);
}