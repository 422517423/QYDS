/**
 * Created by congkeyan on 16/7/22.
 */

//树的初始化
var zTree = function(){
    return {
        init : function(){
            //获取码表数据
            getComCode();
        }
    }
}();

//获得码表数据方法
function getComCode() {
    //品牌类型码表中的数据code为GDS_TYPE 每一个码表的code不一样
    axse("/common/getCodeList.json", {"data":"GDS_TYPE"}, codeListSuccessFn, errorFn);
}

//获取码表数据成功的回调方法
function codeListSuccessFn(data) {
    if (data.resultCode == '00') {
        var array = data.data;
        if (array != null) {
            //为商品分类选择框添加数据
            $.each(array, function(index, item) {
                var $option = $('<option>').attr('value', item.value).text(item.displayCn);
                $("#type").append($option);
            });
        }
        //在这里发起详细信息的请求
        $('#type').bind('change',function(e) {
            //商品分类管理加载数据(前台变化)
            ajaxPost('/gds_type/getTreeList.json', {'type':$('#type').val()}, callback, successFn, failFn, null);
        });


        //商品分类管理加载数据(默认)
        ajaxPost('/gds_type/getTreeList.json', {'type':$('#type').val()}, callback, successFn, failFn, null);

    }else{
        showAlert('码表数据获取失败');
    }
}

// 失败的回调方法
function errorFn() {
    showAlert('数据获取失败');
}

//回调函数
function callback(data) {
    //服务器端返回的数据
    if($.type(data) == 'string' && data != ''){
        data = $.parseJSON(data);
    }
    //返回正确的时候的业务处理
    if(data.resultCode == '00'){
        if($.isArray(data.data)){
            //参数设定
            var settings = {
                view: {
                    //只能单选
                    selectedMulti: false
                },
                data: {
                    //画面显示项目
                    key: {
                        name: "goodsTypeNameCn"
                    },
                    simpleData: {
                        enable: true,
                        idKey: "goodsTypeId",
                        pIdKey: "goodsTypeIdParent",
                        name: "goodsTypeNameCn",
                        sort: "sort",
                        rootPId: 0
                    }
                },
                callback: {
                    onClick: function(event, treeId, treeNode){
                        $('#button-groups a').removeAttr('disabled');
                        if(treeNode.isParent){
                            $('#delBtn').attr('disabled','disabled');
                        }
                    },onRemove: function(event, treeId, treeNode){
                        var parentNode = treeNode.getParentNode();
                        var tree = $.fn.zTree.getZTreeObj(treeId);
                        var nodes = [];
                        if(parentNode == null){
                            nodes = resetSort(tree.getNodes());
                        }else{
                            nodes = resetSort(treeNode.getParentNode().children);
                        }
                        if(nodes.length > 0){
                            $.each(nodes,function(i,v){
                                tree.updateNode(v);
                            });
                        }
                    }
                }
            };
            //数据初始化
            $.fn.zTree.init($("#gdsTypeTree"),settings, data.data);
        }
    }else{
        //返回失败时候的业务处理
        showAlert('商品分类列表获取失败');
        $('#button-groups a').attr('disabled','disabled');
    }
}

function failFn(){
    //服务器异常处理
    showAlert('商品分类列表获取失败');
    $('#button-groups a').attr('disabled','disabled');
}

function successFn() {
    $('#button-groups a').not(':eq(0)').attr('disabled','disabled');
    //添加按钮注册事件
    $('#addBtn').on('click',function(){
        var tree = $.fn.zTree.getZTreeObj("gdsTypeTree");
        var selectNode = tree.getSelectedNodes();

        var sort = 0;

        //非第一层分类
        if(selectNode.length > 0){

            var nodes;
            if(selectNode[0] == null){
                nodes = tree.getNodes();
                if(typeof nodes != 'undefined'){
                    sort = nodes.length;
                }
            }else{
                nodes = selectNode[0].children;
                if(typeof nodes != 'undefined'){
                    sort = nodes.length;
                }
            }

            var goodsTypeNameCn = selectNode[0].goodsTypeNameCn;
            var goodsTypeId = selectNode[0].goodsTypeId;
            var type = selectNode[0].type;
            sessionStorage.setItem("sort",sort);
            sessionStorage.setItem("goodsTypeNameCn",goodsTypeNameCn);
            sessionStorage.setItem("goodsTypeId",goodsTypeId);
            sessionStorage.setItem("goodsTypeId",goodsTypeId);
            sessionStorage.setItem("type",type);
            //0代表在该节点上添加
            sessionStorage.setItem("goodsTypeAdd","0");
        }else{
            //顶级节点的时候设定为空
            sessionStorage.setItem("sort",sort);
            sessionStorage.setItem("goodsTypeNameCn","");
            sessionStorage.setItem("goodsTypeId","");
            sessionStorage.setItem("goodsTypeAdd","");
        }
        $('#content').load('gds_type/edit.html');

    });

    //编辑按钮
    $('#editBtn').on('click',function(){
        var tree = $.fn.zTree.getZTreeObj("gdsTypeTree");
        var selectNode = tree.getSelectedNodes();
        var goodsTypeNameCn = selectNode[0].goodsTypeNameCn;
        var goodsTypeId = selectNode[0].goodsTypeId;
        var sort = selectNode[0].sort;
        sessionStorage.setItem("goodsTypeNameCn",goodsTypeNameCn);
        sessionStorage.setItem("goodsTypeId",goodsTypeId);
        sessionStorage.setItem("sort",sort);
        //0代表在该节点上添加
        sessionStorage.setItem("goodsTypeAdd","1");
        $('#content').load('gds_type/edit.html');

    });


    //删除按钮
    $('#delBtn').on('click',function(){
        var tree = $.fn.zTree.getZTreeObj("gdsTypeTree");
        var selectNode = tree.getSelectedNodes();
        var goodsTypeId = selectNode[0].goodsTypeId;
        showConfirm('确认删除该分类吗?',function(){
            ajaxPost('/gds_type/delete.json',
                {'goodsTypeId':goodsTypeId},function(data){
                    if($.type(data) == 'string' && data != ''){
                        data = $.parseJSON(data);
                    }

                    if(data.resultCode == '00'){
                        tree.removeNode(selectNode[0],true);
                    }else{
                        showAlert("分类删除失败");
                    }
                },null,null,null);
        });
    });

    //上移按钮
    $('#upBtn').on('click',function(){
        var tree = $.fn.zTree.getZTreeObj("gdsTypeTree");
        var selectNode = tree.getSelectedNodes();
        var node = selectNode[0].getPreNode();

        var data = new Array();
        data.push({goodsTypeId: selectNode[0].goodsTypeId,sort: node.sort});
        data.push({goodsTypeId: node.goodsTypeId,sort: selectNode[0].sort});
        console.log("data === " +data);
        ajaxPost('/gds_type/resort.json',
            {'data':JSON.stringify(data)},function (data) {
                if($.type(data) == 'string' && data != ''){
                    data = $.parseJSON(data);
                }

                if(data.resultCode == '00'){
                    tree.moveNode(node,selectNode[0],'prev');
                    var temp = selectNode[0].sort;
                    selectNode[0].sort = node.sort;
                    node.sort = temp;
                    tree.updateNode(selectNode[0]);
                    tree.updateNode(node);
                    tree.cancelSelectedNode(selectNode[0]);
                    $('#button-groups a').not(':eq(0)').attr('disabled','disabled');

                }
            },null,null,null);
    });

    //下移按钮
    $('#downBtn').on('click',function(){
        var tree = $.fn.zTree.getZTreeObj("gdsTypeTree");
        var selectNode = tree.getSelectedNodes();
        var node = selectNode[0].getNextNode();

        var data = new Array();
        data.push({goodsTypeId: selectNode[0].goodsTypeId,sort: node.sort});
        data.push({goodsTypeId: node.goodsTypeId,sort: selectNode[0].sort});
        console.log("data === " +data);
        ajaxPost('/gds_type/resort.json',{'data':JSON.stringify(data)},function (data) {
            if($.type(data) == 'string' && data != ''){
                data = $.parseJSON(data);
            }

            if(data.resultCode == '00'){
                tree.moveNode(node,selectNode[0],'next');
                var temp = selectNode[0].sort;
                selectNode[0].sort = node.sort;
                node.sort = temp;
                tree.updateNode(selectNode[0]);
                tree.updateNode(node);
                tree.cancelSelectedNode(selectNode[0]);
                $('#button-groups a').not(':eq(0)').attr('disabled','disabled');

            }
        },null,null,null);

    });

}
