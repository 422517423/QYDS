package net.dlyt.qyds.common.form;

import net.dlyt.qyds.common.dto.OrdList;
import net.dlyt.qyds.common.dto.OrdMaster;

import java.util.List;

/**
 * Created by wenxuechao on 16/8/7.
 */
public class OrdSubmitForm extends OrdMaster {

    private String ordListJson;

    private String couponMemberId;

    private String newmemberId;

    private List<OrdList> ordList;

    private String addressId = null;

    //门店自提的时候联系人的电话和姓名
    private String cname;
    private String ctel;

    private String selectedCoupons;

    public String getSelectedCoupons() {
        return selectedCoupons;
    }

    public void setSelectedCoupons(String selectedCoupons) {
        this.selectedCoupons = selectedCoupons;
    }

    public List<OrdList> getOrdList() {
        return ordList;
    }

    public void setOrdList(List<OrdList> ordList) {
        this.ordList = ordList;
    }

    public String getOrdListJson() {
        return ordListJson;
    }

    public void setOrdListJson(String ordListJson) {
        this.ordListJson = ordListJson;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getCouponMemberId() {
        return couponMemberId;
    }

    public void setCouponMemberId(String couponMemberId) {
        this.couponMemberId = couponMemberId;
    }

    public String getNewmemberId() {
        return newmemberId;
    }

    public void setNewmemberId(String newmemberId) {
        this.newmemberId = newmemberId;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCtel() {
        return ctel;
    }

    public void setCtel(String ctel) {
        this.ctel = ctel;
    }
}
