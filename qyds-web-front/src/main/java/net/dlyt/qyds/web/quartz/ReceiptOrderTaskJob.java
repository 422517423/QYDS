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
public class ReceiptOrderTaskJob extends QuartzJobBean{
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ReceiptOrderTaskJob.class);

	@Resource
	private OrdMasterService ordMasterService;

	//业务逻辑处理 确认收货7天后不允许再次申请退货
	private void finishOrder7DaysAgo(){
		// 将7天以前的订单状态为已收货的订单,变为订单完成
		ordMasterService.finishOrder7DaysAgo();
	}

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {

		JobDataMap paramMap = context.getJobDetail().getJobDataMap();
		ordMasterService = (OrdMasterService)SpringApplicationContextHolder.getBean("ordMasterService");

		try {
			finishOrder7DaysAgo();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}



}
