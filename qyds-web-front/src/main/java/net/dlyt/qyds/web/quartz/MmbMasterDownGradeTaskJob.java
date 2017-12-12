package net.dlyt.qyds.web.quartz;

import net.dlyt.qyds.web.context.SpringApplicationContextHolder;
import net.dlyt.qyds.web.service.MmbLevelManagerService;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class MmbMasterDownGradeTaskJob extends QuartzJobBean{
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MmbMasterDownGradeTaskJob.class);

	@Autowired
	private MmbLevelManagerService mmbLevelManagerService;

	//业务逻辑处理 清理30分钟未付款订单
	private void downGrade(){

		mmbLevelManagerService.relegation();
	}

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {

		if (!TaskJobConstants.RUNJOB_SWITCH_ALL || !TaskJobConstants.RUNJOB_SWITCH_MMB_MASTER_DOWN_GRADE) return;
		mmbLevelManagerService = (MmbLevelManagerService) SpringApplicationContextHolder.getBean("mmbLevelManagerService");

		try {
			downGrade();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}



}
