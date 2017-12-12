package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.MmbGroupMember;
import net.dlyt.qyds.common.dto.ext.MmbGroupMemberExt;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by YiLian on 16/7/31.
 */
@Repository
public interface MmbGroupMemberMapperExt {

    List<MmbGroupMemberExt> select(MmbGroupMember record);

    void deleteGroup(MmbGroupMember record);

    void removeMemberFormGroup(MmbGroupMember record);
}
