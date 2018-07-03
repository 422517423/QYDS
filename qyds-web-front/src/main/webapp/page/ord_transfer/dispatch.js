var orderTransferId = sessionStorage.getItem("orderTransferId");
var orderId = sessionStorage.getItem("orderId");
var sku = sessionStorage.getItem("sku");

var oTable;
$(document).ready(function () {
    $("#dispatch_store_select_dialog").modal('show');
    initProvince();
    initTable();
    //检索按钮点击事件
    $("#store_select_btn_search").click(function () {
        oTable.fnDraw();
    });
});

//获得码表数据方法
function initProvince() {
    getErpProvince("store_province_code","store_city_code","store_district_code");
}
function selectProvince() {
    getErpCity("store_province_code","store_city_code","store_district_code");
}
function selectCity() {
    getErpDistrict("store_city_code","store_district_code");
}

function initTable() {
    var tableOption = {
        "bProcessing": true,
        "bServerSide": true,
        "fnServerData": fnServerData,
        "bFilter": false,
        "bSort": false,
        "sAjaxSource": "../ord_transfer/getStoreList.json",
        "fnServerParams": function (aoData) {
            aoData.push({"name": "sku", "value": sku});
            aoData.push({"name": "provinceCode", "value":$("#store_province_code").val()});
            aoData.push({"name": "cityCode", "value":$("#store_city_code").val()});
            aoData.push({"name": "districtCode", "value":$("#store_district_code").val()});
        },
        "aoColumns": [
            {"mData": "storeNameCn"},
            {"mData": "newCount"},
            {"mData": "phone"},
            {
                "mData": null,
                'bSortable': false,
                "fnRender": function (oObj) {
                    if(oObj.aData.newCount > 0){
                        return '<a onclick=dispatch("' + oObj.aData.erpStoreId + '")>派单</a>';
                    } else {
                        return '';
                    }
                }
            }
        ],
    };
    oTable = $('#store_select_table').dataTable(tableOption);
    $('#store_select_table_info').hide();
    $('#store_select_table_length').hide();
    $('.dataTables_paginate').hide();
}

function dispatch (storeCode){
    var url = "/ord_transfer/dispatch.json";
    var param = {
        orderTransferId:orderTransferId,
        orderId:orderId,
        dispatchStore: storeCode
    };
    var success = function (data) {
        if (data.resultCode == '00') {
            $("#dispatch_store_select_dialog").modal('hide');
            showAlert('提交成功!', '提示', function () {
                dispatchback();
            });
        } else {
            $("#dispatch_store_select_dialog").modal('hide');
            showAlert('提交失败:' + data.resultMessage);
        }
    };
    var error = function () {
        $("#dispatch_store_select_dialog").modal('hide');
        showAlert('网络异常.');
    };
    axse(url, param, success, error);
}

//function showTip(message) {
//    $("#store_select_tip").text(message);
//    setTimeout(function () {
//        $("#store_select_tip").text("");
//    }, 2000);
//}