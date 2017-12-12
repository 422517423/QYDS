package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.ErpGoodsSeason;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zlh on 2016/7/29.
 */
@Repository
public interface ErpGoodsSeasonMapperExt {
    //取得全部信息
    List<ErpGoodsSeason> selectAll();
    //删除全部信息
    void deleteAll();
    //无时间更新
    void updateNoTime(ErpGoodsSeason record);
}