package net.dlyt.qyds.web.controller.forerp;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.common.StringUtil;
import net.dlyt.qyds.web.service.ErpProduceLineService;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by zlh on 2016/7/30.
 */
@Controller
@RequestMapping("/erp_produce_line")
public class ErpProduceLineController {

    @Resource
    private ErpProduceLineService service;
    protected final Logger log = LoggerFactory.getLogger(ErpProduceLineService.class);

//    @RequestMapping("input.json")
//    @ResponseBody
//    //batch:1为整体导入，删除所以旧数据重新导入，0为部分更新，列表数据中只包括变化的数据，需要参照style处理
//    public JSONObject input(@RequestParam("data") String data, @RequestParam("batch") int batch){
//        JSONObject map = new JSONObject();
//        try{
//            if (batch == 1) {
//                //整体导入
//                map = service.inputAll(data);
//            } else if (batch == 0) {
//                //部分更新
//                map = service.updateByList(data);
//            } else {
//                throw new Exception("参数错误");
//            }
//        }catch(Exception e){
//            map.put("resultCode",Constants.FAIL);
//            map.put("message", e.getMessage());
//        }
//        return map;
//    }

    @RequestMapping("input.json")
    @ResponseBody
    //batch:1为整体导入，删除所以旧数据重新导入，0为部分更新，列表数据中只包括变化的数据，需要参照style处理
    public JSONObject input(@RequestParam("data") String data, @RequestParam("batch") int batch,@RequestParam(required = false)String key){
        JSONObject map = new JSONObject();
        try{
            data = StringUtil.getErpData(data,key);
            log.debug("param:"+ data);
            if (batch == 1) {
                //整体导入
                map = service.inputAll(data);
            } else if (batch == 0) {
                //部分更新
                map = service.updateByList(data);
            } else {
                throw new Exception("参数错误");
            }
        }catch(Exception e){
            map.put("resultCode",Constants.FAIL);
            map.put("message", e.getMessage());
        }
        log.debug("result:"+map);
        return map;
    }
}
