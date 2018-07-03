/**
 * Created by wy on 2016/8/3.
 */
var oRow;
var goodsId = sessionStorage.getItem("goodsId");
var sku = sessionStorage.getItem("sku");
var bankId = sessionStorage.getItem("bankId");

$(document).ready(function () {
    //获取码表数据
    getGoodsType("GOODS_TYPE", "type");
    getOptionCode("BANK_IO_TYPE", "bank_type");
    initTable();
    //检索按钮点击事件
    $("#btn_search").click(function () {
        //刷新Datatable，会自动激发retrieveData
        oTable.fnDraw();
    });
});
var oTable;
//表格js初始化
function initTable() {
    oTable = $('#bnk_master_table').dataTable({
        "aoColumns": [
            {"mData": "goodsTypeName"},
            {"mData": "gdsTypeName"},
            {"mData": "goodsCode"},
            {"mData": "erpGoodsCode"},
            {"mData": "sku"},
            {"mData": "erpSku"},
            {"mData": "bankTypeName"},
            {"mData": "inoutCount"},
            {
                "mData": null,
                "fnRender": function (oObj) {
                    return oObj.aData.inoutTime = new Date(oObj.aData.inoutTime).Format('yyyy-MM-dd');
                }
            },
            // {"mData": "comment"},
            // {"mData": "userName"},
            {
                "mData": null,
                "fnRender": function (oObj) {
                    return oObj.aData.insertTime = new Date(oObj.aData.insertTime).Format('yyyy-MM-dd');
                }
            },
        ],
        "bAutoWidth": false,    //关闭自适应列宽，导致列表后半部空白。IE8下现象
        "bProcessing": false,
        "bServerSide": true,
        "sAjaxSource": "../bnk_records/getList.json",
        "bSort": false,
        "fnServerParams": function (aoData) {
            aoData.push({"name": "goodsId", "value": goodsId});
            aoData.push({"name": "sku", "value": sku});
            aoData.push({"name":"erpStoreId","value": bankId});
            aoData.push({"name": "goodsType", "value": $("#goods_type").val()});
            aoData.push({"name": "bankType", "value": $("#bank_type").val()});
        },
        "fnServerData": fnServerData,
        "bFilter": false
    });
}
//获取码表数据-商品类型
function getGoodsType() {
    axse("/common/getCodeList.json", {"data": "GOODS_TYPE"}, goodsTypeSuccessFn, errorFn);
}
//获取码表数据-商品类型成功的回调方法
function goodsTypeSuccessFn(data) {
    if (data.resultCode == '00') {
        var array = data.data;
        if (array != null) {
            //为商品类型选择框添加数据
            $.each(array, function (index, item) {
                if (item.value != '30') {
                    var $option = $('<option>').attr('value', item.value).text(item.displayCn);
                    $("#goods_type").append($option);
                }
            });
        }
    } else {
        showAlert('数据获取失败');
    }
}
//返回前页面
function back() {
    $('#content').load('bnk_master/list.html');
}

