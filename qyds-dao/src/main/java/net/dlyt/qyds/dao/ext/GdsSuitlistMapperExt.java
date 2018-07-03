package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.GdsMasterExt;
import net.dlyt.qyds.common.dto.GdsSuitlist;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GdsSuitlistMapperExt {

   /**
    * 获取待选择商品列表
    * @return
    */
   List<GdsMasterExt> selectAllForSuitlist(GdsMasterExt ext);
   /**
    * 获取带选择商品列表不分页的总数
    * @return
    */
   int getAllDataCountForSuitlist(GdsMasterExt ext);


   /**
    * 获取已经选择的套装列表(分页)
    * @return
    */
   List<GdsMasterExt> selectAllByGoodsId(GdsMasterExt ext);

   /**
    * 获取已经选择的套装列表总数
    * @return
    */
   int getAllDataCountByGoodsId(GdsMasterExt ext);

   /**
    * 通过组成套装的商品ID和商品ID
    * @param gdsSuitlist
    * @return
     */
   GdsSuitlist selectByGoodsIdAndSuitId(GdsSuitlist gdsSuitlist);

   /**
    * 获取已经选择的套装列表(不分页)
    * @return
    */
   List<GdsSuitlist> selectByGoodsId(String goodsId);

    /**
     * 根据组成套装的商品ID 获取套装ID
     * @param goodsId
     * @return
     */
   List<GdsSuitlist> selectByGoodsIdSuit(String goodsId);

   /**
    * 根据商品ID删除套装信息
    * @param gdsSuitlist
     */
   void updateByGoodsId(GdsSuitlist gdsSuitlist);


   /**
    * 物理删除已经组成的套装商品
    * @param goodsId
     */
   void delete(String goodsId);


}