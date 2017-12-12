package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.GdsSku;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GdsSkuMapperExt {

    /**
     * 根据商品ID获取SKU列表
     * @param goodsId
     * @return
     */
    List<GdsSku> selectByGoodsId(String goodsId);

    /**
     * 根据goodsId更新
     * @param gdsSku
     */
    void updateByGoodsId(GdsSku gdsSku);

}