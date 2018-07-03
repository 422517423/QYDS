package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.ShpOrg;
import net.dlyt.qyds.common.form.GdsMasterForm;
import net.dlyt.qyds.common.form.SkuForm;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by cjk on 16/8/4.
 */
@Repository
public interface SkuMapperExt {
    //获得全部组织
    List<SkuForm> select(SkuForm form);

    int selectCount(SkuForm form);

    SkuForm selectBySkuId(String skuId);

    float getSuitPrice(String s);

    List<SkuForm> getSkuByIds(String s);

    List<SkuForm> selectSkuColor(SkuForm form);

    int selectSkuColorCount(SkuForm form);

    List<SkuForm> getSkuColorsByGoodsCodeAndColorCode(String s);
}
