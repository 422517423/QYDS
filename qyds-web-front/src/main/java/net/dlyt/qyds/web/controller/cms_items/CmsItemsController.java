package net.dlyt.qyds.web.controller.cms_items;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.CmsItems;
import net.dlyt.qyds.common.dto.ext.CmsItemsExt;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.service.CmsItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

/**
 * Created by C_Nagai on 2016/7/28.
 */
@Controller
@RequestMapping("/cms_items")
public class CmsItemsController {

    @Autowired
    private CmsItemsService cmsItensService;

    @RequestMapping("checkItemCode")
    public @ResponseBody void checkItemCode(HttpServletResponse response, String itemCode, String itemId){
        try{
            JSONObject json = cmsItensService.selectByItemCode(itemCode);
            if (json != null && Constants.NORMAL.equals(json.getString("resultCode"))) {
                CmsItems cmsItems = (CmsItems)json.get("data");
                if (cmsItems == null) {
                    response.getWriter().print(true);
                } else {
                    if (cmsItems.getItemId() != null && itemId.equals(cmsItems.getItemId())) {
                        response.getWriter().print(true);
                    } else {
                        response.getWriter().print(false);
                    }
                }

            } else {
                response.getWriter().print(true);
            }
        }catch(Exception e){
//            response.getWriter().print(false);
        }
    }

    @RequestMapping("selectAll")
    @ResponseBody
    public JSONObject getMenu(){
        JSONObject json = new JSONObject();

        try{
            List<CmsItemsExt> list = cmsItensService.selectAll();
            json.put("data",list);
            json.put("resultCode", Constants.NORMAL);
        }catch (Exception e){
            json.put("resultCode",Constants.FAIL);
        }

        return json;
    }

    @RequestMapping("edit")
    @ResponseBody
    public JSONObject edit(String data) {
        JSONObject json = new JSONObject();
        try {
            CmsItems form = (CmsItems) JSON.parseObject(data, CmsItems.class);
            CmsItems cmsItem = cmsItensService.selectByPrimaryKey(form.getItemId());
            json.put("data", cmsItem);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    @RequestMapping("save")
    @ResponseBody
    public JSONObject save(String data) {
        JSONObject json = new JSONObject();
        try {
            CmsItems form = (CmsItems) JSON.parseObject(data, CmsItems.class);
            // 新建
            if (StringUtils.isEmpty(form.getItemId())) {
                String randomUUID = UUID.randomUUID().toString();
                form.setItemId(randomUUID);
                form.setShopId(Constants.ORGID);
                cmsItensService.insertSelective(form);

            // 修改
            } else {
                cmsItensService.updateByPrimaryKeySelective(form);

            }

            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    @RequestMapping("resort")
    @ResponseBody
    public JSONObject resort(@RequestParam("data") String data){
        JSONObject map = new JSONObject();

        try{
            List<CmsItems> list = JSONArray.parseArray(data,CmsItems.class);
            cmsItensService.updateSort(list);
            map.put("resultCode",Constants.NORMAL);
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
        }
        return map;
    }

    @RequestMapping("delete")
    @ResponseBody
    public JSONObject delete(@RequestParam("data") String data){
        JSONObject map = new JSONObject();

        try{
            CmsItems cmsItems = JSON.parseObject(data, CmsItems.class);
            cmsItensService.delete(cmsItems);
            map.put("resultCode",Constants.NORMAL);
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
        }
        return map;
    }
}
