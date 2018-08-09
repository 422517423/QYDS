var conditionLable = "";
var conditionUnitLabel = "";
var valueLable = "";
var valueUnitLabel = "";
$(document).ready(function () {
    $("#template_detail_dialog").modal('show');

    $("#temp_detail_act_template_form input").attr("disabled", true);
    $("#temp_detail_act_template_form textarea").attr("disabled", true);
    $("#temp_detail_act_template_form select").attr("disabled", true);
    $("textarea").css("resize","none");
    //获取商品类型码表数据
    getActivityOptionCode();
    $("#temp_detail_btn_cancel").click(function () {
        back();
    });
});

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
                    $("#temp_detail_actition_type").append($option);
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
    $("#temp_detail_actition_type").val(data.actitionType);
    $("#temp_detail_temp_name").val(data.tempName);
    $("#temp_detail_comment").val(data.comment);
    paramList = data.paramList;
    refreshParamList();
}
var selectedCouponId = null;
function refreshParamList() {
    setLableText($("#temp_detail_actition_type").val());
    if (!paramList || paramList.length == 0) {
        paramList = [{"paramCondition": "", "paramValue": ""}];
    }
    // 清空
    $("#temp_detail_paramList").html($(".template.param-item"));

    $.each(paramList, function (index, param) {
        var $item = $('#temp_detail_paramList .template.param-item').clone();
        // 赋值
        $('.condition-input', $item).val(param.paramCondition);
        $('.condition-label', $item).text(conditionLable);
        $('.condition-unit-label', $item).text(conditionUnitLabel);
        $('.value-label', $item).text(valueLable);
        $('.value-unit-label', $item).text(valueUnitLabel);
        $('.value-input', $item).val(param.paramValue);

        if("40" == $("#temp_detail_actition_type").val()){
            $('.value-detail', $item).click(function (e) {
                e.preventDefault();
                // 查看优惠劵详细
                $('#customDialog').load('act_template/coupon_detail.html');
                selectedCouponId = param.paramValue;
            });

        }else{
            $('.value-detail', $item).hide();
        }

        if ("10" == $("#temp_detail_actition_type").val()) {
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

        if("11" == $("#temp_detail_actition_type").val()){
            // 秒杀的时候不显示规则
            $("#div_act_rule").hide();
        } else {
            $("#div_act_rule").show();
        }

        $item.removeClass('template').appendTo('#temp_detail_paramList').show();
    });
}

function setLableText(acttype) {

    switch (acttype) {
        case "10": // "10";"特价"
            conditionLable = "";
            conditionUnitLabel = "";
            valueLable = "特价:";
            valueUnitLabel = "元。";
            break;
        case "20": // "20";"折扣"
            conditionLable = "满";
            conditionUnitLabel = "件，";
            valueLable = "折扣为:";
            valueUnitLabel = "折。";
            break;
        case "30": // "30";"积分换购"
            conditionLable = "用";
            conditionUnitLabel = "积分，";
            valueLable = "加";
            valueUnitLabel = "元。";
            break;
        case "40":// "40";"满送"
            conditionLable = "满";
            conditionUnitLabel = "元，";
            valueLable = "送:";
            valueUnitLabel = "优惠券。";
            break;
        case "41": // "41";"满减"
            conditionLable = "满";
            conditionUnitLabel = "元，";
            valueLable = "立减:";
            valueUnitLabel = "元。";
            break;
        case "45": // "45";"每满减"
            conditionLable = "每满";
            conditionUnitLabel = "元，";
            valueLable = "立减:";
            valueUnitLabel = "元。";
            break;
        case "42": // "42";"满送"
            conditionLable = "满";
            conditionUnitLabel = "元，";
            valueLable = "送货品:";
            valueUnitLabel = "";
            break;
    }
}