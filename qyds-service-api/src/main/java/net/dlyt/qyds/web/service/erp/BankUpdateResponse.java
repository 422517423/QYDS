
package net.dlyt.qyds.web.service.erp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type的 Java 类。
 * <p>
 * <p>以下模式片段指定包含在此类中的预期内容。
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BankUpdateResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "bankUpdateResult"
})
@XmlRootElement(name = "BankUpdateResponse")
public class BankUpdateResponse {

    @XmlElement(name = "BankUpdateResult")
    protected String bankUpdateResult;

    /**
     * 获取bankUpdateResult属性的值。
     *
     * @return possible object is
     * {@link String }
     */
    public String getBankUpdateResult() {
        return bankUpdateResult;
    }

    /**
     * 设置bankUpdateResult属性的值。
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setBankUpdateResult(String value) {
        this.bankUpdateResult = value;
    }

}
