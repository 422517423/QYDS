/**
 * Created by wy on 2016/7/18.
 */
var oTable;
var oRow;
var userId = sessionStorage.getItem("userId");
var userName = sessionStorage.getItem("userName");
//弹出modal的设置
var modalSettings = {
    backdrop: 'static',
    keyboard: false
};
$(document).ready(function () {
    getList();
    var validator = $("#com_code_form").validate({
        errorElement: 'span', //default input error message container
        errorClass: 'help-block', // default input error message class
        focusInvalid: false, // do not focus the last invalid input
        rules: {
            name: {
                required: true
            },
            value: {
                required: true
            },
            display_cn: {
                required: true
            },
            display_en: {
                required: true
            },
            comment: {
                required: true
            },
            code: {
                required: true
            }
        },
        messages: {
            name: {
                required: "请输入编码名称"
            },
            value: {
                required: "请输入编码值"
            },
            display_cn: {
                required: "请输入中文表示"
            },
            display_en: {
                required: "请输入英文表示"
            },
            code: {
                required: "编码"
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
//表格js初始化
var TableEditable = function () {
    return {
        init: function () {
            oTable = $('#com_code_table').dataTable({
                "aLengthMenu": [
                    [5, 15, 20, -1],
                    [5, 15, 20, "All"] // 分页设置
                ],
                // 每一页面显示五条
                "iDisplayLength": 5,

                "sPaginationType": "bootstrap",
                //汉化
                "oLanguage": {
                    "sLengthMenu": "_MENU_ 件",
                    "oPaginate": {
                        "sPrevious": "上一件",
                        "sNext": "下一件"
                    }
                },
                //降序排列 第一列降序
                "aaSorting": [[0, 'desc']]
            });
            //隐藏第一列
            oTable.fnSetColumnVis(0, false);
            jQuery('#com_code_table_wrapper .dataTables_filter input').addClass("form-control input-medium"); // modify table search input
            jQuery('#com_code_table_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
            jQuery('#com_code_table_wrapper .dataTables_length select').select2({
                showSearchInput: false //hide search box with special css class
            }); // initialize select2 dropdown
        }
    };
}();
/*添加按钮*/
$("#addBtn").click(function () {
    oRow = null;

    $("#code").attr("disabled", false);
    $("#value").attr("disabled", false);

    $("#com_code_edit").modal(modalSettings);
});
/*点击详细或添加保存*/
$("#save_btn").click(function () {
    if (!$("#com_code_form").valid()) {
        return;
    }
    var json = {};
    json.code = $("#code").val();
    json.name = $("#name").val();
    json.value = $("#value").val();
    json.displayCn = $("#display_cn").val();
    json.displayEn = $("#display_en").val();
    json.comment = $("#comment").val();
    json.insertUserId = userId;
    json.updateUserId = userId;

    var url = '';
    if (oRow != null) {
        url = "/com_code/updateItem.json";
    } else {
        url = "/com_code/insertItem.json";
    }

    axse(url, {'data': JSON.stringify(json)}, saveSuccessFn, errorFn);
});
/*文本框reset*/
$("#com_code_edit").on("hidden.bs.modal", function (e) {
    $('#com_code_form')[0].reset();
    $("#com_code_form").validate().resetForm();
});
/*列表获取的后台访问方法*/
function getList() {
    axse("/com_code/getList.json", {}, successFn, errorFn);
}
/*初始化列表成功的回调方法*/
function successFn(data) {
    if (data.resultCode == '00') {
        var array = data.data;
        if (array != null) {
            for (var i = 0; i < array.length; i++) {
                var data = new Array();
                data[0] = array[i].updateTime;
                data[1] = array[i].code;
                data[2] = array[i].name;
                data[3] = array[i].value;
                data[4] = array[i].displayCn;
                data[5] = array[i].displayEn;
                data[6] = array[i].comment;
                data[7] = array[i].userName;
                data[8] = array[i].insertTime;
                data[9] = '<a class="com_code_edit" href="#"code="' + array[i].code + '"value="' + array[i].value + '" data-toggle="modal">详细</a>';
                // data[10] = '<a class="com_code_delete" code="' + array[i].code + '"value="' + array[i].value + '"  href="javascript:;">删除</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
                addRow(oTable, data);
            }
        }
    } else {

    }
}
/*点击详细*/
$(".com_code_edit").live('click', function () {
    oRow = $(this).parents('tr')[0];
    var editCode = $(this).attr("code");
    var editValue = $(this).attr("value");

    $("#code").attr("disabled", true);
    $("#value").attr("disabled", true);

    $("#com_code_edit").modal();
    $("#com_code_edit").modal(modalSettings);
    $("#com_code_edit").modal('show');
    axse("/com_code/edit.json", {"code": editCode, "value": editValue}, editSuccessFn, errorFn);
});
/*详细画面回调*/
function editSuccessFn(data) {
    if (data.resultCode == '00') {
        $("#code").val(data.data.code);
        $("#name").val(data.data.name);
        $("#value").val(data.data.value);
        $("#display_cn").val(data.data.displayCn);
        $("#display_en").val(data.data.displayEn);
        $("#comment").val(data.data.comment);
    }
}
/*保存成功方法回调*/
function saveSuccessFn(data) {
    console.log(data);
    if (data.resultCode == '00') {
        if (oRow != null) {
            updateRow(oTable, oRow, data);
        } else {
            var rowData = new Array();
            rowData[0] = data.data.updateTime;
            rowData[1] = data.data.code;
            rowData[2] = data.data.name;
            rowData[3] = data.data.value;
            rowData[4] = data.data.displayCn;
            rowData[5] = data.data.displayEn;
            rowData[6] = data.data.comment;
            rowData[7] = userName;
            rowData[8] = data.data.insertTime;
            rowData[9] = '<a class="com_code_edit" href="#" code="' + data.data.code + '" value="' + data.data.value + '" data-toggle="modal">详细</a>';
            // rowData[10] = '<a class="com_code_delete" code="' + data.data.code + '" value="' + data.data.value + '" href="javascript:;">删除</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
            addRow(oTable, rowData);
        }
        $("#com_code_edit").modal('hide');
    } else {
        showTip('保存失败,原因:' + data.resultMessage);
    }
}
/* 删除提示框*/
var deleteCode;
var deleteValue;
$(".com_code_delete").live('click', function () {
    oRow = $(this).parents('tr')[0];
    deleteCode = $(this).attr("code");
    deleteValue = $(this).attr("value");
    console.log(deleteValue);
    showConfirm('确认删除该条信息吗?', function () {
        axse("/com_code/delete.json", {"code": deleteCode,"value":deleteValue}, delSuccessFn, errorFn);
    });
});
/*删除成功方法回调*/
function delSuccessFn(data) {
    if (data.resultCode == '00') {
        oTable.fnDeleteRow(oRow);
    }
}
function updateRow(oTable, nRow, data) {
    oTable.fnUpdate(data.data.updateTime, nRow, 0, false);
    oTable.fnUpdate(data.data.code, nRow, 1, false);
    oTable.fnUpdate(data.data.name, nRow, 2, false);
    oTable.fnUpdate(data.data.value, nRow, 3, false);
    oTable.fnUpdate(data.data.displayCn, nRow, 4, false);
    oTable.fnUpdate(data.data.displayEn, nRow, 5, false);
    oTable.fnUpdate(data.data.comment, nRow, 6, false);
    oTable.fnUpdate(userName, nRow, 7, false);
    oTable.fnUpdate(data.data.insertTime, nRow, 8, false);
    oTable.fnDraw();
}
/*向表格里边添加一行数据*/
function addRow(oTable, data) {
    oTable.fnAddData(data);
}
/*失败的回调方法*/
function errorFn() {
    showAlert('列表数据获取失败');
}

function showTip(message) {
    $("#com_code_edit_tip").text(message);
    setTimeout(function () {
        $("#com_code_edit_tip").text("");
    }, 2000);
}

