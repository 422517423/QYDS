package net.dlyt.qyds.web.controller.excel;

import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by zlh on 2016/12/14.
 */
@Controller
@RequestMapping("/excel_download")
public class ExcelDownloadController {

    @RequestMapping("test")
    public @ResponseBody
    void test(HttpServletRequest request, HttpServletResponse response){
        try{
            HSSFWorkbook workbook = new HSSFWorkbook();                     // 创建工作簿对象
            HSSFSheet sheet = workbook.createSheet("导出");                  // 创建工作表

            // 产生表格标题行
            HSSFRow rowm = sheet.createRow(0);
            HSSFCell cellTiltle = rowm.createCell(0);

            //sheet样式定义【getColumnTopStyle()/getStyle()均为自定义方法 - 在下面  - 可扩展】
            HSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook);//获取列头样式对象
            HSSFCellStyle style = this.getStyle(workbook);                  //单元格样式对象

//            sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, (rowName.length-1)));
            cellTiltle.setCellStyle(columnTopStyle);
//            cellTiltle.setCellValue(title);

            // 定义所需列数
//            int columnNum = rowName.length;
            HSSFRow rowRowName = sheet.createRow(0);                // 在索引2的位置创建行(最顶端的行开始的第二行)

            // 将列头设置到sheet的单元格中
//            for(int n=0;n<columnNum;n++){
//                HSSFCell  cellRowName = rowRowName.createCell(n);               //创建列头对应个数的单元格
//                cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING);             //设置列头单元格的数据类型
//                HSSFRichTextString text = new HSSFRichTextString(rowName[n]);
//                cellRowName.setCellValue(text);                                 //设置列头单元格的值
//                cellRowName.setCellStyle(columnTopStyle);                       //设置列头单元格样式
//            }

            //将查询出的数据设置到sheet对应的单元格中
//            for(int i=0;i<datas.size();i++){
//
//                TUserWishExt dto = datas.get(i);//遍历每个对象
//                HSSFRow row = sheet.createRow(i+1);//创建所需的行数
//
//                HSSFCell  cell = null;   //设置单元格的数据类型
//                cell = row.createCell(0,HSSFCell.CELL_TYPE_NUMERIC);
//                cell.setCellValue(dto.getId());
//
//                cell = row.createCell(1,HSSFCell.CELL_TYPE_NUMERIC);
//                cell.setCellValue(dto.getGomeUserId());
//
//                cell = row.createCell(2,HSSFCell.CELL_TYPE_NUMERIC);
//                cell.setCellValue(dto.getName());
//
//                cell = row.createCell(3,HSSFCell.CELL_TYPE_NUMERIC);
//                cell.setCellValue(dto.getPhone());
//
//                cell = row.createCell(4,HSSFCell.CELL_TYPE_STRING);
//                cell.setCellValue(dto.getHouseAddress());
//
//                cell = row.createCell(5,HSSFCell.CELL_TYPE_NUMERIC);
//                cell.setCellValue(dto.getHouseType());
//
//                cell = row.createCell(6,HSSFCell.CELL_TYPE_NUMERIC);
//                cell.setCellValue(dto.getArea());
//
//                cell = row.createCell(7,HSSFCell.CELL_TYPE_NUMERIC);
//                cell.setCellValue(dto.getHouseStatus());
//
//                cell = row.createCell(8,HSSFCell.CELL_TYPE_STRING);
//                cell.setCellValue(dto.getPackageName());
//
//                cell = row.createCell(9,HSSFCell.CELL_TYPE_STRING);
//                cell.setCellValue(dto.getProviderName());
//
//                cell = row.createCell(10,HSSFCell.CELL_TYPE_STRING);
//                cell.setCellValue(dto.getPrice()==null?0:dto.getPrice());
//
//                cell = row.createCell(11,HSSFCell.CELL_TYPE_STRING);
//                cell.setCellValue(dto.getStatus_name());
//
//                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
//                cell = row.createCell(12,HSSFCell.CELL_TYPE_STRING);
//                if(dto.getcAddon() != null){
//                    cell.setCellValue(sdf.format(dto.getcAddon()));
//                }else{
//                    cell.setCellValue("");
//                }
//
//                cell = row.createCell(13,HSSFCell.CELL_TYPE_STRING);
//                if(dto.getCheckedUser() == null){
//                    cell.setCellValue("未处理");
//                }else{
//                    cell.setCellValue("已处理");
//                }
//
//                cell = row.createCell(14,HSSFCell.CELL_TYPE_STRING);
//                cell.setCellValue(dto.getCheckedUser());
//
//                cell = row.createCell(15,HSSFCell.CELL_TYPE_STRING);
//                if(dto.getCheckedRecordStr() != null){
//                    cell.setCellValue(dto.getCheckedRecordStr());
//                }else{
//                    cell.setCellValue("");
//                }
//            }
//            //让列宽随着导出的列长自动适应
//            for (int colNum = 0; colNum < columnNum; colNum++) {
//                int columnWidth = sheet.getColumnWidth(colNum) / 256;
//                for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
//                    HSSFRow currentRow;
//                    //当前行未被使用过
//                    if (sheet.getRow(rowNum) == null) {
//                        currentRow = sheet.createRow(rowNum);
//                    } else {
//                        currentRow = sheet.getRow(rowNum);
//                    }
//                    if (currentRow.getCell(colNum) != null) {
//                        HSSFCell currentCell = currentRow.getCell(colNum);
//                        if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
//                            int length = currentCell.getStringCellValue().getBytes().length;
//                            if (columnWidth < length) {
//                                columnWidth = length;
//                            }
//                        }
//                    }
//                }
//                if(colNum == 0){
//                    sheet.setColumnWidth(colNum, (columnWidth-2) * 256);
//                }else{
//                    sheet.setColumnWidth(colNum, (columnWidth+4) * 256);
//                }
//            }

            if(workbook !=null){
                try
                {
                    String fileName = "Excel-" + String.valueOf(System.currentTimeMillis()).substring(4, 13) + ".xls";
                    String headStr = "attachment; filename=\"" + fileName + "\"";
//                    response = getResponse();

//                    FacesContext context = FacesContext.getCurrentInstance();
//                    ExternalContext ec = context.getExternalContext();
//                    HttpServletResponse response = (HttpServletResponse)ec.getResponse();


                    response.setContentType("APPLICATION/OCTET-STREAM");
                    response.setHeader("Content-Disposition", headStr);
                    OutputStream out = response.getOutputStream();
                    workbook.write(out);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }
//        return new JSONObject();

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
