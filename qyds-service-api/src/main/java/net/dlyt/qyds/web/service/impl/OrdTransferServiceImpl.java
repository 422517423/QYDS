package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import me.chanjar.weixin.common.util.StringUtils;
import net.dlyt.qyds.common.dto.*;
import net.dlyt.qyds.common.dto.ext.OrdTransferListExt;
import net.dlyt.qyds.dao.*;
import net.dlyt.qyds.dao.ext.*;
import net.dlyt.qyds.web.service.OrdTransferService;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.common.ErpSendUtil;
import net.dlyt.qyds.web.service.common.StringUtil;
import net.dlyt.qyds.web.service.common.YtUtil.YtApi;
import net.dlyt.qyds.web.service.exception.ExceptionErrorData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by ZLH on 2016/12/22.
 */

@Service("ordTransferService")
public class OrdTransferServiceImpl implements OrdTransferService {

    protected final Logger log = LoggerFactory.getLogger(OrdTransferServiceImpl.class);
    @Autowired
    private OrdTransferListMapper ordTransferListMapper;
    @Autowired
    private OrdTransferListMapperExt ordTransferListMapperExt;
    @Autowired
    private ErpStoreMapper erpStoreMapper;
    @Autowired
    private OrdSubListMapperExt ordSubListMapperExt;
    @Autowired
    private OrdSubListMapper ordSubListMapper;
    @Autowired
    private BnkMasterMapper bnkMasterMapper;
    @Autowired
    private BnkMasterMapperExt bnkMasterMapperExt;
    @Autowired
    private BnkRecordsMapper bnkRecordsMapper;
    @Autowired
    private OrdMasterMapperExt ordMasterMapperExt;
    @Autowired
    private OrdMasterMapper ordMasterMapper;
    @Autowired
    private OrdHistoryMapperExt ordHistoryMapperExt;


    /**
     * 类型转换
     *
     * @param ordMaster
     * @return
     * @工具类 OrdMaster转换OrdHistory
     */
    public OrdHistory masterToHistory(OrdMaster ordMaster) {
        OrdHistory ordHistory = new OrdHistory();
        BeanUtils.copyProperties(ordMaster, ordHistory);
        return ordHistory;
    }


    /**
     * 取得调货地址
     *
     * @param userMap
     * @return
     */
    @Override
    public JSONObject getApplyAddress(Map<String, Object> userMap, String subOrderId) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(subOrderId)) {
                throw new ExceptionErrorData("子订单ID未指定");
            }
            //重复检查
            OrdTransferList ordTransferList = ordTransferListMapperExt.getBySubOrderId(subOrderId);
            if (ordTransferList!=null) {
                throw new ExceptionErrorData("子订单已经有申请记录");
            }
            OrdMasterExt ordMasterExt = new OrdMasterExt();
            ordMasterExt.setSubOrderId(subOrderId);
            OrdSubList subOrder = ordSubListMapperExt.selectByPrimaryKey(ordMasterExt);
            if (subOrder == null) {
                throw new ExceptionErrorData("子订单不存在");
            }
            if (!"0".equals(subOrder.getDeleted())) {
                throw new ExceptionErrorData("子订单已删除");
            }
            if (!"1".equals(subOrder.getDispatchStatus())) {
                throw new ExceptionErrorData("子订单派单状态已更新");
            }
            if (!StringUtil.isEmpty(subOrder.getUpSeasoning())) {
                throw new ExceptionErrorData("子订单已申请调单");
            }
            if (!userMap.get("orgId").equals(subOrder.getDispatchStore())) {
                throw new ExceptionErrorData("子订单非本店发货");
            }
            if (StringUtil.isEmpty(subOrder.getSkuId())) {
                throw new ExceptionErrorData("子订单SKU信息检索出错");
            }
            OrdTransferList data = new OrdTransferList();
            data.setApplyContactor(userMap.get("userName").toString());
            //取得门店信息
            try {
                ErpStore store = erpStoreMapper.selectByPrimaryKey(userMap.get("orgId").toString());
                if (store != null) {
                    data.setApplyAddress(store.getAddress());
                    data.setApplyPhone(store.getPhone());
                }
            } catch(Exception e) {
                //继续
            }
            json.put("data",data);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 取得发货地址
     *
     * @param TransferId
     * @return
     */
    @Override
    public JSONObject getById(String TransferId) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(TransferId)) {
                throw new ExceptionErrorData("子订单调货ID未指定");
            }
            //取得调货信息
            OrdTransferList ordTransferList = ordTransferListMapper.selectByPrimaryKey(TransferId);
            if (ordTransferList==null) {
                throw new ExceptionErrorData("子订单没有申请记录");
            }
//            ordTransferList.setDeliveryAddress(ordTransferList.getApplyAddress());
//            ordTransferList.setDeliveryContactor(ordTransferList.getDeliveryContactor());
//            ordTransferList.setDeliveryPhone(ordTransferList.getDeliveryPostcode());
//            ordTransferList.setDeliveryPostcode(ordTransferList.getDeliveryPostcode());
            json.put("data",ordTransferList);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 提交调货申请
     *
     * @param form
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject apply(OrdTransferList form) {
        JSONObject json = new JSONObject();
        try {
            //重复检查
            OrdTransferList ordTransferList = ordTransferListMapperExt.getBySubOrderId(form.getSubOrderId());
            if (ordTransferList!=null) {
                throw new ExceptionErrorData("子订单已经有申请记录");
            }
            //取得子订单信息
            OrdMasterExt ordMasterExt = new OrdMasterExt();
            ordMasterExt.setSubOrderId(form.getSubOrderId());
            OrdSubList subOrder = ordSubListMapperExt.selectByPrimaryKey(ordMasterExt);
            if (subOrder == null) {
                throw new ExceptionErrorData("子订单不存在");
            }
            if (!"0".equals(subOrder.getDeleted())) {
                throw new ExceptionErrorData("子订单已删除");
            }
            if (!"1".equals(subOrder.getDispatchStatus())) {
                throw new ExceptionErrorData("子订单派单状态已更新");
            }
            if (!form.getApplyStore().equals(subOrder.getDispatchStore())) {
                throw new ExceptionErrorData("子订单非本店发货");
            }
            if (!StringUtil.isEmpty(subOrder.getUpSeasoning())) {
                throw new ExceptionErrorData("子订单已申请调单");
            }
            if (StringUtil.isEmpty(subOrder.getSkuId())) {
                throw new ExceptionErrorData("子订单SKU信息检索出错");
            }
            Date updateDate = new Date();
            //生成调货数据
            form.setOrderTransferId(UUID.randomUUID().toString());
            form.setOrderId(subOrder.getOrderId());
            form.setTransferStatus("10");
            form.setGoodsType(subOrder.getType());
            form.setGoodsId(subOrder.getGoodsId());
            form.setGoodsCode(subOrder.getGoodsCode());
            form.setGoodsName(subOrder.getGoodsName());
            form.setColoreCode(subOrder.getColoreCode());
            form.setColoreName(subOrder.getColoreName());
            form.setSizeCode(subOrder.getSizeCode());
            form.setSizeName(subOrder.getSizeName());
            form.setSku(subOrder.getSkuId());
            form.setErpSku(subOrder.getErpSku());
            form.setErpStyleNo(subOrder.getErpStyleNo());
            form.setErpColoreCode(subOrder.getErpColoreCode());
            form.setErpColoreName(subOrder.getErpColoreName());
            form.setErpSizeCode(subOrder.getErpSizeCode());
            form.setPrice(subOrder.getPrice());
            form.setPriceDiscount(subOrder.getPriceDiscount());
            form.setPriceShare(subOrder.getPriceShare());
            form.setQuantity(subOrder.getQuantity());
            form.setAmount(subOrder.getAmount());
            form.setAmountDiscount(subOrder.getAmountDiscount());
            form.setApplyTime(updateDate);
//            form.setUpdateUserId(form.getApplyUser());
//            form.setUpdateTime(updateDate);
//            form.setInsertUserId(form.getApplyUser());
//            form.setInsertTime(updateDate);
            ordTransferListMapper.insertSelective(form);
            //更新子订单调货状态为1
            OrdSubList ordSubList = new OrdSubList();
            ordSubList.setSubOrderId(subOrder.getSubOrderId());
            ordSubList.setUpSeasoning("1");
            ordSubList.setUpdateUserId(form.getUpdateUserId());
            ordSubList.setUpdateTime(form.getUpdateTime());
            int count = ordSubListMapper.updateByPrimaryKeySelective(ordSubList);
            if (count != 1) {
                throw new ExceptionErrorData("子订单更新失败");
            }

            //根据ordTransfer的id获取ordmaster对象
            OrdMaster ordMaster = ordMasterMapper.selectByPrimaryKey(form.getOrderId());
            //插入订单历史记录表
            OrdHistory ordHistory = new OrdHistory();
            ordHistory = this.masterToHistory(ordMaster);
            ordHistory.setSeqOrderId(UUID.randomUUID().toString());
            ordHistory.setInsertUserId(ordMaster.getMemberId());
            ordHistory.setOrderStatus("10");
            ordHistory.setPayStatus("20");
            ordHistory.setDeliverStatus("43");
            //主订单操作历史信息插入
            int insertHistoryCount = ordHistoryMapperExt.insertSelective(ordHistory);

            //设置返回数据
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 获取分派调货订单列表
     *
     * @param form
     * @return
     */
    @Override
    public JSONObject getDispatchList(OrdTransferListExt form) {
        JSONObject json = new JSONObject();
        try {
            //取得列表
            List<OrdTransferListExt> list = ordTransferListMapperExt.getDispatchList(form);
            //取得列表件数
            int count = ordTransferListMapperExt.getDispatchListCount(form);
            json.put("aaData", list);
            json.put("sEcho", form.getsEcho());
            json.put("iTotalRecords", count);
            json.put("iTotalDisplayRecords", count);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 调货分派
     *
     * @param form
     * @return
     */
    @Override
    public JSONObject dispatch(OrdTransferList form) {
        JSONObject json = new JSONObject();
        try {
            //设置调货状态为已分派
            form.setTransferStatus("21");
            Date upDate = new Date();
            form.setDispatchTime(upDate);
            form.setUpdateTime(upDate);
            ordTransferListMapper.updateByPrimaryKeySelective(form);

            //根据ordTransfer的id获取ordmaster对象
            OrdMaster ordMaster = ordMasterMapper.selectByPrimaryKey(form.getOrderId());
            //插入订单历史记录表
            OrdHistory ordHistory = new OrdHistory();
            ordHistory = this.masterToHistory(ordMaster);
            ordHistory.setSeqOrderId(UUID.randomUUID().toString());
            ordHistory.setInsertUserId(ordMaster.getMemberId());
            ordHistory.setOrderStatus("10");
            ordHistory.setPayStatus("20");
            ordHistory.setDeliverStatus("39");
            //主订单操作历史信息插入
            int insertHistoryCount = ordHistoryMapperExt.insertSelective(ordHistory);


            //设置返回数据
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 拒绝分派
     *
     * @param form
     * @return
     */
    @Override
    public JSONObject refuse(OrdTransferList form) {
        JSONObject json = new JSONObject();
        try {
            //设置调货状态为拒绝
            form.setTransferStatus("22");
            Date upDate = new Date();
            form.setRefuseTime(upDate);
            form.setUpdateTime(upDate);
            ordTransferListMapper.updateByPrimaryKeySelective(form);

            //根据ordTransfer的id获取ordmaster对象
            OrdMaster ordMaster = ordMasterMapper.selectByPrimaryKey(form.getOrderId());
            //插入订单历史记录表
            OrdHistory ordHistory = new OrdHistory();
            ordHistory = this.masterToHistory(ordMaster);
            ordHistory.setSeqOrderId(UUID.randomUUID().toString());
            ordHistory.setInsertUserId(ordMaster.getMemberId());
            ordHistory.setOrderStatus("10");
            ordHistory.setPayStatus("20");
            ordHistory.setDeliverStatus("37");
            //主订单操作历史信息插入
            int insertHistoryCount = ordHistoryMapperExt.insertSelective(ordHistory);

            //设置返回数据
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 接受分派
     *
     * @param form
     * @return
     */
    @Override
    public JSONObject accept(OrdTransferList form) {
        JSONObject json = new JSONObject();
        try {
            //设置调货状态为接受
            form.setTransferStatus("23");
            Date upDate = new Date();
            form.setAcceptTime(upDate);
            form.setUpdateTime(upDate);
            ordTransferListMapper.updateByPrimaryKeySelective(form);

            //根据ordTransfer的id获取ordmaster对象
            OrdMaster ordMaster = ordMasterMapper.selectByPrimaryKey(form.getOrderId());
            //插入订单历史记录表
            OrdHistory ordHistory = new OrdHistory();
            ordHistory = this.masterToHistory(ordMaster);
            ordHistory.setSeqOrderId(UUID.randomUUID().toString());
            ordHistory.setInsertUserId(ordMaster.getMemberId());
            ordHistory.setOrderStatus("10");
            ordHistory.setPayStatus("20");
            ordHistory.setDeliverStatus("33");
            //主订单操作历史信息插入
            int insertHistoryCount = ordHistoryMapperExt.insertSelective(ordHistory);


            //设置返回数据
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 调货发货
     *
     * @param form
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject delivery(OrdTransferList form) {
        JSONObject json = new JSONObject();
        try {
            //取得调库信息
            OrdTransferList ordTransferList=
                    ordTransferListMapper.selectByPrimaryKey(form.getOrderTransferId());
            if(ordTransferList==null) {
                throw new ExceptionErrorData("没有调货记录");
            }
            //调货状态必须是已分派21或者已接受23
            if (!(ordTransferList.getTransferStatus().equals("21")
                    || ordTransferList.getTransferStatus().equals("23"))) {
                throw new ExceptionErrorData("调货状态不正确");
            }
            //取得库存
            BnkMaster bnk = new BnkMaster();
            bnk.setSku(ordTransferList.getSku());
            bnk.setErpStoreId(ordTransferList.getDispatchStore());
            BnkMaster bnkMaster = bnkMasterMapperExt.selectByGoodSkuId(bnk);
            if(bnkMaster==null) {
                throw new ExceptionErrorData("没有库存");
            }
            if(bnkMaster.getNewCount()<ordTransferList.getQuantity()) {
                throw new ExceptionErrorData("库存不足");
            }
            Date upDate = new Date();
            //更新库存
            BnkMaster bnkm = new BnkMaster();
            bnkm.setBankId(bnkMaster.getBankId());
            bnkm.setLastCount(bnkMaster.getNewCount());
            bnkm.setNewCount(bnkMaster.getNewCount()-ordTransferList.getQuantity());
            bnkm.setUpdateTime(upDate);
            bnkm.setUpdateUserId(form.getUpdateUserId());
            bnkMasterMapper.updateByPrimaryKeySelective(bnkm);
            //库存记录
            BnkRecords records = new BnkRecords();
            records.setShopId("00000000");
            records.setGoodsType(ordTransferList.getGoodsType());
            //goodsId 存成goodsCode
//            records.setGoodsId(ordTransferList.getGoodsId());

            //设置调货状态为已发货  records.setGoodsId(ordTransferList.getGoodsCode());
            records.setGoodsCode(ordTransferList.getGoodsCode());
            records.setSku(ordTransferList.getSku());
            records.setErpStoreId(ordTransferList.getDispatchStore());
            records.setBankType("19");
            records.setInoutCount(-ordTransferList.getQuantity());
            records.setComment("调货发货");
            records.setInsertUserId(form.getUpdateUserId());
            records.setUpdateUserId(form.getUpdateUserId());
            bnkRecordsMapper.insertSelective(records);
//            form.setDispatchStore(null);
            form.setTransferStatus("31");
            form.setDeliveryTime(upDate);
            form.setUpdateTime(upDate);
            ordTransferListMapper.updateByPrimaryKeySelective(form);

            if(StringUtils.isEmpty(form.getExpressNo())){
                // TODO: 2017/12/6 调货创建圆通订单
                // 创建订单
                // 根据调货id查询收货发货人信息
                OrdTransferListExt ordTransferListExt= ordTransferListMapperExt.selectByPrimaryKey(form.getOrderTransferId());
                //ordTransferListExt.setDeliveryContactor(form.getDeliveryContactor());
                ordTransferListExt.setDeliveryPostcode(form.getDeliveryPostcode());
                String result = YtApi.getOrderTracesByXml1(ordTransferListExt);
                // 创建失败回滚
                if(!result.contains("<success>true</success>")){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    json.put("resultCode", Constants.FAIL);
                }
            }
            //ERP发送发货信息
            ErpSendUtil.BankUpdateById(form.getOrderTransferId(),ordTransferListMapper);

            //根据ordTransfer的id获取ordmaster对象
            OrdMaster ordMaster = ordMasterMapper.selectByPrimaryKey(form.getOrderId());
            //插入订单历史记录表
            OrdHistory ordHistory = new OrdHistory();
            ordHistory = masterToHistory(ordMaster);
            ordHistory.setSeqOrderId(UUID.randomUUID().toString());
            ordHistory.setInsertUserId(ordMaster.getMemberId());
            ordHistory.setOrderStatus("10");
            ordHistory.setPayStatus("20");
            ordHistory.setDeliverStatus("40");
            //主订单操作历史信息插入
            int insertHistoryCount = ordHistoryMapperExt.insertSelective(ordHistory);

            //设置返回数据
            json.put("resultCode", Constants.NORMAL);

        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 调货收货
     *
     * @param form
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject receive(OrdTransferList form) {
        JSONObject json = new JSONObject();
        try {
            //取得调库信息
            OrdTransferList ordTransferList=
                    ordTransferListMapper.selectByPrimaryKey(form.getOrderTransferId());
            if(ordTransferList==null) {
                throw new ExceptionErrorData("没有调货记录");
            }
            //调货状态必须是已发货31
            if (!ordTransferList.getTransferStatus().equals("31")) {
                throw new ExceptionErrorData("调货状态不正确");
            }
            //取得库存
            BnkMaster bnk = new BnkMaster();
            bnk.setSku(ordTransferList.getSku());
//            bnk.setErpStoreId(ordTransferList.getDispatchStore());
            bnk.setErpStoreId(ordTransferList.getApplyStore());
            BnkMaster bnkMaster = bnkMasterMapperExt.selectByGoodSkuId(bnk);
            Date upDate = new Date();
            BnkMaster bnkm = new BnkMaster();
            if(bnkMaster==null) {
                //创建库存
                bnkm.setShopId("00000000");
                bnkm.setGoodsType(ordTransferList.getGoodsType());
                //goodsId 存成goodsCode
//                bnkm.setGoodsId(ordTransferList.getGoodsId());
                bnkm.setGoodsId(ordTransferList.getGoodsCode());
                bnkm.setGoodsCode(ordTransferList.getGoodsCode());
                bnkm.setErpGoodsCode(ordTransferList.getGoodsCode());
                bnkm.setSku(ordTransferList.getSku());
                bnkm.setErpSku(ordTransferList.getSku());
                bnkm.setErpStoreId(ordTransferList.getApplyStore());
                bnkm.setNewCount(ordTransferList.getQuantity());
                bnkm.setLastCount(0);
                bnkm.setUpdateUserId(form.getUpdateUserId());
                bnkm.setInsertUserId(form.getUpdateUserId());
                bnkMasterMapper.insertSelective(bnkm);
            } else {
                //更新库存
                bnkm.setBankId(bnkMaster.getBankId());
                bnkm.setLastCount(bnkMaster.getNewCount());
                bnkm.setNewCount(bnkMaster.getNewCount()+ordTransferList.getQuantity());
                bnkm.setUpdateTime(upDate);
                bnkm.setUpdateUserId(form.getUpdateUserId());
                bnkMasterMapper.updateByPrimaryKeySelective(bnkm);
            }
            //库存记录
            BnkRecords records = new BnkRecords();
            records.setShopId("00000000");
            records.setGoodsType(ordTransferList.getGoodsType());
            //goodsId 存成goodsCode
//            records.setGoodsId(ordTransferList.getGoodsId());
            records.setGoodsId(ordTransferList.getGoodsCode());
            records.setGoodsCode(ordTransferList.getGoodsCode());
            records.setSku(ordTransferList.getSku());
            records.setErpStoreId(ordTransferList.getApplyStore());
            records.setBankType("29");
            records.setInoutCount(ordTransferList.getQuantity());
            records.setComment("调货收货");
            records.setUpdateUserId(form.getUpdateUserId());
            records.setInsertUserId(form.getUpdateUserId());
            bnkRecordsMapper.insertSelective(records);
            //设置调货状态为已收货
//            form.setApplyStore(null);
            form.setTransferStatus("32");
            form.setReceiveTime(upDate);
            form.setUpdateTime(upDate);
            ordTransferListMapper.updateByPrimaryKeySelective(form);
            //更新订单子订单调货状态为已调货
            OrdSubList ordSubList = new OrdSubList();
            ordSubList.setSubOrderId(ordTransferList.getSubOrderId());
            ordSubList.setUpSeasoning("2");
            ordSubList.setUpdateUserId(form.getUpdateUserId());
            ordSubList.setUpdateTime(upDate);
            ordSubListMapper.updateByPrimaryKeySelective(ordSubList);
            //ERP发送收发货信息
            ErpSendUtil.BankUpdateById(form.getOrderTransferId(),ordTransferListMapper);

            //根据ordTransfer的id获取ordmaster对象
            OrdMaster ordMaster = ordMasterMapper.selectByPrimaryKey(form.getOrderId());
            //插入订单历史记录表
            OrdHistory ordHistory = new OrdHistory();
            ordHistory = this.masterToHistory(ordMaster);
            ordHistory.setSeqOrderId(UUID.randomUUID().toString());
            ordHistory.setInsertUserId(ordMaster.getMemberId());
            ordHistory.setOrderStatus("10");
            ordHistory.setPayStatus("20");
            ordHistory.setDeliverStatus("41");
            //主订单操作历史信息插入
            int insertHistoryCount = ordHistoryMapperExt.insertSelective(ordHistory);

            //设置返回数据
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 取得详细
     *
     * @param id
     * @return
     */
    @Override
    public JSONObject getDetailById(String id) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(id)) {
                throw new ExceptionErrorData("子订单调货ID未指定");
            }
            //取得调货信息
            OrdTransferListExt data = ordTransferListMapperExt.getDetailById(id);
            if (data==null) {
                throw new ExceptionErrorData("子订单没有申请记录");
            }
            json.put("data",data);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }
}
