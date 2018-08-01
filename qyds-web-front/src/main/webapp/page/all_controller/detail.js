/**
 * Created by dkzhang on 16/8/6.
 */

var allControllerId = sessionStorage.getItem("allControllerId");

$(document).ready(function () {

    $("#allControllerEditArea").modal('show');

    if (allControllerId && allControllerId.length > 0) {
        getDetail();
    }
});

function getDetail() {
    var url = "/all_controller/detail.json";
    var param = {
        allControllerId: allControllerId
    };
    var successCallback = function (data) {
        if (data.resultCode == '00') {
            $("#name_edit").val(data.data.name);
            $("#type_edit").val(data.data.typeName);
            $("#param_one_edit").val(data.data.paramOne);
            $("#param_two_edit").val(data.data.paramTwo);
            $("#param_three_edit").val(data.data.paramThree);
            $("#param_four_edit").val(data.data.paramFour);
            $("#param_five_edit").val(data.data.paramFive);
            $("#comment_edit").val(data.data.comment);
            $("#create_user_name").val(data.data.createUserName);
            $("#update_user_name").val(data.data.updateUserName);
            $("#create_time").val(new Date(data.data.createTime).Format("yyyy-MM-dd hh:mm:ss"));
            $("#update_time").val(new Date(data.data.updateTime).Format("yyyy-MM-dd hh:mm:ss"));
        } else {
            $("#allControllerEditArea").modal('hide');
            showAlert('取得失败,原因:' + data.resultMessage);
        }
    };
    var errorCallback = function () {
        $("#allControllerEditArea").modal('hide');
        showAlert('网络异常.');
    };
    // axse(url, {"data": JSON.stringify(param)}, successCallback, errorCallback);
    axse(url, {"data": JSON.stringify(param)}, successCallback, successCallback);

}
