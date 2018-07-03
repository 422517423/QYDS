package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.form.MmbShoppingBagForm;

import java.util.List;

/**
 * Created by YiLian on 16/8/2.
 */
public interface MmbShoppingBagService {

    /**
     * 购物袋商品一览(有分页)
     *
     * @param form memberId:会员ID
     *             lastUpdateTime:最后一条更新时间(分页用)
     * @return
     */
    JSONObject getList(MmbShoppingBagForm form);

    /**
     * 添加商品(单件商品)
     *
     * @param form 会员ID	memberId
     *             店铺ID	shopId
     *             商品类型	type
     *             商品代码	goodsCode
     *             商品名称	goodsName
     *             商品SKU	sku
     *             件数	quantity
     * @return
     */
    JSONObject add(MmbShoppingBagForm form);

    /**
     * 改变单件商品数量
     *
     * @param form 购物袋编号 bagNo
     *             会员ID	memberId
     *             件数	quantity
     * @return
     */
    JSONObject changeQuantity(MmbShoppingBagForm form);

    /**
     * 改变单件商品活动信息
     *
     * @param form 购物袋编号 bagNo
     *             会员ID	memberId
     *             活动ID	actGoodsId
     * @return
     */
    JSONObject changeActivity(MmbShoppingBagForm form);

    /**
     * 删除商品
     *
     * @param form 购物袋编号 bagNo
     *             会员ID	memberId
     *             批量删除 bags
     * @return
     */
    JSONObject delete(MmbShoppingBagForm form);

    /**
     * 购物袋商品数量
     *
     * @param data memberId:会员ID
     *             lastUpdateTime:最后一条更新时间(分页用)
     * @return
     */
    JSONObject getCount(String data);

}
