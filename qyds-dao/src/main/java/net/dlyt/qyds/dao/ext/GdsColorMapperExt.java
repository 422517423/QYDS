package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.GdsColoreimage;
import net.dlyt.qyds.common.dto.GdsSku;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GdsColorMapperExt {

    /**
     * 根据商品ID获取颜色列表
     * @param goodsId
     * @return
     */
    List<GdsColoreimage> selectColorList(String goodsId);


}