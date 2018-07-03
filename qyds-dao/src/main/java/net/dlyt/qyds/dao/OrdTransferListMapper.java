package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.OrdTransferList;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdTransferListMapper {
    int deleteByPrimaryKey(String orderTransferId);

    int insert(OrdTransferList record);

    int insertSelective(OrdTransferList record);

    OrdTransferList selectByPrimaryKey(String orderTransferId);

    int updateByPrimaryKeySelective(OrdTransferList record);

    int updateByPrimaryKey(OrdTransferList record);
}