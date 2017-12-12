package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.MmbAddress;
import org.springframework.stereotype.Repository;

@Repository
public interface MmbAddressMapper {
    int deleteByPrimaryKey(String addressId);

    int insert(MmbAddress record);

    int insertSelective(MmbAddress record);

    MmbAddress selectByPrimaryKey(String addressId);

    int updateByPrimaryKeySelective(MmbAddress record);

    int updateByPrimaryKey(MmbAddress record);
}