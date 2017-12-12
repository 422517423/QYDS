package net.dlyt.qyds.web.controller.prize_draw;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import me.chanjar.weixin.common.util.StringUtils;
import net.dlyt.qyds.common.dto.ActMaster;
import net.dlyt.qyds.common.dto.PrizeDrawConfig;
import net.dlyt.qyds.common.dto.ext.PrizeDrawExt;
import net.dlyt.qyds.common.dto.ext.PrizeDrawOppoExt;
import net.dlyt.qyds.common.dto.ext.PrizeGoodsExt;
import net.dlyt.qyds.common.form.ActGoodsForm;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.context.PamsDataContext;
import net.dlyt.qyds.web.service.ActMasterService;
import net.dlyt.qyds.web.service.PrizeDrawService;
import net.dlyt.qyds.web.service.common.StringUtil;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/prize_draw")
public class PrizeDrawController {

    @Autowired
    PrizeDrawService prizeDrawService;

    @RequestMapping("getList")
    @ResponseBody
    public JSONObject getList(PrizeDrawExt form) {
        return prizeDrawService.getList(form);
    }

    @RequestMapping("getAllList")
    @ResponseBody
    public JSONObject selectPrizeList() {
        return prizeDrawService.selectPrizeList();
    }

    @RequestMapping("getDetail")
    @ResponseBody
    public JSONObject getDetail(String data) {
        PrizeDrawExt form = (PrizeDrawExt) JSON.parseObject(data, PrizeDrawExt.class);
        return prizeDrawService.getDetail(form);
    }

    @RequestMapping("delete")
    @ResponseBody
    public JSONObject delete(String data) {
        PrizeDrawExt form = (PrizeDrawExt) JSON.parseObject(data, PrizeDrawExt.class);
        form.setUpdateUserId((String) PamsDataContext.get("loginId"));
        return prizeDrawService.delete(form);
    }

    @RequestMapping("edit")
    @ResponseBody
    public JSONObject edit(String data) {
        PrizeDrawExt form = (PrizeDrawExt) JSON.parseObject(data, PrizeDrawExt.class);
        form.setUpdateUserId((String) PamsDataContext.get("loginId"));
        return prizeDrawService.edit(form);
    }

    @RequestMapping("setValid")
    @ResponseBody
    public JSONObject setValid(String data) {
        PrizeDrawExt form = (PrizeDrawExt) JSON.parseObject(data, PrizeDrawExt.class);
        form.setUpdateUserId((String) PamsDataContext.get("loginId"));
        return prizeDrawService.setValid(form);
    }

    @RequestMapping("setInvalid")
    @ResponseBody
    public JSONObject setInvalid(String data) {
        PrizeDrawExt form = (PrizeDrawExt) JSON.parseObject(data, PrizeDrawExt.class);
        form.setUpdateUserId((String) PamsDataContext.get("loginId"));
        return prizeDrawService.setInvalid(form);
    }

    @RequestMapping("getPrizeGoodsList")
    @ResponseBody
    public JSONObject getPrizeGoodsList(String data) {
        PrizeGoodsExt form = (PrizeGoodsExt) JSON.parseObject(data, PrizeGoodsExt.class);
        return prizeDrawService.getPrizeGoodsList(form);
    }

    @RequestMapping("deletePrizeGoods")
    @ResponseBody
    public JSONObject deletePrizeGoods(String data) {
        PrizeGoodsExt form = (PrizeGoodsExt) JSON.parseObject(data, PrizeGoodsExt.class);
        return prizeDrawService.deletePrizeGoods(form);
    }

    @RequestMapping("addPrizeGoods")
    @ResponseBody
    public JSONObject addPrizeGoodsList(String data) {
        PrizeGoodsExt form = (PrizeGoodsExt) JSON.parseObject(data, PrizeGoodsExt.class);
        return prizeDrawService.addPrizeGoods(form);
    }

    @RequestMapping("editPrizeGoods")
    @ResponseBody
    public JSONObject editPrizeGoodsList(String data) {
        PrizeGoodsExt form = (PrizeGoodsExt) JSON.parseObject(data, PrizeGoodsExt.class);
        return prizeDrawService.editPrizeGoods(form);
    }

    /**
     * 管理端获取抽奖人员记录
     * @param form
     * @return
     */
    @RequestMapping("getPrizeOppList")
    @ResponseBody
    public JSONObject getPrizeOppList(PrizeDrawOppoExt form) {
        return prizeDrawService.getPrizeOppList(form);
    }

    @RequestMapping("getPrizeGoodsNameList")
    @ResponseBody
    public JSONObject getPrizeGoodsNameList(PrizeGoodsExt form) {
        return prizeDrawService.getPrizeGoodsList(form);
    }

    @RequestMapping("sendPrizeGoods")
    @ResponseBody
    public JSONObject sendPrizeGoods(String data) {
        PrizeDrawOppoExt form = (PrizeDrawOppoExt) JSON.parseObject(data, PrizeDrawOppoExt.class);
        form.setUpdateUserId((String) PamsDataContext.get("loginId"));
        return prizeDrawService.sendPrizeGoods(form);
    }

    @RequestMapping("getPrizeConfig")
    @ResponseBody
    public JSONObject getPrizeConfig(String data) {
        PrizeDrawConfig form = (PrizeDrawConfig) JSON.parseObject(data, PrizeDrawConfig.class);
        return prizeDrawService.getPrizeConfig(form);
    }

    @RequestMapping("updatePrizeConfig")
    @ResponseBody
    public JSONObject updatePrizeConfig(String data) {
        PrizeDrawConfig form = (PrizeDrawConfig) JSON.parseObject(data, PrizeDrawConfig.class);
        return prizeDrawService.updatePrizeConfig(form);
    }


}
