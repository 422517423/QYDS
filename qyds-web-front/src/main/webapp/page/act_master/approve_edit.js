var activityId = sessionStorage.getItem("activityId");
var activityDetail = null;
var goodsList = [];
var memberList = [];
var subActivityList = [];
var tempId;
var actionType;
$(document).ready(function () {
    $("#act_master_form input").attr("disabled", true);
    $("#act_master_form textarea").attr("disabled", true);
    $("#act_master_form select").attr("disabled", true);
    $("#act_master_form button").attr("disabled", true);
    $("#act_master_form a").attr("disabled", true);
    $("textarea").css("resize", "none");
    $("#btn_view_template").removeAttr("disabled");
    //获取详细
    getActivityDetail();

    $("#btn_cancel").click(function () {
        back();
    });
    $("#btn_approve").click(function () {
        approveConfirm();
    });

    $("#btn_reject").click(function () {
        rejectConfirm();
    });

    $("#btn_view_template").click(function () {
        if(!tempId||tempId.length == 0){
            showAlert("请先选择一个模板");
            return;
        }
        $('#customDialog').load('act_master/template_detail.html');
    });
});

//退回到列表画面
function back() {
    $('#content').load('act_master/approve_list.html');
}

function showHideByActivityUnit() {
    if ($("#activity_unit").val() == "10") {
        // 单品活动
        $("#div_sub_activity_select").hide();
        $("#div_selected_activity").hide();
        $("#btn_select_sub_activity").hide();
        $("#div_is_origin_price").hide();
        $("#div_need_point").hide();
        $("#div_need_fee").hide();
    } else if ($("#activity_unit").val() == "20") {
        $("#div_is_origin_price").show();
        $("#div_need_point").show();
        $("#div_need_fee").show();
        // 订单活动
        $("#div_sub_activity_select").show();
        if ($("#has_sub_activity").val() == "1") {
            $("#div_selected_activity").show();
            $("#btn_select_sub_activity").hide();
        } else {
            $("#div_selected_activity").hide();
            $("#btn_select_sub_activity").hide();
        }

    }
}

function refreshActivityGoodsList() {
    switch ($("#activity_goods_type").val()) {
        case "10":// 全部商品
            $("#btn_select_goods").hide();
            $("#div_selected_goods_type").hide();
            $("#div_selected_goods_brand").hide();
            $("#div_selected_goods").hide();
            $("#div_selected_skus").hide();
            $("#div_selected_sec").hide();
            break;
        case "20":// 按分类
            $("#btn_select_goods").show();
            $("#div_selected_goods_type").show();
            $("#div_selected_goods_brand").hide();
            $("#div_selected_goods").hide();
            $("#div_selected_skus").hide();
            $("#div_selected_sec").hide();
            refreshGoodsTypeList();
            break;
        case "30":// 按品牌
            $("#btn_select_goods").show();
            $("#div_selected_goods_type").hide();
            $("#div_selected_goods_brand").show();
            $("#div_selected_goods").hide();
            $("#div_selected_skus").hide();
            $("#div_selected_sec").hide();
            refreshGoodsBrandList();
            break;
        case "40":// 按商品
            $("#btn_select_goods").show();
            $("#div_selected_goods_type").hide();
            $("#div_selected_goods_brand").hide();
            $("#div_selected_skus").hide();
            $("#div_selected_sec").hide();
            $("#div_selected_goods").show();
            refreshGoodsList();
            break;
        case "50":// 按SKU
        {
            $("#btn_select_goods").show();
            $("#div_selected_goods_type").hide();
            $("#div_selected_goods_brand").hide();
            $("#div_selected_goods").hide();
            if('11' == actionType){
                // 秒杀活动配置
                $("#div_selected_skus").hide();
                $("#div_selected_sec").show();
            } else {
                $("#div_selected_skus").show();
                $("#div_selected_sec").hide();
            }
            refreshSkuList();
        }
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
        $item.removeClass('template').appendTo('#goodsList').show();
    });
}

//退回到列表画面
function gotoListPage(){
    $('#content').load('act_master/approve_list.html');
}

function refreshSkuList() {

    if('11' == actionType){
        refreshSecSkuList();
        return;
    }

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

function refreshSecSkuList() {
    // 清空
    $("#secKillSkuList").html($(".template.sec-item"));
    if (!goodsList || goodsList.length == 0) {
        return;
    }
    console.log('refreshSecSkuList  each');
    $.each(goodsList, function (index, sku) {
        if (sku.type == "20") {
            var skuDetail = JSON.parse(sku.skucontent);
            sku.colorName = skuDetail.sku_value.split("_")[0];
            sku.sizeName = skuDetail.sku_value.split("_")[1];
        }
        var $item = $('#secKillSkuList .template.sec-item').clone();
        // 赋值
        $('.sec-item-index', $item).text(index + 1);
        $('.sec-item-name', $item).text(sku.goodsCode);
        $('.sec-item-color', $item).text(sku.colorName);
        $('.sec-item-size', $item).text(sku.sizeName);
        $('.sec-item-price', $item).text(sku.actPrice);
        $('.sec-item-sec', $item).text(sku.quantity);
        $('.sec-item-max', $item).text(sku.buyMax);
        $item.removeClass('template').appendTo('#secKillSkuList').show();
    });
}

function refreshActivityMemberList() {
    switch ($("#activity_member_type").val()) {
        case "10":// 全部
            $("#btn_select_members").hide();
            $("#div_selected_member_level").hide();
            $("#div_selected_member_group").hide();
            break;
        case "20":// 会员组
            $("#btn_select_members").show();
            $("#div_selected_member_level").hide();
            $("#div_selected_member_group").show();
            refreshMemberGroupList();
            break;
        case "30":// 会员组
            $("#btn_select_members").show();
            $("#div_selected_member_level").show();
            $("#div_selected_member_group").hide();
            refreshMemberLevelList();
            break;
    }
}

function refreshMemberGroupList() {
    // 清空
    $("#memberGroupList").html($(".template.member-item"));
    if (!memberList || memberList.length == 0) {
        return;
    }
    $.each(memberList, function (index, member) {
        var $item = $('#memberGroupList .template.member-group-item').clone();
        // 赋值
        $('.member-group-item-index', $item).text(index + 1);
        $('.member-group-item-name', $item).text(member.memberName);
        $item.removeClass('template').appendTo('#memberGroupList').show();
    });
}

function refreshMemberLevelList() {
    // 清空
    $("#memberLevelList").html($(".template.member-level-item"));
    if (!memberList || memberList.length == 0) {
        return;
    }
    $.each(memberList, function (index, member) {
        var $item = $('#memberLevelList .template.member-level-item').clone();
        // 赋值
        $('.member-level-item-index', $item).text(index + 1);
        $('.member-level-item-name', $item).text(member.memberName);
        $item.removeClass('template').appendTo('#memberLevelList').show();
    });
}
function refreshSubActivityList() {
    // 清空
    $("#subActivityList").html($(".template.activity-item"));
    if (!subActivityList || subActivityList.length == 0) {
        return;
    }
    $.each(subActivityList, function (index, activity) {
        var $item = $('#subActivityList .template.activity-item').clone();
        // 赋值
        $('.activity-item-index', $item).text(index + 1);
        $('.activity-item-name', $item).text(activity.activityName);
        $item.removeClass('template').appendTo('#subActivityList').show();
    });
}

function initForm(data) {
    activityDetail = data;
    tempId = data.tempId;
    actionType = data.actitionType;
    $("#temp_name").val(data.tempName);
    $("#activity_name").val(data.activityName);
    $("#need_point").val(data.needPoint);
    $("#need_fee").val(data.needFee);
    $("#is_member_activity").val(data.isMemberActivity);
    $("#has_sub_activity").val(data.hasSubActivity);
    // "10";"审批中";"20";"审批通过";"30";"审批驳回";"40";"未申请"
    if (data.approveStatus == "10") {
        $("#btn_approve").show();
        $("#btn_reject").show();
        $("#approve_content").removeAttr("disabled");
    }
    if (data.startTime != null) {
        if('11' == actionType){
            $("#start_time").val(new Date(data.startTime).Format("yyyy-MM-dd hh:mm"));
        } else {
            $("#start_time").val(new Date(data.startTime).Format("yyyy-MM-dd"));
        }
    } else {
        $("#start_time").val("");
    }
    if (data.endTime != null) {
        if('11' == actionType){
            $("#end_time").val(new Date(data.endTime).Format("yyyy-MM-dd hh:mm"));
        } else {
            $("#end_time").val(new Date(data.endTime).Format("yyyy-MM-dd"));
        }
    } else {
        $("#end_time").val("");
    }
    $("#can_return").val(data.canReturn);
    $("#can_exchange").val(data.canExchange);
    $("#limit_count").val(data.limitCount);
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
    getGoodsTypeOptionCode();
    getMemberTypeOptionCode();
    getActivityUnitOptionCode();
    getPriceLimitOptionCode();
}

//获得码表数据方法
function getGoodsTypeOptionCode() {
    var url = "/common/getCodeList.json";
    var param = {"data": "ACTITION_GOODS_TYPE"};
    var success = function (data) {
        if (data.resultCode == '00') {
            var array = data.data;
            if (array != null) {
                // "10";"全部"
                // "20";"按分类"
                // "30";"按品牌"
                // "40";"单品"
                //为品牌类型选择框添加数据
                $.each(array, function (index, item) {
                    var $option = $('<option>').attr('value', item.value).text(item.displayCn);
                    $("#activity_goods_type").append($option);
                });
                // 编辑还是新建
                if (activityId && activityId.length > 0) {
                    // 赋值
                    $("#activity_goods_type").val(activityDetail.goodsType);
                    if(activityDetail.goodsType == '60'){
                        $('#sell_year_area').show();
                        $('#season_area').show();

                        getSellerYears();
                    }else if(activityDetail.goodsType == '70'){
                        $('#erp_brand_area').show();
                        getErpBrands();
                    }else if(activityDetail.goodsType == '80'){
                        $('#line_code_area').show();
                        getErpLineCode();
                    }
                    goodsList = activityDetail.goodsList;
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

function getMemberTypeOptionCode() {
    var url = "/common/getCodeList.json";
    var param = {"data": "ACTITION_MEMBER_TYPE"};
    var success = function (data) {
        if (data.resultCode == '00') {
            var array = data.data;
            if (array != null) {
                // "10";"全部"
                // "20";"会员组"
                // "30";"个人"
                //为品牌类型选择框添加数据
                $.each(array, function (index, item) {
                    var $option = $('<option>').attr('value', item.value).text(item.displayCn);
                    $("#activity_member_type").append($option);
                });
                // 编辑还是新建
                if (activityId && activityId.length > 0) {
                    // 赋值
                    $("#activity_member_type").val(activityDetail.memberType);
                    memberList = activityDetail.memberList;
                }
                refreshActivityMemberList();
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

function getActivityUnitOptionCode() {
    var url = "/common/getCodeList.json";
    var param = {"data": "ACTITION_UNIT"};
    var success = function (data) {
        if (data.resultCode == '00') {
            var array = data.data;
            if (array != null) {
                $.each(array, function (index, item) {
                    var $option = $('<option>').attr('value', item.value).text(item.displayCn);
                    $("#activity_unit").append($option);
                });
                // 编辑还是新建
                if (activityId && activityId.length > 0) {
                    // 赋值
                    $("#activity_unit").val(activityDetail.unit);
                    subActivityList = activityDetail.subActivityList;
                    showHideByActivityUnit();
                    refreshSubActivityList();
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
                if (activityDetail != null) {
                    $("#is_origin_price").val(activityDetail.isOriginPrice);
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

function getActivityDetail() {
    var url = "/act_master/getDetail.json";
    var param = {"activityId": activityId};
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
    axse(url, {"data": JSON.stringify(param)}, success, error);
}

function approveConfirm() {
    showConfirm("确定要通过吗?", function () {
        approveItem();
    });
}

function approveItem() {
    var url = "/act_master/approve.json";
    var param = {
        "activityId": activityId,
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
    var url = "/act_master/reject.json";
    var param = {
        "activityId": activityId,
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
                    if(activityDetail == null){
                        sellYear = null;
                    }else if(activityDetail.goodsTypeValue == null || activityDetail.goodsTypeValue.length == 0){
                        sellYear = null;
                    }else{
                        sellYear = activityDetail.goodsTypeValue.split('_')[0];
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
            if (activityDetail != null) {
                // 赋值
                if(activityDetail.goodsType == '70'){
                    $("#activity_goods_erp_brand").val(activityDetail.goodsTypeValue);
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
                if (activityDetail != null) {
                    // 赋值
                    if(activityDetail.goodsType == '80'){
                        $("#activity_goods_line_code").val(activityDetail.goodsTypeValue);
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
                if (activityDetail != null) {
                    // 赋值
                    if(activityDetail.goodsType == '60'){
                        var array = activityDetail.goodsTypeValue.split('_');
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