package net.dlyt.qyds.common.form;

public class UploadImageForm  {

    //上传图片的业务类型
    private String type;

    //上传图片的文件
    private String file;

    //上传图片的图片名称
    private String fileName;

    //上传图片的扩展名
    private String suffix;

    private String id = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}