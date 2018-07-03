package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.GdsColoreimage;
import net.dlyt.qyds.common.dto.GdsMaster;
import net.dlyt.qyds.common.dto.GdsMasterExt;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface GdsMasterMapperExt {

   /**
    * 获取商品列表
    * @return
    */
   List<GdsMasterExt> getAllList(GdsMasterExt ext);

   /**
    * 获取商品列表
    * @return
    */
   List<GdsMasterExt> selectAll(GdsMasterExt ext);

   /**
    * 获取商品列表不分页的总数
    * @return
    */
   int getAllDataCount(GdsMasterExt ext);

   /**
    * 根据商品ID获取sku信息
    * @param goodsId
    * @return
     */
   GdsMasterExt selectItemByKey(String goodsId);

   /**
    * 根据ID获取视图中的数据ERP商品用
    * @param goodsId
    * @return
     */
   GdsMasterExt selectViewByPrimaryKey(String goodsId);

    /**
     * 通过ERP商品code获取颜色code和颜色名称
     * @return
     */
   List<GdsColoreimage> selectColorCodeAndName(String erpGoodsCode);

   /**
    * 根据商品ID,商品SKUID获取商品信息
    * @param gdsMasterExt
    * @return
    */
   Map<String,Object> getGoodsInfo(GdsMasterExt gdsMasterExt);


   //*******************************API接口部分************************************//

   /**
    * 商品列表的获取方法
    *
    * @param gdsMasterExt
    * 包含两个参数 1通过主key获得商品列表
    *            2通过分类获得商品列表
    *            3通过品牌获得商品列表
    *
    *            4必须传递的参数 updateTime 分页用 第一次不传递
    *
    * @return
    */
   List<GdsMasterExt> selectProductsByParameter(GdsMasterExt gdsMasterExt);

   List<GdsMasterExt> selectProductsByParameterForSelectGift(GdsMasterExt gdsMasterExt);

   int selectProductsByParameterCount(GdsMasterExt gdsMasterExt);

   List<GdsMasterExt> selectProductsByParameterUp(GdsMasterExt gdsMasterExt);
   int selectProductsByParameterCountUp(GdsMasterExt gdsMasterExt);

   /**
    * 获取商品单品详细信息以及商品最大最小单价
    * @param gdsMaster
    * 其中包含最小单价minPrice
    *         最大单价maxPrice
    *         单价区间minAndMaxPrice
    * @return
     */
   GdsMasterExt selectGoodsMinAndMaxPrice(GdsMaster gdsMaster);

   /**
    * 获取商品套装详细信息以及商品最大最小单价
    * @param gdsMaster
    * 其中包含最小单价minPrice
    *         最大单价maxPrice
    *         单价区间minAndMaxPrice
    * @return
    */
   GdsMasterExt selectSuitMinAndMaxPrice(GdsMaster gdsMaster);

   //活动tag
   List<HashMap> selectActivityItems(String goodsId);

   //活动tag套餐
   List<HashMap> selectActivityItemsForSuit(String goodsId);

   //活动ID
   List<HashMap> selectActivityIds(String goodsId);

   //活动ID套餐
   List<HashMap> selectActivityIdsForSuit(String goodsId);

   //sku信息
   List<HashMap> selectSkuItems(String goodsId);

   //获取的颜色列表
   List<HashMap> getColorList(String goodsId);

   //获取的尺寸列表
   List<HashMap> getSizeList(String goodsId);

   // 获取SKU信息
   HashMap selectSKUInfo(String skuId);

   // 获取活动商品列表
   List<GdsMasterExt> selectAllByActId(GdsMasterExt gdsMasterExt);

   /**
    * 获取商品列表不分页的总数
    * @return
    */
   int getAllDataCountByActId(GdsMasterExt ext);

   /**
    * 根据商品ID,商品SKUID获取商品信息
    * @触发场合    非套装场合
    * @param gdsMasterExt
    * @return
    */
   Map<String,Object> getAllGoodsInfo(GdsMasterExt gdsMasterExt);

   /**
    * 根据商品ID,商品SKUID获取商品信息
    * @触发场合    套装场合
    * @param gdsMasterExt
    * @return
    */
   Map<String,Object> getSuitGoodsInfo(GdsMasterExt gdsMasterExt);

   /**
    * 根据商品ID获取商品信息
    * @param goodsId
    * @return
    */
   GdsMasterExt selectByPrimaryKey(String goodsId);

   // 根据cmsId获取商品列表
   List<GdsMasterExt> getMasterGoodsByCmsId(String cmsId);

   // 根据商品ID获取商品信息，商品ID可以为多个
   List<GdsMasterExt> getGoodsByGoodsId(String goodsId);

   // 电商单品设置SKU数量
   int checkSkuByGoods(GdsMaster gdsMaster);

   // ERP商品设置SKU数量
   int checkSkuByErp(GdsMaster gdsMaster);

   // 获取ERP发送失败的商品列表
   List<GdsMaster> selectSendFail();

   GdsMaster selectByCode(String goodsCode);

   GdsMaster getErpGoodsCodeByGoodsId(String goodsId);

   GdsMaster selectErpByCode(String goodsCode);

   //获取尺码的列表(三级)
   List<GdsMasterExt> getSizeCodeListByfirstErp(String firstGoodsTypeId);
   List<GdsMasterExt> getSizeCodeListByfirstBrandErp(String firstBrandGoodsTypeId);
   List<GdsMasterExt> getSizeCodeListByGoodsIdsErp(GdsMasterExt ext);
   List<GdsMasterExt> getSizeCodeListByErp(String goodsTypeId);

   /**
    * 获取商品列表
    * @return
    */
   List<GdsMasterExt> getGoodsListByGoodsCode(String goodsCodes);

   String getIdBySku(String sku);
}
