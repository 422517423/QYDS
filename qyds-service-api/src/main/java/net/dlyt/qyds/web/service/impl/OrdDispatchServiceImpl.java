package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import me.chanjar.weixin.common.util.StringUtils;
import net.dlyt.qyds.common.dto.*;
import net.dlyt.qyds.common.dto.ext.ErpStoreExt;
import net.dlyt.qyds.common.form.OrdDispatchForm;
import net.dlyt.qyds.common.form.OrdMasterForm;
import net.dlyt.qyds.dao.*;
import net.dlyt.qyds.dao.ext.*;
import net.dlyt.qyds.web.service.OrdDispatchService;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.common.ErpSendUtil;
import net.dlyt.qyds.web.service.common.StringUtil;
import net.dlyt.qyds.web.service.common.YtUtil.YtApi;
import net.dlyt.qyds.web.service.exception.ExceptionErrorData;
import net.dlyt.qyds.web.service.exception.ExceptionErrorParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by YiLian on 2016/10/10.
 */

@Service("ordDispatchService")
public class OrdDispatchServiceImpl implements OrdDispatchService {

    protected final Logger log = LoggerFactory.getLogger(ErpSendServiceImpl.class);
    @Autowired
    private OrdDispatchMapperExt ordDispatchMapperExt;
    @Autowired
    private OrdDispatchMapper ordDispatchMapper;
    @Autowired
    private OrdMasterMapperExt ordMasterMapperExt;
    @Autowired
    private OrdMasterMapper ordMasterMapper;
    @Autowired
    private OrdSubListMapperExt ordSubListMapperExt;
    @Autowired
    private OrdSubListMapper ordSubListMapper;
    @Autowired
    private BnkMasterMapperExt bnkMasterMapperExt;
    @Autowired
    private BnkRecordsMapper bnkRecordsMapper;
    @Autowired
    private ErpStoreMapper erpStoreMapper;
    @Autowired
    private ErpStoreMapperExt erpStoreMapperExt;
    //添加ordhistorymapper  01.18
  /*  @Autowired
    private OrdHistoryMapperExt ordHistoryMapperExt;*/

    /**
     * 类型转换
     *
     * @param ordMaster
     * @return
     * @工具类 OrdMaster转换OrdHistory
     */
   /* public OrdHistory masterToHistory(OrdMaster ordMaster) {
        OrdHistory ordHistory = new OrdHistory();
        BeanUtils.copyProperties(ordMaster, ordHistory);
        return ordHistory;
    }*/



    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 根据订单ID或订单编码获取派单列表
     *
     * @param form
     * @return
     */
    @Override
    public JSONObject getDispatchOrdMasterList(OrdMasterForm form) {
        JSONObject json = new JSONObject();

        try {

            OrdMasterExt ordMasterExt = new OrdMasterExt();
            ordMasterExt.setShopId(Constants.ORGID);
            ordMasterExt.setOrderId(form.getOrderId());
            ordMasterExt.setOrderCode(form.getOrderCode());
            ordMasterExt.setNeedColumns(Integer.parseInt(form.getiDisplayLength()));
            ordMasterExt.setStartPoint(Integer.parseInt(form.getiDisplayStart()));
            ordMasterExt.setMemberName(form.getMemberName());
            ordMasterExt.setTelephone(form.getTelephone());

            //数据库检索 -- 过滤数据
            List<OrdMasterExt> list = ordMasterMapperExt.selectDispatchOrdMasterList(ordMasterExt);
            int allCount = ordMasterMapperExt.countDispatchOrdMaster(ordMasterExt);

            json.put("aaData", list);
            json.put("sEcho", form.getsEcho());
            json.put("iTotalRecords", allCount);
            json.put("iTotalDisplayRecords", allCount);
            json.put("resultCode", Constants.NORMAL);

        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 根据订单ID取得订单主表信息
     *
     * @param orderId
     * @return
     */
    @Override
    public JSONObject getDispatchOrdMasterInfo(String orderId) {
        JSONObject json = new JSONObject();

        try {
            // 根据主键获取订单信息
            OrdMaster ordMaster = ordMasterMapper.selectByPrimaryKey(orderId);
            if (ordMaster == null) {
                throw new ExceptionErrorData("订单不存在");
            }

            if (!"0".equals(ordMaster.getDeleted())) {
                throw new ExceptionErrorData("订单已近被删除");
            }

            if (!"20".equals(ordMaster.getPayStatus())) {
                throw new ExceptionErrorData("订单未支付");
            }

            if (!"10".equals(ordMaster.getDeliverType())) {
                throw new ExceptionErrorData("订单不是电商发货");
            }

            if (!"10".equals(ordMaster.getOrderStatus()) && !"23".equals(ordMaster.getOrderStatus())) {
                throw new ExceptionErrorData("当前订单状态不可派单发货");
            }

//            if ("2".equals(ordMaster.getDispatchStatus())) {
//                throw new ExceptionErrorData("订单派单状态已更新");
//            }

            json.put("data", ordMaster);
            json.put("resultCode", Constants.NORMAL);

        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 根据订单id获取子订单信息
     *
     * @param orderId
     * @return
     */
    @Override
    public JSONObject getDispatchOrdSubInfo(String orderId, String orgId) {
        JSONObject json = new JSONObject();

        try {

            List<OrdSubListExt> list = ordSubListMapperExt.selectOrderSubInfo(orderId);
            List<OrdSubListExt> results = new ArrayList<>();

            for (OrdSubListExt item : list) {
                if (!StringUtil.isEmpty(item.getDispatchStore()) && !"2".equals(item.getDispatchStatus()) && !StringUtil.isEmpty(orgId) && !orgId.equals(item.getDispatchStore())) {
                    continue;
                }
                String skuJson = item.getSku();
                String sku = "";
                if (skuJson != null && !"".equals(skuJson)) {
                    JSONObject jsonO = JSON.parseObject(skuJson);
                    String color_name = (String) jsonO.get("color_name");
                    String size_name = (String) jsonO.get("size_name");
                    sku += color_name + "" + size_name;
                }

                item.setSku(sku);
                results.add(item);
            }

            json.put("aaData", results);
            json.put("resultCode", Constants.NORMAL);

        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 分派订单到门店
     *
     * @param form
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject dispatchOrderToStore(OrdDispatchForm form) {
        JSONObject json = new JSONObject();

        try {

            String orderId = form.getOrderId();
            String subOrderId = form.getSubOrderId();
            String dispatchStore = form.getDispatchStore();

            if (StringUtil.isEmpty(orderId)) {
                throw new ExceptionErrorParam("缺少主键参数订单ID");
            }

            if (StringUtil.isEmpty(dispatchStore)) {
                throw new ExceptionErrorParam("缺少主键参数门店ID");
            }

            // 根据主键获取订单信息
            OrdMaster ordMaster = ordMasterMapper.selectByPrimaryKey(orderId);
            if (ordMaster == null) {
                throw new ExceptionErrorData("订单不存在");
            }

            if (!"0".equals(ordMaster.getDeleted())) {
                throw new ExceptionErrorData("订单已近被删除");
            }

            if (!"20".equals(ordMaster.getPayStatus())) {
                throw new ExceptionErrorData("订单未支付");
            }

            if (!"10".equals(ordMaster.getDeliverType())) {
                throw new ExceptionErrorData("订单不是电商发货");
            }

            if (!"10".equals(ordMaster.getOrderStatus()) && !"23".equals(ordMaster.getOrderStatus())) {
                throw new ExceptionErrorData("当前订单状态不可派单发货");
            }

            if ("2".equals(ordMaster.getDispatchStatus())) {
                throw new ExceptionErrorData("订单派单状态已更新");
            }

            if ("1".equals(ordMaster.getDispatchStatus()) && StringUtil.isEmpty(subOrderId)) {
                throw new ExceptionErrorData("订单已经部分分派,不能再进行整单分派");
            }

            if (!StringUtil.isEmpty(subOrderId)) {
                OrdMasterExt ordMasterExt = new OrdMasterExt();
                ordMasterExt.setSubOrderId(subOrderId);
                OrdSubList ordSubItem = ordSubListMapperExt.selectByPrimaryKey(ordMasterExt);

                if (ordSubItem == null) {
                    throw new ExceptionErrorData("子订单不存在");
                }

                if (!"0".equals(ordSubItem.getDeleted())) {
                    throw new ExceptionErrorData("子订单已近被删除");
                }

                if ("1".equals(ordSubItem.getDispatchStatus()) || "3".equals(ordSubItem.getDispatchStatus())) {
                    throw new ExceptionErrorData("子订单派单状态已更新");
                }

                boolean lastSubItem = true;
                List<OrdSubListExt> subListExts = ordSubListMapperExt.selectOrderSubInfo(orderId);
                for (OrdSubListExt item : subListExts) {
                    if (!item.getSubOrderId().equals(ordSubItem.getSubOrderId()) &&
                            ("0".equals(item.getDispatchStatus()) || "2".equals(item.getDispatchStatus()))) {
                        lastSubItem = false;
                        break;
                    }
                }

                ordSubItem.setDispatchStatus("1");
                ordSubItem.setDispatchStore(dispatchStore);
                int count = ordSubListMapper.updateByPrimaryKeySelective(ordSubItem);
                if (count != 1) {
                    throw new ExceptionErrorData("子订单派单失败");
                }

                OrdDispatch record = new OrdDispatch();

                record.setDispatchId(UUID.randomUUID().toString());
                record.setOrderId(orderId);
                record.setSubOrderId(ordSubItem.getSubOrderId());
                record.setShopId(ordMaster.getShopId());
                record.setType("0");
                record.setDeleted("0");
                record.setUpdateUserId(form.getUpdateUserId());
                record.setUpdateTime(new Date());
                record.setInsertUserId(form.getUpdateUserId());
                record.setInsertTime(new Date());
                record.setDispatchStore(dispatchStore);

                ordDispatchMapper.insertSelective(record);

                if (lastSubItem) {
                    ordMaster.setDispatchStatus("2");
                    ordMasterMapper.updateByPrimaryKeySelective(ordMaster);

                } else if ("0".equals(ordMaster.getDispatchStatus())) {
                    ordMaster.setDispatchStatus("1");
                    ordMasterMapper.updateByPrimaryKeySelective(ordMaster);
                }

            } else {
                List<OrdSubListExt> subListExts = ordSubListMapperExt.selectOrderSubInfo(orderId);
                for (OrdSubListExt item : subListExts) {

                    if (!"0".equals(item.getDeleted())) {
                        throw new ExceptionErrorData("子订单已近被删除");
                    }

                    if ("1".equals(item.getDispatchStatus()) || "3".equals(item.getDispatchStatus())) {
                        throw new ExceptionErrorData("子订单派单状态已更新");
                    }

                    OrdSubList ordSubItem = new OrdSubList();
                    ordSubItem.setSubOrderId(item.getSubOrderId());
                    ordSubItem.setDispatchStatus("1");
                    ordSubItem.setDispatchStore(dispatchStore);

                    int count = ordSubListMapper.updateByPrimaryKeySelective(ordSubItem);
                    if (count != 1) {
                        throw new ExceptionErrorData("子订单派单失败");
                    }

                    OrdDispatch record = new OrdDispatch();

                    record.setDispatchId(UUID.randomUUID().toString());
                    record.setOrderId(orderId);
                    record.setSubOrderId(item.getSubOrderId());
                    record.setShopId(ordMaster.getShopId());
                    record.setType("0");
                    record.setDeleted("0");
                    record.setUpdateUserId(form.getUpdateUserId());
                    record.setUpdateTime(new Date());
                    record.setInsertUserId(form.getUpdateUserId());
                    record.setInsertTime(new Date());
                    record.setDispatchStore(dispatchStore);

                    ordDispatchMapper.insertSelective(record);

                    ordMaster.setDispatchStatus("2");
                    ordMasterMapper.updateByPrimaryKeySelective(ordMaster);
                }
            }

            json.put("data", ordMaster);
            json.put("resultCode", Constants.NORMAL);

        } catch (Exception e) {

            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }


    /**
     * 修改自提的门店
     *
     * @param form
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject changeOrderToStore(OrdDispatchForm form) {
        JSONObject json = new JSONObject();

        try {

            String orderId = form.getOrderId();
            String dispatchStore = form.getDispatchStore();

            if (StringUtil.isEmpty(orderId)) {
                throw new ExceptionErrorParam("缺少主键参数订单ID");
            }

            if (StringUtil.isEmpty(dispatchStore)) {
                throw new ExceptionErrorParam("缺少主键参数门店ID");
            }

            // 根据主键获取订单信息
            OrdMaster ordMaster = ordMasterMapper.selectByPrimaryKey(orderId);
            if (ordMaster == null) {
                throw new ExceptionErrorData("订单不存在");
            }

            if (!"0".equals(ordMaster.getDeleted())) {
                throw new ExceptionErrorData("订单已近被删除");
            }

            if (!"20".equals(ordMaster.getPayStatus())) {
                throw new ExceptionErrorData("订单未支付");
            }

            if (!"20".equals(ordMaster.getDeliverType())) {
                throw new ExceptionErrorData("订单不是自提发货");
            }


            List<OrdSubListExt> subListExts = ordSubListMapperExt.selectOrderSubInfo(orderId);
            ErpStore erpStore = erpStoreMapper.selectByPrimaryKey(dispatchStore);
            for (OrdSubListExt item : subListExts) {

                if (!"0".equals(item.getDeleted())) {
                    throw new ExceptionErrorData("子订单已近被删除");
                }

                OrdSubList ordSubItem = new OrdSubList();
                ordSubItem.setSubOrderId(item.getSubOrderId());
                ordSubItem.setErpStoreId(dispatchStore);
                ordSubItem.setStoreName(erpStore.getStoreNameCn());
                ordSubItem.setStorePhone(erpStore.getPhone());
                ordSubItem.setDispatchStore(dispatchStore);

                int count = ordSubListMapper.updateByPrimaryKeySelective(ordSubItem);
                if (count != 1) {
                    throw new ExceptionErrorData("门店修改失败");
                }

                ordMaster.setErpStoreId(dispatchStore);
                ordMaster.setStoreName(erpStore.getStoreNameCn());
                ordMaster.setStorePhone(erpStore.getPhone());
                ordMasterMapper.updateByPrimaryKeySelective(ordMaster);
            }


            json.put("data", ordMaster);
            json.put("resultCode", Constants.NORMAL);

        } catch (Exception e) {

            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }


    /**
     * 取消门店指派
     *
     * @param form
     * @return
     */
    @Override
    public JSONObject cancelDispatch(OrdDispatchForm form) {
        JSONObject json = new JSONObject();

        try {

            String orderId = form.getOrderId();
            String subOrderId = form.getSubOrderId();
            if (StringUtil.isEmpty(orderId)) {
                throw new ExceptionErrorParam("缺少主键参数订单ID");
            }

            if (StringUtil.isEmpty(subOrderId)) {
                throw new ExceptionErrorParam("缺少主键参数子订单ID");
            }

            // 根据主键获取订单信息
            OrdMasterExt ordMaster = ordMasterMapperExt.selectOrdInfo(orderId);
            if (ordMaster == null) {
                throw new ExceptionErrorData("订单不存在");
            }

            if (!"0".equals(ordMaster.getDeleted())) {
                throw new ExceptionErrorData("订单已近被删除");
            }

            if (!"20".equals(ordMaster.getPayStatus())) {
                throw new ExceptionErrorData("订单未支付");
            }

            if (!"10".equals(ordMaster.getDeliverType())) {
                throw new ExceptionErrorData("订单不是电商发货");
            }

            if (!"10".equals(ordMaster.getOrderStatus())
                    && !"23".equals(ordMaster.getOrderStatus())) {
                throw new ExceptionErrorData("当前订单状态不可发货");
            }

            // 0-未分派；1-部分分派；2-全部分派；
            if (!"1".equals(ordMaster.getDispatchStatus())
                    && !"2".equals(ordMaster.getDispatchStatus())) {
                throw new ExceptionErrorData("订单没有指派门店");
            }

            OrdMasterExt ordMasterExt = new OrdMasterExt();
            ordMasterExt.setSubOrderId(subOrderId);
            OrdSubList subOrder = ordSubListMapperExt.selectByPrimaryKey(ordMasterExt);

            if (subOrder == null) {
                throw new ExceptionErrorData("子订单不存在");
            }

            if (!"0".equals(subOrder.getDeleted())) {
                throw new ExceptionErrorData("子订单已近被删除");
            }

            // 0-未分派；1-已分派；2-拒绝接受；3-接受分派
            if ("0".equals(subOrder.getDispatchStatus())) {
                throw new ExceptionErrorData("子订单派单状态已更新");
            }

            if(!StringUtil.isEmpty(subOrder.getDeliverStatus())
                    && !"10".equals(subOrder.getDeliverStatus())){
                throw new ExceptionErrorData("子订单已经发货，不能取消指派");
            }

            if (StringUtil.isEmpty(subOrder.getSkuId())) {
                throw new ExceptionErrorData("子订单SKU信息检索出错");
            }

            boolean lastDispatch = true;
            boolean lastDeliver = true;
            List<OrdSubListExt> subListExts = ordSubListMapperExt.selectOrderSubInfo(orderId);
            for (OrdSubListExt item : subListExts) {
                if (!item.getSubOrderId().equals(subOrderId)) {
                    if (!"1".equals(item.getDispatchStatus()) && !"3".equals(item.getDispatchStatus())) {
                        lastDispatch = false;
                    }

                    if (!StringUtil.isEmpty(item.getExpressNo())) {
                        lastDeliver = false;
                    }

                    if (!lastDispatch && !lastDeliver) {
                        break;
                    }
                }
            }

            String oldStore = subOrder.getDispatchStore();
            // 恢复到未指派状态
            subOrder.setDispatchStatus("0");
            subOrder.setDispatchStore("");
            int count = ordSubListMapper.updateByPrimaryKeySelective(subOrder);
            if (count != 1) {
                throw new ExceptionErrorData("子订单更新失败");
            }

            OrdDispatch record = new OrdDispatch();

            record.setDispatchId(UUID.randomUUID().toString());
            record.setOrderId(orderId);
            record.setSubOrderId(subOrderId);
            record.setShopId(ordMaster.getShopId());
            record.setType("3");
            record.setDeleted("0");
            record.setUpdateUserId(form.getUpdateUserId());
            record.setUpdateTime(new Date());
            record.setInsertUserId(form.getUpdateUserId());
            record.setInsertTime(new Date());
            record.setDispatchStore(oldStore);

            ordDispatchMapper.insertSelective(record);

            String deliverStatus = lastDeliver ? "10" : "19";
            String dispatchStatus = lastDispatch ? "0" : "1";

            ordMaster.setDispatchStatus(dispatchStatus);
            ordMaster.setDeliverStatus(deliverStatus);
            ordMasterMapper.updateByPrimaryKeySelective(ordMaster);

            json.put("resultCode", Constants.NORMAL);

        } catch (Exception e) {

            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 店铺获取对应的待发货列表
     *
     * @param form
     * @return
     */
    @Override
    public JSONObject getProcessedOrdMasterList(OrdDispatchForm form) {
        JSONObject json = new JSONObject();

        try {

            form.setShopId(Constants.ORGID);

            //数据库检索 -- 过滤数据
            List<OrdMasterExt> list = ordMasterMapperExt.selectProcessedOrdMasterList(form);
            int allCount = ordMasterMapperExt.countProcessedOrdMaster(form);

            json.put("aaData", list);
            json.put("sEcho", form.getsEcho());
            json.put("iTotalRecords", allCount);
            json.put("iTotalDisplayRecords", allCount);
            json.put("resultCode", Constants.NORMAL);

        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 待发货订单详细
     *
     * @param orderId
     * @return
     */
    @Override
    public JSONObject getProcessedOrdMasterInfo(String orderId) {
        JSONObject json = new JSONObject();

        try {
            // 根据主键获取订单信息
            OrdMaster ordMaster = ordMasterMapper.selectByPrimaryKey(orderId);
            if (ordMaster == null) {
                throw new ExceptionErrorData("订单不存在");
            }

            if (!"0".equals(ordMaster.getDeleted())) {
                throw new ExceptionErrorData("订单已近被删除");
            }

            if (!"20".equals(ordMaster.getPayStatus())) {
                throw new ExceptionErrorData("订单未支付");
            }

//            if (!"10".equals(ordMaster.getDeliverType())) {
//                throw new ExceptionErrorData("订单不是电商发货");
//            }

            if (!"10".equals(ordMaster.getOrderStatus()) && !"23".equals(ordMaster.getOrderStatus())) {
                throw new ExceptionErrorData("当前订单状态不可发货");
            }

            if (!"1".equals(ordMaster.getDispatchStatus()) && !"2".equals(ordMaster.getDispatchStatus())) {
                throw new ExceptionErrorData("订单没有指派门店");
            }

            json.put("data", ordMaster);
            json.put("resultCode", Constants.NORMAL);

        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 根据订单id获取子订单信息(待发货)
     *
     * @param orderId
     * @return
     */
    @Override
    public JSONObject getProcessedOrdList(String orderId, String orgId) {
        JSONObject json = new JSONObject();

        try {

            List<OrdSubListExt> results = new ArrayList<>();

            List<OrdSubListExt> list = ordSubListMapperExt.selectOrderSubInfo(orderId);

            for (OrdSubListExt item : list) {

                if (!StringUtil.isEmpty(orgId) && !orgId.equals(item.getDispatchStore())) {
                    continue;
                }

                String skuJson = item.getSku();
                String sku = "";
                if (skuJson != null && !"".equals(skuJson)) {
                    JSONObject jsonO = JSON.parseObject(skuJson);
                    String color_name = (String) jsonO.get("color_name");
                    String size_name = (String) jsonO.get("size_name");
                    sku += color_name + "" + size_name;
                }

                item.setSku(sku);

                results.add(item);
            }

            json.put("aaData", results);
            json.put("resultCode", Constants.NORMAL);

        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 根据子订单获取待发货基础信息显示
     *
     * @param form
     * @param orgId
     * @return
     */
    @Override
    public JSONObject getSubOrderDeliverInfo(OrdMasterForm form, String orgId) {
        JSONObject json = new JSONObject();

        try {
            // 根据主键获取订单信息
            OrdMasterExt ordMaster = ordMasterMapperExt.selectOrdInfo(form.getOrderId());
            if (ordMaster == null) {
                throw new ExceptionErrorData("订单不存在");
            }

            if (!"0".equals(ordMaster.getDeleted())) {
                throw new ExceptionErrorData("订单已近被删除");
            }

            if (!"20".equals(ordMaster.getPayStatus())) {
                throw new ExceptionErrorData("订单未支付");
            }

//            if (!"10".equals(ordMaster.getDeliverType())) {
//                throw new ExceptionErrorData("订单不是电商发货");
//            }

            if (!"10".equals(ordMaster.getOrderStatus()) && !"23".equals(ordMaster.getOrderStatus())) {
                throw new ExceptionErrorData("当前订单状态不可发货");
            }

            if (!"1".equals(ordMaster.getDispatchStatus()) && !"2".equals(ordMaster.getDispatchStatus())) {
                throw new ExceptionErrorData("订单没有指派门店");
            }

            OrdMasterExt ordMasterExt = new OrdMasterExt();
            ordMasterExt.setSubOrderId(form.getSubOrderId());
            OrdSubList ordSubItem = ordSubListMapperExt.selectByPrimaryKey(ordMasterExt);

            if (ordSubItem == null) {
                throw new ExceptionErrorData("子订单不存在");
            }

            if (!"0".equals(ordSubItem.getDeleted())) {
                throw new ExceptionErrorData("子订单已近被删除");
            }

            if (!"1".equals(ordSubItem.getDispatchStatus())) {
                throw new ExceptionErrorData("子订单派单状态已更新");
            }

            if (!orgId.equals(ordSubItem.getDispatchStore())) {
                throw new ExceptionErrorData("子订单派单已更新");
            }

            String skuJson = ordSubItem.getSku();
            String sku = "";
            if (skuJson != null && !"".equals(skuJson)) {
                JSONObject jsonO = JSON.parseObject(skuJson);
                String color_name = (String) jsonO.get("color_name");
                String size_name = (String) jsonO.get("size_name");
                sku += color_name + "" + size_name;
            }

            ordSubItem.setSku(sku);

            List<OrdSubList> sublist = new ArrayList<>();
            sublist.add(ordSubItem);
            ordMaster.setSubList(sublist);

            json.put("data", ordMaster);
            json.put("resultCode", Constants.NORMAL);

        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 门店电商发货
     *
     * @param ordSubItem
     * @param sysUser
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject deliverSubOrderItem(OrdSubList ordSubItem, SysUser sysUser) {
        JSONObject json = new JSONObject();
        try {

            // 根据主键获取订单信息
            OrdMasterExt ordMaster = ordMasterMapperExt.selectOrdInfo(ordSubItem.getOrderId());
            if (ordMaster == null) {
                throw new ExceptionErrorData("订单不存在");
            }

            if (!"0".equals(ordMaster.getDeleted())) {
                throw new ExceptionErrorData("订单已近被删除");
            }

            if (!"20".equals(ordMaster.getPayStatus())) {
                throw new ExceptionErrorData("订单未支付");
            }

//            if (!"10".equals(ordMaster.getDeliverType())) {
//                throw new ExceptionErrorData("订单不是电商发货");
//            }

            if (!"10".equals(ordMaster.getOrderStatus()) && !"23".equals(ordMaster.getOrderStatus())) {
                throw new ExceptionErrorData("当前订单状态不可发货");
            }

            if (!"1".equals(ordMaster.getDispatchStatus()) && !"2".equals(ordMaster.getDispatchStatus())) {
                throw new ExceptionErrorData("订单没有指派门店");
            }

            OrdMasterExt ordMasterExt = new OrdMasterExt();
            ordMasterExt.setSubOrderId(ordSubItem.getSubOrderId());
            OrdSubListExt subOrder = ordSubListMapperExt.selectByPrimaryKey(ordMasterExt);

            if (subOrder == null) {
                throw new ExceptionErrorData("子订单不存在");
            }

            if (!"0".equals(subOrder.getDeleted())) {
                throw new ExceptionErrorData("子订单已近被删除");
            }

            if (!"1".equals(subOrder.getDispatchStatus())) {
                throw new ExceptionErrorData("子订单派单状态已更新");
            }

            if (!sysUser.getOrgId().equals(subOrder.getDispatchStore())) {
                throw new ExceptionErrorData("子订单派单已更新");
            }

            if (StringUtil.isEmpty(subOrder.getSkuId())) {
                throw new ExceptionErrorData("子订单SKU信息检索出错");
            }

            ErpStoreExt erpStore = new ErpStoreExt();
            erpStore.setSku(subOrder.getSkuId());
            erpStore.setStoreCode(sysUser.getOrgId());

            List<HashMap> list = bnkMasterMapperExt.getQyantityBySkuId(erpStore);
            boolean enough = true;

            if (list == null || list.size() == 0) {
                enough = false;
            } else {
                for (HashMap hashMap : list) {
                    Integer new_count = 0;
                    if (hashMap.get("new_count") != null) {
                        Number number = NumberFormat.getInstance().parse(String.valueOf(hashMap.get("new_count")));
                        new_count = number.intValue();
                    }
                    if (new_count == 0 || new_count < subOrder.getQuantity()) {
                        enough = false;
                        break;
                    }
                }
            }

            if (enough == false) {
                throw new ExceptionErrorData("库存数量不足");
            }

            boolean lastSubItem = true;
            List<OrdSubListExt> subListExts = ordSubListMapperExt.selectOrderSubInfo(ordSubItem.getOrderId());
            for (OrdSubListExt item : subListExts) { // StringUtil.isEmpty(item.getExpressNo()
                // TODO: 2017/12/19 根据子订单的发货状态判断
                if (!item.getSubOrderId().equals(ordSubItem.getSubOrderId()) &&
                        ("未发货").equals(item.getDeliverStatus())) {
                    lastSubItem = false;
                    break;
                }
            }

            ErpStoreExt erpStore1 = erpStoreMapperExt.selectByPrimaryKeyExt(sysUser.getOrgId());
            if (erpStore1 == null) {
                throw new ExceptionErrorData("门店信息不存在");
            }

            subOrder.setDispatchStatus("3");
            // 门店自提是不发快递的
            if(StringUtils.isNotBlank(ordMaster.getDeliverType()) && ("10").equals(ordMaster.getDeliverType())){
                if(ordSubItem.getExpressType()==0){
                    subOrder.setExpressId("YTO");
                    subOrder.setExpressName("圆通快递公司");
                }else{
                    subOrder.setExpressId("SF");
                    subOrder.setExpressName("顺丰快递公司");
                }
            }

            subOrder.setDeliverStatus("20");
            subOrder.setDeliverTime(new Date());
            subOrder.setExpressNo(ordSubItem.getExpressNo());
            subOrder.setErpStoreId(erpStore1.getStoreCode());
            subOrder.setStoreName(erpStore1.getStoreNameCn());
            subOrder.setStorePhone(erpStore1.getPhone());
            subOrder.setStoreDeliveryId(sysUser.getLoginId().toString());
            subOrder.setStoreDeliveryName(sysUser.getUserName());
            subOrder.setShopProvince(erpStore1.getShopProvince());
            subOrder.setShopCity(erpStore1.getShopCity());
            subOrder.setShopDistrict(erpStore1.getShopDistrict());
            subOrder.setShopAddress(erpStore1.getShopAddress());

            int count = ordSubListMapper.updateByPrimaryKeySelective(subOrder);
            if (count != 1) {
                throw new ExceptionErrorData("子订单更新失败");
            }

            OrdDispatch record = new OrdDispatch();

            record.setDispatchId(UUID.randomUUID().toString());
            record.setOrderId(ordSubItem.getOrderId());
            record.setSubOrderId(ordSubItem.getSubOrderId());
            record.setShopId(ordMaster.getShopId());
            record.setType("2");
            record.setDeleted("0");
            record.setUpdateUserId(sysUser.getUserId().toString());
            record.setUpdateTime(new Date());
            record.setInsertUserId(sysUser.getUserId().toString());
            record.setInsertTime(new Date());
            record.setDispatchStore(sysUser.getOrgId());

            ordDispatchMapper.insertSelective(record);

            if (lastSubItem) {
                ordMaster.setDeliverStatus("20");
                ordMaster.setDeliverTime(new Date());
                ordMasterMapper.updateByPrimaryKeySelective(ordMaster);
                try {
                    // ERP交互
//                    sendOrder(ordMaster.getOrderId());
//                    ErpSendUtil.getInstance().SaleInputById(ordMaster.getOrderId());
                    ErpSendUtil.SaleInputById(ordMaster.getOrderId(),ordSubListMapperExt,ordMasterMapper,ordMasterMapperExt);
                }catch (Exception e) {
                    log.info("ERP 数据出错不影响电商");
                }

            } else if ("0".equals(ordMaster.getDispatchStatus())) {
                ordMaster.setDeliverStatus("19");
                ordMasterMapper.updateByPrimaryKeySelective(ordMaster);
            }

            // 电商部分库存操作
            BnkMaster bnkMaster = new BnkMaster();
            if("10".equals(subOrder.getType())){
                bnkMaster.setGoodsId(subOrder.getGoodsCode());
            }else{
                bnkMaster.setGoodsId(subOrder.getGoodsId());
            }

            if (!StringUtil.isEmpty(subOrder.getErpStoreId())) {
                bnkMaster.setErpStoreId(subOrder.getErpStoreId());
            }
            bnkMaster.setSku(subOrder.getSkuId());
            //获取库存信息
            BnkMasterExt bnkMasterExt = bnkMasterMapperExt.selectByGoodSkuId(bnkMaster);

            if (bnkMasterExt == null) {
                throw new ExceptionErrorData("商品库存信息不存在!");
            }
            //变更原库存数
            bnkMasterExt.setLastCount(bnkMasterExt.getNewCount());
            //变更最新库存数
            bnkMasterExt.setNewCount(bnkMasterExt.getNewCount() - subOrder.getQuantity());
            //更新人
            bnkMasterExt.setUpdateUserId(ordMaster.getMemberId());
            //更新库存主表
            bnkMasterMapperExt.cancelOrderAddBank(bnkMasterExt);
            //插入出入库履历表
            BnkRecords bnkRecords = new BnkRecords();
            //数据准备
            bnkRecords.setShopId(bnkMasterExt.getShopId());
            bnkRecords.setGoodsType(bnkMasterExt.getGoodsType());
            bnkRecords.setTypeType(bnkMasterExt.getTypeType());
            bnkRecords.setGoodsId(bnkMasterExt.getGoodsId());
            bnkRecords.setGoodsCode(bnkMasterExt.getGoodsCode());
            bnkRecords.setSku(bnkMasterExt.getSku());
            bnkRecords.setBankType(bnkMasterExt.getBankType());
            bnkRecords.setInoutCount(-subOrder.getQuantity());
            bnkRecords.setComment(bnkMasterExt.getComment());
            bnkRecords.setDeleted(bnkMasterExt.getDeleted());
            bnkRecords.setUpdateUserId(ordMaster.getMemberId());
            bnkRecords.setInsertUserId(ordMaster.getMemberId());
            if(!StringUtil.isEmpty(bnkMaster.getErpStoreId())){
                bnkRecords.setErpStoreId(bnkMaster.getErpStoreId());
            }
            bnkRecordsMapper.insertSelective(bnkRecords);

            // 判断如果是物流发货则创建圆通订单，如果是门店id，则不用创建圆通订单
            if(StringUtils.isNotBlank(ordMaster.getDeliverType()) && ("10").equals(ordMaster.getDeliverType())&& StringUtils.isEmpty(ordSubItem.getExpressNo())){
                // 创建圆通订单
                String result = YtApi.getOrderTracesByXml(ordMaster,subOrder, sysUser ,0);
                // 创建失败回滚
                if(!result.contains("<success>true</success>")){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    json.put("resultCode", Constants.FAIL);
                }
            }
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 门店电商拒绝发货
     *
     * @param ordDispatch
     * @param sysUser
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject rejectDeliverSubOrderItem(OrdDispatch ordDispatch, SysUser sysUser) {
        JSONObject json = new JSONObject();

        try {
            // 根据主键获取订单信息
            OrdMasterExt ordMaster = ordMasterMapperExt.selectOrdInfo(ordDispatch.getOrderId());
            if (ordMaster == null) {
                throw new ExceptionErrorData("订单不存在");
            }

            if (!"0".equals(ordMaster.getDeleted())) {
                throw new ExceptionErrorData("订单已近被删除");
            }

            if (!"20".equals(ordMaster.getPayStatus())) {
                throw new ExceptionErrorData("订单未支付");
            }

            if (!"10".equals(ordMaster.getDeliverType())) {
                throw new ExceptionErrorData("订单不是电商发货");
            }

            if (!"10".equals(ordMaster.getOrderStatus()) && !"23".equals(ordMaster.getOrderStatus())) {
                throw new ExceptionErrorData("当前订单状态不可发货");
            }

            if (!"1".equals(ordMaster.getDispatchStatus()) && !"2".equals(ordMaster.getDispatchStatus())) {
                throw new ExceptionErrorData("订单没有指派门店");
            }

            OrdMasterExt ordMasterExt = new OrdMasterExt();
            ordMasterExt.setSubOrderId(ordDispatch.getSubOrderId());
            OrdSubList subOrder = ordSubListMapperExt.selectByPrimaryKey(ordMasterExt);

            if (subOrder == null) {
                throw new ExceptionErrorData("子订单不存在");
            }

            if (!"0".equals(subOrder.getDeleted())) {
                throw new ExceptionErrorData("子订单已近被删除");
            }

            if (!"1".equals(subOrder.getDispatchStatus())) {
                throw new ExceptionErrorData("子订单派单状态已更新");
            }

            if (!sysUser.getOrgId().equals(subOrder.getDispatchStore())){
                throw new ExceptionErrorData("子订单派单已更新");
            }

            if (StringUtil.isEmpty(subOrder.getSkuId())) {
                throw new ExceptionErrorData("子订单SKU信息检索出错");
            }

            boolean lastDispatch = true;
            boolean lastDeliver = true;
            List<OrdSubListExt> subListExts = ordSubListMapperExt.selectOrderSubInfo(ordDispatch.getOrderId());
            for (OrdSubListExt item : subListExts) {
                if (!item.getSubOrderId().equals(ordDispatch.getSubOrderId())) {
                    if (!"1".equals(item.getDispatchStatus()) && !"3".equals(item.getDispatchStatus())) {
                        lastDispatch = false;
                    }

                    if (!StringUtil.isEmpty(item.getExpressNo())) {
                        lastDeliver = false;
                    }

                    if (!lastDispatch && !lastDeliver) {
                        break;
                    }
                }
            }

            subOrder.setDispatchStatus("2");
            int count = ordSubListMapper.updateByPrimaryKeySelective(subOrder);
            if (count != 1) {
                throw new ExceptionErrorData("子订单更新失败");
            }

            OrdDispatch record = new OrdDispatch();

            record.setDispatchId(UUID.randomUUID().toString());
            record.setOrderId(ordDispatch.getOrderId());
            record.setSubOrderId(ordDispatch.getSubOrderId());
            record.setShopId(ordMaster.getShopId());
            record.setType("1");
            record.setDeleted("0");
            record.setUpdateUserId(sysUser.getUserId().toString());
            record.setUpdateTime(new Date());
            record.setInsertUserId(sysUser.getUserId().toString());
            record.setInsertTime(new Date());
            record.setDispatchStore(sysUser.getOrgId());
            record.setContent(ordDispatch.getContent());

            ordDispatchMapper.insertSelective(record);

            String deliverStatus = lastDeliver ? "10" : "19";
            String dispatchStatus = lastDispatch ? "0" : "1";

            ordMaster.setDispatchStatus(dispatchStatus);
            ordMaster.setDeliverStatus(deliverStatus);
            ordMasterMapper.updateByPrimaryKeySelective(ordMaster);

          /*  //插入订单历史记录表
            OrdHistory ordHistory = new OrdHistory();
            ordHistory = this.masterToHistory(ordMaster);
            ordHistory.setSeqOrderId(UUID.randomUUID().toString());
            ordHistory.setInsertUserId(ordMasterExt.getMemberId());
            //主订单操作历史信息插入
            int insertHistoryCount = ordHistoryMapperExt.insertSelective(ordHistory);*/


            json.put("resultCode", Constants.NORMAL);

        } catch (Exception e) {

            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 门店申请调货
     *
     * @param ordDispatch
     * @param sysUser
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject seasoningDispatch(OrdDispatch ordDispatch, SysUser sysUser) {
        JSONObject json = new JSONObject();

        try {
            // 根据主键获取订单信息
            OrdMasterExt ordMaster = ordMasterMapperExt.selectOrdInfo(ordDispatch.getOrderId());
            if (ordMaster == null) {
                throw new ExceptionErrorData("订单不存在");
            }

            if (!"0".equals(ordMaster.getDeleted())) {
                throw new ExceptionErrorData("订单已近被删除");
            }

            if (!"20".equals(ordMaster.getPayStatus())) {
                throw new ExceptionErrorData("订单未支付");
            }

            if (!"10".equals(ordMaster.getOrderStatus()) && !"23".equals(ordMaster.getOrderStatus())) {
                throw new ExceptionErrorData("当前订单状态不可发货");
            }

            if (!"1".equals(ordMaster.getDispatchStatus()) && !"2".equals(ordMaster.getDispatchStatus())) {
                throw new ExceptionErrorData("订单没有指派门店");
            }

            OrdMasterExt ordMasterExt = new OrdMasterExt();
            ordMasterExt.setSubOrderId(ordDispatch.getSubOrderId());
            OrdSubList subOrder = ordSubListMapperExt.selectByPrimaryKey(ordMasterExt);

            if (subOrder == null) {
                throw new ExceptionErrorData("子订单不存在");
            }

            if (!"0".equals(subOrder.getDeleted())) {
                throw new ExceptionErrorData("子订单已近被删除");
            }

            if (!"1".equals(subOrder.getDispatchStatus())) {
                throw new ExceptionErrorData("子订单派单状态已更新");
            }

            if (!sysUser.getOrgId().equals(subOrder.getDispatchStore())) {
                throw new ExceptionErrorData("子订单派单已更新");
            }

            if (StringUtil.isEmpty(subOrder.getSkuId())) {
                throw new ExceptionErrorData("子订单SKU信息检索出错");
            }

            subOrder.setUpSeasoning("1");
            int count = ordSubListMapper.updateByPrimaryKeySelective(subOrder);
            if (count != 1) {
                throw new ExceptionErrorData("子订单更新失败");
            }

            json.put("resultCode", Constants.NORMAL);

        } catch (Exception e) {

            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }


    /**
     * 调货完成
     *
     * @param ordDispatch
     * @param sysUser
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject seasoningComplete(OrdDispatch ordDispatch, SysUser sysUser) {
        JSONObject json = new JSONObject();

        try {

            OrdMasterExt ordMasterExt = new OrdMasterExt();
            ordMasterExt.setSubOrderId(ordDispatch.getSubOrderId());
            OrdSubList subOrder = ordSubListMapperExt.selectByPrimaryKey(ordMasterExt);

            if (subOrder == null) {
                throw new ExceptionErrorData("子订单不存在");
            }

            if (!"0".equals(subOrder.getDeleted())) {
                throw new ExceptionErrorData("子订单已近被删除");
            }

            if (!"1".equals(subOrder.getDispatchStatus())) {
                throw new ExceptionErrorData("子订单派单状态已更新");
            }

            if (StringUtil.isEmpty(subOrder.getSkuId())) {
                throw new ExceptionErrorData("子订单SKU信息检索出错");
            }

            subOrder.setUpSeasoning("2");
            int count = ordSubListMapper.updateByPrimaryKeySelective(subOrder);
            if (count != 1) {
                throw new ExceptionErrorData("子订单更新失败");
            }

            json.put("resultCode", Constants.NORMAL);

        } catch (Exception e) {

            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }



    /**
     * 门店申请调货
     *
     * @param ordSubListExt
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject getUpSeasoningDispatchList(OrdSubListExt ordSubListExt) {
        JSONObject result = new JSONObject();
        try {

            ordSubListExt.setNeedColumns(Integer.parseInt(ordSubListExt.getiDisplayLength()));
            ordSubListExt.setStartPoint(Integer.parseInt(ordSubListExt.getiDisplayStart()));

            int allCount = ordSubListMapperExt.getUpSeasoningDispatchListCount(ordSubListExt);

            List<OrdSubListExt> list = ordSubListMapperExt.getUpSeasoningDispatchList(ordSubListExt);

            result.put("aaData", list);
            result.put("sEcho", ordSubListExt.getsEcho());
            result.put("iTotalRecords", allCount);
            result.put("iTotalDisplayRecords", allCount);
        } catch (Exception e) {
            result.put("resultCode", Constants.FAIL);
            result.put("message", e.getMessage());
        }
        return result;
    }


    /**
     * 派单履历
     *
     * @param ordDispatch
     * @return
     */
    @Override
    public JSONObject getDispatchHistory(OrdDispatch ordDispatch) {
        JSONObject json = new JSONObject();

        try {

            List<HashMap> list = ordDispatchMapperExt.selectDispatchHistory(ordDispatch);

            json.put("aaData", list);
            json.put("resultCode", Constants.NORMAL);

        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    @Override
    public JSONObject getProofInfo(String orderId, Map<String, Object> userMap) {
        JSONObject result = new JSONObject();
        try {
            String orgId = (String) userMap.get("orgId");
            //取得用户及门店信息
            ErpStore erpStore = erpStoreMapper.selectByPrimaryKey(orgId);
            if (null == erpStore || !"0".equals(erpStore.getDeleted())) {
                throw new ExceptionErrorData("门店信息不存在");
            }
            //取得主订单
            OrdMaster master = ordMasterMapper.selectByPrimaryKey(orderId);
            if (master == null) {
                throw new ExceptionErrorData("指定订单不存在");
            }
            //子订单
            OrdSubList ordSubList = new OrdSubList();
            ordSubList.setOrderId(orderId);
            ordSubList.setErpStoreId(orgId);
            List<OrdSubListExt> subList = ordSubListMapperExt.selectProofDispatch(ordSubList);
            //没有子订单数据
            if (subList == null || subList.size() == 0)
                throw new ExceptionErrorData("没有本店收货数据");
            BigDecimal amountTotle = new BigDecimal(0);
            BigDecimal amountDiscount = new BigDecimal(0);
            String subDetail = "";
            for(OrdSubList sub : subList){
                BigDecimal priceDiscount = sub.getPriceDiscount();
                priceDiscount = priceDiscount == null || priceDiscount.equals(0) ? sub.getPrice() : priceDiscount;
                if(priceDiscount.setScale(2,BigDecimal.ROUND_HALF_DOWN).equals(master.getAmountTotle().setScale(2,BigDecimal.ROUND_HALF_DOWN))){
                    priceDiscount = master.getPayInfact();
                }
                subDetail += "商品: " + sub.getGoodsCode() + "&nbsp;&nbsp;" + sub.getGoodsName() + "&nbsp;&nbsp;" + sub.getSkuId() + "<BR>";
                subDetail += "颜色: " + sub.getColoreName() + "&nbsp;&nbsp;尺码: " + sub.getSizeName() + "<BR>";
                subDetail += "标签价格: " + sub.getPrice() + " 元<BR>";
                subDetail += "折扣价格: " + priceDiscount + " 元<BR>";
                subDetail += "发货时间: " + (sub.getDeliverTime()==null?"":sdf.format(sub.getDeliverTime())) + "<BR>";
                subDetail += "发货人: " + (sub.getStoreDeliveryName()==null?"":sub.getStoreDeliveryName()) + "<BR><BR>";
                amountTotle = amountTotle.add(sub.getPrice());
                amountDiscount = amountDiscount.add(priceDiscount);
            }
            //合计金额
            master.setAmount(amountTotle);
            master.setAmountDiscount(amountDiscount);
            result.put("store",erpStore);
            result.put("master",master);
            result.put("sublist",subDetail);
            result.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            result.put("resultCode", Constants.FAIL);
            result.put("message", e.getMessage());
        }
        return result;
    }

//    private void sendOrder(String orderId) throws Exception{
//        OrdMaster master1 = new OrdMaster();
//        master1.setOrderId(orderId);
//        master1.setErpSendStatus("10");
//        try {
//            //取得主订单
//            OrdMasterExt master = ordMasterMapperExt.selectSendById(orderId);
//            if (master == null) {
//                throw new ExceptionErrorData("指定订单不存在");
//            }
//            //订单表
//            List<OrdSubList> subList = ordSubListMapperExt.selectErpByOrder(orderId);
//            //没有子订单数据
//            if (subList == null || subList.size() == 0) throw new ExceptionErrorData("没有子订单数据");
//
//            BigDecimal amountTotle = new BigDecimal(0);
//            BigDecimal amountDiscount = new BigDecimal(0);
//            List<SaleList> saleList = new ArrayList<>();
//            for(OrdSubList sub : subList){
//                SaleList sale = new SaleList();
//                sale.setOrderId(sub.getOrderId());
//                sale.setSubOrderId(sub.getSubOrderId());
//                sale.setErpSku(sub.getSkuId());
//                sale.setPrice(String.valueOf(sub.getPrice()));
//                BigDecimal priceDiscount = sub.getPriceShare();
//                priceDiscount = priceDiscount == null || priceDiscount.equals(0) ? sub.getPrice() : priceDiscount;
//                sale.setPriceDiscount(String.valueOf(priceDiscount));
//                sale.setDeliverTime(formatTimeStampToYMDHMS(sub.getDeliverTime()));
//                sale.setExpressName(changStringNull(sub.getExpressName()));
//                sale.setExpressNo(changStringNull(sub.getExpressNo()));
//                sale.setErpStoreId(sub.getErpStoreId());
//                sale.setStoreDeliveryName(sub.getStoreDeliveryId());
//                saleList.add(sale);
//                amountTotle = amountTotle.add(sub.getPrice());
//                amountDiscount = amountDiscount.add(priceDiscount);
//            }
//
//            //订单主表
//            SaleMaster m = new SaleMaster();
//            m.setOrderId(master.getOrderId());
//            m.setMemberId(master.getTelephone());
//            m.setMemberName(master.getMemberName());
//            m.setAmountTotle(String.valueOf(amountTotle));
//            m.setAmountDiscount(String.valueOf(amountDiscount));
//            m.setPayTime(formatTimeStampToYMDHMS(master.getPayTime()));
//            m.setQuantity(String.valueOf(saleList.size()));
//            m.setMessage(master.getMessage());
//            m.setDeliveryFree(master.getDeliveryFree());
//            m.setDeliceryFee(String.valueOf(master.getDeliveryFee()));
//            m.setDeliveryType(master.getDeliverType());
//            m.setPayType(master.getPayType());
//            m.setPayDeliveryType(master.getPayDeliveryType());
//            m.setSalerID(master.getInsertUserId());
//
//            Orders order = new Orders();
//            ArrayOfSaleList ll = new ArrayOfSaleList();
//            ll.setSaleList(saleList);
//            order.setSaleMaster(m);
//            order.setSaleList(ll);
//            log.debug("ERP SaleInput Start:param:"+ JSON.toJSONString(order));
//            String result = soap.saleInput(getKeyOrderInput(order), order);
//            log.debug("ERP SaleInput result code:"+result+",param:"+ JSON.toJSONString(order));
//            if (result.equals("00")) {
//            } else if (result.equals("11")) {
//                throw new ExceptionNoPower("ERP数据库连接失败");
//            } else if (result.equals("12")) {
//                throw new ExceptionBusiness("ERP数据库更新失败");
//            } else if (result.equals("13")) {
//                throw new ExceptionBusiness("订单重复");
//            } else if (result.equals("21")) {
//                throw new ExceptionErrorParam("ERP验证失败");
//            } else {
//                throw new ExceptionBusiness("ERP未知错误");
//            }
//        }catch (Exception e){
//            // 若通讯失败,记录失败原因及通讯信息
//            log.debug("ERP SaleInput ERROR"+ e.getMessage());
//            master1.setErpSendStatus("20");
////            throw e;
//        } finally {
//            master1.setUpdateTime(new Date());
//            ordMasterMapper.updateByPrimaryKeySelective(master1);
//        }
//    }
//    private static String changStringNull(String data) {
//        return data == null ? "" : data;
//    }
}
