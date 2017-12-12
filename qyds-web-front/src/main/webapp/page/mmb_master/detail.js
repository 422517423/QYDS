/**
 * Created by dkzhang on 16/8/6.
 */

var memberId = sessionStorage.getItem("memberId");

$(document).ready(function () {

    $("#mmbMasterDetailArea").modal('show');

    if (memberId && memberId.length > 0) {
        getDetail();
    }
});

function getDetail() {
    var url = "/mmb_master/detail.json";
    var param = {
        memberId: memberId
    };
    var successCallback = function (data) {
        if (data.resultCode == '00') {
            $("#open_id").val(data.data.openId);
            $("#member_name").val(data.data.memberName);
            $("#type_name").val(data.data.typeName);
            $("#sex").val(data.data.sexName);

            $("#telephone").val(data.data.telephone);
            $("#point").val(data.data.point);
            $("#all_point").val(data.data.allPoint);
            $("#member_level_id").val(data.data.memberLevelName);
            if (data.data.birthdate) {
                $("#birthdate").val(new Date(data.data.birthdate).Format("yyyy-MM-dd"));
            }

            $("#email").val(data.data.email);

            $("#profession").val(data.data.profession);
            $("#income").val(data.data.income);
            $("#postCode").val(data.data.postCode);
            $("#address").val(data.data.address);

            $("#provinceName").val(data.data.provinceName);
            $("#cityName").val(data.data.cityName);
            $("#districtName").val(data.data.districtName);

            $("#insert_time").val(new Date(data.data.insertTime).Format("yyyy-MM-dd hh:mm:ss"));

        } else {
            $("#mmbMasterDetailArea").modal('hide');
            showAlert('取得失败,原因:' + data.resultMessage);
        }
    };
    var errorCallback = function () {
        $("#mmbMasterDetailArea").modal('hide');
        showAlert('网络异常.');
    };
    // axse(url, {"data": JSON.stringify(param)}, successCallback, errorCallback);
    axse(url, {"data": JSON.stringify(param)}, successCallback, successCallback);

}
