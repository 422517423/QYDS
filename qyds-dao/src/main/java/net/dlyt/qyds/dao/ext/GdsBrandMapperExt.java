package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.GdsBrand;
import net.dlyt.qyds.common.dto.GdsBrandExt;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GdsBrandMapperExt {

   /**
    * 获取商品列表
    * @return
    */
   List<GdsBrandExt> selectAll(GdsBrandExt ext);

   /**
    * 获取商品列表(没有翻页,没有检索条件)
    * @return
    */
   List<GdsBrandExt> getAllList(GdsBrandExt ext);

   /**
    * 获取商品列表不分页的总数
    * @return
    */
   int getAllDataCount(GdsBrandExt ext);

   /**
    * 删除全部ERP品牌数据
    * @return
    */
   void deleteAllErp();

   /**
    * 删除全部ERP品牌数据
    * @return
    */
   void updateErpName(GdsBrand ext);

}