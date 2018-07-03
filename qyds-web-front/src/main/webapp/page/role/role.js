/**
 * Created by C_Nagai on 2016/6/24.
 */
var oTable;
var oRow;
var username = sessionStorage.getItem("userName");
var userId = sessionStorage.getItem("userId");
$(document).ready(function() {

    var validator = $("#roleForm").validate({
        errorElement: 'span', //default input error message container
        errorClass: 'help-block', // default input error message class
        focusInvalid: false, // do not focus the last invalid input
        rules: {
            role_name: {
                required: true
            }
        },
        messages: {
            role_name: {
                required: "请输入角色名称"
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

    $(".role_edit").live('click', function () {
        oRow = $(this).parents('tr')[0];
        var roleId = $(this).attr("roleId");
        axse("/role/edit.json", {"id": roleId}, editSuccessFn, errorFn);
    });

    var deleteRoleId;
    $(".delete_role").live('click', function () {
        oRow = $(this).parents('tr')[0];
        deleteRoleId = $(this).attr("roleId");
        $("#role_delete_modal").modal();
        $("#role_delete_modal").modal(modalSettings);
        $("#role_delete_modal").modal('show');
        
    });

    $("#del_btn").click(function() {
        $("#role_delete_modal").modal('hide');
        var json = {};
        json.roleId = deleteRoleId;
        json.updateUser = userId;
        axse("/role/delete.json", {"data": JSON.stringify(json)}, delSuccessFn, errorFn);
        deleteRoleId = null;
    });

    $('#menuTreeModal').on('hidden.bs.modal',function () {
        $('#menuRoleId').val('');
        $.fn.zTree.destroy("roleMenuTree");
    });

    $("#role_table").on('click','.add_menu', function () {
        var id = $(this).attr('roleid');
        if(id){
            $('#menuRoleId').val(id);

            ajaxPost('/role/getRoleMenu.json',{"id": id},function(data){
                $('#menuTreeModal').modal(modalSettings);

                if($.type(data) == 'string' && data != ''){
                    data = $.parseJSON(data);
                }

                if(data.resultCode == '00'){
                    if($.isArray(data.data)){
                        var settings = {
                            data: {
                                key: {
                                    name: "menuName"
                                },
                                simpleData: {
                                    enable: true,
                                    idKey: "menuId",
                                    pIdKey: "parentId",
                                    name: "menuName",
                                    rootPId: 0
                                }
                            },
                            check: {
                                enable: true
                            }
                        };
                        $.fn.zTree.init($("#roleMenuTree"),settings, data.data);
                        var tree = $.fn.zTree.getZTreeObj("roleMenuTree");
                        tree.expandAll(true);
                    }
                }else{
                    $('#alertMsg').show();
                }
            },null,function () {
                $('#alertMsg').show();
            },null);
        }
    });

    $("#addBtn").click(function() {
        oRow = null;
        $("#role_id").val('');
        $("#roleEdit").modal();
        $("#roleEdit").modal(modalSettings);
        $("#roleEdit").modal('show');
    });

    $("#save_btn").click(function() {
        if (!$("#roleForm").valid()){
            return;
        }

        var json = {};
        json.roleId = $("#role_id").val();
        json.roleName = $("#role_name").val();
        json.createUser = userId;
        json.updateUser = userId;
        axse("/role/save.json", {'data':JSON.stringify(json)}, saveSuccessFn, errorFn);
    });

    $("#roleEdit").on("hidden.bs.modal",function(e){
        $('#roleForm')[0].reset();
        $("#roleForm").validate().resetForm();
    });

    $('#editModal').on('hidden.bs.modal',function(){
        $('#roleMenuTree').empty();
    });

    //保存菜单按钮
    $('#saveMenuBtn').on('click',function(){
        var tree = $.fn.zTree.getZTreeObj("roleMenuTree");
        var nodes = tree.getCheckedNodes(true);
        var roleId = $('#menuRoleId').val();

        var data = new Array();
        $.each(nodes,function(i,v){
            data.push({menuId: v.menuId,roleId: roleId});
        });

        ajaxPost('/role/saveRoleMenu.json',{'data':JSON.stringify(data),'roleId':roleId},function(data){
            if($.type(data) == 'string' && data != ''){
                data = $.parseJSON(data);
            }
            if(data.resultCode == '00'){
                $('#menuTreeModal').modal('hide');
            }else{
                showAlert("菜单授权失败");
            }
        },null,function(){
            showAlert("菜单授权失败");
        },null);

    });

    getList();

});

var modalSettings = {
    backdrop : 'static',
    keyboard : false
};

var TableEditable = function () {
    return {
        //main function to initiate the module
        init: function () {
            oTable = $('#role_table').dataTable({
                "aLengthMenu": [
                    [5, 15, 20, -1],
                    [10, 20, 50, 100] // change per page values here
                ],
                // set the initial value
                "iDisplayLength": 10,

                "sPaginationType": "bootstrap",
                "oLanguage": {
                    "sLengthMenu": "_MENU_ 件",
                    "oPaginate": {
                        "sPrevious": "上一件",
                        "sNext": "下一件"
                    }
                },
                "aoColumnDefs": [{
                    'bSortable': false,
                    'aTargets': [0]
                }],
                //降序排列 第一列降序
                "aaSorting": [[0,'desc']]
            });
            //隐藏第一列
            oTable.fnSetColumnVis( 0, false );
            // jQuery('#role_table_wrapper .dataTables_filter input').addClass("form-control input-medium"); // modify table search input
            // jQuery('#role_table_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
            // jQuery('#role_table_wrapper .dataTables_length select').select2({
            //     showSearchInput : false //hide search box with special css class
            // }); // initialize select2 dropdown

        }
    };
}();

function updateRow(oTable, nRow, data) {
    oTable.fnUpdate(data.data.updateTime, nRow, 0, false);
    oTable.fnUpdate(data.data.roleName, nRow, 1, false);
    // oTable.fnUpdate('<a class="edit" href="">Edit</a>', nRow, 7, false);
    // oTable.fnUpdate('<a class="delete" href="">Delete</a>', nRow, 8, false);
    oTable.fnDraw();
}

function addRow(oTable, data) {
    oTable.fnAddData(data);
}

function getList() {
    axse("/role/getList.json", {}, successFn, errorFn);
}

// 获取角色列表成功方法
function successFn(data) {
    if (data.resultCode == '00') {
        var array = data.data;
        if (array != null) {
            for (var i = 0; i < array.length; i++) {
                var data = new Array();
                data[0] = array[i].updateTime;
                data[1] = array[i].roleName;
                data[2] = array[i].createUser;
                data[3] = new Date(array[i].createTime).Format('yyyy-MM-dd');
                data[4] = '<a class="role_edit" href="#" roleId="' + array[i].roleId + '" data-toggle="modal">详细</a>';
                data[5] = '<a class="delete_role" roleId="' + array[i].roleId + '" href="javascript:;">删除</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="add_menu" roleId="' + array[i].roleId + '" href="javascript:;">绑定菜单</a>';
                addRow(oTable, data);
            }
        }
    }
}

// 根绝角色ID获取角色信息成功方法
function editSuccessFn(data) {
    if (data.resultCode == '00') {
        $("#roleEdit").modal();
        $("#roleEdit").modal(modalSettings);
        $("#roleEdit").modal('show');
        $("#role_name").val(data.data.roleName);
        $("#role_id").val(data.data.roleId);
    }
}

// 保存角色信息成功方法
function saveSuccessFn(data) {
    $("#roleEdit").modal('hide');
    if (data.resultCode == '00') {
        if (oRow != null) {
            updateRow(oTable, oRow, data);
        } else {
            var rowData = new Array();
            rowData[0] = data.data.updateTime;
            rowData[1] = data.data.roleName;
            rowData[2] = data.data.createUser;
            rowData[3] = new Date(data.data.createTime).Format('yyyy-MM-dd');
            rowData[4] = '<a class="role_edit" href="#" roleId="' + data.data.roleId + '" data-toggle="modal">详细</a>';
            rowData[5] = '<a class="delete_role" roleId="' + data.data.roleId + '" href="javascript:;">删除</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class="add_menu" roleId="' + data.data.roleId + '" href="javascript:;">绑定菜单</a>';
            addRow(oTable, rowData);
        }
    }
}

function delSuccessFn (data) {
    if (data.resultCode == '00') {
        oTable.fnDeleteRow(oRow);
    }
}

// 失败方法
function errorFn() {

}


