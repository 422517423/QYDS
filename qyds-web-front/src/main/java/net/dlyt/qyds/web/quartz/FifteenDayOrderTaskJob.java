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
public class FifteenDayOrderTaskJob extends QuartzJobBean{
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(FifteenDayOrderTaskJob.class);

	@Resource
	private OrdMasterService ordMasterService;

	//业务逻辑处理 将15天前状态为待收货的订单,状态变为已收货
	private void receiveOrder15DaysAgo(){
		ordMasterService.receiveOrder15DaysAgo();
	}

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {

		JobDataMap paramMap = context.getJobDetail().getJobDataMap();
		ordMasterService = (OrdMasterService)SpringApplicationContextHolder.getBean("ordMasterService");

		try {
			receiveOrder15DaysAgo();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}



}
