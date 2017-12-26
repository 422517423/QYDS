package net.dlyt.qyds.web.quartz;

import net.dlyt.qyds.web.context.SpringApplicationContextHolder;
import net.dlyt.qyds.web.service.ErpSendService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class ErpSendTaskJob extends QuartzJobBean{
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ErpSendTaskJob.class);

	@Autowired
	private ErpSendService service;

	//业务逻辑处理 清理36个月之前的过期积分
	private void erpSend(){
		//发送未成功的商品分类
		if (TaskJobConstants.RUNJOB_SWITCH_ERP_SEND_GOODS)service.sendFailGoodsType();
		//发送未成功的会员信息
		if (TaskJobConstants.RUNJOB_SWITCH_ERP_SEND_MEMBER)service.sendFailMember();
		//发送未成功的会员积分记录
		if (TaskJobConstants.RUNJOB_SWITCH_ERP_SEND_POINT)service.sendFailPointRecord();
		//发送未成功的新订单
		if (TaskJobConstants.RUNJOB_SWITCH_ERP_SEND_ORDER)service.sendFailOrder();
		//发送未成功的退货订单
		if (TaskJobConstants.RUNJOB_SWITCH_ERP_SEND_RETURN)service.sendFailReturnOrder();
		//发送未成功的优惠券(包括绑定SKU)
		if (TaskJobConstants.RUNJOB_SWITCH_ERP_SEND_COUPON)service.sendFailCoupon();
		//发送未成功的优惠券绑定SKU(和到上一个接口了)
//		if (TaskJobConstants.RUNJOB_SWITCH_ERP_SEND_COUPON_SKU)service.sendCouponSku();
		//发送未成功的会员优惠券
		if (TaskJobConstants.RUNJOB_SWITCH_ERP_SEND_COUPON_MEMBER)service.sendFailCouponMember();
		//发送未成功的已使用优惠券
		if (TaskJobConstants.RUNJOB_SWITCH_ERP_SEND_COUPON_USED)service.sendFailCouponUsed();
		//发送未成功的调货
		if (TaskJobConstants.RUNJOB_SWITCH_ERP_SEND_COUPON_USED)service.sendFailBankUpdate();
		//发送未成功的禁用会员
		if (TaskJobConstants.RUNJOB_SWITCH_ERP_SEND_MEMBER_USED)service.sendFailMemberUsed();
		// TODO: 2017/12/25 发送物流信息
		if (TaskJobConstants.RUNJOB_SWITCH_ERP_SEND_EXPRESS)service.sendFailExpress();
	}

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {

		if (!TaskJobConstants.RUNJOB_SWITCH_ALL || !TaskJobConstants.RUNJOB_SWITCH_ERP_SEND) return;
		service = (ErpSendService) SpringApplicationContextHolder.getBean("erpSendService");

		try {
			logger.debug("ErpSendTaskJob定时任务开始");
			erpSend();
			logger.debug("ErpSendTaskJob定时任务结束");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
