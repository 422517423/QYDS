var tableSkuSelect;
$(document).ready(function () {
    $("#sku_select_dialog").modal('show');
    initTable();
    //检索按钮点击事件
    $("#select_sku_btn_search").click(function () {
        tableSkuSelect.fnDraw();
        $('.allCheck').removeAttr('checked');
    });
    // 获取下拉列表的码表

    $("#sku_select_btn_confirm").click(function () {
        var selectedSkuIds = [];
        $("input[name='select_sku_checkbox']:checked").each(function () {
            selectedSkuIds.push($(this).val());
        });
        if (selectedSkuIds.length > 0) {
            $("#sku_select_dialog").modal('hide');
            // 这个方法在弹出选择模板的画面实现
            var selectedRows = [];
            $.each(selectedSkuIds, function (index, selectId) {
                $.each(tableSkuSelect.fnGetData(), function (index2, row) {
                    if (selectId == row.skuid) {
                        selectedRows.push(row);
                    }
                });
            });
            onSkuSelected(selectedRows);
        } else {
            showTip("请先选择一个商品");
        }
    });
});

function initTable() {
    var tableOption = {
        "bLengthChange": false,
        "bAutoWidth": false,    //关闭自适应列宽，导致列表后半部空白。IE8下现象
        "bProcessing": false,
        "bServerSide": true,
        "fnServerData": fnServerData,
        "bFilter": false,
        "bSort": false,
        "sAjaxSource": "../gds_master/getOnsellSkuList.json",
        "fnServerParams": function (aoData) {
            // 设置参数
            aoData.push({"name": "goodsCode", "value": $("#select_sku_goods_code").val()});
            aoData.push({"name": "colorName", "value": $("#select_sku_goods_color").val()});
        },
        "fnDrawCallback": function() {
            $('.allCheck').removeAttr('checked');
        },
        "aoColumns": [
            {
                "mData": null,
                "fnRender": function (rowData) {
                    var $radio = $('<input>')
                        .attr('name', 'select_sku_checkbox')
                        .attr('type', 'checkbox')
                        .attr('value', rowData.aData.skuid);
                    var $div = $('<div></div>');
                    $radio.appendTo($div);
                    return $div.html();
                }
            },
            {"mData": "goodsCode"},
            {
                "mData": null,// 颜色
                "fnRender": function (rowData) {
                    if (rowData.aData.type == "10") {
                        return rowData.aData.colorName
                    }else{
                        // 黑色_M 175/96A_
                        if(rowData.aData.skucontent&&rowData.aData.skucontent.length>0){
                            var sku = JSON.parse(rowData.aData.skucontent);
                            if(sku&&sku.sku_value){
                                return sku.sku_value.split("_")[0];
                            }else{
                                return "";
                            }
                        }else{
                            return "";
                        }

                    }
                }
            },
            {
                "mData": null,//尺码
                "fnRender": function (rowData) {
                    if (rowData.aData.type == "10") {
                        return rowData.aData.sizeName
                    }else{
                        var sku = JSON.parse(rowData.aData.skucontent);
                        if(sku&&sku.sku_value){
                            return sku.sku_value.split("_")[1];
                        }else{
                            return "";
                        }
                    }
                }
            },
            {"mData": "newCount"}
        ]
    };
    tableSkuSelect = $('#sku_select_table').dataTable(tableOption);
}

function showTip(message) {
    $("#sku_select_tip").text(message);
    setTimeout(function () {
        $("#sku_select_tip").text("");
    }, 2000);
}
function setAllCheckboxs(){
    var status = $('.allCheck').prop('checked');
    var checkboxs = $('#sku_select_table tbody input[type="checkbox"]');
    if(status){
        $.each(checkboxs,function(i,v){
            $(this).attr('checked','checked');
        });
    }else{
        $.each(checkboxs,function(i,v){
            $(this).removeAttr('checked');
        });
    }
}
