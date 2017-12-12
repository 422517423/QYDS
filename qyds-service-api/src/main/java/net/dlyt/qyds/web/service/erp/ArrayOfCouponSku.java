
package net.dlyt.qyds.web.service.erp;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ArrayOfCouponSku complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="ArrayOfCouponSku">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CouponSku" type="{http://tempuri.org/}CouponSku" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfCouponSku", propOrder = {
    "couponSku"
})
public class ArrayOfCouponSku {

    @XmlElement(name = "CouponSku", nillable = true)
    protected List<CouponSku> couponSku;

    /**
     * Gets the value of the couponSku property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the couponSku property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCouponSku().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CouponSku }
     * 
     * 
     */
    public List<CouponSku> getCouponSku() {
        if (couponSku == null) {
            couponSku = new ArrayList<CouponSku>();
        }
        return this.couponSku;
    }

    public void setCouponSku(List<CouponSku> couponSku) {
        this.couponSku = couponSku;
    }
}
