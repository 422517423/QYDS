/**
 * Created by zlh on 16/11/9.
 */

var activityId = sessionStorage.getItem("activityId");
var sortOld = sessionStorage.getItem("sortOld");

$(document).ready(function () {
    $("#changeActSort").modal('show');
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

    $("#btn_actsort").click(function () {
        if ($("#sortForm").valid()) {
            set_sort();
        }
    });
});

function set_sort() {
    var url = "/act_master/setSrot.json";
    var param = {
        activityId: activityId,
        sort: $("#sort").val()
    };

    var success = function (data) {
        if (data.resultCode == '00') {
            $("#changeActSort").modal('hide');
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
