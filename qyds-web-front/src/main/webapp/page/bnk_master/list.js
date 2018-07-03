/**
 * Created by wy on 2016/8/2.
 */
var oRow;
var userId = sessionStorage.getItem("userId");
$(document).ready(function () {
    initTable();
    geterpStore();
    // //获取码表数据
    getGoodsType("GOODS_TYPE", "type");
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
            // {"mData": "gdsTypeName"},
            {"mData": "goodsCode"},
            //{"mData": "erpGoodsCode"},
            {"mData": "sku"},
            {"mData": "erpStoreId"},
            //{"mData": "erpSku"},
            // {"mData": "bankTypeName"},
            {"mData": "newCount"},
            {"mData": "lastCount"},
            // {"mData": "comment"},
            // {"mData": "userName"},
            //{
            //    "mData": null,
            //    "fnRender": function (oObj) {
            //        return oObj.aData.insertTime = new Date(oObj.aData.insertTime).Format('yyyy-MM-dd');
            //    }
            //},
            {
                "mData": null,
                "fnRender": function (oObj) {
                    return oObj.aData.updateTime = new Date(oObj.aData.updateTime).Format('yyyy-MM-dd');
                }
            },
            {
                "mData": null,
                "fnRender": function (oObj) {
                    return '<a onclick=bnk_master("' + oObj.aData.goodsId + '","' + oObj.aData.sku + '","' + oObj.aData.bankId + '")>商品履历</a>&nbsp;&nbsp;<a onclick=bnk_master_change("' + oObj.aData.bankId + '")>库存修改</a>';
                }
            },
        ],
        "bAutoWidth": false,    //关闭自适应列宽，导致列表后半部空白。IE8下现象
        "bProcessing": false,
        "bServerSide": true,
        "sAjaxSource": "../bnk_master/getList.json",
        "bSort": false,
        "fnServerParams": function (aoData) {
            aoData.push({"name": "goodsType", "value": $("#goods_type").val()});
            aoData.push({"name": "sku", "value": $("#sku").val()});
            aoData.push({"name": "erpStoreId", "value": $("#erpStoreId").val()});
        },
        "fnServerData": fnServerData,
        "bFilter": false
    });
}
function bnk_master_change(bankId) {
    sessionStorage.setItem("bankId", bankId);
    $('#customDialog').load('bnk_master/edit.html');
}
//点击商品履历画面跳转
function bnk_master(goodsId, sku,bankId) {
    sessionStorage.setItem("goodsId", goodsId);
    sessionStorage.setItem("sku", sku);
    sessionStorage.setItem("bankId", bankId);
    $('#content').load('bnk_records/list.html');
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
//门店
function geterpStore() {
    axse("/shp_org/getOrg.json", {}, function(data){
        if (data.resultCode == '00') {
            var array = data.data;
            if (array != null) {
                //为商品类型选择框添加数据
                $.each(array, function (index, item) {
                    if (item.value != '30') {
                        var $option = $('<option>').attr('value', item.erpStoreId).text(item.orgName);
                        $("#erpStoreId").append($option);
                    }
                });
            }
        } else {
            showAlert('数据获取失败');
        }
    }, errorFn);
}
