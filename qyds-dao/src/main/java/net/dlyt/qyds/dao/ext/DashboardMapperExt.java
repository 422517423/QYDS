package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.form.EchartsForm;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by YiLian on 16/8/18.
 */
@Repository
public interface DashboardMapperExt {
    List<EchartsForm> queryMemberGroupByLevel();

    List<EchartsForm> queryMemberGroupByYearMonth();

    List<EchartsForm> queryOrderGroupByYearMonth();

    List<EchartsForm> queryGoodsOrderByQuantity();
}
