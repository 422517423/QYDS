package net.dlyt.qyds.web.controller.points_exchange;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.web.service.PointsExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by congkeyan on 2016/7/27.
 */
@Controller
@RequestMapping("/points_exchange_api")
public class PointsExchangePCApiController {

    @Autowired
    private PointsExchangeService pointsExchangeService;

    /**
     * 获取积分兑换的活动信息
     *
     * @param data
     * 包含两个参数 会员ID
     *
     * @return
     */
    @RequestMapping("activityListService")
    @ResponseBody
    public JSONObject activityListService(String data) {
        return pointsExchangeService.activityTypeService(data);
    }


}