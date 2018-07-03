package net.dlyt.qyds.web.controller.act_template;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ActTemplate;
import net.dlyt.qyds.common.dto.ErpGoods;
import net.dlyt.qyds.common.dto.GdsBrand;
import net.dlyt.qyds.common.dto.GdsMasterExt;
import net.dlyt.qyds.common.form.ActTemplateForm;
import net.dlyt.qyds.common.form.GdsMasterForm;
import net.dlyt.qyds.common.form.GoodsCodeColorNameDto;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.common.DataUtils;
import net.dlyt.qyds.web.context.PamsDataContext;
import net.dlyt.qyds.web.service.ActTemplateService;
import net.dlyt.qyds.web.service.common.StringUtil;
import net.dlyt.qyds.web.service.exception.ExceptionErrorParam;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.dlyt.qyds.web.common.StringUtil.getNowCode;

/**
 * Created by cky on 2016/7/18.
 */
@Controller
@RequestMapping("/act_template")
public class ActTemplateController {

    @Autowired
    ActTemplateService actTemplateService;

    @RequestMapping("getList")
    @ResponseBody
    public JSONObject getList(ActTemplateForm form) {
        return actTemplateService.getTemplateList(form);
    }

    @RequestMapping("getApproveList")
    @ResponseBody
    public JSONObject getApproveList(ActTemplateForm form) {
        return actTemplateService.getTemplateApproveList(form);
    }


    @RequestMapping("getDetail")
    @ResponseBody
    public JSONObject getDetail(String data) {
        ActTemplateForm form = (ActTemplateForm) JSON.parseObject(data, ActTemplateForm.class);
        return actTemplateService.getDetail(form);
    }

    @RequestMapping("delete")
    @ResponseBody
    public JSONObject delete(String data) {
        ActTemplateForm form = (ActTemplateForm) JSON.parseObject(data, ActTemplateForm.class);
        return actTemplateService.delete(form);
    }

    @RequestMapping("add")
    @ResponseBody
    public JSONObject add(String data) {
        ActTemplateForm form = (ActTemplateForm) JSON.parseObject(data, ActTemplateForm.class);
        form.setShopId(Constants.ORGID);
        form.setInsertUserId((String) PamsDataContext.get("loginId"));
        return actTemplateService.add(form);
    }

    @RequestMapping("edit")
    @ResponseBody
    public JSONObject edit(String data) {
        ActTemplateForm form = (ActTemplateForm) JSON.parseObject(data, ActTemplateForm.class);
        form.setShopId(Constants.ORGID);
        form.setUpdateUserId((String) PamsDataContext.get("loginId"));
        return actTemplateService.edit(form);
    }

    /**
     * 申请
     *
     * @param data
     * @return
     */
    @RequestMapping("apply")
    @ResponseBody
    public JSONObject apply(String data) {
        ActTemplateForm form = (ActTemplateForm) JSON.parseObject(data, ActTemplateForm.class);
        form.setApplyUserId((String) PamsDataContext.get("loginId"));
        return actTemplateService.apply(form);
    }

    /**
     * 审批通过
     *
     * @param data
     * @return
     */
    @RequestMapping("approve")
    @ResponseBody
    public JSONObject approve(String data) {
        ActTemplateForm form = (ActTemplateForm) JSON.parseObject(data, ActTemplateForm.class);
        form.setApproveUserId((String) PamsDataContext.get("loginId"));
        return actTemplateService.approve(form);
    }

    /**
     * 审批驳回
     *
     * @param data
     * @return
     */
    @RequestMapping("reject")
    @ResponseBody
    public JSONObject reject(String data) {
        ActTemplateForm form = (ActTemplateForm) JSON.parseObject(data, ActTemplateForm.class);
        form.setApproveUserId((String) PamsDataContext.get("loginId"));
        return actTemplateService.reject(form);
    }

    @RequestMapping("uploadSKU")
    public void uploadSKUFile(HttpServletRequest request, HttpServletResponse response) {
        JSONObject map = new JSONObject();
        try {
            if(!ServletFileUpload.isMultipartContent(request)) {
                throw new Exception("服务器异常");
            }
            ServletFileUpload upload = new ServletFileUpload();
            FileItemIterator iter = upload.getItemIterator(request);
            String tempId = null;
            while (iter.hasNext()) {
                FileItemStream fis = iter.next();
                InputStream is = fis.openStream();
                if (fis.isFormField()) {
                    tempId = Streams.asString(is);
                    if(StringUtil.isEmpty(tempId)){
                        throw new ExceptionErrorParam("参数错误");
                    }
                } else {
                    String fileName = fis.getName();

                    int iIndex = fileName.lastIndexOf(".");
                    String ext = (iIndex < 0) ? "" : fileName.substring(iIndex + 1).toLowerCase();
                    if (!"xls".contains(ext)) {
                        throw new ExceptionErrorParam("文件格式错误");
                    }

                    HSSFWorkbook excel = new HSSFWorkbook(is);
                    Sheet sheet = excel.getSheetAt(0);

                    //总行数
                    int trLength = sheet.getLastRowNum();
                    if(trLength < 1) {
                        throw new ExceptionErrorParam("没有数据");
                    }
                    Map<String,GoodsCodeColorNameDto> mapList = new HashMap<String, GoodsCodeColorNameDto>();
                    for (int i = 1; i <= trLength; i++) {
                        Row row = sheet.getRow(i);
                        //得到Excel工作表指定行的单元格
                        Cell cell = row.getCell(1);
                        String goodsCode = cell.getRichStringCellValue().getString();
                        cell = row.getCell(3);
                        String colorName = cell.getRichStringCellValue().getString();
                        GoodsCodeColorNameDto dto = new GoodsCodeColorNameDto();
                        dto.setGoodsCode(goodsCode);
                        dto.setColorName(colorName);
                        mapList.put(goodsCode+colorName,dto);
                    }
                    excel.close();
                    if(mapList == null && mapList.size() == 0){
                        throw new ExceptionErrorParam("没有数据");
                    }
                    List<GoodsCodeColorNameDto> skuList = new ArrayList<GoodsCodeColorNameDto>();
                    for (String key : mapList.keySet()) {
                        skuList.add(mapList.get(key));
                    }
                    //取得用户登录信息
                    Map<String, Object> userMap = PamsDataContext.get();
                    //登录人
                    String loginId = (String) userMap.get("loginId");
                    map = actTemplateService.importSkuList(loginId, tempId, skuList);
                    if (!map.getString("resultCode").equals(Constants.NORMAL)) {
                        throw new ExceptionErrorParam("导入失败");
                    }
                }
            }
            map.put("resultCode", Constants.NORMAL);
            map.put("message", "导入成功");
        } catch (Exception e) {
            map.put("message", e.getMessage());
            map.put("resultCode", Constants.FAIL);
        } finally {
            try {
                response.getWriter().write(map.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping("exportSKUFile")
    public void exportSKUFile(String tempId, HttpServletResponse response) {
        // 创建工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 创建工作簿sheet页
        HSSFSheet sheet = workbook.createSheet("GoodsColor");
        HSSFRow row = null;
        HSSFCell cell = null;
        int line = 0;
        // 标题行
        row = sheet.createRow(line);
        cell = row.createCell(0);
        cell.setCellValue("商品款号");
        cell = row.createCell(1);
        cell.setCellValue("颜色");
        line++;
        try {
            if(StringUtil.isEmpty(tempId)) throw new Exception("参数错误");
            JSONObject result = actTemplateService.getSkuListById(tempId);
            if(result==null) throw new Exception("数据取得失败");
            if(!Constants.NORMAL.equals(result.getString("resultCode"))) throw new Exception("数据取得失败");
            List<ErpGoods> list = (List<ErpGoods>)result.get("data");;
            if(list==null || list.size()==0) throw new Exception("没有数据");
            // 数据行
            for (ErpGoods erpGoods : list) {
                row = sheet.createRow(line);
                cell = row.createCell(0);
                cell.setCellValue(erpGoods.getGoodsCode());
                cell = row.createCell(1);
                cell.setCellValue(erpGoods.getColorName());
                line++;
            }
        } catch (Exception e) {
            row = sheet.createRow(line);
            cell = row.createCell(0);
            cell.setCellValue(e.getMessage());
        } finally {
            try {
                String fileName = "act_tmp_goods_color_list_" + getNowCode() + ".xls";
                String headStr = "attachment; filename=\"" + fileName + "\"";
                response.setContentType("APPLICATION/OCTET-STREAM");
                response.setHeader("Content-Disposition", headStr);
                OutputStream out = response.getOutputStream();
                workbook.write(out);
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
