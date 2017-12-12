/**
 * Created by YiLian on 2016/7/18.
 */
var table;
$(document).ready(function () {

    initTable();

    //检索按钮点击事件
    $("#btn_search").click(function () {
        //刷新Datatable，会自动激发retrieveData
        table.fnDraw();
    });
});

function initTable() {
    var tableOption = {
        "bProcessing": true,
        "bServerSide": true,
        "fnServerData": fnServerData,
        "bFilter": false,
        "bSort": false,
        "sAjaxSource": "../mmb_contact/getList.json",
        "fnServerParams": function (aoData) {
            // 设置参数
            aoData.push({"name": "telephone", "value": $("#telephone").val()});
            aoData.push({"name": "theme", "value": $("#theme").val()});
        },
        "aoColumns": [
            {"mData": "theme"},
            {"mData": "comment"},
            {"mData": "userName"},
            {"mData": "telephone"},
            {"mData": "address"}
        ]
    };
    table = $('#mmb_contact_table').dataTable(tableOption);
}