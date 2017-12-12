package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.ErpCity;
import net.dlyt.qyds.common.dto.ErpDistrict;
import net.dlyt.qyds.common.dto.ErpProvince;
import net.dlyt.qyds.common.dto.ErpStore;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zlh on 2016/7/29.
 */
@Repository
public interface ErpStoreMapperExt {
    //取得全部信息
    List<ErpStore> selectAll();
    //删除全部信息
    void deleteAll();
    //无时间更新
    void updateNoTime(ErpStore record);

    List<ErpProvince> queryAllProvince();

    List<ErpCity> queryCityOfProvince(String provinceCode);

    List<ErpDistrict> queryDistrictOfCity(String cityCode);
}