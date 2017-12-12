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

    //新建按钮点击事件
    $("#btn_add").click(function () {
        sessionStorage.setItem("memberLevelId", "");
        sessionStorage.setItem("memberLevelIsEdit", "1");
        $('#customDialog').load('mmb_level_rule/edit.html');
    });

    //点击系数编辑按钮
    $("#btn_edit_ratio").click(function () {
        $('#customDialog').load('mmb_level_rule/edit_ratio_config.html');
    });

});

function initTable() {
    var tableOption = {
        "bProcessing": true,
        "bServerSide": true,
        "fnServerData": fnServerData,
        "bFilter": false,
        "bSort": false,
        "sAjaxSource": "../mmb_level_rule/getList.json",
        "fnServerParams": function (aoData) {
            // 设置参数
            aoData.push({"name": "memberLevelCode", "value": $("#member_level_code").val()});
        },
        "aoColumns": [
            {"mData": "memberLevelCode"},
            {"mData": "memberLevelName"},
            {"mData": "memberCount"},
            {"mData": "pointRatio"},
            {"mData": "pointSingle"},
            {"mData": "pointLower"},
            {"mData": "pointUpper"},
            {"mData": "discount"},
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
            {
                "mData": null,
                "fnRender": function (rowData) {
                    if (rowData.aData.memberLevelId != null) {
                        return '<a onclick=viewItem("' + rowData.aData.memberLevelId + '")>详细</a>';
                    } else {
                        return "";
                    }
                }
            },
            {
                "mData": null,
                "fnRender": function (rowData) {

                    if (0 == rowData.aData.memberCount) {
                        return '<a onclick=editItem("' + rowData.aData.memberLevelId + '")>编辑</a>'
                            + '&nbsp;&nbsp; '
                            + '<a onclick=deleteConfirm("' + rowData.aData.memberLevelId + '");>删除</a>';
                    } else {
                        return '<a onclick=editItem("' + rowData.aData.memberLevelId + '")>编辑</a>'
                            + '&nbsp;&nbsp;'
                            + '<a onclick=memberList("' + rowData.aData.memberLevelCode + '","' + rowData.aData.memberLevelName + '")>会员一览</a>'

                    }

                }
            }

        ]
    };
    table = $('#mmb_level_rule_table').dataTable(tableOption);
}

//点击详情按钮进行画面跳转
function viewItem(obj) {
    sessionStorage.setItem("memberLevelId", obj);
    sessionStorage.setItem("memberLevelIsEdit", "0");
    $('#customDialog').load('mmb_level_rule/edit.html');
}

//点击编辑按钮进行画面跳转
function editItem(obj) {
    sessionStorage.setItem("memberLevelId", obj);
    sessionStorage.setItem("memberLevelIsEdit", "1");
    $('#customDialog').load('mmb_level_rule/edit.html');
}

//点击会员一览按钮进行画面跳转
function memberList(id, name) {
    sessionStorage.setItem("memberLevelName", name);
    sessionStorage.setItem("memberLevelId", id);
    $('#content').load('mmb_level_rule/members.html');
}

//点击删除按钮确认
function deleteConfirm(obj) {
    showConfirm("确定要删除吗?", function () {
        deleteItem(obj);
    })
}

function deleteItem(obj) {
    var url = "/mmb_level_rule/delete.json";
    var param = {
        "memberLevelId": obj
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