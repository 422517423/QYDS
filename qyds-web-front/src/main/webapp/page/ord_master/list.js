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
            autoclose: true,
            onSelect: gotoDate
        }).on('changeDate',gotoDate);
        $('body').removeClass("modal-open"); // fix bug when inline picker is used in modal
    }

    //检索按钮点击事件
    $("#btn_search").click(function () {
        //刷新Datatable，会自动激发retrieveData
        oTable.fnDraw();
    });

    //导出表格
    $('#btn_export').on('click',function(){
        //var param = "orderId="+$("#orderId").val()+"&"+
        var param = "orderId=&"+
            "orderCode="+$("#orderCode").val()+"&"+
            "actionId="+$("#activity").val()+"&"+
            "couponId="+$("#couppon").val()+"&"+
            "orderStatus="+$("#type").val()+"&"+
            "payStatus="+$("#payStatus").val()+"&"+
            "deliverStatus="+$("#deliverStatus").val()+"&"+
            "orderTimeStart="+$("#orderTimeStart").val()+"&"+
            "orderTimeEnd="+$("#orderTimeEnd").val()+"&"+
            "telephone="+$("#telephone").val()+"&"+
            "helpBuy="+$("#helpBuy").val()+"&"+
            "memberName="+encodeURI($("#memberName").val())+"&"+
            "salerName="+encodeURI($("#salerName").val())+"&"+
            "deliverType="+$("#deliverType").val();

        window.open("/qyds-web-front/ord_master/export.json?" + param);

    });
    //initTable();
});

function gotoDate(){

    var orderTimeStart = $('#orderTimeStart').val();
    var orderTimeEnd = $('#orderTimeEnd').val();

    if(orderTimeStart
        &&orderTimeEnd){
        //获取活动名称和优惠劵名称列表
        var url = "/ord_master/getActivityCouponList.json";
        var json = {};
        json.orderTimeStart = orderTimeStart;
        json.orderTimeEnd = orderTimeEnd;

        var success = function (data) {
            if (data.resultCode == '00') {
                $("#couppon").empty();
                $("#activity").empty();
                var activitylist = data.activitylist;
                var coupponlist = data.coupponlist;

                if (coupponlist != null) {
                    //为优惠劵类型选择框添加数据
                    var $option = $('<option>').attr('value', '').text('全部');
                    $("#couppon").append($option);
                    $.each(coupponlist, function (index, item) {
                         $option = $('<option>').attr('value', item.coupon_id).text(item.coupon_name);
                        $("#couppon").append($option);
                    });
                }

                if (activitylist != null) {
                    var $option = $('<option>').attr('value', '').text('全部');
                    $("#activity").append($option);
                    $.each(activitylist, function (index, item) {
                        var $option = $('<option>').attr('value', item.activity_id).text(item.activity_name);
                        $("#activity").append($option);
                    });
                }


            } else {
                showAlert('活动列表和优惠劵列表数据获取失败');
            }
        };
        var error = function () {
            showAlert('活动列表和优惠劵列表数据获取失败');
        };
        //品牌类型码表中的数据code为GDS_BRAND_TYPE 每一个码表的code不一样
        axse(url, {'data':JSON.stringify(json)}, success, error);
    }

}

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
            aoData.push({"name": "orderStatus", "value": $("#type").val()});
            aoData.push({"name": "payStatus", "value": $("#payStatus").val()});
            aoData.push({"name": "deliverStatus", "value": $("#deliverStatus").val()});
            aoData.push({"name": "orderTimeStart", "value": $("#orderTimeStart").val()});
            aoData.push({"name": "orderTimeEnd", "value": $("#orderTimeEnd").val()});
            aoData.push({"name": "telephone", "value": $("#telephone").val()});
            aoData.push({"name": "deliverType", "value": $("#deliverType").val()});
            aoData.push({"name": "memberName", "value": $("#memberName").val()});
            aoData.push({"name": "actionId", "value": $("#activity").val()});
            aoData.push({"name": "couponId", "value": $("#couppon").val()});
            aoData.push({"name": "helpBuy", "value": $("#helpBuy").val()});
            aoData.push({"name": "salerName", "value": $("#salerName").val()});
            aoData.push({"name": "salerTelephone", "value": $("#salerTelephone").val()});
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
            // 20180124添加申请退款金额
            {"mData": "rexPrice"},
            // 20180124添加实际退款金额
            {"mData": "rexInfactPrice"},
            {"mData": "activityName"},
            {"mData": "couponName"},
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
                    console.log(rowData);
                    if (rowData.aData.helpBuy == "0") {
                        return "自主下单";
                    } else {
                        return "代买下单";
                    }
                }
            },
            {"mData": "salerName"},
            {"mData": "salerTelephone"},
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
    $('#content').load('ord_master/edit.html');
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
    sessionStorage.setItem("helpBuy",$('#helpBuy').val());
    sessionStorage.setItem("salerTelephone",$('#salerTelephone').val());
    sessionStorage.setItem("salerName",$('#salerName').val());
//
//     sessionStorage.setItem("sEcho",oTable.fnSettings()._iDisplayStart);
//     sessionStorage.setItem("iDisplayLength",oTable.fnSettings()._iDisplayLength);

}

function setQuery(){

    var orderCode = sessionStorage.getItem("orderCode");
    if(orderCode){
        $('#orderCode').val(orderCode);
    }

    // var helpBuy = sessionStorage.getItem("helpBuy");
    // if(helpBuy){
    //     $('#helpBuy').val(helpBuy)
    // }

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