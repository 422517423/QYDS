package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.ext.PrizeDrawOppoExt;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * Created by YiLian on 2017/1/4.
 */
@Repository
public interface PrizeDrawOppoMapperExt {

    List<PrizeDrawOppoExt> select(PrizeDrawOppoExt form);

    int selectCount(PrizeDrawOppoExt form);

    List<HashMap> selectMemberPrizeList(PrizeDrawOppoExt form);

    int selectMemberPrizeListCount(PrizeDrawOppoExt form);
}
