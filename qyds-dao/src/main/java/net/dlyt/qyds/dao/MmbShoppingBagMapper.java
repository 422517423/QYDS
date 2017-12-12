package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.MmbShoppingBag;
import org.springframework.stereotype.Repository;

@Repository
public interface MmbShoppingBagMapper {
    int deleteByPrimaryKey(String bagNo);

    int insert(MmbShoppingBag record);

    int insertSelective(MmbShoppingBag record);

    MmbShoppingBag selectByPrimaryKey(String bagNo);

    int updateByPrimaryKeySelective(MmbShoppingBag record);

    int updateByPrimaryKey(MmbShoppingBag record);
}