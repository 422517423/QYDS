package net.dlyt.qyds.common.form;

import net.dlyt.qyds.common.dto.ext.ErpGoodsColorExt;

/**
 * Created by zlh on 2016/7/26.
 */
public class ErpGoodsColorInputForm {

    //批量标记,1为整体导入，删除所以旧数据重新导入，0为部分更新，列表数据中只包括变化的数据，需要参照style处理
    private int  batch;
    //颜色列表数据

    public ErpGoodsColorExt[] getData() {
        return data;
    }

    public void setData(ErpGoodsColorExt[] data) {
        this.data = data;
    }

    public int getBatch() {
        return batch;
    }

    public void setBatch(int batch) {
        this.batch = batch;
    }

    private ErpGoodsColorExt[] data;
}