package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.*;
import net.dlyt.qyds.common.dto.ext.ErpMemberExt;
import net.dlyt.qyds.common.form.ErpMemberForm;
import net.dlyt.qyds.dao.ErpMemberMapper;
import net.dlyt.qyds.dao.MmbMasterMapper;
import net.dlyt.qyds.dao.MmbSalerMapper;
import net.dlyt.qyds.dao.SysUserRoleMapper;
import net.dlyt.qyds.dao.ext.*;
import net.dlyt.qyds.web.service.common.EncryptUtil;
import net.dlyt.qyds.web.service.ErpMemberService;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.common.StringUtil;
import net.dlyt.qyds.web.service.exception.ExceptionBusiness;
import net.dlyt.qyds.web.service.exception.ExceptionErrorParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by zlh on 2016/7/29.
 */
@Service("erpMemberService")
@Transactional(readOnly = true)
public class ErpMemberServiceImpl implements ErpMemberService {

    @Autowired
    private ErpMemberMapper mapper;

    @Autowired
    private ErpMemberMapperExt mapperExt;

    @Autowired
    private MmbMasterMapper mmbMasterMapper;

    @Autowired
    private MmbMasterMapperExt mmbMasterMapperExt;

    @Autowired
    private MmbSalerMapper mmbSalerMapper;

    @Autowired
    private MmbSalerMapperExt mmbSalerMapperExt;

    @Autowired
    private SysUserMapperExt sysUserMapperExt;

    @Autowired
    private SysUserRoleMapperExt sysUserRoleMapperExt;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    /**
     * 根据Id获取会员列表
     *
     * @return
     */
    public JSONObject selectAll(){
        JSONObject map = new JSONObject();
        try{
            List<ErpMember> list = (List<ErpMember>) mapperExt.selectAll();
            map.put("data", list);
            map.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            map.put("resultCode", Constants.FAIL);
//            map.put("message", e.getMessage());
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 根据Id获取店员列表
     *
     * @return
     */
    public JSONObject selectSaler(){
        JSONObject map = new JSONObject();
        try{
            //TODO
            List<ErpMember> list = (List<ErpMember>) mapperExt.selectSaler();
            map.put("data", list);
            map.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            map.put("resultCode", Constants.FAIL);
//            map.put("message", e.getMessage());
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 根据条件获取会员列表
     *
     * @param record
     * @return
     */
    public JSONObject selectBySelective(String record){
        JSONObject map = new JSONObject();
        try{
            ErpMemberForm rec = JSON.parseObject(record, ErpMemberForm.class);
            List<ErpMember> list = (List<ErpMember>) mapperExt.selectByPage(rec);
            map.put("data", list);
            map.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            map.put("resultCode", Constants.FAIL);
//            map.put("message", e.getMessage());
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 根据条件获取会员列表
     *
     * @param record
     * @return
     */
    public JSONObject selectByPage(String record){
        JSONObject map = new JSONObject();
        try{
            ErpMemberForm form = JSON.parseObject(record, ErpMemberForm.class);
            List<ErpMember> list = (List<ErpMember>) mapperExt.selectByPage(form);
            int countAll = mapperExt.getCountByPage(form);
            map.put("aaData", list);
            map.put("sEcho",form.getSEcho());
            map.put("iTotalRecords",countAll);
            map.put("iTotalDisplayRecords",countAll);
            map.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            map.put("resultCode", Constants.FAIL);
//            map.put("message", e.getMessage());
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 根据条件获取会员数量
     *
     * @param record
     * @return
     */
    public JSONObject getCountBySelective(String record){
        JSONObject map = new JSONObject();
        try{
            ErpMemberForm form = JSON.parseObject(record, ErpMemberForm.class);
            int countAll = mapperExt.getCountByPage(form);
            map.put("data", countAll);
            map.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            map.put("resultCode", Constants.FAIL);
//            map.put("message", e.getMessage());
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 根据ID获取会员信息
     *
     * @param id
     * @return
     */
    public JSONObject getById(String id){
        JSONObject map = new JSONObject();
        try{
            if (StringUtil.isEmpty(id)) {
                throw new ExceptionErrorParam("缺少参数会员ID");
            }
            ErpMember record = mapper.selectByPrimaryKey(id);
            map.put("data", record);
            map.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            map.put("resultCode", Constants.FAIL);
//            map.put("message", e.getMessage());
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 根据ID更新会员信息
     *
     * @param record
     */
    public JSONObject updateById(String record){
        JSONObject map = new JSONObject();
        try{
            ErpMember rec = JSON.parseObject(record, ErpMember.class);
            if (StringUtil.isEmpty(rec.getMemberCode())) {
                throw new ExceptionErrorParam("缺少参数会员ID");
            }
            mapper.updateByPrimaryKey(rec);
            map.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            map.put("resultCode", Constants.FAIL);
//            map.put("message", e.getMessage());
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 新建会员信息
     *
     * @param record
     */
    public JSONObject insert(String record){
        JSONObject map = new JSONObject();
        try{
            ErpMember rec = JSON.parseObject(record, ErpMember.class);
            if (StringUtil.isEmpty(rec.getMemberCode())) {
                throw new ExceptionErrorParam("缺少参数会员ID");
            }
            mapper.insertSelective(rec);
            map.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            map.put("resultCode", Constants.FAIL);
//            map.put("message", e.getMessage());
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 根据ID物理删除会员信息
     *
     * @param id
     */
    public JSONObject deleteByPrimaryKey(String id){
        mapper.deleteByPrimaryKey(id);
        JSONObject map = new JSONObject();
        try{
            if (StringUtil.isEmpty(id)) {
                throw new ExceptionErrorParam("缺少参数会员ID");
            }
            mapper.deleteByPrimaryKey(id);
            map.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            map.put("resultCode", Constants.FAIL);
//            map.put("message", e.getMessage());
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 物理删除所有会员信息
     *
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject deleteAll(){
        JSONObject map = new JSONObject();
        try{
            mapperExt.deleteAll();
            map.put("resultCode", Constants.NORMAL);
//        }catch(ExceptionBusiness e){
//            map.put("resultCode", Constants.FAIL);
//            map.put("message", e.getMessage());
        }catch(Exception e){
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 删除原有数据,批量导入会员信息
     *
     * @param list
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject inputAll(String list){
        JSONObject map = new JSONObject();
        try{
            //ERP全部删除
            mapperExt.deleteAll();

            //删除店员前台用户
            MmbSaler mmbSaler = new MmbSaler();
            //会员类型30:店员
            mmbSaler.setType("30");
            mmbSalerMapperExt.deleteBySelective(mmbSaler);

            //删除后台ERP导入用户:
            //删除角色
            sysUserRoleMapperExt.deleteErpStorer();
            //删除用户主表(逻辑删除)
            sysUserMapperExt.deleteErpStorer();

            List<ErpMember> ll = JSON.parseArray(list, ErpMember.class);
            for (ErpMember record : ll) {
                String memberCode = record.getMemberCode();
                if (StringUtil.isEmpty(memberCode)) {
                    throw new ExceptionBusiness("没有会员代码");
                }
                //是否店员
                boolean isStorer = memberCode.length() == 6;
                //店员
                if (isStorer) {
                    if (StringUtil.isEmpty(record.getStoreCode())) {
                        throw new ExceptionBusiness(memberCode+"店员未指定门店");
                    }
                } else {
                    if (StringUtil.isEmpty(record.getMobil())) {
                        throw new ExceptionBusiness("没有会员手机号");
                    }
                    if (!StringUtil.isTelephone(record.getMobil())) {
                        throw new ExceptionErrorParam("手机号码不正确");
                    }
                    if (!record.getMobil().equals(memberCode)) {
                        throw new ExceptionBusiness(record.getMemberCode() + "会员代码与手机号不一致");
                    }
                }
                //插入ERP会员
                mapper.insertSelective(record);

                //店员用户
                if(isStorer) {
                    //更新店员主表
                    MmbSaler saler = changSaler(record);
                    int count = mmbSalerMapperExt.updateByCode(saler);
                    //没有可更新数据的场合进行插入操作
                    if (count == 0) {
                        insertErp(saler);
                    }
                    //插入系统用户
                    insertUser(record);
                } else {
                    //更新会员主表
                    MmbMaster master = changMember(record);
                    int count = mmbMasterMapperExt.updateByCode(master);
                    //没有可更新数据的场合进行插入操作
                    if (count == 0) {
                        insertErp(master);
                    }
                }
            }
            map.put("resultCode", Constants.NORMAL);
        }catch(Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 批量更新会员信息
     *
     * @param list
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public JSONObject updateByList(String list){
        JSONObject map = new JSONObject();
        try{
            List<ErpMemberExt> ll = JSON.parseArray(list, ErpMemberExt.class);
            for (ErpMemberExt record : ll) {
                String style = record.getStyle();
                if (StringUtil.isEmpty(style)) {
                    throw new ExceptionBusiness("没有更新方式");
                }
                String memberCode = record.getMemberCode();
                if (StringUtil.isEmpty(memberCode)) {
                    throw new ExceptionBusiness("没有会员代码");
                }
                //是否店员
                boolean isStorer = memberCode.length() == 6;
                //店员
                if (isStorer) {
                    if (StringUtil.isEmpty(record.getStoreCode())) {
                        throw new ExceptionBusiness(memberCode+"店员未指定门店");
                    }
                } else {
                    if (StringUtil.isEmpty(record.getMobil())) {
                        throw new ExceptionBusiness("没有会员手机号");
                    }
                    if (!StringUtil.isTelephone(record.getMobil())) {
                        throw new ExceptionErrorParam("手机号码不正确");
                    }
                    if (!record.getMobil().equals(memberCode)) {
                        throw new ExceptionBusiness(record.getMemberCode() + "会员代码与手机号不一致");
                    }
                }
                MmbSaler saler = null;
                MmbMaster master = null;
                if (isStorer) {
                    saler = changSaler(record);
                } else {
                    master = changMember(record);
                }
                if(style.equals("10")) {
                    //新增
                    mapper.insertSelective(record);
                    //店员用户
                    if(isStorer) {
                        //插入主表
                        insertErp(saler);
                        //插入系统用户
                        insertUser(record);
                    } else {
                        //插入主表
                        insertErp(master);
                    }
                } else if (style.equals("20")) {
                    //修改
                    int count = mapperExt.updateNoTime(record);
                    if (count == 0) {
                        throw new ExceptionBusiness(record.getMemberCode()+"会员不存在");
                    }
                    //店员用户
                    if(isStorer) {
                        saler.setDeleted("0");
                        count = mmbSalerMapperExt.updateByCode(saler);
                        //插入系统用户
                        insertUser(record);
                    } else {
                        count = mmbMasterMapperExt.updateByCode(master);
                    }
                } else if (style.equals("30")) {
                    //删除
//                    mapper.deleteByPrimaryKey(record.getMemberCode());
                    ErpMember erpMember = new ErpMember();
                    erpMember.setMemberCode(record.getMemberCode());
                    erpMember.setDeleted("1");
                    erpMember.setUpdateTime(new Date());
                    mapper.updateByPrimaryKeySelective(erpMember);

                    //店员用户
                    if(isStorer) {
                        mmbSalerMapperExt.deleteByCode(record.getMemberCode());
                        //插入系统用户
                        deleteUser(record);
                    } else {
                        mmbMasterMapperExt.deleteByCode(record.getMemberCode());
                    }
                } else {
                    throw new ExceptionBusiness("参数style错误");
                }
            }
            map.put("resultCode", Constants.NORMAL);
        }catch(Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            map.put("resultCode", Constants.FAIL);
            map.put("message", e.getMessage());
        }
        return map;
    }

    private MmbMaster changMember(ErpMember record,boolean isStorer) throws Exception {
        MmbMaster master = new MmbMaster();
        //会员类型
        master.setType(isStorer?"30":"10");
        //会员名称
        master.setMemberName(record.getMemberName());
        //电话
        master.setTelephone(record.getMemberCode());
        //积分
//        master.setPoint(record.getPoint());
        //会员级别
        String level = record.getMemberGrade();
        master.setMemberLevelId(level.equals("高级会员")?"30":(level.equals("中级会员")?"20":"10"));
        //生日
        master.setBirthdate(record.getBirthday());
        //邮箱
        master.setEmail(record.getEmail());
        //性别
        master.setSex(record.getSexCode());
        //注册时间
        master.setInsertTime(record.getRegistTime());
        //职业
        master.setProfession(record.getProfession());
        //收入
        master.setIncome(record.getIncome());
        //地址
        master.setProvinceCode(record.getProvinceCode());
        master.setProvinceName(record.getProvinceName());
        master.setCityCode(record.getCityCode());
        master.setCityName(record.getCityName());
        master.setDistrictCode(record.getDistrictCode());
        master.setDistrictName(record.getDistrictName());
        master.setAddress(record.getAddress());
        master.setUpdateUserId("ERP");
        return master;
    }

    private MmbMaster changMember(ErpMember record) throws Exception {
        MmbMaster master = new MmbMaster();
        //会员类型
        master.setType("10");
        //会员名称
        master.setMemberName(record.getMemberName());
        //电话
        master.setTelephone(record.getMemberCode());
        //会员级别
        String level = record.getMemberGrade();
        master.setMemberLevelId(level.equals("高级会员")?"30":(level.equals("中级会员")?"20":"10"));
        //生日
        master.setBirthdate(record.getBirthday());
        //邮箱
        master.setEmail(record.getEmail());
        //性别
        master.setSex(record.getSexCode());
        //注册时间
        master.setInsertTime(record.getRegistTime());
        //职业
        master.setProfession(record.getProfession());
        //收入
        master.setIncome(record.getIncome());
        //地址
        master.setProvinceCode(record.getProvinceCode());
        master.setProvinceName(record.getProvinceName());
        master.setCityCode(record.getCityCode());
        master.setCityName(record.getCityName());
        master.setDistrictCode(record.getDistrictCode());
        master.setDistrictName(record.getDistrictName());
        master.setAddress(record.getAddress());
        master.setUpdateUserId("ERP");
        return master;
    }

    private MmbSaler changSaler(ErpMember record) throws Exception {
        MmbSaler master = new MmbSaler();
        //会员类型
        master.setType("30");
        //会员名称
        master.setMemberName(record.getMemberName());
        //电话
        master.setTelephone(record.getMemberCode());
        master.setOldphone(record.getMobil());
        //生日
        master.setBirthdate(record.getBirthday());
        //邮箱
        master.setEmail(record.getEmail());
        //性别
        master.setSex(record.getSexCode());
        //注册时间
        master.setInsertTime(record.getRegistTime());
        //职业
        master.setProfession(record.getProfession());
        //收入
        master.setIncome(record.getIncome());
        //地址
        master.setProvinceCode(record.getProvinceCode());
        master.setProvinceName(record.getProvinceName());
        master.setCityCode(record.getCityCode());
        master.setCityName(record.getCityName());
        master.setDistrictCode(record.getDistrictCode());
        master.setDistrictName(record.getDistrictName());
        master.setAddress(record.getAddress());
        master.setUpdateUserId("ERP");
        return master;
    }

    private void insertErp(MmbMaster master,boolean isStorer) throws Exception {
        //电话重复检查
//        MmbMaster m = new MmbMaster();
//        m.setTelephone(master.getTelephone());
//        int count = mmbMasterMapperExt.getCountBySelective(m);
//        if (count > 0) {
//            throw new ExceptionBusiness(master.getTelephone()+"会员已注册");
//        }
        //生产UUID作为会员ID
        master.setMemberId(UUID.randomUUID().toString());
        //默认密码
        if (isStorer) {
            master.setPassword(EncryptUtil.encodeMD5(EncryptUtil.encodeMD5(
                    master.getTelephone()).toLowerCase()));
        }
        //会员类型:普通
//        master.setType("10");
        //注册方式:ERP
        master.setRegistStyle("40");
        //有效性
        master.setIsValid("0");
        //删除标记
        master.setDeleted("0");
//        master.setUpdateUserId("ERP");
        master.setInsertUserId("ERP");
        mmbMasterMapper.insertSelective(master);
    }

    private void insertErp(MmbMaster master) throws Exception {
        //重复检查
        int count = mmbMasterMapperExt.countByTelephone(master.getTelephone());
        if (count>0) throw new Exception("该会员已注册");
        //生产UUID作为会员ID
        master.setMemberId(UUID.randomUUID().toString());
        //注册方式:ERP
        master.setRegistStyle("40");
        //有效性
        master.setIsValid("0");
        //删除标记
        master.setDeleted("0");
        master.setInsertUserId("ERP");
        mmbMasterMapper.insertSelective(master);
    }

    private void insertErp(MmbSaler master) throws Exception {
        //重复检查
        int count = mmbSalerMapperExt.getCountByPhone(master.getTelephone());
        if (count>0) throw new Exception("该店员已经存在");
        //生产UUID作为会员ID
        master.setMemberId(UUID.randomUUID().toString());
        //默认密码
        master.setPassword(EncryptUtil.encodeMD5(EncryptUtil.encodeMD5(
                master.getTelephone()).toLowerCase()));
        //有效性
        master.setIsValid("0");
        //删除标记
        master.setDeleted("0");
        master.setInsertUserId("ERP");
        mmbSalerMapper.insertSelective(master);
    }

    private void insertUser(ErpMember member) throws Exception {
        //用户
        SysUser user = new SysUser();
        SysUser userOld = sysUserMapperExt.selectByLoginId(member.getMemberCode());
        boolean isOld = userOld != null;

        user.setUserName(member.getMemberName());
        user.setEmail(member.getEmail());
        user.setMobile(member.getMobil());
        user.setOrgId(member.getStoreCode());
        user.setUpdateUser("ERP");
        if (isOld) {
            user.setUserId(userOld.getUserId());
            user.setIsValid("0");
            sysUserMapperExt.updateByPrimaryKeySelective(user);
        } else {
            user.setShpId(Constants.ORGID);
            user.setLoginId(member.getMemberCode());
            user.setPassword(EncryptUtil.encodeMD5(EncryptUtil.encodeMD5(
                    member.getMemberCode().toLowerCase()).toLowerCase()));
            user.setIsValid("0");
            user.setCreateUser("ERP");
            int retInsert = sysUserMapperExt.insertSelective(user);
            //取得userid
            userOld = sysUserMapperExt.selectByLoginId(member.getMemberCode());
        }

        //更新角色
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUserId(userOld.getUserId());
        //店员角色
        sysUserRole.setRoleId(10066);
        int upcount = sysUserRoleMapper.updateByPrimaryKey(sysUserRole);
        if (upcount<=0){
            sysUserRoleMapper.insert(sysUserRole);
        }

    }

    private void deleteUser(ErpMember member) throws Exception {
        //用户
        SysUser user = sysUserMapperExt.selectByLoginId(member.getMemberCode());
        int retInsert = sysUserMapperExt.deleteByLoginId(member.getMemberCode());

        //删除角色
//        if(user==null)return;
//        int upcount = sysUserRoleMapper.deleteByPrimaryKey(user.getUserId());
    }
}
