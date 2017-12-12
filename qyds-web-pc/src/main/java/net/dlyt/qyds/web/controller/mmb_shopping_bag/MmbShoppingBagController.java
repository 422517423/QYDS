package net.dlyt.qyds.web.controller.mmb_shopping_bag;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.form.MmbShoppingBagForm;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.service.MmbShoppingBagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by YiLian on 16/8/2.
 */
@Controller
@RequestMapping("/mmb_shopping_bag")
public class MmbShoppingBagController {

    @Autowired
    private MmbShoppingBagService mmbShoppingBagService;

    /**
     * 购物袋商品一览(有分页)
     *
     * @param data memberId:会员ID
     *             lastUpdateTime:最后一条更新时间(分页用)
     * @return
     */
    @RequestMapping("getList")
    @ResponseBody
    public JSONObject getList(String data) {

        JSONObject json = new JSONObject();
        try {

            MmbShoppingBagForm form = (MmbShoppingBagForm) JSON.parseObject(data, MmbShoppingBagForm.class);

            json = mmbShoppingBagService.getList(form);

        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 添加商品(单件商品)
     *
     * @param data 会员ID	memberId
     *             店铺ID	shopId
     *             商品类型	type
     *             商品代码	goodsCode
     *             商品名称	goodsName
     *             商品SKU	sku
     *             件数	quantity
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    public JSONObject add(String data) {

        JSONObject json = new JSONObject();
        try {

            MmbShoppingBagForm form = (MmbShoppingBagForm) JSON.parseObject(data, MmbShoppingBagForm.class);

            json = mmbShoppingBagService.add(form);

        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;


    }

    /**
     * 改变单件商品数量
     *
     * @param data 购物袋编号 bagNo
     *             会员ID	memberId
     *             件数	quantity
     * @return
     */
    @RequestMapping("changeQuantity")
    @ResponseBody
    public JSONObject changeQuantity(String data) {

        JSONObject json = new JSONObject();
        try {

            MmbShoppingBagForm form = (MmbShoppingBagForm) JSON.parseObject(data, MmbShoppingBagForm.class);

            json = mmbShoppingBagService.changeQuantity(form);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 改变单件商品活动信息
     *
     * @param data 购物袋编号 bagNo
     *             会员ID	memberId
     *             活动ID	actGoodsId
     * @return
     */
    @RequestMapping("changeActivity")
    @ResponseBody
    public JSONObject changeActivity(String data) {

        JSONObject json = new JSONObject();
        try {

            MmbShoppingBagForm form = (MmbShoppingBagForm) JSON.parseObject(data, MmbShoppingBagForm.class);

            json = mmbShoppingBagService.changeActivity(form);

        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 删除商品
     *
     * @param data 购物袋编号 bagNo
     *             会员ID	memberId
     *             批量删除 delBags
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public JSONObject delete(String data) {

        JSONObject json = new JSONObject();
        try {

            MmbShoppingBagForm form = (MmbShoppingBagForm) JSON.parseObject(data, MmbShoppingBagForm.class);

            json = mmbShoppingBagService.delete(form);

        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 购物袋商品数量
     *
     * @param data memberId:会员ID
     *             lastUpdateTime:最后一条更新时间(分页用)
     * @return
     */
    @RequestMapping("getCount")
    @ResponseBody
    public JSONObject getCount(String data) {
        return mmbShoppingBagService.getCount(data);

    }
}
