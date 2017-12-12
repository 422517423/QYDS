/**
 * Created by panda on 16/6/23.
 */
var zTree = function(){
    return {
        init : function(){
            var userId = sessionStorage.getItem("userId");

            $('#alertMsg').hide();

            function callback(data) {
                if($.type(data) == 'string' && data != ''){
                    data = $.parseJSON(data);
                }

                if(data.resultCode == '00'){
                    if($.isArray(data.data)){

                        var settings = {
                            view: {
                                selectedMulti: false
                            },
                            data: {
                                key: {
                                    name: "itemName"
                                },
                                simpleData: {
                                    enable: true,
                                    idKey: "itemId",
                                    pIdKey: "itemIdParent",
                                    sort: "sort",
                                    itemType: "itemType",
                                    itemName: "itemName",
                                    itemNameParent: "itemNameParent",
                                    itmeFullName: "itmeFullName"
                                }
                            },
                            callback: {
                                onClick: function(event, treeId, treeNode){
                                    $('#button-groups a').removeAttr('disabled');
                                    if(treeNode.isParent){
                                        $('#delBtn').attr('disabled','disabled');
                                    }

                                    if(treeNode.isFirstNode){
                                        $('#upBtn').attr('disabled','disabled');
                                    }

                                    if(treeNode.isLastNode){
                                        $('#downBtn').attr('disabled','disabled');
                                    }
                                },
                                
                                onRemove: function(event, treeId, treeNode){
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
                        $.fn.zTree.init($("#cms_tiems_list"),settings, data.data);
                    }
                }else{
                    $('#alertMsg').show();
                    $('#button-groups a').attr('disabled','disabled');
                }
            }

            function failFn(){
                $('#alertMsg').show();
                $('#button-groups a').attr('disabled','disabled');
            }

            function successFn() {
                var iconPicker = null;
                //按钮初始化
                $('#button-groups a').not(':eq(0)').attr('disabled','disabled');

                var modalSettings = {
                    backdrop : 'static',
                    keyboard : false
                };

                if (jQuery().datepicker) {
                    $('.date-picker').datepicker({
                        rtl: App.isRTL(),
                        autoclose: true,
                        startDate: new Date()
                    });
                    $('body').removeClass("modal-open"); // fix bug when inline picker is used in modal
                }

                //添加按钮注册事件
                $('#add_cms_items_btn').on('click',function(){
                    var tree = $.fn.zTree.getZTreeObj("cms_tiems_list");
                    var selectNode = tree.getSelectedNodes();

                    //非第一层分类
                    if(selectNode.length > 0){
                        var itemId = selectNode[0].itemId;
                        var itemNameParent = selectNode[0].itemName;
                        sessionStorage.setItem("itemId","");
                        sessionStorage.setItem("itemIdParent",itemId);
                        sessionStorage.setItem("itemNameParent",itemNameParent);
                    }else{
                        //顶级节点的时候设定为空
                        sessionStorage.setItem("itemId","");
                        sessionStorage.setItem("itemIdParent","0");
                        sessionStorage.setItem("itemNameParent","");
                    }
                    $('#content').load('cms_items/edit.html');

                });

                //编辑按钮
                $('#cms_item_edit_btn').on('click',function(){
                        var tree = $.fn.zTree.getZTreeObj("cms_tiems_list");
                        var selectNode = tree.getSelectedNodes();

                    var itemId = selectNode[0].itemId;
                    var itemNameParent = selectNode[0].itemNameParent;
                    var itemIdParent = selectNode[0].itemIdParent;
                    if (!itemIdParent || itemIdParent == null) {
                        itemIdParent = '0';
                    }
                    if (!itemNameParent || itemNameParent == null) {
                        itemNameParent = "";
                    }
                    //顶级节点的时候设定为空
                    sessionStorage.setItem("itemId",itemId);
                    sessionStorage.setItem("itemIdParent",itemIdParent);
                    sessionStorage.setItem("itemNameParent",itemNameParent);

                    $('#content').load('cms_items/edit.html');
                });

                //上移按钮
                $('#upBtn').on('click',function(){
                    var tree = $.fn.zTree.getZTreeObj("cms_tiems_list");
                    var selectNode = tree.getSelectedNodes();
                    var node = selectNode[0].getPreNode();

                    var data = new Array();
                    data.push({itemId: selectNode[0].itemId, sort: node.sort});
                    data.push({itemId: node.itemId, sort: selectNode[0].sort});

                    ajaxPost('/cms_items/resort.json',
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
                    var tree = $.fn.zTree.getZTreeObj("cms_tiems_list");
                    var selectNode = tree.getSelectedNodes();
                    var node = selectNode[0].getNextNode();

                    var data = new Array();
                    data.push({itemId: selectNode[0].itemId, sort: node.sort});
                    data.push({itemId: node.itemId, sort: selectNode[0].sort});

                    ajaxPost('/cms_items/resort.json',{'data':JSON.stringify(data)},function (data) {
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

                // // cms主表编辑
                // $("#cms_edit_btn").on("click", function(){
                //     var tree = $.fn.zTree.getZTreeObj("cms_tiems_list");
                //     var selectNode = tree.getSelectedNodes();
                //     var itemId = selectNode[0].itemId;
                //     var itemNameParent = selectNode[0].itemName;
                //     sessionStorage.setItem("itemIdParent",itemId);
                //     sessionStorage.setItem("itemNameParent",itemNameParent);
                //
                //     $('#content').load('cms_items/item_master.html');
                // });

                //删除按钮
                $('#delBtn').on('click',function(){
                    var tree = $.fn.zTree.getZTreeObj("cms_tiems_list");
                    var selectNode = tree.getSelectedNodes();

                    showConfirm('确认删除该条栏目吗?',function(){
                        var json = {};
                        json.itemId = selectNode[0].itemId;
                        ajaxPost('/cms_items/delete.json',
                            {'data':JSON.stringify(json)},function(data){
                            if($.type(data) == 'string' && data != ''){
                                data = $.parseJSON(data);
                            }

                            if(data.resultCode == '00'){
                                tree.removeNode(selectNode[0],true);
                            }else{
                                showAlert("栏目删除失败");
                            }
                        },null,null,null);

                    });
                });

            }

            //栏目树加载数据
            ajaxPost('/cms_items/selectAll.json', {}, callback, successFn, failFn, null);

        }
    }

    function resetSort(nodes) {
        if($.isArray(nodes)){
            $.each(nodes,function (i,v) {
               nodes[i].sort = i;
            });
            return nodes;
        }else {
            return [];
        }
    }
}();