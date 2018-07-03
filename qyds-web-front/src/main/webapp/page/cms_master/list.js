/**
 * Created by cky on 2016/7/18.
 */
var oTable;
$(document).ready(function() {

    // 获取栏目形式
    getComCode();

    initTable();

    // //获取商品类型码表数据
    // getOptionCode("GOODS_TYPE","type");
    // //获取是否上架 是否废除码表数据
    // getOptionCode("YES_NO","is_onsell");
    // //获取是否上架 是否废除码表数据
    // getOptionCode("MAINTAIN_STATUS","maintain_status");


    //绑定添加按钮的点击事件
    $("#addBtn").click(function() {
        sessionStorage.setItem("cmsId","");
        $('#content').load('cms_master/edit.html');
    });

    //检索按钮点击事件
    $("#btn_search").click(function(){
        //刷新Datatable，会自动激发retrieveData
        oTable.fnDraw();
    });

});


//表格js初始化
function initTable() {
    oTable = $('#cms_master_table').dataTable({
        "aoColumns": [
            { "mData": "itemName" },
            { "mData": "itemTypeName"},
            { "mData": "title" },
            { "mData": "textComment"},
            // { "mData": "listJson" },
            { "mData": "comment" },
            { "mData": "insertUserId" },
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
                    return '<a class="master_edit" href="#" onclick="master_edit(this);" id="' + oObj.aData.cmsId + '">详细</a>';
                }
            },
            {
                "mData": null,
                "fnRender": function ( oObj ) {
                    //电商
                    return '<a class="master_delete" onclick="master_del(this);"  id="' + oObj.aData.cmsId + '" href="javascript:;">删除</a> &nbsp;&nbsp; ';
                    }

            }

        ],
        "bAutoWidth": false,    //关闭自适应列宽，导致列表后半部空白。IE8下现象
        "bProcessing": false,
        "bServerSide": true,
        "bSort": false,
        "sAjaxSource": "../cms_master/getList.json",
        "fnServerParams": function ( aoData ) {
            aoData.push( { "name": "title", "value": $("#title").val() } );
            aoData.push( { "name": "itemType", "value": $("#item_type").val() } );
        },
        "fnServerData": fnServerData,
        "bFilter": false
    });

}

// 失败的回调方法
function errorFn() {
    showAlert('数据获取失败');
}

//点击详情按钮进行画面跳转
function master_edit(obj){
    var cmsId = obj.id;
    sessionStorage.setItem("cmsId",cmsId);
    $('#content').load('cms_master/edit.html');
}

//点击删除按钮
function master_del(obj){
    var cmsId = obj.id;
    showConfirm('确认删除该CMS数据吗?',function(){
        axse("/cms_master/delete.json", {"cmsId":cmsId}, delSuccessFn, errorFn);
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

//下架按钮点击事件
function master_unsell(obj){
    var goodsId = obj.id;
    showConfirm('确认要下架该商品吗?',function(){
        axse("/gds_master/master_unsell.json", {"goodsId":goodsId}, unsellSuccessFn, errorFn);
    });
}

//获得码表数据方法
function getComCode() {
    //品牌类型码表中的数据code为GDS_TYPE 每一个码表的code不一样
    axse("/common/getCodeList.json", {"data":"CMS_ITEM_TYPE"}, codeListSuccessFn, errorFn);
}

//获取码表数据成功的回调方法
function codeListSuccessFn(data) {
    if (data.resultCode == '00') {
        var array = data.data;
        if (array != null) {
            //为商品分类选择框添加数据
            $.each(array, function(index, item) {
                var $option = $('<option>').attr('value', item.value).text(item.displayCn);
                $("#item_type").append($option);
            });
        }
    }else{
        showAlert('码表数据获取失败');
    }
}