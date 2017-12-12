package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.BnkMaster;
import net.dlyt.qyds.common.dto.BnkMasterExt;
import net.dlyt.qyds.common.dto.ErpBankRecord;
import net.dlyt.qyds.common.dto.ErpStore;
import net.dlyt.qyds.common.dto.ext.ErpStoreExt;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * Created by wy on 2016/8/2.
 */
@Repository
public interface BnkMasterMapperExt {
    /*获取商品库存列表数据*/
    List<BnkMasterExt> selectAll(BnkMasterExt form);

    /*获取商品库存总行数*/
    int getAllDataCount(BnkMaster bnk);

    //插入库存表
    int insertSelective(BnkMaster record);
    /*根据ID获取信息*/
    BnkMaster selectByPrimaryKey(String bankId);

    //选择门店列表的时候调用
    List<HashMap> getOrgList(ErpStoreExt erpStore);

    //选择全部门店列表包括仓库的时候调用
    List<HashMap> getOrgAllList(ErpStoreExt erpStore);

    //根据sku获取库存数量
    List<HashMap> getQyantityBySkuId(ErpStoreExt erpStore);

    //根据sku获取包括仓库的库存数量
    List<HashMap> getQyantityAllBySkuId(ErpStoreExt erpStore);


    /**
     * 根据商品ID,商品SKUID获取库存信息
     * @param bnkMaster
     * @return
     */
    BnkMasterExt selectByGoodSkuId(BnkMaster bnkMaster);

    /**
     * 取消订单更新库存主表
     * @param bnkMasterExt
     */
    void cancelOrderAddBank(BnkMasterExt bnkMasterExt);

    //根据ERP数据取得库存数据
    BnkMaster getByErp(ErpBankRecord erp);

    //根据ERP数据插入库存数据
    int insertByErp(ErpBankRecord erp);

    //更新库存数据
    int updateCountById(BnkMaster erp);

    List<BnkMaster> getReportBank();

    List<BnkMasterExt> getSkuStoreList(BnkMasterExt form);
}
