/**
 * Created by YiLian on 2016/7/18.
 */
var table;
$(document).ready(function () {

    getOptionCode("MEMBER_LEVEL", "member_level_code");

    initTable();

    //检索按钮点击事件
    $("#btn_search").click(function () {
        //刷新Datatable，会自动激发retrieveData
        table.fnDraw();
    });
});

function initTable() {
    var tableOption = {
        "bProcessing": true,
        "bServerSide": true,
        "fnServerData": fnServerData,
        "bFilter": false,
        "bSort": false,
        "sAjaxSource": "../mmb_level/getList.json",
        "fnServerParams": function (aoData) {
            // 设置参数
            aoData.push({"name": "currentLevelId", "value": $("#member_level_code").val()})
        },
        "aoColumns": [
            {"mData": "memberName"},
            {"mData": "telephone"},
            {"mData": "allPoint"},
            {"mData": "pointSingle"},
            {"mData": "currentLevelName"},
            {"mData": "approvalLevelName"},
            {
                "mData": null,
                "fnRender": function (rowData) {

                    return '<a onclick=approvalItem("' + rowData.aData.memberId + '","' + rowData.aData.approvalLevelId + '");>确认</a>'
                    + '&nbsp;&nbsp; <a onclick=ponit_record("' + rowData.aData.memberId + '");>积分履历</a>';

                }
            }

        ]
    };
    table = $('#mmb_level_table').dataTable(tableOption);
}

function approvalItem(memberId, approvalLevelId) {
    var url = "/mmb_level/approval.json";
    var param = {
        "memberId": memberId,
        "approvalLevelId": approvalLevelId
    };
    var success = function (data) {
        if (data.resultCode == '00') {
            showAlert("升级成功!", function () {
                table.fnDraw();
            });
        } else {
            showAlert('操作失败,原因:' + data.resultMessage);
        }
    };
    var error = function () {
        showAlert('操作失败,网络异常.');
    };
    axse(url, {'data': JSON.stringify(param)}, success, error);
}

function ponit_record(memberId) {

    sessionStorage.setItem("memberId", memberId);
    $('#customDialog').load('mmb_master/point_record.html');
}
