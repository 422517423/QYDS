package net.dlyt.qyds.web.controller.mmb_point_record;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.form.MmbPointRecordForm;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.service.MmbPointRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by YiLian on 16/7/28.
 */
@Controller
@RequestMapping("/mmb_point_record")
public class MmbPointRecordController {

    @Autowired
    private MmbPointRecordService mmbPointRecordService;


    /**
     * 取得会员积分履历列表(有分页)
     *
     * @param data memberId:会员ID
     *             currentPage:当前页数,从0开始
     * @return
     */
    @RequestMapping("getList")
    public
    @ResponseBody
    JSONObject getList(String data) {
        JSONObject json = new JSONObject();
        try {
            MmbPointRecordForm form = (MmbPointRecordForm) JSON.parseObject(data, MmbPointRecordForm.class);
            json = mmbPointRecordService.getRecordList(form);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }


    /**
     * 会员积分记录添加
     *
     * @param data memberId:会员ID
     *             ruleId:积分规则ID(累计)
     *             cash:实际现金数额
     *             worthId:积分规则ID(消费)
     *             worthCash:积分抵值金额
     *             worthPoint:抵值积分
     *             scoreSource:积分来源(订单号)
     *             exchangeId:积分规则ID(换购)
     *             exchangePoint:换购积分
     *             updateUserId: 如果是手工添加需要传值,默认为会员自己
     * @return
     */
    @RequestMapping("add")
    public
    @ResponseBody
    JSONObject add(String data) {
        JSONObject json = new JSONObject();
        try {

            MmbPointRecordForm form = (MmbPointRecordForm) JSON.parseObject(data, MmbPointRecordForm.class);
            json = mmbPointRecordService.add(form);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }


}
