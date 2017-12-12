package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.ShpMaster;
import net.dlyt.qyds.common.dto.ShpMasterExt;
import net.dlyt.qyds.common.dto.SysRole;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by pc on 2016/7/18.
 */
@Repository
public interface ShpMasterMapperExt {
    /**
     * 获取店铺列表信息
     * @param
     */
    List<ShpMasterExt> selectAll();

}
