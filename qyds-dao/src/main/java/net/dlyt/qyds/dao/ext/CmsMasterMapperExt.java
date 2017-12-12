package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.CmsItems;
import net.dlyt.qyds.common.dto.CmsMaster;
import net.dlyt.qyds.common.dto.ext.CmsItemsExt;
import net.dlyt.qyds.common.dto.ext.CmsMasterExt;
import net.dlyt.qyds.common.form.CmsMsterForm;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CmsMasterMapperExt {

    Short selectMaxSrot(CmsMaster record);

    List<CmsMasterExt> selectCmsMasterByCmsId(CmsMaster record);

    List<CmsMasterExt> selectAll(CmsMasterExt ext);

    int getAllDataCount(CmsMasterExt ext);

    List<CmsMasterExt> selectAllCmsMaster();

    List<CmsMasterExt> selectAllCmsItems();

    List<CmsMasterExt> selectAllCmsMasterGoods();

    // 获取栏目下的cms
    List<CmsMasterExt> selectCmsMasterByItemCode(String itemCode);

    // 获取栏目下的cms
    List<CmsMasterExt> selectCmsMasterByItemId(String itemId);

    int delete(String cmsId);

    List<CmsMasterExt> getGoodsTypeIndex(String itemCode);

}