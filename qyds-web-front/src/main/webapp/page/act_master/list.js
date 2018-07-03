var table;
$(document).ready(function () {
    initTable();
    //新建按钮的点击事件
    $("#btn_add").click(function () {
        addItem();
    });

    //检索按钮点击事件
    $("#btn_search").click(function () {
        table.fnDraw();
    });
    $("#apply_dialog_btn_apply").click(function () {
        applyItem();
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
        "sAjaxSource": "../act_master/getList.json",
        "fnServerParams": function (aoData) {
            // 设置参数
            aoData.push({"name": "tempName", "value": $("#temp_name").val()});
            aoData.push({"name": "activityName", "value": $("#activity_name").val()});
            aoData.push({"name": "actitionType", "value": $("#activity_type").val()});
            aoData.push({"name": "approveStatus", "value": $("#approve_status").val()});
        },
        "aoColumns": [
            {"mData": "sort"},
            {"mData": "activityName"},
            {
                "mData": "startTime",
                "fnRender": function (rowData) {
                    if (rowData.aData.startTime != null) {
                        return new Date(rowData.aData.startTime).Format("yyyy-MM-dd");
                    } else {
                        return "";
                    }
                }
            },
            {
                "mData": "endTime",
                "fnRender": function (rowData) {
                    if (rowData.aData.endTime != null) {
                        return new Date(rowData.aData.endTime).Format("yyyy-MM-dd");
                    } else {
                        return "";
                    }
                }
            },
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
                    sReturn += '<a onclick=change_sort("' + rowData.aData.activityId + '","'+((rowData.aData.sort || rowData.aData.sort==0)?rowData.aData.sort:'')+'")>修改顺序</a>&nbsp;&nbsp;';
                    sReturn += '<a onclick=editItem("' + rowData.aData.activityId + '")>编辑</a>&nbsp;&nbsp;';
                    // 未申请和已申请状态和驳回的才可以编辑
                    if ('40' == rowData.aData.approveStatus || '10' == rowData.aData.approveStatus || '30' == rowData.aData.approveStatus) {
                        if('11' == rowData.aData.actitionType){
                            sReturn += '<a onclick=uploadSecKillItem("' + rowData.aData.activityId + '","' + rowData.aData.activityName + '")>商品导入</a>&nbsp;&nbsp;';
                        } else if ('50' == rowData.aData.goodsType){
                            sReturn += '<a onclick=uploadSKUItem("' + rowData.aData.activityId + '","' + rowData.aData.activityName + '")>批量SKU导入</a>&nbsp;&nbsp;';
                        }
                    }

                    if ('50' == rowData.aData.goodsType){
                        sReturn += '<a onclick=exportSKUItem("' + rowData.aData.activityId +'")>导出SKU</a>&nbsp;&nbsp;';
                    }

                    // 未申请状态的才可以申请
                    if ('40' == rowData.aData.approveStatus) {
                        sReturn += '<a onclick=applyConfirm("' + rowData.aData.activityId + '")>申请</a>&nbsp;&nbsp;';
                    }
                    // 未申请和已申请状态和驳回的的才可以删除
                    if ('40' == rowData.aData.approveStatus || '10' == rowData.aData.approveStatus || '30' == rowData.aData.approveStatus) {
                        sReturn += '<a onclick=deleteConfirm("' + rowData.aData.activityId + '")>删除</a>&nbsp;&nbsp;';
                    }

                    return sReturn;
                }
            }

        ]
    };
    table = $('#act_master_table').dataTable(tableOption);
}

function addItem() {
    sessionStorage.setItem("activityId", "");
    sessionStorage.setItem("editable", "1");
    $('#content').load('act_master/edit.html');
}

function viewItem(activityId) {
    sessionStorage.setItem("activityId", activityId);
    sessionStorage.setItem("editable", "0");
    $('#content').load('act_master/edit.html');
}

function change_sort(activityId,sort) {
    sessionStorage.setItem("activityId", activityId);
    sessionStorage.setItem("sortOld", sort);
    $('#customDialog').load('act_master/sort.html');
}

function editItem(activityId) {
    sessionStorage.setItem("activityId", activityId);
    sessionStorage.setItem("editable", "1");
    $('#content').load('act_master/edit.html');
}

function uploadSecKillItem(activityId, activityName){
    sessionStorage.setItem("activityId", activityId);
    sessionStorage.setItem("activityName", activityName);
    $('#customDialog').load('act_master/uploadSecKill.html');
}

function uploadSKUItem(activityId, activityName){
    sessionStorage.setItem("activityId", activityId);
    sessionStorage.setItem("activityName", activityName);
    $('#customDialog').load('act_master/uploadSKU.html');
}

function exportSKUItem(activityId){
    window.open("/qyds-web-front/act_master/exportSKUFile.json?activityId=" + activityId);
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

var applyTempId;
function applyConfirm(activityId) {
    $("#apply_dialog_apply_content").val("");
    applyTempId = activityId;
    $("#apply_dialog").modal('show');
}

function applyItem() {
    var url = "/act_master/apply.json";
    var param = {
        "activityId": applyTempId,
        "applyContent": $("#apply_dialog_apply_content").val()
    };
    var success = function (data) {
        if (data.resultCode == '00') {
            $("#apply_dialog").modal('hide');
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
    $("#apply_dialog_tip").text(message);
    setTimeout(function () {
        $("#apply_dialog_tip").text("");
    }, 2000);
}