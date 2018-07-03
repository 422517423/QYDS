package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.MmbShoppingBag;
import net.dlyt.qyds.common.dto.ext.MmbShoppingBagExt;
import net.dlyt.qyds.common.dto.ext.MmbShoppingSKuExt;
import net.dlyt.qyds.common.form.MmbShoppingBagForm;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by YiLian on 16/8/2.
 */
@Repository
public interface MmbShoppingBagMapperExt {

    /**
     * 查询购物车一览
     *
     * @param form
     * @return
     */
    List<MmbShoppingBagExt> queryList(MmbShoppingBagForm form);


    /**
     * 查询商品关联SKU信息一览
     *
     * @param bagNo 购物车ID
     * @return
     */
    List<MmbShoppingSKuExt> querySKUList(String bagNo);

    /**
     * 查询同样的商品信息
     * 同样商品ID的购物车Id列表
     *
     * @param form 会员ID	memberId
     *             商品ID	goodsId
     * @return
     */
    List<MmbShoppingBagExt> selectSameShoppingIdGoods(MmbShoppingBagForm form);

    /**
     * 根据购物车编码数组获取数据
     * @param bagNoArray
     * @return
     */
    List<MmbShoppingBagExt> queryListByBagNo(String bagNoArray);

    /**
     * 查询购物车数量
     *
     * @param form
     * @return
     */
    List<MmbShoppingBagExt> getCountForPc(MmbShoppingBagForm form);
}
