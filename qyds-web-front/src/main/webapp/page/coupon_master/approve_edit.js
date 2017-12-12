var couponId = sessionStorage.getItem("couponId");
var couponDetail = null;
$(document).ready(function () {
    $("#coupon_master_form input").attr("disabled", true);
    $("#coupon_master_form textarea").attr("disabled", true);
    $("#coupon_master_form select").attr("disabled", true);
    $("#coupon_master_form button").attr("disabled", true);
    $("#coupon_master_form a").attr("disabled", true);
    $("textarea").css("resize", "none");
    getDetail();
    $("#btn_cancel").click(function () {
        back();
    });
    $("#btn_approve").click(function () {
        approveConfirm(couponId);
    });

    $("#btn_reject").click(function () {
        rejectConfirm(couponId);
    });
});

//退回到列表画面
function back() {
    $('#content').load('coupon_master/approve_list.html');
}


function getDetail() {
    var url = "/coupon_master/getDetail.json";
    var param = {"couponId": couponId};
    var success = function (data) {
        if (data.resultCode == '00') {
            initForm(data.data);
        } else {
            showAlert('码表数据获取失败');
        }
    };
    var error = function () {
        showAlert('码表数据获取失败');
    };
    axse(url, {"data": JSON.stringify(param)}, success, error);
}

function showHideForm() {
    if ($("#distribute_type").val() == "50") {
        $("#div_exchange_point").show();
    } else {
        $("#div_exchange_point").hide();
    }
    if(("10" == $("#coupon_type").val() && "10" == $("#distribute_type").val()) || "20" == $("#coupon_type").val()||"50" ==  $("#distribute_type").val()){
        $("#div_member_level").show();
    }else{
        $("#div_member_level").hide();
    }
    if ($("#distribute_type").val() == "60") {
        $("#div_price").show();
    } else {
        $("#div_price").hide();
    }
}

function refreshActivityGoodsList() {
    switch ($("#goods_type").val()) {
        case "10":// 全部商品
            $("#btn_select_goods").hide();
            $("#div_selected_goods_type").hide();
            $("#div_selected_goods_brand").hide();
            $("#div_selected_goods").hide();
            $("#div_selected_skus").hide();
            break;
        case "20":// 按分类
            $("#btn_select_goods").show();
            $("#div_selected_goods_type").show();
            $("#div_selected_goods_brand").hide();
            $("#div_selected_goods").hide();
            $("#div_selected_skus").hide();
            refreshGoodsTypeList();
            break;
        case "30":// 按品牌
            $("#btn_select_goods").show();
            $("#div_selected_goods_type").hide();
            $("#div_selected_goods_brand").show();
            $("#div_selected_goods").hide();
            $("#div_selected_skus").hide();
            refreshGoodsBrandList();
            break;
        case "40":// 按商品
            $("#btn_select_goods").show();
            $("#div_selected_goods_type").hide();
            $("#div_selected_goods_brand").hide();
            $("#div_selected_skus").hide();
            $("#div_selected_goods").show();
            refreshGoodsList();
            break;
        case "50":// 按SKU
            $("#btn_select_goods").show();
            $("#div_selected_goods_type").hide();
            $("#div_selected_goods_brand").hide();
            $("#div_selected_goods").hide();
            $("#div_selected_skus").show();
            refreshSkuList();
            break;
    }
}

function refreshGoodsTypeList() {
    // 清空
    $("#goodsTypeList").html($(".template.goods-type-item"));
    if (!goodsList || goodsList.length == 0) {
        return;
    }
    $.each(goodsList, function (index, goodsType) {
        var $item = $('#goodsTypeList .template.goods-type-item').clone();
        // 赋值
        $('.goods-type-item-index', $item).text(index + 1);
        $('.goods-type-item-name', $item).text(goodsType.goodsName);
        $('.goods-type-item-delete', $item).click(function () {
            goodsList.splice(index, 1);
            refreshGoodsTypeList();
        });
        $item.removeClass('template').appendTo('#goodsTypeList').show();
    });
}

function refreshGoodsBrandList() {
    // 清空
    $("#goodsBrandList").html($(".template.goods-brand-item"));
    if (!goodsList || goodsList.length == 0) {
        return;
    }
    $.each(goodsList, function (index, goodsBrand) {
        var $item = $('#goodsBrandList .template.goods-brand-item').clone();
        // 赋值
        $('.goods-brand-item-index', $item).text(index + 1);
        $('.goods-brand-item-name', $item).text(goodsBrand.goodsName);
        $item.removeClass('template').appendTo('#goodsBrandList').show();
    });
}

function refreshGoodsList() {
    console.log(goodsList);
    // 清空
    $("#goodsList").html($(".template.goods-item"));
    if (!goodsList || goodsList.length == 0) {
        return;
    }
    $.each(goodsList, function (index, goods) {
        var $item = $('#goodsList .template.goods-item').clone();
        // 赋值
        $('.goods-item-index', $item).text(index + 1);
        $('.goods-item-name', $item).text(goods.goodsName);
        $('.goods-item-code', $item).text(goods.goodsCode);
        $item.removeClass('template').appendTo('#goodsList').show();
    });
}

function refreshSkuList() {
    // 清空
    $("#skuList").html($(".template.sku-item"));
    if (!goodsList || goodsList.length == 0) {
        return;
    }
    $.each(goodsList, function (index, sku) {
        if (sku.type == "20") {
            var skuDetail = JSON.parse(sku.skucontent);
            sku.colorName = skuDetail.sku_value.split("_")[0];
            sku.sizeName = skuDetail.sku_value.split("_")[1];
        }

        var $item = $('#skuList .template.sku-item').clone();
        // 赋值
        $('.sku-item-index', $item).text(index + 1);
        $('.sku-item-name', $item).text(sku.goodsCode);
        $('.sku-item-color', $item).text(sku.colorName);
        $('.sku-item-size', $item).text(sku.sizeName);
        $item.removeClass('template').appendTo('#skuList').show();
    });
}

//获得码表数据方法
function getCouponTypeOptionCode() {
    var url = "/common/getCodeList.json";
    var param = {"data": "COUPON_TYPE"};
    var success = function (data) {
        if (data.resultCode == '00') {
            var array = data.data;
            if (array != null) {
                //为品牌类型选择框添加数据
                $.each(array, function (index, item) {
                    var $option = $('<option>').attr('value', item.value).text(item.displayCn);
                    $("#coupon_type").append($option);
                });
                // 编辑还是新建
                if (couponDetail != null) {
                    $("#coupon_type").val(couponDetail.couponType);
                }
            }
        } else {
            showAlert('码表数据获取失败');
        }
    };
    var error = function () {
        showAlert('码表数据获取失败');
    };
    //品牌类型码表中的数据code为GDS_BRAND_TYPE 每一个码表的code不一样
    axse(url, param, success, error);
}

//获得码表数据方法
function getDistributeTypeOptionCode() {
    var url = "/common/getCodeList.json";
    var param = {"data": "COUPON_DISTRIBUTE_TYPE"};
    var success = function (data) {
        if (data.resultCode == '00') {
            var array = data.data;
            if (array != null) {
                //为品牌类型选择框添加数据
                $.each(array, function (index, item) {
                    var $option = $('<option>').attr('value', item.value).text(item.displayCn);
                    $("#distribute_type").append($option);
                });
                // 编辑还是新建
                if (couponDetail != null) {
                    $("#distribute_type").val(couponDetail.distributeType);
                }
                showHideForm();
            }
        } else {
            showAlert('码表数据获取失败');
        }
    };
    var error = function () {
        showAlert('码表数据获取失败');
    };
    //品牌类型码表中的数据code为GDS_BRAND_TYPE 每一个码表的code不一样
    axse(url, param, success, error);
}

function initForm(data) {
    console.log(data);
    couponDetail = data;
    $("#coupon_type").val(data.couponType);
    $("#coupon_name").val(data.couponName);
    $("#worth").val(data.worth);
    $("#coupon_code").val(data.couponCode);
    $("#max_count").val(data.maxCount);
    $("#min_order_price").val(data.minOrderPrice);
    $("#min_goods_count").val(data.minGoodsCount);
    $("#valid_days").val(data.validDays);
    $("#coupon_image").attr("src", displayUri + orignal + data.couponImage);
    // "10";"审批中";"20";"审批通过";"30";"审批驳回";"40";"未申请"
    if (data.approveStatus == "10") {
        $("#btn_approve").show();
        $("#btn_reject").show();
        $("#approve_content").removeAttr("disabled");
    }
    if (data.startTime != null) {
        $("#start_time").val(new Date(data.startTime).Format("yyyy-MM-dd"));
    } else {
        $("#start_time").val("");
    }
    if (data.endTime != null) {
        $("#end_time").val(new Date(data.endTime).Format("yyyy-MM-dd"));
    } else {
        $("#end_time").val("");
    }
    if (data.sendStartTime != null) {
        $("#send_start_time").val(new Date(data.sendStartTime).Format("yyyy-MM-dd"));
    } else {
        $("#send_start_time").val("");
    }
    if (data.sendEndTime != null) {
        $("#send_end_time").val(new Date(data.sendEndTime).Format("yyyy-MM-dd"));
    } else {
        $("#send_end_time").val("");
    }
    $("#comment").val(data.comment);
    $("#apply_user_name").val(data.applyUserName);
    $("#apply_content").val(data.applyContent);
    if (data.applyTime != null) {
        $("#apply_time").val(new Date(data.applyTime).Format("yyyy-MM-dd hh:mm:ss"));
    } else {
        $("#apply_time").val("");
    }
    if (data.approveTime != null) {
        $("#approve_time").val(new Date(data.approveTime).Format("yyyy-MM-dd hh:mm:ss"));
    } else {
        $("#approve_time").val("");
    }
    $("#approve_status").val(data.approveStatusCn);
    $("#approve_content").val(data.approveContent);
    $("#approve_user_name").val(data.approveUserName);
    $("input[name='coupon_style'][value="+data.couponStyle+"]").attr("checked",true);
    $("#discount").val(data.discount);
    $("#member_level").val(data.memberLevel);
    $("#exchange_point").val(data.exchangePoint);
    $("#per_max_count").val(data.perMaxCount);
    $("#price").val(data.price);
    $("#is_valid").val(data.isValid);
    getCouponTypeOptionCode();
    getDistributeTypeOptionCode();
    getCouponScopeOptionCode();
    getPriceLimitOptionCode();
    getGoodsTypeOptionCode();
    getMemberLevelOptionCode();
    showHideByCouponStyle();
    //
    // if ("20" == data.couponType || "30" == data.couponType) {
    //     $("#div_valid_days").show();
    //     $("#div_start_time").hide();
    //     $("#div_end_time").hide();
    // } else {
    //     $("#div_valid_days").hide();
    //     $("#div_start_time").show();
    //     $("#div_end_time").show();
    // }
    // 生日劵才会要求选择会员级别
    if(("10" == $("#coupon_type").val() && "10" == $("#distribute_type").val()) || "20" == data.couponType ||"50" ==  data.distributeType){
        $("#div_member_level").show();
    }else{
        $("#div_member_level").hide();
    }

    // 按使用天数
    if (data.validDays && data.validDays > 0) {
        $("#div_start_time").hide();
        $("#div_end_time").hide();
        $("#div_valid_days").show();
        $("input[name='validDate'][value='20']").attr("checked", true);
    } else {
        $("#div_start_time").show();
        $("#div_end_time").show();
        $("#div_valid_days").hide();
        $("input[name='validDate'][value='10']").attr("checked", true);
    }
}

function showHideByCouponStyle() {
    if ("0" == $("input[name=coupon_style]:checked").val()) {
        //抵值
        $("#div_worth").show();
        $("#div_discount").hide();
    } else {
        //折扣
        $("#div_worth").hide();
        $("#div_discount").show();
    }
}
//获得码表数据方法
function getCouponScopeOptionCode() {
    var url = "/common/getCodeList.json";
    var param = {"data": "COUPON_SCOPE"};
    var success = function (data) {
        if (data.resultCode == '00') {
            var array = data.data;
            if (array != null) {
                //为品牌类型选择框添加数据
                $.each(array, function (index, item) {
                    var $option = $('<option>').attr('value', item.value).text(item.displayCn);
                    $("#coupon_scope").append($option);
                });
                // 编辑还是新建
                if (couponDetail != null) {
                    $("#coupon_scope").val(couponDetail.couponScope);
                }
            }
        } else {
            showAlert('码表数据获取失败');
        }
    };
    var error = function () {
        showAlert('码表数据获取失败');
    };
    //品牌类型码表中的数据code为GDS_BRAND_TYPE 每一个码表的code不一样
    axse(url, param, success, error);
}

//获得码表数据方法
function getPriceLimitOptionCode() {
    var url = "/common/getCodeList.json";
    var param = {"data": "COUPON_GOODS_PRICE_LIMIT"};
    var success = function (data) {
        if (data.resultCode == '00') {
            var array = data.data;
            if (array != null) {
                //为品牌类型选择框添加数据
                $.each(array, function (index, item) {
                    var $option = $('<option>').attr('value', item.value).text(item.displayCn);
                    $("#is_origin_price").append($option);
                });
                // 编辑还是新建
                if (couponDetail != null) {
                    $("#is_origin_price").val(couponDetail.isOriginPrice);
                }
            }
        } else {
            showAlert('码表数据获取失败');
        }
    };
    var error = function () {
        showAlert('码表数据获取失败');
    };
    //品牌类型码表中的数据code为GDS_BRAND_TYPE 每一个码表的code不一样
    axse(url, param, success, error);
}


//获得码表数据方法
function getGoodsTypeOptionCode() {
    var url = "/common/getCodeList.json";
    var param = {"data": "ACTITION_GOODS_TYPE"};
    var success = function (data) {
        if (data.resultCode == '00') {
            var array = data.data;
            if (array != null) {
                //为品牌类型选择框添加数据
                $.each(array, function (index, item) {
                    var $option = $('<option>').attr('value', item.value).text(item.displayCn);
                    $("#goods_type").append($option);
                });
                // 编辑还是新建
                if (couponDetail != null) {
                    $("#goods_type").val(couponDetail.goodsType);
                    if(couponDetail.goodsType == '60'){
                        $('#sell_year_area').show();
                        $('#season_area').show();

                        getSellerYears();
                    }else if(couponDetail.goodsType == '70'){
                        $('#erp_brand_area').show();
                        getErpBrands();
                    }else if(couponDetail.goodsType == '80'){
                        $('#line_code_area').show();
                        getErpLineCode();
                    }
                    goodsList = couponDetail.goodsList;
                }
                refreshActivityGoodsList();
            }
        } else {
            showAlert('码表数据获取失败');
        }
    };
    var error = function () {
        showAlert('码表数据获取失败');
    };
    //品牌类型码表中的数据code为GDS_BRAND_TYPE 每一个码表的code不一样
    axse(url, param, success, error);
}

//获得码表数据方法
function getMemberLevelOptionCode() {
    var url = "/common/getCodeList.json";
    var param = {"data": "MEMBER_LEVEL"};
    var success = function (data) {
        if (data.resultCode == '00') {
            var array = data.data;
            if (array != null) {
                //为品牌类型选择框添加数据
                $.each(array, function (index, item) {
                    var $option = $('<option>').attr('value', item.value).text(item.displayCn);
                    $("#member_level").append($option);
                });
                // 编辑还是新建
                if (couponDetail != null) {
                    $("#member_level").val(couponDetail.memberLevel);
                }
            }
        } else {
            showAlert('码表数据获取失败');
        }
    };
    var error = function () {
        showAlert('码表数据获取失败');
    };
    //品牌类型码表中的数据code为GDS_BRAND_TYPE 每一个码表的code不一样
    axse(url, param, success, error);
}

function approveConfirm() {
    showConfirm("确定要通过吗?", function () {
        approveItem();
    });
}

function approveItem() {
    var url = "/coupon_master/approve.json";
    var param = {
        "couponId": couponId,
        "approveContent": $("#approve_content").val()
    };
    var success = function (data) {
        if (data.resultCode == '00') {
            showAlert("提交成功!", function () {
                back();
            });
        } else {
            showAlert('提交失败,原因:' + data.resultMessage);
        }
    };
    var error = function () {
        showAlert('提交失败,网络异常.');
    };
    axse(url, {'data': JSON.stringify(param)}, success, error);
}

function rejectConfirm() {
    showConfirm("确定要驳回吗?", function () {
        rejectItem();
    })
}

function rejectItem() {
    var url = "/coupon_master/reject.json";
    var param = {
        "couponId": couponId,
        "approveContent": $("#approve_content").val()
    };
    var success = function (data) {
        if (data.resultCode == '00') {
            showAlert("提交成功!", function () {
                back();
            });
        } else {
            showAlert('提交失败,原因:' + data.resultMessage);
        }
    };
    var error = function () {
        showAlert('提交失败,网络异常.');
    };
    axse(url, {'data': JSON.stringify(param)}, success, error);
}


function getSellerYears() {
    var url = "/act_master/getSellerYears.json";
    var success = function (data) {
        if (data.resultCode == '00') {
            console.log(data);
            $("#activity_goods_sell_year").empty();
            var array = data.data;
            if (array != null) {
                //为年份选择框添加数据
                $.each(array, function (index, item) {
                    var $option = $('<option>').attr('value', item.sell_year).text(item.sell_year);
                    $("#activity_goods_sell_year").append($option);

                    var sellYear = null;
                    if(couponDetail == null){
                        sellYear = null;
                    }else if(couponDetail.goodsTypeValue == null || couponDetail.goodsTypeValue.length == 0){
                        sellYear = null;
                    }else{
                        sellYear = couponDetail.goodsTypeValue.split('_')[0];
                    }

                    if(sellYear == null || sellYear.length == 0){
                        if(index == 0){
                            getSellerSeasons(item.sell_year);
                        }
                    }else if(item.sell_year == sellYear){
                        getSellerSeasons(item.sell_year);
                    }
                });
            }
        } else {
            showAlert('详细数据获取失败');
        }
    };
    var error = function () {
        showAlert('网络异常');
    };
    axse(url, {}, success, error);
}

function getErpBrands() {
    var url = "/act_master/getErpBrands.json";
    var success = function (data) {
        if (data.resultCode == '00') {
            console.log(data);
            $("#activity_goods_erp_brand").empty();
            var array = data.data;
            if (array != null) {
                //为erp品牌选择框添加数据
                $.each(array, function (index, item) {
                    var $option = $('<option>').attr('value', item.brand_code).text(item.brand_name);
                    $("#activity_goods_erp_brand").append($option);

                });
            }

            // 编辑还是新建
            if (couponDetail != null) {
                // 赋值
                if(couponDetail.goodsType == '70'){
                    $("#activity_goods_erp_brand").val(couponDetail.goodsTypeValue);
                }
            }

        } else {
            showAlert('详细数据获取失败');
        }
    };
    var error = function () {
        showAlert('网络异常');
    };
    axse(url, {}, success, error);
}

function getErpLineCode() {
    var url = "/act_master/getErpLineCode.json";
    var success = function (data) {
        if (data.resultCode == '00') {
            console.log(data);
            $("#activity_goods_line_code").empty();
            var array = data.data;
            if (array != null) {
                //为erp生产线选择框添加数据
                $.each(array, function (index, item) {
                    var $option = $('<option>').attr('value', item.line_code).text(item.line_name);
                    $("#activity_goods_line_code").append($option);

                });

                // 编辑还是新建
                if (couponDetail != null) {
                    // 赋值
                    if(couponDetail.goodsType == '80'){
                        $("#activity_goods_line_code").val(couponDetail.goodsTypeValue);
                    }
                }
            }
        } else {
            showAlert('详细数据获取失败');
        }
    };
    var error = function () {
        showAlert('网络异常');
    };
    axse(url, {}, success, error);
}


function getSellerSeasons(year) {
    var url = "/act_master/getSellerSeasons.json";
    var success = function (data) {
        if (data.resultCode == '00') {
            console.log(data);
            var array = data.data;
            $("#activity_goods_season_code").empty();
            $("#activity_goods_season_code").append('<option value="">请选择...</option>');
            if (array != null) {
                //为季节选择框添加数据
                $.each(array, function (index, item) {
                    var $option = $('<option>').attr('value', item.season_code).text(item.season_name);
                    $("#activity_goods_season_code").append($option);
                });

                // 编辑还是新建
                if (couponDetail != null) {
                    // 赋值
                    if(couponDetail.goodsType == '60'){
                        var array = couponDetail.goodsTypeValue.split('_');
                        if(array[0] != null&&array[0].length > 0){
                            $("#activity_goods_sell_year").val(array[0]);
                        }
                        if(array[1] != null&&array[1].length > 0){
                            $("#activity_goods_season_code").val(array[1]);
                        }

                    }
                }
            }
        } else {
            showAlert('详细数据获取失败');
        }
    };
    var error = function () {
        showAlert('网络异常');
    };
    axse(url, {"data": year}, success, error);
}