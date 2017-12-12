/**
 * Created by wenxuechao on 16/7/23.
 */
var userId = sessionStorage.getItem("userId");
var orderId = sessionStorage.getItem("orderId");
var oTable;
var suboTable;
var historyTable;
var orderDetail = null;
$(document).ready(function () {
    //获取码表数据
    getEditData();
    //订单商品加载
    initGoodsTable();
    initSubGoodsTable();
    initHistoryTable();
    $('#return_btn').click(function () {
        back();
    });

    $("#btn_dispatch_all").click(function(){
        sessionStorage.setItem("orderId", orderId);
        sessionStorage.setItem("orderSubId", "");
        $('#customDialog').load('ord_change/dispatchStore.html');
    });
});

//退回到列表画面
function back() {
    if(sessionStorage.getItem("backPage") == null){
        $('#content').load(sessionStorage.getItem("backPage"));
    }else{
        $('#content').load('ord_change/list.html');
    }
    sessionStorage.setItem("backPage",null);
}

// 获取订单详细数据
function getEditData() {
    var url = "/ord_master/edit.json";
    var success = function (data) {
        if (data.resultCode == '00') {
            initForm(data.data);
        } else {
            showAlert('详细数据获取失败');
        }
    };
    var error = function () {
        showAlert('网络异常');
    };
    axse(url, {"orderId": orderId}, success, error);
}

//获得码表数据方法
function getOrderTypeOption() {
    var url = "/common/getCodeList.json";
    var success = function (data) {
        if (data.resultCode == '00') {
            var array = data.data;
            if (array != null) {
                //为品牌类型选择框添加数据
                $.each(array, function (index, item) {
                    var $option = $('<option>').attr('value', item.value).text(item.displayCn);
                    $("#type").append($option);
                });
            }
            //在这里发起详细信息的请求
            //编辑的时候获取数据进行回显
            $("#type").val(orderDetail.orderType);
        } else {
            showAlert('码表数据获取失败');
        }
    };
    var error = function () {
        showAlert('网络异常');
    };
    //品牌类型码表中的数据code为GDS_BRAND_TYPE 每一个码表的code不一样
    axse(url, {"data": "ORDER_TYPE"}, success, error);
}

//获取详细信息成功的回调 用来显示
function initForm(item) {
    orderDetail = item;
    $('#order_code').val(item.orderCode);
    $('#order_status').val(item.orderStatusName);
    if ("32" == item.orderStatus) {
        // 如果是退货完成,显示退货金额
        getReturnInfact();
    }
    $('#amount_totle').val(item.amountTotle + "元");
    $('#amount_discount').val(item.amountDiscount + "元");
    if (item.amountCoupon) {
        $('#amount_coupon').val(item.amountCoupon + "元");
    } else {
        $('#amount_coupon').val("0元");
    }
    $('#delivery_fee').val(item.deliveryFee + "元");
    $('#pay_infact').val(item.payInfact + "元");
    $('#pay_infact2').val(item.payInfact + "元");

    if (item.deliveryFree == 1) {
        $('#delivery_free').val("是");
        $('.delivery_free_yes').hide();
    } else {
        $('#delivery_free').val("否");
    }
    $('#order_time').val(new Date(item.orderTime).Format("yyyy-MM-dd hh:mm:ss"));
    $('#message').val(item.message);
    if (item.want_invoice == 1) {
        $('#want_invoice').val("是");
    } else {
        $('.want_invoice_yes').hide();
        $('#want_invoice').val("否");
    }
    $('#invoice_title').val(item.invoiceTitle);
    $('#invoice_address').val(item.invoiceAddress);
    $('#invoice_tel').val(item.invoiceTel);
    $('#invoice_taxno').val(item.invoiceTaxno);
    $('#invoice_bank').val(item.invoiceBank);
    $('#invoice_account').val(item.invoiceAccount);
    if (item.deliverType == 10) {
        $('#deliver_type').val('电商发货');
        $('.deliver_type_20').hide();
    } else {
        $('#deliver_type').val('门店自提');
        $('.deliver_type_10').hide();
    }
    $('#delivery_contactor').val(item.deliveryContactor);
    $('#delivery_phone').val(item.deliveryPhone);
    $('#delivery_postcode').val(item.deliveryPostcode);
    var address  = item.districtidProvince+item.districtidCity + item.districtidDistrict+item.deliveryAddress;
    $('#delivery_address').val(address);
    $('#store_name').val(item.storeName);
    $('#store_phone').val(item.storePhone);
    $('#store_delivery_name').val(item.storeDeliveryName);
    if (item.canReturn == 1) {
        $('#can_return').val("是");
    } else {
        $('#can_return').val("否");
    }
    if (item.canExchange == 1) {
        $('#can_exchange').val("是");
    } else {
        $('#can_exchange').val("否");
    }
    if (item.canDivide == 1) {
        $('#can_divide').val("是");
    } else {
        $('#can_divide').val("否");
    }
    if (item.activity) {
        $("#activity_name").val(item.activity.activityName);
        $("#activity_temp_name").val(item.activity.tempName);
        $("#activity_discount").val(item.amountDiscount);
        $("#activity_need_point").val(item.activity.needPoint);
        $("#activity_need_fee").val(item.activity.needFee + "元");
        if (item.activity.canReturn == "1") {
            $("#activity_can_return").val("是");
        } else {
            $("#activity_can_return").val("否");
        }
    } else {
        $("#activity_name").val("无");
        $("#activity_temp_name").val("无");
        $("#activity_discount").val("无");
        $("#activity_need_point").val("无");
        $("#activity_need_fee").val("无");
        $("#activity_can_return").val("是");
    }
    if (item.coupon) {
        $("#coupon_name").val(item.coupon.couponName);
        $("#coupon_worth").val(item.coupon.worth + "元");
    }
    // 获取码表数据
    getOrderTypeOption();
}

//  订单商品表格js初始化 开始
function initGoodsTable() {
    var tableOption = {
        "bProcessing": true,
        "bServerSide": true,
        "fnServerData": fnServerData,
        "bFilter": false,
        "bSort": false,
        "sAjaxSource": "../ord_master/getOrderGoodsInfo.json",
        "fnServerParams": function (aoData) {
            aoData.push({"name": "orderId", "value": orderId});
        },
        "aoColumns": [
            {"mData": "type"},
            {"mData": "goods_code"},
            {"mData": "goods_name"},
            {"mData": "sku"},
            {"mData": "quantity"},
            {"mData": "amount"},
            {"mData": "amount_discount"},
            {"mData": "action_name"}

        ]
    };
    oTable = $('#ord_goods_table').dataTable(tableOption);
}

//  订单商品表格js初始化 结束

//  子订单表格js初始化 开始
function initSubGoodsTable() {
    var tableOption = {
        "bProcessing": true,
        "bServerSide": true,
        "fnServerData": fnServerData,
        "bFilter": false,
        "bSort": false,
        "sAjaxSource": "../ord_master/getOrderSubInfo.json",
        "fnServerParams": function (aoData) {
            aoData.push({"name": "orderId", "value": orderId});
        },
        "aoColumns": [
            {"mData": "type"},
            {"mData": "goodsCode"},
            {"mData": "goodsName"},
            {"mData": "sku"},
            {"mData": "quantity"},
            {"mData": "price"},
            {"mData": "priceDiscount"},
            {"mData": "priceShare"},
            {"mData": "actionName"},
            {"mData": "deliverStatus"},
            {
                "mData": "deliverTime",
                "fnRender": function (rowData) {
                    if (rowData.aData.deliverTime != null) {
                        return new Date(rowData.aData.deliverTime).Format("yyyy-MM-dd hh:mm:ss");
                    } else {
                        return "";
                    }
                }
            },
            {"mData": "deliverType"},
            {"mData": "expressName"},
            {"mData": "expressNo"},
            {"mData": "storeName"},
            {"mData": "storePhone"},
            {"mData": "storeDeliveryName"},
            {"mData": "rexStatusName"},
            {"mData": "rexStoreName"}

        ]
    };
    suboTable = $('#ord_sub_table').dataTable(tableOption);
}

function initHistoryTable() {
    var tableOption = {
        "bProcessing": true,
        "bPaginate": false,
        "bServerSide": true,
        "fnServerData": fnServerData,
        "bFilter": false,
        "bSort": false,
        "sAjaxSource": "../ord_master/getOrderHistoryList.json",
        "fnServerParams": function (aoData) {
            aoData.push({"name": "orderId", "value": orderId});
        },
        "aoColumns": [
            {"mData": "orderStatusName"},
            {
                "mData": "insertTime",
                "fnRender": function (rowData) {
                    if (rowData.aData.insertTime != null) {
                        return new Date(rowData.aData.insertTime).Format("yyyy-MM-dd hh:mm:ss");
                    } else {
                        return "";
                    }
                }
            }
        ]
    };
    historyTable = $('#ord_history_table').dataTable(tableOption);
}

function getReturnInfact() {
    var url = "/ord_master/getReturnInfo.json";
    var param = {"orderId": orderId};
    var success = function (data) {
        if (data.resultCode == '00') {
            var refundTotal = 0;
            $.each(data.results, function (index, goods) {
                refundTotal += goods.refundInfact;
            });
            $("#refund_total").val(refundTotal.toFixed(2));
            $("#refundTotalDiv").show();
        } else {
            showAlert('码表数据获取失败');
        }
    };
    var error = function () {
        showAlert('码表数据获取失败');
    };
    axse(url, {"data": JSON.stringify(param)}, success, error);
}

