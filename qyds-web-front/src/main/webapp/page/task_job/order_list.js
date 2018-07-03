/**
 * Created by zlh on 16/11/9.
 */
var oTable;
var oRow;
$(document).ready(function() {

    //全部发送按钮点击事件
    $("#btn_send_all").click(function(){
        showConfirm("确定要发送全部订单?", function () {
            sendAll();
        });
    });
    getList();
});

var TableEditable = function () {
    return {
        //main function to initiate the module
        init: function () {
            oTable = $('#ord_master_table').dataTable({
                "aLengthMenu": [
                    [5, 15, 20, -1],
                    [5, 15, 20, "All"] // change per page values here
                ],
                // set the initial value
                "iDisplayLength": -1,

                "sPaginationType": "bootstrap",
                "oLanguage": {
                    "sLengthMenu": "_MENU_ 件",
                    "oPaginate": {
                        "sPrevious": "上一件",
                        "sNext": "下一件"
                    }
                },
                //排序 第一列升序
                "bSort": false
                //,"aaSorting": [[0,'asc']]
            });
        }
    };
}();

var modalSettings = {
    backdrop : 'static',
    keyboard : false
};

function addRow(data) {
    oTable.fnAddData(data);
}

function getList() {
    axse("/task_job/getFailOrder.json", {}, successFn, errorFn);
}

// 获取品牌列表
function successFn(data) {
    if (data.resultCode == '00') {
        var array = data.data;
        if (array != null) {
            for (var i = 0; i < array.length; i++) {
                var data = new Array();
                data[0] = '<a class="order_edit" href="#" onclick=order_edit("'+ array[i].orderId+'");>详细</a>&nbsp;&nbsp; <a onclick=send("' + array[i].orderId + '")>发送</a>&nbsp;&nbsp;';
                data[1] = array[i].orderId;
                data[2] = array[i].orderCode;
                data[3] = array[i].amountTotle;
                data[4] = new Date(array[i].orderTime).Format('yyyy-MM-dd');
                addRow(data);
            }
        }
    }
}

function errorFn(data) {
    showAlert('操作失败');
}

function send(orderId) {
    showConfirm("确定要发送该订单?", function () {
        sendOrder(orderId);
    });
}

function sendOrder(orderId) {
    var url = "/task_job/sendOrderById.json";
    var success = function (data) {
        if (data.resultCode == '00') {
            showAlert("发送成功!", function () {
                oTable.fnDraw();
            });
        } else {
            showAlert('发送失败,原因:' + data.message);
        }
    };
    var error = function () {
        showAlert('发送失败,网络异常.');
    };
    axse(url, {"orderId": orderId}, success, error);
}

function sendAll() {
    var url = "/task_job/sendFailOrder.json";
    var success = function (data) {
        if (data.resultCode == '00') {
            showAlert("发送成功!", function () {
                oTable.fnDraw();
            });
        } else {
            showAlert('发送失败,原因:' + data.message);
        }
    };
    var error = function () {
        showAlert('发送失败,网络异常.');
    };
    axse(url, {}, success, error);
}

function order_edit(id) {
    sessionStorage.setItem("backPage","task_job/order_list.html");
    sessionStorage.setItem("orderId", id);
    $('#content').load('ord_master/edit.html');
}