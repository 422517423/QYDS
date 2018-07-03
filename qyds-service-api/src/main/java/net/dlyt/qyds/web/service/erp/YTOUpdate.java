
package net.dlyt.qyds.web.service.erp;

import javax.xml.bind.annotation.*;


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
 *         &lt;element name="Banks" type="{http://tempuri.org/}Banktransfer" minOccurs="0"/>
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
    "yto"
})
@XmlRootElement(name = "YTOUpdate")
public class YTOUpdate {

    @XmlElement(name = "Key")
    protected String key;
    @XmlElement(name = "YTOs")
    protected YTO yto;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public YTO getYto() {
        return yto;
    }

    public void setYto(YTO yto) {
        this.yto = yto;
    }
}
