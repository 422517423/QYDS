package net.dlyt.qyds.web.controller.mmb_contact;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.MmbContact;
import net.dlyt.qyds.web.common.Constants;
import net.dlyt.qyds.web.service.MmbContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by congky on 16/8/2.
 */
@Controller
@RequestMapping("/mmb_contact")
public class MmbContactController {


    @Autowired
    private MmbContactService mmbContactService;


    /**
     * 添加送货地址
     * 默认地址标记为0,其他标记为1
     *
     * @param data theme:主题
     *             address:地址
     *             telephone:电话
     *             userName:姓名
     *             districtidStreet:街道
     *             comment:说明
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    public JSONObject add(String data) {

        JSONObject json = new JSONObject();
        //TODO
        //data = "{'theme':'客户服务','comment':'添加您的问题','userName':'张三','telephone':'13478787878','address':'123456@163.com'}";
        try {

            MmbContact form = (MmbContact) JSON.parseObject(data, MmbContact.class);
            json = mmbContactService.add(form);

        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", e.getMessage());
        }
        return json;
    }

}
