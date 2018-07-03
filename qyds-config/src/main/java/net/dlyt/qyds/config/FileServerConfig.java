package net.dlyt.qyds.config;

/**
 * Created by dkzhang on 16/8/8.
 */
public class FileServerConfig {


    private String uploadHostPrefix = null;
    private String uploadContextPath = "/qyds-web-file";

    private String storagePath = "/mnt/tomcat/qyds/qyds_file/";

    private String urlHostPrefix = null;

    private String urlContextPath = "/images";

    public String getUploadHostPrefix() {
        return uploadHostPrefix;
    }

    /**
     * http前缀,含主机及端口
     * @param uploadHostPrefix http://domain:port
     */
    public void setUploadHostPrefix(String uploadHostPrefix) {
        this.uploadHostPrefix = uploadHostPrefix;
    }

    public String getUploadContextPath() {
        return uploadContextPath;
    }

    /**
     * 基本访问路径
     * @param uploadContextPath /project-module
     */
    public void setUploadContextPath(String uploadContextPath) {
        this.uploadContextPath = uploadContextPath;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public String getUrlContextPath() {
        return urlContextPath;
    }

    public void setUrlContextPath(String urlContextPath) {
        this.urlContextPath = urlContextPath;
    }

    public String getUrlHostPrefix() {
        return urlHostPrefix;
    }

    public void setUrlHostPrefix(String urlHostPrefix) {
        this.urlHostPrefix = urlHostPrefix;
    }

    /**
     * 上传的地址上下文前缀
     * @return 上传地址前缀
     */
    public String getUploadContext(){
        return uploadHostPrefix + uploadContextPath;
    }

    /**
     * 文件访问URL地址上下文前缀(需拼接文件尺寸)
     * @return 上传地址前缀
     */
    public String getUrlContext(){
        return urlHostPrefix + urlContextPath;
    }
}
