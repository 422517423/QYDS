package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.*;
import net.dlyt.qyds.dao.BnkMasterMapper;
import net.dlyt.qyds.dao.GdsMasterMapper;
import net.dlyt.qyds.dao.GdsSellMapper;
import net.dlyt.qyds.dao.ext.GdsMasterMapperExt;
import net.dlyt.qyds.dao.ext.GdsSkuMapperExt;
import net.dlyt.qyds.web.service.CatcheRemoveService;
import net.dlyt.qyds.web.service.GdsSellService;
import net.dlyt.qyds.web.service.GdsSkuService;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.common.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by congkeyan on 2016/7/19.
 */
@Service("gdsSellService")
@Transactional(readOnly = true)
public class GdsSellServiceImpl implements GdsSellService {

    @Autowired
    private GdsSellMapper gdsSellMapper;

    @Autowired
    private GdsMasterMapper gdsMasterMapper;

    @Autowired
    private GdsMasterMapperExt gdsMasterMapperExt;

    @Autowired
    private CatcheRemoveService catcheRemoveService;
//
//    @Autowired
//    private BnkMasterMapperExt bnkMasterMapperExt;

    /**
     * 查询上架信息表获取商品的上架信息
     * 检索出已经配置的相关推荐
     * 见错处已经配置的配套商品
     * @param goodsId
     * @return
     */
    public JSONObject edit(String goodsId){
        JSONObject map = new JSONObject();
        try{
            //根据商品ID获取商品上架信息
            GdsSell gdsSell =  gdsSellMapper.selectByPrimaryKey(goodsId);
            if(gdsSell != null){

                List<GdsMasterExt> recommendList = new ArrayList<GdsMasterExt>();
                List<GdsMasterExt> mattingList = new ArrayList<GdsMasterExt>();
                //相关推荐
                String recommend = gdsSell.getRecommendJson();
                if(!StringUtils.isEmpty(recommend)){
                    String[] recommendArray = recommend.split(",");

                    for(int i = 0;i<recommendArray.length;i++){
                        String recommendGoodsId = recommendArray[i];
                        //通过goodsID获取商品信息
                        GdsMasterExt gdsMasterExt = gdsMasterMapperExt.selectItemByKey(recommendGoodsId);
                        recommendList.add(gdsMasterExt);
                    }
                }

                String matting = gdsSell.getMatingJson();
                if(!StringUtils.isEmpty(recommend)){
                    //配套商品
                    String[] mattingArray = matting.split(",");

                    for(int i = 0;i<mattingArray.length;i++){
                        String mattingGoodsId = mattingArray[i];
                        //通过goodsID获取商品信息
                        GdsMasterExt gdsMasterExt = gdsMasterMapperExt.selectItemByKey(mattingGoodsId);
                        mattingList.add(gdsMasterExt);
                    }
                }

                map.put("data",gdsSell);
                map.put("recommendList",recommendList);
                map.put("mattingList",mattingList);
            }

            map.put("resultCode", Constants.NORMAL);
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
        }
        return map;
    }

    /**
     * 保存数据到商品上架信息中
     * @param data
     * @return
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    @Caching(evict = { @CacheEvict(value="${goods_list_catch_name}",allEntries=true), @CacheEvict(value="${new_goods_list_catch_name}",allEntries=true)
            , @CacheEvict(value="${activity_goods_list_catch_name}",allEntries=true) , @CacheEvict(value="${goods_second_main_catch_name}",allEntries=true)
            , @CacheEvict(value="${cms_items_catch_name}",allEntries=true)})
    public JSONObject save(String data, Map<String, Object> userMap) {
        JSONObject map = new JSONObject();
        try{
            GdsSell gdsSell = (GdsSell) JSON.parseObject(data, GdsSell.class);
            if(gdsSell == null){
                map.put("resultCode", Constants.FAIL);
                return map;
            }
            JSONObject jsonObject = JSON.parseObject(data);
//            String actBank = (String)jsonObject.get("actBank");
            //通过goodsID获取商品信息
            GdsMasterExt gdsMasterExt = gdsMasterMapperExt.selectItemByKey(gdsSell.getGoodsId());

            //前台来的推荐商品和配套商品要add到数据库中的对应字段上
            String recommendJson = gdsSell.getRecommendJson();
            String matingJson = gdsSell.getMatingJson();

            //根据商品ID获取商品上架信息
            GdsSell sell =  gdsSellMapper.selectByPrimaryKey(gdsSell.getGoodsId());
            if(sell == null){
                //插入到数据库中
                gdsSell.setUpdateUserId((String)userMap.get("loginId"));
                gdsSell.setInsertUserId((String)userMap.get("loginId"));
                Date date = new Date();
                gdsSell.setUpdateTime(date);
                gdsSell.setInsertTime(date);
                gdsSell.setOnsellInfactTime(DataUtils.formatTimeStampToYMD(date));
                gdsSell.setDeleted("0");
                gdsSell.setRecommendJson(recommendJson);
                gdsSell.setMatingJson(matingJson);
                insertInfo(gdsSell, userMap);

//                //填写的实际库存要插入到库存表中(只对电商商品作用)
//                if("20".equals(gdsMasterExt.getType())){
//                    BnkMaster bnkMaster = new BnkMaster();
//                    bnkMaster.setGoodsId(gdsMasterExt.getGoodsId());
//                    bnkMaster.setShopId(Constants.ORGID);
//                    bnkMaster.setGoodsType(gdsMasterExt.getType());
//                    bnkMaster.setTypeType(gdsMasterExt.getType());
//                    bnkMaster.setGoodsCode(gdsMasterExt.getGoodsCode());
//                    bnkMaster.setNewCount(Integer.parseInt(actBank));
//                    bnkMaster.setBankType("19");
//                    bnkMaster.setDeleted("0");
//                    bnkMaster.setInsertTime(date);
//                    bnkMaster.setUpdateTime(date);
//                    bnkMaster.setInsertUserId((String)userMap.get("loginId"));
//                    bnkMaster.setUpdateUserId((String)userMap.get("loginId"));
//
//                    bnkMasterMapperExt.insertSelective(bnkMaster);
//                }
            }else {
                //更新到数据库中
                gdsSell.setUpdateUserId((String)userMap.get("loginId"));
                Date date = new Date();
                gdsSell.setUpdateTime(date);
                String recommend = sell.getRecommendJson();
                if(StringUtils.isEmpty(recommend)){
                    recommend = "";
                }else {
                    if(!recommend.endsWith(",")){
                        recommend = recommend + ',';
                    }
                }

                gdsSell.setRecommendJson(recommend + recommendJson);
                String mating = sell.getMatingJson();
                if(StringUtils.isEmpty(mating)){
                    mating = "";
                }else {
                    if(!mating.endsWith(",")){
                        mating = mating + ',';
                    }
                }
                gdsSell.setMatingJson(mating + matingJson);
                updateInfo(gdsSell, userMap);
            }
            //调用清理缓存 全部
            catcheRemoveService.sendPost(Constants.PC_SOAP_URL+"removeAll.json",null);
            catcheRemoveService.sendPost(Constants.WX_SOAP_URL+"removeAll.json",null);
            map.put("resultCode",Constants.NORMAL);
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
        }
        return map;
    }


    //更新商品上架信息
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    private void updateInfo(GdsSell gdsSell, Map<String, Object> userMap){
        gdsSellMapper.updateByPrimaryKeySelective(gdsSell);

        //上架更改商品主表的数据状态
        GdsMaster record = new GdsMaster();
        record.setIsOnsell("0");
        record.setGoodsId(gdsSell.getGoodsId());
        record.setUpdateTime(new Date());
        record.setUpdateUserId((String)userMap.get("loginId"));
        gdsMasterMapper.updateByPrimaryKeySelective(record);
    }

    //插入商品上架信息
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    private void insertInfo(GdsSell gdsSell, Map<String, Object> userMap){
        //上架
        gdsSellMapper.insertSelective(gdsSell);
        //上架更改商品主表的数据状态
        GdsMaster record = new GdsMaster();
        record.setIsOnsell("0");
        record.setGoodsId(gdsSell.getGoodsId());
        record.setUpdateTime(new Date());
        record.setUpdateUserId((String)userMap.get("loginId"));
        gdsMasterMapper.updateByPrimaryKeySelective(record);

    }

    /**
     * 保存数据到商品上架信息中
     * @param goodsIds
     * @return
     */
    public JSONObject selectDatas(String goodsIds) {
        JSONObject map = new JSONObject();
        try{

            if(StringUtils.isEmpty(goodsIds)){
                map.put("resultCode", Constants.FAIL);
                return map;
            }

            String[] goodsIdArray = goodsIds.split(",");
            List<GdsMasterExt> list = new ArrayList<GdsMasterExt>();
            for(int i = 0;i<goodsIdArray.length;i++){
                list.add(gdsMasterMapperExt.selectItemByKey(goodsIdArray[i]));
            }

            map.put("data",list);
            map.put("resultCode",Constants.NORMAL);
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
        }
        return map;
    }

    /**
     * 通过ID删除数据
     * @param goodsId
     * @param delGoodsId
     * @param flag
     * @return
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject delete(String goodsId, String delGoodsId, String flag, Map<String, Object> userMap) {
        JSONObject json = new JSONObject();
        try{
            GdsSell gdsSell =  gdsSellMapper.selectByPrimaryKey(goodsId);
            if("sell_delete_tj".equals(flag)){
                json.put("flag","0");
                //删除推荐的对应字段
                String recommendJson = gdsSell.getRecommendJson();
                String[] recomendArray = recommendJson.split(",");
                String new_recommendJson = "";
                for(int i=0; i<recomendArray.length;i++){
                    //重新拼接 不相等的时候拼接
                    if(!delGoodsId.equals(recomendArray[i])){
                        if(i == recomendArray.length -1){
                            new_recommendJson = new_recommendJson + recomendArray[i] ;
                        }else{
                            new_recommendJson = new_recommendJson + recomendArray[i] + ",";
                        }
                    }
                }
                gdsSell.setRecommendJson(new_recommendJson);
            }else{
                json.put("flag","1");
                //删除推荐的对应字段
                String matingJson = gdsSell.getMatingJson();
                String[] matingArray = matingJson.split(",");
                String new_matingJson = "";
                for(int i=0; i<matingArray.length;i++){
                    //重新拼接 不相等的时候拼接
                    if(!delGoodsId.equals(matingArray[i])){
                        if(i == matingArray.length -1){
                            new_matingJson = new_matingJson + matingArray[i] ;
                        }else{
                            new_matingJson = new_matingJson + matingArray[i] + ",";
                        }
                    }
                }
                gdsSell.setMatingJson(new_matingJson);
            }
            updateInfo(gdsSell, userMap);
            json.put("data",gdsSell);
            json.put("resultCode", Constants.NORMAL);
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

}
