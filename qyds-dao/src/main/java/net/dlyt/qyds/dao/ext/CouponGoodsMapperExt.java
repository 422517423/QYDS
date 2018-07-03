package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.CouponGoods;
import net.dlyt.qyds.common.dto.ext.CouponMasterExt;
import net.dlyt.qyds.common.form.CouponGoodsForm;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by cjk on 16/9/4.
 */
@Repository
public interface CouponGoodsMapperExt {
    void deleteByCouponId(CouponGoods form);

    List<CouponGoods> selectByCouponId(CouponGoods form);

    List<CouponGoodsForm> selectGoodsTypeByCouponId(CouponGoods goods);

    List<CouponGoodsForm> selectGoodsBrandByCouponId(CouponGoods goods);

    List<CouponGoodsForm> selectGoodsByCouponId(CouponGoods goods);

    List<CouponGoodsForm> selectSkuByCouponId(CouponGoods goods);

    List<CouponGoodsForm> selectInfoByCouponId(CouponGoods goods);

    //add by zlh
    List<CouponGoods> selectByCouponIdS(String couponId);

    List<Map> selectSKUByYearAndSeason(CouponMasterExt form);

    List<Map> selectSKUByErpBrand(CouponMasterExt form);

    List<Map> selectSKUByLineCode(CouponMasterExt form);
}
