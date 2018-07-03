
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
 *         &lt;element name="Sms" type="{http://tempuri.org/}SmsSend" minOccurs="0"/>
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
    "sms"
})
@XmlRootElement(name = "SendSms")
public class SendSms {

    @XmlElement(name = "Sms")
    protected SmsSend sms;

    /**
     * 获取sms属性的值。
     * 
     * @return
     *     possible object is
     *     {@link SmsSend }
     *     
     */
    public SmsSend getSms() {
        return sms;
    }

    /**
     * 设置sms属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link SmsSend }
     *     
     */
    public void setSms(SmsSend value) {
        this.sms = value;
    }

}
