package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.ActMaster;
import net.dlyt.qyds.common.dto.OrdMasterExt;
import net.dlyt.qyds.common.form.ActMasterForm;
import net.dlyt.qyds.common.form.SkuForm;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cjk on 16/8/1.
 */
@Repository
public interface ActMasterMapperExt {

    List<ActMaster> getAllList(ActMasterForm form);

    List<ActMasterForm> selectAllActList(ActMasterForm form);

    List<ActMasterForm> selectAllActListByMember(ActMasterForm form);

    List<ActMasterForm> selectPointsActList(ActMasterForm form);

    List<ActMaster> select(ActMaster form);

    int selectCount(ActMaster form);

    int checkExistByActivityName(ActMaster form);

    List<ActMaster> selectApproveList(ActMasterForm form);

    List<String> getSellerYears();

    List<Map> getSellerSeasons(String year);

    List<Map> getErpBrands();

    List<Map> getErpLineCode();

    int selectApproveCount(ActMasterForm form);

    ActMasterForm selectById(ActMasterForm tempId);

    List<ActMasterForm> selectActivitiesBySkuInfo(SkuForm skuInfo);

    List<ActMasterForm> selectOrderActivity();

    ActMasterForm selectByPrimaryKey(String activityId);

    // 查询当天有效期内的所有秒杀
    List<ActMasterForm> selectSecKillActivityList(ActMasterForm form);

    // 根据开始时间查询某一时间段内
    List<ActMasterForm> selectSecKillActivityByTime(ActMasterForm form);

    List<HashMap> getActiveIdAndList(OrdMasterExt ordMasterExt);

    List<HashMap> getCoupponIdAndList(OrdMasterExt ordMasterExt);

    List<ActMasterForm> selectAllActivitiesBySkuInfo
            (@Param("goodsId")String goodsId, @Param("brandId")String brandId, @Param("goodsTypeId")String goodsTypeId, @Param("skuIdList")List<String> skuIdList);
}
