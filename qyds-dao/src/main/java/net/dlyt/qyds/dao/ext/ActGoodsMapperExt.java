package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.ActGoods;
import net.dlyt.qyds.common.dto.ActTempParam;
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
public interface ActGoodsMapperExt {

    void deleteByActivityId(ActGoods form);

    List<ActGoods> selectByActivityId(ActGoods form);

    List<ActGoodsForm> selectGoodsTypeByActivityId(ActGoods goods);

    List<ActGoodsForm> selectGoodsBrandByActivityId(ActGoods goods);

    List<ActGoodsForm> selectGoodsByActivityId(ActGoods goods);

    List<ActGoodsForm> selectSkuByActivityId(ActGoods goods);

    List<ActGoodsForm> selectSkuByActivityIdUp(ActGoods goods);

    void deleteByActGoodsId(String actGoodsId);

    List<HashMap<String,String>> selectAllGoodsMap();

    List<ActGoods> selectActGoodsInfo(ActGoods form);

    /**
     * 根据订单ID查询秒杀活动商品信息
     * @param orderId
     * @return
     */
    List<ActGoods> selectSecKillGoodsByOrderId(String orderId);

    // 根据activityId查询商品List
    List<ActGoods> selectSecGoodsByActivity(ActMasterForm form);

    int countSecGoodsByActivity(ActMasterForm actForm);

    ActGoods selectSecGoodsInfo(ActGoods goodsInfo);

    List<Map> selectSKUByYearAndSeason(ActMasterForm form);

    List<Map> selectSKUByErpBrand(ActMasterForm form);

    List<Map> selectSKUByLineCode(ActMasterForm form);

}
