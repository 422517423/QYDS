var oTZTable;
var userId = sessionStorage.getItem("userId");
var goodsId = sessionStorage.getItem("goodsId");

$(document).ready(function() {

	getEditData();

	//表单验证的定义
	$("#gds_sell_form").validate({
		errorElement: 'span', //default input error message container
		errorClass: 'help-block', // default input error message class
		focusInvalid: false, // do not focus the last invalid input
		rules: {
			// limit_count: {
			// 	required: true
			// },
			safe_bank: {
				required: true
			}
			//,act_bank:{
			//	required: true
			//}
		},
		messages: {
			// limit_count: {
			// 	required: "请填写限购件数"
			// },
			safe_bank: {
				required: "请填写安全库存"
			}
			//,act_bank:{
			//	required: "请填写实际库存"
			//}

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
		if (!$("#gds_sell_form").valid()){
			// 检测商品是否填写SKU信息
			var json = {};
			json.goodsId = goodsId;
			axse("/gds_master/checkSku.json", {'data':JSON.stringify(json)}, checkSkuSuccessFn, errorFn);
		} else {
			var json = {};
			json.goodsId = goodsId;
			axse("/gds_master/checkSku.json", {'data':JSON.stringify(json)}, checkSkuSuccessFn, errorFn);
		}
	});

	//取消按钮的点击事件
	$('#cancel_btn').click(function(){
		gotoListPage();
	});

	//属性添加按钮的点击事件
	$('#icon-plus-sign').click(function(){
		var property_group = $('#property_group').clone();
		$(property_group).css('display','block');
		$('#property_define').append(property_group);
	});

	//套装商品维护按钮点击事件
	$('#icon-plus-sign-sku-tj-add').click(function(){
		sessionStorage.setItem("pageFrom_sell","0");
		$('#customDialog').load('gds_master/recommend_edit.html');
	});

	$('#icon-plus-sign-sku-pt-add').click(function(){
		sessionStorage.setItem("pageFrom_sell","1");
		$('#customDialog').load('gds_master/recommend_edit.html');
	});
});


//退回到列表画面
function gotoListPage(){
	$('#content').load('gds_master/list.html');
}

//获取详细信息
function getEditData(){
	axse("/gds_sell/edit.json", {"goodsId":goodsId}, eidtSuccessFn, errorFn);
}


//获取详细信息成功的回调 用来显示
function eidtSuccessFn(data){
	if (data.resultCode == '00') {
		var item = data.data;

		if(item != null){
			//$('.act_bank').hide();
			//属性
			if(item.propertySellJson != null){
				var specification = JSON.parse(item.propertySellJson);
				//自有属性
				for(var index=0;index<specification.length;index++){
					var property_group = $('#property_group').clone();
					$(property_group).css('display','block');
					$('.specification',property_group).val(specification[index].specification);
					$('.specification_value',property_group).val(specification[index].specification_value);
					$('#property_define').append(property_group);
				}
			}

			// $("#limit_count").val(item.limitCount);
			$('#safe_bank').val(item.safeBank);
			//$('#offsell_reason').val(item.offsellReason);
		}else{
			//$('.act_bank').show();
		}

		//推荐
		var recommendList = data.recommendList;
		$('#body_tj').empty();
		if(recommendList != null){
			for(var i=0; i<recommendList.length;i++){
				var item = recommendList[i];
				if(item != null){
					var row = '<tr class="row_tr">';
					row = row + '<td>'+item.typeName+'</td>';
					if(item.brandName == null){
						row = row + '<td></td>';
					}else{
						row = row + '<td>'+item.brandName +'</td>';
					}
					if(item.erpStyleNo == null){
						row = row + '<td></td>';
					}else{
						row = row + '<td>'+item.erpStyleNo +'</td>';
					}
					row = row + '<td>'+item.goodsTypeName+'</td>';
					row = row + '<td>'+item.goodsCode+'</td>';
					row = row + '<td>'+item.goodsName+'</td>';
					row = row + '<td>'+item.erpGoodsCode+'</td>';
					row = row + '<td>'+item.erpGoodsName+'</td>';
					row = row + '<td><a class="sell_delete_tj" id="'+goodsId + '||'+ item.goodsId +'" onclick="sell_delete(this)" href="javascript:;">删除</a></td>';
					row = row + '</tr>';
					$('#body_tj').append($(row));

				}
			}
		}

		//配套
		var mattingList = data.mattingList;
		$('#body_pt').empty();
		if(mattingList != null){
			for(var i=0; i<mattingList.length;i++){
				var item = mattingList[i];
				if(item != null){
					var row = '<tr class="row_tr">';
					row = row + '<td>'+item.typeName+'</td>';
					if(item.brandName == null){
						row = row + '<td></td>';
					}else{
						row = row + '<td>'+item.brandName +'</td>';
					}
					if(item.erpStyleNo == null){
						row = row + '<td></td>';
					}else{
						row = row + '<td>'+item.erpStyleNo +'</td>';
					}
					row = row + '<td>'+item.goodsTypeName+'</td>';
					row = row + '<td>'+item.goodsCode+'</td>';
					row = row + '<td>'+item.goodsName+'</td>';
					row = row + '<td>'+item.erpGoodsCode+'</td>';
					row = row + '<td>'+item.erpGoodsName+'</td>';
					row = row + '<td><a class="sell_delete_pt" id="'+goodsId+ '||'+ item.goodsId +'" onclick="sell_delete(this)" href="javascript:;">删除</a></td>';
					row = row + '</tr>';
					$('#body_pt').append($(row));
				}
			}
		}



	}else{
		showAlert('商品上架信息获取失败');
	}
}

//展示表格信息
function tableDisplay(ids,flg){
	ids = ids.substring(0,ids.length);
	axse("/gds_sell/selectDatas.json", {"goodsIds":ids}, selectSuccessFn, errorFn);
}

//获取数据根据弹出画面选择的商品ID获取
function selectSuccessFn(data){
	if(data.data != null){
		for(var i=0; i<data.data.length;i++){
			var item = data.data[i];
			var row = '<tr class="row_tr">';
			row = row + '<td>'+item.typeName+'</td>';
			if(item.brandName == null){
				row = row + '<td></td>';
			}else{
				row = row + '<td>'+item.brandName +'</td>';
			}
			if(item.erpStyleNo == null){
				row = row + '<td></td>';
			}else{
				row = row + '<td>'+item.erpStyleNo +'</td>';
			}
			row = row + '<td>'+item.goodsTypeName+'</td>';
			row = row + '<td>'+item.goodsCode+'</td>';
			row = row + '<td>'+item.goodsName+'</td>';
			row = row + '<td>'+item.erpGoodsCode+'</td>';
			row = row + '<td>'+item.erpGoodsName+'</td>';
			if(sessionStorage.getItem("pageFrom_sell")=="0"){
				row = row + '<td><a class="tj_delete" id="'+item.goodsId+'"  href="javascript:;">未保存</a></td>';
			}else{
				row = row + '<td><a class="pt_delete" id="'+item.goodsId+'"  href="javascript:;">未保存</a></td>';
			}
			row = row + '</tr>';
			if(sessionStorage.getItem("pageFrom_sell")=="0"){
				$('#body_tj').append($(row));
			}else{
				$('#body_pt').append($(row));
			}

		}
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


//提示信息方法
function showTip(message) {
	$("#gds_master_edit_tip").text(message);
	setTimeout(function () {
		$("#gds_master_edit_tip").text("");
	}, 2000);
}

//点击删除按钮
function sell_delete(obj){
	var goodsId = obj.id.split('||')[0];
	var delGoodsId = obj.id.split('||')[1];

	var flag = $(obj).attr('class');
	showConfirm('确认删除该商品吗?',function(){
		axse("/gds_sell/delete.json", {"goodsId":goodsId,"delGoodsId":delGoodsId,"flag":flag}, delSuccessFn, errorFn);
	});

}

//删除方法的回调函数
function delSuccessFn(data){
	if (data.resultCode == '00') {
		//if(data.flag == "0"){
		//	//推荐
		//	$('#body_tj').empty();
		//	var recommendList = data.recommendList;
		//	if(recommendList != null){
		//		for(var i=0; i<recommendList.length;i++){
		//			var item = recommendList[i];
		//			console.log("item == "+item);
		//			if(item != null){
		//				var row = '<tr class="row_tr">';
		//				row = row + '<td>'+item.typeName+'</td>';
		//				if(item.brandName == null){
		//					row = row + '<td></td>';
		//				}else{
		//					row = row + '<td>'+item.brandName +'</td>';
		//				}
		//				if(item.erpStyleNo == null){
		//					row = row + '<td></td>';
		//				}else{
		//					row = row + '<td>'+item.erpStyleNo +'</td>';
		//				}
		//				row = row + '<td>'+item.goodsTypeName+'</td>';
        //
		//				row = row + '<td>'+item.goodsCode+'</td>';
		//				row = row + '<td>'+item.goodsName+'</td>';
		//				row = row + '<td>'+item.erpGoodsCode+'</td>';
		//				row = row + '<td>'+item.erpGoodsName+'</td>';
		//				row = row + '<td><a class="sell_delete_tj" id="'+goodsId + '||'+ item.goodsId +'" onclick="sell_delete(this)" href="javascript:;">删除</a></td>';
		//				row = row + '</tr>';
		//				$('#body_tj').append($(row));
		//			}
		//		}
		//	}
		//}else{
		//	//配套
		//	$('#body_pt').empty();
		//	var mattingList = data.mattingList;
		//	if(mattingList != null){
		//		for(var i=0; i<mattingList.length;i++){
		//			var item = mattingList[i];
		//			if(item != null){
		//				var row = '<tr class="row_tr">';
		//				row = row + '<td>'+item.typeName+'</td>';
		//				if(item.brandName == null){
		//					row = row + '<td></td>';
		//				}else{
		//					row = row + '<td>'+item.brandName +'</td>';
		//				}
		//				if(item.erpStyleNo == null){
		//					row = row + '<td></td>';
		//				}else{
		//					row = row + '<td>'+item.erpStyleNo +'</td>';
		//				}
		//				row = row + '<td>'+item.goodsTypeName+'</td>';
        //
		//				row = row + '<td>'+item.goodsCode+'</td>';
		//				row = row + '<td>'+item.goodsName+'</td>';
		//				row = row + '<td>'+item.erpGoodsCode+'</td>';
		//				row = row + '<td>'+item.erpGoodsName+'</td>';
		//				row = row + '<td><a class="sell_delete_pt" id="'+goodsId+ '||'+ item.goodsId +'" onclick="sell_delete(this)" href="javascript:;">删除</a></td>';
		//				row = row + '</tr>';
		//				$('#body_pt').append($(row));
		//			}
		//		}
		//	}
		//}
		getEditData();
	}else{
		showAlert('删除失败');
	}
}

function checkSkuSuccessFn(data) {
	if (data.resultCode == '00') {
		// 未设置SKU
		if (data.skuNo == 0) {
			showTip("请先设置SKU");
		} else {
			var json = {};
			// json.limitCount = $("#limit_count").val();
			json.goodsId = goodsId;
			json.propertySellJson = JSON.stringify(getSpecification());
			json.recommendJson = $('#recommend_goods_id').val();
			json.matingJson = $('#mating_goods_id').val();
			json.safeBank = $('#safe_bank').val();
			//json.actBank = $('#act_bank').val();
			//json.offsellReason = $('#offsell_reason').val();

			//拼接套装的IDS
			var goodsIds_tj = "";
			$.each($('.tj_delete'), function(index, item) {
				goodsIds_tj = goodsIds_tj + item.id + ",";
			});
			json.recommendJson = goodsIds_tj;

			var goodsIds_pt = "";
			$.each($('.pt_delete'), function(index, item) {
				goodsIds_pt = goodsIds_pt + item.id + ",";
			});
			json.matingJson = goodsIds_pt;

			axse("/gds_sell/save.json", {'data':JSON.stringify(json)}, saveSuccessFn, errorFn);
		}
	}
}

function showTip(message) {
	$("#suitlist_edit_tip").text(message);
	setTimeout(function () {
		$("#suitlist_edit_tip").text("");
	}, 2000);
}