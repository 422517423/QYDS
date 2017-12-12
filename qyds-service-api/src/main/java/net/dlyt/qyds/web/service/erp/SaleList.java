
package net.dlyt.qyds.web.service.erp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>SaleList complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="SaleList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="orderId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subOrderId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="erpSku" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="price" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="priceDiscount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="deliverTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="expressName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="expressNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="erpStoreId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="storeDeliveryName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="innerBuy" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SaleList", propOrder = {
    "orderId",
    "subOrderId",
    "erpSku",
    "price",
    "priceDiscount",
    "deliverTime",
    "expressName",
    "expressNo",
    "erpStoreId",
    "storeDeliveryName",
    "innerBuy"
})
public class SaleList {

    protected String orderId;
    protected String subOrderId;
    protected String erpSku;
    protected String price;
    protected String priceDiscount;
    protected String deliverTime;
    protected String expressName;
    protected String expressNo;
    protected String erpStoreId;
    protected String storeDeliveryName;
    protected String innerBuy;

    /**
     * 获取orderId属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * 设置orderId属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderId(String value) {
        this.orderId = value;
    }

    /**
     * 获取subOrderId属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubOrderId() {
        return subOrderId;
    }

    /**
     * 设置subOrderId属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubOrderId(String value) {
        this.subOrderId = value;
    }

    /**
     * 获取erpSku属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErpSku() {
        return erpSku;
    }

    /**
     * 设置erpSku属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErpSku(String value) {
        this.erpSku = value;
    }

    /**
     * 获取price属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrice() {
        return price;
    }

    /**
     * 设置price属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrice(String value) {
        this.price = value;
    }

    /**
     * 获取priceDiscount属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPriceDiscount() {
        return priceDiscount;
    }

    /**
     * 设置priceDiscount属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPriceDiscount(String value) {
        this.priceDiscount = value;
    }

    /**
     * 获取deliverTime属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliverTime() {
        return deliverTime;
    }

    /**
     * 设置deliverTime属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliverTime(String value) {
        this.deliverTime = value;
    }

    /**
     * 获取expressName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpressName() {
        return expressName;
    }

    /**
     * 设置expressName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpressName(String value) {
        this.expressName = value;
    }

    /**
     * 获取expressNo属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpressNo() {
        return expressNo;
    }

    /**
     * 设置expressNo属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpressNo(String value) {
        this.expressNo = value;
    }

    /**
     * 获取erpStoreId属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErpStoreId() {
        return erpStoreId;
    }

    /**
     * 设置erpStoreId属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErpStoreId(String value) {
        this.erpStoreId = value;
    }

    /**
     * 获取storeDeliveryName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStoreDeliveryName() {
        return storeDeliveryName;
    }

    /**
     * 设置storeDeliveryName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStoreDeliveryName(String value) {
        this.storeDeliveryName = value;
    }

    /**
     * 获取innerBuy属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInnerBuy() {
        return innerBuy;
    }

    /**
     * 设置innerBuy属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInnerBuy(String value) {
        this.innerBuy = value;
    }

}
