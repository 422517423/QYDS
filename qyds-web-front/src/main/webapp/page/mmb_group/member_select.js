var groupId = sessionStorage.getItem("groupId");

var tableMemberSelect;
var member_addList = [];
var member_oldList = [];
var member_delList = [];

$(document).ready(function () {
    $("#member_select_dialog").modal('show');

    member_addList = [];
    member_oldList = [];
    member_delList = [];

    initTable();
    //检索按钮点击事件
    $("#member_select_btn_search").click(function () {
        member_addList.splice(0,member_addList.length);
        member_oldList.splice(0,member_oldList.length);
        member_delList.splice(0,member_delList.length);
        tableMemberSelect.fnDraw();
    });
    // 获取下拉列表的码表
    //获取码表数据
    getOptionCode("MEMBER_LEVEL", "member_select_level");
    getOptionCode("MEMBER_TYPE", "member_select_type");

    ajaxPost('/mmb_group/getProvinces.json',{},function(res){
        if (res.resultCode == '00') {
            var data = res.data;
            $.each(data,function(i,v){
                $('<option value="'+v.pcode+'">'+v.pname+'</option>').appendTo($('#province_code'));
            })
        }
    });

    $('#province_code').change(function(){
        var province = $(this).val();
        if(province){
            ajaxPost('/mmb_group/getProvinces.json',{'provinceCode':province},function(res){

                if (res.resultCode == '00') {
                    var data = res.data;

                    $.each(data,function(i,v){
                        $('<option value="'+v.ccode+'">'+v.cname+'</option>').appendTo($('#city_code'));
                    })
                }
            });
        }else{
            $('#city_code').empty().append('<option value=""></option>');
            $('#district_code').empty().append('<option value=""></option>');
        }
    });

    $('#city_code').change(function(){
        var province = $('#province_code').val();
        var city = $(this).val();
        if(city){
            ajaxPost('/mmb_group/getProvinces.json',{'provinceCode':province,'cityCode':city},function(res){

                if (res.resultCode == '00') {
                    var data = res.data;

                    $.each(data,function(i,v){
                        $('<option value="'+v.dcode+'">'+v.dname+'</option>').appendTo($('#district_code'));
                    })
                }
            });
        }else{
            $('#district_code').empty().append('<option value=""></option>');
        }
    });

    $("#member_select_btn_confirm").click(function () {
        var url = "/mmb_group/updateMemberList.json";
        var param = {
            groupId: groupId,
            addList: member_addList,
            delList: member_delList
        };
        var success = function (data) {
            if (data.resultCode == '00') {
                $("#member_select_dialog").modal('hide');
                showAlert('提交成功!', '提示', function () {
                    table.fnDraw();
                });
            } else {
                showTip('提交失败,原因:' + data.resultMessage);
            }
        };
        var error = function () {
            showTip('网络异常.');
        };
        axse(url, {"data": JSON.stringify(param)}, success, error);
    });

    if (jQuery().datepicker) {
        $('.date-picker').datepicker({
            rtl: App.isRTL(),
            autoclose: true
        });
        $('body').removeClass("modal-open"); // fix bug when inline picker is used in modal
    }

    App.initScroller();
});

function initTable() {
    var tableOption = {
        "bLengthChange": false,
        "bAutoWidth": false,    //关闭自适应列宽，导致列表后半部空白。IE8下现象
        "bProcessing": false,
        "bServerSide": true,
        "fnServerData": fnServerData,
        "bFilter": false,
        "bSort": false,
        "sAjaxSource": "../mmb_group/getMemberList.json",
        "fnServerParams": function (aoData) {
            // 设置参数
            aoData.push({"name": "groupId", "value": groupId});
            aoData.push({"name": "memberType", "value": $("#member_select_type").val()});
            aoData.push({"name": "memberLevelId", "value": $("#member_select_level").val()});
            aoData.push({"name": "memberName", "value": $("#member_select_name").val()});
            aoData.push({"name": "telephone", "value": $("#member_select_telephone").val()});
            aoData.push({"name": "memberStatus", "value": $("#member_select_status").val()});
            aoData.push({"name": "iTimeStart", "value": $("#insert_time_start").val()});
            aoData.push({"name": "iTimeEnd", "value": $("#insert_time_end").val()});
            aoData.push({"name": "provinceCode", "value": $("#province_code").val()});
            aoData.push({"name": "cityCode", "value": $("#city_code").val()});
            aoData.push({"name": "districtCode", "value": $("#district_time").val()});
        },
        "aoColumns": [
            {
                "mData": null,
                "fnRender": function (rowData) {

                    var memberId = rowData.aData.memberId;
                    var memberStatus = rowData.aData.memberStatus;
                    if (memberStatus == '0' && -1 == member_oldList.indexOf(memberId)) {
                        member_oldList.push(memberId);
                    }

                    //
                    // if (member_oldList.indexOf(memberId) > -1 && -1 == member_delList.indexOf(memberId)) {
                    //     return '<input type="checkbox" checked="checked" onclick=changeSelected(this,"' + memberId + '")></input>'
                    // } else if (member_addList.indexOf(memberId) > -1) {
                    //     return '<input type="checkbox" checked="checked" onclick=changeSelected(this,"' + memberId + '")></input>'
                    // } else {
                    //     return '<input type="checkbox" onclick=changeSelected(this,"' + memberId + '")></input>'
                    // }


                    if (member_oldList.indexOf(memberId) > -1 && -1 == member_delList.indexOf(memberId)) {
                        return '<input type="checkbox" checked="checked" onclick=changeStatus(this,"' + memberId + '") data="'+memberId+'"/>';
                    } else if (member_addList.indexOf(memberId) > -1) {
                        return '<input type="checkbox" checked="checked" onclick=changeStatus(this,"' + memberId + '") data="'+memberId+'"/>';
                    } else {
                        return '<input type="checkbox" onclick=changeStatus(this,"' + memberId + '") data="'+memberId+'"/>';
                    }

                }
            },
            {"mData": "memberName"},
            {"mData": "memberLevelName"},
            {"mData": "memberTypeName"}
        ],
        "fnDrawCallback": function() {
            member_addList.splice(0,member_addList.length);
            member_delList.splice(0,member_delList.length);

            var checkboxs = $('#member_select_table tbody input[type="checkbox"]');
            var isAll = true;

            $.each(checkboxs,function(i,v){
                var memberId = $(this).attr('data');
                if(member_oldList.indexOf(memberId) == -1){
                    isAll = false;
                }
            });

            if(isAll){
                $('.allCheck').attr('checked','checked');
            }else{
                $('.allCheck').removeAttr('checked');
            }
        },
        "fnPreDrawCallback": function(){
            //member_oldList.splice(0,member_oldList.length);
            //isAll = true;
        }
    };
    tableMemberSelect = $('#member_select_table').dataTable(tableOption);
}

// function changeSelected(checkbox, memberId) {
//     if (checkbox.checked == true) {
//
//         if (member_oldList.indexOf(memberId) > -1 && member_delList.indexOf(memberId) > -1) {
//             var index = member_delList.indexOf(memberId);
//             member_delList.splice(index, 1);
//         }
//         else if (-1 == member_addList.indexOf(memberId)) {
//             member_addList.push(memberId);
//         }
//
//
//     } else {
//         if (member_oldList.indexOf(memberId) > -1 && -1 == member_delList.indexOf(memberId)) {
//             member_delList.push(memberId);
//         } else if (member_addList.indexOf(memberId) > -1) {
//             var index = member_addList.indexOf(memberId);
//             member_addList.splice(index, 1);
//         }
//     }
// }

function changeStatus(checkbox, memberId) {
    if (checkbox.checked == true) {
        $(checkbox).attr('checked','checked');

        if (member_oldList.indexOf(memberId) > -1 && member_delList.indexOf(memberId) > -1) {
            var index = member_delList.indexOf(memberId);
            member_delList.splice(index, 1);
        }
        else if (-1 == member_addList.indexOf(memberId) && member_oldList.indexOf(memberId) == -1) {
            member_addList.push(memberId);
        }

        if(tableMemberSelect && (member_addList.length == tableMemberSelect.fnSettings()._iDisplayLength || $('#member_select_table tbody input[type="checkbox"][checked=checked]').length == tableMemberSelect.fnSettings()._iDisplayLength)){
            $('.allCheck').attr('checked','checked');
        }

    } else {
        $(checkbox).removeAttr('checked');
        if (member_oldList.indexOf(memberId) > -1 && -1 == member_delList.indexOf(memberId)) {
            member_delList.push(memberId);
        } else if (member_addList.indexOf(memberId) > -1) {
            var index = member_addList.indexOf(memberId);
            member_addList.splice(index, 1);
        }

        $('.allCheck').removeAttr('checked');
    }
}

function setAllCheckboxs(){
    var status = $('.allCheck').prop('checked');
    var checkboxs = $('#member_select_table tbody input[type="checkbox"]');
    if(status){
        $.each(checkboxs,function(i,v){
            var memberId = $(this).attr('data');
            if(-1 == member_addList.indexOf(memberId) && member_oldList.indexOf(memberId) == -1){
                member_addList.push(memberId);
                $(this).attr('checked','checked');
            }else{
                $(this).attr('checked','checked');
            }
        });
        member_delList.splice(0,member_delList.length);
    }else{
        $.each(checkboxs,function(i,v){
            var memberId = $(this).attr('data');
            if(-1 == member_delList.indexOf(memberId)){
                member_delList.push(memberId);
                $(this).removeAttr('checked');
            }else{
                $(this).removeAttr('checked');
            }
        });
        member_addList.splice(0,member_addList.length);
    }
}

function showTip(message) {
    $("#member_select_tip").text(message);
    setTimeout(function () {
        $("#member_select_tip").text("");
    }, 2000);
}