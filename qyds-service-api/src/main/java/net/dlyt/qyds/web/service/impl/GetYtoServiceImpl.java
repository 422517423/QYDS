package net.dlyt.qyds.web.service.impl;

import me.chanjar.weixin.common.util.StringUtils;
import me.chanjar.weixin.mp.util.URLUtil;
import me.chanjar.weixin.mp.util.xml.XMLUtil;
import net.dlyt.qyds.common.dto.*;
import net.dlyt.qyds.dao.OrdLogisticStatusMapper;
import net.dlyt.qyds.dao.OrdSubListMapper;
import net.dlyt.qyds.dao.OrdTransferListMapper;
import net.dlyt.qyds.dao.ext.OrdSubListMapperExt;
import net.dlyt.qyds.web.service.GetYtoService;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.common.ErpSendUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service("getYtoService")
@Transactional(readOnly = true)
public class GetYtoServiceImpl implements GetYtoService {

    protected final Logger log = LoggerFactory.getLogger(GetYtoServiceImpl.class);
    @Autowired
    private OrdSubListMapperExt ordSubListMapperExt;
    @Autowired
    private OrdSubListMapper ordSubListMapper;
    @Autowired
    private OrdTransferListMapper ordTransferListMapper;
    @Autowired
    private OrdLogisticStatusMapper ordLogisticStatusMapper;

    private static final String HEAD = "head";
    private static final String BODY = "body";

    @Override
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public boolean getYtoInfo(YtoForm ytoForm) throws Exception {
        /*
        return：如果成功接收，返回xml成功，否则返回xml：失败
        处理：1.先进行url解码
             2.然后将xml转换为map格式
             3.通过key&value进行取值
         */
        if (ytoForm!=null){
            OrdLogisticStatus ordLogisticStatus = new OrdLogisticStatus();
            String param = ytoForm.getLogistics_interface();
            param = URLUtil.getURLDecoderString(param);
            if (param!=null&&!param.equals("")){
                Map<String, Object> map = XMLUtil.xml2Map(param);
                if (map!=null){
                    //处理map
                    String mailNo = (String) map.get("mailNo");
                    String txLogisticID = (String) map.get("txLogisticID");
                    String infoContent = (String) map.get("infoContent");
                    // TODO: 2017/12/5 根据txLogisticID获取单个订单
                    if (StringUtils.isNotBlank(mailNo) && StringUtils.isNotBlank(txLogisticID)){
                        // 根据物流号先查询子订单信息
                        OrdSubListExt ordSubListExt = ordSubListMapperExt.selectByTxLogisticID(txLogisticID);
                        if(ordSubListExt != null){
                            // 物流号
                            ordSubListExt.setExpressNo(mailNo);
                            OrdSubList OrdSubList = new OrdSubList();
                            BeanUtils.copyProperties(ordSubListExt,OrdSubList);
                            // TODO: 2017/12/5 将mailno放进数据库
                            ordSubListMapper.updateByPrimaryKeySelective(OrdSubList);
                            // 保存物流信息
                            ordLogisticStatus = saveOrdLogisticStatus(map,ordSubListExt.getSubOrderId());
                        }else{
                            // 根据物流号再查询调货订单信息
                            OrdTransferList ordTransferList = ordTransferListMapper.selectByPrimaryKey(txLogisticID);
                            if(ordTransferList != null){
                                ordTransferList.setExpressNo(mailNo);
                                // TODO: 2017/12/5 将mailno放进数据库
                                ordTransferListMapper.updateByPrimaryKeySelective(ordTransferList);
                                // 保存物流信息
                                ordLogisticStatus = saveOrdLogisticStatus(map,ordTransferList.getOrderTransferId());
                            }
                        }
                        try {
                            // ERP交互(圆通快递状态更新接口),将圆通返回信息返给ERP端
                            ErpSendUtil.YTOUpdate(ordLogisticStatus,ordLogisticStatusMapper);
                        }catch (Exception e) {
                            log.info("ERP 数据出错不影响电商");
                        }
                        return true;
                    }else {
                        return true;
                    }
                }
            }
        }
        //成功
        return false;
    }

    /**
     * 保存物流信息
     * @return
     */
    public OrdLogisticStatus saveOrdLogisticStatus(Map<String, Object> map,String orderId){
        java.text.SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss");
        Date date = new Date();
        OrdLogisticStatus ordLogisticStatus = new OrdLogisticStatus();
        String randomUUID = UUID.randomUUID().toString();
        ordLogisticStatus.setOrdLogisticId(randomUUID);
        ordLogisticStatus.setOrderId(orderId);
        ordLogisticStatus.setLogisticProviderId((String) map.get("logisticProviderID"));
        ordLogisticStatus.setClientId((String) map.get("clientID"));
        ordLogisticStatus.setMailNo((String) map.get("mailNo"));
        ordLogisticStatus.setTxLogisticId((String) map.get("txLogisticID"));
        ordLogisticStatus.setInfoType((String) map.get("infoType"));
        ordLogisticStatus.setInfoContent((String) map.get("infoContent"));
        ordLogisticStatus.setRemark((String) map.get("Remark"));
        ordLogisticStatus.setWeight((String) map.get("weight"));
        ordLogisticStatus.setSignedName((String) map.get("signedName"));
        try {
           date = formatter.parse((String) map.get("acceptTime"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ordLogisticStatus.setAcceptTime(date);
        ordLogisticStatus.setContactInfo((String) map.get("contactInfo"));
        ordLogisticStatusMapper.insertSelective(ordLogisticStatus);
        return ordLogisticStatus;
    }

    @Override
    public String getTxLogisticID(YtoForm ytoForm) throws Exception {
        if (ytoForm!=null){
            String param = ytoForm.getLogistics_interface();
            param = URLUtil.getURLDecoderString(param);
            if (param!=null&&!param.equals("")){
//                Map<String,Object> map = GetYtoServiceImpl.parseXmlStr(param);
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
}
