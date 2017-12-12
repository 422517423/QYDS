package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.ext.MmbGroupExt;
import net.dlyt.qyds.common.dto.ext.MmbGroupMemberExt;
import net.dlyt.qyds.common.form.MmbGroupForm;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MmbGroupMapperExt {

    /**
     * 获取积分规则列表
     *
     * @param form
     * @return
     */
    List<MmbGroupExt> selectGroupList(MmbGroupForm form);

    int getGroupListCount(MmbGroupForm form);

    /**
     * 根据名称(相等)检索,检证是否有重名的分组
     *
     * @param form
     * @return
     */
    int countGroupByName(MmbGroupForm form);

    /**
     * 查看会员组详细
     *
     * @param groupId
     * @return
     */
    MmbGroupExt select(String groupId);

    List<MmbGroupMemberExt> selectMemberList(MmbGroupForm form);

    int countMemberList(MmbGroupForm form);
}