var oTZTable;
var editor = null;
var goodsId = selectedGoodsId;
// 是否同步到服务器
var synchronizedServer = true;
$(document).ready(function() {
	$("#goods_detail_dialog").modal('show');
	//初始化富文本区域
	// initEditor();
	$("#gds_master_form input").attr("disabled", true);
	$("#gds_master_form textarea").attr("disabled", true);
	$("#gds_master_form select").attr("disabled", true);
	$("#gds_master_form a").attr("disabled", true);

	//获取商品类型码表数据
	getOptionCode("GOODS_TYPE","type");

	//获取是否上架 是否废除码表数据
	//getOptionCode("YES_NO","is_onsell");
	//getOptionCode("YES_NO","is_waste");
	//getOptionCode("YES_NO","is_image_property");

	//获取维护状态
	//getOptionCode("MAINTAIN_STATUS","maintain_status");

	//获取是否上架 (商品类型 需要特殊处理)
	getComCodeOnsell();
	getComCodeMainTain();

	//商品品牌选择
	$('.select2_category').select2({
		placeholder: "Select an option",
		allowClear: true
	});


	$('#selectGoodsType').hide();
	//商品分类选择不可编辑
	$('#goods_type_name_path').attr('disabled','disabled');

	//***********************************品牌系列***************************************//
	$('#selectGoodsBrandType').hide();
	//商品分类选择不可编辑
	$('#goods_brand_type_name_path').attr('disabled','disabled');
	//***********************************品牌系列***************************************//

	//维护状态不可编辑
	$('#maintain_status').val('20');//未完成状态
	$('#maintain_status').attr('disabled','disabled');


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
			//erp_style_no: {
			//	required: true
			//},
			goods_code: {
				required: true
			},
			goods_name: {
				required: true
			},
			erp_goods_code: {
				required: true
			},
			erp_goods_name: {
				required: true
			},
			maintain_status: {
				required: true,
				min: 1
			},
			is_onsell: {
				required: true,
				min: 0
			},
			//is_waste: {
			//	required: true,
			//	min: 0
			//},
			search_key: {
				required: true
			}
		},
		messages: {
			type: {
				required: "请选择商品类型",
				min: "请选择商品类型"
			},
			//erp_style_no: {
			//	required: "请输入ERP分类款号编码"
			//},
			goods_code: {
				required: "请输入商品代码"
			},
			goods_name: {
				required: "请输入商品名称"
			},
			erp_goods_code: {
				required: "请输入ERP款号"
			},
			erp_goods_name: {
				required: "请输入ERP款名"
			},
			maintain_status: {
				required: "请选择维护状态",min: "请选择维护状态"
			},
			is_onsell: {
				required: "请选择是否上架",min: "请选择是否上架"
			},
			//is_waste: {
			//	required: "请选择是否废弃",min: "请选择是否废弃"
			//},
			search_key: {
				required: "请输入检索主key"
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

	//默认隐藏ERP商品的内容
	$('.erp').hide();
	$('.tz').hide();

	//初始化列表 获取已经选择的商品
	initTable();
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

var zTreeBrand = function(){
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
						$.fn.zTree.init($("#gdsBrandTypeTree"),settings, data.data);
					}
				}else{
					//返回失败时候的业务处理
					showAlert('品牌系列列表获取失败');
				}
			}

			function failFn(){
				//服务器异常处理
				showAlert('品牌系列列表获取失败');
			}

			function successFn() {

			}
			ajaxPost('/gds_brand_type/getTreeList.json', {}, callback, successFn, failFn, null);
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
		'map', // Baidu地图
		 'source' // 源代码
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
			$('#brand_id').attr("disabled","disabled");
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

		$('#goods_brand_type_id').val(item.goods_brand_type_id);
		$('#goods_brand_type_id_path').val(item.goods_brand_type_id_path);
		$('#goods_brand_type_code').val(item.goods_brand_type_code);
		$('#goods_brand_type_code_path').val(item.goods_brand_type_code_path);
		$('#goods_brand_type_name_path').val(item.goods_brand_type_name_path);

		//$('#erp_style_no').val(item.erp_style_no);
		$('#goods_code').val(item.goods_code);
		$('#goods_name').val(item.goods_name);
		$('#erp_goods_code').val(item.erp_goods_code);
		$('#erp_goods_name').val(item.erp_goods_name);
		$('#maintain_status').val(item.maintain_status);
		$('#is_onsell').val(item.is_onsell);
		//$('#is_waste').val(item.is_waste);

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
				$('.specification_sku',property_group_sku).attr("disabled","disabled");
				$('.specification_value_sku',property_group_sku).attr("disabled","disabled");
				$('.icon-remove',property_group_sku).hide();

				$('#property_define_sku').append(property_group_sku);
			}
		}
		$('#icon-plus-sign-sku').hide();

		if(item.introduce_html != null){
			var introduceHtml = item.introduce_html;
			// editor.setContent(introduceHtml);
		}

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

					//url值
					$("#logo_url_"+index).val(imageUrlJson[index]);
					$("#logo_img_"+index).attr("src", displayUri + orignal + imageUrlJson[index]);
				}
			}
		}

		//初始化数
		zTree.init();
		zTreeBrand.init();

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

	//从dom中移除
	$(obj).parent().parent().remove();

	//删除的时候要对img容器中的所有图片重新排序id重新绑定事件
	$.each($('#sku_image_div .img_clone'), function(index, item) {
		$('img',item).attr("id","logo_img_"+index);
		$("input[type='file']",item).attr("id","logo_img_file_"+index);
		$("input[type='text']",item).attr("id","logo_url_"+index);
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


	//ERP单品check
	if($('#type').val() == '10'){
		var re = true;
		//商品分类选择
		if($('#goods_brand_type_name_path').val().length == 0){
			$('#goods_brand_type_name_path').parent().parent().addClass('has-error');
			$('#goods_brand_type_name_path').parent().append('<span for="goods_brand_type_name_path" class="help-block">请选择品牌系列.</span>');
			re = false;
		}else{
			$('#goods_brand_type_name_path').parent().parent().removeClass('has-error');
			$('#goods_brand_type_name_path').parent().find('span').remove();
		}
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
		var ascii = "";
		for(var j=0;j<array[i].length;j++){
			ascii = ascii + array[i][j].charCodeAt();
		}
		property_json.specification_sku_ascii = ascii;
		property_json.specification_value_sku = array_value[i];
		for(var j=0;j<array_value[i].length;j++){
			ascii = ascii + array_value[i][j].charCodeAt();
		}
		property_json.specification_value_sku_ascii = ascii;
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
						return '<a class="master_delete"  name = "'+oObj.aData.suitId +'"  id="' + oObj.aData.goodsId + '" href="javascript:;">删除</a> ';
					}else{
						return '<a class="master_delete" id="' + oObj.aData.goodsId + '" href="javascript:;">未保存</a> ';
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
