// 获取各种高度 start
function getClientHeight(){
    var clientHeight=0;
    if(document.body.clientHeight&&document.documentElement.clientHeight){
        var clientHeight=(document.body.clientHeight<document.documentElement.clientHeight)?document.body.clientHeight:document.documentElement.clientHeight;
    }else{
        var clientHeight=(document.body.clientHeight>document.documentElement.clientHeight)?document.body.clientHeight:document.documentElement.clientHeight;
    }
    return clientHeight;
}
//取窗口滚动条高度
function getScrollTop(){
    var scrollTop=0;
    if(document.documentElement&&document.documentElement.scrollTop){
        scrollTop=document.documentElement.scrollTop;
    }else if(document.body){
        scrollTop=document.body.scrollTop;
    }
    return scrollTop;
}
//取文档内容实际高度
function getScrollHeight(){
    return Math.max(document.body.scrollHeight,document.documentElement.scrollHeight);
}
// 获取各种高度 end

// countdown plugin
var dealunaTimer = {

    MAX_COUNT:60,
    countMap:new HashMap(),
    interValMap:new HashMap(),

    init:function(btnId){
        var $curDom = $('#'+ btnId);
        $curDom.text("");
        var that = this;
        var retainCount = this.countMap.get(btnId);
        var $curDom = $('#'+ btnId);

        if(!isNaN(retainCount)){
            if(parseInt(retainCount) > 0){
                // 继续
                $curDom.text(that.countMap.get(btnId) + "秒");
                $curDom.attr("disabled", "true");

                window.clearInterval(this.interValMap.get(btnId));//停止计时器
                // 重设计时器
                var interValObj = window.setInterval(function(){
                    that.setRemainTime(btnId);
                }, 1000);
                that.interValMap.put(btnId,interValObj);

            }else{
                $curDom.text("获取验证码");
                $curDom.removeAttr("disabled");

            }
        }else{
            $curDom.text("获取验证码");
            $curDom.removeAttr("disabled");
        }
    },
    countDown : function(btnId) {

        var that = this;
        this.countMap.put(btnId,that.MAX_COUNT);

        var $curDom = $('#'+ btnId);
        $curDom.attr("disabled", "true");
        $curDom.text(that.countMap.get(btnId) + "秒");

        var interValObj = window.setInterval(function(){
            that.setRemainTime(btnId);
        }, 1000);
        that.interValMap.put(btnId,interValObj);
    },
    setRemainTime : function(btnId){
        if (parseInt(this.countMap.get(btnId)) == 0) {
            window.clearInterval(this.interValMap.get(btnId));//停止计时器
            var $curDom = $('#'+ btnId);
            if($curDom.length > 0){
                $curDom.text("获取验证码");
                $curDom.removeAttr("disabled");//启用按钮
            }
        }
        else {
            var curCount = parseInt(this.countMap.get(btnId));
            curCount--;
            this.countMap.put(btnId,curCount);
            var $curDom = $('#'+ btnId);
            if($curDom.length > 0){
                $curDom.text(curCount + "秒");
            }
        }
    }
};

// Accordion plugin
var Accordion = function(el, multiple) {
    this.el = el || {};
    this.multiple = multiple || false;

    // Variables privadas
    var links = this.el.find('.link');
    // Evento
    links.on('click', {el: this.el, multiple: this.multiple}, this.dropdown)

    $('.submenu li').click(function(){
        $('.submenu li').removeClass('active');
        $(this).addClass('active');
    })
}

Accordion.prototype.dropdown = function(e) {
    $('.submenu li').removeClass('active');
    var $el = e.data.el;
    $this = $(this),
        $next = $this.next();

    $next.slideToggle();
    $this.parent().toggleClass('open');

    if (!e.data.multiple) {
        $el.find('.submenu').not($next).slideUp().parent().removeClass('open');
    };
}

/**
 * Created by sunt on 16/9/12.
 */
function switchMenu(curMenu){
    $('.menu--shylock .menu__link').removeClass('active');
    $('#'+curMenu).addClass('active');

}

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
    // App.blockUI($('body'), true);
    $.ajax({
        type: "post",
        data: data,
        url: '/qyds-web-pc' + url,
        dataType: "json",
        success: function (d) {
            if (d.resultCode == "403" || d.resultCode == "401") {
                // window.location.href = "login.html"
            }else{
                successfn(d);
            }
            // App.unblockUI($('body'));
        },
        error: function (e) {
            errorfn(e);
            // App.unblockUI($('body'));
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
    // App.blockUI($('body'), true);
    $.ajax({
        type: "post",
        data: data,
        url: uploadUri,
        dataType: "json",
        success: function (d) {
            if (d.resultCode == "403" || d.resultCode == "401") {
                // window.location.href = "login.html"
            }else{
                successfn(d);
            }
            // App.unblockUI($('body'));
        },
        error: function (e) {
            errorfn(e);
            // App.unblockUI($('body'));
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
    var re = $.post('/qyds-web-pc' + url, data, callback);

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
        "success": fnCallback,
        "error": function (e) {
            console.log(e.message);
        }
    });
}

//获得码表数据方法
function getOptionCode(codeType, selectId) {
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