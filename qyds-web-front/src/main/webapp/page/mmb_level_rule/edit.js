/**
 * Created by YiLian on 16/7/29.
 */

var memberLevelId = sessionStorage.getItem("memberLevelId");
var memberLevelIsEdit = sessionStorage.getItem("memberLevelIsEdit");

$(document).ready(function () {

    $("#mmbLevelRuleEditArea").modal('show');

    getOptionCode("MEMBER_LEVEL", "member_level_code_edit");

    if (memberLevelId && memberLevelId.length > 0) {
        getDetail();

        $("#member_level_code_edit").attr("disabled", true);

        if (memberLevelIsEdit == '0') {
            $("#btn_save").attr("disabled", true);

            $("#mmb_level_rule_edit_form input,textarea,select").attr("disabled", true);
            $("#member_level_code").attr("disabled",false);
            $("#btn_save").hide();
        }

    }

    //表单验证的定义
    $("#mmb_level_rule_edit_form").validate({
        errorElement: 'span',
        errorClass: 'help-block',
        focusInvalid: false,
        rules: {
            //member_level_code_edit: {
            //    required: true,
            //    maxlength: 20
            //},
            //member_level_name_edit: {
            //    required: true,
            //    maxlength: 50
            //},
            point_radio_edit: {
                required: true,
                max: 999.99,
                min: 1.00
            },
            point_lower_edit: {
                required: true
            },
            //point_upper_edit: {
            //    required: true
            //},
            discount_edit: {
                required: true,
                max: 1.00,
                min: 0.01

            },
            comment_edit: {
                maxlength: 200
            }

        },
        messages: {
            //member_level_code_edit: {
            //    required: "请输入级别代码",
            //    maxlength: "级别代码最大长度不能超过20"
            //},
            //member_level_name_edit: {
            //    required: "请输入级别名称",
            //    maxlength: "级别名称最大长度不能超过50"
            //},
            point_radio_edit: {
                required: "请输入积分系数",
                max: "积分系数不能大于999.99",
                min: "积分系数不能小于1.00"
            },
            point_lower_edit: {
                required: "请输入积分下限值"
            },
            //point_upper_edit: {
            //    required: "请输入积分上限值"
            //},
            discount_edit: {
                required: "请输入优惠折扣",
                max: "优惠折扣不能大于1.00",
                min: "优惠折扣不能小于0.01"
            },
            comment_edit: {
                maxlength: "说明最大长度不能超过200"
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

        if ($("#mmb_level_rule_edit_form").valid()) {
            save();
        }
    });

});

function getDetail() {
    var url = "/mmb_level_rule/getDetail.json";
    var param = {
        memberLevelId: memberLevelId
    };
    var success = function (data) {
        if (data.resultCode == '00') {
            $("#member_level_code_edit").val(data.data.memberLevelCode);
            $("#point_radio_edit").val(data.data.pointRatio);
            $("#point_single_edit").val(data.data.pointSingle);
            $("#point_lower_edit").val(data.data.pointLower);
            $("#point_upper_edit").val(data.data.pointUpper);
            $("#discount_edit").val(data.data.discount);
            $("#comment_edit").val(data.data.comment);
            /*20171214*/
            $("#point_cumulative_edit").val(data.data.pointCumulative);
        } else {
            $("#mmbLevelRuleEditArea").modal('hide');
            showAlert('取得失败,原因:' + data.resultMessage);
        }
    };
    var error = function () {
        $("#mmbLevelRuleEditArea").modal('hide');
        showAlert('网络异常.');
    };
    axse(url, {"data": JSON.stringify(param)}, success, error);
}

function save() {

    var url = "";
    if (memberLevelId && memberLevelId.length > 0) {
        url = "/mmb_level_rule/edit.json";
    } else {
        url = "/mmb_level_rule/add.json";
    }

    var param = {
        memberLevelId: memberLevelId,
        memberLevelCode: $("#member_level_code_edit").val(),
        pointRatio: $("#point_radio_edit").val(),
        pointSingle: $("#point_single_edit").val(),
        /* 20171214*/
        pointCumulative: $("#point_cumulative_edit").val(),
        pointLower: $("#point_lower_edit").val(),
        pointUpper: $("#point_upper_edit").val(),
        discount: $("#discount_edit").val(),
        comment: $("#comment_edit").val()
    };

    var success = function (data) {
        if (data.resultCode == '00') {
            $("#mmbLevelRuleEditArea").modal('hide');
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
    $("#mmb_level_rule_edit_tip").text(message);
    setTimeout(function () {
        $("#mmb_level_rule_edit_tip").text("");
    }, 2000);
}