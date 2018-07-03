package net.dlyt.qyds.web.quartz;

import net.dlyt.qyds.web.service.OrdMasterService;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Deprecated
@Component
public class UnpayOrderTaskJob extends QuartzJobBean{
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UnpayOrderTaskJob.class);

	@Autowired
	private OrdMasterService ordMasterService;

	//业务逻辑处理 清理30分钟未付款订单
	private void cancelOrder(String orderId){
		ordMasterService.cancancelOrderQuartz(orderId);
	}

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {

		if (!TaskJobConstants.RUNJOB_SWITCH_ALL || !TaskJobConstants.RUNJOB_SWITCH_UNPAY_ORDER) return;
		JobDataMap paramMap = context.getJobDetail().getJobDataMap();
		String orderId = paramMap.getString("orderId");
		try {
			cancelOrder(orderId);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}



}
