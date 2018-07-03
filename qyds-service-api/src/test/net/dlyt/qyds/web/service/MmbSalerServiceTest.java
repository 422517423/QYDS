package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class) //表示继承了SpringJUnit4ClassRunner类
@ContextConfiguration(locations = {"classpath:spring-service.xml","classpath:service-cache.xml","classpath:weixin-beans.xml"})//放的是spring的配置文件
public class MmbSalerServiceTest {
    @Resource
    private MmbSalerService mmbSalerService;

    @Test
    public void makeQrCodeForAllSaler() {
        JSONObject jsonObject = mmbSalerService.makeQrCodeForAllSaler();
        System.out.println(jsonObject);
    }
}