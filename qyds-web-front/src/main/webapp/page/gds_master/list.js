/**
 * Created by cky on 2016/7/18.
 */
var goodsStatus_dashboard = sessionStorage.getItem("goodsStatus_dashboard");
var oTable;
$(document).ready(function() {

    //获取商品类型码表数据
    getOptionCode("GOODS_TYPE","type");
    //获取是否上架 是否废除码表数据
    getOptionCode("MAINTAIN_STATUS","maintain_status");
    //获取是否上架 是否废除码表数据
    getOptionCode("YES_NO","is_onsell", function(flag){
        if(flag){
            if(goodsStatus_dashboard == '10'){
                $("#is_onsell").val('0');
                sessionStorage.removeItem("goodsStatus_dashboard");
            }
        }
        initTable();
    });


    //绑定添加按钮的点击事件
    $("#addBtn").click(function() {
        sessionStorage.setItem("goodsId","");
        sessionStorage.setItem("goodsIds","");
        $('#content').load('gds_master/edit.html');
    });

    //检索按钮点击事件
    $("#btn_search").click(function(){
        //刷新Datatable，会自动激发retrieveData
        sessionStorage.setItem("goods_list_page",0);
        setSessionValue();
        oTable.fnDraw();
    });

    $("#save_btn").click(function() {

        var goodsId = sessionStorage.getItem("goodsId_sort");
        var sort = $("#sort").val();
        if(sort == null || sort == 0){
            showAlert("请正确输入序号!")
            return;
        }

        var json = {};
        json.goodsId = goodsId;
        json.sort = sort;
        axse("/gds_master/sortSave.json", {'data':JSON.stringify(json)}, saveSuccessFn, errorFn);
    });

});


//表格js初始化
function initTable() {
    setQuery();


    oTable = $('#gds_master_table').dataTable({
        "aoColumns": [
            { "mData": "goodsId" },
            { "mData": "typeName" },
            { "mData": "brandName"},
            //{   "mData": null,
            //    "fnRender": function ( oObj ) {
            //        if(oObj.aData.erpBrandName != null && oObj.aData.erpBrandName != ""){
            //            return oObj.aData.erpBrandName;
            //        }else{
            //            return oObj.aData.erpStyleNo;
            //        }
            //    }
            //},
            { "mData": "goodsTypeName" },
            { "mData": "goodsCode"},
            { "mData": "goodsName" },
            { "mData": "erpGoodsCode"},
            { "mData": "erpGoodsName" },
            { "mData": "maintainStatusName" },
            {   "mData": null,
                "fnRender": function ( oObj ) {
                    if(oObj.aData.isOnsell == "0"){
                        return "是";
                    }else{
                        return "否";
                    }
                }
            },
            //{   "mData": null,
            //    "fnRender": function ( oObj ) {
            //        if(oObj.aData.isWaste == "0"){
            //            return "是";
            //        }else{
            //            return "否";
            //        }
            //    }
            //},
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
            { "mData": "sort" },
            {
                "mData": null,
                "fnRender": function ( oObj ) {
                    if(oObj.aData.isOnsell == "0"){
                        return '<a class="master_edit" href="#" onclick="master_edit(this);" id="' + oObj.aData.goodsId + '">详细</a>'
                            + '&nbsp;&nbsp;<a class="master_sort" href="#" onclick="master_sort(this);" id="' + oObj.aData.goodsId + '">序号</a>'
                            + (oObj.aData.type == '10'?' &nbsp;&nbsp; <a class="erp_sku_list" href="#" onclick=erp_sku_list("'+oObj.aData.erpGoodsCode+'")>SKU详细</a>':'');
                    }else{
                        return '<a class="master_edit" href="#" onclick="master_edit(this);" id="' + oObj.aData.goodsId + '">详细</a>'
                            + (oObj.aData.type == '10'?' &nbsp;&nbsp; <a class="erp_sku_list" href="#" onclick=erp_sku_list("'+oObj.aData.erpGoodsCode+'")>SKU详细</a>':'');
                    }


                }
            },
            {
                "mData": null,
                "fnRender": function ( oObj ) {

                    //电商
                    if(oObj.aData.type == '20'){
                        //维护状态在未发布的时候有发布按钮
                        if(oObj.aData.maintainStatus == '20'){
                            return '<a class="master_delete" onclick="master_del(this);"  id="' + oObj.aData.goodsId + '" href="javascript:;">删除</a> &nbsp;&nbsp; <a class="master_edit" href="#" onclick="master_sku(this);" id="' + oObj.aData.goodsId + '">sku配置</a>&nbsp;&nbsp; <a class="public_goods" href="#" onclick="public_goods(this);" id="' + oObj.aData.goodsId + '">发布</a>';
                        }else if(oObj.aData.maintainStatus == '30'){
                            if(oObj.aData.isOnsell == '1'){
                                return '<a class="master_delete" onclick="master_del(this);"  id="' + oObj.aData.goodsId + '" href="javascript:;">删除</a> &nbsp;&nbsp; <a class="master_edit" href="#" onclick="master_sku(this);" id="' + oObj.aData.goodsId + '">sku配置</a>&nbsp;&nbsp; <a class="master_sell" href="#" onclick="master_sell(this);" id="' + oObj.aData.goodsId + '">上架</a>'
                            }else{
                                return ' <a class="master_edit" href="#" onclick="master_sku(this);" id="' + oObj.aData.goodsId + '">sku配置</a>&nbsp;&nbsp; <a class="master_sell" href="#" onclick="master_sell(this);" id="' + oObj.aData.goodsId + '">上架</a>&nbsp;&nbsp;<a class="master_sell" href="#" onclick="master_unsell(this);" id="' + oObj.aData.goodsId + '">下架</a>'
                            }

                        }else{
                            return '<a class="master_delete" onclick="master_del(this);"  id="' + oObj.aData.goodsId + '" href="javascript:;">删除</a> &nbsp;&nbsp; <a class="master_edit" href="#" onclick="master_sku(this);" id="' + oObj.aData.goodsId + '">sku配置</a>'
                        }
                    }else if(oObj.aData.type == '30'){
                        //套装
                        if(oObj.aData.maintainStatus == '20'){
                            return '<a class="master_delete" onclick="master_del(this);"  id="' + oObj.aData.goodsId + '" href="javascript:;">删除</a>&nbsp;&nbsp; <a class="public_goods" href="#" onclick="public_goods(this);" id="' + oObj.aData.goodsId + '">发布</a>';
                        }else if(oObj.aData.maintainStatus == '30'){
                            if(oObj.aData.isOnsell == '1'){
                                return '<a class="master_delete" onclick="master_del(this);"  id="' + oObj.aData.goodsId + '" href="javascript:;">删除</a>&nbsp;&nbsp; <a class="master_sell" href="#" onclick="master_sell(this);" id="' + oObj.aData.goodsId + '">上架</a>';
                            }else{
                                return '<a class="master_sell" href="#" onclick="master_sell(this);" id="' + oObj.aData.goodsId + '">上架</a>&nbsp;&nbsp;<a class="master_sell" href="#" onclick="master_unsell(this);" id="' + oObj.aData.goodsId + '">下架</a>';
                            }

                        }else{
                            return '<a class="master_delete" onclick="master_del(this);"  id="' + oObj.aData.goodsId + '" href="javascript:;">删除</a>';
                        }

                    }else {
                        //ERP
                        if(oObj.aData.maintainStatus == '20'){
                            return '<a class="public_goods" href="#" onclick="public_goods(this);" id="' + oObj.aData.goodsId + '">发布</a>&nbsp;&nbsp; <a class="master_erp_sku" href="#" onclick="master_erp_sku(this);" id="' + oObj.aData.goodsId + '">颜色配置</a>';
                        }else if(oObj.aData.maintainStatus == '30'){
                            if(oObj.aData.isOnsell == '1'){
                                return '<a class="master_erp_sku" href="#" onclick="master_erp_sku(this);" id="' + oObj.aData.goodsId + '">颜色配置</a>&nbsp;&nbsp; <a class="master_sell" href="#" onclick="master_sell(this);" id="' + oObj.aData.goodsId + '">上架</a>';
                            }else{
                                return '<a class="master_erp_sku" href="#" onclick="master_erp_sku(this);" id="' + oObj.aData.goodsId + '">颜色配置</a>&nbsp;&nbsp; <a class="master_sell" href="#" onclick="master_sell(this);" id="' + oObj.aData.goodsId + '">上架</a>&nbsp;&nbsp;<a class="master_sell" href="#" onclick="master_unsell(this);" id="' + oObj.aData.goodsId + '">下架</a>';
                            }


                        }else{
                            return '';
                        }
                    }

                },
                "bUseRendered": false
            }

        ],
        "bAutoWidth": false,    //关闭自适应列宽，导致列表后半部空白。IE8下现象
        "bProcessing": false,
        "bServerSide": true,
        "bSort": false,
        "sAjaxSource": "../gds_master/getList.json",
        "fnServerParams": function ( aoData ) {
            aoData.push( { "name": "goodsName", "value": $("#goods_name").val() } );
            aoData.push( { "name": "type", "value": $("#type").val() } );
            aoData.push( { "name": "isOnsell", "value": $("#is_onsell").val() } );
            aoData.push( { "name": "maintainStatus", "value": $("#maintain_status").val() } );
            aoData.push( { "name": "goodsCode", "value": $("#goods_code").val() } );

        },
        "fnServerData": fnServerData,
        "bFilter": false,
        "showRedirect": true,
        "fnDrawCallback": function(){
             var oTable = $("#gds_master_table").dataTable();
             $('#redirect').keyup(function(e){
                 if($(this).val() && $(this).val()>0){
                     var redirectpage = $(this).val()-1;
                 }else{
                     var redirectpage = 0;
                 }
                 oTable.fnPageChange(redirectpage);
             });
            // var lastPage = sessionStorage.getItem("goods_list_page");
            // if(lastPage&&lastPage!=0){
            //     sessionStorage.setItem("goods_list_page",0);
            //     oTable.fnPageChange(parseInt(lastPage));
            // }
        }
    });

}

// 失败的回调方法
function errorFn() {
    showAlert('数据获取失败');
}

//点击详情按钮进行画面跳转
function master_edit(obj){
    setSessionValue();

    var goodsId = obj.id;
    sessionStorage.setItem("goods_list_page",oTable.fnPagingInfo().iPage);
    sessionStorage.setItem("goodsId",goodsId);
    sessionStorage.setItem("goodsIds","");
    $('#content').load('gds_master/edit.html');
}

//点击编辑序号的按钮修改事件
function master_sort(obj){
    setSessionValue();

    var goodsId = obj.id;
    $("#sortEdit").modal();
    $("#sortEdit").modal(modalSettings);
    $("#sortEdit").modal('show');
    sessionStorage.setItem("goodsId_sort",goodsId);
}

//点击SKU详情按钮进行画面跳转
function erp_sku_list(code){
    setSessionValue();

    sessionStorage.setItem("goods_list_page",oTable.fnPagingInfo().iPage);
    sessionStorage.setItem("goodsCode",code);
    $('#content').load('gds_master/erp_sku_list.html');
}

//点击删除按钮
function master_del(obj){
    var goodsId = obj.id;
    showConfirm('确认删除该商品吗?',function(){
        axse("/gds_master/delete.json", {"goodsId":goodsId}, delSuccessFn, errorFn);
    });

}

//发布按钮的点击事件改变维护状态
function public_goods(obj){
    var goodsId = obj.id;
    showConfirm('确认发布商品吗?',function(){
        axse("/gds_master/publicGoods.json", {"goodsId":goodsId}, publicSuccessFn, errorFn);
    });

}


//点击sku配置画面跳转
function master_sku(obj){
    var goodsId = obj.id;
    sessionStorage.setItem("goodsId",goodsId);
    $('#content').load('gds_master/sku_edit.html');
}

//ERP商品的场合点击sku配置画面跳转
function master_erp_sku(obj){
    var goodsId = obj.id;
    sessionStorage.setItem("goodsId",goodsId);
    $('#content').load('gds_master/erp_sku_edit.html');
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

//重新排序
function saveSuccessFn(data){
    $("#sortEdit").modal('hide');
    if (data.resultCode == '00') {
        //刷新Datatable，会自动激发retrieveData
        oTable.fnDraw();
    }else{
        showAlert('保存失败');
    }
}

//发布方法的回调函数
function publicSuccessFn(data){
    if (data.resultCode == '00') {
        //刷新Datatable，会自动激发retrieveData
        oTable.fnDraw();
    }else{
        showAlert('发布失败');
    }
}

//上架画面跳转
function master_sell(obj){
    var goodsId = obj.id;
    sessionStorage.setItem("goodsId",goodsId);
    $('#content').load('gds_master/master_sell.html');
}


//下架按钮点击事件
function master_unsell(obj){
    var goodsId = obj.id;
    showConfirm('确认要下架该商品吗?',function(){
        axse("/gds_master/master_unsell.json", {"goodsId":goodsId}, unsellSuccessFn, errorFn);
    });
}

//下架方法的回调函数
function unsellSuccessFn(data){
    if (data.resultCode == '00') {
        //刷新Datatable，会自动激发retrieveData
        oTable.fnDraw();
    }else{
        showAlert('下架失败');
    }
}

function setSessionValue(){
    //搜索框的值
    sessionStorage.setItem("goods_name",$('#goods_name').val());
    sessionStorage.setItem("type",$('#type').val());
    sessionStorage.setItem("is_onsell",$('#is_onsell').val());
    sessionStorage.setItem("maintain_status",$('#maintain_status').val());
    sessionStorage.setItem("goods_code",$('#goods_code').val());


//
//     sessionStorage.setItem("sEcho",oTable.fnSettings()._iDisplayStart);
//    sessionStorage.setItem("iDisplayLength",oTable.fnSettings()._iDisplayLength);

}

function setQuery(){
    var goods_name = sessionStorage.getItem("goods_name");
    if(goods_name){
        $('#goods_name').val(goods_name);
    }

    var type = sessionStorage.getItem("type");
    if(type){
        $('#type option[value="'+type+'"]').attr('selected','selected');
    }

    var is_onsell = sessionStorage.getItem("is_onsell");
    if(is_onsell){
        $('#is_onsell option[value="'+is_onsell+'"]').attr('selected','selected');
    }

    var maintain_status = sessionStorage.getItem("maintain_status");
    if(maintain_status){
        $('#maintain_status option[value="'+maintain_status+'"]').attr('selected','selected');
    }

    var goods_code = sessionStorage.getItem("goods_code");
    if(goods_code){
        $('#goods_code').val(goods_code);
    }

    //var iDisplayStart = sessionStorage.getItem("iDisplayStart");

}
