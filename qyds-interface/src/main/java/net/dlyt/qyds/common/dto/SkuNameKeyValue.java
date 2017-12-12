package net.dlyt.qyds.common.dto;
import java.io.Serializable;
import java.util.List;

/**
 * Created by cky on 2016/8/8.
 */
public class SkuNameKeyValue implements Serializable {


    //颜色
    private String name;

    //520
    private String key;

    //黑色
    private String value;

    private String bnk_less_limit;
    private String bnk_no_limit;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getBnk_less_limit() {
        return bnk_less_limit;
    }

    public void setBnk_less_limit(String bnk_less_limit) {
        this.bnk_less_limit = bnk_less_limit;
    }

    public String getBnk_no_limit() {
        return bnk_no_limit;
    }

    public void setBnk_no_limit(String bnk_no_limit) {
        this.bnk_no_limit = bnk_no_limit;
    }
}
