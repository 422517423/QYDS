package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.MmbSaler;
import org.springframework.stereotype.Repository;

@Repository
public interface MmbSalerMapper {
    int deleteByPrimaryKey(String memberId);

    int insert(MmbSaler record);

    int insertSelective(MmbSaler record);

    MmbSaler selectByPrimaryKey(String memberId);

    int updateByPrimaryKeySelective(MmbSaler record);

    int updateByPrimaryKey(MmbSaler record);
}