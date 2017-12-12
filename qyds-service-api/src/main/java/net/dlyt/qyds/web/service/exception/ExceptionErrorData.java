/*
 *  Copyright(c) 2013 Hi-Think All rights reserved. 
 */
package net.dlyt.qyds.web.service.exception;

import net.dlyt.qyds.web.service.common.Constants;

/**
 * 数据错误异常<BR>
 *
 * @author CJK
 */
public class ExceptionErrorData extends ExceptionBusiness {

    private static final long serialVersionUID = 1L;

    /**
     * 数据错误异常<BR>
     */
    public ExceptionErrorData() {
        super("数据错误");
        resultCd = Constants.ERROR_DATA;
    }

    public ExceptionErrorData(String message) {
        super(message);
        resultCd = Constants.ERROR_DATA;
    }
}
