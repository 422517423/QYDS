package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.GdsColoreimage;
import net.dlyt.qyds.common.dto.GdsMaster;
import net.dlyt.qyds.dao.GdsColoreimageMapper;
import net.dlyt.qyds.dao.GdsMasterMapper;
import net.dlyt.qyds.web.service.CatcheRemoveService;
import net.dlyt.qyds.web.service.GdsColorService;
import net.dlyt.qyds.web.service.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by congkeyan on 2016/7/19.
 */
@Service("gdsColorService")
@Transactional(readOnly = true)
public class GdsColorServiceImpl implements GdsColorService {

    @Autowired
    private GdsMasterMapper gdsMasterMapper;

    @Autowired
    private GdsColoreimageMapper gdsColorMapper;

    @Autowired
    private CatcheRemoveService catcheRemoveService;

    /**
     * 保存数据到颜色表中
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
            GdsColoreimage gdsColoreimage = (GdsColoreimage) JSON.parseObject(data, GdsColoreimage.class);
            gdsColoreimage.setUpdateTime(new Date());
            gdsColoreimage.setUpdateUserId((String)userMap.get("loginId"));
            gdsColorMapper.updateByPrimaryKeySelective(gdsColoreimage);
            //更新主表的更新时间和人
            GdsMaster record = new GdsMaster();
            record.setGoodsId(gdsColoreimage.getGoodsId());
            record.setUpdateTime(new Date());
            record.setUpdateUserId((String)userMap.get("loginId"));
            gdsMasterMapper.updateByPrimaryKeySelective(record);

            //调用清理缓存 全部
            catcheRemoveService.sendPost(Constants.PC_SOAP_URL+"removeAll.json",null);
            catcheRemoveService.sendPost(Constants.WX_SOAP_URL+"removeAll.json",null);

            map.put("resultCode", Constants.NORMAL);
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
        }
        return map;
    }


    public JSONObject edit(String goodsColoreId) {
        JSONObject json = new JSONObject();
        try{
            if (!StringUtils.isEmpty(goodsColoreId)) {
                GdsColoreimage GdsColoreimage = gdsColorMapper.selectByPrimaryKey(goodsColoreId);
                json.put("data", GdsColoreimage);
            }
            json.put("resultCode", Constants.NORMAL);
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
        }
        return json;
    }


}
