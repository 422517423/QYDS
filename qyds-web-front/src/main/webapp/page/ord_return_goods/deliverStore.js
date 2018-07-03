var rexOrderId = sessionStorage.getItem("rexOrderId");

var sTable;
$(document).ready(function () {
    $("#deliver_store_select_dialog").modal('show');

    getOrgAddressList(null, null);

    initTable();

    //检索按钮点击事件
    $("#store_select_btn_search").click(function () {
        sTable.fnDraw();
    });
});

function selectProvince() {

    var objS = document.getElementById("store_province_code");

    var pcode = objS.options[objS.selectedIndex].value;

    $("#store_city_code").empty();
    $("#store_district_code").empty();

    if (pcode) {
        getOrgAddressList('0', pcode);
    }
}

function selectCity() {

    var objS = document.getElementById("store_city_code");

    var ccode = objS.options[objS.selectedIndex].value;

    $("#store_district_code").empty();

    if (ccode) {
        getOrgAddressList('1', ccode);
    }
}

function getOrgAddressList(type, code){
    var url = "/ord_dispatch/getOrgAddressList.json";
    var param = {
        "provinceCode": type == '0' ? code : null,
        "cityCode": type == '1' ? code : null
    };

    var success = function (data) {
        if (data.resultCode == '00') {
            var array = data.data;
            if (array != null) {

                if (type == '0') {
                    $("#store_city_code").append($('<option>').attr('value', '-1').text('请选择'));
                    $.each(array, function (index, item) {
                        var $option = $('<option>').attr('value', item.ccode).text(item.cname);
                        $("#store_city_code").append($option);
                    });

                    $("#store_district_code").append($('<option>').attr('value', '-1').text('请选择'));

                } else if (type == '1') {
                    $("#store_district_code").append($('<option>').attr('value', '-1').text('请选择'));
                    $.each(array, function (index, item) {
                        var $option = $('<option>').attr('value', item.dcode).text(item.dname);
                        $("#store_district_code").append($option);
                    });
                } else {
                    $("#store_province_code").append($('<option>').attr('value', '-1').text('请选择'));
                    $.each(array, function (index, item) {
                        var $option = $('<option>').attr('value', item.pcode).text(item.pname);
                        $("#store_province_code").append($option);
                    });

                    $("#store_city_code").append($('<option>').attr('value', '-1').text('请选择'));
                    $("#store_district_code").append($('<option>').attr('value', '-1').text('请选择'));
                }
            }
        } else {
            showTip('门店地址失败,原因:' + data.resultMessage);
        }
    };
    var error = function () {
        showTip('网络异常.');
    };
    axse(url, {"data": JSON.stringify(param)}, success, error);
}

function initTable() {
    var tableOption = {
        "bProcessing": true,
        "bServerSide": true,
        "fnServerData": fnServerData,
        "bFilter": false,
        "bSort": false,
        "sAjaxSource": "../ord_dispatch/getAllStoreList.json",
        "fnServerParams": function (aoData) {

            var provinceCode =  $("#store_province_code").val();
            var cityCode =  $("#store_city_code").val();
            var districtCode =  $("#store_district_code").val();

            aoData.push({"name": "provinceCode", "value":(provinceCode == -1 ? null : provinceCode)});
            aoData.push({"name": "cityCode", "value": (cityCode == -1 ? null : cityCode)});
            aoData.push({"name": "districtCode", "value": (districtCode == -1 ? null : districtCode)});

        },
        "aoColumns": [
            {
                "mData": null,
                'bSortable': false,
                "fnRender": function (oObj) {
                    return '<a onclick=selectStoreItem("' + oObj.aData.store_code + '")>选择</a>';
                }
            },
            {"mData": "store_name_cn"},
            {"mData": "phone"}
        ],
    };

    sTable = $('#store_select_table').dataTable(tableOption);

    $('#store_select_table_info').hide();
    $('#store_select_table_length').hide();
    $('.dataTables_paginate').hide();
}

function selectStoreItem (storeCode){

    var url = "/ord_return_goods/editSubOrderStore.json";

    var param = {
        rexOrderId:rexOrderId,
        rexStoreId: storeCode
    };

    var success = function (data) {
        if (data.resultCode == '00') {
            $("#deliver_store_select_dialog").modal('hide');
            showAlert('提交成功!', '提示', function () {
                suboTable.fnDraw();
            });
        } else {
            showTip('提交失败,原因:' + data.resultMessage);
        }
    };
    var error = function () {
        showTip('网络异常.');
    };
    console.log(param);

    axse(url, {"data": JSON.stringify(param)}, success, error);
}

function showTip(message) {
    $("#store_select_tip").text(message);
    setTimeout(function () {
        $("#store_select_tip").text("");
    }, 2000);
}