package net.dlyt.qyds.common.dto.ext;

import net.dlyt.qyds.common.dto.ErpStore;

/**
 * Created by zlh on 2016/7/28.
 */
public class ErpStoreExt extends ErpStore {

    // 更新方式,10新增,20修改,30删除
    private String style;

    private  String sku;

    // 20180103 扩展字段
    // 发货门店地址
    private String shopProvince;
    // 发货门店地址
    private String shopCity;
    // 发货门店区域
    private String shopDistrict;
    // 发货门店地址
    private String shopAddress;

    public String getShopProvince() {
        return shopProvince;
    }

    public void setShopProvince(String shopProvince) {
        this.shopProvince = shopProvince;
    }

    public String getShopCity() {
        return shopCity;
    }

    public void setShopCity(String shopCity) {
        this.shopCity = shopCity;
    }

    public String getShopDistrict() {
        return shopDistrict;
    }

    public void setShopDistrict(String shopDistrict) {
        this.shopDistrict = shopDistrict;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }
    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
}
