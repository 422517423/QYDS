package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.OrdList;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wenxuechao on 16/7/30.
 */
@Repository
public interface OrdListMapperExt {

    List<OrdList> selectOrderGoodsInfo(String orderId);

    List<OrdList> selectOrdListByOrderId(String ord_ids);

    /**
     * 插入订单明细表
     * @param ordList
     * @return
     */
    int insertSelective(OrdList ordList);
}
