package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ErpGoodsSize;
import net.dlyt.qyds.common.dto.ext.ErpGoodsSizeExt;
import net.dlyt.qyds.common.dto.ErpGoodsSizeKey;
import net.dlyt.qyds.dao.ErpGoodsSizeMapper;
import net.dlyt.qyds.dao.ext.ErpGoodsSizeMapperExt;
import net.dlyt.qyds.web.service.CatcheRemoveService;
import net.dlyt.qyds.web.service.ErpGoodsSizeService;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zlh on 2016/7/29.
 */
@Service("erpGoodsSizeService")
@Transactional(readOnly = true)
public class ErpGoodsSizeServiceImpl implements ErpGoodsSizeService {

    @Autowired
    private ErpGoodsSizeMapper mapper;

    @Autowired
    private ErpGoodsSizeMapperExt mapperExt;

    @Autowired
    private CatcheRemoveService catcheRemoveService;

    /**
     * 根据Id获取商品尺码列表
     *
     * @return
     */
    public JSONObject selectAll(){
        JSONObject map = new JSONObject();
        try{
            List<ErpGoodsSize> list = (List<ErpGoodsSize>) mapperExt.selectAll();
            map.put("data", list);
            map.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            map.put("resultCode", Constants.FAIL);
//            map.put("message", e.getMessage());
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 根据ID获取商品尺码信息
     *
     * @param key
     * @return
     */
    public JSONObject getByKey(String key){
        JSONObject map = new JSONObject();
        try{
            ErpGoodsSizeKey k = JSON.parseObject(key, ErpGoodsSizeKey.class);
            ErpGoodsSize data = mapper.selectByPrimaryKey(k);
            map.put("data", data);
            map.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            map.put("resultCode", Constants.FAIL);
//            map.put("message", e.getMessage());
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 根据ID更新商品尺码信息
     *
     * @param record
     */
    public JSONObject updateByKey(String record){
        JSONObject map = new JSONObject();
        try{
            ErpGoodsSize rec = JSON.parseObject(record, ErpGoodsSize.class);
            mapper.updateByPrimaryKey(rec);
            map.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            map.put("resultCode", Constants.FAIL);
//            map.put("message", e.getMessage());
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 新建商品尺码信息
     *
     * @param record
     */
    public JSONObject insert(String record){
        JSONObject map = new JSONObject();
        try{
            ErpGoodsSize rec = JSON.parseObject(record, ErpGoodsSize.class);
            mapper.insertSelective(rec);
            map.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            map.put("resultCode", Constants.FAIL);
//            map.put("message", e.getMessage());
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 根据ID物理删除商品尺码信息
     *
     * @param key
     */
    public JSONObject deleteByPrimaryKey(String key){
        JSONObject map = new JSONObject();
        try{
            ErpGoodsSizeKey k = JSON.parseObject(key, ErpGoodsSizeKey.class);
            mapper.deleteByPrimaryKey(k);
            map.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            map.put("resultCode", Constants.FAIL);
//            map.put("message", e.getMessage());
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
   }

    /**
     * 物理删除所有商品尺码信息
     *
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject deleteAll(){
        JSONObject map = new JSONObject();
        try{
            mapperExt.deleteAll();
            map.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            map.put("resultCode", Constants.FAIL);
//            map.put("message", e.getMessage());
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 删除原有数据,批量导入商品尺码信息
     *
     * @param list
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    @Caching(evict = { @CacheEvict(value="${goods_list_catch_name}",allEntries=true), @CacheEvict(value="${new_goods_list_catch_name}",allEntries=true)
            , @CacheEvict(value="${activity_goods_list_catch_name}",allEntries=true) , @CacheEvict(value="${goods_second_main_catch_name}",allEntries=true)
            , @CacheEvict(value="${cms_items_catch_name}",allEntries=true)})
    public JSONObject inputAll(String list){
        JSONObject map = new JSONObject();
        try{
            //全部删除
            mapperExt.deleteAll();
            List<ErpGoodsSize> ll = JSON.parseArray(list,ErpGoodsSize.class);
            for (ErpGoodsSize record : ll) {
                //插入
                mapper.insertSelective(record);
            }

            //调用清理缓存 全部
            catcheRemoveService.sendPost(Constants.PC_SOAP_URL+"removeAll.json",null);
            catcheRemoveService.sendPost(Constants.WX_SOAP_URL+"removeAll.json",null);
            map.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            map.put("resultCode", Constants.FAIL);
//            map.put("message", e.getMessage());
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 批量更新商品信息
     *
     * @param list
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    @Caching(evict = { @CacheEvict(value="${goods_list_catch_name}",allEntries=true), @CacheEvict(value="${new_goods_list_catch_name}",allEntries=true)
            , @CacheEvict(value="${activity_goods_list_catch_name}",allEntries=true) , @CacheEvict(value="${goods_second_main_catch_name}",allEntries=true)
            , @CacheEvict(value="${cms_items_catch_name}",allEntries=true)})
    public JSONObject updateByList(String list){
        JSONObject map = new JSONObject();
        try{
            List<ErpGoodsSizeExt> ll = JSON.parseArray(list,ErpGoodsSizeExt.class);
            for (ErpGoodsSizeExt record : ll) {
                String style = record.getStyle();
                if(style.equals("10")) {
                    //新增
                    mapper.insertSelective(record);
                } else if (style.equals("20")) {
                    //修改
                    mapperExt.updateNoTime(record);
                } else if (style.equals("30")) {
                    //删除
                    mapper.deleteByPrimaryKey(record);
                } else {
                }
            }
            //调用清理缓存 全部
            catcheRemoveService.sendPost(Constants.PC_SOAP_URL+"removeAll.json",null);
            catcheRemoveService.sendPost(Constants.WX_SOAP_URL+"removeAll.json",null);
            map.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            map.put("resultCode", Constants.FAIL);
//            map.put("message", e.getMessage());
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }
}
