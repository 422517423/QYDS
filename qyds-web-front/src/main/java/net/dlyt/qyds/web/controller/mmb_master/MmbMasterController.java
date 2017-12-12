package net.dlyt.qyds.web.controller.mmb_master;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.MmbMaster;
import net.dlyt.qyds.common.dto.ext.MmbMasterExt;
import net.dlyt.qyds.common.form.MmbMasterForm;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.context.PamsDataContext;
import net.dlyt.qyds.web.service.MmbMasterService;
import net.dlyt.qyds.web.service.exception.ExceptionErrorParam;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by C_Nagai on 2016/7/27.
 */
@Controller
@RequestMapping("/mmb_master")
public class MmbMasterController {

    @Autowired
    private MmbMasterService mmbMasterService;


    @RequestMapping("getList")
    public
    @ResponseBody
    JSONObject getList(MmbMasterForm form) {
        JSONObject json = new JSONObject();
        try {
            MmbMasterExt ext = new MmbMasterExt();
            ext.setNeedColumns(Integer.parseInt(form.getiDisplayLength()));
            ext.setStartPoint(Integer.parseInt(form.getiDisplayStart()));
            ext.setMemberName(form.getMemberName());
            ext.setType(form.getType());
            ext.setTelephone(form.getTelephone());
            ext.setMemberLevelId(form.getMemberLevelId());
            ext.setDeleted(form.getDeleted());
            ext.setIsValid(form.getIsValid());
            if (!StringUtils.isEmpty(form.getDistrictCode())) {
                ext.setDistrictCode(form.getDistrictCode());
            } else if (!StringUtils.isEmpty(form.getCityCode())) {
                ext.setCityCode(form.getCityCode());
            } else if (!StringUtils.isEmpty(form.getProvinceCode())) {
                ext.setProvinceCode(form.getProvinceCode());
            }
            ext.setStartDate(form.getStartDate());
            ext.setEndDate(form.getEndDate());

            List<MmbMasterExt> list = mmbMasterService.selectAll(ext);
            int allCount = mmbMasterService.getAllDataCount(ext);

            json.put("sEcho", form.getsEcho());
            json.put("iTotalRecords", allCount);
            json.put("iTotalDisplayRecords", allCount);
            json.put("aaData", list);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }


    @RequestMapping("detail")
    public
    @ResponseBody
    JSONObject detail(String data) {

        JSONObject json = new JSONObject();
        try {
            JSONObject param = JSONObject.parseObject(data);

            if (param == null) {
                throw new ExceptionErrorParam("参数为空");
            }

            MmbMaster mmbMaster = new MmbMaster();
            mmbMaster.setMemberId(param.getString("memberId"));
            json = mmbMasterService.selectBySelective(mmbMaster);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }

        return json;

    }

    @RequestMapping("edit")
    public
    @ResponseBody
    JSONObject edit(String data) {
        JSONObject json = new JSONObject();
        try {
            MmbMaster mmbMaster = (MmbMaster) JSON.parseObject(data, MmbMaster.class);

            if (mmbMaster == null) {
                throw new ExceptionErrorParam("参数为空");
            }

            if (StringUtils.isEmpty(mmbMaster.getMemberId())) {
                throw new ExceptionErrorParam("缺少主键信息");
            } else {
                json = mmbMasterService.updateByPrimaryKeySelective(mmbMaster);
            }
        } catch (Exception e) {

            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;

    }


    @RequestMapping("save")
    public
    @ResponseBody
    JSONObject save(String data) {
        JSONObject json = new JSONObject();
        try {
            MmbMaster mmbMaster = (MmbMaster) JSON.parseObject(data, MmbMaster.class);

            if (mmbMaster == null) {
                throw new ExceptionErrorParam("参数为空");
            }

            String telephone = mmbMaster.getTelephone();
            String memberName = mmbMaster.getMemberName();
            Date date = mmbMaster.getBirthdate();
            if (StringUtils.isEmpty(telephone)
                    || StringUtils.isEmpty(memberName)
                    || date == null) {
                throw new ExceptionErrorParam("提交的注册信息不足");
            }

            mmbMaster.setSex(StringUtils.isEmpty(mmbMaster.getSex()) ? "0" : mmbMaster.getSex());
            mmbMaster.setRegistStyle("30");//30	门店		注册方式-门店
            mmbMaster.setMemberLevelId("10");
//            mmbMaster.setPassword();
            json = mmbMasterService.registerUser(mmbMaster);

        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;

    }

//    /**
//     * 默认密码使用生日,如果没有生日信息则使用123456
//     *
//     * @param birthDate
//     * @return
//     */
//    private String password(Date birthDate) {
//        String rs = "123456";
//        if (!StringUtils.isEmpty(birthDate)) {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//            rs = sdf.format(birthDate);
//        }
//        return rs;
//    }

    @RequestMapping("delete")
    public
    @ResponseBody
    JSONObject delete(String data) {
        JSONObject json = new JSONObject();
        try {
            MmbMaster mmbMaster = (MmbMaster) JSON.parseObject(data, MmbMaster.class);
            json = mmbMasterService.deleteByPrimaryKey(mmbMaster);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    @RequestMapping("undelete")
    public
    @ResponseBody
    JSONObject undelete(String data) {
        JSONObject json = new JSONObject();
        try {
            MmbMaster mmbMaster = (MmbMaster) JSON.parseObject(data, MmbMaster.class);
            json = mmbMasterService.undeleteByPrimaryKey(mmbMaster);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }


    /**
     * ERP地址列表取得
     *
     * @param mmbMaster
     * @return
     */
    @RequestMapping("getAddressList")
    @ResponseBody
    public JSONObject getAddressList(MmbMaster mmbMaster) {
        JSONObject json = new JSONObject();
        try {
            json = mmbMasterService.getAddressList(mmbMaster);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;

    }

    @RequestMapping("changeGrade")
    public
    @ResponseBody
    JSONObject changeGrade(String data) {
        JSONObject json = new JSONObject();
        try {
            MmbMaster mmbMaster = (MmbMaster) JSON.parseObject(data, MmbMaster.class);
            if (mmbMaster == null) {
                throw new ExceptionErrorParam("参数为空");
            }
            if (StringUtils.isEmpty(mmbMaster.getMemberId())) {
                throw new ExceptionErrorParam("会员ID未指定");
            }
            if (StringUtils.isEmpty(mmbMaster.getMemberLevelId())) {
                throw new ExceptionErrorParam("会员级别未指定");
            }
            //取得用户登录信息
            Map<String, Object> userMap = PamsDataContext.get();
            //登录人
            String loginId = (String) userMap.get("loginId");
            //设定用户信息
            mmbMaster.setUpdateUserId(loginId);
            json = mmbMasterService.changeGrade(mmbMaster);

        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;

    }
    /**
     * excel导出
     * @return json
     */
    @RequestMapping("export")
    public void export(@RequestParam String memberName,
                       @RequestParam String type
            ,@RequestParam String telephone
            ,@RequestParam String memberLevelId
            ,@RequestParam String deleted
            ,@RequestParam String startDate
            ,@RequestParam String endDate
            ,@RequestParam String provinceCode
            ,@RequestParam String cityCode
            ,@RequestParam String districtCode,HttpServletResponse response){

        try{
            memberName = new String(memberName.getBytes("iso-8859-1"),"utf-8");

            MmbMasterExt ext = new MmbMasterExt();
            ext.setMemberName(memberName);
            ext.setType(type);
            ext.setTelephone(telephone);
            ext.setMemberLevelId(memberLevelId);
            ext.setDeleted(deleted);
            if (!StringUtils.isEmpty(districtCode) && !districtCode.equals("null")) {
                ext.setDistrictCode(districtCode);
            } else if (!StringUtils.isEmpty(cityCode) && !cityCode.equals("null")) {
                ext.setCityCode(cityCode);
            } else if (!StringUtils.isEmpty(provinceCode) && !provinceCode.equals("")) {
                ext.setProvinceCode(provinceCode);
            }
            ext.setStartDate(startDate);
            ext.setEndDate(endDate);
            //获取订单数据
            List<MmbMasterExt> list = mmbMasterService.export(ext);

            if(list != null){
                int rowNum = 0;
                HSSFWorkbook wb = new HSSFWorkbook();
                HSSFSheet sheet1 = wb.createSheet("会员列表");

                HSSFRow row1 = sheet1.createRow(0);

                HSSFCellStyle columnTopStyle = this.getColumnTopStyle(wb);//获取列头样式对象

                String[] titles1 = new String[]{"会员级别","会员类型","注册日期","姓名","可用积分","累计积分","性别","微信绑定","手机号","邮箱","省","市","区"};
                for(int i=0;i<titles1.length;i++){
                    HSSFCell cellRowName = row1.createCell(i);               //创建列头对应个数的单元格
                    cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING);             //设置列头单元格的数据类型
                    HSSFRichTextString text = new HSSFRichTextString(titles1[i]);
                    cellRowName.setCellValue(text);                                 //设置列头单元格的值
                    cellRowName.setCellStyle(columnTopStyle);
                }


                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

                for(int m=0;m<list.size();m++){
                    MmbMasterExt temp = list.get(m);
                    HSSFRow row = sheet1.createRow(m+1);

                    HSSFCell cell = null;   //设置单元格的数据类型
                    cell = row.createCell(0,HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(temp.getMemberLevelName());

                    cell = row.createCell(1,HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(temp.getTypeName());

                    cell = row.createCell(2,HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(df.format(temp.getInsertTime()));

                    cell = row.createCell(3,HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(temp.getMemberName());

                    cell = row.createCell(4,HSSFCell.CELL_TYPE_NUMERIC);
                    cell.setCellValue(temp.getPoint());

                    cell = row.createCell(5,HSSFCell.CELL_TYPE_NUMERIC);
                    cell.setCellValue(temp.getAllPoint());

                    cell = row.createCell(6,HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(temp.getSexName());

                    cell = row.createCell(7,HSSFCell.CELL_TYPE_STRING);
                    if(!StringUtils.isEmpty(temp.getOpenId())){
                        cell.setCellValue("是");
                    }else{
                        cell.setCellValue("否");
                    }

                    cell = row.createCell(8,HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(temp.getTelephone());

                    cell = row.createCell(9,HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(temp.getEmail());

                    cell = row.createCell(10,HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(temp.getProvinceName());

                    cell = row.createCell(11,HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(temp.getCityName());

                    cell = row.createCell(12,HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(temp.getDistrictName());

                }
                String fileName = "Excel_MEMBER.xls";
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