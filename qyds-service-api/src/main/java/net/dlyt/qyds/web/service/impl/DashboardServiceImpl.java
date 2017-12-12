package net.dlyt.qyds.web.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.dlyt.qyds.common.dto.GdsMasterExt;
import net.dlyt.qyds.common.dto.OrdMasterExt;
import net.dlyt.qyds.common.dto.ext.MmbMasterExt;
import net.dlyt.qyds.common.form.EchartsForm;
import net.dlyt.qyds.dao.ext.DashboardMapperExt;
import net.dlyt.qyds.dao.ext.GdsMasterMapperExt;
import net.dlyt.qyds.dao.ext.MmbMasterMapperExt;
import net.dlyt.qyds.dao.ext.OrdMasterMapperExt;
import net.dlyt.qyds.web.service.DashboardService;
import net.dlyt.qyds.web.service.common.Constants;
import net.dlyt.qyds.web.service.common.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by YiLian on 16/8/18.
 */
@Service("dashboardService")
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private OrdMasterMapperExt ordMasterMapperExt;

    @Autowired
    private GdsMasterMapperExt gdsMasterMapperExt;

    @Autowired
    private MmbMasterMapperExt mmbMasterMapperExt;

    @Autowired
    private DashboardMapperExt dashboardMapperExt;

    /**
     * 待指派门店订单数量统计
     *
     * @return
     */
    @Override
    public JSONObject countPendingDispatchOrder() {
        JSONObject json = new JSONObject();
        try {
            OrdMasterExt ordMasterExt = new OrdMasterExt();
            ordMasterExt.setShopId(Constants.ORGID);

            int count = ordMasterMapperExt.countPendingDispatchOrder(ordMasterExt);

            json.put("data", count);
            json.put("resultCode", Constants.NORMAL);

        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;

    }

    /**
     * 未发货订单数量
     *
     * @return
     */
    @Override
    public JSONObject countPendingDeliveryOrder() {
        JSONObject json = new JSONObject();
        try {
            // 付款状态	pay_status	character varying	2			10	10.未付款，20.付款成功，21.付款失败，30.退款中，31.退款完成，40.补款中，41.补款完成
            // 发货状态	deliver_status	character varying	2			10	10.未发货，20.已发货，21.已收货，30.退货已发货，31.退货已收货，40.换货已发退货，41.换货已收退货，43.换货已发新货，44.换货已收新货
            OrdMasterExt ordMasterExt = new OrdMasterExt();
            ordMasterExt.setShopId(Constants.ORGID);
            ordMasterExt.setDeliverStatus("10");
            ordMasterExt.setPayStatus("20");
            int count = ordMasterMapperExt.getAllDatasCount(ordMasterExt);
            json.put("data", count);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }

    /**
     * 待退货订单数量
     *
     * @return
     */
    @Override
    public JSONObject countPendingRejectedOrder() {
        JSONObject json = new JSONObject();
        try {
            // 订单正常状态	order_status	character varying	2			10	10.订单未完成，20.订单退单中，21.订单退单完成，30.退货中，31.退货完成，40.换货中，41.换货完成，90.订单完成
            OrdMasterExt ordMasterExt = new OrdMasterExt();
            ordMasterExt.setShopId(Constants.ORGID);
            ordMasterExt.setOrderStatus("30");
            int count = ordMasterMapperExt.getAllDatasCount(ordMasterExt);
            json.put("data", count);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }

    /**
     * 上架商品数量
     *
     * @return
     */
    @Override
    public JSONObject countGoodsOnSell() {
        JSONObject json = new JSONObject();
        try {
            GdsMasterExt ext = new GdsMasterExt();
            ext.setShopId(Constants.ORGID);
            // 是否上架	is_onsell	0.是，1.否
            ext.setIsOnsell("0");

            //获取商品的总数
            int count = gdsMasterMapperExt.getAllDataCount(ext);

            json.put("data", count);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }

    /**
     * 注册会员数量
     *
     * @return
     */
    @Override
    public JSONObject countMember() {
        JSONObject json = new JSONObject();
        try {
            MmbMasterExt ext = new MmbMasterExt();
            //获取会员的总数
            int count = mmbMasterMapperExt.getAllDataCount(ext);

            json.put("data", count);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;

    }

    /**
     * 订单数量(金额)趋势
     *
     * @return
     */
    @Override
    public JSONObject getOrderCountList() {

        JSONObject json = new JSONObject();
        try {
            List<EchartsForm> list = dashboardMapperExt.queryOrderGroupByYearMonth();

            List<String> dateList = getMonthsOfYear();

            List<Integer> countList = new ArrayList<>();
            List<BigDecimal> amountList = new ArrayList<>();
            Integer count;
            BigDecimal amount;
            for (String item : dateList) {
                count = 0;
                amount = new BigDecimal(0);
                for (EchartsForm form : list) {
                    if (item.equals(form.getTitle())) {
                        count = form.getCount();
                        amount = form.getAmount();
                        break;
                    }
                }

                countList.add(count);
                amountList.add(amount);
            }

            json.put("xData", dateList);
            json.put("yData", countList);
            json.put("mData", amountList);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }

    /**
     * 会员等级分布
     *
     * @return
     */
    @Override
    public JSONObject getMemberLevelList() {

        JSONObject json = new JSONObject();
        try {
            List<EchartsForm> list = dashboardMapperExt.queryMemberGroupByLevel();

            JSONArray array = new JSONArray();

            for (EchartsForm item : list) {
                JSONObject jsonItem = new JSONObject();
                jsonItem.put("name", item.getTitle());
                jsonItem.put("value", item.getCount());

                array.add(jsonItem);
            }

            json.put("cData", array);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }

    /**
     * 会员注册绑定新增趋势
     *
     * @return
     */
    @Override
    public JSONObject getMemberAddList() {
        JSONObject json = new JSONObject();
        try {
            List<EchartsForm> list = dashboardMapperExt.queryMemberGroupByYearMonth();

            List<String> dateList = getMonthsOfYear();

            List<Integer> countList = new ArrayList<>();
            Integer count;
            for (String item : dateList) {
                count = 0;
                for (EchartsForm form : list) {
                    if (item.equals(form.getTitle())) {
                        count = form.getCount();
                        break;
                    }
                }

                countList.add(count);

            }

            json.put("xData", dateList);
            json.put("yData", countList);
            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }

    /**
     * 销量排行前十商品
     *
     * @return
     */
    @Override
    public JSONObject getGoodsTopList() {
        JSONObject json = new JSONObject();
        try {
            List<EchartsForm> list = dashboardMapperExt.queryGoodsOrderByQuantity();

            List<String> xList = new ArrayList<>();
            List<Integer> yList = new ArrayList<>();
            List<String> mList = new ArrayList<>();

            for (EchartsForm item : list) {
                xList.add(StringUtil.isEmpty(item.getTitle()) ? "未知商品" : item.getTitle());
                yList.add(item.getCount());
                mList.add(StringUtil.isEmpty(item.getCode()) ? "未知商品" : item.getCode());
            }

            json.put("xData", xList);
            json.put("yData", yList);
            json.put("mData", mList);

            json.put("resultCode", Constants.NORMAL);
        } catch (Exception e) {
            json.put("resultCode", Constants.FAIL);
            json.put("resultMessage", Constants.FAIL_MESSAGE);
        }
        return json;
    }

    /**
     * 取得一年的所有的所有月份
     *
     * @return
     */
    private List<String> getMonthsOfYear() {
        List<String> datelist = new ArrayList<String>();
        try {

            int MONTH_COUNT = -12;


            int amount = MONTH_COUNT + 1;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            start.setTime(new Date());
            end.setTime(new Date());
            end.add(Calendar.MONTH, amount);
            while (end.before(start) || end.equals(start)) {
                datelist.add(format.format(end.getTime()));
                end.add(Calendar.MONTH, 1);
            }

        } catch (Exception e) {

        }

        return datelist;
    }
}
