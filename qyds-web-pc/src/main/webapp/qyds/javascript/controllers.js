//var commonMessage = new Object();
//commonMessage.networkErrorMsg = "网络异常";
// 提交订单页面中判断跳转来源 0:立即购买 1:购物车结算
var resultPath;
angular.module('dealuna.controllers', [])
    // header
    .controller('headerCtrl', ["$scope", "$state", "headerService", "$modal", "localStorageService","indexGdsBrandTypeService",'popupService','$rootScope','favoriteService','alertService',
        function ($scope, $state, headerService, $modal, localStorageService,indexGdsBrandTypeService,popupService,$rootScope,favoriteService,alertService) { //header

            //meta信息
            new headerService({}).getMetaData()
                .then(function (res) {
                    if (res.resultCode == "00") {
                        var metaDate = res.data;
                        $('meta[name="description"]').attr('content',metaDate.seo_description);
                        $('meta[name="Keywords"]').attr('content',metaDate.seo_keywords);
                    }
            });

            //快递信息s
            new headerService({}).getDeliverData()
                .then(function (res) {
                    if (res.resultCode == "00") {
                        var metaDate = res.data;
                        $scope.deliverInfo = metaDate;
                    }
                });

            $scope.contactUs = function(){
                //popupService.showAlert('aaa');可以增加关闭时候的回调方法,如popupService.showAlert('aaa',cb)
                //popupService.showConfirm('aaa',function(){alert('111')},'成功');可以增加取消时候的回调方法,如popupService.showConfirm('aaa',function(){alert('111')},'成功',function(){'222'});
                //popupService.showToast('aaa');可以自定义toast时长(默认为3秒),如:popupService.showToast('aaa',5000);
                $state.go('contactUs');
            }

            $scope.msTest = function () {
                $state.go('msTest');
            }

            $scope.initFlg = true;

            angular.element(document).ready(function() {
                var curMenuId = localStorageService.get(KEY_MENU);
                setTimeout(function(){
                    switchMenu(curMenuId);
                },1000);
            });

            var userInfo = localStorageService.get(KEY_USERINFO);
            if (userInfo) {
                $scope.hasLogon = true;
            } else {
                $scope.hasLogon = false;
                $scope.userName = "";
            }

            $scope.searchInfo = {
                value: ""
            };
            $rootScope.$on('$stateChangeStart', function(){
                setTimeout(function () {
                    if($state.current.name!="searchGoods"){
                        $rootScope.searchInfo.value = "";
                    }
                },1000);
            });
            // 头部信息取得
            new headerService({}).getHeaderData()
                .then(function (res) {
                    if (res.resultCode == "00") {
                        $scope.headerData = res.results;
                        var percent = 99 / (3 + $scope.headerData.length) + '%';
                        $scope.headerStyle = {width:percent};
                        new indexGdsBrandTypeService({})
                            .then(function (resp) {
                                if (resp.resultCode == "00") {
                                    $scope.indexGdsBrandTypeData = resp.results;
                                } else {
                                    //popupService.showToast(resp.resultMessage);
                                }
                            }, function (error) {
                                //popupService.showToast(commonMessage.networkErrorMsg);
                            });
                        // $ionicSlideBoxDelegate.update();
                    } else {
                        // popupService.showToast(res.resultMessage);
                    }
                }, function () {
                    // popupService.showToast(commonMessage.networkErrorMsg);
                    //alert();
            });

            // 首页
            $scope.goHomepage = function () {
                localStorageService.set(KEY_MENU,'nav_home');
                switchMenu('nav_home');
                $state.go("homepage");
                $scope.navbarCollapsed  = false;
            };

            $scope.goStoreList = function () {
                $state.go('storeList');
                $scope.navbarCollapsed  = false;
            }
            // 关于我们
            $scope.goAbout = function () {
                localStorageService.set(KEY_MENU,'nav_about');
                switchMenu('nav_about');
                $state.go("about",{"pageParam":JSON.stringify({'aaa':'bbb','ccc':'ddd'})});
                $scope.navbarCollapsed  = false;

            };
            // 检索商品
            $scope.goProductList = function () {
                $state.go("searchGoods", {'searchKey': $scope.searchInfo.value});
            };

            // 退出登录
            $scope.logout = function () {

                popupService.showConfirm('确定要离开了么?', function (){
                    localStorageService.clear(KEY_USERINFO);
                    $scope.hasLogon = false;
                    $scope.userName = "";

                    $scope.goHomepage();
                });
            };

            // 登录注册
            $scope.goLogin = function (nextPage) {
                $scope.navbarCollapsed  = false;
                var loginModalInstance = $modal.open({
                    templateUrl: 'html/login.html',
                    controller: 'loginCtrl',
                    backdrop: true,
                    resolve: {
                        //items: function () {
                        //    return $scope.items;
                        //}
                    }
                });
                loginModalInstance.opened.then(function () {//模态窗口打开之后执行的函数
                    $scope.type = 'login';
                });
                loginModalInstance.result.then(
                    function (result) {

                        var userInfo = localStorageService.get(KEY_USERINFO);
                        if (userInfo) {
                            $scope.hasLogon = true;

                            $scope.getInventoryAlarming();

                            if(nextPage){
                                $state.go(nextPage);
                            }
                        } else {
                            $scope.hasLogon = false;
                            $scope.userName = "";
                        }


                    }, function (reason) {
                        console.log(reason);//点击空白区域，总会输出backdrop click，点击取消，则会暑促cancel
                    });
            };

            // 心愿单
            $scope.goFavorite = function () {
                var userInfo = localStorageService.get(KEY_USERINFO);
                if (userInfo) {
                    $state.go("personalCenter.favorite");
                } else {
                    $scope.goLogin("personalCenter.favorite");
                }
                $scope.navbarCollapsed  = false;
            };
            // 购物袋
            $scope.goBag = function () {
                var userInfo = localStorageService.get(KEY_USERINFO);
                if (userInfo) {
                    $state.go("personalCenter.bag");
                } else {
                    $scope.goLogin("personalCenter.bag");
                }
                $scope.navbarCollapsed  = false;
            };
            // 个人中心
            // personalHome
            $scope.goPersonalHome = function () {
                var userInfo = localStorageService.get(KEY_USERINFO);
                if (userInfo) {
                    $state.go("personalCenter.personal");
                } else {
                    $scope.goLogin("personalCenter.personal");
                }
                $scope.navbarCollapsed  = false;
            };

            // 新品
            $scope.goGoodsList = function () {
                localStorageService.set(KEY_MENU,'nav_new');
                switchMenu('nav_new');
                $state.go("goodsList",{"actId":'',"firstGoodsTypeId": '', "goodsType": ''});
                $scope.navbarCollapsed  = false;
            };

            // 商品详细
            $scope.goOtherPage = function (type, typeId,index,cmsId) {
                $scope.navbarCollapsed  = false;
                console.log('inininin');

                if(type == 41){// add by sunt
                    localStorageService.set(KEY_MENU,'nav_sort');
                    switchMenu('nav_sort');
                }else{
                    localStorageService.set(KEY_MENU,'nav_group'+index);
                    switchMenu('nav_group'+index);
                }

                // 点击商品的时候
                if (type == 41) {
                    $state.go("goodsDetail", {"goodsId": typeId});
                    // var array = typeId.split(",");
                    // if(array != null && array.length > 1){
                    //     $state.go("goodsListDisplay", {"type" : typeId});
                    // }else {
                    //     $state.go("goodsDetail", {"goodsId" : typeId});
                    // }

                    // 商品分类的时候
                } else if (type == 42) {
                    $state.go("goodsList", {"actId":'',"firstGoodsTypeId": typeId, "type": type,"cmsId":'',"pageType":'',"classifyId":'',"classifyName":''});

                    // 活动的时候
                } else if (type == 43) {
                    $state.go("goodsList", {"actId": typeId,"firstGoodsTypeId":'', "type": type,"cmsId":'',"pageType":'',"classifyId":'',"classifyName":''});
                }
            };

            $scope.goMagazine = function() {
                $state.go("magazine");
                $scope.navbarCollapsed  = false;
            }
            //// 登录
            //$scope.goLogin = function(){
            //    //$state.go("login");
            //};

            $scope.getInventoryAlarming = function() {
                var userInfo = localStorageService.get(KEY_USERINFO);
                if(!userInfo){
                    return;
                }
                var param = {
                    memberId : userInfo.memberId
                };
                new favoriteService(param).getInventoryAlarming()
                    .then(function (res) {
                        if(res.resultCode == "90"){
                            if($state.current.name!="personalCenter.favorite"){
                                $rootScope.favoriteClassTag = true;
                                //$rootScope.favoriteStyle = {"color":"red","text-decoration":"blink"};
                            }
                            alertService.add('danger', '心愿单中商品库存不足,快去看看吧！',function () {
                                $state.go("personalCenter.favorite");
                            });
                        }
                    }, function () {
                    });
            };

            $scope.getInventoryAlarming();

        }])
    // 首页
    .controller('homepageCtrl', ["$scope", "$state", "localStorageService", "indexFigureService", "indexNewsService","indexNews1Service",
        "indexNewsTwoService","indexRegion2Service","indexRegion2_1Service","indexRegion3Service","indexRegion3PhoneService","indexRegion4Service","indexRegion5Service","indexRegion6Service","indexRegion7Service","indexRegion8Service",
        "indexNewGoodsService", "headerService", "$rootScope", 'popupService','couponAllService','$modal','WeiXinService',
        function ($scope, $state, localStorageService, indexFigureService, indexNewsService,indexNews1Service,
                  indexNewsTwoService,indexRegion2Service,indexRegion2_1Service,indexRegion3Service,indexRegion3PhoneService,indexRegion4Service,indexRegion5Service,indexRegion6Service,indexRegion7Service,indexRegion8Service, indexNewGoodsService, headerService, $rootScope, popupService,couponAllService,$modal,WeiXinService) {
            var userInfo = localStorageService.get(KEY_USERINFO);
            //清空记录 
            $rootScope.goodsTypeId = '';
            $rootScope.actId = '';
            $rootScope.firstGoodsTypeId = '';
            $rootScope.searchKey = '';
            $rootScope.goodsId = '';

            // 轮播图
            var json = {};
            json.itemCode = 'index_figure';
            json.isChild = '0';
            new indexFigureService({"data": JSON.stringify(json)})
                .then(function (res) {
                    if (res.resultCode == "00") {
                        $scope.interval = 5000;
                        $scope.slides = res.results;
                        console.log($scope.slides);
                        $('.glyphicon-chevron-left').hide();
                        $('.glyphicon-chevron-right').hide();
                        //$scope.indexFigureData = res.results;
                        //$("#slider").responsiveSlides({
                        //    auto: true,
                        //    nav: true,
                        //    speed: 500,
                        //    namespace: "callbacks",
                        //    pager: true
                        //});

                        // $ionicSlideBoxDelegate.update();
                    } else {
                        // popupService.showToast(res.resultMessage);
                    }
                }, function () {
                    // popupService.showToast(commonMessage.networkErrorMsg);
                });

            // 新闻部分模版1
            new indexNewsService({})
                .then(function (res) {
                    if (res.resultCode == "00") {
                        $scope.indexNewsData = res.results;
                    } else {
                        // popupService.showToast(res.resultMessage);
                    }
                }, function (error) {
                    // popupService.showToast(commonMessage.networkErrorMsg);
                });

            // 新闻部分模版2
            new indexNewsTwoService({})
                .then(function (res) {
                    if (res.resultCode == "00") {
                        $scope.indexNewsTwoData = res.results;
                    } else {
                        // popupService.showToast(res.resultMessage);
                    }
                }, function (error) {
                    // popupService.showToast(commonMessage.networkErrorMsg);
                });


            // 模版1
            new indexNews1Service({})
                .then(function (res) {
                    if (res.resultCode == "00") {
                        $scope.indexNewsData1 = res.results;
                    } else {
                        // popupService.showToast(res.resultMessage);
                    }
                }, function (error) {
                    // popupService.showToast(commonMessage.networkErrorMsg);
                });

            //// 模版2
            //new indexNews2Service({})
            //    .then(function (res) {
            //        if (res.resultCode == "00") {
            //            $scope.indexNewsData2 = res.results;
            //        } else {
            //            // popupService.showToast(res.resultMessage);
            //        }
            //    }, function (error) {
            //        // popupService.showToast(commonMessage.networkErrorMsg);
            //    });
            //
            //
            //// 模版3
            //new indexNews3Service({})
            //    .then(function (res) {
            //        if (res.resultCode == "00") {
            //            $scope.indexNewsData3 = res.results;
            //        } else {
            //            // popupService.showToast(res.resultMessage);
            //        }
            //    }, function (error) {
            //        // popupService.showToast(commonMessage.networkErrorMsg);
            //    });
            //
            //// 模版4
            //new indexNews4Service({})
            //    .then(function (res) {
            //        if (res.resultCode == "00") {
            //            $scope.indexNewsData4 = res.results;
            //        } else {
            //            // popupService.showToast(res.resultMessage);
            //        }
            //    }, function (error) {
            //        // popupService.showToast(commonMessage.networkErrorMsg);
            //    });
            //
            //// 模版5
            //new indexNews5Service({})
            //    .then(function (res) {
            //        if (res.resultCode == "00") {
            //            $scope.indexNewsData5 = res.results;
            //        } else {
            //            // popupService.showToast(res.resultMessage);
            //        }
            //    }, function (error) {
            //        // popupService.showToast(commonMessage.networkErrorMsg);
            //    });
            //
            //// 模版6
            //new indexNews6Service({})
            //    .then(function (res) {
            //        if (res.resultCode == "00") {
            //            $scope.indexNewsData6 = res.results;
            //        } else {
            //            // popupService.showToast(res.resultMessage);
            //        }
            //    }, function (error) {
            //        // popupService.showToast(commonMessage.networkErrorMsg);
            //    });

            // 区域2
            new indexRegion2Service({})
                .then(function (res) {
                    if (res.resultCode == "00") {
                        $scope.indexRegion2Data = res.results[0];
                    } else {
                        // popupService.showToast(res.resultMessage);
                    }
                }, function (error) {
                    // popupService.showToast(commonMessage.networkErrorMsg);
                });

            // 区域2-1
            new indexRegion2_1Service({})
                .then(function (res) {
                    if (res.resultCode == "00" && res.results!=null && res.results!=undefined) {
                        $scope.indexRegion2_1Data = res.results[0];
                    } else {
                        // popupService.showToast(res.resultMessage);
                    }
                }, function (error) {
                    // popupService.showToast(commonMessage.networkErrorMsg);
                });

            // 新品推荐
            $scope.indexNewGoodsAllData = [];
            $scope.indexNewGoodsData = [];
            new indexNewGoodsService({})
                .then(function (res) {
                    if (res.resultCode == "00") {
                        $scope.title = res.results[0];
                        $scope.indexNewGoodsData = res.results[0].gdsMasterExtList;
                        console.log($scope.indexNewGoodsData);
                        $scope.indexNewGoodsAllData = res.results[0].gdsMasterExtList;
                        if($scope.indexNewGoodsAllData.length>8){
                            $scope.indexNewGoodsData = res.results[0].gdsMasterExtList.slice(0,8);
                        }
                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function (error) {
                    popupService.showToast(commonMessage.networkErrorMsg);
                });

            //add by cjk start
            var memberId = "";
            if(userInfo != null){
                memberId = userInfo.memberId;
            }
            new couponAllService({"memberId": memberId}).getAllCoupons()
                .then(function (res) {
                    if (res.resultCode == "00") {
                        $scope.couponData = res.results;
                        console.log($scope.couponData);
                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function (error) {
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
            $scope.getCoupon = function (couponId,distributeType,price) {
                var userInfo = localStorageService.get(KEY_USERINFO);
                if(!userInfo){
                    $scope.goLogin();
                    return;
                }
                // 手动领取
                var param = {"memberId": userInfo.memberId, "couponId": couponId};
                new couponAllService(param).getCoupon()
                    .then(function (res) {
                        if (res.resultCode == "00") {
                            if(distributeType == "60") {
                                // 现金购买
                                $scope.doPay(res.result, price);
                            }else{
                                popupService.showToast("领取成功!");
                            }
                        } else {
                            popupService.showToast(res.resultMessage);
                        }
                    }, function (error) {
                        popupService.showToast(commonMessage.networkErrorMsg);
                    });
            };
            // 支付
            $scope.doPay = function (orderId,payInfact) {
                var scope = $scope.$new();
                var selectPayModalInstance = $modal.open({
                    templateUrl: 'html/selectPay.html',
                    controller: 'selectPayCtrl',
                    backdrop: "static",
                    keyboard: false,
                    scope: scope
                });
                selectPayModalInstance.opened.then(function () {//模态窗口打开之后执行的函数

                });
                selectPayModalInstance.result.then(
                    function (result) {
                        if (result) {
                            if("10" == result.type){
                                //支付测试
                                $('#orderId').val(orderId);
                                $('#ordMaster')[0].action = '/qyds-web-pc/alipay/orderAliPay.json';
                                $('#ordMaster')[0].submit();
                            }else if("20" == result.type){
                                $scope.getWXPayInfo(orderId,payInfact);
                            }else if("30" == result.type){
                                $('#orderId').val(orderId);
                                $('#ordMaster')[0].action = '/qyds-web-pc/unionpay/orderUnionPay.json';
                                $('#ordMaster')[0].submit();
                            }
                        }
                    }, function (reason) {
                        //点击空白区域，总会输出backdrop click，点击取消，则会暑促cancel
                    });
            };

            $scope.getWXPayInfo = function (out_trade_no,payInfact) {
                var params = {
                    "openid": "",
                    "product_id":out_trade_no,
                    "body": "商品订单"+out_trade_no,
                    "out_trade_no": out_trade_no,
                    "total_fee": (parseFloat(payInfact)*100).toFixed(0),
                    "trade_type": "NATIVE"
                };

                new WeiXinService(params).getWxPayInfo().then(function (response) {
                    if (response.resultCode == '00') {
                        var codeUrl  = response.results.code_url;
                        var scope = $scope.$new();
                        scope.codeUrl = codeUrl;
                        var wxPayModalInstance = $modal.open({
                            templateUrl: 'html/wxPay.html',
                            controller: 'wxPayCtrl',
                            backdrop: "static",
                            keyboard: false,
                            scope: scope
                        });
                        wxPayModalInstance.opened.then(function () {//模态窗口打开之后执行的函数

                        });
                        wxPayModalInstance.result.then(
                            function (result) {
                                $scope.doRefresh();
                            }, function (reason) {
                                $scope.doRefresh();
                            });
                    } else {
                        popupService.showToast("支付失败,原因:" + response.resultMessage);
                        $scope.doRefresh();
                    }
                }, function (error) {
                    popupService.showToast("支付失败,可在订单列表中继续支付");
                    $scope.doRefresh();
                });
            };

            $scope.doMore = function(){
                $scope.indexNewGoodsData = $scope.indexNewGoodsAllData;
            };

            // 商品详细
            $scope.goGoodsDetail = function (goodsId) {
                $state.go("goodsDetail", {"goodsId": goodsId});
            };

            // 商品详细
            $scope.goOtherPage = function (type, typeId,index,cmsId) {
                // 点击商品的时候
                if (type == 41) {
                    var array = typeId.split(",");
                    if(array != null && array.length > 1){
                        //$state.go("goodsListDisplay", {"type" : typeId});
                        $state.go("goodsList", {"actId":'',"firstGoodsTypeId": typeId, "type": type,"cmsId":cmsId,"pageType":'',"classifyId":'',"classifyName":''});
                    }else if(array.length == 1){
                        $state.go("goodsDetail", {"goodsId" : typeId});
                    }
                    //else{
                    //    // 跳转到个人中心
                    //    var userInfo = localStorageService.get(KEY_USERINFO);
                    //    if (userInfo) {
                    //        $state.go("personalCenter.pointExchange");
                    //    } else {
                    //        $scope.goLogin("personalCenter.pointExchange");
                    //    }
                    //}

                    // 商品分类的时候
                } else if (type == 42) {
                    // alert(typeId);
                    $state.go("goodsList", {"actId":'',"firstGoodsTypeId": typeId, "type": type,"cmsId":'',"pageType":'',"classifyId":'',"classifyName":''});

                    // 活动的时候
                } else if (type == 43) {
                    $state.go("goodsList", {"actId": typeId,"firstGoodsTypeId":'', "type": type,"cmsId":'',"pageType":'',"classifyId":'',"classifyName":''});
                } else if (type == 44){

                    //提示登陆
                    var userInfo = localStorageService.get(KEY_USERINFO);
                    if(!userInfo){
                        $scope.goLogin();
                        return;
                    }

                    //跳转新的活动画面
                    $state.go("pointsExchangePageNew");
                } else if (type == 45){

                    //提示登陆

                    var userInfo = localStorageService.get(KEY_USERINFO);
                    if(!userInfo){
                        $scope.goLogin();
                        return;
                    }

                    //跳转积分兑换优惠劵画面
                    $state.go("pointsCoupponPage");
                }else if (type == 46){

                    //跳转秒杀画面
                    $state.go("secKill");
                }else if (type == 47){
                    var userInfo = localStorageService.get(KEY_USERINFO);
                    if(!userInfo){
                        $scope.goLogin();
                        return;
                    }
                    //跳转抽奖画面
                    $state.go("prizeDraw",{"prizeDrawId": typeId});
                }
            };

            $scope.goTop = function(){
                $('body,html').animate({scrollTop:0},500);
                return false;
            };

            $scope.goActivityMore = function(activityName){
                // $scope.searchInfo.value = activityName;
                // $state.go("searchGoods", {'searchKey': activityName});
                var type = $scope.slides[0].itemType;
                var typeId = $scope.slides[0].itemTypeVal;
                var index = '';
                var cmsId = $scope.slides[0].cmsId;

                // 点击商品的时候
                if (type == 41) {
                    var array = typeId.split(",");
                    if(array != null && array.length > 1){
                        //$state.go("goodsListDisplay", {"type" : typeId});
                        $state.go("goodsList", {"actId":'',"firstGoodsTypeId": typeId, "type": type,"cmsId":cmsId,"pageType":'',"classifyId":'',"classifyName":''});
                    }else {
                        $state.go("goodsDetail", {"goodsId" : typeId});
                    }

                    // 商品分类的时候
                } else if (type == 42) {
                    $state.go("goodsList", {"actId":'',"firstGoodsTypeId": typeId, "type": type,"cmsId":'',"pageType":'',"classifyId":'',"classifyName":''});

                    // 活动的时候
                } else if (type == 43) {
                    $state.go("goodsList", {"actId": typeId,"firstGoodsTypeId":'', "type": type,"cmsId":'',"pageType":'',"classifyId":'',"classifyName":''});
                }
            };

            // 分类区
            var pageType = '';
            $scope.categories = [
            //    {
            //    typeId:"66c85de5-2bd4-4517-8387-cdcda445a5b4",
            //    pageType:"0",
            //    imageUrl:"image_temp2/3_1.jpg"
            //},{
            //    typeId:"b97f11e6-df14-4c75-ab1f-2c037c70f77b",
            //    pageType:"1",
            //    imageUrl:"image_temp2/3_2.jpg"
            //},{
            //    typeId:"b97f11e6-df14-4c75-ab1f-2c037c70f77b",
            //    pageType:"2",
            //    imageUrl:"image_temp2/3_3.jpg"
            //},{
            //    typeId:"b97f11e6-df14-4c75-ab1f-2c037c70f77b",
            //    pageType:"3",
            //    imageUrl:"image_temp2/3_4.jpg"
            //},{
            //    typeId:"b97f11e6-df14-4c75-ab1f-2c037c70f77b",
            //    pageType:"4",
            //    imageUrl:"image_temp2/3_5.jpg"
            //},{
            //    typeId:"b97f11e6-df14-4c75-ab1f-2c037c70f77b",
            //    pageType:"5",
            //    imageUrl:"image_temp2/3_6.jpg"
            //},{
            //    typeId:"b97f11e6-df14-4c75-ab1f-2c037c70f77b",
            //    pageType:"6",
            //    imageUrl:"image_temp2/3_7.jpg"
            //}
            ];
            $scope.categoriesPhone = [];

            // 区域3
            new indexRegion3Service({})
                .then(function (res) {
                    if (res.resultCode == "00") {
                        $scope.indexRegion3Data = res.results[0];
                        if($scope.indexRegion3Data.cmList != null){
                            for(var i = 0; i<$scope.indexRegion3Data.cmList.length; i++){
                                var item = $scope.indexRegion3Data.cmList[i];
                                var json = {};
                                json.typeId = item.itemTypeVal.split('_')[0];
                                json.pageType = item.comment;
                                json.imageUrl = item.imageUrl;
                                json.classifyId = item.itemTypeVal;
                                json.classifyName = item.textComment;
                                $scope.categories.push(json);
                            }

                            angular.element(document).ready(function() {
                                var swipeCategory = new Swiper('.swiper-category', {
                                    slidesPerView: 7,
                                    spaceBetween: 30,
                                    freeMode: true
                                });
                            });
                        }
                    } else {
                        // popupService.showToast(res.resultMessage);
                    }
                }, function (error) {
                    // popupService.showToast(commonMessage.networkErrorMsg);
                });

            //区域3手机
            new indexRegion3PhoneService({})
                .then(function (res) {
                    if (res.resultCode == "00") {
                        $scope.indexRegion3PhoneData = res.results[0];
                        if($scope.indexRegion3PhoneData.cmList != null){
                            for(var i = 0; i<$scope.indexRegion3PhoneData.cmList.length; i++){
                                var item = $scope.indexRegion3PhoneData.cmList[i];
                                var json = {};
                                json.typeId = item.itemTypeVal.split('_')[0];
                                json.pageType = item.comment;
                                json.imageUrl = item.imageUrl;
                                json.classifyId = item.itemTypeVal;
                                json.classifyName = item.textComment;
                                $scope.categoriesPhone.push(json);
                            }

                            angular.element(document).ready(function() {
                                var swipeCategory = new Swiper('.swiper-category', {
                                    slidesPerView: 7,
                                    spaceBetween: 30,
                                    freeMode: true
                                });
                            });
                        }
                    } else {
                        // popupService.showToast(res.resultMessage);
                    }
                }, function (error) {
                    // popupService.showToast(commonMessage.networkErrorMsg);
                });

            //// 区域4
            //new indexRegion4Service({})
            //    .then(function (res) {
            //        if (res.resultCode == "00") {
            //            $scope.indexRegion4Data = res.results[0];
            //        } else {
            //            // popupService.showToast(res.resultMessage);
            //        }
            //    }, function (error) {
            //        // popupService.showToast(commonMessage.networkErrorMsg);
            //    });
            //
            //// 区域5
            //new indexRegion5Service({})
            //    .then(function (res) {
            //        if (res.resultCode == "00") {
            //            $scope.indexRegion5Data = res.results[0];
            //        } else {
            //            // popupService.showToast(res.resultMessage);
            //        }
            //    }, function (error) {
            //        // popupService.showToast(commonMessage.networkErrorMsg);
            //    });
            //// 区域6
            //new indexRegion6Service({})
            //    .then(function (res) {
            //        if (res.resultCode == "00") {
            //            $scope.indexRegion6Data = res.results[0];
            //        } else {
            //            // popupService.showToast(res.resultMessage);
            //        }
            //    }, function (error) {
            //        // popupService.showToast(commonMessage.networkErrorMsg);
            //    });
            //
            //// 区域7
            //new indexRegion7Service({})
            //    .then(function (res) {
            //        if (res.resultCode == "00") {
            //            $scope.indexRegion7Data = res.results[0];
            //        } else {
            //            // popupService.showToast(res.resultMessage);
            //        }
            //    }, function (error) {
            //        // popupService.showToast(commonMessage.networkErrorMsg);
            //    });

            $scope.goGoodsList = function(typeId,pageType,classifyId,classifyName){
                $state.go("goodsList", {"actId":'',"firstGoodsTypeId": typeId, "type": 42,"cmsId":'',"pageType":pageType,"classifyId":classifyId,"classifyName":classifyName});
            };

            // 搭配区
            $scope.matches = [
            //    {
            //    goodsId:"f491e5e4-7340-4ae1-87b3-9e71d66fcbc8",
            //    imageUrl:"image_temp2/8_1.jpg"
            //},{
            //    goodsId:"869a56f6-bc5d-400c-850d-99eae7e6e2ab",
            //    imageUrl:"image_temp2/8_2.jpg"
            //},{
            //    goodsId:"e2c162f7-c5f1-40be-9897-b23e9f94396a",
            //    imageUrl:"image_temp2/8_3.jpg"
            //},{
            //    goodsId:"2801e4ef-6693-48f9-8c6f-b7afafca58b5",
            //    imageUrl:"image_temp2/8_4.jpg"
            //},{
            //    goodsId:"9b6d014c-f4e0-4926-925b-bf14049c605e",
            //    imageUrl:"image_temp2/8_5.jpg"
            //},{
            //    goodsId:"b16ee3db-3866-4889-9cfd-1c670d947eb8",
            //    imageUrl:"image_temp2/8_6.jpg"
            //},{
            //    goodsId:"c05d5bd7-42b8-4c06-bcdc-19046ea3a228",
            //    imageUrl:"image_temp2/8_7.jpg"
            //},{
            //    goodsId:"62b9c5a8-d5e0-4736-b6f0-25ef83e604f7",
            //    imageUrl:"image_temp2/8_8.jpg"
            //},{
            //    goodsId:"c5947986-4fec-4f01-9743-8a9f423e2e5d",
            //    imageUrl:"image_temp2/8_9.jpg"
            //}
            ];

            // 区域8
            new indexRegion8Service({})
                .then(function (res) {
                    if (res.resultCode == "00") {
                        $scope.indexRegion8Data = res.results[0];
                        if($scope.indexRegion8Data.cmList != null){

                            $scope.titleImg = $scope.indexRegion8Data.cmList[0].imageUrl;
                            for(var i = 1; i<$scope.indexRegion8Data.cmList.length; i++){
                                var item = $scope.indexRegion8Data.cmList[i];
                                var json = {};
                                json.goodsId = item.itemTypeVal;
                                json.imageUrl = item.imageUrl;
                                $scope.matches.push(json);
                            }

                            angular.element(document).ready(function() {
                                var swipeCategory = new Swiper('.swiper-category', {
                                    slidesPerView: 7,
                                    spaceBetween: 30,
                                    freeMode: true
                                });
                                var swipeBottom= new Swiper('.swiper-bottom', {
                                    nextButton: '.swiper-button-next',
                                    prevButton: '.swiper-button-prev',
                                    slidesPerView: 7,
                                    spaceBetween: 2,
                                    freeMode: true,
                                    loop:true
                                });
                                var swipeBottomPhone = new Swiper('.swiper-bottom-phone', {
                                    nextButton: '.swiper-button-next',
                                    prevButton: '.swiper-button-prev',
                                    slidesPerView: 3,
                                    spaceBetween: 2,
                                    freeMode: true,
                                    loop:true
                                });
                            });
                            $scope.showShadow = function(match){
                                console.log("enter");
                                if(match.showShadow != '1'){
                                    match.showShadow = '1';
                                }
                            }
                            $scope.hideShadow = function(match){
                                console.log("out");
                                if(match.showShadow != '0'){
                                    match.showShadow = '0';
                                }
                            }
                        }
                    } else {
                        // popupService.showToast(res.resultMessage);
                    }
                }, function (error) {
                    // popupService.showToast(commonMessage.networkErrorMsg);
                });

            // 返回顶部
            $scope.scroll2Top = function(){
                $('body,html').animate({scrollTop:0},500);
                return false;
            }
            $scope.openPrizeDraw = function(){
                var userInfo = localStorageService.get(KEY_USERINFO);
                if(!userInfo){
                    $scope.goLogin();
                    return;
                }
                var scope = $scope.$new();
                var prizeDrawModalInstance = $modal.open({
                    templateUrl: 'html/prizeDraw.html',
                    controller: 'prizeDrawCtrl',
                    backdrop: "static",
                    keyboard: false,
                    scope: scope
                });
                prizeDrawModalInstance.opened.then(function () {//模态窗口打开之后执行的函数

                });
                prizeDrawModalInstance.result.then(
                    function (result) {
                        if (result) {
                        }
                    }, function (reason) {
                        //点击空白区域，总会输出backdrop click，点击取消，则会暑促cancel
                    });
            }
        }])
    // 个人中心
    .controller('personalCenterCtrl', ["$scope", "$state", "$http", 'localStorageService',
        function ($scope, $state, $http, localStorageService) {

            var info = localStorageService.get(KEY_USERINFO);
            if (!info) {
                $scope.goLogin();
                return;
            }else{
                $scope.userInfo = info;
            }

            //默认一个选择的功能 例如我的订单
            // 购物袋
            $scope.goBag = function () {
                $state.go("personalCenter.bag");
            };

            // 个人信息
            $scope.goPersonalInfo = function () {
                $state.go("personalCenter.personal");
            };

            // 收货地址
            $scope.goAddressList = function () {
                $state.go("personalCenter.addressList");
            };
            // 心愿单
            $scope.goFavorite = function () {
                $state.go("personalCenter.favorite");
            };

            // 我的优惠券
            $scope.goCouponList = function () {
                $state.go("personalCenter.couponList");
            };

            //我的订单
            $scope.goOrderList = function () {
                $state.go("personalCenter.orderList");
            };

            //线下订单
            $scope.goOrderListOffLine = function () {
                $state.go("personalCenter.orderListOffLine");
            };

            //我的积分
            $scope.goPointExchange = function () {
                $state.go("personalCenter.pointExchange");
            };

        }])
    // 我的订单
    .controller('orderListCtrl', ["$scope", "$state", "orderService", "localStorageService", 'popupService','$modal','WeiXinService',
        function ($scope, $state, orderService, localStorageService,popupService,$modal,WeiXinService) {
        var userInfo = localStorageService.get(KEY_USERINFO);

        $scope.currentPage = 1;
        $scope.currentPagePhone = 1;
        $scope.totalPage = 0;
        $scope.pageSize = 7;
        $scope.orderList = [];
        $scope.orderPhoneList = [];
        $scope.totalcount = 0;

        $scope.orderType = 1;

        $scope.orderChangePage = function () {
            //分页样式初始化
            $(".pager").ucPager({
                //pageClass     : "分页样式",
                currentPage: $scope.currentPage, //当前页数
                totalPage: $scope.totalPage,   //总页数
                pageSize: $scope.pageSize,   //每一页显示的件数
                clickCallback: function (page) {
                    //下一页 上一页 页数跳转
                    $scope.currentPage = page;
                    $scope.getData();
                    // $scope.orderChangePage();
                }
            });
        };

        $scope.selectByStatus = function(type) {
            $scope.currentPage = 1;
            $scope.orderList = [];
            $scope.totalPage = 0;
            $scope.orderType = type;
            $scope.getData();
        };

        $scope.setSelectedTabStyle = function(type) {
            if(type==$scope.orderType){
                return {"color":"#333333"};
            }else{
                return {"color":"#999999"};
            }
        };

        $scope.selectPhoneByStatus = function(type) {
            $scope.currentPagePhone = 1;
            $scope.orderPhoneList = [];
            $scope.totalPage = 0;
            $scope.orderType = type;
            $scope.hasMore = true;
            $scope.getPhoneData();
        };

        $scope.getData = function () {
            var param = {
                "memberId": userInfo.memberId,
                "needColumns": $scope.pageSize,
                "startPoint": ($scope.currentPage-1) * $scope.pageSize,
                "type":$scope.orderType
            };
            new orderService(param).getList()
                .then(function (res) {
                        if (res.resultCode == "00") {
                            $scope.totalPage = res.totalPage;
                            $scope.orderList = res.results;
                            $scope.allCount = res.allCount;
                            $scope.waitPayCount = res.waitPayCount;
                            $scope.waitDeliveryCount = res.waitDeliveryCount;
                            $scope.waitReceiveCount = res.waitReceiveCount;
                            $scope.completedCount = res.completedCount;
                            $scope.orderChangePage();
                        } else {
                            popupService.showToast(res.resultMessage);
                        }
                    }, function (error) {
                        $scope.hasMore = false;
                        popupService.showToast(commonMessage.networkErrorMsg);
                    }
                );
        };
        $scope.isPhoneLoading = false;
        $scope.hasMore = true;
        $scope.getPhoneData = function () {
            if($scope.isPhoneLoading){
                return;
            }
            $scope.isPhoneLoading = true;

            var param = {
                "memberId": userInfo.memberId,
                "needColumns": $scope.pageSize,
                "startPoint": ($scope.currentPagePhone - 1) * $scope.pageSize,
                "type": $scope.orderType
            };

            //初始化调用
            new orderService(param).getList().then(function (res) {
                $scope.isPhoneLoading = false;
                if (res.resultCode == "00") {
                    if (res.results.length != 0) {
                        $scope.orderPhoneList = $scope.orderPhoneList.concat(res.results);
                        $scope.allCount = res.allCount;
                        $scope.waitPayCount = res.waitPayCount;
                        $scope.waitDeliveryCount = res.waitDeliveryCount;
                        $scope.waitReceiveCount = res.waitReceiveCount;
                        $scope.completedCount = res.completedCount;
                        $scope.totalcount = res.totalCount;
                        if(res.results.length<$scope.pageSize){
                            $scope.hasMore = false;
                        }
                    } else {
                        $scope.currentPagePhone = $scope.currentPagePhone - 1;
                        $scope.hasMore = false;
                    }
                } else {
                    popupService.showToast(res.resultMessage);
                }
            }, function (err) {
                $scope.isPhoneLoading = false;
                popupService.showToast(commonMessage.networkErrorMsg);
            });
        };

        //加载更多
        $scope.doGetMore = function () {
            $scope.currentPagePhone = $scope.currentPagePhone + 1;
            $scope.getPhoneData($scope.orderType);
        };

        $scope.doRefresh = function(){
            $scope.selectByStatus($scope.orderType);
            $scope.selectPhoneByStatus($scope.orderType);
        };

        // 跳转物流信息
        $scope.goLogisticsList = function (gdsId) {
            $state.go("personalCenter.logisticsList", {"orderId": gdsId});
        };

        $scope.applyRefund = function (orderId) {
            $state.go("personalCenter.applyRefund", {orderId:orderId});
        };

        // 支付
        $scope.doPay = function (orderId,orderCode,payInfact) {
            var scope = $scope.$new();
            var selectPayModalInstance = $modal.open({
                templateUrl: 'html/selectPay.html',
                controller: 'selectPayCtrl',
                backdrop: "static",
                keyboard: false,
                scope: scope
            });
            selectPayModalInstance.opened.then(function () {//模态窗口打开之后执行的函数

            });
            selectPayModalInstance.result.then(
                function (result) {
                    if (result) {
                        if("10" == result.type){
                            //支付测试
                            $('#orderId').val(orderId);
                            $('#ordMaster')[0].action = '/qyds-web-pc/alipay/orderAliPay.json';
                            $('#ordMaster')[0].submit();
                        }else if("20" == result.type){
                            $scope.getWXPayInfo(orderCode,payInfact);
                        }else if("30" == result.type){
                            $('#orderId').val(orderId);
                            $('#ordMaster')[0].action = '/qyds-web-pc/unionpay/orderUnionPay.json';
                            $('#ordMaster')[0].submit();
                        }
                    }
                }, function (reason) {
                    //点击空白区域，总会输出backdrop click，点击取消，则会暑促cancel
                });
        };

            $scope.getWXPayInfo = function (out_trade_no,payInfact) {
                var params = {
                    "openid": "",
                    "product_id":out_trade_no,
                    "body": "商品订单"+out_trade_no,
                    "out_trade_no": out_trade_no,
                    "total_fee": (parseFloat(payInfact)*100).toFixed(0),
                    "trade_type": "NATIVE"
                };

                new WeiXinService(params).getWxPayInfo().then(function (response) {
                    if (response.resultCode == '00') {
                        var codeUrl  = response.results.code_url;
                        var scope = $scope.$new();
                        scope.codeUrl = codeUrl;
                        var wxPayModalInstance = $modal.open({
                            templateUrl: 'html/wxPay.html',
                            controller: 'wxPayCtrl',
                            backdrop: "static",
                            keyboard: false,
                            scope: scope
                        });
                        wxPayModalInstance.opened.then(function () {//模态窗口打开之后执行的函数

                        });
                        wxPayModalInstance.result.then(
                            function (result) {
                                $scope.doRefresh();
                            }, function (reason) {
                                $scope.doRefresh();
                            });
                    } else {
                        popupService.showToast("支付失败,原因:" + response.resultMessage);
                        $scope.doRefresh();
                    }
                }, function (error) {
                    popupService.showToast("支付失败,可在订单列表中继续支付");
                    $scope.doRefresh();
                });
            };

        //订单详情
        $scope.orderDetail = function(id){
            $state.go("personalCenter.orderDetail", {"orderId": id});
        };

        // 取消订单
        $scope.cancelOrder = function (orderId) {

            popupService.showConfirm('确定要取消此订单吗?', function(){

                var param = {
                    "memberId": userInfo.memberId,
                    "orderId": orderId
                };

                new orderService(param).cancelOrder()
                    .then(function (res) {
                        if (res.resultCode == "00") {
                            popupService.showToast("订单已取消。");
                            $scope.doRefresh();
                        } else {
                            popupService.showToast(res.resultMessage);
                        }
                    }, function (error) {
                        popupService.showToast(commonMessage.networkErrorMsg);
                    });
            });
        };

        // 确认收货
        $scope.confirmReceiptInMaster = function (orderId) {
            popupService.showConfirm('确定已收到此订单中所有的商品了吗?', function(){
                var param = {
                    "memberId": userInfo.memberId,
                    "orderId": orderId
                };

                new orderService(param).confirmReceiptInMaster().then(function (res) {
                    if (res.resultCode == "00") {
                        popupService.showToast("订单已确认收货。");
                        $scope.doRefresh();
                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function (error) {
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
            });
        };

        // 删除订单
        $scope.deleteOrder = function (orderId) {
            popupService.showConfirm('确定要删除此订单吗?', function(){
                var param = {
                    "memberId": userInfo.memberId,
                    "orderId": orderId
                };

                new orderService(param).deleteOrder().then(function (res) {
                            if (res.resultCode == "00") {
                                popupService.showToast("订单已删除。");
                                $scope.doRefresh();
                            } else {
                                popupService.showToast(res.resultMessage);
                            }
                        }, function (error) {
                            popupService.showToast(commonMessage.networkErrorMsg);
                });
            });

        };
        // 退货（全单）
        $scope.refunds = function (orderItem) {
            localStorageService.set(KEY_PARAM_REFUND_ORDER,orderItem);
            $state.go("personalCenter.applyReturnGoods");
        };

        // 跳转到商品详细
        $scope.goGoodsDetail = function (gdsId) {
            $state.go("goodsDetail", {"goodsId": gdsId});
        };

        $scope.getData(1);
        $scope.getPhoneData(1);
    }])
    // 线下订单
    .controller('orderListOffLineCtrl', ["$scope", "$state", "orderOffLineService", "localStorageService", 'popupService',
        function ($scope, $state, orderOffLineService, localStorageService,popupService) {
            var userInfo = localStorageService.get(KEY_USERINFO);

            $scope.currentPage = 1;
            $scope.currentPagePhone = 1;
            $scope.totalPage = 0;
            $scope.pageSize = 7;
            $scope.orderList = [];
            $scope.orderPhoneList = [];
            $scope.totalcount = 0;

            $scope.orderChangePage = function () {
                //分页样式初始化
                $(".pager").ucPager({
                    //pageClass     : "分页样式",
                    currentPage: $scope.currentPage, //当前页数
                    totalPage: $scope.totalPage,   //总页数
                    pageSize: $scope.pageSize,   //每一页显示的件数
                    clickCallback: function (page) {
                        //下一页 上一页 页数跳转
                        $scope.currentPage = page;
                        $scope.getData();
                        // $scope.orderChangePage();
                    }
                });
            };

            $scope.getData = function () {
                var param = {
                    "memberId": userInfo.memberId,
                    "needColumns": $scope.pageSize,
                    "startPoint": ($scope.currentPage-1) * $scope.pageSize
                };
                new orderOffLineService(param).getList()
                    .then(function (res) {
                            if (res.resultCode == "00") {
                                $scope.totalPage = res.totalPage;
                                $scope.orderList = res.results;
                                $scope.allCount = res.allCount;
console.log($scope.orderList);
                                $scope.orderChangePage();
                            } else {
                                popupService.showToast(res.resultMessage);
                            }
                        }, function (error) {
                            $scope.hasMore = false;
                            popupService.showToast(commonMessage.networkErrorMsg);
                        }
                    );
            };
            $scope.isPhoneLoading = false;
            $scope.hasMore = true;
            $scope.getPhoneData = function () {
                if($scope.isPhoneLoading){
                    return;
                }
                $scope.isPhoneLoading = true;

                var param = {
                    "memberId": userInfo.memberId,
                    "needColumns": $scope.pageSize,
                    "startPoint": ($scope.currentPagePhone - 1) * $scope.pageSize
                };

                //初始化调用
                new orderOffLineService(param).getList().then(function (res) {
                    $scope.isPhoneLoading = false;
                    if (res.resultCode == "00") {
                        if (res.results.length != 0) {
                            $scope.orderPhoneList = $scope.orderPhoneList.concat(res.results);
                            $scope.allCount = res.allCount;

                            $scope.totalcount = res.totalCount;
                            if(res.results.length<$scope.pageSize){
                                $scope.hasMore = false;
                            }
                        } else {
                            $scope.currentPagePhone = $scope.currentPagePhone - 1;
                            $scope.hasMore = false;
                        }
                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function (err) {
                    $scope.isPhoneLoading = false;
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
            };

            //加载更多
            $scope.doGetMore = function () {
                $scope.currentPagePhone = $scope.currentPagePhone + 1;
                $scope.getPhoneData($scope.orderType);
            };

            $scope.getData(1);
            $scope.getPhoneData(1);
        }])
    //退货画面
    .controller('applyReturnGoodsCtrl', ["$scope", "$state", "orderService", "localStorageService", 'popupService', 'storeService', '$modal',
        function ($scope, $state, orderService, localStorageService, popupService, storeService, $modal) {
            //用户信息
            var userInfo = localStorageService.get(KEY_USERINFO);
            //订单信息
            $scope.refundsInfo = localStorageService.get(KEY_PARAM_REFUND_ORDER);

            angular.element(document).ready(function () {
                for (var i = 0; i < $scope.refundsInfo.ordList.length; i++) {
                    var goods = $scope.refundsInfo.ordList[i];
                    goods.newQuantity = goods.quantity;
                    $('.numSpinner' + i).spinner({min: 0, value: 1, max: goods.quantity});

                }
            });
            //退货原因
            $scope.refundsInfo.applyComment = "";
            // 退换货地点 (10.电商，20.门店)，默认电商退货。
            $scope.refundsInfo.rexPoint = "10";
            $scope.refundsInfo.rexStoreId = "";
            $scope.refundsInfo.rexStoreName = "";

            $scope.changeRexPoint = function (rexP) {
                $scope.refundsInfo.rexPoint = rexP;
            };

            // 退货
            $scope.refundOrder = function () {
                // 退货数量
                var returnCount = "";
                // 子订单id
                var detailId = "";
                // 判断用户输入的退货数量是不是全都是0.
                var allZero = true;
                // 输入值check
                // 该订单允许拆单退货
                if ($scope.refundsInfo.canDivide == '1') {
                    // 检测客户选择的退货数量是否超过已购买商品的数量
                    for (var i = 0; i < $scope.refundsInfo.ordList.length; i++) {
                        var goods = $scope.refundsInfo.ordList[i];

                        if (goods.newQuantity > goods.quantity) {
                            popupService.showToast("此订单中的第" + (i + 1) + "件商品：【" + goods.goodsName + "】所输入的退货数量超过了您购买的该商品数量。请重新输入该商品的退货数量。");
                            return;
                        }
                        if (goods.newQuantity != 0) {
                            allZero = false;
                        }
                        detailId += goods.detailId + ",";
                        returnCount += goods.newQuantity + ",";
                    }

                    // 用户输入的退货数量全都是0
                    if (allZero) {
                        popupService.showToast("请输入想要退货的数量。");
                        return;
                    }
                }
                ;

                if ($scope.refundsInfo.applyComment == '') {
                    popupService.showToast("请输入您的退货理由。");
                    return;
                }
                ;

                var rexStoreId = null;
                var rexStoreName = null;

                // 退货方式
                if ($scope.refundsInfo.rexPoint == '20') {
                    // 门店退货
                    if (!$scope.refundsInfo.rexStoreId || $scope.refundsInfo.rexStoreId.length == 0) {
                        popupService.showToast("请选择退货的门店");
                        return;
                    }
                    rexStoreId = $scope.refundsInfo.rexStoreId;
                    rexStoreName = $scope.refundsInfo.rexStoreName;
                }

                popupService.showConfirm('确定要申请退货所选中的商品吗?', function () {

                    // 退货的门店信息
                    var param = {
                        "memberId": userInfo.memberId,
                        "orderId": $scope.refundsInfo.orderId,
                        "applyComment": $scope.refundsInfo.applyComment,
                        "returnExchangeExt": "10",
                        "rexPoint": $scope.refundsInfo.rexPoint,
                        "rexStoreId": rexStoreId,
                        "rexStoreName": rexStoreName
                    };

                    // 该订单允许拆单退货
                    if ($scope.refundsInfo.canDivide == '1') {
                        param.detailId = detailId.substr(0, detailId.length - 1);
                        param.returnCount = returnCount.substr(0, returnCount.length - 1);
                        new orderService(param).applyReturnSubGoods()
                            .then(function (res) {
                                if (res.resultCode == "00") {
                                    popupService.showToast("退货申请已提交，请耐心等待客服人员与您联系。");
                                    $scope.goBack();
                                } else {
                                    popupService.showToast(res.resultMessage);
                                }
                            }, function (error) {
                                popupService.showToast(commonMessage.networkErrorMsg);
                            });
                    } else {
                        new orderService(param).applyReturnGoods().then(function (res) {
                            if (res.resultCode == "00") {
                                popupService.showToast("退货申请已提交，请耐心等待客服人员与您联系。");
                                $scope.goBack();
                            } else {
                                popupService.showToast(res.resultMessage);
                            }
                        }, function (error) {
                            popupService.showToast(commonMessage.networkErrorMsg);
                        });
                    }
                });
            };

            $scope.goBack = function () {
                $state.go("personalCenter.orderList");
            };
            $scope.selectStore = function () {
                var scope = $scope.$new();
                var selectStoreModalInstance = $modal.open({
                    templateUrl: 'html/selectStore.html',
                    controller: 'selectStoreCtrl',
                    backdrop: true,
                    scope: scope
                });
                selectStoreModalInstance.opened.then(function () {//模态窗口打开之后执行的函数

                });
                selectStoreModalInstance.result.then(
                    function (result) {
                        if (result) {
                            $scope.refundsInfo.rexStoreId = result.storeId;
                            $scope.refundsInfo.rexStoreAddress = result.storeAddress;
                            $scope.refundsInfo.rexStoreName = result.storeName;
                        }
                    }, function (reason) {
                        //点击空白区域，总会输出backdrop click，点击取消，则会暑促cancel
                    });
            };

        }])

    //退款画面
    .controller('applyRefundCtrl', ["$scope","$state","orderService","localStorageService",'popupService','$stateParams',
        function ($scope,$state,orderService,localStorageService,popupService,$stateParams) {
            var userInfo = localStorageService.get(KEY_USERINFO);
            $scope.orderId = $stateParams.orderId;
            console.log($scope.orderId);
            $scope.refundsInfo = {
                applyComment:""
            }
            // 退货
            $scope.applyRefund = function () {

                if ($scope.refundsInfo.applyComment == '') {
                    popupService.showToast("请输入您的退款理由。");
                    return;
                };

                popupService.showConfirm('确定要申请退款吗?',function(){

                    var param = {
                        "memberId": userInfo.memberId,
                        "orderId": $scope.orderId,
                        "applyComment": $scope.refundsInfo.applyComment
                    };
                    new orderService(param).applyRefund().then(function (res) {
                        if (res.resultCode == "00") {
                            popupService.showToast("退款申请已提交，请耐心等待客服人员与您联系。");
                            $scope.goBack();
                        } else {
                            popupService.showToast(res.resultMessage);
                        }
                    }, function (error) {
                        popupService.showToast(commonMessage.networkErrorMsg);
                    });
                });
            };

            $scope.goBack = function () {
                history.go(-1);
            };
        }])

    // 优惠券
    .controller('couponCenterCtrl', ["$scope","$state","couponService","localStorageService",'popupService',
        function ($scope,$state,couponService,localStorageService, popupService) {
        var userInfo = localStorageService.get(KEY_USERINFO);
        $scope.couponListData = [];     //PC 初始化的场合 
        $scope.couponListPhoneData = [];
        $scope.currentPage = 1;
        $scope.currentPagePhone = 1;
        $scope.totalPage = 0;
        $scope.pageSize = 7;

        $scope.type = 0;

        $scope.setSelectedTabStyle = function(type) {
            if(type==$scope.type){
                return {"color":"#333333"};
            }else{
                return {"color":"#999999"};
            }
        };

        $scope.refreshData = function(type) {
            $scope.type = type;
            var param = {};
            param.currentPage = $scope.currentPage;
            param.totalPage = $scope.totalPage;
            param.pageSize = $scope.pageSize;
            param.memberId = userInfo.memberId;

            if(type != 0){
                param.status = type;
            }

            new couponService({"data":param})
                .then(function (res) {
                    if (res.resultCode == "00") {
                        $scope.couponListData = res.data;
                        $scope.currentPage = res.currentPage; //当前页数
                        $scope.totalPage = res.totalPage;   //总页数
                        $scope.pageSize = res.pageSize;   //每一页显示的件数
                        $scope.iTotalRecords = res.iTotalRecords;
                        $scope.usedCount = res.usedCount;
                        $scope.notUsedCount = res.notUsedCount;
                        $scope.dochangePage();
                        // $scope.footerData = res.results;
                        // $ionicSlideBoxDelegate.update();
                    } else {
                         popupService.showToast(res.resultMessage);
                    }
                }, function () {
                     popupService.showToast(commonMessage.networkErrorMsg);
                });
        };
        $scope.hasMore = true;
            $scope.refreshPhoneData = function(type){
                $scope.hasMore = true;
                $scope.couponListPhoneData = [];
                $scope.getPhoneData(type);
            }
            $scope.getPhoneData = function(type) {
            if($scope.isPhoneLoading){
                return;
            }
            $scope.isPhoneLoading = true;
            $scope.type = type;

            var count = 0;
            var start = 0;
            if($scope.couponListPhoneData.length){
                count = $scope.pageSize;
                start = ($scope.currentPagePhone-1) * count;
                if(start >= $scope.iTotalRecords){
                    return;
                }
            }
            var param = {};
            param.currentPagePhone = $scope.currentPagePhone;
            param.totalPage = $scope.totalPage;
            param.pageSize = $scope.pageSize;
            param.memberId = userInfo.memberId;

            if(type != 0){
                param.status = type;
            }

            new couponService({"data":param})
                .then(function (res) {
                    $scope.isPhoneLoading = false;
                    if (res.resultCode == "00") {
                        $scope.couponListPhoneData = $scope.couponListPhoneData.concat(res.data);
                        $scope.pageSize = res.pageSize;   //每一页显示的件数
                        $scope.iTotalRecords = res.iTotalRecords;
                        $scope.usedCount = res.usedCount;
                        $scope.notUsedCount = res.notUsedCount;
                        $scope.dochangePage();
                        if(res.data.length>=$scope.pageSize){
                            $scope.hasMore = true;
                        }else{
                            $scope.hasMore = false;
                        }
                        // $scope.footerData = res.results;
                        // $ionicSlideBoxDelegate.update();
                    } else {
                        $scope.currentPagePhone = $scope.currentPagePhone - 1;
                         popupService.showToast(res.resultMessage);
                    }
                }, function () {
                    $scope.hasMore = false;
                    $scope.isPhoneLoading = false;
                     popupService.showToast(commonMessage.networkErrorMsg);
                });
        };

        //加载更多
        $scope.doGetMore = function () {
            $scope.currentPagePhone = $scope.currentPagePhone + 1;
            $scope.getPhoneData($scope.type);
        };

        $scope.refreshData(0);
        $scope.getPhoneData(0);

        $scope.dochangePage = function () {
            //分页样式初始化
            $(".pager").ucPager({
                //pageClass     : "分页样式",
                currentPage: $scope.currentPage, //当前页数
                totalPage: $scope.totalPage,   //总页数
                pageSize: $scope.pageSize,   //每一页显示的件数
                clickCallback: function (page) {
                    //下一页 上一页 页数跳转
                    $scope.currentPage = page;
                    $scope.refreshData($scope.type);
                    $scope.dochangePage($scope.type);
                }
            });
        };
    }])
    // 地址
    .controller('addressCenterCtrl', ["$scope", '$rootScope', "localStorageService", 'addressService', '$modal', 'popupService',
        function ($scope, $rootScope, localStorageService, addressService, $modal, popupService) {
            var info = localStorageService.get(KEY_USERINFO);
            $scope.userInfo = info;

            $scope.getAddressList = function (){
                var param = {"memberId": $scope.userInfo.memberId};
                new addressService(param).getList()
                    .then(function (res) {
                        if (res.resultCode == "00") {
                            $scope.addressList = res.results;
                        } else {
                            popupService.showToast(res.resultMessage);
                        }
                    }, function (error) {
                        popupService.showToast(commonMessage.networkErrorMsg);
                    });
            };

            if (info) {
                $scope.getAddressList();
            } else {
                $scope.goLogin();
            }

            $scope.modifyAddress = function (addressId) {

                var scope = $scope.$new();
                scope.param = {
                    type: "modify",
                    addressId: addressId
                };

                var addressModalInstance = $modal.open({
                    templateUrl: 'html/modifyAddress.html',
                    controller: 'modifyAddressCtrl',
                    backdrop: true,
                    scope: scope
                });
                addressModalInstance.opened.then(function () {//模态窗口打开之后执行的函数

                });
                addressModalInstance.result.then(
                    function (result) {
                        $scope.getAddressList();
                    }, function (reason) {
                        console.log(reason);//点击空白区域，总会输出backdrop click，点击取消，则会暑促cancel
                    });
            };

            $scope.addAddress = function () {

                var scope = $scope.$new();
                scope.param = {
                    type: "add"
                };

                var addressModalInstance = $modal.open({
                    templateUrl: 'html/modifyAddress.html',
                    controller: 'modifyAddressCtrl',
                    backdrop: true,
                    scope: scope
                });
                addressModalInstance.opened.then(function () {//模态窗口打开之后执行的函数

                });
                addressModalInstance.result.then(
                    function (result) {
                        $scope.getAddressList();
                    }, function (reason) {
                        console.log(reason);//点击空白区域，总会输出backdrop click，点击取消，则会暑促cancel
                    });
            };

            $scope.deleteAddress = function (addressId) {

                popupService.showConfirm('确定删除该收货地址信息?', function(){
                    var param = {
                        addressId: addressId,
                        memberId: $scope.userInfo.memberId
                    };

                    new addressService(param).deleteItem()
                        .then(function (res) {
                            if (res.resultCode == "00") {
                                popupService.showToast("删除成功");
                                $scope.getAddressList();
                            } else {
                                popupService.showToast(res.resultMessage);
                            }
                        }, function (error) {
                            popupService.showToast(commonMessage.networkErrorMsg);
                        });
                });
            };

            $scope.changeDefault = function (addressId) {

                var param = {
                    addressId: addressId,
                    memberId: $scope.userInfo.memberId
                };

                new addressService(param).changeDefault()
                    .then(function (res) {
                        if (res.resultCode == "00") {
                            $scope.addressList = res.results;
                            popupService.showToast("设置成功");
                        } else {
                            popupService.showToast(res.resultMessage);
                        }
                    }, function (error) {
                        popupService.showToast(commonMessage.networkErrorMsg);
                    });
            };

        }])

    // 编辑收货地址
    .controller('modifyAddressCtrl', ["$scope", "$state", "$stateParams", '$modalInstance', 'commonService', 'addressService', 'localStorageService', 'popupService',
        function ($scope, $state, $stateParams, $modalInstance, commonService, addressService, localStorageService, popupService) {

            $scope.info = {provinceCd: '0', cityCd: '0', areaCd: '0', streetCd: '0'};//初始化联动下拉

            var userInfo = localStorageService.get(KEY_USERINFO);

            $scope.addressInfo = {
                name: null,
                tel: null,
                address: null,
                isDefault:false
            };

            var data = $scope.param;

            if (data.type == 'add') {
                // 获取下拉列表
                new commonService().getProvinces()
                    .then(function (res) {
                        if (res.resultCode == "00") {
                            $scope.provincesList = res.results;
                            $scope.citiesList = [];//初始化时候为空
                            $scope.areasList = [];//初始化时候为空
                            $scope.streetsList = [];//初始化时候为空
                        } else {
                            popupService.showToast(res.resultMessage);
                        }
                    }, function (error) {
                        popupService.showToast(commonMessage.networkErrorMsg);
                    });
            } else if (data.type == 'modify') {
                var param = {
                    addressId: data.addressId,
                    memberId: userInfo.memberId
                };
                new addressService(param).getDetail()
                    .then(function (res) {

                        if (res.resultCode == "00") {

                            $scope.addressInfo.address = res.result.address;
                            $scope.addressInfo.name = res.result.contactor;
                            $scope.addressInfo.tel = res.result.phone;
                            if("0" == res.result.isDefault){
                                $scope.addressInfo.isDefault =true;
                            }else{
                                $scope.addressInfo.isDefault =false;
                            }

                            $scope.info.areaCd = res.result.districtidDistrict;
                            $scope.info.provinceCd = res.result.districtidProvince;
                            $scope.info.cityCd = res.result.districtidCity;
                            $scope.info.streetCd = res.result.districtidStreet;

                            new commonService().getProvinces()
                                .then(function (res) {
                                    if (res.resultCode == "00") {
                                        $scope.provincesList = res.results;
                                    } else {
                                        popupService.showToast(res.resultMessage);
                                    }
                                }, function (error) {
                                    popupService.showToast(commonMessage.networkErrorMsg);
                                });

                            new commonService({"parentId": $scope.info.provinceCd}).getSubAddresses()
                                .then(function (res) {
                                    if (res.resultCode == "00") {
                                        $scope.citiesList = res.results;
                                    } else {
                                        popupService.showToast(res.resultMessage);
                                    }
                                }, function (error) {
                                    popupService.showToast(commonMessage.networkErrorMsg);
                                });

                            new commonService({"parentId": $scope.info.cityCd}).getSubAddresses()
                                .then(function (res) {
                                    if (res.resultCode == "00") {
                                        $scope.areasList = res.results;
                                    } else {
                                        popupService.showToast(res.resultMessage);
                                    }
                                }, function (error) {
                                    popupService.showToast(commonMessage.networkErrorMsg);
                                });

                            // <!-- 街道不显示 -->
                            //new commonService({"parentId": $scope.info.areaCd}).getSubAddresses()
                            //    .then(function (res) {
                            //        if (res.resultCode == "00") {
                            //            $scope.streetsList = res.results;
                            //        } else {
                            //            popupService.showToast(res.resultMessage);
                            //        }
                            //    }, function (error) {
                            //        popupService.showToast(commonMessage.networkErrorMsg);
                            //    });
                            // <!-- 街道不显示 -->
                        } else {
                            popupService.showToast(res.resultMessage);
                        }
                    }, function (error) {
                        popupService.showToast(commonMessage.networkErrorMsg);
                    });
            }

            // 联动区域变换
            $scope.changeArea = function (type, code) {
                var param = {"parentId": code};
                new commonService(param).getSubAddresses()
                    .then(function (res) {
                        if (res.resultCode == "00") {
                            switch (type) {
                                case '0':
                                    $scope.citiesList = res.results;
                                    $scope.areasList = [];
                                    $scope.streetsList = [];
                                    $scope.info.cityCd = '0';
                                    $scope.info.areaCd = '0';
                                    $scope.info.streetCd = '0';
                                    break;
                                case '1':
                                    $scope.areasList = res.results;
                                    $scope.streetsList = [];
                                    $scope.info.areaCd = '0';
                                    $scope.info.streetCd = '0';
                                    break;
                                case '2':
                                    $scope.streetsList = res.results;
                                    $scope.info.streetCd = '0';
                                    break;
                            }
                        } else {
                            popupService.showToast(res.resultMessage);
                        }
                    }, function (error) {
                        popupService.showToast(commonMessage.networkErrorMsg);
                    });
            };

            // 确认地址
            $scope.confirmAddress = function (isValid) {

                $scope.submitted = true;

                if(!isValid){
                    return;
                }

                if($scope.info.provinceCd == '0'
                    || $scope.info.cityCd == '0'
                    || $scope.info.areaCd == '0'){
                    return;
                }

                if (data.type == 'add') {

                    var param = {
                        memberId: userInfo.memberId,
                        districtidProvince: $scope.info.provinceCd,
                        districtidCity: $scope.info.cityCd,
                        districtidDistrict: $scope.info.areaCd,
                        districtidStreet: '',//$scope.info.streetCd,<!-- 街道不显示 -->
                        address: $scope.addressInfo.address,
                        contactor: $scope.addressInfo.name,
                        phone: $scope.addressInfo.tel,
                        isDefault:$scope.addressInfo.isDefault?"0":"1"
                    };

                    new addressService(param).add()
                        .then(function (res) {
                            if (res.resultCode == "00") {
                                popupService.showToast('添加成功');
                                $modalInstance.close();
                            } else {
                                popupService.showToast(res.resultMessage);
                            }
                        }, function (error) {
                            popupService.showToast(commonMessage.networkErrorMsg);
                        });

                } else if (data.type == 'modify') {

                    var param = {
                        addressId: data.addressId,
                        memberId: userInfo.memberId,
                        districtidProvince: $scope.info.provinceCd,
                        districtidCity: $scope.info.cityCd,
                        districtidDistrict: $scope.info.areaCd,
                        districtidStreet: '',//$scope.info.streetCd,<!-- 街道不显示 -->
                        address: $scope.addressInfo.address,
                        contactor: $scope.addressInfo.name,
                        phone: $scope.addressInfo.tel,
                        isDefault:$scope.addressInfo.isDefault?"0":"1"
                    };

                    new addressService(param).edit()
                        .then(function (res) {
                            if (res.resultCode == "00") {
                                popupService.showToast("修改成功");
                                $modalInstance.close();
                            } else {
                                popupService.showToast(res.resultMessage);
                            }
                        }, function (error) {
                            popupService.showToast(commonMessage.networkErrorMsg);
                        });
                }
            };

            $scope.closeModify = function () {
                $modalInstance.close();
            }
        }])

    // 选择赠送货品
    .controller('selectGiftCtrl', ["$scope", "$state", "$stateParams", '$modalInstance', 'confirmOrderService', 'popupService',
        function ($scope, $state, $stateParams, $modalInstance, confirmOrderService, popupService) {
            $scope.getGoodsDetail = function(){
                var param = {
                    "goodsCode": $scope.goods.goodsCode
                };
                new confirmOrderService(param).getGiftDetailByCode().then(function (vresponse) {
                    var res = vresponse.results;
                    if (vresponse.resultCode == "00") {
                        $scope.productDetailData = res;
                        // 过滤颜色
                        var validColorList = [];
                        angular.forEach($scope.productDetailData.colorList, function (color) {
                            var isValid = false;
                            angular.forEach($scope.goods.activityColorCodeList, function (colorCode) {
                                if(colorCode == color.key){
                                    isValid = true;
                                }
                            });
                            if(isValid){
                                validColorList.push(color);
                            }
                        });
                        $scope.productDetailData.colorList = validColorList;

                        //PC端图片部分
                        $scope.imageUrlJsonPc = $scope.productDetailData.imageUrlJsonPc;
                        $scope.collectNo = res.collectNo;
                        $scope.skuList = res.skulist;
                        $scope.skuImage = res.imageUrlJsonPc;
                        $scope.skuPrice = res.minAndMaxPrice;
                        $scope.newCount = 0;
                        $scope.activityPrice = res.minAndMaxPriceActivity;

                        $scope.getColorClass = function (colorValue) {
                            if ($scope.selectedColor == colorValue) {
                                return "selected";
                            } else {
                                return "";
                            }
                        };
                        $scope.getSizeClass = function (sizeValue) {
                            if ($scope.selectedSize == sizeValue) {
                                return "selected";
                            } else {
                                return "";
                            }
                        };

                        $scope.setColor = function (colorValue, colorName) {
                            $scope.selectedColor = colorValue;
                            $scope.selectedColorName = colorName;
                            $scope.refreshSkuPrice();
                        };

                        $scope.setSize = function (sizeValue,sizeName) {
                            $scope.selectedSize = sizeValue;
                            $scope.selectedSizeName = sizeName;
                            $scope.refreshSkuPrice();
                        };
                        angular.element(document).ready(function() {
                            var swiper = new Swiper('.swiper-container', {
                                nextButton: '.swiper-button-next',
                                prevButton: '.swiper-button-prev',
                                slidesPerView: 'auto',
                                centeredSlides: true,
                                initialSlide:0,
                                paginationClickable: true,
                                loop: true
                            });

                            //$scope.initGoodsDetail();
                            $scope.selectedCount = {
                                value: 1
                            };
                        });
                    } else {
                        popupService.showToast(vresponse.resultMessage);
                    }
                }, function (err) {
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
            };
            $scope.storeInit = true;
            $scope.refreshSkuPrice = function () {
                //如果某一个sku不存在在列表项目的时候
                $scope.storeInit = false;
                var isExist = false;
                angular.forEach($scope.skuList, function (sku) {
                    if (sku.nameKeyValues[0].key == $scope.selectedColor && sku.nameKeyValues[1].key == $scope.selectedSize) {
                        $scope.selectedSku = sku;
                        $scope.skuPrice = "¥" + sku.price;
                        $scope.newCount = sku.newCount;
                        $scope.activityPrice = "¥" + sku.activityPrice;
                        $scope.selectedSkuId = sku.skuid;
                        // $scope.skuImage = sku.imgs;
                        isExist = true;
                    }
                });

                if (!isExist) {
                    $scope.newCount = null;
                }
            };

            $scope.getGoodsDetail();
            $scope.confirm = function(){
                //判断没有选择SKU
                if($scope.selectedSkuId == null){
                    popupService.showToast("请选择商品的颜色和尺码.");
                    return;
                }

                if(parseInt($scope.newCount)<parseInt($scope.selectedCount.value)){
                    popupService.showToast("库存不足.");
                    //popupService.showToast("库存不足.");
                    return;
                }

                // goodsJson.actionId = goods.activity.activityId;
                // goodsJson.actionName =  goods.activity.activityName;
                // goodsJson.priceDiscount = goods.activity.newPrice;
                // goodsJson.amount = (goods.activity.originPrice*goods.quantity).toFixed(2);
                // goodsJson.amountDiscount = (goods.activity.newPrice*goods.quantity).toFixed(2);

                var goodsInfo = {};
                goodsInfo.goodsId = $scope.productDetailData.goodsId;
                goodsInfo.goodsName = $scope.productDetailData.goodsName;
                goodsInfo.imageUrlJson = $scope.productDetailData.imageUrlJson;
                goodsInfo.quantity = 1;
                goodsInfo.type = $scope.productDetailData.type;
                goodsInfo.isGift = "1";
                goodsInfo.giftActivityId = $scope.activity.activityId;
                $scope.activity.newPrice = $scope.activity.needFee;
                $scope.activity.originPrice = $scope.selectedSku.price;
                goodsInfo.activity = $scope.activity;
                var ordConfirmOrderUnitExtList = [];
                var sku = {};
                sku.colorName = $scope.selectedColorName;
                sku.sizeName = $scope.selectedSizeName;
                sku.goodsId = $scope.productDetailData.goodsId;
                sku.imageUrlJson = $scope.selectedSku.imgs[0];
                sku.price = $scope.selectedSku.price;
                sku.skuId = $scope.selectedSkuId;
                ordConfirmOrderUnitExtList.push(sku);
                goodsInfo.ordConfirmOrderUnitExtList = ordConfirmOrderUnitExtList;
                $modalInstance.close(goodsInfo);
            };
            $scope.close = function(){
                $modalInstance.close();
            };
        }])


    // 选择门店
    .controller('selectStoreCtrl', ["$scope", "$state", "$stateParams", '$modalInstance', 'storeService', 'popupService',
        function ($scope, $state, $stateParams, $modalInstance, storeService, popupService) {
            $scope.info = {
                areaCd:'0',
                cityCd:'0',
                provinceCd:'0'
            };
            $scope.getProvinces = function (){
                new storeService(null).getOrgAddressList()
                    .then(function (res) {
                        if (res.resultCode == '00') {
                            $scope.provincesList = res.data;
                        } else {
                            popupService.showToast(res.resultMessage);
                        }

                    }, function () {
                        popupService.showToast(commonMessage.networkErrorMsg);
                    });
            };

            // 联动区域变换
            $scope.changeArea = function (type, code) {
                // 0:已知省，1：已知市
                switch (type) {
                    case '0':
                        $scope.areasList = [];
                        $scope.info.cityCd = '0';
                        $scope.info.areaCd = '0';
                        break;
                    case '1':
                        $scope.info.areaCd = '0';
                        break;
                }

                var param = {
                    "provinceCode": type == '0' ? code : null,
                    "cityCode": type == '1' ? code : null
                };

                new storeService(param).getOrgAddressList()
                    .then(function (res) {
                        if (res.resultCode == "00") {
                            switch (type) {
                                case '0':
                                    $scope.citiesList = res.data;
                                    break;
                                case '1':
                                    $scope.areasList = res.data;
                                    break;
                            }
                        }
                    }, function (error) {
                        console.log(error);
                    });
                $scope.getStoreList();
            };

            $scope.getStoreList = function (){
                $scope.isLoading = true;
                $scope.orgList = [];
                var param = {
                    provinceCode:($scope.info.provinceCd == '0' ? null : $scope.info.provinceCd),
                    cityCode:($scope.info.cityCd == '0' ? null : $scope.info.cityCd),
                    districtCode:($scope.info.areaCd == '0' ? null :$scope.info.areaCd)
                };
                // 获取门店信息
                new storeService(param).getStoreList()
                    .then(function (res) {
                        $scope.isLoading = false;
                        if (res.resultCode == '00') {
                            $scope.orgList = res.data;
                        }
                    }, function (error) {
                        $scope.isLoading = false;
                    });
            };
            $scope.isLoading = false;
            $scope.getProvinces();
            $scope.getStoreList();
            $scope.confirm = function(selectedStore){
                console.log(selectedStore);
                var selectedStore = {
                    storeId:selectedStore.store_code,
                    storeAddress:selectedStore.address,
                    storeName:selectedStore.store_name_cn
                };
                $modalInstance.close(selectedStore);
            };
            $scope.close = function(){
                $modalInstance.close();
            };
        }])

    // personalHome
    .controller('personalHomeCtrl', ["$scope", 'localStorageService',"couponCountService", 'authService',
        'orderService', '$state', 'popupService',
        function ($scope, localStorageService,couponCountService, authService, orderService, $state,popupService) {

        var info = localStorageService.get(KEY_USERINFO);
        if(info){
            $scope.userInfo = info;
        }

        $scope.goShopping = function (){
            $state.go("homepage");
        };

        $scope.getPersonalInfo = function (){
            var param = {
                memberId: $scope.userInfo.memberId
            };

            new authService(param).getPersonalInfo()
                .then(function (res) {

                    if (res.resultCode == '00') {

                        localStorageService.set(KEY_USERINFO, res.data);

                        $scope.userInfo = res.data;

                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function (error) {
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
        };

        $scope.getCouponInfo = function(){
            // 获取优惠劵数量
            new couponCountService({"memberId":info.memberId})
                .then(function (res) {
                    if (res.resultCode == "00") {
                        $scope.couponCountData = res.results;
                        // $ionicSlideBoxDelegate.update();
                    } else {
                         popupService.showToast(res.resultMessage);
                    }
                }, function () {
                     popupService.showToast(commonMessage.networkErrorMsg);
                });
        };

        $scope.getData = function () {

            var param = {
                "memberId": $scope.userInfo.memberId,
                "iDisplayLength": 7,
                "iDisplayStart": 0,
                "orderStatus": '',
                "payStatus": '',
                "deliverStatus": '',
                "type": '1'
            };

            new orderService(param).getList()
                .then(function (res) {
                        if (res.resultCode == "00") {
                            $scope.orderList = res.aaData;
                        } else {
                            popupService.showToast(res.resultMessage);
                        }
                    }, function (error) {
                        //$scope.hasMore = false;
                        popupService.showToast(commonMessage.networkErrorMsg);
                    }
                );
        };

        // 跳转物流信息
        $scope.goLogisticsList = function (gdsId) {
            $state.go("personalCenter.logisticsList", {"orderId": gdsId});
        };

        //订单详情
        $scope.orderDetail = function(id){
            $state.go("personalCenter.orderDetail", {"orderId": id});
        };

        // 取消订单
        $scope.cancelOrder = function (orderId) {
            popupService.showConfirm('确定要取消此订单吗?', function(){
                var param = {
                    "memberId": $scope.userInfo.memberId,
                    "orderId": orderId
                };

                new orderService(param).cancelOrder()
                    .then(function (res) {
                        if (res.resultCode == "00") {
                            popupService.showToast("订单已取消。");
                            $scope.getData();
                        } else {
                            popupService.showToast(res.resultMessage);
                        }
                    }, function (error) {
                        popupService.showToast(commonMessage.networkErrorMsg);
                    });
            });
        };

        // 确认收货
        $scope.confirmReceiptInMaster = function (orderId) {
            popupService.showConfirm('确定已收到此订单中所有的商品了吗?', function(){
                var param = {
                    "memberId": $scope.userInfo.memberId,
                    "orderId": orderId
                };

                new orderService(param).confirmReceiptInMaster().then(function (res) {
                    if (res.resultCode == "00") {
                        popupService.showToast("订单已确认收货。");
                        $scope.getData();
                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function (error) {
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
            });
        };

        // 删除订单
        $scope.deleteOrder = function (orderId) {
            popupService.showConfirm('确定要删除此订单吗?', function(){
                var param = {
                    "memberId": $scope.userInfo.memberId,
                    "orderId": orderId
                };

                new orderService(param).deleteOrder().then(function (res) {
                    if (res.resultCode == "00") {
                        popupService.showToast("订单已删除。");
                        $scope.getData();
                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function (error) {
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
            });

        };

            // 支付
            $scope.doPay = function (orderId,orderCode) {
                var scope = $scope.$new();
                var selectPayModalInstance = $modal.open({
                    templateUrl: 'html/selectPay.html',
                    controller: 'selectPayCtrl',
                    backdrop: "static",
                    keyboard: false,
                    scope: scope
                });
                selectPayModalInstance.opened.then(function () {//模态窗口打开之后执行的函数

                });
                selectPayModalInstance.result.then(
                    function (result) {
                        if (result) {
                            if("10" == result.type){
                                //支付测试
                                $('#orderId').val(orderId);
                                $('#ordMaster')[0].action = '/qyds-web-pc/alipay/orderAliPay.json';
                                $('#ordMaster')[0].submit();
                            }else if("20" == result.type){
                                $scope.getWXPayInfo(orderCode);
                            }else if("30" == result.type){
                                $('#orderId').val(orderId);
                                $('#ordMaster')[0].action = '/qyds-web-pc/unionpay/orderUnionPay.json';
                                $('#ordMaster')[0].submit();
                            }
                        }
                    }, function (reason) {
                        //点击空白区域，总会输出backdrop click，点击取消，则会暑促cancel
                    });
            };

            $scope.getWXPayInfo = function (out_trade_no) {
                var params = {
                    "openid": "",
                    "product_id":out_trade_no,
                    "body": "商品订单"+out_trade_no,
                    "out_trade_no": out_trade_no,
                    "total_fee": (parseFloat($scope.orderDetail.payInfact)*100).toFixed(0),
                    "trade_type": "NATIVE"
                };

                new WeiXinService(params).getWxPayInfo().then(function (response) {
                    if (response.resultCode == '00') {
                        var codeUrl  = response.results.code_url;
                        var scope = $scope.$new();
                        scope.codeUrl = codeUrl;
                        var wxPayModalInstance = $modal.open({
                            templateUrl: 'html/wxPay.html',
                            controller: 'wxPayCtrl',
                            backdrop: "static",
                            keyboard: false,
                            scope: scope
                        });
                        wxPayModalInstance.opened.then(function () {//模态窗口打开之后执行的函数

                        });
                        wxPayModalInstance.result.then(
                            function (result) {
                                $scope.doRefresh();
                            }, function (reason) {
                                $scope.doRefresh();
                            });
                    } else {
                        popupService.showToast("支付失败,原因:" + response.resultMessage);
                        $scope.doRefresh();
                    }
                }, function (error) {
                    popupService.showToast("支付失败,可在订单列表中继续支付");
                    $scope.doRefresh();
                });
            };

        //订单详情
        $scope.orderDetail = function(id){
            $state.go("personalCenter.orderDetail", {"orderId": id});
        };


        // 退货（全单）
        $scope.refunds = function (orderItem) {
            localStorageService.set(KEY_PARAM_REFUND_ORDER,orderItem);
            $state.go("personalCenter.applyReturnGoods");
        };

        // 跳转到商品详细
        $scope.goGoodsDetail = function (gdsId) {
            $state.go("goodsDetail", {"goodsId": gdsId});
        };

        //我的订单
        $scope.goOrderList = function () {
            $state.go("personalCenter.orderList");
        };

        $scope.getPersonalInfo();
        $scope.getCouponInfo();
        $scope.getData();

    }])

    // 关于我们
    .controller('aboutCtrl', ["$scope", "$state", "$stateParams", function ($scope, $state, $stateParams) {
        var param = JSON.parse($stateParams.pageParam);
    }])

    // 杂志
    .controller('magazineCtrl', ["$scope", "$state", "$stateParams", "magazineListService", function ($scope, $state, $stateParams, magazineListService) {
        // 获取配置的杂志信息
        var param = {
            itemCode:'magazine_index',
            isChild:'1'
        };

        $scope.magazineListDataTypes = [];
        $scope.magazineListData = [];

        new magazineListService(param)
            .then(function (res) {
                if (res.resultCode == "00") {
                    $scope.magazineListDataTypes = res.results;
                    $scope.magazineListData = res.results[0].cmList;
                } else {
                    // popupService.showToast(res.resultMessage);
                }
            }, function (error) {
                // popupService.showToast(commonMessage.networkErrorMsg);
            });

        $scope.getOtherType = function(itemCode){
            for(var i = 0; i < $scope.magazineListDataTypes.length; i++ ){
                if($scope.magazineListDataTypes[i].itemCode == itemCode ){
                    $scope.magazineListData = $scope.magazineListDataTypes[i].cmList;
                    return;
                }
            }
        };

        $scope.goToTheStory = function(cmsId) {
            $state.go("theStory", {"cmsId": cmsId});
        }
    }])

    // 杂志背景故事
    .controller('theStoryCtrl', ["$scope", "$state", "$stateParams", "theStoryService","$sce", function ($scope, $state, $stateParams, theStoryService,$sce) {
        // 获取杂志背景故事
        new theStoryService({"cmsId":$stateParams.cmsId})
            .then(function (res) {
                if (res.resultCode == "00") {
                    $scope.magazineStoryData = res.results;
                    $scope.contentHtml = $sce.trustAsHtml($scope.magazineStoryData.contentHtml);
                } else {
                    // popupService.showToast(res.resultMessage);
                }
            }, function (error) {
                // popupService.showToast(commonMessage.networkErrorMsg);
            });

    }])

    //// 检索商品
    //.controller('searchGoodsCtrl', ["$scope", "$state","$stateParams", function ($scope, $state,$stateParams) {
    //    //alert($stateParams.searchKey);
    //    console.log("商品检索");
    //}])

    // 门店信息
    .controller('storeListCtrl', ["$scope", "$state", 'storeService', 'popupService',
        function ($scope, $state, storeService, popupService) {
        $scope.info = {provinceCd: '0', cityCd: '0', areaCd: '0'};//初始化联动下拉

        $scope.storeList = [];

        $scope.getData = function (){

            var param = {
                provinceCode:($scope.info.provinceCd == '0' ? null : $scope.info.provinceCd),
                cityCode:($scope.info.cityCd == '0' ? null : $scope.info.cityCd),
                districtCode:($scope.info.areaCd == '0' ? null :$scope.info.areaCd)
            };

            new storeService(param).getStoreList()
                .then(function (res) {
                    $scope.storeList = res.data;
                    console.log("$scope.storeList === "+$scope.storeList);
                }, function (error) {
                    console.log(error);
                });
        };

        $scope.getProvinces = function (){
            new storeService(null).getOrgAddressList()
                .then(function (res) {
                    if (res.resultCode == '00') {
                        $scope.provincesList = res.data;
                    } else {
                        popupService.showToast(res.resultMessage);
                    }

                }, function () {
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
        };


        // 联动区域变换
        $scope.changeArea = function (type, code) {
            var param = {
                "provinceCode": type == '0' ? code : null,
                "cityCode": type == '1' ? code : null
            };

            switch (type) {
                case '0':
                    $scope.areasList = [];
                    $scope.info.cityCd = '0';
                    $scope.info.areaCd = '0';
                    break;
                case '1':
                    $scope.info.areaCd = '0';
                    break;
            }

            $scope.getData();

            new storeService(param).getOrgAddressList()
                .then(function (res) {
                    if (res.resultCode == "00") {
                        switch (type) {
                            case '0':
                                $scope.citiesList = res.data;
                                break;
                            case '1':
                                $scope.areasList = res.data;
                                break;
                        }
                    }
                }, function (error) {
                    console.log(error);
                });
        };

        $scope.getData();
        $scope.getProvinces();
    }])

    // 联系我们
    .controller('contactUsCtrl', ["$scope", "$state", 'contactUsService','popupService', function ($scope, $state, contactUsService,popupService) {

        $scope.contactUsInfo = {topic:'客户服务',name:'',mail:'',phone:'',comment:''};
        $scope.topicList = ["客户服务", "新闻媒体",'投资者', "其他"];
        $scope.save = function (isValid) {

            $scope.submitted = true;

            if (!isValid) {
                return;
            }

            var param = {
                "theme": $scope.contactUsInfo.topic,
                "address": $scope.contactUsInfo.mail,
                "userName": $scope.contactUsInfo.name,
                "telephone": $scope.contactUsInfo.phone,
                "comment": $scope.contactUsInfo.comment
            };

            new contactUsService(param).upInfo()
                .then(function (res) {
                    if (res.resultCode == "00") {
                        popupService.showToast('提交成功');
                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function () {
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
        }
    }])

    // 秒杀测试
    .controller('secKillCtrl', ["$scope","$rootScope", "$state", 'popupService','secKillImageService','$interval', 'secKillService', 'localStorageService',
        function ($scope,$rootScope, $state,popupService,secKillImageService,$interval, secKillService, localStorageService) {
        $scope.clickIndex = 0;

        $scope.times = [];

        $scope.goodsList = [];
        //当前时间
        $scope.nowTime ;
        $scope.timer;
        $scope.currentPage = 0;
        $scope.currentPagePhone = 0;
        $scope.totalPage = 0;
        $scope.pageSize = 4;

        $scope.switchTm = function(index, time){
            $scope.currentPage = 0;
            $scope.currentPagePhone = 0;
            $scope.totalPage = 0;
            $scope.clickIndex = index;
            $scope.goodsList = [];
            $interval.cancel($scope.timer);
            $scope.getAllTimes();
        };


        // 海报区域
        new secKillImageService({})
            .then(function (res) {
                if (res.resultCode == "00") {
                    if(res.results != null && res.results.length > 0){
                        $scope.activityPoints = res.results[0];
                        if($scope.activityPoints.cmList != null){
                            $scope.playbill = $scope.activityPoints.cmList[0];
                        }
                    }
                } else {
                    // popupService.showToast(res.resultMessage);
                }
            }, function (error) {
                // popupService.showToast(commonMessage.networkErrorMsg);
            });

        $scope.getAllTimes = function (){

            var userInfo = localStorageService.get(KEY_USERINFO);
            var memberId = "";
            if (userInfo) {
                memberId = userInfo.memberId;
            }

            var param = {
                "memberId": memberId
            };

            new secKillService(param).getTimes().then(
                function (res) {
                    if (res.resultCode == "00") {
                        $scope.times = res.data;
                        $interval.cancel($scope.timer);
                        var currentTime = parseInt($scope.times[$scope.clickIndex].currentTime);
                        var started = $scope.times[$scope.clickIndex].started;
                        var endTime = parseInt($scope.times[$scope.clickIndex].endTimeSec);
                        var startTime = parseInt($scope.times[$scope.clickIndex].startTimeSec);
                        $scope.nowTime = currentTime;
                        if(started == '0'){
                            // 结束时间减去nowTime + 1000/s
                            var diffTime = $scope.diffTime(endTime,currentTime);
                            console.log(diffTime);
                            $scope.activityTime = '抢购中 先下单先得哦！距结束:'+diffTime;
                            // 1s钟加1000
                            $scope.timer = $interval(function(){
                                var rediffTime = $scope.reDiffTime(endTime,$scope.nowTime);
                                $scope.activityTime = '抢购中 先下单先得哦！距结束:'+rediffTime;
                                console.log(rediffTime);
                            },1000);
                        }else{
                            // 结束时间减去nowTime + 1000/s
                            var diffTime = $scope.diffTime(startTime,currentTime);
                            console.log(diffTime);
                            $scope.activityTime = '即将开始 先下单先得哦！距开始:'+diffTime;
                            // 1s钟加1000
                            $scope.timer = $interval(function(){
                                var rediffTime = $scope.reDiffTime(startTime,$scope.nowTime);
                                $scope.activityTime = '即将开始 先下单先得哦！距开始:'+rediffTime;
                                console.log(rediffTime);
                            },1000);
                        }

                        if($scope.times && $scope.times.length > 0){
                            $scope.getGoods($scope.times[$scope.clickIndex]);
                        } else {
                            $scope.goodsList = [];
                        }
                    }
                }, function () {
                    popupService.showToast(commonMessage.networkErrorMsg);
                }
            );
        };

            $scope.diffTime = function(endTime,nowTime){

                var diff=endTime-nowTime;//时间差的毫秒数

                //计算出相差天数
                var days=Math.floor(diff/(24*3600*1000));

                //计算出小时数
                var leave1=diff%(24*3600*1000);    //计算天数后剩余的毫秒数
                var hours=Math.floor(leave1/(3600*1000));
                //计算相差分钟数
                var leave2=leave1%(3600*1000);        //计算小时数后剩余的毫秒数
                var minutes=Math.floor(leave2/(60*1000));

                //计算相差秒数
                var leave3=leave2%(60*1000);      //计算分钟数后剩余的毫秒数
                var seconds=Math.round(leave3/1000);

                var returnStr = seconds + "秒";
                if(minutes>0) {
                    returnStr = minutes + "分" + returnStr;
                }
                if(hours>0) {
                    returnStr = hours + "小时" + returnStr;
                }
                if(days>0) {
                    returnStr = days + "天" + returnStr;
                }
                return returnStr;
            };

            $scope.reDiffTime = function(endTime,nowTime){
                $scope.nowTime = nowTime + 1000;
                //如果当前时间和下一个的开始时间一致要刷新
                var startTime;
                if($scope.times[1]){
                    startTime = parseInt($scope.times[1].startTimeSec);
                    if($scope.nowTime > startTime){
                        $scope.getAllTimes();
                    }
                }

                var rediffTime = $scope.diffTime(endTime , nowTime);
                return rediffTime;
            };

            //$scope.$on('$destroy',function(){
            //    $interval.cancel($scope.timer);
            //});

            $.event.add(window, "scroll", function() {
                var pTop = $(window).scrollTop();
                var height = getClientHeight();
                var theight = getScrollTop();
                var rheight = getScrollHeight();
                var pBottom = rheight-theight-height;

                if(pBottom < 130){
                    if(!$rootScope.isLoadShowing){
                        //finished = false;
                        if($scope.currentPage < $scope.totalPage){
                            console.log($scope.currentPage);
                            $scope.getGoods($scope.times[$scope.clickIndex]);
                        }
                    }
                }
            });

            $scope.$on('$destroy',function(){
                $.event.remove(window, "scroll");
            });

            $scope.isLoading = false;
            $scope.getGoods = function (time){
            var userInfo = localStorageService.get(KEY_USERINFO);
            var memberId = "";
            if (userInfo) {
                memberId = userInfo.memberId;
            }

            var param = {
                "memberId": memberId,
                "activityTime": time.startTime,
                "page":$scope.currentPage,
                "displayLength":$scope.pageSize
            };

            if($scope.isLoading){
                return;
            }
            $scope.isLoading = true;

            new secKillService(param).getProducts().then(
                function (res) {
                    if (res.resultCode == "00") {
                        $scope.isLoading = false;
                        $scope.goodsList = $scope.goodsList.concat(res.data);
                        $scope.totalPage = res.iTotalRecords;
                        $scope.currentPage++;

                        angular.forEach($scope.goodsList, function (item) {
                            item.actInfo.sold = item.actInfo.quantity-item.actInfo.surplus;
                            if(item.actInfo.sold == 0){
                                item.actInfo.ratio = 0;
                            }else{
                                item.actInfo.ratio = ((item.actInfo.quantity-item.actInfo.surplus)/item.actInfo.quantity) + 0.1;
                            }

                        });

                    }
                }, function () {
                    $scope.isLoading = false;
                    popupService.showToast(commonMessage.networkErrorMsg);
                }
            );
        };

        $scope.getProgressStyle = function (radio) {
            return {"width":radio * 100 +"%"};
        };

        //商品详细画面跳转
        $scope.goProdocutDetail = function (goodsId) {
            $scope.getAllTimes();
            var url = '/qyds-web-pc/qyds/#/goodsDetail/' + goodsId;
            window.open(url);
            //$state.go("goodsDetail", {"goodsId": goodsId});
        };

        $scope.getAllTimes();

    }])

    .controller('popClassifyCtrl', ["$scope", "$state", "$stateParams", "$modalInstance","typePhoneService","brandtypeService", "localStorageService", "popupService",
        function ($scope, $state, $stateParams, $modalInstance,typePhoneService,brandtypeService,localStorageService, popupService) {

        // var data = $scope.param;
        // var goodsTypeId = data.goodsTypeId;
        // var actId = data.actId;
        // var firstGoodsTypeId = data.firstGoodsTypeId;
        // var currentPage = data.currentPage;
        // var totalPage = data.totalPage;
        // var pageSize = data.pageSize;
        var firstGoodsTypeId = $scope.param.firstGoodsTypeId;
        // 头部信息取得
        new typePhoneService({})
            .then(function (res) {

                if (res.resultCode == "00") {
                    $scope.levelRootList = res.results;
                    new brandtypeService({})
                        .then(function (res) {
                            if (res.resultCode == "00") {
                                $scope.levelRootList = $scope.levelRootList.concat(res.results);
                                $scope.levelSecondList = [];
                                $scope.levelThirdList = [];
                                if(firstGoodsTypeId != null && firstGoodsTypeId.length > 0){
                                    $scope.currentRootCode = firstGoodsTypeId;
                                    $scope.selectClassify('1',firstGoodsTypeId);
                                }else{
                                    $scope.currentRootCode = '';
                                }
                                $scope.currentSecondCode = '';
                                $scope.currentThirdCode = '';
                            } else {
                                popupService.showToast(res.resultMessage);
                            }
                        }, function () {
                            popupService.showToast(commonMessage.networkErrorMsg);
                        });
                } else {
                     popupService.showToast(res.resultMessage);
                }
            }, function () {
                 popupService.showToast(commonMessage.networkErrorMsg);

            });

        // $scope.doCallServicePhone = function () {
        //     //初始化调用
        //     $scope.goodsListPhone = [];
        //     new goodsListService({
        //         "goodsTypeId": goodsTypeId,
        //         "actId": actId,
        //         "firstGoodsTypeId": firstGoodsTypeId,
        //         "currentPage": currentPage,
        //         "totalPage": totalPage,
        //         "pageSize": pageSize
        //     }).then(function (res) {
        //         console.log(res);
        //         if (res.resultCode == "00") {
        //             if (res.results.length != 0) {
        //                 $scope.goodsListPhone = $scope.goodsListPhone.concat(res.results);
        //             } else {
        //                 $scope.currentPagePhone = $scope.currentPagePhone - 1;
        //                 //popupService.showToast("没有更多数据");
        //             }
        //             $scope.totalPage = res.allCount;
        //             $scope.$apply();
        //         } else {
        //             //popupService.showToast(res.resultMessage);
        //         }
        //     }, function (err) {
        //         //popupService.showToast(commonMessage.networkErrorMsg);
        //     });
        // };

        $scope.confirm = function () {
            //TODO
            localStorageService.set(KEY_GOODSTYPE, firstGoodsTypeId);
            $modalInstance.close();
            // $scope.doCallServicePhone();
        };

        $scope.selectClassify = function (level, code) {

            if (level == '1') {
                $scope.currentRootCode = code;
                angular.forEach($scope.levelRootList, function (data) {
                    if (data.goodsTypeId == code) {
                        $scope.levelSecondList = data.secondTypeList;
                        $scope.levelThirdList = [];
                        $scope.currentSecondCode = '';
                        $scope.currentThirdCode = '';
                        firstGoodsTypeId = code;
                        // $scope.doCallService();
                    }
                });
                // TODO
            } else if (level == '2') {
                $scope.currentSecondCode = code;
                angular.forEach($scope.levelSecondList, function (data) {
                    if (data.goodsTypeId == code) {
                        $scope.levelThirdList = data.secondTypeList;
                        $scope.currentThirdCode = '';
                        firstGoodsTypeId = $scope.currentRootCode + "_" + code;
                        $scope.doCallService();
                    }
                });
                // TODO
            } else if (level == '3') {
                $scope.currentThirdCode = code;
                // TODO
                firstGoodsTypeId = $scope.currentRootCode + "_" + $scope.currentSecondCode + "_" + code;
                $scope.doCallService();

            }

            return false;
        };
    }])
    // 积分兑换商品画面
    .controller('pointsExchangePageCtrl', ["$scope", "$state", "$stateParams","$rootScope", "$modal","localStorageService","popupService",
        "activityListService","goodsListService","activityPointsService",
        function ($scope, $state, $stateParams, $rootScope, $modal,localStorageService,popupService,activityListService,goodsListService,activityPointsService) {

            var actId = "";
            //会员ID
            var userInfo = localStorageService.get(KEY_USERINFO);
            var memberId = "";
            if (userInfo) {
                memberId = userInfo.memberId;
            }

            //PC 初始化的场合
            $scope.currentPage = 1;
            $scope.currentPagePhone = 1;
            $scope.totalPage = 0;
            $scope.pageSize = 8;
            $scope.goodsList = [];
            $scope.goodsListPhone = [];
            $scope.phoneAndPC = $('#phone').css('display');
            $scope.info = {activityId: '', activityName: ''};//初始化联动下拉

            // 活动列表积分兑换的所有活动列表
            new activityListService({"memberId":memberId})
                .then(function (res) {
                    if (res.resultCode == "00") {
                        $scope.activityList = res.data;

                        //默认选择第一个
                        if($scope.activityList != null && $scope.activityList.length>0){
                            $scope.getDataByActId($scope.activityList[0].activityId,$scope.activityList[0].activityName)
                        }
                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function () {
                    popupService.showToast(commonMessage.networkErrorMsg);
                });

            // 海报区域
            new activityPointsService({})
                .then(function (res) {
                    if (res.resultCode == "00") {
                        if(res.results != null && res.results.length > 0){
                            $scope.activityPoints = res.results[0];
                            if($scope.activityPoints.cmList != null){
                                $scope.playbill = $scope.activityPoints.cmList[0];
                            }
                        }
                    } else {
                        // popupService.showToast(res.resultMessage);
                    }
                }, function (error) {
                    // popupService.showToast(commonMessage.networkErrorMsg);
                });

            $scope.getDataByActId = function (activityId,activityName) {

                $scope.goodListTitle={selectSize:''};
                $scope.goodsListInfo = {"from": "", "to": ""};
                $scope.sortByTimeValue = 0;
                $scope.sortByPriceValue = 0;

                // 清空检索结果
                $scope.currentPage = 1;
                $scope.currentPagePhone = 1;
                $scope.totalPage = 0;
                $scope.goodsList = [];

                actId = activityId;
                $scope.info.activityId = activityId;
                if($scope.phoneAndPC == 'none'){
                    $scope.doCallService();
                }else{
                    $scope.doCallServicePhone();
                }
            };

            //加载更多
            $scope.doGetMore = function () {
                $scope.isPageChange = true;
                $scope.currentPagePhone = $scope.currentPagePhone + 1;
                $scope.doCallServicePhone();
            };

            $scope.changeActivity = function(activityId){
                $scope.goodListTitle={selectSize:''};
                $scope.goodsListInfo = {"from": "", "to": ""};
                $scope.sortByTimeValue = 0;
                $scope.sortByPriceValue = 0;

                // 清空检索结果
                $scope.currentPage = 1;
                $scope.currentPagePhone = 1;
                $scope.totalPage = 0;
                $scope.goodsList = [];

                actId = activityId;
                $scope.doCallServicePhone();
            };

            $.event.add(window, "scroll", function() {
                var pTop = $(window).scrollTop();
                var height = getClientHeight();
                var theight = getScrollTop();
                var rheight = getScrollHeight();
                var pBottom = rheight-theight-height;

                if(pTop > 130){// 固定
                    if($('.goodListTitle').length > 0){
                        $('.goodListTitle').css({'position':'fixed','top':'0px',left:'0px','zIndex':100});
                        $('.goodListTitle').addClass('fixTop');
                    }
                }else{// 恢复
                    if($('.goodListTitle').length > 0) {
                        $('.goodListTitle').css({'position': 'static', 'top': '', 'left': '', 'zIndex': 0});
                        $('.goodListTitle').removeClass('fixTop');
                    }
                }

                if(pBottom < 130){
                    if(!$rootScope.isLoadShowing){
                        //finished = false;
                        if($scope.currentPage <= $scope.totalPage){
                            console.log($scope.currentPage);
                            $scope.doCallService();
                        }
                    }
                }
            });

            $scope.$on('$destroy',function(){
                $.event.remove(window, "scroll");
            });

            $scope.goPointsCouppon = function(){
                //跳转积分兑换优惠劵画面
                $state.go("pointsCoupponPage");
            };

            // 返回顶部
            $scope.goTop = function(){
                $('body,html').animate({scrollTop:0},500);
                return false;
            }
            $scope.openFilterPanel = function(){
                if($('.webui-popover').length > 0){
                    $('.webui-popover').remove();
                }else{
                }
                WebuiPopovers.updateContent($('.filterBtn'),$('.filterTemplate').html());
                setTimeout(function(){

                    $('.webui-popover-content .filterPanel #priceFrom').val($scope.goodsListInfo.from);
                    $('.webui-popover-content .filterPanel #priceTo').val($scope.goodsListInfo.to);

                    $('.webui-popover-content .filterPanel .sizeTag').off('click').on('click',function(){
                        $(this).addClass('active').siblings().removeClass('active');
                        $scope.goodListTitle.selectSize = $(this).attr('data-code');
                    });

                    $('.webui-popover-content .filterPanel #priceFrom,.webui-popover-content .filterPanel #priceTo').off('change').on('change',function(){
                        var val = $(this).val();
                        if(val != '' && isNaN(val)){
                            $(this).val('');
                            return;
                        }

                        var minV = $('.webui-popover-content .filterPanel #priceFrom').val();
                        var maxV = $('.webui-popover-content .filterPanel #priceTo').val();

                        if($(this).hasClass('min') && !isNaN(maxV )){
                            if(parseFloat(minV) > parseFloat(maxV)){
                                $(this).val('');
                                return;
                            }
                        }
                        if($(this).hasClass('max') && !isNaN(minV)){
                            if(parseFloat(minV) > parseFloat(maxV)){
                                $(this).val('');
                                return;
                            }
                        }

                    });
                    $('.webui-popover-content .filterPanel #clearBtn').off('click').on('click',function(){
                        $('.webui-popover-content .filterPanel .sizeTag').removeClass('active');
                        $('.webui-popover-content .filterPanel #priceFrom').val('');
                        $('.webui-popover-content .filterPanel #priceTo').val('')
                        $scope.goodListTitle.selectSize = '';
                        $scope.goodsListInfo.from = '';
                        $scope.goodsListInfo.to = '';
                    });
                    $('.webui-popover-content .filterPanel #okBtn').off('click').on('click',function(){

                        console.log($('.webui-popover-content .filterPanel #priceFrom').length);

                        $scope.goodsListInfo.from = $('.webui-popover-content .filterPanel #priceFrom').val();
                        $scope.goodsListInfo.to = $('.webui-popover-content .filterPanel #priceTo').val();
                        console.log($scope.goodsListInfo.from);
                        console.log($scope.goodsListInfo.to);
                        $scope.currentPage = 1;
                        $scope.currentPagePhone = 1;
                        $scope.totalPage = 0;
                        $scope.goodsList = [];
                        $scope.doCallService();
                        WebuiPopovers.hideAll();
                        $scope.goTop();
                    })
                },0);

            }
            //排序 按照价格
            //变量forphone
            $scope.isupSortprice = true;
            $scope.sortByPrice = function(){
                var $target = $('.goodListTitle .sort.price');
                if($target.hasClass('down')){
                    //$target.removeClass('down').addClass('up');
                    $scope.sortByPriceValue = 2;
                }else{
                    //$target.removeClass('up').addClass('down');
                    $scope.sortByPriceValue = 1;
                }
                $scope.sortByTimeValue = 0;
                $scope.totalPage = 0;
                if($scope.phoneAndPC == 'none'){
                    $scope.currentPage = 1;
                    $scope.goodsList = [];
                    $scope.goodsListPhone = [];
                    $scope.doCallService();

                }else{
                    $scope.currentPagePhone = 1;
                    $scope.goodsList = [];
                    $scope.goodsListPhone = [];
                    if($scope.isupSortprice){
                        $scope.sortByPriceValue = 2;
                    }else{
                        $scope.sortByPriceValue = 1;
                    }
                    $scope.isupSortprice = !$scope.isupSortprice;
                    $scope.doCallServicePhone();
                }

            };
            //排序 按照时间
            //变量forphone
            $scope.isupSort = true;
            $scope.sortByTime = function(){
                var $target = $('.goodListTitle .sort.time');
                if($target.hasClass('down')){
                    //$target.removeClass('down').addClass('up');
                    $scope.sortByTimeValue = 2;
                }else{
                    //$target.removeClass('up').addClass('down');
                    $scope.sortByTimeValue = 1;
                }
                $scope.sortByPriceValue = 0;
                $scope.totalPage = 0;
                if($scope.phoneAndPC == 'none'){
                    $scope.currentPage = 1;
                    $scope.goodsList = [];
                    $scope.goodsListPhone = [];
                    $scope.doCallService();
                }else{
                    $scope.goodsList = [];
                    $scope.goodsListPhone = [];
                    $scope.currentPagePhone = 1;
                    if($scope.isupSort){
                        $scope.sortByTimeValue = 2;
                    }else{
                        $scope.sortByTimeValue = 1;
                    }
                    $scope.isupSort = !$scope.isupSort;
                    $scope.doCallServicePhone();
                }
            };

            $scope.doCallService = function (cb) {
                if($scope.isLoading){
                    return;
                }
                $scope.isLoading = true;
                //初始化调用
                new goodsListService({
                    "sizeCode":$scope.goodListTitle.selectSize,
                    "from": $scope.goodsListInfo.from,
                    "to": $scope.goodsListInfo.to,
                    "actId": actId,
                    "memberId":memberId,
                    "sortByPrice":$scope.sortByPriceValue,
                    "sortByTime":$scope.sortByTimeValue,
                    "currentPage": $scope.currentPage,
                    "totalPage": $scope.totalPage,
                    "pageSize": $scope.pageSize
                }).then(function (res) {
                    $scope.isLoading = false;
                    if (res.resultCode == "00") {
                        $scope.goodsList = $scope.goodsList.concat(res.results);
                        $scope.totalPage = res.allCount;
                        //$scope.dochangePage();
                        $scope.currentPage++;
                        angular.element(document).ready(function() {
                            // 筛选
                            $('.filterBtn').webuiPopover({
                                width:450,
                                height:260,
                                padding:false,
                                animation:'pop',
                                content:function(){
                                    var html = $('.filterTemplate').html();

                                    return html;
                                }
                            });
                        });
                        $scope.sizeList = res.sizeList;

                        if(cb) cb(true);
                        //console.log($('.goodListTitle .sort.price'));
                    } else {
                        popupService.showToast(res.resultMessage);
                        if(cb) cb(true);
                    }
                }, function (err) {
                    $scope.isLoading = true;
                    popupService.showToast(commonMessage.networkErrorMsg);
                    if(cb) cb(true);
                });
            };

            //商品详细画面跳转
            $scope.goProdocutDetail = function (goodsId) {
                var url = '/qyds-web-pc/qyds/#/goodsDetail/' + goodsId;
                window.open(url);
                //$state.go("goodsDetail", {"goodsId": goodsId});
            };

            $scope.doCallServicePhone = function () {
                if($scope.isPhoneLoading){
                    return;
                }
                $scope.isPhoneLoading = true;

                //初始化调用
                new goodsListService({
                    "actId": actId,
                    "memberId":memberId,
                    "sortByPrice":$scope.sortByPriceValue,
                    "sortByTime":$scope.sortByTimeValue,
                    "currentPage": $scope.currentPagePhone,
                    "totalPage": $scope.totalPage,
                    "pageSize": $scope.pageSize
                }).then(function (res) {
                    $scope.isPhoneLoading = false;
                    if (res.resultCode == "00") {
                        if (res.results.length != 0) {
                            if($scope.isPageChange){
                                $scope.goodsListPhone = $scope.goodsListPhone.concat(res.results);
                            }else{
                                $scope.goodsListPhone = res.results;
                            }
                        } else {
                            $scope.currentPagePhone = $scope.currentPagePhone - 1;
                            //popupService.showToast("没有更多数据");
                        }
                        $scope.isPageChange = false;
                        $scope.totalPage = res.allCount;
                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function (err) {
                    $scope.isPhoneLoading = true;
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
            };
        }])


    // 积分兑换商品画面
    .controller('pointsExchangePageNewCtrl', ["$scope", "$state", "$stateParams","$rootScope", "$modal","localStorageService","popupService",
        "activityListService","goodsListService","activityPointsService",
        function ($scope, $state, $stateParams, $rootScope, $modal,localStorageService,popupService,activityListService,goodsListService,activityPointsService) {


            var actId = "";
            //会员ID
            var userInfo = localStorageService.get(KEY_USERINFO);
            var memberId = "";
            if (userInfo) {
                memberId = userInfo.memberId;
            }
            //
            ////PC 初始化的场合
            //$scope.currentPage = 1;
            //$scope.currentPagePhone = 1;
            //$scope.totalPage = 0;
            //$scope.pageSize = 8;
            $scope.goodsList = [];
            $scope.goodsListPhone = [];
            $scope.phoneAndPC = $('#phone').css('display');
            $scope.info = {activityId: '', activityName: ''};//初始化联动下拉
            $scope.activityIdList = [];
            // 活动列表积分兑换的所有活动列表
            new activityListService({"memberId":memberId})
                .then(function (res) {
                    if (res.resultCode == "00") {
                        $scope.activityList = res.data;

                        //默认选择第一个
                        if($scope.activityList != null && $scope.activityList.length>0){

                            for(var i = 0; i < $scope.activityList.length; i++){
                                $scope.activityIdList.push($scope.activityList[i].activityName);
                                $scope.getDataByActId($scope.activityList[i].activityId,$scope.activityList[i].activityName);
                            }


                        }
                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function () {
                    popupService.showToast(commonMessage.networkErrorMsg);
                });

            // 海报区域
            new activityPointsService({})
                .then(function (res) {
                    if (res.resultCode == "00") {
                        if(res.results != null && res.results.length > 0){
                            $scope.activityPoints = res.results[0];
                            if($scope.activityPoints.cmList != null){
                                $scope.playbill = $scope.activityPoints.cmList[0];
                                $scope.playbilltop = $scope.activityPoints.cmList[1];
                            }
                        }
                    } else {
                        // popupService.showToast(res.resultMessage);
                    }
                }, function (error) {
                    // popupService.showToast(commonMessage.networkErrorMsg);
                });

            $scope.getDataByActId = function (activityId,activityName) {

                $scope.goodListTitle={selectSize:''};
                $scope.goodsListInfo = {"from": "", "to": ""};
                $scope.sortByTimeValue = 0;
                $scope.sortByPriceValue = 0;

                // 清空检索结果
                $scope.currentPage = 1;
                $scope.currentPagePhone = 1;
                $scope.totalPage = 0;

                actId = activityId;
                $scope.info.activityId = activityId;
                if($scope.phoneAndPC == 'none'){
                    $scope.doCallService();
                }else{
                    $scope.doCallServicePhone();
                }
            };

            $scope.goPointsCouppon = function(){
                //跳转积分兑换优惠劵画面
                $state.go("pointsCoupponPage");
            };

            $scope.doCallService = function (cb) {
                $scope.isLoading = true;
                //初始化调用
                new goodsListService({
                    "sizeCode":$scope.goodListTitle.selectSize,
                    "from": $scope.goodsListInfo.from,
                    "to": $scope.goodsListInfo.to,
                    "actId": actId,
                    "memberId":memberId,
                    "sortByPrice":$scope.sortByPriceValue,
                    "sortByTime":$scope.sortByTimeValue,
                    "currentPage": $scope.currentPage,
                    "totalPage": $scope.totalPage,
                    "pageSize": $scope.pageSize
                }).then(function (res) {
                    $scope.isLoading = false;
                    if (res.resultCode == "00") {
                        $scope.goodsList = $scope.goodsList.concat(res.results);
                        $scope.totalPage = res.allCount;
                        $scope.currentPage++;
                        //重组前台显示的顺序
                        $scope.goodsListNew = [];
                        for(var i = 0; i<$scope.activityIdList.length; i++){
                            var activityName = $scope.activityIdList[i];
                            for(var j=0; j<$scope.goodsList.length; j++){
                                if(activityName == $scope.goodsList[j].activityName){
                                    $scope.goodsListNew = $scope.goodsListNew.concat($scope.goodsList[j]);
                                    break;
                                }
                            }
                        }
                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function (err) {
                    $scope.isLoading = true;
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
            };

            //商品详细画面跳转
            $scope.goProdocutDetail = function (goodsId) {
                var url = '/qyds-web-pc/qyds/#/goodsDetail/' + goodsId;
                window.open(url);
                //$state.go("goodsDetail", {"goodsId": goodsId});
            };

            $scope.doCallServicePhone = function () {

                $scope.isPhoneLoading = true;
                //初始化调用
                new goodsListService({
                    "actId": actId,
                    "memberId":memberId,
                    "sortByPrice":$scope.sortByPriceValue,
                    "sortByTime":$scope.sortByTimeValue,
                    "currentPage": $scope.currentPagePhone,
                    "totalPage": $scope.totalPage,
                    "pageSize": $scope.pageSize
                }).then(function (res) {
                    $scope.isPhoneLoading = false;
                    if (res.resultCode == "00") {
                        if (res.results.length != 0) {
                            $scope.goodsListPhone = $scope.goodsListPhone.concat(res.results);

                            //重组前台显示的顺序
                            $scope.goodsListPhoneNew = [];
                            for(var i = 0; i<$scope.activityIdList.length; i++){
                                var activityName = $scope.activityIdList[i];
                                for(var j=0; j<$scope.goodsListPhone.length; j++){
                                    if(activityName == $scope.goodsListPhone[j].activityName){
                                        $scope.goodsListPhoneNew = $scope.goodsListPhoneNew.concat($scope.goodsListPhone[j]);
                                        break;
                                    }
                                }
                            }

                        } else {
                            $scope.currentPagePhone = $scope.currentPagePhone - 1;
                            //popupService.showToast("没有更多数据");
                        }
                        $scope.isPageChange = false;
                        $scope.totalPage = res.allCount;
                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function (err) {
                    $scope.isPhoneLoading = true;
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
            };
        }])


    // 积分兑换优惠劵
    .controller('pointsCoupponPageCtrl', ["$scope","$state","couponService","couponPointsService","currentPointsService","localStorageService",'popupService','pointExchangeService',
        function ($scope,$state,couponService,couponPointsService,currentPointsService,localStorageService, popupService,pointExchangeService) {
            var userInfo = localStorageService.get(KEY_USERINFO);
            $scope.couponList = [];
            $scope.point = userInfo.point;

            // 海报区域
            new couponPointsService({})
                .then(function (res) {
                    if (res.resultCode == "00") {
                        if(res.results != null && res.results.length > 0){
                            $scope.activityPoints = res.results[0];
                            if($scope.activityPoints.cmList != null){
                                $scope.playbill = $scope.activityPoints.cmList[0];
                            }
                        }
                    } else {
                        // popupService.showToast(res.resultMessage);
                    }
                }, function (error) {
                    // popupService.showToast(commonMessage.networkErrorMsg);
                });

            $scope.doRefreshCoupon = function () {
                $scope.couponList = [];
                $scope.getData();
            };

            $scope.getData = function () {
                var param = {
                    "memberId": userInfo == null ? "" : userInfo.memberId
                };
                new pointExchangeService(param)
                    .getPointExchangeCoupons()
                    .then(function (res) {
                        if (res.resultCode == "00") {
                            $scope.couponList = $scope.couponList.concat(res.results);
                            console.log($scope.couponList);
                        } else {
                            popupService.showToast(res.resultMessage);
                        }
                    }, function (error) {
                        popupService.showToast(commonMessage.networkErrorMsg);
                    });
            };

            $scope.getCoupon = function (couponId) {
                var param = {"memberId": userInfo.memberId, "couponId": couponId};
                new pointExchangeService(param)
                    .getCoupon()
                    .then(function (res) {
                        if (res.resultCode == "00") {
                            popupService.showToast("兑换成功!");
                            $scope.doRefreshCoupon();
                            //更新积分
                            $scope.getCurrentPoints();
                        } else {
                            popupService.showToast(res.resultMessage);
                        }
                    }, function (error) {
                        popupService.showToast(commonMessage.networkErrorMsg);
                    });

            };

            $scope.goPointsGoods = function (){
                //跳转新的活动画面
                $state.go("pointsExchangePageNew");
            };

            $scope.getCurrentPoints = function () {
                var param = {
                    "memberId": userInfo == null ? "" : userInfo.memberId
                };
                new currentPointsService(param)
                    .then(function (res) {
                        if (res.resultCode == "00") {
                            if(res.data != null){
                                $scope.point = res.data.point;
                                localStorageService.set(KEY_USERINFO, res.data);
                                console.log(res.data.point);
                            }
                        } else {
                             popupService.showToast(res.resultMessage);
                        }
                    }, function (error) {
                        popupService.showToast(commonMessage.networkErrorMsg);
                    });
            };
//更新积分
            $scope.getCurrentPoints();
            $scope.doRefreshCoupon();
        }])
    // 商品列表
    .controller('goodsListCtrl', ["$scope", "$state", "$stateParams", "goodsTypeIndexService", "goodsListService", "activityTypeListService","getNewDataService","$rootScope", "$modal", "typeService","brandtypeService","localStorageService","popupService",
        function ($scope, $state, $stateParams, goodsTypeIndexService, goodsListService,activityTypeListService,getNewDataService, $rootScope, $modal, typeService,brandtypeService,localStorageService,popupService) {

            //数据集合
            $scope.goodsListClass = "col-xs-12 col-sm-12";
            $scope.goodsTypeAllGoodsListData = [];
            $scope.goodsList = [];
            $scope.goodsListPhone = [];
            $scope.goodsListFromnType = $stateParams.type;
            // 排序初始化
            $scope.sortByPriceValue = 0;
            $scope.sortByTimeValue = 0;
            //PC 初始化的场合
            $scope.currentPage = 1;
            $scope.currentPagePhone = 1;
            $scope.totalPage = 0;
            $scope.pageSize = 8;
            $scope.isPageChange = false;
            $scope.phoneAndPC = $('#phone').css('display');
            $scope.isLoading = false;
            $scope.isPhoneLoading = false;
            //是否第一次进入到首页
            $scope.isFirstInMain = 0;

            var firstGoodsTypeId = "";
            var pageType = "";
            var classifyId = "";
            var classifyName = "";
            var goodsTypeId = "";
            var actId = "";
            var cmsId = "";
            $scope.newDataList = "";
            if($scope.goodsListFromnType == '41'){
                $scope.newDataList = $stateParams.firstGoodsTypeId;
                cmsId = $stateParams.cmsId;
                if(cmsId != null && cmsId.length > 0){
                    $scope.sortByTimeValue = 3;
                }
            }else{
                firstGoodsTypeId = $stateParams.firstGoodsTypeId;
                pageType = $stateParams.pageType;
                classifyId = $stateParams.classifyId;
                classifyName = $stateParams.classifyName;
                goodsTypeId = $stateParams.type;
                actId = $stateParams.actId;
            }
            //全部的参数
            $scope.firstGoodsTypeId = $stateParams.firstGoodsTypeId;
            if (goodsTypeId == "42") {
                $scope.goodsListClass = "col-xs-9 col-sm-10";
            }

            $scope.crumbs = {first:'',second:'',third:''};

            //$scope.sort={'price':'down','time':'up'};
            //会员ID
            var userInfo = localStorageService.get(KEY_USERINFO);
            var memberId = "";
            if (userInfo) {
                memberId = userInfo.memberId;
            }


            //加载 滚动条事件
            //if($('.goodListTitle').length > 0){
            //    var $goodListTitle = $('.goodListTitle');
            //    var finished = true;

            $.event.add(window, "scroll", function() {
                var pTop = $(window).scrollTop();
                var height = getClientHeight();
                var theight = getScrollTop();
                var rheight = getScrollHeight();
                var pBottom = rheight-theight-height;

                if(pTop > 130){// 固定
                    if($('.goodListTitle').length > 0){
                        $('.goodListTitle').css({'position':'fixed','top':'0px',left:'0px','zIndex':100});
                        $('.goodListTitle').addClass('fixTop');
                    }
                }else{// 恢复
                    if($('.goodListTitle').length > 0) {
                        $('.goodListTitle').css({'position': 'static', 'top': '', 'left': '', 'zIndex': 0});
                        $('.goodListTitle').removeClass('fixTop');
                    }
                }

                if(pBottom < 130){
                    if(!$rootScope.isLoadShowing){
                        //finished = false;
                        if($scope.currentPage <= $scope.totalPage){
                            console.log($scope.currentPage);
                            $scope.doCallService();
                        }
                    }
                }
            });

            //}

            $scope.$on('$destroy',function(){
                $.event.remove(window, "scroll");
            });

            // 活动列表获取(折扣和特价两种活动)
            new activityTypeListService({"memberId":memberId})
                .then(function (res) {
                    if (res.resultCode == "00") {
                        $scope.activityTypeList = res.data;
                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function () {
                    popupService.showToast(commonMessage.networkErrorMsg);
                });


            // 头部信息取得
            new typeService({"firstGoodsTypeId":firstGoodsTypeId})
                .then(function (res) {
                    if (res.resultCode == "00") {
                        $scope.levelRootList = res.results;
                        $scope.crumbs.first = $scope.levelRootList[0].goodsTypeNameCn;
                        //angular.element(document).ready(function() {
                        //    var accordion = new Accordion($('#accordion'), false);
                        //});
                        //console.log($scope.levelRootList);
                        //    $scope.currentSecondCode = '';
                        //    $scope.currentThirdCode = '';
                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function () {
                    popupService.showToast(commonMessage.networkErrorMsg);
                });

            $scope.titleClick = function(level, code, s_name){
                firstGoodsTypeId = code;
                // 清空检索结果
                $scope.currentPage = 1;
                $scope.currentPagePhone = 1;
                $scope.totalPage = 0;
                $scope.goodsList = [];

                $scope.crumbs.second = '';
                $scope.crumbs.third = '';

                $scope.goodListTitle={selectSize:''};
                $scope.goodsListInfo = {"from": "", "to": ""};
                $scope.newDataList = "";
                actId = "";
                cmsId = "";
                $scope.sortByTimeValue = 0;
                $scope.sortByPriceValue = 0;
                $scope.isFirstInMain = 0;
                $scope.getGoodsTypeIndex();
            };
            $scope.selectClassify = function (level, code, s_name,t_name) {
                firstGoodsTypeId = code;
                // 清空检索结果
                $scope.currentPage = 1;
                $scope.currentPagePhone = 1;
                $scope.totalPage = 0;
                $scope.goodsList = [];

                if (level == '2') {
                    $scope.crumbs.second = s_name;
                    $scope.crumbs.third = '';
                } else if (level == '3') {
                    $scope.crumbs.second = s_name;
                    $scope.crumbs.third = t_name;
                }
                $scope.goodListTitle={selectSize:''};
                $scope.goodsListInfo = {"from": "", "to": ""};
                $scope.newDataList = "";
                actId = "";
                cmsId = "";
                $scope.sortByTimeValue = 0;
                $scope.sortByPriceValue = 0;
                $scope.isFirstInMain = 0;
                $scope.doCallService();
            };

            //排序 按照价格
            //变量forphone
            $scope.isupSortprice = true;
            $scope.sortByPrice = function(){
                var $target = $('.goodListTitle .sort.price');
                if($target.hasClass('down')){
                    //$target.removeClass('down').addClass('up');
                    $scope.sortByPriceValue = 2;
                }else{
                    //$target.removeClass('up').addClass('down');
                    $scope.sortByPriceValue = 1;
                }
                $scope.sortByTimeValue = 0;
                $scope.totalPage = 0;
                if($scope.phoneAndPC == 'none'){
                    $scope.currentPage = 1;
                    $scope.goodsList = [];
                    $scope.goodsListPhone = [];
                    $scope.doCallService();

                }else{
                    $scope.currentPagePhone = 1;
                    $scope.goodsList = [];
                    $scope.goodsListPhone = [];
                    if($scope.isupSortprice){
                        $scope.sortByPriceValue = 2;
                    }else{
                        $scope.sortByPriceValue = 1;
                    }
                    $scope.isupSortprice = !$scope.isupSortprice;
                    $scope.doCallServicePhone();
                }

            };
            //排序 按照时间
            //变量forphone
            $scope.isupSort = true;
            $scope.sortByTime = function(){
                var $target = $('.goodListTitle .sort.time');
                if($target.hasClass('down')){
                    //$target.removeClass('down').addClass('up');
                    $scope.sortByTimeValue = 2;
                }else{
                    //$target.removeClass('up').addClass('down');
                    $scope.sortByTimeValue = 1;
                }
                $scope.sortByPriceValue = 0;
                $scope.totalPage = 0;
                if($scope.phoneAndPC == 'none'){
                    $scope.currentPage = 1;
                    $scope.goodsList = [];
                    $scope.goodsListPhone = [];
                    $scope.doCallService();
                }else{
                    $scope.goodsList = [];
                    $scope.goodsListPhone = [];
                    $scope.currentPagePhone = 1;
                    if($scope.isupSort){
                        $scope.sortByTimeValue = 2;
                    }else{
                        $scope.sortByTimeValue = 1;
                    }
                    $scope.isupSort = !$scope.isupSort;
                    $scope.doCallServicePhone();
                }
            };

            // 获取商品分类的二级首页
            $scope.getGoodsTypeIndex = function () {
                if($scope.isLoading){
                    return;
                }
                $scope.isLoading = true;
                //初始化调用
                new goodsTypeIndexService({
                    "firstGoodsTypeId": firstGoodsTypeId,
                    "memberId":memberId
                }).then(function (res) {
                    $scope.isLoading = false;
                    if (res.resultCode == "00") {
                        console.log(res);
                        $scope.goodsTypeNewData = res.newList;
                        $scope.goodsTypeAllGoodsListData = res.goodsList;
                        if ($scope.goodsTypeAllGoodsListData.length > 6) {
                            $scope.goodsTypeGoodsListData = res.goodsList.slice(0,6);
                        } else {
                            $scope.goodsTypeGoodsListData = res.goodsList;
                        }
                        $scope.isFirstInMain = 1;

                        //从首页直接跳转直接跳转
                        if(pageType != null && pageType.length > 0 && pageType == '3'){
                            // alert("首页");
                            //新品 3
                            $scope.getNewData();
                        }else if(pageType != null && pageType.length > 0 ){
                            // 其他
                            // alert("其他");
                            $scope.selectClassify('2',classifyId,classifyName,'');
                        }
                        pageType = '';
                        classifyId = '';
                        classifyName = '';
                    } else {
                        // alert("其他d")
                        // popupService.showToast(res.resultMessage);
                    }
                }, function (err) {
                    $scope.isLoading = false;
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
            };

            //$scope.newDataList = "";
            $scope.getNewData = function () {

                $scope.goodListTitle={selectSize:''};
                $scope.goodsListInfo = {"from": "", "to": ""};
                $scope.newDataList = "";
                actId = "";
                cmsId = "";
                $scope.sortByTimeValue = 3;
                $scope.sortByPriceValue = 0;
                $scope.isFirstInMain = 0;

                // 清空检索结果
                $scope.currentPage = 1;
                $scope.currentPagePhone = 1;
                $scope.totalPage = 0;
                $scope.goodsList = [];

                $scope.crumbs.second = '新品';
                $scope.crumbs.third = '';
                //var goodsTypeIdD = firstGoodsTypeId;
                if(firstGoodsTypeId != null && firstGoodsTypeId.split("_").length > 0){
                    firstGoodsTypeId = firstGoodsTypeId.split("_")[0];
                }
                new getNewDataService({"firstGoodsTypeId": firstGoodsTypeId})
                    .then(function (res) {
                        if (res.resultCode == "00") {
                            $scope.newDataList = res.results;
                            cmsId = res.cmsId;
                            console.log(res.results);
                            if($scope.newDataList  == null || $scope.newDataList.length == 0){
                                $scope.newDataList="####################################";
                            }
                            $scope.doCallService();
                        } else {
                            popupService.showToast(res.resultMessage);
                        }
                    }, function () {
                        popupService.showToast(commonMessage.networkErrorMsg);
                    });
            };

            $scope.getDataByActId = function (activityId,activityName) {

                $scope.goodListTitle={selectSize:''};
                $scope.goodsListInfo = {"from": "", "to": ""};
                $scope.newDataList = "";
                actId = "";
                cmsId = "";
                $scope.sortByTimeValue = 0;
                $scope.sortByPriceValue = 0;
                $scope.isFirstInMain = 0;

                // 清空检索结果
                $scope.currentPage = 1;
                $scope.currentPagePhone = 1;
                $scope.totalPage = 0;
                $scope.goodsList = [];

                $scope.crumbs.second = '活动';
                $scope.crumbs.third = activityName;
                actId = activityId;
                if(firstGoodsTypeId != null && firstGoodsTypeId.split("_").length > 0){
                    firstGoodsTypeId = firstGoodsTypeId.split("_")[0];
                }
                $scope.doCallService();
            };


            $scope.goodListTitle={selectSize:''};
            $scope.goodsListInfo = {"from": "", "to": ""};
            $scope.doCallService = function (cb) {
                if($scope.isLoading){
                    return;
                }
                $scope.isLoading = true;
                //初始化调用
                new goodsListService({
                    "goodsIds": $scope.newDataList,
                    "sizeCode":$scope.goodListTitle.selectSize,
                    "from": $scope.goodsListInfo.from,
                    "to": $scope.goodsListInfo.to,
                    "actId": actId,
                    "cmsId":cmsId,
                    "firstGoodsTypeId": firstGoodsTypeId,
                    "memberId":memberId,
                    "sortByPrice":$scope.sortByPriceValue,
                    "sortByTime":$scope.sortByTimeValue,
                    "currentPage": $scope.currentPage,
                    "totalPage": $scope.totalPage,
                    "pageSize": $scope.pageSize
                }).then(function (res) {
                    $scope.isLoading = false;
                    if (res.resultCode == "00") {
                        $scope.goodsList = $scope.goodsList.concat(res.results);
                        $scope.totalPage = res.allCount;
                        //$scope.dochangePage();
                        $scope.currentPage++;
                        angular.element(document).ready(function() {
                            // 筛选
                            $('.filterBtn').webuiPopover({
                                width:450,
                                height:260,
                                padding:false,
                                animation:'pop',
                                content:function(){
                                    var html = $('.filterTemplate').html();

                                    return html;
                                }
                            });
                        });
                        $scope.sizeList = res.sizeList;

                        if(cb) cb(true);
                        //console.log($('.goodListTitle .sort.price'));
                    } else {
                        popupService.showToast(res.resultMessage);
                        if(cb) cb(true);
                    }
                }, function (err) {
                    $scope.isLoading = true;
                    popupService.showToast(commonMessage.networkErrorMsg);
                    if(cb) cb(true);
                });
            };

            $scope.doCallServicePhone = function () {
                if($scope.isPhoneLoading){
                    return;
                }
                $scope.isPhoneLoading = true;
                //初始化调用
                new goodsListService({
                    //"goodsTypeId": goodsTypeId,
                    "goodsIds": $scope.newDataList,
                    "actId": actId,
                    "cmsId":cmsId,
                    "firstGoodsTypeId": firstGoodsTypeId,
                    "memberId":memberId,
                    "sortByPrice":$scope.sortByPriceValue,
                    "sortByTime":$scope.sortByTimeValue,
                    "currentPage": $scope.currentPagePhone,
                    "totalPage": $scope.totalPage,
                    "pageSize": $scope.pageSize
                }).then(function (res) {
                    $scope.isPhoneLoading = false;
                    if (res.resultCode == "00") {
                        if (res.results.length != 0) {
                            if($scope.isPageChange){
                                $scope.goodsListPhone = $scope.goodsListPhone.concat(res.results);
                            }else{
                                $scope.goodsListPhone = res.results;
                            }
                        } else {
                            $scope.currentPagePhone = $scope.currentPagePhone - 1;
                            //popupService.showToast("没有更多数据");
                        }
                        $scope.isPageChange = false;
                        $scope.totalPage = res.allCount;
                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function (err) {
                    $scope.isPhoneLoading = true;
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
            };

            var scope = $scope.$new();
            scope.param = {
                firstGoodsTypeId: firstGoodsTypeId
            };

            $scope.popClassify_xs = function () {
                var classifyModalInstance = $modal.open({
                    templateUrl: 'html/classifyModal.html',
                    controller: 'popClassifyCtrl',
                    scope: scope,
                    backdrop: true,
                    resolve: {
                        //items: function () {
                        //    return $scope.items;
                        //}
                    }
                });
                classifyModalInstance.opened.then(function () {//模态窗口打开之后执行的函数
                });
                classifyModalInstance.result.then(
                    function (result) {
                        firstGoodsTypeId = localStorageService.get(KEY_GOODSTYPE);
                        $scope.newDataList = "";
                        $scope.doCallServicePhone();
                        $scope.goodsListPhone = [];
                    }, function (reason) {
                        console.log(reason);//点击空白区域，总会输出backdrop click，点击取消，则会暑促cancel
                    });

            };

            if($scope.phoneAndPC == 'none'){
                if (goodsTypeId == "42"&&firstGoodsTypeId!='b97f11e6-df14-4c75-ab1f-2c037c70f77b_cd73737f-64e0-44ae-9e6a-728120151f67_b8a3e681-6454-4436-aae7-0b414f801316') {
                    $scope.getGoodsTypeIndex();
                }
                else {
                    $scope.doCallService();
                }
            }else{

                //从首页直接跳转直接跳转
                if(pageType != null && pageType.length > 0 && pageType == '3'){
                    //新品 3
                    $scope.getNewData();
                }else if(pageType != null && pageType.length > 0){
                    //其他
                    firstGoodsTypeId = classifyId;
                }
                pageType = '';
                classifyId = '';

                $scope.doCallServicePhone();
            }

            //加载更多
            $scope.doGetMore = function () {
                $scope.isPageChange = true;
                $scope.currentPagePhone = $scope.currentPagePhone + 1;
                $scope.doCallServicePhone();
            };

            //商品详细画面跳转
            $scope.goProdocutDetail = function (goodsId) {
                var url = '/qyds-web-pc/qyds/#/goodsDetail/' + goodsId;
                window.open(url);
                //$state.go("goodsDetail", {"goodsId": goodsId});
            };

            // 获取所有男士商品
            $scope.getGoodsList = function () {
                $scope.goodListTitle={selectSize:''};
                $scope.goodsListInfo = {"from": "", "to": ""};
                $scope.newDataList = "";
                actId = "";
                cmsId = "";
                $scope.sortByTimeValue = 0;
                $scope.sortByPriceValue = 0;
                $scope.isFirstInMain = 0;

                // 清空检索结果
                $scope.currentPage = 1;
                $scope.currentPagePhone = 1;
                $scope.totalPage = 0;
                $scope.goodsList = [];

                $scope.crumbs.second = '全部';
                $scope.crumbs.third = '';
                if(firstGoodsTypeId != null && firstGoodsTypeId.split("_").length > 0){
                    firstGoodsTypeId = firstGoodsTypeId.split("_")[0];
                }
                $scope.doCallService();
            };
            // 返回顶部
            $scope.goTop = function(){
                $('body,html').animate({scrollTop:0},500);
                return false;
            }
            $scope.openFilterPanel = function(){
                if($('.webui-popover').length > 0){
                    $('.webui-popover').remove();
                }else{
                }
                WebuiPopovers.updateContent($('.filterBtn'),$('.filterTemplate').html());
                setTimeout(function(){

                    $('.webui-popover-content .filterPanel #priceFrom').val($scope.goodsListInfo.from);
                    $('.webui-popover-content .filterPanel #priceTo').val($scope.goodsListInfo.to);

                    $('.webui-popover-content .filterPanel .sizeTag').off('click').on('click',function(){
                        $(this).addClass('active').siblings().removeClass('active');
                        $scope.goodListTitle.selectSize = $(this).attr('data-code');
                    });

                    $('.webui-popover-content .filterPanel #priceFrom,.webui-popover-content .filterPanel #priceTo').off('change').on('change',function(){
                        var val = $(this).val();
                        if(val != '' && isNaN(val)){
                            $(this).val('');
                            return;
                        }

                        var minV = $('.webui-popover-content .filterPanel #priceFrom').val();
                        var maxV = $('.webui-popover-content .filterPanel #priceTo').val();

                        if($(this).hasClass('min') && !isNaN(maxV )){
                            if(parseFloat(minV) > parseFloat(maxV)){
                                $(this).val('');
                                return;
                            }
                        }
                        if($(this).hasClass('max') && !isNaN(minV)){
                            if(parseFloat(minV) > parseFloat(maxV)){
                                $(this).val('');
                                return;
                            }
                        }

                    });
                    $('.webui-popover-content .filterPanel #clearBtn').off('click').on('click',function(){
                        $('.webui-popover-content .filterPanel .sizeTag').removeClass('active');
                        $('.webui-popover-content .filterPanel #priceFrom').val('');
                        $('.webui-popover-content .filterPanel #priceTo').val('')
                        $scope.goodListTitle.selectSize = '';
                        $scope.goodsListInfo.from = '';
                        $scope.goodsListInfo.to = '';
                    });
                    $('.webui-popover-content .filterPanel #okBtn').off('click').on('click',function(){

                        console.log($('.webui-popover-content .filterPanel #priceFrom').length);

                        $scope.goodsListInfo.from = $('.webui-popover-content .filterPanel #priceFrom').val();
                        $scope.goodsListInfo.to = $('.webui-popover-content .filterPanel #priceTo').val();
                        console.log($scope.goodsListInfo.from);
                        console.log($scope.goodsListInfo.to);
                        $scope.currentPage = 1;
                        $scope.currentPagePhone = 1;
                        $scope.totalPage = 0;
                        $scope.goodsList = [];
                        $scope.doCallService();
                        WebuiPopovers.hideAll();
                        $scope.goTop();
                    })
                },0);

            }
        }])


    // 新品列表 start
    .controller('goodsListDisplayCtrl', ["$scope", "$state", "$stateParams", "goodsListDisplayService", "$rootScope", "$modal", "typeService","brandtypeService","localStorageService","popupService",
        function ($scope, $state, $stateParams, goodsListDisplayService, $rootScope, $modal, typeService,brandtypeService,localStorageService, popupService) {
            //数据集合
            $scope.goodsList = [];
            $scope.goodsListPhone = [];
            var goodsTypeId = $stateParams.type;
            $scope.phoneAndPC = $('#phone').css('display');


            //会员ID
            var userInfo = localStorageService.get(KEY_USERINFO);
            var memberId = "";
            if (userInfo) {
                memberId = userInfo.memberId;
            }

            $scope.doCallService = function () {
                //初始化调用
                new goodsListDisplayService({
                    "goodsTypeId": goodsTypeId,
                    "memberId":memberId
                }).then(function (res) {
                    if (res.resultCode == "00") {
                        $scope.goodsList = res.results;
                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function (err) {
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
            };

            $scope.doCallServicePhone = function () {
                //初始化调用
                new goodsListDisplayService({
                    "goodsTypeId": goodsTypeId,
                    "memberId":memberId
                }).then(function (res) {
                    if (res.resultCode == "00") {
                        $scope.goodsListPhone = res.results;
                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function (err) {
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
            };

            if($scope.phoneAndPC == 'none') {
                $scope.doCallService();
            }else {
                $scope.doCallServicePhone();
            }

            //商品详细画面跳转
            $scope.goProdocutDetail = function (goodsId) {
                var url = '/qyds-web-pc/qyds/#/goodsDetail/' + goodsId;
                window.open(url);
                //$state.go("goodsDetail", {"goodsId": goodsId});
            };

        }])

    // 检索商品 
    .controller('searchGoodsCtrl', ["$scope", "$state", "$stateParams", "hotSearchService", "$rootScope", "popupService","localStorageService", function ($scope, $state, $stateParams, hotSearchService, $rootScope,popupService,localStorageService) {
        var searchKey = $stateParams.searchKey;      //
        // var searchKey = ""; 
        // if($stateParams.searchKey!=null && $stateParams.searchKey.length > 0){ 
        //    searchKey = $stateParams.searchKey; 
        //    $rootScope.searchKey = $stateParams.searchKey; 
        // }else{ 
        //    searchKey = $rootScope.searchKey; 
        // } 

        //会员ID
        var userInfo = localStorageService.get(KEY_USERINFO);
        var memberId = "";
        if (userInfo) {
            memberId = userInfo.memberId;
        }

        $scope.goodsList = [];
        $scope.goodsListPhone = [];     //PC 初始化的场合 
        $scope.currentPage = 1;
        $scope.currentPagePhone = 1;
        $scope.totalPage = 0;
        $scope.pageSize = 8;
        $scope.phoneAndPC = $('#phone').css('display');
        $scope.isLoading = false;
        $scope.isPhoneLoading = false;
        $scope.hasMore = true;
        $scope.dochangePage = function () {
            //分页样式初始化 
            $(".pager").ucPager({
                //pageClass     : "分页样式", 
                currentPage: $scope.currentPage, //当前页数 
                totalPage: $scope.totalPage,   //总页数 
                pageSize: $scope.pageSize,   //每一页显示的件数 
                clickCallback: function (page) {
                    //下一页 上一页 页数跳转 
                    $scope.currentPage = page;
                    $scope.doCallService();
                    $scope.dochangePage();
                }
            });
        };
        $scope.doCallService = function () {
            if($scope.isLoading){
                return;
            }
            $scope.isLoading = true;
            new hotSearchService({
                "searchKey": searchKey,
                "memberId":memberId,
                "currentPage": $scope.currentPage,
                "totalPage": $scope.totalPage,
                "pageSize": $scope.pageSize
            }).then(function (res) {
                $scope.isLoading = false;
                if (res.resultCode == "00") {
                    $scope.goodsList = res.results;
                    $scope.totalPage = res.allCount;
                    $scope.dochangePage();
                } else {
                    popupService.showToast(res.resultMessage); 
                }
            }, function (err) {
                $scope.isLoading = false;
                popupService.showToast(commonMessage.networkErrorMsg); 
            });
        };
        $scope.doCallServicePhone = function () {
            if($scope.isPhoneLoading){
                return;
            }
            $scope.isPhoneLoading = true;
            new hotSearchService({
                "searchKey": searchKey,
                "memberId":memberId,
                "currentPage": $scope.currentPagePhone,
                "totalPage": $scope.totalPage,
                "pageSize": $scope.pageSize
            }).then(function (res) {
                $scope.isPhoneLoading = false;
                if (res.resultCode == "00") {
                    if (res.results.length != 0) {
                        $scope.goodsListPhone = $scope.goodsListPhone.concat(res.results);
                        if(res.results.length>=$scope.pageSize){
                            $scope.hasMore = true;
                        }else{
                            $scope.hasMore = false;
                        }
                    } else {
                        $scope.hasMore = false;
                        $scope.currentPagePhone = $scope.currentPagePhone - 1;
                        //popupService.showToast("没有更多数据"); 
                    }
                    $scope.totalPage = res.allCount;
                } else {
                    $scope.hasMore = false;
                    popupService.showToast(res.resultMessage); 
                }
            }, function (err) {
                $scope.isPhoneLoading = false;
                popupService.showToast(commonMessage.networkErrorMsg); 
            });
        };

        if($scope.phoneAndPC == 'none') {
            $scope.doCallService();
        }else{
            $scope.doCallServicePhone();
        }
        //加载更多 
        $scope.doGetMore = function () {
            $scope.currentPagePhone = $scope.currentPagePhone + 1;
            $scope.doCallServicePhone();
        };

        //商品详细画面跳转
        $scope.goProdocutDetail = function (goodsId) {
            var url = '/qyds-web-pc/qyds/#/goodsDetail/' + goodsId;
            window.open(url);
            //$state.go("goodsDetail", {"goodsId": goodsId});
        };
    }])


    // 商品详细
    .controller('goodsDetailCtrl', ["$scope", "$state", "$stateParams","$interval", "localStorageService", "productDetailService", "favoriteService",
        "shoppingBagService", "$rootScope", "$sce",'$modal','popupService','confirmOrderService',
        function ($scope, $state, $stateParams,$interval, localStorageService, productDetailService, favoriteService,
                  shoppingBagService, $rootScope, $sce,$modal,popupService, confirmOrderService) {
        var goodDetailBx = {};
        var bxpager = {};
        var userInfo = localStorageService.get(KEY_USERINFO);
        var memberId = "";

        if (userInfo) {
            memberId = userInfo.memberId;
        }
        var goodsId = "";
        if ($stateParams.goodsId != null
            && $stateParams.goodsId.length > 0) {
            $rootScope.goodsId = $stateParams.goodsId;
            goodsId = $stateParams.goodsId;
        } else {
            goodsId = $rootScope.goodsId;
        }
            $scope.detailType = "1";
        $scope.setDetailType = function(type){
            $scope.detailType = type;
        }
        // goodsDetail初始化
        $scope.initGoodsDetail = function () {

            $('.numSpinner').spinner({min:1,value:1});

            $('.square_check').iCheck({
                checkboxClass: 'icheckbox_square',
                radioClass: 'iradio_square',
                //increaseArea: '15%' // optional
            });
        };
        //当前时间
        $scope.nowTime ;
        $scope.timer;
        //库存部分判断初次进入
        $scope.storeInit = true;
        $scope.initGoodsDetail();
            $scope.getHtml  =function(text){
                if(text){
                    return text.replace(/\n/g,"<br/>");
                }
            }
            new productDetailService({"goodsId": goodsId, "memberId": memberId}).then(function (vresponse) {

            var res = vresponse.results;
            if (vresponse.resultCode == "00") {

                $scope.productDetailData = res;

                if(res.activityInfo){
                    var nowTime = res.nowTime;
                    var startTime = res.activityInfo.startTime;
                    var endTime = res.activityInfo.endTime;

                    $scope.nowTime = nowTime;
                    // 结束时间减去nowTime + 1000/s
                    var diffTime = $scope.diffTime(endTime,nowTime);
                    $scope.activityTime = diffTime;
                    // 1s钟加1000
                    $scope.timer = $interval(function(){
                        var rediffTime = $scope.reDiffTime(endTime,$scope.nowTime);
                        $scope.activityTime = rediffTime;
                        console.log(rediffTime);
                    },1000);
                    //timer.then(function(){
                    //    console.log('创建成功')
                    //}, function(){
                    //        console.log('创建不成功')
                    //    });

                }



                // 保存我的浏览记录 Start
                var history = {
                    goodsTypeId : $scope.productDetailData.goodsTypeId,
                    goodsId : $scope.productDetailData.goodsId,
                    goodsTypeName : $scope.productDetailData.goodsTypeNamePath,
                    goodsName:$scope.productDetailData.goodsName,
                    goodsImage:res.imageUrlJsonPc[0]
                };

                var goodsHistoryList = localStorageService.get("GOODS_TYPE_HISTORY");
                if(!goodsHistoryList){
                    goodsHistoryList = [];
                    goodsHistoryList.push(history);
                    localStorageService.set("GOODS_TYPE_HISTORY",goodsHistoryList);
                }else{
                    var hasData = false;
                    angular.forEach(goodsHistoryList,function(goodsType){
                        if(goodsType.goodsId == history.goodsId){
                           hasData = true;
                        }
                    });
                    if(!hasData){
                        goodsHistoryList.push(history);
                        localStorageService.set("GOODS_TYPE_HISTORY",goodsHistoryList);
                    }
                }
                $scope.goodsHistoryList = goodsHistoryList;
                // 保存我的浏览记录 End
                $scope.goodsTypeIdPath = $scope.productDetailData.goodsTypeIdPath.split('_')[0];
                $scope.description = $sce.trustAsHtml($scope.getHtml($scope.productDetailData.description));
                $scope.sizeDescription = $sce.trustAsHtml($scope.getHtml($scope.productDetailData.sizeDescription));
                //搭配商品
                //var mating_list = res.mating_list;
                ////记录checkbox的选择与否
                //var checkboxArray = new Array();
                //var minPriceArray = new Array();
                //var maxPriceArray = new Array();

                ////主商品的最大最小价钱
                //$scope.totalPriceMin = res.minPrice;
                //$scope.totalPriceMax = res.maxPrice;
                ////主商品算一件(搭配)
                //$scope.productCount = 1;
                //$scope.totalPriceDis = res.minAndMaxPrice;

                //if(mating_list != null){
                //    for(var i = 0; i<mating_list.length; i++){
                //        checkboxArray.push(false);
                //        minPriceArray.push(mating_list[i].minPrice);
                //        maxPriceArray.push(mating_list[i].maxPrice);
                //    }
                //}
                //
                //$scope.checkboxArray = checkboxArray;
                //$scope.minPriceArray = minPriceArray;
                //$scope.maxPriceArray = maxPriceArray;

                //PC端图片部分
                $scope.imageUrlJsonPc = $scope.productDetailData.imageUrlJsonPc;
                $scope.collectNo = res.collectNo;

                if (res.type != "30") {
                    $scope.skuList = res.skulist;
                    $scope.skuImage = res.imageUrlJsonPc;
                    $scope.skuPrice = res.minAndMaxPrice;
                    $scope.newCount = 0;
                    $scope.activityPrice = res.minAndMaxPriceActivity;

                    //$scope.selectedColor = res.colorList[0].key;
                    //$scope.selectedSize = res.sizeList[0].key;
                    $scope.introduceHtml = $sce.trustAsHtml(res.introduceHtml);
                    // 售后服务维护内容
                    $scope.goodsServeHtml = $sce.trustAsHtml(res.goodsServeHtml);
                    //$scope.refreshSkuPrice();


                    $scope.getColorClass = function (colorValue) {
                        if ($scope.selectedColor == colorValue) {
                            return "selected";
                        } else {
                            return "";
                        }
                    };
                    $scope.getSizeClass = function (sizeValue) {
                        if ($scope.selectedSize == sizeValue) {
                            return "selected";
                        } else {
                            return "";
                        }
                    };

                    $scope.setColor = function (colorValue, colorName) {
                        $scope.selectedColor = colorValue;
                        $scope.refreshSkuPrice();
                    };

                    $scope.setSize = function (sizeValue) {
                        $scope.selectedSize = sizeValue;
                        $scope.refreshSkuPrice();
                    };

                } else {

                    $scope.skuPrice = res.minAndMaxPrice;
                    $scope.activityPrice = res.minAndMaxPriceActivity;
                    $scope.skuImage = res.imageUrlJsonPc;
                    $scope.introduceHtml = $sce.trustAsHtml(res.introduceHtml);
                    // 售后服务维护内容
                    $scope.goodsServeHtml = $sce.trustAsHtml(res.goodsServeHtml);

                    $scope.goodsList = res.goodsList;
                    //var imgArray = new Array();
                    var selecedColorArray = new Array();
                    var selecedSizeArray = new Array();
                    var skuPriceArray = new Array();
                    var skuActivityPriceArray = new Array();
                    var selectSkuIdArray = new Array();
                    var skuNewCountArray = new Array();
                    var isExist = false;
                    for (var i = 0; i < res.goodsList.length; i++) {
                        //imgArray.push(res.goodsList[i].skulist[0].imgs[0]);
                        skuPriceArray.push(res.goodsList[i].skulist[0].price);
                        //skuNewCountArray.push(res.goodsList[i].skulist[0].newCount);
                        skuNewCountArray.push('0');
                        if (res.goodsList[i].skulist[0].newCount == null
                            || res.goodsList[i].skulist[0].newCount == '0') {
                            isExist = true;
                        }
                        skuActivityPriceArray.push(res.goodsList[i].skulist[0].activityPrice);
                        //selecedColorArray.push(res.goodsList[i].colorList[0].key);
                        //selecedSizeArray.push(res.goodsList[i].sizeList[0].key);
                        //selectSkuIdArray.push(res.goodsList[i].skulist[0].skuid);
                        selecedColorArray.push("");
                        selecedSizeArray.push("");
                        selectSkuIdArray.push("");
                    }
                    $scope.good = $scope.goodsList[0];
                    $scope.skuPriceFruit = skuPriceArray[0];
                    $scope.skuPriceActivityPriceFruit = skuActivityPriceArray[0];
                    $scope.selectedColor = selecedColorArray;
                    $scope.selectedSize = selecedSizeArray;
                    $scope.selectedSkuId = selectSkuIdArray;
                    $scope.newCount = skuNewCountArray;
                    //$scope.refreshSkuPriceForSuit(0);
                    $scope.index = 0;
                    //判断是否存在库存为空的数据的标记
                    if (isExist) {
                        $scope.isHasStore = "0";
                    } else {
                        $scope.isHasStore = "1";
                    }

                    $scope.getColorClass = function ( colorValue) {

                        if (colorValue == $scope.selectedColor[$scope.index]) {
                            return "selected";
                        } else {
                            return "";
                        }
                    };
                    $scope.getSizeClass = function ( sizeValue) {

                        if (sizeValue == $scope.selectedSize[$scope.index]) {
                            return "selected";
                        } else {
                            return "";
                        }

                    };

                    $scope.setColor = function ( colorValue) {
                        $scope.selectedColor[$scope.index] = colorValue;
                        $scope.refreshSkuPriceForSuit($scope.index);
                    };

                    $scope.setSize = function ( sizeValue) {

                        $scope.selectedSize[$scope.index] = sizeValue;
                        $scope.refreshSkuPriceForSuit($scope.index);
                    };

                }

                //setTimeout(function(){
                    angular.element(document).ready(function() {
                        goodDetailBx = $('#goodDetailSlider').bxSlider({
                            pagerCustom: '#bx-pager',
                            controls: false,
                            preloadImages:'all',
                            startSlide:0,
                            onSlideAfter: function (e) {
                                $('.bxslider img').attr('data-imagezoom', true);
                            },
                            onSlideBefore: function (e) {
                                $('.bxslider img').removeAttr('data-imagezoom');
                            },
                            onSliderLoad:function(currentIndex){
                            }
                        });

                        bxpager = new Swiper('.swiper-container', {
                            pagination: '.swiper-pagination',
                            nextButton: '.swiper-button-next',
                            prevButton: '.swiper-button-prev',
                            direction: 'vertical',
                            slidesPerView: 5,
                            centeredSlides: false,
                            paginationClickable: true,
                            spaceBetween: 10,
                            initialSlide:0,
                            loop: false,
                            // loopAdditionalSlides:1
                    });
                        var swiper = new Swiper('.swiper-container-phone', {
                            pagination: '.swiper-pagination',
                            slidesPerView: 'auto',
                            centeredSlides: true,
                            initialSlide:1,
                            paginationClickable: true,
                            spaceBetween: 10,
                            loop: true
                        });

                        var swiperBottom = new Swiper('.swiper-container-bottom', {
                            nextButton: '.swiper-button-next',
                            prevButton: '.swiper-button-prev',
                            slidesPerView: 5,
                            spaceBetween: 30,
                            freeMode: true
                        });
                        var swiperBottomPhone = new Swiper('.swiper-container-bottom-phone', {
                            nextButton: '.swiper-button-next',
                            prevButton: '.swiper-button-prev',
                            slidesPerView: 3,
                            spaceBetween: 10,
                            freeMode: true
                        });

                        //$scope.initGoodsDetail();
                        $scope.selectedCount = {
                            value: 1
                        };

                        var goodsArray = new Array();
                        if($scope.goodsList  == null){
                            return;
                        }
                        for(var i = 0; i < $scope.goodsList.length; i++ ){
                            var item = {};
                            item.stepTag = i + 1;
                            item.title = '组成套装商品';
                            item.time = '';
                            goodsArray.push(item);
                        }

                        $(".fruitStep").loadStep({
                            //ystep的外观大小
                            //可选值：small,large
                            size: "large",
                            //ystep配色方案
                            //可选值：green,blue
                            color: "blue",
                            clickCb:function(index) {
                                $scope.index = index;
                                $scope.good = $scope.goodsList[index];
                                $scope.refreshSkuPriceForSuit(index);
                                $scope.$apply();
                                return true;
                            },
                            //ystep中包含的步骤
                            steps: goodsArray

                        });
                        //$(".fruitStep").setStep(3)
                    });


                //},5000);
            } else {
                popupService.showToast(vresponse.resultMessage);
            }
        }, function (err) {
            popupService.showToast(commonMessage.networkErrorMsg);
        });
            $scope.selectedTab = 1;

        $scope.selectTab = function (index) {
            $scope.selectedTab = index;
        };

        $scope.diffTime = function(endTime,nowTime){

            var diff=endTime-nowTime;//时间差的毫秒数

            //计算出相差天数
            var days=Math.floor(diff/(24*3600*1000));

            //计算出小时数
            var leave1=diff%(24*3600*1000);    //计算天数后剩余的毫秒数
            var hours=Math.floor(leave1/(3600*1000));
            //计算相差分钟数
            var leave2=leave1%(3600*1000);        //计算小时数后剩余的毫秒数
            var minutes=Math.floor(leave2/(60*1000));

            //计算相差秒数
            var leave3=leave2%(60*1000);      //计算分钟数后剩余的毫秒数
            var seconds=Math.round(leave3/1000);

            var returnStr = seconds + "秒";
            if(minutes>0) {
                returnStr = minutes + "分" + returnStr;
            }
            if(hours>0) {
                returnStr = hours + "小时" + returnStr;
            }
            if(days>0) {
                returnStr = days + "天" + returnStr;
            }
            return returnStr;
        };

        $scope.reDiffTime = function(endTime,nowTime){
            $scope.nowTime = nowTime + 1000;
            console.log(nowTime);
            var rediffTime = $scope.diffTime(endTime , nowTime);
            return rediffTime;
        };
        $scope.$on('$destroy',function(){
            $interval.cancel($scope.timer);
        });

        $scope.confirmPopover = function () {
            //var isExist = false;
            //for(var i = 0; i<$scope.checkboxArray.length; i++){
            //    if($scope.checkboxArray[i] == true){
            //        isExist = true;
            //    }
            //}
            userInfo = localStorageService.get(KEY_USERINFO);
            if(!userInfo){
                $scope.goLogin();
                return;
            }


                // 立即购买.跳转到订单列表
                if($scope.productDetailData.type!="30"){

                    //判断没有选择SKU
                    if($scope.selectedSkuId == null){
                        popupService.showToast("请选择商品的颜色和尺码.");
                        return;
                    }

                    if(parseInt($scope.newCount)<parseInt($scope.selectedCount.value)){
                        popupService.showToast("库存不足.");
                        //popupService.showToast("库存不足.");
                        return;
                    }
                    var goodsInfo = {};
                    goodsInfo.goodsId = $scope.productDetailData.goodsId;
                    goodsInfo.type = $scope.productDetailData.type;
                    goodsInfo.skuId = $scope.selectedSkuId;
                    goodsInfo.quantity = $scope.selectedCount.value;

                    $scope.checkOrderConfirmBySingle(goodsInfo);
                }else{

                    var isSkuAvalible = true;
                    angular.forEach($scope.selectedSkuId,function(sku){
                        if(sku == null || sku.length == 0){
                            isSkuAvalible = false;
                            return false;
                        }
                    });
                    if(!isSkuAvalible){
                        popupService.showToast("请选择组成套装所有商品的颜色和尺码.");
                        //popupService.showToast("库存不足.");
                        return;
                    }

                    var isCountAvalible = true;
                    angular.forEach($scope.newCount,function(count){
                        if(parseInt(count) < parseInt($scope.selectedCount.value)){
                            isCountAvalible = false;
                            return false;
                        }
                    });
                    if(!isCountAvalible){
                        popupService.showToast("组成套装商品的库存不足.");
                        //popupService.showToast("库存不足.");
                        return;
                    }

                    var goodsInfo = {};
                    goodsInfo.goodsId = $scope.productDetailData.goodsId;
                    goodsInfo.type = $scope.productDetailData.type;
                    goodsInfo.quantity = $scope.selectedCount.value;
                    var skuList = [];
                    angular.forEach($scope.productDetailData.goodsList,function(sku,index){
                        skuList.push({
                            "goodsId":sku.goodsId,
                            "goodsSkuId":$scope.selectedSkuId[index]
                        });
                    });
                    goodsInfo.skuList = skuList;

                    $rootScope.shoppingBagsOfConfirmOrder = undefined;
                    $rootScope.singleGoodsInfoOfConfirmOrder = undefined;
                    $rootScope.suitGoodsInfoOfConfirmOrder = undefined;

                    localStorageService.set(KEY_PARAM_COMFIRM_ORDER, goodsInfo);
                    $state.go("personalCenter.confirmOrder", {suitGoodsInfo:'#'});
                }
        };

        $scope.checkOrderConfirmBySingle = function (goodsInfo) {
            var param = {
                memberId: userInfo.memberId,
                goodsId: goodsInfo.goodsId,
                type: goodsInfo.type,
                goodsSkuId: goodsInfo.skuId,
                quantity: goodsInfo.quantity
            };
            new confirmOrderService(param).checkDataBySingleGoods().then(function (res) {
                if(res.resultCode == "00"){

                    $rootScope.shoppingBagsOfConfirmOrder = undefined;
                    $rootScope.singleGoodsInfoOfConfirmOrder = undefined;
                    $rootScope.suitGoodsInfoOfConfirmOrder = undefined;
                    resultPath=0;
                    localStorageService.set(KEY_PARAM_COMFIRM_ORDER, goodsInfo);
                    $state.go("personalCenter.confirmOrder", {singleGoodsInfo:'#'});

                }else{
                    popupService.showToast(res.resultMessage);
                }
            }, function (error) {
                popupService.showToast(commonMessage.networkErrorMsg);
            });
        };

        $scope.addToShopBag = function () {
            userInfo = localStorageService.get(KEY_USERINFO);
            if(!userInfo){
                $scope.goLogin();
                return;
            }

            // 会员ID	memberId
            // *             店铺ID	shopId
            // *             商品类型	type
            // *             商品代码	goodsId
            // *             商品名称	goodsName
            // *             商品SKU	skuList
            // *             件数	quantity
            //if(!$scope.goodsCount||$scope.goodsCount==0){
            //    popupService.showToast("请选择购买件数.");
            //    return;
            //}
            var skuList  =[];
            if($scope.productDetailData.type!="30") {

                //判断没有选择SKU
                if($scope.selectedSkuId == null){
                    popupService.showToast("请选择商品的颜色和尺码.");
                    return;
                }
                skuList.push({skuId: $scope.selectedSkuId});
                if(parseInt($scope.newCount)<parseInt($scope.selectedCount.value)){
                    popupService.showToast("库存不足.");
                    //popupService.showToast("库存不足.");
                    return;
                }
            }else{

                var isSkuAvalible = true;
                angular.forEach($scope.selectedSkuId,function(sku){
                    if(sku == null || sku.length == 0){
                        isSkuAvalible = false;
                        return false;
                    }
                });
                if(!isSkuAvalible){
                    popupService.showToast("请选择组成套装所有商品的颜色和尺码.");
                    //popupService.showToast("库存不足.");
                    return;
                }

                var isCountAvalible = true;
                angular.forEach($scope.newCount,function(count){
                    if(parseInt(count) < parseInt($scope.selectedCount.value)){
                        isCountAvalible = false;
                        return false;
                    }
                });
                if(!isCountAvalible){
                    popupService.showToast("组成套装商品的库存不足.");
                    //popupService.showToast("库存不足.");
                    return;
                }


                angular.forEach($scope.productDetailData.goodsList,function(sku,index){
                    skuList.push({
                        "skuId":$scope.selectedSkuId[index]
                    });
                });
            }
            var param  = {
                memberId:userInfo.memberId,
                type:$scope.productDetailData.type,
                goodsId:$scope.productDetailData.goodsId,
                goodsName:$scope.productDetailData.goodsName,
                quantity:$scope.selectedCount.value,
                skuList:skuList
            };
            new shoppingBagService(param).add()
                .then(function (res) {
                    if(res.resultCode=="00"){
                        popupService.showToast("加入成功");
                    }else{
                        popupService.showToast("加入失败,原因:"+res.resultMessage);
                    }

                }, function (error) {
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
        };

        $scope.setWishlist = function (value) {
            userInfo = localStorageService.get(KEY_USERINFO);
            if (!userInfo) {
                $scope.goLogin();
                return;
            }

            $scope.productDetailData.isInWishlist = value;

            if (value == '1') {

                var param = {
                    memberId: userInfo.memberId,
                    type: "10",
                    objectId: $scope.productDetailData.goodsId,
                    name: $scope.productDetailData.goodsName,
                    url: ""
                };
                new favoriteService(param).addFavorite()
                    .then(function (res) {
                        if (res.resultCode == "00") {
                            $scope.collectNo = res.result.collectNo;
                            popupService.showToast("加入成功");
                        } else {
                            popupService.showToast(res.resultMessage);
                        }
                    }, function (error) {
                        popupService.showToast(commonMessage.networkErrorMsg);
                    });

            } else {
                var param = {
                    memberId: userInfo.memberId,
                    collectNo: $scope.collectNo
                };
                new favoriteService(param).deleteFavorite()
                    .then(function (res) {
                        if (res.resultCode == "00") {
                            popupService.showToast("取消成功");
                        } else {
                            popupService.showToast(res.resultMessage);
                        }
                    }, function (error) {
                        popupService.showToast(commonMessage.networkErrorMsg);
                    });
            }
        };

        $scope.refreshSkuPrice = function () {
            //如果某一个sku不存在在列表项目的时候
            var isExist = false;
            $scope.storeInit = false;
            angular.forEach($scope.skuList, function (sku) {
                if (sku.nameKeyValues[0].key == $scope.selectedColor && sku.nameKeyValues[1].key == $scope.selectedSize) {
                    $scope.skuPrice = "¥" + sku.price;
console.log(sku);
                    //大于20 显示无货 20-50显示可能购买 50显示有货
                    $scope.newCountDisplay = 0;
                    if(parseInt(sku.nameKeyValues[1].bnk_no_limit) >  parseInt(sku.newCount)){
                        $scope.newCount = 0;
                    }else if(parseInt(sku.nameKeyValues[1].bnk_no_limit) <  parseInt(sku.newCount)
                        && parseInt(sku.nameKeyValues[1].bnk_less_limit) >  parseInt(sku.newCount)){
                        $scope.newCount = sku.newCount;
                        $scope.newCountDisplay = 1;
                    }else{
                        $scope.newCount = sku.newCount;
                    }

                    $scope.activityPrice = "¥" + sku.activityPrice;
                    $scope.selectedSkuId = sku.skuid;
                    $scope.productDetailData.activityName = sku.activityName;

                    if(sku.activityType != null && sku.activityType == "11"){
                        $('#countdown').show();
                    }else{
                        $('#countdown').hide();
                    }


                    // $scope.skuImage = sku.imgs;

                    // angular.element(document).ready(function() {
                    //     goodDetailBx.reloadSlider();
                    //     bxpager = new Swiper('.swiper-container', {
                    //         pagination: '.swiper-pagination',
                    //         nextButton: '.swiper-button-next',
                    //         prevButton: '.swiper-button-prev',
                    //         direction: 'vertical',
                    //         slidesPerView: 5,
                    //         centeredSlides: false,
                    //         paginationClickable: true,
                    //         spaceBetween: 10,
                    //         initialSlide:0,
                    //         loop: false,
                    //     });
                    //
                    // });

                    isExist = true;
                }


                if (sku.nameKeyValues[0].key == $scope.selectedColor) {
                     $scope.skuImage = sku.imgs;
                     angular.element(document).ready(function() {
                         goodDetailBx.reloadSlider();
                         bxpager = new Swiper('.swiper-container', {
                             pagination: '.swiper-pagination',
                             nextButton: '.swiper-button-next',
                             prevButton: '.swiper-button-prev',
                             direction: 'vertical',
                             slidesPerView: 5,
                             centeredSlides: false,
                             paginationClickable: true,
                             spaceBetween: 10,
                             initialSlide:0,
                             loop: false,
                         });

                     });
                }
            });

            if (!isExist) {
                $scope.newCount = null;
            }
        };

        $scope.refreshSkuPriceForSuit = function (index) {
            $scope.storeInit = false;
            angular.forEach($scope.goodsList[index].skulist, function (sku) {
                if (sku.nameKeyValues[0].key == $scope.selectedColor[index] && sku.nameKeyValues[1].key == $scope.selectedSize[index]) {
                    //$scope.skuPriceFruit[index] = sku.price;
                    //大于20 显示无货 20-50显示可能购买 50显示有货
                    $scope.newCount[index] = sku.newCount;
                    //$scope.skuPriceActivityPriceFruit[index] = sku.activityPrice;
                    $scope.selectedSkuId[index] = sku.skuid;
                    $scope.skuImage = sku.imgs;
                    angular.element(document).ready(function() {
                        goodDetailBx.reloadSlider();

                    });
                }
            });

            var isExist = false;
            jQuery('.storeCount').each(function () {
                if (jQuery(this).text().indexOf('无货') != -1) {
                    isExist = true;
                }
            });
            if (isExist) {
                $scope.isHasStore = "0";
            } else {
                $scope.isHasStore = "1";
            }
        };

        //商品详细画面跳转
        $scope.goProdocutDetail = function (goodsId) {
            $state.go("goodsDetail", {"goodsId": goodsId});
        };

        //checkbox点击动作
        $scope.doCheck = function (index) {
            $scope.checkboxArray[index] = !$scope.checkboxArray[index];

            //在这里要计算搭配了几件商品 搭配之后的价钱 还要是区间价格
            if($scope.checkboxArray[index] == true){
                $scope.totalPriceMin = parseInt($scope.totalPriceMin) + parseInt($scope.minPriceArray[index]);
                $scope.totalPriceMax = parseInt($scope.totalPriceMax) + parseInt($scope.maxPriceArray[index]);
                $scope.productCount = $scope.productCount + 1;
            }else{
                $scope.totalPriceMin = parseInt($scope.totalPriceMin) - parseInt($scope.minPriceArray[index]);
                $scope.totalPriceMax = parseInt($scope.totalPriceMax) - parseInt($scope.maxPriceArray[index]);
                $scope.productCount = $scope.productCount - 1;
            }

            if($scope.totalPriceMin == $scope.totalPriceMax){
                $scope.totalPriceDis = '¥' + $scope.totalPriceMin;
            }else{
                $scope.totalPriceDis = '¥' + $scope.totalPriceMin + '~' + '¥' + $scope.totalPriceMax;
            }

        };

        //商品预约
        $scope.orderPopover = function(goodsId){
            if(!userInfo){
                $scope.goLogin();
                return;
            }
            var scope = $scope.$new();
            scope.param = {
                goodsId: goodsId
            };

            var goodsOrderModalInstance = $modal.open({
                templateUrl: 'html/goodsOrder.html',
                controller: 'goodsOrderCtrl',
                backdrop: true,
                scope: scope
            });

            goodsOrderModalInstance.opened.then(function () {//模态窗口打开之后执行的函数

            });
        }

            $scope.openSizeGuide = function () {
                var scope = $scope.$new();
                scope.goodsType = "";
                var selectGiftModalInstance = $modal.open({
                    templateUrl: 'html/sizeGuide.html',
                    controller: 'sizeGuideCtrl',
                    backdrop: true,
                    scope: scope
                });
                selectGiftModalInstance.opened.then(function () {//模态窗口打开之后执行的函数

                });
                selectGiftModalInstance.result.then(
                    function (result) {
                    }, function (reason) {
                        //点击空白区域，总会输出backdrop click，点击取消，则会暑促cancel
                    });
            };

            $scope.isShowAllRecommand  =false;
            $scope.showAllRecommand = function(){
                $scope.isShowAllRecommand = true;
            }

            $scope.hideAllRecommand = function(){
                $scope.isShowAllRecommand = false;
            }

    }])

    // 预约编辑
    .controller('goodsOrderCtrl', ["$scope", "$state", "$stateParams", '$modalInstance', 'commonService', 'goodsOrderService', 'localStorageService', 'popupService',
        function ($scope, $state, $stateParams, $modalInstance, commonService, goodsOrderService, localStorageService, popupService) {

        var userInfo = localStorageService.get(KEY_USERINFO);

        if(!userInfo){
            $scope.goLogin();
            return;
        }

        var memberId = "";
        if (userInfo) {
            memberId = userInfo.memberId;
        }

        var data = $scope.param;

        //商品预约关闭
        $scope.closeModify = function () {
            $modalInstance.close();
        }

        //商品预约
        $scope.confirmOrder = function (isValid) {
            $scope.submitted = true;

            if(!isValid){
                return;
            }

            var param = {
                goodsId : data.goodsId,
                userName : $scope.goodsOrder.userName,
                telephone : $scope.goodsOrder.telephone,
                comment : $scope.goodsOrder.comment,
                insertUserId : memberId,
                updateUserId : memberId
            }

            new goodsOrderService(param).then(function (res) {
                    if (res.resultCode == "00") {
                        popupService.showToast('预约成功');
                        $modalInstance.close();
                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function (error) {
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
        }
    }])
    // 尺码大小指南
    .controller('sizeGuideCtrl', ["$scope","$modalInstance",
        function ($scope,$modalInstance) {
            //关闭
            $scope.close = function () {
                $modalInstance.close();
            }

        }])

    // 登录注册 start
    .controller('loginCtrl', function ($scope, $state,
                                       localStorageService, $rootScope,
                                       $modalInstance, authService, commonService, popupService) {


        $scope.loginForm = {
            tel: '',
            password: ''
        };

        $scope.registerInfo = {tel: '', captcha: '', password: '', rePassword: '', name: '', agree: false};

        $scope.restForm = {tel: '', captcha: '', password: '', rePassword: ''};

        $scope.showPassword = false;

        $scope.changeShowPassword = function(){
          $scope.showPassword = !$scope.showPassword;
        };

        $scope.registerAgree = function(){
          $scope.registerInfo.agree = !$scope.registerInfo.agree;
        };

        $scope.login = function (isValid) {

            $scope.submitted_login = true;

            if(!isValid){
                return;
            }

            var param = {
                telephone: $scope.loginForm.tel,
                password: $.md5($scope.loginForm.password)
            };

            new authService(param).login()
                .then(function (res) {

                    if (res.resultCode == '00') {

                        //popupService.showToast('登录成功');
                        localStorageService.set(KEY_USERINFO, res.data);

                        $modalInstance.close();

                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function (error) {
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
        };


        $scope.getRegisterVerifyCode = function (isValid) {

            $scope.submitted_captcha = true;
            $scope.submitted_register = false;

            if(!isValid){
                return;
            }

            dealunaTimer.countDown('captcha_reg_btn');
            $scope.getVerifyCode($scope.registerInfo.tel, '10');

        };

        $scope.getRestVerifyCode = function (isValid) {

            $scope.submitted_reset = false;
            $scope.submitted_captcha = true;

            if(!isValid){
                return;
            }

            dealunaTimer.countDown('captcha_reset_btn');
            $scope.getVerifyCode($scope.restForm.tel, '40');
        };

        $scope.getVerifyCode = function (tel, type) {
            //发送验证码
            var param = {"mobile": tel, "orgId": type};
            new commonService(param).sendCaptcha()
                .then(function (res) {
                    if (res.resultCode == "00") {
                        popupService.showToast("验证码已经发送到您的手机");
                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function (error) {
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
        };
        // 注册
        $scope.register = function (isValid) {

            $scope.submitted_register = true;
            $scope.submitted_captcha = false;

            if(!isValid){
                return;
            }

            var param = {
                telephone: $scope.registerInfo.tel,
                password: $.md5($scope.registerInfo.password),
                captcha: $scope.registerInfo.captcha,
                memberName: $scope.registerInfo.name,
                sex: $scope.registerInfo.sex,
                birthdate: $('#inputBirthDate').val()
            };

            new authService(param).register()
                .then(function (res) {
                    if (res.resultCode == "00") {
                        localStorageService.set(KEY_USERINFO, res.data);
                        popupService.showToast('注册成功,请妥善保管你的密码');

                        $modalInstance.close();
                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function (error) {
                    popupService.showToast(commonMessage.networkErrorMsg);
                });

        };

        $scope.restPassword = function (isValid) {

            $scope.submitted_reset = true;
            $scope.submitted_captcha = false;

            if(!isValid){
                return;
            }

            var param = {
                telephone: $scope.restForm.tel,
                password: $.md5($scope.restForm.password),
                captcha: $scope.restForm.captcha
            };

            new authService(param).changePassword()
                .then(function (res) {
                    if (res.resultCode == "00") {
                        localStorageService.set(KEY_USERINFO, res.data);
                        popupService.showToast('修改成功,请妥善保管你的密码');

                        $modalInstance.close();
                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function (error) {
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
        };

        $scope.goRegister = function () {

            // 性别options
            $scope.genderList = {
                '--请选择--': "0",
                '男': "1",
                '女': "2"
            };

            $scope.registerInfo = {tel: '', captcha: '', password: '', rePassword: '', name: '', agree: false, sex:'0', birthdate:''};

            $scope.type = 'register';

            // 验证码继续
            angular.element(document).ready(function() {
                dealunaTimer.init('captcha_reg_btn');

                // 日期插件
                $("#inputBirthDate").cxCalendar();
            });

        };

        $scope.goRestPassword = function () {

            $scope.restForm = {tel: '', captcha: '', password: '', rePassword: ''};

            $scope.type = 'restPassword';

            // 验证码继续
            angular.element(document).ready(function() {
                dealunaTimer.init('captcha_reset_btn');
            });
        };

        $scope.showPrivacy = function(){
            $scope.type='privacy';
        }
    })
    // 登录注册 end
    // 购物袋 start
    .controller('bagCtrl', function ($scope, $state, $rootScope,
                                     localStorageService, shoppingBagService, popupService, confirmOrderService) {

        var userInfo = localStorageService.get(KEY_USERINFO);


        $(".shoppingBagStep").loadStep({

            size: "large",
            color: "blue",
            //clickCb:function(index) {
            //    alert('clickCb==' + index);
            //},

            steps: [{
                //步骤名称
                stepTag:'1',
                title: "挑选商品",
                time: '',
                //cb:function(index){alert(index);}
            }, {
                stepTag:'2',
                title: "填写订单",
                time: ''
            }, {
                stepTag:'3',
                title: "提交订单",
                time: ''
            }]
        });

        var userInfo = localStorageService.get(KEY_USERINFO);

        $scope.init = function () {

            if(!userInfo){
                $scope.goodsList = [];
                return;
            }
            $scope.totalPriceStr = 0;
            $scope.totalCheckedStr = 0;
            $scope.allCheck = {};
            $scope.allCheck.checked = false;
            var param = {
                memberId: userInfo.memberId,
                lastUpdateTime: null
            };
            $scope.getList(param);
        };
        $scope.isLoading = false;
        $scope.getList = function(param){
            if($scope.isLoading){
                return;
            }
            $scope.isLoading  =true;
            new shoppingBagService(param).getList()
                .then(function (res) {
                    $scope.isLoading = false;
                    if (res.resultCode == "00") {
                        $scope.goodsList = res.results;
                        // do something
                        // 设置选中的活动
                        angular.forEach($scope.goodsList, function (goods, index) {
                            if (goods.actGoodsId == null || !goods.activityList || goods.activityList.length == 0) {
                                $scope.goodsList[index].activity = null;
                                $scope.goodsList[index].actGoodsId = "";
                            } else {
                                angular.forEach(goods.activityList, function (activity, index2) {
                                    if (goods.actGoodsId == activity.activityId) {
                                        $scope.goodsList[index].activity = activity;
                                    }
                                });
                            }
                        });
                        // 计算套装的原价
                        angular.forEach($scope.goodsList, function (goods, index) {
                            if (goods.type == "30") {
                                var price = 0;
                                angular.forEach(goods.skuList, function (sku, index2) {
                                    price += sku.price;
                                });
                                goods.price = price.toFixed(2);
                            }
                        });
                        //angular.element(document).ready(function() {
                        //    $('.numSpinner').spinner({min:1,value:1});
                        //});
                        if (res.length != 0) {
                            angular.element(document).ready(function() {
                                var doAfterChange = function (id, value) {
                                    var index = parseInt(id.split("quantity_")[1]);
                                    $scope.changeQuantity(index, value);
                                };
                                $('.numSpinner').spinner({callback: doAfterChange, min: 1});
                            });
                        }
                        $scope.setTotalPrice();
                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function () {
                    $scope.isLoading = false;
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
        };

        //调用后台修改购物车件数
        $scope.changeQuantity = function(index, value){

            var goodItem = $scope.goodsList[index];

            goodItem.quantity = value;

            var param = {
                bagNo:goodItem.bagNo,
                memberId:userInfo.memberId,
                quantity:goodItem.quantity
            };
            new shoppingBagService(param).changeQuantity()
                .then(function (res) {
                    if(res.resultCode=="00"){
                        $scope.setTotalPrice();
                    }else{
                        popupService.showToast("修改数量失败,原因:"+res.resultMessage);
                    }
                }, function (error) {
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
        };
        //调用后台修改购物车活动
        $scope.changeActivity = function(index){
            var param = {
                bagNo:$scope.goodsList[index].bagNo,
                memberId:userInfo.memberId,
                actGoodsId:$scope.goodsList[index].actGoodsId
            };
            new shoppingBagService(param).changeActivity()
                .then(function (res) {
                    if(res.resultCode=="00"){
                        $scope.setTotalPrice();
                    }else{
                        popupService.showToast("修改活动失败,原因:"+res.resultMessage);
                    }
                }, function (error) {
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
        };

        $scope.goShopping = function (){
            $state.go("homepage");
        };
        // 结算
        $scope.setTotalPrice = function () {
            var totalPrice = 0;
            var totalCount = 0;
            var goodsCount = 0;
            angular.forEach($scope.goodsList, function (goods, index) {
                if(goods.isChecked){
                    goodsCount ++;
                    totalCount += parseInt(goods.quantity);
                    if (goods.activity == null) {
                        // 没有活动
                        var singlePrice = 0;
                        angular.forEach(goods.skuList, function (sku, index2) {
                            singlePrice += sku.price;
                            /*// todo没有活动的商品高级会员打88折
                            if(userInfo.memberLevelId == '30'){
                                singlePrice+=sku.price*0.88
                            }else{
                                singlePrice += sku.price;
                            }*/
                        });
                        totalPrice += singlePrice * parseInt(goods.quantity);
                    } else {
                        //有活动按照活动价
                        totalPrice += goods.activity.newPrice * parseInt(goods.quantity);
                        // todo有活动的如果不是5折活动高级会员都打88折
                       /* if(goods.activity.tempId != '4cecaf45-b443-474f-a90c-6eebdd670e87'){
                            totalPrice += goods.activity.newPrice*0.88 * parseInt(goods.quantity);
                        }else{
                            totalPrice += goods.activity.newPrice * parseInt(goods.quantity);
                        }*/
                    }
                }
            });
            $scope.totalPriceStr = totalPrice.toFixed(2);
            $scope.totalCheckedStr = totalCount;

            if(goodsCount > 0 && goodsCount == $scope.goodsList.length){
                $scope.allCheck.checked = true;
            } else {
                $scope.allCheck.checked = false;
            }
        };

        $scope.getPriceStrFixed = function (price){
            if(price - parseInt(price) == 0){
                return price.toFixed(2);
            } else {
              return price;
            }
        };

        $scope.goProdocutDetail = function (goodsId) {
            $state.go("goodsDetail", {"goodsId": goodsId});
        };

        $scope.setSelectedActivity = function(index){
            if($scope.goodsList[index].actGoodsId == ""){
                $scope.goodsList[index].activity = null;
            }else{
                angular.forEach($scope.goodsList[index].activityList, function (activity, index2) {
                    if($scope.goodsList[index].actGoodsId == activity.activityId){
                        $scope.goodsList[index].activity = activity;
                    }
                });
            }
            $scope.changeActivity(index);
        };

        $scope.onAllCheckChange = function(){
            if($scope.allCheck.checked){
                angular.forEach($scope.goodsList, function (goods, index) {
                    goods.isChecked = true;
                });
            }else{
                angular.forEach($scope.goodsList, function (goods, index) {
                    goods.isChecked = false;
                });
            }
            $scope.setTotalPrice();
        };


        $scope.removeFromBag = function (index) {
            popupService.showConfirm('确定要将此商品移出购物袋吗?', function (){
                var param = {
                    bagNo:$scope.goodsList[index].bagNo,
                    memberId:userInfo.memberId
                };
                new shoppingBagService(param).remove()
                    .then(function (res) {
                        if(res.resultCode=="00"){
                            $scope.goodsList.splice(index,1);
                            $scope.setTotalPrice();
                        }else{
                            popupService.showToast("删除活动失败,原因:"+res.resultMessage);
                        }
                    }, function (error) {
                        popupService.showToast(commonMessage.networkErrorMsg);
                    });
            })
        };

        // 点击结算
        $scope.confirmOrder = function () {
            var submitGoodsList = [];
            angular.forEach($scope.goodsList, function (goods, index) {
                if(goods.isChecked){
                    submitGoodsList.push(goods);
                }
            });
            if(submitGoodsList.length==0){
                popupService.showToast("请选择要结算的商品.");
                return;
            }
            $scope.checkOrderConfirm(submitGoodsList);

        };

        $scope.checkOrderConfirm = function (submitGoodsList) {
            var bagNoArray = [];
            angular.forEach(submitGoodsList, function (bag, index) {
                bagNoArray.push(bag.bagNo);
            });
            var param = {
                memberId: userInfo.memberId,
                bagNoArray: bagNoArray
            };
            new confirmOrderService(param).checkDataByBag().then(function (res) {
                if(res.resultCode == "00"){
                    $rootScope.shoppingBagsOfConfirmOrder = undefined;
                    $rootScope.singleGoodsInfoOfConfirmOrder = undefined;
                    $rootScope.suitGoodsInfoOfConfirmOrder = undefined;
                    //console.log(submitGoodsList);
                    resultPath=1;
                    localStorageService.set(KEY_PARAM_COMFIRM_ORDER, submitGoodsList);

                    $state.go("personalCenter.confirmOrder", {shoppingBags: '#'});
                }else{
                    popupService.showToast(res.resultMessage);
                }
            }, function (error) {
                popupService.showToast(commonMessage.networkErrorMsg);
            });
        };

        $scope.init();
    })





    // 购物袋 end
        // 订单详情画面 start
        .controller('orderDetailCtrl', function ($scope,$rootScope, $state,$stateParams, localStorageService,$filter,
                                                 orderService, popupService,$modal,WeiXinService) {
            var userInfo = localStorageService.get(KEY_USERINFO);

            if($stateParams.orderId != null
                && $stateParams.orderId.length > 0){
                $rootScope.orderId = $stateParams.orderId;
            }

            // 初始化订单详情画面
            $scope.initData = function () {
                var param = new Object();
                param.orderId = $rootScope.orderId;
                new orderService().showOrderDetail(param)
                    .then(function (res) {
                        console.log(res);
                            if (res.resultCode == "00") {
                                $scope.orderDetail = res.aaData;
                                console.log($scope.orderDetail);
                                $scope.initStep();

                            } else {
                                popupService.showToast(res.resultMessage);
                            }
                        }, function (error) {
                            popupService.showToast(commonMessage.networkErrorMsg);

                        }
                    );
            };

            $scope.initStep = function(){
                $(".orderStep").empty().loadStep({
                    //ystep的外观大小
                    //可选值：small,large
                    size: "large",
                    //ystep配色方案
                    //可选值：green,blue
                    color: "blue",
                    //ystep中包含的步骤
                    steps: [{
                        //步骤名称
                        stepTag:'1',
                        title: "提交订单",
                        time: $scope.showTime($scope.orderDetail.orderTime)
                    }, {
                        stepTag:'2',
                        title: "付款成功",
                        time: $scope.showTime($scope.orderDetail.payTime)
                    }, {
                        stepTag:'3',
                        title: "商品出库",
                        time: $scope.showTime($scope.orderDetail.deliverTime)
                    }, {
                        stepTag:'4',
                        title: "确认收货",
                        time: $scope.showTime($scope.orderDetail.receiptTime)
                    }, {
                        stepTag:'5',
                        title: "完成",
                        time: ''
                    }]
                });


                if ($scope.orderDetail.orderStatus == 11) {
                    <!-- 11.订单取消-->
                    $(".orderStep").setStep(0);
                } else if ($scope.orderDetail.orderStatus == 10 && ($scope.orderDetail.payStatus == 10 || $scope.orderDetail.payStatus == 21)) {
                    <!-- 10.订单未完成 and (10.未付款 or 21.付款失败) -->
                    $(".orderStep").setStep(1);
                } else if (($scope.orderDetail.orderStatus == 10 || $scope.orderDetail.orderStatus == 23) && $scope.orderDetail.payStatus == 20 && $scope.orderDetail.deliverStatus == 10) {
                    <!-- 10.订单未完成 and 20.付款成功 and 20.全部发货 -->
                    $(".orderStep").setStep(2);
                } else if (($scope.orderDetail.orderStatus == 10 || $scope.orderDetail.orderStatus == 23) && $scope.orderDetail.payStatus == 20 && $scope.orderDetail.deliverStatus == 20) {
                    $(".orderStep").setStep(3);
                } else if (($scope.orderDetail.orderStatus == 10 || $scope.orderDetail.orderStatus == 23) && $scope.orderDetail.deliverStatus == 21 && $scope.orderDetail.payStatus == 20) {
                    $(".orderStep").setStep(5);
                } else if ($scope.orderDetail.orderStatus == 90) {
                    $(".orderStep").setStep(5);
                } else if ($scope.orderDetail.orderStatus == 30) {
                    <!-- 退货申请中 -->
                    $(".orderStep").setStep(5);
                } else if ($scope.orderDetail.orderStatus == 31) {
                    <!-- 退货中 -->
                    $(".orderStep").setStep(5);
                } else if ($scope.orderDetail.orderStatus == 32) {
                    <!-- 退货 -->
                    $(".orderStep").setStep(5);
                }
            };

            $scope.initData();

            $scope.showTime = function (time){
                if(time && time > 0){
                    return $filter('date')(time, 'MM-dd hh:mm');
                }
                return '';
            };
            // 支付
            $scope.doPay = function (orderId,orderCode) {
                var scope = $scope.$new();
                var selectPayModalInstance = $modal.open({
                    templateUrl: 'html/selectPay.html',
                    controller: 'selectPayCtrl',
                    backdrop: "static",
                    keyboard: false,
                    scope: scope
                });
                selectPayModalInstance.opened.then(function () {//模态窗口打开之后执行的函数

                });
                selectPayModalInstance.result.then(
                    function (result) {
                        if (result) {
                            if("10" == result.type){
                                //支付测试
                                $('#orderId').val($rootScope.orderId);
                                $('#ordMaster')[0].action = '/qyds-web-pc/alipay/orderAliPay.json';
                                $('#ordMaster')[0].submit();
                            }else if("20" == result.type){
                                $scope.getWXPayInfo();
                            }else if("30" == result.type){
                                $('#orderId').val($rootScope.orderId);
                                $('#ordMaster')[0].action = '/qyds-web-pc/unionpay/orderUnionPay.json';
                                $('#ordMaster')[0].submit();
                            }
                        }
                    }, function (reason) {
                        //点击空白区域，总会输出backdrop click，点击取消，则会暑促cancel
                    });
            };

            $scope.getWXPayInfo = function () {
                var params = {
                    "openid": "",
                    "product_id":$scope.orderDetail.orderCode,
                    "body": "商品订单"+$scope.orderDetail.orderCode,
                    "out_trade_no": $scope.orderDetail.orderCode,
                    "total_fee": (parseFloat($scope.orderDetail.payInfact)*100).toFixed(0),
                    "trade_type": "NATIVE"
                };

                new WeiXinService(params).getWxPayInfo().then(function (response) {
                    if (response.resultCode == '00') {
                        var codeUrl  = response.results.code_url;
                        var scope = $scope.$new();
                        scope.codeUrl = codeUrl;
                        var wxPayModalInstance = $modal.open({
                            templateUrl: 'html/wxPay.html',
                            controller: 'wxPayCtrl',
                            backdrop: "static",
                            keyboard: false,
                            scope: scope
                        });
                        wxPayModalInstance.opened.then(function () {//模态窗口打开之后执行的函数

                        });
                        wxPayModalInstance.result.then(
                            function (result) {
                                $scope.initData();
                            }, function (reason) {
                                $scope.initData();
                            });
                    } else {
                        popupService.showToast("支付失败,原因:" + response.resultMessage);
                    }
                }, function (error) {
                    popupService.showToast("支付失败,请稍后重试");
                });
            };

            // 跳转到商品详细
            $scope.goGoodsDetail = function (gdsId) {
                $state.go("goodsDetail", {"type" : gdsId});
            };

            // 删除订单
            $scope.deleteOrder = function (orderId) {
                popupService.showConfirm('确定要删除此订单吗?', function(){
                    var param = {
                        "memberId": userInfo.memberId,
                        "orderId": orderId
                    };

                    new orderService(param).deleteOrder()
                        .then(function (res) {
                                if (res.resultCode == "00") {
                                    popupService.showToast("订单已删除。");
                                    $state.go("personalCenter.orderList");
                                } else {
                                    popupService.showToast(res.resultMessage);
                                }
                            }, function (error) {
                            popupService.showToast(commonMessage.networkErrorMsg);
                            }
                        );
                });
            };

            // 取消订单
            $scope.cancelOrder = function (orderId) {
                popupService.showConfirm('确定要取消此订单吗?', function() {
                    var param = {
                        "memberId": userInfo.memberId,
                        "orderId": orderId
                    };

                    new orderService(param).cancelOrder()
                        .then(function (res) {
                                if (res.resultCode == "00") {
                                    popupService.showToast("订单已取消。");
                                    $scope.initData();
                                } else {
                                    popupService.showToast(res.resultMessage);
                                }
                            }, function (error) {
                            popupService.showToast(commonMessage.networkErrorMsg);
                            }
                        );
                });
            };

            // 确认收货
            $scope.confirmReceiptInMaster = function (orderId) {
                popupService.showConfirm('确定已收到此订单中所有的商品了吗?', function() {
                    var param = {
                        "memberId": userInfo.memberId,
                        "orderId": orderId
                    };

                    new orderService(param).confirmReceiptInMaster()
                        .then(function (res) {
                                if (res.resultCode == "00") {
                                    popupService.showToast("订单已确认收货。");
                                    $scope.initData();
                                } else {
                                    popupService.showToast(res.resultMessage);
                                }
                            }, function (error) {
                            popupService.showToast(commonMessage.networkErrorMsg);
                            }
                        );
                });
            };

            // 退货（全单）
            $scope.refunds = function (orderItem) {
                localStorageService.set(KEY_PARAM_REFUND_ORDER,orderItem);
                $state.go("personalCenter.applyReturnGoods");
            };

            // 申请退款
            $scope.applyRefund = function (orderId) {
                $state.go("personalCenter.applyRefund", {orderId:orderId});
            };

            // 物流详情
            $scope.logisticsDetail = function () {
                $state.go("personalCenter.logisticsList", {"orderId": $state.params.orderId});
            };

            // 跳转物流信息
            $scope.goLogisticsList = function (gdsId) {
                $state.go("personalCenter.logisticsList", {"orderId": gdsId});
            };
        })
        // 订单详情画面 end
        // 物流详情画面 start
    .controller('logisticsListCtrl', function ($scope,$rootScope, $stateParams, $state,
                                                  orderService, localStorageService, logisticsService, popupService) {
        var orderId = "";
        if($stateParams.orderId != null
            && $stateParams.orderId.length > 0){
            $rootScope.orderIdLogistics = $stateParams.orderId;
            orderId = $stateParams.orderId;
        }else{
            orderId = $rootScope.orderIdLogistics;
        }

        $scope.doRefresh = function () {
            $scope.logistics = [];
            $scope.getData();
        };

        $scope.getData = function (){

            var param = {
                orderId : orderId
            };

            new logisticsService(param).then(function (res) {
                if(res.resultCode == '00'){
                    $scope.logistics = res.results;
                }else{
                    $scope.logistics = [];
                    popupService.showToast("物流信息取得失败");
                }
            }, function (error) {
                $scope.logistics = [];
                popupService.showToast(commonMessage.networkErrorMsg);
            });
        };

        $scope.getData();

        $scope.confirmReceived = function (orderId, expressNo){
            popupService.showConfirm('确定已收到商品了吗?', function() {
                var userInfo = localStorageService.get(KEY_USERINFO);

                var param = {
                    memberId: userInfo.memberId,
                    orderId: orderId,
                    expressNo: expressNo
                };

                new orderService().confirmReceived(param)
                    .then(function (res) {
                            if (res.resultCode == "00") {
                                popupService.showToast("订单已确认收货。");
                                $scope.doRefresh();
                            } else {
                                popupService.showToast(res.resultMessage);
                            }
                        }, function (error) {
                            popupService.showToast(commonMessage.networkErrorMsg);
                        }
                    );
            });
        };
    })
    // 物流详情画面 end
    //
    //    // 退货画面 start
    //    .controller('refundsCtrl', function ($scope,$rootScope, $state,$stateParams, $ionicHistory, $ionicTabsDelegate, localStorageService, popupService, orderService) {
    //        $rootScope.setTitle("退货");
    //        var userInfo = localStorageService.get(KEY_USERINFO);
    //
    //        // 从前一页传过来的订单信息
    //        $scope.refundsInfo = $stateParams.orderItem;
    //        ionic.DomUtil.ready(function () {
    //            $jq('.refundsNumSpinner').spinner({});
    //            // 退货件数
    //            for(var i = 0; i < $scope.refundsInfo.ordList.length; i++) {
    //                var goods = $scope.refundsInfo.ordList[i];
    //                goods.newQuantity = goods.quantity;
    //            }
    //            // 退货原因
    //            $scope.refundsInfo.applyComment = "";
    //            // 退换货地点 (10.电商，20.门店)，默认电商退货。
    //            $scope.refundsInfo.rexPoint = "10";
    //
    //            // 获取门店信息
    //            new orderService().getOrgList()
    //                .then(function (res) {
    //                    if (res.resultCode == '99') {
    //                        popupService.showAlert("", "获取门店信息过程中出现问题，请联系？？？!");
    //                        return;
    //                    } else {
    //                        $scope.orgList = res.data;
    //                    }
    //                }, function (error) {
    //                    $scope.hasMore = false;
    //                }
    //            );
    //
    //        });
    //
    //        $scope.changeRexPoint = function (rexP) {
    //            $scope.refundsInfo.rexPoint = rexP;
    //        }
    //
    //        // 退货
    //        $scope.refundOrder = function () {
    //            // 退货数量
    //            var returnCount = "";
    //            // 子订单id
    //            var detailId = "";
    //            // 判断用户输入的退货数量是不是全都是0.
    //            var allZero = true;
    //            // 输入值check
    //            // 该订单允许拆单退货
    //            if ($scope.refundsInfo.canDivide == '1') {
    //                // 检测客户选择的退货数量是否超过已购买商品的数量
    //                for(var i = 0; i < $scope.refundsInfo.ordList.length; i++) {
    //                    var goods = $scope.refundsInfo.ordList[i];
    //
    //                    if (goods.newQuantity > goods.quantity) {
    //                        popupService.showAlert("", "此订单中的第" + (i + 1) + "件商品：【" + goods.goodsName + "】所输入的退货数量超过了您购买的该商品数量。请重新输入该商品的退货数量。");
    //                        return;
    //                    }
    //                    if(goods.newQuantity != 0) {
    //                        allZero = false;
    //                    }
    //                    detailId += goods.detailId + ",";
    //                    returnCount += goods.newQuantity + ",";
    //                }
    //
    //                // 用户输入的退货数量全都是0
    //                if (allZero) {
    //                    popupService.showAlert("", "请输入想要退货的数量。");
    //                    return;
    //                }
    //            }
    //
    //            if ($scope.refundsInfo.applyComment == '') {
    //                popupService.showAlert("", "请输入您的退货理由。");
    //                return;
    //            }
    //
    //            popupService.showConfirm("申请退货", "确定要申请退货所选中的商品吗？", function () {
    //
    //                var param = {// TODO 临时数据！！！！！
    //                    "memberId": userInfo.memberId,
    //                    "orderId": $scope.refundsInfo.orderId,
    //                    "applyComment": $scope.refundsInfo.applyComment,
    //                    "returnExchangeExt": "10",
    //                    "rexPoint": $scope.refundsInfo.rexPoint
    //                };
    //
    //                if ($scope.refundsInfo.rexPoint == '20') {
    //                    param.rexStoreId = $scope.refundsInfo.rexStoreId.org_id;
    //                }
    //
    //                // 该订单允许拆单退货
    //                if ($scope.refundsInfo.canDivide == '1') {
    //                    param.detailId = detailId.substr(0, detailId.length - 1);
    //                    param.returnCount = returnCount.substr(0, returnCount.length - 1);
    //                    new orderService().applyReturnSubGoods(param)
    //                        .then(function (res) {
    //                                if (res.resultCode == "00") {
    //                                    popupService.showAlert("", "退货申请已提交，请耐心等待客服人员与您联系。");
    //                                    $scope.goBack();
    //                                } else {
    //                                    popupService.showToast(res.resultMessage);
    //                                }
    //                            }, function (error) {
    //                                popupService.showToast(commonMessage.networkErrorMsg);
    //                            }
    //                        );
    //                } else {
    //                    new orderService().applyReturnGoods(param)
    //                        .then(function (res) {
    //                                if (res.resultCode == "00") {
    //                                    popupService.showAlert("", "退货申请已提交，请耐心等待客服人员与您联系。");
    //                                    $scope.goBack();
    //                                } else {
    //                                    popupService.showToast(res.resultMessage);
    //                                }
    //                            }, function (error) {
    //                                popupService.showToast(commonMessage.networkErrorMsg);
    //                            }
    //                        );
    //                }
    //            });
    //        }
    //
    //        $scope.goBack = function () {
    //            $ionicHistory.goBack();
    //        }
    //    })
    //    // 退货画面 end
    //
    // 确认订单 start
    .controller('confirmOrderCtrl', function ($scope, $state, $stateParams, $rootScope, $modal,
                                              localStorageService,
                                              confirmOrderService,
                                              shoppingBagService,
                                              addressService,
                                              checkInputTelService,
                                              storeService, popupService,commonService,WeiXinService) {

        $(".confirmStep").loadStep({
            //ystep的外观大小
            //可选值：small,large
            size: "large",
            //ystep配色方案
            //可选值：green,blue
            color: "blue",
            //ystep中包含的步骤
            steps: [{
                //步骤名称
                stepTag:'1',
                title: "挑选商品",
                time: ''
            }, {
                stepTag:'2',
                title: "填写订单",
                time: ''
            }, {
                stepTag:'3',
                title: "提交订单",
                time: ''
            }]
        });
        $(".confirmStep").setStep(2);

        var paramOfConfirmOrder = localStorageService.get(KEY_PARAM_COMFIRM_ORDER);

        if(paramOfConfirmOrder){

            if ($stateParams.shoppingBags) {
                $rootScope.shoppingBagsOfConfirmOrder = paramOfConfirmOrder;
            }
            if ($stateParams.singleGoodsInfo) {
                $rootScope.singleGoodsInfoOfConfirmOrder = paramOfConfirmOrder;
            }
            if ($stateParams.suitGoodsInfo) {
                $rootScope.suitGoodsInfoOfConfirmOrder = paramOfConfirmOrder;
            }
        }

        if($rootScope.commentMessage){
            $scope.comment = {
                message : $rootScope.commentMessage
            };
        } else {
            $scope.comment = {
                message: ''
            }
        }

        var userInfo = localStorageService.get(KEY_USERINFO);
        //TODO 这块之后要改成==30

        $scope.info = {provinceCd: '0', cityCd: '0', areaCd: '0'};//初始化联动下拉
        $scope.selectedOrderActivity = {
            id:-1
        };
        $scope.selectedCoupon = {};
        // 商品总价
        $scope.goodsTotalPrice = 0.00;
        // 订单参加活动后的价格
        $scope.orderDiscountPrice = 0.00;
        // 订单最终价格
        $scope.orderFinalPrice = 0.00;

        $scope.goodsExchangePointCount = 0;

        $scope.exchangePointCount = 0;

        $scope.activityPointCount = 0;

        $scope.couponList = [];

        $scope.selectedDeliveryTab = "10";

        $scope.storeList = [];
        $scope.showAllAddressFlag = "0";
        $scope.showAllStoreFlag = "0";

        //30是门店的话要显示 输入会员电话
        $scope.agentFlag = 0;
        $scope.conInfo = {
            tel:"",
            captcha:""
        }
        //TODO 这块之后要改成==30
        if(userInfo.type == '30'){
            $scope.agentFlag = 1;
        }
        $scope.isCanSubmit = true;
        $scope.telOnBlur = function(){
            var tel = $('#tel').val();
            var param = {
                tel: tel
            };
            new checkInputTelService(param).checkInputTel().then(function (res) {
                if(res.resultCode == "00"){
                    if(res.results.length == 0){
                        popupService.showToast("不存在输入的会员!");
                        $scope.isCanSubmit = false;
                    }else{
                        $scope.isCanSubmit = true;
                    }
                }else{
                    popupService.showToast(commonMessage.networkErrorMsg);
                }
            }, function (error) {
                popupService.showToast(commonMessage.networkErrorMsg);
            });
        };

        //初始化函数
        $scope.init = function () {

            $scope.shoppingBags = $rootScope.shoppingBagsOfConfirmOrder;
            $scope.singleGoodsInfo = $rootScope.singleGoodsInfoOfConfirmOrder;
            $scope.suitGoodsInfo = $rootScope.suitGoodsInfoOfConfirmOrder;

            // 选择地址后画面信息更新
            $scope.addressInfo = $rootScope.selectedAddressInfo;

            $scope.shopInfo = $rootScope.selectedShopInfo;

            // 开票信息
            $scope.invoiceInfo = $rootScope.invoiceInfo;
            if ($rootScope.selectedDeliveryTab) {
                if($scope.selectedDeliveryTab == null || $scope.selectedDeliveryTab.length == 0){
                    $scope.selectedDeliveryTab = $rootScope.selectedDeliveryTab;
                }
            }

            if ($scope.shoppingBags) {
                $scope.getOrderConfirmByShoppingBag();
            } else if ($scope.singleGoodsInfo) {
                $scope.getOrderConfirmBySingle();
            } else if ($scope.suitGoodsInfo) {
                $scope.getOrderConfirmBySuit();
            } else {
                popupService.showAlert("商品信息更新了,请返回首页刷新重试.", function (){
                    history.go(-1);
                });
            }
            $scope.getAddressList(false);

        };

        $scope.getOrderConfirmByShoppingBag = function () {
            var bagNoArray = [];
            angular.forEach($scope.shoppingBags, function (shopingBag, index) {
                bagNoArray.push(shopingBag.bagNo);
            });
            var param = {
                memberId: userInfo.memberId,
                bagNoArray: bagNoArray
            };
            new confirmOrderService(param).getDataByBag().then(function (res) {
                if(res.resultCode == "00"){
                    $scope.processConfirmData(res.results);
                }else{
                    popupService.showToast(res.resultMessage, function (){
                        setTimeout(function () {
                            history.go(-1);
                        },2000);
                    });
                }
            }, function (error) {
                popupService.showToast(commonMessage.networkErrorMsg);
            });
        };

        $scope.getOrderConfirmBySingle = function () {
            var param = {
                memberId: userInfo.memberId,
                goodsId: $scope.singleGoodsInfo.goodsId,
                type: $scope.singleGoodsInfo.type,
                goodsSkuId: $scope.singleGoodsInfo.skuId,
                quantity: $scope.singleGoodsInfo.quantity
            };
            new confirmOrderService(param).getDataBySingleGoods().then(function (res) {
                if(res.resultCode == "00"){
                    $scope.processConfirmData(res.results);
                }else{
                    popupService.showToast(res.resultMessage, function (){
                        setTimeout(function () {
                            history.go(-1);
                        },2000);
                    });
                }
            }, function (error) {
                popupService.showToast(commonMessage.networkErrorMsg);
            });
        };

        $scope.getOrderConfirmBySuit = function () {
            var skuArray = [];
            angular.forEach($scope.suitGoodsInfo.skuList, function (sku, index) {
                skuArray.push({
                    "goodsId":sku.goodsId,
                    "goodsSkuId":sku.goodsSkuId
                });
            });
            var param = {
                memberId: userInfo.memberId,
                goodsId: $scope.suitGoodsInfo.goodsId,
                gdIdAndSkuIdJson: JSON.stringify(skuArray),
                quantity: $scope.suitGoodsInfo.quantity
            };
            new confirmOrderService(param).getDataBySuitGoods().then(function (res) {
                if(res.resultCode == "00"){
                    $scope.processConfirmData(res.results);
                }else{
                    popupService.showToast(res.resultMessage, function (){
                        setTimeout(function () {
                            history.go(-1);
                        },2000);
                    });
                }
            }, function (error) {
                popupService.showToast(commonMessage.networkErrorMsg);
            });
        };

        $scope.processConfirmData = function (result) {
            console.log(result);
            $scope.confirmData = result;
            var selectAddressInfo = $rootScope.selectedAddressInfo;
            if(selectAddressInfo && selectAddressInfo.addressId){
                $scope.addressInfo = selectAddressInfo;
            }else{
                $scope.addressInfo = $scope.confirmData.mmbAddressExt;
            }

            if("1" == userInfo.isSaller){
                $scope.confirmData.actMasterList = [];
            }
            if($scope.confirmData.actMasterList!=null&&$scope.confirmData.actMasterList.length>0){
                $scope.selectedOrderActivity.id = $scope.confirmData.actMasterList[0].activityId;
            }
            $scope.setGoodsTotalPrice();
            $scope.setOrderFinalPrice();
            $scope.getCouponList();
            // 获取
            $scope.getMasterD();
        };

        $scope.checkOrderConfirm2 = function (submitGoodsList) {
            // 购物车编码
            var bagNoArray = [];
            bagNoArray=$scope.confirmData.bagNoArray;
            var param = {
                memberId: userInfo.memberId,
                memberPhone:$scope.conInfo.tel,
                bagNoArray: bagNoArray
            };
            new confirmOrderService(param).getDataByBag().then(function (res) {
                if(res.resultCode == "00"){
                   /* var submitGoodsList1 = [];
                    angular.forEach(res.results.goodsInfo, function (goods, index) {
                        submitGoodsList1.push(goods);
                    });*/
                    $rootScope.shoppingBagsOfConfirmOrder = undefined;
                    $rootScope.singleGoodsInfoOfConfirmOrder = undefined;
                    $rootScope.suitGoodsInfoOfConfirmOrder = undefined;
                    // $scope.confirmData.goodsInfo=submitGoodsList1;
                    // 将后台传过来的数据赋给页面的全局变量
                    $scope.confirmData.goodsInfo=res.results.goodsInfo;
                    // 合计
                    $scope.goodsTotalPrice = res.results.goodsTotalPrice;
                    $scope.exchangePointCount = res.results.exchangePointCount;
                    // 重新计算页面的价格
                    $scope.setOrderFinalPrice();
                    // 修改本地的数据值(这个可以不要)
                    localStorageService.set(KEY_PARAM_COMFIRM_ORDER, $scope.confirmData.goodsInfo);
                   // $state.go("personalCenter.confirmOrder", {shoppingBags: '#'});
                }else{
                    popupService.showToast(res.resultMessage);
                }
            }, function (error) {
                popupService.showToast(commonMessage.networkErrorMsg);
            });
        };

        // 立即购买提交
        $scope.checkOrderConfirm1 = function (submitGoodsList) {
            var param = {
                memberId: userInfo.memberId,
                goodsId: submitGoodsList[0].goodsId,
                type: submitGoodsList[0].type,
                goodsSkuId: submitGoodsList[0].ordConfirmOrderUnitExtList[0].skuId,
                quantity: submitGoodsList[0].quantity,
                memberPhone:$scope.conInfo.tel
            };
            new confirmOrderService(param).getDataBySingleGoods().then(function (res) {
                if(res.resultCode == "00"){
                    console.log(res.results);
                    $rootScope.shoppingBagsOfConfirmOrder = undefined;
                    $rootScope.singleGoodsInfoOfConfirmOrder = undefined;
                    $rootScope.suitGoodsInfoOfConfirmOrder = undefined;
                    // 将后台传过来的数据赋给页面的全局变量
                    $scope.confirmData.goodsInfo=res.results.goodsInfo;
                    // 合计
                    $scope.goodsTotalPrice = res.results.goodsTotalPrice;
                    $scope.exchangePointCount = res.results.exchangePointCount;
                    // 重新计算页面的价格
                    $scope.setOrderFinalPrice();
                    localStorageService.set(KEY_PARAM_COMFIRM_ORDER, res.results.goodsInfo);
                    //$state.go("personalCenter.confirmOrder", {shoppingBags: '#'});
                }else{
                    popupService.showToast(res.resultMessage);
                }
            }, function (error) {
                popupService.showToast(commonMessage.networkErrorMsg);
            });
        };

        // 20180108根据手机号查询会员等级
        $scope.getMasterD = function () {
            if($scope.conInfo.tel){
                var submitGoodsList = [];
                // 遍历页面的商品信息
                angular.forEach($scope.confirmData.goodsInfo, function (goods, index) {
                    submitGoodsList.push(goods);
                });
                //0.立即购买 1.购物车
                if(resultPath == 0){
                    $scope.checkOrderConfirm1(submitGoodsList);
                }else{
                    $scope.checkOrderConfirm2(submitGoodsList);
                }
            }
        };


        $scope.getCouponList = function (isNeedPhone) {
            if($scope.agentFlag !=0){
                // 如果是店员，必须有客户的电话号码
                if(!$scope.conInfo.tel||$scope.conInfo.tel.length==0){
                    if(isNeedPhone=='1'){
                       popupService.showToast("请输入会员手机号");
                    }
                    $scope.setOrderFinalPrice();
                    return;
                }
            }
            $scope.selectedCoupon.id = -1;
            var hasOrderActivity = "1";
            if(!$scope.selectedOrderActivity.id||$scope.selectedOrderActivity.id==-1){
                hasOrderActivity = "0";
            }
            $scope.couponList = [];
            var param = {
                memberId: userInfo.memberId,
                goodsInfo:$scope.confirmData.goodsInfo,
                orderPrice: $scope.orderFinalPrice,
                hasOrderActivity:hasOrderActivity
            };
            if($scope.conInfo.tel){
                param.telephone = $scope.conInfo.tel;
            }
            new confirmOrderService(param).getOrderCoupons().then(function (res) {
                if(res.resultCode == "00"){
                    $scope.couponList = res.results;
                    if("1" == userInfo.isSaller){
                        $scope.couponList = [];
                    }
                    if($scope.couponList&&$scope.couponList.length>0){
                        $scope.selectedCoupon.id = $scope.couponList[0].couponMemberId;
                    }
                    $scope.setOrderFinalPrice();
                }else{
                    popupService.showToast(res.resultMessage);
                }
            }, function (error) {
                popupService.showToast(commonMessage.networkErrorMsg);
            });
        };
        //计算总金额
        $scope.setGoodsTotalPrice = function(){
            var goodsTotal = 0;
            var exchangePointCount = 0;
            var goodsCount = 0
            angular.forEach($scope.confirmData.goodsInfo, function (goods) {
                if(goods.type!="30"){
                    var goodsPrice =  goods.ordConfirmOrderUnitExtList[0].price;
                    var goodsPoint = 0;
                    if(goods.activity){
                        // 是后台传来的活动优惠后价格
                        goodsPrice = goods.activity.newPrice;
                        goodsPoint = goods.activity.point==null?0:goods.activity.point;
                    }
                    /*// 判断会员等级(如果是高级会员，则根据折扣后金额判断是否打88折)
                    if(userInfo.memberLevelId =='30'){
                        // 如果有活动，判断是否是折扣活动，如果是折扣活动，则判断活动折扣除以0.88是否大于5.7，如果大于，则打88折
                        if(goods.activity.activityType != null && goods.activity.activityType =="20"){
                            // 折扣除以0.88是否大于5.7
                            if(goods.activity.paramValue !=null ){
                                if(goods.activity.paramValue/0.88 > 5.7){
                                    goodsPrice = goodsPrice*0.88;
                                }
                            }
                        }else{
                            goodsPrice = goodsPrice*0.88;
                        }
                    }*/
                    goodsTotal += parseFloat(goodsPrice)*goods.quantity;
                    exchangePointCount += goodsPoint*goods.quantity;
                    goodsCount +=parseInt(goods.quantity);
                    console.log(parseFloat(goodsPrice)*goods.quantity);
                }else{
                    // 套装
                    var goodsPrice =  0;
                    var goodsPoint = 0;
                    angular.forEach(goods.ordConfirmOrderUnitExtList, function (sku,index2) {
                        goodsPrice += parseFloat(sku.price);
                    });
                    if(goods.activity){
                        goodsPrice = goods.activity.newPrice;
                        goodsPoint = goods.activity.point==null?0:goods.activity.point;
                    }
                   /* // todo判断该活动不是五折活动
                    if(goods.activity == null || (goods.activity != null && goods.activity.tempId != '4cecaf45-b443-474f-a90c-6eebdd670e87')){
                        // 高级会员除了5折活动都打88折
                        if(userInfo.memberLevelId =='30'){
                            goodsPrice = goodsPrice*0.88;
                        }
                    }*/
                    goodsTotal += parseFloat(goodsPrice)*goods.quantity;
                    exchangePointCount += goodsPoint*goods.quantity;
                    goodsCount +=parseInt(goods.quantity);
                }
            });
            $scope.goodsCount = goodsCount;
            $scope.goodsTotalPrice = goodsTotal.toFixed(2);
            console.log("goodsTotalPrice");
            console.log($scope.goodsTotalPrice);
            $scope.goodsExchangePointCount = exchangePointCount;
        };

        $scope.setOrderFinalPrice = function(){
            // 在商品总价的基础上减去订单优惠和优惠券抵值（先算优惠券，再算整单活动）
            var orderDiscount = parseFloat($scope.goodsTotalPrice);
            $scope.discountPrice = 0;
            $scope.activityPointCount = 0;
            // todo
            // 先算优惠券
            if($scope.couponList!=null&&$scope.couponList.length>0){
                angular.forEach($scope.couponList, function (coupon) {
                    coupon.discountPrice = parseFloat(orderDiscount - orderDiscount * coupon.discount / 10).toFixed(2);
                    if($scope.selectedCoupon.id == coupon.couponMemberId){
                        // 判断选中优惠券类型，如果是生日券，则高级会员不打88
                        // coupon.couponType=="20" 是生日券
                        /*if(coupon.couponType=="20"){
                            // b==false:是指打了88折了
                            if(b==false){
                                // 还原88折前金额
                                orderDiscount = orderDiscount/0.88;
                                // 还原商品合计价格
                                $scope.goodsTotalPrice = orderDiscount.toFixed(2);
                                console.log("goodsTotalPrice");
                                console.log($scope.goodsTotalPrice);
                                b=true;
                            }
                        }*/
                        // 选中的优惠券
                        if(coupon.couponStyle =="0") {
                            // 抵值
                            orderDiscount = orderDiscount - coupon.worth;
                        }else if(coupon.couponStyle =="1"){
                            // 打折
                            orderDiscount = orderDiscount - parseFloat(coupon.discountPrice);
                        }
                    }
                });
            }

            $scope.orderDiscountPrice = orderDiscount.toFixed(2);
            var orderFinal = orderDiscount;
            // todo
            // 再算订单整单活动
            if($scope.confirmData.actMasterList!=null&&$scope.confirmData.actMasterList.length>0){
                angular.forEach($scope.confirmData.actMasterList, function (activity) {
                    if($scope.selectedOrderActivity.id == activity.activityId){
                        // 选中的活动
                        orderFinal = orderFinal - activity.cutPrice;
                        // if(activity.needFee){
                        //     orderDiscount = orderDiscount + activity.needFee;
                        // }
                        $scope.discountPrice = activity.cutPrice;
                        $scope.activityPointCount = activity.needPoint;
                    }
                });
            }

            if(orderFinal < $scope.goodsCount ){
                orderFinal = parseFloat($scope.goodsCount);
            }

            $scope.orderFinalPrice = orderFinal.toFixed(2);
            $scope.exchangePointCount  = $scope.activityPointCount +  $scope.goodsExchangePointCount;
        };

        $scope.setSelectedActivity = function(id){
            $scope.removeGiftGoods();
            $scope.selectedOrderActivity.id = id;
            $scope.getCouponList();
        };
        $scope.setSelectedCoupon = function(id){
            $scope.selectedCoupon.id = id;
            $scope.setOrderFinalPrice();
        };

        $scope.selectTab = function (index) {
            $scope.selectedDeliveryTab = index;
            $rootScope.selectedDeliveryTab = index;
        };

        $scope.submitOrder = function (isValid) {

            $scope.submitted = true;
            if(!isValid){
                return;
            }
            var orderListJson = [];
            angular.forEach($scope.confirmData.goodsInfo, function (goods) {
                var goodsJson = {};
                goodsJson.goodsId = goods.goodsId;
                goodsJson.skuInfo = [];
                angular.forEach(goods.ordConfirmOrderUnitExtList, function (sku) {
                    goodsJson.skuInfo.push({skuId: sku.skuId});
                });
                if(goods.activity){
                    goodsJson.actionId = goods.activity.activityId;
                    goodsJson.actionName =  goods.activity.activityName;
                    goodsJson.priceDiscount = goods.activity.newPrice;
                    goodsJson.amount = (goods.activity.originPrice*goods.quantity).toFixed(2);
                    goodsJson.amountDiscount = (goods.activity.newPrice*goods.quantity).toFixed(2);
                }else{
                    goodsJson.actionId = "";
                    goodsJson.actionName =  "";
                    goodsJson.priceDiscount = "";
                    goodsJson.amount = 0;

                    angular.forEach(goods.ordConfirmOrderUnitExtList, function (sku) {
                        goodsJson.amount += parseFloat(sku.price);
                    });
                    goodsJson.amount = (goodsJson.amount * goods.quantity).toFixed(2);
                }
                goodsJson.quantity = goods.quantity;
                goodsJson.isGift = goods.isGift;
                orderListJson.push(goodsJson);
            });
            // 订单级别的活动
            var actionName = null;
            var actionId = null;
            if($scope.selectedOrderActivity.id != -1){
                var selectedActivity = null;
                angular.forEach($scope.confirmData.actMasterList, function (actMaster) {
                    if(actMaster.activityId == $scope.selectedOrderActivity.id){
                        selectedActivity = actMaster;
                        actionName = actMaster.activityName;
                        actionId = actMaster.activityId;
                    }
                });
                if(selectedActivity != null && selectedActivity.activityType =="42"){

                    // 满送货品的活动必须要选择货品,判断有没有选择货品
                    var hasSelected = false;
                    angular.forEach($scope.confirmData.goodsInfo, function (goods,index) {
                        if(goods.isGift=="1"&&goods.giftActivityId == selectedActivity.activityId){
                            hasSelected = true;
                        }
                    });
                    if(!hasSelected){
                        popupService.showToast("请选择活动的赠品.");
                        return;
                    }
                }
            }

            //优惠券
            var couponMemberId = null;
            var amountCoupon = null;
            if($scope.selectedCoupon.id != -1) {
                angular.forEach($scope.couponList, function (coupon) {
                    if (coupon.couponMemberId == $scope.selectedCoupon.id) {
                        couponMemberId = coupon.couponMemberId;
                        if(coupon.couponStyle =="0") {
                            // 抵值
                            amountCoupon = coupon.worth;
                        }else if(coupon.couponStyle =="1"){
                            // 打折
                            amountCoupon = coupon.discountPrice;
                        }
                    }
                });
            }
            var orderListJsonStr = JSON.stringify(orderListJson);

            var erpStoreId = "";
            var storeName = "";
            var storePhone = "";
            var addressId = "";
            // 送货方式
            if($scope.selectedDeliveryTab == "10"){
                if(!$scope.addressInfo||$scope.addressInfo.addressId == null||$scope.addressInfo.addressId.length==0){
                    popupService.showToast("请选择收货地址");
                    return;
                }else{
                    addressId = $scope.addressInfo.addressId;
                }
            }else if($scope.selectedDeliveryTab == "20"){
                if(!$scope.shopInfo||$scope.shopInfo.store_code == null||$scope.shopInfo.store_code.length==0){
                    popupService.showToast("请选择门店");
                    return;
                }else{
                    erpStoreId = $scope.shopInfo.store_code;
                    storeName = $scope.shopInfo.store_name_cn;
                    storePhone = $scope.shopInfo.phone;
                }
            }
            // 发票信息 1是需要，0不需要
            var wantinvoice = '0';
            var invoiceTitle = "";
            var ivainvoiceAddress = "";
            var invoiceTel = "";
            var invoiceTaxno = "";
            var invoiceBankv = "";
            var invoiceAccount = "";

            if ($scope.invoiceInfo) {
                if ($scope.invoiceInfo.type == '10'
                    && $scope.invoiceInfo.invoiceTitle && $scope.invoiceInfo.invoiceTitle.length > 0) {
                    invoiceTitle = $scope.invoiceInfo.invoiceTitle;
                    wantinvoice = '1';
                } else if ($scope.invoiceInfo.type == '20'
                    && $scope.invoiceInfo.companyName && $scope.invoiceInfo.companyName.length > 0
                    && $scope.invoiceInfo.taxpayerCode && $scope.invoiceInfo.taxpayerCode.length > 0
                    && $scope.invoiceInfo.companyAddress && $scope.invoiceInfo.companyAddress.length > 0
                    && $scope.invoiceInfo.companyTel && $scope.invoiceInfo.companyTel.length > 0
                    && $scope.invoiceInfo.bankName && $scope.invoiceInfo.bankName.length > 0
                    && $scope.invoiceInfo.bankAccount && $scope.invoiceInfo.bankAccount.length > 0) {

                    wantinvoice = '1';

                    invoiceTitle = $scope.invoiceInfo.companyName;
                    ivainvoiceAddress = $scope.invoiceInfo.companyAddress;
                    invoiceTel = $scope.invoiceInfo.companyTel;
                    invoiceTaxno = $scope.invoiceInfo.taxpayerCode;
                    invoiceBankv = $scope.invoiceInfo.bankName;
                    invoiceAccount = $scope.invoiceInfo.bankAccount;
                }
            }

            //添加联系人信息
            if($scope.selectedDeliveryTab == '20'){
                $scope.cName = $scope.conInfo.cName;
                $scope.ctel = $scope.conInfo.ctel;
            }else{
                $scope.cName = '';
                $scope.ctel = '';
            }
            var param = {
                ordListJson:orderListJsonStr,
                memberId:userInfo.memberId,
                newmemberId:$scope.conInfo.tel,
                captcha:$scope.conInfo.captcha,
                shopId:"00000000",
                amountTotle:$scope.goodsTotalPrice,
                actionId:actionId,
                actionName:actionName,
                amountDiscount:$scope.discountPrice,
                couponMemberId:couponMemberId,
                amountCoupon:amountCoupon,
                exchangePointCount:$scope.exchangePointCount,
                pointCount:0,
                amountPoint:0,
                //付款方式 10.支付宝，20.微信支付
                payType:"",
                //订单类型 10.PC电商，11.手机电商，20.微信，30.APP
                orderType:"10",
                payInfact:$scope.orderFinalPrice,
                message:$scope.comment.message,
                wantinvoice:wantinvoice,
                invoiceTitle:invoiceTitle,
                invoiceAddress:ivainvoiceAddress,
                invoiceTel:invoiceTel,
                invoiceTaxno:invoiceTaxno,
                invoiceBankv:invoiceBankv,
                invoiceAccount:invoiceAccount,
                deliverType:$scope.selectedDeliveryTab,
                addressId:addressId,
                erpStoreId:erpStoreId,
                storeName:storeName,
                storePhone:storePhone,
                cname:$scope.cName,
                ctel:$scope.ctel
            };
            new confirmOrderService(param).submitOrder().then(function (res) {
                if(res.resultCode == "00"){
                    popupService.showToast('提交成功');
                    $scope.removeShoppingbag();
                    var scope = $scope.$new();
                    var selectPayModalInstance = $modal.open({
                        templateUrl: 'html/selectPay.html',
                        controller: 'selectPayCtrl',
                        backdrop: "static",
                        keyboard: false,
                        scope: scope
                    });
                    selectPayModalInstance.opened.then(function () {//模态窗口打开之后执行的函数

                    });
                    selectPayModalInstance.result.then(
                        function (result) {
                            if (result) {
                                if("10" == result.type){
                                    $scope.doPay(res.resultsOrderId);
                                }else if("20" == result.type){
                                    $scope.getWXPayInfo(res.results);
                                }else if("30" == result.type){
                                    $('#orderId').val(res.resultsOrderId);
                                    $('#ordMaster1')[0].action = '/qyds-web-pc/unionpay/orderUnionPay.json';
                                    $('#ordMaster1')[0].submit();
                                }
                            }
                        }, function (reason) {
                            //点击空白区域，总会输出backdrop click，点击取消，则会暑促cancel
                        });
                }else{
                    popupService.showToast(res.resultMessage);
                }
            }, function (error) {
                popupService.showToast(commonMessage.networkErrorMsg);
            });

        };

        // 支付
        $scope.doPay = function (orderId) {
            $('#orderId').val(orderId);
            $('#ordMaster1')[0].action = '/qyds-web-pc/alipay/orderAliPay.json';
            $('#ordMaster1')[0].submit();
        };

        $scope.getWXPayInfo = function (out_trade_no) {
            var params = {
                "openid": "",
                "product_id":out_trade_no,
                "body": "商品订单"+out_trade_no,
                "out_trade_no": out_trade_no,
                "total_fee": (parseFloat($scope.orderFinalPrice)*100).toFixed(0),
                "trade_type": "NATIVE"
            };

           new WeiXinService(params).getWxPayInfo().then(function (response) {
                console.log(response);
                if (response.resultCode == '00') {
                    var codeUrl  = response.results.code_url;
                    var scope = $scope.$new();
                    scope.codeUrl = codeUrl;
                    var wxPayModalInstance = $modal.open({
                        templateUrl: 'html/wxPay.html',
                        controller: 'wxPayCtrl',
                        backdrop: "static",
                        keyboard: false,
                        scope: scope
                    });
                    wxPayModalInstance.opened.then(function () {//模态窗口打开之后执行的函数

                    });
                    wxPayModalInstance.result.then(
                        function (result) {
                            $state.go("personalCenter.orderList");
                        }, function (reason) {
                            $state.go("personalCenter.orderList");
                        });
                } else {
                    popupService.showToast("支付失败,原因:" + response.resultMessage);
                    $state.go("personalCenter.orderList");
                }
            }, function (error) {
                popupService.showToast("支付失败,可在订单列表中继续支付");
                $state.go("personalCenter.orderList");
            });
        };
        $scope.removeShoppingbag = function(){
            if(!$scope.shoppingBags||$scope.shoppingBags.length==0){
                return;
            }
            var bagNoArray = [];
            angular.forEach($scope.shoppingBags, function (shopingBag, index) {
                bagNoArray.push(shopingBag.bagNo);
            });
            var param = {
                memberId: userInfo.memberId,
                delBags: bagNoArray
            };
            new shoppingBagService(param).remove()
                .then(function (res) {
                }, function (error) {
                });
        };

        $scope.clearRootScopeOfConfirmOrder = function (){
            // 清空此次提交信息
            $rootScope.commentMessage = undefined;
            // 选择地址后画面信息更新
            $rootScope.selectedAddressInfo = undefined;
            $rootScope.selectedShopInfo = undefined;
            // 开票信息
            $rootScope.invoiceInfo = undefined;

            $rootScope.selectedDeliveryTab = undefined;

            localStorageService.clear(KEY_PARAM_COMFIRM_ORDER);
        };

        $scope.showAllAddress = function(flag){
            $scope.showAllAddressFlag = flag;
            $scope.getAddressList(false);
        };

        $scope.showAllStore = function(flag){
            $scope.showAllStoreFlag = flag;
            $scope.getStoreList();
        };

        // 使用新地址:取得最新的insertTime作为默认选项
        $scope.getAddressList = function (lastChecked) {
            var param = {"memberId": userInfo.memberId};
            new addressService(param).getList()
                .then(function (res) {
                    if (res.resultCode == "00") {
                        if ($scope.showAllAddressFlag == '1') {
                            var tempArray = [];
                            angular.forEach(res.results, function (item) {
                                if (tempArray.length < 3) {
                                    tempArray.push(item);
                                }
                            });
                            $scope.addressList = tempArray;

                        } else {
                            $scope.addressList = res.results;

                            if(lastChecked){
                                var lastInfo;
                                angular.forEach($scope.addressList, function (item) {
                                    if (!lastInfo || lastInfo.insertTime < item.insertTime) {
                                        lastInfo = item;
                                    }
                                });

                                if(lastInfo){
                                    $scope.addressInfo = lastInfo;
                                    $rootScope.selectedAddressInfo=lastInfo;
                                }
                            }

                        }
                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function (error) {
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
        };

        $scope.getSelectAddressCls = function(selectedId){
            if(!$scope.addressInfo){
                return "";
            }
            if(selectedId == $scope.addressInfo.addressId){
                return "selected";
            }else{
                return "";
            }
        };
        $scope.getSelectStoreCls = function(selectedId){
            if(!$scope.shopInfo){
                return "";
            }
            if(selectedId == $scope.shopInfo.store_code){
                return "selected";
            }else{
                return "";
            }
        };


        $scope.selectedAddress = function(addressInfo){
            $scope.addressInfo = addressInfo;
            $rootScope.selectedAddressInfo=addressInfo;
        };

        $scope.selectedStore = function(storeInfo){
            $scope.shopInfo = storeInfo;
            $rootScope.selectedShopInfo=storeInfo;
        };

        $scope.getStoreList = function (){
            if($scope.isLoadingStore){
                return;
            }
            $scope.isLoadingStore = true;
            var orderListJson = [];
            angular.forEach($scope.confirmData.goodsInfo, function (goods) {
                var goodsJson = {};
                goodsJson.goodsId = goods.goodsId;
                goodsJson.skuInfo = [];
                angular.forEach(goods.ordConfirmOrderUnitExtList, function (sku) {
                    goodsJson.skuInfo.push({skuId: sku.skuId});

                });
                goodsJson.quantity = goods.quantity;

                orderListJson.push(goodsJson);
            });

            var param = {
                orderList:orderListJson,
                provinceCode:($scope.info.provinceCd == '0' ? null : $scope.info.provinceCd),
                cityCode:($scope.info.cityCd == '0' ? null : $scope.info.cityCd),
                districtCode:($scope.info.areaCd == '0' ? null :$scope.info.areaCd)
            };

            new storeService(param).getStoreList()
                .then(function (res) {
                    $scope.isLoadingStore = false;
                if($scope.showAllStoreFlag == '1'){
                    var tempArray = [];
                    angular.forEach(res.data, function (store) {
                        if(tempArray.length < 3){
                            tempArray.push(store);
                        }
                    });
                    $scope.storeList = tempArray;
                } else {
                    $scope.storeList = res.data;
                    if($scope.storeList&& $scope.storeList.length>0){
                        $scope.shopInfo = $scope.storeList[0];
                    }
                }

            }, function (error) {
                    $scope.isLoadingStore = false;
                popupService.showToast(commonMessage.networkErrorMsg);
            });
        };

        $scope.totalPriceOfGoods = function (goodsItem){
            // 套装原价计算
            var goodsPrice = 0;
            angular.forEach(goodsItem.ordConfirmOrderUnitExtList, function (sku, index) {
                goodsPrice += parseFloat(sku.price);
            });

            return parseFloat(goodsPrice).toFixed(2);
        };

        $scope.selectTab = function (index) {

            $scope.selectedDeliveryTab = index;
            $rootScope.selectedDeliveryTab = index;

            if(index == '20' && $scope.storeList.length == 0){
                $scope.getStoreList();
            }
        };

        $scope.init();

        $scope.addAddress = function () {

            var scope = $scope.$new();
            scope.param = {
                type: "add"
            };

            var addressModalInstance = $modal.open({
                templateUrl: 'html/modifyAddress.html',
                controller: 'modifyAddressCtrl',
                backdrop: true,
                scope: scope
            });
            addressModalInstance.opened.then(function () {//模态窗口打开之后执行的函数

            });
            addressModalInstance.result.then(
                function (result) {
                    $scope.getAddressList(true);
                }, function (reason) {
                    console.log(result);//点击空白区域，总会输出backdrop click，点击取消，则会暑促cancel
                });
        };

        $scope.getProvinces = function (){
            new storeService(null).getOrgAddressList()
                .then(function (res) {
                    if (res.resultCode == '00') {
                        console.log(res);
                        $scope.provincesList = res.data;
                    } else {
                        popupService.showToast(res.resultMessage);
                    }

                }, function () {
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
        };

        // 联动区域变换
        $scope.changeArea = function (type, code) {
            var param = {
                "provinceCode": type == '0' ? code : null,
                "cityCode": type == '1' ? code : null
            };

            switch (type) {
                case '0':
                    $scope.areasList = [];
                    $scope.info.cityCd = '0';
                    $scope.info.areaCd = '0';
                    break;
                case '1':
                    $scope.info.areaCd = '0';
                    break;
            }

            $scope.getStoreList();

            new storeService(param).getOrgAddressList()
                .then(function (res) {
                    if (res.resultCode == "00") {
                        switch (type) {
                            case '0':
                                $scope.citiesList = res.data;
                                break;
                            case '1':
                                $scope.areasList = res.data;
                                break;
                        }
                    }
                }, function (error) {
                    console.log(error);
                });
        };

        $scope.getProvinces();


        $scope.showOrgSelect = function (flag){
            $scope.showOrgSelectFlg = flag;
        };

        $scope.selectGiftSku = function (goods,activity) {
            var scope = $scope.$new();
            scope.goods = goods;
            scope.activity = activity;
            var selectGiftModalInstance = $modal.open({
                templateUrl: 'html/selectGift.html',
                controller: 'selectGiftCtrl',
                backdrop: true,
                scope: scope
            });
            selectGiftModalInstance.opened.then(function () {//模态窗口打开之后执行的函数

            });
            selectGiftModalInstance.result.then(
                function (result) {
                    if(result){
                        $scope.removeGiftGoods();
                        $scope.confirmData.goodsInfo.push(result);
                        $scope.setGoodsTotalPrice();
                        $scope.setOrderFinalPrice();
                    }
                }, function (reason) {
                    //点击空白区域，总会输出backdrop click，点击取消，则会暑促cancel
                });
        };
        $scope.removeGiftGoods = function(){
            angular.forEach($scope.confirmData.goodsInfo, function (goods,index) {
                if(goods.isGift=="1"){
                    $scope.confirmData.goodsInfo.splice(index,1);
                }
            });
        };

        $scope.getVerifyCode = function (isValid) {
            $scope.submitted_captcha = true;
            if(!isValid){
                return;
            }
            //发送验证码
            var param = {"mobile": $scope.conInfo.tel, "orgId": "50"};
            new commonService(param).sendCaptcha()
                .then(function (res) {
                    if (res.resultCode == "00") {
                        popupService.showToast("验证码已经发送到您的手机");
                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function (error) {
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
            dealunaTimer.countDown('captcha_reset_btn1');
        };

    })
    // 确认订单 end
    // 个人信息 start
    .controller('personalCtrl', function ($scope, $rootScope, $state, localStorageService, personalService, $filter,fileReader,uploadImageService, popupService,authService) {
        $scope.info = {provinceCd: '0', cityCd: '0', areaCd: '0'};//初始化联动下拉

        $scope.personalInfo = {};
        $scope.getPersonalInfo = function(memberId){
            var param =  {
                memberId:memberId
            };
            new authService(param).getPersonalInfo()
                .then(function (res) {
                    if (res.resultCode == '00') {
                        localStorageService.set(KEY_USERINFO, res.data);
                        $scope.personalInfo = res.data;
                        if($scope.personalInfo.birthdate > 0){
                            $scope.birthdateStatus = '10';
                        } else {
                            $scope.birthdateStatus = '20';
                        }

                        // 用户头像
                        // $scope.imageSrc = DISPLAY_URI + THUMB_120 + userInfo.memberPic;

                        // 职业options
                        $scope.professionList = ["零售业", "餐饮业", "娱乐业", "文体业", "法律", "医疗", "IT行业", "军人", "设计", "金融",
                            "服务业", "制造业", "房地产", "公共事业", "教育", "学生", "自由职业", "其他"];
                        $scope.incomeList = ["3K以下", "3K-5K", "5K-8K", "8K以上"];

                        // 性别options
                        $scope.genderList = {
                            '--请选择--': "0",
                            '男': "1",
                            '女': "2"
                        };

                        // 格式化生日
                        if ($scope.personalInfo.birthdate) {
                            $scope.personalInfo.birthdate = $filter('date')($scope.personalInfo.birthdate, 'yyyy-MM-dd');
                        }


                        new personalService({}).getERPAddressInfo()
                            .then(function (res) {
                                if (res.resultCode == '00') {
                                    $scope.provincesList = res.data;

                                    if ($scope.personalInfo.provinceCode) {
                                        $scope.info.provinceCd = $scope.personalInfo.provinceCode;
                                    }
                                } else {
                                    popupService.showToast(res.resultMessage);
                                }

                            }, function () {
                                popupService.showToast(commonMessage.networkErrorMsg);
                            });

                        $scope.birthdate = {
                            data: undefined
                        };

                        if ($scope.personalInfo.birthdate > 0) {
                            $scope.currentDate = new Date($scope.personalInfo.birthdate);
                        }

                        if ($scope.personalInfo.provinceCode) {
                            $scope.getDefaultCityList($scope.personalInfo.provinceCode, $scope.personalInfo.cityCode, $scope.personalInfo.districtCode);
                        }
                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function (error) {
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
        };


        // 打开图片的时候
        $scope.getFile = function () {
            fileReader.readAsDataUrl($scope.file, $scope)
                .then(function (result) {
                    // $scope.imageSrc = result;
                    var names = $scope.file.name.split(".");
                    var param = {
                        type : "CMS_MASTER",
                        file : result,
                        fileName : names[0],
                        suffix : names[1]
                    };
                    new uploadImageService({"data":param})
                        .then(function (res) {
                            if (res.resultCode == "00") {
                                // $scope.imageSrc = DISPLAY_URI + THUMB_120 + res.url;
                                $scope.personalInfo.memberPic = res.url;

                            } else {
                                 popupService.showToast(res.resultMessage);
                            }
                        }, function () {
                             popupService.showToast(commonMessage.networkErrorMsg);
                        });

                });
        };

        // 日期插件 start
        angular.element(document).ready(function() {
            $("#inputBirthDate").cxCalendar();
        });
        // 日期插件 end

        //初始化函数
        $scope.loadUserInfo = function () {

            var userInfo = localStorageService.get(KEY_USERINFO);
            if(userInfo){
                $scope.getPersonalInfo(userInfo.memberId);
            }
        };

        // 联动区域变换
        $scope.changeArea = function (type, code) {
            var param = {
                "provinceCode": type == '0' ? code : null,
                "cityCode": type == '1' ? code : null
            };
            new personalService(param).getERPAddressInfo()
                .then(function (res) {
                    if (res.resultCode == "00") {
                        switch (type) {
                            case '0':
                                $scope.citiesList = res.data;
                                $scope.areasList = [];
                                $scope.info.cityCd = '0';
                                $scope.info.areaCd = '0';
                                break;
                            case '1':
                                $scope.areasList = res.data;
                                $scope.info.areaCd = '0';
                                break;
                        }
                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function (error) {
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
        };

        $scope.getDefaultCityList = function (pcode, ccode, dcode) {
            var param = {
                "provinceCode": pcode
            };
            new personalService(param).getERPAddressInfo()
                .then(function (res) {
                    if (res.resultCode == "00") {
                        $scope.citiesList = res.data;
                        $scope.areasList = [];
                        $scope.info.areaCd = '0';
                        if (ccode && ccode > 0) {
                            $scope.info.cityCd = ccode;

                            $scope.getDefaultAreaList(ccode, dcode);

                        } else {
                            $scope.info.cityCd = '0';
                        }
                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function (error) {
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
        };

        $scope.getDefaultAreaList = function (ccode, dcode) {
            var param = {
                "cityCode": ccode
            };
            new personalService(param).getERPAddressInfo()
                .then(function (res) {
                    if (res.resultCode == "00") {
                        $scope.areasList = res.data;

                        if (dcode && dcode > 0) {
                            $scope.info.areaCd = dcode;
                        } else {
                            $scope.info.areaCd = '0';
                        }
                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function (error) {
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
        };

        $scope.modifyPersonalInfo = function (isValid) {

            $scope.submitted = true;

            if(!isValid){
                return;
            }

            var param = {
                "memberId": $scope.personalInfo.memberId,
                "memberName": $scope.personalInfo.memberName,
                "sex": $scope.personalInfo.sex,
                //"birthdate": $scope.personalInfo.birthdate,
                "birthdate": $('#inputBirthDate').val(),
                "email": $scope.personalInfo.email,
                "profession": $scope.personalInfo.profession,
                "income": $scope.personalInfo.income,
                "postCode": $scope.personalInfo.postCode,
                "provinceCode": $scope.info.provinceCd,
                "cityCode": $scope.info.cityCd,
                "districtCode": $scope.info.areaCd,
                "address": $scope.personalInfo.address,
                "memberPic" : $scope.personalInfo.memberPic
            };
            new personalService(param).editPersonal()
                .then(function (res) {
                    if (res.resultCode == "00") {
                        $scope.personal = res.data;
                        localStorageService.set(KEY_USERINFO, res.data);

                        if($scope.personal.birthdate > 0){
                            $scope.birthdateStatus = '10';
                        } else {
                            $scope.birthdateStatus = '20';
                        }

                        // 格式化生日
                        if ($scope.personalInfo.birthdate) {
                            $scope.personalInfo.birthdate = $filter('date')($scope.personalInfo.birthdate, 'yyyy-MM-dd');
                        }

                        popupService.showToast('修改成功');

                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function () {
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
        };

        //进入页面后初始化页面
        $scope.loadUserInfo();
    })
    // 个人信息 end
    // 我的心愿单 start
    .controller('favoriteCtrl', function ($scope, $rootScope, $state, localStorageService, favoriteService, popupService, favoritePhoneService) {

        // 将header的心愿单恢复为黑色
        $rootScope.favoriteClassTag = false;

        $scope.goodsListPhone = [];
        $scope.goodsList = null;
        $scope.currentPage = 1;
        $scope.currentPagePhone = 1;
        $scope.totalPage = 0;
        $scope.pageSize = 12;
        $scope.hasMore = true;
        $scope.isLoading = false;
        $scope.isPhoneLoading = false;
        // pc心愿单商品列表取得
        $scope.getPCList = function() {
            if($scope.isLoading){
                return;
            }
            $scope.isLoading = true;
            $scope.goodsList = null;
            var param = {};
            param.currentPage = $scope.currentPage;
            param.totalPage = $scope.totalPage;
            param.pageSize = $scope.pageSize;
            param.memberId = userInfo.memberId;
            param.type = "10";
            // pc商品取得
            new favoriteService(param).getList()
                .then(function (res) {
                    $scope.isLoading = false;
                    if (res.resultCode == "00") {
                        $scope.goodsList = res.results;
                        $scope.currentPage = res.currentPage; //当前页数
                        $scope.totalPage = res.totalPage;   //总页数
                        $scope.pageSize = res.pageSize;   //每一页显示的件数
                        $scope.dochangePage();
                        // $scope.footerData = res.results;
                        // $ionicSlideBoxDelegate.update();
                    } else {
                         popupService.showToast(res.resultMessage);
                    }
                }, function () {
                     $scope.isLoading = false;
                     popupService.showToast(commonMessage.networkErrorMsg);
                });
        };

        //加载更多
        $scope.doGetMore = function () {
            $scope.currentPagePhone = $scope.currentPagePhone + 1;
            $scope.getPhoneList();
        };

        // 手机心愿单商品列表取得
        $scope.getPhoneList = function () {
            if($scope.isPhoneLoading){
                return;
            }
            $scope.isPhoneLoading = true;
            var param = {
                "currentPage": $scope.currentPagePhone,
                "totalPage": $scope.totalPage,
                "pageSize": $scope.pageSize,
                "memberId": userInfo.memberId,
                "type": "10"
            };
            //初始化调用
            new favoritePhoneService(param).getList().then(function (res) {
                $scope.isPhoneLoading = false;
                if (res.resultCode == "00") {
                    if (res.results.length != 0) {
                        $scope.goodsListPhone = $scope.goodsListPhone.concat(res.results);
                        if(res.results.length < $scope.pageSize){
                            $scope.hasMore = false;
                        }else{
                            $scope.hasMore = true;
                        }
                    } else {
                        $scope.hasMore = false;
                        $scope.currentPagePhone = $scope.currentPagePhone - 1;
                    }
                    $scope.totalPage = res.allCount;
                } else {
                    $scope.hasMore = false;
                    popupService.showToast(res.resultMessage);
                }
            }, function (err) {
                $scope.hasMore = false;
                $scope.isPhoneLoading = false;
                popupService.showToast(commonMessage.networkErrorMsg);
            });
        };



        $scope.dochangePage = function () {
            //分页样式初始化
            $(".pager").ucPager({
                //pageClass     : "分页样式",
                currentPage: $scope.currentPage, //当前页数
                totalPage: $scope.totalPage,   //总页数
                pageSize: $scope.pageSize,   //每一页显示的件数
                clickCallback: function (page) {
                    //下一页 上一页 页数跳转
                    $scope.currentPage = page;
                    $scope.getPCList();
                    $scope.dochangePage();
                }
            });
        };
        $scope.removeFavorite = function (collectNo) {
            var param = {
                memberId: userInfo.memberId,
                collectNo: collectNo
            };
            new favoriteService(param).deleteFavorite()
                .then(function (res) {
                    if ('00' == res.resultCode) {
                        popupService.showToast("取消成功");
                        $scope.currentPage = 1;
                        $scope.goodsListPhone = [];
                        $scope.getPCList();
                        $scope.getPhoneList();
                    }
                }, function (error) {
                    popupService.showToast(commonMessage.networkErrorMsg);
                });

        };

        $scope.goProductDetail = function (goodsId) {
            $state.go("goodsDetail", {"goodsId": goodsId});
        };

        $scope.goShopping = function (){
            $state.go("homepage");
        };

        var userInfo = localStorageService.get(KEY_USERINFO);
        if (!userInfo) {
            popupService.showToast('用户未登录');
        }else{
            $scope.getPCList();
            $scope.getPhoneList();
        }
    })
    // 积分兑换
    .controller('pointExchangeCtrl', ["$scope","$state","couponService","currentPointsService","localStorageService",'popupService','pointExchangeService',
        function ($scope,$state,couponService,currentPointsService,localStorageService, popupService,pointExchangeService) {
            var userInfo = localStorageService.get(KEY_USERINFO);
            if(!userInfo){
                $scope.goLogin();
                return;
            }
            $scope.couponList = [];
            $scope.point = userInfo.point;
            $scope.doRefreshCoupon = function () {
                $scope.couponList = [];
                $scope.getData();
            };
            $scope.getData = function () {
                var param = {
                    "memberId": userInfo == null ? "" : userInfo.memberId
                };
                new pointExchangeService(param)
                    .getPointExchangeCoupons()
                    .then(function (res) {
                        if (res.resultCode == "00") {
                            $scope.couponList = $scope.couponList.concat(res.results);
                        } else {
                            popupService.showToast(res.resultMessage);
                        }
                    }, function (error) {
                        popupService.showToast(commonMessage.networkErrorMsg);
                    });
            };

            $scope.getCoupon = function (couponId) {
                var param = {"memberId": userInfo.memberId, "couponId": couponId};
                new pointExchangeService(param)
                    .getCoupon()
                    .then(function (res) {
                        if (res.resultCode == "00") {
                            popupService.showToast("兑换成功!");
                            $scope.doRefreshCoupon();
                            $scope.getCurrentPoints();
                        } else {
                            popupService.showToast(res.resultMessage);
                        }
                    }, function (error) {
                        popupService.showToast(commonMessage.networkErrorMsg);
                    });

            };

            $scope.getCurrentPoints = function () {
                var param = {
                    "memberId": userInfo == null ? "" : userInfo.memberId
                };
                new currentPointsService(param)
                    .then(function (res) {
                        if (res.resultCode == "00") {
                            if(res.data != null){
                                $scope.point = res.data.point;
                                localStorageService.set(KEY_USERINFO, res.data);
                            }
                        } else {
                            popupService.showToast(res.resultMessage);
                        }
                    }, function (error) {
                        popupService.showToast(commonMessage.networkErrorMsg);
                    });
            };

            $scope.doRefreshCoupon();
            $scope.getCurrentPoints();
        }])
    // footer
    .controller('footerCtrl', ["$scope", "$state", "footerService", function ($scope, $state, footerService) { //footer

        // footer息取得
        new footerService({})
            .then(function (res) {
                if (res.resultCode == "00") {
                    $scope.footerData = res.results;
                    // $ionicSlideBoxDelegate.update();
                } else {
                    // popupService.showToast(res.resultMessage);
                }
            }, function () {
                // popupService.showToast(commonMessage.networkErrorMsg);
            });

        // footer详细
        $scope.goExplainPage = function (cms) {
            if(cms.itemUrl&&cms.itemUrl.length>0){
                window.location.href = cms.itemUrl;
            }else{
                $state.go("explainPage", {"cmsId":cms.cmsId});
            }

            // window.open("html/explainpage.html?cmsId=" + cmsId, "_fotterInfo");
        };
    }])
    // 选择支付方式
    .controller('selectPayCtrl', ["$scope", "$state", "$stateParams", '$modalInstance',
        function ($scope, $state, $stateParams, $modalInstance) {
            $scope.type = '10';
            $scope.getSelectedStyle = function(type){
                if($scope.type == type){
                    return {"border":"2px #ff6600 solid"};
                }else{
                    return {"border":"2px #cccccc solid"};
                }
            };
            $scope.setSelectedType = function(type){
                $scope.type = type
            };
            $scope.confirm = function(){
                var selectedPay = {
                    type: $scope.type
                };
                $modalInstance.close(selectedPay);
            };
        }])
        .controller('wxPayCtrl', ["$scope", "$state", "$stateParams", '$modalInstance',
        function ($scope, $state, $stateParams, $modalInstance) {
            angular.element(document).ready(function() {
                var qrcode = new QRCode(document.getElementById("qrcode_wx_pay"), {
                    width : 150,
                    height : 150
                });
                qrcode.makeCode($scope.codeUrl);
            });
            $scope.close = function(){
                $modalInstance.close();
            };
        }])
        // 选择支付方式
    .controller('selectPayCtrl', ["$scope", "$state", "$stateParams", '$modalInstance',
        function ($scope, $state, $stateParams, $modalInstance) {
            $scope.type = '10';
            $scope.getSelectedStyle = function(type){
                if($scope.type == type){
                    return {"border":"2px #ff6600 solid"};
                }else{
                    return {"border":"2px #cccccc solid"};
                }
            };
            $scope.setSelectedType = function(type){
                $scope.type = type
            };
            $scope.confirm = function(){
                var selectedPay = {
                    type: $scope.type
                };
                $modalInstance.close(selectedPay);
            };
        }])
    .controller('prizeDrawCtrl', ["$rootScope","$scope", "$state", "$stateParams","popupService",'prizeDrawService','localStorageService',
        function ($rootScope,$scope, $state, $stateParams,popupService,prizeDrawService,localStorageService) {

            var userInfo = localStorageService.get(KEY_USERINFO);
            //手机还是pc
            $scope.phoneAndPC = $('#phone').css('display');
            //抽奖机会次数
            $scope.oppCount = 0;
            // 活动ID prizeDrawId
            var prizeDrawId = "";
            if ($stateParams.prizeDrawId != null
                && $stateParams.prizeDrawId.length > 0) {
                $rootScope.prizeDrawId = $stateParams.prizeDrawId;
                prizeDrawId = $stateParams.prizeDrawId;
            } else {
                prizeDrawId = $rootScope.prizeDrawId;
            }
            //var prizeDrawId = '562a9ef9-1dcd-4464-95cc-21eb06bdcf74';
            // 会员ID
            var memberId = userInfo.memberId;

            $scope.getImageService = function() {

                new prizeDrawService().getImageService()
                    .then(function (res) {
                        if(res.results != null && res.results.length > 0){
                            $scope.activityPoints = res.results[0];
                            if($scope.activityPoints.cmList != null){
                                for(var i = 0; i< $scope.activityPoints.cmList.length; i++ ){
                                    var item = $scope.activityPoints.cmList[i];
                                    if(prizeDrawId == item.itemTypeVal){
                                        $scope.playbill = $scope.activityPoints.cmList[i];
                                        break;
                                    }
                                }
                            }
                        }
                    }, function () {
                        popupService.showToast(commonMessage.networkErrorMsg);
                    });
            }


            $scope.getPrizeDraw = function() {
                var param = {
                    prizeDrawId: prizeDrawId,
                    memberId:memberId
                };
                new prizeDrawService(param).getPrizeDrawById()
                    .then(function (res) {
                        if (res.resultCode == "00") {
                            //抽奖机会次数
                            $scope.oppCount = res.result.drawCount;
                            //活动时间
                            $scope.startTime = new Date(res.result.prizeDraw.startTime).Format("yyyy-MM-dd hh:mm:ss");
                            $scope.endTime = new Date(res.result.prizeDraw.endTime).Format("yyyy-MM-dd hh:mm:ss");

                            //是否可以抽奖
                            if($scope.oppCount == 0 ||
                                res.result.prizeDraw.currentTime > res.result.prizeDraw.endTime
                                || res.result.prizeDraw.currentTime < res.result.prizeDraw.startTime
                                || res.result.prizeDraw.isValid == '0'){
                                //未开始
                                $('.gb-turntable-btn').addClass("disabled");
                            } else {
                                //正在开始
                                $('.gb-turntable-btn').removeClass("disabled");
                            }

                            //积分兑换的信息
                            $scope.exchangeFlag = res.result.prizeDraw.exchangeFlag;
                            $scope.exchangePoint = res.result.prizeDraw.exchangePoint;

                            //订单的信息
                            $scope.isOrder = res.result.prizeDraw.isOrder;
                            $scope.orderAmount = res.result.prizeDraw.orderAmount;

                            //抽奖纪录
                            $scope.winList = res.result.winList;

                            if($scope.phoneAndPC == 'none'){
                                angular.element(document).ready(function() {
                                    $scope.scroll('list1', 2000, 1, 20);//停留时间，相对速度（越小越快）,每次滚动多少，最好和Li的Line-height一致
                                });
                            }else{
                                angular.element(document).ready(function() {
                                    $scope.scroll('list1-phone', 2000, 1, 20);//停留时间，相对速度（越小越快）,每次滚动多少，最好和Li的Line-height一致
                                });
                            }
                            $scope.getWinningList();
                        } else {
                            popupService.showToast(res.resultMessage);
                        }
                    }, function () {
                        popupService.showToast(commonMessage.networkErrorMsg);
                    });
            };

            $scope.exchangePrizeWithPoint = function(){
                var param = {
                    prizeDrawId: prizeDrawId,
                    memberId:memberId
                };

                new prizeDrawService(param).exchangePrizeWithPoint()
                    .then(function (res) {
                        if (res.resultCode == "00") {
                            popupService.showToast('兑换成功');
                            $scope.getPrizeDraw();
                        } else {
                            popupService.showToast(res.resultMessage);
                        }
                    }, function () {
                        popupService.showToast(commonMessage.networkErrorMsg);
                    });
            };


            $scope.getWinningList = function(){
                var param = {
                    prizeDrawId: prizeDrawId
                };

                new prizeDrawService(param).getWinningList()
                    .then(function (res) {
                        if (res.resultCode == "00") {
                            $scope.allRecord = res.result;
                            if($scope.phoneAndPC == 'none'){
                                angular.element(document).ready(function() {
                                    $scope.scroll('list', 2000, 1, 20);//停留时间，相对速度（越小越快）,每次滚动多少，最好和Li的Line-height一致
                                });
                            }else{
                                angular.element(document).ready(function() {
                                    $scope.scroll('list-phone', 2000, 1, 20);//停留时间，相对速度（越小越快）,每次滚动多少，最好和Li的Line-height一致
                                });
                            }

                        } else {
                            popupService.showToast(res.resultMessage);
                        }
                    }, function () {
                        popupService.showToast(commonMessage.networkErrorMsg);
                    });
            };

            $scope.getPrizeGoodsList = function() {
                var param = {
                    prizeDrawId: prizeDrawId
                };
                new prizeDrawService(param).getPrizeGoodsList()
                    .then(function (res) {
                        if (res.resultCode == "00") {
                            $scope.initPanel(res.result);
                        } else {

                        }
                    }, function () {
                    });
            };

            $scope.initPanel = function(prizeGoodsList){
                console.log(prizeGoodsList);
                var prizes = ['谢谢参与'];
                angular.forEach(prizeGoodsList,function(prizeGoods){
                    prizes.push(prizeGoods.prizeGoodsName);
                });
                angular.element(document).ready(function() {
                    var container ;
                    var turntableId ;
                    if($scope.phoneAndPC == 'none'){
                        container = document.getElementById("turntable-container");
                        turntableId = 'turntable';
                    }else{
                        container = document.getElementById("turntable-container-phone");
                        turntableId = 'turntable-phone';
                    }
                    container.style.transition = "all 30s ease";
                    gbTurntable.init({
                        id: turntableId,
                        config: function (callback) {
                            // 获取奖品信息
                            callback && callback(prizes);
                        },
                        getPrize: function (callback) {
                            var param = {
                                prizeDrawId: prizeDrawId,
                                userId:userInfo.memberId
                            };
                            new prizeDrawService(param).prizeDraw()
                                .then(function (res) {
                                    if (res.resultCode == "00") {
                                        if(res.result&&res.result != ""){
                                            //container.style.transition = "all 10s ease";
                                            console.log(res.result);
                                            var index = $scope.getPrizeIndex(res.result,prizeGoodsList);
                                            callback && callback([index, 1]);
                                        }
                                    }else{
                                        console.log(res);
                                    }
                                }, function () {
                                });
                            callback && callback([0, 1]);
                            // // 获取中奖信息
                            // var num = Math.random() * 7 >>> 0;   //奖品ID
                            // var chances = num;  // 可抽奖次数
                            // callback && callback([0, 1]);
                            // setTimeout(function () {
                            //     callback && callback([1, 1]);
                            // },2000)
                        },
                        gotBack: function (data,id) {
                            $scope.getPrizeDraw();
                            popupService.showToast('恭喜抽中' + data);
                        }
                    });
                });
            }

            $scope.getPrizeIndex = function (prizeGoodsId,prizeGoodsList) {
                var index = 0;
                angular.forEach(prizeGoodsList,function(prizeGoods,i){
                    if(prizeGoods.prizeGoodsId == prizeGoodsId){
                        index = i+1;
                    }
                });
                return index;
            };

            $scope.scroll = function(element, delay, speed, lineHeight) {
                var numpergroup = 1;
                var slideBox = (typeof element == 'string')?document.getElementById(element):element;
                if(slideBox == null){
                    return;
                }
                angular.element(document).ready(function() {
                    var delay = delay||1000;
                    var speed=speed||20;
                    var lineHeight = lineHeight||20;
                    var tid = null, pause = false;
                    var liLength = slideBox.getElementsByTagName('li').length;

                    var heightPx = $(slideBox).css('height');
                    var height = heightPx.substr(0,heightPx.length-2);
                    if(height >= liLength * 20){
                        return;
                    }

                    var lack = numpergroup-liLength%numpergroup;
                    for(i=0;i<lack;i++){
                        //slideBox.appendChild(document.createElement("li"));
                    }
                    var start = function() {
                        tid=setInterval(slide, speed);
                    };
                    var slide = function() {
                        if (pause) return;
                        slideBox.scrollTop += 2;
                        if ( slideBox.scrollTop % lineHeight == 0 ) {
                            clearInterval(tid);
                            for(i=0;i<numpergroup;i++){
                                slideBox.appendChild(slideBox.getElementsByTagName('li')[0]);
                            }
                            slideBox.scrollTop = 0;
                            setTimeout(start, delay);
                        }
                    };
                    slideBox.onmouseover=function(){pause=true;};
                    slideBox.onmouseout=function(){pause=false;};
                    setTimeout(start, delay);
                });

            }
            //$scope.scroll('list', 2000, 1, 20);//停留时间，相对速度（越小越快）,每次滚动多少，最好和Li的Line-height一致
            //$scope.scroll('list1', 2000, 1, 20);//停留时间，相对速度（越小越快）,每次滚动多少，最好和Li的Line-height一致
            //$scope.scroll('list-phone', 2000, 1, 20);//停留时间，相对速度（越小越快）,每次滚动多少，最好和Li的Line-height一致
            //$scope.scroll('list1-phone', 2000, 1, 20);//停留时间，相对速度（越小越快）,每次滚动多少，最好和Li的Line-height一致

            $scope.getPrizeDraw();
            $scope.getImageService();
            $scope.getPrizeGoodsList();
        }])

    // footer描述
    .controller('footerExplainCtrl', ["$scope", "$state", "$stateParams", "popupService", "footerExplainService","$sce", function ($scope, $state, $stateParams, popupService, footerExplainService,$sce) { //header
        // footer描述取得
        new footerExplainService({"cmsId":$stateParams.cmsId})
            .then(function (res) {
                if (res.resultCode == "00") {
                    $scope.footerExplainData = res.results;
                    $scope.contentHtml = $sce.trustAsHtml($scope.footerExplainData.contentHtml);
                    // $ionicSlideBoxDelegate.update();
                } else {
                    // popupService.showToast(res.resultMessage);
                }
            }, function () {
                // popupService.showToast(commonMessage.networkErrorMsg);
            });
    }])