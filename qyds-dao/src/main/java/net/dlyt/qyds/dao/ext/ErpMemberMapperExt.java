package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.ErpMember;
import net.dlyt.qyds.common.form.ErpMemberForm;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zlh on 2016/7/29.
 */
@Repository
public interface ErpMemberMapperExt {
    //取得全部信息
    List<ErpMember> selectAll();
    //根据条件取得分页信息
    List<ErpMember> selectByPage(ErpMemberForm record);
    //根据条件取得总件数
    int getCountByPage(ErpMemberForm record);
    //删除全部信息
    void deleteAll();
    //无时间更新
    int updateNoTime(ErpMember record);
    //取得全部信息
    List<ErpMember> selectSaler();
}