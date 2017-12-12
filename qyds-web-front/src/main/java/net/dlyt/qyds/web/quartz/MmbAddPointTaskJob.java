package net.dlyt.qyds.web.quartz;

import net.dlyt.qyds.web.context.SpringApplicationContextHolder;
import net.dlyt.qyds.web.service.MmbPointRecordService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class MmbAddPointTaskJob extends QuartzJobBean {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MmbAddPointTaskJob.class);

    @Autowired
    private MmbPointRecordService mmbPointRecordService;

    //业务逻辑处理 累计用户可用积分
    private void addMmbMasterPoint() {

        mmbPointRecordService.addPoint();
    }

    @Override
    protected void executeInternal(JobExecutionContext context)
            throws JobExecutionException {

        if (!TaskJobConstants.RUNJOB_SWITCH_ALL || !TaskJobConstants.RUNJOB_SWITCH_MMB_ADD_POINT) return;
        mmbPointRecordService = (MmbPointRecordService) SpringApplicationContextHolder.getBean("mmbPointRecordService");

        try {
            addMmbMasterPoint();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
