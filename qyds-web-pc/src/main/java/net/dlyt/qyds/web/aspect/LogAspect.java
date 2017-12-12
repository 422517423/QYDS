package net.dlyt.qyds.web.aspect;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.SysLogLogon;
import net.dlyt.qyds.common.dto.SysLogOperation;
import net.dlyt.qyds.common.dto.SysUser;
import net.dlyt.qyds.common.dto.ext.MmbMasterExt;
import net.dlyt.qyds.web.context.IPUtils;
import net.dlyt.qyds.web.context.PamsDataContext;
import net.dlyt.qyds.web.service.LogService;
import net.dlyt.qyds.web.service.common.Constants;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Aspect
@Component
public class LogAspect {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(LogAspect.class);
    @Autowired
    private LogService logService;

    @Pointcut("execution(* net.dlyt.qyds.web.controller.*.*.get*(..)) || " +
            "execution(* net.dlyt.qyds.web.controller.*.*.update*(..)) || " +
            "execution(* net.dlyt.qyds.web.controller.*.*.delete*(..)) || " +
            "execution(* net.dlyt.qyds.web.controller.*.*.save*(..)) || " +
            "execution(* net.dlyt.qyds.web.controller.*.*.edit*(..))")
    public void businessPointcut() {
    }

    @Pointcut("execution(* net.dlyt.qyds.web.controller.login.LoginController.login(..))")
    public void loginPointcut() {
    }

    /**
     * 前置通知 用于拦截Controller层记录用户的操作
     */
    @Around("businessPointcut()")
    public Object aroundBusiness(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        long start = System.currentTimeMillis();
        result = point.proceed();
        long end = System.currentTimeMillis();

        Object[] args = point.getArgs();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        MmbMasterExt sysUser = null;

//        Cookie[] cookies = request.getCookies();
//        if(cookies != null && cookies.length > 0) {
//            for(int idx = 0; idx < cookies.length; ++idx) {
//                Cookie ck = cookies[idx];
//                if(ck != null && "the_user_info".equals(ck.getName())) {
//                    sysUser = (SysUser) JSONObject.parseObject((String)ck.getValue(), SysUser.class);
//                    break;
//                }
//            }
//        }

        sysUser = (MmbMasterExt)PamsDataContext.getSysUser();


        if (result instanceof JSONObject) {
            JSONObject resultJson = (JSONObject) result;

            SysLogOperation record = new SysLogOperation();
            record.setUserId(sysUser== null?"":sysUser.getMemberId());
            record.setDescription(request.getSession().getId());
            record.setUserIp(IPUtils.getRemoteIp(request));
            record.setOperate(point.toString());
            record.setUrl(request.getRequestURI());
            record.setStartTime(new Date(start));
            record.setEndTime(new Date(end));
            record.setTimeCost((int)(end - start));
            record.setModule(request.getContextPath());
            JSONObject serviceResult = logService.insertOperationLog(record);


            if (null != serviceResult.get("resultCode") && !Constants.NORMAL.equals(serviceResult.get("resultCode"))) {
                afterThrowing(point, new Exception(null != serviceResult.get("message") ? serviceResult.get("message").toString() : ""));
                return serviceResult;
            }
        }

        return result;
    }

    /**
     * 记录登陆日志
     */
    @Around("loginPointcut()")
    public Object aroundLogin(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        long start = System.currentTimeMillis();
        result = point.proceed();

        Object[] args = point.getArgs();
        SysUser sysUser = null;
        if(args != null && args.length > 0){
            sysUser = (SysUser) JSONObject.parseObject((String)args[0], SysUser.class);
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();


        if (result instanceof JSONObject) {
            JSONObject resultJson = (JSONObject) result;

            SysLogLogon record = new SysLogLogon();
            record.setUserId(sysUser.getLoginId());
            record.setSessionId(request.getSession().getId());
            record.setUserIp(IPUtils.getRemoteIp(request));
            record.setLogonTime(new Date());
            record.setExceptionReason(resultJson.getString("resultCode"));
            JSONObject serviceResult = logService.insertLogonLog(record);

            if (null != serviceResult.get("resultCode") && !Constants.NORMAL.equals(serviceResult.get("resultCode"))) {
                afterThrowing(point, new Exception(null != serviceResult.get("message") ? serviceResult.get("message").toString() : ""));
                return serviceResult;
            }

        }
        return result;
    }

    /**
     * 操作异常记录
     */
    @AfterThrowing(pointcut = "businessPointcut()", throwing = "e")
    public void afterThrowing(JoinPoint point, Throwable e) throws Exception {
        try {
//            ErrLogModel errLogModel = new ErrLogModel();
//            acquireBaseLogInfo(errLogModel);
//            errLogModel.setException(e.toString());
//            errLogModel.setMethodName(point.getTarget().getClass().getName() + "." + point.getSignature().getName());
//            logService.createErrLog(errLogModel);
//
//            if (logger.isDebugEnabled()) {
//                logger.debug("====================日志模型====================");
//                System.out.println(errLogModel);
//                logger.debug("====================日志模型====================");
//            }
//
//            if (logger.isErrorEnabled()) {
//                logger.debug("====================异常信息====================");
//                logger.error("出现异常: ", e);
//                logger.debug("====================异常信息====================");
//            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
