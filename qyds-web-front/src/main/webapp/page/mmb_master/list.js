/**
 * Created by YiLian on 2016/7/18.
 */
var table;
$(document).ready(function () {

    // 获取下拉列表的码表
    getOptionCode("SEX", "cond_sex");
    getOptionCode("MEMBER_TYPE", "cond_type");
    getOptionCode("MEMBER_LEVEL", "cond_member_level_id");
    initProvince();
    initTable();

    if (jQuery().datepicker) {
        $('.date-picker').datepicker({
            rtl: App.isRTL(),
            autoclose: true
        });
    }

    //检索按钮点击事件
    $("#btn_search").click(function () {
        //刷新Datatable，会自动激发retrieveData
        table.fnDraw();
    });

    //新建按钮点击事件
    $("#btn_add").click(function () {
        sessionStorage.setItem("memberId", "");
        $('#customDialog').load('mmb_master/edit.html');
    });

    $('#btn_export').on('click',function(){
        var param = "memberName="+$("#cond_member_name").val()+"&"+
            "type="+$("#cond_type").val()+"&"+
            "telephone="+$("#cond_telephone").val()+"&"+
            "memberLevelId="+$("#cond_member_level_id").val()+"&"+
            "deleted="+$("#cond_deleted").val()+"&"+
            "startDate="+$("#cond_start_date").val()+"&"+
            "endDate="+$("#cond_end_date").val()+"&"+
            "provinceCode="+$("#cond_province_code").val()+"&"+
            "cityCode="+encodeURI($("#cond_city_code").val())+"&"+
            "districtCode="+$("#cond_district_code").val();

        window.open("/qyds-web-front/mmb_master/export.json?" + param);

    });

});

//获得码表数据方法
function initProvince() {
    getErpProvince("cond_province_code","cond_city_code","cond_district_code");
}

function selectProvince() {
    getErpCity("cond_province_code","cond_city_code","cond_district_code");
}

function selectCity() {
    getErpDistrict("cond_city_code","cond_district_code");
}

function initTable() {
    var tableOption = {
        "bProcessing": true,
        "bServerSide": true,
        "fnServerData": fnServerData,
        "oLanguage": {
            "sEmptyTable": "没有数据",
            "sLengthMenu": "_MENU_ 件",
            "oPaginate": {
                "sPrevious": "上一件",
                "sNext": "下一件"
            }
        },
        "bFilter": false,
        "bSort": false,
        "sAjaxSource": "../mmb_master/getList.json",
        "fnServerParams": function (aoData) {
            // 设置参数
            aoData.push({"name": "memberName", "value": $("#cond_member_name").val()});
            aoData.push({"name": "type", "value": $("#cond_type").val()});
            aoData.push({"name": "telephone", "value": $("#cond_telephone").val()});
            aoData.push({"name": "memberLevelId", "value": $("#cond_member_level_id").val()});
            aoData.push({"name": "deleted", "value": $("#cond_deleted").val()});
            aoData.push({"name": "startDate", "value": $("#cond_start_date").val()});
            aoData.push({"name": "endDate", "value": $("#cond_end_date").val()});
            aoData.push({"name": "provinceCode", "value": $("#cond_province_code").val()});
            aoData.push({"name": "cityCode", "value": $("#cond_city_code").val()});
            aoData.push({"name": "districtCode", "value": $("#cond_district_code").val()});
        },
        "aoColumns": [
            {"mData": "memberLevelName"},
            {"mData": "typeName"},
            {
                "mData": null,
                "fnRender": function (rowData) {
                    if (rowData.aData.insertTime != null) {
                        return new Date(rowData.aData.insertTime).Format("yyyy-MM-dd");
                    } else {
                        return "";
                    }
                }
            },
            {"mData": "memberName"},
            {"mData": "point"},
            {"mData": "allPoint"},
            {"mData": "sexName"},
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
            {"mData": "telephone"},
            {"mData": "email"},
            {"mData": "provinceName"},
            {"mData": "cityName"},
            {"mData": "districtName"},
            {
                "mData": null,
                "fnRender": function (rowData) {
                    return '<a onclick=master_detail("' + rowData.aData.memberId + '")>详细</a>'
                        + '&nbsp;&nbsp; '
                        + '<a onclick=master_edit("' + rowData.aData.memberId + '");>编辑</a>'
                        // + '&nbsp;&nbsp; '
                        // + '<a onclick=deleteConfirm("' + rowData.aData.memberId + '");>删除</a>';
                        + '&nbsp;&nbsp; <a onclick=ponit_record("' + rowData.aData.memberId + '");>积分履历</a>'
                        + '&nbsp;&nbsp; <a onclick=coupon_record("' + rowData.aData.memberId + '");>优惠劵</a>';

                }
            }

        ]
    };
    table = $('#mmb_master_table').dataTable(tableOption);
}

//点击详情按钮进行画面跳转
function master_detail(memberId) {

    sessionStorage.setItem("memberId", memberId);
    $('#customDialog').load('mmb_master/detail.html');
}

function master_edit(memberId) {

    sessionStorage.setItem("memberId", memberId);
    $('#customDialog').load('mmb_master/edit.html');
}

function ponit_record(memberId) {

    sessionStorage.setItem("memberId", memberId);
    $('#customDialog').load('mmb_master/point_record.html');
}

function coupon_record(memberId) {

    sessionStorage.setItem("memberId", memberId);
    $('#customDialog').load('mmb_master/coupon_record.html');
}

//点击删除按钮确认
function deleteConfirm(obj) {
    showConfirm("确定要删除吗?", function () {
        deleteItem(obj);
    })
}

function deleteItem(obj) {
    var url = "/mmb_master/delete.json";
    var param = {
        "memberId": obj
    };

    var success = function (data) {
        if (data.resultCode == '00') {
            showAlert("删除成功!", function () {
                table.fnDraw();
            });
        } else {
            showAlert('删除失败,原因:' + data.resultMessage);
        }
    };
    var error = function () {
        showAlert('删除失败,网络异常.');
    };
    axse(url, {'data': JSON.stringify(param)}, success, error);
}