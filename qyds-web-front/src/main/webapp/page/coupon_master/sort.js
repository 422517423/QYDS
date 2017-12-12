/**
 * Created by zlh on 16/11/9.
 */

var couponId = sessionStorage.getItem("couponId");
var sortOld = sessionStorage.getItem("sortOld");
$(document).ready(function () {
    $("#changeSort").modal('show');
    $("#sortOld").val(sortOld);
    $("#sort").val(sortOld);
    //表单验证的定义
    $("#sortForm").validate({
        errorElement: 'span',
        errorClass: 'help-block',
        focusInvalid: false,
        rules: {
            sort: {
                required: true
            }

        },
        messages: {
            sort: {
                required: "请输入新序号!"
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

    $("#btn_sort").click(function () {
        if ($("#sortForm").valid()) {
            change_sort();
        }
    });
});

function change_sort() {
    var url = "/coupon_master/setSrot.json";

    var param = {
        couponId: couponId,
        sort: $("#sort").val()
    };

    var success = function (data) {
        if (data.resultCode == '00') {
            $("#changeSort").modal('hide');
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
    $("#change_sort_tip").text(message);
    setTimeout(function () {
        $("#change_grade_tip").text("");
    }, 2000);
}
