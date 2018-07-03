/**
 * Created by wenxuechao on 16/7/23.
 */
var userId = sessionStorage.getItem("userId");
var orderId = sessionStorage.getItem("orderId");
var oTable;
var suboTable;
var orderDetail = null;
var refundTempId = null;
$(document).ready(function () {
    //获取码表数据
    getEditData();
    //订单商品加载
    initGoodsTable();
    initSubGoodsTable();
    $('#return_btn').click(function () {
        back();
    });
    $("#refund_dialog_btn").click(function () {
        //refundSubmit();
        //20180124 当输入完退款金额后，点击确定按钮，弹出确认框
        if ($("#refund_dialog_content").val() == "") {
            showRefundTip("请输入退款金额");
            return;
        }else{
            $("#priceSpan").text($("#refund_dialog_content").val());
            $("#qu_dialog").modal('show');
        }
    });

    // 20180124 确认框点击是
    $("#qu_dialog_btn").click(function () {
       refundSubmit();
    });
});

function refreshData() {
    getEditData();
    oTable.fnDraw();
    suboTable.fnDraw();
}

//退回到列表画面
function back() {
    $('#content').load('ord_return_goods/refund_list.html');
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
    if(item.amountCoupon){
        $('#amount_coupon').val(item.amountCoupon + "元");
    }else{
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
            {
                "mData": null,
                "fnRender": function (oObj) {
                    var strOperate = "";
                    // "退换货状态";"10";"申请中";
                    // "退换货状态";"11";"申请驳回";
                    // "退换货状态";"20";"退货待收"
                    // "退换货状态";"22";"退货已收"
                    // "退换货状态";"31";"返货已发";
                    // "退换货状态";"32";"返货已收";
                    // "退换货状态";"40";"待退款";
                    // "退换货状态";"41";"已退款";
                    if (oObj.aData.rexStatus == '40') {
                        strOperate += '<a href="javascript:void(0);" onclick=refundConfirm("' + oObj.aData.rexOrderId + '");>&nbsp;退款确认</a>';
                    }
                    return strOperate;
                }
            },
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

//  子订单表格js初始化 结束

/**
 * 退款确认按钮
 * @功能 跳转至退单页面,退款至买家
 * @param obj
 */
function refundConfirm(rexOrderId) {
    $("#refund_dialog_content").val("");
    refundTempId = rexOrderId;
    $("#refund_dialog").modal('show');
}

function refundSubmit() {
    if ($("#refund_dialog_content").val() == "") {
        showRefundTip("请输入退款金额");
        return;
    }
    var url = "/ord_return_goods/refund.json";
    var param = {
        "rexOrderId": refundTempId,
        "refundInfact": $("#refund_dialog_content").val()
    };
    var success = function (data) {
        if (data.resultCode == '00') {
            $("#refund_dialog").modal('hide');
            // 关闭确认框
            $("#qu_dialog").modal('hide');
            showAlert("提交成功!");
            refreshData()
        } else {
            showRefundTip('提交失败,原因:' + data.resultMessage);
        }
    };
    var error = function () {
        showRefundTip('提交失败,网络异常.');
    };
    axse(url, {'data': JSON.stringify(param)}, success, error);
}

function showRefundTip(message) {
    $("#refund_dialog_tip").text(message);
    setTimeout(function () {
        $("#refund_dialog_tip").text("");
    }, 2000);
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