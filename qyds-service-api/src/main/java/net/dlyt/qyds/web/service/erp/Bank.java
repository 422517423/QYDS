
package net.dlyt.qyds.web.service.erp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Bank complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="Bank">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SendStoreId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TakeStoreId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BankTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SalerID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="erpSku" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="quantity" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="StoreKind" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Bank", propOrder = {
    "sendStoreId",
    "takeStoreId",
    "bankTime",
    "salerID",
    "erpSku",
    "quantity",
    "storeKind"
})
public class Bank {

    @XmlElement(name = "SendStoreId")
    protected String sendStoreId;
    @XmlElement(name = "TakeStoreId")
    protected String takeStoreId;
    @XmlElement(name = "BankTime")
    protected String bankTime;
    @XmlElement(name = "SalerID")
    protected String salerID;
    protected String erpSku;
    protected String quantity;
    @XmlElement(name = "StoreKind")
    protected String storeKind;

    /**
     * 获取sendStoreId属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSendStoreId() {
        return sendStoreId;
    }

    /**
     * 设置sendStoreId属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSendStoreId(String value) {
        this.sendStoreId = value;
    }

    /**
     * 获取takeStoreId属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTakeStoreId() {
        return takeStoreId;
    }

    /**
     * 设置takeStoreId属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTakeStoreId(String value) {
        this.takeStoreId = value;
    }

    /**
     * 获取bankTime属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBankTime() {
        return bankTime;
    }

    /**
     * 设置bankTime属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBankTime(String value) {
        this.bankTime = value;
    }

    /**
     * 获取salerID属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSalerID() {
        return salerID;
    }

    /**
     * 设置salerID属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSalerID(String value) {
        this.salerID = value;
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
     * 获取quantity属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQuantity() {
        return quantity;
    }

    /**
     * 设置quantity属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQuantity(String value) {
        this.quantity = value;
    }

    /**
     * 获取storeKind属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStoreKind() {
        return storeKind;
    }

    /**
     * 设置storeKind属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStoreKind(String value) {
        this.storeKind = value;
    }

}
