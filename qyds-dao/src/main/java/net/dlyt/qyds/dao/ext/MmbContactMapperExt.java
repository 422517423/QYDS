package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.MmbContact;
import net.dlyt.qyds.common.form.MmbContactExt;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MmbContactMapperExt {

    /**
     * 联系我们
     *
     * @param form
     * @return
     */
    List<MmbContact> selectAllList(MmbContactExt form);

    int selectCountAllList(MmbContactExt form);

}