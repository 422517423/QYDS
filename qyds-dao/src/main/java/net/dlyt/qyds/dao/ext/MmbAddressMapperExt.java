package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.MmbAddress;
import net.dlyt.qyds.common.dto.ext.MmbAddressExt;
import net.dlyt.qyds.common.form.MmbAddressForm;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by YiLian on 16/8/2.
 */
@Repository
public interface MmbAddressMapperExt {

    /**
     * 会员送货地址一览(一次性全部取得,不分页)
     *
     * @param form memberId:会员ID
     * @return
     */
    List<MmbAddressExt> queryList(MmbAddressForm form);

    /**
     * 取得会员默认送货地址
     *
     * isDefault为0的为默认地址
     * 如果上述条件不满足取排序第一条作为默认地址
     *
     * @param form memberId:会员ID
     * @return
     */
    List<MmbAddressExt> selectDefaultAddress(MmbAddressForm form);

    /**
     * 根据会员ID获取地址信息
     * @return
     */
    List<MmbAddress> selectByMemberId(MmbAddress mmbAddress);


    /**
     * 取得会员送货地址详细
     *
     * @param form memberId:会员ID
     * @return
     */
    MmbAddressExt selectAddressByPK(MmbAddressForm form);
}
