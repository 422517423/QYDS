/**
 * Created by zlh on 2016/6/24.
 */
var oTable;
var oRow;
var username = sessionStorage.getItem("userName");
var userId = sessionStorage.getItem("userId");
$(document).ready(function() {

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


    getList();
});

var TableEditable = function () {
    return {
        //main function to initiate the module
        init: function () {
            oTable = $('#size_table').dataTable({
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
                //排序 第一三列升序
                "aaSorting": [[0,'asc'],[2,'asc']]
            });
        }
    };
}();

var modalSettings = {
    backdrop : 'static',
    keyboard : false
};

function addRow(oTable, data) {
    var aiNew = oTable.fnAddData(data);
}

function getList() {
    axse("/erp_goods_size/getAll.json", {}, successFn, errorFn);
}

// 获取商品尺码列表
function successFn(data) {
    if (data.resultCode == '00') {
        var array = data.data;
        console.log(array);
        if (array != null) {
            for (var i = 0; i < array.length; i++) {
                var data = new Array();
                //console.log(array[]);
                data[0] = array[i].sizeTypeCode;
                data[1] = array[i].typeName;
                data[2] = array[i].sort;
                data[3] = array[i].sizeCode;
                data[4] = array[i].sizeFullNameCn;
                data[5] = new Date(array[i].insertTime).Format('yyyy-MM-dd');
                data[6] = new Date(array[i].updateTime).Format('yyyy-MM-dd');
                data[7] = array[i].bnkNoLimit;
                data[8] = array[i].bnkLessLimit;
                data[9] = '<a class="store_edit" href="#" sizeTypeCode="' + array[i].sizeTypeCode + '" sizeCode="' + array[i].sizeCode + '" data-toggle="modal">安全库存</a>';
                addRow(oTable, data);
            }
        }
    }
}


function saveSuccessFn(data){
    $("#storeEdit").modal('hide');
    if (data.resultCode == '00') {
        updateRow(oTable, oRow, data);
    }
}

function updateRow(oTable, nRow, data) {
    console.log(data.data);
    oTable.fnUpdate(data.data.bnkNoLimit, nRow, 7, false);
    oTable.fnUpdate(data.data.bnkLessLimit, nRow, 8, false);
    oTable.fnUpdate('<a class="store_edit" href="#" sizeTypeCode="' + data.data.sizeTypeCode + '" sizeCode="' + data.data.sizeCode + '" data-toggle="modal">安全库存</a>', nRow, 9, false);
    oTable.fnDraw();
}

function errorFn(data) {
    showAlert('操作失败');
}