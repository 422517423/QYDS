/**
 * Created by YiLian on 2016/10/10.
 */

var table;
$(document).ready(function () {
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
        "bFilter": false,
        "bSort": false,
        "sAjaxSource": "../ord_dispatch/getProcessedOrdMasterList.json",
        "fnServerParams": function (aoData) {
            // 设置参数
            //aoData.push({"name": "orderId", "value": $("#orderId").val()});
            aoData.push({"name": "orderCode", "value": $("#orderCode").val()});
        },
        "aoColumns": [
            //{"mData": "orderId"},
            {"mData": "orderCode"},
            {
                "mData": "orderTime",
                "fnRender": function (rowData) {
                    return getLocalTime(rowData.aData.orderTime);
                }
            },
            {"mData": "deliverStatusCn"},
            {
                "mData": null,
                "fnRender": function (rowData) {
                    if (rowData.aData.orderId != null) {
                        //return '<a onclick=processedItemDetail("' + rowData.aData.orderId + '")>订单详细</a>';
                        var str = '<a onclick=processedItemDetail("' + rowData.aData.orderId + '")>订单详细</a>';
                        //if (rowData.aData.deliverStatus=='20') {
                        if (rowData.aData.deliverCount>0) {
                            str += '&nbsp;&nbsp; <a onclick=order_print("' + rowData.aData.orderId + '");>发货凭证</a>';
                        }
                        return str;
                    } else {
                        return "";
                    }

                }
            }

        ]
    };
    table = $('#ord_processed_table').dataTable(tableOption);
}

//时间戳转换日期
function getLocalTime(nS) {
    return new Date(parseInt(nS)).toLocaleString().replace(/:\d{1,2}$/,' ');
}

//点击编辑按钮进行画面跳转
function processedItemDetail(obj) {
    sessionStorage.setItem("orderId", obj);
    $('#content').load('ord_dispatch/processedOrdSubList.html');
}


//点击打印按钮
function order_print(orderId) {
    sessionStorage.setItem("orderId", orderId);
    $('#customDialog').load('ord_dispatch/proof.html');
}
