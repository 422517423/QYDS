/**
 * Created by zlh on 16/10/27.
 */

var memberId = sessionStorage.getItem("memberId");
var tableCoupon;
$(document).ready(function () {

    $("#recordArea").modal('show');

    initTable();
});

//表格js初始化
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
        "sAjaxSource": "../coupon_master/couponRecord.json",
        "fnServerParams": function (aoData) {
            // 设置参数
            aoData.push( { "name": "memberId", "value": memberId } );
        },
        "aoColumns": [
            {
                "mData": "sendTime",
                "fnRender": function (rowData) {
                    if (rowData.aData.sendTime != null) {
                        return new Date(rowData.aData.sendTime).Format("yyyy-MM-dd hh:mm:ss");
                    } else {
                        return "";
                    }
                }
            },
            { "mData": "insertUserId"},
            { "mData": "couponName"},
            { "mData": "couponStyle"},
            { "mData": "discount"},
            { "mData": "statusName"},
            {
                "mData": "usedTime",
                "fnRender": function (rowData) {
                    if (rowData.aData.usedTime != null) {
                        return new Date(rowData.aData.usedTime).Format("yyyy-MM-dd hh:mm:ss");
                    } else {
                        return "";
                    }
                }
            }
        ]
    };
    tableCoupon = $('#record_table').dataTable(tableOption);
}

var modalSettings = {
    backdrop : 'static',
    keyboard : false
};
