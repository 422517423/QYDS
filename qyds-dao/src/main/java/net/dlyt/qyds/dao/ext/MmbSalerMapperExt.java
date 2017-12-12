package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.MmbSaler;
import net.dlyt.qyds.common.dto.ext.MmbSalerExt;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ZLH on 2016/12/16.
 */
@Repository
public interface MmbSalerMapperExt {

    // 获取会员列表
    List<MmbSalerExt> selectAll(MmbSalerExt ext);

    int getAllDataCount(MmbSalerExt ext);

    MmbSalerExt selectBySelective(MmbSaler sysUser);

    MmbSalerExt getUserOrgInfo(String telphone);

    int updateByCode(MmbSaler record);

    int deleteByCode(String code);

    int getCountBySelective(MmbSaler ext);

    // 根据手机号获取会员列表
    List<MmbSalerExt> selectAllByPhone(MmbSalerExt ext);

    int getAllDataCountByPhone(MmbSalerExt ext);

    List<MmbSalerExt> selectAllByTel(MmbSalerExt ext);

    int deleteBySelective(MmbSaler mmbSaler);

    int clearOldphoneById(String memberId);

    // 获取会员对账单列表
    List<MmbSalerExt> selectReport();

    int getCountByPhone(String tel);
}
