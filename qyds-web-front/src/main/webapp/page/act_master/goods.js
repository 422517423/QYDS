/**
 * Created by cky on 2016/7/18.
 */
var oTable;

$(document).ready(function() {
    //获取列表数据
    getList();
    //检索按钮点击事件
    $("#btn_search").click(function(){
        getList();
    });
});

//表格js初始化
var TableEditable = function () {
    return {
        init: function () {
            oTable = $('#act_master_table').dataTable({
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
                "aaSorting": [[0,'desc']]
            });
            //隐藏第一列
            oTable.fnSetColumnVis( 0, false );
            jQuery('#act_master_table_wrapper .dataTables_filter input').addClass("form-control input-medium"); // modify table search input
            jQuery('#act_master_table_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
            jQuery('#act_master_table_wrapper .dataTables_length select').select2({
                showSearchInput : false //hide search box with special css class
            }); // initialize select2 dropdown


        }
    };
}();

//列表获取的后台访问方法
function getList() {
    axse("/act_master/goods.json", {}, successFn, errorFn);
}


// 列表获取成功的回调方法
function successFn(data) {
    if (data.resultCode == '00') {
        var array = data.data;
        if (array != null) {
            for (var i = 0; i < array.length; i++) {
                var data = new Array();
                data[0] = array[i].updateTime;
                data[1] = array[i].tempName;
                data[2] = array[i].comment;
                data[3] = array[i].startTime;
                data[4] = array[i].endTime;
                data[5] = array[i].applyUser;
                data[6] = array[i].applyTime;
                data[7] = array[i].approveStatus;
                data[8] = array[i].approveUser;
                data[9] = array[i].approveTime;
                data[10] = '<a class="brand_edit" href="#" roleId="' + array[i].brandId + '" data-toggle="modal">详细</a>';
                data[11] = '<a class="brand_delete" roleId="' + array[i].brandId + '" href="javascript:;">删除</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="add_menu" roleId="' + array[i].roleId + '" href="javascript:;">审核</a>';
                addRow(oTable, data);
            }
        }
    }
}
// 失败的回调方法
function errorFn() {
    showAlert('列表数据获取失败');
}

//向表格里边添加一行数据
function addRow(oTable, data) {
    oTable.fnAddData(data);
}