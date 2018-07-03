/**
 * Created by zlh on 2016/7/31.
 */
var oTable;
var oRow;
$(document).ready(function() {

    initTable();

    initRTable();

    if (jQuery().datepicker) {
        $('.date-picker').datepicker({
            rtl: App.isRTL(),
            autoclose: true,
        });
        $('body').removeClass("modal-open");
    }

    //获取性别码表数据
    getOptionCode("SEX","sex_code");

    //检索按钮点击事件
    $("#btn_search").click(function(){
        //刷新Datatable，会自动激发retrieveData
        oTable.fnDraw();
    });

    //检索按钮点击事件
    $("#btn_record").click(function(){
        //刷新Datatable，会自动激发retrieveData
        rTable.fnDraw();
    });

});


//表格js初始化
function initTable() {
    oTable = $('#list_table').dataTable({
        "aoColumns": [
            { "mData": "memberCode" },
            { "mData": "memberName"},
            //{ "mData": "sexName"},
            {   "mData": null,
                "fnRender": function ( oObj ) {
                    if(oObj.aData.sexCode == "1"){
                        return "男";
                    }else if(oObj.aData.sexCode == "2"){
                        return "女";
                    }else if(oObj.aData.sexCode == "2"){
                        return "";
                    }
                }
            },
            {
                "mData": "birthday",
                "fnRender": function (rowData) {
                    if (rowData.aData.birthday != null) {
                        return new Date(rowData.aData.birthday).Format("yyyy-MM-dd");
                    } else {
                        return "";
                    }
                }
            },
            { "mData": "mobil"},
            { "mData": "address"},
            {
                "mData": "registTime",
                "fnRender": function (rowData) {
                    if (rowData.aData.registTime != null) {
                        return new Date(rowData.aData.registTime).Format("yyyy-MM-dd");
                    } else {
                        return "";
                    }
                }
            },
            { "mData": "memberGrade"},
            {
                "mData": null,
                "fnRender": function ( rowData ) {
                    return '<a class="member_edit" href="#" onclick=edit("' + rowData.aData.memberCode + '")>详细</a>'
                        +' <a class="record" href="#" onclick=record("' + rowData.aData.memberCode + '")>积分履历</a>';
                }
            }

        ],
        "bAutoWidth": false,    //关闭自适应列宽，导致列表后半部空白。IE8下现象
        "bProcessing": false,
        "bServerSide": true,
        "bSort": false,
        "sAjaxSource": "../erp_member/getPage.json",
        "fnServerParams": function ( aoData ) {
            aoData.push( { "name": "memberCode", "value": $("#member_code_in").val() } );
            aoData.push( { "name": "memberName", "value": $("#member_name_in").val() } );
            aoData.push( { "name": "mobil", "value": $("#mobil_in").val() } );
            aoData.push( { "name": "address", "value": $("#address_in").val() } );
        },
        "fnServerData": fnServerData,
        "bFilter": false
    });

}
//表格js初始化
function initRTable() {
    rTable = $('#record_table').dataTable({
        "aoColumns": [
            {
                "mData": "inoutTime",
                "fnRender": function (rowData) {
                    if (rowData.aData.inoutTime != null) {
                        return new Date(rowData.aData.inoutTime).Format("yyyy-MM-dd hh:mm:ss");
                    } else {
                        return "";
                    }
                }
            },
            { "mData": "inoutPoint"}
        ],
        "bAutoWidth": false,    //关闭自适应列宽，导致列表后半部空白。IE8下现象
        "bProcessing": false,
        "bServerSide": true,
        "bSort": false,
        "sAjaxSource": "../erp_point_record/getRecordPage.json",
        "fnServerParams": function ( aoData ) {
            aoData.push( { "name": "memberCode", "value": $("#member_code_record").val() } );
            aoData.push( { "name": "startDate", "value": $("#start_date").val() } );
            aoData.push( { "name": "endDate", "value": $("#end_date").val() } );
        },
        "fnServerData": fnServerData,
        "bFilter": false

    });
}

var modalSettings = {
    backdrop : 'static',
    keyboard : false
};

//点击详情按钮
function edit(code){
    axse("/erp_member/edit.json", {"id": code}, editSuccessFn, errorFn);
}

//点击详情按钮
function record(code){
    $("#member_code_record").val(code);
    rTable.fnDraw();
    $("#recordArea").modal();
    $("#recordArea").modal(modalSettings);
    $("#recordArea").modal('show');
}

function editSuccessFn(data) {
    if (data.resultCode == '00') {
        $("#member_code").val(data.data.memberCode);
        $("#member_name").val(data.data.memberName);
        $("#sex_code").val(data.data.sexCode);
        //$("#sex_name").val(data.data.sexName);
        $("#mobil").val(data.data.mobil);
        $("#birthday").val(new Date(data.data.birthday).Format("yyyy-MM-dd"));
        $("#province_name").val(data.data.provinceName);
        $("#city_name").val(data.data.cityName);
        $("#district_name").val(data.data.districtName);
        $("#email").val(data.data.email);
        $("#store_name").val(data.data.storeName);
        $("#seller_name").val(data.data.sellerName);
        $("#profession").val(data.data.profession);
        $("#address").val(data.data.address);
        $("#post_code").val(data.data.postCode);
        $("#checked").val(data.data.checked);
        $("#income").val(data.data.income);
        $("#regist_time").val(new Date(data.data.registTime).Format("yyyy-MM-dd"));
        $("#member_grade").val(data.data.memberGrade);
        $("#amount").val(data.data.amount);
        $("#point").val(data.data.point);
        $("#ticket").val(data.data.ticket);
        $("#editArea").modal();
        $("#editArea").modal(modalSettings);
        $("#editArea").modal('show');
    }
}

// 失败的回调方法
function errorFn() {
    showAlert('数据获取失败');
}
