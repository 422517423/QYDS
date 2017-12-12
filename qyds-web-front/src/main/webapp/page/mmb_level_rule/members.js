/**
 * Created by YiLian on 2016/7/18.
 */
var table;
var memberLevelId = sessionStorage.getItem("memberLevelId");
var memberLevelName = sessionStorage.getItem("memberLevelName");
$(document).ready(function () {

    $("#member_level_name").text(memberLevelName);
    $("#member_level_id").val(memberLevelId);


    initTable();

});

function initTable() {
    var tableOption = {
        "bProcessing": true,
        "bServerSide": true,
        "fnServerData": fnServerData,
        "bFilter": false,
        "bSort": false,
        "sAjaxSource": "../mmb_master/getList.json",
        "fnServerParams": function (aoData) {
            // 设置参数
            aoData.push({"name": "memberLevelId", "value": memberLevelId});
        },
        "aoColumns": [
            {"mData": "memberName"},
            {"mData": "typeName"},
            {"mData": "sexName"},
            {"mData": "telephone"},
            {
                "mData": null,
                "fnRender": function (rowData) {
                    if (rowData.aData.openId && rowData.aData.openId.length > 0) {
                        return "是";
                    } else {
                        return "否";
                    }

                }
            },
            {"mData": "point"},
            {"mData": "allPoint"},
            {
                "mData": "insertTime",
                "fnRender": function (rowData) {
                    if (rowData.aData.insertTime != null) {
                        return new Date(rowData.aData.insertTime).Format("yyyy-MM-dd hh:mm:ss");
                    } else {
                        return "";
                    }
                }
            }


        ]
    };
    table = $('#members_table').dataTable(tableOption);
}

//退回到列表画面
function back() {
    $('#content').load('mmb_level_rule/list.html');
}
