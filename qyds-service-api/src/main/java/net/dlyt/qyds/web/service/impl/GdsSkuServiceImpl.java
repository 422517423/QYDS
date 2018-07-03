package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.*;
import net.dlyt.qyds.dao.GdsDetailMapper;
import net.dlyt.qyds.dao.GdsMasterMapper;
import net.dlyt.qyds.dao.GdsSkuMapper;
import net.dlyt.qyds.dao.ext.BnkMasterMapperExt;
import net.dlyt.qyds.dao.ext.GdsMasterMapperExt;
import net.dlyt.qyds.dao.ext.GdsSkuMapperExt;
import net.dlyt.qyds.web.service.GdsMasterService;
import net.dlyt.qyds.web.service.GdsSkuService;
import net.dlyt.qyds.web.service.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by congkeyan on 2016/7/19.
 */
@Service("gdsSkuService")
@Transactional(readOnly = true)
public class GdsSkuServiceImpl implements GdsSkuService {

    @Autowired
    private GdsSkuMapper gdsSkuMapper;

    @Autowired
    private GdsSkuMapperExt gdsSkuMapperExt;

    @Autowired
    private GdsMasterMapper gdsMasterMapper;

    @Autowired
    private GdsMasterMapperExt gdsMasterMapperExt;

    @Autowired
    private BnkMasterMapperExt bnkMasterMapperExt;


    /**
     * 插入商品SKU表
     * @param data
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject save(String data, Map<String, Object> userMap){
        JSONObject map = new JSONObject();
        try{
            //获取前台传递的数据
            GdsSku gdsSku = (GdsSku) JSON.parseObject(data, GdsSku.class);
            if(gdsSku == null){
                map.put("resultCode", Constants.FAIL);
                return map;
            }

            JSONObject jsonObject = JSON.parseObject(data);
            String actBank = (String)jsonObject.get("actBank");
            //通过goodsID获取商品信息
            GdsMasterExt gdsMasterExt = gdsMasterMapperExt.selectItemByKey(gdsSku.getGoodsId());

            if(StringUtils.isEmpty(gdsSku.getGoodsSkuId())){
                //新建
                //主表
                gdsSku.setDeleted(Constants.DELETED_NO);
                String randomUUID = UUID.randomUUID().toString();
                gdsSku.setGoodsSkuId(randomUUID);
                Date date = new Date();
                gdsSku.setInsertTime(date);
                gdsSku.setUpdateTime(date);

                //填写的实际库存要插入到库存表中(只对电商商品作用)
                if("20".equals(gdsMasterExt.getType())){
                    BnkMaster bnkMaster = new BnkMaster();
                    bnkMaster.setGoodsId(gdsMasterExt.getGoodsId());
                    bnkMaster.setShopId(Constants.ORGID);
                    bnkMaster.setSku(randomUUID);
                    bnkMaster.setGoodsType(gdsMasterExt.getType());
                    bnkMaster.setTypeType(gdsMasterExt.getType());
                    bnkMaster.setGoodsCode(gdsMasterExt.getGoodsCode());
                    bnkMaster.setNewCount(Integer.parseInt(actBank));
                    bnkMaster.setBankType("19");
                    bnkMaster.setDeleted("0");
                    bnkMaster.setInsertTime(date);
                    bnkMaster.setUpdateTime(date);
                    bnkMaster.setInsertUserId((String)userMap.get("loginId"));
                    bnkMaster.setUpdateUserId((String)userMap.get("loginId"));

                    bnkMasterMapperExt.insertSelective(bnkMaster);
                }

                //插入主表和详细表
                this.doInsert(gdsSku);

            }else{
                //编辑
                //主表
                gdsSku.setUpdateTime(new Date());
                gdsSku.setInsertUserId(null);

                //插入主表和详细表
                this.doUpdate(gdsSku);

            }

            //更新主表的更新时间和人
            GdsMaster record = new GdsMaster();
            record.setGoodsId(gdsSku.getGoodsId());
            record.setUpdateTime(new Date());
            record.setUpdateUserId((String)userMap.get("loginId"));
            gdsMasterMapper.updateByPrimaryKeySelective(record);

            //根据商品ID获取sku列表
            List<GdsSku> gdsSkuList = this.selectSkuList(gdsSku.getGoodsId());
            map.put("gdsSkuList",gdsSkuList);
            map.put("resultCode",Constants.NORMAL);
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
        }
        return map;
    }


    /**
     * 编辑商品SKU表
     * @param goodsSkuId,skuKey,goodsId
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject edit(String goodsSkuId,String skuKey,String goodsId){
        JSONObject json = new JSONObject();
        try{
            //默认img
            String imgUri = "";

            if (!StringUtils.isEmpty(goodsSkuId)) {
                //SKU表数据
                GdsSku gdsSku = this.selectByPrimaryKey(goodsSkuId);
                json.put("data", gdsSku);
            }else{
                //根据商品ID获取sku列表
                List<GdsSku> gdsSkuList = this.selectSkuList(goodsId);
                for(GdsSku gdsSku : gdsSkuList){

                    //找到了就跳出循环
                    if(imgUri.trim().length() > 0){
                        break;
                    }

                    String sku = gdsSku.getSku();
                    JSONObject jsonObject = (JSONObject)JSONObject.parse(sku);
                    String key = (String)jsonObject.get("sku_key");
                    String[] strs = skuKey.split("_");
                    for(int i=0; i<strs.length; i++){
                        String keyParameter = strs[i];
                        if(key.contains(keyParameter)){
                            //获取对应的图片
                            JSONArray array = (JSONArray)jsonObject.get("sku_img");
                            if(array.size() > 0){
                                imgUri = array.get(0).toString();
                                break;
                            }
                        }
                    }
                }

            }
            json.put("imgUri", imgUri);
            json.put("resultCode", Constants.NORMAL);
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    /**
     * 插入商品SKU表
     * @param gdsSku
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    private void doInsert(GdsSku gdsSku){
        gdsSkuMapper.insertSelective(gdsSku);
    }

    /**
     * 更新商品SKU表数据
     * @param gdsSku
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    private void doUpdate(GdsSku gdsSku) {
        gdsSkuMapper.updateByPrimaryKeySelective(gdsSku);
    }


    //根据商品ID获取SKU列表
    private List<GdsSku> selectSkuList(String goodsId){
        return gdsSkuMapperExt.selectByGoodsId(goodsId);
    }

    //SKUID获取信息
    private GdsSku selectByPrimaryKey(String goodsSkuId){
        return gdsSkuMapper.selectByPrimaryKey(goodsSkuId);
    }

}
