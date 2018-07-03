package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.GdsBrand;
import net.dlyt.qyds.common.dto.GdsBrandExt;
import net.dlyt.qyds.common.form.GdsBrandForm;
import net.dlyt.qyds.dao.GdsBrandMapper;
import net.dlyt.qyds.dao.ext.GdsBrandMapperExt;
import net.dlyt.qyds.web.service.GdsBrandService;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.common.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by congkeyan on 2016/7/19.
 */
@Service("gdsBrandService")
@Transactional(readOnly = true)
public class GdsBrandServiceImpl implements GdsBrandService {

    @Autowired
    private GdsBrandMapperExt gdsBrandMapperExt;

    @Autowired
    private GdsBrandMapper gdsBrandMapper;

    /**
     * 获取商品品牌列表
     * @return
     */
    public JSONObject selectAll(GdsBrandForm form){
        JSONObject json = new JSONObject();
        try{
            GdsBrandExt ext = new GdsBrandExt();
            ext.setShopId(Constants.ORGID);
            ext.setBrandName(form.getBrandName());
            ext.setType(form.getType());
            ext.setNeedColumns(Integer.parseInt(form.getiDisplayLength()));
            ext.setStartPoint(Integer.parseInt(form.getiDisplayStart()));

            //获取商品品牌列表
            List<GdsBrandExt> list = gdsBrandMapperExt.selectAll(ext);
            //获取商品的总数
            int allCount = gdsBrandMapperExt.getAllDataCount(ext);

            json.put("aaData", list);
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
     * 获取商品品牌列表(没有翻页,没有检索条件)
     * @return
     */
    public JSONObject getAllList(String type){

        JSONObject json = new JSONObject();
        try{
            GdsBrandExt ext = new GdsBrandExt();
            ext.setShopId(Constants.ORGID);
            if(StringUtil.isEmpty(type) || "0".equals(type)){
                ext.setType(null);
            }else{
                ext.setType(type);
            }

            //数据库检索
            List<GdsBrandExt> list = gdsBrandMapperExt.getAllList(ext);
            json.put("data", list);
            json.put("resultCode", Constants.NORMAL);

        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    /**
     * 保存数据到商品品牌表中
     * @param gdsBrand
     * @return
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject save(GdsBrand gdsBrand) {
        JSONObject json = new JSONObject();
        try{
            JSONObject jsonObject = new JSONObject();
            gdsBrand.setShopId(Constants.ORGID);
            //新规
            if (StringUtils.isEmpty(gdsBrand.getBrandId())) {
                gdsBrand.setBrandId( UUID.randomUUID().toString());
                gdsBrand.setInsertTime(new Date());
                gdsBrand.setUpdateTime(new Date());
                gdsBrand.setDeleted(Constants.DELETED_NO);
                gdsBrandMapper.insertSelective(gdsBrand);
            } else {
                gdsBrand.setUpdateTime(new Date());
                gdsBrand.setInsertUserId(null);
                if(gdsBrand.getLogoUrl().length() == 0){
                    gdsBrand.setLogoUrl(null);
                }
                gdsBrandMapper.updateByPrimaryKeySelective(gdsBrand);
            }

            json.put("resultCode", Constants.NORMAL);
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    /**
     * 根据Id获取详细数据
     *
     * @param brandId
     * @return
     */
    public JSONObject selectByPrimaryKey(String brandId){

        JSONObject json = new JSONObject();
        try{
            if (!StringUtils.isEmpty(brandId)) {
                GdsBrand gdsBrand = gdsBrandMapper.selectByPrimaryKey(brandId);
                if (gdsBrand != null) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("brand_id", gdsBrand.getBrandId());
                    jsonObject.put("type", gdsBrand.getType());
                    jsonObject.put("brand_code", gdsBrand.getBrandCode());
                    jsonObject.put("brand_name", gdsBrand.getBrandName());
                    jsonObject.put("erp_brand_code", gdsBrand.getErpBrandCode());
                    if(!StringUtils.isEmpty(gdsBrand.getLogoUrl())){
                        jsonObject.put("logo_url", gdsBrand.getLogoUrl());
                    }
                    jsonObject.put("introduce_html", gdsBrand.getIntroduceHtml());

                    json.put("data", jsonObject);
                }
            }
            json.put("resultCode", Constants.NORMAL);
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }

    /**
     * 删除品牌信息
     * @param brandId
     * @return
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject delete(String brandId){
        JSONObject json = new JSONObject();
        try{
            if (!StringUtils.isEmpty(brandId)) {
                GdsBrand gdsBrand = new GdsBrand();
                gdsBrand.setBrandId(brandId);
                gdsBrand.setDeleted("1");
                gdsBrandMapper.updateByPrimaryKeySelective(gdsBrand);
            }
            json.put("resultCode", Constants.NORMAL);
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
        }
        return json;

    }
}
