package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.UsrShop;
import org.springframework.stereotype.Repository;

@Repository
public interface UsrShopMapper {
    int deleteByPrimaryKey(String userShopId);

    int insert(UsrShop record);

    int insertSelective(UsrShop record);

    UsrShop selectByPrimaryKey(String userShopId);

    int updateByPrimaryKeySelective(UsrShop record);

    int updateByPrimaryKey(UsrShop record);
}