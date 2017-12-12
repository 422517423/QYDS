/**
 * Created by zlh on 16/10/27.
 */

var memberId = sessionStorage.getItem("memberId");
var pointTable;
$(document).ready(function () {

    $("#recordArea").modal('show');

    initTable();

    //if (jQuery().datepicker) {
    //    $('.date-picker').datepicker({
    //        rtl: App.isRTL(),
    //        autoclose: true,
    //    });
    //    $('body').removeClass("modal-open");
    //}
});

//表格js初始化
function initTable() {
    var tableOption = {
        "bProcessing": true,
        "bServerSide": true,
        "fnServerData": fnServerData,
        "oLanguage": {
            "sEmptyTable": "没有数据",
            "sLengthMenu": "_MENU_ 件",
            "oPaginate": {
                "sPrevious": "上一件",
                "sNext": "下一件"
            }
        },
        "bFilter": false,
        "bSort": false,
        "sAjaxSource": "../mmb_point_record/pointRecord.json",
        "fnServerParams": function (aoData) {
            // 设置参数
            aoData.push( { "name": "memberId", "value": memberId } );
        },
        "aoColumns": [
            {
                "mData": "pointTime",
                "fnRender": function (rowData) {
                    if (rowData.aData.pointTime != null) {
                        return new Date(rowData.aData.pointTime).Format("yyyy-MM-dd hh:mm:ss");
                    } else {
                        return "";
                    }
                }
            },
            { "mData": "typeName"},
            { "mData": "point"},
            //{ "mData": "ruleName"},
            {
                "mData": "ruleName",
                "fnRender": function (rowData) {
                    rule=rowData.aData.ruleId
                    if (rule == '10') {
                        return "已生效";
                    } else if (rule == '20'){
                        return "未生效";
                    } else if (rule == '70'){
                        return "已消费";
                    } else {
                        return "";
                    }
                }
            }
        ]
    };
    pointTable = $('#record_table').dataTable(tableOption);
}

var modalSettings = {
    backdrop : 'static',
    keyboard : false
};
