var tableSubActivtuSelect;
var sub_activitySelectList;
$(document).ready(function () {
    $("#sub_activity_select_dialog").modal('show');
    initTable();
    //检索按钮点击事件
    $("#select_sub_activity_btn_search").click(function () {
        tableSubActivitySelect.fnDraw();
    });
    // 获取下拉列表的码表

    $("#sub_activity_select_btn_confirm").click(function () {
        var subActivitySelectId = $("input[name='select_sub_activity_radio']:checked").val();
        if (subActivitySelectId && subActivitySelectId.length > 0) {
            $("#sub_activity_select_dialog").modal('hide');
            // 这个方法在弹出选择模板的画面实现
            $.each(tableSubActivitySelect.fnGetData(), function (index, row) {
                if (subActivitySelectId == row.activityId) {
                    onSubActivitySelected(row);
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
            aoData.push({ "name": "activityName", "value": $("#select_sub_activity_name").val() } );
            aoData.push({ "name": "unit", "value": "10" });
            aoData.push({"name": "approveStatus", "value": "20"});
        },
        "aoColumns": [
            {
                "mData": null,
                "fnRender": function (rowData) {
                    var $radio = $('<input>')
                        .attr('name', 'select_sub_activity_radio')
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
    tableSubActivitySelect = $('#sub_activity_select_table').dataTable(tableOption);
}

function showTip(message) {
    $("#sub_activity_select_tip").text(message);
    setTimeout(function () {
        $("#sub_activity_select_tip").text("");
    }, 2000);
}