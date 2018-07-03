package net.dlyt.qyds.web.controller.mmb_level_rule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.MmbLevelRule;
import net.dlyt.qyds.common.dto.ext.MmbLevelRuleExt;
import net.dlyt.qyds.common.dto.ext.MmbMasterExt;
import net.dlyt.qyds.common.form.ActMasterForm;
import net.dlyt.qyds.common.form.MmbGoodsExtForm;
import net.dlyt.qyds.common.form.MmbGroupForm;
import net.dlyt.qyds.common.form.MmbLevelRuleForm;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.context.PamsDataContext;
import net.dlyt.qyds.web.service.MmbLevelRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by C_Nagai on 2016/7/27.
 */
@Controller
@RequestMapping("/mmb_level_rule")
public class MmbLevelRuleController {

    @Autowired
    private MmbLevelRuleService mmbLevelRuleService;

    @RequestMapping("getList")
    public
    @ResponseBody
    JSONObject getList(MmbLevelRuleForm form) {

        return mmbLevelRuleService.getList(form);
    }

    @RequestMapping("getDetail")
    @ResponseBody
    public JSONObject getDetail(String data) {

        MmbLevelRuleForm form = (MmbLevelRuleForm) JSON.parseObject(data, MmbLevelRuleForm.class);

        return mmbLevelRuleService.getDetail(form);
    }

    /**
     * 会员等级规则添加
     *
     * @param data
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    public JSONObject add(String data) {

        MmbLevelRuleForm form = (MmbLevelRuleForm) JSON.parseObject(data, MmbLevelRuleForm.class);
        String userId = (String) PamsDataContext.get("loginId");
        return mmbLevelRuleService.add(form, userId);
    }


    /**
     * 会员等级规则编辑
     *
     * @param data
     * @return
     */
    @RequestMapping("edit")
    @ResponseBody
    public JSONObject edit(String data) {

        MmbLevelRuleForm form = (MmbLevelRuleForm) JSON.parseObject(data, MmbLevelRuleForm.class);
        String userId = (String) PamsDataContext.get("loginId");
        return mmbLevelRuleService.edit(form, userId);
    }


    /**
     * 会员等级规则删除
     *
     * @param data
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public JSONObject delete(String data) {

        MmbLevelRuleForm form = (MmbLevelRuleForm) JSON.parseObject(data, MmbLevelRuleForm.class);
        String userId = (String) PamsDataContext.get("loginId");
        return mmbLevelRuleService.delete(form, userId);
    }

    /**
     * 添加活动相关的商品，根据goodsType的不同
     *
     * @param data
     * @return
     */
    @RequestMapping("addGoods")
    @ResponseBody
    public JSONObject addGoods(String data) {
        MmbGoodsExtForm form = (MmbGoodsExtForm) JSON.parseObject(data, MmbGoodsExtForm.class);
        return mmbLevelRuleService.addGoods(form);
    }

    @RequestMapping("getGoodsList")
    @ResponseBody
    public JSONObject getGoodsList(String data) {
        MmbGoodsExtForm form = (MmbGoodsExtForm) JSON.parseObject(data, MmbGoodsExtForm.class);
        return mmbLevelRuleService.getGoodsList(form.getMemberLevelId());
    }

    /**
     * 删除活动相关的商品，根据goodsType的不同
     *
     * @param data
     * @return
     */
    @RequestMapping("deleteGoods")
    @ResponseBody
    public JSONObject deleteGoods(String data) {
        MmbGoodsExtForm form = (MmbGoodsExtForm) JSON.parseObject(data, MmbGoodsExtForm.class);
        return mmbLevelRuleService.deleteGoods(form);
    }

}