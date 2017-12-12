package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.GdsMasterExt;
import net.dlyt.qyds.common.dto.MmbCollection;
import net.dlyt.qyds.common.dto.ext.MmbCollectionExt;
import net.dlyt.qyds.common.form.MmbCollectionForm;
import net.dlyt.qyds.dao.MmbCollectionMapper;
import net.dlyt.qyds.dao.ext.GdsMasterMapperExt;
import net.dlyt.qyds.dao.ext.MmbCollectionMapperExt;
import net.dlyt.qyds.web.service.MmbCollectionService;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.common.StringUtil;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import net.dlyt.qyds.web.service.exception.ExceptionErrorParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by YiLian on 16/8/2.
 */
@Service("mmbCollectionService")
public class MmbCollectionServiceImpl implements MmbCollectionService {

    @Autowired
    private MmbCollectionMapperExt mmbCollectionMapperExt;

    @Autowired
    private MmbCollectionMapper mmbCollectionMapper;

    @Autowired
    private GdsMasterMapperExt gdsMasterMapperExt;

    /**
     * 收藏一览
     *
     * @param form memberId:会员ID
     *             type:收藏类型(10.商品，20.店铺)
     *             lastUpdateTime:分页标记(最后一条修改时间)
     * @return
     */
    public JSONObject getList(MmbCollectionForm form) {
        JSONObject json = new JSONObject();
        try {

            if (StringUtil.isEmpty(form.getMemberId())) {
                throw new ExceptionErrorParam("缺少参数会员ID");
            }

            List<MmbCollectionExt> list;
            int allCount = 0;

            if (form.getPageSize() > 0) {
                form.setiDisplayLength(form.getPageSize());
                int iDisplayStart = form.getPageSize() * (form.getCurrentPage() - 1);
                form.setiDisplayStart(iDisplayStart);
                list = mmbCollectionMapperExt.getListForPC(form);
                allCount = mmbCollectionMapperExt.getCountForPC(form);
            } else {
                list = mmbCollectionMapperExt.queryList(form);
            }


            GdsMasterExt gdsMasterExt;
            for (MmbCollectionExt item : list) {
                gdsMasterExt = new GdsMasterExt();
                gdsMasterExt.setGoodsId(item.getObjectId());
                List<GdsMasterExt> pList = gdsMasterMapperExt.selectProductsByParameter(gdsMasterExt);
                if (pList != null && pList.size() > 0) {
                    item.setGdsMasterExt(pList.get(0));
                }

                int minCount = mmbCollectionMapperExt.queryMinCountOfGoods(item.getObjectId());
                if(minCount < Constants.INVENTORY_WARNING_COUNT){
                    item.setHasEnough("1");
                } else {
                    item.setHasEnough("0");
                }
            }

            json.put("results", list);
            if (form.getPageSize() > 0) {
                json.put("currentPage", form.getCurrentPage());
                json.put("pageSize", form.getPageSize());
                json.put("iTotalRecords", allCount);
                json.put("totalPage", (allCount % form.getPageSize()) == 0 ? allCount / form.getPageSize() : (allCount / form.getPageSize() + 1));
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
     * 收藏一览
     *
     * @param form memberId:会员ID
     *             type:收藏类型(10.商品，20.店铺)
     *             lastUpdateTime:分页标记(最后一条修改时间)
     * @return
     */
    public JSONObject getPhoneList(MmbCollectionForm form) {
        JSONObject json = new JSONObject();
        try {

            if (StringUtil.isEmpty(form.getMemberId())) {
                throw new ExceptionErrorParam("缺少参数会员ID");
            }

            List<MmbCollectionExt> list;
            int allCount = 0;

            if (form.getPageSize() > 0) {
                form.setiDisplayLength(form.getPageSize());
                int iDisplayStart = form.getPageSize() * (form.getCurrentPage() - 1);
                form.setiDisplayStart(iDisplayStart);
                list = mmbCollectionMapperExt.getListForPC(form);
                allCount = mmbCollectionMapperExt.getCountForPC(form);
            } else {
                list = mmbCollectionMapperExt.queryList(form);
            }


            GdsMasterExt gdsMasterExt;
            for (MmbCollectionExt item : list) {
                gdsMasterExt = new GdsMasterExt();
                gdsMasterExt.setGoodsId(item.getObjectId());
                List<GdsMasterExt> pList = gdsMasterMapperExt.selectProductsByParameter(gdsMasterExt);
                if (pList != null && pList.size() > 0) {
                    item.setGdsMasterExt(pList.get(0));
                }

                int minCount = mmbCollectionMapperExt.queryMinCountOfGoods(item.getObjectId());
                if(minCount < Constants.INVENTORY_WARNING_COUNT){
                    item.setHasEnough("1");
                } else {
                    item.setHasEnough("0");
                }
            }

            json.put("results", list);
            if (form.getPageSize() > 0) {
                json.put("currentPage", form.getCurrentPage());
                json.put("pageSize", form.getPageSize());
                json.put("iTotalRecords", allCount);
                json.put("totalPage", (allCount % form.getPageSize()) == 0 ? allCount / form.getPageSize() : (allCount / form.getPageSize() + 1));
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
     * 添加收藏
     *
     * @param form memberId:会员ID
     *             type:收藏类型(10.商品，20.店铺)
     *             objectId:收藏对象ID
     *             name:名称
     *             url:收藏URL
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JSONObject add(MmbCollectionForm form) {
        JSONObject json = new JSONObject();
        try {

            if (StringUtil.isEmpty(form.getMemberId())) {
                throw new ExceptionErrorParam("缺少参数会员ID");
            }

            if (StringUtil.isEmpty(form.getType())) {
                throw new ExceptionErrorParam("缺少参数收藏类型");
            }

            if (StringUtil.isEmpty(form.getObjectId())) {
                throw new ExceptionErrorParam("缺少参数收藏对象ID");
            }

            if (StringUtil.isEmpty(form.getName())) {
                throw new ExceptionErrorParam("缺少参数名称");
            }

//            if (StringUtil.isEmpty(form.getUrl())) {
//                throw new ExceptionErrorParam("缺少参数收藏URL");
//            }

            // 查找重复收藏
            MmbCollectionExt mmbCollectionExt = mmbCollectionMapperExt.selectSameCollection(form);

            Date now = new Date();

            if (mmbCollectionExt != null) {
                // 已经存在的收藏更新收藏时间
                MmbCollection record = mmbCollectionMapper.selectByPrimaryKey(mmbCollectionExt.getCollectNo());

                if (record != null) {

                    record.setCollectTime(now);
                    record.setUpdateTime(now);
                    record.setUpdateUserId(form.getMemberId());
                    mmbCollectionMapper.updateByPrimaryKeySelective(record);
                }

            } else {

                MmbCollection record = new MmbCollection();

                // 会员ID
                record.setMemberId(form.getMemberId());
                //收藏类型
                record.setType(form.getType());
                //收藏对象ID
                record.setObjectId(form.getObjectId());
                //名称
                record.setName(form.getName());
                //收藏URL
                record.setUrl(form.getUrl());
                //收藏时间
                record.setCollectTime(now);
                //删除标记
                record.setDeleted("0");
                //修改人
                record.setUpdateUserId(form.getMemberId());
                //修改时间
                record.setUpdateTime(now);
                //创建人
                record.setInsertUserId(form.getMemberId());
                //创建时间
                record.setInsertTime(now);

                mmbCollectionMapper.insertSelective(record);
            }

            MmbCollectionExt result = mmbCollectionMapperExt.selectSameCollection(form);

            json.put("result", result);
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
     * 删除收藏
     *
     * @param form memberId:会员ID
     *             collectNo:收藏编号
     *             collections:批量删除
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JSONObject delete(MmbCollectionForm form) {
        JSONObject json = new JSONObject();
        try {

            if (StringUtil.isEmpty(form.getMemberId())) {
                throw new ExceptionErrorParam("缺少参数会员ID");
            }

            boolean hasDeleted = false;
            // 批量删除
            if (null != form.getCollections() && form.getCollections().size() > 0) {

                for (Integer collectNo : form.getCollections()) {
                    MmbCollection item = mmbCollectionMapper.selectByPrimaryKey(collectNo);

                    if (item != null && "0".equals(item.getDeleted())) {
                        //删除标记
                        item.setDeleted("1");
                        //修改人
                        item.setUpdateUserId(form.getMemberId());
                        //修改时间
                        item.setUpdateTime(new Date());

                        mmbCollectionMapper.updateByPrimaryKeySelective(item);
                    }

                    if (collectNo == form.getCollectNo()) {
                        // 如果已经删除了,可以减少一次删除操作
                        hasDeleted = true;
                    }
                }
            }

            // 删除单条记录
            if (null != form.getCollectNo() && form.getCollectNo() > 0 && !hasDeleted) {
                MmbCollection record = mmbCollectionMapper.selectByPrimaryKey(form.getCollectNo());

                if (record != null && "0".equals(record.getDeleted())) {
                    //删除标记
                    record.setDeleted("1");
                    //修改人
                    record.setUpdateUserId(form.getMemberId());
                    //修改时间
                    record.setUpdateTime(new Date());

                    mmbCollectionMapper.updateByPrimaryKeySelective(record);
                }
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
     * 获取心愿单数量
     *
     * @param data memberId:会员ID
     *             type:收藏类型(10.商品，20.店铺)
     *             lastUpdateTime:分页标记(最后一条修改时间)
     * @return
     */
    public JSONObject getCountForPC(String data) {
        JSONObject json = new JSONObject();
        try {
            MmbCollectionForm form = (MmbCollectionForm) JSON.parseObject(data, MmbCollectionForm.class);
            if (StringUtil.isEmpty(form.getMemberId())) {
                throw new ExceptionErrorParam("缺少参数会员ID");
            }
            int allCount = mmbCollectionMapperExt.getCountForPC(form);
            json.put("results", allCount);
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
     * 获取心愿单库存情况[库存报警]
     *
     * @param data memberId:会员ID
     * @return
     */
    @Override
    public JSONObject getInventoryAlarming(String data) {
        JSONObject json = new JSONObject();
        try{
            MmbCollectionForm form = (MmbCollectionForm) JSON.parseObject(data, MmbCollectionForm.class);
            if (StringUtil.isEmpty(form.getMemberId())) {
                throw new ExceptionErrorParam("缺少参数会员ID");
            }

            List<HashMap> list = mmbCollectionMapperExt.queryCollectionNewCount(form.getMemberId());

            json.put("resultCode", Constants.NORMAL);
            json.put("resultMessage", "库存充足");

            for (HashMap item: list){
                long count = (long) item.get("count");
                if(count < Constants.INVENTORY_WARNING_COUNT){
                    json = new JSONObject();
                    json.put("resultCode", Constants.NO_STORE);
                    json.put("resultMessage", "心愿单宝贝有库存不足,请不要错过");
                    break;
                }
            }

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
