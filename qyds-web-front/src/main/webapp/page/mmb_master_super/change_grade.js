/**
 * Created by zlh on 16/11/5.
 */

var memberId = sessionStorage.getItem("memberId");

$(document).ready(function () {
    $("#changeGrade").modal('show');
    getOptionCode("MEMBER_LEVEL", "member_level_id");
    member_level_id.val=sessionStorage.getItem("grade");
    $("#member_level_id").val(sessionStorage.getItem("grade"));
    //表单验证的定义
    $("#gradeForm").validate({
        errorElement: 'span',
        errorClass: 'help-block',
        focusInvalid: false,
        //rules: {
        //    cond_member_level_id: {
        //        required: true
        //    }
        //
        //},
        messages: {
            cond_member_level_id: {
                required: "请选择会员级别!"
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

    $("#btn_grade").click(function () {
        if ($("#gradeForm").valid()) {
            grade();
        }
    });
});

function grade() {
    var url = "/mmb_master/changeGrade.json";

    var param = {
        memberId: memberId,
        memberLevelId: $("#member_level_id").val()
    };

    var success = function (data) {
        if (data.resultCode == '00') {
            $("#changeGrade").modal('hide');
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
    $("#change_grade_tip").text(message);
    setTimeout(function () {
        $("#change_grade_tip").text("");
    }, 2000);
}
