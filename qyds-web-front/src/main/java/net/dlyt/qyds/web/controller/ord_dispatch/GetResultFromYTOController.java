package net.dlyt.qyds.web.controller.ord_dispatch;

import net.dlyt.qyds.common.dto.Response;
import net.dlyt.qyds.common.dto.YtoForm;
import net.dlyt.qyds.web.service.GetYtoService;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GetResultFromYTOController {
    @Autowired
    private GetYtoService getYtoService;

    @RequestMapping(value = "/resultFromYTO")
    @ResponseBody
    public String getYtoInfo(YtoForm ytoForm) throws Exception {
        System.out.println("圆通调用");
        //Response response = new Response();
        //response.setLogisticProviderID("YTO");
        //设置物流号(从ytoform的值中获取)
        //service 中加一个解析后获取物流号的方法
        String txLogisticID = getYtoService.getTxLogisticID(ytoForm);
        //response.setTxLogisticID(txLogisticID);
        //判断请求内容中是否有运单号，返回boolean值
        boolean flag = getYtoService.getYtoInfo(ytoForm);

        // 返回成功/失败数据
        StringBuilder builders = new StringBuilder();
        builders.append ("<Response>");
        builders.append("<logisticProviderID>YTO</logisticProviderID>");
        builders.append("<txLogisticID> " + txLogisticID + "</txLogisticID>");
        builders.append("<success>" + flag + "</success>");

        if (!flag){
            builders.append("<reason>S01</reason>");
            //response.setSuccess(true);
            //return response;
        }else{
            //response.setSuccess(false);
            //return response;
        }

        builders.append("</Response>");

        return builders.toString();
    }
}
