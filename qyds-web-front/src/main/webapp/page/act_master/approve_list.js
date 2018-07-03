var table;
$(document).ready(function () {
    initTable();

    //检索按钮点击事件
    $("#btn_search").click(function () {
        table.fnDraw();
    });


    $("#reject_dialog_btn_reject").click(function () {
        rejectItem();
    });

    // 获取下拉列表的码表
    getOptionCode("ACTITION_TYPE", "activity_type");
    getOptionCode("APPROVE_STATUS", "approve_status");
});

function initTable() {
    var tableOption = {
        "bProcessing": true,
        "bServerSide": true,
        "fnServerData": fnServerData,
        "bFilter": false,
        "bSort": false,
        "sAjaxSource": "../act_master/getApproveList.json",
        "fnServerParams": function (aoData) {
            // 设置参数
            // 设置参数
            aoData.push({"name": "tempName", "value": $("#temp_name").val()});
            aoData.push({"name": "activityName", "value": $("#activity_name").val()});
            aoData.push({"name": "actitionType", "value": $("#activity_type").val()});
            aoData.push({"name": "approveStatus", "value": $("#approve_status").val()});
        },
        "aoColumns": [
            {"mData": "activityName"},
            {"mData": "tempName"},
            {"mData": "actitionTypeCn"},
            {"mData": "applyUserName"},
            {
                "mData": "applyTime",
                "fnRender": function (rowData) {
                    if (rowData.aData.applyTime != null) {
                        return new Date(rowData.aData.applyTime).Format("yyyy-MM-dd hh:mm:ss");
                    } else {
                        return "";
                    }
                }
            },
            {"mData": "approveStatusCn"},
            {"mData": "approveUserName"},
            {
                "mData": "approveTime",
                "fnRender": function (rowData) {
                    if (rowData.aData.approveTime != null) {
                        return new Date(rowData.aData.approveTime).Format("yyyy-MM-dd hh:mm:ss");
                    } else {
                        return "";
                    }
                }

            }, {
                "mData": null,
                "fnRender": function (rowData) {
                    var sReturn = '';
                    sReturn += '<a onclick=viewItem("' + rowData.aData.activityId + '")>详细</a>&nbsp;&nbsp;';
                    if ('10' == rowData.aData.approveStatus) {
                        sReturn += '<a onclick=approveConfirm("' + rowData.aData.activityId + '")>通过</a>&nbsp;&nbsp;';
                        sReturn += '<a onclick=rejectConfirm("' + rowData.aData.activityId + '")>驳回</a>&nbsp;&nbsp;';
                    }
                    sReturn += '<a onclick=deleteConfirm("' + rowData.aData.activityId + '")>删除</a>&nbsp;&nbsp;';
                    return sReturn;
                }
            }
        ]
    };
    table = $('#act_master_approve_table').dataTable(tableOption);
}

function viewItem(activityId) {
    sessionStorage.setItem("activityId", activityId);
    $('#content').load('act_master/approve_edit.html');
}

function approveConfirm(activityId) {
    showConfirm("确定要通过吗?", function () {
        approveItem(activityId);
    });
}

function approveItem(activityId) {
    var url = "/act_master/approve.json";
    var param = {
        "activityId": activityId
    };
    var success = function (data) {
        if (data.resultCode == '00') {
            showAlert("提交成功!", function () {
                table.fnDraw();
            });
        } else {
            showAlert('提交失败,原因:' + data.resultMessage);
        }
    };
    var error = function () {
        showAlert('提交失败,网络异常.');
    };
    axse(url, {'data': JSON.stringify(param)}, success, error);
}
var rejectTempId = null;
function rejectConfirm(activityId) {
    $("#reject_dialog_approve_content").val("");
    rejectTempId = activityId;
    $("#reject_dialog").modal('show');
}

function rejectItem() {
    var url = "/act_master/reject.json";
    var param = {
        "activityId": rejectTempId,
        "approveContent":$("#reject_dialog_approve_content").val()
    };
    var success = function (data) {
        if (data.resultCode == '00') {
            $("#reject_dialog").modal('hide');
            showAlert("提交成功!", function () {
                table.fnDraw();
            });
        } else {
            showTip('提交失败,原因:' + data.resultMessage);
        }
    };
    var error = function () {
        showTip('提交失败,网络异常.');
    };
    axse(url, {'data': JSON.stringify(param)}, success, error);
}


function deleteConfirm(activityId) {
    showConfirm("确定要删除吗?", function () {
        deleteItem(activityId);
    })
}

function deleteItem(activityId) {
    var url = "/act_master/delete.json";
    var param = {
        "activityId": activityId
    };
    var success = function (data) {
        if (data.resultCode == '00') {
            showAlert("删除成功!", function () {
                table.fnDraw();
            });
        } else {
            showAlert('删除失败,原因:' + data.resultMessage);
        }
    };
    var error = function () {
        showAlert('删除失败,网络异常.');
    };
    axse(url, {'data': JSON.stringify(param)}, success, error);
}

function showTip(message) {
    $("#reject_dialog_tip").text(message);
    setTimeout(function () {
        $("#reject_dialog_tip").text("");
    }, 2000);
}

