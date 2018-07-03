package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.form.MmbCollectionForm;

import java.util.List;

/**
 * Created by YiLian on 16/8/2.
 */
public interface MmbCollectionService {

    /**
     * 查询会员的收藏一览
     *
     * @param form memberId:会员ID
     *             type:收藏类型(10.商品，20.店铺)
     *             lastUpdatetime:分页标记(最后一条修改时间)
     * @return
     */
    JSONObject getList(MmbCollectionForm form);

    /**
     * 查询会员的收藏一览
     *
     * @param form memberId:会员ID
     *             type:收藏类型(10.商品，20.店铺)
     *             lastUpdatetime:分页标记(最后一条修改时间)
     * @return
     */
    JSONObject getPhoneList(MmbCollectionForm form);

    /**
     * 添加收藏
     *
     * @param form memberId:会员ID
     *             type:收藏类型(10.商品，20.店铺)
     *             objectId:收藏对象ID
     *             name:名称
     *             url:收藏URL
     * @return
     */
    JSONObject add(MmbCollectionForm form);

    /**
     * 删除收藏
     *
     * @param form memberId:会员ID
     *             collectNo:收藏编号
     *             collections:批量删除
     * @return
     */
    JSONObject delete(MmbCollectionForm form);

    /**
     * 获取心愿单数量
     *
     * @param data memberId:会员ID
     *             type:收藏类型(10.商品，20.店铺)
     *             lastUpdatetime:分页标记(最后一条修改时间)
     * @return
     */
    JSONObject getCountForPC(String data);

    /**
     * 获取心愿单库存情况[库存报警]
     *
     * @param data memberId:会员ID
     * @return
     */
    JSONObject getInventoryAlarming(String data);
}
