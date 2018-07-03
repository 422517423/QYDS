/**
 * Created by YiLian on 2016/7/18.
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
        "sAjaxSource": "../mmb_contact/getList.json",
        "fnServerParams": function (aoData) {
            // 设置参数
            aoData.push({"name": "telephone", "value": $("#telephone").val()});
            aoData.push({"name": "theme", "value": $("#theme").val()});
        },
        "aoColumns": [
            {"mData": "theme"},
            {"mData": "comment"},
            {"mData": "userName"},
            {"mData": "telephone"},
            {"mData": "address"},
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
        ],"fnCreatedRow": function( row, data, dataIndex ) {
            if(data.comment.length > 20){//只有超长，才有td点击事件
                $(row).children('td').eq(1).attr('onclick','javascript:changeShowRemarks(this);');
            }
            $(row).children('td').eq(1).attr('content',data.comment);
        },

        "aoColumnDefs" : [
            {
                "aTargets" :　[1],
                "mRender" : function(data, type, full){
                    if (full.comment.length  > 20) {
                        return getPartialRemarksHtml(full.comment);//显示部分信息
                    } else {
                        return full.comment;//显示原始全部信息            }
                    }
                }
            }
        ]
    };
    table = $('#mmb_contact_table').dataTable(tableOption);
}

//切换显示备注信息，显示部分或者全部
function changeShowRemarks(obj){//obj是td
    var content = $(obj).attr("content");
    if(content != null && content != ''){
        if($(obj).attr("isDetail") == 'true'){//当前显示的是详细备注，切换到显示部分
            //$(obj).removeAttr('isDetail');//remove也可以
            $(obj).attr('isDetail',false);
            $(obj).html(getPartialRemarksHtml(content));
        }else{//当前显示的是部分备注信息，切换到显示全部
            $(obj).attr('isDetail',true);
            $(obj).html(getTotalRemarksHtml(content));
        }
    }
}
//部分备注信息
function getPartialRemarksHtml(remarks){
    return remarks.substr(0,20) + '&nbsp;&nbsp;<a href="javascript:void(0);" ><b>更多</b></a>';
}

//全部备注信息
function getTotalRemarksHtml(remarks){
    return remarks + '&nbsp;&nbsp;<a href="javascript:void(0);" >收起</a>';
}