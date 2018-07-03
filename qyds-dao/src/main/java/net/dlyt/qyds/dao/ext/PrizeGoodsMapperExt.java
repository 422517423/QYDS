package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.PrizeGoods;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by cjk on 2016/12/15.
 */
@Repository
public interface PrizeGoodsMapperExt {

    List<PrizeGoods> selectByPrizeDrawId(String prizeDrawId);
}
