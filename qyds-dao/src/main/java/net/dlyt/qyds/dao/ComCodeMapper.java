package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.ComCode;
import net.dlyt.qyds.common.dto.ComCodeKey;
import org.springframework.stereotype.Repository;

@Repository
public interface ComCodeMapper {

    int deleteByPrimaryKey(ComCodeKey key);

    int insert(ComCode record);

    int insertSelective(ComCode record);

    ComCode selectByPrimaryKey(ComCodeKey key);

    int updateByPrimaryKeySelective(ComCode record);

    int updateByPrimaryKey(ComCode record);
}