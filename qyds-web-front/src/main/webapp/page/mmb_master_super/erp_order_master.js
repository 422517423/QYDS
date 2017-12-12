/**
 * Created by zlh on 2016/6/24.
 */
var oTable;
var oRow;
var memberId = sessionStorage.getItem("memberId");
$(document).ready(function() {
    $("#erpOrderMaster").modal('show');
    getList();
});

var TableEditable = function () {
    return {
        //main function to initiate the module
        init: function () {
            oTable = $('#erp_order_master_table').dataTable({
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

    var url = "/ord_master/getOffLineOrderList.json";

    var success = function (data) {
        if (data.resultCode == '00') {
            var array = data.results;
            if (array != null) {
                for (var i = 0; i < array.length; i++) {
                    var data = new Array();
                    data[0] = array[i].orderCode;
                    data[1] = array[i].memberCode;
                    data[2] = array[i].memberName;
                    data[3] = array[i].storeCode;
                    data[4] = array[i].storeName;
                    data[5] = array[i].orderTime==null?"":new Date(array[i].orderTime).Format('yyyy-MM-dd');
                    data[6] = array[i].amount;
                    addRow(data);
                }
            }
        } else {
            showAlert('线下订单获取失败!');
        }
    };
    var error = function () {
        showAlert('线下订单获取失败!');
    };
    axse(url, {'data': memberId}, success, error);
}


function errorFn(data) {
    showAlert('操作失败');
}


