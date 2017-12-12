package net.dlyt.qyds.web.controller.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpPrepayIdResult;
import net.dlyt.qyds.common.dto.SysRole;
import net.dlyt.qyds.common.dto.SysRoleMenuKey;
import net.dlyt.qyds.common.dto.SysUser;
import net.dlyt.qyds.common.form.WapWxForm;
import net.dlyt.qyds.web.common.ClientIPUtil;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.service.OrdMasterService;
import net.dlyt.qyds.web.service.SysRoleService;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import net.dlyt.qyds.web.service.exception.ExceptionErrorParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by C_Nagai on 2016/6/25.
 */
@Controller
@RequestMapping("/wechat")
public class WeiXinController {

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private OrdMasterService ordMasterService;
    /**
     * 微信统一下单接口
     *
     * @param data
     * @param request
     * @return
     */
    @RequestMapping("getWxPayInfo")
    @ResponseBody
    public JSONObject getWxPayInfo(String data, HttpServletRequest request) {
        JSONObject json = new JSONObject();
        try {
            Map<String, String> parameters = (Map<String, String>) JSON.parseObject(data, Map.class);

            //如果是现金购买优惠劵不进行秒杀check
            if(!parameters.get("product_id").startsWith("COUPON")){
                ordMasterService.checkSecActivityOrderInfo(null, parameters.get("product_id"));
            }
            parameters.put("spbill_create_ip",
                    ClientIPUtil.getRemoteHost(request));
            // 20171208 测试
            //parameters.put("notify_url", "https://www.dealuna.com/qyds-wx-front/wechat/asyncWechatPay.json");
            parameters.put("notify_url", "https://115.29.146.180/qyds-wx-front/wechat/asyncWechatPay.json");
            WxMpPrepayIdResult result = wxMpService.getPrepayId(parameters);
            // 返回结果code
            json.put("results", result);
            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            // 没取到数据或取到错误数据。
            json.put("resultCode", e.resultCd);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            // 其他系统异常。
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }
}
