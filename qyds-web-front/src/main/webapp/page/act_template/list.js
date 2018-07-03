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
        "sAjaxSource": "../act_template/getList.json",
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
                    // 未申请和已申请状态的才可以编辑
                    if ('40' == rowData.aData.approveStatus || '10' == rowData.aData.approveStatus || '30' == rowData.aData.approveStatus) {
                        sReturn += '<a onclick=editItem("' + rowData.aData.tempId + '")>编辑</a>&nbsp;&nbsp;';
                        // 满送货品可导入SKU
                        if ('42' == rowData.aData.actitionType){
                            sReturn += '<a onclick=uploadSKUItem("' + rowData.aData.tempId + '","' + rowData.aData.tempName + '")>批量SKU导入</a>&nbsp;&nbsp;';
                        }
                    }
                    // 满送货品可导出SKU
                    if ('42' == rowData.aData.actitionType){
                        sReturn += '<a onclick=exportSKUItem("' + rowData.aData.tempId +'")>导出SKU</a>&nbsp;&nbsp;';
                    }
                    // 未申请状态的才可以申请
                    if ('40' == rowData.aData.approveStatus) {
                        sReturn += '<a onclick=applyConfirm("' + rowData.aData.tempId + '")>申请</a>&nbsp;&nbsp;';
                    }
                    // 未申请和已申请状态的才可以删除
                    if ('40' == rowData.aData.approveStatus || '10' == rowData.aData.approveStatus || '30' == rowData.aData.approveStatus) {
                        sReturn += '<a onclick=deleteConfirm("' + rowData.aData.tempId + '")>删除</a>&nbsp;&nbsp;';
                    }
                    return sReturn;
                }
            }

        ]
    };
    table = $('#act_template_table').dataTable(tableOption);
}

function addItem() {
    sessionStorage.setItem("tempId", "");
    sessionStorage.setItem("editable", "1");
    $('#content').load('act_template/edit.html');
}

function viewItem(tempId) {
    sessionStorage.setItem("tempId", tempId);
    sessionStorage.setItem("editable", "0");
    $('#content').load('act_template/edit.html');
}

function editItem(tempId) {
    sessionStorage.setItem("tempId", tempId);
    sessionStorage.setItem("editable", "1");
    $('#content').load('act_template/edit.html');
}

function deleteConfirm(tempId) {
    showConfirm("确定要删除吗?", function () {
        deleteItem(tempId);
    })
}

function deleteItem(tempId) {
    var url = "/act_template/delete.json";
    var param = {
        "tempId": tempId
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
function applyConfirm(tempId) {
    $("#apply_dialog_apply_content").val("");
    applyTempId = tempId;
    $("#apply_dialog").modal('show');
}

function applyItem() {
    var url = "/act_template/apply.json";
    var param = {
        "tempId": applyTempId,
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


function uploadSKUItem(tempId, tempName){
    sessionStorage.setItem("tempId", tempId);
    sessionStorage.setItem("tempName", tempName);
    $('#customDialog').load('act_template/uploadSKU.html');
}

function exportSKUItem(tempId){
    window.open("/qyds-web-front/act_template/exportSKUFile.json?tempId=" + tempId);
}
