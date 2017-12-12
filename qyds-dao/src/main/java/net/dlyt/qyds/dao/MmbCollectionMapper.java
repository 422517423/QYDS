package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.MmbCollection;
import org.springframework.stereotype.Repository;

@Repository
public interface MmbCollectionMapper {
    int deleteByPrimaryKey(Integer collectNo);

    int insert(MmbCollection record);

    int insertSelective(MmbCollection record);

    MmbCollection selectByPrimaryKey(Integer collectNo);

    int updateByPrimaryKeySelective(MmbCollection record);

    int updateByPrimaryKey(MmbCollection record);
}