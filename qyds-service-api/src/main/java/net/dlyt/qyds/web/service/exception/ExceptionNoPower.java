/*
 *  Copyright(c) 2013 Hi-Think All rights reserved. 
 */
package net.dlyt.qyds.web.service.exception;

import net.dlyt.qyds.web.service.common.Constants;

/**
 * 没有访问权限异常<BR>
 *
 * @author CJK
 * @version 1.0.0.0
 *          <p>
 *          <PRE>
 *          ■变更履历■
 *          2013/7/24 新建
 *          </PRE>
 */
public class ExceptionNoPower extends ExceptionBusiness {

    private static final long serialVersionUID = 1L;

    /**
     * 没有访问权限异常<BR>
     */
    public ExceptionNoPower() {
        super("没有访问权限");
        resultCd =  Constants.NO_POWER;

    }

    public ExceptionNoPower(String message) {
        super(message);
        resultCd =  Constants.NO_POWER;
    }
}
