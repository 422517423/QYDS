package net.dlyt.qyds.common.dto.ext;

import net.dlyt.qyds.common.dto.GdsMasterExt;
import net.dlyt.qyds.common.dto.MmbCollection;

/**
 * Created by YiLian on 16/8/2.
 */
public class MmbCollectionExt extends MmbCollection {

    // 商品信息
    private GdsMasterExt gdsMasterExt;

    /**
     * 库存足够:0-足够;1-库存紧张
     */
    private String hasEnough;

    public String getHasEnough() {
        return hasEnough;
    }

    public void setHasEnough(String hasEnough) {
        this.hasEnough = hasEnough;
    }

    public GdsMasterExt getGdsMasterExt() {
        return gdsMasterExt;
    }

    public void setGdsMasterExt(GdsMasterExt gdsMasterExt) {
        this.gdsMasterExt = gdsMasterExt;
    }
}
