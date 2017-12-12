package net.dlyt.qyds.web.service.impl;

import me.chanjar.weixin.common.util.StringUtils;
import me.chanjar.weixin.mp.util.URLUtil;
import me.chanjar.weixin.mp.util.xml.XMLUtil;
import net.dlyt.qyds.common.dto.OrdSubList;
import net.dlyt.qyds.common.dto.OrdSubListExt;
import net.dlyt.qyds.common.dto.OrdTransferList;
import net.dlyt.qyds.common.dto.YtoForm;
import net.dlyt.qyds.dao.OrdSubListMapper;
import net.dlyt.qyds.dao.OrdTransferListMapper;
import net.dlyt.qyds.dao.ext.OrdSubListMapperExt;
import net.dlyt.qyds.web.service.GetYtoService;
import net.dlyt.qyds.web.service.common.ErpSendUtil;
import org.dom4j.DocumentException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("getYtoService")
@Transactional(readOnly = true)
public class GetYtoServiceImpl implements GetYtoService {

    @Autowired
    private OrdSubListMapperExt ordSubListMapperExt;
    @Autowired
    private OrdSubListMapper ordSubListMapper;
    @Autowired
    private OrdTransferListMapper ordTransferListMapper;

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
            String param = ytoForm.getLogistics_interface();
            param = URLUtil.getURLDecoderString(param);
            if (param!=null&&!param.equals("")){
//                Map<String,Object> map = GetYtoServiceImpl.parseXmlStr(param);
                Map<String, Object> map = XMLUtil.xml2Map(param);
                if (map!=null){
                    //处理map
                    String mailNo = (String) map.get("mailNo");
                    String txLogisticID = (String) map.get("txLogisticID");
                    // TODO: 2017/12/5 根据txLogisticID获取单个订单
                    if (StringUtils.isNotBlank(mailNo) && StringUtils.isNotBlank(txLogisticID)){
                        // 根据物流号先查询子订单信息
                        OrdSubListExt ordSubListExt = ordSubListMapperExt.selectByTxLogisticID(txLogisticID);
                        if(ordSubListExt != null){
                            ordSubListExt.setExpressNo(mailNo);
                            OrdSubList OrdSubList = new OrdSubList();
                            BeanUtils.copyProperties(ordSubListExt,OrdSubList);
                            // TODO: 2017/12/5 将mailno放进数据库
                            ordSubListMapper.updateByPrimaryKeySelective(OrdSubList);
                        }else{
                            // 根据物流号再查询调货订单信息
                            OrdTransferList ordTransferList = ordTransferListMapper.selectByPrimaryKey(txLogisticID);
                            if(ordTransferList != null){
                                ordTransferList.setExpressNo(mailNo);
                                // TODO: 2017/12/5 将mailno放进数据库
                                ordTransferListMapper.updateByPrimaryKeySelective(ordTransferList);
                            }
                        }
                        // ERP交互(圆通快递状态更新接口),将圆通返回信息返给ERP端
                        ErpSendUtil.YTOUpdate(map);
                        return true;
                    }else {
                        return true;
                    }
                }
            }
        }
//        JSONObject jsonObject = new JSONObject();
        //成功
        return false;
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
