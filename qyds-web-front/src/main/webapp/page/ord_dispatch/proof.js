/**
 * Created by zlh on 16/10/27.
 */

var orderId = sessionStorage.getItem("orderId");

$(document).ready(function () {

    $("#proofArea").modal('show');

    $("#btn_print").click(function () {
        proof_print();
    });

    if (orderId && orderId.length > 0) {
        getDetail();
    }
});

function getDetail() {
    var url = "/ord_dispatch/proof.json";
    var param = {
        orderId: orderId
    };
    var successCallback = function (data) {
        if (data.resultCode == '00') {
            order_id.innerHTML = data.master.orderId;
            order_code.innerHTML = data.master.orderCode;
            order_time.innerHTML = new Date(data.master.orderTime).Format("yyyy-MM-dd  hh:mm");
            detail.innerHTML = data.sublist;
            price_discount.innerHTML = data.master.amountDiscount + " 元";
            store_name.innerHTML = data.store.storeNameCn;
            store_addr.innerHTML = data.store.address;
            store_tel.innerHTML = data.store.phone;
            print_time.innerHTML = new Date().Format("yyyy-MM-dd  hh:mm");
        } else {
            $("#proofArea").modal('hide');
            showAlert('取得失败,原因:' + data.resultMessage);
        }
    };
    var errorCallback = function () {
        $("#proofArea").modal('hide');
        showAlert('网络异常.');
    };
    // axse(url, {"data": JSON.stringify(param)}, successCallback, errorCallback);
    axse(url, {"data": JSON.stringify(param)}, successCallback, successCallback);

}

//点击打印按钮
function proof_print() {
    win=window.open();
    win.document.body.innerHTML=proofContent.innerHTML;
    win.print();
    win.close();
}
