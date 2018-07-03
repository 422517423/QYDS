var tempId = sessionStorage.getItem("tempId");
var conditionLable = "";
var conditionUnitLabel = "";
var valueLable = "";
var valueUnitLabel = "";
var skuList  =[];
$(document).ready(function () {
    $("#act_template_form input").attr("disabled", true);
    $("#act_template_form textarea").attr("disabled", true);
    $("#act_template_form select").attr("disabled", true);
    $("textarea").css("resize","none");
    //获取商品类型码表数据
    getActivityOptionCode();
    $("#btn_cancel").click(function () {
        back();
    });

    $("#btn_approve").click(function () {
        approveConfirm(tempId);
    });

    $("#btn_reject").click(function () {
        rejectConfirm(tempId);
    });

});

//退回到列表画面
function back() {
    $('#content').load('act_template/approve_list.html');
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
                // 编辑或查看
                getTempDetail();
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
            // 赋值
            initForm(data.data);
        } else {
            showAlert('数据获取失败');
        }
    };
    var error = function () {
        showAlert('数据获取失败');
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
    // "10";"审批中";"20";"审批通过";"30";"审批驳回";"40";"未申请"
    if (data.approveStatus == "10") {
        $("#btn_approve").show();
        $("#btn_reject").show();
        $("#approve_content").removeAttr("disabled");
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
var selectedCouponId = null;
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
        $('.value-input', $item).val(param.paramValue);
        $('.value-detail', $item).hide();
        if("40" == $("#actition_type").val()){
            $('.value-detail', $item).show();
            $('.value-detail', $item).click(function (e) {
                e.preventDefault();
                // 查看优惠劵详细
                $('#customDialog').load('act_template/coupon_detail.html');
                selectedCouponId = param.paramValue;
            });

        }
        if ("42" == $("#actition_type").val()) {
            $('.value-input', $item).hide();
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

        if("11" == $("#actition_type").val()){
            // 秒杀不显示规则
            $("#div_act_rule").hide();
        } else {
            $("#div_act_rule").show();
        }

        $item.removeClass('template').appendTo('#paramList').show();
    });
}

function setLableText(acttype) {

    switch (acttype) {
        case "10": // "10";"特价"
            conditionLable = "";
            conditionUnitLabel = "";
            valueLable = "特价:";
            valueUnitLabel = "元。";
            $("#div_selected_skus").hide();
            break;
        case "20": // "20";"折扣"
            conditionLable = "满";
            conditionUnitLabel = "件，";
            valueLable = "折扣为:";
            valueUnitLabel = "折。";
            $("#div_selected_skus").hide();
            break;
        case "30": // "30";"积分换购"
            conditionLable = "用";
            conditionUnitLabel = "积分，";
            valueLable = "加";
            valueUnitLabel = "元。";
            $("#div_selected_skus").hide();
            break;
        case "40":// "40";"满送"
            conditionLable = "满";
            conditionUnitLabel = "元，";
            valueLable = "送:";
            valueUnitLabel = "优惠券。";
            $("#div_selected_skus").hide();
            break;
        case "41": // "41";"满减"
            conditionLable = "满";
            conditionUnitLabel = "元，";
            valueLable = "立减:";
            valueUnitLabel = "元。";
            $("#div_selected_skus").hide();
            break;
        case "42": // "42";"满送"
            conditionLable = "满";
            conditionUnitLabel = "元，";
            valueLable = "送货品(消费者任选一件):";
            valueUnitLabel = "";
            $("#div_selected_skus").show();
            break;
    }
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
        $item.removeClass('template').appendTo('#skuList').show();
    });
}

function approveConfirm(tempId) {
    showConfirm("确定要通过吗?", function () {
        approveItem(tempId);
    });
}

function approveItem(tempId) {
    var url = "/act_template/approve.json";
    var param = {
        "tempId": tempId,
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

function rejectConfirm(tempId) {
    showConfirm("确定要驳回吗?", function () {
        rejectItem(tempId);
    })
}

//退回到列表画面
function gotoListPage(){
    $('#content').load('act_template/approve_list.html');
}

function rejectItem(tempId) {
    var url = "/act_template/reject.json";
    var param = {
        "tempId": tempId,
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