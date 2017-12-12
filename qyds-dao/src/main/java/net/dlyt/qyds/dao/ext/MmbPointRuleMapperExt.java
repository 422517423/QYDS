package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.ext.MmbPointRuleExt;
import net.dlyt.qyds.common.form.MmbPointRuleForm;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by YiLian on 16/7/29.
 */
@Repository
public interface MmbPointRuleMapperExt {

    // 获取积分规则列表
    List<MmbPointRuleExt> selectRuleList(MmbPointRuleForm form);

    int getRuleListCount(MmbPointRuleForm form);

    /**
     * 检查是否存在同名或者是同编码的规则存在(不包括rule_id相同)
     *
     * @param form
     * @return
     */
    int countSameRule(MmbPointRuleForm form);

    /**
     * 根据主键取得规则详细
     *
     * @param form
     * @return
     */
    MmbPointRuleExt select(MmbPointRuleForm form);

    /**
     * 根据规则编码获取规则详细
     *
     * @param ruleCode
     * @return
     */
    MmbPointRuleExt selectByRuleCode(String ruleCode);
}
