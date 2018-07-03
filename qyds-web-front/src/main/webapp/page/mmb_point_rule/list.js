/**
 * Created by YiLian on 2016/7/18.
 */
var table;
$(document).ready(function () {

    getOptionCode("POINT_RULE", "rule_code_q", function(flag){
        if(flag){
            initTable();
        } else {

        }
    });

    //检索按钮点击事件
    $("#btn_search").click(function () {
        //刷新Datatable，会自动激发retrieveData
        table.fnDraw();
    });

    //新建按钮点击事件
    $("#btn_add").click(function () {
        sessionStorage.setItem("ruleId", "");
        sessionStorage.setItem("ruleIsEdit", "1");
        $('#customDialog').load('mmb_point_rule/edit.html');
    });

});

function initTable() {
    var tableOption = {
        "bProcessing": true,
        "bServerSide": true,
        "fnServerData": fnServerData,
        "bFilter": false,
        "bSort": false,
        "sAjaxSource": "../mmb_point_rule/getList.json",
        "fnServerParams": function (aoData) {
            // 设置参数
            aoData.push({"name": "ruleCode", "value": $("#rule_code_q").val()});
            aoData.push({"name": "point", "value": $("#point").val()});
        },
        "aoColumns": [
            {"mData": "ruleName"},
            {"mData": "ruleCode"},
            {"mData": "point"},
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
                    return '<a onclick=viewItem("' + rowData.aData.ruleId + '")>详细</a>';
                }
            },
            {
                "mData": null,
                "fnRender": function (rowData) {
                    return '<a onclick=editItem("' + rowData.aData.ruleId + '")>编辑</a>'
                        + '&nbsp;&nbsp; '
                        + '<a onclick=deleteConfirm("' + rowData.aData.ruleId + '")>删除</a>';
                }
            }
        ]
    };
    table = $('#mmb_point_rule_table').dataTable(tableOption);
}

//点击详情按钮进行画面跳转
function viewItem(obj) {
    sessionStorage.setItem("ruleId", obj);
    sessionStorage.setItem("ruleIsEdit", "0");
    $('#customDialog').load('mmb_point_rule/edit.html');
}

//点击编辑按钮进行画面跳转
function editItem(obj) {
    sessionStorage.setItem("ruleId", obj);
    sessionStorage.setItem("ruleIsEdit", "1");
    $('#customDialog').load('mmb_point_rule/edit.html');
}

//点击删除按钮确认
function deleteConfirm(obj) {
    showConfirm("确定要删除吗?", function () {
        deleteItem(obj);
    })
}

function deleteItem(obj) {
    var url = "/mmb_point_rule/delete.json";
    var param = {
        "ruleId": obj
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
