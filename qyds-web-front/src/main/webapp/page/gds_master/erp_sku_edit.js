var userId = sessionStorage.getItem("userId");
var goodsId = sessionStorage.getItem("goodsId");

$(document).ready(function() {

	//颜色相信信息获取
	getEditData();

	//保存按钮点击事件
	$("#save_btn").click(function(){

		//其他验证
		if (!customerCheck()){
			return;
		}
        //
		var json = {};
		json.goodsColoreId = $("#goods_colore_id").val();
		json.goodsId = goodsId;

		//图片信息
		var imageArray = "";
		$.each($("#sku_image_div input[type='text']"), function(index, item) {
			imageArray = imageArray + $("#"+item.id).val() + ",";
		});
		json.imageUrlJson = imageArray;
		//
		//var array = new Array();
		//$.each($("#sku_image_div input[type='text']"), function(index, item) {
		//	array.push($("#"+item.id).val());
		//});
		//json.imageUrlJson = array;
		axse("/gds_color/save.json", {'data':JSON.stringify(json)}, saveSuccessFn, errorFn);

	});

	//取消按钮的点击事件
	$('#cancel_btn').click(function(){
		//删除掉所有的事件
		$.each($('#sku_image_div .img_clone'), function(index, item) {
			$("#logo_img_"+index).die('click');
			$("#logo_img_file_"+index).die('change');
		});

		$("#colorManage").modal('hide');
	});

	//sku属性添加按钮的点击事件
	$('#icon-plus-sign-sku').click(function(){


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
		type : "GDS_COLOR",
		file : data,
		fileName : fileName,
		suffix : suffix
	};

	var success = function(data) {
		if (data.resultCode == '00') {
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
}


//退回到列表画面
function gotoListPage(){
	$('#content').load('gds_master/list.html');
}

//获取详细信息
function getEditData(){
	axse("/gds_master/skuEdit.json", {"goodsId":goodsId}, eidtSuccessFn, errorFn);
}

//获取详细信息成功的回调 用来显示
function eidtSuccessFn(data){
	if (data.resultCode == '00') {
		var item = data.data;

		$('#goods_id').val(item.goodsId);
		$('#type').text(item.typeName);
		if(item.brandName == null){
			$('#brand_id').text(item.erpStyleNo);
		}else{
			$('#brand_id').text(item.brandName);
		}
		$('#goods_type').text(item.goodsTypeName);
		$('#erp_style_no').text(item.erpStyleNo);
		$('#goodsCode').text(item.goodsCode);
		$('#goods_name').text(item.goodsName);
		$('#erp_goods_code').text(item.erpGoodsCode);
		$('#erp_goods_name').text(item.erpGoodsName);
		$('#maintain_status').text(item.maintainStatusName);
		$('#is_onsell').text(item.isOnsell=="0"?"是":"否");
		$('#is_waste').text(item.isWaste=="0"?"是":"否");
		$('#search_key').text(item.searchKey);

		//颜色列表
		var colorList = data.colorList;
		//根据数据组成显示表格
		tableDisplay(colorList);

	}else{
		showAlert('ERP商品颜色信息获取失败');
	}
}

//根据数据显示table
function tableDisplay(colorList){

	$('#body').empty();
	for(var i = 0; i < colorList.length; i++){
		var row = '<tr class="row_tr">';
		var coloreCode = colorList[i].coloreCode;
		var coloreName = colorList[i].coloreName;
		var goodsColoreId = colorList[i].goodsColoreId;
		row = row + '<td>'+coloreCode+'</td>' + '<td>'+coloreName+'</td>' + '<td><a class="color" href="#" onclick="doColorConfirm(this);" id="' + goodsColoreId + '">配置</a></td>';
		row = row + '</tr>';
		$('#body').append($(row));
	}

}

//点击配置的方法
function doColorConfirm(item){
	$('#colorForm')[0].reset();
	$("#colorForm").validate().resetForm();
	$("#goods_colore_id").val(item.id);
	$('#colorManage').modal(modalSettings);
	//获取图片信息
	axse("/gds_color/edit.json", {"goodsColoreId":item.id}, editSuccessFn, errorFn);
}

//数据编辑的回调方法
function editSuccessFn(data){
	if (data.resultCode == '00') {
		$('#sku_image_div').empty();
		if(data.data != null){
			//var imageUrlJson = JSON.parse(data.data.imageUrlJson);
			var imageUrlJson = data.data.imageUrlJson.split(",");
			if(imageUrlJson != null){
				for(var i = 0; i < imageUrlJson.length - 1; i++){
					var img_clone = $('#img_clone').clone();
					$(img_clone).css('display','block');

					$("#logo_img",img_clone).attr("id","logo_img_"+i);
					$("#logo_img_file",img_clone).attr("id","logo_img_file_"+i);
					$("#logo_url",img_clone).attr("id","logo_url_"+i);

					//事件追击
					//图片的点击事件
					$("#logo_img_"+i).live('click', function () {
						var idx = $(this)[0].id.split('_')[2];
						$("#logo_img_file_"+idx).click();
					});

					//当文件变换的事件
					$('#logo_img_file_'+i).live('change',function(e) {
						var idx = $(this)[0].id.split('_')[3];
						processImage(this, $('#logo_img_'+idx),idx);
					});

					$('#sku_image_div').append(img_clone);
					//url值
					$("#logo_url_"+i).val(imageUrlJson[i]);
					$("#logo_img_"+i).attr("src", displayUri + orignal  + imageUrlJson[i]);
				}
			}
		}

	}else{
		showAlert('颜色的图片数据获取失败')
	}
}
//
//数据保存成功的回调方法
function saveSuccessFn(data){

	if (data.resultCode == '00') {
		$("#colorManage").modal('hide');
		//删除掉所有的事件
		$.each($('#sku_image_div .img_clone'), function(index, item) {
			$("#logo_img_"+index).die('click');
			$("#logo_img_file_"+index).die('change');
		});
	}else{
		showTip('sku数据保存失败');
	}
}

// 失败的回调方法
function errorFn() {
	showAlert('数据获取失败');
}

//属性删除的点击事件
function removeSpecification(obj){

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

	$('#icon-plus-sign-sku').parent().parent().removeClass('has-error');
	$('#icon-plus-sign-sku').parent().find('.help-block').remove();

	var re = true;
	//图片区域的图片个数
	var count = $('#sku_image_div .img_clone').length;
	if(count == 0){
		$('#icon-plus-sign-sku').parent().parent().addClass('has-error');
		$('#icon-plus-sign-sku').parent().append('<span for="goods_type_name_path" class="help-block">请维护颜色的图片.</span>');
		re = false;
	}else{

		//判断添加的图片中有没有没有上传的图片存在
		var isExistEmptyFile = false;
		$.each($('#sku_image_div .img_clone'), function(index, item) {
			if($("#logo_url_"+index).val() == null
				|| $("#logo_url_"+index).val().length == 0){
				isExistEmptyFile = true;
			}
		});

		if(isExistEmptyFile){
			$('#icon-plus-sign-sku').parent().parent().addClass('has-error');
			$('#icon-plus-sign-sku').parent().append('<span for="goods_type_name_path" class="help-block">您添加的图片还没有全部上传图片.</span>');
			re = false;
		}else{
			$('#icon-plus-sign-sku').parent().parent().removeClass('has-error');
			$('#icon-plus-sign-sku').parent().find('.help-block').remove();
		}
	}
	return re;
}

function showTip(message) {
	$("#sku_edit_tip").text(message);
	setTimeout(function () {
		$("#sku_edit_tip").text("");
	}, 2000);
}