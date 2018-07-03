
package net.dlyt.qyds.web.service.erp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>BaseDate complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="BaseDate">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Vip" type="{http://tempuri.org/}Vip" minOccurs="0"/>
 *         &lt;element name="VipPoint" type="{http://tempuri.org/}VipPoint" minOccurs="0"/>
 *         &lt;element name="Goods" type="{http://tempuri.org/}Goods" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseDate", propOrder = {
    "vip",
    "vipPoint",
    "goods"
})
public class BaseDate {

    @XmlElement(name = "Vip")
    protected Vip vip;
    @XmlElement(name = "VipPoint")
    protected VipPoint vipPoint;
    @XmlElement(name = "Goods")
    protected Goods goods;

    /**
     * 获取vip属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Vip }
     *     
     */
    public Vip getVip() {
        return vip;
    }

    /**
     * 设置vip属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Vip }
     *     
     */
    public void setVip(Vip value) {
        this.vip = value;
    }

    /**
     * 获取vipPoint属性的值。
     * 
     * @return
     *     possible object is
     *     {@link VipPoint }
     *     
     */
    public VipPoint getVipPoint() {
        return vipPoint;
    }

    /**
     * 设置vipPoint属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link VipPoint }
     *     
     */
    public void setVipPoint(VipPoint value) {
        this.vipPoint = value;
    }

    /**
     * 获取goods属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Goods }
     *     
     */
    public Goods getGoods() {
        return goods;
    }

    /**
     * 设置goods属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Goods }
     *     
     */
    public void setGoods(Goods value) {
        this.goods = value;
    }

}
