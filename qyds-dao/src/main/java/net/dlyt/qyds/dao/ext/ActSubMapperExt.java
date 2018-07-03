package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.ActMaster;
import net.dlyt.qyds.common.dto.ActSub;
import net.dlyt.qyds.common.form.ActMasterForm;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by cjk on 16/9/5.
 */
@Repository
public interface ActSubMapperExt {

    void deleteByActivityId(ActSub form);

    List<ActMasterForm> selectByActivityId(ActSub form);
}
