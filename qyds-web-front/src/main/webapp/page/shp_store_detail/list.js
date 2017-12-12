/**
 * Created by wy on 2016/7/18.
 */
var oTable;

$(document).ready(function() {

    initTable();

    //绑定添加按钮的点击事件
    $("#addBtn").click(function() {
        sessionStorage.setItem("","");
        $('#content').load('shp_stroe_detail/edit.html');
    });

    //检索按钮点击事件
    $("#btn_search").click(function(){
        //刷新Datatable，会自动激发retrieveData
        oTable.fnDraw();
    });
});
//表格js初始化
function initTable() {
    oTable = $('#shp_stroe_table').dataTable({
        "aoColumns": [
            { "mData": "searchKey"},
            { "mData": "imageUrl"},
            { "mData": "contactor" },
            { "mData": "phone" },
            { "mData": "districtidProvince" },
            { "mData": "districtidCity" },
            { "mData": "districtidDistrict" },
            { "mData": "address" },
            { "mData": "longitude" },
            { "mData": "latitude" },
            { "mData": "introduceHtml" },
            { "mData": "userName" },
            {
                "mData": null,
                "fnRender": function (oObj) {
                    return oObj.aData.insertTime = new Date(oObj.aData.insertTime).Format('yyyy-MM-dd');
                }
            },
            {
                "mData": null,
                "fnRender": function ( oObj ) {
                    return '<a class="brand_edit" href="#" onclick="shp_stroe_detail(this);" id="' + oObj.aData.orgId + '">详细</a>';
                }
            },
            {
                "mData": null,
                "fnRender": function ( oObj ) {
                    return '<a class="brand_delete" onclick="shp_stroe_del(this);"  id="' + oObj.aData.orgId + '" href="javascript:;">删除</a>';
                }
            }

        ],
        "bAutoWidth": false,    //关闭自适应列宽，导致列表后半部空白。IE8下现象
        "bProcessing": false,
        "bServerSide": true,
        "sAjaxSource": "../shp_stroe_detail/getList.json",
        "bSort": false,
        "fnServerParams": function ( aoData ) {
            aoData.push( { "name": "brandName", "value": $("#").val() } );
            aoData.push( { "name": "type", "value": $("#").val() } );
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
function shp_stroe_detail(obj){

    var orgId = obj.id;
    console.log(orgId);
    sessionStorage.setItem("orgId",orgId);
    $('#content').load('shp_store_detail/edit.html');
}

//点击删除按钮
function shp_stroe_del(obj){
    var orgId = obj.id;
    showConfirm('确认删除该品牌吗?',function(){
        axse("/shp_store_detail/delete.json", {"orgId":orgId}, delSuccessFn, errorFn);
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