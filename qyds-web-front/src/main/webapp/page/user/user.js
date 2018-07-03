/**
 * Created by YiLian on 2016/7/18.
 */
var table;
var username = sessionStorage.getItem("userName");
var userId = sessionStorage.getItem("userId");
var orgId = sessionStorage.getItem("orgId");

$(document).ready(function () {

    $("#userForm").validate({
        errorElement: 'span', //default input error message container
        errorClass: 'help-block', // default input error message class
        focusInvalid: false, // do not focus the last invalid input
        rules: {
            login_id: {
                required: true,
                minlength: 6,
                remote:{
                    type:"POST",
                    url:"/qyds-web-front/auth/checkloginId.json",
                    dataType: "json",
                    data:{
                        loginId:function(){return $("#login_id").val();},
                        userId:function(){return $("#user_id").val();}
                    }
                }
            },
            // password: {
            //     required: true,
            //     minlength: 6
            // },
            user_name: {
                required: true
            },
            role_id: {
                required: true,
                min: 1
            },
            email: {
                required: true,
                email: true
            },
            mobile_edit: {
                required: true,
                mobile:true
            }
            // agree: "required"
        },
        messages: {
            login_id: {
                required: "请输入登录账号",
                minlength: "登录账号不能小于6位",
                remote: "用户名存在"
            },
            // password: {
            //     required: "请输入登录密码",
            //     minlength: "登录密码不能小于6位"
            // },
            user_name: {
                required: "请输入用户名称"
            },
            role_id: {
                required: "请选择角色",
                min: "请选择角色组"
            },
            email: {
                required: "请输入邮箱",
                email: "请输入正确的邮箱"
            },
            mobile_edit: {
                required: "请输入手机号"
            }
        },

        highlight: function (element) { // hightlight error inputs
            $(element)
                .closest('.form-group').addClass('has-error'); // set error class to the control group
        },

        unhighlight: function (element) { // revert the change done by hightlight
            $(element)
                .closest('.form-group').removeClass('has-error'); // set error class to the control group
        },

        success: function (label) {
            label
                .closest('.form-group').removeClass('has-error'); // set success class to the control group
        }
    });

    $.validator.addMethod('mobile', function( value, element ){

        // /^1\d{10}$/ 来自支付宝的正则
        return this.optional( element ) || /^1\d{10}$/.test( value );

    }, '请输入正确的手机号码');


    initTable();

    //检索按钮点击事件
    $("#btn_search").click(function () {
        //刷新Datatable，会自动激发retrieveData
        table.fnDraw();
    });

    $(".user_edit").live('click', function () {
        oRow = $(this).parents('tr')[0];
        var userId = $(this).attr("userid");
        $("#userEditArea").modal();
        $("#userEditArea").modal(modalSettings);
        $("#userEditArea").modal('show');
        axse("/user/edit.json", {"id": userId}, editSuccessFn, errorFn);
    });

    $("#addBtn").click(function() {
        oRow = null;
        $("#user_id").val('');
        $("#job_id").multipleSelect('refresh');
        $("#userEditArea").modal();
        $("#userEditArea").modal(modalSettings);
        $("#userEditArea").modal('show');
        $('#userForm')[0].reset();
    });

    $("#save_btn").click(function() {
        if (!$("#userForm").valid()){
            return;
        }

        var json = {};
        json.orgId = $("#org_id").val();
        json.userId = $("#user_id").val();
        json.loginId = $("#login_id").val();
        // json.password = $.md5($("#password").val());
        json.userName = $("#user_name").val();
        json.roleId = $("#role_id").val();
        json.orgId = $("#org_id").val();
        json.jobIdArray = $("#job_id").val();
        json.mobile = $("#mobile_edit").val();
        json.email = $("#email").val();
        json.createUser = userId;
        json.updateUser = userId;
        console.log(json);
        axse("/user/save.json", {'data':JSON.stringify(json)}, saveSuccessFn, errorFn);
    });

    var deleteUserId;
    $(".delete_user").live('click', function () {
        oRow = $(this).parents('tr')[0];
        deleteUserId = $(this).attr('userid');
        $("#user_delete_modal").modal();
        $("#user_delete_modal").modal(modalSettings);
        $("#user_delete_modal").modal('show');

    });

    //删除按钮点击
    $("#del_btn").click(function() {
        $("#user_delete_modal").modal('hide');
        var json = {};
        json.userId = deleteUserId;
        json.updateUser = userId;
        axse("/user/delete.json", {"data": JSON.stringify(json)}, delSuccessFn, errorFn);
        deleteUserId = null;
    });


    var resetPasswordId;
    var loginUserId;
    $(".reset_password").live('click', function () {
        oRow = $(this).parents('tr')[0];
        oRow = $(this).closest('tr');
        loginUserId = oRow.children().eq(0).text();
        resetPasswordId = $(this).attr('userid');
        $("#change_password_modal").modal();
        $("#change_password_modal").modal(modalSettings);
        $("#change_password_modal").modal('show');
    });

    //重置密码点击
    $("#resetPassword").click(function() {
        $("#change_password_modal").modal('hide');
        var json = {};
        json.userId = resetPasswordId;
        json.updateUser = userId;
        json.loginId = loginUserId;
        // json.password = $.md5(loginUserId);
        axse("/user/resetPassword.json", {"data": JSON.stringify(json)}, resetSuccessFn, errorFn);
        deleteUserId = null;
    });


    getSysRole();
    getShpChildOrg();
});

// 保存成功
function saveSuccessFn(data) {
    $("#userEditArea").modal('hide');
    if (data.resultCode == '00') {
        table.fnDraw();
    }
}


function editSuccessFn(data) {
    if (data.resultCode == '00') {
        console.log(data.data);
        $("#userEditArea").modal();
        $("#userEditArea").modal(modalSettings);
        $("#userEditArea").modal('show');

        $("#user_id").val(data.data.userId);
        $("#login_id").val(data.data.loginId);
        $("#org_id").val(data.data.orgId);
        // $("#password").parent().parent().hide();
        $("#user_name").val(data.data.userName);
        $("#role_id").val(data.data.roleId);
        $("#org_name").val(data.data.orgName);
        $("#mobile_edit").val(data.data.mobile);
        $("#email").val(data.data.email);
        $("#job_id").val(data.data.jobIdArray);
        $("#job_id").multipleSelect('refresh');
    }
}

function initTable() {
    var tableOption = {
        "bProcessing": true,
        "bServerSide": true,
        "fnServerData": fnServerData,
        "bFilter": false,
        "bSort": false,
        "sAjaxSource": "../user/getList.json",
        "fnServerParams": function (aoData) {
            // 设置参数
            aoData.push({"name": "mobile", "value": $("#mobile").val()});
            aoData.push({"name": "userName", "value": $("#userName").val()});
            aoData.push({"name": "orgName", "value": $("#orgName").val()});
        },
        "aoColumns": [
            {"mData": "loginId"},
            {"mData": "userName"},
            {"mData": "roleName"},
            {"mData": "mobile"},
            {"mData": "email"},
            {"mData": "orgName"},
            {"mData": "createUser"},
            {
                "mData": null,
                "fnRender": function (rowData) {
                    return '<a class="user_edit" href="#" userid="' + rowData.aData.userId + '"  data-toggle="modal">详细</a>&nbsp;&nbsp;<a class="delete_user" userid="' + rowData.aData.userId + '" href="javascript:;">删除</a>&nbsp;&nbsp;<a class="reset_password" userid="' + rowData.aData.userId + '" href="javascript:;">重置密码</a> ';

                }
            }
        ]
    };
    table = $('#user_table').dataTable(tableOption);
}


function getSysRole() {
    axse("/user/getSysRole.json", {}, sysRoleSuccessFn, errorFn);
}

function getShpChildOrg() {
    axse("/user/getShpChildOrg.json", {}, shpOrgSuccessFn, errorFn);
}

// 获取用户系统角色
function sysRoleSuccessFn(data) {
    if (data.resultCode == '00') {
        var array = data.data;
        if (array != null) {
            $("#role_id").empty();
            $('<option value=""></option>').appendTo($("#role_id"));
            for (var i = 0; i < array.length; i++) {
                var data = new Array()
                $('<option value="' + array[i].roleId + '">' + array[i].roleName + '</option>').appendTo($("#role_id"));

            }
        }
    }
}

// 获取组织名称
function shpOrgSuccessFn(data) {
    if (data.resultCode == '00') {
        var array = data.data;
        console.log(array);
        if (array != null ) {
            $("#org_id").empty();
            $('<option value=""></option>').appendTo($("#org_id"));
            for (var i = 0; i < array.length; i++) {
                $('<option value="' + array[i].orgId + '">' + array[i].orgName + '</option>').appendTo($("#org_id"));
            }
        }
    }
}


function delSuccessFn (data) {
    if (data.resultCode == '00') {
        //刷新Datatable，会自动激发retrieveData
        table.fnDraw();
    }
}

function errorFn(data) {
    showAlert('操作失败');
}

function resetSuccessFn (data) {
    if (data.resultCode == '00') {
        showAlert('重置密码成功');
    }else{
        showAlert('重置密码失败');
    }
}