
var userId = sessionStorage.getItem("userId");
var goodsId = sessionStorage.getItem("goodsId");
var actId = sessionStorage.getItem("actId");
var checkGoodsId = sessionStorage.getItem("checkGoodsId");
var itemsCode = sessionStorage.getItem("itemsCode");

$(document).ready(function() {
	$("#goods_list").modal('show');


	//获取码表数据(商品类型 需要特殊处理)
	getComCode();

	//获取是否上架 是否废除码表数据
	getOptionCode("YES_NO","is_onsell_suitlist");
	//获取是否上架 是否废除码表数据
	getOptionCode("MAINTAIN_STATUS","maintain_status_suitlist");

	initTable();

	//检索按钮点击事件
	$("#btn_search_suitlist").click(function(){
		//刷新Datatable，会自动激发retrieveData
		oTable.fnDraw();
	});

	//保存按钮点击事件
	$("#btn_save").click(function(){
		var ids = "";
		var checkIds = ""
		$('.checkboxes').each(function(){
			//商品ID列表
			if($(this).is(':checked') ){
				if (checkIds == "") {
					checkIds = $(this).attr("id");
				} else {
					checkIds = checkIds + "," + $(this).attr("id");
				}
				if($(this).attr("disabled") == null ){
					if (ids == "") {
						ids = $(this).attr("id");
					} else {
						ids = ids + "," + $(this).attr("id");
					}
				}
			}
		});

		if(ids.length > 0){
			if (!itemsCode || itemsCode != 'index_figure') {
				$("#goods_list").modal('hide');
				addGoodsForTable(ids);
			} else {
				// var idArray = checkIds.split(",");
				// if (idArray.length > 1) {
				// 	showTip("您只能选择一件推荐商品");
				// } else {
				// 	$("#goods_list").modal('hide');
				// 	addGoodsForTable(ids);
				// }

                $("#goods_list").modal('hide');
                addGoodsForTable(ids);
			}

			// tableDisplay(ids);
		}else{
			if (!itemsCode || itemsCode != 'index_figure') {
				showTip("您还没有选择商品");
			} else {
				// if (checkIds == "" || checkIds == null) {
				// 	showTip("您还没有选择商品");
				// } else {
				// 	var idArray = checkIds.split(",");
				// 	if (idArray.length > 1) {
				// 		showTip("您只能选择一件推荐商品");
				// 	} else if (idArray.length == 1) {
				// 		$("#goods_list").modal('hide');
				// 		addGoodsForTable(ids);
				// 	}
				// }

                $("#goods_list").modal('hide');
                addGoodsForTable(ids);

			}

		}
	});

});



//获得码表数据方法
function getComCode() {
	//品牌类型码表中的数据code为GDS_TYPE 每一个码表的code不一样
	axse("/common/getCodeList.json", {"data":"GDS_TYPE"}, codeListSuccessFn, errorFn);
}

//获取码表数据成功的回调方法
function codeListSuccessFn(data) {
	if (data.resultCode == '00') {
		var array = data.data;
		if (array != null) {
			//为商品类型选择框添加数据
			$.each(array, function(index, item) {
				if(item.value != '30'){
					var $option = $('<option>').attr('value', item.value).text(item.displayCn);
					$("#type_suitlist").append($option);
				}
			});
		}
	}else{
		showAlert('码表数据获取失败');
	}
}

//表格js初始化
function initTable() {
	oTable = $('#gds_master_table_recommend').dataTable({
		"aoColumns": [
			{ "mData": null,
				"fnRender": function ( oObj ) {
					return '<input type="checkbox" id="' + oObj.aData.goodsId + '" class="checkboxes" value="1" />';

				}
			},
			{ "mData": "typeName" },
			{ "mData": "brandName"},
			{ "mData": "goodsTypeName" },
			{ "mData": "goodsCode"},
			{ "mData": "goodsName" }

		],
		"bAutoWidth": false,    //关闭自适应列宽，导致列表后半部空白。IE8下现象
		"bProcessing": false,
		"bServerSide": true,
		"bSort": false,
		"sAjaxSource": "../gds_master/getList.json",
		"fnServerParams": function ( aoData ) {
			aoData.push( { "name": "goodsName", "value": $("#goods_name_suitlist").val() } );
			aoData.push( { "name": "type", "value": $("#type_suitlist").val() } );
			aoData.push( { "name": "goodsCode", "value": $("#goods_code").val() } );
			aoData.push( { "name": "isOnsell", "value": "0" } );

		},
		"fnServerData": fnServerData,
		"fnRowCallback": fnRowCallback,
		"bFilter": false
	});

	jQuery('#gds_master_table_suitlist .group-checkable').change(function () {
		var set = jQuery(this).attr("data-set");
		var checked = jQuery(this).is(":checked");
		jQuery(set).each(function () {
			if (checked) {
				$(this).attr("checked", true);
			} else {
				$(this).attr("checked", false);
			}
			$(this).parents('tr').toggleClass("active");
		});
		//jQuery.uniform.update(set);

	});

	jQuery('#gds_master_table_suitlist tbody tr .checkboxes').change(function(){
		$(this).parents('tr').toggleClass("active");
	});

}

// 失败的回调方法
function errorFn() {
	showAlert('数据获取失败');
}

function showTip(message) {
	$("#suitlist_edit_tip").text(message);
	setTimeout(function () {
		$("#suitlist_edit_tip").text("");
	}, 2000);
}

function fnRowCallback(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
	if (checkGoodsId.indexOf(aData.goodsId) > -1) {
		$('td:eq(0)', nRow).html('<input type="checkbox" id="' + aData.goodsId + '" class="checkboxes" value="1" checked="checked" disabled/>');
	} else {
		$('td:eq(0)', nRow).html('<input type="checkbox" id="' + aData.goodsId + '" class="checkboxes" value="1" />');
	}
}