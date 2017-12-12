/**
 * Created by pancd on 16/6/23.
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
                                    name: "title"
                                },
                                simpleData: {
                                    enable: true,
                                    idKey: "cmsId",
                                    pIdKey: "cmsIdParent",
                                    sort: "sort",
                                    name: "title",
                                    level: "1"
                                }
                            },
                            callback: {
                                onClick: function(event, treeId, treeNode){
                                    $('#button-groups a').removeAttr('disabled');
                                    // if(treeNode.isParent){
                                    //     $('#delBtn').attr('disabled','disabled');
                                    // }
                                    var parentId = treeNode.cmsIdParent;
                                    if(treeNode.isFirstNode || parentId == null){
                                        $('#upBtn').attr('disabled','disabled');
                                    }

                                    if(treeNode.isLastNode || parentId == null){
                                        $('#downBtn').attr('disabled','disabled');
                                    }
                                },
                                
                                // onRemove: function(event, treeId, treeNode){
                                //     var parentNode = treeNode.getParentNode();
                                //     var tree = $.fn.zTree.getZTreeObj(treeId);
                                //     var nodes = [];
                                //     if(parentNode == null){
                                //         nodes = resetSort(tree.getNodes());
                                //     }else{
                                //         nodes = resetSort(treeNode.getParentNode().children);
                                //     }
                                //     if(nodes.length > 0){
                                //         $.each(nodes,function(i,v){
                                //             tree.updateNode(v);
                                //         });
                                //     }
                                // }
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
                // //按钮初始化
                // $('#button-groups a').not(':eq(0)').attr('disabled','disabled');

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

                //上移按钮
                $('#upBtn').on('click',function(){
                    var tree = $.fn.zTree.getZTreeObj("cms_tiems_list");
                    var selectNode = tree.getSelectedNodes();
                    var node = selectNode[0].getPreNode();

                    var data = new Array();
                    data.push({cmsId: selectNode[0].cmsId, sort: node.sort, level: node.level});
                    data.push({cmsId: node.cmsId, sort: selectNode[0].sort, level: node.level});

                    ajaxPost('/cms_master/resort.json',
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
                            // $('#button-groups a').not(':eq(0)').attr('disabled','disabled');

                        }
                    },null,null,null);
                });

                //下移按钮
                $('#downBtn').on('click',function(){
                    var tree = $.fn.zTree.getZTreeObj("cms_tiems_list");
                    var selectNode = tree.getSelectedNodes();
                    var node = selectNode[0].getNextNode();

                    var data = new Array();
                    data.push({cmsId: selectNode[0].cmsId, sort: node.sort, level: node.level});
                    data.push({cmsId: node.cmsId, sort: selectNode[0].sort, level: node.level});

                    ajaxPost('/cms_master/resort.json',{'data':JSON.stringify(data)},function (data) {
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
                            // $('#button-groups a').not(':eq(0)').attr('disabled','disabled');

                        }
                    },null,null,null);

                });

            }

            //栏目树加载数据
            ajaxPost('/cms_master/orderList.json', {}, callback, successFn, failFn, null);

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