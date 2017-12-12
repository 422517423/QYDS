package net.dlyt.qyds.common.dto;
import java.io.Serializable;
import java.util.List;

/**
 * Created by cky on 2016/8/8.
 */
public class ViewOnsellSku  implements Serializable {


     //SKUID
    private String skuid;

    //name key value:颜色 520 黑
    private List<SkuNameKeyValue> nameKeyValues;

    //图片信息
    private List<String> imgs;

    //市场价格
    private String price;

    //优惠价格
    private String activityPrice;

    //实际库存
    private String newCount;

    //活动名称
    private  String activityName;

    //活动类型
    private  String activityType;

    public String getSkuid() {
        return skuid;
    }

    public void setSkuid(String skuid) {
        this.skuid = skuid;
    }

    public List<SkuNameKeyValue> getNameKeyValues() {
        return nameKeyValues;
    }

    public void setNameKeyValues(List<SkuNameKeyValue> nameKeyValues) {
        this.nameKeyValues = nameKeyValues;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNewCount() {
        return newCount;
    }

    public void setNewCount(String newCount) {
        this.newCount = newCount;
    }

    public String getActivityPrice() {
        return activityPrice;
    }

    public void setActivityPrice(String activityPrice) {
        this.activityPrice = activityPrice;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }
}
