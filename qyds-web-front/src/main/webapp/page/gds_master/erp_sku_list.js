/**
 * Created by zlh on 2016/6/24.
 */
var eTable;
var oRow;
$(document).ready(function() {

    //取消按钮的点击事件
    $('#cancel_btn').click(function(){
        gotoListPage();
    });


    $(".store_edit").live('click', function () {
        oRow = $(this).parents('tr')[0];
        var sizeTypeCode = $(this).attr("sizeTypeCode");
        var sizeCode = $(this).attr("sizeCode");
        $("#storeEdit").modal();
        $("#storeEdit").modal(modalSettings);
        $("#storeEdit").modal('show');
        sessionStorage.setItem("sizeTypeCode",sizeTypeCode);
        sessionStorage.setItem("sizeCode",sizeCode);
    });

    $("#save_btn").click(function() {

        var json = {};
        json.sizeTypeCode = sessionStorage.getItem("sizeTypeCode");
        json.sizeCode = sessionStorage.getItem("sizeCode");
        json.bnkNoLimit = $("#bnk_no_limit").val();
        json.bnkLessLimit = $("#bnk_less_limit").val();
        axse("/gds_master/storeSave.json", {'data':JSON.stringify(json)}, saveSuccessFn, errorFn);
    });


    getList(sessionStorage.getItem("goodsCode"));
});

var ErpSkuListTable = function () {
    return {
        //main function to initiate the module
        init: function () {
            eTable = $('#sku_list_table').dataTable({
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
                "bSort": false,
                //升序排列 第一列升序
                //"aaSorting": [[0,'asc']]
            });
        }
    };
}();

var modalSettings = {
    backdrop : 'static',
    keyboard : false
};

function addRow(data) {
    var aiNew = eTable.fnAddData(data);
}

function getList(goodCode) {
    axse("/gds_master/erpSkuList.json", {'goodCode':goodCode}, successFn, errorFn);
}

// 获取颜色列表
function successFn(data) {
    if (data.resultCode == '00') {
        var array = data.data;
        //console.log(array);
        if (array != null) {
            for (var i = 0; i < array.length; i++) {
                var data = new Array();
                //console.log(array[]);
                data[0] = array[i].sku;
                data[1] = array[i].colorName;
                data[2] = array[i].sizeName;
                data[3] = array[i].price;
                //data[4] = array[i].bnkNoLimit;
                //data[5] = array[i].bnkLessLimit;
                //data[6] = '<a class="store_edit" href="#" sizeTypeCode="' + array[i].sizeTypeCode + '" sizeCode="' + array[i].sizeCode + '" data-toggle="modal">安全库存</a>';
                addRow(data);
            }
        }
    }
}

function saveSuccessFn(data){
    $("#storeEdit").modal('hide');
    if (data.resultCode == '00') {
        updateRow(eTable, oRow, data);
    }
}

function updateRow(oTable, nRow, data) {
    console.log(data.data);
    //oTable.fnUpdate(data.data.sku, nRow, 0, false);
    //oTable.fnUpdate(data.data.colorName, nRow, 1, false);
    //oTable.fnUpdate(data.data.sizeName, nRow, 2, false);
    //oTable.fnUpdate(data.data.price, nRow, 3, false);
    oTable.fnUpdate(data.data.bnkNoLimit, nRow, 4, false);
    oTable.fnUpdate(data.data.bnkLessLimit, nRow, 5, false);
    oTable.fnUpdate('<a class="store_edit" href="#" sizeTypeCode="' + data.data.sizeTypeCode + '" sizeCode="' + data.data.sizeCode + '" data-toggle="modal">安全库存</a>', nRow, 6, false);
    oTable.fnDraw();
}


//退回到列表画面
function gotoListPage(){
    $('#content').load('gds_master/list.html');
}

function errorFn(data) {
    showAlert('操作失败');
}