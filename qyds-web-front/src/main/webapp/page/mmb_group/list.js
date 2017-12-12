/**
 * Created by YiLian on 2016/7/18.
 */
var table;
$(document).ready(function () {

    getOptionCode("MEMBER_GROUP_TYPE", "type");

    initTable();
    //检索按钮点击事件
    $("#btn_search").click(function () {
        //刷新Datatable，会自动激发retrieveData
        table.fnDraw();
    });

    //新建按钮点击事件
    $("#btn_add").click(function () {
        sessionStorage.setItem("groupId", "");
        sessionStorage.setItem("groupIsEdit", "1");
        $('#customDialog').load('mmb_group/edit.html');
    });

});

function initTable() {
    var tableOption = {
        "bProcessing": true,
        "bServerSide": true,
        "fnServerData": fnServerData,
        "bFilter": false,
        "bSort": false,
        "sAjaxSource": "../mmb_group/getList.json",
        "fnServerParams": function (aoData) {
            // 设置参数
            aoData.push({"name": "type", "value": $("#type").val()});
            aoData.push({"name": "groupName", "value": $("#group_name").val()});
        },
        "aoColumns": [
            {"mData": "groupName"},
            {"mData": "typeName"},
            {"mData": "updateUserName"},
            {
                "mData": "updateTime",
                "fnRender": function (rowData) {
                    if (rowData.aData.updateTime != null) {
                        return new Date(rowData.aData.updateTime).Format("yyyy-MM-dd hh:mm:ss");
                    } else {
                        return "";
                    }
                }

            },
            //{
            //    "mData": null,
            //    "fnRender": function (rowData) {
            //        return '<a onclick=viewItem("' + rowData.aData.groupId + '")>详细</a>';
            //    }
            //},
            {
                "mData": null,
                "fnRender": function (rowData) {
                    return '<a onclick=configItem("' + rowData.aData.groupId + '")>会员分组</a>'
                        + '&nbsp;&nbsp; '
                        + '<a onclick=editItem("' + rowData.aData.groupId + '")>编辑</a>'
                        + '&nbsp;&nbsp; '
                        +  '<a onclick=deleteConfirm("' + rowData.aData.groupId + '")>删除</a>';
                }
            }
        ]
    };
    table = $('#mmb_group_table').dataTable(tableOption);
}

//点击详情按钮进行画面跳转
function viewItem(obj) {
    sessionStorage.setItem("groupId", obj);
    sessionStorage.setItem("groupIsEdit", "0");
    $('#customDialog').load('mmb_group/edit.html');
}

//点击会员分组按钮进行画面跳转
function configItem(obj) {
    sessionStorage.setItem("groupId", obj);
    sessionStorage.setItem("groupIsEdit", "0");
    $('#customDialog').load('mmb_group/member_select.html');
}

//点击编辑按钮进行画面跳转
function editItem(obj) {
    sessionStorage.setItem("groupId", obj);
    sessionStorage.setItem("groupIsEdit", "1");
    $('#customDialog').load('mmb_group/edit.html');
}

//点击删除按钮确认
function deleteConfirm(obj) {
    showConfirm("确定要删除吗?", function () {
        deleteItem(obj);
    })
}

function deleteItem(obj) {
    var url = "/mmb_group/delete.json";
    var param = {
        "groupId": obj
    };
    console.log(param);
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
