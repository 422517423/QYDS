/**
 * Created by zlh on 2016/6/24.
 */
var oTable;
var oRow;
var username = sessionStorage.getItem("userName");
var userId = sessionStorage.getItem("userId");
$(document).ready(function() {
    getList();
});

var TableEditable = function () {
    return {
        //main function to initiate the module
        init: function () {
            oTable = $('#type_table').dataTable({
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
    axse("/erp_goods_type/getAll.json", {}, successFn, errorFn);
}

// 获取商品类别列表
function successFn(data) {
    if (data.resultCode == '00') {
        var array = data.data;
        console.log(array);
        if (array != null) {
            for (var i = 0; i < array.length; i++) {
                var data = new Array();
                //console.log(array[]);
                data[0] = array[i].topTypeCode;
                data[1] = array[i].topTypeNameCn;
                data[2] = array[i].typeCode;
                data[3] = array[i].typeNameCn;
                data[4] = new Date(array[i].insertTime).Format('yyyy-MM-dd');
                data[5] = new Date(array[i].updateTime).Format('yyyy-MM-dd');
                addRow(oTable, data);
            }
        }
    }
}

function errorFn(data) {
    showAlert('操作失败');
}