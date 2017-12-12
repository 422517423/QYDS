package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ErpPointRecord;
import net.dlyt.qyds.common.dto.MmbMaster;
import net.dlyt.qyds.common.dto.MmbPointRecord;
import net.dlyt.qyds.common.form.ErpPointRecordForm;
import net.dlyt.qyds.dao.ErpPointRecordMapper;
import net.dlyt.qyds.dao.MmbPointRecordMapper;
import net.dlyt.qyds.dao.ext.ErpPointRecordMapperExt;
import net.dlyt.qyds.dao.ext.MmbMasterMapperExt;
import net.dlyt.qyds.dao.ext.MmbPointRecordMapperExt;
import net.dlyt.qyds.web.service.ErpPointRecordService;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.common.NumberUtil;
import net.dlyt.qyds.web.service.common.StringUtil;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import net.dlyt.qyds.web.service.exception.ExceptionErrorData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static net.dlyt.qyds.web.service.common.DataUtils.daysToNow;

/**
 * Created by zlh on 2016/7/29.
 */
@Service("erpPointRecordService")
@Transactional(readOnly = true)
public class ErpPointRecordServiceImpl implements ErpPointRecordService {

    @Autowired
    private ErpPointRecordMapper mapper;

    @Autowired
    private ErpPointRecordMapperExt mapperExt;

    @Autowired
    private MmbMasterMapperExt mapperMmbMasterExt;

    @Autowired
    private MmbPointRecordMapper mapperMmbPoint;

    @Autowired
    private MmbPointRecordMapperExt mapperMmbPointExt;

    /**
     * 根据条件获取积分记录列表
     *
     * @param record
     * @return
     */
    public JSONObject selectRecordByPage(String record){
        JSONObject json = new JSONObject();
        try{
            ErpPointRecordForm form = (ErpPointRecordForm) JSON.parseObject(record, ErpPointRecordForm.class);
            List<ErpPointRecord> list = (List<ErpPointRecord>) mapperExt.selectRecordByPage(form);
            int countAll = mapperExt.getRecordCountByPage(form);
            json.put("aaData", list);
            json.put("sEcho",form.getSEcho());
            json.put("iTotalRecords",countAll);
            json.put("iTotalDisplayRecords",countAll);
            json.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            json.put("resultCode", Constants.FAIL);
//            json.put("message", e.getMessage());
        }catch(Exception e){
            json.put("resultCode", Constants.FAIL);
            json.put("message", e.getMessage());
        }
        return json;
    }

    /**
     * 导入会员积分信息
     *
     * @param data
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject inputAll(String data){
        JSONObject json = new JSONObject();
        try{
            List<ErpPointRecord> list = JSONArray.parseArray(data, ErpPointRecord.class);
            for (ErpPointRecord record : list) {
                if(StringUtil.isEmpty(record.getMemberCode())) {
                    throw new ExceptionBusiness("没有会员编码");
                }
                //检查会员
                MmbMaster member = mapperMmbMasterExt.getPointByCode(record.getMemberCode());
                if (member == null) {
                    throw new ExceptionBusiness(record.getMemberCode()+"会员不存在");
                }
                //插入ERP积分履历
                mapper.insertSelective(record);
                //积分是否生效
                Date ioTime = record.getInoutTime();
                if (ioTime == null) throw new ExceptionErrorData("没有积分时间");
                boolean isValid = daysToNow(ioTime) >= 30;
                int surplusPoint = record.getInoutPoint();
                member.setAllPoint(member.getAllPoint() + surplusPoint);
                if (isValid || surplusPoint < 0) {
                    //会员积分更新
                    member.setPoint(member.getPoint() + surplusPoint);
//                    member.setAllPoint(member.getAllPoint() + (surplusPoint > 0 ? surplusPoint : 0));
//                    mapperMmbMasterExt.updatePointById(member);
                }
                mapperMmbMasterExt.updatePointById(member);
                //插入会员积分履历
                MmbPointRecord mmbRecord = new MmbPointRecord();
                mmbRecord.setMemberId(member.getMemberId());
                //生效积分10,未生效积分11,消费积分70
                mmbRecord.setType(surplusPoint>0?(isValid?"10":"11"):"70");
                mmbRecord.setRuleId(mmbRecord.getType());
                //定级积分是否30天有效
                mmbRecord.setPointCash(NumberUtil.formatInteger(record.getAmount()));
                mmbRecord.setPoint(surplusPoint);
                mmbRecord.setPointTime(record.getInoutTime());
//                mmbRecord.setRecordType(record.getType());
                mmbRecord.setUpdateUserId("ERP");
                mmbRecord.setInsertUserId("ERP");
                //已生效的收入积分
                mmbRecord.setPointSurplus((surplusPoint > 0 && isValid) ? surplusPoint : 0);
                mapperMmbPoint.insertSelective(mmbRecord);
                if (surplusPoint >= 0) continue;
                //扣除积分处理
                //取得ERP有效积分记录
                List<MmbPointRecord> listRecord = mapperMmbPointExt.selectErpPointByMemberd(member.getMemberId());
                if(listRecord == null || listRecord.size() == 0){
                    throw new ExceptionBusiness(record.getMemberCode()+"会员没有可用积分");
                }
                for (MmbPointRecord r : listRecord) {
                    surplusPoint += r.getPointSurplus();
                    r.setPointSurplus(surplusPoint>0?surplusPoint:0);
                    mapperMmbPointExt.updateSurplusPointById(r);
                    if (surplusPoint >= 0) break;
                }
                if(surplusPoint < 0){
                    throw new ExceptionBusiness(record.getMemberCode()+"会员可用积分不足");
                }
            }
            json.put("resultCode", Constants.NORMAL);
        }catch(Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            json.put("resultCode", Constants.FAIL);
            json.put("message", e.getMessage());
        }
        return json;
    }
}
