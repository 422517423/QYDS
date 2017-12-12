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
        "sAjaxSource": "../ord_return_goods/receiveGoodsList.json",
        "fnServerParams": function (aoData) {
            // 设置参数
            aoData.push({"name": "orderCode", "value": $("#orderCode").val()});
        },
        "aoColumns": [
            {"mData": "orderCode"},
            {"mData": "orderStatusName"},
            {"mData": "deliverStatusCn"},
            {"mData": "amountTotle"},
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
            {
                "mData": null,
                'bSortable': false,
                "fnRender": function (oObj) {
                    var str = '<a class="order_edit" href="#" onclick="order_edit(this);" id="' + oObj.aData.orderId + '">详细</a>';
                    if (oObj.aData.deliverStatus=='31') {
                        str += '&nbsp;&nbsp; <a onclick=order_print("' + oObj.aData.orderId + '");>收货凭证</a>';
                    }
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
    $('#content').load('ord_return_goods/goods_receive.html');
}

//点击打印按钮
function order_print(orderId) {
    sessionStorage.setItem("orderId", orderId);
    $('#customDialog').load('ord_return_goods/proof.html');
}
