/**
 * Created by zlh on 16/12/26.
 */

var orderTransferId = sessionStorage.getItem("orderTransferId");
$(document).ready(function () {
    $("#deliverArea").modal('show');
    $("#btn_delivery").click(function () {
        delivery();
    });

    if (orderTransferId && orderTransferId.length > 0) {
        getAddress();
    }
});

function getAddress() {
    var url = "/ord_transfer/getAddressDelivery.json";
    var param = {
        orderTransferId: orderTransferId
    };
    var successCallback = function (data) {
        if (data.resultCode == '00') {
            var item = data.data;
            $('#delivery_contactor').val(item.applyContactor);
            $('#delivery_phone').val(item.applyPhone);
            $('#delivery_postcode').val(item.applyPostcode);
            $('#delivery_address').val(item.applyAddress);
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
function delivery() {
    var url = "/ord_transfer/delivery.json";
    var param = {
        orderTransferId: orderTransferId,
        deliveryContactor: $("#delivery_contactor").val(),
        deliveryPhone: $("#delivery_phone").val(),
        deliveryPostcode: $("#delivery_postcode").val(),
        deliveryAddress: $("#delivery_address").val(),
        expressId: $("#express_id").val(),
        expressName: $("#express_name").val(),
        expressNo: $("#express_no").val()
    };
    var successCallback = function (data) {
        if (data.resultCode == '00') {
            $("#deliverArea").modal('hide');
            showAlert('提交成功!');
            deliveryBack();
        } else {
            $("#deliverArea").modal('hide');
            showAlert('提交失败:' + data.resultMessage);
        }
    };
    var errorCallback = function () {
        $("#deliverArea").modal('hide');
        showAlert('网络异常.');
    };
    axse(url, param, successCallback, errorCallback);
}
