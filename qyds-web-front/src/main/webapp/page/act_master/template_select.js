var table;
$(document).ready(function () {
    $("#template_select_dialog").modal('show');
    initTable();
    //检索按钮点击事件
    $("#template_select_btn_search").click(function () {
        table.fnDraw();
    });
    // 获取下拉列表的码表
    getOptionCode("ACTITION_TYPE", "template_select_actition_type");
    $("#template_select_btn_confirm").click(function () {
        var selectedTempId = $("input[name='select_template_radio']:checked").val();
        if (selectedTempId && selectedTempId.length > 0) {
            $("#template_select_dialog").modal('hide');
            // 这个方法在弹出选择模板的画面实现
            onTemplateSelected(selectedTempId);
        } else {
            showTip("请先选择一个模板");
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
        "sAjaxSource": "../act_template/getList.json",
        "fnServerParams": function (aoData) {
            // 设置参数
            aoData.push({"name": "tempName", "value": $("#template_select_temp_name").val()});
            aoData.push({"name": "actitionType", "value": $("#template_select_actition_type").val()});
            aoData.push({"name": "approveStatus", "value": "20"});
        },
        "aoColumns": [
            {
                "mData": null,
                "fnRender": function (rowData) {
                    var $radio = $('<input>', {
                        'class': 'accordion'
                    }).attr('name', 'select_template_radio').attr('type', 'radio').attr('value', rowData.aData.tempId);
                    var $div = $('<div></div>');
                    $radio.appendTo($div);
                    return $div.html();
                }
            },
            {"mData": "tempName"},
            {"mData": "actitionTypeCn"}
        ]
    };
    table = $('#template_select_table').dataTable(tableOption);
}

function showTip(message) {
    $("#template_select_tip").text(message);
    setTimeout(function () {
        $("#template_select_tip").text("");
    }, 2000);
}