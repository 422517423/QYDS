
package net.dlyt.qyds.web.service.erp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Key" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VipPoint" type="{http://tempuri.org/}BaseDate" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "key",
    "vipPoint"
})
@XmlRootElement(name = "VIPUsedUpdate")
public class VIPUsedUpdate {

    @XmlElement(name = "Key")
    protected String key;
    @XmlElement(name = "VipPoint")
    protected BaseDate vipPoint;

    /**
     * 获取key属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKey() {
        return key;
    }

    /**
     * 设置key属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKey(String value) {
        this.key = value;
    }

    /**
     * 获取vipPoint属性的值。
     * 
     * @return
     *     possible object is
     *     {@link BaseDate }
     *     
     */
    public BaseDate getVipPoint() {
        return vipPoint;
    }

    /**
     * 设置vipPoint属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link BaseDate }
     *     
     */
    public void setVipPoint(BaseDate value) {
        this.vipPoint = value;
    }

}
