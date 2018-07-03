package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.OrdHistory;
import net.dlyt.qyds.common.dto.ext.OrdHistoryExt;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdHistoryMapperExt {

    int insertSelective(OrdHistory record);

    List<OrdHistoryExt> selectByOrderId(String orderId);
}