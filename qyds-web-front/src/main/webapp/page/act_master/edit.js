var activityId = sessionStorage.getItem("activityId");
var editable = sessionStorage.getItem("editable");
var isAdd = false;
var activityDetail = null;
var goodsList = [];
var memberList = [];
var subActivityList = [];
var tempId;
var actionType;
$(document).ready(function () {
    // 审批部分不可编辑
    $("#act_master_approve_form input").attr("disabled", true);
    $("#act_master_approve_form textarea").attr("disabled", true);
    $("textarea").css("resize", "none");
    if (editable == "0") {
        $("#act_master_form input").attr("disabled", true);
        $("#act_master_form textarea").attr("disabled", true);
        $("#act_master_form select").attr("disabled", true);
        $("#act_master_form button").attr("disabled", true);
        $("#act_master_form a").attr("disabled", true);
        $("#btn_save").hide();
    } else {
        $('.date-picker').datepicker({
            autoclose: true,
            startDate: new Date()
        });

        $('#datetimepicker').datetimepicker();
    }
    $("#activity_unit").attr("disabled", true);
    $("#btn_view_template").removeAttr("disabled");

    if (activityId && activityId.length > 0) {
        //获取详细
        getActivityDetail();
    } else {
        isAdd = true;
        activityId = getUUID();
        getMemberTypeOptionCode();
        getGoodsTypeOptionCode();
        getActivityUnitOptionCode();
        getPriceLimitOptionCode();
    }

    //表单验证的定义
    $("#act_master_form").validate({
        errorElement: 'span', //default input error message container
        errorClass: 'help-block', // default input error message class
        focusInvalid: false, // do not focus the last invalid input
        errorPlacement: function (error, element) {
            if ($(element).attr('name') == 'start_time' || $(element).attr('name') == 'end_time') {
                $(element).parent().after(error)
            } else {
                $(element).after(error)
            }
        },
        rules: {
            temp_name: {
                required: true
            },
            activity_name: {
                required: true
            },
            start_time: {
                required: true
            },
            end_time: {
                required: true
            },
            need_fee: {
                required: true,
                maxlength: 9
            },
            need_point: {
                required: true,
                maxlength: 9
            },
            activity_goods_type: {
                required: true,
            },
        },
        messages: {
            temp_name: {
                required: "请选择模板"
            },
            activity_name: {
                required: "请输入活动名称",
                maxlength: "不能超过30个字符"
            },
            limit_count: {
                maxlength: "不能超过9位数"
            },
            need_fee: {
                required: "请输入需要加钱",
                maxlength: "不能超过9位数"
            },
            comment: {
                maxlength: "不能超过200个字符"
            },
            apply_content: {
                maxlength: "不能超过200个字符"
            },
            start_time: {
                required: "请选择活动开始时间"
            },
            end_time: {
                required: "请选择活动结束时间"
            },
            need_point: {
                required: "请输入需要加积分",
                maxlength: "不能超过9位数"
            },
            activity_goods_type: {
                required: "请输入活动商品"
            }
        },

        highlight: function (element) { // hightlight error inputs
            $(element)
                .closest('.form-group').addClass('has-error'); // set error class to the control group
        },

        unhighlight: function (element) { // revert the change done by hightlight
            $(element)
                .closest('.form-group').removeClass('has-error'); // set error class to the control group
        },

        success: function (label) {
            label
                .closest('.form-group').removeClass('has-error'); // set success class to the control group
        }
    });

    //保存按钮点击事件
    $("#btn_save").click(function () {
        if (!$("#act_master_form").valid()) {
            return;
        }
        if (!checkParams()) {
            showAlert("请输入活动规则.", "提示");
            return;
        }
        save();
    });

    $("#btn_select_template").click(function () {
        $('#customDialog').load('act_master/template_select.html');
    });
    $("#btn_view_template").click(function () {
        if (!tempId || tempId.length == 0) {
            showAlert("请先选择一个模板");
            return;
        }
        $('#customDialog').load('act_master/template_detail.html');
    });

    $("#btn_cancel").click(function () {
        back();
    });

    $("#activity_goods_type").change(function () {
        getGoodsList();
    });
    $("#activity_member_type").change(function () {
        memberList = [];
        refreshActivityMemberList();
    });

    $("#activity_goods_sell_year").change(function () {
        activityDetail.goodsTypeValue = '';
        getSellerSeasons($(this).val());
    });

    $("#has_sub_activity").change(function () {
        subActivityList = [];
        refreshActivityList();
    });

    $("#btn_apply").click(function () {
        applyConfirm(activityId);
    });

    $("#btn_select_goods").click(function () {
        switch ($("#activity_goods_type").val()) {
            case "10":// 全部商品
                break;
            case "20":// 按分类
                // 弹出商品分类列表
                $('#customDialog').load('act_master/goods_type_select.html');
                break;
            case "30":// 按品牌
                $('#customDialog').load('act_master/brand_select.html');
                break;
            case "40":// 按商品
                $('#customDialog').load('act_master/goods_select.html');
                break;
            case "50":// 按SKU
                $('#customDialog').load('act_master/sku_select.html');
                break;
        }
    });

    $("#btn_select_members").click(function () {
        switch ($("#activity_member_type").val()) {
            case "10":// 全部会员
                break;
            case "20":// 按会员组
                $('#customDialog').load('act_master/member_group_select.html');
                break;
            case "30":// 按会员级别
                $('#customDialog').load('act_master/member_level_select.html');
                break;
        }

    });

    $("#btn_select_sub_activity").click(function () {
        switch ($("#has_sub_activity").val()) {
            case "0":// 全部商品
                break;
            case "1":// 按分类
                // 弹出商品分类列表
                $('#customDialog').load('act_master/sub_activity_select.html');
                break;
        }
    });

    if ($("#has_sub_activity").val() == "0") {
        $("#btn_select_sub_activity").hide();
    } else {
        $("#btn_select_sub_activity").show();
    }

    $(".datetime-picker").datetimepicker({
        autoclose: true,
        isRTL: App.isRTL(),
        format: "yyyy-MM-dd hh:ii",
        inputMask: true,
        pickDate: true,
        pickTime: true,
        pickerPosition: (App.isRTL() ? "bottom-right" : "bottom-left")
    });

});

//退回到列表画面
function back() {
    $('#content').load('act_master/list.html');
}

function onTemplateSelected(selectedTempId) {
    // 获取模板详细
    tempId = selectedTempId;
    var url = "/act_template/getDetail.json";
    var param = {"tempId": tempId};
    var success = function (data) {
        if (data.resultCode == '00') {
            $("#temp_name").val(data.data.tempName);
            actionType = data.data.actitionType;
            if ("10" == data.data.actitionType || "11" == data.data.actitionType || "20" == data.data.actitionType || "21" == data.data.actitionType || "30" == data.data.actitionType) {
                // 单品活动
                $("#activity_unit").val("10");
            } else {
                // 订单活动
                $("#activity_unit").val("20");
            }
            showHideByActivityType();
            showHideByActivityUnit();
        } else {
            showAlert('模板数据获取失败');
        }
    };
    var error = function () {
        showAlert('模板数据获取失败');
    };
    axse(url, {"data": JSON.stringify(param)}, success, error);

}

function showHideByActivityType() {
    if ('11' == actionType) {
        $("#div_date_start").hide();
        $("#div_date_end").hide();
        $("#div_datetime_start").show();
        $("#div_datetime_end").show();

        $("#activity_goods_type").val("50");
        $("#activity_goods_type").attr("disabled", true);


        refreshActivityGoodsList();

    } else {

        // $("#activity_goods_type").val("10");
        $("#activity_goods_type").attr("disabled", false);

        $("#div_date_start").show();
        $("#div_date_end").show();
        $("#div_datetime_start").hide();
        $("#div_datetime_end").hide();
    }
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
        $("#div_is_member_activity").show();
    } else if ($("#activity_unit").val() == "20") {
        $("#div_is_origin_price").show();
        // 订单活动
        $("#div_sub_activity_select").show();
        $("#div_need_point").show();
        $("#div_need_fee").show();
        $("#div_is_member_activity").hide();
        if ($("#has_sub_activity").val() == "1") {
            $("#div_selected_activity").show();
            if (editable) {
                $("#btn_select_sub_activity").show();
            } else {
                $("#btn_select_sub_activity").hide();
            }
        } else {
            $("#div_selected_activity").hide();
            $("#btn_select_sub_activity").hide();
        }

    }
}

function onGoodsTypeSelected(goodsTypeId, goodTypeName) {
    if (!goodsList) {
        goodsList = [];
    }
    var hasSelected = false;
    $.each(goodsList, function (index, goods) {
        if (goods.goodsId == goodsTypeId) {
            //已经有了
            hasSelected = true;
        }
    });
    if (!hasSelected) {
        var addGoodsList = [];
        addGoodsList.push({"goodsId": goodsTypeId, "goodsName": goodTypeName});
        addGoods(addGoodsList);
    }
}

function onBrandSelected(selectedBrands) {
    if (!goodsList) {
        goodsList = [];
    }
    var addGoodsList = [];
    $.each(selectedBrands, function (index, brand) {
        var hasSelected = false;
        $.each(goodsList, function (index2, goods) {
            if (goods.goodsId == brand.brandId) {
                //已经有了
                hasSelected = true;
            }
        });
        if (!hasSelected) {
            addGoodsList.push({"goodsId": brand.brandId,
                "goodsName": brand.brandName});
        }
    });
    addGoods(addGoodsList);
}
function onGoodsSelected(selectedGoods) {
    if (!goodsList) {
        goodsList = [];
    }
    var hasSelected = false;
    $.each(goodsList, function (index, goods) {
        if (goods.goodsId == selectedGoods.goodsId) {
            //已经有了
            hasSelected = true;
        }
    });
    if (!hasSelected) {
        var addGoodsList = [];
        addGoodsList.push({
            "goodsId": selectedGoods.goodsId,
            "goodsCode": selectedGoods.goodsCode,
            "goodsName": selectedGoods.goodsName
        });
        addGoods(addGoodsList);
    }
}

function onSkuSelected(selectedSkus) {
    if (!goodsList) {
        goodsList = [];
    }
    var addGoodsList = [];
    $.each(selectedSkus, function (index, sku) {
        var hasSelected = false;
        $.each(goodsList, function (index2, goods) {
            if (goods.skuId == sku.skuid) {
                //已经有了
                hasSelected = true;
            }
        });
        if (!hasSelected) {
            addGoodsList.push({
                "type": sku.type,
                "goodsId": sku.goodsId,
                "goodsName": sku.goodsName,
                "goodsCode": sku.goodsCode,
                "skuId": sku.skuid,
                "colorName": sku.colorName,
                "sizeName": sku.sizeName,
                "skucontent": sku.skucontent,
                "newCount": sku.newCount,
                "price": sku.price
            });
        }
    });
    addGoods(addGoodsList);
}

function onMemberGroupSelected(selectedMember) {
    if (!memberList) {
        memberList = [];
    }
    memberList.push({"memberId": selectedMember.groupId, "memberName": selectedMember.groupName});
    refreshMemberGroupList();
}

function onMemberLevelSelected(selectedMember) {
    if (!memberList) {
        memberList = [];
    }
    memberList.push({"memberId": selectedMember.memberLevelCode, "memberName": selectedMember.memberLevelName});
    refreshMemberLevelList();
}

function onSubActivitySelected(activity) {
    if (!subActivityList) {
        subActivityList = [];
    }
    subActivityList.push({"activityId": activity.activityId, "activityName": activity.activityName});
    refreshSubActivityList();
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
            $("#sell_year_area").hide();
            $("#season_area").hide();
            $("#erp_brand_area").hide();
            $("#line_code_area").hide();


            $("#activity_goods_sell_year").val('');
            $("#activity_goods_season_code").val('');
            $("#activity_goods_erp_brand").val('');
            $("#activity_goods_line_code").val('');

            break;
        case "20":// 按分类
            $("#btn_select_goods").show();
            $("#div_selected_goods_type").show();
            $("#div_selected_goods_brand").hide();
            $("#div_selected_goods").hide();
            $("#div_selected_skus").hide();
            $("#div_selected_sec").hide();
            $("#sell_year_area").hide();
            $("#season_area").hide();
            $("#erp_brand_area").hide();
            $("#line_code_area").hide();

            $("#activity_goods_sell_year").val('');
            $("#activity_goods_season_code").val('');
            $("#activity_goods_erp_brand").val('');
            $("#activity_goods_line_code").val('');

            refreshGoodsTypeList();
            break;
        case "30":// 按品牌
            $("#btn_select_goods").show();
            $("#div_selected_goods_type").hide();
            $("#div_selected_goods_brand").show();
            $("#div_selected_goods").hide();
            $("#div_selected_skus").hide();
            $("#div_selected_sec").hide();
            $("#sell_year_area").hide();
            $("#season_area").hide();
            $("#erp_brand_area").hide();
            $("#line_code_area").hide();

            $("#activity_goods_sell_year").val('');
            $("#activity_goods_season_code").val('');
            $("#activity_goods_erp_brand").val('');
            $("#activity_goods_line_code").val('');

            refreshGoodsBrandList();
            break;
        case "40":// 按商品
            $("#btn_select_goods").show();
            $("#div_selected_goods_type").hide();
            $("#div_selected_goods_brand").hide();
            $("#div_selected_skus").hide();
            $("#div_selected_goods").show();
            $("#div_selected_sec").hide();
            $("#sell_year_area").hide();
            $("#season_area").hide();
            $("#erp_brand_area").hide();
            $("#line_code_area").hide();

            $("#activity_goods_sell_year").val('');
            $("#activity_goods_season_code").val('');
            $("#activity_goods_erp_brand").val('');
            $("#activity_goods_line_code").val('');

            refreshGoodsList();
            break;
        case "50":// 按SKU
        {
            $("#btn_select_goods").show();
            $("#div_selected_goods_type").hide();
            $("#div_selected_goods_brand").hide();
            $("#div_selected_goods").hide();
            if ('11' == actionType) {
                // 秒杀活动配置
                $("#div_selected_skus").hide();
                $("#div_selected_sec").show();
            } else {
                $("#div_selected_skus").show();
                $("#div_selected_sec").hide();
            }
            $("#sell_year_area").hide();
            $("#season_area").hide();
            $("#erp_brand_area").hide();
            $("#line_code_area").hide();

            $("#activity_goods_sell_year").val('');
            $("#activity_goods_season_code").val('');
            $("#activity_goods_erp_brand").val('');
            $("#activity_goods_line_code").val('');

            refreshSkuList();
            break;
        }
        case "60"://年份季节
        {
            $("#btn_select_goods").hide();
            $("#div_selected_goods_type").hide();
            $("#div_selected_goods_brand").hide();
            $("#div_selected_goods").hide();
            $("#div_selected_skus").hide();
            $("#div_selected_sec").hide();
            $("#sell_year_area").show();
            $("#season_area").show();
            $("#erp_brand_area").hide();
            $("#line_code_area").hide();

            $("#activity_goods_erp_brand").val('');
            $("#activity_goods_line_code").val('');

            //年份列表获取
            getSellerYears();


            break;
        }
        case "70"://品牌
        {
            $("#btn_select_goods").hide();
            $("#div_selected_goods_type").hide();
            $("#div_selected_goods_brand").hide();
            $("#div_selected_goods").hide();
            $("#div_selected_skus").hide();
            $("#div_selected_sec").hide();
            $("#sell_year_area").hide();
            $("#season_area").hide();
            $("#erp_brand_area").show();
            $("#line_code_area").hide();

            $("#activity_goods_sell_year").val('');
            $("#activity_goods_season_code").val('');
            $("#activity_goods_line_code").val('');

            //ERP品牌列表获取
            getErpBrands();

            break;
        }
        case "80"://生产线
        {

            $("#btn_select_goods").hide();
            $("#div_selected_goods_type").hide();
            $("#div_selected_goods_brand").hide();
            $("#div_selected_goods").hide();
            $("#div_selected_skus").hide();
            $("#div_selected_sec").hide();
            $("#sell_year_area").hide();
            $("#season_area").hide();
            $("#erp_brand_area").hide();
            $("#line_code_area").show();

            $("#activity_goods_sell_year").val('');
            $("#activity_goods_season_code").val('');
            $("#activity_goods_erp_brand").val('');

            //ERP生产线列表获取
            getErpLineCode();

            break;
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
        if (editable == "0") {
            $('.goods-type-item-delete', $item).hide();
        } else {
            $('.goods-type-item-delete', $item).show();
            $('.goods-type-item-delete', $item).click(function () {
                deleteGoods(goodsType)
            });
        }

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
        if (editable == "0") {
            $('.goods-brand-item-delete', $item).hide();
        } else {
            $('.goods-brand-item-delete', $item).show();
            $('.goods-brand-item-delete', $item).click(function () {
                deleteGoods(goodsBrand)
            });
        }

        $item.removeClass('template').appendTo('#goodsBrandList').show();
    });
}

function refreshGoodsList() {
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
        if (editable == "0") {
            $('.goods-item-delete', $item).hide();
        } else {
            $('.goods-item-delete', $item).show();
            $('.goods-item-delete', $item).click(function () {
                deleteGoods(goods)
            });
        }

        $item.removeClass('template').appendTo('#goodsList').show();
    });
}

function refreshSkuList() {

    if ('11' == actionType) {
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
        if (editable == "0") {
            $('.sku-item-delete', $item).hide();
        } else {
            $('.sku-item-delete', $item).show();
            $('.sku-item-delete', $item).click(function () {
                deleteGoods(sku)
            });
        }

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
        $('.sec-item-max', $item).text(sku.buyMax ? sku.buyMax : "");
        if (editable == "0") {
            $('.sec-item-delete', $item).hide();
            $('.sec-item-edit', $item).hide();

        } else {
            $('.sec-item-edit', $item).show();
            $('.sec-item-edit', $item).click(
                function () {
                    sessionStorage.setItem("index", index);
                    sessionStorage.setItem("goodsName", sku.goodsCode);
                    sessionStorage.setItem("goodsStore", sku.newCount);
                    sessionStorage.setItem("goodsPrice", sku.price);
                    sessionStorage.setItem("goodsActPrice", sku.actPrice);
                    sessionStorage.setItem("goodsQuantity", sku.quantity);
                    sessionStorage.setItem("orderMax", sku.buyMax ? sku.buyMax : "");
                    $('#customDialog').load('act_master/quantity.html');
                }
            );
            $('.sec-item-delete', $item).show();
            $('.sec-item-delete', $item).click(function () {
                deleteGoods(sku)
            });
        }

        $item.removeClass('template').appendTo('#secKillSkuList').show();
    });
}

function resetSecList(item, actPrice, quantity, max) {
    $.each(goodsList, function (index, sku) {
        if (item == index) {
            sku.actPrice = actPrice;
            sku.quantity = quantity;
            sku.buyMax = max;
            editGoods(sku);
            // refreshSecSkuList();
            return;
        }
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

//退回到列表画面
function gotoListPage(){
    $('#content').load('act_master/list.html');
}

function refreshMemberGroupList() {
    // 清空
    $("#memberGroupList").html($(".template.member-group-item"));
    if (!memberList || memberList.length == 0) {
        return;
    }
    $.each(memberList, function (index, member) {
        var $item = $('#memberGroupList .template.member-group-item').clone();
        // 赋值
        $('.member-group-item-index', $item).text(index + 1);
        $('.member-group-item-name', $item).text(member.memberName);
        if (editable == "0") {
            $('.member-group-item-delete', $item).hide();
        } else {
            $('.member-group-item-delete', $item).show();
            $('.member-group-item-delete', $item).click(function () {
                memberList.splice(index, 1);
                refreshMemberGroupList();
            });
        }

        $item.removeClass('template').appendTo('#memberGroupList').show();
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
        if (editable == "0") {
            $('.activity-item-delete', $item).hide();
        } else {
            $('.activity-item-delete', $item).show();
            $('.activity-item-delete', $item).click(function () {
                subActivityList.splice(index, 1);
                refreshSubActivityList();
            });
        }
        $item.removeClass('template').appendTo('#subActivityList').show();
    });
}


function refreshActivityList() {
    switch ($("#has_sub_activity").val()) {
        case "0":// 全部
            $("#btn_select_sub_activity").hide();
            $("#div_selected_activity").hide();
            break;
        case "1":// 会员组
            $("#btn_select_sub_activity").show();
            $("#div_selected_activity").show();
            refreshSubActivityList();
            break;
    }
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
        if (editable == "0") {
            $('.member-level-item-delete', $item).hide();
        } else {
            $('.member-level-item-delete', $item).show();
            $('.member-level-item-delete', $item).click(function () {
                memberList.splice(index, 1);
                refreshMemberLevelList();
            });
        }
        $item.removeClass('template').appendTo('#memberLevelList').show();
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
    if ('11' == actionType) {
        if (data.startTime != null) {
            $("#start_datetime").val(new Date(data.startTime).Format("yyyy-MM-dd hh:mm"));
        } else {
            $("#start_datetime").val("");
        }
        if (data.endTime != null) {
            $("#end_datetime").val(new Date(data.endTime).Format("yyyy-MM-dd hh:mm"));
        } else {
            $("#end_datetime").val("");
        }
    } else {
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
    }

    $("#can_return").val(data.canReturn);
    $("#has_sub_activity").val(data.hasSubActivity);
    $("#can_exchange").val(data.canExchange);
    $("#limit_count").val(data.limitCount);
    $("#comment").val(data.comment);
    $("#apply_user_name").val(data.applyUserName);
    $("#apply_content").val(data.applyContent);
    if ("40" == data.approveStatus) {
        $("#btn_apply").show();
    }
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


function save() {
    var url = null;
    // 编辑
    if (isAdd) {
        url = "/act_master/add.json";
    } else {
        url = "/act_master/edit.json";
    }

    var param = {
        activityId: activityId,
        actitionType: actionType,
        activityName: $("#activity_name").val(),
        tempId: tempId,
        tempName: $("#temp_name").val(),
        goodsType: $("#activity_goods_type").val(),
        memberType: $("#activity_member_type").val(),
        startTimeStr: $("#start_time").val(),
        endTimeStr: $("#end_time").val(),
        startDateTimeStr: $("#start_datetime").val(),
        endDateTimeStr: $("#end_datetime").val(),
        canExchange: $("#can_exchange").val(),
        canReturn: $("#can_return").val(),
        limitCount: $("#limit_count").val(),
        comment: $("#comment").val(),
        applyContent: $("#apply_content").val(),
        unit: $("#activity_unit").val(),
        hasSubActivity: $("#has_sub_activity").val(),
        isOriginPrice: $("#is_origin_price").val(),
        // goodsList: goodsList,
        memberList: memberList,
        subActivityList: subActivityList,
        needPoint: $("#need_point").val(),
        needFee: $("#need_fee").val(),
        isMemberActivity: $("#is_member_activity").val(),
        activityGoodsSellYear:$("#activity_goods_sell_year").val(),
        activityGoodsSeasonCode:$("#activity_goods_season_code").val(),
        activityGoodsErpBrand:$("#activity_goods_erp_brand").val(),
        activityGoodsLineCode:$("#activity_goods_line_code").val()
    };
    var success = function (data) {
        if (data.resultCode == '00') {
            showAlert('提交成功!', '提示', function () {
                back();
            });
        } else {
            showAlert('提交失败,原因:' + data.resultMessage);
        }
    };
    var error = function () {
        showAlert('网络异常.');
    };
    axse(url, {"data": JSON.stringify(param)}, success, error);
}

function applyConfirm(activityId) {
    showConfirm("确定要提交申请吗?", function () {
        applyItem(activityId);
    })
}

function applyItem(activityId) {
    var url = "/act_master/apply.json";
    var param = {
        "activityId": activityId
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

function checkParams() {
    var isValid = true;

    return isValid;
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
                // "40";"商品"
                // "50";"sku"
                //为品牌类型选择框添加数据
                $.each(array, function (index, item) {
                    var $option = $('<option>').attr('value', item.value).text(item.displayCn);
                    $("#activity_goods_type").append($option);
                });
                // 编辑还是新建
                if (!isAdd) {
                    // 赋值
                    $("#activity_goods_type").val(activityDetail.goodsType);
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
                if (!isAdd) {
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
                if (!isAdd) {
                    // 赋值
                    $("#activity_unit").val(activityDetail.unit);
                    subActivityList = activityDetail.subActivityList;
                    showHideByActivityType();
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

/**
 * 获取活动详细
 */
function getActivityDetail() {
    var url = "/act_master/getDetail.json";
    var param = {"activityId": activityId};
    var success = function (data) {
        if (data.resultCode == '00') {
            console.log(data);
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

/**
 * 添加关联商品
 */
function addGoods(addGoodsList) {
    if(!addGoodsList || addGoodsList.length==0){
       return;
    }
    var url = "/act_master/addGoods.json";
    var param = {
        "activityId": activityId,
        "goodsType": $("#activity_goods_type").val(),
        "goodsList": addGoodsList
    };
    var success = function (data) {
        if (data.resultCode == '00') {
            getGoodsList();
        } else {
            showAlert('详细数据获取失败');
        }
    };
    var error = function () {
        showAlert('网络异常');
    };
    axse(url, {"data": JSON.stringify(param)}, success, error);
}

/**
 * 添加关联商品
 */
function editGoods(editGoods) {
    var editGoodsList = [];
    editGoodsList.push(editGoods);
    var url = "/act_master/editGoods.json";
    var param = {
        "activityId": activityId,
        "goodsType": $("#activity_goods_type").val(),
        "goodsList": editGoodsList
    };
    var success = function (data) {
        if (data.resultCode == '00'){
            getGoodsList();
        } else {
            showAlert('详细数据获取失败');
        }
    };
    var error = function () {
        showAlert('网络异常');
    };
    axse(url, {"data": JSON.stringify(param)}, success, error);
}

/**
 * 删除关联商品
 */
function deleteGoods(deleteGoods) {
    showConfirm("确定要删除吗？",function(){
        var deleteGoodsList = [];
        deleteGoodsList.push(deleteGoods);
        var url = "/act_master/deleteGoods.json";
        var param = {
            "activityId": activityId,
            "goodsType": $("#activity_goods_type").val(),
            "goodsList": deleteGoodsList
        };
        var success = function (data) {
            if (data.resultCode == '00') {
                getGoodsList();
            } else {
                showAlert('详细数据获取失败');
            }
        };
        var error = function () {
            showAlert('网络异常');
        };
        axse(url, {"data": JSON.stringify(param)}, success, error);
    });
}

/**
 * 获取关联商品列表
 */
function getGoodsList() {
    var url = "/act_master/getGoodsList.json";
    var param = {
        "activityId": activityId,
        "goodsType": $("#activity_goods_type").val()
    };
    var success = function (data) {
        if (data.resultCode == '00') {
            goodsList = data.result;
            refreshActivityGoodsList();
        } else {
            showAlert('详细数据获取失败');
        }
    };
    var error = function () {
        showAlert('网络异常');
    };
    axse(url, {"data": JSON.stringify(param)}, success, error);
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
            if (!isAdd) {
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
                if (!isAdd) {
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
                if (!isAdd) {
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