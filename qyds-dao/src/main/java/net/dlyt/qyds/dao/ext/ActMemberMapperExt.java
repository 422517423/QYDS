package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.ActGoods;
import net.dlyt.qyds.common.dto.ActMember;
import net.dlyt.qyds.common.form.ActMemberForm;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by cjk on 16/8/4.
 */
@Repository
public interface ActMemberMapperExt {
    
    void deleteByActivityId(ActMember form);

    List<ActMember> selectByActivityId(ActMember form);

    List<ActMemberForm> selectMemberGroupByActivityId(ActMember member);

    List<ActMemberForm> selectMemberLevelByActivityId(ActMember member);
}
