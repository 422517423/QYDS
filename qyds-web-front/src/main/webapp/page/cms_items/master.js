var imgNoIndex = 1;
var imgNo;
var inpNoIndex = 1;
var inpAttributeNoIndex = 0;
var userId = sessionStorage.getItem("userId");
var itemId = sessionStorage.getItem("itemIdParent");
$(document).ready(function() {

	//初始化富文本区域
	initEditor();

	// 初始化
	var json = {};
	json.itemId = itemId;
	axse("/cms_master/edit.json", {"data": JSON.stringify(json)}, initSuccessFn, initErrorFn);

	$("#cancel_btn").click(function() {
		gotoListPage();
	});

	//保存按钮点击事件
	$("#save_btn").click(function(){
		var imgErrorFlag = false;
		var inpErrorFlag = false;
		var attributeErrorFlag = false;
		$(".img-list").each(function(){
			var eachImgNo = $(this).attr("imgNo");

			if(!$("#img_url_"+eachImgNo).val()) {
				$(this).find('span').show();
				$("#img_error_"+eachImgNo).show();
				imgErrorFlag = true;
			}
		});

		$(".inp-list").each(function() {
			var eachInpNo = $(this).attr("inpNo");

			if(!$("#inp_name_"+eachInpNo).val() || $("#inp_name_"+eachInpNo).val() == "") {
				$("#inp_error_"+eachInpNo).show();
				imgErrorFlag = true;
			}
		});

		$(".inp-attribute-list").each(function() {
			var eachInpNo = $(this).attr("inpNo");
			var eachInpAttributeNo = $(this).attr("inpAttributeNo");

			if(!$("#attribute_name_"+ eachInpNo + "_" + eachInpAttributeNo).val()) {
				$("#attribute_error_"+ eachInpNo + "_" + eachInpAttributeNo).show();
				attributeErrorFlag = true;
			}
		});

		if (imgErrorFlag || inpErrorFlag || attributeErrorFlag) {
			return;
		}

		var json = {};
		var jsonList = {};
		var imgArray = new Array();

		$(".img-list").each(function(){
			var eachImgNo = $(this).attr("imgNo");

			var imgJson = {};
			imgJson.imgUrl = $("#img_url_"+eachImgNo).val();
			imgJson.imgLink = $("#img_link_"+eachImgNo).val();
			imgArray.push(imgJson);
		});
		jsonList.imgArray = imgArray;

		var inpArray = new Array();
		$(".inp-list").each(function() {
			var eachInpNo = $(this).attr("inpNo");

			var inpJson = {};
			inpJson.inpName = $("#inp_name_"+eachInpNo).val();
			inpJson.inpLink = $("#inp_link_"+eachInpNo).val();
			var inpAttributeArray = new Array();
			$(this).find(".inp-attribute-list").each(function() {
				var inpAttributeNo = $(this).attr("inpAttributeNo");

				var inpAttributeJson = {};
				inpAttributeJson.inpAttributeName = $("#attribute_name_"+eachInpNo+"_"+inpAttributeNo).val();
				inpAttributeJson.inpAttributeLink = $("#attribute_link_"+eachInpNo+"_"+inpAttributeNo).val();
				inpAttributeArray.push(inpAttributeJson);
			});
			inpJson.inpAttributeArray = inpAttributeArray;

			inpArray.push(inpJson);
		});
		jsonList.inpArray = inpArray;

		json.cmsId = $("#cms_id").val();
		json.itemId = itemId;
		json.listJson = jsonList;
		json.title = $("#title").val();
		json.contentHtml = editor.getContent();
		json.comment = $("#comment").val();
		if (!$("#cms_id").val()) {
			json.insertUserId = userId;
		}
		json.updateUserId = userId;

		axse("/cms_master/save.json", {"data": JSON.stringify(json)}, saveSuccessFn, saveErrorFn);

	});

	$("input[type=checkbox]").each(function(){
		var division = $(this).attr('cxb_type');
		if ($(this).attr("checked") == 'checked') {
			$("#"+division+"_show_area").show();
		} else {
			$("#"+division+"_show_area").hide();
		}
	});

	$("input[type=checkbox]").on("click", function(){
		var division = $(this).attr('cxb_type');
		if ($(this).attr("checked") == 'checked') {
			$("#"+division+"_show_area").show();
		} else {
			$("#"+division+"_show_area").hide();
		}
	});
	var callFlag = false;
	//图片的点击事件
	$('.img_click').on("click", function() {
		imgNo = $(this).attr("imgNo");
		callFlag = true;
		// $('.img_file_click').trigger('change');
		$("#img_file_"+imgNo).click();
		// processImage($("#img_file_"+imgNo), $('#img'+imgNo));
	});

	//当文件变换的事件
	$('.img_file_click').on("change", function(e) {
		if (callFlag) {
			imgNo = $(this).attr("imgNo");
		}
		processImage(this, $('#img_'+imgNo));

	});

	// 添加图片
	$("#add_img_btn").click(function(){
		imgNoIndex ++;
		var item = "";
		item += '<div class="col-md-12 img-list" style="height:80px;margin-top: 10px;" imgNo="' + imgNoIndex + '">';
		item += '   <div class="col-md-2">';
		item += '      <img id="img_' + imgNoIndex +'" class="img_click" style="width:80px;height:80px;border: 1px solid #d4d4d4" src="" imgNo="' + imgNoIndex + '"/>';
		item += '      <input type="file" class="img_file_click" id="img_file_' + imgNoIndex + '" style="display: none" imgNo="' + imgNoIndex + '"/>';
		item += '      <input type="text" id="img_url_' + imgNoIndex + '" hidden />';
		item += '   </div>';
		item += '   <div class="col-md-8 control-label" style="padding-top: 20px;">';
		item += '      <label  class="col-md-2 control-label">连接地址</label>';
		item += '      <div class="col-md-5">';
		item += '         <input type="text" id="img_link_' + imgNoIndex + '" data-required="1" class="form-control specification">';
		item += '      </div>';
		item += '   </div>';
		item += '   <li class="col-md-2 icon-remove" onclick="removeImg(this);" style="margin-top: 30px;"></li>';
		item += '</div>';
		item += '   <span class="col-md-12" id="img_error_' + imgNoIndex + '" style="color: red;height: 40px;padding-top: 5px;margin-left: 10px;" hidden>请选择图片</span>';
		$("#img_show_area").append(item);
		$('.img_click').unbind("click");

		$('.img_click').on("click", function() {
			imgNo = $(this).attr("imgNo");
			callFlag = true;
			// $('.img_file_click').trigger('change');
			$("#img_file_"+imgNo).click();
			// processImage($("#img_file_"+imgNo), $('#img'+imgNo));
		});

		$('.img_file_click').unbind("change");
		//当文件变换的事件
		$('.img_file_click').on("change", function(e) {
			if (callFlag) {
				imgNo = $(this).attr("imgNo");
			}
			processImage(this, $('#img_'+imgNo));
			// imgNo = null;
			// callFlag = false
		});
	});

	// 添加输入框
	$("#add_inp_btn").click(function(){
		var inpNo = $(this).attr('inpNo');
		inpNoIndex ++;
		var item = "";
		item += '<div class="col-md-12 inp-list" style="margin-top: 10px;" inpNo="' + inpNoIndex + '">';
		item += '<div class="form-group">';
		item += '<div class="col-md-12" style="height: 40px;">';
		item += '<div class="form-group">';
		item += '<div class="col-md-2">';
		item += '<label  class="control-label" style="text-align: left">属性名称</label>';
		item += '</div>';
		item += '<div class="col-md-3">';
		item += '<input type="text" id="inp_name_' + inpNoIndex + '" data-required="1" class="form-control specification">';
		item += '</div>';
		item += '<div class="col-md-2">';
		item += '<label  class="control-label" style="text-align: left">属性连接</label>';
		item += '</div>';
		item += '<div class="col-md-3">';
		item += '<input type="text" data-required="1" id="inp_link_' + inpNoIndex + '" class="form-control specification_value">';
		item += '</div>';
		item += '<li class="col-md-2 icon-remove" onclick="removeInpArea(this);" style="margin-top: 10px;"></li>';
		item += '</div>';
		item += '</div>';
		item += '</div>';
		item += '<div class="form-group" id="inp_error_' + inpNoIndex + '" hidden >';
		item += '<div class="col-md-12" >';
		item += '<div class="form-group">';
		item += '<div class="col-md-2"></div>';
		item += '<span class="col-md-10" style="color: red;height: 40px;padding-top: 5px;margin-left: 10px;" >请输入属性名称</span>';
		item += '</div>';
		item += '</div>';
		item += '</div>';
		item += '<div class="form-group">';
		item += '<div class="col-md-12">';
		item += '<div class="form-group">';
		item += '<div class="col-md-2">';
		item += '<label  class="ontrol-label">子属性维护</label>';
		item += '<li class="icon-plus-sign add_inp_attribute_btn" id="add_inp_attribute_btn_' + inpNoIndex + '" style="padding-left: 10px;" inpNo="' + inpNoIndex + '"></li>';
		item += '</div>';
		item += '<div class="col-md-10" >';
		item += '</div>';
		item += '</div>';
		item += '</div>';
		item += '</div>';
		item += '</div>';

		$("#inp_form-group_show_area").append(item);

		$('.add_inp_attribute_btn').unbind("click");
		// 添加属性
		$(".add_inp_attribute_btn").on("click", function(){
			var inpNo = $(this).attr('inpNo');
			inpAttributeNoIndex ++;
			var item = "";
			item += '<div class="form-group">';
			item += '<div class="col-md-12 inp-attribute-list" style="height: 40px;margin-top: 10px;" inpNo="' + inpNo + '" inpAttributeNo="' + inpAttributeNoIndex + '">';
			item += '   <div class="form-group">';
			item += '   <label  class="col-md-2 control-label" style="text-align: left">属性名称</label>';
			item += '   <div class="col-md-3">';
			item += '      <input type="text" id="attribute_name_' + inpNo + "_" + inpAttributeNoIndex + '" data-required="1" class="form-control specification">';
			item += '   </div>';
			item += '   <label  class="col-md-2 control-label" style="text-align: left">属性连接</label>';
			item += '   <div class="col-md-3">';
			item += '      <input type="text" data-required="1" id="attribute_link_' + inpNo + "_" + inpAttributeNoIndex + '" class="form-control specification_value">';
			item += '   </div>';
			item += '   <li class="col-md-2 icon-remove" onclick="removeSpecification(this);" style="margin-top: 10px;"></li>';
			item += '   </div>';
			item += '   <div class="form-group" id="attribute_error_' + inpNo + "_" + inpAttributeNoIndex + '" hidden>';
			item += '   <label  class="col-md-2 control-label" style="text-align: left"></label>';
			item += '   <div class="col-md-8">';
			item += '      <span style="color: red;height: 40px;padding-top: 5px;margin-left: 10px;" >请输入属性名称</span>';
			item += '   </div>';
			item += '   <div class="col-md-2"></div>';
			item += '   </div>';
			item += '</div>';
			item += '</div>';
			$(this).parent().next().append(item);
			// $("#attribute_show_area").append(item);
		});
	});

	// 添加属性
	$(".add_inp_attribute_btn").on("click", function(){
		var inpNo = $(this).attr('inpNo');
		inpAttributeNoIndex ++;
		var item = "";
		item += '<div class="form-group">';
		item += '<div class="col-md-12 inp-attribute-list" style="height: 40px;margin-top: 10px;" inpNo="' + inpNo + '" inpAttributeNo="' + inpAttributeNoIndex + '">';
		item += '   <div class="form-group">';
		item += '   <label  class="col-md-2 control-label" style="text-align: left">属性名称</label>';
		item += '   <div class="col-md-3">';
		item += '      <input type="text" id="attribute_name_' + inpNo + "_" + inpAttributeNoIndex + '" data-required="1" class="form-control specification">';
		item += '   </div>';
		item += '   <label  class="col-md-2 control-label" style="text-align: left">属性连接</label>';
		item += '   <div class="col-md-3">';
		item += '      <input type="text" data-required="1" id="attribute_link_' + inpNo + "_" + inpAttributeNoIndex + '" class="form-control specification_value">';
		item += '   </div>';
		item += '   <li class="col-md-2 icon-remove" onclick="removeSpecification(this);" style="margin-top: 10px;"></li>';
		item += '   </div>';
		item += '   <div class="form-group" id="attribute_error_' + inpNo + "_" + inpAttributeNoIndex + '" hidden>';
		item += '   <label  class="col-md-2 control-label" style="text-align: left"></label>';
		item += '   <div class="col-md-8">';
		item += '      <span style="color: red;height: 40px;padding-top: 5px;margin-left: 10px;" >请输入属性名称</span>';
		item += '   </div>';
		item += '   <div class="col-md-2"></div>';
		item += '   </div>';
		item += '</div>';
		item += '</div>';
		$(this).parent().next().append(item);
		// $("#attribute_show_area").append(item);
	});


});

//退回到列表画面
function gotoListPage(){
	$('#content').load('cms_items/list.html');
}

//处理图片的方法
function processImage(fileInput, image) {
	var filepath = $(fileInput).val();
	// console.log(filepath);
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
		type : "CMS_MASTER",
		file : data,
		fileName : fileName,
		suffix : suffix
	};

	var success = function(data) {
		if (data.resultCode == '00') {
			img.attr("src", baseUri + IMAGE_URL + CMS_MASTER_URL + data.data);
			$("#img_url_"+imgNo).val(data.url);
			// imgNo = null;
			// callFlag = false
		} else {
			showAlert('图片上传失败');
		}
	};
	var error = function() {
		showAlert('图片上传失败');
	};
	ajaxPost(url, param, null, success, error, null);
}

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
			'|', 'form-groupspacingtop', // 段前距
			'form-groupspacingbottom', // 段后距
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
			// 'insertform-group', // 前插入行
			// 'insertcol', // 前插入列
			// 'mergeright', // 右合并单元格
			// 'mergedown', // 下合并单元格
			// 'deleteform-group', // 删除行
			// 'deletecol', // 删除列
			// 'splittoform-groups', // 拆分成行
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
	editor.render("content_html");

	editor.addListener('ready', function() {
		//富文本区域准备完成的业务处理
	});

}

function saveSuccessFn(data) {
	$('#content').load('cms_items/list.html');
}

function saveErrorFn() {

}

// 初始化页面
function initSuccessFn(data) {
	if (data.resultCode == '00') {
		var aData = data.data;
		if (aData != null) {
			$("#cms_id").val(aData.cmsId);
			$("#title").val(aData.title);

			// JSON列表
			var listJson = JSON.parse(aData.listJson);

			// 图片区域
			var imgArray = listJson.imgArray;
			if (imgArray != null && imgArray.length > 0) {
				$("#img_check_box").attr("checked", true);
				$("#img_show_area").show();
				$("#img_"+imgNoIndex).attr("src", baseUri + IMAGE_URL + CMS_MASTER_URL + imgArray[0].imgUrl);
				$("#img_url_"+imgNoIndex).val(imgArray[0].imgUrl);
				$("#img_link_"+imgNoIndex).val(imgArray[0].imgLink);
				for (var i = 1; i < imgArray.length; i ++) {
					$("#add_img_btn").trigger("click");
					$("#img_"+imgNoIndex).attr("src", baseUri + IMAGE_URL + CMS_MASTER_URL + imgArray[i].imgUrl);
					$("#img_url_"+imgNoIndex).val(imgArray[i].imgUrl);
					$("#img_link_"+imgNoIndex).val(imgArray[i].imgLink);
				}
			}

			// 文字说明部分
			var inpArray = listJson.inpArray;
			if (inpArray != null && inpArray.length > 0) {
				$("#attribute_check_box").attr("checked", true);
				$("#inp_show_area").show();
				$("#inp_name_"+inpNoIndex).val(inpArray[0].inpName);
				$("#inp_link_"+inpNoIndex).val(inpArray[0].inpLink);
				var inpAttributeArray = inpArray[0].inpAttributeArray;
				if (inpAttributeArray != null && inpAttributeArray.length > 0) {
					for (var i = 0; i < inpAttributeArray.length; i ++) {
						$("#add_inp_attribute_btn_"+inpNoIndex).trigger("click");
						$("#attribute_name_"+inpNoIndex+"_"+inpAttributeNoIndex).val(inpAttributeArray[i].inpAttributeName);
						$("#attribute_link_"+inpNoIndex+"_"+inpAttributeNoIndex).val(inpAttributeArray[i].inpAttributeLink);
					}
				}
				for (var i = 1; i < inpArray.length; i ++) {
					$("#add_inp_btn").trigger("click");
					$("#inp_name_"+inpNoIndex).val(inpArray[i].inpName);
					$("#inp_link_"+inpNoIndex).val(inpArray[i].inpLink);
					var inpAttributeArray = inpArray[i].inpAttributeArray;
					if (inpAttributeArray != null && inpAttributeArray.length > 0) {
						for (var i = 0; i < inpAttributeArray.length; i ++) {
							$("#add_inp_attribute_btn_"+inpNoIndex).trigger("click");
							$("#attribute_name_"+inpNoIndex+"_"+inpAttributeNoIndex).val(inpAttributeArray[i].inpAttributeName);
							$("#attribute_link_"+inpNoIndex+"_"+inpAttributeNoIndex).val(inpAttributeArray[i].inpAttributeLink);
						}
					}
				}
			}

			// 图文HTML
			var contentHtml = aData.contentHtml;
			if (contentHtml != null) {
				$("#ueditor_check_box").attr("checked", true);
				editor.setContent(contentHtml);
				$("#ueditor_show_area").show();
				imgNoIndex
			}

			$("#comment").val(aData.comment);
		}
	}
}

// 初始化页面失败
function initErrorFn(data) {

}

//图片删除的点击事件
function removeImg(obj){
	$(obj).parent().remove();
}

//属性删除的点击事件
function removeSpecification(obj){
	$(obj).parent().parent().parent().remove();
}

//属性删除的点击事件
function removeInpArea(obj){
	$(obj).parent().parent().parent().parent().remove();
}
