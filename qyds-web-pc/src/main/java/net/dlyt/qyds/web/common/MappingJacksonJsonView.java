package net.dlyt.qyds.web.common;


import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MappingJacksonJsonView extends
		org.springframework.web.servlet.view.json.MappingJacksonJsonView {

	@Override
	protected Object filterModel(Map<String, Object> model) {
		Map<String, Object> result = new HashMap<String, Object>(model.size());
		Set<String> renderedAttributes = !CollectionUtils.isEmpty(this
				.getRenderedAttributes()) ? this.getRenderedAttributes()
				: model.keySet();
		for (Map.Entry<String, Object> entry : model.entrySet()) {
			if (!(entry.getValue() instanceof BindingResult)
					&& renderedAttributes.contains(entry.getKey())) {
				String key = entry.getKey();
				if (!key.startsWith("_") && !key.endsWith("_")) {
				  if (key.length() < 4 || !key.substring(key.length() - 4).equals("Form")) {
				    result.put(entry.getKey(), entry.getValue());
				  }
				}
			}
		}
		return result;
	}

}
