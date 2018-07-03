
package net.dlyt.qyds.web.service.erp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Coupon complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="Coupon">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CouponIssue" type="{http://tempuri.org/}CouponIssue" minOccurs="0"/>
 *         &lt;element name="CouponSku" type="{http://tempuri.org/}ArrayOfCouponSku" minOccurs="0"/>
 *         &lt;element name="CouponSend" type="{http://tempuri.org/}CouponSend" minOccurs="0"/>
 *         &lt;element name="CouponUsed" type="{http://tempuri.org/}CouponUsed" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Coupon", propOrder = {
    "couponIssue",
    "couponSku",
    "couponSend",
    "couponUsed"
})
public class Coupon {

    @XmlElement(name = "CouponIssue")
    protected CouponIssue couponIssue;
    @XmlElement(name = "CouponSku")
    protected ArrayOfCouponSku couponSku;
    @XmlElement(name = "CouponSend")
    protected CouponSend couponSend;
    @XmlElement(name = "CouponUsed")
    protected CouponUsed couponUsed;

    /**
     * 获取couponIssue属性的值。
     * 
     * @return
     *     possible object is
     *     {@link CouponIssue }
     *     
     */
    public CouponIssue getCouponIssue() {
        return couponIssue;
    }

    /**
     * 设置couponIssue属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link CouponIssue }
     *     
     */
    public void setCouponIssue(CouponIssue value) {
        this.couponIssue = value;
    }

    /**
     * 获取couponSku属性的值。
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfCouponSku }
     *     
     */
    public ArrayOfCouponSku getCouponSku() {
        return couponSku;
    }

    /**
     * 设置couponSku属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfCouponSku }
     *     
     */
    public void setCouponSku(ArrayOfCouponSku value) {
        this.couponSku = value;
    }

    /**
     * 获取couponSend属性的值。
     * 
     * @return
     *     possible object is
     *     {@link CouponSend }
     *     
     */
    public CouponSend getCouponSend() {
        return couponSend;
    }

    /**
     * 设置couponSend属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link CouponSend }
     *     
     */
    public void setCouponSend(CouponSend value) {
        this.couponSend = value;
    }

    /**
     * 获取couponUsed属性的值。
     * 
     * @return
     *     possible object is
     *     {@link CouponUsed }
     *     
     */
    public CouponUsed getCouponUsed() {
        return couponUsed;
    }

    /**
     * 设置couponUsed属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link CouponUsed }
     *     
     */
    public void setCouponUsed(CouponUsed value) {
        this.couponUsed = value;
    }

}
