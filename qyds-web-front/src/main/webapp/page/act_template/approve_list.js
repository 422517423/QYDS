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
    getOptionCode("ACTITION_TYPE", "actition_type");
    getOptionCode("APPROVE_STATUS", "approve_status");
});

function initTable() {
    var tableOption = {
        "bProcessing": true,
        "bServerSide": true,
        "fnServerData": fnServerData,
        "bFilter": false,
        "bSort": false,
        "sAjaxSource": "../act_template/getApproveList.json",
        "fnServerParams": function (aoData) {
            // 设置参数
            aoData.push({"name": "tempName", "value": $("#temp_name").val()});
            aoData.push({"name": "actitionType", "value": $("#actition_type").val()});
            aoData.push({"name": "approveStatus", "value": $("#approve_status").val()});
        },
        "aoColumns": [
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
                    sReturn += '<a onclick=viewItem("' + rowData.aData.tempId + '")>详细</a>&nbsp;&nbsp;';
                    if ('10' == rowData.aData.approveStatus) {
                        sReturn += '<a onclick=approveConfirm("' + rowData.aData.tempId + '")>通过</a>&nbsp;&nbsp;';
                        sReturn += '<a onclick=rejectConfirm("' + rowData.aData.tempId + '")>驳回</a>&nbsp;&nbsp;';
                    }
                    return sReturn;
                }
            }
        ]
    };
    table = $('#act_template_approve_table').dataTable(tableOption);
}

function viewItem(tempId) {
    sessionStorage.setItem("tempId", tempId);
    $('#content').load('act_template/approve_edit.html');
}

function approveConfirm(tempId) {
    showConfirm("确定要通过吗?", function () {
        approveItem(tempId);
    });
}

function approveItem(tempId) {
    var url = "/act_template/approve.json";
    var param = {
        "tempId": tempId
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
function rejectConfirm(tempId) {
    $("#reject_dialog_approve_content").val("");
    rejectTempId = tempId;
    $("#reject_dialog").modal('show');
}

function rejectItem() {
    var url = "/act_template/reject.json";
    var param = {
        "tempId": rejectTempId,
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

function showTip(message) {
    $("#reject_dialog_tip").text(message);
    setTimeout(function () {
        $("#reject_dialog_tip").text("");
    }, 2000);
}

