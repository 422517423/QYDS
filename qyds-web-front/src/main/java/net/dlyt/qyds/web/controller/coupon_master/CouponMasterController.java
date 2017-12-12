package net.dlyt.qyds.web.controller.coupon_master;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.CouponMaster;
import net.dlyt.qyds.common.dto.CouponMember;
import net.dlyt.qyds.common.dto.ErpGoods;
import net.dlyt.qyds.common.dto.ext.CouponMasterExt;
import net.dlyt.qyds.common.dto.ext.CouponMemberExt;
import net.dlyt.qyds.dao.ext.CouponGoodsMapperExt;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.context.PamsDataContext;
import net.dlyt.qyds.web.service.CouponMasterService;
import net.dlyt.qyds.web.service.CouponMemberService;
import net.dlyt.qyds.web.service.common.StringUtil;
import net.dlyt.qyds.web.service.exception.ExceptionErrorParam;
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
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static net.dlyt.qyds.web.common.StringUtil.getNowCode;

/**
 * Created by cky on 2016/7/18.
 */
@Controller
@RequestMapping("/coupon_master")
public class CouponMasterController {

    @Autowired
    private CouponMasterService couponMasterService;
    @Autowired
    private CouponMemberService couponMemberService;

    @RequestMapping("getList")
    @ResponseBody
    public JSONObject getList(CouponMasterExt form) {
        return couponMasterService.getList(form);
    }

    @RequestMapping("getApproveList")
    @ResponseBody
    public JSONObject getApproveList(CouponMasterExt form) {
        return couponMasterService.getApproveList(form);
    }


    @RequestMapping("getDetail")
    @ResponseBody
    public JSONObject getDetail(String data) {
        CouponMasterExt form = (CouponMasterExt) JSON.parseObject(data, CouponMasterExt.class);
        return couponMasterService.getDetail(form);
    }

    @RequestMapping("delete")
    @ResponseBody
    public JSONObject delete(String data) {
        CouponMasterExt form = (CouponMasterExt) JSON.parseObject(data, CouponMasterExt.class);
        return couponMasterService.delete(form);
    }

    @RequestMapping("add")
    @ResponseBody
    public JSONObject add(String data) {
        try {
            CouponMasterExt form = (CouponMasterExt) JSON.parseObject(data, CouponMasterExt.class);
            form.setShopId(Constants.ORGID);
            form.setInsertUserId((String) PamsDataContext.get("loginId"));
            return couponMasterService.add(form);
        } catch (Exception e) {
            JSONObject json = new JSONObject();
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
            return json;
        }
    }

    @RequestMapping("edit")
    @ResponseBody
    public JSONObject edit(String data) {
        try {
            CouponMasterExt form = (CouponMasterExt) JSON.parseObject(data, CouponMasterExt.class);
            form.setShopId(Constants.ORGID);
            form.setUpdateUserId((String) PamsDataContext.get("loginId"));
            return couponMasterService.edit(form);
        } catch (Exception e) {
            JSONObject json = new JSONObject();
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
            return json;
        }
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
        CouponMasterExt form = (CouponMasterExt) JSON.parseObject(data, CouponMasterExt.class);
        form.setApplyUserId((String) PamsDataContext.get("loginId"));
        return couponMasterService.apply(form);
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
        CouponMasterExt form = (CouponMasterExt) JSON.parseObject(data, CouponMasterExt.class);
        form.setApproveUserId((String) PamsDataContext.get("loginId"));
        return couponMasterService.approve(form);
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
        CouponMasterExt form = (CouponMasterExt) JSON.parseObject(data, CouponMasterExt.class);
        form.setApproveUserId((String) PamsDataContext.get("loginId"));
        return couponMasterService.reject(form);
    }


    @RequestMapping("distributeCoupon")
    @ResponseBody
    public JSONObject distributeCoupon(String data) {
        CouponMasterExt form = (CouponMasterExt) JSON.parseObject(data, CouponMasterExt.class);
        return couponMemberService.distributeCoupon(form);
    }

    @RequestMapping("couponRecord")
    public
    @ResponseBody
    JSONObject couponRecord(CouponMemberExt form) {
        JSONObject map = new JSONObject();
        try{
            map = couponMemberService.selectRecordByPage(form);
        }catch(Exception e){
            map.put("resultCode",Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    @RequestMapping("sendToMember")
    @ResponseBody
    public JSONObject sendToMember(String data) {
        CouponMember form = (CouponMember) JSON.parseObject(data, CouponMember.class);
        //取得用户登录信息
        Map<String, Object> userMap = PamsDataContext.get();
        //登录人
        String loginId = (String) userMap.get("loginId");
        //设定用户信息
        form.setInsertUserId(loginId);
        form.setUpdateUserId(loginId);

        return couponMemberService.sendToMember(form);
    }

    @RequestMapping("getSendList")
    @ResponseBody
    public JSONObject getSendList() {
        return couponMasterService.getSendList();
    }

    @RequestMapping("setSrot")
    @ResponseBody
    public JSONObject setSrot(String data) {
        CouponMaster form = JSON.parseObject(data, CouponMaster.class);
        //取得用户登录信息
        Map<String, Object> userMap = PamsDataContext.get();
        //登录人
        String loginId = (String) userMap.get("loginId");
        //设定用户信息
        form.setUpdateUserId(loginId);
        form.setUpdateTime(new Date());

        return couponMasterService.setSort(form);
    }

    @RequestMapping("setValid")
    @ResponseBody
    public JSONObject setValid(String data) {
        CouponMasterExt form = (CouponMasterExt) JSON.parseObject(data, CouponMasterExt.class);
        return couponMasterService.setValid(form);
    }

    @RequestMapping("setInvalid")
    @ResponseBody
    public JSONObject setInvalid(String data) {
        CouponMasterExt form = (CouponMasterExt) JSON.parseObject(data, CouponMasterExt.class);
        return couponMasterService.setInvalid(form);
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
            String couponId = null;
            while (iter.hasNext()) {
                FileItemStream fis = iter.next();
                InputStream is = fis.openStream();
                if (fis.isFormField()) {
                    couponId = Streams.asString(is);
                    if(StringUtil.isEmpty(couponId)){
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
                    List<String> skuList = new ArrayList<String>();
                    for (int i = 1; i <= trLength; i++) {
                        Row row = sheet.getRow(i);
                        //得到Excel工作表指定行的单元格
                        Cell cell = row.getCell(0);
                        String value = cell.getRichStringCellValue().getString();;
                        skuList.add(value);
                    }
                    excel.close();
                    if(skuList == null && skuList.size() == 0){
                        throw new ExceptionErrorParam("没有数据");
                    }
                    //取得用户登录信息
                    Map<String, Object> userMap = PamsDataContext.get();
                    //登录人
                    String loginId = (String) userMap.get("loginId");
                    map = couponMasterService.importSkuList(loginId, couponId, skuList);
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
    public void exportSKUFile(String couponId, HttpServletResponse response) {
        // 创建工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 创建工作簿sheet页
        HSSFSheet sheet = workbook.createSheet("SKU");
        HSSFRow row = null;
        HSSFCell cell = null;
        int line = 0;
        // 标题行
        row = sheet.createRow(line);
        cell = row.createCell(0);
        cell.setCellValue("条形码");
        cell = row.createCell(1);
        cell.setCellValue("商品款号");
        cell = row.createCell(2);
        cell.setCellValue("颜色");
        cell = row.createCell(3);
        cell.setCellValue("尺码");
        line++;
        try {
//            String couponId = request.getParameter("couponId1").toString();
            if(StringUtil.isEmpty(couponId)) throw new Exception("参数错误");
            JSONObject result = couponMasterService.getSkuListById(couponId);
            if(result==null) throw new Exception("数据取得失败");
            if(!Constants.NORMAL.equals(result.getString("resultCode"))) throw new Exception("数据取得失败");
            List<ErpGoods> list = (List<ErpGoods>)result.get("data");;
            if(list==null || list.size()==0) throw new Exception("没有数据");
            // 数据行
            for (ErpGoods erpGoods : list) {
                row = sheet.createRow(line);
                cell = row.createCell(0);
                cell.setCellValue(erpGoods.getSku());
                cell = row.createCell(1);
                cell.setCellValue(erpGoods.getGoodsCode());
                cell = row.createCell(2);
                cell.setCellValue(erpGoods.getColorName());
                cell = row.createCell(3);
                cell.setCellValue(erpGoods.getSizeName());
                line++;
            }
        } catch (Exception e) {
            row = sheet.createRow(line);
            cell = row.createCell(0);
            cell.setCellValue(e.getMessage());
        } finally {
            try {
                String fileName = "coupon_sku_list_" + getNowCode() + ".xls";
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

    @RequestMapping("getGoodsList")
    @ResponseBody
    public JSONObject getGoodsList(String data) {
        CouponMasterExt form = (CouponMasterExt) JSON.parseObject(data, CouponMasterExt.class);
        return couponMasterService.getGoodsList(form);
    }

    /**
     * 添加活动相关的商品，根据goodsType的不同
     * @param data
     * @return
     */
    @RequestMapping("addGoods")
    @ResponseBody
    public JSONObject addGoods(String data) {
        CouponMasterExt form = (CouponMasterExt) JSON.parseObject(data, CouponMasterExt.class);
        return couponMasterService.addGoods(form);
    }

    /**
     * 删除活动相关的商品，根据goodsType的不同
     * @param data
     * @return
     */
    @RequestMapping("deleteGoods")
    @ResponseBody
    public JSONObject deleteGoods(String data) {
        CouponMasterExt form = (CouponMasterExt) JSON.parseObject(data, CouponMasterExt.class);
        return couponMasterService.deleteGoods(form);
    }
}
