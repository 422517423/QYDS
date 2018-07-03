package net.dlyt.qyds.web.service;

import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.ComCode;
import net.dlyt.qyds.common.dto.ComCodeExt;
import net.dlyt.qyds.common.dto.ComCodeKey;

import java.util.List;

/**
 * Created by wy on 2016/7/14.
 */
public interface ComCodeService {

    /*获取用户列表*/
    JSONObject selectAll();

    /* 获取码表信息*/
    JSONObject edit(String code, String value);

    /*保存*/
    JSONObject save(String data, String type);

    /*删除码表信息*/
    JSONObject delete(String data);

    /* 获取码表信息*/
    //valueString:'10','20','30'
    JSONObject selectByValueString(String code, String valueString);

}
