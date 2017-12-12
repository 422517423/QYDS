/**
 * Created by cky on 2016/7/18.
 */

//弹出modal的设置
var oRow;
var userId = sessionStorage.getItem("userId");
var userName= sessionStorage.getItem("userName");

var modalSettings = {
    backdrop: 'static',
    keyboard: false
};

$(document).ready(function () {
    getList();
    $("#ShpMasterForm").validate({
        errorElement: 'span', //default input error message container
        errorClass: 'help-block', // default input error message class
        focusInvalid: false, // do not focus the last invalid input
        rules: {
            shop_code: {
                required: true
            },
            shop_short_name: {
                required: true
            },
            shop_name: {
                required: true
            },
            comment: {
                required: true
            }
        },
        messages: {
            shop_code: {
                required: "请输入店铺编码"
            },
            shop_short_name: {
                required: "请输入店铺简称"
            },
            shop_name: {
                required: "请输入店铺名称"
            },
            comment: {
                required: "请输入说明"
            }

        },
        highlight: function (element) { // hightlight error inputs
            $(element)
                .closest('.form-group').addClass('has-error'); // set error class to the control group
        },

        unhighlight: function (element) { // revert the change done by hightlight
            $(element)
                .closest('.form-group').removeClass('has-error'); // set error class to the control group
        },

        success: function (label) {
            label
                .closest('.form-group').removeClass('has-error'); // set success class to the control group
        }
    });
});
/*文本框清空*/
$("#shp_master_edit").on("hidden.bs.modal", function (e) {
    $('#ShpMasterForm')[0].reset();
    $("#ShpMasterForm").validate().resetForm();
});

var oTable;
/*表格初始化*/
var TableEditable = function () {
    return {
        init: function () {
            oTable = $('#shp_master_table').dataTable({
                "aLengthMenu": [
                    [5, 15, 20, -1],
                    [5, 15, 20, "All"] // change per page values here
                ],
                // set the initial value
                "iDisplayLength": 5,

                "sPaginationType": "bootstrap",
                "oLanguage": {
                    "sLengthMenu": "_MENU_ 件",
                    "oPaginate": {
                        "sPrevious": "上一件",
                        "sNext": "下一件"
                    }
                },
                "aoColumnDefs": [{
                    'bSortable': false,
                    'aTargets': [0]
                }],
                /*降序排列 第一列降序*/
                "aaSorting": [[0, 'desc']]
            });
            /*隐藏第一列*/
            oTable.fnSetColumnVis(0, false);
            jQuery('#shp_master_table_wrapper .dataTables_filter input').addClass("form-control input-medium"); // modify table search input
            jQuery('#shp_master_table_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
            jQuery('#shp_master_table_wrapper .dataTables_length select').select2({
                showSearchInput: false //hide search box with special css class
            }); // initialize select2 dropdown

        }
    };
}();
/*店铺详细信息*/
var shpmasterId;
$(".master_edit").live('click', function () {
    var shpmasterId = $(this).attr("shopid");
    oRow = $(this).parents('tr')[0];
    $("#shp_master_edit").modal();
    $("#shp_master_edit").modal(modalSettings);
    $("#shp_master_edit").modal('show');
    axse("/shp_master/edit.json", {"shopId": shpmasterId}, editSuccessFn, errorFn);
});
/*详细画面回调*/
function editSuccessFn(data) {
    if (data.resultCode == '00') {
        $("#shop_name").val(data.data.shopName);
        $("#shop_short_name").val(data.data.shopShortName);
        $("#shop_code").val(data.data.shopCode);
        $("#comment").val(data.data.comment);
    }
}
/*列表初始化*/
function getList() {
    axse("/shp_master/getList.json", {}, successFn, errorFn);
}
/*列表获取成功回调*/
function successFn(data) {
    if (data.resultCode == '00') {
        var array = data.data;
        if (array != null) {
            for (var i = 0; i < array.length; i++) {
                var data = new Array();
                data[0] = array[i].updateTime;
                data[1] = array[i].shopCode;
                data[2] = array[i].shopShortName;
                data[3] = array[i].shopName;
                data[4] = array[i].comment;
                data[5] = array[i].userName;
                data[6] = array[i].insertTime;
                data[7] = '<a class="master_edit" href="#" shopid="' + array[i].shopId + '" data-toggle="modal">详细</a>';
                data[8] = '<a class="master_delete" shopid="' + array[i].shopId + '" href="javascript:;">删除' +
                    '</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
                addRow(oTable, data);
            }
        }
    }
}
/*绑定添加按钮*/
$("#addBtn").click(function () {
    oRow = null;
    $("#ShpMasterForm").validate().resetForm();
    $("#shp_master_edit").modal();
    $("#shp_master_edit").modal(modalSettings);
    $("#shp_master_edit").modal('show');
});
/*绑定添加/编辑的保存钮*/
$("#save_btn").click(function () {
    if (!$("#ShpMasterForm").valid()) {
        return;
    }
    var json = {};
    json.shopId = $("#shop_id").val();
    json.shopCode = $("#shop_code").val();
    json.shopName = $("#shop_name").val();
    json.shopShortName = $("#shop_short_name").val();
    json.comment = $("#comment").val();
    json.updateUserId = userId;
    json.insertUserId = userId;
    axse("/shp_master/save.json", {'data': JSON.stringify(json)}, saveSuccessFn, errorFn);
});
/*保存成功回调*/
function saveSuccessFn(data) {
    $("#shp_master_edit").modal('hide');
    if (data.resultCode == '00') {
        if (oRow != null) {
            updateRow(oTable, oRow, data);
        } else {
            var rosData = new Array();
            rosData[0] = data.data.updateTime;
            rosData[1] = data.data.shopCode;
            rosData[2] = data.data.shopShortName;
            rosData[3] = data.data.shopName;
            rosData[4] = data.data.comment;
            rosData[5] = userName;
            rosData[6] = data.data.insertTime;
            rosData[7] = '<a class="master_edit" href="#" shopid="' + data.data.shopId + '" data-toggle="modal">详细</a>';
            rosData[8] = '<a class="master_delete" shopid="' + data.data.shopId + '" href="javascript:;">删除' +
                '</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
            addRow(oTable, rosData);
        }
    }
}
/*绑定删除按钮*/
var deletemasterId;
$(".master_delete").live('click', function () {
    oRow = $(this).parents('tr')[0];
    deletemasterId = $(this).attr('shopid');
    showConfirm('确认删除该店铺信息吗?', function () {
        axse("/shp_master/delete.json", {"shopId": deletemasterId}, delSuccessFn, errorFn);
    });
});
function delSuccessFn(data) {
    if (data.resultCode == '00') {
        oTable.fnDeleteRow(oRow);
    }
}
function updateRow(oTable, nRow, data) {
    oTable.fnUpdate(data.data.updateTime, nRow, 0, false);
    oTable.fnUpdate(data.data.shopCode, nRow, 1, false);
    oTable.fnUpdate(data.data.shopShortName, nRow, 2, false);
    oTable.fnUpdate(data.data.shopName, nRow, 3, false);
    oTable.fnUpdate(data.data.comment, nRow, 4, false);
    oTable.fnUpdate(userName, nRow, 5, false);
    oTable.fnUpdate(data.data.insertTime, nRow, 6, false);
    oTable.fnDraw();
}
/*回调失败方法*/
function errorFn() {
    showAlert('列表数据获取失败');
}
function addRow(oTable, data) {
    var aiNew = oTable.fnAddData(data);
}