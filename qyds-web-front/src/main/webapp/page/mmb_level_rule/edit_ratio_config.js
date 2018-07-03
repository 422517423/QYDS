/**
 * Created by YiLian on 16/7/29.
 */

var rule_form = {
    code:null,
    type:null
};

$(document).ready(function () {

    $("#mmbLevelRuleRatioEditArea").modal('show');

    getDetail();

    //表单验证的定义
    $("#mmb_level_rule_ratio_edit_form").validate({
        errorElement: 'span',
        errorClass: 'help-block',
        focusInvalid: false,
        rules: {
            ratio_edit: {
                required: true,
                max: 1.00,
                min: 0.01
            }
        },
        messages: {
            ratio_edit: {
                required: "请输入保级系数",
                max: "保级系数不能大于1.00",
                min: "保级系数不能小于0.01"
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

        if ($("#mmb_level_rule_ratio_edit_form").valid()) {
            save();
        }
    });

});

function getDetail() {
    var url = "/com_config/getLevelRatio.json";

    var success = function (data) {
        if (data.resultCode == '00') {
            rule_form = data.data;
            $("#ratio_edit").val(data.data.param);
        } else {
            $("#mmbLevelRuleRatioEditArea").modal('hide');
            showAlert('取得失败,原因:' + data.resultMessage);
        }
    };
    var error = function () {
        $("#mmbLevelRuleRatioEditArea").modal('hide');
        showAlert('网络异常.');
    };
    axse(url, null, success, error);
}

function save() {

    var url = "/com_config/editLevelRatio.json";

    var param = {
        code:rule_form.code,
        type:rule_form.type,
        param: $("#ratio_edit").val()
    };

    var success = function (data) {
        if (data.resultCode == '00') {
            $("#mmbLevelRuleRatioEditArea").modal('hide');
            showAlert('提交成功!', '提示', function () {

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
    $("#mmb_level_rule_ratio_edit_tip").text(message);
    setTimeout(function () {
        $("#mmb_level_rule_ratio_edit_tip").text("");
    }, 2000);
}