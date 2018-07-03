package net.dlyt.qyds.web.common.unionpay.web;

import net.dlyt.qyds.web.common.unionpay.sdk.SDKConfig;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;


/**
 * 重要：联调接入的时候请务必仔细阅读注释！！！
 * 
 * 功能：从应用的classpath下加载acp_sdk.properties属性文件并将该属性文件中的键值对赋值到SDKConfig类中 <br>
 * 
 */
public class AutoLoadServlet extends HttpServlet {
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		
		SDKConfig.getConfig().loadPropertiesFromSrc();
		
		super.init();
	}
}
