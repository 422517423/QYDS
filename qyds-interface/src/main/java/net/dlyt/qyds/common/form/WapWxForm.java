package net.dlyt.qyds.common.form;

import java.io.Serializable;

public class WapWxForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String code = null;
	private String url = null;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
