package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.ext.ErpBankRecordExt;
import net.dlyt.qyds.common.form.ErpBankRecordForm;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zlh on 2016/7/29.
 */
@Repository
public interface ErpBankRecordMapperExt {
    //取得全部信息
    List<ErpBankRecordExt> selectAll();
    //根据条件取得分页信息
    List<ErpBankRecordExt> selectByPage(ErpBankRecordForm form);
    //根据条件取得件数信息
    int getCountByPage(ErpBankRecordForm form);
    //根据条件取得合计分页信息
    List<ErpBankRecordExt> selectSumByPage(ErpBankRecordForm form);
    //根据条件取得合计件数信息
    int getSumCountByPage(ErpBankRecordForm form);
    //根据条件取得分页信息
    List<ErpBankRecordExt> selectRecordByPage(ErpBankRecordForm form);
    //根据条件取得件数信息
    int getRecordCountByPage(ErpBankRecordForm form);
    //删除全部颜色信息
    void deleteAll();
}