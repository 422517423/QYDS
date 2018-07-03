package me.chanjar.weixin.mp.bean.result;

import java.io.Serializable;
import java.util.List;

import me.chanjar.weixin.mp.util.json.WxMpGsonBuilder;

public class WxMpUserBatchGetResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<WxMpUser> userInfoList = null;

	public List<WxMpUser> getUserInfoList() {
		return userInfoList;
	}

	public void setUserInfoList(List<WxMpUser> userInfoList) {
		this.userInfoList = userInfoList;
	}

	public static WxMpUserBatchGetResult fromJson(String json) {
		return WxMpGsonBuilder.INSTANCE.create().fromJson(json,
				WxMpUserBatchGetResult.class);
	}
}
