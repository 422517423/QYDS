/**
 * Created by dkzhang on 16/8/6.
 */

var orderId = sessionStorage.getItem("orderId");
var orderSubId = sessionStorage.getItem("orderSubId");

$(document).ready(function () {

    $("#rejectDispatchArea").modal('show');

    //表单验证的定义
    $("#rejectForm").validate({
        errorElement: 'span',
        errorClass: 'help-block',
        focusInvalid: false,
        rules: {
            reject_content: {
                required: true
            }

        },
        messages: {
            reject_content: {
                required: "请输入拒绝理由"
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
        if ($("#rejectForm").valid()) {
            save();
        }
    });
});

function save() {
    var url = "/ord_dispatch/rejectDeliverSubOrderItem.json";

    var param = {
        orderId: orderId,
        subOrderId:orderSubId,
        content: $("#reject_content").val()
    };

    var success = function (data) {
        if (data.resultCode == '00') {
            $("#rejectDispatchArea").modal('hide');
            showAlert('提交成功!', '提示', function () {
                processedTable.fnDraw();
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
    $("#reject_dispatch_tip").text(message);
    setTimeout(function () {
        $("#reject_dispatch_tip").text("");
    }, 2000);
}
