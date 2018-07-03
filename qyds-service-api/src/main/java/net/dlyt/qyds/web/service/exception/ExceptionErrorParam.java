/*
 *  Copyright(c) 2013 Hi-Think All rights reserved. 
 */
package net.dlyt.qyds.web.service.exception;

import net.dlyt.qyds.web.service.common.Constants;

/**
 * 数据错误异常<BR>
 * 
 * @author CJK
 * @version 1.0.0.0
 * 
 *          <PRE>
 * ■变更履历■ 
 * 2013/7/24 新建
 * </PRE>
 */
public class ExceptionErrorParam extends ExceptionBusiness {

	private static final long serialVersionUID = 1L;

	/**
	 * 参数错误异常<BR>
	 */
	public ExceptionErrorParam() {
		super("参数错误");
		resultCd =  Constants.ERROR_PARAM;
	}
	
	public ExceptionErrorParam(String message) {
		super(message);
		resultCd =  Constants.ERROR_PARAM;
	}
}
