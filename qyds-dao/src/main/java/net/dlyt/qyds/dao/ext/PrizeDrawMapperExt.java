package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.PrizeDraw;
import net.dlyt.qyds.common.dto.ext.PrizeDrawExt;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by cjk on 2016/12/15.
 */
@Repository
public interface PrizeDrawMapperExt {
    
    List<PrizeDrawExt> select(PrizeDrawExt form);

    int selectCount(PrizeDrawExt form);

    int checkExistByPrizeDrawName(String prizeDrawName);

    List<PrizeDraw> selectPrizeList(PrizeDrawExt prizeDrawExt);
}
