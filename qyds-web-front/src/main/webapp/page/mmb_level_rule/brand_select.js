var tableBrandSelect;
$(document).ready(function () {
    $("#brand_select_dialog").modal('show');
    initTable();
    //检索按钮点击事件
    $("#brand_select_btn_search").click(function () {
        tableBrandSelect.fnDraw();
    });
    // 获取下拉列表的码表
    //获取码表数据
    getOptionCode("GDS_BRAND_TYPE", "brand_select_type");
    $("#brand_select_btn_confirm").click(function () {
        var selectedIds = [];

        $("input[name='select_brand_checkbox']:checked").each(function () {
            selectedIds.push($(this).val());
        });

        if (selectedIds && selectedIds.length > 0) {
            $("#brand_select_dialog").modal('hide');
            // 这个方法在弹出选择模板的画面实现
            var selectedRows = [];
            $.each(selectedIds, function (index, selectId) {
                $.each(tableBrandSelect.fnGetData(), function (index2, row) {
                    if (selectId == row.brandId) {
                        selectedRows.push(row);
                    }
                });
            });
            onBrandSelect(selectedRows);
        } else {
            showTip("请先选择一个品牌");
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
        "sAjaxSource": "../gds_brand/getList.json",
        "fnServerParams": function (aoData) {
            // 设置参数
            aoData.push({"name": "brandName", "value": $("#brand_select_name").val()});
            aoData.push({"name": "type", "value": $("#brand_select_type").val()});
        },
        "aoColumns": [
            {
                "mData": null,
                "fnRender": function (rowData) {
                    var $radio = $('<input>')
                        .attr('name', 'select_brand_checkbox')
                        .attr('type', 'checkbox')
                        .attr('value', rowData.aData.brandId);
                    var $div = $('<div></div>');
                    $radio.appendTo($div);
                    return $div.html();
                }
            },
            {"mData": "typeName"},
            {"mData": "brandName"}
        ]
    };
    tableBrandSelect = $('#brand_select_table').dataTable(tableOption);
}

function showTip(message) {
    $("#brand_select_tip").text(message);
    setTimeout(function () {
        $("#brand_select_tip").text("");
    }, 2000);
}



