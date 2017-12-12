/**
 * Created by dkzhang on 16/8/6.
 */

var memberId = sessionStorage.getItem("memberId");

$(document).ready(function () {

    var professionList = ["零售业", "餐饮业", "娱乐业", "文体业", "法律", "医疗", "IT行业", "军人", "设计", "金融",
        "服务业", "制造业", "房地产", "公共事业", "教育", "学生", "自由职业", "其他"];
    var incomeList = ["3K以下", "3K-5K", "5K-8K", "8K以上"];

    $.each(professionList, function (index, item) {
        var $option = $('<option>').attr('value', item).text(item);
        $("#profession_edit").append($option);
    });

    $.each(incomeList, function (index, item) {
        var $option = $('<option>').attr('value', item).text(item);
        $("#income_edit").append($option);
    });

    $("#mmbMasterEditArea").modal('show');

    getAddressList(null, null);
    if (memberId && memberId.length > 0) {
        getSexSelectCode();
    } else {
        getOptionCode("SEX", "sex_edit");
        getOptionCode("MEMBER_TYPE", "type_edit");
    }


    //表单验证的定义
    $("#userEditForm").validate({
        errorElement: 'span',
        errorClass: 'help-block',
        focusInvalid: false,
        rules: {
            member_name_edit: {
                required: true
            },
            telephone_edit: {
                required: true
            },
            birthdate_edit: {
                required: true

            }

        },
        messages: {
            member_name_edit: {
                required: "请输入姓名"
            },
            telephone_edit: {
                required: "请输入手机号"
            },
            birthdate_edit: {
                required: "请输入生日"
            }
        },

        highlight: function (element) {
            $(element)
                .closest('.form-group').addClass('has-error');
        },

        unhighlight: function (element) {
            $(element)
                .closest('.form-group').removeClass('has-error');
        },

        success: function (label) {
            label
                .closest('.form-group').removeClass('has-error');
        }
    });

    $("#btn_save").click(function () {
        if ($("#userEditForm").valid()) {
            save();
        }
    });
});

function getDetail() {
    var url = "/mmb_master/detail.json";
    var param = {
        memberId: memberId
    };
    var successCallback = function (data) {

        console.log(data);

        if (data.resultCode == '00') {

            $("#member_name_edit").val(data.data.memberName);

            $("#telephone_edit").val(data.data.telephone);
            $("#sex_edit").val(data.data.sex);
            $("#type_edit").val(data.data.type);

            if (data.data.birthdate) {
                $("#birthdate_edit").val(new Date(data.data.birthdate).Format("yyyy-MM-dd"));
            }

            $("#email_edit").val(data.data.email);

            $("#profession_edit").val(data.data.profession);
            $("#income_edit").val(data.data.income);
            $("#postCode_edit").val(data.data.postCode);
            $("#address_edit").val(data.data.address);

            $("#province_edit").val(data.data.provinceCode);

            if (data.data.provinceCode && data.data.provinceCode.length > 0) {
                getDefaultAddressList('0', data.data.provinceCode, data.data.cityCode, data.data.districtCode);
            }

        } else {
            $("#mmbMasterEditArea").modal('hide');
            showAlert('取得失败,原因:' + data.resultMessage);
        }
    };
    var errorCallback = function () {
        $("#mmbMasterEditArea").modal('hide');
        showAlert('网络异常.');
    };
    axse(url, {"data": JSON.stringify(param)}, successCallback, successCallback);

}

function selectProvince() {

    var objS = document.getElementById("province_edit");

    var pcode = objS.options[objS.selectedIndex].value;

    $("#city_edit").empty();
    $("#district_edit").empty();

    if (pcode) {
        getAddressList('0', pcode);
    }
}

function selectCity() {

    var objS = document.getElementById("city_edit");

    var ccode = objS.options[objS.selectedIndex].value;

    $("#district_edit").empty();

    if (ccode) {
        getAddressList('1', ccode);
    }
}

function save() {
    var url = "";
    if (memberId && memberId.length > 0) {
        url = "/mmb_master/edit.json";
    } else {
        url = "/mmb_master/save.json";
    }

    var param = {
        memberId: memberId,
        memberName: $("#member_name_edit").val(),
        telephone: $("#telephone_edit").val(),
        sex: $("#sex_edit").val(),
        type: $("#type_edit").val(),
        birthdate: $("#birthdate_edit").val(),
        email: $("#email_edit").val(),

        profession: $("#profession_edit").val(),
        income: $("#income_edit").val(),
        postCode: $("#postCode_edit").val(),
        address: $("#address_edit").val(),

        provinceCode: $("#province_edit").val() == '-1' ? '' : $("#province_edit").val(),
        cityCode: $("#city_edit").val() == '-1' ? '' : $("#city_edit").val(),
        districtCode: $("#district_edit").val() == '-1' ? '' : $("#district_edit").val()

    };

    var success = function (data) {
        if (data.resultCode == '00') {
            $("#mmbMasterEditArea").modal('hide');
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
}


function showTip(message) {
    $("#mmb_master_edit_tip").text(message);
    setTimeout(function () {
        $("#mmb_master_edit_tip").text("");
    }, 2000);
}

//获得码表数据方法
function getSexSelectCode() {
    var url = "/common/getCodeList.json";
    var param = {"data": 'SEX'};
    var success = function (data) {
        if (data.resultCode == '00') {
            var array = data.data;
            if (array != null) {
                //为品牌类型选择框添加数据
                $.each(array, function (index, item) {
                    var $option = $('<option>').attr('value', item.value).text(item.displayCn);
                    $("#sex_edit").append($option);
                });
            }

            getTypeSelectCode();
        } else {
            showAlert('码表数据获取失败');
        }
    };
    var error = function () {
        showAlert('码表数据获取失败');
    };
    //品牌类型码表中的数据code为GDS_BRAND_TYPE 每一个码表的code不一样
    axse(url, param, success, error);
}

function getTypeSelectCode() {
    var url = "/common/getCodeList.json";
    var param = {"data": 'MEMBER_TYPE'};
    var success = function (data) {
        if (data.resultCode == '00') {
            var array = data.data;
            if (array != null) {
                //为品牌类型选择框添加数据
                $.each(array, function (index, item) {
                    var $option = $('<option>').attr('value', item.value).text(item.displayCn);
                    $("#type_edit").append($option);
                });
            }

            getDetail();
        } else {
            showAlert('码表数据获取失败');
        }
    };
    var error = function () {
        showAlert('码表数据获取失败');
    };
    //品牌类型码表中的数据code为GDS_BRAND_TYPE 每一个码表的code不一样
    axse(url, param, success, error);
}


//获得码表数据方法
function getAddressList(type, code) {

    var url = "/mmb_master/getAddressList.json";
    var param = {
        "provinceCode": type == '0' ? code : null,
        "cityCode": type == '1' ? code : null
    };
    var success = function (data) {
        if (data.resultCode == '00') {
            var array = data.data;
            console.log(array);
            if (array != null) {

                if (type == '0') {
                    $("#city_edit").append($('<option>').attr('value', '-1').text('请选择'));
                    $.each(array, function (index, item) {
                        var $option = $('<option>').attr('value', item.ccode).text(item.cname);
                        $("#city_edit").append($option);
                    });
                } else if (type == '1') {
                    $("#district_edit").append($('<option>').attr('value', '-1').text('请选择'));
                    $.each(array, function (index, item) {
                        var $option = $('<option>').attr('value', item.dcode).text(item.dname);
                        $("#district_edit").append($option);
                    });
                } else {
                    $("#province_edit").append($('<option>').attr('value', '-1').text('请选择'));
                    $.each(array, function (index, item) {
                        var $option = $('<option>').attr('value', item.pcode).text(item.pname);
                        $("#province_edit").append($option);
                    });
                }
            }
        } else {
            showAlert('码表数据获取失败');
        }
    };
    var error = function () {
        showAlert('码表数据获取失败');
    };
    //品牌类型码表中的数据code为GDS_BRAND_TYPE 每一个码表的code不一样
    axse(url, param, success, error);
}

function getDefaultAddressList(type, code, city, district) {

    var url = "/mmb_master/getAddressList.json";
    var param = {
        "provinceCode": type == '0' ? code : null,
        "cityCode": type == '1' ? code : null
    };
    var success = function (data) {
        if (data.resultCode == '00') {
            var array = data.data;
            console.log(array);
            if (array != null) {

                if (type == '0') {
                    $("#city_edit").append($('<option>').attr('value', '-1').text('请选择'));
                    $.each(array, function (index, item) {
                        var $option = $('<option>').attr('value', item.ccode).text(item.cname);
                        $("#city_edit").append($option);
                    });
                    $("#city_edit").val(city);

                    if(city && city.length > 0){
                        getDefaultAddressList('1', city, null, district)
                    }

                } else if (type == '1') {
                    $("#district_edit").append($('<option>').attr('value', '-1').text('请选择'));
                    $.each(array, function (index, item) {
                        var $option = $('<option>').attr('value', item.dcode).text(item.dname);
                        $("#district_edit").append($option);
                    });
                    $("#district_edit").val(district);
                }
            }
        } else {
            showAlert('码表数据获取失败');
        }
    };
    var error = function () {
        showAlert('码表数据获取失败');
    };
    //品牌类型码表中的数据code为GDS_BRAND_TYPE 每一个码表的code不一样
    axse(url, param, success, error);
}
