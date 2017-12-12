package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.CmsMaster;
import net.dlyt.qyds.common.dto.CmsMasterGoods;
import net.dlyt.qyds.common.dto.ext.CmsMasterExt;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CmsMasterGoodsMapperExt {

    Short selectMaxSrot(CmsMasterGoods cmsMasterGoods);

    List<CmsMasterGoods> selectGoodsByCmsId(String cmsId);
}