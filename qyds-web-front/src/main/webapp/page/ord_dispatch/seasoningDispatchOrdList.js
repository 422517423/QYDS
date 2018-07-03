/**
 * Created by YiLian on 2016/10/10.
 */

var table;
$(document).ready(function () {
    initTable();

    //检索按钮点击事件
    $("#btn_search").click(function () {
        //刷新Datatable，会自动激发retrieveData
        table.fnDraw();
    });
});

function initTable() {
    var tableOption = {
        "bProcessing": true,
        "bServerSide": true,
        "fnServerData": fnServerData,
        "bFilter": false,
        "bSort": false,
        "sAjaxSource": "../ord_dispatch/getUpSeasoningDispatchList.json",
        "fnServerParams": function (aoData) {
            // 设置参数
            aoData.push({"name": "orderId", "value": $("#orderId").val()});
            aoData.push({"name": "subOrderId", "value": $("#orderCode").val()});
        },
        "aoColumns": [
            {"mData": "subOrderId"},
            {"mData": "orderId"},
            {
                "mData": "insertTime",
                "fnRender": function (rowData) {
                    return getLocalTime(rowData.aData.insertTime);
                }
            },
            {
                "mData": null,
                "fnRender": function (rowData) {
                    console.log(rowData.aData);
                    if (rowData.aData.upSeasoning == '1') {
                        return "未调货";
                    } else {
                        return "已调货";
                    }
                }

            },
            {
                "mData": null,
                "fnRender": function (rowData) {

                    if (rowData.aData.upSeasoning == '1') {
                        return '<a onclick=seasoningItem("' + rowData.aData.subOrderId + '")>调货完成</a>';
                    } else {
                        return "";
                    }

                }
            }

        ]
    };
    table = $('#ord_seasoning_table').dataTable(tableOption);
}

//时间戳转换日期
function getLocalTime(nS) {
    return new Date(parseInt(nS)).toLocaleString().replace(/:\d{1,2}$/, ' ');
}

//点击编辑按钮进行画面跳转
function seasoningItem(obj) {
    var url = "/ord_dispatch/seasoningComplete.json";

    var param = {
        subOrderId:obj
    };

    var success = function (data) {
        if (data.resultCode == '00') {
            showAlert('提交成功!', '提示', function () {
                table.fnDraw();
            });
        } else {
            showAlert('提交失败,原因:' + data.resultMessage);
        }
    };
    var error = function () {
        showTip('网络异常.');
    };
    axse(url, {"data": JSON.stringify(param)}, success, error);
}