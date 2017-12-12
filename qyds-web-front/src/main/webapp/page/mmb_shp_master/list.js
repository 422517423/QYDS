/**
 * Created by YiLian on 2016/7/18.
 */
var table;
$(document).ready(function () {

    $("#btn_search").attr("disabled", true);

    // 获取下拉列表的码表
    getOptionCode("SEX", "cond_sex");
    getOptionCode("MEMBER_TYPE", "cond_type");
    getOptionCode("MEMBER_LEVEL", "cond_member_level_id");

    initTable();

    //检索按钮点击事件
    $("#btn_search").click(function () {
        //刷新Datatable，会自动激发retrieveData
        table.fnDraw();
    });

    //新建按钮点击事件
    $("#btn_add").click(function () {
        sessionStorage.setItem("memberId", "");
        $('#customDialog').load('mmb_master/edit.html');
    });

    $("#cond_telephone").on("change", function(){
        $("#btn_search").attr("disabled", false);
        if ($(this).val() == "") {
            $("#btn_search").attr("disabled", true);
        } else {
            if (!$(this).val().match(/^(((13[0-9]{1})|159|153)+\d{8})$/)) {
                $("#btn_search").attr("disabled", true);
            }
        }
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
        "sAjaxSource": "../mmb_shp_master/getList.json",
        "fnServerParams": function (aoData) {
            // 设置参数
            aoData.push({"name": "telephone", "value": $("#cond_telephone").val()});
        },
        "aoColumns": [
            {"mData": "memberLevelName"},
            {"mData": "typeName"},
            {"mData": "memberName"},
            {"mData": "point"},
            {"mData": "allPoint"},
            {"mData": "sexName"},
            {
                "mData": null,
                "fnRender": function (rowData) {
                    if (rowData.aData.openId && rowData.aData.openId.length > 0) {
                        return "是";
                    } else {
                        return "否";
                    }

                }
            },
            {"mData": "telephone"},
            {"mData": "email"},
            {
                "mData": null,
                "fnRender": function (rowData) {
                    return '<a onclick=master_detail("' + rowData.aData.memberId + '")>详细</a>';
                        // + '&nbsp;&nbsp; '
                        // + '<a onclick=master_edit("' + rowData.aData.memberId + '");>编辑</a>';
                        // + '&nbsp;&nbsp; '
                        // + '<a onclick=deleteConfirm("' + rowData.aData.memberId + '");>删除</a>';

                }
            }

        ]
    };
    table = $('#mmb_master_table').dataTable(tableOption);
}

//点击详情按钮进行画面跳转
function master_detail(memberId) {

    sessionStorage.setItem("memberId", memberId);
    $('#customDialog').load('mmb_master/detail.html');
}

function master_edit(memberId) {

    sessionStorage.setItem("memberId", memberId);
    $('#customDialog').load('mmb_master/edit.html');
}

//点击删除按钮确认
function deleteConfirm(obj) {
    showConfirm("确定要删除吗?", function () {
        deleteItem(obj);
    })
}

function deleteItem(obj) {
    var url = "/mmb_master/delete.json";
    var param = {
        "memberId": obj
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