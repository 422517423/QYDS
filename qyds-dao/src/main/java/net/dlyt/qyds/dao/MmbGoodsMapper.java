package net.dlyt.qyds.dao;

import net.dlyt.qyds.common.dto.MmbGoods;
import net.dlyt.qyds.common.form.MmbGoodsForm;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MmbGoodsMapper {

    int insertSelective(MmbGoods record);

    List<MmbGoodsForm> selectByLevelId(String mmbLevelId);

    int deleteByPrimaryKey(String mmbGoodsId);

    int selectBrandExistByGoodsId(String goodsId);
}
