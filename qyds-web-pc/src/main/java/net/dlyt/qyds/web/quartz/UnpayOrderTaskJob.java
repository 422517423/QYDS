package net.dlyt.qyds.web.quartz;

import net.dlyt.qyds.web.context.SpringApplicationContextHolder;
import net.dlyt.qyds.web.service.OrdMasterService;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class UnpayOrderTaskJob extends QuartzJobBean{
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UnpayOrderTaskJob.class);

	@Resource
	private OrdMasterService ordMasterService;

	//业务逻辑处理 清理30分钟未付款订单
	private void cancelOrder(String orderCode){
		ordMasterService.cancancelOrderQuartz(orderCode);
	}

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {

		JobDataMap paramMap = context.getJobDetail().getJobDataMap();
		String orderCode = paramMap.getString("orderCode");
		ordMasterService = (OrdMasterService)SpringApplicationContextHolder.getBean("ordMasterService");

		try {
			cancelOrder(orderCode);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}



}
