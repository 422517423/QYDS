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
    getOptionCode("COUPON_TYPE", "coupon_type");
    getOptionCode("APPROVE_STATUS", "approve_status");
});

function initTable() {
    var tableOption = {
        "bProcessing": true,
        "bServerSide": true,
        "fnServerData": fnServerData,
        "bFilter": false,
        "bSort": false,
        "sAjaxSource": "../coupon_master/getApproveList.json",
        "fnServerParams": function (aoData) {
            // 设置参数
            aoData.push({"name": "couponName", "value": $("#coupon_name").val()});
            aoData.push({"name": "couponType", "value": $("#coupon_type").val()});
            aoData.push({"name": "approveStatus", "value": $("#approve_status").val()});
            aoData.push({"name": "isValid", "value": $("#is_valid").val()});
        },
        "aoColumns": [
            {"mData": "couponName"},
            {"mData": "couponTypeCn"},
            {"mData": "allCount"},
            {"mData": "usedCount"},
            {"mData": "unuseCount"},
            {
                "mData": null,
                "fnRender": function (rowData) {
                    if (rowData.aData.isValid == '1') {
                        return "是";
                    } else {
                        return "<span style='color: #f00;'>否</span>";
                    }
                }
            },
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
                    console.log(rowData.aData);
                    var sReturn = '';
                    sReturn += '<a onclick=viewItem("' + rowData.aData.couponId + '")>详细</a>&nbsp;&nbsp;';
                    if ('10' == rowData.aData.approveStatus) {
                        sReturn += '<a onclick=approveConfirm("' + rowData.aData.couponId + '")>通过</a>&nbsp;&nbsp;';
                        sReturn += '<a onclick=rejectConfirm("' + rowData.aData.couponId + '")>驳回</a>&nbsp;&nbsp;';
                    }
                    if ("20" == rowData.aData.approveStatus) {
                        if ("1" == rowData.aData.isValid) {
                            sReturn += '<a onclick=setInvalid("' + rowData.aData.couponId + '")>停用</a>&nbsp;&nbsp;';
                        } else {
                            sReturn += '<a onclick=setValid("' + rowData.aData.couponId + '")>启用</a>&nbsp;&nbsp;';
                        }
                    }
                    sReturn += '<a onclick=deleteConfirm("' + rowData.aData.couponId + '")>删除</a>&nbsp;&nbsp;';
                    return sReturn;
                }
            }
        ]
    };
    table = $('#coupon_master_approve_table').dataTable(tableOption);
}

function viewItem(couponId) {
    sessionStorage.setItem("couponId", couponId);
    $('#content').load('coupon_master/approve_edit.html');
}

function approveConfirm(couponId) {
    showConfirm("确定要通过吗?", function () {
        approveItem(couponId);
    });
}

function approveItem(couponId) {
    var url = "/coupon_master/approve.json";
    var param = {
        "couponId": couponId
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
var rejectCouponId = null;
function rejectConfirm(couponId) {
    $("#reject_dialog_approve_content").val("");
    rejectCouponId = couponId;
    $("#reject_dialog").modal('show');
}

function rejectItem() {
    var url = "/coupon_master/reject.json";
    var param = {
        "couponId": rejectCouponId,
        "approveContent": $("#reject_dialog_approve_content").val()
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

function deleteConfirm(couponId) {
    showConfirm("确定要删除吗?", function () {
        deleteItem(couponId);
    })
}

function deleteItem(couponId) {
    var url = "/coupon_master/delete.json";
    var param = {
        "couponId": couponId
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

function setValid(couponId) {
    var url = "/coupon_master/setValid.json";
    var param = {
        "couponId": couponId
    };
    var success = function (data) {
        if (data.resultCode == '00') {
            showAlert("启用成功!", function () {
                table.fnDraw();
            });
        } else {
            showAlert('启用失败,原因:' + data.resultMessage);
        }
    };
    var error = function () {
        showAlert('启用失败,网络异常.');
    };
    axse(url, {'data': JSON.stringify(param)}, success, error);
}

function setInvalid(couponId) {
    var url = "/coupon_master/setInvalid.json";
    var param = {
        "couponId": couponId
    };
    var success = function (data) {
        if (data.resultCode == '00') {
            showAlert("停用成功!", function () {
                table.fnDraw();
            });
        } else {
            showAlert('停用失败,原因:' + data.resultMessage);
        }
    };
    var error = function () {
        showAlert('停用失败,网络异常.');
    };
    axse(url, {'data': JSON.stringify(param)}, success, error);
}

function showTip(message) {
    $("#reject_dialog_tip").text(message);
    setTimeout(function () {
        $("#reject_dialog_tip").text("");
    }, 2000);
}

