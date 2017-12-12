package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.CmsItems;
import net.dlyt.qyds.common.dto.CmsMaster;
import net.dlyt.qyds.common.dto.ext.CmsItemsExt;
import net.dlyt.qyds.common.dto.ext.CmsMasterExt;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CmsItemsMapperExt {

    List<CmsItemsExt> selectAll();

    Short selectMaxSrot(CmsItems record);

    int delete(CmsItems record);

    CmsItems selectByItemCode(String itemCode);

    List<CmsItemsExt> getChildItemList(String itemCode);

    List<CmsMasterExt> getMetaData(String itemCode);

    CmsMasterExt getDeliverData(String itemCode);
}