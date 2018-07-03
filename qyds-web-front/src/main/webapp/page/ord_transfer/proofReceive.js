/**
 * Created by zlh on 16/12/26.
 */

var orderTransferId = sessionStorage.getItem("orderTransferId");

$(document).ready(function () {
    $("#proofDeliveryArea").modal('show');
    $("#btn_print").click(function () {
        proof_print();
    });
    if (orderTransferId && orderTransferId.length > 0) {
        getDetail();
    }
});

function getDetail() {
    var url = "/ord_transfer/detail.json";
    var param = {
        orderTransferId: orderTransferId
    };
    var successCallback = function (data) {
        console.log(data);
        if (data.resultCode == '00') {
            label_goods_name.innerHTML = data.data.goodsName;
            label_sku.innerHTML = data.data.sku;
            label_colore_name.innerHTML = data.data.coloreName;
            label_size_name.innerHTML = data.data.sizeName;
            label_quantity.innerHTML = data.data.quantity;
            label_dispatch_store.innerHTML = data.data.dispatchStore;
            label_express_name.innerHTML = data.data.expressName;
            label_express_no.innerHTML = data.data.expressNo;
            label_receive_user.innerHTML = data.data.receiveUser;
            label_receive_time.innerHTML = new Date(data.data.receiveTime).Format("yyyy-MM-dd  hh:mm");
            label_apply_store.innerHTML = data.data.applyStore;
            label_print_time.innerHTML = new Date().Format("yyyy-MM-dd  hh:mm");
        } else {
            $("#proofDeliveryArea").modal('hide');
            showAlert('取得失败,原因:' + data.resultMessage);
        }
    };
    var errorCallback = function () {
        $("#proofDeliveryArea").modal('hide');
        showAlert('网络异常.');
    };
    // axse(url, {"data": JSON.stringify(param)}, successCallback, errorCallback);
    axse(url, param, successCallback, successCallback);

}

//点击打印按钮
function proof_print() {
    win=window.open();
    win.document.body.innerHTML=proofContent.innerHTML;
    win.print();
    win.close();
}
