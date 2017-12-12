var prizeDrawId = sessionStorage.getItem("prizeDrawId");

$(document).ready(function () {
    $("#configPrize").modal('show');
    // $("#sortOld").val(sortOld);
    // $("#sort").val(sortOld);

    initConfig();

    $("#btn_save_config").click(function () {
        saveConfig();
    });
});

function initConfig() {
    var url = "/prize_draw/getPrizeConfig.json";
    var param = {
        prizeDrawId: prizeDrawId
    };

    var success = function (data) {
        if (data.resultCode == '00') {
            $("#is_login").val(data.result.isLogin);
            $("#is_exchange").val(data.result.exchangeFlag);
            $("#exchange_point").val(data.result.exchangePoint);
            $("#is_order").val(data.result.isOrder);
            $("#order_amount").val(data.result.orderAmount);
        } else {
            $("#configPrize").modal('hide');
            showAlert('获取失败!', '提示', function () {
            });
        }
    };
    var error = function () {
        $("#configPrize").modal('hide');
        showAlert('网络异常!', '提示', function () {
        });
    };
    axse(url, {"data": JSON.stringify(param)}, success, error);
}

function saveConfig() {

    if("1" == $("#is_exchange").val()){
        var point = $("#exchange_point").val();
        if(point == undefined || point == 0 || point.length == 0){
            showTip('请输入兑换积分');
            return;
        }
    }

    if("1" == $("#is_order").val()){
        var amount = $("#order_amount").val();
        if(amount == undefined || amount == 0 || amount.length == 0){
            showTip('请输入订单金额');
            return;
        }
    }

    var url = "/prize_draw/updatePrizeConfig.json";
    var param = {
        prizeDrawId: prizeDrawId,
        isLogin: $("#is_login").val(),
        exchangeFlag: $("#is_exchange").val(),
        exchangePoint: $("#exchange_point").val(),
        isOrder: $("#is_order").val(),
        orderAmount: $("#order_amount").val()
    };

    var success = function (data) {
        if (data.resultCode == '00') {
            $("#configPrize").modal('hide');
            showAlert('提交成功!', '提示', function () {
                table.fnDraw();
            });
        } else {
            showTip('提交失败,原因:' + data.resultMessage);
        }
    };
    var error = function () {
        showTip('网络异常.');
    };
    axse(url, {"data": JSON.stringify(param)}, success, error);
}


function showTip(message) {
    $("#prize_config_tip").text(message);
    setTimeout(function () {
        $("#prize_config_tip").text("");
    }, 2000);
}
