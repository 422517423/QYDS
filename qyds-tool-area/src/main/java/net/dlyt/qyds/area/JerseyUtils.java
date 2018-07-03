package net.dlyt.qyds.area;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

/**
 * Created by dkzhang on 16/7/18.
 */
public class JerseyUtils {


    public static String post(String url , MultivaluedMap params){
        Client client = Client.create();
        WebResource resource = client.resource(url);
        String result = resource.header("Accept-Language","zh-CN,zh;q=0.8,en;q=0.6")
                .header("content-length","12")
                .header("X-Requested-With", "XMLHttpRequest")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36")
                .header("Accept","application/json, text/javascript, */*; q=0.01")
                .header("Content-Type","application/x-www-form-urlencoded")
                .header("Cookie", "__jdv=122270672|direct|-|none|-; _tp=Ex1QCFTBUJzpOcHApaXS%2BA%3D%3D; unick=dkzhang; _pst=gamlty; TrackID=1ykNG89UW8yLSnk6_2Xv-l_8w7TmrMWbJcWFOv1ijc5WYG9fPfrlPQRinylVht9LDMOSUDo1BOCyuoH9_OytfMie618xaABX_nI54970ioGtjLVdOeEJLcjQBjv0F-dDP; pinId=XAdMmjAY1iU; ceshi3.com=8S9Jyz_WybLOr-phmyrvzS4JNF8CZaAS5DKLAOzRLx0; pin=gamlty; thor=6C551E41AE9AAE114026AF6C77F354A65B7FBD78902F804C4DFC96CE544DED6784B9B3E6F98449AAD23E390C131635DD10BE1D4BC12E3B4F15C5CFF41D552584B0ED6A9904876CB72E9309398426E17F516C805963C2139BD62491461AF0A8DB7F1F3C26534E2CA63AC2F0B7D6A5AA7EDB3B86CD9443E72079F3E3D25571C660; __jda=122270672.68992345.1467961246.1468822925.1468825557.4; __jdb=122270672.4.68992345|4.1468825557; __jdc=122270672; __jdu=68992345; JSESSIONID=1C1CAF69C2773B132C581BC1DF817726.s1")
                .type(MediaType.APPLICATION_FORM_URLENCODED).post(String.class, params);
//        System.out.println(result);
        return result;
    }


    public static String get(String url){
        Client client = Client.create();
        WebResource resource = client.resource(url);
        String result = resource.header("Accept-Language","zh-CN,zh;q=0.8,en;q=0.6")
                .header("content-length","12")
                .header("X-Requested-With", "XMLHttpRequest")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36")
                .header("Accept","application/json, text/javascript, */*; q=0.01")
                .header("Content-Type","application/x-www-form-urlencoded")
                .header("Cookie", "__jdv=122270672|direct|-|none|-; _tp=Ex1QCFTBUJzpOcHApaXS%2BA%3D%3D; unick=dkzhang; _pst=gamlty; TrackID=1ykNG89UW8yLSnk6_2Xv-l_8w7TmrMWbJcWFOv1ijc5WYG9fPfrlPQRinylVht9LDMOSUDo1BOCyuoH9_OytfMie618xaABX_nI54970ioGtjLVdOeEJLcjQBjv0F-dDP; pinId=XAdMmjAY1iU; ceshi3.com=8S9Jyz_WybLOr-phmyrvzS4JNF8CZaAS5DKLAOzRLx0; pin=gamlty; thor=6C551E41AE9AAE114026AF6C77F354A65B7FBD78902F804C4DFC96CE544DED6784B9B3E6F98449AAD23E390C131635DD10BE1D4BC12E3B4F15C5CFF41D552584B0ED6A9904876CB72E9309398426E17F516C805963C2139BD62491461AF0A8DB7F1F3C26534E2CA63AC2F0B7D6A5AA7EDB3B86CD9443E72079F3E3D25571C660; __jda=122270672.68992345.1467961246.1468822925.1468825557.4; __jdb=122270672.4.68992345|4.1468825557; __jdc=122270672; __jdu=68992345; JSESSIONID=1C1CAF69C2773B132C581BC1DF817726.s1")
                .accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_FORM_URLENCODED).get(String.class);
//        System.out.println(result);
        return result;
    }
}
