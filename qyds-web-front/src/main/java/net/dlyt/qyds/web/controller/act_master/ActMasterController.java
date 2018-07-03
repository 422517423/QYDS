package net.dlyt.qyds.web.controller.act_master;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import me.chanjar.weixin.common.util.StringUtils;
import net.dlyt.qyds.common.dto.ActGoods;
import net.dlyt.qyds.common.dto.ActMaster;
import net.dlyt.qyds.common.dto.ext.CouponMasterExt;
import net.dlyt.qyds.common.form.ActGoodsForm;
import net.dlyt.qyds.common.form.ActMasterForm;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.context.PamsDataContext;
import net.dlyt.qyds.web.service.ActMasterService;
import net.dlyt.qyds.web.service.common.StringUtil;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
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
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/act_master")
public class ActMasterController {

    // sheet标题行下标
    private static final int SHEET_ROW_TITLE = 0;
    private static final String SUFFIX = ".xls";
    @Autowired
    ActMasterService actMasterService;

    /**
     * 获取所有活动列表
     *
     * @return
     */
    @RequestMapping("getAllList")
    public @ResponseBody
    JSONObject getAllList() {
        return actMasterService.getAllList();
    }

    @RequestMapping("getList")
    @ResponseBody
    public JSONObject getList(ActMasterForm form) {
        return actMasterService.getList(form);
    }

    @RequestMapping("getApproveList")
    @ResponseBody
    public JSONObject getApproveList(ActMasterForm form) {
        return actMasterService.getApproveList(form);
    }

    @RequestMapping("getDetail")
    @ResponseBody
    public JSONObject getDetail(String data) {
        ActMasterForm form = (ActMasterForm) JSON.parseObject(data, ActMasterForm.class);
        return actMasterService.getDetail(form);
    }

    @RequestMapping("getSellerYears")
    @ResponseBody
    public JSONObject getSellerYears() {
        return actMasterService.getSellerYears();
    }

    @RequestMapping("getSellerSeasons")
    @ResponseBody
    public JSONObject getSellerSeasons(String data) {
        return actMasterService.getSellerSeasons(data);
    }

    @RequestMapping("getErpBrands")
    @ResponseBody
    public JSONObject getErpBrands() {
        return actMasterService.getErpBrands();
    }

    @RequestMapping("getErpLineCode")
    @ResponseBody
    public JSONObject getErpLineCode() {
        return actMasterService.getErpLineCode();
    }

    @RequestMapping("delete")
    @ResponseBody
    public JSONObject delete(String data) {
        ActMasterForm form = (ActMasterForm) JSON.parseObject(data, ActMasterForm.class);
        return actMasterService.delete(form);
    }

    @RequestMapping("add")
    @ResponseBody
    public JSONObject add(String data) {
        ActMasterForm form = (ActMasterForm) JSON.parseObject(data, ActMasterForm.class);
        form.setShopId(Constants.ORGID);
        form.setInsertUserId((String) PamsDataContext.get("loginId"));
        return actMasterService.add(form);
    }

    @RequestMapping("edit")
    @ResponseBody
    public JSONObject edit(String data) {
        ActMasterForm form = (ActMasterForm) JSON.parseObject(data, ActMasterForm.class);
        form.setShopId(Constants.ORGID);
        form.setUpdateUserId((String) PamsDataContext.get("loginId"));
        return actMasterService.edit(form);
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
        ActMasterForm form = (ActMasterForm) JSON.parseObject(data, ActMasterForm.class);
        form.setApplyUserId((String) PamsDataContext.get("loginId"));
        return actMasterService.apply(form);
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
        ActMasterForm form = (ActMasterForm) JSON.parseObject(data, ActMasterForm.class);
        form.setApproveUserId((String) PamsDataContext.get("loginId"));
        return actMasterService.approve(form);
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
        ActMasterForm form = (ActMasterForm) JSON.parseObject(data, ActMasterForm.class);
        form.setApproveUserId((String) PamsDataContext.get("loginId"));
        return actMasterService.reject(form);
    }

    @RequestMapping("setSrot")
    @ResponseBody
    public JSONObject setSrot(String data) {
        ActMaster form = JSON.parseObject(data, ActMaster.class);
        //取得用户登录信息
        Map<String, Object> userMap = PamsDataContext.get();
        //登录人
        String loginId = (String) userMap.get("loginId");
        //设定用户信息
        form.setUpdateUserId(loginId);
        form.setUpdateTime(new Date());

        return actMasterService.setSort(form);
    }

    @RequestMapping("uploadSKU.json")
    public void uploadSKUFile(HttpServletRequest request, HttpServletResponse response) {
        JSONObject map = new JSONObject();
        try {
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);

            if (isMultipart) {
                ServletFileUpload upload = new ServletFileUpload();
                FileItemIterator iter = upload.getItemIterator(request);
                String activityId = null;
                while (iter.hasNext()) {
                    FileItemStream fis = iter.next();
                    InputStream is = fis.openStream();

                    if (fis.isFormField()) {
                        activityId = Streams.asString(is);
                        if (StringUtil.isEmpty(activityId)) {
                            map.put("resultCode", Constants.FAIL);
                            map.put("resultMessage", "活动ID不能为空");
                            response.getWriter().write(map.toJSONString());
                            return;
                        }
                    } else {
                        String fileName = fis.getName();

                        int iIndex = fileName.lastIndexOf(".");
                        String ext = (iIndex < 0) ? "" : fileName.substring(iIndex + 1).toLowerCase();
                        if (!"xls".contains(ext)) {
                            map.put("resultCode", Constants.FAIL);
                            response.getWriter().write(map.toJSONString());
                            return;
                        }

                        Sheet sheet = null;
                        if (ext.equals("xls")) {
                            HSSFWorkbook excel = new HSSFWorkbook(is);
                            sheet = excel.getSheetAt(0);
                        }

                        //总行数
                        int trLength = sheet.getLastRowNum();

                        List<String> skuList = new ArrayList<String>();
                        for (int i = 1; i <= trLength; i++) {
                            Row row1 = sheet.getRow(i);
                            //得到Excel工作表指定行的单元格
                            Cell cell0 = row1.getCell(0);
                            String value = getCellFormatValue(cell0);
                            skuList.add(value);
                        }

                        if (skuList != null && skuList.size() > 0) {

                            //取得用户登录信息
                            Map<String, Object> userMap = PamsDataContext.get();
                            //登录人
                            String loginId = (String) userMap.get("loginId");

                            actMasterService.uploadSKUInfo(loginId, activityId, skuList);
                        }
                    }
                }

                map.put("resultCode", Constants.NORMAL);
                map.put("message", "导入成功");
            } else {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "服务器异常");
            }

            response.getWriter().write(map.toJSONString());
        } catch (Exception e) {
            map.put("resultCode", Constants.FAIL);
        }

    }

    @RequestMapping("uploadSecKill.json")
    public void uploadSecKill(HttpServletRequest request, HttpServletResponse response) {
        JSONObject map = new JSONObject();
        try {
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);

            //取得用户登录信息
            Map<String, Object> userMap = PamsDataContext.get();
            //登录人
            String loginId = (String) userMap.get("loginId");

            if (isMultipart) {
                ServletFileUpload upload = new ServletFileUpload();
                FileItemIterator iter = upload.getItemIterator(request);
                String activityId = null;
                while (iter.hasNext()) {
                    FileItemStream fis = iter.next();
                    InputStream is = fis.openStream();

                    if (fis.isFormField()) {
                        activityId = Streams.asString(is);
                        if (StringUtil.isEmpty(activityId)) {
                            map.put("resultCode", Constants.FAIL);
                            map.put("resultMessage", "活动ID不能为空");
                            response.getWriter().write(map.toJSONString());
                            return;
                        }
                    } else {
                        String fileName = fis.getName();

                        int iIndex = fileName.lastIndexOf(".");
                        String ext = (iIndex < 0) ? "" : fileName.substring(iIndex + 1).toLowerCase();
                        if (!"xls".contains(ext)) {
                            map.put("resultCode", Constants.FAIL);
                            response.getWriter().write(map.toJSONString());
                            return;
                        }

                        Sheet sheet = null;
                        if (ext.equals("xls")) {
                            HSSFWorkbook excel = new HSSFWorkbook(is);
                            sheet = excel.getSheetAt(0);
                        }

                        //总行数
                        int trLength = sheet.getLastRowNum();

                        List<ActGoods> skuList = new ArrayList<ActGoods>();
                        for (int i = 1; i <= trLength; i++) {

                            ActGoods item = new ActGoods();
                            item.setActivityId(activityId);
                            item.setInsertUserId(loginId);

                            Row row1 = sheet.getRow(i);
                            //得到Excel工作表指定行的单元格
                            Cell cell0 = row1.getCell(0);
                            String skuId = getCellFormatValue(cell0);
                            item.setSkuId(skuId);

                            Cell cell1 = row1.getCell(1);
                            String price = getCellFormatValue(cell1);
                            item.setActPrice(new BigDecimal(price));

                            Cell cell2 = row1.getCell(2);
                            String quantity = getCellFormatValue(cell2);
                            item.setQuantity(new BigDecimal(quantity).intValue());

                            Cell cell3 = row1.getCell(3);
                            String bugMax = getCellFormatValue(cell3);
                            if (!StringUtil.isEmpty(bugMax)) {
                                item.setBuyMax(new BigDecimal(bugMax).intValue());
                            }

                            skuList.add(item);
                        }

                        actMasterService.uploadSecKillGoods(skuList);
                    }
                }

                map.put("resultCode", Constants.NORMAL);
                map.put("message", "导入成功");
            } else {
                map.put("resultCode", Constants.FAIL);
                map.put("message", "服务器异常");
            }

            response.getWriter().write(map.toJSONString());
        } catch (Exception e) {
            map.put("resultCode", Constants.FAIL);
        }

    }

    @RequestMapping("exportSKUFile.json")
    public void exportSKUFile(@RequestParam String activityId, HttpServletResponse response) {

        OutputStream out = null;
        try {

            List<ActGoodsForm> dataList = null;

            if (!StringUtils.isEmpty(activityId)) {
                JSONObject skuResult = actMasterService.getActivitySKUList(activityId);
                if (null != skuResult && Constants.NORMAL.endsWith(skuResult.getString("resultCode"))) {
                    dataList = (List<ActGoodsForm>) skuResult.get("resultList");
                }
            }

            if (null == dataList) {
                dataList = new ArrayList<ActGoodsForm>();
            }

            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

            // 创建工作簿
            HSSFWorkbook workbook = new HSSFWorkbook();

            // 创建工作簿sheet页
            HSSFSheet sheet = workbook.createSheet();
            // 创建sheet页标题行
            HSSFRow titleRow = sheet.createRow(SHEET_ROW_TITLE);

            String[] titleNames = {"条形码", "商品款号", "颜色", "尺码"};

            // 设置标题行数据
            for (int i = 0; i < titleNames.length; i++) {
                String title = titleNames[i];
                HSSFCell cell = titleRow.createCell(i);
                cell.setCellValue(title);
            }

            // 标题行以后，开始写入数据
            // 行
            for (int i = 1; i <= dataList.size(); i++) {
                ActGoodsForm item = dataList.get(i - 1);
                HSSFRow dataRow = sheet.createRow(i);

                // 列
                for (int j = 0; j < 4; j++) {
                    String dataItem = "";
                    if (j == 0) {
                        dataItem = item.getSkuId();
                    } else if (j == 1) {
                        dataItem = item.getGoodsCode();
                    } else if (j == 2) {
                        dataItem = item.getColorName();
                    } else if (j == 3) {
                        dataItem = item.getSizeName();
                    }

                    dataItem = dataItem == null ? "" : dataItem;

                    HSSFCell cell = dataRow.createCell(j);
                    cell.setCellValue(dataItem);
                }
            }

            Date date = new Date();
            String timeMark = sf.format(date);
            String fileName = "export_file_" + timeMark + SUFFIX;

            out = response.getOutputStream();
            response.reset();
            response.setHeader("Content-disposition", "attachment; filename=" + fileName);
            response.setContentType("application/msexcel");
            workbook.write(out);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据HSSFCell类型设置数据
     *
     * @param cell
     * @return
     */
    private String getCellFormatValue(Cell cell) {
        String cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
                // 如果当前Cell的Type为NUMERIC
                case Cell.CELL_TYPE_NUMERIC: {
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        // 如果是Date类型则，转化为Data格式
                        Date date = cell.getDateCellValue();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        cellvalue = sdf.format(date);

                    } else {
                        cellvalue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case Cell.CELL_TYPE_FORMULA: {
                    // 判断当前的cell是否为Date
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        // 如果是Date类型则，转化为Data格式
                        Date date = cell.getDateCellValue();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        cellvalue = sdf.format(date);

                    }
                    // 如果是纯数字
                    else {
                        // 取得当前Cell的数值
                        cellvalue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                // 如果当前Cell的Type为STRIN
                case Cell.CELL_TYPE_STRING:
                    // 取得当前的Cell字符串
                    cellvalue = cell.getRichStringCellValue().getString();
                    break;
                // 默认的Cell值
                default:
                    cellvalue = "";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;

    }

    @RequestMapping("setValid")
    @ResponseBody
    public JSONObject setValid(String data) {
        ActMasterForm form = (ActMasterForm) JSON.parseObject(data, ActMasterForm.class);
        return actMasterService.setValid(form);
    }

    @RequestMapping("setInvalid")
    @ResponseBody
    public JSONObject setInvalid(String data) {
        ActMasterForm form = (ActMasterForm) JSON.parseObject(data, ActMasterForm.class);
        return actMasterService.setInvalid(form);
    }


    @RequestMapping("getGoodsList")
    @ResponseBody
    public JSONObject getGoodsList(String data) {
        ActMasterForm form = (ActMasterForm) JSON.parseObject(data, ActMasterForm.class);
        return actMasterService.getGoodsList(form);
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
        ActMasterForm form = (ActMasterForm) JSON.parseObject(data, ActMasterForm.class);
        return actMasterService.addGoods(form);
    }

    /**
     * 编辑活动相关的商品，根据goodsType的不同
     *
     * @param data
     * @return
     */
    @RequestMapping("editGoods")
    @ResponseBody
    public JSONObject editGoods(String data) {
        ActMasterForm form = (ActMasterForm) JSON.parseObject(data, ActMasterForm.class);
        return actMasterService.editGoods(form);
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
        ActMasterForm form = (ActMasterForm) JSON.parseObject(data, ActMasterForm.class);
        return actMasterService.deleteGoods(form);
    }
}
