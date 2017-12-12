/**
 * Created by ZLH on 2016/12/16.
 */
var table;
$(document).ready(function () {

    // 获取下拉列表的码表
    getOptionCode("SEX", "cond_sex");

    initTable();

    //检索按钮点击事件
    $("#btn_search").click(function () {
        //刷新Datatable，会自动激发retrieveData
        table.fnDraw();
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
        "sAjaxSource": "../mmb_saler/getList.json",
        "fnServerParams": function (aoData) {
            // 设置参数
            aoData.push({"name": "telephone", "value": $("#cond_telephone").val()});
            aoData.push({"name": "memberName", "value": $("#cond_member_name").val()});
            aoData.push({"name": "oldphone", "value": $("#cond_oldphone").val()});
            aoData.push({"name": "deleted", "value": $("#cond_deleted").val()});
        },
        "aoColumns": [
            {"mData": "telephone"},
            {"mData": "memberName"},
            {"mData": "sexName"},
            {"mData": "oldphone"},
            {"mData": "email"},
            {
                "mData": null,
                "fnRender": function (rowData) {
                    if (rowData.aData.deleted == '1') {
                        return "已删除";
                    } else {
                        return "未删除";
                    }

                }
            },
            {
                "mData": null,
                "fnRender": function (rowData) {
                    return '<a onclick=master_detail("' + rowData.aData.memberId + '")>详细</a>'
                }
            }

        ]
    };
    table = $('#mmb_master_table').dataTable(tableOption);
}

//点击详情按钮进行画面跳转
function master_detail(memberId) {

    sessionStorage.setItem("memberId", memberId);
    $('#customDialog').load('mmb_saler/detail.html');
}
