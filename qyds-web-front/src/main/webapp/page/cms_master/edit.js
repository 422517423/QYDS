var imgNoIndex = 1;
var userId = sessionStorage.getItem("userId");
var itemId;
var cmsId = sessionStorage.getItem("cmsId");
// var masterActGoodsId;
$(document).ready(function() {

	$('#selectItems').hide();
	$('#selectGoodsType').hide();
	$(".items_class").hide();
	$(".change_class").hide();
	$("#image_error_msg").hide();
	$("#goods_list_add_area").hide();
	$("#goods_list_area").hide();
	$("#goods_list_error_msg").hide();
	$("#items_type_error_msg").hide();
	// $("#act_gds_multiple_area").hide();
	//商品分类选择不可编辑
	$('#goods_type_name_path').attr('disabled','disabled');

	//初始化富文本区域
	initEditor();

	// 获取栏目形式
	getComCode();
	// // 商品列表
	// getGdsList();
	// // 多选商品列表
	// getMultipleGdsList();
	// 获取商品分类
	getGdsComCode();
	// 活动列表
	getActList();
	// 抽奖列表
	getPrizeDrawList();
	// 获取商品分类列表
	getGoodsTypeList();
	// // 多选商品列表
	// getActGdsList('no');
	// 获取选中商品
	tableDisplay();

	//表单验证的定义
	$("#gds_type_form").validate({
		errorElement: 'span', //default input error message container
		errorClass: 'help-block', // default input error message class
		focusInvalid: false, // do not focus the last invalid input
		rules: {
			title: {
				required: true
			},
			items_type_name_path: {
				required: true
			},
			text_comment: {
				required: true
			},
			goods_type: {
				required: true,
				remote:{
					type:"POST",
					url:"/qyds-web-front/cms_master/checkGdsTypeId.json",
					dataType: "json",
					data:{
						gdsTypeId:function(){return $("#goods_type").val();},
						itemId:function(){return itemId;},
						cmsId:function(){return $("#cms_id").val();}
					}
				}
			},
			act: {
				required: true
			},
			prizeDraw: {
				required: true
			}
		},
		messages: {
			title: {
				required: "请选输入标题"
			},
			items_type_name_path: {
				required: "请选择栏目"
			},
			text_comment: {
				required: "请输入文字"
			},
			item_url: {
				required: "请输入链接"
			},
			goods_type: {
				required: "请输入商品分类",
				remote:"商品分类已经存在"
			},
			act: {
				required: "请输入活动"
			},
			prizeDraw: {
				required: "请输入抽奖活动"
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

	// 初始化
	if (cmsId && cmsId != null) {
		var json = {};
		json.cmsId = cmsId;

		axse("/cms_master/edit.json", {"data": JSON.stringify(json)}, initSuccessFn, initErrorFn);
	}


	$("#cancel_btn").click(function() {
		gotoListPage();
	});

	//保存按钮点击事件
	$("#save_btn").click(function(){
		$("#image_error_msg").hide();
		$("#image_title_area").css("color", "#000");
		$("#goods_list_error_msg").hide();
		$("#goods_title_area").css("color", "#000");
		$("#items_type_error_msg").hide();
		$("#items_type_area").css("color", "#000");
		if (!$("#gds_type_form").valid()){
			if ($("#items_type_name_path").val() == null || $("#items_type_name_path").val() == "") {
				$("#items_type_error_msg").show();
				$("#items_type_area").css("color", "#b94a48");
			}

			if (!$("#image_url").val() && !$("#image_title_area").is(":hidden")) {
				$("#image_error_msg").show();
				$("#image_title_area").css("color", "#b94a48");
			}


			if (!$("#goods_list_add_area").is(":hidden") && ($("#item_type").val() && $("#item_type").val() == 41)) {
				if ($(".master_goods_delete").length == 0) {
					$("#goods_list_error_msg").show();
					$("#goods_title_area").css("color", "#b94a48");
				}
			}
			return;
		} else {
			var errormsg = true;

			if ($("#items_type_name_path").val() == null || $("#items_type_name_path").val() == "") {
				$("#items_type_error_msg").show();
				$("#items_type_area").css("color", "#b94a48");
				errormsg = false;
			}

			if (!$("#image_url").val() && !$("#image_title_area").is(":hidden")) {
				$("#image_error_msg").show();
				$("#image_title_area").css("color", "#b94a48");
				errormsg = false;
			}

			if (!$("#goods_list_add_area").is(":hidden") && ($("#item_type").val() && $("#item_type").val() == 41)) {
				if ($(".master_goods_delete").length == 0) {
					$("#goods_list_error_msg").show();
					$("#goods_title_area").css("color", "#b94a48");
					errormsg = false;
				}
			}
			if (!errormsg) {
				return;
			}
		}

		var json = {};
		json.cmsId = $("#cms_id").val();
		json.itemId = $("#items_type_id_path").val();
		json.itemType = $("#item_type").val();
		json.title = $("#title").val();
		json.textComment = $("#text_comment").val();
		json.contentHtml = editor.getContent();
		json.comment = $("#comment").val();
		json.itemUrl = $("#item_url").val();
		json.itemCode = $("#items_code_path").val();
		if ($("#cms_id").val() == null || $("#cms_id").val() == "") {
			json.insertUserId = userId;
		}
		json.updateUserId = userId;
		// var itemType;
		// $(".change_class").each(function() {
		// 	if(!$(this).is(':hidden')){
		// 		itemType = $(this).attr("itemType");
		// 	}
		// });
		var jsonList = {};
		// jsonList.text = $("#text").val();
		jsonList.imageUrl = $("#image_url").val();
		jsonList.imageLink = $("#img_link").val();

		var itemType = $("#item_type").val();

		// 放致单选多选  默认单选
		json.goodsIdFlag = '10';

		var jsonValue = '';

		// 选择商品的时候
		if (itemType == '41') {

			// if (!$("#items_code_path").val() || ($("#items_code_path").val() != 'index_figure' && $("#items_code_path").val() != 'index_new')) {
			// 	var goodsIds = '';
			// 	$(".master_goods_delete").each(function() {
			// 		console.log(goodsIds);
			// 		var goodsId = $(this).attr("goodsId");
			// 		if (goodsIds == '') {
			// 			goodsIds = goodsId;
			// 		} else {
			// 			goodsIds = goodsIds + "," + goodsId;
			// 		}
			// 	});
			// 	json.goodsId = goodsIds;
			// 	jsonList.value = goodsIds;
			// } else {
			// 	$(".master_goods_delete").each(function() {
			// 		var goodsId = $(this).attr("goodsId");
			// 		json.goodsId = goodsId;
			// 		jsonList.value = goodsId;
			// 	});
            //
			// }

			var goodsIds = '';
			$(".master_goods_delete").each(function() {
				var goodsId = $(this).attr("goodsId");
				if (goodsIds == '') {
					goodsIds = goodsId;
				} else {
					goodsIds = goodsIds + "," + goodsId;
				}
			});
			json.goodsId = goodsIds;
			jsonList.value = goodsIds;

			// if (!$("#gds_area").is(":hidden")) {
			// 	json.goodsId = $("#gds").val();
			// 	jsonList.value = $("#gds").val();
			// }
            //
			// if (!$("#gds_multiple_area").is(":hidden")) {
			// 	var goodsId = '';
			// 	if ($("#gds_multiple").val()) {
			// 		for (var i = 0; i < $("#gds_multiple").val().length; i ++) {
			// 			if (goodsId == '') {
			// 				goodsId = $("#gds_multiple").val()[i];
			// 			} else {
			// 				goodsId = goodsId + "," + $("#gds_multiple").val()[i];
			// 			}
			// 		}
			// 	}
			// 	// 设置多选
			// 	json.goodsIdFlag = '20';
			// 	json.goodsId = goodsId;
			// }

		// 选择商品分类的时候
		} else if (itemType == '42') {
			jsonList.goodsType = $("#type").val();
			jsonList.text = $("#goods_type_name_path").val();
			// jsonValue = $("#goods_type").val();
			jsonValue = $("#goods_type_id_path").val();
			json.goodsTypeId = jsonValue;
			jsonList.value = jsonValue;
			var goodsIds = '';
			$(".master_goods_delete").each(function() {
				var goodsId = $(this).attr("goodsId");
				if (goodsIds == '') {
					goodsIds = goodsId;
				} else {
					goodsIds = goodsIds + "," + goodsId;
				}
			});
			json.goodsIdFlag = '20';
			json.goodsId = goodsIds;
		// 选择活动的时候
		} else if (itemType == '43') {
			if (!$("#items_code_path").val() || ($("#items_code_path").val() != 'index_figure' && $("#items_code_path").val() != 'index_new')) {
				json.actId = $("#act").val();
				var actGoodsId = '';
				$(".master_goods_delete").each(function() {
					var goodsId = $(this).attr("goodsId");
					if (actGoodsId == '') {
						actGoodsId = goodsId;
					} else {
						actGoodsId = actGoodsId + "," + goodsId;
					}
				});
				// if ($("#act_gds_multiple").val()) {
				// 	for (var i = 0; i < $("#act_gds_multiple").val().length; i ++) {
				// 		if (actGoodsId == '') {
				// 			actGoodsId = $("#act_gds_multiple").val()[i];
				// 		} else {
				// 			actGoodsId = actGoodsId + "," + $("#act_gds_multiple").val()[i];
				// 		}
				// 	}
				// }

				// 设置多选
				json.goodsIdFlag = '20';
				json.goodsId = actGoodsId;
			}
			jsonList.value = $("#act").val();

		} else if(itemType == '47'){
			jsonList.value = $("#prizeDraw").val();
		}


		json.listJson = jsonList;
		axse("/cms_master/save.json", {"data": JSON.stringify(json)}, saveSuccessFn, saveErrorFn);

	});

	$("#act").change(function() {
		// getActGdsList($(this).val());
		$("#body_tj").empty();
		$(this).valid();
	});

	$("#goods_type").change(function() {
		$(this).valid();
	});

	//图片的点击事件
	$('img').on("click", function() {
		$("#image_img_file").click();
	});

	//当文件变换的事件
	$('#image_img_file').on("change", function(e) {
		processImage(this, $('#img'));

	});

	//套装商品维护按钮点击事件
	$('#add_act_goods').click(function(){
		var checkGoodsId = '';
		$(".master_goods_delete").each(function(){
			var goodsId = $(this).attr("goodsId");
			if (checkGoodsId == '') {
				checkGoodsId = goodsId;
			} else {
				checkGoodsId = checkGoodsId + "," + goodsId;
			}
		});

        App.blockUI('body',true);

		if ($("#item_type").val() == '41') {
			sessionStorage.setItem("checkGoodsId", checkGoodsId);
			sessionStorage.setItem("itemsCode", $("#items_code_path").val());
			$('#customDialog').load('cms_master/goods_list.html');

		} else if ($("#item_type").val() == '42') {
			sessionStorage.setItem("checkGoodsId", checkGoodsId);
			sessionStorage.setItem("goodsTypeId", $("#goods_type_id_path").val());
			$('#customDialog').load('cms_master/good_type_goods_list.html');

		} else if ($("#item_type").val() == '43') {
			sessionStorage.setItem("checkGoodsId",checkGoodsId);
			sessionStorage.setItem("actId",$("#act").val());
			$('#customDialog').load('cms_master/act_goods_list.html');
			
		}

        App.unblockUI('body');
	});

	//栏目选择按钮的点击事件
	$('#items_icon_search').click(function() {
		var attr = $('#selectItems').css('display');
		if(attr == 'none'){
			$('#selectItems').show();
		}else{
			$('#selectItems').hide();
		}
	});

	//商品分类商品选择按钮的点击事件
	$('#goods_icon_search').click(function() {
		var attr = $('#selectGoodsType').css('display');
		if(attr == 'none'){
			$('#selectGoodsType').show();
		}else{
			$('#selectGoodsType').hide();
		}
	});

	// cms形式变换的时候
	$('#item_type').change(function () {
		$(".change_class").hide();
		var types = $("#item_type").val();
		$(".change_class").each(function(){
			var itemType = $(this).attr("itemType");
			if (types && types == itemType) {
				$(this).show();
			}
		});

		if(types == '42'){

            if($('#iThpe').val() != 42){
                if(!$('#type').find('option:checked').length){
                    $('#type').find('option').eq(0).attr('selected','selected');
                }
                zTree.init();
                $('.form-group .change_class').hide();
                $('.form-group .change_class[itemtype = "42"]').show();
                $('#selectGoodsType').show();

            }
			if (!$("#items_code_path").val() || ($("#items_code_path").val() != 'index_figure' && $("#items_code_path").val() != 'index_new')) {
				$("#goods_list_add_area").show();
				$("#goods_list_area").show();
			} else {
				$("#goods_list_add_area").hide();
				$("#goods_list_area").hide();
			}
		}

		// // 选择商品的时候
		// if (types == '41') {
		// 	if (!$("#items_code_path").val() || $("#items_code_path").val() != 'index_figure') {
		// 		$("#gds_area").hide();
		// 	} else {
		// 		$("#gds_multiple_area").hide();
		// 	}
		// }

		// 选择活动的时候
		if (types == '43') {
			if (!$("#items_code_path").val() || ($("#items_code_path").val() != 'index_figure' && $("#items_code_path").val() != 'index_new')) {
				$("#goods_list_add_area").show();
				$("#goods_list_area").show();
			} else {
				$("#goods_list_add_area").hide();
				$("#goods_list_area").hide();
			}
		}
	})



	//商品品牌选择
	$('.select2_category').select2({
		placeholder: "Select an option",
		allowClear: true
	});


	$("#save_sort").click(function() {

		var cms_sort = sessionStorage.getItem("cms_sort");
		var sort = $("#sort").val();
		if(sort == null || sort == 0){
			showAlert("请正确输入序号!")
			return;
		}

		var json = {};
		json.cmsGdsId = cms_sort;
		json.sort = sort;

		$("#sortEdit").modal('hide');
		ajaxPost('/cms_master/resetCmsGdsIds.json',
			{'data':JSON.stringify(json)},function (data) {
				if($.type(data) == 'string' && data != ''){
					data = $.parseJSON(data);
				}

				if(data.resultCode == '00'){
					tableDisplay();
				}
			},null,null,null);
	});


	$("#item_type").trigger("change");

});

//栏目树的初始化
var itemZree = function(){
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
									name: "itemName"
								},
								simpleData: {
									enable: true,
									idKey: "itemId",
									pIdKey: "itemIdParent",
									itemName: "itemName",
									itemType: "itemType",
									itemCode : "itemCode"
								}
							}
						};
						//数据初始化
						$.fn.zTree.init($("#cmsItemsTree"),settings, data.data);
					}
				}else{
					//返回失败时候的业务处理
					showAlert('栏目列表获取失败');
				}
			}

			function failFn(){
				//服务器异常处理
				showAlert('栏目列表获取失败');
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

				//确定按钮
				$('#itemsConfirm').on('click',function(){
					var tree = $.fn.zTree.getZTreeObj("cmsItemsTree");
					var selectNode = tree.getSelectedNodes();

					itemId = selectNode[0].itemId;
					var itemName = selectNode[0].itemName;
					var itemType = selectNode[0].itemType;
					var itemCode = selectNode[0].itemCode;

					var itemTypeArray = itemType.split(",");
					$(".items_class").each(function() {
						var type = $(this).attr("itemType");
						for (var i = 0; i < itemTypeArray.length; i ++) {
							if (type && type == itemTypeArray[i]) {
								$(this).show();
								break;
							}
						}
					});

					$("#items_type_name_path").val(itemName);
					$("#items_type_id_path").val(itemId);
					$("#items_code_path").val(itemCode);
					$('#selectItems').hide();

					if ($("#goods_type").val()) {
						$("#goods_type").valid();
					}

					$("#body_tj").empty();

					if ($("#item_type").val() == '43') {
						if (!$("#items_code_path").val() || ($("#items_code_path").val() != 'index_figure' && $("#items_code_path").val() != 'index_new')) {
							$("#goods_list_add_area").show();
							$("#goods_list_area").show();
						} else {
							$("#goods_list_add_area").hide();
							$("#goods_list_area").hide();
						}
					}

					$("#items_type_error_msg").hide();
					$("#items_type_area").css("color", "#000");
				});

			}

			//栏目树加载数据
			ajaxPost('/cms_items/selectAll.json', {}, callback, successFn, failFn, null);
		}
	}
}();

//商品分类树的初始化
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
			// ajaxPost('/gds_type/getTreeList.json', {}, callback, successFn, failFn, null);
			var type = "";
			if($('#type').val() != 30){
				type = $('#type').val();
			}
			ajaxPost('/gds_type/getTreeList.json', {"type":type}, callback, successFn, failFn, null);
		}
	}
}();

//获得码表数据方法
function getComCode() {
	//品牌类型码表中的数据code为GDS_TYPE 每一个码表的code不一样
	axse("/common/getCodeList.json", {"data":"CMS_MASTER_TYPE"}, codeListSuccessFn, errorFn);
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
function getMultipleGdsList() {
	//品牌类型码表中的数据code为GDS_TYPE 每一个码表的code不一样
	axse("/gds_master/getAllList.json", {}, gdsMultipleListSuccessFn, errorFn);
}

//获取码表数据成功的回调方法
function gdsMultipleListSuccessFn(data) {
	if (data.resultCode == '00') {
		var array = data.data;
		if (array != null) {
			//为商品分类选择框添加数据
			$.each(array, function(index, item) {
				var $option = $('<option>').attr('value', item.goodsId).text(item.goodsName);
				$("#gds_multiple").append($option);
			});
		}
		$('#gds_multiple').change(function () {
			// console.log($(this).val());
		}).multipleSelect({
			width: '100%'
		});
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

//获得抽奖数据方法
function getPrizeDrawList() {
	//品牌类型码表中的数据code为GDS_TYPE 每一个码表的code不一样
	axse("/prize_draw/getAllList.json", {}, prizeDrawSuccessFn, errorFn);
}

function prizeDrawSuccessFn(data) {
	if (data.resultCode == '00') {
		var array = data.result;
		if (array != null) {
			//为活动选择框添加数据
			$.each(array, function(index, item) {
				var $option = $('<option>').attr('value', item.prizeDrawId).text(item.prizeDrawName);
				$("#prizeDraw").append($option);
			});
		}
	}else{
		showAlert('码表数据获取失败');
	}
}


//获得商品分类数据方法
function getGoodsTypeList() {
	//品牌类型码表中的数据code为GDS_TYPE 每一个码表的code不一样
	// var json = {};
	// json.activityId = catId;
	// axse("/gds_master/getAllList.json", {"data": JSON.stringify(json)}, gdsActListSuccessFn, errorFn);
	axse("/gds_type/getGdsTypeFirstFloorList.json", {}, gdsTypeListSuccessFn, errorFn);
}

//获取数据成功的回调方法
function gdsTypeListSuccessFn(data) {
	if (data.resultCode == '00') {
		var array = data.results;
		if (array != null) {
			//为商品分类选择框添加数据
			$.each(array, function(index, item) {
				var $option = $('<option>').attr('value', item.goodsTypeId).text(item.goodsTypeNameCn);
				$("#goods_type").append($option);
			});
		}
	}else{
		showAlert('商品数据获取失败');
	}
}

//获得活动商品数据方法
function getActGdsList(catId) {
	//品牌类型码表中的数据code为GDS_TYPE 每一个码表的code不一样
	// var json = {};
	// json.activityId = catId;
	// axse("/gds_master/getAllList.json", {"data": JSON.stringify(json)}, gdsActListSuccessFn, errorFn);
	axse("/gds_master/selectAllByActId.json", {"activityId" : catId}, gdsActListSuccessFn, errorFn);
}

//获取数据成功的回调方法
function gdsActListSuccessFn(data) {
	if (data.resultCode == '00') {
		$("#act_gds_multiple").empty();
		var array = data.data;
		if (array != null) {
			//为商品分类选择框添加数据
			$.each(array, function(index, item) {
				var $option = $('<option>').attr('value', item.goodsId).text(item.goodsName);
				$("#act_gds_multiple").append($option);
			});
		}
		$('#act_gds_multiple').change(function () {
			// console.log($(this).val());
		}).multipleSelect({
			width: '100%'
		});
		// $("#act_gds_multiple").val(masterActGoodsId);
		// $("#act_gds_multiple").trigger("change");
		// $("#act_gds_multiple").multipleSelect('refresh');
		// masterActGoodsId = null;
	}else{
		showAlert('商品数据获取失败');
	}
}

//退回到列表画面
function gotoListPage(){
	$('#content').load('cms_master/list.html');
}

//处理图片的方法
function processImage(fileInput, image) {
	var filepath = $(fileInput).val();
	// console.log(filepath);
	var extStart = filepath.lastIndexOf(".");
	var ext = filepath.substring(extStart + 1, filepath.length).toUpperCase();
	var fileName = getFileName(filepath);
	if (ext != "JPG" && ext != "JPEG" && ext != "PNG"  && ext != "GIF") {
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
	// var url = uploadUri;
	var param = {
		type : "CMS_MASTER",
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
	// ajaxPost(url, param, null, success, error, null);
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
			'insertvideo', // 视频
			'map', // Baidu地图
			'source', // 源代码
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
			 'insertimage' // 多图上传
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
	$('#content').load('cms_master/list.html');
}

function saveErrorFn() {

}

// 初始化页面
function initSuccessFn(data) {
	if (data.resultCode == '00') {
		var aData = data.data;
		if (aData != null) {
            $('#iThpe').val(aData.itemType);

			$("#cms_id").val(aData.cmsId);
			$("#title").val(aData.title);
			$("#items_type_id_path").val(aData.itemId);
			$("#items_type_name_path").val(aData.itemName);
			$("#items_code_path").val(aData.itemCode);
			var itemTypeArray = aData.cmsItemType.split(',');
			$(".items_class").each(function() {
				var type = $(this).attr("itemType");
				for (var i = 0; i < itemTypeArray.length; i ++) {
					if (type && type == itemTypeArray[i]) {
						$(this).show();
						break;
					}
				}
			});
			$("#text_comment").val(aData.textComment);
			$("#item_type").val(aData.itemType);
			$("#item_url").val(aData.itemUrl);
			$(".change_class").each(function() {
				var itemType = $(this).attr("itemType");
				if (aData.itemType && aData.itemType == itemType) {
					$(this).show();
				}
			});
			if (aData.itemType == '41') {
				// // 单选
				// if (aData.goodsIdFlag == "10") {
				// 	$("#gds_area").show();
				// 	$("#gds_multiple_area").hide();
				// 	$("#gds").select2().select2("val", aData.goodsId);
				// 	// $("#gds").val(aData.goodsId);
				// 	// $("#gds").trigger("change");
				// } else {
				// 	$("#gds_area").hide();
				// 	$("#gds_multiple_area").show();
				// 	$("#gds_multiple").val(aData.goodsId.split(","));
				// 	// $("#gds_multiple").trigger("change");
				// 	$("#gds_multiple").multipleSelect('refresh');
				// }
				$("#goods_list_add_area").show();
				$("#goods_list_area").show();
				tableDisplay();

			} else if (aData.itemType == '43') {
				// $("#act").val(aData.actId);
				$("#act").select2().select2("val", aData.actId);
				if ("0" == aData.actGoodsFlag) {
					// $("#act").trigger("change");
					// $("#act_gds_multiple_area").show();
					// masterActGoodsId = aData.goodsId.split(",");
					$("#goods_list_add_area").show();
					$("#goods_list_area").show();
					tableDisplay();
				}
			}

			var listJson = JSON.parse(aData.listJson);
			if (listJson != null) {
				// $("#img").attr("src", baseUri + IMAGE_URL + CMS_MASTER_URL + listJson.imageUrl);
				$("#img").attr("src", displayUri + orignal + listJson.imageUrl);
				$("#image_url").val(listJson.imageUrl);
				$("#img_link").val(listJson.imageLink);
				if (aData.itemType == '42') {
					// $("#goods_type").val(listJson.value);
					// $("#goods_type").select2().select2("val", listJson.value);
					// $("#goods_type_name_path").val(listJson.text);
					$("#type").val(listJson.goodsType);
					zTree.init();
					$("#goods_type_name_path").val(listJson.text);
					$("#goods_type_id_path").val(listJson.value);
					// 商品分类二级首页使用
					if ("0" == aData.actGoodsFlag) {
						$("#goods_list_add_area").show();
						$("#goods_list_area").show();
						tableDisplay();
					}
				}else if(aData.itemType == '47'){
					$("#prizeDraw").select2().select2("val", listJson.value);
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

// 初始化表单 获取
function tableDisplay() {

	if ($("#item_type").val() == '41') {
		axse("/cms_master/getMasterGoodsByCmsId.json", {"cmsId": cmsId}, tableDisplaySuccess, errorFn);

	} else if ($("#item_type").val() == '42') {
		axse("/cms_master/getMasterGoodsByCmsId.json", {"cmsId": cmsId}, tableDisplaySuccess, errorFn);

	} else if ($("#item_type").val() == '43') {
		axse("/cms_master/getMasterGoodsByCmsId.json", {"cmsId": cmsId}, tableDisplaySuccess, errorFn);

	}

}

function tableDisplaySuccess(data) {
	if (data.resultCode == '00') {
		$("#body_tj").empty();
		aData = data.data;
		if (aData != null) {
			var item;
			for (var i = 0; i < aData.length; i ++) {
				item = aData[i];
				var row = '<tr class="row_tr">';
				row = row + '<td><input type="checkbox" id="alldel" name="alldel" class="checkboxes alldel" value="1" /></td>';
				row = row + '<td>'+item.typeName+'</td>';
				if(item.brandName == null){
					row = row + '<td></td>';
				}else{
					row = row + '<td>'+item.brandName +'</td>';
				}
				// if(item.erpStyleNo == null){
				// 	row = row + '<td></td>';
				// }else{
				// 	row = row + '<td>'+item.erpStyleNo +'</td>';
				// }
				row = row + '<td>'+item.goodsTypeName+'</td>';
				row = row + '<td>'+item.goodsCode+'</td>';
				row = row + '<td>'+item.goodsName+'</td>';
				row = row + '<td>'+item.sort+'</td>';
				// row = row + '<td>'+item.erpGoodsName+'</td>';
				row = row + '<td><a class="master_goods_delete" id="'+ item.goodsId +'" goodsId="' + item.goodsId + '" onclick="goods_delete(this)" href="javascript:;">删除</a>' +
					'&nbsp;&nbsp;<a class="master_sort_input" id="'+ item.cmsGdsId +'" sortId="' + item.sort + '" onclick="inputSort(this)" href="javascript:;">填写序号</a>' +
					'&nbsp;&nbsp;<a class="master_goods_up" id="'+ item.cmsGdsId +'" sortId="' + item.sort + '" onclick="goods_up(this)" href="javascript:;">上移</a>' +
					'&nbsp;&nbsp;<a class="master_goods_up" id="'+ item.cmsGdsId +'" sortId="' + item.sort + '" onclick="goods_down(this)" href="javascript:;">下移</a>' +
					'</td>';
				row = row + '</tr>';
				$('#body_tj').append($(row));
			}

		}
	}
}

// 选取商品，获取商品信息
function addGoodsForTable(goodsIds) {
	axse("/gds_master/getGoodsByGoodsId.json", {"goodsIds": goodsIds}, addGoodsForTableSuccess, errorFn);
}

function addGoodsForTableSuccess(data) {
	if (data.resultCode == '00') {
		aData = data.data;
		if (aData != null) {
			var item;
			for (var i = 0; i < aData.length; i ++) {
				item = aData[i];
				var row = '<tr class="row_tr">';
				row = row + '<td></td>';
				row = row + '<td>'+item.typeName+'</td>';
				if(item.brandName == null){
					row = row + '<td></td>';
				}else{
					row = row + '<td>'+item.brandName +'</td>';
				}
				row = row + '<td>'+item.goodsTypeName+'</td>';
				row = row + '<td>'+item.goodsCode+'</td>';
				row = row + '<td>'+item.goodsName+'</td>';
				row = row + '<td></td>';
				// row = row + '<td>'+item.erpGoodsCode+'</td>';
				// row = row + '<td>'+item.erpGoodsName+'</td>';
				row = row + '<td><a class="master_goods_delete" id="'+ item.goodsId +'" goodsId="' + item.goodsId + '" onclick="goods_delete(this)" href="javascript:;">删除</a></td>';
				row = row + '</tr>';
				$('#body_tj').append($(row));
			}

		}
	}
}

//点击删除按钮
function goods_delete(obj) {
	showConfirm('确认删除该商品吗?',function(){
		$(obj).parent().parent().remove();
	});
}


// 点击批量删除的按钮
function all_delete(){
	showConfirm('确认要删除选择的商品吗?',function(){
		$('.alldel').each(function (i){
			if($(this).attr("checked") == 'checked'){
				$(this).parent().parent().remove();
			}
		});
	});
}

//上移
function goods_up(obj){
console.log($(obj).parent().parent().prev());
	var data = new Array();
	data.push({cmsGdsId: $('.master_goods_up',$($(obj).parent().parent().prev())).attr('id'), sort: $(obj).attr("sortId")});
	data.push({cmsGdsId: $(obj).attr("id"), sort: $('.master_goods_up',$($(obj).parent().parent().prev())).attr('sortId')});


	ajaxPost('/cms_master/resortCmsGdsIds.json',
		{'data':JSON.stringify(data)},function (data) {
			if($.type(data) == 'string' && data != ''){
				data = $.parseJSON(data);
			}

			if(data.resultCode == '00'){
				tableDisplay();
			}
		},null,null,null);

}

//点击编辑序号的按钮修改事件
function inputSort(obj){

	var cms_sort = obj.id;
	$("#sortEdit").modal();
	$("#sortEdit").modal(modalSettings);
	$("#sortEdit").modal('show');
	sessionStorage.setItem("cms_sort",cms_sort);
}


//下移
function goods_down(obj){
	console.log($(obj).parent().parent().next());
	var data = new Array();
	data.push({cmsGdsId: $('.master_goods_up',$($(obj).parent().parent().next())).attr('id'), sort: $(obj).attr("sortId")});
	data.push({cmsGdsId: $(obj).attr("id"), sort: $('.master_goods_up',$($(obj).parent().parent().next())).attr('sortId')});


	ajaxPost('/cms_master/resortCmsGdsIds.json',
		{'data':JSON.stringify(data)},function (data) {
			if($.type(data) == 'string' && data != ''){
				data = $.parseJSON(data);
			}

			if(data.resultCode == '00'){
				tableDisplay();
			}
		},null,null,null);

}



//获得码表数据方法
function getGdsComCode() {
	//品牌类型码表中的数据code为GDS_TYPE 每一个码表的code不一样
	axse("/common/getCodeList.json", {"data":"GDS_TYPE"}, gdsListSuccessFn, gdsErrorFn);
}

//获取码表数据成功的回调方法
function gdsListSuccessFn(data) {
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
			// //商品分类管理加载数据(前台变化)
			// ajaxPost('/gds_type/getTreeList.json', {'type':$('#type').val()}, callback, successFn, failFn, null);
			zTree.init();
		});


		// //商品分类管理加载数据(默认)
		// ajaxPost('/gds_type/getTreeList.json', {'type':$('#type').val()}, callback, successFn, failFn, null);

	}else{
		showAlert('码表数据获取失败');
	}
}

// 失败的回调方法
function gdsErrorFn() {
	showAlert('数据获取失败');
}