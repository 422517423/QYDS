package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.GdsMasterExt;
import net.dlyt.qyds.common.dto.GdsSell;
import net.dlyt.qyds.common.dto.GdsSuitlist;
import net.dlyt.qyds.common.form.GdsMasterForm;
import net.dlyt.qyds.dao.GdsSellMapper;
import net.dlyt.qyds.dao.GdsSuitlistMapper;
import net.dlyt.qyds.dao.ext.GdsMasterMapperExt;
import net.dlyt.qyds.dao.ext.GdsSuitlistMapperExt;
import net.dlyt.qyds.web.service.GdsSuitlistService;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.common.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by congkeyan on 2016/7/19.
 */
@Service("gdsSuitlistService")
@Transactional(readOnly = true)
public class GdsSuitlistServiceImpl implements GdsSuitlistService {

    @Autowired
    private GdsSuitlistMapper gdsSuitlistMapper;

    @Autowired
    private GdsSuitlistMapperExt gdsSuitlistMapperExt;

    @Autowired
    private GdsMasterMapperExt gdsMasterMapperExt;

    @Autowired
    private GdsSellMapper gdsSellMapper;

    /**
     * 根据店铺ID获取商品品牌列表
     * @param form
     * @return
     */
    public JSONObject getList(GdsMasterForm form) {
        JSONObject json = new JSONObject();
        try{
            GdsMasterExt ext = new GdsMasterExt();
            ext.setShopId(Constants.ORGID);
            ext.setGoodsName(form.getGoodsName());
            ext.setType(form.getType());
            ext.setNeedColumns(Integer.parseInt(form.getiDisplayLength()));
            ext.setStartPoint(Integer.parseInt(form.getiDisplayStart()));
            ext.setIsOnsell(form.getIsOnsell());
            ext.setGoodsCode(form.getGoodsCode());
            ext.setMaintainStatus(form.getMaintainStatus());

            //数据库检索 -- 过滤数据
            List<GdsMasterExt> list = gdsSuitlistMapperExt.selectAllForSuitlist(ext);
            int allCount = gdsSuitlistMapperExt.getAllDataCountForSuitlist(ext);
            List<GdsSuitlist> suilist = new ArrayList<GdsSuitlist>();
            String sell_json = "";
            if(!StringUtils.isEmpty(form.getPageFrom_sell())){
                GdsSell sell = gdsSellMapper.selectByPrimaryKey(form.getGoodsId());
                if(sell != null){
                    if("0".equals(form.getPageFrom_sell())){
                        //推荐商品
                        sell_json = sell.getRecommendJson();
                    }else{
                        sell_json = sell.getMatingJson();
                    }
                }
            }else{
                //通过商品ID获取已经选择了的套装商品
                suilist = gdsSuitlistMapperExt.selectByGoodsId(form.getGoodsId());
            }


            JSONArray array = new JSONArray();
            JSONObject jsonObject = null;

            //为前台准备数据
            for (GdsMasterExt gdsMasterExt : list) {
                jsonObject = new JSONObject();
                jsonObject.put("goodsId",gdsMasterExt.getGoodsId());
                //遍历已经选择的套装商品如果存在至标记
                boolean isExist = false;
                for (GdsSuitlist gdsSuitlist : suilist){
                    if(gdsMasterExt.getGoodsId().equals(gdsSuitlist.getGoodsIdSuit())){
                        isExist = true;
                        break;
                    }
                }
                //遍历已经选择的推荐和配套商品如果存在至标记
                if(!StringUtils.isEmpty(form.getPageFrom_sell())){
                    if(!StringUtils.isEmpty(sell_json)){
                        String[] goodsarray = sell_json.split(",");
                        for(int i=0;i<goodsarray.length;i++){
                            if(gdsMasterExt.getGoodsId().equals(goodsarray[i])){
                                isExist = true;
                                break;
                            }
                        }
                    }
                }


                //如果存在至标记
                if(isExist){
                    jsonObject.put("checked","1");
                }else{
                    jsonObject.put("checked","0");
                }
                jsonObject.put("shopId",gdsMasterExt.getShopId());
                jsonObject.put("type",gdsMasterExt.getType());
                jsonObject.put("typeName",gdsMasterExt.getTypeName());
                jsonObject.put("brand_id",gdsMasterExt.getBrandId());
                jsonObject.put("brandName",gdsMasterExt.getBrandName());
                jsonObject.put("goodsTypeName",gdsMasterExt.getGoodsTypeName());
                jsonObject.put("erpStyleNo",gdsMasterExt.getErpStyleNo());
                jsonObject.put("goodsCode",gdsMasterExt.getGoodsCode());
                jsonObject.put("goodsName",gdsMasterExt.getGoodsName());
                jsonObject.put("erpGoodsCode",gdsMasterExt.getErpGoodsCode());
                jsonObject.put("erpGoodsName",gdsMasterExt.getErpGoodsName());
                jsonObject.put("maintainStatus",gdsMasterExt.getMaintainStatus());
                jsonObject.put("maintainStatusName",gdsMasterExt.getMaintainStatusName());
                jsonObject.put("isOnsell",gdsMasterExt.getIsOnsell());
                jsonObject.put("isWaste",gdsMasterExt.getIsWaste());
                jsonObject.put("deleted", gdsMasterExt.getDeleted());
                jsonObject.put("updateUserId", gdsMasterExt.getUpdateUserId());
                jsonObject.put("insertUserId", gdsMasterExt.getInsertUserId());
                jsonObject.put("insertTime", DataUtils.formatTimeStampToYMD(gdsMasterExt.getInsertTime()));
                jsonObject.put("updateTime",gdsMasterExt.getUpdateTime());
                jsonObject.put("createUserName",gdsMasterExt.getLoginUserName());
                array.add(jsonObject);
            }
            json.put("aaData", array);
            json.put("sEcho",form.getsEcho());
            json.put("iTotalRecords",allCount);
            json.put("iTotalDisplayRecords",allCount);


            json.put("resultCode", Constants.NORMAL);

        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    /**
     * 保存数据到商品SKU表中
     * @param data
     * @return
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject save(String data, Map<String, Object> userMap) {
        JSONObject map = new JSONObject();
        try{

            //获取前台传递的数据
            if(data == null || data.trim().length() == 0){
                map.put("resultCode", Constants.FAIL);
                return map;
            }
            String loginUserId = (String)userMap.get("loginId");
            doInsert(data,loginUserId);
            map.put("resultCode",Constants.NORMAL);
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
        }
        return map;
    }

    /**
     * 获取已经选择的商品信息
     * @param form
     * @return
     */
    public JSONObject getSelectList(GdsMasterForm form) {
        JSONObject json = new JSONObject();
        try{
            List<GdsMasterExt> list = new ArrayList<GdsMasterExt>();
            //本接口有两部分数据组成
            //1弹出画面中选择的商品
            String goodsIds = form.getGoodsIds();
            if(!StringUtils.isEmpty(goodsIds)){
                String[] ids = goodsIds.split(",");
                for(int i=0;i<ids.length;i++){
                    String goodsId = ids[i];
                    GdsMasterExt gdsMasterExt = gdsMasterMapperExt.selectItemByKey(goodsId);
                    list.add(gdsMasterExt);
                }
            }
            int allCount = list.size();
            //2是之前添加过的数据通过商品表商品ID和套餐表的新商品ID关联获取
            GdsMasterExt ext = new GdsMasterExt();
            ext.setShopId(Constants.ORGID);
            ext.setNeedColumns(Integer.parseInt(form.getiDisplayLength()));
            ext.setStartPoint(Integer.parseInt(form.getiDisplayStart()));
            ext.setGoodsId(form.getGoodsId());

            //数据库检索 -- 过滤数据
            List<GdsMasterExt> listData = gdsSuitlistMapperExt.selectAllByGoodsId(ext);
            list.addAll(listData);
            int allCountData = gdsSuitlistMapperExt.getAllDataCountByGoodsId(ext);
            allCount = allCount + allCountData;

            JSONArray array = new JSONArray();
            JSONObject jsonObject = null;

            //为前台准备数据
            for (GdsMasterExt gdsMasterExt : list) {
                jsonObject = new JSONObject();
                jsonObject.put("suitId",gdsMasterExt.getSuitId());
                jsonObject.put("goodsId",gdsMasterExt.getGoodsId());
                jsonObject.put("shopId",gdsMasterExt.getShopId());
                jsonObject.put("type",gdsMasterExt.getType());
                jsonObject.put("typeName",gdsMasterExt.getTypeName());
                jsonObject.put("brand_id",gdsMasterExt.getBrandId());
                jsonObject.put("brandName",gdsMasterExt.getBrandName());
                jsonObject.put("goodsTypeName",gdsMasterExt.getGoodsTypeName());
                jsonObject.put("erpStyleNo",gdsMasterExt.getErpStyleNo());
                jsonObject.put("goodsCode",gdsMasterExt.getGoodsCode());
                jsonObject.put("goodsName",gdsMasterExt.getGoodsName());
                jsonObject.put("erpGoodsCode",gdsMasterExt.getErpGoodsCode());
                jsonObject.put("erpGoodsName",gdsMasterExt.getErpGoodsName());
                jsonObject.put("maintainStatus",gdsMasterExt.getMaintainStatus());
                jsonObject.put("maintainStatusName",gdsMasterExt.getMaintainStatusName());
                jsonObject.put("isOnsell",gdsMasterExt.getIsOnsell());
                jsonObject.put("isWaste",gdsMasterExt.getIsWaste());
                jsonObject.put("deleted", gdsMasterExt.getDeleted());
                jsonObject.put("updateUserId", gdsMasterExt.getUpdateUserId());
                jsonObject.put("insertUserId", gdsMasterExt.getInsertUserId());
                jsonObject.put("insertTime", DataUtils.formatTimeStampToYMD(gdsMasterExt.getInsertTime()));
                jsonObject.put("updateTime",gdsMasterExt.getUpdateTime());
                jsonObject.put("createUserName",gdsMasterExt.getLoginUserName());
                array.add(jsonObject);
            }
            json.put("aaData", array);
            json.put("sEcho",form.getsEcho());
            json.put("iTotalRecords",allCount);
            json.put("iTotalDisplayRecords",allCount);


            json.put("resultCode", Constants.NORMAL);

        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

//    /**
//     * 获取等选择的商品列表
//     * @return
//     */
//    public List<GdsMasterExt> selectAllForSuitlist(GdsMasterExt ext){
//        List<GdsMasterExt> list = gdsSuitlistMapperExt.selectAllForSuitlist(ext);
//        return list;
//    }
//
//    /**
//     * 获取带选择的商品列表 不分页的个数
//     * @return
//     */
//    public int getAllDataCountForSuitlist(GdsMasterExt ext){
//        return gdsSuitlistMapperExt.getAllDataCountForSuitlist(ext);
//    }

//    /**
//     * 获取已经选择的套装列表
//     * @return
//     */
//    public List<GdsMasterExt> selectAllByGoodsId(GdsMasterExt ext){
//        List<GdsMasterExt> list = gdsSuitlistMapperExt.selectAllByGoodsId(ext);
//        return list;
//    }

//    /**
//     * 获取已经选择的套装列表(不分页)
//     * @return
//     */
//    public List<GdsSuitlist> selectByGoodsId(String goodsId){
//        List<GdsSuitlist> list = gdsSuitlistMapperExt.selectByGoodsId(goodsId);
//        return list;
//    }

    /**
     * 删除已经选择的套装商品数据
     * @param suitId
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject delete(String suitId){
        JSONObject json = new JSONObject();
        try{
            if (!StringUtils.isEmpty(suitId)) {
                GdsSuitlist gdsSuitlist = new GdsSuitlist();
                gdsSuitlist.setSuitId(suitId);
                gdsSuitlist.setDeleted("1");
                gdsSuitlistMapper.updateByPrimaryKeySelective(gdsSuitlist);
            }
            json.put("resultCode", Constants.NORMAL);
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
        }
        return json;

    }



//    /**
//     * 获取已经选择的套装列表总数
//     * @return
//     */
//    public int getAllDataCountByGoodsId(GdsMasterExt ext){
//        return gdsSuitlistMapperExt.getAllDataCountByGoodsId(ext);
//    }

    /**
     * 插入商品套装明细
     * @param ids
     */
//    @Transactional(readOnly = false,rollbackFor = Exception.class)
    private void doInsert(String ids,String loginUserId){

        String[] idArray = ids.split(",");
        //遍历
        for(int i = 0; i < idArray.length; i++){
            GdsSuitlist gdsSuitlist = new GdsSuitlist();
            gdsSuitlist.setSuitId(UUID.randomUUID().toString());
            gdsSuitlist.setGoodsId(idArray[i]);
            gdsSuitlist.setDeleted("0");
            Date date = new Date();
            gdsSuitlist.setInsertTime(date);
            gdsSuitlist.setUpdateTime(date);
            gdsSuitlist.setInsertUserId(loginUserId);
            gdsSuitlist.setUpdateUserId(loginUserId);
            gdsSuitlistMapper.insertSelective(gdsSuitlist);
        }

    }


}
