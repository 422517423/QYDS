package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.OrdDispatch;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * Created by YiLian on 2016/10/10.
 */
@Repository
public interface OrdDispatchMapperExt {

    List<HashMap> selectDispatchHistory(OrdDispatch ordDispatch);
}
