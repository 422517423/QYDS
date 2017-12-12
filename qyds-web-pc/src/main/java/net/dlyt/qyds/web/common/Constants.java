package net.dlyt.qyds.web.common;

/**
 * Created by panda on 16/6/26.
 */
public class Constants {
    //成功
    public static final  String NORMAL = "00";
    //失败
    public static final String FAIL = "99";
    //没有权限
    public static final String UNAUTHORIZED = "401";

    public static final String FORBIDDEN = "403";

    //新建操作标识符
    public static final String INSERT = "insert";
    //编辑操作标识符
    public static final String UPDATE = "update";

    //有效
    public static final String VALID = "0";
    //无效
    public static final String INVALID = "1";

    // 组织编码
    public static final String ORGID = "00000000";
    // 生成ID随机数的长度
    public static final Integer ID_COUNT = 8;

    public static final String UNAUTHORIZED_URL = "401.json";

    public static final String COOKIE_KEY = "unique_token_id";

    public static final String FAIL_MESSAGE = "system error!";

    /**
     * 下拉框获取区分
     */
    // 任务状态
    public static final String TASK_STATUS = "task_status";

    // 任务异常
    public static final String IS_EXCEPTION = "is_exception";


    /** 硬盘内图片真实路径 */
//	 public static final String FILE_DIR = "/Users/keyancong/Documents/mnt/tomcat/qyds_file/";
//    public static final String FILE_DIR = "/Users/dkzhang/Pictures/";
//    public static final String FILE_DIR = "/mnt/tomcat/qyds_file/";

    /** 数据库保存的资源图片URL */
    public static final String IMAGE_URL = "image/";
//
//    /** 附件服务地址 */
//	 public static final String FILE_SERVER_URL =
//	 "http://localhost:8080/qyds_file/";
////    public static final String FILE_SERVER_URL = "http://120.26.230.73/qydsfile/";
//
//    /** 图片访问目录 功能别 **/
//    //商品品牌
//    public static final String GDS_BRAND_TYPE = "GDS_BRAND_TYPE/";
//    //商品分类
//    public static final String GDS_TYPE = "GDS_TYPE/";

}
