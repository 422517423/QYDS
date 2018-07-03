$(document).ready(function () {
    $("#activity_select_dialog").modal('show');
    initTable();
    //检索按钮点击事件
    $("#select_activity_btn_search").click(function () {
        tableActivitySelect.fnDraw();
    });
    // 获取下拉列表的码表

    $("#activity_select_btn_confirm").click(function () {
        var activitySelectId = $("input[name='select_activity_radio']:checked").val();
        if (activitySelectId && activitySelectId.length > 0) {
            $("#activity_select_dialog").modal('hide');
            // 这个方法在弹出选择模板的画面实现
            $.each(tableActivitySelect.fnGetData(), function (index, row) {
                if (activitySelectId == row.activityId) {
                    onFirstBuyActivitySelected(row);
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
        "sAjaxSource": "../act_master/getList.json",
        "fnServerParams": function (aoData) {
            // 设置参数
            aoData.push({ "name": "activityName", "value": $("#select_activity_name").val() } );
            aoData.push({ "name": "unit", "value": "20" });
            aoData.push({"name": "approveStatus", "value": "20"});
        },
        "aoColumns": [
            {
                "mData": null,
                "fnRender": function (rowData) {
                    var $radio = $('<input>')
                        .attr('name', 'select_activity_radio')
                        .attr('type', 'radio')
                        .attr('value', rowData.aData.activityId);
                    var $div = $('<div></div>');
                    $radio.appendTo($div);
                    return $div.html();
                }
            },
            {"mData": "actitionTypeCn"},
            { "mData": "activityName" }
        ]
    };
    tableActivitySelect = $('#activity_select_table').dataTable(tableOption);
}

function showTip(message) {
    $("#activity_select_tip").text(message);
    setTimeout(function () {
        $("#activity_select_tip").text("");
    }, 2000);
}