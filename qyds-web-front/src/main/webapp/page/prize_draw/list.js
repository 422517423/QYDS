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
});

function initTable() {
    var tableOption = {
        "bProcessing": true,
        "bServerSide": true,
        "fnServerData": fnServerData,
        "bFilter": false,
        "bSort": false,
        "sAjaxSource": "../prize_draw/getList.json",
        "fnServerParams": function (aoData) {
            // 设置参数
            aoData.push({"name": "prizeDrawName", "value": $("#prize_draw_name").val()});
            aoData.push({"name": "isValid", "value": $("#is_valid").val()});
        },
        "aoColumns": [
            {"mData": "prizeDrawName"},
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
            {
                "mData": null,
                "fnRender": function (rowData) {
                    if (rowData.aData.isValid == "1") {
                        return "是";
                    } else {
                        return "否";
                    }
                }
            },
            {
                "mData": null,
                "fnRender": function (rowData) {
                    if (rowData.aData.exchangeFlag == "1") {
                        return "是";
                    } else {
                        return "否";
                    }
                }
            },
            {
                "mData": null,
                "fnRender": function (rowData) {
                    if (rowData.aData.isLogin == "1") {
                        return "是";
                    } else {
                        return "否";
                    }
                }
            },
            {
                "mData": null,
                "fnRender": function (rowData) {
                    if (rowData.aData.isOrder == "1") {
                        return "是";
                    } else {
                        return "否";
                    }
                }
            },
            {
                "mData": null,
                "fnRender": function (rowData) {
                    var sReturn = '';
                    sReturn += '<a onclick=viewItem("' + rowData.aData.prizeDrawId + '")>查看详细</a>&nbsp;&nbsp;';
                    sReturn += '<a onclick=viewItemRecord("' + rowData.aData.prizeDrawId + '")>抽奖情况</a>&nbsp;&nbsp;';
                    sReturn += '<a onclick=configItem("' + rowData.aData.prizeDrawId + '")>设置</a>&nbsp;&nbsp;';
                    if(rowData.aData.isValid == "1"){
                        sReturn += '<a onclick=setInvalid("' + rowData.aData.prizeDrawId + '")>停用</a>&nbsp;&nbsp;';
                    }else{
                        sReturn += '<a onclick=setValid("' + rowData.aData.prizeDrawId + '")>启用</a>&nbsp;&nbsp;';
                    }


                    sReturn += '<a onclick=deleteConfirm("' + rowData.aData.prizeDrawId + '")>删除</a>';
                    return sReturn;
                }
            }

        ]
    };
    table = $('#prize_draw_table').dataTable(tableOption);
}

function addItem() {
    sessionStorage.setItem("prizeDrawId", "");
    $('#content').load('prize_draw/edit.html');
}

function viewItem(prizeDrawId) {
    sessionStorage.setItem("prizeDrawId", prizeDrawId);
    $('#content').load('prize_draw/edit.html');
}

function viewItemRecord(prizeDrawId){
    sessionStorage.setItem("prizeDrawId", prizeDrawId);
    $('#content').load('prize_draw/prize_oppo.html');
}

function configItem(prizeDrawId){
    sessionStorage.setItem("prizeDrawId", prizeDrawId);
    $('#customDialog').load('prize_draw/prize_config.html');
}

function deleteConfirm(prizeDrawId) {
    showConfirm("确定要删除吗?", function () {
        deleteItem(prizeDrawId);
    })
}

function deleteItem(prizeDrawId) {
    var url = "/prize_draw/delete.json";
    var param = {
        "prizeDrawId": prizeDrawId
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


function setValid(prizeDrawId) {
    var url = "/prize_draw/setValid.json";
    var param = {
        "prizeDrawId": prizeDrawId
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

function setInvalid(prizeDrawId) {
    var url = "/prize_draw/setInvalid.json";
    var param = {
        "prizeDrawId": prizeDrawId
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