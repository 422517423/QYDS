package net.dlyt.qyds.web.controller.erp;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.*;
import net.dlyt.qyds.common.dto.ext.MmbMasterExt;
import net.dlyt.qyds.common.dto.ext.MmbSalerExt;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.common.SmsSendUtil;
import net.dlyt.qyds.web.service.*;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;

import static net.dlyt.qyds.web.common.StringUtil.getNowCode;

/**
 * Created by zlh on 2016/12/14.
 */
@Controller
@RequestMapping("/erp_report")
public class ErpReportController {

    @Resource
    private ErpStoreService erpStoreService;

    @Resource
    private ErpBrandService erpBrandService;

    @Resource
    private ErpProduceLineService erpProduceLineService;

    @Resource
    private ErpGoodsColorService erpGoodsColorService;

    @Resource
    private ErpGoodsSizeService erpGoodsSizeService;

    @Resource
    private ErpGoodsService erpGoodsService;

    @Resource
    private MmbSalerService mmbSalerService;

    @Resource
    private ErpMemberService erpMemberService;

    @Autowired
    private MmbMasterService mmbMasterService;

    @Autowired
    private OrdMasterService ordMasterService;

    @Autowired
    private BnkMasterService bnkMasterService;

    // 生成码表对账单
    @RequestMapping("code")
    public @ResponseBody
    void reportCode(HttpServletRequest request, HttpServletResponse response){
        try{
            // 创建工作簿对象
            HSSFWorkbook workbook = new HSSFWorkbook();
            // 门店：门店编码
            sheetStore(workbook);
            // 品牌：品牌编码
            sheetBrand(workbook);
            // 产品线：产品线编码
            sheetLine(workbook);
            // 颜色：颜色编码
            sheetColor(workbook);
            // 尺码：尺码类型编码、尺码编码
            sheetSize(workbook);
            // 商品：商品编码
            sheetGoods(workbook);
            // 店员：员工编号、员工姓名
            sheetSaler(workbook);

            // 文件返回
            if(workbook !=null) {
                try {
                    String fileName = "report_code_" + getNowCode() + ".xls";
                    String headStr = "attachment; filename=\"" + fileName + "\"";
                    response.setContentType("APPLICATION/OCTET-STREAM");
                    response.setHeader("Content-Disposition", headStr);
                    OutputStream out = response.getOutputStream();
                    workbook.write(out);
                }
                catch (IOException e) {
                    throw e;
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    // 生成门店SHEET
    void sheetStore(HSSFWorkbook workbook) throws Exception {
        try{
            HSSFSheet sheet = workbook.createSheet("门店");
            // 设置列头
            HSSFRow row = sheet.createRow(0);
            HSSFCell cell  = row.createCell(0);
            cell.setCellValue("门店编码");
            // 取得数据
            JSONObject json = erpStoreService.selectAll();
            String resultCode = json.getString("resultCode");
            if (resultCode.equals(Constants.FAIL)) {
                throw new Exception("数据取得失败");
            }
            List<ErpStore> list = (List<ErpStore>)json.get("data");
            for (int i = 0;i<list.size();i++) {
                row = sheet.createRow(i+1);
                cell = row.createCell(0);
                cell.setCellValue(list.get(i).getStoreCode());
            }
        }catch(Exception e) {
            throw e;
        }
    }
    // 生成品牌SHEET
    void sheetBrand(HSSFWorkbook workbook) throws Exception {
        try{
            HSSFSheet sheet = workbook.createSheet("品牌");
            // 设置列头
            HSSFRow row = sheet.createRow(0);
            HSSFCell cell  = row.createCell(0);
            cell.setCellValue("品牌编码");
            // 取得数据
            JSONObject json = erpBrandService.selectAll();
            String resultCode = json.getString("resultCode");
            if (resultCode.equals(Constants.FAIL)) {
                throw new Exception("数据取得失败");
            }
            List<ErpBrand> list = (List<ErpBrand>)json.get("data");
            for (int i = 0;i<list.size();i++) {
                row = sheet.createRow(i+1);
                cell = row.createCell(0);
                cell.setCellValue(list.get(i).getBrandCode());
            }
        }catch(Exception e) {
            throw e;
        }
    }
    // 产品线SHEET
    void sheetLine(HSSFWorkbook workbook) throws Exception {
        try{
            HSSFSheet sheet = workbook.createSheet("产品线");
            // 设置列头
            HSSFRow row = sheet.createRow(0);
            HSSFCell cell  = row.createCell(0);
            cell.setCellValue("产品线编码");
            // 取得数据
            JSONObject json = erpProduceLineService.selectAll();
            String resultCode = json.getString("resultCode");
            if (resultCode.equals(Constants.FAIL)) {
                throw new Exception("数据取得失败");
            }
            List<ErpProduceLine> list = (List<ErpProduceLine>)json.get("data");
            for (int i = 0;i<list.size();i++) {
                row = sheet.createRow(i+1);
                cell = row.createCell(0);
                cell.setCellValue(list.get(i).getLineCode());
            }
        }catch(Exception e) {
            throw e;
        }
    }
    // 颜色SHEET
    void sheetColor(HSSFWorkbook workbook) throws Exception {
        try{
            HSSFSheet sheet = workbook.createSheet("颜色");
            // 设置列头
            HSSFRow row = sheet.createRow(0);
            HSSFCell cell  = row.createCell(0);
            cell.setCellValue("颜色编码");
            // 取得数据
            JSONObject json = erpGoodsColorService.selectAll();
            String resultCode = json.getString("resultCode");
            if (resultCode.equals(Constants.FAIL)) {
                throw new Exception("数据取得失败");
            }
            List<ErpGoodsColor> list = (List<ErpGoodsColor>)json.get("data");
            for (int i = 0;i<list.size();i++) {
                row = sheet.createRow(i+1);
                cell = row.createCell(0);
                cell.setCellValue(list.get(i).getColorCode());
            }
        }catch(Exception e) {
            throw e;
        }
    }
    // 尺码SHEET
    void sheetSize(HSSFWorkbook workbook) throws Exception {
        try{
            HSSFSheet sheet = workbook.createSheet("尺码");
            // 设置列头
            HSSFRow row = sheet.createRow(0);
            HSSFCell cell  = row.createCell(0);
            cell.setCellValue("尺码类型编码");
            cell  = row.createCell(1);
            cell.setCellValue("尺码编码");
            // 取得数据
            JSONObject json = erpGoodsSizeService.selectAll();
            String resultCode = json.getString("resultCode");
            if (resultCode.equals(Constants.FAIL)) {
                throw new Exception("数据取得失败");
            }
            List<ErpGoodsSize> list = (List<ErpGoodsSize>)json.get("data");
            for (int i = 0;i<list.size();i++) {
                row = sheet.createRow(i+1);
                cell = row.createCell(0);
                cell.setCellValue(list.get(i).getSizeTypeCode());
                cell = row.createCell(1);
                cell.setCellValue(list.get(i).getSizeCode());
            }
        }catch(Exception e) {
            throw e;
        }
    }
    // 商品SHEET
    void sheetGoods(HSSFWorkbook workbook) throws Exception {
        try{
            HSSFSheet sheet = workbook.createSheet("商品");
            // 设置列头
            HSSFRow row = sheet.createRow(0);
            HSSFCell cell  = row.createCell(0);
            cell.setCellValue("商品编码");
            // 取得数据
            JSONObject json = erpGoodsService.selectAll();
            String resultCode = json.getString("resultCode");
            if (resultCode.equals(Constants.FAIL)) {
                throw new Exception("数据取得失败");
            }
            List<ErpGoods> list = (List<ErpGoods>)json.get("data");
            for (int i = 0;i<list.size();i++) {
                row = sheet.createRow(i+1);
                cell = row.createCell(0);
                cell.setCellValue(list.get(i).getSku());
            }
        }catch(Exception e) {
            throw e;
        }
    }
    // 员工SHEET
    void sheetSaler(HSSFWorkbook workbook) throws Exception {
        try{
            HSSFSheet sheet = workbook.createSheet("员工");
            // 设置列头
            HSSFRow row = sheet.createRow(0);
            HSSFCell cell  = row.createCell(0);
            cell.setCellValue("员工编码");
            cell  = row.createCell(1);
            cell.setCellValue("员工姓名");
            // 取得数据
            JSONObject json = mmbSalerService.selectReport();
            String resultCode = json.getString("resultCode");
            if (resultCode.equals(Constants.FAIL)) {
                throw new Exception("数据取得失败");
            }
            List<MmbSalerExt> list = (List<MmbSalerExt>)json.get("data");
            for (int i = 0;i<list.size();i++) {
                row = sheet.createRow(i+1);
                cell = row.createCell(0);
                cell.setCellValue(list.get(i).getTelephone());
                cell = row.createCell(1);
                cell.setCellValue(list.get(i).getMemberName());
                if (list.get(i).getDeleted().equals("1")) {
                    cell = row.createCell(2);
                    cell.setCellValue("已删除");
                }
            }
        }catch(Exception e) {
            throw e;
        }
    }

    // 生成会员对账单
    @RequestMapping("member")
    public @ResponseBody
    void reportMember(HttpServletRequest request, HttpServletResponse response){
        try{
            // 创建工作簿对象
            HSSFWorkbook workbook = new HSSFWorkbook();
            // 会员：会员编号、会员姓名、会员汇总积分
            sheetMember(workbook);

            // 文件返回
            if(workbook !=null) {
                try {
                    String fileName = "report_member_" + getNowCode() + ".xls";
                    String headStr = "attachment; filename=\"" + fileName + "\"";
                    response.setContentType("APPLICATION/OCTET-STREAM");
                    response.setHeader("Content-Disposition", headStr);
                    OutputStream out = response.getOutputStream();
                    workbook.write(out);
                }
                catch (IOException e) {
                    throw e;
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    // 生成会员SHEET
    void sheetMember(HSSFWorkbook workbook) throws Exception {
        try{
            HSSFSheet sheet = workbook.createSheet("会员");
            // 设置列头
            HSSFRow row = sheet.createRow(0);
            HSSFCell cell  = row.createCell(0);
            cell.setCellValue("会员编号");
            cell  = row.createCell(1);
            cell.setCellValue("会员姓名");
            cell  = row.createCell(2);
            cell.setCellValue("会员汇总积分");
            // 取得数据
            JSONObject json = mmbMasterService.selectReport();
            String resultCode = json.getString("resultCode");
            if (resultCode.equals(Constants.FAIL)) {
                throw new Exception("数据取得失败");
            }
            List<MmbMasterExt> list = (List<MmbMasterExt>)json.get("data");
            for (int i = 0;i<list.size();i++) {
                row = sheet.createRow(i+1);
                cell = row.createCell(0);
                cell.setCellValue(list.get(i).getTelephone());
                cell = row.createCell(1);
                cell.setCellValue(getNull2Blank(list.get(i).getMemberName()));
                cell = row.createCell(2);
                cell.setCellValue(getNull2Blank(list.get(i).getPoint()));
            }
        }catch(Exception e) {
            throw e;
        }
    }

    // 订单对账单
    @RequestMapping("order")
    public @ResponseBody
    void reportOrder(HttpServletRequest request, HttpServletResponse response){
        try{
            // 创建工作簿对象
            HSSFWorkbook workbook = new HSSFWorkbook();
            String dateStart = request.getParameter("start_date").toString();
            String dateEnd = request.getParameter("end_date").toString();
            // 销售订单：订单编码、订单汇总数量、订单汇总金额
            sheetSale(workbook,dateStart,dateEnd);
            // 退货订单：订单编码、订单汇总数量、订单汇总金额
            sheetReturn(workbook,dateStart,dateEnd);

            // 文件返回
            if(workbook !=null) {
                try {
                    String fileName = "report_order_" + getNowCode() + ".xls";
                    String headStr = "attachment; filename=\"" + fileName + "\"";
                    response.setContentType("APPLICATION/OCTET-STREAM");
                    response.setHeader("Content-Disposition", headStr);
                    OutputStream out = response.getOutputStream();
                    workbook.write(out);
                }
                catch (IOException e) {
                    throw e;
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    // 销售订单SHEET
    void sheetSale(HSSFWorkbook workbook,String sDate,String eDate) throws Exception {
        try{
            HSSFSheet sheet = workbook.createSheet("销售订单");
            // 设置列头
            HSSFRow row = sheet.createRow(0);
            HSSFCell cell  = row.createCell(0);
            cell.setCellValue("订单编码");
            cell  = row.createCell(1);
            cell.setCellValue("订单汇总数量");
            cell  = row.createCell(2);
            cell.setCellValue("订单汇总金额");
            // 取得数据
            OrdMasterExt ordMasterExt = new OrdMasterExt();
            ordMasterExt.setOrderTimeStart(sDate);
            ordMasterExt.setOrderTimeEnd(eDate);

            JSONObject json = ordMasterService.getReportSale(ordMasterExt);
            String resultCode = json.getString("resultCode");
            if (resultCode.equals(Constants.FAIL)) {
                throw new Exception("数据取得失败");
            }
            List<OrdSubList> list = (List<OrdSubList>)json.get("data");
            for (int i = 0;i<list.size();i++) {
                row = sheet.createRow(i+1);
                cell = row.createCell(0);
                cell.setCellValue(list.get(i).getOrderId());
                cell = row.createCell(1);
                cell.setCellValue(list.get(i).getQuantity());
                cell = row.createCell(2);
                cell.setCellValue(getNull2Blank(list.get(i).getPriceShare()));
            }
        }catch(Exception e) {
            throw e;
        }
    }
    // 退货订单SHEET
    void sheetReturn(HSSFWorkbook workbook,String sDate,String eDate) throws Exception {
        try{
            HSSFSheet sheet = workbook.createSheet("退货订单");
            // 设置列头
            HSSFRow row = sheet.createRow(0);
            HSSFCell cell  = row.createCell(0);
            cell.setCellValue("订单编码");
            cell  = row.createCell(1);
            cell.setCellValue("订单汇总数量");
            cell  = row.createCell(2);
            cell.setCellValue("订单汇总金额");
            // 取得数据
            OrdMasterExt ordMasterExt = new OrdMasterExt();
            ordMasterExt.setOrderTimeStart(sDate);
            ordMasterExt.setOrderTimeEnd(eDate);

            JSONObject json = ordMasterService.getReportReturn(ordMasterExt);
            String resultCode = json.getString("resultCode");
            if (resultCode.equals(Constants.FAIL)) {
                throw new Exception("数据取得失败");
            }
            List<OrdSubList> list = (List<OrdSubList>)json.get("data");
            for (int i = 0;i<list.size();i++) {
                row = sheet.createRow(i+1);
                cell = row.createCell(0);
                cell.setCellValue(list.get(i).getOrderId());
                cell = row.createCell(1);
                cell.setCellValue(list.get(i).getQuantity());
                cell = row.createCell(2);
                cell.setCellValue(getNull2Blank(list.get(i).getPriceShare()));
            }
        }catch(Exception e) {
            throw e;
        }
    }

    // 库存对账单
    @RequestMapping("bank")
    public @ResponseBody
    void reportBank(HttpServletResponse response){
        try{
            // 创建工作簿对象
            HSSFWorkbook workbook = new HSSFWorkbook();
            // 库存：款号、门店编码、库存汇总数量
            sheetBank(workbook);

            // 文件返回
            if(workbook !=null) {
                try {
                    String fileName = "report_bank_" + getNowCode() + ".xls";
                    String headStr = "attachment; filename=\"" + fileName + "\"";
                    response.setContentType("APPLICATION/OCTET-STREAM");
                    response.setHeader("Content-Disposition", headStr);
                    OutputStream out = response.getOutputStream();
                    workbook.write(out);
                }
                catch (IOException e) {
                    throw e;
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    // 库存SHEET
    void sheetBank(HSSFWorkbook workbook) throws Exception {
        try{
            HSSFSheet sheet = workbook.createSheet("库存");
            // 设置列头
            HSSFRow row = sheet.createRow(0);
            HSSFCell cell  = row.createCell(0);
            cell.setCellValue("款号");
            cell  = row.createCell(1);
            cell.setCellValue("门店编码");
            cell  = row.createCell(2);
            cell.setCellValue("库存汇总数量");
            // 取得数据
            JSONObject json = bnkMasterService.getReportBank();
            String resultCode = json.getString("resultCode");
            if (resultCode.equals(Constants.FAIL)) {
                throw new Exception("数据取得失败");
            }
            List<BnkMaster> list = (List<BnkMaster>)json.get("data");
            for (int i = 0;i<list.size();i++) {
                row = sheet.createRow(i+1);
                cell = row.createCell(0);
                cell.setCellValue(list.get(i).getErpGoodsCode());
                cell = row.createCell(1);
                cell.setCellValue(list.get(i).getErpStoreId());
                cell = row.createCell(2);
                cell.setCellValue(getNull2Blank(list.get(i).getNewCount()));
            }
        }catch(Exception e) {
            throw e;
        }
    }

    @RequestMapping("sendSms")
    public @ResponseBody
    JSONObject sendSms(){
        return SmsSendUtil.test();
    }

    private String getNull2Blank(String s) {
        return s==null?"":s;
    }

    private String getNull2Blank(Integer s) {
        return s==null?"":s.toString();
    }

    private String getNull2Blank(BigDecimal s) {
        return s==null?"":s.toString();
    }
}
