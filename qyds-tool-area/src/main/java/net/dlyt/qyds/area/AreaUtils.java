package net.dlyt.qyds.area;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import net.dlyt.qyds.common.dto.ComDistrict;

import javax.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.dlyt.qyds.dao.ComDistrictMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component("areaUtils")
public class AreaUtils {
    Logger log = Logger.getLogger(AreaUtils.class);
    private final static String GET_PROVINCE_URI = "http://easybuy.jd.com//address/getProvinces.action";
    private final static String GET_CITY_URI = "http://easybuy.jd.com//address/getCitys.action";
    private final static String GET_COUNTY_URI = "http://easybuy.jd.com//address/getCountys.action";
    private final static String GET_TOWN_URI = "http://easybuy.jd.com//address/getTowns.action";


    @Autowired
    private ComDistrictMapper comDistrictMapper;


    public List<ComDistrict> getProviences() throws Exception{

        MultivaluedMap params = new MultivaluedMapImpl();
        String cityJson=JerseyUtils.post(GET_PROVINCE_URI, params);
        return getAddress(cityJson, "0", "1");
    }

    public List<ComDistrict> getCities(String parentId) throws Exception{

        MultivaluedMap params = new MultivaluedMapImpl();
        params.add("provinceId", parentId);
        String cityJson=JerseyUtils.post(GET_CITY_URI, params);
        return getAddress(cityJson, parentId, "2");
    }

    public List<ComDistrict> getCounties(String parentId) throws Exception{

        MultivaluedMap params = new MultivaluedMapImpl();
        params.add("cityId", parentId);
        String cityJson=JerseyUtils.post(GET_COUNTY_URI, params);
        return getAddress(cityJson, parentId, "3");
    }

    public List<ComDistrict> getTowns(String parentId) throws Exception{

        MultivaluedMap params = new MultivaluedMapImpl();
        params.add("countyId", parentId);
        String cityJson=JerseyUtils.post(GET_TOWN_URI, params);
        return getAddress(cityJson, parentId, "4");
    }


    private List<ComDistrict> getAddress(String json, String parentId, String type){
        JSONObject cities = JSON.parseObject(json);
        List<ComDistrict> arrayCities = new ArrayList<ComDistrict>();

        if(cities == null){
            return arrayCities;
        }
        for(String key : cities.keySet()){
            ComDistrict city = new ComDistrict();
            city.setDistrictId(key);
            city.setDistrictName(cities.getString(key));
            city.setDistrictFullName(cities.getString(key));
            city.setParentId(parentId);
            city.setDeleted("0");
            city.setType(type);
            city.setSort(Integer.valueOf(key));
            Date now = new Date();
            city.setInsertTime(now);
            city.setUpdateTime(now);
            city.setInsertUserId("000000");
            city.setUpdateUserId("000000");
            comDistrictMapper.insert(city);

            arrayCities.add(city);
        }
        if(arrayCities.size() > 0) {
            log.debug(JSONArray.toJSONString(arrayCities));
        }
        return arrayCities;
    }


    public void doGetAddresses() {
        System.out.println("I am called by MethodInvokingJobDetailFactoryBean using SimpleTriggerFactoryBean");

        try {

            List<ComDistrict> provinces = getProviences();
//            List<ComDistrict> provinces = new ArrayList<ComDistrict>();
//            ComDistrict p1 = new ComDistrict();
//            p1.setDistrictId("8");
//            provinces.add(p1);
            for (ComDistrict p : provinces) {


                List<ComDistrict> cities = getCities(p.getDistrictId());
                for (ComDistrict c : cities) {
                    List<ComDistrict> countys = getCounties(c.getDistrictId());
                    for (ComDistrict c2 : countys) {
                        List<ComDistrict> townws = getTowns(c2.getDistrictId());
                        Thread.sleep(10);
                        townws = null;
                    }
                    countys = null;
                }
                cities = null;
            }

//            List<City> cities = getCities("1310");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
