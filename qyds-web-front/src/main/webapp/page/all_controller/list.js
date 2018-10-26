/**
 * Created by YiLian on 2016/7/18.
 */
var table;
$(document).ready(function () {

    // 获取下拉列表的码表
    getOptionCode("ALL_CONTROLLER_TYPE", "cond_type");
    initTable();

    if (jQuery().datepicker) {
        $('.date-picker').datepicker({
            rtl: App.isRTL(),
            autoclose: true
        });
    }

    //检索按钮点击事件
    $("#btn_search").click(function () {
        //刷新Datatable，会自动激发retrieveData
        table.fnDraw();
    });

    //新建按钮点击事件
    $("#btn_add").click(function () {
        sessionStorage.setItem("allControllerId", "");
        $('#customDialog').load('all_controller/edit.html');
    });

});

function initTable() {
    var tableOption = {
        "bProcessing": true,
        "bServerSide": true,
        "fnServerData": fnServerData,
        "oLanguage": {
            "sEmptyTable": "没有数据",
            "sLengthMenu": "_MENU_ 件",
            "oPaginate": {
                "sPrevious": "上一件",
                "sNext": "下一件"
            }
        },
        "bFilter": false,
        "bSort": false,
        "sAjaxSource": "../all_controller/getList.json",
        "fnServerParams": function (aoData) {
            // 设置参数
            aoData.push({"name": "name", "value": $("#cond_controller_name").val()});
            aoData.push({"name": "type", "value": $("#cond_type").val()});
        },
        "aoColumns": [
            {"mData": "name"},
            {"mData": "typeName"},
            {"mData": "paramOne"},
            {"mData": "paramTwo"},
            {"mData": "paramThree"},
            {"mData": "paramFour"},
            {"mData": "paramFive"},
            {
                "mData": null,
                "fnRender": function (rowData) {
                    if (rowData.aData.status===0) {
                        return "关";
                    } else if (rowData.aData.status===1 ) {
                        return "开";
                    }else {
                        return "--";
                    }

                }
            },
            {"mData": "createUserName"},
            {
                "mData": null,
                "fnRender": function (rowData) {
                    if (rowData.aData.createTime != null) {
                        return new Date(rowData.aData.createTime).Format("yyyy-MM-dd");
                    } else {
                        return "";
                    }
                }
            },
            {"mData": "updateUserName"},
            {
                "mData": null,
                "fnRender": function (rowData) {
                    if (rowData.aData.updateTime != null) {
                        return new Date(rowData.aData.updateTime).Format("yyyy-MM-dd");
                    } else {
                        return "";
                    }
                }
            },
            {"mData": "comment"},
            {
                "mData": null,
                "fnRender":function (rowData) {
                    if (rowData.aData.status===0) {
                        return '<a onclick=master_detail("' + rowData.aData.allControllerId + '")>详细</a>'
                        + '&nbsp;&nbsp; '
                        + '<a onclick=master_edit("' + rowData.aData.allControllerId + '");>编辑</a>'
                        + '&nbsp;&nbsp; '
                        + '<a onclick=deleteConfirm("' + rowData.aData.allControllerId + '");>删除</a>'
                        + '&nbsp;&nbsp; '
                        + '<a onclick=openItem("' + rowData.aData.allControllerId + '");>开启</a>';
                    } else if (rowData.aData.status===1 ) {
                        return '<a onclick=master_detail("' + rowData.aData.allControllerId + '")>详细</a>'
                            + '&nbsp;&nbsp; '
                            + '<a onclick=master_edit("' + rowData.aData.allControllerId + '");>编辑</a>'
                            + '&nbsp;&nbsp; '
                            + '<a onclick=deleteConfirm("' + rowData.aData.allControllerId + '");>删除</a>'
                            + '&nbsp;&nbsp; '
                            + '<a onclick=closeItem("' + rowData.aData.allControllerId + '");>关闭</a>';
                    }
                }
            }

        ]
    };
    table = $('#all_controller_table').dataTable(tableOption);
}

//点击详情按钮进行画面跳转
function master_detail(allControllerId) {

    sessionStorage.setItem("allControllerId", allControllerId);
    $('#customDialog').load('all_controller/detail.html');
}

function master_edit(allControllerId) {

    sessionStorage.setItem("allControllerId", allControllerId);
    $('#customDialog').load('all_controller/edit.html');
}


//点击删除按钮确认
function deleteConfirm(obj) {
    showConfirm("确定要删除吗?", function () {
        deleteItem(obj);
    })
}

function deleteItem(obj) {
    var url = "/all_controller/delete.json";
    var param = {
        "allControllerId": obj
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

function openItem(obj) {
    var url = "/all_controller/edit.json";
    var param = {
        "allControllerId": obj ,
        "status":1
    };

    var success = function (data) {
        if (data.resultCode == '00') {
            showAlert("开启成功!", function () {
                table.fnDraw();
            });
        } else {
            showAlert('开启失败,原因:' + data.resultMessage);
        }
    };
    var error = function () {
        showAlert('开启失败,网络异常.');
    };
    axse(url, {'data': JSON.stringify(param)}, success, error);
}

function closeItem(obj) {
    var url = "/all_controller/edit.json";
    var param = {
        "allControllerId": obj ,
        "status":0
    };

    var success = function (data) {
        if (data.resultCode == '00') {
            showAlert("关闭成功!", function () {
                table.fnDraw();
            });
        } else {
            showAlert('关闭失败,原因:' + data.resultMessage);
        }
    };
    var error = function () {
        showAlert('关闭失败,网络异常.');
    };
    axse(url, {'data': JSON.stringify(param)}, success, error);
}