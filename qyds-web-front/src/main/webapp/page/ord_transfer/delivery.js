/**
 * Created by zlh on 16/12/26.
 */

var orderTransferId = sessionStorage.getItem("orderTransferId");
var orderId = sessionStorage.getItem("orderId");
$(document).ready(function () {
    var obj_select = document.getElementById("expressChoose1");
    var obj_div = document.getElementById("mailNoDiv1");
    obj_select.onchange = function(){
        obj_div.style.display = this.value==1? "block" : "none";
        if(this.value==0){
            $("#express_id").val("YTO");
            $("#express_name").val("圆通快递公司");
            $("#express_no").val("");
        }else{
            $("#express_id").val("SF");
            $("#express_name").val("顺丰快递公司");
        }
    };

    //表单验证的定义
    $("#deliverForm1").validate({
        errorElement: 'span',
        errorClass: 'help-block',
        focusInvalid: false,
        rules: {
            express_no: {
                required: true
            }

        },
        messages: {
            express_no: {
                required: "请输入快递单号"
            }
        },

        highlight: function (element) {
            $(element)
                .closest('.form-group').addClass('has-error');
        },

        unhighlight: function (element) {
            $(element)
                .closest('.form-group').removeClass('has-error');
        },

        success: function (label) {
            label
                .closest('.form-group').removeClass('has-error');
        }
    });


    $("#deliverArea").modal('show');
    $("#btn_delivery").click(function () {
        if ($("#deliverForm1").valid()) {
            delivery();
        }
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
        orderId:orderId,
        deliveryContactor: $("#delivery_contactor").val(),
        deliveryPhone: $("#delivery_phone").val(),
        deliveryPostcode: $("#delivery_postcode").val(),
        deliveryAddress: $("#delivery_address").val(),
        expressId: $("#express_id").val(),
        expressName: $("#express_name").val(),
        expressNo: $("#express_no").val(),
        expressType:$("#expressChoose1").val()
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
