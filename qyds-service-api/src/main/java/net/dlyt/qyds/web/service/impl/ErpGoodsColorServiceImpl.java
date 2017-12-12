package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ErpGoodsColor;
import net.dlyt.qyds.common.dto.ext.ErpGoodsColorExt;
import net.dlyt.qyds.dao.ErpGoodsColorMapper;
import net.dlyt.qyds.dao.ext.ErpGoodsColorMapperExt;
import net.dlyt.qyds.web.service.CatcheRemoveService;
import net.dlyt.qyds.web.service.ErpGoodsColorService;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.common.StringUtil;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import net.dlyt.qyds.web.service.exception.ExceptionErrorParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zlh on 2016/7/26.
 */
@Service("erpGoodsColorService")
@Transactional(readOnly = true)
public class ErpGoodsColorServiceImpl implements ErpGoodsColorService {

    @Autowired
    private ErpGoodsColorMapper mapper;

    @Autowired
    private ErpGoodsColorMapperExt mapperExt;

    @Autowired
    private CatcheRemoveService catcheRemoveService;

    /**
     * 根据Id获取颜色列表
     *
     * @return
     */
    public JSONObject selectAll(){
        JSONObject json = new JSONObject();
        try{
            List<ErpGoodsColor> list = (List<ErpGoodsColor>) mapperExt.selectAll();
            json.put("data", list);
            json.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            json.put("resultCode", Constants.FAIL);
//            json.put("message", e.getMessage());
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
            json.put("message", e.getMessage());
        }
        return json;
    }

    /**
     * 根据条件获取颜色列表
     *
     * @param record
     * @return
     */
    public JSONObject selectBySelective(String record){
        JSONObject json = new JSONObject();
        try{
            ErpGoodsColor rec = JSON.parseObject(record, ErpGoodsColor.class);
            List<ErpGoodsColor> list = (List<ErpGoodsColor>) mapperExt.selectByPage(rec);
            json.put("data", list);
            json.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            json.put("resultCode", Constants.FAIL);
//            json.put("message", e.getMessage());
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
            json.put("message", e.getMessage());
        }
        return json;
    }

    /**
     * 根据ID获取颜色信息
     *
     * @param id
     * @return
     */
    public JSONObject getById(String id){
        JSONObject json = new JSONObject();
        try{
            if (StringUtil.isEmpty(id)) {
                throw new ExceptionErrorParam("缺少参数颜色ID");
            }
            ErpGoodsColor data = mapper.selectByPrimaryKey(id);
            json.put("data", data);
            json.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            json.put("resultCode", Constants.FAIL);
//            json.put("message", e.getMessage());
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
            json.put("message", e.getMessage());
        }
        return json;
    }

    /**
     * 根据ID更新颜色信息
     *
     * @param record
     */
    public JSONObject updateById(String record){
        JSONObject json = new JSONObject();
        try{
            ErpGoodsColor rec = JSON.parseObject(record, ErpGoodsColor.class);
            if (StringUtil.isEmpty(rec.getColorCode())) {
                throw new ExceptionErrorParam("缺少参数颜色ID");
            }
            mapper.updateByPrimaryKey(rec);
            json.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            json.put("resultCode", Constants.FAIL);
//            json.put("message", e.getMessage());
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
            json.put("message", e.getMessage());
        }
        return json;
    }

    /**
     * 新建颜色信息
     *
     * @param record
     */
    public JSONObject insert(String record){
        JSONObject json = new JSONObject();
        try{
            ErpGoodsColor rec = JSON.parseObject(record, ErpGoodsColor.class);
            if (StringUtil.isEmpty(rec.getColorCode())) {
                throw new ExceptionErrorParam("缺少参数颜色ID");
            }
            mapper.insertSelective(rec);
            json.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            json.put("resultCode", Constants.FAIL);
//            json.put("message", e.getMessage());
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
            json.put("message", e.getMessage());
        }
        return json;
    }

    /**
     * 根据ID物理删除颜色信息
     *
     * @param id
     */
    public JSONObject deleteByPrimaryKey(String id){
        JSONObject json = new JSONObject();
        try{
            if (StringUtil.isEmpty(id)) {
                throw new ExceptionErrorParam("缺少参数颜色ID");
            }
            mapper.deleteByPrimaryKey(id);
            json.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            json.put("resultCode", Constants.FAIL);
//            json.put("message", e.getMessage());
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
            json.put("message", e.getMessage());
        }
        return json;
    }

    /**
     * 物理删除所有颜色信息
     *
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject deleteAll(){
        JSONObject json = new JSONObject();
        try{
            mapperExt.deleteAll();
            json.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            json.put("resultCode", Constants.FAIL);
//            json.put("message", e.getMessage());
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
            json.put("message", e.getMessage());
        }
        return json;
    }

    /**
     * 删除原有数据,批量导入颜色信息
     *
     * @param data
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    @Caching(evict = { @CacheEvict(value="${goods_list_catch_name}",allEntries=true), @CacheEvict(value="${new_goods_list_catch_name}",allEntries=true)
            , @CacheEvict(value="${activity_goods_list_catch_name}",allEntries=true) , @CacheEvict(value="${goods_second_main_catch_name}",allEntries=true)
            , @CacheEvict(value="${cms_items_catch_name}",allEntries=true)})
    public JSONObject inputAll(String data){
        JSONObject json = new JSONObject();
        try{
            //全部删除
            mapperExt.deleteAll();
            List<ErpGoodsColor> list = JSON.parseArray(data,ErpGoodsColor.class);
            for (ErpGoodsColor record : list) {
                //插入
                mapper.insertSelective(record);
            }
            //调用清理缓存 全部
            catcheRemoveService.sendPost(Constants.PC_SOAP_URL+"removeAll.json",null);
            catcheRemoveService.sendPost(Constants.WX_SOAP_URL+"removeAll.json",null);
            json.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            json.put("resultCode", Constants.FAIL);
//            json.put("message", e.getMessage());
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
            json.put("message", e.getMessage());
        }
        return json;
    }

    /**
     * 批量更新颜色信息
     *
     * @param data
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    @Caching(evict = { @CacheEvict(value="${goods_list_catch_name}",allEntries=true), @CacheEvict(value="${new_goods_list_catch_name}",allEntries=true)
            , @CacheEvict(value="${activity_goods_list_catch_name}",allEntries=true) , @CacheEvict(value="${goods_second_main_catch_name}",allEntries=true)
            , @CacheEvict(value="${cms_items_catch_name}",allEntries=true)})
    public JSONObject updateByList(String data){
        JSONObject json = new JSONObject();
        try{
            //全部删除
            List<ErpGoodsColorExt> list = JSON.parseArray(data,ErpGoodsColorExt.class);
            for (ErpGoodsColorExt record : list) {
                String style = record.getStyle();
                if(style.equals("10")) {
                    //新增
                    mapper.insertSelective(record);
                } else if (style.equals("20")) {
                    //修改
                    mapperExt.updateNoTime(record);
                } else if (style.equals("30")) {
                    //删除
                    mapper.deleteByPrimaryKey(record.getColorCode());
                } else {
                }
            }
            //调用清理缓存 全部
            catcheRemoveService.sendPost(Constants.PC_SOAP_URL+"removeAll.json",null);
            catcheRemoveService.sendPost(Constants.WX_SOAP_URL+"removeAll.json",null);
            json.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            json.put("resultCode", Constants.FAIL);
//            json.put("message", e.getMessage());
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
            json.put("message", e.getMessage());
        }
        return json;
    }
}
