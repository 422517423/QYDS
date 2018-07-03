var prizeDrawId = sessionStorage.getItem("prizeDrawId");
var prizeGoodsList = [];
var selectedPrizeGoods = null;
$(document).ready(function () {
    if (prizeDrawId && prizeDrawId.length > 0) {
        //获取详细
        getDetail();
    } else {
        // 新建 生成一个id
        prizeDrawId = getUUID();
    }
    $('.date-picker').datepicker({
        autoclose: true,
        startDate: new Date()
    });
    //表单验证的定义
    $("#prize_draw_form").validate({
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
            prize_draw_name: {
                required: true,
                maxlength: 50
            },
            start_time: {
                required: true
            },
            end_time: {
                required: true
            }
        },
        messages: {
            prize_draw_name: {
                required: "请输入抽奖名称",
                maxlength: "不能超过50个字符"
            },
            start_time: {
                required: "请选择抽奖活动开始时间"
            },
            end_time: {
                required: "请选择抽奖结束时间"
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
        if (!$("#prize_draw_form").valid()) {
            return;
        }
        if (!prizeGoodsList || prizeGoodsList.length == 0) {
            showAlert("请添加至少一个奖品.", "提示");
            return;
        }
        save();
    });

    $("#btn_add_prize_goods").click(function () {
        selectedPrizeGoods = null;
        $('#customDialog').load('prize_draw/prize_goods_edit.html');
    });

    $("#btn_cancel").click(function () {
        back();
    });
});

//退回到列表画面
function back() {
    $('#content').load('prize_draw/list.html');
}

function refreshPrizeGoodsList() {
    // 清空
    $("#prizeGoodsList").html($(".template.prize-goods-item"));
    if (!prizeGoodsList || prizeGoodsList.length == 0) {
        return;
    }
    $.each(prizeGoodsList, function (index, prizeGoods) {
        var $item = $('#prizeGoodsList .template.prize-goods-item').clone();
        // 赋值

        $('.prize-goods-item-sort', $item).text(prizeGoods.sort);
        // $('.prize-goods-item-image', $item).attr("src",prizeGoods.prizeGoodsImage);
        $('.prize-goods-item-name', $item).text(prizeGoods.prizeGoodsName);
        $('.prize-goods-item-count', $item).text(prizeGoods.prizeGoodsCount);
        $('.prize-goods-item-win-percent', $item).text(prizeGoods.winPercent);
        $('.prize-goods-item-desc', $item).text(prizeGoods.prizeGoodsDesc);
        $('.prize-goods-item-delete', $item).click(function () {
            deletePrizeGoods(prizeGoods.prizeGoodsId);
        });
        $('.prize-goods-item-edit', $item).click(function () {
            selectedPrizeGoods = prizeGoods;
            $('#customDialog').load('prize_draw/prize_goods_edit.html');
        });

        $item.removeClass('template').appendTo('#prizeGoodsList').show();
    });
}

function initForm(data) {
    $("#prize_draw_name").val(data.prizeDrawName);
    $("#can_repeat_win").val(data.canRepeatWin);
    $("#is_valid").val(data.isValid);
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
}


function save() {
    var url = "/prize_draw/edit.json";
    var param = {
        prizeDrawId: prizeDrawId,
        prizeDrawName: $("#prize_draw_name").val(),
        startTimeStr: $("#start_time").val(),
        endTimeStr: $("#end_time").val(),
        canRepeatWin: $("#can_repeat_win").val(),
        isValid: $("#is_valid").val(),
        comment: $("#comment").val()
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

function getDetail() {
    var url = "/prize_draw/getDetail.json";
    var param = {"prizeDrawId": prizeDrawId};
    var success = function (data) {
        if (data.resultCode == '00') {
            initForm(data.result);
            getPrizeGoodsList();
        } else {
            showAlert('详细数据获取失败');
        }
    };
    var error = function () {
        showAlert('网络异常');
    };
    axse(url, {"data": JSON.stringify(param)}, success, error);
}

function getPrizeGoodsList() {
    var url = "/prize_draw/getPrizeGoodsList.json";
    var param = {"prizeDrawId": prizeDrawId};
    var success = function (data) {
        if (data.resultCode == '00') {
            prizeGoodsList = data.result;
            refreshPrizeGoodsList();
        } else {
            showAlert('详细数据获取失败');
        }
    };
    var error = function () {
        showAlert('网络异常');
    };
    axse(url, {"data": JSON.stringify(param)}, success, error);
}

function deletePrizeGoods(prizeGoodsId) {
    var url = "/prize_draw/deletePrizeGoods.json";
    var param = {"prizeGoodsId": prizeGoodsId};
    var success = function (data) {
        if (data.resultCode == '00') {
            getPrizeGoodsList();
        } else {
            showAlert('删除失败');
        }
    };
    var error = function () {
        showAlert('网络异常');
    };
    axse(url, {"data": JSON.stringify(param)}, success, error);
}