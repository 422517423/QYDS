var couponId = selectedCouponId;
var couponDetail = null;
var couponImage = null;
$(document).ready(function () {
    $("#coupon_detail_dialog").modal('show');
    $("textarea").css("resize", "none");
    $("#coupon_master_form input").attr("disabled", true);
    $("#coupon_master_form textarea").attr("disabled", true);
    $("#coupon_master_form select").attr("disabled", true);
    getDetail();
});


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
                $("#coupon_type").val(couponDetail.couponType);
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
                $("#distribute_type").val(couponDetail.distributeType);
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

function getDetail() {
    var url = "/coupon_master/getDetail.json";
    var param = {"couponId": couponId};
    var success = function (data) {
        if (data.resultCode == '00') {
            initForm(data.data);
            getCouponTypeOptionCode();
            getDistributeTypeOptionCode();
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
    couponDetail = data;
    $("#coupon_type").val(data.couponType);
    $("#coupon_name").val(data.couponName);
    $("#worth").val(data.worth);
    $("#coupon_code").val(data.couponCode);
    $("#max_count").val(data.maxCount);
    $("#min_order_price").val(data.minOrderPrice);
    $("#coupon_image").attr("src", displayUri + orignal + data.couponImage);
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
}