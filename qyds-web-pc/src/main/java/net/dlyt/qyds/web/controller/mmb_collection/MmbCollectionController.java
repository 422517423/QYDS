package net.dlyt.qyds.web.controller.mmb_collection;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.form.MmbCollectionForm;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.service.MmbCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by YiLian on 16/8/2.
 */
@Controller
@RequestMapping("/mmb_collection")
public class MmbCollectionController {

    @Autowired
    private MmbCollectionService mmbCollectionService;

    /**
     * 查询会员的收藏一览
     *
     * @param data memberId:会员ID
     *             type:收藏类型(10.商品，20.店铺)
     *             lastUpdateTime:分页标记(最后一条修改时间)
     * @return
     */
    @RequestMapping("getList")
    @ResponseBody
    public JSONObject getList(String data) {

        JSONObject json = new JSONObject();
        try {
            MmbCollectionForm form = (MmbCollectionForm) JSON.parseObject(data, MmbCollectionForm.class);
            json = mmbCollectionService.getList(form);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 查询会员的收藏一览
     *
     * @param data memberId:会员ID
     *             type:收藏类型(10.商品，20.店铺)
     *             lastUpdateTime:分页标记(最后一条修改时间)
     * @return
     */
    @RequestMapping("getPhoneList")
    @ResponseBody
    public JSONObject getPhoneList(String data) {

        JSONObject json = new JSONObject();
        try {
            MmbCollectionForm form = (MmbCollectionForm) JSON.parseObject(data, MmbCollectionForm.class);
            json = mmbCollectionService.getPhoneList(form);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 添加收藏
     *
     * @param data memberId:会员ID
     *             type:收藏类型(10.商品，20.店铺)
     *             objectId:收藏对象ID
     *             name:名称
     *             url:收藏URL
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    public JSONObject add(String data) {

        JSONObject json = new JSONObject();
        try {
            MmbCollectionForm form = (MmbCollectionForm) JSON.parseObject(data, MmbCollectionForm.class);
            json = mmbCollectionService.add(form);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 删除收藏
     *
     * @param data memberId:会员ID
     *             collectNo:收藏编号
     *             collections:批量删除
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public JSONObject delete(String data) {
        JSONObject json = new JSONObject();
        try {
            MmbCollectionForm form = (MmbCollectionForm) JSON.parseObject(data, MmbCollectionForm.class);
            json = mmbCollectionService.delete(form);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 获取心愿单数量
     *
     * @param data memberId:会员ID
     *             collectNo:收藏编号
     *             collections:批量删除
     * @return
     */
    @RequestMapping("getCount")
    @ResponseBody
    public JSONObject getCountForPC(String data) {
        return mmbCollectionService.getCountForPC(data);
    }


    /**
     * 获取心愿单库存情况[库存报警]
     *
     * @param data memberId:会员ID
     * @return
     */
    @RequestMapping("getInventoryAlarming")
    @ResponseBody
    public JSONObject getInventoryAlarming(String data) {
        return mmbCollectionService.getInventoryAlarming(data);
    }
}
