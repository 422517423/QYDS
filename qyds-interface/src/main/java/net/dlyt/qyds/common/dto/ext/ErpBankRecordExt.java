package net.dlyt.qyds.common.dto.ext;


import net.dlyt.qyds.common.dto.ErpBankRecord;

/**
 * Created by zlh on 2016/7/26.
 */
public class ErpBankRecordExt extends ErpBankRecord {

    //商品名称
    private String goodsNameCn;
    //颜色代码
    private String colorCodee;
    //颜色名称
    private String colorName;
    //尺码种类代码
    private String sizeTypeCode;
    //尺码种类名称
    private String sizeTypeName;
    //尺码排序
    private int sizeSort;
    //尺码代码
    private String sizeCode;
    //尺码名称
    private String sizeName;
    //门店名称
    private String storeNameCn;

    public String getGoodsNameCn() {
        return goodsNameCn;
    }

    public void setGoodsNameCn(String goodsNameCn) {
        this.goodsNameCn = goodsNameCn;
    }

    public String getColorCodee() {
        return colorCodee;
    }

    public void setColorCodee(String colorCodee) {
        this.colorCodee = colorCodee;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getSizeTypeCode() {
        return sizeTypeCode;
    }

    public void setSizeTypeCode(String sizeTypeCode) {
        this.sizeTypeCode = sizeTypeCode;
    }

    public String getSizeTypeName() {
        return sizeTypeName;
    }

    public void setSizeTypeName(String sizeTypeName) {
        this.sizeTypeName = sizeTypeName;
    }

    public int getSizeSort() {
        return sizeSort;
    }

    public void setSizeSort(int sizeSort) {
        this.sizeSort = sizeSort;
    }

    public String getSizeCode() {
        return sizeCode;
    }

    public void setSizeCode(String sizeCode) {
        this.sizeCode = sizeCode;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public String getStoreNameCn() {
        return storeNameCn;
    }

    public void setStoreNameCn(String storeNameCn) {
        this.storeNameCn = storeNameCn;
    }
}