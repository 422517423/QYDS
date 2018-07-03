package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.ErpPointRecord;
import net.dlyt.qyds.common.form.ErpPointRecordForm;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zlh on 2016/7/29.
 */
@Repository
public interface ErpPointRecordMapperExt {
    //根据条件取得分页信息
    List<ErpPointRecord> selectRecordByPage(ErpPointRecordForm form);
    //根据条件取得件数信息
    int getRecordCountByPage(ErpPointRecordForm form);
}