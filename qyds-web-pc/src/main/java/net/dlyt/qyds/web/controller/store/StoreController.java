package net.dlyt.qyds.web.controller.store;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ErpStore;
import net.dlyt.qyds.common.dto.MmbMaster;
import net.dlyt.qyds.common.dto.ext.ErpStoreExt;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.service.BnkMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @功能 微信端订单模块调用接口
 * Created by wenxuechao on 16/7/26.
 */
@Controller
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private BnkMasterService bnkMasterService;

    /**
     * 获取门店信息
     *
     * @return
     */
    @RequestMapping("getOrgList")
    public
    @ResponseBody
    JSONObject getOrgList(String data) {
        return bnkMasterService.getOrgList(data);
    }


    /**
     * 门店地址列表取得
     *
     * @param data
     * @return
     */
    @RequestMapping("getOrgAddressList")
    @ResponseBody
    public JSONObject getAddressList(String data) {
        JSONObject json = new JSONObject();
        try {
            ErpStore store = (ErpStore) JSON.parseObject(data, ErpStore.class);
            json = bnkMasterService.getOrgAddressList(store);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;

    }
}
