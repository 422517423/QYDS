package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.*;
import net.dlyt.qyds.common.dto.ext.ErpStoreExt;
import net.dlyt.qyds.common.form.BnkMasterForm;
import net.dlyt.qyds.common.form.OrdDispatchForm;
import net.dlyt.qyds.dao.*;
import net.dlyt.qyds.dao.ext.*;
import net.dlyt.qyds.web.service.BnkMasterService;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.common.StringUtil;
import net.dlyt.qyds.web.service.erp.*;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static net.dlyt.qyds.web.service.common.ErpKeyUtil.getKeyOrderInput;

/**
 * Created by pc on 2016/8/2.
 */
@Service("bnkMasterService")
@Transactional(readOnly = true)
public class BnkMasterServiceImpl implements BnkMasterService {
    protected final Logger log = LoggerFactory.getLogger(BnkMasterServiceImpl.class);
    net.dlyt.qyds.web.service.erp.Service service;// = new net.dlyt.qyds.web.service.erp.Service();
    ServiceSoap soap;// = service.getServiceSoap();
    @Autowired
    private BnkMasterMapperExt bnkMasterMapperExt;
    @Autowired
    private BnkMasterMapper bnkMasterMapper;
    @Autowired
    private OrdSubListMapperExt ordSubListMapperExt;
    @Autowired
    private OrdSubListMapper ordSubListMapper;
    @Autowired
    private BnkRecordsMapper bnkRecordsMapper;
    @Autowired
    private OrdMasterMapperExt ordMasterMapperExt;
    @Autowired
    private OrdMasterMapper ordMasterMapper;
    @Autowired
    private ErpStoreMapperExt erpStoreMapperExt;
    @Autowired
    private OrdReturnExchangeMapperExt ordReturnExchangeMapperExt;
    @Autowired
    private GdsMasterMapper gdsMasterMapper;

    /**
     * 获取商品库存列表信息
     *
     * @param form
     * @return
     */
    public JSONObject selectAll(BnkMasterForm form) {
        JSONObject json = new JSONObject();
        try {
            BnkMasterExt ext = new BnkMasterExt();
            ext.setBankType(form.getBankType());
            ext.setGoodsType(form.getGoodsType());
            ext.setNeedColumns(Integer.parseInt(form.getiDisplayLength()));
            ext.setStartPoint(Integer.parseInt(form.getiDisplayStart()));
            ext.setSku(form.getSku());
            ext.setErpStoreId(form.getErpStoreId());
            //数据库检索 -- 过滤数据
            List<BnkMasterExt> list = bnkMasterMapperExt.selectAll(ext);
            //获取总数
            int allCount = bnkMasterMapperExt.getAllDataCount(ext);
            json.put("aaData", list);
            json.put("sEcho", form.getsEcho());
            json.put("iTotalRecords", allCount);
            json.put("iTotalDisplayRecords", allCount);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    /**
     * 获取详细信息
     *
     * @param bankId
     * @return
     */
    public JSONObject selectByPrimaryKey(String bankId) {
        JSONObject json = new JSONObject();
        try {
            BnkMaster master = bnkMasterMapper.selectByPrimaryKey(Integer.parseInt(bankId));
            json.put("data", master);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    /**
     * 商品再入库
     *
     * @param data
     * @return
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public JSONObject save(String data) {
        JSONObject json = new JSONObject();
        try {
            BnkMaster bnkmaster = JSON.parseObject(data, BnkMaster.class);
            int newCount = bnkmaster.getNewCount();
            // 更新库存信息
            BnkMaster master = bnkMasterMapper.selectByPrimaryKey(bnkmaster.getBankId());
            int lastCount = master.getNewCount();
            bnkmaster.setLastCount(lastCount);
            bnkmaster.setNewCount(newCount);
            bnkmaster.setUpdateTime(Calendar.getInstance().getTime());
            bnkMasterMapper.updateByPrimaryKeySelective(bnkmaster);
            // 插入库存变更履历
            BnkRecords bnkRecords = new BnkRecords();
            bnkRecords.setShopId(Constants.ORGID);
            bnkRecords.setGoodsType(master.getGoodsType());
            bnkRecords.setTypeType(master.getTypeType());
            bnkRecords.setGoodsId(master.getGoodsId());
            bnkRecords.setGoodsCode(master.getGoodsCode());
            bnkRecords.setErpGoodsCode(master.getErpGoodsCode());
            bnkRecords.setSku(master.getSku());
            bnkRecords.setErpSku(master.getErpSku());
            bnkRecords.setErpStoreId(master.getErpStoreId());
            // TODO 入库类型待确认
            bnkRecords.setBankType("10");
            int balanceCount = newCount - lastCount;
            bnkRecords.setInoutCount(balanceCount);
            bnkRecords.setComment(bnkmaster.getComment());
            bnkRecords.setInsertUserId(bnkmaster.getUpdateUserId());
            bnkRecords.setUpdateUserId(bnkmaster.getUpdateUserId());
            bnkRecordsMapper.insertSelective(bnkRecords);
            json.put("data", bnkmaster);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    /**
     * 选择门店列表
     *
     * @return
     */
    public JSONObject getOrgList(String data) {

        JSONObject json = new JSONObject();
        try {

            JSONObject jsonObject = (JSONObject) JSONObject.parse(data);

            ErpStoreExt erpStore = new ErpStoreExt();
            if (!StringUtil.isEmpty(data)) {
                if (jsonObject.containsKey("provinceCode")) {
                    erpStore.setProvinceCode(jsonObject.getString("provinceCode"));
                }

                if (jsonObject.containsKey("cityCode")) {
                    erpStore.setCityCode(jsonObject.getString("cityCode"));
                }

                if (jsonObject.containsKey("districtCode")) {
                    erpStore.setDistrictCode(jsonObject.getString("districtCode"));
                }
            }

            if (!StringUtil.isEmpty(data) && jsonObject.size() > 0 && jsonObject.containsKey("orderList")) {
                //JSONObject jsonObject = (JSONObject)JSONObject.parse(data);
                JSONArray jsonArray = (JSONArray) jsonObject.get("orderList");
                List<HashMap> mapList = new ArrayList<HashMap>();
                List<String> skuIdlist = new ArrayList<String>();
                List<String> qyantitylist = new ArrayList<String>();

                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject JSONItem = (JSONObject) jsonArray.get(i);
                    JSONArray jsonArraySku = (JSONArray) JSONItem.get("skuInfo");
                    JSONObject sku = (JSONObject) jsonArraySku.get(0);
                    String skuId = (String) sku.get("skuId");
                    String qyantity = String.valueOf(JSONItem.get("quantity"));
                    erpStore.setSku(skuId);
                    //根据sku获取qyantity
                    List<HashMap> hashMap = bnkMasterMapperExt.getQyantityBySkuId(erpStore);
                    HashMap resultHashMap = new HashMap<String, Map>();
                    resultHashMap.put(skuId, hashMap);

                    mapList.add(resultHashMap);
                    skuIdlist.add(skuId);
                    qyantitylist.add(qyantity);
                }

                //判断数量和库存 整合map里边的数据
                for (int i = 0; i < skuIdlist.size(); i++) {
                    String skuId = skuIdlist.get(i);
                    String qyantity = qyantitylist.get(i);
                    HashMap resultHash = (HashMap) mapList.get(i);
                    List<HashMap> listHash = (List<HashMap>) resultHash.get(skuId);
                    for (int j = 0; j < listHash.size(); j++) {
                        HashMap hashMap = (HashMap) listHash.get(j);

                        Long new_count = Long.valueOf(0);
                        if (hashMap.get("new_count") != null) {
                            Number number = NumberFormat.getInstance().parse(String.valueOf(hashMap.get("new_count")));
                            new_count = number.longValue();
                        }
                        if (new_count == 0) {
                            hashMap.put("new_count", 0);
                            continue;
                        } else {
                            if (new_count < Integer.parseInt(qyantity)) {
                                hashMap.put("new_count", 0);
                                continue;
                            }
                        }
                    }

                }
                //组成用来检查的结果集合
                List<HashMap> resultCollection = new ArrayList<HashMap>();
                for (int i = 0; i < skuIdlist.size(); i++) {
                    if (i == 0) {
                        continue;
                    }
                    String skuId = skuIdlist.get(i);
                    HashMap resultHash = (HashMap) mapList.get(i);
                    List<HashMap> listHash = (List<HashMap>) resultHash.get(skuId);
                    resultCollection.addAll(listHash);
                }

                //最终返回的结果
                List<HashMap> result = new ArrayList<HashMap>();
                for (int i = 0; i < skuIdlist.size(); i++) {
                    String skuId = skuIdlist.get(0);
                    HashMap resultHash = (HashMap) mapList.get(i);
                    List<HashMap> listHash = (List<HashMap>) resultHash.get(skuId);
                    for (int j = 0; j < listHash.size(); j++) {
                        HashMap hashMap = (HashMap) listHash.get(j);
                        String org_id = hashMap.get("store_code") == null ? null : (String) hashMap.get("store_code");
                        Long new_count = Long.valueOf(0);
                        if (hashMap.get("new_count") != null) {
                            Number number = NumberFormat.getInstance().parse(String.valueOf(hashMap.get("new_count")));
                            new_count = number.longValue();
                        }
                        if (new_count == 0) {
                            result.add(hashMap);
                        } else {
                            //检查其他的
                            for (int k = 0; k < resultCollection.size(); k++) {
                                HashMap hashMapcheck = (HashMap) resultCollection.get(k);
                                String org_id_check = hashMapcheck.get("store_code") == null ? null : (String) hashMapcheck.get("store_code");

                                Long new_count_check = Long.valueOf(0);
                                if (hashMapcheck.get("new_count") != null) {
                                    Number number = NumberFormat.getInstance().parse(String.valueOf(hashMapcheck.get("new_count")));
                                    new_count_check = number.longValue();
                                }

                                if (org_id.equals(org_id_check) && new_count_check == 0) {
                                    hashMap.put("new_count", 0);
                                    break;
                                }
                            }
                            result.add(hashMap);
                        }
                    }
                    break;
                }

                //订单的请况，只过滤有货的
//                List<HashMap> result2 = new ArrayList<HashMap>();
//                for (int i=0;i<result.size();i++){
//                    if(result.get(i).get("new_count")!=null&&result.get(i).get("new_count")!=0){
//                        result2.add(result.get(i));
//                    }
//                }
                json.put("data", result);
                json.put("resultCode", Constants.NORMAL);

            } else {
                //选择门店列表的时候调用
                List<HashMap> orgList = bnkMasterMapperExt.getOrgList(erpStore);
                json.put("data", orgList);
                json.put("resultCode", Constants.NORMAL);
            }


        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
    public void cancelOrderAddBank(String orderId, String memberId) {
        //根据订单ID获取订单商品信息
        List<OrdSubListExt> lists = ordSubListMapperExt.selectOrderSubInfoOrderByExpressNo(orderId);
        for (OrdSubListExt ordSubList : lists) {
            //电商商品库存变更
//            if ("20".equals(ordSubList.getType())) {
            BnkMaster bnkMaster = new BnkMaster();
            //bnkMaster.setGoodsId(ordSubList.getGoodsId());
            if (StringUtil.isEmpty(ordSubList.getGoodsId())) {
                bnkMaster.setGoodsId(ordSubList.getGoodsCode());
            } else {
                bnkMaster.setGoodsId(ordSubList.getGoodsId());
            }
            if (!StringUtil.isEmpty(ordSubList.getErpStoreId())) {
                bnkMaster.setErpStoreId(ordSubList.getErpStoreId());
            }
            bnkMaster.setSku(ordSubList.getSkuId());
            //获取库存信息
            BnkMasterExt bnkMasterExt = bnkMasterMapperExt.selectByGoodSkuId(bnkMaster);
            if (bnkMasterExt == null) {
                throw new ExceptionBusiness("商品库存信息不存在!");
            }
            //变更原库存数
            bnkMasterExt.setLastCount(bnkMasterExt.getNewCount());
            //变更最新库存数
            bnkMasterExt.setNewCount(ordSubList.getQuantity() + bnkMasterExt.getNewCount());
            //取消订单时候，更新销量值（完成）（不用了）
            GdsMaster gdsMaster = gdsMasterMapper.selectByPrimaryKey(ordSubList.getGoodsId());
            int sales = gdsMaster.getSales();
            if (sales>0){
                gdsMaster.setSales(--sales);
                gdsMasterMapper.updateByPrimaryKeySelective(gdsMaster);
            }
            //更新人
            bnkMasterExt.setUpdateUserId(memberId);
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
            bnkRecords.setInoutCount(ordSubList.getQuantity());
            bnkRecords.setComment(bnkMasterExt.getComment());
            bnkRecords.setDeleted(bnkMasterExt.getDeleted());
            bnkRecords.setUpdateUserId(memberId);
            bnkRecords.setInsertUserId(memberId);
            bnkRecordsMapper.insertSelective(bnkRecords);
//            }
        }
    }

//    @Transactional(readOnly = false, rollbackFor = Exception.class)
//    @Override
//    public void submitOrderReduceBank(String orderId, String memberId) throws Exception {

        //根据订单ID获取订单商品信息
//        List<OrdSubListExt> lists = ordSubListMapperExt.selectOrderSubInfoOrderByExpressNo(orderId);
//        //ERP商品信息合集
//        List<OrdSubList> erpLists = new ArrayList<>();
//        for (OrdSubList ordSubList : lists) {
//            if ("10".equals(ordSubList.getType())) {
//                erpLists.add(ordSubList);
//            }
//        }
//        //若存在ERP商品场合,调用ERP接口更新库存信息
//        //TODO 若存在ERP商品和平台商品同时购买场合,订单主表中的价格及优惠价格信息并非准确
//        //TODO 此种场合调用ERP接口时,商品信息仅有ERP商品的.
//        if (erpLists != null && erpLists.size() > 0) {
//            BigDecimal amountTotle = new BigDecimal(0);
//            BigDecimal amountDiscount = new BigDecimal(0);
//            OrdMasterExt ordMasterExt = ordMasterMapperExt.selectOrdInfo(orderId);
//            Orders order = new Orders();
//            ArrayOfSaleList ll = new ArrayOfSaleList();
//            List<SaleList> l = new ArrayList<>();
//            for (OrdSubList osl : erpLists) {
//                SaleList saleList = new SaleList();
//                saleList.setOrderId(orderId);
//                saleList.setSubOrderId(osl.getSubOrderId());
//                saleList.setErpSku(osl.getSkuId());
//                saleList.setPrice(String.valueOf(osl.getPrice()));
//                BigDecimal riceDiscount = osl.getPriceDiscount();
//                riceDiscount = riceDiscount == null ? osl.getPrice() : riceDiscount;
//                saleList.setPriceDiscount(String.valueOf(riceDiscount));
//                amountTotle = amountTotle.add(osl.getPrice());
//                amountDiscount = amountDiscount.add(riceDiscount);
//                l.add(saleList);
//            }
//            ll.setSaleList(l);
//            SaleMaster m = new SaleMaster();
//            m.setOrderId(ordMasterExt.getOrderId());
//            m.setMemberId(ordMasterExt.getTelephone());
//            m.setMemberName(ordMasterExt.getMemberName());
//            m.setAmountTotle(String.valueOf(amountTotle));
//            m.setAmountDiscount(String.valueOf(amountDiscount));
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String payTime = sdf.format(ordMasterExt.getPayTime());
//            m.setPayTime(payTime);
//            m.setQuantity(String.valueOf(l.size()));
//            m.setPreOrderId(ordMasterExt.getOrderId());
//            m.setMessage(ordMasterExt.getMessage());
//            m.setDeliveryFree(ordMasterExt.getDeliveryFree());
//            m.setDeliceryFee(String.valueOf(ordMasterExt.getDeliveryFee()));
//            m.setDeliveryType(ordMasterExt.getDeliverType());
//            m.setPayType(ordMasterExt.getPayType());
//            m.setPayStatus(ordMasterExt.getPayStatus());
//            m.setPayDeliveryType(ordMasterExt.getPayDeliveryType());
//            m.setDeliveryPostcode(ordMasterExt.getDeliveryPostcode());
//            m.setDistrictidProvince(ordMasterExt.getDistrictidProvince());
//            m.setDistrictidCity(ordMasterExt.getDistrictidCity());
//            m.setDistrictidDistrict(ordMasterExt.getDistrictidDistrict());
//            m.setDeliveryAddress(ordMasterExt.getDeliveryAddress());
//            m.setDeliveryContactor(ordMasterExt.getDeliveryContactor());
//            m.setDeliveryPhone(ordMasterExt.getDeliveryPhone());
//            m.setErpStoreId(ordMasterExt.getErpStoreId());
//            m.setStoreName(ordMasterExt.getStoreName());
//            order.setSaleMaster(m);
//            order.setSaleList(ll);
//            String result = "";
//            OrdMaster master = new OrdMaster();
//            master.setOrderId(orderId);
//            master.setUpdateUserId(memberId);
//            master.setErpSendStatus("10");
//            try {
//                log.debug("ERP SaleInput input param:" + JSON.toJSONString(order));
//                System.out.println("ERP SaleInput input param:" + JSON.toJSONString(order));
//                result = soap.saleInput(getKeyOrderInput(order), order);
//                log.debug("ERP SaleInput result code:" + result);
//                System.out.println("ERP SaleInput result code:" + result);
//                if (result.equals("00")) {
//                } else if (result.equals("11")) {
//                    throw new Exception("ERP数据库连接失败");
//                } else if (result.equals("12")) {
//                    throw new Exception("ERP数据库更新失败");
//                } else if (result.equals("13")) {
//                    throw new ExceptionBusiness("订单重复");
//                } else if (result.equals("21")) {
//                    throw new Exception("ERP验证失败");
//                } else {
//                    throw new Exception("ERP未知错误");
//                }
//            } catch (Exception e) {
//                // 若通讯失败,记录失败原因及通讯信息,未发送成功标记
//                master.setErpSendStatus("20");
//            } finally {
//                master.setUpdateTime(new Date());
//                ordMasterMapper.updateByPrimaryKeySelective(master);
//            }
//        }
//    }

//    @Transactional(readOnly = false, rollbackFor = Exception.class)
//    @Override
//    public void returnOrderAddBank(String orderId, String subOrderId, String memberId) throws Exception {
//        if (orderId != null && subOrderId == null) {
//            //根据订单ID获取订单商品信息
//            List<OrdSubListExt> lists = ordSubListMapperExt.selectOrderSubInfoOrderByExpressNo(orderId);
//
//            OrdMasterExt ordMasterExt = ordMasterMapperExt.selectOrdInfo(orderId);
//            Orders order = new Orders();
//            ArrayOfSaleList ll = new ArrayOfSaleList();
//            List<SaleList> l = new ArrayList<>();
////
////            SaleMaster m = new SaleMaster();
////            m.setOrderId(ordMasterExt.getOrderId());
////            m.setMemberId(ordMasterExt.getTelephone());
////            m.setMemberName(ordMasterExt.getMemberName());
////            m.setAmountTotle(String.valueOf(ordMasterExt.getAmountTotle()));
////            m.setAmountDiscount(String.valueOf(ordMasterExt.getAmountDiscount()));
////            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////            String payTime = sdf.format(ordMasterExt.getPayTime());
////            m.setPayTime(payTime);
////            m.setQuantity(String.valueOf(l.size()));
////            m.setPreOrderId(ordMasterExt.getOrderId());
////            m.setMessage(ordMasterExt.getMessage());
////            m.setDeliveryFree(ordMasterExt.getDeliveryFree());
////            m.setDeliceryFee(String.valueOf(ordMasterExt.getDeliveryFee()));
////            m.setDeliveryType(ordMasterExt.getDeliverType());
////            m.setPayType(ordMasterExt.getPayType());
////            m.setPayStatus(ordMasterExt.getPayStatus());
////            m.setPayDeliveryType(ordMasterExt.getPayDeliveryType());
////            m.setDeliveryPostcode(ordMasterExt.getDeliveryPostcode());
////            m.setDistrictidProvince(ordMasterExt.getDistrictidProvince());
////            m.setDistrictidCity(ordMasterExt.getDistrictidCity());
////            m.setDistrictidDistrict(ordMasterExt.getDistrictidDistrict());
////            m.setDeliveryAddress(ordMasterExt.getDeliveryAddress());
////            m.setDeliveryContactor(ordMasterExt.getDeliveryContactor());
////            m.setDeliveryPhone(ordMasterExt.getDeliveryPhone());
////            m.setErpStoreId(ordMasterExt.getErpStoreId());
////            m.setStoreName(ordMasterExt.getStoreName());
////            order.setSaleMaster(m);
//
//            BigDecimal amountTotle = new BigDecimal(0);
//            BigDecimal amountDiscount = new BigDecimal(0);
//            for (OrdSubList ordSubList : lists) {
//                //电商商品库存变更
//                //TODO 在这里如果改成电商管理库存将把if去掉
////                if ("20".equals(ordSubList.getType())) {
//                BnkMaster bnkMaster = new BnkMaster();
//                if (StringUtil.isEmpty(ordSubList.getGoodsId())) {
//                    bnkMaster.setGoodsId(ordSubList.getGoodsCode());
//                } else {
//                    bnkMaster.setGoodsId(ordSubList.getGoodsId());
//                }
//                if (!StringUtil.isEmpty(ordSubList.getErpStoreId())) {
//                    bnkMaster.setErpStoreId(ordSubList.getErpStoreId());
//                }
//                //bnkMaster.setGoodsId(ordSubList.getGoodsId());
//                bnkMaster.setSku(ordSubList.getSkuId());
//                //获取库存信息
//                BnkMasterExt bnkMasterExt = bnkMasterMapperExt.selectByGoodSkuId(bnkMaster);
//                if (bnkMasterExt == null) {
//                    throw new ExceptionBusiness("商品库存信息不存在!");
//                }
//                //变更原库存数
//                bnkMasterExt.setLastCount(bnkMasterExt.getNewCount());
//                //变更最新库存数
//                //bnkMasterExt.setNewCount(bnkMasterExt.getNewCount() - ordSubList.getQuantity());
//                bnkMasterExt.setNewCount(bnkMasterExt.getNewCount() + ordSubList.getQuantity());
//                //更新人
//                bnkMasterExt.setUpdateUserId(memberId);
//                //更新库存主表
//                bnkMasterMapperExt.cancelOrderAddBank(bnkMasterExt);
//                //插入出入库履历表
//                BnkRecords bnkRecords = new BnkRecords();
//                //数据准备
//                bnkRecords.setShopId(bnkMasterExt.getShopId());
//                bnkRecords.setGoodsType(bnkMasterExt.getGoodsType());
//                bnkRecords.setTypeType(bnkMasterExt.getTypeType());
//                bnkRecords.setGoodsId(bnkMasterExt.getGoodsId());
//                bnkRecords.setGoodsCode(bnkMasterExt.getGoodsCode());
//                bnkRecords.setSku(bnkMasterExt.getSku());
//                bnkRecords.setBankType(bnkMasterExt.getBankType());
//                //bnkRecords.setInoutCount(-ordSubList.getQuantity());
//                bnkRecords.setInoutCount(ordSubList.getQuantity());
//                bnkRecords.setComment(bnkMasterExt.getComment());
//                bnkRecords.setDeleted(bnkMasterExt.getDeleted());
//                bnkRecords.setUpdateUserId(memberId);
//                bnkRecords.setInsertUserId(memberId);
//                bnkRecordsMapper.insertSelective(bnkRecords);
////                }
//                //ERP商品场合
////                else
//                if ("10".equals(ordSubList.getType())) {
//                    //若存在ERP商品场合,调用ERP接口更新库存信息
//                    //TODO 若存在ERP商品和平台商品同时购买场合,订单主表中的价格及优惠价格信息并非准确
//                    //TODO 此种场合调用ERP接口时,商品信息仅有ERP商品的.
//                    if (lists != null && lists.size() > 0) {
//                        SaleList saleList = new SaleList();
//                        saleList.setOrderId(orderId);
//                        saleList.setSubOrderId(ordSubList.getSubOrderId());
//                        saleList.setErpSku(ordSubList.getSkuId());
//                        saleList.setPrice(String.valueOf(ordSubList.getPrice()));
//                        BigDecimal riceDiscount = ordSubList.getPriceDiscount();
//                        riceDiscount = riceDiscount == null ? ordSubList.getPrice() : riceDiscount;
//                        saleList.setPriceDiscount(String.valueOf(riceDiscount));
//                        l.add(saleList);
//                        amountTotle = amountTotle.add(ordSubList.getPrice());
//                        amountDiscount = amountDiscount.add(riceDiscount);
//                    }
//                }
//            }
//
//            SaleMaster m = new SaleMaster();
//            m.setOrderId(ordMasterExt.getOrderId());
//            m.setMemberId(ordMasterExt.getTelephone());
//            m.setMemberName(ordMasterExt.getMemberName());
//            m.setAmountTotle(String.valueOf(amountTotle));
//            m.setAmountDiscount(String.valueOf(amountDiscount));
////            m.setAmountTotle(String.valueOf(ordMasterExt.getAmountTotle()));
////            m.setAmountDiscount(String.valueOf(ordMasterExt.getPayInfact()));
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String payTime = sdf.format(ordMasterExt.getPayTime());
//            m.setPayTime(payTime);
//            m.setQuantity(String.valueOf(l.size()));
////            m.setPreOrderId(ordMasterExt.getOrderId());
//            m.setMessage(ordMasterExt.getMessage());
//            m.setDeliveryFree(ordMasterExt.getDeliveryFree());
//            m.setDeliceryFee(String.valueOf(ordMasterExt.getDeliveryFee()));
//            m.setDeliveryType(ordMasterExt.getDeliverType());
//            m.setPayType(ordMasterExt.getPayType());
////            m.setPayStatus(ordMasterExt.getPayStatus());
//            m.setPayDeliveryType(ordMasterExt.getPayDeliveryType());
////            m.setDeliveryPostcode(ordMasterExt.getDeliveryPostcode());
////            m.setDistrictidProvince(ordMasterExt.getDistrictidProvince());
////            m.setDistrictidCity(ordMasterExt.getDistrictidCity());
////            m.setDistrictidDistrict(ordMasterExt.getDistrictidDistrict());
////            m.setDeliveryAddress(ordMasterExt.getDeliveryAddress());
////            m.setDeliveryContactor(ordMasterExt.getDeliveryContactor());
////            m.setDeliveryPhone(ordMasterExt.getDeliveryPhone());
////            m.setErpStoreId(ordMasterExt.getErpStoreId());
////            m.setStoreName(ordMasterExt.getStoreName());
//            order.setSaleMaster(m);
//
//            if (l != null && l.size() > 0) {
//                ll.setSaleList(l);
//                order.setSaleList(ll);
//                String result = "";
//                OrdReturnExchange master = new OrdReturnExchange();
//                master.setOrderId(orderId);
//                master.setUpdateUserId(memberId);
//                master.setErpSendStatus("10");
//                try {
//                    result = soap.returnInput(getKeyOrderInput(order), order);
//                    log.debug("ERP ReturnInput result code:" + result + ",param:" + JSON.toJSONString(order));
//                    if (result.equals("00")) {
//                    } else if (result.equals("11")) {
//                        throw new Exception("ERP数据库连接失败");
//                    } else if (result.equals("12")) {
//                        throw new Exception("ERP数据库更新失败");
//                    } else if (result.equals("21")) {
//                        throw new Exception("ERP验证失败");
//                    } else {
//                        throw new Exception("ERP未知错误");
//                    }
//                } catch (Exception e) {
//                    // 若通讯失败,记录失败原因及通讯信息
//                    master.setErpSendStatus("20");
//                } finally {
//                    ordReturnExchangeMapperExt.updateSendById(master);
//                }
                //ERP商品场合
//                else
//                if ("10".equals(ordSubList.getType())) {
//                    //若存在ERP商品场合,调用ERP接口更新库存信息
//                    //TODO 若存在ERP商品和平台商品同时购买场合,订单主表中的价格及优惠价格信息并非准确
//                    //TODO 此种场合调用ERP接口时,商品信息仅有ERP商品的.
//                    if (lists != null && lists.size() > 0) {
//                        SaleList saleList = new SaleList();
//                        saleList.setOrderId(orderId);
//                        saleList.setSubOrderId(ordSubList.getSubOrderId());
//                        saleList.setErpSku(ordSubList.getSkuId());
//                        saleList.setPrice(String.valueOf(ordSubList.getPrice()));
////                        BigDecimal riceDiscount = ordSubList.getPriceDiscount();
//                        BigDecimal riceDiscount = ordSubList.getPriceShare();
//                        riceDiscount = riceDiscount == null ? ordSubList.getPrice() : riceDiscount;
//                        saleList.setPriceDiscount(String.valueOf(riceDiscount));
//                        l.add(saleList);
//                        amountTotle = amountTotle.add(ordSubList.getPrice());
//                        amountDiscount = amountDiscount.add(riceDiscount);
//                    }
//                }
//            }
//
//            SaleMaster m = new SaleMaster();
//            m.setOrderId(ordMasterExt.getOrderId());
//            m.setMemberId(ordMasterExt.getTelephone());
//            m.setMemberName(ordMasterExt.getMemberName());
//            m.setAmountTotle(String.valueOf(amountTotle));
//            m.setAmountDiscount(String.valueOf(amountDiscount));
////            m.setAmountTotle(String.valueOf(ordMasterExt.getAmountTotle()));
////            m.setAmountDiscount(String.valueOf(ordMasterExt.getPayInfact()));
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String payTime = sdf.format(ordMasterExt.getPayTime());
//            m.setPayTime(payTime);
//            m.setQuantity(String.valueOf(l.size()));
////            m.setPreOrderId(ordMasterExt.getOrderId());
//            m.setMessage(ordMasterExt.getMessage());
//            m.setDeliveryFree(ordMasterExt.getDeliveryFree());
//            m.setDeliceryFee(String.valueOf(ordMasterExt.getDeliveryFee()));
//            m.setDeliveryType(ordMasterExt.getDeliverType());
//            m.setPayType(ordMasterExt.getPayType());
////            m.setPayStatus(ordMasterExt.getPayStatus());
//            m.setPayDeliveryType(ordMasterExt.getPayDeliveryType());
////            m.setDeliveryPostcode(ordMasterExt.getDeliveryPostcode());
////            m.setDistrictidProvince(ordMasterExt.getDistrictidProvince());
////            m.setDistrictidCity(ordMasterExt.getDistrictidCity());
////            m.setDistrictidDistrict(ordMasterExt.getDistrictidDistrict());
////            m.setDeliveryAddress(ordMasterExt.getDeliveryAddress());
////            m.setDeliveryContactor(ordMasterExt.getDeliveryContactor());
////            m.setDeliveryPhone(ordMasterExt.getDeliveryPhone());
////            m.setErpStoreId(ordMasterExt.getErpStoreId());
////            m.setStoreName(ordMasterExt.getStoreName());
//            order.setSaleMaster(m);
//
//            if (l != null && l.size() > 0) {
//                ll.setSaleList(l);
//                order.setSaleList(ll);
//                String result = "";
//                OrdReturnExchange master = new OrdReturnExchange();
//                master.setOrderId(orderId);
//                master.setUpdateUserId(memberId);
//                master.setErpSendStatus("10");
//                try {
//                    result = soap.returnInput(getKeyOrderInput(order), order);
//                    log.debug("ERP ReturnInput result code:" + result + ",param:" + JSON.toJSONString(order));
//                    if (result.equals("00")) {
//                    } else if (result.equals("11")) {
//                        throw new Exception("ERP数据库连接失败");
//                    } else if (result.equals("12")) {
//                        throw new Exception("ERP数据库更新失败");
//                    } else if (result.equals("21")) {
//                        throw new Exception("ERP验证失败");
//                    } else {
//                        throw new Exception("ERP未知错误");
//                    }
//                } catch (Exception e) {
//                    // 若通讯失败,记录失败原因及通讯信息
//                    master.setErpSendStatus("20");
//                } finally {
//                    ordReturnExchangeMapperExt.updateSendById(master);
//                }
//            }
//        } else if (orderId == null && subOrderId != null) {
//            OrdSubList ordList = ordSubListMapper.selectByPrimaryKey(subOrderId);
            //电商商品库存变更
            //TODO 在这里如果改成电商管理库存将把if去掉
//            if ("20".equals(ordList.getType())) {
//                BnkMaster bnkMaster = new BnkMaster();
//            }
//        } else if (orderId == null && subOrderId != null) {
//            OrdSubList ordList = ordSubListMapper.selectByPrimaryKey(subOrderId);
//            //电商商品库存变更
//            //TODO 在这里如果改成电商管理库存将把if去掉
////            if ("20".equals(ordList.getType())) {
////                BnkMaster bnkMaster = new BnkMaster();
////                bnkMaster.setGoodsId(ordList.getGoodsId());
////                bnkMaster.setSku(ordList.getSkuId());
//
//            BnkMaster bnkMaster = new BnkMaster();
//            if (StringUtil.isEmpty(ordList.getGoodsId())) {
//                bnkMaster.setGoodsId(ordList.getGoodsCode());
//            } else {
//                bnkMaster.setGoodsId(ordList.getGoodsId());
//            }
//            if (!StringUtil.isEmpty(ordList.getErpStoreId())) {
//                bnkMaster.setErpStoreId(ordList.getErpStoreId());
//            }
//            //bnkMaster.setGoodsId(ordSubList.getGoodsId());
//            bnkMaster.setSku(ordList.getSkuId());
//
//            //获取库存信息
//            BnkMasterExt bnkMasterExt = bnkMasterMapperExt.selectByGoodSkuId(bnkMaster);
//            if (bnkMasterExt == null) {
//                throw new ExceptionBusiness("商品库存信息不存在!");
//            }
//            //变更原库存数
//            bnkMasterExt.setLastCount(bnkMasterExt.getNewCount());
//            //变更最新库存数
//            bnkMasterExt.setNewCount(bnkMasterExt.getNewCount() + ordList.getQuantity());
//            //更新人
//            bnkMasterExt.setUpdateUserId(memberId);
//            //更新库存主表
//            bnkMasterMapperExt.cancelOrderAddBank(bnkMasterExt);
//            //插入出入库履历表
//            BnkRecords bnkRecords = new BnkRecords();
//            //数据准备
//            bnkRecords.setShopId(bnkMasterExt.getShopId());
//            bnkRecords.setGoodsType(bnkMasterExt.getGoodsType());
//            bnkRecords.setTypeType(bnkMasterExt.getTypeType());
//            bnkRecords.setGoodsId(bnkMasterExt.getGoodsId());
//            bnkRecords.setGoodsCode(bnkMasterExt.getGoodsCode());
//            bnkRecords.setSku(bnkMasterExt.getSku());
//            bnkRecords.setBankType(bnkMasterExt.getBankType());
//            bnkRecords.setInoutCount(ordList.getQuantity());
//            bnkRecords.setComment(bnkMasterExt.getComment());
//            bnkRecords.setDeleted(bnkMasterExt.getDeleted());
//            bnkRecords.setUpdateUserId(memberId);
//            bnkRecords.setInsertUserId(memberId);
//            bnkRecordsMapper.insertSelective(bnkRecords);
////            }
//            //ERP商品
////            else
//            if ("10".equals(ordList.getType())) {
//                //若存在ERP商品场合,调用ERP接口更新库存信息
//                //TODO 若存在ERP商品和平台商品同时购买场合,订单主表中的价格及优惠价格信息并非准确
//                //TODO 此种场合调用ERP接口时,商品信息仅有ERP商品的.
//                OrdMasterExt ordMasterExt = ordMasterMapperExt.selectOrdInfo(ordList.getOrderId());
//                Orders order = new Orders();
//                ArrayOfSaleList ll = new ArrayOfSaleList();
//                List<SaleList> l = new ArrayList<>();
//
//                SaleList saleList = new SaleList();
//                saleList.setOrderId(ordMasterExt.getOrderId());
//                saleList.setSubOrderId(ordList.getSubOrderId());
//                saleList.setErpSku(ordList.getSkuId());
//                saleList.setPrice(String.valueOf(ordList.getPrice()));
//                BigDecimal riceDiscount = ordList.getPriceDiscount();
//                riceDiscount = riceDiscount == null ? ordList.getPrice() : riceDiscount;
//                saleList.setPriceDiscount(String.valueOf(riceDiscount));
//                l.add(saleList);
//
//                ll.setSaleList(l);
//                SaleMaster m = new SaleMaster();
//                m.setOrderId(ordMasterExt.getOrderId());
//                m.setMemberId(ordMasterExt.getTelephone());
//                m.setMemberName(ordMasterExt.getMemberName());
////                m.setAmountTotle(String.valueOf(ordMasterExt.getAmountTotle()));
////                m.setAmountDiscount(String.valueOf(ordMasterExt.getPayInfact()));
//                m.setAmountTotle(String.valueOf(ordList.getPrice()));
//                m.setAmountDiscount(String.valueOf(ordList.getPriceDiscount()));
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String payTime = sdf.format(ordMasterExt.getPayTime());
//                m.setPayTime(payTime);
//                m.setQuantity(String.valueOf(l.size()));
////                m.setPreOrderId(ordMasterExt.getOrderId());
//                m.setMessage(ordMasterExt.getMessage());
//                m.setDeliveryFree(ordMasterExt.getDeliveryFree());
//                m.setDeliceryFee(String.valueOf(ordMasterExt.getDeliveryFee()));
//                m.setDeliveryType(ordMasterExt.getDeliverType());
//                m.setPayType(ordMasterExt.getPayType());
////                m.setPayStatus(ordMasterExt.getPayStatus());
//                m.setPayDeliveryType(ordMasterExt.getPayDeliveryType());
////                m.setDeliveryPostcode(ordMasterExt.getDeliveryPostcode());
////                m.setDistrictidProvince(ordMasterExt.getDistrictidProvince());
////                m.setDistrictidCity(ordMasterExt.getDistrictidCity());
////                m.setDistrictidDistrict(ordMasterExt.getDistrictidDistrict());
////                m.setDeliveryAddress(ordMasterExt.getDeliveryAddress());
////                m.setDeliveryContactor(ordMasterExt.getDeliveryContactor());
////                m.setDeliveryPhone(ordMasterExt.getDeliveryPhone());
////                m.setErpStoreId(ordMasterExt.getErpStoreId());
////                m.setStoreName(ordMasterExt.getStoreName());
//                order.setSaleMaster(m);
//                order.setSaleList(ll);
//                String result = "";
//                OrdReturnExchange master = new OrdReturnExchange();
//                master.setSubOrderId(subOrderId);
//                master.setUpdateUserId(memberId);
//                master.setErpSendStatus("10");
//                try {
//                    result = soap.returnInput(getKeyOrderInput(order), order);
//                    log.debug("ERP ReturnInput result code:" + result + ",param:" + JSON.toJSONString(order));
//                    if (result.equals("00")) {
//                    } else if (result.equals("11")) {
//                        throw new Exception("ERP数据库连接失败");
//                    } else if (result.equals("12")) {
//                        throw new Exception("ERP数据库更新失败");
//                    } else if (result.equals("21")) {
//                        throw new Exception("ERP验证失败");
//                    } else {
//                        throw new Exception("ERP未知错误");
//                    }
//                } catch (Exception e) {
//                    // 若通讯失败,记录失败原因及通讯信息
//                    master.setErpSendStatus("20");
//                } finally {
//                    ordReturnExchangeMapperExt.updateSendById(master);
//                }
//            }
//        }
//    }


    /**
     * 门店地址
     *
     * @param store
     * @return
     */
    @Override
    public JSONObject getOrgAddressList(ErpStore store) {
        JSONObject json = new JSONObject();
        try {

            if (store == null || (StringUtil.isEmpty(store.getProvinceCode())
                    && StringUtil.isEmpty(store.getCityCode()))) {
                // 取得省
                List<ErpProvince> list = erpStoreMapperExt.queryAllProvince();
                json.put("data", list);
            } else if (!StringUtil.isEmpty(store.getProvinceCode()) &&
                    StringUtil.isEmpty(store.getCityCode())) {
                List<ErpCity> list = erpStoreMapperExt.queryCityOfProvince(store.getProvinceCode());
                json.put("data", list);
            } else if (!StringUtil.isEmpty(store.getCityCode())) {
                List<ErpDistrict> list = erpStoreMapperExt.queryDistrictOfCity(store.getCityCode());
                json.put("data", list);
            }

            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 根据订单ID或子订单ID获取可发货门店列表
     *
     * @param data
     * @return
     */
    @Override
    public JSONObject getDispatchStoreList(OrdDispatchForm data) {

        JSONObject json = new JSONObject();
        try {

            ErpStoreExt erpStore = new ErpStoreExt();

            erpStore.setProvinceCode(data.getProvinceCode());
            erpStore.setCityCode(data.getCityCode());
            erpStore.setDistrictCode(data.getDistrictCode());

            List<OrdSubListExt> sublist = new ArrayList<>();

            if (StringUtil.isEmpty(data.getSubOrderId())) {
                sublist = ordSubListMapperExt.selectOrderSubInfo(data.getOrderId());
            } else {
                OrdMasterExt ordMasterExt = new OrdMasterExt();
                ordMasterExt.setSubOrderId(data.getSubOrderId());
                OrdSubList item = ordSubListMapperExt.selectByPrimaryKey(ordMasterExt);

                OrdSubListExt itemExt = new OrdSubListExt();
                itemExt.setSkuId(item.getSkuId());
                itemExt.setQuantity(item.getQuantity());
                sublist.add(itemExt);
            }

            List<HashMap> mapList = new ArrayList<HashMap>();
            List<String> skuIdlist = new ArrayList<String>();
            List<String> qyantitylist = new ArrayList<String>();

            for (OrdSubListExt item : sublist) {
                String skuId = item.getSkuId();
                if (skuIdlist.contains(skuId)) {

                    int index = skuIdlist.indexOf(skuId);
                    String old = qyantitylist.get(index);

                    int newCount = Integer.valueOf(old) + item.getQuantity();
                    qyantitylist.set(index, String.valueOf(newCount));

                } else {
                    erpStore.setSku(skuId);

                    //根据sku获取qyantity
//                    List<HashMap> hashMap = bnkMasterMapperExt.getQyantityBySkuId(erpStore);
                    //追加仓库
                    List<HashMap> hashMap = bnkMasterMapperExt.getQyantityAllBySkuId(erpStore);
                    HashMap resultHashMap = new HashMap<String, Map>();
                    resultHashMap.put(skuId, hashMap);
                    mapList.add(resultHashMap);
                    skuIdlist.add(skuId);
                    qyantitylist.add(String.valueOf(item.getQuantity()));
                }
            }

            //判断数量和库存 整合map里边的数据
            for (int i = 0; i < skuIdlist.size(); i++) {
                String skuId = skuIdlist.get(i);
                String qyantity = qyantitylist.get(i);
                HashMap resultHash = (HashMap) mapList.get(i);
                List<HashMap> listHash = (List<HashMap>) resultHash.get(skuId);
                for (int j = 0; j < listHash.size(); j++) {
                    HashMap hashMap = (HashMap) listHash.get(j);

                    Long new_count = Long.valueOf(0);
                    if (hashMap.get("new_count") != null) {
                        Number number = NumberFormat.getInstance().parse(String.valueOf(hashMap.get("new_count")));
                        new_count = number.longValue();
                    }
                    if (new_count == 0) {
                        hashMap.put("new_count", 0);
                        continue;
                    } else {
                        if (new_count < Integer.parseInt(qyantity)) {
                            hashMap.put("new_count", 0);
                            continue;
                        }
                    }
                }

            }
            //组成用来检查的结果集合
            List<HashMap> resultCollection = new ArrayList<HashMap>();
            for (int i = 0; i < skuIdlist.size(); i++) {
                if (i == 0) {
                    continue;
                }
                String skuId = skuIdlist.get(i);
                HashMap resultHash = (HashMap) mapList.get(i);
                List<HashMap> listHash = (List<HashMap>) resultHash.get(skuId);
                resultCollection.addAll(listHash);
            }

            //最终返回的结果
            List<HashMap> result = new ArrayList<HashMap>();
            for (int i = 0; i < skuIdlist.size(); i++) {
                String skuId = skuIdlist.get(0);
                HashMap resultHash = (HashMap) mapList.get(i);
                List<HashMap> listHash = (List<HashMap>) resultHash.get(skuId);
                for (int j = 0; j < listHash.size(); j++) {
                    HashMap hashMap = (HashMap) listHash.get(j);
                    String org_id = hashMap.get("store_code") == null ? null : (String) hashMap.get("store_code");
                    Long new_count = Long.valueOf(0);
                    if (hashMap.get("new_count") != null) {
                        Number number = NumberFormat.getInstance().parse(String.valueOf(hashMap.get("new_count")));
                        new_count = number.longValue();
                    }
                    if (new_count == 0) {
                        result.add(hashMap);
                    } else {
                        //检查其他的
                        for (int k = 0; k < resultCollection.size(); k++) {
                            HashMap hashMapcheck = (HashMap) resultCollection.get(k);
                            String org_id_check = hashMapcheck.get("store_code") == null ? null : (String) hashMapcheck.get("store_code");

                            Long new_count_check = Long.valueOf(0);
                            if (hashMapcheck.get("new_count") != null) {
                                Number number = NumberFormat.getInstance().parse(String.valueOf(hashMapcheck.get("new_count")));
                                new_count_check = number.longValue();
                            }

                            if (org_id.equals(org_id_check) && new_count_check == 0) {
                                hashMap.put("new_count", 0);
                                break;
                            }
                        }
                        result.add(hashMap);
                    }
                }
                break;
            }

            json.put("aaData", result);
            json.put("resultCode", Constants.NORMAL);


        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    @Override
    public JSONObject getAllStoreList(OrdDispatchForm form) {
        JSONObject json = new JSONObject();
        try {

            ErpStoreExt erpStore = new ErpStoreExt();

            erpStore.setProvinceCode(form.getProvinceCode());
            erpStore.setCityCode(form.getCityCode());
            erpStore.setDistrictCode(form.getDistrictCode());

            List<HashMap> orgList = bnkMasterMapperExt.getOrgAllList(erpStore);

            json.put("aaData", orgList);
            json.put("resultCode", Constants.NORMAL);


        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    @Override
    public JSONObject getReportBank() {
        JSONObject json = new JSONObject();
        try {
            List<BnkMaster> orgList = bnkMasterMapperExt.getReportBank();
            json.put("data", orgList);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    @Override
    public JSONObject getSkuStoreList(BnkMasterExt form) {
        JSONObject json = new JSONObject();
        try {
            List<BnkMasterExt> list = bnkMasterMapperExt.getSkuStoreList(form);
            //TODO
            json.put("aaData", list);
//            json.put("sEcho", form.getsEcho());
//            json.put("iTotalRecords", list.size());
//            json.put("iTotalDisplayRecords", list.size());
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }
}