package net.dlyt.qyds.web.common;

import org.springframework.mail.javamail.ConfigurableMimeFileTypeMap;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

/**
 * Implementation of ViewResolver for attachment file.
 */
public class AttachmentViewResolver extends UrlBasedViewResolver {

	// ConfigurableMimeFileTypeMap
	private ConfigurableMimeFileTypeMap mimeFileTypeMap = new ConfigurableMimeFileTypeMap();

	@Override
	protected Class<?> requiredViewClass() {
		return AttachmentView.class;
	}

	/** Constructor */
	public AttachmentViewResolver() {
	    setViewClassEx();
	}

	private void setViewClassEx() {
	    super.setViewClass(AttachmentView.class);
	}

	@Override
	protected AbstractUrlBasedView buildView(String viewName) throws Exception {
		AbstractUrlBasedView view = super.buildView(viewName);
		view.setContentType(mimeFileTypeMap.getContentType(viewName));
		return view;
	}
}
