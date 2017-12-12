var tableCouponSelect;
var couponSelectList;
$(document).ready(function () {
    $("#coupon_select_dialog").modal('show');
    initTable();
    //检索按钮点击事件
    $("#coupon_select_btn_search").click(function () {
        tableCouponSelect.fnDraw();
    });
    // 获取下拉列表的码表
    //获取码表数据
    getOptionCode("GDS_BRAND_TYPE", "coupon_select_type");
    $("#coupon_select_btn_confirm").click(function () {
        var selectedCouponId = $("input[name='select_coupon_radio']:checked").val();
        if (selectedCouponId && selectedCouponId.length > 0) {
            $("#coupon_select_dialog").modal('hide');
            // 这个方法在弹出选择模板的画面实现
            $.each(tableCouponSelect.fnGetData(), function (index, row) {
                if (selectedCouponId == row.couponId) {
                    onCouponSelected(row);
                    return false
                }
            });

        } else {
            showTip("请先选择一个品牌");
        }
    });
});

function initTable() {
    var tableOption = {
        "bLengthChange": false,
        "bProcessing": false,
        "bServerSide": true,
        "fnServerData": fnServerData,
        "bFilter": false,
        "bSort": false,
        "sAjaxSource": "../coupon_master/getList.json",
        "fnServerParams": function (aoData) {
            // 设置参数
            aoData.push({"name": "couponName", "value": $("#coupon_select_name").val()});
            aoData.push({"name": "couponType", "value": "10"});
            aoData.push({"name": "distributeType", "value": "20"});
            aoData.push({"name": "approveStatus", "value": "20"});
        },
        "aoColumns": [
            {
                "mData": null,
                "fnRender": function (rowData) {
                    var $radio = $('<input>')
                        .attr('name', 'select_coupon_radio')
                        .attr('type', 'radio')
                        .attr('value', rowData.aData.couponId);
                    var $div = $('<div></div>');
                    $radio.appendTo($div);
                    return $div.html();
                }
            },
            {"mData": "couponName"},
            {"mData": "couponTypeCn"},
            {"mData": "worth"}
        ]
    };
    tableCouponSelect = $('#coupon_select_table').dataTable(tableOption);
}

function showTip(message) {
    $("#coupon_select_tip").text(message);
    setTimeout(function () {
        $("#coupon_select_tip").text("");
    }, 2000);
}