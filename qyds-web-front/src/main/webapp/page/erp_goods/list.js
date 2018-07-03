/**
 * Created by zlh on 2016/8/4.
 */
var oTable;
var oRow;
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
    oTable = $('#list_table').dataTable({
        "aoColumns": [
            { "mData": "sku"},
            { "mData": "goodsNameCn"},
            { "mData": "brandName"},
            { "mData": "lineName"},
            { "mData": "colorName"},
            { "mData": "sizeName"},
            { "mData": "price"},
            {
                "mData": null,
                "fnRender": function ( rowData ) {
                    return '<a class="edit" href="#" onclick=edit("' + rowData.aData.sku + '")>详细</a>';
                }
            }

        ],
        "bAutoWidth": false,    //关闭自适应列宽，导致列表后半部空白。IE8下现象
        "bProcessing": false,
        "bServerSide": true,
        "bSort": false,
        "sAjaxSource": "../erp_goods/getPage.json",
        "fnServerParams": function ( aoData ) {
            aoData.push( { "name": "sku", "value": $("#sku_in").val() } );
            aoData.push( { "name": "goodsNameCn", "value": $("#goods_name_cn_in").val() } );
            aoData.push( { "name": "brandName", "value": $("#brand_name_in").val() } );
            aoData.push( { "name": "lineName", "value": $("#line_name_in").val() } );
            aoData.push( { "name": "colorName", "value": $("#color_name_in").val() } );
            aoData.push( { "name": "sizeName", "value": $("#size_name_in").val() } );
        },
        "fnServerData": fnServerData,
        "bFilter": false
    });

}

var modalSettings = {
    backdrop : 'static',
    keyboard : false
};

//点击详情按钮
function edit(code){
    axse("/erp_goods/edit.json", {"id": code}, editSuccessFn, errorFn);
}

function editSuccessFn(data) {
    if (data.resultCode == '00') {
        $("#sku").val(data.data.sku);
        $("#goods_code").val(data.data.goodsCode);
        $("#goods_name_cn").val(data.data.goodsNameCn);
        $("#brand_name").val(data.data.brandName);
        $("#line_name").val(data.data.lineName);
        $("#color_name").val(data.data.colorName);
        $("#size_name").val(data.data.sizeName);
        $("#sell_year").val(data.data.sellYear);
        $("#season_name").val(data.data.seasonName);
        $("#price").val(data.data.price);
        $("#face").val(data.data.face);
        $("#material").val(data.data.material);
        $("#filler").val(data.data.filler);
        $("#editArea").modal();
        $("#editArea").modal(modalSettings);
        $("#editArea").modal('show');
    }
}

// 失败的回调方法
function errorFn() {
    showAlert('数据获取失败');
}
