package net.dlyt.qyds.web.controller.ord_master;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.*;
import net.dlyt.qyds.common.dto.ext.OrdHistoryExt;
import net.dlyt.qyds.common.form.OrdMasterForm;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.common.DataUtils;
import net.dlyt.qyds.web.context.PamsDataContext;
import net.dlyt.qyds.web.service.OrdMasterService;
import net.dlyt.qyds.web.service.common.StringUtil;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by wenxuechao on 16/7/23.
 */
@Controller
@RequestMapping("/ord_master")
public class OrdMasterController {


    @Autowired
    private OrdMasterService ordMasterService;


    /**
     * 获取活动列表还有优惠劵列表
     *
     */

    @RequestMapping("getActivityCouponList")
    public @ResponseBody
    JSONObject getActivityCouponList(String data){
        return ordMasterService.getActivityCouponList(data);
    }

    /**
     * 根据店铺ID获取订单列表信息
     * @param form
     * @return json
     */
    @RequestMapping("getList")
    public @ResponseBody
    JSONObject getList(OrdMasterForm form){
        JSONObject json = new JSONObject();

        try {
            //获取查询条件数据
            OrdMasterExt ordMasterExt = new OrdMasterExt();
            ordMasterExt.setShopId(Constants.ORGID);
            //TODO 获取登陆者的组织ID,方法可能会变更
            SysUser sysUser = (SysUser) PamsDataContext.getSysUser();
            ordMasterExt.setErpStoreId(sysUser.getOrgId());
            ordMasterExt.setOrderId(form.getOrderId());
            ordMasterExt.setOrderCode(form.getOrderCode());
            ordMasterExt.setOrderStatus(form.getOrderStatus());
            ordMasterExt.setPayStatus(form.getPayStatus());
            ordMasterExt.setDeliverStatus(form.getDeliverStatus());
            ordMasterExt.setNeedColumns(Integer.parseInt(form.getiDisplayLength()));
            ordMasterExt.setStartPoint(Integer.parseInt(form.getiDisplayStart()));
            ordMasterExt.setTelephone(form.getTelephone());
            ordMasterExt.setOrderTimeStart(form.getOrderTimeStart());
            ordMasterExt.setOrderTimeEnd(form.getOrderTimeEnd());
            ordMasterExt.setDeliverType(form.getDeliverType());
            ordMasterExt.setMemberName(form.getMemberName());
            ordMasterExt.setActionId(form.getActionId());
            ordMasterExt.setCouponId(form.getCouponId());
            ordMasterExt.setHelpBuy(form.getHelpBuy());
            ordMasterExt.setSalerId(form.getSalerId());
            ordMasterExt.setSalerName(form.getSalerName());
            ordMasterExt.setSalerTelephone(form.getSalerTelephone());
            //获取订单数据
            List<OrdMasterExt> list = ordMasterService.getAllDatas(ordMasterExt);
            //获取订单条数
            int ordCounts = ordMasterService.getAllDatasCount(ordMasterExt);
            json.put("aaData", list);
            json.put("sEcho", form.getsEcho());
            json.put("iTotalRecords", ordCounts);
            json.put("iTotalDisplayRecords", ordCounts);


            json.put("resultCode", Constants.NORMAL);
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    /**
     * 根据店铺ID获取订单列表信息
     * @param form
     * @return json
     */
    @RequestMapping("getSelfOrderList")
    public @ResponseBody
    JSONObject getSelfOrderList(OrdMasterForm form){
        JSONObject json = new JSONObject();

        try {

            SysUser sysUser = (SysUser) PamsDataContext.getSysUser();

            json = ordMasterService.getSelfOrderList(form, sysUser);

        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    @RequestMapping("edit")
    public @ResponseBody JSONObject edit(@RequestParam(required = false)String orderId){
        JSONObject json = new JSONObject();
        try{
            // 根据主键获取订单信息
            OrdMasterExt ordMaster = ordMasterService.selectByPrimaryKey(orderId);
            json.put("data",ordMaster);
            json.put("resultCode", Constants.NORMAL);
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    /**
     * 根据订单id获取订单商品信息
     * @param form
     * @return json
     */
    @RequestMapping("getOrderGoodsInfo")
    public @ResponseBody
    JSONObject getOrderGoodsInfo(OrdMasterForm form){
        JSONObject json = new JSONObject();
        try {
            //根据订单id获取商品信息
            List<OrdList> list = ordMasterService.selectOrderGoodsInfo(form.getOrderId());

            JSONArray array = new JSONArray();
            JSONObject jsonObject = null;
            for(OrdList ol : list){
                jsonObject = new JSONObject();
                jsonObject.put("detail_id",ol.getDetailId());
                jsonObject.put("type",ol.getType());
                jsonObject.put("goods_code",ol.getGoodsCode());
                jsonObject.put("goods_name",ol.getGoodsName());
                String skuJson = ol.getSku();
                String sku = "";
                if (skuJson != null && !"".equals(skuJson)) {
                    JSONArray skuArray = JSON.parseArray(skuJson);
                    if (skuArray != null && skuArray.size() > 0) {
                        for (int i = 0; i < skuArray.size(); i ++) {
                            JSONObject jsonO = (JSONObject)skuArray.get(i);
                            String color_name = (String)jsonO.get("color_name");
                            String size_name = (String)jsonO.get("size_name");
                            if ("".equals(sku)) {
                                sku += color_name + "" + size_name;
                            } else {
                                sku += ";" + color_name + "" + size_name;
                            }
                        }
                    }
                }
                jsonObject.put("sku",sku);
                jsonObject.put("quantity",ol.getQuantity());
                jsonObject.put("amount",ol.getAmount());
                jsonObject.put("amount_discount",ol.getAmountDiscount());
                jsonObject.put("action_name",ol.getActionName());
                array.add(jsonObject);
            }
            json.put("aaData", array);
            json.put("sEcho", form.getsEcho());
            json.put("iTotalRecords", list.size());
            json.put("iTotalDisplayRecords", list.size());

            json.put("resultCode", Constants.NORMAL);
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    /**
     * 根据订单id获取子订单信息
     * @param form
     * @return json
     */
    @RequestMapping("getOrderSubInfo")
    public @ResponseBody
    JSONObject getOrderSubInfo(OrdMasterForm form){
        JSONObject json = new JSONObject();
        try {
            //根据订单id获取商品信息
            List<OrdSubListExt> list = ordMasterService.selectOrderSubInfo(form.getOrderId());
            OrdMasterExt ordMasterExt = ordMasterService.selectByPrimaryKey(form.getOrderId());
            SysUser sysUser = (SysUser) PamsDataContext.getSysUser();
            for(OrdSubListExt osl : list){
                osl.setOrgId(sysUser.getOrgId());
                String skuJson = osl.getSku();
                String sku = "";
                if (skuJson != null && !"".equals(skuJson)) {
                    JSONObject jsonO = JSON.parseObject(skuJson);
                    String color_name = (String)jsonO.get("color_name");
                    String size_name = (String)jsonO.get("size_name");
                    sku += color_name + "" + size_name;
                }
                osl.setSku(sku);
                if(StringUtil.isEmpty(osl.getRexStatus())){
                    osl.setRexStatus("00");
                    osl.setRexStatusName("未退货");
                }

                if("20".equals(ordMasterExt.getDeliverType())){
                    osl.setDeliverType("门店自提");
                    osl.setExpressName("");
                }
            }
            json.put("aaData", list);
            json.put("sEcho", form.getsEcho());
            json.put("iTotalRecords", list.size());
            json.put("iTotalDisplayRecords", list.size());

            json.put("resultCode", Constants.NORMAL);
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    /**
     * 根据订单id获取订单商品信息
     * @param form
     * @return json
     */
    @RequestMapping("getOrderHistoryList")
    public @ResponseBody
    JSONObject getOrderHistoryList(OrdMasterForm form){
        JSONObject json = new JSONObject();
        try {
            //根据订单id获取订单履历
            List<OrdHistoryExt> list = ordMasterService.getOrderHistoryList(form.getOrderId());
            json.put("aaData", list);
            json.put("sEcho", form.getsEcho());
            json.put("iTotalRecords", list.size());
            json.put("iTotalDisplayRecords", list.size());
            json.put("resultCode", Constants.NORMAL);
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    /**
     * 获取退货信息
     * @param form
     * @return json
     */
    @RequestMapping("returnGoodsInfo")
    public @ResponseBody
    JSONObject returnGoodsInfo(OrdMasterForm form){
        JSONObject json = new JSONObject();
        try {
            //参数配置
            OrdReturnExchangeExt ordReturnExchangeExt = new OrdReturnExchangeExt();
            ordReturnExchangeExt.setOrderId(form.getOrderId());
            ordReturnExchangeExt.setSubOrderId(form.getSubOrderId());
            //根据订单id获取商品信息
            List<OrdReturnExchangeExt> list = ordMasterService.getReturnGoodsInfo(ordReturnExchangeExt);
            if(list == null || list.size() == 0){
                json.put("aaData", null);
                json.put("resultCode", Constants.NORMAL);
                return json;
            }else{
                JSONObject jsonObject = new JSONObject();
                OrdReturnExchangeExt ore = list.get(0);

                jsonObject.put("rexOrderId",ore.getRexOrderId());
                jsonObject.put("orderId",ore.getOrderId());
                jsonObject.put("subOrderId",ore.getSubOrderId());
                jsonObject.put("orderCode",ore.getOrderCode());
                jsonObject.put("memberId",ore.getMemberId());
                jsonObject.put("rexType",ore.getRexType());
                jsonObject.put("rexMode",ore.getRexMode());
                jsonObject.put("rexStatus",ore.getRexStatus());
                jsonObject.put("refundStatus",ore.getRefundStatus());
                jsonObject.put("refundGoods",ore.getRefundGoods());
                jsonObject.put("rexStatusCode",ore.getRexStatusCode());
                jsonObject.put("applyTime",ore.getApplyTime());
                jsonObject.put("applyComment",ore.getApplyComment());
                jsonObject.put("applyAnswerComment",ore.getApplyAnswerComment());

                json.put("data", jsonObject);
                json.put("resultCode", Constants.NORMAL);
            }
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    /**
     * 全单退货订单卖家确认收货
     * @param orderId
     * @return json
     */
    @RequestMapping("acceptReturnAllGoods")
    public @ResponseBody
    JSONObject acceptReturnAllGoods(String orderId){
        JSONObject json = new JSONObject();
        try {
            //退货商品卖家确认收货
            ordMasterService.acceptReturnAllGoods(orderId, PamsDataContext.get());
            json.put("resultCode", Constants.NORMAL);
        }catch (ExceptionBusiness eb){
            json.put("resultCode", Constants.INVALID);
            json.put("resultMessage", eb.getMessage());
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }


    /**
     * 拆单退货订单卖家确认收货
     * @param subOrderId
     * @return json
     */
    @RequestMapping("acceptReturnSubGoods")
    public @ResponseBody
    JSONObject acceptReturnSubGoods(String subOrderId){
        JSONObject json = new JSONObject();
        try {
            //拆单退货商品卖家确认收货
            ordMasterService.acceptReturnSubGoods(subOrderId, PamsDataContext.get());
            json.put("resultCode", Constants.NORMAL);
        }catch (ExceptionBusiness eb){
            json.put("resultCode", Constants.INVALID);
            json.put("resultMessage", eb.getMessage());
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    /**
     * 卖家确认退单
     * @param data
     * @return json
     */
    @RequestMapping("submitReturnMoney")
    public @ResponseBody
    JSONObject submitReturnMoney(String data){
        OrdReturnExchange ordReturnExchange = JSON.parseObject(data, OrdReturnExchange.class);
        JSONObject json = new JSONObject();
        try {
            //拆单退货商品卖家确认收货
            ordMasterService.submitReturnMoney(ordReturnExchange, PamsDataContext.get());
            json.put("resultCode", Constants.NORMAL);
        }catch (ExceptionBusiness eb){
            json.put("resultCode", Constants.INVALID);
            json.put("resultMessage", eb.getMessage());
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    /**
     * 卖家确认退单
     * @param data
     * @return json
     */
    @RequestMapping("getReturnInfo")
    public @ResponseBody
    JSONObject getReturnInfo(String data){
        OrdReturnExchangeExt ordReturnExchange = JSON.parseObject(data, OrdReturnExchangeExt.class);
        return ordMasterService.getReturnInfo(ordReturnExchange);
    }

    @RequestMapping("confirmReceiptInMaster")
    public @ResponseBody
    JSONObject confirmReceiptInMaster(String data){
        OrdMasterExt form = (OrdMasterExt) JSON.parseObject(data, OrdMasterExt.class);
        //取得用户登录信息
        Map<String, Object> userMap = PamsDataContext.get();
        //登录人
        String loginId = (String) userMap.get("loginId");
        form.setUpdateUserId(loginId);
        return ordMasterService.confirmReceiptInMaster(form);
    }


    /**
     * 获取线下订单的方法
     * @param data
     * @return json
     */
    @RequestMapping("getOffLineOrderList")
    public @ResponseBody
    JSONObject getOrderListOffLineByMemberIdForWeb(String data){
        return ordMasterService.getOrderListOffLineByMemberIdForWeb(data);
    }


    /**
     * excel导出
     * @return json
     */
    @RequestMapping("export")
    public void export(@RequestParam String orderId,
                       @RequestParam String orderCode
                    ,@RequestParam String actionId
                    ,@RequestParam String couponId
                    ,@RequestParam String orderStatus
                    ,@RequestParam String payStatus
                    ,@RequestParam String deliverStatus
                    ,@RequestParam String orderTimeStart
                    ,@RequestParam String orderTimeEnd
                    ,@RequestParam String telephone
                    ,@RequestParam String memberName
                    ,@RequestParam String deliverType
                    ,@RequestParam String helpBuy,
                       HttpServletResponse response){

        try{
            memberName = new String(memberName.getBytes("iso-8859-1"),"utf-8");

            OrdMasterExt ordMasterExt = new OrdMasterExt();
            ordMasterExt.setShopId(Constants.ORGID);
            //TODO 获取登陆者的组织ID,方法可能会变更
            SysUser sysUser = (SysUser) PamsDataContext.getSysUser();
            ordMasterExt.setErpStoreId(sysUser.getOrgId());
            ordMasterExt.setOrderId(orderId);
            ordMasterExt.setOrderCode(orderCode);
            ordMasterExt.setOrderStatus(orderStatus);
            ordMasterExt.setPayStatus(payStatus);
            ordMasterExt.setDeliverStatus(deliverStatus);
            ordMasterExt.setTelephone(telephone);
            ordMasterExt.setOrderTimeStart(orderTimeStart);
            ordMasterExt.setOrderTimeEnd(orderTimeEnd);
            ordMasterExt.setDeliverType(deliverType);
            ordMasterExt.setMemberName(memberName);
            ordMasterExt.setActionId(actionId);
            ordMasterExt.setCouponId(couponId);
            ordMasterExt.setHelpBuy(helpBuy);
            //获取订单数据
            List<OrdMasterExt> list = ordMasterService.excelExport(ordMasterExt);

            if(list != null){
                int rowNum = 0;
                HSSFWorkbook wb = new HSSFWorkbook();
                HSSFSheet sheet1 = wb.createSheet("主订单");
                HSSFSheet sheet2 = wb.createSheet("子订单");

                HSSFRow row1 = sheet1.createRow(0);
                HSSFRow row2 = sheet2.createRow(0);

                HSSFCellStyle columnTopStyle = this.getColumnTopStyle(wb);//获取列头样式对象

                String[] titles1 = new String[]{"订单ID","订单编码","会员名","会员电话","活动名称","原价","折扣价","优惠券名称","下单时间","省","市","区","地址"};
                for(int i=0;i<titles1.length;i++){
                    HSSFCell  cellRowName = row1.createCell(i);               //创建列头对应个数的单元格
                    cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING);             //设置列头单元格的数据类型
                    HSSFRichTextString text = new HSSFRichTextString(titles1[i]);
                    cellRowName.setCellValue(text);                                 //设置列头单元格的值
                    cellRowName.setCellStyle(columnTopStyle);
                }

                String[] titles2 = new String[]{"子订单ID","订单ID","商品名","原价","折扣价","件数","活动名称","下单时间","发货门店名称","快递公司名称","快递单号"};
                for(int i=0;i<titles2.length;i++){
                    HSSFCell cellRowName = row2.createCell(i);               //创建列头对应个数的单元格
                    cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING);             //设置列头单元格的数据类型
                    HSSFRichTextString text = new HSSFRichTextString(titles2[i]);
                    cellRowName.setCellValue(text);                                 //设置列头单元格的值
                    cellRowName.setCellStyle(columnTopStyle);
                }

                for(int m=0;m<list.size();m++){
                    OrdMasterExt temp = list.get(m);
                    HSSFRow row = sheet1.createRow(m+1);

                    HSSFCell cell = null;   //设置单元格的数据类型
                    cell = row.createCell(0,HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(temp.getOrderId());

                    cell = row.createCell(1,HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(temp.getOrderCode());

                    cell = row.createCell(2,HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(temp.getMemberName());

                    cell = row.createCell(3,HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(temp.getTelephone());

                    cell = row.createCell(4,HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(temp.getActionName());

                    cell = row.createCell(5,HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(String.valueOf(temp.getAmountTotle()));

                    cell = row.createCell(6,HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(String.valueOf(temp.getPayInfact()));

                    cell = row.createCell(7,HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(temp.getCouponName());

                    cell = row.createCell(8,HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(temp.getInsertTimeString());

                    cell = row.createCell(9,HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(temp.getDistrictidProvince());

                    cell = row.createCell(10,HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(temp.getDistrictidCity());

                    cell = row.createCell(11,HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(temp.getDistrictidDistrict());

                    cell = row.createCell(12,HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(temp.getDeliveryAddress());

                    List<OrdSubListExt> subList = temp.getOrdSubListExtList();
                    if(subList != null){
                        for(int n=0;n<subList.size();n++){
                            OrdSubListExt subTemp = subList.get(n);

                            rowNum += 1;
                            HSSFRow row3 = sheet2.createRow(rowNum);

                            HSSFCell cell1 = null;   //设置单元格的数据类型
                            cell1 = row3.createCell(0,HSSFCell.CELL_TYPE_STRING);
                            cell1.setCellValue(subTemp.getSubOrderId());

                            cell1 = row3.createCell(1,HSSFCell.CELL_TYPE_STRING);
                            cell1.setCellValue(subTemp.getOrderId());

                            cell1 = row3.createCell(2,HSSFCell.CELL_TYPE_STRING);
                            cell1.setCellValue(subTemp.getGoodsName());

                            cell1 = row3.createCell(3,HSSFCell.CELL_TYPE_STRING);
                            cell1.setCellValue(String.valueOf(subTemp.getPrice()));

                            cell1 = row3.createCell(4,HSSFCell.CELL_TYPE_STRING);
                            cell1.setCellValue(String.valueOf(subTemp.getPriceDiscount()==null?0:subTemp.getPriceDiscount()));

                            cell1 = row3.createCell(5,HSSFCell.CELL_TYPE_STRING);
                            cell1.setCellValue(subTemp.getQuantity());

                            cell1 = row3.createCell(6,HSSFCell.CELL_TYPE_STRING);
                            cell1.setCellValue(subTemp.getActionName());

                            cell1 = row3.createCell(7,HSSFCell.CELL_TYPE_STRING);
                            cell1.setCellValue(subTemp.getInsertTimeString());

                            cell1 = row3.createCell(8,HSSFCell.CELL_TYPE_STRING);
                            cell1.setCellValue(subTemp.getStoreName());

                            cell1 = row3.createCell(9,HSSFCell.CELL_TYPE_STRING);
                            if("20".equals(temp.getDeliverType())){
                                cell1.setCellValue("");
                            }else{
                                cell1.setCellValue(subTemp.getExpressName());
                            }

                            cell1 = row3.createCell(10,HSSFCell.CELL_TYPE_STRING);
                            if("20".equals(temp.getDeliverType())){
                                cell1.setCellValue("");
                            }else{
                                cell1.setCellValue(subTemp.getExpressNo());
                            }
                        }


                    }
                }
                String fileName = "Excel-" + String.valueOf(System.currentTimeMillis()).substring(4, 13) + ".xls";
                String headStr = "attachment; filename=\"" + fileName + "\"";

                response.setContentType("APPLICATION/OCTET-STREAM");
                response.setHeader("Content-Disposition", headStr);
                OutputStream out = response.getOutputStream();
                wb.write(out);
            }


        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
    * 列头单元格样式
    */
    public HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {

        // 设置字体
        HSSFFont font = workbook.createFont();
        //设置字体大小
        font.setFontHeightInPoints((short)11);
        //字体加粗
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        //设置字体名字
        font.setFontName("Courier New");
        //设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        //设置底边框;
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        //设置底边框颜色;
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        //设置左边框;
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        //设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        //设置右边框;
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        //设置右边框颜色;
        style.setRightBorderColor(HSSFColor.BLACK.index);
        //设置顶边框;
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        //设置顶边框颜色;
        style.setTopBorderColor(HSSFColor.BLACK.index);
        //在样式用应用设置的字体;
        style.setFont(font);
        //设置自动换行;
        style.setWrapText(false);
        //设置水平对齐的样式为居中对齐;
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        return style;

    }

    /*
        * 列数据信息单元格样式
        */
    public HSSFCellStyle getStyle(HSSFWorkbook workbook) {
        // 设置字体
        HSSFFont font = workbook.createFont();
        //设置字体大小
        //font.setFontHeightInPoints((short)10);
        //字体加粗
        //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        //设置字体名字
        font.setFontName("Courier New");
        //设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        //设置底边框;
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        //设置底边框颜色;
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        //设置左边框;
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        //设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        //设置右边框;
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        //设置右边框颜色;
        style.setRightBorderColor(HSSFColor.BLACK.index);
        //设置顶边框;
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        //设置顶边框颜色;
        style.setTopBorderColor(HSSFColor.BLACK.index);
        //在样式用应用设置的字体;
        style.setFont(font);
        //设置自动换行;
        style.setWrapText(false);
        //设置水平对齐的样式为居中对齐;
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        return style;

    }
}
