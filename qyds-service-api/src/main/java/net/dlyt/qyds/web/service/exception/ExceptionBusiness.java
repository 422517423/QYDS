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
 *          <p>
 *          <PRE>
 *          ■变更履历■
 *          2013/7/24 新建
 *          </PRE>
 */
public class ExceptionBusiness extends RuntimeException {

    /**
     * 处理结果CD
     */
    public String resultCd = Constants.FAIL;

    private static final long serialVersionUID = 1L;

    /**
     * 数据错误异常<BR>
     */
    public ExceptionBusiness() {
        super("服务器错误");
    }

    public ExceptionBusiness(String message) {
        super(message);
    }
}
