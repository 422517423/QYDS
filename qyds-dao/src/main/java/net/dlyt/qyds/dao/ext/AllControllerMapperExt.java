package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.AllController;
import net.dlyt.qyds.common.dto.ext.AllControllerExt;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AllControllerMapperExt {
    List<AllControllerExt> selectAll(AllControllerExt ext);

    int getAllDataCount(AllControllerExt ext);

    AllControllerExt selectBySelective(AllController allController);
}
