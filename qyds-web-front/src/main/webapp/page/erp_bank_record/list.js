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

    //检索按钮点击事件
    $("#btn_record").click(function(){
        //刷新Datatable，会自动激发retrieveData
        rTable.fnDraw();
    });

});


//表格js初始化
function initTable() {
    //alert(aoData);
    oTable = $('#list_table').dataTable({
        "aoColumns": [
            { "mData": "erpSku"},
            { "mData": "goodsNameCn"},
            { "mData": "colorName"},
            { "mData": "sizeName"},
            { "mData": "storeNameCn"},
            { "mData": "inoutCount"},
            {
                "mData": null,
                "fnRender": function ( rowData ) {
                    return '<a class="record" href="#" onclick=record("' + rowData.aData.erpSku + '","' + rowData.aData.erpStoreid + '")>库存记录</a>';
                }
            }

        ],
        "bAutoWidth": false,    //关闭自适应列宽，导致列表后半部空白。IE8下现象
        "bProcessing": false,
        "bServerSide": true,
        "bSort": false,
        "sAjaxSource": "../erp_bank_record/getSumPage.json",
        "fnServerParams": function ( aoData ) {
            aoData.push( { "name": "erpSku", "value": $("#erp_sku_in").val() } );
            aoData.push( { "name": "goodsNameCn", "value": $("#goods_name_cn_in").val() } );
            aoData.push( { "name": "colorName", "value": $("#color_name_in").val() } );
            aoData.push( { "name": "sizeName", "value": $("#size_name_in").val() } );
            aoData.push( { "name": "storeNameCn", "value": $("#store_name_cn_in").val() } );
        },
        "fnServerData": fnServerData,
        "bFilter": false
    });
}
//表格js初始化
function initRTable() {
    rTable = $('#record_table').dataTable({
        "aoColumns": [
            {
                "mData": "inoutTime",
                "fnRender": function (rowData) {
                    if (rowData.aData.inoutTime != null) {
                        return new Date(rowData.aData.inoutTime).Format("yyyy-MM-dd hh:mm:ss");
                    } else {
                        return "";
                    }
                }
            },
            { "mData": "inoutCount"}
        ],
        "bAutoWidth": false,    //关闭自适应列宽，导致列表后半部空白。IE8下现象
        "bProcessing": false,
        "bServerSide": true,
        "bSort": false,
        "sAjaxSource": "../erp_bank_record/getRecordPage.json",
        "fnServerParams": function ( aoData ) {
            aoData.push( { "name": "erpSku", "value": $("#erp_sku_record").val() } );
            aoData.push( { "name": "erpStoreid", "value": $("#erp_storeid_record").val() } );
            aoData.push( { "name": "startDate", "value": $("#start_date").val() } );
            aoData.push( { "name": "endDate", "value": $("#end_date").val() } );
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
function record(sku,storeid){
    $("#erp_sku_record").val(sku);
    $("#erp_storeid_record").val(storeid);
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
