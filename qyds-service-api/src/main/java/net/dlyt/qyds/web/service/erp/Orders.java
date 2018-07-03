
package net.dlyt.qyds.web.service.erp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Orders complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="Orders">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SaleMaster" type="{http://tempuri.org/}SaleMaster" minOccurs="0"/>
 *         &lt;element name="SaleList" type="{http://tempuri.org/}ArrayOfSaleList" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Orders", propOrder = {
    "saleMaster",
    "saleList"
})
public class Orders {

    @XmlElement(name = "SaleMaster")
    protected SaleMaster saleMaster;
    @XmlElement(name = "SaleList")
    protected ArrayOfSaleList saleList;

    /**
     * 获取saleMaster属性的值。
     * 
     * @return
     *     possible object is
     *     {@link SaleMaster }
     *     
     */
    public SaleMaster getSaleMaster() {
        return saleMaster;
    }

    /**
     * 设置saleMaster属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link SaleMaster }
     *     
     */
    public void setSaleMaster(SaleMaster value) {
        this.saleMaster = value;
    }

    /**
     * 获取saleList属性的值。
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfSaleList }
     *     
     */
    public ArrayOfSaleList getSaleList() {
        return saleList;
    }

    /**
     * 设置saleList属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfSaleList }
     *     
     */
    public void setSaleList(ArrayOfSaleList value) {
        this.saleList = value;
    }

}
