package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.CmsItems;
import net.dlyt.qyds.common.dto.CmsMaster;
import net.dlyt.qyds.common.dto.CmsMasterGoods;
import net.dlyt.qyds.common.dto.GdsMasterExt;
import net.dlyt.qyds.common.dto.ext.CmsMasterExt;
import net.dlyt.qyds.common.dto.ext.MmbMasterExt;
import net.dlyt.qyds.common.dto.ext.MmbShoppingBagExt;
import net.dlyt.qyds.common.dto.ext.MmbShoppingSKuExt;
import net.dlyt.qyds.common.form.CmsMsterForm;
import net.dlyt.qyds.dao.CmsMasterGoodsMapper;
import net.dlyt.qyds.dao.CmsMasterMapper;
import net.dlyt.qyds.dao.ext.CmsMasterGoodsMapperExt;
import net.dlyt.qyds.dao.ext.CmsMasterMapperExt;
import net.dlyt.qyds.dao.ext.GdsMasterMapperExt;
import net.dlyt.qyds.web.service.CatcheRemoveService;
import net.dlyt.qyds.web.service.CmsMasterService;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.common.StringUtil;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import net.dlyt.qyds.web.service.exception.ExceptionErrorParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by C_Nagai on 2016/7/30.
 */
@Service("cmsMasterService")
@Transactional(readOnly = true)
public class CmsMasterServiceImpl implements CmsMasterService {

    @Autowired
    private CmsMasterMapper cmsMasterMapper;

    @Autowired
    private CmsMasterMapperExt cmsMasterMapperExt;

    @Autowired
    private CmsMasterGoodsMapperExt cmsMasterGoodsMapperExt;

    @Autowired
    private CmsMasterGoodsMapper cmsMasterGoodsMapper;

    @Autowired
    private GdsMasterMapperExt gdsMasterMapperExt;

    @Autowired
    private CatcheRemoveService catcheRemoveService;

    public JSONObject getList(CmsMsterForm form) {
        JSONObject json = new JSONObject();
        try {
            CmsMasterExt ext = new CmsMasterExt();
            ext.setNeedColumns(Integer.valueOf(form.getiDisplayLength()));
            ext.setStartPoint(Integer.valueOf(form.getiDisplayStart()));
            ext.setTitle(form.getTitle());
            ext.setItemType(form.getItemType());
            ext.setItemId(form.getItemId());

            List<CmsMasterExt> list = cmsMasterMapperExt.selectAll(ext);
            int allCount = cmsMasterMapperExt.getAllDataCount(ext);

            json.put("sEcho", form.getsEcho());
            json.put("iTotalRecords", allCount);
            json.put("iTotalDisplayRecords", allCount);
            json.put("aaData", list);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public int insertSelective(CmsMaster record){


        return cmsMasterMapper.insertSelective(record);
    }

    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public int updateByPrimaryKeySelective(CmsMaster record){
        return cmsMasterMapper.updateByPrimaryKeySelective(record);
    }

    public JSONObject selectCmsMasterByCmsId(CmsMaster record){
        JSONObject json = new JSONObject();
        try {

            // 获取CMS主表数据
            if (!StringUtils.isEmpty(record.getCmsId())) {
                List<CmsMasterExt> list =  cmsMasterMapperExt.selectCmsMasterByCmsId(record);
                if (list.size() == 1) {
                    CmsMasterExt cmsMasterExt = list.get(0);

                    // 选择商品分类的时候
//                    if (Constants.CMS_MASTER_ADS.equals(cmsMasterExt.getItemType())) {
//                        // TODO 没有特殊处理
//
//                    // 选择活动/商品的时候
//                    } else {
                        List<CmsMasterGoods> goodsList = cmsMasterGoodsMapperExt.selectGoodsByCmsId(cmsMasterExt.getCmsId());
                        // 为商品/活动关联商品的时候
                        if (goodsList != null && goodsList.size() > 0) {
                            cmsMasterExt.setActId(goodsList.get(0).getActId());
//                            CmsMasterGoods cmsMasterGoods = null;
//                            String goodsId = "";
//                            for (int i = 0; i < goodsList.size(); i ++) {
//                                cmsMasterGoods = goodsList.get(i);
//                                if (i == 0) {
//                                    cmsMasterExt.setActId(cmsMasterGoods.getActId());
//                                }
//                                if ("".equals(goodsId)) {
//                                    goodsId = cmsMasterGoods.getGoodsId();
//                                } else {
//                                    goodsId = goodsId + "," + cmsMasterGoods.getGoodsId();
//                                }
//                            }
//                            cmsMasterExt.setGoodsId(goodsId);
                            cmsMasterExt.setActGoodsFlag("0");
                        } else {
                            String jsonList = cmsMasterExt.getListJson();
                            JSONObject jsonObj = JSON.parseObject(jsonList);
                            cmsMasterExt.setActId((String)jsonObj.get("value"));
                            if (!"index_figure".equals(cmsMasterExt.getItemCode()) && !"index_new".equals(cmsMasterExt.getItemCode())) {
                                cmsMasterExt.setActGoodsFlag("0");
                            } else {
                                cmsMasterExt.setActGoodsFlag("1");
                            }

                        }
//                    }
                    json.put("data", cmsMasterExt);
                }
            }

            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;

    }

    @Transactional(readOnly = false,rollbackFor = Exception.class)
    @Caching(evict = {  @CacheEvict(value="${new_goods_list_catch_name}",allEntries=true)
            , @CacheEvict(value="${goods_second_main_catch_name}",allEntries=true)
            , @CacheEvict(value="${cms_items_catch_name}",allEntries=true)})
    public JSONObject save(CmsMasterExt form){
        JSONObject json = new JSONObject();
        try {

            // 新建
            if (StringUtils.isEmpty(form.getCmsId())) {
                String randomUUID = UUID.randomUUID().toString();
                form.setCmsId(randomUUID);
                form.setShopId(Constants.ORGID);
                Short sort = cmsMasterMapperExt.selectMaxSrot(form);
                if (sort == null) {
                    form.setSort(Short.valueOf("1"));
                } else {
                    form.setSort( ++sort);
                }
                cmsMasterMapper.insertSelective(form);

                // 选择商品的时候
                if (Constants.CMS_MASTER_GDS.equals(form.getItemType())) {
                    String[] goodsArray = form.getGoodsId().split(",");
//                    // 单选的时候
//                    if (Constants.GOODS_RADIO.equals(form.getGoodsChoiceFlag())) {
//                        String goodsId = form.getGoodsId();
//                        goodsArray = new String[]{goodsId};
//
//                    // 多选的时候
//                    } else {
//                        String goodsId = form.getGoodsId();
//                        goodsArray = goodsId.split(",");
//                    }

                    CmsMasterGoods cmsMasterGoods = null;
                    String actRandomUUID = UUID.randomUUID().toString();
                    for (String goodsId : goodsArray) {
                        if (goodsId != null && !"".equals(goodsId)) {
                            cmsMasterGoods = new CmsMasterGoods();
                            String cmsRandomUUID = UUID.randomUUID().toString();
                            cmsMasterGoods.setCmsGdsId(cmsRandomUUID);
                            cmsMasterGoods.setShopId(Constants.ORGID);
                            cmsMasterGoods.setCmsId(form.getCmsId());
                            cmsMasterGoods.setActId(actRandomUUID);
                            Short cgdSort = cmsMasterGoodsMapperExt.selectMaxSrot(cmsMasterGoods);
                            if (cgdSort == null) {
                                cmsMasterGoods.setSort(Short.valueOf("1"));
                            } else {
                                cmsMasterGoods.setSort( ++cgdSort);
                            }
                            cmsMasterGoods.setGoodsId(goodsId);
                            cmsMasterGoods.setInsertUserId(form.getInsertUserId());
                            cmsMasterGoods.setUpdateUserId(form.getUpdateUserId());
                            cmsMasterGoodsMapper.insertSelective(cmsMasterGoods);
                        }
                    }

                // 选择商品分类的时候
                } else if (Constants.CMS_MASTER_ADS.equals(form.getItemType())) {
                    // TODO 没有特殊处理
                    String goodsId = form.getGoodsId();
                    String[] goodsArray = goodsId.split(",");
                    CmsMasterGoods cmsMasterGoods = null;
                    for (String actGoodsId : goodsArray) {
                        if (actGoodsId != null && !"".equals(actGoodsId)) {
                            cmsMasterGoods = new CmsMasterGoods();
                            String cmsRandomUUID = UUID.randomUUID().toString();
                            cmsMasterGoods.setCmsGdsId(cmsRandomUUID);
                            cmsMasterGoods.setShopId(Constants.ORGID);
                            cmsMasterGoods.setCmsId(form.getCmsId());
                            cmsMasterGoods.setActId(form.getGoodsTypeId());
                            Short cgdSort = cmsMasterGoodsMapperExt.selectMaxSrot(cmsMasterGoods);
                            if (cgdSort == null) {
                                cmsMasterGoods.setSort(Short.valueOf("1"));
                            } else {
                                cmsMasterGoods.setSort( ++cgdSort);
                            }
                            cmsMasterGoods.setGoodsId(actGoodsId);
                            cmsMasterGoods.setInsertUserId(form.getInsertUserId());
                            cmsMasterGoods.setUpdateUserId(form.getUpdateUserId());
                            cmsMasterGoodsMapper.insertSelective(cmsMasterGoods);
                        }
                    }
                // 选择活动的时候
                } else if (Constants.CMS_MASTER_ACT.equals(form.getItemType())) {
                    String goodsId = form.getGoodsId();
                    String[] goodsArray = goodsId.split(",");
                    CmsMasterGoods cmsMasterGoods = null;
                    for (String actGoodsId : goodsArray) {
                        if (actGoodsId != null && !"".equals(actGoodsId)) {
                            cmsMasterGoods = new CmsMasterGoods();
                            String cmsRandomUUID = UUID.randomUUID().toString();
                            cmsMasterGoods.setCmsGdsId(cmsRandomUUID);
                            cmsMasterGoods.setShopId(Constants.ORGID);
                            cmsMasterGoods.setCmsId(form.getCmsId());
                            cmsMasterGoods.setActId(form.getActId());
                            Short cgdSort = cmsMasterGoodsMapperExt.selectMaxSrot(cmsMasterGoods);
                            if (cgdSort == null) {
                                cmsMasterGoods.setSort(Short.valueOf("1"));
                            } else {
                                cmsMasterGoods.setSort( ++cgdSort);
                            }
                            cmsMasterGoods.setGoodsId(actGoodsId);
                            cmsMasterGoods.setInsertUserId(form.getInsertUserId());
                            cmsMasterGoods.setUpdateUserId(form.getUpdateUserId());
                            cmsMasterGoodsMapper.insertSelective(cmsMasterGoods);
                        }
                    }
                }

                // 修改
            } else {
                cmsMasterMapper.updateByPrimaryKeySelective(form);
                // 选择商品的时候
                if (Constants.CMS_MASTER_GDS.equals(form.getItemType())) {
                    List<CmsMasterGoods> goodsList = cmsMasterGoodsMapperExt.selectGoodsByCmsId(form.getCmsId());
                    String goodsIdStr = form.getGoodsId();
                    // 删除呗移除的商品
                    for (int i = 0; i < goodsList.size(); i ++) {
                        if (goodsIdStr.indexOf(goodsList.get(i).getGoodsId()) < 0) {
                            cmsMasterGoodsMapper.deleteByPrimaryKey(goodsList.get(i).getCmsGdsId());
                        } else {
                            if (goodsIdStr.indexOf(goodsList.get(i).getGoodsId()) == 0) {
                                goodsIdStr = goodsIdStr.replace(goodsList.get(i).getGoodsId(), "");
                            } else {
                                goodsIdStr = goodsIdStr.replace("," + goodsList.get(i).getGoodsId(), "");
                            }
                        }
                    }

                    if (goodsIdStr != null && !"".equals(goodsIdStr)) {
                        String[] goodsArray = goodsIdStr.split(",");
                        CmsMasterGoods cmsMasterGoods = null;
                        for (String goodsId : goodsArray) {
                            if (goodsId != null && !"".equals(goodsId)) {
                                cmsMasterGoods = new CmsMasterGoods();
                                String cmsRandomUUID = UUID.randomUUID().toString();
                                cmsMasterGoods.setCmsGdsId(cmsRandomUUID);
                                cmsMasterGoods.setShopId(Constants.ORGID);
                                cmsMasterGoods.setCmsId(form.getCmsId());
                                if(goodsList != null && goodsList.size() > 0){
                                    cmsMasterGoods.setActId(goodsList.get(0).getActId());
                                }else{
                                    cmsMasterGoods.setActId("");
                                }

                                Short cgdSort = cmsMasterGoodsMapperExt.selectMaxSrot(cmsMasterGoods);
                                if (cgdSort == null) {
                                    cmsMasterGoods.setSort(Short.valueOf("1"));
                                } else {
                                    cmsMasterGoods.setSort(++cgdSort);
                                }
                                cmsMasterGoods.setGoodsId(goodsId);
                                cmsMasterGoods.setInsertUserId(form.getInsertUserId());
                                cmsMasterGoods.setUpdateUserId(form.getUpdateUserId());
                                cmsMasterGoodsMapper.insertSelective(cmsMasterGoods);
                            }
                        }
                    }

                    // 选择商品分类的时候
                } else if (Constants.CMS_MASTER_ADS.equals(form.getItemType())) {
                    // TODO 没有特殊处理
//                    String goodsId = form.getGoodsId();
//                    String[] goodsArray = goodsId.split(",");
                        List<CmsMasterGoods> goodsList = cmsMasterGoodsMapperExt.selectGoodsByCmsId(form.getCmsId());
                        String goodsIdStr = form.getGoodsId();
                        if (goodsList != null && goodsList.size() > 0) {
                            String oldActId = goodsList.get(0).getActId();
                            // 删除呗移除的商品
                            if (oldActId.equals(form.getGoodsTypeId())) {
                                for (int i = 0; i < goodsList.size(); i ++) {
                                    if (goodsIdStr.indexOf(goodsList.get(i).getGoodsId()) < 0) {
                                        cmsMasterGoodsMapper.deleteByPrimaryKey(goodsList.get(i).getCmsGdsId());
                                    } else {
                                        if (goodsIdStr.indexOf(goodsList.get(i).getGoodsId()) == 0) {
                                            goodsIdStr = goodsIdStr.replace(goodsList.get(i).getGoodsId(), "");
                                        } else {
                                            goodsIdStr = goodsIdStr.replace("," + goodsList.get(i).getGoodsId(), "");
                                        }
                                    }
                                }
                            } else {
                                for (int i = 0; i < goodsList.size(); i ++) {
                                    cmsMasterGoodsMapper.deleteByPrimaryKey(goodsList.get(i).getCmsGdsId());
                                }
                            }
                        }

                        if (goodsIdStr != null && !"".equals(goodsIdStr)) {
                            String[] goodsArray = goodsIdStr.split(",");
                            CmsMasterGoods cmsMasterGoods = null;
                            for (String actGoodsId : goodsArray) {
                                if (actGoodsId != null && !"".equals(actGoodsId)) {
                                    cmsMasterGoods = new CmsMasterGoods();
                                    String cmsRandomUUID = UUID.randomUUID().toString();
                                    cmsMasterGoods.setCmsGdsId(cmsRandomUUID);
                                    cmsMasterGoods.setShopId(Constants.ORGID);
                                    cmsMasterGoods.setCmsId(form.getCmsId());
                                    cmsMasterGoods.setActId(form.getGoodsTypeId());
                                    Short cgdSort = cmsMasterGoodsMapperExt.selectMaxSrot(cmsMasterGoods);
                                    if (cgdSort == null) {
                                        cmsMasterGoods.setSort(Short.valueOf("1"));
                                    } else {
                                        cmsMasterGoods.setSort( ++cgdSort);
                                    }
                                    cmsMasterGoods.setGoodsId(actGoodsId);
                                    cmsMasterGoods.setInsertUserId(form.getInsertUserId());
                                    cmsMasterGoods.setUpdateUserId(form.getUpdateUserId());
                                    cmsMasterGoodsMapper.insertSelective(cmsMasterGoods);
                                }
                            }
                        }

                    // 选择活动的时候
                } else if (Constants.CMS_MASTER_ACT.equals(form.getItemType())) {
//                    String goodsId = form.getGoodsId();
//                    String[] goodsArray = goodsId.split(",");
                    List<CmsMasterGoods> goodsList = cmsMasterGoodsMapperExt.selectGoodsByCmsId(form.getCmsId());
                    String goodsIdStr = form.getGoodsId();
                    if (goodsList != null && goodsList.size() > 0) {
                        String oldActId = goodsList.get(0).getActId();
                        // 删除呗移除的商品
                        if (form.getActId().equals(oldActId)) {
                            for (int i = 0; i < goodsList.size(); i ++) {
                                if (goodsIdStr.indexOf(goodsList.get(i).getGoodsId()) < 0) {
                                    cmsMasterGoodsMapper.deleteByPrimaryKey(goodsList.get(i).getCmsGdsId());
                                } else {
                                    if (goodsIdStr.indexOf(goodsList.get(i).getGoodsId()) == 0) {
                                        goodsIdStr = goodsIdStr.replace(goodsList.get(i).getGoodsId(), "");
                                    } else {
                                        goodsIdStr = goodsIdStr.replace("," + goodsList.get(i).getGoodsId(), "");
                                    }
                                }
                            }
                        } else {
                            for (int i = 0; i < goodsList.size(); i ++) {
                                cmsMasterGoodsMapper.deleteByPrimaryKey(goodsList.get(i).getCmsGdsId());
                            }
                        }
                    }

                    if (goodsIdStr != null && !"".equals(goodsIdStr)) {
                        String[] goodsArray = goodsIdStr.split(",");
                        CmsMasterGoods cmsMasterGoods = null;
                        for (String actGoodsId : goodsArray) {
                            if (actGoodsId != null && !"".equals(actGoodsId)) {
                                cmsMasterGoods = new CmsMasterGoods();
                                String cmsRandomUUID = UUID.randomUUID().toString();
                                cmsMasterGoods.setCmsGdsId(cmsRandomUUID);
                                cmsMasterGoods.setShopId(Constants.ORGID);
                                cmsMasterGoods.setCmsId(form.getCmsId());
                                cmsMasterGoods.setActId(form.getActId());
                                Short cgdSort = cmsMasterGoodsMapperExt.selectMaxSrot(cmsMasterGoods);
                                if (cgdSort == null) {
                                    cmsMasterGoods.setSort(Short.valueOf("1"));
                                } else {
                                    cmsMasterGoods.setSort( ++cgdSort);
                                }
                                cmsMasterGoods.setGoodsId(actGoodsId);
                                cmsMasterGoods.setInsertUserId(form.getInsertUserId());
                                cmsMasterGoods.setUpdateUserId(form.getUpdateUserId());
                                cmsMasterGoodsMapper.insertSelective(cmsMasterGoods);
                            }
                        }
                    }

                }

            }
            //调用清理缓存 全部
            catcheRemoveService.sendPost(Constants.PC_SOAP_URL+"removeSome.json",null);
            catcheRemoveService.sendPost(Constants.WX_SOAP_URL+"removeSome.json",null);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    /**
     * cms排序列表取得
     * @return
     */
    public JSONObject orderList() {
        JSONObject json = new JSONObject();
        try {

            json.put("resultCode", Constants.NORMAL);
            // 获取所有cms管理数据
            List<CmsMasterExt> childList = cmsMasterMapperExt.selectAllCmsMaster();
            // 获取所有cms栏目数据
            List<CmsMasterExt> parentList = cmsMasterMapperExt.selectAllCmsItems();
            // 获取CMS管理关联数据
            List<CmsMasterExt> childGdsList = cmsMasterMapperExt.selectAllCmsMasterGoods();
            List<CmsMasterExt> list = new ArrayList<CmsMasterExt>();
            list.addAll(parentList);
            list.addAll(childList);
            list.addAll(childGdsList);
            json.put("data", list);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject updateSort(String data) {
        JSONObject map = new JSONObject();

        try{
            List<CmsMasterExt> list = JSONArray.parseArray(data,CmsMasterExt.class);
            for (CmsMasterExt cmsMaster : list) {
                // 变更cms管理排序
                if ("1".equals(cmsMaster.getLevel())) {
                    cmsMasterMapper.updateByPrimaryKeySelective(cmsMaster);

                // 变更cms商品表排序
                } else {
                    CmsMasterGoods cmsMasterGoods = new CmsMasterGoods();
                    cmsMasterGoods.setCmsGdsId(cmsMaster.getCmsId());
                    cmsMasterGoods.setSort(cmsMaster.getSort());
                    cmsMasterGoods.setUpdateUserId(cmsMaster.getUpdateUserId());
                    cmsMasterGoodsMapper.updateByPrimaryKeySelective(cmsMasterGoods);
                }

            }
            map.put("resultCode",Constants.NORMAL);
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
        }
        return map;
    }

    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject resortCmsGdsIds(String data) {
        JSONObject map = new JSONObject();

        try{
            List<CmsMasterGoods> list = JSONArray.parseArray(data,CmsMasterGoods.class);
            for(CmsMasterGoods item : list){
                cmsMasterGoodsMapper.updateByPrimaryKeySelective(item);
            }
            map.put("resultCode",Constants.NORMAL);
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
        }
        return map;
    }

    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject resetCmsGdsIds(String data) {
        JSONObject map = new JSONObject();

        try{
            CmsMasterGoods item = (CmsMasterGoods) JSON.parseObject(data, CmsMasterGoods.class);
            cmsMasterGoodsMapper.updateByPrimaryKeySelective(item);
            map.put("resultCode",Constants.NORMAL);
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
        }
        return map;
    }


    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject delete(String cmsId) {
        JSONObject json = new JSONObject();

        try{
            cmsMasterMapperExt.delete(cmsId);
            json.put("resultCode",Constants.NORMAL);
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    public boolean checkGdsTypeId(String gdsTypeId, String itemId, String cmsId) {
        boolean result = true;
        List<CmsMasterExt> list = cmsMasterMapperExt.selectCmsMasterByItemId(itemId);
        for (CmsMasterExt cmsMasterExt : list) {
            if (!Constants.CMS_MASTER_ADS.equals(cmsMasterExt.getItemType())) {
                continue;
            }
            String listJson = cmsMasterExt.getListJson();
            JSONObject json = JSON.parseObject(listJson);
            String extGdsTypeId = (String)json.get("value");
            if (extGdsTypeId.equals(gdsTypeId) && !cmsMasterExt.getCmsId().equals(cmsId)) {
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     * 根据cmsId获取商品列表
     * @param cmsId
     * @return
     */
    public JSONObject getMasterGoodsByCmsId(String cmsId) {
        JSONObject json = new JSONObject();

        try{
            List<GdsMasterExt> list = gdsMasterMapperExt.getMasterGoodsByCmsId(cmsId);
            json.put("data",list);
            json.put("resultCode",Constants.NORMAL);
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }
}
