package net.dlyt.qyds.web.controller.gds_color;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.*;
import net.dlyt.qyds.common.form.GdsMasterForm;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.common.DataUtils;
import net.dlyt.qyds.web.context.PamsDataContext;
import net.dlyt.qyds.web.service.GdsBrandService;
import net.dlyt.qyds.web.service.GdsColorService;
import net.dlyt.qyds.web.service.GdsMasterService;
import net.dlyt.qyds.web.service.GdsSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by cky on 2016/7/18.
 */
@Controller
@RequestMapping("/gds_color")
public class GdsColorController {

    @Autowired
    private GdsColorService gdsColorService;
    /**
     * 保存数据到颜色表中
     * @param data
     * @return
     */
    @RequestMapping("save")
    public @ResponseBody
    JSONObject save(String data){
        return gdsColorService.save(data, PamsDataContext.get());
    }

    /**
     * 通过ID获取数据
     * @param goodsColoreId
     * @return
     */
    @RequestMapping("edit")
    public @ResponseBody JSONObject edit(@RequestParam(required = false)String goodsColoreId){
        return gdsColorService.edit(goodsColoreId);
    }



}
