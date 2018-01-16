/**
 * Created by dkzhang on 16/8/6.
 */

var orderId = sessionStorage.getItem("orderId");
var orderSubId = sessionStorage.getItem("orderSubId");

$(document).ready(function () {
    var obj_select = document.getElementById("expressChoose");
    var obj_div = document.getElementById("mailNoDiv");
    obj_select.onchange = function(){
        console.log("12312312313");
        obj_div.style.display = this.value==1? "block" : "none";
    };

    $("#subOrderDeliverArea").modal('show');

    getDetail();

    //表单验证的定义
    $("#deliverForm").validate({
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

    $("#btn_save").click(function () {
        if ($("#deliverForm").valid()) {
            save();
        }
    });
});

function getDetail() {
    var url = "/ord_dispatch/getSubOrderDeliverInfo.json";
    var param = {
        orderId: orderId,
        subOrderId:orderSubId
    };

    console.log(param);
    var successCallback = function (data) {

        console.log(data);

        if (data.resultCode == '00') {

            var item = data.data;
            var subItem = item.subList[0];
            $("#goods_code_processed").val(subItem.goodsCode);
            $("#goods_name_processed").val(subItem.goodsName);
            $("#sku_processed").val(subItem.sku);
            $("#delivery_contactor_processed").val(item.deliveryContactor);
            $("#district_name_processed").val(item.deliveryAddress);

        } else {
            $("#subOrderDeliverArea").modal('hide');
            showAlert('取得失败,原因:' + data.resultMessage);
        }
    };
    var errorCallback = function () {
        $("#subOrderDeliverArea").modal('hide');
        showAlert('网络异常.');
    };
    axse(url, {"data": JSON.stringify(param)}, successCallback, errorCallback);
}

function save() {
    var url = "/ord_dispatch/deliverSubOrderItem.json";

    var param = {
        orderId: orderId,
        subOrderId:orderSubId,
        expressNo: $("#express_no").val()
    };

    $('#btn_save').attr("disabled", true);
    var success = function (data) {
        if (data.resultCode == '00') {
            $('#btn_save').attr("disabled", false);
            $("#subOrderDeliverArea").modal('hide');
            showAlert('提交成功!', '提示', function () {
            });
            getData();
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
    $("#processed_deliver_tip").text(message);
    setTimeout(function () {
        $("#processed_deliver_tip").text("");
    }, 2000);
}
