/**
 * Created by wy on 2016/8/7.
 */
var bankId = sessionStorage.getItem("bankId");
var userId = sessionStorage.getItem("userId");
$(document).ready(function () {
    $("#bnkMasterEditArea").modal('show');

    // if (goodsId && goodsId.length > 0) {
    //     getNewCount();
    // }
    //表单验证的定义
    $("#bnk_master_edit_form").validate({
        errorElement: 'span',
        errorClass: 'help-block',
        focusInvalid: false,
        rules: {
            new_count: {
                required: true,
                digits: true
            }
        },
        messages: {
            new_count: {
                required: "请输入最新库存数",
                digits: "请输入正确库存"
            },
        },
        highlight: function (element) { // hightlight error inputs
            $(element)
                .closest('.form-group').addClass('has-error'); // set error class to the control group
        },

        unhighlight: function (element) { // revert the change done by hightlight
            $(element)
                .closest('.form-group').removeClass('has-error'); // set error class to the control group
        },

        success: function (label) {
            label
                .closest('.form-group').removeClass('has-error'); // set success class to the control group
        }
    });
});
// function getNewCount() {
//     axse("/bnk_master/edit.json", {"bankId": bankId}, getNewCountSuccessFn, errorFn);
// }
// function getNewCountSuccessFn(data) {
//     if (data.resultCode == '00') {
//         $("#new_count").val(data.data.newCount);
//     } else {
//         showAlert('数据获取失败');
//     }
// }
$("#btn_save").click(function () {
    if (!$("#bnk_master_edit_form").valid()){
        return;
    }

    $("#bnkMasterEditArea").modal('hide');
    var json = {};
    json.bankId = bankId;
    json.newCount = $("#new_count").val();
    json.comment = $("#comment").val();
    json.updateUserId = userId;
    axse("/bnk_master/update.json", {"data": JSON.stringify(json)}, getdataSuccessFn, errorFn);
})
//数据保存成功的回调方法
function getdataSuccessFn(data) {
    gotoListPage();
}
//退回到列表画面
function gotoListPage() {
    $('#content').load('bnk_master/list.html');
}