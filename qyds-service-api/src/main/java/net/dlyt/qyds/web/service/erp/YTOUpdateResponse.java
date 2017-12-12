
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
 *         &lt;element name="YTOUpdateResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "YTOUpdateResult"
})
@XmlRootElement(name = "YTOUpdateResponse")
public class YTOUpdateResponse {

    @XmlElement(name = "YTOUpdateResult")
    protected String YTOUpdateResult;

    public String getYTOUpdateResult() {
        return YTOUpdateResult;
    }

    public void setYTOUpdateResult(String YTOUpdateResult) {
        this.YTOUpdateResult = YTOUpdateResult;
    }
}
