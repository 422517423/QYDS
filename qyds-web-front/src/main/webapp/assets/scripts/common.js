/**
 * Created by panda on 16/6/25.
 */
function showConfirm(message, callback) {
    $('#confirmModal .modal-body p').text(message);
    $('#confirmModal').modal('show');
    $("#confirmBtn").unbind('click');
    $("#confirmBtn").one("click", function () {
        if (callback) {
            callback();
        }
    });
}

function showConfirmD(message, callback) {
    $('#confirmModalD .modal-body p').text(message);
    $('#confirmModalD').modal('show');
    $("#confirmBtnD").unbind('click');
    $("#confirmBtnD").one("click", function () {
        if (callback) {
            callback();
        }
    });
}

/**
 * ajax封装
 * url 发送请求的地址
 * data 发送到服务器的数据，数组存储，如：{"date": new Date().getTime(), "state": 1}
 * dataType 预期服务器返回的数据类型，常用的如：xml、html、json、text
 * successfn 成功回调函数
 * errorfn 失败回调函数
 */
function axse(url, data, successfn, errorfn) {
    data = (data == null || data == "" || typeof(data) == "undefined") ? {"date": new Date().getTime()} : data;
    App.blockUI($('body'), true);
    $.ajax({
        type: "post",
        data: data,
        url: '/qyds-web-front' + url,
        dataType: "json",
        success: function (d) {
            if (d.resultCode == "403" || d.resultCode == "401") {
                window.location.href = "login.html"
            }else{
                successfn(d);
            }
            App.unblockUI($('body'));
        },
        error: function (e) {
            errorfn(e);
            App.unblockUI($('body'));
        }
    });
};

/**
 * ajax封装
 * url 发送请求的地址
 * data 发送到服务器的数据，数组存储，如：{"date": new Date().getTime(), "state": 1}
 * dataType 预期服务器返回的数据类型，常用的如：xml、html、json、text
 * successfn 成功回调函数
 * errorfn 失败回调函数
 */
function axseForImage(url, data, successfn, errorfn) {
    data = (data == null || data == "" || typeof(data) == "undefined") ? {"date": new Date().getTime()} : data;
    App.blockUI($('body'), true);
    $.ajax({
        type: "post",
        data: data,
        url: uploadUri,
        dataType: "json",
        success: function (d) {
            if (d.resultCode == "403" || d.resultCode == "401") {
                window.location.href = "login.html"
            }else{
                successfn(d);
            }
            App.unblockUI($('body'));
        },
        error: function (e) {
            errorfn(e);
            App.unblockUI($('body'));
        }
    });
};
/**
 * $.post封装
 * @param url 请求参数
 * @param data 请求数据 ,为空填写 {}
 * @param callback 执行后的回调函数
 * @param success 执行完回调函数callback后执行的函数,为空填写null
 * @param fail 请求失败时执行的函数,没有填写null
 * @param always 最后执行的函数,没有填写null
 */
function ajaxPost(url, data, callback, success, fail, always) {
    //App.blockUI($('body'),true);
    var re = $.post('/qyds-web-front' + url, data, callback);

    if (isFunction(success)) {
        re = re.done(success);
        //App.unblockUI($('body'));
    }

    if (isFunction(fail)) {
        re = re.fail(fail);
        //App.unblockUI($('body'));
    }

    if (isFunction(always)) {
        re = re.always(always)
    }
}

function isFunction(fn) {
    return Object.prototype.toString.call(fn) === '[object Function]';
}

/**
 *对Date的扩展，将 Date 转化为指定格式的String
 *月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
 *年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
 *例子：
 *(new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
 *(new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
 */
Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

/**
 * 获取cookie信息
 * @type {{init}}
 */
var userInfoInit = function () {
    return {
        init: function () {
            if ($.cookie("the_user_info") == null || $.cookie("the_user_info") == 'null') {
                window.location.href = 'login.html';
            } else {
                var userJson = $.cookie("the_user_info");
                var userInfo = JSON.parse(userJson);
                return userInfo;

            }
        }
    }

}();

//列表访问数据库方法
function fnServerData(sSource, aoData, fnCallback, oSettings) {
    oSettings.jqXHR = $.ajax({
        "dataType": 'json',
        "type": "POST",
        "url": sSource,
        "data": aoData,
        "success": function(json){
            if(json.resultCode=="401"){
                window.location.href = "login.html";
            } else{
                fnCallback(json);
            }

        },
        "error": function (e) {
            console.log(e.message);
        }
    });
}

//获得码表数据方法
function getOptionCode(codeType, selectId,cb) {
    var url = "/common/getCodeList.json";
    var param = {"data": codeType};
    var success = function (data) {
        if (data.resultCode == '00') {
            var array = data.data;
            if (array != null) {
                //为品牌类型选择框添加数据
                $.each(array, function (index, item) {
                    var $option = $('<option>').attr('value', item.value).text(item.displayCn);
                    $("#" + selectId).append($option);
                });
                if(cb){
                 cb(true);
                }
            }
        } else {
            showAlert('码表数据获取失败');
            if(cb){
                cb(false);
            }
        }
    };
    var error = function () {
        showAlert('码表数据获取失败');
        if(cb){
            cb(false);
        }
    };
    //品牌类型码表中的数据code为GDS_BRAND_TYPE 每一个码表的code不一样
    axse(url, param, success, error);
}

//获得码表数据方法
function getSelectCode(codeType, selectId,codeString,cb) {
    var url = "/common/getCodeListByCodeString.json";
    var param = {"data": codeType,"value":codeString};
    var success = function (data) {
        if (data.resultCode == '00') {
            var array = data.data;
            if (array != null) {
                //为品牌类型选择框添加数据
                $.each(array, function (index, item) {
                    var $option = $('<option>').attr('value', item.value).text(item.displayCn);
                    $("#" + selectId).append($option);
                });
                if(cb){
                    cb(true);
                }
            }
        } else {
            showAlert('码表数据获取失败');
            if(cb){
                cb(false);
            }
        }
    };
    var error = function () {
        showAlert('码表数据获取失败');
        if(cb){
            cb(false);
        }
    };
    //品牌类型码表中的数据code为GDS_BRAND_TYPE 每一个码表的code不一样
    axse(url, param, success, error);
}

//model 设定
var modalSettings = {
    backdrop: 'static',
    keyboard: false
};


//图片上传的URI
var uploadUri ;
//图片显示的URI
var displayUri ;

//原图
var orignal = "/orignal";
//qrcode
var qrcode="/qrcode/";
//缩略图
var thumb120 = "/thumb120";
//缩略图
var thumb480 = "/thumb480";
//缩略图
var thumb800 = "/thumb800";
//缩略图
var thumb1200 = "/thumb1200";


//
////图片地址准备配置到前台
//var baseUri = "http://localhost:8080/qyds_file/"
////var baseUri = "http://120.26.230.73/qydsfile/";
//
///** 图片上传控件 保存的资源图片URL */
//var IMAGE_URL = "image/";
//
///** 图片访问目录 功能别 **/
////商品品牌
//var GDS_BRAND_TYPE_URL = "GDS_BRAND_TYPE/";
////商品分类
//var GDS_TYPE_URL = "GDS_TYPE/";
////商品SKU
//var GDS_SKU_URL = "GDS_SKU/";
////商品颜色
//var GDS_COLOR_URL = "GDS_COLOR/";
////商品主表
//var GDS_MASTER_URL = "GDS_MASTER/";
////CMS编辑
//var CMS_MASTER_URL = "CMS_MASTER/";

// (function() {
//     if (window.HashQuery) {
//         return;
//     }
//     window.HashQuery = function() {
//     };
//     HashQuery.prototype = {
//         parseFromLocation: function() {
//             if (location.hash === '' || location.hash.length === 0) {
//                 return;
//             }
//             var properties = location.hash.substr().split('|');
//             var index = 0;
//             for (var p in this) {
//                 if (!this.hasOwnProperty(p) || typeof this[p] != 'string') {
//                     continue;
//                 }
//                 if (index < properties.length) {
//                     this[p] = properties[index];
//                     if (this[p] === '-') {
//                         this[p] = '';
//                     }
//                 }
//                 index++;
//             }
//         },
//         updateLocation: function() {
//             var properties = [];
//             for (var p in this) {
//                 if (!this.hasOwnProperty(p) || typeof this[p] != 'string') {
//                     continue;
//                 }
//                 var value = this[p];
//                 properties.push(value === '' ? '-' : value);
//             }
//             var url = location.origin + location.pathname + location.search + "#" + properties.join('|');
//             location.replace(url);
//         }
//     };
// })();
//
// (function() {
//     window.TaskSearchHashQuery = function () {
//         HashQuery.constructor.call(this);
//         this.goods_name = '';
//         this.type = '';
//         this.is_onsell = '';
//         this.maintain_status = '';
//         this.goods_code = '';
//     };
//     TaskSearchHashQuery.constructor = TaskSearchHashQuery;
//     TaskSearchHashQuery.prototype = new HashQuery();
// })();


//获得ERP省数据方法
function getErpProvince(province,city,district) {
    $("#" + province).empty();
    $("#" + city).empty();
    $("#" + district).empty();
    var url = "/com_discrict/getErpProvince.json";
    var param = {};
    var success = function (data) {
        if (data.resultCode == '00') {
            var $option = $('<option>').attr('value', '').text('请选择');
            $("#" + province).append($option);
            var array = data.data;
            if (array != null) {
                $.each(array, function (index, item) {
                    $option = $('<option>').attr('value', item.pcode).text(item.pname);
                    $("#" + province).append($option);
                });
            }
        } else {
            showAlert('省数据获取失败');
        }
    };
    var error = function () {
        showAlert('省数据获取失败');
    };
    axse(url, param, success, error);
}

//获得ERP市数据方法
function getErpCity(province,city,district) {
    $("#" + city).empty();
    $("#" + district).empty();
    //var code = $("#" + province).value;
    var p=document.getElementById(province);
    var code = p.options[p.selectedIndex].value;
    if(!code) return;
    var url = "/com_discrict/getErpCity.json";
    var param = {"pCode": code};
    var success = function (data) {
        if (data.resultCode == '00') {
            var $option = $('<option>').attr('value', '').text('请选择');
            $("#" + city).append($option);
            var array = data.data;
            if (array != null) {
                $.each(array, function (index, item) {
                    $option = $('<option>').attr('value', item.ccode).text(item.cname);
                    $("#" + city).append($option);
                });
            }
        } else {
            showAlert('市数据获取失败');
        }
    };
    var error = function () {
        showAlert('市数据获取失败');
    };
    axse(url, param, success, error);
}

//获得ERP区数据方法
function getErpDistrict(city,district) {
    $("#" + district).empty();
    var c=document.getElementById(city);
    var code = c.options[c.selectedIndex].value;
    if(!code) return;
    var url = "/com_discrict/getErpDistrict.json";
    var param = {"cCode": code};
    var success = function (data) {
        if (data.resultCode == '00') {
            var $option = $('<option>').attr('value', '').text('请选择');
            $("#" + district).append($option);
            var array = data.data;
            if (array != null) {
                $.each(array, function (index, item) {
                    $option = $('<option>').attr('value', item.dcode).text(item.dname);
                    $("#" + district).append($option);
                });
            }
        } else {
            showAlert('区数据获取失败');
        }
    };
    var error = function () {
        showAlert('区数据获取失败');
    };
    axse(url, param, success, error);
}


 function getUUID (len, radix) {
    var CHARS = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
    var chars = CHARS, uuid = [], i;
    radix = radix || chars.length;

    if (len) {
        // Compact form
        for (i = 0; i < len; i++) uuid[i] = chars[0 | Math.random()*radix];
    } else {
        // rfc4122, version 4 form
        var r;

        // rfc4122 requires these characters
        uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
        uuid[14] = '4';

        // Fill in random data.  At i==19 set the high bits of clock sequence as
        // per rfc4122, sec. 4.1.5
        for (i = 0; i < 36; i++) {
            if (!uuid[i]) {
                r = 0 | Math.random()*16;
                uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];
            }
        }
    }

    return uuid.join('');
};