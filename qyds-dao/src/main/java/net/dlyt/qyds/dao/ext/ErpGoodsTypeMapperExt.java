package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.ErpGoodsType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zlh on 2016/7/29.
 */
@Repository
public interface ErpGoodsTypeMapperExt {
    //取得全部信息
    List<ErpGoodsType> selectAll();
    //删除全部信息
    void deleteAll();
    //无时间更新
    void updateNoTime(ErpGoodsType record);
}