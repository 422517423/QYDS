package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.*;
import net.dlyt.qyds.common.dto.PrizeDraw;
import net.dlyt.qyds.common.dto.ext.PrizeDrawExt;
import net.dlyt.qyds.common.dto.ext.PrizeDrawExt;
import net.dlyt.qyds.common.dto.ext.PrizeDrawOppoExt;
import net.dlyt.qyds.common.dto.ext.PrizeGoodsExt;
import net.dlyt.qyds.common.form.MmbPointRecordForm;
import net.dlyt.qyds.common.form.SkuForm;
import net.dlyt.qyds.dao.*;
import net.dlyt.qyds.dao.ext.PrizeDrawMapperExt;
import net.dlyt.qyds.dao.ext.PrizeDrawOppoMapperExt;
import net.dlyt.qyds.dao.ext.PrizeGoodsMapperExt;
import net.dlyt.qyds.web.service.MmbMasterService;
import net.dlyt.qyds.web.service.MmbPointRecordService;
import net.dlyt.qyds.web.service.PrizeDrawService;
import net.dlyt.qyds.web.service.common.ComCode;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.common.StringUtil;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import net.dlyt.qyds.web.service.exception.ExceptionErrorData;
import net.dlyt.qyds.web.service.exception.ExceptionErrorParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by cjk on 2016/12/15.
 */
@Service("prizeDrawService")
public class PrizeDrawServiceImpl implements PrizeDrawService {

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
    @Autowired
    private PrizeDrawMapper prizeDrawMapper;
    @Autowired
    private PrizeDrawMapperExt prizeDrawMapperExt;
    @Autowired
    private PrizeGoodsMapper prizeGoodsMapper;
    @Autowired
    private PrizeGoodsMapperExt prizeGoodsMapperExt;
    @Autowired
    private PrizeDrawOppoMapperExt prizeDrawOppoMapperExt;
    @Autowired
    private PrizeDrawOppoMapper prizeDrawOppoMapper;
    @Autowired
    private PrizeDrawConfigMapper prizeDrawConfigMapper;
    @Autowired
    private PrizeDrawRecordMapper prizeDrawRecordMapper;

    @Autowired
    private MmbMasterMapper mmbMasterMapper;

    @Autowired
    private MmbPointRecordService mmbPointRecordService;

    public static void main(String aaa[]) {
        for (int i = 0; i < 10000; i++) {
            draw();
        }
    }

    //测试抽奖
    private static void draw() {
        String[] bbb = {"0.1", "1", "50", "3"};
        List<String> aa = Arrays.asList(bbb);
        double randomNumber = Math.random() * 100;
        int index = -1;
        for (int i = 0; i < aa.size(); i++) {
            double minNumber = 0;
            for (int j = 0; j < i; j++) {
                minNumber += Double.valueOf(aa.get(j));
            }
            double maxNumber = Double.valueOf(aa.get(i)) + minNumber;
            // 0-1之间的随机数小于等于概率值，说明中奖了
            if (randomNumber >= minNumber && randomNumber < maxNumber) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            System.out.println("抽中" + aa.get(index));
        } else {
            System.out.println("没抽中");
        }
    }

    @Override
    public JSONObject selectPrizeList() {
        JSONObject json = new JSONObject();
        try {
            PrizeDrawExt prizeDrawExt = new PrizeDrawExt();
            List<PrizeDraw> list = prizeDrawMapperExt.selectPrizeList(prizeDrawExt);
            json.put("result", list);
            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", e.resultCd);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }



    @Override
    public JSONObject getList(PrizeDrawExt form) {
        JSONObject json = new JSONObject();
        try {
            //数据库检索 -- 过滤数据
            form.setDeleted(Constants.DELETED_NO);
            List<PrizeDrawExt> list = prizeDrawMapperExt.select(form);

            for (PrizeDrawExt item : list) {
                PrizeDrawConfig config = queryPrizeConfigInfo(item.getPrizeDrawId());

                item.setExchangeFlag(config.getExchangeFlag());
                item.setExchangePoint(config.getExchangePoint());
                item.setIsLogin(config.getIsLogin());
                item.setIsOrder(config.getIsOrder());
                item.setOrderAmount(config.getOrderAmount());
            }

            int allCount = prizeDrawMapperExt.selectCount(form);
            json.put("aaData", list);
            json.put("sEcho", form.getsEcho());
            json.put("iTotalRecords", allCount);
            json.put("iTotalDisplayRecords", allCount);
            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", e.resultCd);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }

    @Override
    public JSONObject getDetail(PrizeDrawExt form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getPrizeDrawId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            PrizeDraw data = prizeDrawMapper.selectByPrimaryKey(form.getPrizeDrawId());
            if (data == null) {
                throw new ExceptionErrorData("抽奖不存在");
            }
            PrizeDrawExt prizeDraw = new PrizeDrawExt();
            prizeDraw.setCurrentTime(new Date());
            BeanUtils.copyProperties(data, prizeDraw);
            json.put("result", prizeDraw);
            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", e.resultCd);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject delete(PrizeDrawExt form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getPrizeDrawId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            PrizeDraw record = new PrizeDraw();
            record.setPrizeDrawId(form.getPrizeDrawId());
            record.setDeleted(Constants.DELETED_YES);
            prizeDrawMapper.updateByPrimaryKeySelective(record);
            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            json.put("resultCode", e.resultCd);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject edit(PrizeDrawExt form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getPrizeDrawId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            if (StringUtil.isEmpty(form.getPrizeDrawName())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            if (StringUtil.isEmpty(form.getStartTimeStr())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            if (StringUtil.isEmpty(form.getEndTimeStr())) {
                throw new ExceptionErrorParam("缺少参数");
            }

            // 检索出原来的数据
            PrizeDraw oldData = prizeDrawMapper.selectByPrimaryKey(form.getPrizeDrawId());
            // 检测是否重名
            if (oldData == null || !oldData.getPrizeDrawName().equals(form.getPrizeDrawName())) {
                // 判断模板名称是否重复
                int count = prizeDrawMapperExt.checkExistByPrizeDrawName(form.getPrizeDrawName());
                if (count > 0) {
                    throw new ExceptionErrorData("已经存在相同名称的抽奖");
                }
            }
            if (oldData == null || Constants.DELETED_YES.equals(oldData.getDeleted())) {
                // 不存在，新建
                addPrizeDraw(form);
            } else {
                editPrizeDraw(form);
            }
            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            json.put("resultCode", e.resultCd);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }

    private void addPrizeDraw(PrizeDrawExt form) throws Exception {
        // 插入模板主表
        Date date = new Date();
        PrizeDraw at = new PrizeDraw();
        at.setPrizeDrawId(form.getPrizeDrawId());
        at.setDeleted(Constants.DELETED_NO);
        at.setPrizeDrawName(form.getPrizeDrawName());
        at.setCanRepeatWin(form.getCanRepeatWin());
        at.setIsValid(form.getIsValid());
        at.setComment(form.getComment());
        at.setInsertUserId(form.getInsertUserId());
        at.setUpdateUserId(form.getInsertUserId());
        at.setInsertTime(date);
        at.setUpdateTime(date);
        if (!StringUtil.isEmpty(form.getStartTimeStr())) {
            form.setStartTime(sdf.parse(form.getStartTimeStr() + " 00:00:00"));
        }
        if (!StringUtil.isEmpty(form.getEndTimeStr())) {
            form.setEndTime(sdf.parse(form.getEndTimeStr() + " 23:59:59"));
        }
        prizeDrawMapper.insertSelective(at);
    }

    private void editPrizeDraw(PrizeDrawExt form) throws Exception {
        // 更新模板主表
        Date date = new Date();
        form.setUpdateUserId(form.getUpdateUserId());
        form.setUpdateTime(date);
        if (!StringUtil.isEmpty(form.getStartTimeStr())) {
            form.setStartTime(sdf.parse(form.getStartTimeStr() + " 00:00:00"));
        }
        if (!StringUtil.isEmpty(form.getEndTimeStr())) {
            form.setEndTime(sdf.parse(form.getEndTimeStr() + " 23:59:59"));
        }
        prizeDrawMapper.updateByPrimaryKeySelective(form);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject prizeDraw(PrizeDrawExt form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getPrizeDrawId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            if (StringUtil.isEmpty(form.getUserId())) {
                throw new ExceptionErrorParam("缺少参数");
            }

            // 查询抽奖活动
            PrizeDraw prizeDraw = prizeDrawMapper.selectByPrimaryKey(form.getPrizeDrawId());
            if (prizeDraw == null) {
                throw new ExceptionErrorData("抽奖活动不存在");
            }
            if ("1".equals(prizeDraw.getDeleted())) {
                throw new ExceptionErrorData("抽奖活动已结束");
            }
            if ("0".equals(prizeDraw.getIsValid())) {
                throw new ExceptionErrorData("抽奖活动已暂停");
            }
            Date now = new Date();
            if (prizeDraw.getStartTime().compareTo(now) > 0) {
                throw new ExceptionErrorData("抽奖活动还没开始");
            }
            if (prizeDraw.getEndTime().compareTo(now) < 0) {
                throw new ExceptionErrorData("抽奖活动已结束");
            }
            // 检索参数
            List<PrizeGoods> prizeGoodsList = prizeGoodsMapperExt.selectByPrizeDrawId(prizeDraw.getPrizeDrawId());
            if (prizeGoodsList == null || prizeGoodsList.size() == 0) {
                // 没有设置奖品，认为没抽中
                json.put("result", "");
            } else {
                // 抽中的奖品的ID
                String prizeGoods = doPrizeDraw(prizeGoodsList);

                String prizeGoodsId = "";
                String prizeGoodsName = "";
                if (!StringUtil.isEmpty(prizeGoods)) {
                    prizeGoodsId = prizeGoods.split(",")[0];
                    prizeGoodsName = prizeGoods.split(",")[1];
                }

                // 消除抽奖机会
                PrizeDrawOppoExt prizeDrawOppoExt = new PrizeDrawOppoExt();
                prizeDrawOppoExt.setMemberId(form.getUserId());
                prizeDrawOppoExt.setPrizeDrawId(form.getPrizeDrawId());
                prizeDrawOppoExt.setIsDrawed("0");
                prizeDrawOppoExt.setDeleted(Constants.DELETED_NO);
                prizeDrawOppoExt.setiDisplayLength(1);
                List<PrizeDrawOppoExt> oppList = prizeDrawOppoMapperExt.select(prizeDrawOppoExt);
                PrizeDrawOppoExt oppItem = oppList.get(0);
                String oppId = oppItem.getPrizeDrawOppoId();
                PrizeDrawOppo prizeDrawOppo = new PrizeDrawOppo();
                prizeDrawOppo.setPrizeDrawOppoId(oppId);
                prizeDrawOppo.setIsDrawed("1");
                if (!StringUtil.isEmpty(prizeGoods)) {
                    prizeDrawOppo.setIsWin("1");
                    prizeDrawOppo.setPrizeName(prizeGoodsName);
                } else {
                    prizeDrawOppo.setIsWin("0");
                    prizeDrawOppo.setPrizeName("未中奖");
                }
                prizeDrawOppo.setUpdateTime(new Date());
                prizeDrawOppo.setUpdateUserId(form.getUserId());
                prizeDrawOppoMapper.updateByPrimaryKeySelective(prizeDrawOppo);

                // 添加抽奖履历
                PrizeDrawRecord record = new PrizeDrawRecord();
                record.setPrizeDrawRecordId(UUID.randomUUID().toString());
                record.setUserId(form.getUserId());
                record.setPrizeDrawId(form.getPrizeDrawId());
                if (!StringUtil.isEmpty(prizeGoods)) {
                    record.setIsWin("1");
                    record.setPrizeGoodsId(prizeGoodsId);
                } else {
                    record.setIsWin("0");
                    record.setPrizeGoodsId("未中奖");
                }
                record.setPrizeDrawOppoId(oppId);
                record.setIsNotify("0");
                record.setInsertUserId(form.getUserId());
                record.setUpdateUserId(form.getUserId());
                prizeDrawRecordMapper.insertSelective(record);

                json.put("result", prizeGoodsId);
            }
            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            json.put("resultCode", e.resultCd);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }

    /**
     * 抽奖主算法 返回抽中的奖品ID 没有抽中则返回空字符串
     *
     * @param prizeGoodsList
     * @return
     */
    private String doPrizeDraw(List<PrizeGoods> prizeGoodsList) {
        double randomNumber = Math.random() * 100;
        int index = -1;
        for (int i = 0; i < prizeGoodsList.size(); i++) {
            double minNumber = 0;
            for (int j = 0; j < i; j++) {
                minNumber += Double.valueOf(prizeGoodsList.get(j).getWinPercent());
            }
            double maxNumber = Double.valueOf(prizeGoodsList.get(i).getWinPercent()) + minNumber;
            // 0-1之间的随机数小于等于概率值，说明中奖了
            if (minNumber <= randomNumber && randomNumber < maxNumber) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            return prizeGoodsList.get(index).getPrizeGoodsId() + ',' + prizeGoodsList.get(index).getPrizeGoodsName();
        } else {
            return "";
        }
    }

    @Override
    public JSONObject getPrizeGoodsList(PrizeGoodsExt form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getPrizeDrawId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            // 检索参数
            List<PrizeGoods> prizeGoodsList = prizeGoodsMapperExt.selectByPrizeDrawId(form.getPrizeDrawId());
            json.put("result", prizeGoodsList);
            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            json.put("resultCode", e.resultCd);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }

    @Override
    public JSONObject deletePrizeGoods(PrizeGoodsExt form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getPrizeGoodsId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            // 检索参数
            PrizeGoods prizeGoods = new PrizeGoods();
            prizeGoods.setPrizeGoodsId(form.getPrizeGoodsId());
            prizeGoods.setDeleted(Constants.DELETED_YES);
            prizeGoodsMapper.updateByPrimaryKeySelective(prizeGoods);
            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            json.put("resultCode", e.resultCd);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }

    @Override
    public JSONObject addPrizeGoods(PrizeGoodsExt form) {

        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getPrizeDrawId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            if (StringUtil.isEmpty(form.getPrizeGoodsName())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            if (form.getPrizeGoodsCount() == null) {
                throw new ExceptionErrorParam("缺少参数");
            }
            if (form.getSort() == null) {
                throw new ExceptionErrorParam("缺少参数");
            }
            if (StringUtil.isEmpty(form.getWinPercent())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            PrizeGoods prizeGoods = new PrizeGoods();
            String id = UUID.randomUUID().toString();
            prizeGoods.setPrizeGoodsId(id);
            prizeGoods.setPrizeDrawId(form.getPrizeDrawId());
            prizeGoods.setPrizeGoodsName(form.getPrizeGoodsName());
            prizeGoods.setPrizeGoodsCount(form.getPrizeGoodsCount());
            prizeGoods.setSort(form.getSort());
            prizeGoods.setWinPercent(form.getWinPercent());
            prizeGoods.setPrizeGoodsCountLeft(form.getPrizeGoodsCount());
            prizeGoods.setPrizeGoodsDesc(form.getPrizeGoodsDesc());
            prizeGoods.setPrizeGoodsImage(form.getPrizeGoodsImage());
            prizeGoods.setDeleted(Constants.DELETED_NO);
            prizeGoods.setInsertTime(new Date());
            prizeGoods.setInsertUserId(form.getInsertUserId());
            prizeGoodsMapper.insertSelective(prizeGoods);
            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            json.put("resultCode", e.resultCd);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }

    @Override
    public JSONObject editPrizeGoods(PrizeGoodsExt form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getPrizeDrawId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            if (StringUtil.isEmpty(form.getPrizeGoodsName())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            if (form.getPrizeGoodsCount() == null) {
                throw new ExceptionErrorParam("缺少参数");
            }
            if (form.getSort() == null) {
                throw new ExceptionErrorParam("缺少参数");
            }
            if (StringUtil.isEmpty(form.getWinPercent())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            prizeGoodsMapper.updateByPrimaryKeySelective(form);
            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            json.put("resultCode", e.resultCd);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }

    @Override
    public JSONObject setValid(PrizeDrawExt form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getPrizeDrawId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            PrizeDraw record = new PrizeDraw();
            record.setPrizeDrawId(form.getPrizeDrawId());
            record.setIsValid("1");
            record.setUpdateTime(new Date());
            record.setUpdateUserId(form.getUpdateUserId());
            prizeDrawMapper.updateByPrimaryKeySelective(record);
            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", e.resultCd);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }

    @Override
    public JSONObject setInvalid(PrizeDrawExt form) {
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isEmpty(form.getPrizeDrawId())) {
                throw new ExceptionErrorParam("缺少参数");
            }
            PrizeDraw record = new PrizeDraw();
            record.setPrizeDrawId(form.getPrizeDrawId());
            record.setIsValid("0");
            record.setUpdateTime(new Date());
            record.setUpdateUserId(form.getUpdateUserId());
            prizeDrawMapper.updateByPrimaryKeySelective(record);
            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", e.resultCd);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }


    @Override
    public JSONObject getPrizeOppList(PrizeDrawOppoExt form) {
        JSONObject json = new JSONObject();
        try {

            if (StringUtil.isEmpty(form.getPrizeDrawId())) {
                throw new ExceptionErrorParam("缺少参数");
            }

            //数据库检索 -- 过滤数据
            form.setDeleted(Constants.DELETED_NO);
            List<PrizeDrawOppoExt> list = prizeDrawOppoMapperExt.select(form);
            int allCount = prizeDrawOppoMapperExt.selectCount(form);
            json.put("aaData", list);
            json.put("sEcho", form.getsEcho());
            json.put("iTotalRecords", allCount);
            json.put("iTotalDisplayRecords", allCount);
            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", e.resultCd);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }

    @Override
    public JSONObject sendPrizeGoods(PrizeDrawOppoExt form) {
        JSONObject json = new JSONObject();
        try {

            if (StringUtil.isEmpty(form.getPrizeDrawOppoId())) {
                throw new ExceptionErrorParam("缺少参数");
            }

            PrizeDrawOppo prizeDrawOppo = prizeDrawOppoMapper.selectByPrimaryKey(form.getPrizeDrawOppoId());

            if (null == prizeDrawOppo || !Constants.DELETED_NO.equals(prizeDrawOppo.getDeleted())) {
                throw new ExceptionErrorParam("抽奖信息不存在");
            }

            if (!"1".equals(prizeDrawOppo.getIsDrawed())) {
                throw new ExceptionErrorParam("抽奖未进行");
            }

            if (!"1".equals(prizeDrawOppo.getIsWin())) {
                throw new ExceptionErrorParam("该用户未中奖");
            }

            if ("20".equals(prizeDrawOppo.getDeliveryStatus())) {
                throw new ExceptionErrorParam("该奖品已经发放，不能重复发放！");
            }

            prizeDrawOppo.setDeliveryStatus("20");
            prizeDrawOppo.setDeliveryComment(form.getDeliveryComment());
            prizeDrawOppo.setUpdateTime(new Date());
            prizeDrawOppo.setUpdateUserId(form.getUpdateUserId());

            prizeDrawOppoMapper.updateByPrimaryKeySelective(prizeDrawOppo);

            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", e.resultCd);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }

    @Override
    public JSONObject getPrizeConfig(PrizeDrawConfig form) {
        JSONObject json = new JSONObject();
        try {

            if (StringUtil.isEmpty(form.getPrizeDrawId())) {
                throw new ExceptionErrorParam("缺少参数");
            }

            PrizeDrawConfig data = queryPrizeConfigInfo(form.getPrizeDrawId());

            json.put("result", data);
            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", e.resultCd);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }

    private PrizeDrawConfig queryPrizeConfigInfo(String prizeDrawId) {

        PrizeDrawConfig prizeDrawConfig = prizeDrawConfigMapper.selectByPrimaryKey(prizeDrawId);
        if (prizeDrawConfig == null) {
            prizeDrawConfig = new PrizeDrawConfig();
            prizeDrawConfig.setPrizeDrawId(prizeDrawId);
            prizeDrawConfig.setExchangeFlag("0");
            prizeDrawConfig.setExchangePoint(BigDecimal.ZERO);
            prizeDrawConfig.setIsLogin("0");
            prizeDrawConfig.setIsOrder("0");
            prizeDrawConfig.setOrderAmount(BigDecimal.ZERO);
        }

        return prizeDrawConfig;
    }

    @Override
    public JSONObject updatePrizeConfig(PrizeDrawConfig form) {
        JSONObject json = new JSONObject();
        try {

            if (StringUtil.isEmpty(form.getPrizeDrawId())) {
                throw new ExceptionErrorParam("缺少参数");
            }

            PrizeDrawConfig prizeDrawConfig = prizeDrawConfigMapper.selectByPrimaryKey(form.getPrizeDrawId());

            if (prizeDrawConfig == null) {
                prizeDrawConfigMapper.insertSelective(form);
            } else {
                prizeDrawConfigMapper.updateByPrimaryKeySelective(form);
            }

            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", e.resultCd);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }

    /**
     * 添加（兑换）抽奖机会
     * <p>
     * 1、兑换只能指定单一兑换
     * 2、订单满赠及注册赠送都是根据配置查询获得活动结果(可能多条)
     *
     * @param memberId    用户ID
     * @param prizeDrawId 活动ID
     * @param type        操作类型 PrizeDrawOppoType
     * @param orderAmount 订单金额
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JSONObject addPrizeDrawOppo(String memberId, String prizeDrawId, String type, BigDecimal orderAmount) {
        JSONObject json = new JSONObject();
        try {

            if (StringUtil.isEmpty(memberId)) {
                throw new ExceptionErrorParam("缺少用户参照");
            }

            MmbMaster mmbMaster = mmbMasterMapper.selectByPrimaryKey(memberId);
            if (mmbMaster == null || !Constants.DELETED_NO.equals(mmbMaster.getDeleted())) {
                throw new ExceptionErrorParam("用户不存在");
            }

            if (ComCode.PrizeDrawOppoType.EXCHANGE.equals(type)) {
                // 积分兑换
                if (StringUtil.isEmpty(prizeDrawId)) {
                    throw new ExceptionErrorParam("缺少抽奖活动参数");
                }

                // 查询抽奖活动
                PrizeDraw prizeDraw = prizeDrawMapper.selectByPrimaryKey(prizeDrawId);
                if (prizeDraw == null) {
                    throw new ExceptionErrorData("抽奖活动不存在");
                }
                if ("1".equals(prizeDraw.getDeleted())) {
                    throw new ExceptionErrorData("抽奖活动已结束");
                }
                if ("0".equals(prizeDraw.getIsValid())) {
                    throw new ExceptionErrorData("抽奖活动已暂停");
                }
                Date now = new Date();
                if (prizeDraw.getEndTime().before(now)) {
                    throw new ExceptionErrorData("抽奖活动已结束");
                }

                PrizeDrawConfig prizeDrawConfig = queryPrizeConfigInfo(prizeDrawId);
                if (!"1".equals(prizeDrawConfig.getExchangeFlag())) {
                    throw new ExceptionErrorParam("该抽奖活动不可兑换");
                }

                if (mmbMaster.getPoint() < prizeDrawConfig.getExchangePoint().intValue()) {
                    throw new ExceptionErrorParam("会员可用积分不足");
                }

                // 扣积分
                MmbPointRecordForm point = new MmbPointRecordForm();
                point.setMemberId(memberId);
                point.setScoreSource(prizeDrawId);
                point.setExchangeId("60");
                point.setExchangePoint(prizeDrawConfig.getExchangePoint().intValue());
                mmbPointRecordService.add(point);

                PrizeDrawOppo record = new PrizeDrawOppo();
                record.setPrizeDrawOppoId(UUID.randomUUID().toString());
                record.setMemberId(memberId);
                record.setPrizeDrawId(prizeDrawId);
                record.setIsDrawed("0");
                record.setDeleted(Constants.DELETED_NO);
                record.setUpdateUserId(memberId);
                record.setUpdateTime(now);
                record.setInsertUserId(memberId);
                record.setInsertTime(now);

                prizeDrawOppoMapper.insertSelective(record);

            } else if (ComCode.PrizeDrawOppoType.REGISTER.equals(type)
                    || ComCode.PrizeDrawOppoType.ORDER.equals(type)) {

                PrizeDrawExt prizeDrawExt = new PrizeDrawExt();
                Date now = new Date();
                if (ComCode.PrizeDrawOppoType.REGISTER.equals(type)) {
                    prizeDrawExt.setIsLogin("1");
                } else {
                    prizeDrawExt.setIsOrder("1");
                    prizeDrawExt.setOrderAmount(orderAmount);
                }
                prizeDrawExt.setStartTime(now);
                prizeDrawExt.setEndTime(now);
                List<PrizeDraw> list = prizeDrawMapperExt.selectPrizeList(prizeDrawExt);

                PrizeDrawOppo record;
                String id;
                for (PrizeDraw item : list) {
                    record = new PrizeDrawOppo();
                    id = UUID.randomUUID().toString();
                    record.setPrizeDrawOppoId(id);
                    record.setMemberId(memberId);
                    record.setPrizeDrawId(item.getPrizeDrawId());
                    record.setIsDrawed("0");
                    record.setDeleted(Constants.DELETED_NO);
                    record.setUpdateUserId(memberId);
                    record.setUpdateTime(now);
                    record.setInsertUserId(memberId);
                    record.setInsertTime(now);

                    prizeDrawOppoMapper.insertSelective(record);
                }

            } else {
                throw new ExceptionErrorParam("参数不正确");
            }

            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", e.resultCd);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }

    /**
     * 获取某一活动的所有抽奖名单
     *
     * @param form
     * @return
     */
    @Override
    public JSONObject getWinningList(PrizeDrawOppoExt form) {
        JSONObject json = new JSONObject();
        try {

            if (StringUtil.isEmpty(form.getPrizeDrawId())) {
                throw new ExceptionErrorParam("缺少抽奖活动参数");
            }
            form.setIsDrawed("1");
            form.setIsWin("1");

            List<PrizeDrawOppoExt> list = prizeDrawOppoMapperExt.select(form);

            json.put("result", list);
            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", e.resultCd);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }

    @Override
    public JSONObject getPrizeDrawInfo(String memberId, String prizeDrawId) {
        JSONObject json = new JSONObject();
        try {

            JSONObject result = new JSONObject();

            if (StringUtil.isEmpty(memberId)) {
                throw new ExceptionErrorParam("会员未登录");
            }

            if (StringUtil.isEmpty(prizeDrawId)) {
                throw new ExceptionErrorParam("缺少抽奖活动ID");
            }

            PrizeDraw prizeDraw = prizeDrawMapper.selectByPrimaryKey(prizeDrawId);
            if (prizeDraw == null) {
                throw new ExceptionErrorData("抽奖不存在");
            }
            PrizeDrawExt prizeDrawExt = new PrizeDrawExt();
            BeanUtils.copyProperties(prizeDraw, prizeDrawExt);

            prizeDrawExt.setCurrentTime(new Date());

            PrizeDrawConfig config = queryPrizeConfigInfo(prizeDrawId);

            prizeDrawExt.setExchangeFlag(config.getExchangeFlag());
            prizeDrawExt.setExchangePoint(config.getExchangePoint());
            prizeDrawExt.setIsLogin(config.getIsLogin());
            prizeDrawExt.setIsOrder(config.getIsOrder());
            prizeDrawExt.setOrderAmount(config.getOrderAmount());

            // 活动信息及活动配置信息
            result.put("prizeDraw", prizeDrawExt);

            // 已获奖情况
            PrizeDrawOppoExt prizeDrawOppoExt = new PrizeDrawOppoExt();
            prizeDrawOppoExt.setMemberId(memberId);
            prizeDrawOppoExt.setPrizeDrawId(prizeDrawId);
            prizeDrawOppoExt.setIsDrawed("1");
            prizeDrawOppoExt.setIsWin("1");
            List<PrizeDrawOppoExt> winList = prizeDrawOppoMapperExt.select(prizeDrawOppoExt);
            result.put("winList", winList);

            // 剩余抽奖次数
            prizeDrawOppoExt = new PrizeDrawOppoExt();
            prizeDrawOppoExt.setMemberId(memberId);
            prizeDrawOppoExt.setPrizeDrawId(prizeDrawId);
            prizeDrawOppoExt.setIsDrawed("0");
            int count = prizeDrawOppoMapperExt.selectCount(prizeDrawOppoExt);
            result.put("drawCount", count);

            json.put("result", result);
            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", e.resultCd);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }

    @Override
    public JSONObject getMemberPrizeList(PrizeDrawOppoExt form) {
        JSONObject json = new JSONObject();
        try {

            if (StringUtil.isEmpty(form.getMemberId())) {
                throw new ExceptionErrorParam("会员未登录");
            }

            List<HashMap> list = prizeDrawOppoMapperExt.selectMemberPrizeList(form);
            int allCount = prizeDrawOppoMapperExt.selectMemberPrizeListCount(form);

            json.put("result", list);
            json.put("resultCount", allCount);
            json.put("resultCode", Constants.NORMAL);
        } catch (ExceptionBusiness e) {
            json.put("resultCode", e.resultCd);
            json.put("resultMessage", e.getMessage());
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }
}
