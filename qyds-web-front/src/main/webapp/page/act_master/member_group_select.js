var tableMemberGroupSelect;
var member_groupSelectList;
$(document).ready(function () {
    $("#member_group_select_dialog").modal('show');
    initTable();
    //检索按钮点击事件
    $("#member_group_select_btn_search").click(function () {
        tableMemberGroupSelect.fnDraw();
    });
    // 获取下拉列表的码表
    //获取码表数据
    getOptionCode("MEMBER_GROUP_TYPE", "member_group_select_type");
    $("#member_group_select_btn_confirm").click(function () {
        var selectedMemberGroupId = $("input[name='select_member_group_radio']:checked").val();
        if (selectedMemberGroupId && selectedMemberGroupId.length > 0) {
            $("#member_group_select_dialog").modal('hide');
            // 这个方法在弹出选择模板的画面实现
            $.each(tableMemberGroupSelect.fnGetData(), function (index, row) {
                if (selectedMemberGroupId == row.groupId) {
                    onMemberGroupSelected(row);
                    return false;
                }
            });

        } else {
            showTip("请先选择一个分组");
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
        "sAjaxSource": "../mmb_group/getList.json",
        "fnServerParams": function (aoData) {
            // 设置参数
            aoData.push({"name": "type", "value": $("#member_group_select_type").val()});
            aoData.push({"name": "groupName", "value": $("#member_group_select_name").val()});
        },
        "aoColumns": [
            {
                "mData": null,
                "fnRender": function (rowData) {
                    var $radio = $('<input>')
                        .attr('name', 'select_member_group_radio')
                        .attr('type', 'radio')
                        .attr('value', rowData.aData.groupId);
                    var $div = $('<div></div>');
                    $radio.appendTo($div);
                    return $div.html();
                }
            },
            {"mData": "groupName"},
            {"mData": "typeName"}
        ]
    };
    tableMemberGroupSelect = $('#member_group_select_table').dataTable(tableOption);
}

function showTip(message) {
    $("#member_group_select_tip").text(message);
    setTimeout(function () {
        $("#member_group_select_tip").text("");
    }, 2000);
}