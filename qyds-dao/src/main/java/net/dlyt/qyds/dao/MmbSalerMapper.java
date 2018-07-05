package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.MmbSaler;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MmbSalerMapper {
    List<MmbSaler> selectAll();

    int deleteByPrimaryKey(String memberId);

    int insert(MmbSaler record);

    int insertSelective(MmbSaler record);

    MmbSaler selectByPrimaryKey(String memberId);

    List<MmbSaler> selectBySalerId(String salerId);

    int updateByPrimaryKeySelective(MmbSaler record);

    int updateByPrimaryKey(MmbSaler record);
}