'use strict'

angular.module('dealuna', [
    // 'ngRoute',
    'ui.router',
    'ui.bootstrap',
    'dealuna.controllers',
    'dealuna.routes',
    'dealuna.filters',
    'dealuna.directives',
    'dealuna.services'])

.config(['$httpProvider', function($httpProvider) {
    $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8';
    $httpProvider.interceptors.push('LoadingIntercepter');
}])



.run(function( $http, $rootScope,$state,
              displayUriService,getBnkLimitService, uploadUriService) {

    var requestCount = 0;
    /** loading 拦截器 **/
    $rootScope.$on('LOADING:SHOW', function () {
        requestCount++;
        if (!$rootScope.isLoadShowing) {
            $('body').jqLoading();
            $("body").css("overflow", "hidden");
            //var params = {
            //    template: '<div style="float: left"><img src="image/icon_loading.gif" style="width: 40px"/></div><div style="margin-top: 10px;float: left">正在加载中……</div>'
            //};
            //$ionicLoading.show(params)
        }
        $rootScope.isLoadShowing = true;

    });

    $rootScope.$on('LOADING:HIDE', function () {
        requestCount--;
        if (requestCount <= 0) {
            $rootScope.isLoadShowing = false;
            //$ionicLoading.hide()
            $('body').jqLoading("destroy");
            $("body").css("overflow", "auto");

        }
    });
    
    // 服务器端取得图片地址前缀
    new displayUriService({}).then(function(res){
        DISPLAY_URI = res.data;
    },function(err){
        //TODO
    });
    //=========init end==========


    // 服务器端取得图片地址前缀
    new uploadUriService({}).then(function(res){
        UPLOAD_URI = res.data;
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
});
