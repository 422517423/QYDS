/**
 * Created by YiLian on 2016/7/18.
 */
var table;
var memberId = sessionStorage.getItem("memberId");
$(document).ready(function () {
    initTable();

    //检索按钮点击事件
    $("#btn_search").click(function () {
        table.fnDraw();
    });

    $('.date-picker').datepicker({
        rtl: App.isRTL(),
        autoclose: true
    });
});

function initTable() {
    var tableOption = {
        "bProcessing": true,
        "bServerSide": true,
        "fnServerData": fnServerData,
        "bFilter": false,
        "bSort": false,
        "sAjaxSource": "../mmb_point_record/getList.json",
        "fnServerParams": function (aoData) {
            // 设置参数
            aoData.push({"name": "memberId", "value": memberId});
            aoData.push({"name": "pointTimeStart", "value": $("#pointTimeStart").val()});
            aoData.push({"name": "pointTimeEnd", "value": $("#pointTimeEnd").val()});
        },
        "aoColumns": [
            {"mData": "typeName"},
            {"mData": "point"},
            {"mData": "pointTime"},
            {"mData": "scoreSource"}
        ]
    };
    table = $('#mmb_point_record_table').dataTable(tableOption);
}

