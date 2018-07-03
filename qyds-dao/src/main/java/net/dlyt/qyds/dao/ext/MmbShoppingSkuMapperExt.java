package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.MmbShoppingSku;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by YiLian on 16/8/4.
 */
@Repository
public interface MmbShoppingSkuMapperExt {

    /**
     * 根据购物车ID获取所有的商品SKU
     *
     * @param bagNo 购物车ID
     * @return
     */
    List<MmbShoppingSku> selectAllSkuByBagNo(String bagNo);

    /**
     * 删除购物车关联数据
     *
     * @param dto bagNo 购物车ID
     */
    void deleteByBagNo(MmbShoppingSku dto);
}
