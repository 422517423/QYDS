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
            oTable = $('#color_table').dataTable({
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
    axse("/erp_color/getAll.json", {}, successFn, errorFn);
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
                data[0] = array[i].colorCode;
                data[1] = array[i].helpcode;
                data[2] = array[i].colorNameEn;
                data[3] = array[i].colorNameCn;
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