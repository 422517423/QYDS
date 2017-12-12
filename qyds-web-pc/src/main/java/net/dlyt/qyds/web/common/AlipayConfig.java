package net.dlyt.qyds.web.common;

/**
 * Created by wenxuechao on 16/8/27.
 */

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.3
 *日期：2012-08-10
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。

 *提示：如何获取安全校验码和合作身份者ID
 *1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 *3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”

 *安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？
 *解决方法：
 *1、检查浏览器配置，不让浏览器做弹框屏蔽设置
 *2、更换浏览器或电脑，重新登录查询。
 */
public class AlipayConfig {

    //↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    // 合作身份者ID，以2088开头由16位纯数字组成的字符串
    public static String partner = "2088221943157675";
    //public static String partner = "2088621609204743";

    // 合作身份者ID
   public static String seller_email = "tianmao@dealuna.com";
    //public static String seller_email = "aicheshenghuo111@126.com";

    // 商户的私钥
    public static String key = "4nfpkjxlq7kuuy58z60pgz88bqsa4xyj";
   //public static String key = "MIICeQIBADANBgkqhkiG9w0BAQEFAASCAmMwggJfAgEAAoGBAOknlo8GUXfRvHF2IED4HBWUAYwlNH4+yEE0HubjpJnmz3oq6ltECZ8DE+UDNOuta454cwVmQV8wD7Vizmzb2xn7APd07v1r6iDIoscg1UyWC2evn2OCOhre2w04CqAdEric6wjGeKKlClhXoP2XTo/8yWYfwrbLuZg9uJEv4OaZAgMBAAECgYEA4OMDcM8KU4Uku1fSAniONi39VB0qxb5uz52jVNcsFBox80djp0Y39X01ekaei9hJDzGU2BdqZJLqA+3a59skN9Kt0L1cT8AI0Bor7UK1T3pDi3r7Csho4v8KabnDVFIzEDrgwy78WN/ZETgI6cCdY1Q0ce9EJcINP0w/j5J2ZNECQQD4AMJ1rG0BWeFi00gZxf4fZ9NjtQewvLHv5Yl1yB5M+V80FVutg3kctPWIoLGTz5DiUvD7PXnwON0ZCxiq1fTlAkEA8KxBzD2MznYuva3N3dOTQ15RDChH88TLSHpsBbFak4dWBixpFQXt0UVKsyFBS7YnrhIcb9/v25eItpF0elfjpQJBAOsstTqnZrroQ53WY0HliiS37TJY4dEMLuQh06c4PNubglc+hbBVw0ygoVGNqLdVFrw4EML+uho9ohR0fFDMEa0CQQDUahRAoOpPprsmKeoSqJPG182Mf1jR65THSVVhyOB6aO4h0LyGE4+MLi3535XzrrdiyRVUID9gfmPpToORH1UJAkEAuistuQWKZQ3QkchUCdweR9jZ1+Qg9t25tGNBJrquCTIZGLs0qQR0xYlVbq00aADFN2J99eJD+Ng18m0qAmO4OA==";
    //↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑


    // 调试用，创建TXT日志文件夹路径
    public static String log_path = "D:\\";

    // 字符编码格式 目前支持 gbk 或 utf-8
    public static String input_charset = "gbk";

    // 签名方式 不需修改
    public static String sign_type = "MD5";
}
