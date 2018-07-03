package net.dlyt.qyds.common.dto.ext;

import net.dlyt.qyds.common.dto.MmbAddress;

/**
 * Created by YiLian on 16/8/2.
 */
public class MmbAddressExt extends MmbAddress {
    // 省
    private String districtidProvinceName;

    // 市
    private String districtidCityName;
    // 区
    private String districtidDistrictName;
    // 街道
    private String districtidStreetName;

    public String getDistrictidProvinceName() {
        return districtidProvinceName;
    }

    public void setDistrictidProvinceName(String districtidProvinceName) {
        this.districtidProvinceName = districtidProvinceName;
    }

    public String getDistrictidCityName() {
        return districtidCityName;
    }

    public void setDistrictidCityName(String districtidCityName) {
        this.districtidCityName = districtidCityName;
    }

    public String getDistrictidDistrictName() {
        return districtidDistrictName;
    }

    public void setDistrictidDistrictName(String districtidDistrictName) {
        this.districtidDistrictName = districtidDistrictName;
    }

    public String getDistrictidStreetName() {
        return districtidStreetName;
    }

    public void setDistrictidStreetName(String districtidStreetName) {
        this.districtidStreetName = districtidStreetName;
    }
}
