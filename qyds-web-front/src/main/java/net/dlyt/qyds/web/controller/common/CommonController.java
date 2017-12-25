package net.dlyt.qyds.web.controller.common;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ComCode;
import net.dlyt.qyds.config.FileServerConfig;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.service.ComCodeService;
import net.dlyt.qyds.web.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by congkeyan on 2016/7/19.
 */
@Controller
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private CommonService commonService;

    @Autowired
    private FileServerConfig fileServerConfig;

    @Autowired
    private ComCodeService comCodeService;

    /**
     * 根据码表类型获取对应的码表列表
     * @param data
     * @return
     */
    @RequestMapping("getCodeList")
    public @ResponseBody
    JSONObject getList(String data){
        JSONObject json = new JSONObject();

        try{
            if (!StringUtils.isEmpty(data)) {
                List<ComCode> list = commonService.selectComCodeByCategory(data);
                json.put("data", list);
            }

            json.put("resultCode", Constants.NORMAL);
    }catch(Exception e){
        json.put("resultCode", Constants.FAIL);
    }
        return json;
    }

//    /**
//     * 上传图片保存图片,返回给前端访问图片的url
//     * @param form
//     * @return
//     */
//    @RequestMapping("uploadImage")
//    public @ResponseBody
//    JSONObject uploadImage(UploadImageForm form){
//
//        JSONObject json = new JSONObject();
//        try{
//            String suffix = form.getSuffix();
//            String id = UUID.randomUUID().toString();
//            String savedPath = fileServerConfig.getStoragePath() + "/orignal";
//            String subPath = "/" + form.getType() + "/" + id.substring(0, 2) + "/";
//            String fileName =  id + "." + suffix;
//            // 原始图
//            String temp = form.getFile().split("base64,")[1];
//            FileOperator.decoderBase64File(temp, savedPath + subPath, fileName);
//            String tagetPath =  id + "." + suffix;
//
//            // 缩略图做成
//            QydsSchedulerFactory.doThumbnail(subPath + fileName);
//
////            json.put("url",id + "." + suffix);
////            json.put("data", tagetPath);
//
//            json.put("url",subPath + fileName);
//            json.put("data", subPath + fileName);
//            json.put("resultCode", Constants.NORMAL);
//
//        }catch(Exception e){
//            json.put("resultCode", Constants.FAIL);
//        }
//        return json;
//    }

    @RequestMapping("getUploadUrl")
    public @ResponseBody
    JSONObject getUploadUrl(){
        JSONObject json = new JSONObject();
        json.put("data", fileServerConfig.getUploadContext() + "/common/uploadImage.json");
        json.put("resultCode", Constants.NORMAL);
        return json;
    }


    @RequestMapping("getImageUrl")
    public @ResponseBody
    JSONObject getImageUrl(){
        JSONObject json = new JSONObject();
        json.put("data", fileServerConfig.getUrlContext());
        json.put("resultCode", Constants.NORMAL);
        return json;
    }

    /**
     * 根据码表类型获取对应的码表列表
     * @param data
     * @return
     */
    @RequestMapping("getCodeListByCodeString")
    public @ResponseBody
    JSONObject getCodeListByCodeString(String data,String value){
        JSONObject json = new JSONObject();

        try{
            if (!StringUtils.isEmpty(data) && !StringUtils.isEmpty(value)) {
                json = comCodeService.selectByValueString(data,value);
            }

            json.put("resultCode", Constants.NORMAL);
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

}
