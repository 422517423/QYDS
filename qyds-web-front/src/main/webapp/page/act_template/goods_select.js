var tableGoodsSelect;
var goodsSelectList;
$(document).ready(function () {
    $("#goods_select_dialog").modal('show');
    initTable();
    //检索按钮点击事件
    $("#select_goods_btn_search").click(function () {
        tableGoodsSelect.fnDraw();
    });
    // 获取下拉列表的码表
    
    $("#goods_select_btn_confirm").click(function () {
        var selectedGoodsId = $("input[name='select_goods_radio']:checked").val();
        if (selectedGoodsId && selectedGoodsId.length > 0) {
            $("#goods_select_dialog").modal('hide');
            // 这个方法在弹出选择模板的画面实现
            $.each(tableGoodsSelect.fnGetData(), function (index, row) {
                if (selectedGoodsId == row.goodsId) {
                    onGoodsSelected(row);
                    return false;
                }
            });
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
        "sAjaxSource": "../gds_master/getList.json",
        "fnServerParams": function (aoData) {
            // 设置参数
            aoData.push( { "name": "goodsName", "value": $("#select_goods_name").val() } );
            // aoData.push( { "name": "brandId", "value": $("#select_goods_type").val() } );
            aoData.push( { "name": "isOnsell", "value": "0" } );
            aoData.push( { "name": "maintainStatus", "value": "30" } );
        },
        "aoColumns": [
            {
                "mData": null,
                "fnRender": function (rowData) {
                    var $radio = $('<input>')
                        .attr('name', 'select_goods_radio')
                        .attr('type', 'radio')
                        .attr('value', rowData.aData.goodsId);
                    var $div = $('<div></div>');
                    $radio.appendTo($div);
                    return $div.html();
                }
            },
            { "mData": "goodsCode"},
            { "mData": "goodsName" }
        ]
    };
    tableGoodsSelect = $('#goods_select_table').dataTable(tableOption);
}

function showTip(message) {
    $("#goods_select_tip").text(message);
    setTimeout(function () {
        $("#goods_select_tip").text("");
    }, 2000);
}