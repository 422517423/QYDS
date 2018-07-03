package net.dlyt.qyds.web.controller.com_code;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ComCode;
import net.dlyt.qyds.common.dto.ComCodeExt;
import net.dlyt.qyds.common.dto.ComCodeKey;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.common.DataUtils;
import net.dlyt.qyds.web.service.ComCodeService;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by wy on 2016/7/18.
 */
@Controller
@RequestMapping("/com_code")
public class ComCodeController {

    @Resource
    private ComCodeService comCodeService;

    /*初始化*/
    @RequestMapping("getList")
    public
    @ResponseBody
    JSONObject getList() {
        return comCodeService.selectAll();
    }

    /*详细页面根据ID取值*/
    @RequestMapping("edit")
    public
    @ResponseBody
    JSONObject edit(@RequestParam(required = false) String code, String value) {
        return comCodeService.edit(code,value);
    }

//    /*编辑和新建保存*/
//    @RequestMapping("save")
//    public
//    @ResponseBody
//    JSONObject save(String data) {
//        return comCodeService.save(data);
//    }
    /*删除*/
    @RequestMapping("delete")
    public
    @ResponseBody
    JSONObject delete(String data) {
        return comCodeService.delete(data);
    }
}
