/**
 * Created by zlh on 2016/8/4.
 */
var oTable;
var rTable;
var oRow;
$(document).ready(function() {

    initTable();

    initRTable();

    if (jQuery().datepicker) {
        $('.date-picker').datepicker({
            rtl: App.isRTL(),
            autoclose: true,
            //start_date: new Date()
        });
        $('body').removeClass("modal-open"); // fix bug when inline picker is used in modal
    }

    //检索按钮点击事件
    $("#btn_search").click(function(){
        //刷新Datatable，会自动激发retrieveData
        oTable.fnDraw();
    });
});


//表格js初始化
function initTable() {
    //alert(aoData);
    oTable = $('#list_table').dataTable({
        "aoColumns": [
            { "mData": "orderCode"},
            { "mData": "memberCode"},
            { "mData": "memberName"},
            { "mData": "storeCode"},
            { "mData": "storeName"},
            {
                "mData": "orderTime",
                "fnRender": function (rowData) {
                    if (rowData.aData.orderTime != null) {
                        return new Date(rowData.aData.orderTime).Format("yyyy-MM-dd hh:mm:ss");
                    } else {
                        return "";
                    }
                }
            },
            { "mData": "amount"},
            {
                "mData": null,
                "fnRender": function ( rowData ) {
                    return '<a class="record" href="#" onclick=record("' + rowData.aData.orderCode + '")>订单明细</a>';
                }
            }

        ],
        "bAutoWidth": false,    //关闭自适应列宽，导致列表后半部空白。IE8下现象
        "bProcessing": false,
        "bServerSide": true,
        "bSort": false,
        "sAjaxSource": "../erp_order/getMasterPage.json",
        "fnServerParams": function ( aoData ) {
            aoData.push( { "name": "orderCode", "value": $("#order_code_in").val() } );
            aoData.push( { "name": "memberCode", "value": $("#member_code_in").val() } );
            aoData.push( { "name": "memberName", "value": $("#member_name_in").val() } );
            aoData.push( { "name": "storeName", "value": $("#store_name_in").val() } );
        },
        "fnServerData": fnServerData,
        "bFilter": false
    });
}
//表格js初始化
function initRTable() {
    rTable = $('#record_table').dataTable({
        "aoColumns": [
            { "mData": "sku"},
            { "mData": "goodsName"},
            { "mData": "color"},
            { "mData": "size"},
            { "mData": "price"},
            { "mData": "count"},
            { "mData": "amount"}
        ],
        "bAutoWidth": false,    //关闭自适应列宽，导致列表后半部空白。IE8下现象
        "bProcessing": false,
        "bServerSide": true,
        "bSort": false,
        "sAjaxSource": "../erp_order/getSubPage.json",
        "fnServerParams": function ( aoData ) {
            aoData.push( { "name": "orderCode", "value": $("#order_code").val() } );
        },
        "fnServerData": fnServerData,
        "bFilter": false

    });
    //jQuery('#record_table_wrapper .dataTables_length select').css("margin-top","2px"); // modify table per page dropdown
}
var modalSettings = {
    backdrop : 'static',
    keyboard : false
};

//点击详情按钮
function record(orderCode){
    $("#order_code").val(orderCode);
    rTable.fnDraw();
    $("#editArea").modal();
    $("#editArea").modal(modalSettings);
    $("#editArea").modal('show');
}

function editSuccessFn(data) {
    if (data.resultCode == '00') {
        //$("#editArea").modal();
        //$("#editArea").modal(modalSettings);
        //$("#editArea").modal('show');
    }
}

// 失败的回调方法
function errorFn() {
    showAlert('数据获取失败');
}
