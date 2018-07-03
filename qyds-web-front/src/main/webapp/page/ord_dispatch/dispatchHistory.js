var orderId = sessionStorage.getItem("orderId");
var orderSubId = sessionStorage.getItem("orderSubId");

var xTable;
$(document).ready(function () {
    $("#dispatch_history_dialog").modal('show');

    initTable();
});

function initTable() {
    var tableOption = {
        "bProcessing": true,
        "bServerSide": true,
        "fnServerData": fnServerData,
        "bFilter": false,
        "bSort": false,
        "sAjaxSource": "../ord_dispatch/getDispatchHistory.json",
        "fnServerParams": function (aoData) {
            aoData.push({"name": "orderId", "value": orderId});
            aoData.push({"name": "subOrderId", "value": orderSubId});
        },
        "aoColumns": [
            {
                "mData": null,
                "fnRender": function (oObj) {
                    return getLocalTime(oObj.aData.update_time);
                }
            },
            {"mData": "store_name_cn"},
            {
                "mData": null,
                "fnRender": function (oObj) {
                    if(oObj.aData.type == 0){
                        return '已派单'
                    } else if(oObj.aData.type == 1) {
                        return '拒绝接单';
                    } else if(oObj.aData.type == 2) {
                        return '已接单';
                    } else if(oObj.aData.type == 3) {
                        return '派单取消';
                    } else {
                        return "";
                    }
                }
            },
            {
                "mData": null,
                "fnRender": function (oObj) {
                    if(oObj.aData.content_store != undefined){
                        return oObj.aData.content_store;
                    } else {
                        return "";
                    }

                }
            }
        ]
    };

    xTable = $('#dispatch_history_table').dataTable(tableOption);

    $('#dispatch_history_table_info').hide();
    $('#dispatch_history_table_length').hide();
    $('.dataTables_paginate').hide();
}

//时间戳转换日期
function getLocalTime(nS) {
    return new Date(parseInt(nS)).toLocaleString().replace(/:\d{1,2}$/,' ');
}

function showTip(message) {
    $("#dispatch_history_tip").text(message);
    setTimeout(function () {
        $("#dispatch_history_tip").text("");
    }, 2000);
}