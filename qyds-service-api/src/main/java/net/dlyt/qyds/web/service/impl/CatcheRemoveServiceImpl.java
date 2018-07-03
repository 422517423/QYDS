package net.dlyt.qyds.web.service.impl;


import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.web.service.CatcheRemoveService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by congky on 2016/8/14.
 */
@Service("catcheRemoveService")
public class CatcheRemoveServiceImpl implements CatcheRemoveService {

    @Caching(evict = { @CacheEvict(value="${goods_list_catch_name}",allEntries=true), @CacheEvict(value="${new_goods_list_catch_name}",allEntries=true)
            , @CacheEvict(value="${activity_goods_list_catch_name}",allEntries=true) , @CacheEvict(value="${goods_second_main_catch_name}",allEntries=true)
            , @CacheEvict(value="${cms_items_catch_name}",allEntries=true)})
    public JSONObject removeAll(){
        return null;
    }

    @Caching(evict = { @CacheEvict(value="${goods_list_catch_name}",allEntries=true),@CacheEvict(value="${new_goods_list_catch_name}",allEntries=true), @CacheEvict(value="${goods_second_main_catch_name}",allEntries=true)
            , @CacheEvict(value="${cms_items_catch_name}",allEntries=true)})
    public JSONObject removeSome(){
        return null;
    }

    @Caching(evict = {  @CacheEvict(value="${activity_goods_list_catch_name}",allEntries=true) })
    public JSONObject removeOnly(){
        return null;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     * @param url 发送请求的 URL
     * @param params 请求的参数集合
     * @return 远程资源的响应结果
     */
    public String sendPost(String url, Map<String, String> params) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn =(HttpURLConnection) realUrl.openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // POST方法
            conn.setRequestMethod("POST");
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("kdniao-nocache", "true");
            conn.connect();
            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            // 发送请求参数
            if (params != null) {
                StringBuilder param = new StringBuilder();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if(param.length()>0){
                        param.append("&");
                    }
                    param.append(entry.getKey());
                    param.append("=");
                    param.append(entry.getValue());
                    //System.out.println(entry.getKey()+":"+entry.getValue());
                }
                //System.out.println("param:"+param.toString());
                out.write(param.toString());
            }
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result.toString();
    }

}
