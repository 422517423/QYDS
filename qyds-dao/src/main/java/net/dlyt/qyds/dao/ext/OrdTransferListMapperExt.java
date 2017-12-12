package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.OrdTransferList;
import net.dlyt.qyds.common.dto.ext.OrdTransferListExt;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdTransferListMapperExt {

    OrdTransferList getBySubOrderId(String subOrderId);

    List<OrdTransferListExt> getDispatchList(OrdTransferListExt form);

    int getDispatchListCount(OrdTransferListExt form);

    List<OrdTransferList> selectSendFail();

    OrdTransferListExt getDetailById(String orderTransferId);

    // TODO: 2017/12/6 获取调货订单信息(包括发货/收货门店信息) 
    OrdTransferListExt selectByPrimaryKey(String orderTransferId);
}