/**
 * Created by zlh on 16/11/9.
 */

var index = sessionStorage.getItem("index");
var goodsName = sessionStorage.getItem("goodsName");
var goodsStore = sessionStorage.getItem("goodsStore");
var goodsPrice = sessionStorage.getItem("goodsPrice");
var goodsActPrice = sessionStorage.getItem("goodsActPrice");
var goodsQuantity = sessionStorage.getItem("goodsQuantity");
var orderMax = sessionStorage.getItem("orderMax");

$(document).ready(function () {
    $("#changeSecGoodsQuantity").modal('show');
    $("#goodsName").val(goodsName);
    $("#goodsPrice").val(goodsPrice);
    $("#goodsActPrice").val(goodsActPrice);
    $("#goodsStore").val(goodsStore);
    $("#goodsQuantity").val(goodsQuantity);
    $("#orderMax").val(orderMax);
    //表单验证的定义
    $("#sortForm").validate({
        errorElement: 'span',
        errorClass: 'help-block',
        focusInvalid: false,
        rules: {
            goodsActPrice: {
                required: true
            },
            goodsQuantity: {
                required: true
            },
            orderMax: {
                required: true
            }

        },
        messages: {
            goodsActPrice: {
                required: "请输入活动价格!"
            },
            goodsQuantity: {
                required: "请输入活动件数!"
            },
            orderMax: {
                required: "请输入购买限制件数!"
            }
        },

        highlight: function (element) {
            $(element)
                .closest('.form-group').addClass('has-error');
        },

        unhighlight: function (element) {
            $(element)
                .closest('.form-group').removeClass('has-error');
        },

        success: function (label) {
            label
                .closest('.form-group').removeClass('has-error');
        }
    });

    $("#btn_save_quantity").click(function () {
        if ($("#quantityForm").valid()) {
            set_goodsQuantity();
        }
    });
});

function set_goodsQuantity() {

    var actPrice = $("#goodsActPrice").val();
    if(parseFloat(actPrice) >= parseFloat(goodsPrice)){
        showTip('活动价格不能大于商品正价.');
        return;
    }

    var quantity = $("#goodsQuantity").val();
    if(quantity == undefined || parseInt(quantity) <= 0){
        showTip('活动件数不正确.');
        return;
    }

    var max = $("#orderMax").val();
    if(max == undefined || parseInt(max) <= 0){
        showTip('购买限制件数不正确.');
        return;
    }

    if(parseInt(goodsStore) < parseInt(quantity)){
        showTip('活动件数不能大于库存数量.');
        return;
    }

    if(parseInt(quantity) < parseInt(max)){
        showTip('购买限制件数不能大于活动件数.');
        return;
    }

    resetSecList(index, actPrice, quantity, max);
    $("#changeSecGoodsQuantity").modal('hide');
}


function showTip(message) {
    $("#change_quantity_tip").text(message);
    setTimeout(function () {
        $("#change_quantity_tip").text("");
    }, 2000);
}
