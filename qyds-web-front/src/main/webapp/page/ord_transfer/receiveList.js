/**
 * Created by ZLH on 2016/12/25.
 */

var table;
var orderId = sessionStorage.getItem("orderId");
$(document).ready(function () {
    initTable();
    getSelectCode("TRANSFER_STATUS", "transferStatus","'10','21','22','23','31','32'",function(){
        var payStatus = sessionStorage.getItem("payStatus");
        if(payStatus){
            $('#payStatus option[value="'+payStatus+'"]').attr('selected','selected');
        }
    });

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
        "sAjaxSource": "../ord_transfer/getReceiveList.json",
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
            //{"mData": "applyStore"},
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
                    var status=rowData.aData.transferStatus;
                    var ret='';
                    if (status == '31') {
                        ret += ' &nbsp;&nbsp;<a onclick=receive("' + rowData.aData.orderTransferId + '","' + rowData.aData.sku +  '")>收货</a>';
                    }
                    if (status == '32') {
                        ret += ' &nbsp;&nbsp;<a onclick=goproof("' + rowData.aData.orderTransferId + '","' + rowData.aData.sku +  '")>收货凭证</a>';
                    }
                    return ret;
                }
            }

        ]
    };
    table = $('#ord_receive_table').dataTable(tableOption);
}

//时间戳转换日期
function getLocalTime(ns) {
    if(!ns) return '';
    return new Date(parseInt(ns)).toLocaleString().replace(/:\d{1,2}$/, ' ');
}

//点击编辑按钮进行画面跳转
function receive(orderTransferId) {
    var url = "/ord_transfer/receive.json";
    var param = {
        orderTransferId: orderTransferId,
        orderId:orderId
    };
    var success = function (data) {
        if (data.resultCode == '00') {
            showAlert('提交成功!', '提示', function () {
                table.fnDraw();
            });
        } else {
            showTip('提交失败:' + data.resultMessage);
        }
    };
    var error = function () {
        showTip('网络异常.');
    };
    axse(url, param, success, error);
}

//点击打印按钮
function proof(orderTransferId) {
    sessionStorage.setItem("orderTransferId", orderTransferId);
    $('#customDialog').load('ord_transfer/proofReceive.html');
}

//点击打印按钮
function goproof(orderTransferId) {
    sessionStorage.setItem("orderTransferId", orderTransferId);
    $('#customDialog').load('ord_transfer/proofReceive.html');
}
