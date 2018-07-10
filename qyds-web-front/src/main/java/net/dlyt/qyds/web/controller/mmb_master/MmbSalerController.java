package net.dlyt.qyds.web.controller.mmb_master;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.MmbSaler;
import net.dlyt.qyds.common.dto.SysUser;
import net.dlyt.qyds.common.dto.SysUserExt;
import net.dlyt.qyds.common.dto.ext.MmbSalerExt;
import net.dlyt.qyds.common.form.MmbSalerForm;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.context.PamsDataContext;
import net.dlyt.qyds.web.service.MmbSalerService;
import net.dlyt.qyds.web.service.exception.ExceptionErrorParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by ZLH on 2016/12/16.
 */
@Controller
@RequestMapping("/mmb_saler")
public class MmbSalerController {

    @Autowired
    private MmbSalerService mmbSalerService;

    @RequestMapping("getList")
    public
    @ResponseBody
    JSONObject getList(MmbSalerForm form) {
        JSONObject json = new JSONObject();
        try {
            //取出登录者的id,是sys_user的loginId
            //userId是六位员工编码，对应员工表的六位
            String userId = (String) PamsDataContext.get("loginId");
            //如果这个人是店员，根据角色判断
            SysUserExt salerByLoginId = mmbSalerService.getSalerByLoginId(userId);
            if (salerByLoginId!=null&&salerByLoginId.getRoleId()==10066){
                form.setTelephone(userId);
                return mmbSalerService.getListByPhone(form);
            }
            MmbSalerExt ext = new MmbSalerExt();
            ext.setNeedColumns(Integer.parseInt(form.getiDisplayLength()));
            ext.setStartPoint(Integer.parseInt(form.getiDisplayStart()));
            ext.setMemberName(form.getMemberName());
            ext.setTelephone(form.getTelephone());
            ext.setOldphone(form.getOldphone());
            ext.setDeleted(form.getDeleted());

            List<MmbSalerExt> list = mmbSalerService.selectAll(ext);
            int allCount = mmbSalerService.getAllDataCount(ext);

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

            MmbSaler mmbSaler = new MmbSaler();
            mmbSaler.setMemberId(param.getString("memberId"));
            json = mmbSalerService.selectBySelective(mmbSaler);
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
            MmbSaler mmbSaler = (MmbSaler) JSON.parseObject(data, MmbSaler.class);

            if (mmbSaler == null) {
                throw new ExceptionErrorParam("参数为空");
            }

            if (StringUtils.isEmpty(mmbSaler.getMemberId())) {
                throw new ExceptionErrorParam("缺少主键信息");
            } else {
                json = mmbSalerService.updateByPrimaryKeySelective(mmbSaler);
            }
        } catch (Exception e) {

            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;

    }

    @RequestMapping("getQRcode")
    public
    @ResponseBody
    JSONObject getQRcode() {
        JSONObject json = new JSONObject();
        try {
            mmbSalerService.makeQrCodeForAllSaler();
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }

        return json;

    }
}