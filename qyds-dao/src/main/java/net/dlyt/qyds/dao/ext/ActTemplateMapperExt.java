package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.ActTemplate;
import net.dlyt.qyds.common.form.ActTemplateForm;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by cjk on 16/7/27.
 */
@Repository
public interface ActTemplateMapperExt {
    List<ActTemplate> select(ActTemplate form);
    int selectCount(ActTemplate form);
    int checkExistByTempName(ActTemplate form);

    List<ActTemplate> selectApproveList(ActTemplateForm form);

    int selectApproveCount(ActTemplateForm form);

    ActTemplateForm selectById(ActTemplateForm tempId);
}
