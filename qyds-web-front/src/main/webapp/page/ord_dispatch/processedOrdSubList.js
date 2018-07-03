/**
 * Created by YiLian on 2016/10/10.
 */
var orderId = sessionStorage.getItem("orderId");
var orderDetail;
var processedTableD;
$(document).ready(function () {

    getData();

    $('#return_btn').click(function () {
        gotoListPage();
    });

    $("#btn_dispatch_all").click(function () {
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
function getData() {
    axse("/ord_dispatch/getProcessedOrdMasterInfo.json", {"orderId": orderId}, viewSuccessFn, errorFn);
}

//退回到列表画面
function gotoListPage() {
    $('#content').load('ord_dispatch/processedOrdList.html');
}

//获取详细信息成功的回调 用来显示
function viewSuccessFn(data) {
    if (data.resultCode == '00') {
        var item = data.data;
        orderDetail = data.data;
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
        //if(processedTableD){
        //    processedTableD.fnDraw();
        //}else{
        //    //加载子订单数据
        //    SubTableEditableD.init();
        //}
        SubTableEditableD.init();
    } else {
        showAlert(data.resultMessage);
    }
}

//时间戳转换日期
function getLocalTime(nS) {
    return new Date(parseInt(nS)).toLocaleString().replace(/:\d{1,2}$/, ' ');
}

var SubTableEditableD = function () {
    return {
        init: function () {
            $('#ord_sub_table').dataTable().fnDestroy();
            processedTableD = $('#ord_sub_table').dataTable({

                "aoColumns": [
                    {"mData": "goodsCode", 'bSortable': false},
                    {"mData": "goodsName", 'bSortable': false},
                    {"mData": "sku", 'bSortable': false},
                    {"mData": "quantity", 'bSortable': false},
                    {"mData": "price", 'bSortable': false},
                    {"mData": "priceDiscount", 'bSortable': false},
                    {"mData": "priceShare", 'bSortable': false},
                    {"mData": "actionName", 'bSortable': false},
                    {"mData": "deliverStatus", 'bSortable': false},
                    {
                        "mData": "deliverTime", 'bSortable': false,
                        "fnRender": function (rowData) {
                            if (rowData.aData.deliverTime != null) {
                                return new Date(rowData.aData.deliverTime).Format("yyyy-MM-dd hh:mm:ss");
                            } else {
                                return "";
                            }
                        }
                    },
                    {"mData": "deliverType", 'bSortable': false},
                    {"mData": "expressName", 'bSortable': false},
                    {"mData": "expressNo", 'bSortable': false},
                    {"mData": "storeName", 'bSortable': false},
                    {"mData": "storePhone", 'bSortable': false},
                    {"mData": "storeDeliveryName", 'bSortable': false},
                    {
                        "mData": null,
                        'bSortable': false,
                        "fnRender": function (oObj) {
                            if(oObj.aData.dispatchStatus == '1'){
                                if(oObj.aData.erpStoreId == null || oObj.aData.erpStoreId.length == 0){
                                    return '<a onclick=rejectDispatch("' + oObj.aData.subOrderId + '")>拒绝接单</a>'
                                        + '&nbsp;&nbsp;' + '<a onclick=subOrderDispatch("' + oObj.aData.subOrderId + '",0)>发货</a>';
                                }else{
                                    if(oObj.aData.upSeasoning == null || oObj.aData.upSeasoning.length == 0){
                                        //return '<a onclick=seasoningDispatch("' + oObj.aData.subOrderId + '")>申请调货</a>'
                                        return '<a onclick=applyTransfer("' + oObj.aData.subOrderId + '")>申请调货</a>'
                                            + '&nbsp;&nbsp;' + '<a onclick=subOrderDispatch("' + oObj.aData.subOrderId + '",1)>发货</a>';
                                    }else if(oObj.aData.upSeasoning == '1'){
                                        return '已经申请';
                                    }else{
                                        return '已经调货'
                                            + '&nbsp;&nbsp;' + '<a onclick=subOrderDispatch("' + oObj.aData.subOrderId + '",1)>发货</a>';
                                    }
                                }
                            } else {
                                if(orderDetail.deliverType == '20'&&orderDetail.deliverStatus=='20'){
                                    // 门店发货
                                    return '<a onclick=confirmReceiveGoods()>客户已收货</a>';
                                }else{
                                    return "已经发货";
                                }

                            }

                        }
                    }
                ],
                "bAutoWidth": false,    //关闭自适应列宽，导致列表后半部空白。IE8下现象
                "bProcessing": false,
                "bServerSide": true,
                "sAjaxSource": "../ord_dispatch/getDispatchOrdSubInfo.json",
                "fnServerParams": function (aoData) {
                    aoData.push({"name": "orderId", "value": orderId});
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

function rejectDispatch(obj) {
    sessionStorage.setItem("orderId", orderId);
    sessionStorage.setItem("orderSubId", obj);
    $('#customDialog').load('ord_dispatch/rejectDispatch.html');
}

function seasoningDispatch(obj) {
    var url = "/ord_dispatch/seasoningDispatch.json";

    var param = {
        orderId: orderId,
        subOrderId:obj
    };

    var success = function (data) {
        if (data.resultCode == '00') {
            showAlert('提交成功!', '提示', function () {
            });
            getData();
        } else {
            showAlert('提交失败,原因:' + data.resultMessage);
        }
    };
    var error = function () {
        showTip('网络异常.');
    };
    axse(url, {"data": JSON.stringify(param)}, success, error);
}

    function subOrderDispatch(obj,type) {
    showConfirmD('确定已经检查过门店有该货品,并且没有损坏吗？如果不检查而发货后果自负!',function () {
        sessionStorage.setItem("orderId", orderId);
        sessionStorage.setItem("orderSubId", obj);
        sessionStorage.setItem("type" ,type);
        $('#customDialog').load('ord_dispatch/processedDeliver.html');
    });
}

function confirmReceiveGoods(){
    showConfirm('确认客户已经拿到全部商品了吗？',function () {
        var param = {
            memberId:orderDetail.memberId,
            orderId:orderDetail.orderId,
        }
        var url = "/ord_master/confirmReceiptInMaster.json";
        var success = function (data) {
            if (data.resultCode == '00') {
                showAlert("提交成功!", function () {
                });
                getData();
            } else {
                showAlert('提交失败,原因:' + data.resultMessage);
            }
        };
        var error = function () {
            showAlert('提交失败,网络异常.');
        };
        axse(url, {'data': JSON.stringify(param)}, success, error);
    });
}


function applyTransfer(subId) {
    sessionStorage.setItem("orderSubId", subId);
    $('#customDialog').load('ord_transfer/apply.html');
}


//退回到列表画面
function transferBack() {
    //SubTableEditable.init();
    getData();
}
