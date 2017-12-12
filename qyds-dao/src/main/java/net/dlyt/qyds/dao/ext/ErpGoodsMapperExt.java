package net.dlyt.qyds.dao.ext;

import net.dlyt.qyds.common.dto.ErpGoods;
import net.dlyt.qyds.common.dto.ext.ErpGoodsExt;
import net.dlyt.qyds.common.form.ErpGoodsForm;
import net.dlyt.qyds.common.form.GoodsCodeColorNameDto;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zlh on 2016/7/29.
 */
@Repository
public interface ErpGoodsMapperExt {
    //取得全部信息
    List<ErpGoods> selectAll();
    //根据条件取得分页信息
    List<ErpGoodsExt> selectByPage(ErpGoodsForm record);
    //根据商品代码取得分信息
    List<ErpGoodsExt> selectByCode(String code);
    //根据条件取得总数量
    int getCountByPage(ErpGoodsForm record);
    //根据ID取得商品信息
    ErpGoodsExt selectById(String id);
    //删除全部信息
    void deleteAll();
    //无时间更新
    void updateNoTime(ErpGoods record);
    //取得优惠券SKU列表
    List<ErpGoods> getCouponSkuListById(String id);
    //取得满送货品活动的商品颜色列表
    List<ErpGoods> getActGoodsColorListById(String tempId);
    //通过颜色名称取得满送货品活动的商品颜色列表
    List<GoodsCodeColorNameDto> getGoodsColorCodeByName(String nameJson);
}