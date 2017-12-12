package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.*;
import net.dlyt.qyds.common.dto.ext.MmbShoppingBagExt;
import net.dlyt.qyds.common.dto.ext.MmbShoppingSKuExt;
import net.dlyt.qyds.common.form.MmbShoppingBagForm;
import net.dlyt.qyds.common.form.MmbShoppingSkuForm;
import net.dlyt.qyds.dao.*;
import net.dlyt.qyds.dao.ext.GdsMasterMapperExt;
import net.dlyt.qyds.dao.ext.MmbShoppingBagMapperExt;
import net.dlyt.qyds.dao.ext.MmbShoppingSkuMapperExt;
import net.dlyt.qyds.web.service.ActMasterService;
import net.dlyt.qyds.web.service.MmbShoppingBagService;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.common.StringUtil;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import net.dlyt.qyds.web.service.exception.ExceptionErrorData;
import net.dlyt.qyds.web.service.exception.ExceptionErrorParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by YiLian on 16/8/2.
 */
@Service("mmbShoppingBagService")
public class MmbShoppingBagServiceImpl implements MmbShoppingBagService {

    @Autowired
    private MmbShoppingBagMapperExt mmbShoppingBagMapperExt;

    @Autowired
    private MmbShoppingBagMapper mmbShoppingBagMapper;

    @Autowired
    private GdsMasterMapper gdsMasterMapper;

    @Autowired
    private GdsSkuMapper gdsSkuMapper;

    @Autowired
    private MmbShoppingSkuMapper mmbShoppingSkuMapper;

    @Autowired
    private MmbShoppingSkuMapperExt mmbShoppingSkuMapperExt;

    @Autowired
    private ErpGoodsMapper erpGoodsMapper;

    @Autowired
    private ActMasterService actMasterService;

    @Autowired
    private GdsMasterMapperExt gdsMasterMapperExt;

    /**
     * 购物袋商品一览(有分页)
     *
     * @param form memberId:会员ID
     *             lastUpdateTime:最后一条更新时间(分页用)
     * @return
     */
    public JSONObject getList(MmbShoppingBagForm form) {
        JSONObject json = new JSONObject();
        try {

            if (StringUtil.isEmpty(form.getMemberId())) {
                throw new ExceptionErrorParam("缺少参数会员ID");
            }

            List<MmbShoppingBagExt> list = mmbShoppingBagMapperExt.queryList(form);
            for (MmbShoppingBagExt item : list) {
                // 添加SKU详细信息
                List<MmbShoppingSKuExt> skuList = mmbShoppingBagMapperExt.querySKUList(item.getBagNo());
                item.setSkuList(skuList);
            }

            // 添加活动信息
            json = actMasterService.bindActivityForShopingBag(list,form.getMemberId());

        } catch (ExceptionBusiness e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 添加商品(单件商品)
     *
     * @param form 会员ID	memberId
     *             店铺ID	shopId
     *             商品类型	type
     *             商品代码	goodsCode
     *             商品名称	goodsName
     *             商品SKU	sku
     *             件数	quantity
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JSONObject add(MmbShoppingBagForm form) {
        JSONObject json = new JSONObject();
        try {

            addBag(form);

            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 改变单件商品数量
     *
     * @param form 购物袋编号 bagNo
     *             会员ID	memberId
     *             件数	quantity
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JSONObject changeQuantity(MmbShoppingBagForm form) {
        JSONObject json = new JSONObject();
        try {

            if (StringUtil.isEmpty(form.getMemberId())) {
                throw new ExceptionErrorParam("缺少参数会员ID");
            }

            if (null == form.getBagNo()) {
                throw new ExceptionErrorParam("缺少参数购物袋ID");
            }

            if (null == form.getQuantity() || form.getQuantity() == 0) {
                throw new ExceptionErrorParam("参数件数不正确");
            }

            MmbShoppingBag item = mmbShoppingBagMapper.selectByPrimaryKey(form.getBagNo());
            if (item != null && "0".equals(item.getDeleted())
                    && form.getMemberId().equals(item.getMemberId())) {

                item.setQuantity(form.getQuantity());
                item.setUpdateTime(new Date());
                item.setUpdateUserId(form.getMemberId());

                mmbShoppingBagMapper.updateByPrimaryKeySelective(item);
            }

            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }


    /**
     * 改变单件商品活动信息
     *
     * @param form 购物袋编号 bagNo
     *             会员ID	memberId
     *             活动ID	actGoodsId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JSONObject changeActivity(MmbShoppingBagForm form) {
        JSONObject json = new JSONObject();
        try {

            if (StringUtil.isEmpty(form.getMemberId())) {
                throw new ExceptionErrorParam("缺少参数会员ID");
            }

            if (null == form.getBagNo()) {
                throw new ExceptionErrorParam("缺少参数购物袋ID");
            }

            MmbShoppingBag item = mmbShoppingBagMapper.selectByPrimaryKey(form.getBagNo());
            if (item != null && "0".equals(item.getDeleted())
                    && form.getMemberId().equals(item.getMemberId())) {

                item.setActGoodsId(form.getActGoodsId());
                item.setUpdateTime(new Date());
                item.setUpdateUserId(form.getMemberId());

                mmbShoppingBagMapper.updateByPrimaryKeySelective(item);
            }

            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 删除商品
     *
     * @param form 购物袋编号 bagNo
     *             会员ID	memberId
     *             批量删除 delBags
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JSONObject delete(MmbShoppingBagForm form) {

        JSONObject json = new JSONObject();
        try {

            if (StringUtil.isEmpty(form.getMemberId())) {
                throw new ExceptionErrorParam("缺少参数会员ID");
            }

            Boolean hasDeleted = false;

            // 批量删除
            if(form.getDelBags()!=null&&form.getDelBags().size()>0){

                for (String bagNo : form.getDelBags()) {

                    deleteShoppingBag(bagNo, form.getMemberId());

                    // 如果重复则不需要单独删除
                    if (bagNo.equals(form.getBagNo())) {
                        hasDeleted = true;
                    }
                }
            }
            if (!hasDeleted && form.getBagNo() != null) {
                // 单独删除一条记录
                deleteShoppingBag(form.getBagNo(), form.getMemberId());
            }

            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * 删除逻辑
     *
     * @param bagNo
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    private void deleteShoppingBag(String bagNo, String memberId) throws Exception {
        try {

            if (bagNo == null || memberId == null) {
                return;
            }

            MmbShoppingBag item = mmbShoppingBagMapper.selectByPrimaryKey(bagNo);
            if (item != null && "0".equals(item.getDeleted())
                    && memberId.equals(item.getMemberId())) {

                Date now = new Date();
                item.setDeleted("1");
                item.setUpdateTime(now);
                item.setUpdateUserId(memberId);

                mmbShoppingBagMapper.updateByPrimaryKeySelective(item);

                MmbShoppingSku sku = new MmbShoppingSku();
                sku.setBagNo(bagNo);
                sku.setUpdateTime(now);
                sku.setUpdateUserId(memberId);

                mmbShoppingSkuMapperExt.deleteByBagNo(sku);

            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 添加逻辑
     *
     * @param form 会员ID	memberId
     *             商品ID	goodsId
     *             商品活动ID	 actGoodsId
     *             商品SKU	skuList List<MmbShoppingSkuForm>
     *             件数	quantity
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    private void addBag(MmbShoppingBagForm form) throws Exception {
        try {

            if (StringUtil.isEmpty(form.getMemberId())) {
                throw new ExceptionErrorParam("缺少参数会员ID");
            }

            if (StringUtil.isEmpty(form.getGoodsId())) {
                throw new ExceptionErrorParam("缺少参数商品ID");
            }

            if (form.getSkuList() == null || form.getSkuList().size() == 0) {
                throw new ExceptionErrorParam("缺少参数商品SKU");
            }

            if (null == form.getQuantity() || form.getQuantity() == 0) {
                form.setQuantity(1);
            }

            // 商品验证
            GdsMaster gdsMaster = gdsMasterMapper.selectByPrimaryKey(form.getGoodsId());
            if (gdsMaster == null) {
                throw new ExceptionErrorData("商品不存在");
            }

            if (!"0".equals(gdsMaster.getDeleted())) {
                // 0.否，1.是
                throw new ExceptionErrorData("商品已经被删除");
            }

            if (!"0".equals(gdsMaster.getIsOnsell())) {
                // 0.否，1.是
                throw new ExceptionErrorData("商品已经被下架");
            }

            // SKU的数据关联关系
            List<String> skuKeys = new ArrayList<String>();
            for (MmbShoppingSkuForm item : form.getSkuList()) {

                skuKeys.add(item.getSkuId());

                HashMap map = gdsMasterMapperExt.selectSKUInfo(item.getSkuId());

                if(map == null || map.isEmpty()){
                    throw new ExceptionErrorData("商品SKU不存在");
                }
            }


            List<MmbShoppingBagExt> sameList = mmbShoppingBagMapperExt.selectSameShoppingIdGoods(form);

            Boolean isSame = false;

            for (MmbShoppingBagExt item : sameList) {

                List<MmbShoppingSku> list = mmbShoppingSkuMapperExt.selectAllSkuByBagNo(item.getBagNo());
                if (list != null && list.size() == skuKeys.size()) {
                    for (MmbShoppingSku sku : list) {
                        if (!skuKeys.contains(sku.getSkuId())) {
                            // 结束循环,查找下一条
                            break;
                        } else if (skuKeys.contains(sku.getSkuId())
                                && list.indexOf(sku) == list.size() - 1) {
                            // 判断:直到最后一个SKU都能全部匹配包含,则是同一件套装商品
                            isSame = true;
                        }
                    }
                }

                if (isSame) {

                    // 如果存在同样商品ID,则数目相加处理
                    MmbShoppingBag record = new MmbShoppingBag();
                    record.setBagNo(item.getBagNo());
                    // 商品活动ID
                    if (form.getActGoodsId() != null && !form.getActGoodsId().equals(item.getActGoodsId())) {
                        // 如果有新的活动,则替换为最新的活动信息
                        record.setActGoodsId(form.getActGoodsId());
                    }
                    record.setQuantity(item.getQuantity() + form.getQuantity());
                    record.setUpdateTime(new Date());
                    record.setUpdateUserId(form.getMemberId());

                    mmbShoppingBagMapper.updateByPrimaryKeySelective(record);

                    // 已经找到,结束循环
                    break;
                }
            }

            if (isSame) {
                // 已经增加数量就不需要继续操作了
                return;
            }

            // 新增一条购物车信息,同时记录对应的SKU信息
            Date now = new Date();
            String id = UUID.randomUUID().toString();

            // 新增购物袋信息
            MmbShoppingBag record = new MmbShoppingBag();
            // 购物袋编号
            record.setBagNo(id);
            // 会员ID
            record.setMemberId(form.getMemberId());
            // 商品ID
            record.setGoodsId(form.getGoodsId());
            // 商品活动ID
            record.setActGoodsId(form.getActGoodsId());
            // 件数
            record.setQuantity(form.getQuantity());
            // 删除标记
            record.setDeleted("0");
            // 修改人
            record.setUpdateUserId(form.getMemberId());
            // 修改时间
            record.setUpdateTime(now);
            // 创建人
            record.setInsertUserId(form.getMemberId());
            // 创建时间
            record.setInsertTime(now);

            mmbShoppingBagMapper.insertSelective(record);

            for (MmbShoppingSkuForm item : form.getSkuList()) {
                // 插入SKU关联信息
                MmbShoppingSku shoppingSku = new MmbShoppingSku();
                shoppingSku.setBagNo(id);
                shoppingSku.setSkuId(item.getSkuId());
                shoppingSku.setType(item.getType());
                shoppingSku.setDeleted("0");
                shoppingSku.setUpdateUserId(form.getMemberId());
                shoppingSku.setUpdateTime(now);
                shoppingSku.setInsertUserId(form.getMemberId());
                shoppingSku.setInsertTime(now);
                mmbShoppingSkuMapper.insertSelective(shoppingSku);
            }

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 购物袋商品数量
     *
     * @param data memberId:会员ID
     *             lastUpdateTime:最后一条更新时间(分页用)
     * @return
     */
    public JSONObject getCount(String data) {
        JSONObject json = new JSONObject();
        try {
            MmbShoppingBagForm form = JSON.parseObject(data, MmbShoppingBagForm.class);
            if (StringUtil.isEmpty(form.getMemberId())) {
                throw new ExceptionErrorParam("缺少参数会员ID");
            }

            List<MmbShoppingBagExt> list = mmbShoppingBagMapperExt.getCountForPc(form);
            json.put("results", Constants.FAIL);
            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

}
