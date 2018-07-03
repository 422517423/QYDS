package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * Created by congky on 2016/8/14.
 */
public interface CatcheRemoveService {

    String sendPost(String url, Map<String, String> params);

    JSONObject removeAll();

    JSONObject removeSome();

    JSONObject removeOnly();

}
