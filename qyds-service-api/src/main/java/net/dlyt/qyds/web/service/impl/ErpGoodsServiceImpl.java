package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ErpGoods;
import net.dlyt.qyds.common.dto.ext.ErpGoodsExt;
import net.dlyt.qyds.common.form.ErpGoodsForm;
import net.dlyt.qyds.dao.ErpGoodsMapper;
import net.dlyt.qyds.dao.ext.ErpGoodsMapperExt;
import net.dlyt.qyds.web.service.CatcheRemoveService;
import net.dlyt.qyds.web.service.ErpGoodsService;
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
 * Created by zlh on 2016/7/29.
 */
@Service("erpGoodsService")
@Transactional(readOnly = true)
public class ErpGoodsServiceImpl implements ErpGoodsService {

    @Autowired
    private ErpGoodsMapper mapper;

    @Autowired
    private ErpGoodsMapperExt mapperExt;

    @Autowired
    private CatcheRemoveService catcheRemoveService;

    /**
     * 根据Id获取商品列表
     *
     * @return
     */
    public JSONObject selectAll(){
        JSONObject map = new JSONObject();
        try{
            List<ErpGoods> list = (List<ErpGoods>) mapperExt.selectAll();
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
     * 根据商品代码获取商品列表
     *
     * @return
     */
    public JSONObject selectByCode(String code){
        JSONObject map = new JSONObject();
        try{
            if (StringUtil.isEmpty(code)) {
                throw new ExceptionBusiness("参数错误,没有商品代码");
            }
            List<ErpGoodsExt> list = (List<ErpGoodsExt>) mapperExt.selectByCode(code);
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
     * 根据条件获取商品列表
     *
     * @param record
     * @return
     */
    public JSONObject selectBySelective(String record){
        JSONObject map = new JSONObject();
        try{
            ErpGoodsForm rec = JSON.parseObject(record, ErpGoodsForm.class);
            List<ErpGoodsExt> list = (List<ErpGoodsExt>) mapperExt.selectByPage(rec);
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
     * 根据条件获取商品列表
     *
     * @param record
     * @return
     */
    public JSONObject selectByPage(String record){
        JSONObject map = new JSONObject();
        try{
            ErpGoodsForm form = JSON.parseObject(record,ErpGoodsForm.class);
            List<ErpGoodsExt> list = mapperExt.selectByPage(form);
            int countAll = mapperExt.getCountByPage(form);
            map.put("aaData", list);
            map.put("sEcho",form.getSEcho());
            map.put("iTotalRecords",countAll);
            map.put("iTotalDisplayRecords",countAll);
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
     * 根据条件获取商品数量
     *
     * @param record
     * @return
     */
    public JSONObject getCountBySelective(String record){
        JSONObject map = new JSONObject();
        try{
            ErpGoodsForm form = JSON.parseObject(record,ErpGoodsForm.class);
            int countAll = mapperExt.getCountByPage(form);
            map.put("data", countAll);
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
     * 根据ID获取商品信息
     *
     * @param id
     * @return
     */
    public JSONObject getById(String id){
        JSONObject map = new JSONObject();
        try{
            if (StringUtil.isEmpty(id)) {
                throw new ExceptionErrorParam("缺少参数SKU");
            }
            ErpGoodsExt data = mapperExt.selectById(id);
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
     * 根据ID更新商品信息
     *
     * @param record
     */
    public JSONObject updateById(String record){
        JSONObject map = new JSONObject();
        try{
            ErpGoodsForm form = JSON.parseObject(record,ErpGoodsForm.class);
            if (StringUtil.isEmpty(form.getSku())) {
                throw new ExceptionErrorParam("缺少参数SKU");
            }
            mapper.updateByPrimaryKey(form);
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
     * 新建商品信息
     *
     * @param record
     */
    public JSONObject insert(String record){
        JSONObject map = new JSONObject();
        try{
            ErpGoodsForm form = JSON.parseObject(record,ErpGoodsForm.class);
            if (StringUtil.isEmpty(form.getSku())) {
                throw new ExceptionErrorParam("缺少参数SKU");
            }
            mapper.insertSelective(form);
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
     * 根据ID物理删除商品信息
     *
     * @param id
     */
    public JSONObject deleteByPrimaryKey(String id){
        JSONObject map = new JSONObject();
        try{
            if (StringUtil.isEmpty(id)) {
                throw new ExceptionErrorParam("缺少参数SKU");
            }
            mapper.deleteByPrimaryKey(id);
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
     * 物理删除所有商品信息
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
     * 删除原有数据,批量导入商品信息
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
            List<ErpGoods> ll = JSON.parseArray(list, ErpGoods.class);
            for (ErpGoods record : ll) {
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
            List<ErpGoodsExt> ll = JSON.parseArray(list, ErpGoodsExt.class);
            for (ErpGoodsExt record : ll) {
                String style = record.getStyle();
                if(style.equals("10")) {
                    //新增
                    mapper.insertSelective(record);
                } else if (style.equals("20")) {
                    //修改
                    mapperExt.updateNoTime(record);
                } else if (style.equals("30")) {
                    //删除
                    mapper.deleteByPrimaryKey(record.getSku());
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
