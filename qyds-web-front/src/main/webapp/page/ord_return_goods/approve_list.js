var deliverStatus_dashboard = sessionStorage.getItem("deliverStatus_dashboard");

var table;

//页面初始化加载
$(document).ready(function () {
    //检索按钮点击事件
    $("#btn_search").click(function () {
        table.fnDraw();
    });
    initTable();
});

function initTable() {
    var tableOption = {
        "bProcessing": true,
        "bServerSide": true,
        "fnServerData": fnServerData,
        "bFilter": false,
        "bSort": false,
        "sAjaxSource": "../ord_master/getList.json",
        "fnServerParams": function (aoData) {
            // 设置参数
            aoData.push({"name": "orderStatus", "value": "30"});
            aoData.push({"name": "orderCode", "value": $("#orderCode").val()});
        },
        "aoColumns": [
            {"mData": "orderCode"},
            {"mData": "orderStatusName"},
            {"mData": "orderTypeCn"},
            {"mData": "amountTotle"},
            {"mData": "rexPrice"},
            {
                "mData": "orderTime",
                "fnRender": function (rowData) {
                    if (rowData.aData.orderTime != null) {
                        return new Date(rowData.aData.orderTime).Format("yyyy-MM-dd hh:mm:ss");
                    } else {
                        return "";
                    }
                }
            },
            {"mData": "comment"},
            {
                "mData": null,
                'bSortable': false,
                "fnRender": function (oObj) {
                    var str = '<a class="order_edit" href="#" onclick="order_edit(this);" id="' + oObj.aData.orderId + '">详细</a>';
                    return str;
                }
            }
        ]
    };
    table = $('#ord_master_table').dataTable(tableOption);
}

//点击详情按钮进行画面跳转
function order_edit(obj) {
    var orderId = $(obj).attr("id");
    sessionStorage.setItem("orderId", orderId);
    $('#content').load('ord_return_goods/approve.html');
}