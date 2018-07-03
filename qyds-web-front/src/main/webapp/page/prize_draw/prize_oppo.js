var prizeDrawId = sessionStorage.getItem("prizeDrawId");
var oppoTable;
$(document).ready(function () {

    $("#btn_cancel").click(function () {
        back();
    });

    $("#btn_reset").click(
        function(){
            $("#is_drawed").val("");
            $("#is_win").val("");
            $("#prize_name").val("");
            $("#delivery_status").val("");
            oppoTable.fnDraw();
        }
    );

    //检索按钮点击事件
    $("#btn_search").click(function () {
        oppoTable.fnDraw();
    });

    $("#send_dialog_btn_confirm").click(function () {
        sendItemConfirm();
    });

    initOppoTable();

    ajaxPost('/prize_draw/getPrizeGoodsNameList.json',{'prizeDrawId':prizeDrawId},function(res){
        if (res.resultCode == '00') {
            var data = res.result;
            $.each(data,function(i,v){
                $('<option value="'+v.prizeGoodsName+'">'+v.prizeGoodsName+'</option>').appendTo($('#prize_name'));
            })
        }
    });

});

function resetIsDrawed(){
    if($("#is_drawed").val() == '0'){
        $("#is_win").val("");
        $("#prize_name").val("");
        $("#delivery_status").val("");
    }
}

//退回到列表画面
function back() {
    $('#content').load('prize_draw/list.html');
}


function initOppoTable() {
    var tableOption = {
        "bProcessing": true,
        "bServerSide": true,
        "fnServerData": fnServerData,
        "bFilter": false,
        "bSort": false,
        "sAjaxSource": "../prize_draw/getPrizeOppList.json",
        "fnServerParams": function (aData) {
            // 设置参数
            aData.push({"name": "prizeDrawId", "value": prizeDrawId});
            aData.push({"name": "isDrawed", "value": $("#is_drawed").val()});
            aData.push({"name": "isWin", "value": $("#is_win").val()});
            aData.push({"name": "prizeName", "value": $("#prize_name").val()});
            aData.push({"name": "deliveryStatus", "value": $("#delivery_status").val()});
        },
        "aoColumns": [
            {"mData": "memberName"},
            {"mData": "telephone"},
            {
                "mData": null,
                "fnRender": function (rowData) {
                    if (rowData.aData.isDrawed == "1") {
                        return "已抽奖";
                    } else {
                        return "未抽奖";
                    }
                }
            },
            {"mData": "prizeName"},
            {
                "mData": null,
                "fnRender": function (rowData) {
                    if (rowData.aData.deliveryStatus == "20") {
                        return "已发放";
                    } else if (rowData.aData.deliveryStatus == "10" && rowData.aData.isDrawed == "10") {
                        return "未发放";
                    } else {
                        return "";
                    }
                }
            },
            {"mData": "deliveryComment"},
            {
                "mData": null,
                "fnRender": function (rowData) {
                    var sReturn = '';
                    if(rowData.aData.isDrawed == "1" && rowData.aData.deliveryStatus == "10"){
                        sReturn += '<a onclick=issueConfirm("' + rowData.aData.prizeDrawOppoId + '")>发放奖品</a>';
                    }
                    return sReturn;
                }
            }

        ]
    };

    oppoTable = $('#prize_oppo_table').dataTable(tableOption);
}

var prizeDrawOppoId;
function issueConfirm(id){
    $("#send_dialog_content").val("");
    prizeDrawOppoId = id;
    $("#send_dialog").modal('show');
}

function sendItemConfirm() {
    var url = "/prize_draw/sendPrizeGoods.json";
    var param = {
        "prizeDrawOppoId": prizeDrawOppoId,
        "deliveryComment": $("#send_dialog_content").val()
    };
    var success = function (data) {
        if (data.resultCode == '00') {
            $("#send_dialog").modal('hide');
            showAlert("提交成功!", function () {
                oppoTable.fnDraw();
            });
        } else {
            showTip('提交失败,原因:' + data.resultMessage);
        }
    };
    var error = function () {
        showTip('提交失败,网络异常.');
    };
    axse(url, {'data': JSON.stringify(param)}, success, error);
}

function showTip(message) {
    $("#send_dialog_tip").text(message);
    setTimeout(function () {
        $("#send_dialog_tip").text("");
    }, 2000);
}
