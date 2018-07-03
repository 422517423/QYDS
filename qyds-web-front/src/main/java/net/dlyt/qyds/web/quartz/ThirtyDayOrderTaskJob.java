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
public class ThirtyDayOrderTaskJob extends QuartzJobBean{
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ThirtyDayOrderTaskJob.class);

	@Resource
	private OrdMasterService ordMasterService;

	//业务逻辑处理
	private void closeOrder30DaysAgo(){
		//将30天前状态为订单完成的订单,状态变为交易完成
		ordMasterService.closeOrder30DaysAgo();
	}

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {

		JobDataMap paramMap = context.getJobDetail().getJobDataMap();
		ordMasterService = (OrdMasterService)SpringApplicationContextHolder.getBean("ordMasterService");

		try {
			closeOrder30DaysAgo();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}



}
