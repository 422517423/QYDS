var oTable;

//页面初始化加载
$(document).ready(function () {
    getSelectCode("ORDER_STATUS", "type","'10','11','20','21','22','23','30','31','32','33','90'");
    getSelectCode("PAY_STATUE", "payStatus","'10','20','21','30','31'");
    getSelectCode("DELIVER_STATUS", "deliverStatus","'10','19','20','21','30','31'");
    initTable();
    //检索按钮点击事件
    $("#btn_search").click(function () {
        //刷新Datatable，会自动激发retrieveData
        oTable.fnDraw();
    });

});

function initTable(){
    var tableOption = {
        "bProcessing": true,
        "bServerSide": true,
        "fnServerData": fnServerData,
        "bFilter": false,
        "bSort": false,
        "sAjaxSource": "../ord_master/getSelfOrderList.json",
        "fnServerParams": function (aoData) {
            aoData.push({"name": "orderId", "value": $("#orderId").val()});
            aoData.push({"name": "orderCode", "value": $("#orderCode").val()});
            aoData.push({"name": "orderStatus", "value": $("#type").val()});
            aoData.push({"name": "payStatus", "value": $("#payStatus").val()});
            aoData.push({"name": "deliverStatus", "value": $("#deliverStatus").val()});
        },
        "aoColumns": [
            {"mData": "orderCode"},
            {"mData": "orderStatusName"},
            {"mData": "orderTypeCn"},
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
            {"mData": "comment"},
            {
                "mData": null,
                "fnRender": function (rowData) {
                    var str = '<a class="order_edit" href="#" onclick=order_edit("'+ rowData.aData.orderId+'");>详细</a>';
                    return str;

                }
            }

        ]
    };
    oTable = $('#ord_master_table').dataTable(tableOption);
}
//点击详情按钮进行画面跳转
function order_edit(id) {
    sessionStorage.setItem("orderId", id);
    $('#content').load('ord_store/detail.html');
}