package net.dlyt.qyds.common.form;

import net.dlyt.qyds.common.dto.MmbCollection;

import java.util.Date;
import java.util.List;

/**
 * Created by YiLian on 16/8/2.
 */
public class MmbCollectionForm extends MmbCollection {

    /**
     * 删除多条收藏时使用的参数
     */
    private List<Integer> collections;

    /**
     * 最后一条更新时间(分页用)
     */
    private Date lastUpdateTime;

    // 当前页数
    private int currentPage;

    // 总页数
    private int totalPage;

    // 每页件数
    private int pageSize;

    //开始页面
    private int iDisplayStart;

    //每一页显示的项目
    private int iDisplayLength;

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public List<Integer> getCollections() {
        return collections;
    }

    public void setCollections(List<Integer> collections) {
        this.collections = collections;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getiDisplayStart() {
        return iDisplayStart;
    }

    public void setiDisplayStart(int iDisplayStart) {
        this.iDisplayStart = iDisplayStart;
    }

    public int getiDisplayLength() {
        return iDisplayLength;
    }

    public void setiDisplayLength(int iDisplayLength) {
        this.iDisplayLength = iDisplayLength;
    }
}
