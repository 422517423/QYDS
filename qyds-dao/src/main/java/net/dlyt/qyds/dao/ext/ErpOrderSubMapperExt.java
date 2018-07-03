package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.ErpOrderSub;
import net.dlyt.qyds.common.form.ErpOrderSubForm;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zlh on 2016/7/29.
 */
@Repository
public interface ErpOrderSubMapperExt {
    //根据条件取得分页信息
    List<ErpOrderSub> selectByPage(ErpOrderSubForm form);
    //根据条件取得件数信息
    int getCountByPage(ErpOrderSubForm form);

    List<ErpOrderSub> selectByKey(String orderCode);

}