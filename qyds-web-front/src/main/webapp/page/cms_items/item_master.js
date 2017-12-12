var imgNoIndex = 1;
var imgNo;
var inpNoIndex = 1;
var inpAttributeNoIndex = 0;
var userId = sessionStorage.getItem("userId");
var itemId = sessionStorage.getItem("itemIdParent");
$(document).ready(function() {

	//初始化富文本区域
	initEditor();

	// 获取栏目形式
	getComCode();
	// 商品列表
	getGdsList();
	// 活动列表
	getActList();

	//表单验证的定义
	$("#gds_type_form").validate({
		errorElement: 'span', //default input error message container
		errorClass: 'help-block', // default input error message class
		focusInvalid: false, // do not focus the last invalid input
		rules: {
			title: {
				required: true
			},
			item_type: {
				required: true
			},
			text: {
				required: true
			},
			link: {
				required: true
			},
			gds: {
				required: true
			},
			goods_type_name_path: {
				required: true
			},
			act: {
				required: true
			}


		},
		messages: {
			title: {
				required: "请选输入标题"
			},
			item_type: {
				required: "请选择栏目形式"
			},
			text: {
				required: "请输入文字"
			},
			link: {
				required: "请输入链接"
			},
			gds: {
				required: "请输入商品"
			},
			goods_type_name_path: {
				required: "请输入商品分类"
			},
			act: {
				required: "请输入活动"
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

	// $("#item_type").change(function(){
	//
	// }).multipleSelect({
	// 	width: '100%'
	// });

	// 初始化
	var json = {};
	json.itemId = itemId;

	axse("/cms_master/edit.json", {"data": JSON.stringify(json)}, initSuccessFn, initErrorFn);

	$("#cancel_btn").click(function() {
		gotoListPage();
	});

	//保存按钮点击事件
	$("#save_btn").click(function(){
		if (!$("#gds_type_form").valid()){
			return;
		}

		var json = {};
		json.cmsId = $("#cms_id").val();
		json.itemId = itemId;
		var itemType = '';
		for (var i = 0; i < $("#item_type").val().length; i ++) {
			if (itemType == '') {
				itemType = $("#item_type").val()[i];
			} else {
				itemType = itemType + "," + $("#item_type").val()[i];
			}
		}
		json.itemType = itemType;
		json.title = $("#title").val();
		json.contentHtml = editor.getContent();
		json.comment = $("#comment").val();
		if (itemId == null || itemId == "") {
			json.insertUserId = userId;
		}
		json.updateUserId = userId;
		var itemType;
		$(".change_class").each(function() {
			if(!$(this).is(':hidden')){
				itemType = $(this).attr("itemType");
			}
		});
		var jsonList = {};
		jsonList.text = $("#text").val();
		jsonList.imageUrl = $("#image_url").val();
		jsonList.link = $("#link").val();
		jsonList.gds = $("#gds").val();
		var jsonObj = {};
		jsonObj.name = $("#goods_type_name_path").val();
		jsonObj.value = $("#goods_type_id_path").val();
		jsonList.goodsType = jsonObj;
		jsonList.act = $("#act").val();
		// if (itemType == '10') {
		// 	jsonList = $("#text").val();
		// } else if (itemType == '20') {
		// 	jsonList = $("#image_url").val();
		// } else if (itemType == '40') {
		// 	jsonList = $("#link").val();
		// } else if (itemType == '41') {
		// 	jsonList = $("#gds").val();
		// } else if (itemType == '42') {
		// 	var jsonObj = {};
		// 	jsonObj.name = $("#goods_type_name_path").val();
		// 	jsonObj.value = $("#goods_type_id_path").val();
		// 	jsonList = jsonObj;
        //
		// } else if (itemType == '43') {
		// 	jsonList = $("#act").val();
		// }
		json.listJson = jsonList;
		
		axse("/cms_master/save.json", {"data": JSON.stringify(json)}, saveSuccessFn, saveErrorFn);

	});



	//图片的点击事件
	$('img').on("click", function() {
		$("#image_img_file").click();
	});

	//当文件变换的事件
	$('#image_img_file').on("change", function(e) {
		processImage(this, $('#img'));

	});

	//商品分类商品选择按钮的点击事件
	$('.icon-search').click(function() {
		var attr = $('#selectGoodsType').css('display');
		if(attr == 'none'){
			$('#selectGoodsType').show();
		}else{
			$('#selectGoodsType').hide();
		}
	});

	//商品品牌选择
	$('.select2_category').select2({
		placeholder: "Select an option",
		allowClear: true
	});

	$('#selectGoodsType').hide();
	$(".change_class").hide();
	$("#item_type").trigger("change");
	//商品分类选择不可编辑
	$('#goods_type_name_path').attr('disabled','disabled');
});

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
			ajaxPost('/gds_type/getTreeList.json', {}, callback, successFn, failFn, null);
		}
	}
}();

//获得码表数据方法
function getComCode() {
	//品牌类型码表中的数据code为GDS_TYPE 每一个码表的code不一样
	axse("/common/getCodeList.json", {"data":"CMS_ITEM_TYPE"}, codeListSuccessFn, errorFn);
}

//获取码表数据成功的回调方法
function codeListSuccessFn(data) {
	if (data.resultCode == '00') {
		var array = data.data;
		if (array != null) {
			//为商品分类选择框添加数据
			$.each(array, function(index, item) {
				var $option = $('<option>').attr('value', item.value).text(item.displayCn);
				$("#item_type").append($option);
			});
			$('#item_type').change(function () {
				$(".change_class").hide();
				var types = $("#item_type").val();
				$(".change_class").each(function(){
					var itemType = $(this).attr("itemType");
					if (types != null && types.length > 0) {
						for (var i = 0; i < types.length; i++) {
							if (types[i] && types[i] == itemType) {
								$(this).show();
							}
						}
					}

				});
				console.log($(this).val());
			})
		}
	}else{
		showAlert('码表数据获取失败');
	}
}

//获得商品数据方法
function getGdsList() {
	//品牌类型码表中的数据code为GDS_TYPE 每一个码表的code不一样
	axse("/gds_master/getAllList.json", {}, gdsListSuccessFn, errorFn);
}

//获取码表数据成功的回调方法
function gdsListSuccessFn(data) {
	if (data.resultCode == '00') {
		var array = data.data;
		if (array != null) {
			//为商品分类选择框添加数据
			$.each(array, function(index, item) {
				var $option = $('<option>').attr('value', item.goodsId).text(item.goodsName);
				$("#gds").append($option);
			});
		}
	}else{
		showAlert('商品数据获取失败');
	}
}

//获得商品数据方法
function getActList() {
	//品牌类型码表中的数据code为GDS_TYPE 每一个码表的code不一样
	axse("/act_master/getAllList.json", {}, actListSuccessFn, errorFn);
}

//获取码表数据成功的回调方法
function actListSuccessFn(data) {
	if (data.resultCode == '00') {
		var array = data.data;
		if (array != null) {
			//为活动选择框添加数据
			$.each(array, function(index, item) {
				var $option = $('<option>').attr('value', item.activityId).text(item.activityName);
				$("#act").append($option);
			});
		}
	}else{
		showAlert('码表数据获取失败');
	}
}

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
			$("#image_url").val(data.url);
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
			var types = aData.itemType.split(',');
			$("#item_type").val(types);
			$("#item_type").trigger("change");
			$("#item_type").multipleSelect('refresh');
			var jsonList = JSON.parse(aData.listJson);
			$("#text").val(jsonList.text);
			$("#img").attr("src", baseUri + IMAGE_URL + CMS_MASTER_URL + jsonList.imageUrl);
			$("#image_url").val(jsonList.imageUrl);
			$("#link").val(jsonList.link);
			$("#gds").val(jsonList.gds);
			$("#gds").trigger("change");
			var jsonObj = jsonList.goodsType;
			$("#goods_type_name_path").val(jsonObj.name);
			$("#goods_type_id_path").val(jsonObj.value);
			$("#act").val(jsonList.act);
			$("#act").trigger("change");
			// if (aData.itemType == '10') {
			// 	$("#text").val(aData.listJson);
			// } else if (aData.itemType == '20') {
			// 	$("#img").attr("src", baseUri + IMAGE_URL + CMS_MASTER_URL + aData.listJson);
			// 	$("#image_url").val(aData.listJson);
			// } else if (aData.itemType == '40') {
			// 	$("#link").val(aData.listJson);
			// } else if (aData.itemType == '41') {
			// 	$("#gds").val(aData.listJson);
			// 	$("#gds").trigger("change");
			// } else if (aData.itemType == '42') {
			// 	var jsonObj = JSON.parse(aData.listJson);
			// 	$("#goods_type_id_path").val(jsonObj.value);
			// 	$("#goods_type_name_path").val(jsonObj.name);
			// } else if (aData.itemType == '43') {
			// 	$("#act").val(aData.listJson);
			// 	$("#act").trigger("change");
			// }

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
