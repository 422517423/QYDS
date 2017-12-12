// Ionic Starter App

// angular.module is a global place for creating, registering and retrieving Angular modules
// 'starter' is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of 'requires'
angular.module('dealnua', ['ionic', 'dealnua.controllers', 'dealnua.filter','dealnua.routes','dealnua.directive',
    'dealnua.services','ionic-datepicker'
])

.config(['$ionicConfigProvider','$httpProvider','$compileProvider', function($ionicConfigProvider,$httpProvider,$compileProvider) {
    $ionicConfigProvider.tabs.position('bottom'); // other values: top
    $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8';
    $httpProvider.interceptors.push('LoadingIntercepter');
    $compileProvider.imgSrcSanitizationWhitelist('^(http|https|weixin|wxlocalresource)');
}])
.config(function ($ionicConfigProvider, ionicDatePickerProvider) {

    var datePickerObj = {
        setLabel: '确定',
        todayLabel: '今天',
        closeLabel: '关闭',
        mondayFirst: false,
        inputDate: new Date(),
        weeksList: ["日", "一", "二", "三", "四", "五", "六"],
        monthsList: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
        templateType: 'popup',
        showTodayButton: true,
        dateFormat: 'yyyy/MM/dd',
        closeOnSelect: false,
        //disableWeekdays: [6],
        from: new Date(2015, 8, 1)
    };

    ionicDatePickerProvider.configDatePicker(datePickerObj);

    $ionicConfigProvider.tabs.position('bottom');

})

.run(function($ionicPlatform, $http, $rootScope,$ionicHistory,$state,
              messageService,displayUriService,getBnkLimitService,authService,
              dateService,localStorageService,WeiXinService,popupService,$ionicLoading) {

    //===common method start====
    $rootScope.goBack = function(){
      $ionicHistory.goBack();
    };
    //===common method end======

    //===loading 监听器 start====
    var requestCount = 0;
    /** loading 拦截器 **/
    $rootScope.$on('LOADING:SHOW', function () {
        requestCount++;
        if (!$rootScope.isLoadShowing) {
            var params = {
                template: '<div style="float: left"><img src="image/icon_loading.gif" style="width: 40px"/></div><div style="margin-top: 10px;float: left">正在加载中……</div>'
            };
            $ionicLoading.show(params)
        }
        $rootScope.isLoadShowing = true;
    });

    $rootScope.$on('LOADING:HIDE', function () {
        requestCount--;
        if (requestCount <= 0) {
            $rootScope.isLoadShowing = false;
            $ionicLoading.hide()
        }
    });
    //===loading 监听器 end====

    $rootScope.$on('$stateChangeStart', function(){
        $rootScope.$broadcast('LOADING:HIDE');
    });

    //=======init start==========
    // test data start
    $rootScope.autoLogin = function(openId){
        if(!openId || openId.length==0){
            popupService.showAlert("提示","请从微信菜单中进入本页面.");
            return;
        }
        // 自动登录
        new authService({"openId":openId}).login().then(function(res){
            localStorageService.clear(KEY_USERINFO);
            var rsCode = res.resultCode;
            if(rsCode == '00'){// 登录成功
                console.log('登录用户信息');
                console.log(res);

                localStorageService.set(KEY_USERINFO, res.data);
            }else if(rsCode == '95'){// 没有对应的会员(需要绑定或者注册)
                popupService.showConfirm("提示","您的微信号还没有绑定平台账号,是否去绑定?",function(){
                    $state.go('binding');
                },"去绑定");
            }else{
                popupService.showConfirm("提示","您的微信号还没有绑定平台账号,是否去绑定?",function(){
                    $state.go('binding');
                },"去绑定");
            }
        },function(err){
            localStorageService.clear(KEY_USERINFO);
        });
    }

    // test data  end

    // 服务器端取得图片地址前缀
    new displayUriService({}).then(function(res){
        DISPLAY_URI = res.data;
    },function(err){
        //TODO
    });

    // 库存限制接口
    new getBnkLimitService({}).then(function(res){
        for(var i = 0; i<res.data.length; i++){
            if(res.data[i].value == '20'){
                BnkLimit20 = res.data[i].displayCn;
            }else if(res.data[i].value == '50'){
                BnkLimit50 = res.data[i].displayCn;
            }
        }
    },function(err){
        //TODO
    });

    //=========init end==========

    //$rootScope.userInfo = localStorageService.get("USER_INFO");

    //$rootScope.checkLogin = function(){
    //    if(!userInfo){
    //       $state.go("login", {});
    //       return;
    //    }
    //};

    // 设置title start
    $rootScope.setTitle = function(title){
        var $body = $jq('body');
        document.title = title;
        var $iframe = $jq("<iframe style='display:none;' src='/favicon.ico'></iframe>");
        $iframe.on('load',function() {
            setTimeout(function() {
                $iframe.off('load').remove();
            }, 0);
        }).appendTo($body);
    };
    // 设置title end

    // 获取微信信息start
    $rootScope.getParamFromUrl = function (param) {
        var reg = new RegExp("(^|&)" + param + "=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if (r != null)return decodeURI(r[2]);
        return null;
    };

    $rootScope.weixinInit = function(){
        // 从微信跳转来的
        var code = $rootScope.getParamFromUrl("code");
        if (code && code.length > 0) {
            WeiXinService.getOpenid({code: code})
                .then(function (response) {
                    if (response.resultCode == "00") {
                        $rootScope.openid = response.results;
                        $rootScope.autoLogin($rootScope.openid);
                        WeiXinService.initWeixinJS();
                    } else {
                        popupService.showToast("微信接口初始化失败,服务异常");
                    }
                });
            wx.error(function (res) {
                popupService.showToast("微信接口初始化失败:" + res.errMsg);
            });

        }else{
            popupService.showAlert("提示","请从微信菜单中进入本页面.");
            // $rootScope.openid = "oquY8t8wKbfdlMo-OcrXSezyZCF8";
            $rootScope.autoLogin($rootScope.openid);
        }
    };
    $rootScope.weixinInit();
    wx.ready(function () {
        wx.hideAllNonBaseMenuItem();
    });
    // 获取微信信息end
});
