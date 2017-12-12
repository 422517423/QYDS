package net.dlyt.qyds.web.quartz;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.MmbMaster;
import net.dlyt.qyds.common.dto.ext.MmbPointRecordExt;
import net.dlyt.qyds.config.SmsCaptchaConfig;
import net.dlyt.qyds.web.common.SmsSendUtil;
import net.dlyt.qyds.web.context.SpringApplicationContextHolder;
import net.dlyt.qyds.web.service.MmbPointRecordService;
import net.dlyt.qyds.web.service.SysSmsCaptchaService;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.common.StringUtil;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MmbBonusPointCleanTaskJob extends QuartzJobBean{
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MmbBonusPointCleanTaskJob.class);

	@Autowired
	private MmbPointRecordService mmbPointRecordService;

	@Autowired
	private static SmsCaptchaConfig smsCaptchaConfig;

	//业务逻辑处理 清理36个月之前的过期积分
	private void bonusPointClean(){

		mmbPointRecordService.clearSurplusPoint();
	}

	//业务逻辑处理 清理36个月之前的过期积分
	private void bonusPointNotify(){
		JSONObject json = mmbPointRecordService.notifyClearPoint();
		if (json==null) return;
		if (json.getString("resultCode").equals(Constants.FAIL)) return;
		List<MmbPointRecordExt> list = (List<MmbPointRecordExt>)json.get("data");
		for (MmbPointRecordExt item : list) {
			json = SmsSendUtil.sendNotifyPoint(item,smsCaptchaConfig);
			logger.debug(json.getString("resultMessage"));
		}
	}

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {

		if (!TaskJobConstants.RUNJOB_SWITCH_ALL || !TaskJobConstants.RUNJOB_SWITCH_MMB_BONUS_POINT_CLEAN) return;
		mmbPointRecordService = (MmbPointRecordService) SpringApplicationContextHolder.getBean("mmbPointRecordService");

		try {
			bonusPointClean();
			//提前一个月通知
			bonusPointNotify();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}



}
