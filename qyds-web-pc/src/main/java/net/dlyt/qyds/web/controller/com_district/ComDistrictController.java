package net.dlyt.qyds.web.controller.com_district;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.service.ComDistrictService;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by wy on 2016/7/18.
 */
@Controller
@RequestMapping("/com_discrict")
public class ComDistrictController {

    @Resource
    private ComDistrictService comDistrictService;

    /**
     * 初始省份地址取得
     */
    @RequestMapping("getProvinces")
    public
    @ResponseBody
    JSONObject getProvinces() {
        return comDistrictService.selectByParentId("0");
    }

    /**
     * 下一级地址列表取得
     *
     * @param parentId 上级地址编码
     * @return 下一级地址列表
     */
    @RequestMapping("getSubAddresses")
    public
    @ResponseBody
    JSONObject getSubAddresses(String parentId) {
        if (StringUtils.isEmpty(parentId)) {
            JSONObject json = new JSONObject();
            json.put("resultCode", Constants.FAIL);
        }
        return comDistrictService.selectByParentId(parentId);
    }
}
