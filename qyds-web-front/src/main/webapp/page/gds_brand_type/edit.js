
var editor = null;
var userId = sessionStorage.getItem("userId");
var goodsTypeId = sessionStorage.getItem("goodsTypeId");
var goodsTypeNameCn = sessionStorage.getItem("goodsTypeNameCn");
var goodsTypeAdd = sessionStorage.getItem("goodsTypeAdd");
var type = sessionStorage.getItem("type");
var sort = sessionStorage.getItem("sort");

$(document).ready(function() {
	//初始化富文本区域
	initEditor();

	//获取码表数据
	//getComCode();
	//在这里发起详细信息的请求
	if(goodsTypeId != null && goodsTypeId.length > 0){
		//goodsTypeAdd == '0' 的时候代表在改节点下添加子节点
		if(goodsTypeAdd == "0"){
			//父节点
			$('#goods_type_id_parent_name').val(goodsTypeNameCn);
			$('#goods_type_id_parent').val(goodsTypeId);
			$('#type').val(type);
			$('#type').attr('disabled','disabled');
			goodsTypeId = "";
		}else{
			//根据分类ID获取当前选择数据的详细信息
			getEditData();
		}

	}else{
		$('#goods_type_id_parent_name').val('顶级分类');
	}
	$('#goods_type_id_parent_name').attr('disabled','disabled');

	//表单验证的定义
	$("#gds_type_form").validate({
		errorElement: 'span', //default input error message container
		errorClass: 'help-block', // default input error message class
		focusInvalid: false, // do not focus the last invalid input
		rules: {
			type: {
				required: true,
				min:1
			},
			goods_type_code: {
				required: true
			},
			erp_style_no: {
				required: true
			},
			goods_type_name_en: {
				required: true
			},
			goods_type_name_cn: {
				required: true
			},
			goods_type_full_name_en: {
				required: true
			},
			goods_type_full_name_cn: {
				required: true
			}



		},
		messages: {
			type: {
				required: "请选择商品分类类型",
				min: "请选择商品分类类型"
			},
			goods_type_code: {
				required: "请输入商品分类代码"
			},
			erp_style_no: {
				required: "请输入ERP款号编码"
			},
			goods_type_name_en: {
				required: "请输入英文名称"
			},
			goods_type_name_cn: {
				required: "请输入中文名称"
			},
			goods_type_full_name_en: {
				required: "请输入英文全称"
			},
			goods_type_full_name_cn: {
				required: "请输入中文全称"
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
		if (!$("#gds_type_form").valid()){
			return;
		}

		var json = {};
		json.type = $("#type").val();
		json.goodsTypeId = goodsTypeId;
		json.goodsTypeIdParent = $("#goods_type_id_parent").val();
		json.goodsTypeCode = $("#goods_type_code").val();
		json.erpStyleNo = $("#erp_style_no").val();
		json.goodsTypeNameEn = $("#goods_type_name_en").val();
		json.goodsTypeNameCn = $("#goods_type_name_cn").val();
		json.goodsTypeFullNameEn = $("#goods_type_full_name_en").val();
		json.goodsTypeFullNameCn = $("#goods_type_full_name_cn").val();
		json.imageUrl = $("#image_url").val();
		json.sort = sort;

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
		var jsonText = JSON.stringify(jsonArray);
		json.propertyDefineJson = jsonText;
		json.introduce = editor.getContent();
		json.insertUserId = userId;
		json.updateUserId = userId;
		axse("/gds_brand_type/save.json", {'data':JSON.stringify(json)}, saveSuccessFn, errorFn);

	});

	//图片的点击事件
	$('#logo_img').click(function() {
		$("#logo_img_file").click();
	});

	//当文件变换的事件
	$('#logo_img_file').change(function(e) {
		processImage(this, $('#logo_img'));
	});

	//取消按钮的点击事件
	$('#cancel_btn').click(function(){
		gotoListPage();
	});


	//属性添加按钮的点击事件
	$('.icon-plus-sign').click(function(){
		var property_group = $('#property_group').clone();
		$(property_group).css('display','block');
		$('#property_define').append(property_group);
	});

});

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
	editor.render("introduce");

	editor.addListener('ready', function() {
		//富文本区域准备完成的业务处理
	});

}

//退回到列表画面
function gotoListPage(){
	$('#content').load('gds_brand_type/list.html');
}
//
////获得码表数据方法
//function getComCode() {
//	//品牌类型码表中的数据code为GDS_TYPE 每一个码表的code不一样
//	axse("/common/getCodeList.json", {"data":"GDS_TYPE"}, codeListSuccessFn, errorFn);
//}

//获取详细信息
function getEditData(){
	axse("/gds_brand_type/edit.json", {"goodsTypeId":goodsTypeId}, eidtSuccessFn, errorFn);
}


//处理图片的方法
function processImage(fileInput, image) {
	var filepath = $(fileInput).val();
	console.log(filepath);
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
			uploadImage(image, this.result, fileName, ext);
		}
	} else {
		showAlert("请选择图片文件！", "提示");
		return;
	}
}

//图片上传网络请求方法
function uploadImage(img, data, fileName, suffix) {
	var url = "/common/uploadImage.json";
	var param = {
		type : "GDS_TYPE",
		file : data,
		fileName : fileName,
		suffix : suffix
	};

	var success = function(data) {
		if (data.resultCode == '00') {
			img.attr("src", displayUri + orignal + data.data);
			$("#image_url").val(data.url);
		} else {
			showAlert('图片上传失败');
		}
	};
	var error = function() {
		showAlert('图片上传失败');
	};
	axseForImage(null, param, success, error);
}


////获取码表数据成功的回调方法
//function codeListSuccessFn(data) {
//	if (data.resultCode == '00') {
//		if((goodsTypeId == null || goodsTypeId.length == 0)
//			&& type != null && type.length != 0){
//			return;
//		}
//		var array = data.data;
//		$("#type").empty();
//		if (array != null) {
//			//为商品分类选择框添加数据
//			$.each(array, function(index, item) {
//				var $option = $('<option>').attr('value', item.value).text(item.displayCn);
//				$("#type").append($option);
//			});
//		}
//		//在这里发起详细信息的请求
//		if(goodsTypeId != null && goodsTypeId.length > 0){
//			//goodsTypeAdd == '0' 的时候代表在改节点下添加子节点
//			if(goodsTypeAdd == "0"){
//				//父节点
//				$('#goods_type_id_parent_name').val(goodsTypeNameCn);
//				$('#goods_type_id_parent').val(goodsTypeId);
//				$('#type').val(type);
//				$('#type').attr('disabled','disabled');
//				goodsTypeId = "";
//			}else{
//				//根据分类ID获取当前选择数据的详细信息
//				getEditData();
//			}
//
//		}else{
//			$('#goods_type_id_parent_name').val('顶级分类');
//		}
//		$('#goods_type_id_parent_name').attr('disabled','disabled');
//	}else{
//		showAlert('码表数据获取失败');
//	}
//}

//获取详细信息成功的回调 用来显示
function eidtSuccessFn(data){
	if (data.resultCode == '00') {

		//画面元素赋予初始值
		var item = data.data;
		$('#goods_type_id').val(item.goods_type_id);
		//$('#type').val(item.type);
		//$('#type').attr('disabled','disabled');
		$('#goods_type_code').val(item.goods_type_code);
		$('#erp_style_no').val(item.erp_style_no);
		$('#goods_type_name_en').val(item.goods_type_name_en);
		$('#goods_type_name_cn').val(item.goods_type_name_cn);
		$('#goods_type_full_name_en').val(item.goods_type_full_name_en);
		$('#goods_type_full_name_cn').val(item.goods_type_full_name_cn);

		$("#logo_img").attr("src", displayUri + orignal +  item.image_url);
		var introduce = item.introduce;
		editor.setContent(introduce);

		$('#goods_type_id_parent_name').val(data.pName);
		$('#goods_type_id_parent').val(item.goods_type_id_parent);
		$('#goods_type_id').val(item.goods_type_id);

		//属性
		var property = JSON.parse(item.property_define_json);
		for(var index=0;index<property.length;index++){
			var property_group = $('#property_group').clone();
			$(property_group).css('display','block');
			$('.specification',property_group).val(property[index].specification);
			$('.specification_value',property_group).val(property[index].specification_value);
			$('#property_define').append(property_group);
		}


	}else{
		showAlert('品牌系列数据获取失败');
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