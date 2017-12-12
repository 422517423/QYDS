/**
 * Created by YiLian on 16/7/29.
 */

var groupId = sessionStorage.getItem("groupId");
var groupIsEdit = sessionStorage.getItem("groupIsEdit");

$(document).ready(function () {

    $("#mmbGroupEditArea").modal('show');
    getOptionCode("MEMBER_GROUP_TYPE", "type_edit");

    if (groupId && groupId.length > 0) {
        getDetail();

        if (groupIsEdit == '0') {
            $("#btn_save").attr("disabled", true);

            $("#mmb_group_edit_form input,textarea,select").attr("disabled", true);

            $("#btn_save").hide();
        }

    }

    //表单验证的定义
    $("#mmb_group_edit_form").validate({
        errorElement: 'span',
        errorClass: 'help-block',
        focusInvalid: false,
        rules: {
            group_name_edit: {
                required: true,
                maxlength: 20
            },
            type_edit: {
                required: true
            }
        },
        messages: {
            group_name_edit: {
                required: "请输入会员组名称",
                max: "会员组名称长度不能超过50"
            },
            type_edit: {
                required: "请选择会员组类型"
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

        if ($("#mmb_group_edit_form").valid()) {
            save();
        }
    });

});

function getDetail() {
    var url = "/mmb_group/getDetail.json";
    var param = {
        groupId: groupId
    };
    var success = function (data) {
        if (data.resultCode == '00') {
            $("#group_name_edit").val(data.data.groupName);
            $("#type_edit").val(data.data.type);
        } else {
            $("#mmbGroupEditArea").modal('hide');
            showAlert('取得失败,原因:' + data.resultMessage);
        }
    };
    var error = function () {
        $("#mmbGroupEditArea").modal('hide');
        showAlert('网络异常.');
    };
    axse(url, {"data": JSON.stringify(param)}, success, error);
}

function save() {
    var url = null;
    // 编辑
    if (groupId && groupId.length > 0) {
        url = "/mmb_group/edit.json";
    } else {
        groupId = null;
        url = "/mmb_group/add.json";
    }

    var param = {
        groupId: groupId,
        type: $("#type_edit").val(),
        groupName: $("#group_name_edit").val()
    };
    var success = function (data) {
        if (data.resultCode == '00') {
            $("#mmbGroupEditArea").modal('hide');
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
    $("#mmb_group_edit_tip").text(message);
    setTimeout(function () {
        $("#mmb_group_edit_tip").text("");
    }, 2000);
}