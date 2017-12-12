/**
 * Created by ZLH on 2016/12/24.
 */

var tableDispatch;
$(document).ready(function () {
    getSelectCode("TRANSFER_STATUS", "transferStatus","'10','21','22','23','31','32'");
    initTable();
    //检索按钮点击事件
    $("#btn_search").click(function () {
        //刷新Datatable，会自动激发retrieveData
        tableDispatch.fnDraw();
    });
});

function initTable() {
    var tableOption = {
        "bProcessing": true,
        "bServerSide": true,
        "fnServerData": fnServerData,
        "bFilter": false,
        "bSort": false,
        "sAjaxSource": "../ord_transfer/getDispatchList.json",
        "fnServerParams": function (aoData) {
            // 设置参数
            //aoData.push({"name": "orderId", "value": $("#orderId").val()});
            //aoData.push({"name": "subOrderId", "value": $("#orderCode").val()});
            aoData.push({"name": "sku", "value": $("#sku").val()});
            aoData.push({"name": "transferStatus", "value": $("#transferStatus").val()});
        },
        "aoColumns": [
            //{"mData": "subOrderId"},
            //{"mData": "orderId"},
            //{
            //    "mData": "orderTime",
            //    "fnRender": function (rowData) {
            //        return getLocalTime(rowData.aData.insertTime);
            //    }
            //},
            {"mData": "sku"},
            {"mData": "goodsName"},
            {"mData": "coloreName"},
            {"mData": "sizeName"},
            {"mData": "quantity"},
            {"mData": "transferStatusName"},
            {"mData": "applyStore"},
            {
                "mData": "applyTime",
                "fnRender": function (rowData) {
                    return getLocalTime(rowData.aData.applyTime);
                }
            },
            {"mData": "dispatchStore"},
            {
                "mData": "dispatchTime",
                "fnRender": function (rowData) {
                    return getLocalTime(rowData.aData.dispatchTime);
                }
            },
            {
                "mData": "deliveryTime",
                "fnRender": function (rowData) {
                    return getLocalTime(rowData.aData.deliveryTime);
                }
            },
            {
                "mData": "receiveTime",
                "fnRender": function (rowData) {
                    return getLocalTime(rowData.aData.receiveTime);
                }
            },
            {
                "mData": null,
                "fnRender": function (rowData) {
                    if (rowData.aData.transferStatus == '10' || rowData.aData.transferStatus == '22') {
                        return '<a onclick=godispatch("' + rowData.aData.orderTransferId + '","' + rowData.aData.sku +  '")>分派门店</a>';
                    } else {
                        return '';
                    }
                }
            }

        ]
    };
    tableDispatch = $('#ord_dispatch_table').dataTable(tableOption);
}

//时间戳转换日期
function getLocalTime(ns) {
    if(!ns) return '';
    return new Date(parseInt(ns)).toLocaleString().replace(/:\d{1,2}$/, ' ');
}

//点击编辑按钮进行画面跳转
function godispatch(orderTransferId,sku) {
    sessionStorage.setItem("orderTransferId", orderTransferId);
    sessionStorage.setItem("sku", sku);
    $('#customDialog').load('ord_transfer/dispatch.html');
}

function dispatchback() {
    tableDispatch.fnDraw();
}