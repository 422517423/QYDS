package net.dlyt.qyds.web.controller.prize_draw;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.PrizeDrawOppo;
import net.dlyt.qyds.common.dto.ext.PrizeDrawExt;
import net.dlyt.qyds.common.dto.ext.PrizeDrawOppoExt;
import net.dlyt.qyds.common.dto.ext.PrizeGoodsExt;
import net.dlyt.qyds.web.service.PrizeDrawService;
import net.dlyt.qyds.web.service.common.ComCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by cjk on 2016/12/17.
 */
@Controller
@RequestMapping("/prize_draw")
public class PrizeDrawController {
    @Autowired
    private PrizeDrawService prizeDrawService;

    @RequestMapping("prizeDraw")
    @ResponseBody
    public JSONObject prizeDraw(String data) {
        PrizeDrawExt form = (PrizeDrawExt) JSON.parseObject(data, PrizeDrawExt.class);
        return prizeDrawService.prizeDraw(form);
    }

    @RequestMapping("getDetail")
    @ResponseBody
    public JSONObject getDetail(String data) {
        PrizeDrawExt form = (PrizeDrawExt) JSON.parseObject(data, PrizeDrawExt.class);
        return prizeDrawService.getDetail(form);
    }

    @RequestMapping("getPrizeGoodsList")
    @ResponseBody
    public JSONObject getPrizeGoodsList(String data) {
        PrizeGoodsExt form = (PrizeGoodsExt) JSON.parseObject(data, PrizeGoodsExt.class);
        return prizeDrawService.getPrizeGoodsList(form);
    }

//    @RequestMapping("deletePrizeGoods")
//    @ResponseBody
//    public JSONObject deletePrizeGoods(String data) {
//        PrizeGoodsExt form = (PrizeGoodsExt) JSON.parseObject(data, PrizeGoodsExt.class);
//        return prizeDrawService.deletePrizeGoods(form);
//    }
//
//    @RequestMapping("addPrizeGoods")
//    @ResponseBody
//    public JSONObject addPrizeGoodsList(String data) {
//        PrizeGoodsExt form = (PrizeGoodsExt) JSON.parseObject(data, PrizeGoodsExt.class);
//        return prizeDrawService.addPrizeGoods(form);
//    }
//
//    @RequestMapping("editPrizeGoods")
//    @ResponseBody
//    public JSONObject editPrizeGoodsList(String data) {
//        PrizeGoodsExt form = (PrizeGoodsExt) JSON.parseObject(data, PrizeGoodsExt.class);
//        return prizeDrawService.editPrizeGoods(form);
//    }

    /*****************************************消费端接口********************************************/

    /**
     * 兑换抽奖机会
     *
     * @param data memberId-用户ID prizeDrawId-活动ID
     * @return
     */
    @RequestMapping("exchangePrizeWithPoint")
    @ResponseBody
    public JSONObject exchangePrizeWithPoint(String data) {
        PrizeDrawOppo form = (PrizeDrawOppo) JSON.parseObject(data, PrizeDrawOppo.class);
        return prizeDrawService.addPrizeDrawOppo(form.getMemberId(), form.getPrizeDrawId(), ComCode.PrizeDrawOppoType.EXCHANGE, null);
    }

    /**
     * 获取某一抽奖活动的中奖名单
     *
     * @param data prizeDrawId-活动ID
     * @return
     */
    @RequestMapping("getWinningList")
    @ResponseBody
    public JSONObject getWinningList(String data) {
        PrizeDrawOppoExt form = (PrizeDrawOppoExt) JSON.parseObject(data, PrizeDrawOppoExt.class);
        return prizeDrawService.getWinningList(form);
    }


    /**
     * 获取某一抽奖活动信息
     *
     * 1、活动开始结束时间
     * 2、会员已抽奖情况
     * 3、会员剩余抽奖次数
     * 4、活动配置情况
     *
     * @param data memberId-用户ID prizeDrawId-活动ID
     * @return
     */
    @RequestMapping("getPrizeDrawInfo")
    @ResponseBody
    public JSONObject getPrizeDrawInfo(String data) {
        PrizeDrawOppo form = (PrizeDrawOppo) JSON.parseObject(data, PrizeDrawOppo.class);
        return prizeDrawService.getPrizeDrawInfo(form.getMemberId(), form.getPrizeDrawId());
    }

}
