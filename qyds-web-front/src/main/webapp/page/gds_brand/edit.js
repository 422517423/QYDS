
var editor = null;
var userId = sessionStorage.getItem("userId");
var brandId = sessionStorage.getItem("brandId");
// 是否同步到服务器
var synchronizedServer = true;
$(document).ready(function() {
	//初始化富文本区域
	initEditor();

	//获取码表数据
	getOptionCode("GDS_BRAND_TYPE","type");

	//编辑的时候获取数据进行回显
	if(brandId != null && brandId.length > 0){
		getEditData();
	}

	//表单验证的定义
	$("#gds_brand_form").validate({
		errorElement: 'span', //default input error message container
		errorClass: 'help-block', // default input error message class
		focusInvalid: false, // do not focus the last invalid input
		rules: {
			type: {
				required: true,
				min: 1
			},
			brand_code: {
				required: true
			},
			erp_brand_code: {
				required: true
			},
			brand_name: {
				required: true
			}
		},
		messages: {
			type: {
				required: "请选择品牌类型",
				min: "请选择品牌类型"
			},
			brand_code: {
				required: "请输入品牌代码"
			},
			erp_brand_code: {
				required: "请输入ERP款号编码"
			},
			brand_name: {
				required: "请输入品牌名称"
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
		if (!$("#gds_brand_form").valid()){
			return;
		}

		if(brandId == null || brandId.length == 0){
			if($("#type").val() == '10'){
				showAlert("不能新建ERP类型的品牌.");
				return;
			}
		}

		var json = {};
		json.type = $("#type").val();
		json.brandId = brandId;
		json.brandCode = $("#brand_code").val();
		json.erpBrandCode = $("#erp_brand_code").val();
		json.brandName = $("#brand_name").val();
		json.logoUrl = $("#logo_url").val();
		json.introduceHtml = editor.getContent();
		json.insertUserId = userId;
		json.updateUserId = userId;
		axse("/gds_brand/save.json", {'data':JSON.stringify(json)}, saveSuccessFn, errorFn);

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
	})

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
	editor.render("introduce_html");

	editor.addListener('ready', function() {
		//富文本区域准备完成的业务处理
	});

}

//退回到列表画面
function gotoListPage(){
	$('#content').load('gds_brand/list.html');
}

//获取详细信息
function getEditData(){
	axse("/gds_brand/edit.json", {"brandId":brandId}, eidtSuccessFn, errorFn);
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
		type : "GDS_BRAND_TYPE",
		file : data,
		fileName : fileName,
		suffix : suffix
	};

	var success = function(data) {
		if (data.resultCode == '00') {
			img.attr("src", displayUri + orignal  + data.data);
			$("#logo_url").val(data.url);
		} else {
			showAlert('图片上传失败');
		}
	};
	var error = function() {
		showAlert('图片上传失败');
	};
	axseForImage(null, param, success, error);
}


//获取详细信息成功的回调 用来显示
function eidtSuccessFn(data){
	if (data.resultCode == '00') {
		var item = data.data;
		$('#type').val(item.type);
		$('#type').attr("disabled","disabled");
		$('#brand_code').val(item.brand_code);
		$('#erp_brand_code').val(item.erp_brand_code);
		$('#brand_name').val(item.brand_name);
		$("#logo_img").attr("src", displayUri + orignal  + item.logo_url);
		var introduceHtml = item.introduce_html;
		$('#brand_id').val(item.brand_id);
		editor.setContent(introduceHtml);

	}else{
		showAlert('品牌数据获取失败');
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
