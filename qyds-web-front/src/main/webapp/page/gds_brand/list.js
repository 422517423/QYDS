/**
 * Created by cky on 2016/7/18.
 */
var oTable;

$(document).ready(function() {

    initTable();

    //获取码表数据
    getOptionCode("GDS_BRAND_TYPE","type");
    //绑定添加按钮的点击事件
    $("#addBtn").click(function() {
        sessionStorage.setItem("brandId","");
        $('#content').load('gds_brand/edit.html');
    });

    //检索按钮点击事件
    $("#btn_search").click(function(){
        //刷新Datatable，会自动激发retrieveData
        oTable.fnDraw();
    });

});

//表格js初始化
function initTable() {
    oTable = $('#gds_brand_table').dataTable({

        "aoColumns": [
            { "mData": "typeName"},
            { "mData": "brandCode"},
            { "mData": "erpBrandCode" },
            { "mData": "brandName" },
            { "mData": "loginUserName" },
            {
                "mData": "insertTime",
                "fnRender": function (rowData) {
                    if (rowData.aData.insertTime != null) {
                        return new Date(rowData.aData.insertTime).Format("yyyy-MM-dd hh:mm:ss");
                    } else {
                        return "";
                    }
                }
            },
            {
                "mData": null,
                "fnRender": function ( oObj ) {
                    return '<a class="brand_edit" href="#" onclick="brand_edit(this);" id="' + oObj.aData.brandId + '">详细</a>';
                }
            },
            {
                "mData": null,
                "fnRender": function ( oObj ) {
                    return '<a class="brand_delete" onclick="brand_del(this);"  id="' + oObj.aData.brandId + '" href="javascript:;">删除</a>';
                }
            }

        ],
        "bAutoWidth": false,    //关闭自适应列宽，导致列表后半部空白。IE8下现象
        "bProcessing": false,
        "bServerSide": true,
        "sAjaxSource": "../gds_brand/getList.json",
        "bSort": false,
        "fnServerParams": function ( aoData ) {
            aoData.push( { "name": "brandName", "value": $("#brand_name").val() } );
            aoData.push( { "name": "type", "value": $("#type").val() } );
        },
        "fnServerData": fnServerData,
        "bFilter": false
    });
}

//获得码表数据方法
function getComCode() {
    //品牌类型码表中的数据code为GDS_BRAND_TYPE 每一个码表的code不一样
    axse("/common/getCodeList.json", {"data":"GDS_BRAND_TYPE"}, codeListSuccessFn, errorFn);
}

// 失败的回调方法
function errorFn() {
    showAlert('数据获取失败');
}

//点击详情按钮进行画面跳转
function brand_edit(obj){
    var brandId = obj.id;
    sessionStorage.setItem("brandId",brandId);
    $('#content').load('gds_brand/edit.html');
}

//点击删除按钮
function brand_del(obj){
    var brandId = obj.id;
    showConfirm('确认删除该品牌吗?',function(){
        axse("/gds_brand/delete.json", {"brandId":brandId}, delSuccessFn, errorFn);
    });

}

//删除方法的回调函数
function delSuccessFn(data){
    if (data.resultCode == '00') {
        //刷新Datatable，会自动激发retrieveData
        oTable.fnDraw();
    }else{
        showAlert('删除失败');
    }
}