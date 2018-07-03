package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.ComCode;
import net.dlyt.qyds.common.dto.ComCodeExt;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComCodeMapperExt {

    List<ComCodeExt> selectAll();
    List<ComCode> selectComCodeByCategory(String category);

    int updateByPrimaryKeySelective(ComCode record);

    List<ComCode> selectByNeed(ComCode record);
}