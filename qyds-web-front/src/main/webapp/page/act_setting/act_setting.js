$(document).ready(function () {
    $("#btn_select_first_buy_activity").click(function () {
        // 弹出商品分类列表
        $('#customDialog').load('act_setting/first_buy_activity_select.html');
    });
    $("#btn_select_first_buy_activity_clear").click(function () {
        $("#first_buy_activity").val("");
        $("#first_buy_activity_name").val("");
    });
    $("#btn_select_first_buy_coupon").click(function () {
        // 弹出商品分类列表
        $('#customDialog').load('act_setting/first_buy_coupon_select.html');
    });
    $("#btn_select_first_buy_coupon_clear").click(function () {
        $("#first_buy_coupon").val("");
        $("#first_buy_coupon_name").val("");
    });

    $("#save_btn").click(function () {
        save();
    });
    getDetail();

});
function onFirstBuyActivitySelected(activity) {
    $("#first_buy_activity").val(activity.activityId);
    $("#first_buy_activity_name").val(activity.activityName);
}

function onFirstBuyCouponSelected(coupon) {
    $("#first_buy_coupon").val(coupon.couponId);
    $("#first_buy_coupon_name").val(coupon.couponName);
}

function getDetail() {
    var url = "/com_setting/getDetail.json";
    var param = {};
    var success = function (data) {
        if (data.resultCode == '00') {
            initForm(data.result);
        } else {
            showAlert('获取失败,原因:' + data.resultMessage);
        }
    };
    var error = function () {
        showAlert('网络异常.');
    };
    axse(url, {"data": JSON.stringify(param)}, success, error);
}

function initForm(data){
    $("#first_buy_activity").val(data.firstBuyActivity);
    if(data.firstBuyActivityDetail){
        $("#first_buy_activity_name").val(data.firstBuyActivityDetail.activityName);
    }
    $("#first_buy_coupon").val(data.firstBuyCoupon);
    if(data.firstBuyCouponDetail){
        $("#first_buy_coupon_name").val(data.firstBuyCouponDetail.couponName);
    }
}

function save() {
    var url = "/com_setting/edit.json";
    var param = {
        firstBuyActivity: $("#first_buy_activity").val(),
        firstBuyCoupon: $("#first_buy_coupon").val()
    };
    var success = function (data) {
        if (data.resultCode == '00') {
            showAlert('提交成功!', '提示');
        } else {
            showAlert('提交失败,原因:' + data.resultMessage);
        }
    };
    var error = function () {
        showAlert('网络异常.');
    };
    axse(url, {"data": JSON.stringify(param)}, success, error);
}