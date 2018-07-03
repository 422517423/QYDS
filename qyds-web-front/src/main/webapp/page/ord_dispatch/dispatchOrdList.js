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
        "sAjaxSource": "../ord_dispatch/getDispatchOrdMasterList.json",
        "fnServerParams": function (aoData) {
            // 设置参数
            //aoData.push({"name": "orderId", "value": $("#orderId").val()});
            aoData.push({"name": "orderCode", "value": $("#orderCode").val()});
            aoData.push({"name": "memberName", "value": $("#memberName").val()});
            aoData.push({"name": "telephone", "value": $("#telephone").val()});
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
            {"mData": "memberName"},
            {"mData": "telephone"},
            {
                "mData": "type",
                "fnRender": function (rowData) {
                    if (rowData.aData.type == '1') {
                        return "店铺拒绝接单";
                    } else {
                        if(rowData.aData.dispatchStatus == '0'){
                            return "未指派";
                        } else if (rowData.aData.dispatchStatus == '1'){
                            return "部分指派";
                        }  else if (rowData.aData.dispatchStatus == '2'){
                            return "已指派待发货";
                        } else {
                            return "";
                        }

                    }
                }

            },
            {
                "mData": null,
                "fnRender": function (rowData) {

                    if (rowData.aData.orderId != null) {
                        return '<a onclick=dispatchItem("' + rowData.aData.orderId + '")>订单详细</a>';
                    } else {
                        return "";
                    }

                }
            }

        ]
    };
    table = $('#ord_dispatch_table').dataTable(tableOption);
}

//时间戳转换日期
function getLocalTime(nS) {
    return new Date(parseInt(nS)).toLocaleString().replace(/:\d{1,2}$/, ' ');
}

//点击编辑按钮进行画面跳转
function dispatchItem(obj) {
    sessionStorage.setItem("orderId", obj);
    sessionStorage.setItem("orderIsDispatch", "1");
    $('#content').load('ord_dispatch/dispatchOrdSubList.html');
}