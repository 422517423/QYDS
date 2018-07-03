package net.dlyt.qyds.web.quartz;

import net.dlyt.qyds.web.service.common.StringUtil;
import org.quartz.*;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.UUID;

import static org.quartz.DateBuilder.nextGivenSecondDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by dkzhang on 16/8/3.
 */
public class QydsSchedulerFactory {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(QydsSchedulerFactory.class);

    private static SchedulerFactory schedFact;// = new org.quartz.impl.StdSchedulerFactory();
    private static Scheduler scheduler;

    public static Scheduler getScheduler() throws SchedulerException {
        if(schedFact == null){
            schedFact = new org.quartz.impl.StdSchedulerFactory();
            scheduler = schedFact.getScheduler();
            scheduler.start();
            return scheduler;
        }
        if(scheduler == null){
            scheduler = schedFact.getScheduler();
            scheduler.start();
            return scheduler;
        }
        if(!scheduler.isStarted()){
            scheduler.start();
        }
        return scheduler;
    }

    public static SimpleTrigger getSimpleTrigger(
            String triggerId, String group, int startTimeInSeconds,
            int repeatCount, int repeatIntervalInSeconds) throws SchedulerException {
        SimpleTrigger simpleTrigger = (SimpleTrigger) newTrigger()
                .withIdentity(triggerId, group)
                .startAt(nextGivenSecondDate(null, startTimeInSeconds))
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(repeatIntervalInSeconds)
                        .withRepeatCount(repeatCount)
                )
                .build();
        return simpleTrigger;
    }



//    public static void doThumbnail(String shortUrl, String id){
//        try {
//            // schedule it to run!
//            if(StringUtil.isEmpty(id)){
//                id = UUID.randomUUID().toString();
//            }
//            JobDetail job = newJob(ThumbnailTaskJob.class).withIdentity("thumbJob:" + id, "thumb").build();
//            job.getJobDataMap().put("short_image_url", shortUrl);
//            Date ft = getScheduler().scheduleJob(job, getSimpleTrigger("thumbTrigger:" + id, "thumb", 0, 0, 0));
//            logger.info(job.getKey() + " will run at: " + ft + " and repeat 0  times ");
//        }catch (SchedulerException se){
//            se.printStackTrace();
//        }
//    }

    public static SimpleTrigger getSimpleTrigger(
            String triggerId, String group, Date startTime,
            int repeatCount, int repeatIntervalInSeconds) throws SchedulerException {
        SimpleTrigger simpleTrigger = (SimpleTrigger) newTrigger()
                .withIdentity(triggerId, group)
                .startAt(startTime)
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(repeatIntervalInSeconds)
                        .withRepeatCount(repeatCount)
                )
                .build();
        return simpleTrigger;
    }

    public static void doCancelUnnpayOrder(String orderId, String id){
        try {
            // schedule it to run!
            if(StringUtil.isEmpty(id)){
                id = UUID.randomUUID().toString();
            }
            JobDetail job = newJob(UnpayOrderTaskJob.class).withIdentity("unpayJob:" + id, "order").build();
            job.getJobDataMap().put("orderCode", orderId);
            Long nowTime = System.currentTimeMillis();
            nowTime += 30 * 60 * 1000;
            Date startTime = new Date(nowTime);
            Date ft = getScheduler().scheduleJob(job, getSimpleTrigger("unpayTrigger:" + id, "order", startTime, 0, 0));
            logger.info(job.getKey() + " will run at: " + ft + " and repeat 0  times ");
        }catch (SchedulerException se){
            se.printStackTrace();
        }
    }

//    /**
//     * 确认收货7天后不允许退货
//     * @param orderCode
//     * @param id
//     */
//    public static void doCancelReturnOrder(String orderCode, String id){
//        try {
//            // schedule it to run!
//            if(StringUtil.isEmpty(id)){
//                id = UUID.randomUUID().toString();
//            }
//            JobDetail job = newJob(ReceiptOrderTaskJob.class).withIdentity("unReturnJob:" + id, "order").build();
//            job.getJobDataMap().put("orderCode", orderCode);
//            Long nowTime = System.currentTimeMillis();
//            nowTime += 7 * 24 * 60 * 60 * 1000;
//            Date startTime = new Date(nowTime);
//            Date ft = getScheduler().scheduleJob(job, getSimpleTrigger("unReturnJob:" + id, "order", startTime, 0, 0));
//            logger.info(job.getKey() + " will run at: " + ft + " and repeat 0  times ");
//        }catch (SchedulerException se){
//            se.printStackTrace();
//        }
//    }
//
//    /**
//     * 商家发货后,30天后不允许退货
//     * @param orderCode
//     * @param id
//     */
//    public static void do30DayUnReturnOrder(String orderCode, String id){
//        try {
//            // schedule it to run!
//            if(StringUtil.isEmpty(id)){
//                id = UUID.randomUUID().toString();
//            }
//            JobDetail job = newJob(ThirtyDayOrderTaskJob.class).withIdentity("30DayUnReturnJob:" + id, "order").build();
//            job.getJobDataMap().put("orderCode", orderCode);
//            Long nowTime = System.currentTimeMillis();
//            nowTime += 30 * 24 * 60 * 60 * 1000;
//            Date startTime = new Date(nowTime);
//            Date ft = getScheduler().scheduleJob(job, getSimpleTrigger("30DayUnReturnJob:" + id, "order", startTime, 0, 0));
//            logger.info(job.getKey() + " will run at: " + ft + " and repeat 0  times ");
//        }catch (SchedulerException se){
//            se.printStackTrace();
//        }
//    }
}
