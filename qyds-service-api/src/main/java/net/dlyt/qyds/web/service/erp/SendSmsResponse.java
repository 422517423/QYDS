
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
 *         &lt;element name="SendSmsResult" type="{http://tempuri.org/}SmsResult" minOccurs="0"/>
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
    "sendSmsResult"
})
@XmlRootElement(name = "SendSmsResponse")
public class SendSmsResponse {

    @XmlElement(name = "SendSmsResult")
    protected SmsResult sendSmsResult;

    /**
     * 获取sendSmsResult属性的值。
     * 
     * @return
     *     possible object is
     *     {@link SmsResult }
     *     
     */
    public SmsResult getSendSmsResult() {
        return sendSmsResult;
    }

    /**
     * 设置sendSmsResult属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link SmsResult }
     *     
     */
    public void setSendSmsResult(SmsResult value) {
        this.sendSmsResult = value;
    }

}
