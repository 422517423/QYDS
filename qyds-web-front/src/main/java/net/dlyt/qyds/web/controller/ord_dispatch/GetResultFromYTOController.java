package net.dlyt.qyds.web.controller.ord_dispatch;

import net.dlyt.qyds.common.dto.YtoForm;
import net.dlyt.qyds.web.service.ErpSendService;
import net.dlyt.qyds.web.service.GetYtoService;
import net.dlyt.qyds.web.service.common.YtUtil.YtApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GetResultFromYTOController {
    @Autowired
    private GetYtoService getYtoService;

    @Autowired
    private ErpSendService erpSendService;

    /**
     * 物流状态通知接口
     * @param ytoForm
     * @return
     * @throws Exception
     */

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



   /* *//**
     * 物流状态通知接口
     * @return
     * @throws Exception
     *//*
    @RequestMapping(value = "/resultFromYTO")
    @ResponseBody
    public String getYtoInfo1(String logistics_interface, String data_digest, String clientId, String type) throws Exception {
        YtoForm ytoForm = new YtoForm();
        ytoForm.setLogistics_interface(logistics_interface);
        ytoForm.setData_digest(data_digest);
        ytoForm.setClientId(clientId);
        ytoForm.setType(type);
        System.out.println("圆通调用");
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
        }else{
        }
        builders.append("</Response>");
        return builders.toString();
    }*/


    /**
     * 物流状态通知接口
     * @param logistics_interface
     * @param data_digest
     * @param clientId
     * @param type
     * @return
     * @throws Exception
     */
    /*@RequestMapping(value = "/resultFromYTO")
    @ResponseBody
    public String getYtoInfo1(String logistics_interface, String data_digest, String clientId, String type) throws Exception {
        System.out.println("圆通调用");
        String txLogisticID = getTxLogisticID(logistics_interface);
        //判断请求内容中是否有运单号，返回boolean值
        boolean flag = getYtoInfo(logistics_interface);

        // 返回成功/失败数据
        StringBuilder builders = new StringBuilder();
        builders.append ("<Response>");
        builders.append("<logisticProviderID>YTO</logisticProviderID>");
        builders.append("<txLogisticID> " + txLogisticID + "</txLogisticID>");
        builders.append("<success>" + flag + "</success>");

        if (!flag){
            builders.append("<reason>没有物流号huo</reason>");
        }
        builders.append("</Response>");
        return builders.toString();
    }

    public String getTxLogisticID(String logistics_interface) throws Exception {
        if (logistics_interface!=null){
            String param = logistics_interface;
            param = URLUtil.getURLDecoderString(param);
            if (param!=null&&!param.equals("")){
                Map<String, Object> map = XMLUtil.xml2Map(param);
                if (map!=null){
                    //处理map
                    String txLogisticID = (String) map.get("txLogisticID");
                    return txLogisticID;
                }
            }
        }
        return "";
    }

    public boolean getYtoInfo(String logistics_interface) throws Exception {
        if (logistics_interface!=null){
            String param = logistics_interface;
            param = URLUtil.getURLDecoderString(param);
            if (param!=null&&!param.equals("")){
                Map<String, Object> map = XMLUtil.xml2Map(param);
                if (map!=null){
                    //处理map
                    String mailNo = (String) map.get("mailNo");
                    String txLogisticID = (String) map.get("txLogisticID");
                    // TODO: 2017/12/5 根据txLogisticID获取单个订单
                    if (StringUtils.isNotBlank(mailNo) && StringUtils.isNotBlank(txLogisticID)){
                        // 根据物流号先查询子订单信息
                        System.out.println("运单号为"+mailNo);
                        return true;
                    }else {
                        return true;
                    }
                }
            }
        }
        return false;
    }
  */


    /**
     * 取消圆通订单接口
     * @param orderId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/cancelYto")
    @ResponseBody
    public String cancelYto(String orderId) throws Exception {
        String result = YtApi.cancelYto(orderId);
        System.out.println(result);
        return result;
    }

   /* @RequestMapping(value = "/sendFailExpress")
    @ResponseBody
    public void sendFailExpress(){
        erpSendService.sendFailExpress();
    }*/
}
