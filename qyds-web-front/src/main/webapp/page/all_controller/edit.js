/**
 * Created by dkzhang on 16/8/6.
 */

var allControllerId = sessionStorage.getItem("allControllerId");

$(document).ready(function () {

    $("#allControllerEditArea").modal('show');

    if (allControllerId && allControllerId.length > 0) {
        getTypeSelectCode();
    } else {
        getOptionCode("ALL_CONTROLLER_TYPE", "type_edit");
    }


    //表单验证的定义
    $("#allControllerEditForm").validate({
        errorElement: 'span',
        errorClass: 'help-block',
        focusInvalid: false,
        rules: {
            name_edit: {
                required: true
            }
        },
        messages: {
            name_edit: {
                required: "请输入姓名"
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
        if ($("#allControllerEditForm").valid()) {
            save();
        }
    });
});

function getDetail() {
    var url = "/all_controller/detail.json";
    var param = {
        allControllerId : allControllerId
    };
    var successCallback = function (data) {

        console.log(data);

        if (data.resultCode == '00') {

            $("#name_edit").val(data.data.name);
            $("#type_edit").val(data.data.type);
            $("#param_one_edit").val(data.data.paramOne);
            $("#param_two_edit").val(data.data.paramTwo);
            $("#param_three_edit").val(data.data.paramThree);
            $("#param_four_edit").val(data.data.paramFour);
            $("#param_five_edit").val(data.data.paramFive);
            $("#comment_edit").val(data.data.comment);

        } else {
            $("#allControllerEditArea").modal('hide');
            showAlert('取得失败,原因:' + data.resultMessage);
        }
    };
    var errorCallback = function () {
        $("#allControllerEditArea").modal('hide');
        showAlert('网络异常.');
    };
    axse(url, {"data": JSON.stringify(param)}, successCallback, successCallback);

}

function save() {
    var url = "";
    if (allControllerId && allControllerId.length > 0) {
        url = "/all_controller/edit.json";
    } else {
        url = "/all_controller/save.json";
    }

    var param = {
        allControllerId:allControllerId,
        name:$("#name_edit").val(),
        type:$("#type_edit").val(),
        paramOne:$("#param_one_edit").val(),
        paramTwo:$("#param_two_edit").val(),
        paramThree:$("#param_three_edit").val(),
        paramFour:$("#param_four_edit").val(),
        paramFive:$("#param_five_edit").val(),
        comment:$("#comment_edit").val(),
    };

    var success = function (data) {
        if (data.resultCode == '00') {
            $("#allControllerEditArea").modal('hide');
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
    $("#all_controller_edit_tip").text(message);
    setTimeout(function () {
        $("#all_controller_edit_tip").text("");
    }, 2000);
}

function getTypeSelectCode() {
    var url = "/common/getCodeList.json";
    var param = {"data": 'ALL_CONTROLLER_TYPE'};
    var success = function (data) {
        if (data.resultCode == '00') {
            var array = data.data;
            if (array != null) {
                //为品牌类型选择框添加数据
                $.each(array, function (index, item) {
                    var $option = $('<option>').attr('value', item.value).text(item.displayCn);
                    $("#type_edit").append($option);
                });
            }

            getDetail();
        } else {
            showAlert('码表数据获取失败');
        }
    };
    var error = function () {
        showAlert('码表数据获取失败');
    };
    //品牌类型码表中的数据code为GDS_BRAND_TYPE 每一个码表的code不一样
    axse(url, param, success, error);
}

