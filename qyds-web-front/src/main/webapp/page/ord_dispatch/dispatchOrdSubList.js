/**
 * Created by YiLian on 2016/10/10.
 */
var orderId = sessionStorage.getItem("orderId");

var dispatchSubTable;
$(document).ready(function() {

    getData();

    $('#return_btn').click(function(){
        gotoListPage();
    });

    $("#btn_dispatch_all").click(function(){
        sessionStorage.setItem("orderId", orderId);
        sessionStorage.setItem("orderSubId", "");
        $('#customDialog').load('ord_dispatch/dispatchStore.html');
    });
});

// 失败的回调方法
function errorFn() {
    showAlert('数据获取失败');
}

// 获取订单详细数据
function getData(){
    axse("/ord_dispatch/getDispatchOrdMasterInfo.json", {"orderId":orderId}, viewSuccessFn, errorFn);
}

//退回到列表画面
function gotoListPage(){
    $('#content').load('ord_dispatch/dispatchOrdList.html');
}

//获取详细信息成功的回调 用来显示
function viewSuccessFn(data){
    if (data.resultCode == '00') {
        var item = data.data;

        $('#order_code').val(item.orderCode);
        $('#order_status').val(item.orderStatusName);
        $('#order_time').val(getLocalTime(item.orderTime));
        $('#message').val(item.message);
        $('#delivery_contactor').val(item.deliveryContactor);
        $('#delivery_phone').val(item.deliveryPhone);
        $('#delivery_address').val(item.deliveryAddress);
        $('#province_name').val(item.districtidProvince);
        $('#city_name').val(item.districtidCity);
        $('#district_name').val(item.districtidDistrict);

        //加载子订单数据
        SubTableEditable.init();

    }else{
        showAlert(data.resultMessage);
    }
}

//时间戳转换日期
function getLocalTime(nS) {
    return new Date(parseInt(nS)).toLocaleString().replace(/:\d{1,2}$/,' ');
}

var SubTableEditable = function () {
    return {
        init: function () {
            dispatchSubTable = $('#ord_sub_table').dataTable({

                "aoColumns": [
                    { "mData": "goodsCode",'bSortable': false },
                    { "mData": "goodsName",'bSortable': false },
                    { "mData": "sku",'bSortable': false },
                    { "mData": "quantity",'bSortable': false },
                    { "mData": null,
                        'bSortable': false,
                        "fnRender": function (oObj) {
                            if(oObj.aData.dispatchStatus == '0'){
                                return '未指派门店';
                            } else if(oObj.aData.dispatchStatus == '2'){
                                return '需重新指派门店';
                            } else {
                                return oObj.aData.deliverStatus;
                            }
                        }
                    },
                    {
                        "mData": null,
                        'bSortable': false,
                        "fnRender": function (oObj) {
                            return '<a onclick=dispatchHistory("' + oObj.aData.subOrderId + '")>查看履历</a>';
                        }
                    },
                    {
                        "mData": null,
                        'bSortable': false,
                        "fnRender": function ( oObj ) {
                            if(oObj.aData.dispatchStatus != '1' && oObj.aData.dispatchStatus != '3'){
                                return '<a onclick=subOrderDispatch("' + oObj.aData.subOrderId + '")>指派门店</a>';
                            } else if (oObj.aData.deliverStatusCode == '10') {
                                return '<a onclick=subOrderCancelDispatch("' + oObj.aData.subOrderId + '")>取消指派</a>';
                            } else {
                                return "";
                            }

                        }
                    }
                ],
                "bAutoWidth": false,    //关闭自适应列宽，导致列表后半部空白。IE8下现象
                "bProcessing": false,
                "bServerSide": true,
                "sAjaxSource": "../ord_dispatch/getDispatchOrdSubInfo.json",
                "fnServerParams": function ( aoData ) {
                    aoData.push( { "name": "orderId", "value": orderId } );
                },
                "fnServerData": fnServerData,
                "bFilter": false                       //不使用过滤功能
            });

            $('#ord_sub_table_info').hide();
            $('#ord_sub_table_length').hide();
            $('.dataTables_paginate').hide();
        }
    };
}();

function dispatchHistory(obj){
    sessionStorage.setItem("orderId", orderId);
    sessionStorage.setItem("orderSubId", obj);
    $('#customDialog').load('ord_dispatch/dispatchHistory.html');
}

function subOrderDispatch(obj){
    sessionStorage.setItem("orderId", orderId);
    sessionStorage.setItem("orderSubId", obj);
    $('#customDialog').load('ord_dispatch/dispatchStore.html');
}

function subOrderCancelDispatch(obj){
    showConfirm("确定要取消指派吗?", function () {
        cancelItem(obj);
    });
}

function cancelItem(orderSubId) {
    var url = "/ord_dispatch/cancelDispatch.json";
    var param = {
        "orderId": orderId,
        "subOrderId":orderSubId
    };
    var success = function (data) {
        if (data.resultCode == '00') {
            showAlert("操作成功!", function () {
                dispatchSubTable.fnDraw();
            });
        } else {
            showAlert('操作失败,原因:' + data.resultMessage);
            dispatchSubTable.fnDraw();
        }
    };
    var error = function () {
        showAlert('操作失败,网络异常.');
    };
    axse(url, {'data': JSON.stringify(param)}, success, error);
}