var userId = sessionStorage.getItem("userId");
var goodsId = sessionStorage.getItem("goodsId");

$(document).ready(function() {

	//sku相信信息获取
	getEditData();

	//表单验证的定义
	$("#skuForm").validate({
		errorElement: 'span', //default input error message container
		errorClass: 'help-block', // default input error message class
		focusInvalid: false, // do not focus the last invalid input
		rules: {
			price_edit: {
				required: true
			},
			safe_bank_edit: {
				required: true
			},act_bank:{
				required: true
			}
		},
		messages: {
			price_edit: {
				required: "请输入市场价格"
			},
			safe_bank_edit: {
				required: "请输入安全库存"
			},act_bank:{
				required: "请输入实际库存"
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
		if (!$("#skuForm").valid()){
			return;
		}
		//其他验证
		if (!customerCheck()){
			return;
		}
        //
		var json = {};
		json.goodsId = $("#goods_id").val();
		json.goodsSkuId = $("#goods_sku_id").val();
		json.price = $("#price_edit").val();
		json.safeBank = $("#safe_bank_edit").val();
		var jsonSpe = {};
		jsonSpe.sku_key = $("#sku_key").val();
		var array = new Array();
		$.each($("#sku_image_div input[type='text']"), function(index, item) {
			array.push($("#"+item.id).val());
		});
		jsonSpe.sku_img = array;
		jsonSpe.sku_value = $("#sku_name").val();
		jsonSpe.sku_display = $("#sku_display").val();
		json.sku = jsonSpe;
		json.insertUserId = userId;
		json.updateUserId = userId;
		json.actBank = $('#act_bank').val();
		axse("/gds_sku/save.json", {'data':JSON.stringify(json)}, saveSuccessFn, errorFn);

	});

	//取消按钮的点击事件
	$('#cancel_btn').click(function(){
		//删除掉所有的事件
		$.each($('#sku_image_div .img_clone'), function(index, item) {
			$("#logo_img_"+index).die('click');
			$("#logo_img_file_"+index).die('change');
		});
		$("#skuManage").modal('hide');
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
	var url = "/common/uploadImage.json";
	var param = {
		type : "GDS_SKU",
		file : data,
		fileName : fileName,
		suffix : suffix
	};

	var success = function(data) {
		if (data.resultCode == '00') {
			img.attr("src", displayUri + orignal  + data.data);
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

//组成table的数据准备
//排列组合之前的数据
var jsonTextSpeSku;
//排列组合之后的数据
var finalKeyArray;

//获取详细信息成功的回调 用来显示
function eidtSuccessFn(data){
	if (data.resultCode == '00') {
		var item = data.data;

		$('#goods_id').val(item.goodsId);
		$('#type').text(item.typeName);
		$('#brand_id').text(item.brandName);
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

		//sku属性
		var property = JSON.parse(item.propertyJson);
		jsonTextSpeSku = JSON.parse(property.jsonTextSpeSku);

		//根据网络请求的数据排列组合得到排列后的数据
		finalKeyArray = plzh(jsonTextSpeSku);


		//根据数据组成显示表格
		tableDisplay(finalKeyArray,jsonTextSpeSku,data.gdsSkuList);



	}else{
		showAlert('商品SKU信息获取失败');
	}
}

//根据网络请求的数据排列组合得到排列后的数据
function plzh(jsonTextSpeSku){
	var skuSelectKey = "";
	//sku便利数据组合-开始
	var skuMutiArray = new Array();
	//单个的sku种类数组
	var skuSimpleArray = new Array();
	for(var i=0;i<jsonTextSpeSku.length;i++){
		//第一次加入到单个的sku数组里边
		if(i == 0){
			skuSimpleArray.push(jsonTextSpeSku[i]);
			if(i == jsonTextSpeSku.length - 1){
				//如果不等加入到复数的sku数组里边
				skuMutiArray.push(skuSimpleArray);
			}
		}else{
			//如果相等加入到单个的sku数组里边
			if(skuSelectKey == jsonTextSpeSku[i].specification_sku_ascii){
				skuSimpleArray.push(jsonTextSpeSku[i]);
			}else{
				//如果不等加入到复数的sku数组里边
				skuMutiArray.push(skuSimpleArray);
				//重新开始添加单个的sku数组
				skuSimpleArray = new Array();
				skuSimpleArray.push(jsonTextSpeSku[i]);
			}

			//最后一个的时候也要加入到大数组里边
			if(i == jsonTextSpeSku.length - 1){
				//如果不等加入到复数的sku数组里边
				skuMutiArray.push(skuSimpleArray);
			}
		}

		skuSelectKey = jsonTextSpeSku[i].specification_sku_ascii;
	}

	//需要的分组数组组合完成之后 递归拼接出表格数据
	var ret = "";
	var sa = skuMutiArray[0];
	var rrList = skuMutiArray.slice(1);

	//排列组合侯的数组
	var keyArray = new Array();
	for (var i = 0;i < sa.length; i++) {
		ret = "";
		var ss = sa[i];
		ret += tt(ss, rrList);
		keyArray.push(ret);
	}


	//排列组合数据再加工
	var finalKeyArray = new Array();
	for(var i = 0; i<keyArray.length; i++){
		var str = keyArray[i];
		if(typeof str.split("||") != ""){
			var array = str.split("||");
			for(var j = 0; j < array.length; j++){
				if(array[j].length > 0){
					finalKeyArray.push(array[j]);
				}
			}
		}else{
			finalKeyArray.push(str);
		}
	}

	return finalKeyArray;
}

//根据数据显示table
//skudata代表sku编辑的时候
function tableDisplay(finalKeyArray,jsonTextSpeSku,gdsSkuList){

	$('#title').empty();
	$('#body').empty();
	//标题添加
	$('#title').append('<th>SKUID</th>');
	var colomn_name = "";
	for(var i = 0; i < finalKeyArray.length; i++){
		var item = finalKeyArray[i];
		var array = item.split(",");

		//列数据
		if(i == 0){
			for(var j = 0; j < array.length; j++){
				var specification_value_sku_ascii = array[j];
				//获取列的名称
				var specification_sku = getColumnName(specification_value_sku_ascii,jsonTextSpeSku);
				colomn_name = colomn_name + specification_sku + '_';
				$('#title').append('<th>'+specification_sku+'</th>');
			}
			$('#title').append('<th>配置</th>');
		}
		var row = '<tr class="row_tr">';


		//用于回显的数据重新描画
		if(gdsSkuList == null){
			//SKUID列
			row = row + '<td></td>';
		}else{
			var isReadySet = false;

			for(var j = 0; j < gdsSkuList.length; j++ ){
				//SKU配置回显示
				var skudata = gdsSkuList[j];
				var sku = JSON.parse(skudata.sku);
				var sku_key = sku.sku_key;
				sku_key = sku_key.substring(0,sku_key.length - 1);
				sku_key = sku_key.replace(/_/g, ",");
				if(sku_key == item){
					row = row + '<td>'+skudata.goodsSkuId+'</td>';
					isReadySet = true;
					break;
				}
			}
			//没有被设定过的显示空
			if(!isReadySet){
				row = row + '<td></td>';
			}
		}

		//行数据
		var specification_value_key = "";
		var specification_value_sku_value = "";
		for(var j = 0; j < array.length; j++){
			var specification_value_sku_ascii = array[j];
			specification_value_key = specification_value_key + specification_value_sku_ascii + "_";
			//获取行的名称
			var specification_value_sku = getRowName(specification_value_sku_ascii,jsonTextSpeSku);
			specification_value_sku_value = specification_value_sku_value + specification_value_sku + "_";
			row = row + '<td>'+specification_value_sku+'</td>';
		}

		row = row + '<td><a class="sku_new" type="'+colomn_name+'" id="'+specification_value_key+'" name="'+specification_value_sku_value+'" onclick="sku_new(this);" href="javascript:;">配置</a></td>';
		row = row + '</tr>';
		$('#body').append($(row));

	}
}

//根据sku_ascii获取行名称
function getRowName(specification_value_sku_ascii,jsonTextSpeSku){

	var specification_value_sku = "";
	for(var i=0; i<jsonTextSpeSku.length; i++){
		var sku_ascii = jsonTextSpeSku[i].specification_value_sku_ascii;
		if(sku_ascii == specification_value_sku_ascii){
			specification_value_sku = jsonTextSpeSku[i].specification_value_sku;
			break;
		}
	}
	return specification_value_sku;
}

//根据sku_ascii获取列名称
function getColumnName(specification_value_sku_ascii,jsonTextSpeSku){

	var specification_sku = "";
	for(var i=0; i<jsonTextSpeSku.length; i++){
		var sku_ascii = jsonTextSpeSku[i].specification_value_sku_ascii;
		if(sku_ascii == specification_value_sku_ascii){
			specification_sku = jsonTextSpeSku[i].specification_sku;
			break;
		}
	}
	return specification_sku;
}


//数据组合的递归方法
function tt(str,rList){
	var ret = "";

	if (rList.length > 1) {
		var sa = rList[0];
		var rrList = rList.slice(1);
		for (var i = 0;i < sa.length; i++) {
			var ss =  "";
			if(typeof str == "string"){
				if(str.lastIndexOf('||') != -1){
					str = str.substring(0,str.length -2);
				}
				ss = str + "," + sa[i].specification_value_sku_ascii + "||" ;
			}else{
				ss = str.specification_value_sku_ascii + "," + sa[i].specification_value_sku_ascii + "||" ;
			}
			ret += tt(ss, rrList);
		}
	} else if (rList.length == 1) {
		var sa = rList[0];
		for (var i = 0;i < sa.length; i++) {

			var ss =  "";
			if(typeof str == "string"){
				if(str.lastIndexOf('||') != -1){
					str = str.substring(0,str.length -2);
				}
				ss = str + "," + sa[i].specification_value_sku_ascii + "||" ;
			}else{
				ss = str.specification_value_sku_ascii + "," + sa[i].specification_value_sku_ascii + "||" ;
			}

			ret += ss;
		}
	} else {
		ret = str.specification_value_sku_ascii;
	}
	return ret;
}

//点击配置的方法
function sku_new(item){
	$('#skuForm')[0].reset();
	$("#skuForm").validate().resetForm();
	$("#sku_key").val(item.id);
	$("#sku_name").val(item.name);
	$("#sku_display").val($(item).attr('type'));
	$('#skuManage').modal(modalSettings);

	var skuId = $(item).parent().parent().find('td:eq(0)').text();
	//配置已经配置过的sku
	axse("/gds_sku/edit.json", {"goodsSkuId":skuId,"skuKey":$("#sku_key").val(),"goodsId":$('#goods_id').val()}, editSuccessFn, errorFn);

}

//数据编辑的回调方法
function editSuccessFn(data){
	if (data.resultCode == '00') {
		//删除掉所有的事件
		$.each($('#sku_image_div .img_clone'), function(index, item) {
			$("#logo_img_"+index).die('click');
			$("#logo_img_file_"+index).die('change');
		});
		$('#sku_image_div').empty();
		if(data.data != null){
			$('.act_bank').hide();
			$('#goods_sku_id').val(data.data.goodsSkuId);
			$('#price_edit').val(data.data.price);
			$('#safe_bank_edit').val(data.data.safeBank);
			var sku = data.data.sku;
			var sku_json = JSON.parse(sku);
			var imgs = sku_json.sku_img;
			//回显图片列表
			//对img容器中的所有图片重新排序id重新绑定事件
			for(var i = 0; i < imgs.length; i++){
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
				$("#logo_url_"+i).val(imgs[i]);
				$("#logo_img_"+i).attr("src", displayUri + orignal  + imgs[i]);

			}
		}else{

			$('.act_bank').show();
			//默认图片设定
			var imgUri = data.imgUri;
			if(imgUri != null && imgUri.length > 0){
				var img_clone = $('#img_clone').clone();
				$(img_clone).css('display','block');

				$("#logo_img",img_clone).attr("id","logo_img_0");
				$("#logo_img_file",img_clone).attr("id","logo_img_file_0");
				$("#logo_url",img_clone).attr("id","logo_url_0");

				//事件追击
				//图片的点击事件
				$("#logo_img_0").live('click', function () {
					$("#logo_img_file_0").click();
				});

				//当文件变换的事件
				$('#logo_img_file_0').live('change',function(e) {
					processImage(this, $('#logo_img_0'),0);
				});

				$('#sku_image_div').append(img_clone);
				//url值
				$("#logo_url_0").val(imgUri);
				$("#logo_img_0").attr("src", displayUri + orignal  + imgUri);
			}

		}

	}else{
		showAlert('sku编辑数据获取失败')
	}
}

//数据保存成功的回调方法
function saveSuccessFn(data){

	if (data.resultCode == '00') {
		$("#skuManage").modal('hide');
		//重新刷新table
		tableDisplay(finalKeyArray,jsonTextSpeSku,data.gdsSkuList);
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
		$('#icon-plus-sign-sku').parent().append('<span for="goods_type_name_path" class="help-block">请维护商品SKU的图片.</span>');
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