package net.dlyt.qyds.web.controller.task_job;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.config.FileServerConfig;
import net.dlyt.qyds.web.service.*;
import net.dlyt.qyds.web.service.common.Constants;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;

/**
 * Created by zlh on 2016/11/7.
 */
@Controller
@RequestMapping("/task_job")
public class TaskJobController {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(TaskJobController.class);

    @Autowired
    private CouponMemberService couponMemberService;

    @Autowired
    private ErpSendService erpSendService;

    @Autowired
    private MmbPointRecordService mmbPointRecordService;

    @Autowired
    private MmbLevelManagerService mmbLevelManagerService;

    @Autowired
    private OrdMasterService ordMasterService;

    @Autowired
    private FileServerConfig fileServerConfig;

    @Autowired
    private MmbMasterService mmbMasterService;

    private static int[] resizeArray = {120, 480, 800, 1200};

    @RequestMapping("distributeBirthdayCoupon")
    @ResponseBody
    public JSONObject distributeBirthdayCoupon() {
        return couponMemberService.distributeBirthdayCoupon();
    }

    @RequestMapping("addMmbMasterPoint")
    @ResponseBody
    public JSONObject addMmbMasterPoint() {
        return mmbPointRecordService.addPoint();
    }

    @RequestMapping("clearSurplusPoint")
    @ResponseBody
    public JSONObject clearSurplusPoint() {
        return mmbPointRecordService.clearSurplusPoint();
    }

    @RequestMapping("downGrade")
    @ResponseBody
    public JSONObject downGrade() {
        return mmbLevelManagerService.relegation();
    }

    @RequestMapping("cancelOrder")
    @ResponseBody
    public JSONObject cancelOrder(String orderId) {
        ordMasterService.cancancelOrderQuartz(orderId);

        JSONObject json = new JSONObject();
        json.put("resultCode", Constants.NORMAL);
        return json;
    }

    //TODO
    @RequestMapping("scaleResize")
    @ResponseBody
    public JSONObject scaleResize(String shortUrl) {

        String srcImg = null;
        String destImg = null;

        String basePath = fileServerConfig.getStoragePath();

        JSONObject json = new JSONObject();
        try {
            for (int w : resizeArray) {
                srcImg = basePath.concat("/orignal");
                srcImg = srcImg.concat(shortUrl);
                destImg = basePath.concat("/thumb" + w);
                destImg = destImg.concat(shortUrl);
                mkdirs(destImg);
                File file = new File(destImg);
                if(file.exists()){
                    continue;
                }

                logger.debug("src=" + srcImg);
                scaleResize(srcImg, destImg, w, w, true);
                logger.debug("dest=" + destImg);
                json.put("resultCode", Constants.NORMAL);
            }
        }
        catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("message", e.getMessage());
        }
        return json;

    }


    /**
     * 等比缩放图片（如果width为空，则按height缩放; 如果height为空，则按width缩放）
     *
     * @param srcImg 源图片路径
     * @param destImg 目标图片路径
     * @param width 缩放后的宽度
     * @param height 缩放后的高度
     * @param isResize 是否以缩放方式，而非缩略图方式
     * @throws Exception
     */
    private void scaleResize(String srcImg, String destImg,
                            Integer width, Integer height, boolean isResize) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImg);
        if(isResize){
            op.resize(width, height);
        }else {
            op.sample(width, height);
        }
        op.addImage(destImg);
        ConvertCmd cmd = new ConvertCmd(true);
        cmd.run(op);
    }


    private boolean mkdirs(String path) {
        File file = new File(path);
        if (file.exists()) {
            return true;
        }
        return file.getParentFile().mkdirs();
    }

    @RequestMapping("erpSend")
    @ResponseBody
    public JSONObject erpSend() {
        //发送未成功的商品分类
        erpSendService.sendFailGoodsType();
        //发送未成功的会员信息
        erpSendService.sendFailMember();
        //发送未成功的会员积分记录
        erpSendService.sendFailPointRecord();
        //发送未成功的新订单
        erpSendService.sendFailOrder();
        //发送未成功的退货订单
        erpSendService.sendFailReturnOrder();
        //发送未成功的优惠券(包括绑定SKU)
        erpSendService.sendFailCoupon();
        //发送未成功的会员优惠券
        erpSendService.sendFailCouponMember();
        //发送未成功的已使用优惠券
        erpSendService.sendFailCouponUsed();

        JSONObject json = new JSONObject();
        json.put("resultCode", Constants.NORMAL);
        return json;
    }

    @RequestMapping("sendFailGoodsType")
    @ResponseBody
    public JSONObject sendFailGoodsType() {
        //发送未成功的商品分类
        return erpSendService.sendFailGoodsType();
    }

    @RequestMapping("sendFailGoodsTypeById")
    @ResponseBody
    public JSONObject sendFailGoodsTypeById(@RequestParam(required = false)String id) {
        //发送未成功的商品分类
        return erpSendService.sendGoodsTypeById(id);
    }

    @RequestMapping("sendFailMember")
    @ResponseBody
    public JSONObject sendFailMember() {
        //发送未成功的会员信息
        return erpSendService.sendFailMember();
    }

    @RequestMapping("sendFailPointRecord")
    @ResponseBody
    public JSONObject sendFailPointRecord() {
        //发送未成功的会员积分记录
        return erpSendService.sendFailPointRecord();
    }

    @RequestMapping("sendFailOrder")
    @ResponseBody
    public JSONObject sendFailOrder() {
        //发送未成功的新订单
        erpSendService.sendFailOrder();
        JSONObject json = new JSONObject();
        json.put("resultCode", Constants.NORMAL);
        return json;
    }

    @RequestMapping("sendFailReturnOrder")
    @ResponseBody
    public JSONObject sendFailReturnOrder() {
        //发送未成功的退货订单
        return erpSendService.sendFailReturnOrder();
    }

    @RequestMapping("sendFailCoupon")
    @ResponseBody
    public JSONObject sendFailCoupon() {
        //发送未成功的优惠券(包括绑定SKU)
        return erpSendService.sendFailCoupon();
    }

    @RequestMapping("sendFailCouponMember")
    @ResponseBody
    public JSONObject sendFailCouponMember() {
        //发送未成功的会员优惠券
        return erpSendService.sendFailCouponMember();
    }

    @RequestMapping("sendFailCouponUsed")
    @ResponseBody
    public JSONObject sendFailCouponUsed() {
        //发送未成功的已使用优惠券
        return erpSendService.sendFailCouponUsed();
    }

    @RequestMapping("getFailOrder")
    public
    @ResponseBody
    JSONObject getFailOrder() {
        return erpSendService.selectSendFailOrder();
    }

    @RequestMapping("sendOrderById")
    public
    @ResponseBody
    JSONObject sendOrderById(@RequestParam(required = false)String orderId) {
        return erpSendService.sendOrderById(orderId);
    }

    @RequestMapping("receiveOrder15DaysAgo")
    @ResponseBody
    public JSONObject receiveOrder15DaysAgo() {
        //发送未成功的已使用优惠券
        return ordMasterService.receiveOrder15DaysAgo();
    }

    @RequestMapping("finishOrder7DaysAgo")
    @ResponseBody
    public JSONObject finishOrder7DaysAgo() {
        //发送未成功的已使用优惠券
        return ordMasterService.finishOrder7DaysAgo();
    }

    @RequestMapping("closeOrder30DaysAgo")
    @ResponseBody
    public JSONObject closeOrder30DaysAgo() {
        //发送未成功的已使用优惠券
        return ordMasterService.closeOrder30DaysAgo();
    }
}
