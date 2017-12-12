/**
 * Created by zlh on 2016/6/24.
 */
var oTable;
var oRow;
var memberId = sessionStorage.getItem("memberId");
$(document).ready(function() {
    $("#sendCoupon").modal('show');
    getList();
});

var TableEditable = function () {
    return {
        //main function to initiate the module
        init: function () {
            oTable = $('#send_coupon_table').dataTable({
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
    axse("/coupon_master/getSendList.json", {}, successFn, errorFn);
}

// 获取品牌列表
function successFn(data) {
    if (data.resultCode == '00') {
        var array = data.data;
        if (array != null) {
            for (var i = 0; i < array.length; i++) {
                var data = new Array();
                data[0] = '<a onclick=send("' + array[i].couponId + '")>补发</a>&nbsp;&nbsp;';
                data[1] = array[i].couponName;
                data[2] = array[i].couponTypeCn;
                data[3] = array[i].distributeTypeCn;
                data[4] = new Date(array[i].sendStartTime).Format('yyyy-MM-dd');
                data[5] = new Date(array[i].sendEndTime).Format('yyyy-MM-dd');
                data[6] = array[i].startTime==null?"":new Date(array[i].startTime).Format('yyyy-MM-dd');
                data[7] = array[i].endTime==null?"":new Date(array[i].endTime).Format('yyyy-MM-dd');
                data[8] = array[i].maxCount;
                data[9] = array[i].distributedCount;
                addRow(data);
                //alert(array[i].couponName);
            }
        }
    }
}

function errorFn(data) {
    showAlert('操作失败');
}

function send(couponId) {
    showConfirm("确定要补发该优惠券?", function () {
        sendCoupon(couponId);
    });
}

function sendCoupon(couponId) {
    var url = "/coupon_master/sendToMember.json";
    var param = {
        "memberId": memberId,
        "couponId": couponId
    };
    var success = function (data) {
        if (data.resultCode == '00') {
            $("#sendCoupon").modal('hide');
            showAlert("发送成功!", function () {
                table.fnDraw();
            });
        } else {
            $("#sendCoupon").modal('show');
            showTip('发送失败,原因:' + data.resultMessage);
        }
    };
    var error = function () {
        $("#sendCoupon").modal('show');
        showTip('发送失败,网络异常.');
    };
    axse(url, {'data': JSON.stringify(param)}, success, error);
}

function showTip(message) {
    $("#send_coupon_tip").text(message);
    setTimeout(function () {
        $("#send_coupon_tip").text("");
    }, 2000);
}

