/**
 * Created by panda on 16/9/26.
 */
var oTable;
$(document).ready(function() {
    initTable();

    //检索按钮点击事件
    $("#btn_search").click(function(){
        //刷新Datatable，会自动激发retrieveData
        oTable.fnDraw();
    });
});

//表格js初始化
function initTable() {
    oTable = $('#gds_schedule').dataTable({
        "aoColumns": [
            { "mData": "goodsName" },
            { "mData": "goodsCode" },
            { "mData": "userName" },
            { "mData": "telephone"},
            { "mData": "comment"},
            {
                "mData": "insertTime",
                "fnRender": function (rowData) {
                    if (rowData.aData.insertTime != null) {
                        return new Date(rowData.aData.insertTime).Format("yyyy-MM-dd hh:mm:ss");
                    } else {
                        return "";
                    }
                }
            }
        ],
        "bAutoWidth": false,    //关闭自适应列宽，导致列表后半部空白。IE8下现象
        "bProcessing": false,
        "bServerSide": true,
        "bSort": false,
        "sAjaxSource": "../gds_master/getGoodsOrder.json",
        "fnServerParams": function ( aoData ) {
            aoData.push( { "name": "goodsName", "value": $("#goods_name").val() } );
            aoData.push( { "name": "userName", "value": $("#user_name").val() } );
            aoData.push( { "name": "telephone", "value": $("#telephone").val() } );
        },
        "fnServerData": fnServerData,
        "bFilter": false
    });
}