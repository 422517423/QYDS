/**
 * Created by zlh on 2016/10/10.
 */

var rexOrderId = sessionStorage.getItem("rexOrderId");
var orderId;

$(document).ready(function() {
    getEditData();
    //通过按钮的点击事件
    $('#pay_btn').click(function(){
        submit();
    });
    //返回按钮的点击事件
    $('#return_btn').click(function(){
        gotoListPage();
    });
});
function submit(){
    var json = {};
    json.rexOrderId = rexOrderId;
    json.orderId = orderId;
    json.refundInfact = $('#refundInfact').val();
    axse("/ord_refund/pay.json", {'data':JSON.stringify(json)}, submitSuccessFn, errorFn);
}

//审批提交成功
function submitSuccessFn(data){
    if(data.resultCode == '00'){
        showAlert('提交成功!');
        gotoListPage();
    }else{
        showAlert(data.resultMessage);
    }

}

// 获取退单信息
function getEditData(){
    axse("/ord_refund/getReturnInfo.json", {"rexOrderId":rexOrderId}, eidtSuccessFn, errorFn);
}

//获取详细信息成功的回调 用来显示
function eidtSuccessFn(data){
    if (data.resultCode == '00') {
        var item = data.data;
        if(item == null){
            showAlert('该订单没有退货信息');
        }else{
            orderId = item.orderId;
            $('#amountTotle').val(item.amountTotle);
            $('#amountDiscount').val(item.amountDiscount);
            $('#amountPoint').val(item.amountPoint);
            $('#amountCoupon').val(item.amountCoupon);
            $('#payInfact').val(item.payInfact);
            $('#orderStatus').val(item.orderStatusName);
            $('#payStatus').val(item.payStatusName);
            $('#deliverStatus').val(item.deliverStatusName);
            $('#rexStatus').val(item.rexStatusName);
            $('#orderTime').val(new Date(item.orderTime).Format("yyyy-MM-dd hh:mm:ss"));
            $('#applyTime').val(new Date(item.applyTime).Format("yyyy-MM-dd hh:mm:ss"));
            $('#applyComment').val(item.applyComment);
            $('#applyAnswerTime').val(new Date(item.applyAnswerTime).Format("yyyy-MM-dd hh:mm:ss"));
            $('#applyAnswerComment').val(item.applyAnswerComment);
            $('#refundInfact').val(item.payInfact);
        }


    }else{
        showAlert('退单信息获取失败');
    }

}

// 失败的回调方法
function errorFn() {
    showAlert('数据获取失败');
}

//退回到列表画面
function gotoListPage(){
    $('#content').load('ord_refund/pay_list.html');
}
