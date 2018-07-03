package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.PrizeDrawConfig;
import net.dlyt.qyds.common.dto.PrizeDrawOppo;
import net.dlyt.qyds.common.dto.ext.PrizeDrawExt;
import net.dlyt.qyds.common.dto.ext.PrizeDrawOppoExt;
import net.dlyt.qyds.common.dto.ext.PrizeGoodsExt;

import java.math.BigDecimal;

/**
 * Created by cjk on 2016/12/15.
 */
public interface PrizeDrawService {

    JSONObject getList(PrizeDrawExt form);

    JSONObject selectPrizeList();

    JSONObject delete(PrizeDrawExt form);

    JSONObject edit(PrizeDrawExt form);

    JSONObject getDetail(PrizeDrawExt form);

    JSONObject prizeDraw(PrizeDrawExt form);

    JSONObject setValid(PrizeDrawExt form);

    JSONObject setInvalid(PrizeDrawExt form);

    JSONObject getPrizeGoodsList(PrizeGoodsExt form);

    JSONObject deletePrizeGoods(PrizeGoodsExt form);

    JSONObject addPrizeGoods(PrizeGoodsExt form);

    JSONObject editPrizeGoods(PrizeGoodsExt form);

    JSONObject getPrizeOppList(PrizeDrawOppoExt form);

    JSONObject sendPrizeGoods(PrizeDrawOppoExt form);

    JSONObject getPrizeConfig(PrizeDrawConfig form);

    JSONObject updatePrizeConfig(PrizeDrawConfig form);

    /**
     * 添加（兑换）抽奖机会
     * <p>
     * 1、兑换只能指定单一兑换
     * 2、订单满赠及注册赠送都是根据配置查询获得活动结果(可能多条)
     *
     * @param memberId 用户ID
     * @param prizeDrawId 活动ID
     * @param type 操作类型 PrizeDrawOppoType
     * @param orderAmount 订单金额
     * @return
     */
    JSONObject addPrizeDrawOppo(String memberId, String prizeDrawId, String type, BigDecimal orderAmount);

    JSONObject getWinningList(PrizeDrawOppoExt form);

    JSONObject getPrizeDrawInfo(String memberId, String prizeDrawId);

    JSONObject getMemberPrizeList(PrizeDrawOppoExt form);
}
