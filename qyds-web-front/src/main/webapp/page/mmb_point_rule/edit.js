/**
 * Created by YiLian on 16/7/29.
 */

var ruleId = sessionStorage.getItem("ruleId");
var ruleIsEdit = sessionStorage.getItem("ruleIsEdit");

$(document).ready(function () {

    getOptionCode("POINT_RULE", "rule_code_edit");

    $("#mmbPointRuleEditArea").modal('show');

    if (ruleId && ruleId.length > 0) {
        getDetail();

        $("#rule_code_edit").attr("disabled", true);

        if (ruleIsEdit == '0') {
            $("#btn_save").attr("disabled", true);

            $("#mmb_point_rule_edit_form input,textarea,select").attr("disabled", true);
            $("#rule_code_q").attr("disabled", false);
            $("#btn_save").hide();
        }

    }

    //表单验证的定义
    $("#mmb_point_rule_edit_form").validate({
        errorElement: 'span',
        errorClass: 'help-block',
        focusInvalid: false,
        rules: {
            //rule_code_edit: {
            //    required: true,
            //    maxlength: 2,
            //    max: 99,
            //    min: 0
            //},
            //rule_name_edit: {
            //    required: true,
            //    maxlength: 60
            //},
            point_edit: {
                required: true
            },
            comment_edit: {
                maxlength: 200
            }
        },
        messages: {
            //rule_code_edit: {
            //    required: "请输入规则编码",
            //    maxlength: "编码规则最大长度不能超过2",
            //    max: "规则编码建议采用0到99数字",
            //    min: "规则编码建议采用0到99数字"
            //},
            //rule_name_edit: {
            //    required: "请输入规则名称",
            //    maxlength: "规则名称最大长度不能超过60"
            //},
            point_edit: {
                required: "请输入积分值"
            },
            comment_edit: {
                maxlength: "说明文字长度不能超过200"
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

        if ($("#mmb_point_rule_edit_form").valid()) {
            save();
        }
    });

});

function getDetail() {
    var url = "/mmb_point_rule/getDetail.json";
    var param = {
        ruleId: ruleId
    };
    var success = function (data) {
        if (data.resultCode == '00') {
            $("#rule_code_edit").val(data.data.ruleCode);
            $("#rule_name_edit").val(data.data.ruleName);
            $("#point_edit").val(data.data.point);
            $("#comment_edit").val(data.data.comment);
        } else {
            $("#mmbPointRuleEditArea").modal('hide');
            showAlert('取得失败,原因:' + data.resultMessage);
        }
    };
    var error = function () {
        $("#mmbPointRuleEditArea").modal('hide');
        showAlert('网络异常.');
    };
    axse(url, {"data": JSON.stringify(param)}, success, error);
}

function save() {
    var url = null;
    // 编辑
    if (ruleId && ruleId.length > 0) {
        url = "/mmb_point_rule/edit.json";
    } else {
        ruleId = null;
        url = "/mmb_point_rule/add.json";
    }

    var param = {
        ruleId: ruleId,
        ruleCode: $("#rule_code_edit").val(),
        // ruleName: $("#rule_name_edit").val(),
        point: $("#point_edit").val(),
        comment: $("#comment_edit").val()
    };
    var success = function (data) {
        if (data.resultCode == '00') {
            $("#mmbPointRuleEditArea").modal('hide');
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
    $("#mmb_point_rule_edit_tip").text(message);
    setTimeout(function () {
        $("#mmb_point_rule_edit_tip").text("");
    }, 2000);
}