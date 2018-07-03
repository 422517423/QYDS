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
            oTable = $('#store_table').dataTable({
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
                //排序 第一列升序
                "aaSorting": [[0,'asc']]
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
    axse("/erp_store/getAll.json", {}, successFn, errorFn);
}

// 获取品牌列表
function successFn(data) {
    if (data.resultCode == '00') {
        var array = data.data;
        console.log(array);
        if (array != null) {
            for (var i = 0; i < array.length; i++) {
                var data = new Array();
                //console.log(array[i]);
                data[0] = array[i].storeCode;
                data[1] = array[i].bankOnly;
                data[2] = array[i].storeNameCn;
                data[3] = array[i].provinceCode;
                data[4] = array[i].provinceName;
                data[5] = array[i].cityCode;
                data[6] = array[i].cityName;
                data[7] = array[i].districtCode;
                data[8] = array[i].districtName;
                data[9] = array[i].address;
                data[10] = array[i].phone;
                data[11] = new Date(array[i].insertTime).Format('yyyy-MM-dd');
                data[12] = new Date(array[i].updateTime).Format('yyyy-MM-dd');
                addRow(oTable, data);
            }
        }
    }
}

function errorFn(data) {
    showAlert('操作失败');
}