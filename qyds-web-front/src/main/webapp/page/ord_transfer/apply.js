/**
 * Created by zlh on 16/10/27.
 */

var orderSubId = sessionStorage.getItem("orderSubId");
$(document).ready(function () {

    $("#applyArea").modal('show');

    $("#btn_apply").click(function () {
        apply();
    });

    if (orderId && orderId.length > 0) {
        getAddress();
    }
});

function getAddress() {
    var url = "/ord_transfer/getAddress.json";
    var param = {
        subOrderId: orderSubId
    };
    var successCallback = function (data) {
        if (data.resultCode == '00') {
            var item = data.data;
            $('#apply_contactor').val(item.applyContactor);
            $('#apply_phone').val(item.applyPhone);
            $('#apply_postcode').val('');
            $('#apply_address').val(item.applyAddress);
        } else {
            $("#applyArea").modal('hide');
            showAlert('数据取得失败:' + data.resultMessage);
        }
    };
    var errorCallback = function () {
        $("#applyArea").modal('hide');
        showAlert('网络异常.');
    };
    axse(url, param, successCallback, errorCallback);
}

//点击提交按钮
function apply() {
    var url = "/ord_transfer/apply.json";
    var param = {
        subOrderId: orderSubId,
        applyContactor: $("#apply_contactor").val(),
        applyPhone: $("#apply_phone").val(),
        applyPostcode: $("#apply_postcode").val(),
        applyAddress: $("#apply_address").val()
    };
    var successCallback = function (data) {
        if (data.resultCode == '00') {
            $("#applyArea").modal('hide');
            showAlert('提交成功!');
            transferBack();
        } else {
            $("#applyArea").modal('hide');
            showAlert('提交失败:' + data.resultMessage);
        }
    };
    var errorCallback = function () {
        $("#applyArea").modal('hide');
        showAlert('网络异常.');
    };
    axse(url, param, successCallback, errorCallback);
}
