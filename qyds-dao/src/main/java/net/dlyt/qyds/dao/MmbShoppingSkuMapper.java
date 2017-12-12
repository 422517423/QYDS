package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.MmbShoppingSku;
import org.springframework.stereotype.Repository;

@Repository
public interface MmbShoppingSkuMapper {
    int insert(MmbShoppingSku record);

    int insertSelective(MmbShoppingSku record);
}