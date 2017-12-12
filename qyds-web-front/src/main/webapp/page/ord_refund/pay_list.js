/**
 * Created by zlh on 2016/10/12.
 */

var deliverStatus_dashboard = sessionStorage.getItem("deliverStatus_dashboard");

var oTable;

//页面初始化加载
$(document).ready(function() {
    //检索按钮点击事件
    $("#btn_search").click(function(){
        //刷新Datatable，会自动激发retrieveData
        oTable.fnDraw();
    });

});

// 失败的回调方法
function errorFn() {
    showAlert('数据获取失败');
}

//列表访问数据库方法
function fnServerData( sSource, aoData, fnCallback, oSettings ){
    oSettings.jqXHR = $.ajax( {
        "dataType": 'json',
        "type": "POST",
        "url": sSource,
        "data": aoData,
        "success": fnCallback,
        "error": function (e) {
            console.log(e.message);
        }
    });
}

//表格js初始化
var TableEditable = function () {
    return {
        init: function () {
            oTable = $('#ord_master_table').dataTable({

                "aLengthMenu": [
                    [10, 20, 50, 100],
                    [10, 20, 50, 100] // change per page values here
                ],
                // set the initial value
                "iDisplayLength": 10,
                "aoColumns": [
                    //{ "mData": "orderId",'bSortable': false },
                    { "mData": "orderCode",'bSortable': false },
                    {
                        "mData": "orderTime",
                        "fnRender": function (rowData) {
                            if (rowData.aData.orderTime != null) {
                                return new Date(rowData.aData.orderTime).Format("yyyy-MM-dd");
                            } else {
                                return "";
                            }
                        }
                    },
                    { "mData": "orderStatusName",'bSortable': false },
                    { "mData": "payStatusName",'bSortable': false },
                    { "mData": "deliverStatusName",'bSortable': false },
                    { "mData": "rexStatusName",'bSortable': false },
                    {
                        "mData": "applyTime",
                        "fnRender": function (rowData) {
                            if (rowData.aData.applyTime != null) {
                                return new Date(rowData.aData.applyTime).Format("yyyy-MM-dd");
                            } else {
                                return "";
                            }
                        }
                    },
                    { "mData": "rexMemberName",'bSortable': false },
                    { "mData": "applyComment",'bSortable': false },
                    {
                        "mData": "applyAnswerTime",
                        "fnRender": function (rowData) {
                            if (rowData.aData.applyAnswerTime != null) {
                                return new Date(rowData.aData.applyAnswerTime).Format("yyyy-MM-dd");
                            } else {
                                return "";
                            }
                        }
                    },
                    { "mData": "applyAnswerUserId",'bSortable': false },
                    { "mData": "applyAnswerComment",'bSortable': false },
                    {
                        "mData": null,
                        'bSortable': false,
                        "fnRender": function ( oObj ) {
                            var str = '<a class="edit" href="#" onclick=edit("' + oObj.aData.orderId + '")>订单详细</a>';
                            str += '<a class="approve" href="#" style="margin-left:5px;" onclick=pay("' + oObj.aData.rexOrderId + '")>退款付款</a>';
                            return str;
                        }
                    }

                ],
                "bAutoWidth": false,    //关闭自适应列宽，导致列表后半部空白。IE8下现象
                "bProcessing": false,
                "bServerSide": true,
                "sAjaxSource": "../ord_refund/payList.json",
                "fnServerParams": function ( aoData ) {
                    aoData.push( { "name": "orderId", "value": $("#orderId").val() } );
                    aoData.push( { "name": "orderCode", "value": $("#orderCode").val() } );
                },
                "fnServerData": fnServerData,
                "bFilter": false                       //不使用过滤功能
            });

        }
    };
}();

//点击详情按钮进行画面跳转
function edit(id){
    sessionStorage.setItem("orderId",id);
    sessionStorage.setItem("backPage","ord_refund/pay_list.html");
    $('#content').load('ord_master/edit.html');
}

//订单退款审批
function pay(id){
    sessionStorage.setItem("rexOrderId",id);
    $('#content').load('ord_refund/pay.html');
}
