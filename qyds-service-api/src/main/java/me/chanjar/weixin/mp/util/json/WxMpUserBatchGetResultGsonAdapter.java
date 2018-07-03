/*
 * KINGSTAR MEDIA SOLUTIONS Co.,LTD. Copyright c 2005-2013. All rights reserved.
 *
 * This source code is the property of KINGSTAR MEDIA SOLUTIONS LTD. It is intended
 * only for the use of KINGSTAR MEDIA application development. Reengineering, reproduction
 * arose from modification of the original source, or other redistribution of this source
 * is not permitted without written permission of the KINGSTAR MEDIA SOLUTIONS LTD.
 */
package me.chanjar.weixin.mp.util.json;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.result.WxMpUserBatchGetResult;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class WxMpUserBatchGetResultGsonAdapter implements
		JsonDeserializer<WxMpUserBatchGetResult> {

	public WxMpUserBatchGetResult deserialize(JsonElement jsonElement,
			Type type, JsonDeserializationContext jsonDeserializationContext)
			throws JsonParseException {
		WxMpUserBatchGetResult wxMpUserBatchGetResult = new WxMpUserBatchGetResult();
		JsonObject json = jsonElement.getAsJsonObject();
		if (json.get("user_info_list") != null
				&& !json.get("user_info_list").isJsonNull()) {
			JsonArray item = json.getAsJsonArray("user_info_list");
			List<WxMpUser> items = new ArrayList<>();
			for (JsonElement anItem : item) {
				JsonObject articleInfo = anItem.getAsJsonObject();
				items.add(WxMpGsonBuilder.create().fromJson(articleInfo,
						WxMpUser.class));
			}
			wxMpUserBatchGetResult.setUserInfoList(items);
		}
		return wxMpUserBatchGetResult;
	}
}
