import net.dlyt.qyds.web.service.erp.BaseDate;
import net.dlyt.qyds.web.service.erp.Service;
import net.dlyt.qyds.web.service.erp.ServiceSoap;
import net.dlyt.qyds.web.service.erp.Vip;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by dkzhang on 16/8/12.
 */
public class Test {


    public static void main(String[] args) throws ParseException {

//        Order order = new Order();
//        order.setSaleMaster(new SaleMaster());
//        List<SaleBean> saleList= new ArrayList<SaleBean>();
//        saleList.add(new SaleBean());
//        saleList.add(new SaleBean());
//        order.setSaleList(saleList);
//
//        String str = JaxbUtil.convertToXml(order);
//        System.out.println(str);


//        Service service = new Service();
//        ServiceSoap soap = service.getServiceSoap();
//
//        BaseDate date = new BaseDate();
//        Vip vip = new Vip();
////        data.Vip.memberCode = "13624083451";
//        vip.setMemberCode("13724083451");
////        data.Vip.memberName = "鲍云威";
//        vip.setMemberName("鲍云威");
////        data.Vip.sexName = "男";
//        vip.setSexName("男");
////        data.Vip.mobil = "13624083452";
//        vip.setMobil("13724083452");
////        data.Vip.birthday = "1979-11-02";
//        vip.setBirthday("1979-11-02");
////        data.Vip.provinceName = "辽宁省";
//        vip.setProvinceName("辽宁省");
////        data.Vip.cityName = "大连市";
//        vip.setCityName("大连市");
////        data.Vip.districtName = "中山区";
//        vip.setDistrictName("中山区");
////        data.Vip.email = "48947612@qq.com";
//        vip.setEmail("48947612@qq.com");
////        data.Vip.storeName = "总部";
//        vip.setStoreName("总部");
////        data.Vip.sellerName = "SYSUser";
//        vip.setSellerName("SYSUser");
////        data.Vip.address = "人民路35号1701";
//        vip.setAddress("人民路35号1701");
////        data.Vip.postCode = "116001";
//        vip.setPostCode("116001");
////        data.Vip.income = "5K-8K";
//        vip.setIncome("5K-8K");
////        data.Vip.registTime = "2016-01-01";
//        vip.setRegistTime("2016-01-01");
////        data.Vip.memberGrade = "普通会员";
//        vip.setMemberGrade("普通会员");
//
//        date.setVip(vip);
//        String respString = soap.vipUpdate(date);
//
//        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date d1=sdf.parse("2016-09-06 10:10:10");
        System.out.println(formatTimeStampToYMDHM(new Date()));

    }

    public static int daysToNow(Date smdate) throws ParseException
    {
//        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
//        smdate=sdf.parse(sdf.format(smdate));
        Date bdate=new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);

        return Integer.parseInt(String.valueOf(between_days));
    }


    /**
     * 时间戳转化成年月日时分秒形式
     * @param dt
     * @return
     */
    public static String formatTimeStampToYMDHM(Date dt) {
        String rs = "";
        if (!StringUtils.isEmpty(dt)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            rs = sdf.format(dt);
        }
        return rs;
    }

}
