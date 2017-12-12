package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.GdsMasterExt;
import net.dlyt.qyds.common.dto.ext.GdsScheduleExt;
import net.dlyt.qyds.dao.GdsScheduleMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by panda on 16/9/26.
 */
@Repository
public interface GdsScheduleMapperExt extends GdsScheduleMapper {

    List<GdsScheduleExt> getGoodsOrderList(GdsMasterExt ext);

    int getAllDataCount(GdsMasterExt ext);
}
