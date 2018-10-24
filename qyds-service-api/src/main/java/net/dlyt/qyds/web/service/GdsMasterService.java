package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.GdsColoreimage;
import net.dlyt.qyds.common.dto.GdsDetail;
import net.dlyt.qyds.common.dto.GdsMaster;
import net.dlyt.qyds.common.dto.GdsMasterExt;
import net.dlyt.qyds.common.form.GdsMasterForm;
import net.dlyt.qyds.common.form.SkuForm;

import java.util.List;
import java.util.Map;

/**
 * Created by congkeyan on 2016/7/19.
 */
public interface GdsMasterService {

    //获取所有商品列表
    JSONObject getAllList();

    //获取商品品牌列表
    JSONObject selectAll(GdsMasterForm form);

    //保存主表和详细表
    JSONObject save(String data, Map<String, Object> userMap);

    //保存排序数据
    JSONObject sortSave(String data);

    //保存安全库存数据
    JSONObject storeSave(String data);

    // 通过ID获取数据
    JSONObject edit(String goodsId);

    //删除商品信息根据ID
    JSONObject delete(String goodsId);

    //发布商品根据ID
    JSONObject publicGoods(String goodsId, Map<String, Object> userMap);

    //根据IDsku数据获取
    JSONObject skuEdit(String goodsId);

    //根据ID下架商品
    JSONObject unsellGoods(String goodsId, Map<String, Object> userMap);

    //选择已经上架的sku
    JSONObject selectOnsellSku(SkuForm form);

    //全選查出來的sku
//    JSONObject selectOnsellSkuForAll(SkuForm form);

    //*******************************API接口部分************************************//

    /**
     * 商品列表的获取方法
     *
     * @param data
     * 包含两个参数 1通过主key获得商品列表
     *            2通过分类获得商品列表
     *            3通过品牌获得商品列表
     *
     *            4必须传递的参数 updateTime 分页用 第一次不传递
     *
     * @return
     */
    JSONObject getProductList(String data);

    List<GdsMasterExt> resetGoodsList(List<GdsMasterExt> list,String memberId);

    /**
     * 商品详细的获取方法
     *
     * @param data
     * 包含两个参数 商品ID
     *
     * @return
     */
    JSONObject getProductDetail(String data);

    //获取猜你喜欢和搭配推荐列表
    JSONObject getMatingListAndRecommendList(String data);

    // 获取活动商品列表
    JSONObject selectAllByActId(GdsMasterForm form);

    // 根据商品ID获取商品信息
    JSONObject getGoodsByGoodsId(String goodsId);

    // 通过ID检测上架商品是否配置SKU
    JSONObject checkSku(String data);

    //根据,分割的字符串查询商品列表
    JSONObject getProductListBySequre(String data);

    //根据活动ID获取商品列表
    JSONObject getProductListByActivityId(String data);
    //根据活动IDs获取商品列表
    JSONObject getProductListByActivityIds(String data);

    JSONObject getSkuColorList(SkuForm form);

    //获取商品品牌列表
    JSONObject selectAllByGoodsTypeId(GdsMasterForm form);

    //商品预约
    JSONObject goodsOrder(String data);

    //活动种类列表获取
    JSONObject activityTypeListService(String memberId);
    //活动种类列表获取
    JSONObject activitysListService(String memberId);

    //商品预约检索
    JSONObject getGoodsOrderList(GdsMasterForm form);

    JSONObject getGiftDetailByCode(String data);

    /**
     * 获取秒杀活动商品列表
     * @param data
     * @return
     */
    JSONObject getSecKillProductList(String data);

    /**
     * 获取秒杀活动时间段
     * @param data
     * @return
     */
    JSONObject getSecKillActivityTimes(String data);
}
