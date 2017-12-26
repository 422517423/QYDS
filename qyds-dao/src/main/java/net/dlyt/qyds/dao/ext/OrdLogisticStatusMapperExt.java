package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.ActGoods;
import net.dlyt.qyds.common.dto.OrdLogisticStatus;
import net.dlyt.qyds.common.form.ActGoodsForm;
import net.dlyt.qyds.common.form.ActMasterForm;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cjk on 16/8/4.
 */
@Repository
public interface OrdLogisticStatusMapperExt {

    /**
     * 查询给erp发送失败的物流信息
     * @return
     */
    List<OrdLogisticStatus>  selectSendFail();

}
