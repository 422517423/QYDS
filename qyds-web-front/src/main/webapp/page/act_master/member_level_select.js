var tableMemberLevelSelect;
var member_levelSelectList;
$(document).ready(function () {
    $("#member_level_select_dialog").modal('show');
    initTable();
    //检索按钮点击事件
    $("#member_level_select_btn_search").click(function () {
        tableMemberLevelSelect.fnDraw();
    });
    // 获取下拉列表的码表
    //获取码表数据
    getOptionCode("MEMBER_GROUP_TYPE", "member_level_select_type");
    $("#member_level_select_btn_confirm").click(function () {
        var selectedMemberLevelId = $("input[name='select_member_level_radio']:checked").val();
        if (selectedMemberLevelId && selectedMemberLevelId.length > 0) {
            $("#member_level_select_dialog").modal('hide');
            // 这个方法在弹出选择模板的画面实现
            $.each(tableMemberLevelSelect.fnGetData(), function (index, row) {
                if (selectedMemberLevelId == row.memberLevelCode) {
                    onMemberLevelSelected(row);
                    return false;
                }
            });

        } else {
            showTip("请先选择一种级别");
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
        "sAjaxSource": "../mmb_level_rule/getList.json",
        "fnServerParams": function (aoData) {
            // 设置参数
            aoData.push({"name": "type", "value": $("#member_level_select_type").val()});
            aoData.push({"name": "groupName", "value": $("#member_level_select_name").val()});
        },
        "aoColumns": [
            {
                "mData": null,
                "fnRender": function (rowData) {
                    var $radio = $('<input>')
                        .attr('name', 'select_member_level_radio')
                        .attr('type', 'radio')
                        .attr('value', rowData.aData.memberLevelCode);
                    var $div = $('<div></div>');
                    $radio.appendTo($div);
                    return $div.html();
                }
            },
            {"mData": "memberLevelName"},
            {"mData": "memberCount"}
        ]
    };
    tableMemberLevelSelect = $('#member_level_select_table').dataTable(tableOption);
}

function showTip(message) {
    $("#member_level_select_tip").text(message);
    setTimeout(function () {
        $("#member_level_select_tip").text("");
    }, 2000);
}