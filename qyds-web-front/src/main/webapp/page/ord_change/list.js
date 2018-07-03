/**
 * Created by wenxuechao on 16/7/23.
 */

var deliverStatus_dashboard = sessionStorage.getItem("deliverStatus_dashboard");

var oTable;

//页面初始化加载
$(document).ready(function () {
    //getOptionCode("ORDER_STATUS", "type");
    //getOptionCode("PAY_STATUE", "payStatus");
    //getOptionCode("DELIVER_STATUS", "deliverStatus");
    // getSelectCode("ORDER_STATUS", "type","'10','11','20','21','22','23','30','31','32','33','90'");
    // getSelectCode("PAY_STATUE", "payStatus","'10','20','21','30','31'");
    // getSelectCode("DELIVER_STATUS", "deliverStatus","'10','19','20','21','30','31'");
    getSelectCode("ORDER_STATUS", "type","'10','11','20','21','22','23','30','31','32','33','90'",function(){
        var type = sessionStorage.getItem("type");
        if(type){
            $('#type option[value='+type+']').attr('selected','selected');
        }
    });

    getSelectCode("PAY_STATUE", "payStatus","'10','20','21','30','31'",function(){
        var payStatus = sessionStorage.getItem("payStatus");
        if(payStatus){
            $('#payStatus option[value="'+payStatus+'"]').attr('selected','selected');
        }
    });

    getOptionCode("DELIVER_TYPE", "deliverType",function(){
        var deliverType = sessionStorage.getItem("deliverType");
        if(deliverType){
            $('#deliverType option[value="'+deliverType+'"]').attr('selected','selected');
        }
    });

    getSelectCode("DELIVER_STATUS", "deliverStatus","'10','19','20','21','30','31'",function(){
        var deliverStatus = sessionStorage.getItem("deliverStatus");
        if(deliverStatus){
            $('#deliverStatus option[value="'+deliverStatus+'"]').attr('selected','selected');
        }
        initTable();
    });

    if (jQuery().datepicker) {
        $('.date-picker').datepicker({
            rtl: App.isRTL(),
            autoclose: true
        });
        $('body').removeClass("modal-open"); // fix bug when inline picker is used in modal
    }

    //检索按钮点击事件
    $("#btn_search").click(function () {
        //刷新Datatable，会自动激发retrieveData
        oTable.fnDraw();
    });


    //$('#btn_export').on('click',function(){
    //    var param = "orderId="+$("#orderId").val()+"&"+
    //        "orderCode="+$("#orderCode").val()+"&"+
    //        "orderStatus="+$("#type").val()+"&"+
    //        "payStatus="+$("#payStatus").val()+"&"+
    //        "deliverStatus="+$("#deliverStatus").val()+"&"+
    //        "orderTimeStart="+$("#orderTimeStart").val()+"&"+
    //        "orderTimeEnd="+$("#orderTimeEnd").val()+"&"+
    //        "telephone="+$("#telephone").val()+"&"+
    //        "memberName="+encodeURI($("#memberName").val())+"&"+
    //        "deliverType="+$("#deliverType").val();
    //
    //    window.open("/qyds-web-front/ord_master/export.json?" + param);
    //
    //});
    //initTable();
});

function initTable(){
    setQuery();
    //alert($("#orderTimeStart").val());
    var tableOption = {
        "bProcessing": true,
        "bServerSide": true,
        "fnServerData": fnServerData,
        "bFilter": false,
        "bSort": false,
        "sAjaxSource": "../ord_master/getList.json",
        "fnServerParams": function (aoData) {
            aoData.push({"name": "orderId", "value": $("#orderId").val()});
            aoData.push({"name": "orderCode", "value": $("#orderCode").val()});
            aoData.push({"name": "orderStatus", "value": '10'});
            aoData.push({"name": "payStatus", "value": '20'});
            aoData.push({"name": "deliverStatus", "value": '10'});
            aoData.push({"name": "orderTimeStart", "value": $("#orderTimeStart").val()});
            aoData.push({"name": "orderTimeEnd", "value": $("#orderTimeEnd").val()});
            aoData.push({"name": "telephone", "value": $("#telephone").val()});
            aoData.push({"name": "deliverType", "value": '20'});
            aoData.push({"name": "memberName", "value": $("#memberName").val()});
        },
        "aoColumns": [
            {"mData": "orderCode"},
            {"mData": "telephone"},
            {"mData": "memberName"},
            {"mData": "orderStatusName"},
            {"mData": "orderTypeCn"},
            {"mData": "deliverTypeCn"},
            {"mData": "amountTotle"},
            {"mData": "payInfact"},
            {
                "mData": "orderTime",
                "fnRender": function (rowData) {
                    if (rowData.aData.orderTime != null) {
                        return new Date(rowData.aData.orderTime).Format("yyyy-MM-dd hh:mm:ss");
                    } else {
                        return "";
                    }
                }
            },
            //{"mData": "comment"},
            {
                "mData": null,
                "fnRender": function (rowData) {
                    var str = '<a class="order_edit" href="#" onclick=order_edit("'+ rowData.aData.orderId+'");>详细</a>';
                    return str;

                }
            }

        ]
    };
    oTable = $('#ord_master_table').dataTable(tableOption);
}
//点击详情按钮进行画面跳转
function order_edit(id) {
    setSessionValue();
    sessionStorage.setItem("orderId", id);
    $('#content').load('ord_change/edit.html');
}


function setSessionValue(){
    //搜索框的值
    sessionStorage.setItem("orderCode",$('#orderCode').val());
    sessionStorage.setItem("type",$('#type').val());
    sessionStorage.setItem("payStatus",$('#payStatus').val());
    sessionStorage.setItem("deliverStatus",$('#deliverStatus').val());
    sessionStorage.setItem("orderTimeStart",$('#orderTimeStart').val());
    sessionStorage.setItem("orderTimeEnd",$('#orderTimeEnd').val());
    sessionStorage.setItem("orderIdForOrderList",$('#orderId').val());
    sessionStorage.setItem("telephone",$('#telephone').val());
    sessionStorage.setItem("memberName",$('#memberName').val());
//
//     sessionStorage.setItem("sEcho",oTable.fnSettings()._iDisplayStart);
//     sessionStorage.setItem("iDisplayLength",oTable.fnSettings()._iDisplayLength);

}

function setQuery(){

    var orderCode = sessionStorage.getItem("orderCode");
    if(orderCode){
        $('#orderCode').val(orderCode);
    }





    var orderTimeStart = sessionStorage.getItem("orderTimeStart");
    if(orderTimeStart){
        $('#orderTimeStart').val(orderTimeStart);
    }

    var orderTimeEnd = sessionStorage.getItem("orderTimeEnd");
    if(orderTimeEnd){
        $('#orderTimeEnd').val(orderTimeEnd);
    }

    var orderId = sessionStorage.getItem("orderIdForOrderList");
    if(orderId){
        $('#orderId').val(orderId);
    }

    var telephone = sessionStorage.getItem("telephone");
    if(telephone){
        $('#telephone').val(telephone);
    }
}