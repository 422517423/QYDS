/**
 * Created by YiLian on 2016/7/18.
 */
var table;
$(document).ready(function () {

    // 获取下拉列表的码表
    getOptionCode("SEX", "cond_sex");
    getOptionCode("MEMBER_TYPE", "cond_type");
    getOptionCode("MEMBER_LEVEL", "cond_member_level_id");

    initTable();

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

});

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
            aoData.push({"name": "isValid", "value": $("#cond_is_valid").val()});
        },
        "aoColumns": [
            {"mData": "memberLevelName"},
            {"mData": "typeName"},
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
            {
                "mData": null,
                "fnRender": function (rowData) {

                    if (rowData.aData.isValid && rowData.aData.isValid == '0') {
                        return '<a onclick=master_detail("' + rowData.aData.memberId + '")>详细</a>'
                            + '&nbsp;&nbsp; '
                            + '<a onclick=master_edit("' + rowData.aData.memberId + '");>编辑</a>'
                            + '&nbsp;&nbsp; '
                            + '<a onclick=deleteConfirm("' + rowData.aData.memberId + '");>禁用</a>'
                            + '&nbsp;&nbsp; <a onclick=ponit_record("' + rowData.aData.memberId + '");>积分履历</a>'
                            + '&nbsp;&nbsp; <a onclick=coupon_record("' + rowData.aData.memberId + '");>优惠劵</a>'
                            + '&nbsp;&nbsp; <a onclick=change_grade("' + rowData.aData.memberId + '","' + rowData.aData.memberLevelId +'");>修改级别</a>'
                            + '&nbsp;&nbsp; <a onclick=send_coupon("' + rowData.aData.memberId + '");>补发优惠劵</a>'
                            + '&nbsp;&nbsp; <a onclick=erp_ord("' + rowData.aData.memberId + '");>线下订单</a>'
                            ;
                    } else {
                        return '<a onclick=master_detail("' + rowData.aData.memberId + '")>详细</a>'
                            + '&nbsp;&nbsp; '
                            + '<a onclick=master_edit("' + rowData.aData.memberId + '");>编辑</a>'
                            + '&nbsp;&nbsp; '
                            + '<a onclick=unDeleteConfirm("' + rowData.aData.memberId + '");>恢复</a>'
                            + '&nbsp;&nbsp; <a onclick=ponit_record("' + rowData.aData.memberId + '");>积分履历</a>'
                            + '&nbsp;&nbsp; <a onclick=coupon_record("' + rowData.aData.memberId + '");>优惠劵</a>'
                            + '&nbsp;&nbsp; <a onclick=change_grade("' + rowData.aData.memberId + '","' + rowData.aData.memberLevelId +'");>修改级别</a>'
                            + '&nbsp;&nbsp; <a onclick=send_coupon("' + rowData.aData.memberId + '");>补发优惠劵</a>'
                            + '&nbsp;&nbsp; <a onclick=erp_ord("' + rowData.aData.memberId + '");>线下订单</a>'
                            ;
                    }

                }
            }

        ]
    };
    table = $('#mmb_master_table').dataTable(tableOption);
}

//点击详情按钮进行画面跳转
function master_detail(memberId,grade) {

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

function send_coupon(memberId) {

    sessionStorage.setItem("memberId", memberId);
    $('#customDialog').load('mmb_master_super/send_coupon.html');
}

function erp_ord(memberId) {

    sessionStorage.setItem("memberId", memberId);
    $('#customDialog').load('mmb_master_super/erp_order_master.html');
}


function change_grade(memberId,grade) {

    sessionStorage.setItem("memberId", memberId);
    sessionStorage.setItem("grade", grade);
    $('#customDialog').load('mmb_master_super/change_grade.html');
}

//点击删除按钮确认
function deleteConfirm(obj) {
    showConfirm("确定要禁用该会员吗?", function () {
        deleteItem(obj);
    })
}

//点击恢复按钮确认
function unDeleteConfirm(obj) {
    showConfirm("确定要恢复该会员吗?", function () {
        undeleteItem(obj);
    })
}

function deleteItem(obj) {
    var url = "/mmb_master/delete.json";
    var param = {
        "memberId": obj
    };

    var success = function (data) {
        if (data.resultCode == '00') {
            showAlert("禁用成功!", function () {
                table.fnDraw();
            });
        } else {
            showAlert('禁用失败,原因:' + data.resultMessage);
        }
    };
    var error = function () {
        showAlert('禁用失败,网络异常.');
    };
    axse(url, {'data': JSON.stringify(param)}, success, error);
}


function undeleteItem(obj) {
    var url = "/mmb_master/undelete.json";
    var param = {
        "memberId": obj
    };

    var success = function (data) {
        if (data.resultCode == '00') {
            showAlert("恢复成功!", function () {
                table.fnDraw();
            });
        } else {
            showAlert('恢复失败,原因:' + data.resultMessage);
        }
    };
    var error = function () {
        showAlert('恢复失败,网络异常.');
    };
    axse(url, {'data': JSON.stringify(param)}, success, error);
}