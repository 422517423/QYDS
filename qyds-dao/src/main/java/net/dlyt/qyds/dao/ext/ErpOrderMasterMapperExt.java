package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.ErpOrderMaster;
import net.dlyt.qyds.common.form.ErpOrderMasterForm;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zlh on 2016/7/29.
 */
@Repository
public interface ErpOrderMasterMapperExt {
    //根据条件取得分页信息
    List<ErpOrderMaster> selectByPage(ErpOrderMasterForm form);
    //根据条件取得件数信息
    int getCountByPage(ErpOrderMasterForm form);
}