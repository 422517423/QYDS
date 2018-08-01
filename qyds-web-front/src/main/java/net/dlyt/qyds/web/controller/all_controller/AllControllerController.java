package net.dlyt.qyds.web.controller.all_controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.AllController;
import net.dlyt.qyds.common.dto.ext.AllControllerExt;
import net.dlyt.qyds.common.form.AllControllerForm;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.context.PamsDataContext;
import net.dlyt.qyds.web.service.AllControllerService;
import net.dlyt.qyds.web.service.exception.ExceptionErrorParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/all_controller")
public class AllControllerController {
    @Autowired
    private AllControllerService allControllerService;

    @RequestMapping("getList")
    public
    @ResponseBody
    JSONObject getList(AllControllerForm form) {
        JSONObject json = new JSONObject();
        try {
            AllControllerExt ext = new AllControllerExt();
            ext.setNeedColumns(Integer.parseInt(form.getiDisplayLength()));
            ext.setStartPoint(Integer.parseInt(form.getiDisplayStart()));
            ext.setName(form.getName());
            ext.setType(form.getType());

            List<AllControllerExt> list = allControllerService.selectAll(ext);
            int allCount = allControllerService.getAllDataCount(ext);

            json.put("sEcho", form.getsEcho());
            json.put("iTotalRecords", allCount);
            json.put("iTotalDisplayRecords", allCount);
            json.put("aaData", list);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

    @RequestMapping("detail")
    public
    @ResponseBody
    JSONObject detail(String data) {

        JSONObject json = new JSONObject();
        try {
            JSONObject param = JSONObject.parseObject(data);

            if (param == null) {
                throw new ExceptionErrorParam("参数为空");
            }

            AllController allController = new AllController();
            allController.setAllControllerId(param.getString("allControllerId"));
            json = allControllerService.selectBySelective(allController);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }

        return json;

    }

    @RequestMapping("edit")
    public
    @ResponseBody
    JSONObject edit(String data) {
        JSONObject json = new JSONObject();
        try {
            AllController allController = (AllController) JSON.parseObject(data, AllController.class);
            if (allController == null) {
                throw new ExceptionErrorParam("参数为空");
            }

            if (StringUtils.isEmpty(allController.getAllControllerId())) {
                throw new ExceptionErrorParam("缺少主键信息");
            } else {
                //取得用户登录信息
                Map<String, Object> userMap = PamsDataContext.get();
                //登录人
                int userIdInt =(Integer) userMap.get("userId");
                String userId = String.valueOf(userIdInt);
                allController.setUpdateTime(new Date());
                allController.setUpdateUserId(userId);
                json = allControllerService.updateByPrimaryKeySelective(allController);
            }
        } catch (Exception e) {

            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;

    }

    @RequestMapping("save")
    public
    @ResponseBody
    JSONObject save(String data) {
        JSONObject json = new JSONObject();
        try {
            AllController allController = (AllController) JSON.parseObject(data, AllController.class);

            if (allController == null) {
                throw new ExceptionErrorParam("参数为空");
            }

            String type = allController.getType();
            String name = allController.getName();
            if (StringUtils.isEmpty(type)
                    || StringUtils.isEmpty(name)) {
                throw new ExceptionErrorParam("提交的信息不足");
            }
            //取得用户登录信息
            Map<String, Object> userMap = PamsDataContext.get();
            //登录人
            int userIdInt =(Integer) userMap.get("userId");
            String userId = String.valueOf(userIdInt);
            //设定用户信息
            allController.setCreateTime(new Date());
            allController.setCreateUserId(userId);
            allController.setAllControllerId(UUID.randomUUID().toString());
            json = allControllerService.insertSelective(allController);

        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;

    }

    @RequestMapping("delete")
    public
    @ResponseBody
    JSONObject delete(String data) {
        JSONObject json = new JSONObject();
        try {
            AllController allController = (AllController) JSON.parseObject(data, AllController.class);
            allController.setStatus(2);
            json = allControllerService.updateByPrimaryKeySelective(allController);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }
}
