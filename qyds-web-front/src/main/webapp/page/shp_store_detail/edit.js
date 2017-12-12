/**
 * Created by wy on 2016/8/11.
 */

var editor = null;
var orgId = sessionStorage.getItem("orgId");
// 是否同步到服务器
var synchronizedServer = true;
$(document).ready(function() {
    //初始化富文本区域
    initEditor();

    //表单验证的定义
    $("#gds_master_form").validate({
        errorElement: 'span', //default input error message container
        errorClass: 'help-block', // default input error message class
        focusInvalid: false, // do not focus the last invalid input
        rules: {
            type: {
                required: true,
                min: 1
            },
            search_key: {
                required: true
            },
            image_url: {
                required: true
            },
            contactor: {
                required: true
            },
            phone: {
                required: true
            },
            districtid_province: {
                required: true
            },
            districtid_city: {
                required: true
            },
            districtid_district: {
                required: true
            },
            address: {
                required: true
            },
            longitude: {
                required: true
            },
            latitude: {
                required: true,
                min: 1
            },
            introduce_html: {
                required: true,
                min: 0
            }
        },
        messages: {
            type: {
                search_key: "请输入检索KEY",
                min: "请输入检索KEY"
            },
            image_url: {
                required: "请输入图片链接地址"
            },
            contactor: {
                required: "请输入联系人"
            },
            phone: {
                required: "请输入联系方式"
            },
            districtid_province: {
                required: "请输入省名称"
            },
            districtid_city: {
                required: "请输入市名称"
            },
            districtid_district: {
                required: "请输入区名称"
            },
            address: {
                required: "请输入地址"
            },
            longitude: {
                required: "请输入经度位置"
            },
            latitude: {
                required: "请输入纬度位置"
            },
            introduce_html: {
                required: "请输入门店介绍信息"
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

    //保存按钮点击事件
    $("#save_btn").click(function(){
        if (!$("#gds_master_form").valid()){
            return;
        }
        //其他验证
        if (!customerCheck()){
            return;
        }

        var json = {};
        json.orgId = orgId;
        json.search_key = $("#search_key").val();
        json.image_url = $("#image_url").val();
        json.contactor = $("#contactor").val();
        json.phone = $("#phone").val();
        json.goodsTypeNamePath = $("#goods_type_name_path").val();
        json.goodsTypeCode = $("#goods_type_code").val();
        json.erpStyleNo = $("#erp_style_no").val();
        json.goodsCode = $("#goods_code").val();
        json.goodsName = $("#goods_name").val();
        json.erpGoodsCode = $("#erp_goods_code").val();
        json.erpGoodsName = $("#erp_goods_name").val();
        json.maintainStatus = $("#maintain_status").val();
        json.isOnsell = $("#is_onsell").val();
        json.isWaste = $("#is_waste").val();
        json.searchKey = $("#search_key").val();
        var jsonTextSpe = JSON.stringify(getSpecification());
        var jsonTextSpeSku = JSON.stringify(getSKUSpecification());

        var jsonSpe = {};
        jsonSpe.specification = jsonTextSpe;
        jsonSpe.jsonTextSpeSku = jsonTextSpeSku;
        json.propertyJson = jsonSpe;
        json.introduceHtml = editor.getContent();
        json.insertUserId = userId;
        json.updateUserId = userId;

        //图片信息
        var imageArray = "";
        $.each($("#sku_image_div input[type='text']"), function(index, item) {
            imageArray = imageArray + $("#"+item.id).val() + ",";
        });
        json.imageUrlJson = imageArray;
        //拼接套装的IDS
        var goodsIds = "";
        $.each($('.master_delete'), function(index, item) {
            goodsIds = goodsIds + item.id + ",";
        });
        json.goodsIds = goodsIds;
        axse("/gds_master/save.json", {'data':JSON.stringify(json)}, saveSuccessFn, errorFn);

    });

    //取消按钮的点击事件
    $('#cancel_btn').click(function(){
        //删除掉所有的事件
        $.each($('#sku_image_div .img_clone'), function(index, item) {
            $("#logo_img_"+index).die('click');
            $("#logo_img_file_"+index).die('change');
        });
        gotoListPage();
    });

    //商品品牌选择的变换事件
    $('#brand_id').change(function(){
        //如果不是请选择 要清除掉css
        if($(this).val() != "0"){
            $(this).parent().parent().removeClass('has-error');
            $(this).parent().find('.help-block').remove();
        }else{
            $(this).parent().parent().addClass('has-error');
            $(this).parent().append('<span for="goods_type_name_path" class="help-block">请选择商品品牌.</span>');
        }
    });

    //默认隐藏ERP商品的内容
    $('.erp').hide();
    $('.tz').hide();

    //商品类型选择的变换事件
    $('#type').change(function(){
        //ERP商品
        if($(this).val() == "10"){
            showTip("商品类型选择ERP单品不能保存");
            $('.erp').show();
            $('.tz').hide();
            $('.sku').hide();
            $('.goodsBrand').show();
            $('.goods_type').show();
            getBrandList();
        }else if($(this).val() == "30"){
            $('.tz').show();
            $('.erp').hide();
            $('.sku').hide();
            //$('.goods_type').hide();
            $('.goodsBrand').hide();
        }else {
            $('.tz').hide();
            $('.erp').hide();
            $('.sku').show();
            $('.goods_type').show();
            $('.goodsBrand').show();
            getBrandList();
        }
        zTree.init();
    });

    //初始化列表 获取已经选择的商品
    initTable();

    //属性添加按钮的点击事件
    $('#icon-plus-sign').click(function(){
        var property_group = $('#property_group').clone();
        $(property_group).css('display','block');
        $('#property_define').append(property_group);
    });

    //sku属性添加按钮的点击事件
    $('#icon-plus-sign-sku').click(function(){
        var property_group_sku = $('#property_group_sku').clone();
        $(property_group_sku).css('display','block');
        $('#property_define_sku').append(property_group_sku);
    });

    //套装商品维护按钮点击事件
    $('#icon-plus-sign-sku-tc-add').click(function(){
        $('#customDialog').load('gds_master/suitlist_edit.html');
    });

    //sku属性添加按钮的点击事件
    $('#icon-plus-sign-sku-img').click(function(){
        var img_clone = $('#img_clone').clone();
        $(img_clone).css('display','block');

        //图片区域的图片个数
        var count = $('#sku_image_div .img_clone').length;
        $("#logo_img",img_clone).attr("id","logo_img_"+count);
        $("#logo_img_file",img_clone).attr("id","logo_img_file_"+count);
        $("#logo_url",img_clone).attr("id","logo_url_"+count);

        //事件追击
        //图片的点击事件
        $("#logo_img_"+count).live('click', function () {
            $("#logo_img_file_"+count).click();
        });

        //当文件变换的事件
        $('#logo_img_file_'+count).live('change',function(e) {
            processImage(this, $('#logo_img_'+count),count);
        });

        $('#sku_image_div').append(img_clone);
    });
});


//处理图片的方法
function processImage(fileInput, image,count) {
    var filepath = $(fileInput).val();
    var extStart = filepath.lastIndexOf(".");
    var ext = filepath.substring(extStart + 1, filepath.length).toUpperCase();
    var fileName = getFileName(filepath);
    if (ext != "JPG" && ext != "JPEG" && ext != "PNG") {
        showAlert("请选择符合要求格式的图片上传！", "提示");
        return;
    }
    // Get a reference to the fileList
    var files = !!fileInput.files ? fileInput.files : [];
    // If no files were selected, or no FileReader support,
    // return
    if (!files.length || !window.FileReader) {
        showAlert("读取图片失败！", "提示");
        return;
    }
    // Only proceed if the selected file is an image
    if (/^image/.test(files[0].type)) {
        // Create a new instance of the FileReader
        var reader = new FileReader();
        // Read the local file as a DataURL
        reader.readAsDataURL(files[0]);
        reader.onloadend = function() {
            uploadImage(image, this.result, fileName, ext,count);
        }
    } else {
        showAlert("请选择图片文件！", "提示");
        return;
    }
}

//图片上传网络请求方法
function uploadImage(img, data, fileName, suffix,count) {
    var url = uploadUri;
    var param = {
        type : "GDS_MASTER",
        file : data,
        fileName : fileName,
        suffix : suffix
    };

    var success = function(data) {
        if (data.resultCode == '00') {
            console.log(count);
            img.attr("src", displayUri + orignal + data.data);
            $("#logo_url_"+count).val(data.url);
        } else {
            showAlert('图片上传失败');
        }
    };
    var error = function() {
        showAlert('图片上传失败');
    };
    axseForImage(null, param, success, error);
    //ajaxPostForImage(url, param, null, success, error, null);
}

//树的初始化
var zTree = function(){
    return {
        init : function(){
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
                                    rootPId: 0
                                }
                            }
                        };
                        //数据初始化
                        $.fn.zTree.init($("#gdsTypeTree"),settings, data.data);
                    }
                }else{
                    //返回失败时候的业务处理
                    showAlert('商品分类列表获取失败');
                }
            }

            function failFn(){
                //服务器异常处理
                showAlert('商品分类列表获取失败');
            }

            function successFn() {
                //确定按钮注册事件
                $('#confirm').on('click',function(){
                    var tree = $.fn.zTree.getZTreeObj("gdsTypeTree");
                    var selectNode = tree.getSelectedNodes()[0];

                    if(selectNode == null){
                        return;
                    }

                    if(selectNode.check_Child_State != -1){
                        showAlert("请选择到最底层维护商品");
                        return;
                    }

                    $('#goods_type_id').val(selectNode.goodsTypeId);
                    $('#goods_type_code').val(selectNode.goodsTypeCode);

                    //商品分类选择的层级关系
                    var goods_type_id_path = "";
                    var goods_type_code_path = "";
                    var goods_type_name_path = "";
                    //组层级关系
                    var goods_type_id_path_array = new Array();
                    var goods_type_code_path_array = new Array();
                    var goods_type_name_path_array = new Array();

                    while (selectNode != null){
                        goods_type_id_path_array.push(selectNode.goodsTypeId);
                        goods_type_code_path_array.push(selectNode.goodsTypeCode);
                        goods_type_name_path_array.push(selectNode.goodsTypeNameCn);
                        selectNode = selectNode.getParentNode();
                    }
                    //把数组中存在的内容反转
                    goods_type_id_path_array.reverse();
                    goods_type_code_path_array.reverse();
                    goods_type_name_path_array.reverse();

                    //再通过数组拼接字符串
                    for(var i=0;i<goods_type_id_path_array.length;i++){
                        if(i==0){
                            goods_type_id_path = goods_type_id_path_array[i];
                        }else{
                            goods_type_id_path = goods_type_id_path + "_" + goods_type_id_path_array[i];
                        }
                    }

                    for(var i=0;i<goods_type_code_path_array.length;i++){
                        if(i==0){
                            goods_type_code_path = goods_type_code_path_array[i];
                        }else{
                            goods_type_code_path = goods_type_code_path + "_" + goods_type_code_path_array[i];
                        }
                    }

                    for(var i=0;i<goods_type_name_path_array.length;i++){
                        if(i==0){
                            goods_type_name_path = goods_type_name_path_array[i];
                        }else{
                            goods_type_name_path = goods_type_name_path + "_" + goods_type_name_path_array[i];
                        }
                    }
                    //给画面端元素赋值
                    $('#goods_type_id_path').val(goods_type_id_path);
                    $('#goods_type_code_path').val(goods_type_code_path);
                    $('#goods_type_name_path').val(goods_type_name_path);

                    //隐藏分类的选择画面
                    $('#selectGoodsType').hide();

                    if(goods_type_name_path.length > 0){
                        $('#goods_type_name_path').parent().parent().removeClass('has-error');
                        $('#goods_type_name_path').parent().find('span').remove();
                    }

                });
            }

            //商品分类管理加载数据
            var type = "";
            if($('#type').val() != 30){
                type = $('#type').val();
            }
            ajaxPost('/gds_type/getTreeList.json', {"type":type}, callback, successFn, failFn, null);
        }
    }
}();

//初始化富文本区域的方法
function initEditor(data) {
    var editorOption = {
        toolbars : [ [
            // 'anchor', // 锚点
            'undo', // 撤销
            'redo', // 重做
            '|', 'fontfamily', // 字体
            'fontsize', // 字号
            '|', 'blockquote', // 引用
            'horizontal', // 分隔线
            '|', 'removeformat', // 清除格式
            'formatmatch' // 格式刷

        ], [ 'bold', // 加粗
            'italic', // 斜体
            'underline', // 下划线
            'forecolor', // 字体颜色
            'backcolor', // 背景色
            '|', 'indent', // 首行缩进
            'justifyleft', // 居左对齐
            'justifyright', // 居右对齐
            'justifycenter', // 居中对齐
            'justifyjustify', // 两端对齐
            '|', 'rowspacingtop', // 段前距
            'rowspacingbottom', // 段后距
            'lineheight', // 行间距
            '|', 'insertorderedlist', // 有序列表
            'insertunorderedlist', // 无序列表
            '|',
            // 'imagenone', // 默认
            // 'imageleft', // 左浮动
            // 'imageright', // 右浮动
            // 'imagecenter', // 居中
            '|', 'emotion', // 表情
            'simpleupload', // 单图上传
            // 'insertvideo', // 视频
            'map' // Baidu地图
            // 'source', // 源代码
            // 'fullscreen', // 全屏
            // 'help', // 帮助
            // 'attachment', // 附件
            // 'strikethrough', // 删除线
            // 'subscript', // 下标
            // 'superscript', // 上标
            // 'fontborder', // 字符边框

            // 'snapscreen', // 截图
            // 'pasteplain', // 纯文本粘贴模式
            // 'selectall', // 全选
            // 'print', // 打印
            // 'preview', // 预览

            // 'time', // 时间
            // 'date', // 日期
            // 'unlink', // 取消链接
            // 'insertrow', // 前插入行
            // 'insertcol', // 前插入列
            // 'mergeright', // 右合并单元格
            // 'mergedown', // 下合并单元格
            // 'deleterow', // 删除行
            // 'deletecol', // 删除列
            // 'splittorows', // 拆分成行
            // 'splittocols', // 拆分成列
            // 'splittocells', // 完全拆分单元格
            // 'deletecaption', // 删除表格标题
            // 'inserttitle', // 插入标题
            // 'mergecells', // 合并多个单元格
            // 'deletetable', // 删除表格
            // 'cleardoc', // 清空文档
            // 'insertparagraphbeforetable', // 表格前插入行
            // 'insertcode', // 代码语言
            // 'paragraph', // 段落格式
            // 'insertimage', // 多图上传
            // 'edittable', // 表格属性
            // 'edittd', // 单元格属性
            // 'link', // 超链接
            // 'spechars', // 特殊字符
            // 'searchreplace', // 查询替换
            // 'gmap', // Google地图
            // 'directionalityltr', // 从左向右输入
            // 'directionalityrtl', // 从右向左输入
            // 'pagebreak', // 分页
            // 'insertframe', // 插入Iframe
            // 'wordimage', // 图片转存
            // 'edittip ', // 编辑提示
            // 'customstyle', // 自定义标题
            // 'autotypeset', // 自动排版
            // 'webapp', // 百度应用
            // 'touppercase', // 字母大写
            // 'tolowercase', // 字母小写
            // 'background', // 背景
            // 'template', // 模板
            // 'scrawl', // 涂鸦
            // 'music', // 音乐
            // 'inserttable', // 插入表格
            // 'drafts', // 从草稿箱加载
            // 'charts', // 图表
        ] ],
        enableAutoSave : false,
        autoSyncData : false,
        catchRemoteImageEnable : false,
        autoFloatEnabled : true,
        enableContextMenu : false
    };
    editor = new baidu.editor.ui.Editor(editorOption);
    editor.render("introduce_html");

    editor.addListener('ready', function() {
        //富文本区域准备完成的业务处理
    });

}

//退回到列表画面
function gotoListPage(){
    $('#content').load('gds_master/list.html');
}

//获取详细信息
function getEditData(){
    axse("/gds_master/edit.json", {"goodsId":goodsId}, eidtSuccessFn, errorFn);
}

//品牌列表的后台访问方法
function getBrandList(){
    //商品分类管理加载数据
    var type = "";
    if($('#type').val() != 30){
        type = $('#type').val();
    }
    axse("/gds_brand/getAllList.json", {"type":type}, brandListSuccessFn, errorFn);
}

//品牌列表数据获取成功的回调方法
function brandListSuccessFn(data){
    if (data.resultCode == '00') {
        var array = data.data;
        $("#brand_id").empty();
        if (array != null) {
            //为维护状态选择框添加数据
            $.each(array, function(index, item) {
                var $option = $('<option>').attr('value', item.brandId).text(item.brandName);
                $("#brand_id").append($option);
            });
        }

    }else{
        showAlert('品牌列表数据获取失败');
    }
}


//获取详细信息成功的回调 用来显示
function eidtSuccessFn(data){
    if (data.resultCode == '00') {
        var item = data.data;

        //如果套装的场合显示套装列表
        if(item.type == '30'){
            $('.tz').show();
            //$('.goods_type').hide();
            $('.goodsBrand').hide();
            $('.sku').hide();
        }else if(item.type == '10'){
            $('.erp').show();
            $('.sku').hide();
            $('.goodsBrand').show();
        }

        $('#goods_id').val(item.goods_id);
        $('#type').val(item.type);
        $('#type').attr("disabled","disabled");

        getBrandList();
        $('#brand_id').val(item.brand_id);
        //商品品牌选择
        $('.select2-chosen').text(item.brandName);
        $('#goods_type_id').val(item.goods_type_id);
        $('#goods_type_id_path').val(item.goods_type_id_path);
        $('#goods_type_code').val(item.goods_type_code);
        $('#goods_type_code_path').val(item.goods_type_code_path);
        $('#goods_type_name_path').val(item.goods_type_name_path);

        $('#erp_style_no').val(item.erp_style_no);
        $('#goods_code').val(item.goods_code);
        $('#goods_name').val(item.goods_name);
        $('#erp_goods_code').val(item.erp_goods_code);
        $('#erp_goods_name').val(item.erp_goods_name);
        $('#maintain_status').val(item.maintain_status);
        $('#is_onsell').val(item.is_onsell);
        $('#is_waste').val(item.is_waste);

        $('#search_key').val(item.search_key);

        //属性
        if(item.property_json != null){
            var property = JSON.parse(item.property_json);
            var specification = JSON.parse(property.specification);
            var jsonTextSpeSku = JSON.parse(property.jsonTextSpeSku);
            //自有属性
            for(var index=0;index<specification.length;index++){
                var property_group = $('#property_group').clone();
                $(property_group).css('display','block');
                $('.specification',property_group).val(specification[index].specification);
                $('.specification_value',property_group).val(specification[index].specification_value);
                $('#property_define').append(property_group);
            }

            //sku属性
            for(var index=0;index<jsonTextSpeSku.length;index++){
                var property_group_sku = $('#property_group_sku').clone();
                $(property_group_sku).css('display','block');
                $('.specification_sku',property_group_sku).val(jsonTextSpeSku[index].specification_sku);
                $('.specification_value_sku',property_group_sku).val(jsonTextSpeSku[index].specification_value_sku);
                //$('.image_property',property_group_sku).val(jsonTextSpeSku[index].is_image_property);
                $('#property_define_sku').append(property_group_sku);
            }
        }

        if(item.introduce_html != null){
            var introduceHtml = item.introduce_html;
            editor.setContent(introduceHtml);
        }

        //删除掉所有的事件
        $.each($('#sku_image_div .img_clone'), function(index, item) {
            $("#logo_img_"+index).die('click');
            $("#logo_img_file_"+index).die('change');
        });
        $('#sku_image_div').empty();
        if(item.imageUrlJson != null){
            //var imageUrlJson = JSON.parse(item.imageUrlJson);
            var imageUrlJson = item.imageUrlJson.split(",");
            if(imageUrlJson != null){
                for(var index = 0; index < imageUrlJson.length - 1; index++){
                    var img_clone = $('#img_clone').clone();
                    $(img_clone).css('display','block');

                    $("#logo_img",img_clone).attr("id","logo_img_"+index);
                    $("#logo_img_file",img_clone).attr("id","logo_img_file_"+index);
                    $("#logo_url",img_clone).attr("id","logo_url_"+index);

                    $('#sku_image_div').append(img_clone);

                    //事件追击
                    //图片的点击事件
                    $("#logo_img_"+index,img_clone).live('click', function () {
                        var idx = $(this)[0].id.split('_')[2];
                        $("#logo_img_file_"+idx).click();
                    });

                    //当文件变换的事件
                    $('#logo_img_file_'+index,img_clone).live('change',function(e) {
                        var idx = $(this)[0].id.split('_')[3];
                        processImage(this, $('#logo_img_'+idx),idx);
                    });


                    //url值
                    $("#logo_url_"+index).val(imageUrlJson[index]);
                    $("#logo_img_"+index).attr("src", displayUri + orignal + imageUrlJson[index]);
                }
            }
        }

        //初始化数
        zTree.init();

    }else{
        showAlert('商品详细数据获取失败');
    }
}

//数据保存成功的回调方法
function saveSuccessFn(){
    gotoListPage();
}

// 失败的回调方法
function errorFn() {
    showAlert('数据获取失败');
}

//属性删除的点击事件
function removeSpecification(obj){
    $(obj).parent().remove();
}

//属性删除的点击事件
function removeSpecificationImg(obj){

    //删除掉所有的事件
    $.each($('#sku_image_div .img_clone'), function(index, item) {
        $("#logo_img_"+index).die('click');
        $("#logo_img_file_"+index).die('change');
    });

    //从dom中移除
    $(obj).parent().parent().remove();

    //删除的时候要对img容器中的所有图片重新排序id重新绑定事件
    $.each($('#sku_image_div .img_clone'), function(index, item) {
        $('img',item).attr("id","logo_img_"+index);
        $("input[type='file']",item).attr("id","logo_img_file_"+index);
        $("input[type='text']",item).attr("id","logo_url_"+index);

        //绑定事件
        $("#logo_img_"+index).live('click', function () {
            $("#logo_img_file_"+index).click();
        });

        //当文件变换的事件
        $('#logo_img_file_'+index).live('change',function(e) {
            processImage(this, $('#logo_img_'+index),index);
        });
    });


}

//自定义check项目
function customerCheck(){

    var re = true;
    //商品分类选择
    if($('#goods_type_name_path').val().length == 0){
        $('#goods_type_name_path').parent().parent().addClass('has-error');
        $('#goods_type_name_path').parent().append('<span for="goods_type_name_path" class="help-block">请选择商品分类.</span>');
        re = false;
    }else{
        $('#goods_type_name_path').parent().parent().removeClass('has-error');
        $('#goods_type_name_path').parent().find('span').remove();
    }


    //ERP商品不check
    if($('#type').val() != '10') {

        //商品品牌选择 check
        if ($('#brand_id').val() == "0" || $('#brand_id').val() == null) {
            $('#brand_id').parent().parent().addClass('has-error');
            $('#brand_id').parent().append('<span for="goods_type_name_path" class="help-block">请选择商品品牌.</span>');
            re = false;
        } else {
            $('#brand_id').parent().parent().removeClass('has-error');
            $('#brand_id').parent().find('.help-block').remove();
        }
    }

    if($('#type').val() == '20'){
        //ERP商品和套装不需要sku必须
        //SKU属性check
        var spe = getSKUSpecification();
        var skucheck = true;
        if(spe.length == 0){
            $('#icon-plus-sign-sku').parent().parent().addClass('has-error');
            $('#icon-plus-sign-sku').parent().append('<span for="goods_type_name_path" class="help-block">请维护商品SKU属性.</span>');
            re = false;
            skucheck = false;
        }

        for(var index=0;index<spe.length;index++){
            var specification_sku = spe[index].specification_sku;
            var specification_value_sku = spe[index].specification_value_sku;
            if(specification_sku.length == 0
                ||specification_value_sku.length == 0){
                re = false;
                skucheck = false;
                $('#icon-plus-sign-sku').parent().parent().addClass('has-error');
                $('#icon-plus-sign-sku').parent().append('<span for="goods_type_name_path" class="help-block">请维护商品SKU属性.</span>');
                break;
            }
        }

        if(skucheck){
            $('#icon-plus-sign-sku').parent().parent().removeClass('has-error');
            $('#icon-plus-sign-sku').parent().find('.help-block').remove();
        }

    }



    //新建套装的时候的业务check
    if($('#type').val() == '30'){
        if($('#gds_suitlist_table .dataTables_empty').length > 0 ){
            $('#icon-plus-sign-sku-tc-add').parent().parent().addClass('has-error');
            $('#icon-plus-sign-sku-tc-add').parent().append('<span for="goods_type_name_path" class="help-block">请维护套装商品.</span>');
            re = false;
        }else{
            $('#icon-plus-sign-sku-tc-add').parent().parent().removeClass('has-error');
            $('#icon-plus-sign-sku-tc-add').parent().find('.help-block').remove();
        }
    }

    //ERP单品不能保存的check
    if($('#type').val() == '10'){
        var attr = $('#type').attr("disabled");
        if(attr == null){
            showAlert("不能新建ERP类型的商品.");
            re = false;
        }
    }


    return re;
}

//自有属性json组成
function getSpecification(){
    //属性定义json组成
    var array = new Array();
    $.each($('.specification'), function(index, item) {
        if(index != 0){
            var specification = $(item).val();
            array.push(specification);
        }
    });
    var array_value = new Array();
    $.each($('.specification_value'), function(index, item) {
        if(index != 0){
            var specification_value = $(item).val();
            array_value.push(specification_value);
        }
    });
    var jsonArray = new Array();
    for(var i = 0; i < array.length; i++){
        var property_json = {};
        property_json.specification = array[i];
        property_json.specification_value = array_value[i];
        jsonArray.push(property_json);
    }
    //var jsonText = JSON.stringify(jsonArray);
    return jsonArray;
}

//SKU属性json组成
function getSKUSpecification(){
    //属性定义json组成
    var array = new Array();
    $.each($('.specification_sku'), function(index, item) {
        if(index != 0){
            var specification = $(item).val();
            array.push(specification);
        }
    });
    var array_value = new Array();
    $.each($('.specification_value_sku'), function(index, item) {
        if(index != 0){
            var specification_value = $(item).val();
            array_value.push(specification_value);
        }
    });

    //var array_property = new Array();
    //$.each($('.image_property'), function(index, item) {
    //	if(index != 0){
    //		var is_image_property = $(item).val();
    //		array_property.push(is_image_property);
    //	}
    //});


    var jsonArray = new Array();
    for(var i = 0; i < array.length; i++){
        var property_json = {};
        property_json.specification_sku = array[i];
        property_json.specification_sku_ascii = array[i].charCodeAt();
        property_json.specification_value_sku = array_value[i];
        property_json.specification_value_sku_ascii = array_value[i].charCodeAt();
        //property_json.is_image_property = array_property[i];
        jsonArray.push(property_json);
    }
    return jsonArray;
}

//提示信息方法
function showTip(message) {
    $("#gds_master_edit_tip").text(message);
    setTimeout(function () {
        $("#gds_master_edit_tip").text("");
    }, 2000);
}

//表格js初始化(已经选择的套装列表)
function initTable() {
    oTZTable = $('#gds_suitlist_table').dataTable({
        "aoColumns": [
            { "mData": "typeName" },
            { "mData": "brandName"},
            { "mData": "erpStyleNo"},
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
            //	"fnRender": function ( oObj ) {
            //		if(oObj.aData.isWaste == "0"){
            //			return "是";
            //		}else{
            //			return "否";
            //		}
            //	}
            //},
            //{ "mData": "createUserName" },
            //{ "mData": "insertTime" },
            {
                "mData": null,
                "fnRender": function ( oObj ) {
                    if(oObj.aData.suitId != null){
                        return '<a class="master_delete" onclick="master_del(this);" name = "'+oObj.aData.suitId +'"  id="' + oObj.aData.goodsId + '" href="javascript:;">删除</a> ';
                    }else{
                        return '<a class="master_delete" onclick=""  id="' + oObj.aData.goodsId + '" href="javascript:;">未保存</a> ';
                    }

                },
                "bUseRendered": false
            }

        ],
        "bAutoWidth": false,    //关闭自适应列宽，导致列表后半部空白。IE8下现象
        "bProcessing": false,
        "bServerSide": true,
        "bSort": false,
        "sAjaxSource": "../gds_suitlist/getSelectList.json",
        "fnServerParams": function ( aoData ) {
            aoData.push( { "name": "goodsId", "value": sessionStorage.getItem("goodsId") } );
            aoData.push( { "name": "goodsIds", "value": sessionStorage.getItem("goodsIds") } );
        },
        "fnServerData": fnServerData,
        "bFilter": false
    });

    $('#gds_suitlist_table_info').hide();
    $('.dataTables_paginate').hide();
    $('#gds_suitlist_table_length').hide();
}


//点击删除按钮
function master_del(obj){
    var suitId = obj.name;
    showConfirm('确认删除该商品吗?',function(){
        axse("/gds_suitlist/delete.json", {"suitId":suitId}, delSuccessFn, errorFn);
    });

}

//删除方法的回调函数
function delSuccessFn(data){
    if (data.resultCode == '00') {
        //刷新Datatable，会自动激发retrieveData
        oTZTable.fnDraw();
    }else{
        showAlert('删除失败');
    }
}

//获得码表数据方法
function getComCodeOnsell() {
    //品牌类型码表中的数据code为GDS_TYPE 每一个码表的code不一样
    axse("/common/getCodeList.json", {"data":"YES_NO"}, codeListSuccessFn, errorFn);
}

//获取码表数据成功的回调方法
function codeListSuccessFn(data) {
    if (data.resultCode == '00') {
        var array = data.data;
        if (array != null) {
            //为是否上架选择框添加数据
            $.each(array, function(index, item) {
                var $option = $('<option>').attr('value', item.value).text(item.displayCn);
                $("#is_onsell").append($option);
            });
            //是否上架
            $('#is_onsell').val('1');//未完成状态(默认)
            $('#is_onsell').attr('disabled','disabled');

            //在这里发起详细信息的请求
            if(goodsId != null && goodsId.length > 0){
                //根据分类ID获取当前选择数据的详细信息
                getEditData();
            }else{
                //获取品牌的列表数据
                getBrandList();
                //删除掉所有的事件
                $.each($('#sku_image_div .img_clone'), function(index, item) {
                    $("#logo_img_"+index).die('click');
                    $("#logo_img_file_"+index).die('change');
                });
            }
        }
    }else{
        showAlert('码表数据获取失败');
    }
}

//获得码表数据方法
function getComCodeMainTain() {
    //品牌类型码表中的数据code为MAINTAIN_STATUS 每一个码表的code不一样
    axse("/common/getCodeList.json", {"data":"MAINTAIN_STATUS"}, codeListStatusSuccessFn, errorFn);
}

//获取码表数据成功的回调方法
function codeListStatusSuccessFn(data) {
    if (data.resultCode == '00') {
        var array = data.data;
        if (array != null) {
            //为维护状态选择框添加数据
            $.each(array, function(index, item) {
                var $option = $('<option>').attr('value', item.value).text(item.displayCn);
                $("#maintain_status").append($option);
            });
            //维护状态
            $('#maintain_status').val('20');//未完成状态(默认)
            $('#maintain_status').attr('disabled','disabled');
        }
    }else{
        showAlert('码表数据获取失败');
    }
}

