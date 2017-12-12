package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.BnkMasterExt;
import net.dlyt.qyds.common.dto.ShpStroeDetail;
import net.dlyt.qyds.common.dto.ext.ShpStroeDetailExt;
import net.dlyt.qyds.common.form.ShpStroeDetailForm;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wy on 2016/8/10.
 */
@Repository
public interface ShpStroeDetailMapperExt {

    List<ShpStroeDetailExt> selectAll(ShpStroeDetailExt form);

    List<ShpStroeDetail> selectByPage(ShpStroeDetail record);


    int getAllDataCount(ShpStroeDetail detail);
}
