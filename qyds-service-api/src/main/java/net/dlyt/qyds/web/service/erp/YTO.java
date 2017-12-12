
package net.dlyt.qyds.web.service.erp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Banktransfer complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="Banktransfer">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Bank" type="{http://tempuri.org/}Bank" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "YTO", propOrder = {
    "ytoPushInfo"
})
public class YTO {
    @XmlElement(name = "YTOStates")
    protected YTOStates ytoPushInfo;


    public YTOStates getYtoPushInfo() {
        return ytoPushInfo;
    }

    public void setYtoPushInfo(YTOStates ytoPushInfo) {
        this.ytoPushInfo = ytoPushInfo;
    }
}
