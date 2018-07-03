package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.ext.MmbLevelRuleExt;
import net.dlyt.qyds.common.form.MmbLevelManagerForm;
import net.dlyt.qyds.common.form.MmbLevelRuleForm;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MmbLevelRuleMapperExt {

    // 获取会员级别列表
    List<MmbLevelRuleExt> selectAll(MmbLevelRuleForm form);

    // 获取会员级别总数
    int getAllDataCount(MmbLevelRuleForm form);

    List<MmbLevelRuleExt> select(MmbLevelRuleForm form);

    // 根据会员单笔消费积分查询会员级别
    MmbLevelRuleExt selectLevelBySinglePoint(Integer point);

    // 根据当前积分查询会员级别(上下限)
    MmbLevelRuleExt selectMemberLevelByYearPoint(Integer point);

    /**
     * 查询当年度由于累计积分满足条件或单笔积分满足条件的升级会员
     *
     * @param form
     * @return
     */
    List<MmbLevelManagerForm> selectApprovalUpMemberList(MmbLevelManagerForm form);

    List<MmbLevelManagerForm> selectApprovalUpMemberListInTwo(MmbLevelManagerForm form);

    int countApprovalUpMemberList(MmbLevelManagerForm form);

    int countApprovalUpMemberListInTwo(MmbLevelManagerForm form);

    /**
     * 查询去年度累计积分不足等级下限80%的降级会员
     *
     * @param ratio
     *
     * @return
     */
   // List<MmbLevelManagerForm> selectRelegationMemberList(float ratio);

    /**
     * 查询去年度累计积分不足等级下限80%的降级会员
     *
     *
     * @return
     */
    List<MmbLevelManagerForm> selectRelegationMemberList();

    List<MmbLevelManagerForm> selectRelegationMemberListOneTime();

}