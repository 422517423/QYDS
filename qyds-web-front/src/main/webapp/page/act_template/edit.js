var tempId = sessionStorage.getItem("tempId");
var editable = sessionStorage.getItem("editable");
var conditionLable = "";
var conditionUnitLabel = "";
var valueLable = "";
var valueUnitLabel = "";
var maxParamsCount = "";
var skuList = [];
$(document).ready(function () {
    // 审批部分不可编辑
    $("#act_template_approve_form input").attr("disabled", true);
    $("#act_template_approve_form textarea").attr("disabled", true);
    $("textarea").css("resize", "none");
    if (editable == "0") {
        $("#act_template_form input").attr("disabled", true);
        $("#act_template_form textarea").attr("disabled", true);
        $("#act_template_form select").attr("disabled", true);
        $("#btn_add_param").hide();
        $("#btn_save").hide();
    }

    //获取商品类型码表数据
    getActivityOptionCode();

    //表单验证的定义
    $("#act_template_form").validate({
        errorElement: 'span', //default input error message container
        errorClass: 'help-block', // default input error message class
        focusInvalid: false, // do not focus the last invalid input
        rules: {
            actition_type: {
                required: true,
                min: 1
            },
            temp_name: {
                required: true
            }
        },
        messages: {
            actition_type: {
                required: "请选择优惠类型",
                min: "请选择优惠类型"
            },
            temp_name: {
                required: "请输入模板名称"
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
        if (!$("#act_template_form").valid()) {
            return;
        }
        if (!checkParams()) {
            showAlert("请输入活动规则.", "提示");
            return;
        }
        save();
    });

    $("#btn_cancel").click(function () {
        back();
    });

    $("#btn_add_param").click(function () {
        paramList.push({"paramCondition": "", "paramValue": ""});
        refreshParamList();
    });

    $("#actition_type").change(function () {
        resetParamList();
    });

    $("#btn_apply").click(function () {
        applyConfirm(tempId);
    });
});

//退回到列表画面
function back() {
    $('#content').load('act_template/list.html');
}
var paramList = [];
function initParams() {
    if (tempId && tempId.length > 0) {
        refreshParamList();
    } else {
        resetParamList();
    }
}

function resetParamList() {
    paramList = [{"paramCondition": "", "paramValue": ""}];
    skuList = [];
    refreshParamList();
}
var selectedCouponIndex = -1;
var selectedCouponId = null;
var selectedGoodsIndex = -1;
var selectedGoodsId = null;
function refreshParamList() {
    setLableText($("#actition_type").val());
    if (!paramList || paramList.length == 0) {
        paramList = [{"paramCondition": "", "paramValue": ""}];
    }
    // 清空
    $("#paramList").html($(".template.param-item"));

    $.each(paramList, function (index, param) {
        var $item = $('#paramList .template.param-item').clone();
        // 赋值
        $('.condition-input', $item).val(param.paramCondition);
        $('.condition-label', $item).text(conditionLable);
        $('.condition-unit-label', $item).text(conditionUnitLabel);
        $('.value-label', $item).text(valueLable);
        $('.value-unit-label', $item).text(valueUnitLabel);
        // 监听事件
        $('.condition-input', $item).change(function () {
            //进行相关操作
            paramList[index].paramCondition = $(this).val();
            // refreshParamList();
        });
        $('.value-input', $item).val(param.paramValue);
        $('.value-input', $item).change(function () {
            paramList[index].paramValue = $(this).val();
            // refreshParamList();
        });
        $('.remove-param', $item).click(function () {
            paramList.splice(index, 1);
            refreshParamList();
        });

        if ("11" == $("#actition_type").val()) {
            // 秒杀活动规则不显示，价格在具体活动中配置
            $('#div_act_rule').hide();
        } else {
            $('#div_act_rule').show();
        }

        if ("10" == $("#actition_type").val()) {
            // 特价的时候不显示条件
            $('.condition-input', $item).hide();
            $('.condition-label', $item).hide();
            $('.condition-unit-label', $item).hide();
            $('.remove-param', $item).hide();
        } else {
            $('.condition-input', $item).show();
            $('.condition-label', $item).show();
            $('.condition-unit-label', $item).show();
            $('.remove-param', $item).show();
        }
        $('.value-detail', $item).hide();
        $('.value-select-goods', $item).hide();

        if ("40" == $("#actition_type").val()) {
            // 满送优惠券,要选择和查看优惠劵
            $('.value-input', $item).click(function () {
                $('#customDialog').load('act_template/coupon_select.html');
                selectedCouponIndex = index;
            });
            $('.value-detail', $item).click(function (e) {
                e.preventDefault();
                // 查看优惠劵详细
                $('#customDialog').load('act_template/coupon_detail.html');
                selectedCouponId = param.paramValue;
            });
            $('.value-input', $item).attr("readonly", "readonly");
            $('.value-input', $item).attr("placeholder", "点击选择");
            $('.value-detail', $item).show();
        }
        if ("42" == $("#actition_type").val()) {
            $('.value-select-goods', $item).show();
            // 满送货品
            $('.value-select-goods', $item).click(function () {
                $('#customDialog').load('act_template/sku_color_select.html');
                selectedGoodsIndex = index;
            });
            $('.value-input', $item).hide();

        }
        // 第一条不能删除
        if (index == 0) {
            $('.remove-param', $item).hide();
        }
        // 不可编辑
        if (editable == "0") {
            $('.remove-param', $item).hide();
            $('.value-select-goods', $item).hide();
        }
        $item.removeClass('template').appendTo('#paramList').show();
    });
    if (paramList.length >= maxParamsCount) {
        $("#btn_add_param").hide();
    } else {
        $("#btn_add_param").show();
    }
}

function onCouponSelected(selectedCoupon) {
    paramList[selectedCouponIndex].paramValue = selectedCoupon.couponId;
    refreshParamList();
}

function onSkuColorSelected(selectedSkus) {
    if (!skuList) {
        skuList = [];
    }
    $.each(selectedSkus, function (index, sku) {
        var hasSelected = false;
        $.each(skuList, function (index2, goods) {
            if (goods.goodsCode + "_" + goods.colorCode == sku.goodsCode + "_" + sku.colorCode) {
                //已经有了
                hasSelected = true;
            }
        });
        if (!hasSelected) {
            skuList.push({
                "type": sku.type,
                "goodsId": sku.goodsId,
                "goodsName": sku.goodsName,
                "goodsCode": sku.goodsCode,
                "skuId": sku.skuid,
                "colorName": sku.colorName,
                "colorCode": sku.colorCode,
                "sizeName": sku.sizeName,
                "skucontent": sku.skucontent
            });
        }
    });
    refreshSkuList();
}

function refreshSkuList() {
    // 清空
    $("#skuList").html($(".template.sku-item"));
    if (!skuList || skuList.length == 0) {
        return;
    }
    var goodsIds = [];
    $.each(skuList, function (index, sku) {
        goodsIds.push({"goodsCode": sku.goodsCode, "colorCode": sku.colorCode});
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
        if (editable == "0") {
            $('.sku-item-delete', $item).hide();
        } else {
            $('.sku-item-delete', $item).show();
            $('.sku-item-delete', $item).click(function () {
                skuList.splice(index, 1);
                refreshSkuList();
            });
        }
        $item.removeClass('template').appendTo('#skuList').show();
    });
    if (selectedGoodsIndex != -1) {
        paramList[selectedGoodsIndex].paramValue = JSON.stringify(goodsIds);
        refreshParamList();
    }
}

function setLableText(acttype) {

    switch (acttype) {
        case "10": // "10";"特价"
            conditionLable = "";
            conditionUnitLabel = "";
            valueLable = "特价:";
            valueUnitLabel = "元。";
            // 特价只能有一个参数规则
            maxParamsCount = 1;
            $("#div_selected_skus").hide();
            break;
        case "11": // "11";"秒杀"
            conditionLable = "";
            conditionUnitLabel = "";
            valueLable = "";
            valueUnitLabel = "";
            // 特价只能有一个参数规则
            maxParamsCount = 1;
            $("#div_selected_skus").hide();
            break;
        case "20": // "20";"折扣"
            conditionLable = "满";
            conditionUnitLabel = "件，";
            valueLable = "折扣为:";
            valueUnitLabel = "折。";
            $("#div_selected_skus").hide();
            maxParamsCount = 3;
            break;
        case "21": // "21";"折扣"
            conditionLable = "第";
            conditionUnitLabel = "件，";
            valueLable = "折扣为:";
            valueUnitLabel = "折。";
            $("#div_selected_skus").hide();
            maxParamsCount = 2;
            break;
        case "30": // "30";"积分换购"
            conditionLable = "用";
            conditionUnitLabel = "积分，";
            valueLable = "加";
            valueUnitLabel = "元。";
            // 积分换购只能有一个参数规则
            $("#div_selected_skus").hide();
            maxParamsCount = 1;
            break;
        case "40":// "40";"满送"
            conditionLable = "满";
            conditionUnitLabel = "元，";
            valueLable = "送:";
            valueUnitLabel = "优惠券。";
            $("#div_selected_skus").hide();
            maxParamsCount = 1;
            break;
        case "41": // "41";"满减"
            conditionLable = "满";
            conditionUnitLabel = "元，";
            valueLable = "立减:";
            valueUnitLabel = "元。";
            $("#div_selected_skus").hide();
            maxParamsCount = 1;
            break;
        case "42": // "42";"满送"
            conditionLable = "满";
            conditionUnitLabel = "元，";
            valueLable = "送货品(消费者任选一件):";
            valueUnitLabel = "";
            $("#div_selected_skus").show();
            maxParamsCount = 1;
            break;
        case "43": // "43";"满送积分"
            conditionLable = "满";
            conditionUnitLabel = "元，";
            valueLable = "送:";
            valueUnitLabel = "积分";
            $("#div_selected_skus").hide();
            maxParamsCount = 1;
            break;
        case "44": // "44";"满折"
            conditionLable = "满";
            conditionUnitLabel = "元，";
            valueLable = "全单打:";
            valueUnitLabel = "折";
            $("#div_selected_skus").hide();
            maxParamsCount = 1;
            break;
    }
    if (editable == "0") {
        $("#btn_add_param").hide();
    }
}

//获得码表数据方法
function getActivityOptionCode() {
    var url = "/common/getCodeList.json";
    var param = {"data": "ACTITION_TYPE"};
    var success = function (data) {
        if (data.resultCode == '00') {
            var array = data.data;
            if (array != null) {
                //为品牌类型选择框添加数据
                $.each(array, function (index, item) {
                    var $option = $('<option>').attr('value', item.value).text(item.displayCn);
                    $("#actition_type").append($option);
                });
                // 编辑还是新建
                if (tempId && tempId.length > 0) {
                    // 编辑或查看
                    getTempDetail();
                } else {
                    // 新建
                    resetParamList();
                }
                setLableText($("#actition_type").val());
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


function getTempDetail() {
    var url = "/act_template/getDetail.json";
    var param = {"tempId": tempId};
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

function initForm(data) {
    skuList = data.skuList;
    $("#actition_type").val(data.actitionType);
    $("#temp_name").val(data.tempName);
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
    paramList = data.paramList;
    refreshParamList();
    refreshSkuList();
}


function save() {
    var url = null;
    // 编辑
    if (tempId && tempId.length > 0) {
        url = "/act_template/edit.json";
    } else {
        tempId = null;
        url = "/act_template/add.json";
    }

    var param = {
        tempId: tempId,
        tempName: $("#temp_name").val(),
        actitionType: $("#actition_type").val(),
        comment: $("#comment").val(),
        applyContent: $("#apply_content").val(),
        paramList: paramList
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
function applyConfirm(tempId) {
    showConfirm("确定要提交申请吗?", function () {
        applyItem(tempId);
    })
}

function applyItem(tempId) {
    var url = "/act_template/apply.json";
    var param = {
        "tempId": tempId
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

//退回到列表画面
function gotoListPage(){
    $('#content').load('act_template/list.html');
}

function checkParams() {
    var isValid = true;
    if('11' == $("#actition_type").val()){
        // "11";"秒杀":不需要条件
        return isValid;
    }
    $.each(paramList, function (index, param) {
        switch ($("#actition_type").val()) {
            case "10": // "10";"特价"
                // 不需要条件
                if (param.paramValue == "") {
                    isValid = false;
                    // 跳出循环
                    return false;
                }
                break;
            case "20": // "20";"折扣"
            case "30": // "30";"积分换购"
            case "31": // "31";"代金券"
            case "40": // "40";"满送"
            case "41": // "41";"满减"
            case "42": // "42";"满送货品"
            case "43": // "43";"满送积分"
            case "44": // "44";"满折"
                if (param.paramCondition == "") {
                    isValid = false;
                    // 跳出循环
                    return false;
                }
                if (param.paramValue == "") {
                    isValid = false;
                    // 跳出循环
                    return false;
                }
                break;
        }
    });
    return isValid;
}