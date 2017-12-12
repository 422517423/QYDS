package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.ActTempParam;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by cjk on 16/7/28.
 */
@Repository
public interface ActTempParamMapperExt {
    void deleteByTempId(ActTempParam param);

    List<ActTempParam> selectByTempId(ActTempParam actTempParam);
}
