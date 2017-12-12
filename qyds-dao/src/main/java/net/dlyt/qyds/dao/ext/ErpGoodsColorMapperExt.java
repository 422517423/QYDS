package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.ErpGoodsColor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zlh on 2016/7/26.
 */
@Repository
public interface ErpGoodsColorMapperExt {
    //取得全部信息
    List<ErpGoodsColor> selectAll();
    //根据条件取得分页信息
    List<ErpGoodsColor> selectByPage(ErpGoodsColor record);
    //删除全部信息
    void deleteAll();
    //无时间更新
    void updateNoTime(ErpGoodsColor record);
}