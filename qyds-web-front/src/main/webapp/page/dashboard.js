/**
 * Created by YiLian on 16/8/18.
 */

$(document).ready(function () {

    initCharts();

    getTitleCount('pending_dispatch');
    getTitleCount('pending_rejected');
    getTitleCount('goods_onSell');
    getTitleCount('member_count');

    getMemberGroupByLevel();

    getMemberGroupByMonthYear();

    getOrderCountGroupByMonthYear();

    getGoodsOrderByQuantity();

});

function initCharts() {
    // 路径配置
    require.config({
        paths : {
            echarts : '../page'
        }
    });
}

function getLineOption(title, xData, yData) {
    return {
        grid : {
            y : 20,
            y2 : 30,
            x : 30,
            x2 : 30
        },
        tooltip : {
            trigger : 'axis'
        },
        xAxis : [ {
            type : 'category',
            boundaryGap : false,
            data : xData
        } ],
        yAxis : [ {
            type : 'value'
        } ],
        series : [ {
            name : title,
            type : 'line',
            data : yData
        } ]
    };
}

function getBarOption(title, xData, yData, mData) {
    return {
        grid : {
            y : 20,
            y2 : 30,
            x : 30,
            x2 : 30
        },
        tooltip : {
            trigger : 'axis'
        },
        calculable : true,
        xAxis : [ {
            type : 'category',
            data : mData,
            axisLabel:{
                show:true,
                interval:0
            }
        } ],
        yAxis : [ {
            type : 'value'
        } ],
        series : [ {
            barWidth : 40,
            name : title,
            type : 'bar',
            data : yData
        } ]
    };
}

function getPieOption(title, data, color) {
    return {
        tooltip : {
            trigger : 'item',
            formatter : "{a} <br/>{b} : {c} ({d}%)"
        },
        series : [ {
            name : title,
            type : 'pie',
            radius : '60%',
            center : [ '50%', '50%' ],
            data : data
        } ],
        color : color
    };
}

function getTitleCount(type) {
    var url;

    if (type == 'pending_dispatch') {
        url = "/dashboard/getPendingDispatchOrderCount.json";
    } else if (type == 'pending_rejected') {
        url = "/dashboard/getPendingRejectedOrderCount.json";
    } else if (type == 'goods_onSell') {
        url = "/dashboard/getGoodsOnSellCount.json";
    } else if (type == 'member_count') {
        url = "/dashboard/getMemberCount.json";
    } else {
        return;
    }

    var success = function (data) {
        if (data.resultCode == '00') {
            if (type == 'pending_dispatch') {
                $("#pending_dispatch").text(data.data);
            } else if (type == 'pending_rejected') {
                $("#pending_rejected").text(data.data);
            } else if (type == 'goods_onSell') {
                $("#goods_onSell").text(data.data);
            } else if (type == 'member_count') {
                $("#member_count").text(data.data);
            } else {
                return;
            }
        }
    };
    var error = function () {
        showAlert('操作失败,网络异常.');
    };
    axse(url, null, success, error);
}

function getMemberGroupByLevel() {

    var url = "/dashboard/getMemberLevelList.json";

    var success = function (data) {
        if (data.resultCode == '00') {
            require(['echarts', 'echarts/chart/pie'], function (ec) {
                var cData = data.cData;
                var colors = ['#C0C0C0', '#FFA500', '#FFD700'];
                var chart = ec.init(document.getElementById('chart3'));
                chart.setOption(getPieOption("会员数量", cData, colors));
            });
        }
    };
    var error = function () {
        showAlert('操作失败,网络异常.');
    };
    axse(url, null, success, error);
}

function getMemberGroupByMonthYear() {
    var url = "/dashboard/getMemberAddList.json";

    var success = function (data) {
        console.log(data);
        if (data.resultCode == '00') {
            require(['echarts', 'echarts/chart/line'], function (ec) {
                var xData = data.xData;
                var yData = data.yData;

                var chart = ec.init(document.getElementById('chart4'));
                chart.setOption(getLineOption("新增注册", xData, yData));
            });
        }
    };
    var error = function () {
        showAlert('操作失败,网络异常.');
    };
    axse(url, null, success, error);
};

function getOrderCountGroupByMonthYear() {
    var url = "/dashboard/getOrderCountList.json";

    var success = function (data) {
        console.log(data);
        if (data.resultCode == '00') {
            require(['echarts', 'echarts/chart/line'], function (ec) {
                var xData = data.xData;
                var yData = data.yData;

                var chart = ec.init(document.getElementById('chart1'));
                chart.setOption(getLineOption("订单数量", xData, yData));
            });

            require(['echarts', 'echarts/chart/line'], function (ec) {
                var xData = data.xData;
                var yData = data.mData;

                var chart = ec.init(document.getElementById('chart2'));
                chart.setOption(getLineOption("订单金额", xData, yData));
            });
        }
    };
    var error = function () {
        showAlert('操作失败,网络异常.');
    };
    axse(url, null, success, error);
};

function getGoodsOrderByQuantity() {
    var url = "/dashboard/getGoodsTopList.json";

    var success = function (data) {
        if (data.resultCode == '00') {

            require(['echarts', 'echarts/chart/bar'], function (ec) {
                var xData = data.xData;
                var yData = data.yData;
                var mData = data.mData;

                var chart = ec.init(document.getElementById('chart5'));
                chart.setOption(getBarOption("销售件数",xData, yData, mData));
            });
        }
    };
    var error = function () {
        showAlert('操作失败,网络异常.');
    };

    axse(url, null, success, error);
};

function goGoodsList(){
    sessionStorage.setItem("goodsStatus_dashboard","10");
    $('#content').load('gds_master/list.html');
}

function goMemberList(){
    $('#content').load('mmb_master/list.html');
}

function goDispatchPage(){
    $('#content').load('ord_dispatch/dispatchOrdList.html');
}

function goApprovePage(){
    $('#content').load('ord_return_goods/approve_list.html');
}