package net.dlyt.qyds.web.quartz;

import net.dlyt.qyds.web.context.SpringApplicationContextHolder;
import net.dlyt.qyds.web.service.CouponMemberService;
import net.dlyt.qyds.web.service.MmbMasterService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class DistributeBirthdayCouponTaskJob extends QuartzJobBean {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DistributeBirthdayCouponTaskJob.class);

    @Autowired
    private CouponMemberService couponMemberService;

    //业务逻辑处理 提前发送生日券
    private void distributeBirthdayCoupon() {

        couponMemberService.distributeBirthdayCoupon();
    }

    @Override
    protected void executeInternal(JobExecutionContext context)
            throws JobExecutionException {
        if (!TaskJobConstants.RUNJOB_SWITCH_ALL || !TaskJobConstants.RUNJOB_SWITCH_DISTRIBUTE_BIRTHDAY_COUPON) return;
        couponMemberService = (CouponMemberService) SpringApplicationContextHolder.getBean("couponMemberService");

        try {
            distributeBirthdayCoupon();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
