
var editor = null;
var userId = sessionStorage.getItem("userId");
var itemId = sessionStorage.getItem("itemId");
var itemIdParent = sessionStorage.getItem("itemIdParent");
var itemNameParent = sessionStorage.getItem("itemNameParent");

$(document).ready(function() {

	//获取码表数据
	getComCode();

	// 上级菜单名称
	$("#item_parent_name").val(itemNameParent);
	$("#item_parent_id").val(itemIdParent);

	if (itemId && itemId != "") {
		var json = {};
		json.itemId = itemId;
		axse("/cms_items/edit.json", {'data':JSON.stringify(json)}, editSuccessFn, errorFn);
	}

	//表单验证的定义
	$("#cms_items_form").validate({
		errorElement: 'span', //default input error message container
		errorClass: 'help-block', // default input error message class
		focusInvalid: false, // do not focus the last invalid input
		rules: {
			item_type: {
				required: true
			},
			item_code: {
				required: true,
				remote:{
					type:"POST",
					url:"/qyds-web-front/cms_items/checkItemCode.json",
					dataType: "json",
					data:{
						itemCode:function(){return $("#item_code").val();},
						itemId:function(){return itemId;}
					}
				}
			},
			item_name: {
				required: true
			},
			itme_full_name: {
				required: true
			}

		},
		messages: {
			type: {
				item_type: "请选择栏目形式"
			},
			item_code: {
				required: "请输入栏目CODE",
				remote: "栏目CODE不能重复"
			},
			item_name: {
				required: "请输入栏目名称"
			},
			itme_full_name: {
				required: "请输入栏目全称"
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
		if (!$("#cms_items_form").valid()){
			return;
		}

		var json = {};
		json.itemId = itemId;
		json.itemIdParent = itemIdParent;

		var itemType = '';
		for (var i =0; i < $("#item_type").val().length; i ++) {
			if (itemType == '') {
				itemType = $("#item_type").val()[i];
			} else {
				itemType = itemType + ',' + $("#item_type").val()[i];
			}
		}
		json.itemType = itemType;
		json.itemCode = $("#item_code").val();
		json.itemName = $("#item_name").val();
		json.itmeFullName = $("#itme_full_name").val();
		json.comment = $("#comment").val();
		if (itemId == null || itemId == "") {
			json.insertUserId = userId;
		}
		json.updateUserId = userId;
		axse("/cms_items/save.json", {'data':JSON.stringify(json)}, saveSuccessFn, errorFn);

	});

	//取消按钮的点击事件
	$('#cancel_btn').click(function(){
		gotoListPage();
	});

});

//退回到列表画面
function gotoListPage(){
	$('#content').load('cms_items/list.html');
}

//获得码表数据方法
function getComCode() {
	//品牌类型码表中的数据code为GDS_TYPE 每一个码表的code不一样
	axse("/common/getCodeList.json", {"data":"CMS_ITEM_TYPE"}, codeListSuccessFn, errorFn);
}

//获取详细信息
function getEditData(){
	axse("/gds_type/edit.json", {"goodsTypeId":goodsTypeId}, eidtSuccessFn, errorFn);
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
		$('#item_type').change(function () {
			console.log($(this).val());
		}).multipleSelect({
			width: '100%'
		});
	}else{
		showAlert('码表数据获取失败');
	}
}

//获取详细信息成功的回调 用来显示
function eidtSuccessFn(data){
	if (data.resultCode == '00') {

		//画面元素赋予初始值
		var item = data.data;

	}else{
		showAlert('品牌数据获取失败');
	}
}

//数据保存成功的回调方法
function saveSuccessFn(){
	gotoListPage();
}

//数据保存成功的回调方法
function editSuccessFn(data){
	if (data.resultCode == '00') {
		var nData = data.data;
		if (nData != null) {
			var types = nData.itemType.split(",");
			$("#item_type").val(types);
			$("#item_type").trigger("change");
			$("#item_type").multipleSelect('refresh');
			$("#item_code").val(nData.itemCode);
			$("#item_name").val(nData.itemName);
			$("#itme_full_name").val(nData.itmeFullName);
			$("#comment").val(nData.comment);
		}
	}
	
}

// 失败的回调方法
function errorFn() {
	showAlert('数据获取失败');
}