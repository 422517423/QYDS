/**
 * Created by panda on 16/6/23.
 */
var zTree = function(){
    return {
        init : function(){
            var userId = sessionStorage.getItem("userId");

            //校验
            $("#menuForm").validate({
                errorElement: 'span', //default input error message container
                errorClass: 'help-block', // default input error message class
                rules: {
                    menuName: {
                        required: true
                    }

                },
                messages: {
                    menuName: {
                        required: "请输入菜单名称"
                    }
                },
                highlight: function (element) { // hightlight error inputs
                    $(element)
                        .closest('.form-group').addClass('has-error'); // set error class to the control group
                },

                unhighlight: function (element) { // revert the change done by hightlight
                    $(element)
                        .closest('.form-group').removeClass('has-error'); // set error class to the control group
                },

                success: function (label) {
                    label
                        .closest('.form-group').removeClass('has-error'); // set success class to the control group
                }
            });

            $('#alertMsg').hide();

            function callback(data) {
                if($.type(data) == 'string' && data != ''){
                    data = $.parseJSON(data);
                }

                if(data.resultCode == '00'){
                    if($.isArray(data.data)){
                        
                        $.each(data.data,function (i,v) {
                            data.data[i].accessStart = new Date(v.accessStart).Format('yyyy-MM-dd');
                            data.data[i].accessEnd = new Date(v.accessEnd).Format('yyyy-MM-dd');
                        });

                        var settings = {
                            view: {
                                selectedMulti: false
                            },
                            data: {
                                key: {
                                    name: "menuName",
                                    url: "menuUrl"
                                },
                                simpleData: {
                                    enable: true,
                                    idKey: "menuId",
                                    pIdKey: "parentId",
                                    name: "menuName",
                                    rootPId: 0,
                                    menuUrl: "url",
                                    accessStart: "accessStart",
                                    accessEnd: "accessEnd",
                                    menuType: "menuType",
                                    sort: "sort",
                                    logoUrl: "logoUrl"
                                }
                            },
                            callback: {
                                onClick: function(event, treeId, treeNode){
                                    $('#button-groups a').removeAttr('disabled');
                                    if(treeNode.isParent){
                                        $('#delBtn').attr('disabled','disabled');
                                        $('#logoDiv').show();
                                        $('#logos').val(treeNode.logoUrl);
                                    }else{
                                        $('#logoDiv').hide();
                                    }

                                    if(treeNode.level == '0'){
                                        $('#logoDiv').show();
                                        $('#logos').val(treeNode.logoUrl);
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
                        $.fn.zTree.init($("#menuTree"),settings, data.data);
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

                $('#editModal').on('hidden.bs.modal',function(){
                    $('#parentMenuDiv').show();
                    $('#menuForm')[0].reset();
                    iconPicker.destroyPicker();
                    $('#editModal input[type="hidden"]').val('');
                    $('#logoDiv').show();
                });

                $('#editModal').on('shown.bs.modal',function(){
                    var tree = $.fn.zTree.getZTreeObj("menuTree");
                    var selectNode = tree.getSelectedNodes();

                    if(selectNode.length > 0){
                        if($('#logos').is(":visible") && selectNode[0].logoUrl){
                            $('#logos').val(selectNode[0].logoUrl);
                        }

                        iconPicker = $('#logos').fontIconPicker({
                            source: icons
                        }).on('change', function() {
                            $('#logoUrl').val($(this).val());

                        });
                    }

                });

                //添加按钮注册事件
                $('#addBtn').on('click',function(){
                    var tree = $.fn.zTree.getZTreeObj("menuTree");
                    var selectNode = tree.getSelectedNodes();

                    if(selectNode.length > 0){
                        $('#editModal').modal(modalSettings);
                        $('#parentId').val(selectNode[0].menuId);
                        $('#parentName').val(selectNode[0].menuName);
                        $('#logoDiv').hide();
                    }else{
                        $('#editModal').modal(modalSettings);
                        $('#parentId').val('0');
                        $('#parentName').val('顶级菜单');
                    }

                    iconPicker = $('#logos').fontIconPicker({
                        source: icons
                    }).on('change', function() {
                        $('#logoUrl').val($(this).val());

                    });



                });

                //编辑按钮
                $('#editBtn').on('click',function(){
                    var tree = $.fn.zTree.getZTreeObj("menuTree");
                    var selectNode = tree.getSelectedNodes();

                    $('#editModal').modal(modalSettings);

                    $('#parentMenuDiv').hide();

                    $('#parentId').val(selectNode[0].parentId);
                    $('#menuId').val(selectNode[0].menuId);
                    $('#accessEnd').val(selectNode[0].accessEnd);
                    $('#accessStart').val(selectNode[0].accessStart);
                    $('#menuUrl').val(selectNode[0].url);
                    $('#menuType option[value="'+selectNode[0].menuType+'"]').attr('selected','selected');
                    $('#menuName').val(selectNode[0].menuName);
                    $('#sort').val(selectNode[0].sort);

                    iconPicker = $('#logos').fontIconPicker({
                        source: icons
                    }).on('change', function() {
                        $('#logoUrl').val($(this).val());

                    });

                });

                //上移按钮
                $('#upBtn').on('click',function(){
                    var tree = $.fn.zTree.getZTreeObj("menuTree");
                    var selectNode = tree.getSelectedNodes();
                    var node = selectNode[0].getPreNode();

                    var data = new Array();
                    data.push({menuId: selectNode[0].menuId,sort: node.sort});
                    data.push({menuId: node.menuId,sort: selectNode[0].sort});

                    ajaxPost('/menu/resort.json',
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
                    var tree = $.fn.zTree.getZTreeObj("menuTree");
                    var selectNode = tree.getSelectedNodes();
                    var node = selectNode[0].getNextNode();

                    var data = new Array();
                    data.push({menuId: selectNode[0].menuId,sort: node.sort});
                    data.push({menuId: node.menuId,sort: selectNode[0].sort});

                    ajaxPost('/menu/resort.json',{'data':JSON.stringify(data)},function (data) {
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

                //删除按钮
                $('#delBtn').on('click',function(){
                    var tree = $.fn.zTree.getZTreeObj("menuTree");
                    var selectNode = tree.getSelectedNodes();

                    showConfirm('确认删除该条菜单吗?',function(){
                        ajaxPost('/menu/delete.json',
                            {'menuId':selectNode[0].menuId,'sort':selectNode[0].sort,'parentId':selectNode[0].parentId},function(data){
                            if($.type(data) == 'string' && data != ''){
                                data = $.parseJSON(data);
                            }

                            if(data.resultCode == '00'){
                                tree.removeNode(selectNode[0],true);
                            }else{
                                showAlert("菜单删除失败");
                            }
                        },null,null,null);

                    });
                });

                //保存按钮
                $('#saveBtn').on('click',function(){
                    var form = $('#menuForm');

                    if (!form.valid()){
                        return false;
                    }

                    var tree = $.fn.zTree.getZTreeObj("menuTree");
                    var selectNode = tree.getSelectedNodes();

                    if($('#sort').val() == '' ){
                        var nodes;
                        var sort = 0;
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

                        $('#sort').val(sort);
                    }
                    
                    function callback(data){
                        if($.type(data) == 'string' && data != ''){
                            data = $.parseJSON(data);
                        }

                        if(data.resultCode == '00'){
                            var data = data.data;

                            if(data.flag == 'insert'){


                                var insertNode = {
                                    'menuId' : data.id,
                                    'parentId' : $('#parentId').val(),
                                    'menuName' : $('#menuName').val(),
                                    'url' : $('#menuUrl').val(),
                                    'accessStart' : $('#accessStart').val(),
                                    'accessEnd' : $('#accessEnd').val(),
                                    'sort' : sort,
                                    'menuType' : $('#menuType').val(),
                                    'logoUrl' : $('#logoUrl').val()
                                };
                                if($('#parentId').val() == '0'){
                                    tree.addNodes(null,'-1',insertNode,false);
                                }else{
                                    tree.addNodes(selectNode[0],'-1',insertNode,true);
                                }
                            }else{
                                selectNode[0].menuId = $('#menuId').val();
                                selectNode[0].parentId = $('#parentId').val();
                                selectNode[0].menuName = $('#menuName').val();
                                selectNode[0].url = $('#menuUrl').val();
                                selectNode[0].accessEnd = $('#accessEnd').val();
                                selectNode[0].accessStart = $('#accessStart').val();
                                selectNode[0].menuType = $('#menuType').val();
                                //selectNode[0].sort = $('#sort').val();
                                selectNode[0].logoUrl = $('#logoUrl').val();
                                tree.updateNode(selectNode[0]);
                            }
                            $('#editModal').modal('hide');
                        }else{
                            //错误提示
                            if(data.flag == 'insert'){
                                showAlert("菜单新建失败");
                            }else{
                                showAlert("菜单编辑失败");
                            }

                        }
                    }

                    ajaxPost('/menu/save.json',form.serialize(),callback,null,null,null);

                    tree.cancelSelectedNode(selectNode[0]);
                    $('#button-groups a').not(':eq(0)').attr('disabled','disabled');
                });
            }

            //菜单树加载数据
            ajaxPost('/menu/getMenu.json', {}, callback, successFn, failFn, null);

            var icons = ['icon-compass', 'icon-eur', 'icon-dollar (alias)', 'icon-yen (alias)', 'icon-won (alias)', 'icon-file-text',
                'icon-sort-by-attributes-alt', 'icon-thumbs-down', 'icon-xing-sign', 'icon-instagram', 'icon-bitbucket-sign',
                'icon-long-arrow-up', 'icon-windows', 'icon-skype', 'icon-male', 'icon-archive', 'icon-renren', 'icon-collapse',
                'icon-euro (alias)', 'icon-inr', 'icon-cny', 'icon-btc', 'icon-sort-by-alphabet', 'icon-sort-by-order',
                'icon-youtube-sign', 'icon-youtube-play', 'icon-flickr', 'icon-tumblr', 'icon-long-arrow-left', 'icon-android',
                'icon-foursquare', 'icon-gittip', 'icon-bug', 'icon-collapse-top', 'icon-gbp', 'icon-rupee (alias)',
                'icon-renminbi (alias)', 'icon-bitcoin (alias)', 'icon-sort-by-alphabet-alt', 'icon-sort-by-order-alt',
                'icon-youtube', 'icon-dropbox', 'icon-adn', 'icon-tumblr-sign', 'icon-long-arrow-right', 'icon-linux',
                'icon-trello', 'icon-sun', 'icon-vk', 'icon-expand', 'icon-usd', 'icon-jpy', 'icon-krw', 'icon-file',
                'icon-sort-by-attributes', 'icon-thumbs-up', 'icon-xing', 'icon-stackexchange', 'icon-bitbucket',
                'icon-long-arrow-down', 'icon-apple', 'icon-dribbble', 'icon-female', 'icon-moon', 'icon-weibo',
                'icon-adjust', 'icon-asterisk', 'icon-ban-circle', 'icon-bar-chart', 'icon-barcode', 'icon-beaker',
                'icon-bell', 'icon-bolt', 'icon-book', 'icon-bookmark', 'icon-bookmark-empty', 'icon-briefcase',
                'icon-bullhorn', 'icon-calendar', 'icon-camera', 'icon-camera-retro', 'icon-certificate', 'icon-check',
                'icon-check-empty', 'icon-cloud', 'icon-cog', 'icon-cogs', 'icon-comment', 'icon-comment-alt',
                'icon-comments', 'icon-comments-alt', 'icon-credit-card', 'icon-dashboard', 'icon-download',
                'icon-download-alt', 'icon-edit', 'icon-envelope', 'icon-envelope-alt', 'icon-exclamation-sign',
                'icon-external-link', 'icon-eye-close', 'icon-eye-open', 'icon-facetime-video', 'icon-film',
                'icon-filter', 'icon-fire', 'icon-flag', 'icon-folder-close', 'icon-folder-open', 'icon-gift',
                'icon-glass', 'icon-globe', 'icon-group', 'icon-hdd', 'icon-headphones', 'icon-heart',
                'icon-heart-empty', 'icon-home', 'icon-inbox', 'icon-info-sign', 'icon-key', 'icon-leaf',
                'icon-legal', 'icon-lemon', 'icon-lock', 'icon-unlock', 'icon-magic', 'icon-magnet', 'icon-map-marker',
                'icon-minus', 'icon-minus-sign', 'icon-money', 'icon-move', 'icon-music', 'icon-off', 'icon-ok',
                'icon-ok-circle', 'icon-ok-sign', 'icon-pencil', 'icon-picture', 'icon-plane', 'icon-plus',
                'icon-plus-sign', 'icon-print', 'icon-pushpin', 'icon-qrcode', 'icon-question-sign', 'icon-random',
                'icon-refresh', 'icon-remove', 'icon-remove-circle', 'icon-remove-sign', 'icon-reorder',
                'icon-resize-horizontal', 'icon-resize-vertical', 'icon-retweet', 'icon-road', 'icon-rss',
                'icon-screenshot', 'icon-search', 'icon-share', 'icon-share-alt', 'icon-shopping-cart', 'icon-signal',
                'icon-signin', 'icon-signout', 'icon-sitemap', 'icon-sort', 'icon-sort-down', 'icon-sort-up',
                'icon-star', 'icon-star-empty', 'icon-star-half', 'icon-tag', 'icon-tags', 'icon-tasks',
                'icon-thumbs-down', 'icon-thumbs-up', 'icon-time', 'icon-tint', 'icon-trash', 'icon-trophy',
                'icon-truck', 'icon-umbrella', 'icon-upload', 'icon-upload-alt', 'icon-user', 'icon-user-md',
                'icon-volume-off', 'icon-volume-down', 'icon-volume-up', 'icon-warning-sign', 'icon-wrench',
                'icon-zoom-in', 'icon-zoom-out', 'icon-bitcoin (alias)', 'icon-eur', 'icon-jpy', 'icon-usd',
                'icon-btc', 'icon-euro (alias)', 'icon-won (alias)', 'icon-cny', 'icon-gbp', 'icon-renminbi (alias)',
                'icon-dollar', 'icon-inr', 'icon-rupee (alias)', 'icon-file', 'icon-cut', 'icon-copy',
                'icon-paste', 'icon-save', 'icon-undo', 'icon-repeat', 'icon-paper-clip', 'icon-text-height',
                'icon-text-width', 'icon-align-left', 'icon-align-center', 'icon-align-right', 'icon-align-justify',
                'icon-indent-left', 'icon-indent-right', 'icon-font', 'icon-bold', 'icon-italic', 'icon-strikethrough',
                'icon-underline', 'icon-link', 'icon-columns', 'icon-table', 'icon-th-large', 'icon-th', 'icon-th-list',
                'icon-list', 'icon-list-ol', 'icon-list-ul', 'icon-list-alt', 'icon-arrow-down', 'icon-arrow-left',
                'icon-arrow-right', 'icon-arrow-up', 'icon-chevron-down', 'icon-circle-arrow-down',
                'icon-circle-arrow-left', 'icon-circle-arrow-right', 'icon-circle-arrow-up', 'icon-chevron-left',
                'icon-caret-down', 'icon-caret-left', 'icon-caret-right', 'icon-caret-up', 'icon-chevron-right',
                'icon-hand-down', 'icon-hand-left', 'icon-hand-right', 'icon-hand-up', 'icon-chevron-up',
                'icon-play-circle', 'icon-play', 'icon-pause', 'icon-stop', 'icon-step-backward', 'icon-fast-backward',
                'icon-backward', 'icon-forward', 'icon-fast-forward', 'icon-step-forward', 'icon-eject',
                'icon-fullscreen', 'icon-resize-full', 'icon-resize-small', 'icon-adn', 'icon-bitbucket-sign',
                'icon-dribbble', 'icon-flickr', 'icon-github-sign', 'icon-html5', 'icon-linux', 'icon-renren',
                'icon-tumblr', 'icon-vk', 'icon-xing-sign', 'icon-android', 'icon-bitcoin', 'icon-dropbox',
                'icon-foursquare', 'icon-gittip', 'icon-instagram', 'icon-maxcdn', 'icon-skype', 'icon-tumblr-sign',
                'icon-weibo', 'icon-youtube', 'icon-apple', 'icon-facebook', 'icon-github', 'icon-google-plus',
                'icon-linkedin', 'icon-pinterest', 'icon-stackexchange', 'icon-twitter', 'icon-windows',
                'icon-youtube-play', 'icon-bitbucket', 'icon-css3', 'icon-facebook-sign', 'icon-github-alt',
                'icon-google-plus-sign', 'icon-linkedin-sign', 'icon-pinterest-sign', 'icon-trello',
                'icon-twitter-sign', 'icon-xing', 'icon-youtube-sign', 'icon-ambulance', 'icon-plus-sign-alt',
                'icon-h-sign', 'icon-stethoscope', 'icon-hospital', 'icon-user-md', 'icon-medkit']
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