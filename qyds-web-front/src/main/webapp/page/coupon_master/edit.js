var couponId = sessionStorage.getItem("couponId");
var editable = sessionStorage.getItem("editable");
var isAdd = false;
var couponDetail = null;
var couponImage = null;
var goodsList = [];
$(document).ready(function () {
    // 审批部分不可编辑
    $("#coupon_master_approve_form input").attr("disabled", true);
    $("#coupon_master_approve_form textarea").attr("disabled", true);
    $("textarea").css("resize", "none");
    if (editable == "0") {
        $("#coupon_master_form input").attr("disabled", true);
        $("#coupon_master_form textarea").attr("disabled", true);
        $("#coupon_master_form select").attr("disabled", true);
        $("#btn_add_param").hide();
        $("#btn_save").hide();
    } else {
        $('.date-picker').datepicker({
            autoclose: true,
            startDate: new Date()
        });
    }

    if (couponId && couponId.length > 0) {
        getDetail();
    } else {
        isAdd = true;
        couponId = getUUID();
        //获取商品类型码表数据
        getCouponTypeOptionCode();
        getDistributeTypeOptionCode();
        getCouponScopeOptionCode();
        getPriceLimitOptionCode();
        getGoodsTypeOptionCode();
        getMemberLevelOptionCode();
    }
    $("#goods_type").change(function () {
        getGoodsList();
    });

    $("#coupon_type").change(function () {
        showHideByCouponType();
    });

    $("#distribute_type").change(function () {
        showHideForm();
    });

    $("#activity_goods_sell_year").change(function () {
        couponDetail.goodsTypeValue = '';
        getSellerSeasons($(this).val());
    });

    $("input[name=coupon_style]").change(function () {
        showHideByCouponStyle();
    });

    $("#btn_select_goods").click(function () {
        switch ($("#goods_type").val()) {
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

    $('#coupon_image').click(function () {
        $("#coupon_image_input").click();
    });

    $('#coupon_image_input').change(function (e) {
        processImage(this, $('#coupon_image'));
    });

    //表单验证的定义
    $("#coupon_master_form").validate({
        errorElement: 'span', //default input error message container
        errorClass: 'help-block', // default input error message class
        focusInvalid: false, // do not focus the last invalid input
        errorPlacement: function (error, element) {
            if ($(element).attr('name') == 'start_time'
                || $(element).attr('name') == 'end_time'
                || $(element).attr('name') == 'send_start_time'
                || $(element).attr('name') == 'end_time') {
                $(element).parent().after(error)
            } else {
                $(element).after(error)
            }
        },
        rules: {
            coupon_type: {
                required: true
            },
            coupon_name: {
                required: true
            },
            worth: {
                required: true
            },
            start_time: {
                required: true
            },
            end_time: {
                required: true
            },
            send_start_time: {
                required: true
            },
            send_end_time: {
                required: true
            },
            valid_days: {
                required: true
            },
            discount: {
                required: true
            },
            exchange_point: {
                required: true
            },
            per_max_count: {
                required: true
            },
            price: {
                required: true
            },

        },
        messages: {
            coupon_type: {
                required: "请选择优惠券类型"
            },
            coupon_name: {
                required: "请输入优惠券名称",
                maxlength: "不能超30个字符"
            },
            worth: {
                required: "请输入优惠券金额",
                maxlength: "不能超9位数"
            },
            exchange_point: {
                maxlength: "不能超9位数"
            },
            max_count: {
                maxlength: "不能超9位数"
            },
            min_order_price: {
                maxlength: "不能超9位数"
            },
            min_goods_count: {
                maxlength: "不能超9位数"
            },
            start_time: {
                required: "请选择生效时间"
            },
            end_time: {
                required: "请选择失效时间"
            },
            send_start_time: {
                required: "请选择开始发放时间"
            },
            send_end_time: {
                required: "请选择结束发放时间"
            },
            valid_days: {
                required: "请输入限制使用天数"
            },
            discount: {
                required: "请输入折扣值"
            },
            exchange_point: {
                required: "请输入兑换所需积分"
            },
            per_max_count: {
                required: "请输入每人最大领取数量"
            },
            price: {
                required: "请输入购买价"
            },
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
        if (!$("#coupon_master_form").valid()) {
            return;
        }
        save();
    });

    $("#btn_cancel").click(function () {
        back();
    });

    $("#btn_apply").click(function () {
        applyConfirm(couponId);
    });

    $('input[name="validDate"]').change(function () {
        showHideByValidDate();
    });
    showHideByCouponStyle();
    showHideByValidDate();
});

//退回到列表画面
function back() {
    $('#content').load('coupon_master/list.html');
}

function showHideByValidDate(){
    if ($("input[name='validDate']:checked").val() == "10") {
        $("#div_start_time").show();
        $("#div_end_time").show();
        $("#div_valid_days").hide();
    } else {
        $("#div_start_time").hide();
        $("#div_end_time").hide();
        $("#div_valid_days").show();
    }
    $("#start_time").val("");
    $("#end_time").val("");
    $("#valid_days").val("");
}

function showHideByCouponType() {
    if ("20" == $("#coupon_type").val() || "30" == $("#coupon_type").val()) {
        $("#distribute_type").val("40");
        $("#distribute_type").attr("disabled", true);
    } else {
        $("#distribute_type").val("10");
        $("#distribute_type").removeAttr("disabled");
    }
    // 生日劵或积分兑换的劵才会要求选择会员级别
    if (("10" == $("#coupon_type").val() && "10" == $("#distribute_type").val()) || "20" == $("#coupon_type").val() || "50" == $("#distribute_type").val()) {
        $("#div_member_level").show();
    } else {
        $("#div_member_level").hide();
    }
    
    if ("40" == $("#coupon_type").val()) {
        $("#couponDiscount").hide();
    }else {
        $("#couponDiscount").show();
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

function processImage(fileInput, image, count) {
    var filepath = $(fileInput).val();
    var extStart = filepath.lastIndexOf(".");
    var ext = filepath.substring(extStart + 1, filepath.length).toUpperCase();
    var fileName = getFileName(filepath);
    if (ext != "JPG" && ext != "JPEG" && ext != "PNG") {
        showAlert("请选择符合要求格式的图片上传！", "提示");
        return;
    }
    // Get a reference to the fileList
    var files = !!fileInput.files ? fileInput.files : [];
    // If no files were selected, or no FileReader support,
    // return
    if (!files.length || !window.FileReader) {
        showAlert("读取图片失败！", "提示");
        return;
    }
    // Only proceed if the selected file is an image
    if (/^image/.test(files[0].type)) {
        // Create a new instance of the FileReader
        var reader = new FileReader();
        // Read the local file as a DataURL
        reader.readAsDataURL(files[0]);
        reader.onloadend = function () {
            uploadImage(image, this.result, fileName, ext, count);
        }
    } else {
        showAlert("请选择图片文件！", "提示");
        return;
    }
}

//退回到列表画面
function gotoListPage(){
    $('#content').load('coupon_master/list.html');
}

//图片上传网络请求方法
function uploadImage(img, data, fileName, suffix, count) {
    var url = uploadUri;
    var param = {
        type: "COUPON_MASTER",
        file: data,
        fileName: fileName,
        suffix: suffix
    };

    var success = function (data) {
        if (data.resultCode == '00') {
            img.attr("src", displayUri + orignal + data.data);
            couponImage = data.url;
        } else {
            showAlert('图片上传失败');
        }
    };
    var error = function () {
        showAlert('图片上传失败');
    };
    axseForImage(null, param, success, error);
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
                showHideByCouponType();
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

function showHideForm() {
    if ($("#distribute_type").val() == "50") {
        $("#div_exchange_point").show();
    } else {
        $("#div_exchange_point").hide();
    }

    if (("10" == $("#coupon_type").val() && "10" == $("#distribute_type").val()) || "20" == $("#coupon_type").val() || "50" == $("#distribute_type").val()) {
        $("#div_member_level").show();
    } else {
        $("#div_member_level").hide();
    }

    if ($("#distribute_type").val() == "60") {
        $("#div_price").show();
    } else {
        $("#div_price").hide();
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
                //为会员级别选择框添加数据
                var $option1 = $('<option>').attr('value', '00').text("不限级别");
                $("#member_level").append($option1);
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

function getDetail() {
    var url = "/coupon_master/getDetail.json";
    var param = {"couponId": couponId};
    var success = function (data) {
        if (data.resultCode == '00') {
            initForm(data.data);
            getCouponTypeOptionCode();
            getDistributeTypeOptionCode();
            getCouponScopeOptionCode();
            getPriceLimitOptionCode();
            getGoodsTypeOptionCode();
            getMemberLevelOptionCode();
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
    $("input[name='coupon_style'][value=" + data.couponStyle + "]").attr("checked", true);
    $("#discount").val(data.discount);
    $("#exchange_point").val(data.exchangePoint);
    $("#per_max_count").val(data.perMaxCount);
    $("#price").val(data.price);
    $("#is_valid").val(data.isValid);
    showHideByCouponStyle();
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

function refreshActivityGoodsList() {
    switch ($("#goods_type").val()) {
        case "10":// 全部商品
            $("#btn_select_goods").hide();
            $("#div_selected_goods_type").hide();
            $("#div_selected_goods_brand").hide();
            $("#div_selected_goods").hide();
            $("#div_selected_skus").hide();
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
            $("#btn_select_goods").show();
            $("#div_selected_goods_type").hide();
            $("#div_selected_goods_brand").hide();
            $("#div_selected_goods").hide();
            $("#div_selected_skus").show();

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
        //$('.sku-item-name', $item).text(sku.skuId);
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

function save() {
    var url = null;
    // 编辑
    if (!isAdd) {
        url = "/coupon_master/edit.json";
    } else {
        url = "/coupon_master/add.json";
    }

    var param = {
        couponId: couponId,
        couponType: $("#coupon_type").val(),
        distributeType: $("#distribute_type").val(),
        couponName: $("#coupon_name").val(),
        couponCode: $("#coupon_code").val(),
        couponScope: $("#coupon_scope").val(),
        goodsType: $("#goods_type").val(),
        isOriginPrice: $("#is_origin_price").val(),
        worth: $("#worth").val(),
        maxCount: $("#max_count").val(),
        minOrderPrice: $("#min_order_price").val(),
        minGoodsCount: $("#min_goods_count").val(),
        startTimeStr: $("#start_time").val(),
        endTimeStr: $("#end_time").val(),
        sendStartTimeStr: $("#send_start_time").val(),
        sendEndTimeStr: $("#send_end_time").val(),
        comment: $("#comment").val(),
        applyContent: $("#apply_content").val(),
        exchangePoint: $("#exchange_point").val(),
        couponImage: couponImage,
        // goodsList: goodsList,
        couponStyle: $("input[name=coupon_style]:checked").val(),
        discount: $("#discount").val(),
        memberLevel: $("#member_level").val(),
        validDays: $("#valid_days").val(),
        perMaxCount: $("#per_max_count").val(),
        price: $("#price").val(),
        isValid: '0',
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

function applyConfirm(couponId) {
    showConfirm("确定要提交申请吗?", function () {
        applyItem(couponId);
    })
}

function applyItem(couponId) {
    var url = "/coupon_master/apply.json";
    var param = {
        "couponId": couponId
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


/**
 * 添加关联商品
 */
function addGoods(addGoodsList) {
    if(!addGoodsList || addGoodsList.length==0){
        return;
    }
    var url = "/coupon_master/addGoods.json";
    var param = {
        "couponId": couponId,
        "goodsType": $("#goods_type").val(),
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
    var url = "/coupon_master/editGoods.json";
    var param = {
        "couponId": couponId,
        "goodsType": $("#goods_type").val(),
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
        var url = "/coupon_master/deleteGoods.json";
        var param = {
            "couponId": couponId,
            "goodsType": $("#goods_type").val(),
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
    var url = "/coupon_master/getGoodsList.json";
    var param = {
        "couponId": couponId,
        "goodsType": $("#goods_type").val()
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
            if (!isAdd) {
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
                if (!isAdd) {
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
                if (!isAdd) {
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