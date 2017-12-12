package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ErpProduceLine;
import net.dlyt.qyds.common.dto.ext.ErpProduceLineExt;
import net.dlyt.qyds.dao.ErpProduceLineMapper;
import net.dlyt.qyds.dao.ext.ErpProduceLineMapperExt;
import net.dlyt.qyds.web.service.ErpProduceLineService;
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
@Service("erpProduceLineService")
@Transactional(readOnly = true)
public class ErpProduceLineServiceImpl implements ErpProduceLineService {

    @Autowired
    private ErpProduceLineMapper mapper;

    @Autowired
    private ErpProduceLineMapperExt mapperExt;

    /**
     * 根据Id获取产品线列表
     *
     * @return
     */
    public JSONObject selectAll(){
        JSONObject map = new JSONObject();
        try{
            List<ErpProduceLine> list = (List<ErpProduceLine>) mapperExt.selectAll();
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
     * 根据ID获取产品线信息
     *
     * @param id
     * @return
     */
    public JSONObject getById(String id){
        JSONObject map = new JSONObject();
        try{
            if (StringUtil.isEmpty(id)) {
                throw new ExceptionErrorParam("缺少参数产品线ID");
            }
            ErpProduceLine data = mapper.selectByPrimaryKey(id);
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
     * 根据ID更新产品线信息
     *
     * @param record
     */
    public JSONObject updateById(String record){
        JSONObject map = new JSONObject();
        try{
            ErpProduceLine rec = JSON.parseObject(record,ErpProduceLine.class);
            if (StringUtil.isEmpty(rec.getLineCode())) {
                throw new ExceptionErrorParam("缺少参数产品线ID");
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
     * 新建产品线信息
     *
     * @param record
     */
    public JSONObject insert(String record){
        JSONObject map = new JSONObject();
        try{
            ErpProduceLine rec = JSON.parseObject(record,ErpProduceLine.class);
            if (StringUtil.isEmpty(rec.getLineCode())) {
                throw new ExceptionErrorParam("缺少参数产品线ID");
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
     * 根据ID物理删除产品线信息
     *
     * @param id
     */
    public JSONObject deleteByPrimaryKey(String id){
        mapper.deleteByPrimaryKey(id);
        JSONObject map = new JSONObject();
        try{
            if (StringUtil.isEmpty(id)) {
                throw new ExceptionErrorParam("缺少参数产品线ID");
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
     * 物理删除所有产品线信息
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
     * 删除原有数据,批量导入产品线信息
     *
     * @param list
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject inputAll(String list){
        JSONObject map = new JSONObject();
        try{
            //全部删除
            mapperExt.deleteAll();
            List<ErpProduceLine> ll = JSON.parseArray(list,ErpProduceLine.class);
            for (ErpProduceLine record : ll) {
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
     * 批量更新产品线信息
     *
     * @param list
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject updateByList(String list){
        JSONObject map = new JSONObject();
        try{
            List<ErpProduceLineExt> ll = JSON.parseArray(list,ErpProduceLineExt.class);
            for (ErpProduceLineExt record : ll) {
                String style = record.getStyle();
                if(style.equals("10")) {
                    //新增
                    mapper.insertSelective(record);
                } else if (style.equals("20")) {
                    //修改
                    mapperExt.updateNoTime(record);
                } else if (style.equals("30")) {
                    //删除
                    mapper.deleteByPrimaryKey(record.getLineCode());
                } else {
                }
            }
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
}
