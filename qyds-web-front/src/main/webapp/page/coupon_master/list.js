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
    getOptionCode("COUPON_TYPE", "coupon_type");
    getOptionCode("APPROVE_STATUS", "approve_status");
    getOptionCode("COUPON_DISTRIBUTE_TYPE", "distribute_type");
});

function initTable() {
    var tableOption = {
        "bProcessing": true,
        "bServerSide": true,
        "fnServerData": fnServerData,
        "bFilter": false,
        "bSort": false,
        "sAjaxSource": "../coupon_master/getList.json",
        "fnServerParams": function (aoData) {
            // 设置参数
            aoData.push({"name": "couponName", "value": $("#coupon_name").val()});
            aoData.push({"name": "couponType", "value": $("#coupon_type").val()});
            aoData.push({"name": "approveStatus", "value": $("#approve_status").val()});
            aoData.push({"name": "distributeType", "value": $("#distribute_type").val()});
            aoData.push({"name": "isValid", "value": $("#is_valid").val()});
        },
        "aoColumns": [
            {"mData": "sort"},
            {"mData": "couponName"},
            {"mData": "couponTypeCn"},
            {"mData": "distributeTypeCn"},
            {"mData": "allCount"},
            // {"mData": "applyUserName"},
            // {
            //     "mData": "applyTime",
            //     "fnRender": function (rowData) {
            //         if (rowData.aData.applyTime != null) {
            //             return new Date(rowData.aData.applyTime).Format("yyyy-MM-dd hh:mm:ss");
            //         } else {
            //             return "";
            //         }
            //     }
            // },
            {"mData": "approveStatusCn"},
            // {"mData": "approveUserName"},
            // {
            //     "mData": "approveTime",
            //     "fnRender": function (rowData) {
            //         if (rowData.aData.approveTime != null) {
            //             return new Date(rowData.aData.approveTime).Format("yyyy-MM-dd hh:mm:ss");
            //         } else {
            //             return "";
            //         }
            //     }
            //
            // },
            {
                "mData": "sendStartTime",
                "fnRender": function (rowData) {
                    if (rowData.aData.sendStartTime != null) {
                        return new Date(rowData.aData.sendStartTime).Format("yyyy-MM-dd");
                    } else {
                        return "";
                    }
                }

            },
            {
                "mData": "sendEndTime",
                "fnRender": function (rowData) {
                    if (rowData.aData.sendEndTime != null) {
                        return new Date(rowData.aData.sendEndTime).Format("yyyy-MM-dd");
                    } else {
                        return "";
                    }
                }

            },
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
            {
                "mData": null,
                "fnRender": function (rowData) {
                    var sReturn = '';
                    sReturn += '<a onclick=viewItem("' + rowData.aData.couponId + '")>详细</a>&nbsp;&nbsp;';
                    sReturn += '<a onclick=sort("' + rowData.aData.couponId + '","'+((rowData.aData.sort || rowData.aData.sort==0)?rowData.aData.sort:'')+'")>设置顺序</a>&nbsp;&nbsp;';
                    // 未申请和已申请状态或已驳回的，或停用好的才可以编辑，编辑之后都变成待申请
                    if ('40' == rowData.aData.approveStatus
                        || '10' == rowData.aData.approveStatus
                        || '30' == rowData.aData.approveStatus
                        || rowData.aData.isValid != '1') {
                        sReturn += '<a onclick=editItem("' + rowData.aData.couponId + '")>编辑</a>&nbsp;&nbsp;';
                        if ('50' == rowData.aData.goodsType){
                            sReturn += '<a onclick=uploadSKUItem("' + rowData.aData.couponId + '","' + rowData.aData.couponName + '")>批量SKU导入</a>&nbsp;&nbsp;';
                        }
                    }
                    if ('50' == rowData.aData.goodsType){
                        sReturn += '<a onclick=exportSKUItem("' + rowData.aData.couponId +'")>导出SKU</a>&nbsp;&nbsp;';
                    }
                    // 未申请状态的才可以申请
                    if ('40' == rowData.aData.approveStatus) {
                        sReturn += '<a onclick=applyConfirm("' + rowData.aData.couponId + '")>申请</a>&nbsp;&nbsp;';
                    }
                    // 未申请和已申请状态或已驳回的才可以删除
                    if ('40' == rowData.aData.approveStatus || '10' == rowData.aData.approveStatus || '30' == rowData.aData.approveStatus) {
                        sReturn += '<a onclick=deleteConfirm("' + rowData.aData.couponId + '")>删除</a>&nbsp;&nbsp;';
                    }
                    // 审批通过并且启用的可以发放
                    if ('20' == rowData.aData.approveStatus && "10" == rowData.aData.distributeType
                                && "1" == rowData.aData.isValid) {
                        sReturn += '<a onclick=distributeConfirm("' + rowData.aData.couponId + '")>发放</a>&nbsp;&nbsp;';
                    }
                    if("20" == rowData.aData.approveStatus){
                        if(rowData.aData.isValid == '1'){
                            sReturn += '<a onclick=setInvalid("' + rowData.aData.couponId + '")>停用</a>&nbsp;&nbsp;';
                        }else{
                            sReturn += '<a onclick=setValid("' + rowData.aData.couponId + '")>启用</a>&nbsp;&nbsp;';
                        }
                    }
                    return sReturn;
                }
            }

        ]
    };
    table = $('#coupon_master_table').dataTable(tableOption);
}

function addItem() {
    sessionStorage.setItem("couponId", "");
    sessionStorage.setItem("editable", "1");
    $('#content').load('coupon_master/edit.html');
}

function viewItem(couponId) {
    sessionStorage.setItem("couponId", couponId);
    sessionStorage.setItem("editable", "0");
    $('#content').load('coupon_master/edit.html');
}

function sort(couponId,sort) {
    sessionStorage.setItem("couponId", couponId);
    sessionStorage.setItem("sortOld", sort);
    $('#customDialog').load('coupon_master/sort.html');
}

function editItem(couponId) {
    sessionStorage.setItem("couponId", couponId);
    sessionStorage.setItem("editable", "1");
    $('#content').load('coupon_master/edit.html');
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


function distributeConfirm(couponId) {
    showConfirm("确定要发放优惠券吗?", function () {
        distributeItem(couponId);
    })
}

function distributeItem(couponId) {
    var url = "/coupon_master/distributeCoupon.json";
    var param = {
        "couponId": couponId
    };
    var success = function (data) {
        console.log(data);
        if (data.resultCode == '00') {
            showAlert("发放成功,本次发放了" + data.results + "张.", function () {
                table.fnDraw();
            });
        } else {
            showAlert('发放失败,原因:' + data.resultMessage);
        }
    };
    var error = function () {
        showAlert('发放失败,网络异常.');
    };
    axse(url, {'data': JSON.stringify(param)}, success, error);
}

var applyTempId;
function applyConfirm(couponId) {
    $("#apply_dialog_apply_content").val("");
    applyTempId = couponId;
    $("#apply_dialog").modal('show');
}

function applyItem() {
    var url = "/coupon_master/apply.json";
    var param = {
        "couponId": applyTempId,
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
    $("#apply_dialog_tip").text(message);
    setTimeout(function () {
        $("#apply_dialog_tip").text("");
    }, 2000);
}

function uploadSKUItem(id,name){
    sessionStorage.setItem("couponId", id);
    sessionStorage.setItem("couponName", name);
    $('#customDialog').load('coupon_master/uploadSKU.html');
}

function exportSKUItem(id){
    window.open("/qyds-web-front/coupon_master/exportSKUFile.json?couponId=" + id);
    //$('#couponId1').val(id);
    //$('#exportForm').couponId1 = id;
    //$('#exportForm').submit();
}
