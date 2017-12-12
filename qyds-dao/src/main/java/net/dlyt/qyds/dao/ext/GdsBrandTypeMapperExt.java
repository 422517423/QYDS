package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.GdsBrandType;
//import net.dlyt.qyds.common.dto.GdsTypeExt;
import net.dlyt.qyds.common.dto.GdsType;
import net.dlyt.qyds.common.dto.GdsTypeExt;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by congkeyan on 16/7/20.
 */
@Repository
public interface GdsBrandTypeMapperExt {

    //获得全部菜单
    List<GdsBrandType> getTreeList(GdsBrandType gdsBrandType);

    void updateSort(GdsBrandType gdsBrandType);

    /***********************API**************************************/

    List<GdsTypeExt> getGdsBrandType();

    List<GdsTypeExt> getSubLevelFloorList(GdsType gdsType);


}
