package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ErpGoodsType;
import net.dlyt.qyds.common.dto.ext.ErpGoodsTypeExt;
import net.dlyt.qyds.dao.ErpGoodsTypeMapper;
import net.dlyt.qyds.dao.ext.ErpGoodsTypeMapperExt;
import net.dlyt.qyds.web.service.ErpGoodsTypeService;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.common.StringUtil;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import net.dlyt.qyds.web.service.exception.ExceptionErrorParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zlh on 2016/7/29.
 */
@Service("erpGoodsTypeService")
@Transactional(readOnly = true)
public class ErpGoodsTypeServiceImpl implements ErpGoodsTypeService {

    @Autowired
    private ErpGoodsTypeMapper mapper;

    @Autowired
    private ErpGoodsTypeMapperExt mapperExt;

    /**
     * 根据Id获取商品种类列表
     *
     * @return
     */
    public JSONObject selectAll(){
        JSONObject map = new JSONObject();
        try{
            List<ErpGoodsType> list = (List<ErpGoodsType>) mapperExt.selectAll();
            map.put("data", list);
            map.put("resultCode", Constants.NORMAL);
        }catch(ExceptionBusiness e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 根据ID获取商品种类信息
     *
     * @param id
     * @return
     */
    public JSONObject getById(String id){
        JSONObject map = new JSONObject();
        try{
            if (StringUtil.isEmpty(id)) {
                throw new ExceptionErrorParam("缺少参数种类ID");
            }
            ErpGoodsType data = mapper.selectByPrimaryKey(id);
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
     * 根据ID更新商品种类信息
     *
     * @param record
     */
    public JSONObject updateById(String record){
        JSONObject map = new JSONObject();
        try{
            ErpGoodsType rec = JSON.parseObject(record,ErpGoodsType.class);
            if (StringUtil.isEmpty(rec.getTypeCode())) {
                throw new ExceptionErrorParam("缺少参数种类ID");
            }
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
     * 新建商品种类信息
     *
     * @param record
     */
    public JSONObject insert(String record){
        JSONObject map = new JSONObject();
        try{
            ErpGoodsType rec = JSON.parseObject(record,ErpGoodsType.class);
            if (StringUtil.isEmpty(rec.getTypeCode())) {
                throw new ExceptionErrorParam("缺少参数种类ID");
            }
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
     * 根据ID物理删除商品种类信息
     *
     * @param id
     */
    public JSONObject deleteByPrimaryKey(String id){
        JSONObject map = new JSONObject();
        try{
            if (StringUtil.isEmpty(id)) {
                throw new ExceptionErrorParam("缺少参数种类ID");
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
     * 物理删除所有商品种类信息
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
     * 删除原有数据,批量导入商品种类信息
     *
     * @param list
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject inputAll(String list){
        JSONObject map = new JSONObject();
        try{
            //全部删除
            mapperExt.deleteAll();
            List<ErpGoodsType> ll = JSON.parseArray(list,ErpGoodsType.class);
            for (ErpGoodsType record : ll) {
                //插入
                mapper.insertSelective(record);
            }
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
     * 批量更新商品种类信息
     *
     * @param list
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject updateByList(String list){
        JSONObject map = new JSONObject();
        try{
            List<ErpGoodsTypeExt> ll = JSON.parseArray(list,ErpGoodsTypeExt.class);
            for (ErpGoodsTypeExt record : ll) {
                String style = record.getStyle();
                if(style.equals("10")) {
                    //新增
                    mapper.insertSelective(record);
                } else if (style.equals("20")) {
                    //修改
                    mapperExt.updateNoTime(record);
                } else if (style.equals("30")) {
                    //删除
                    mapper.deleteByPrimaryKey(record.getTypeCode());
                } else {
                }
            }
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
