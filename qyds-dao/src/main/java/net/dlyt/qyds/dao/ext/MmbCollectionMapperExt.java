package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.ext.MmbCollectionExt;
import net.dlyt.qyds.common.form.MmbCollectionForm;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * Created by YiLian on 16/8/2.
 */
@Repository
public interface MmbCollectionMapperExt {
    /**
     * 查询重复收藏对象
     *
     * @param form memberId:会员ID
     *             type:收藏类型(10.商品，20.店铺)
     *             objectId:收藏对象ID
     * @return
     */
    MmbCollectionExt selectSameCollection(MmbCollectionForm form);

    /**
     * 查询收藏列表
     *
     * @param form memberId:会员ID
     *             type:收藏类型(10.商品，20.店铺)
     *             lastUpdateTime:分页标记(最后一条修改时间)
     * @return
     */
    List<MmbCollectionExt> queryList(MmbCollectionForm form);

    /**
     * 查询收藏列表
     * PC段接口
     * @param form memberId:会员ID
     *             type:收藏类型(10.商品，20.店铺)
     *             lastUpdateTime:分页标记(最后一条修改时间)
     * @return
     */
    List<MmbCollectionExt> getListForPC(MmbCollectionForm form);

    /**
     * 查询收藏列表数量
     * PC段接口
     * @param form memberId:会员ID
     *             type:收藏类型(10.商品，20.店铺)
     *             lastUpdateTime:分页标记(最后一条修改时间)
     * @return
     */
    int getCountForPC(MmbCollectionForm form);

    /**
     * 查询用户收藏库存情况
     *
     * @param memberId
     * @return
     */
    List<HashMap> queryCollectionNewCount(String memberId);

    /**
     * 查询商品库存情况
     *
     * @param goodsId
     * @return
     */
    int queryMinCountOfGoods(String goodsId);
}
