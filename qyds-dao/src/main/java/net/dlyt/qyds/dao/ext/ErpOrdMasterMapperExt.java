package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.OrdMasterExt;
import net.dlyt.qyds.common.dto.ext.ErpOrderMasterExt;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wenxuechao on 16/7/23.
 */

@Repository
public interface ErpOrdMasterMapperExt {

    /**
     * 获取某个用户的全部订单个数
     * @param memberId
     * @return
     */
    int getAllCountByMemberId(String memberId);

    List<ErpOrderMasterExt> getAllOrderByMemberId(OrdMasterExt memberId);

    List<ErpOrderMasterExt> getAllOrderByMemberIdForWeb(String memberId);
}
