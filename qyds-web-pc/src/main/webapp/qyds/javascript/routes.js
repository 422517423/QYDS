angular.module('dealuna.routes', [])

.config(['$stateProvider', '$urlRouterProvider',function($stateProvider,$urlRouterProvider) {
    console.log('rout');
    // $routeProvider.when('/homepage', {templateUrl: 'html/homepage.html', controller: 'homepageCtrl'});
    //
    // $routeProvider.otherwise({redirectTo: '/homepage'});
    $stateProvider
        // 首页
        .state('homepage', {
            url: '/homepage',
            templateUrl: 'html/homepage.html',
            controller: 'homepageCtrl'
        })
        //.state('login', {
        //    url: '/login',
        //    templateUrl: 'html/login.html',
        //    controller: 'loginCtrl'
        //})
        // 商品列表
        .state('goodsList', {
            url: '/goodsList/{actId}/{firstGoodsTypeId}/{type}/{cmsId}/{pageType}/{classifyId}/{classifyName}',
            //params:{"type":'',"actId":'', "firstGoodsTypeId":''},
            templateUrl: 'html/goodList.html',
            controller: 'goodsListCtrl'
        })
        .state('goodsListDisplay', {
            url: '/goodsListDisplay/:type',
            templateUrl: 'html/goodListDisplay.html',
            controller: 'goodsListDisplayCtrl'
        })
        // 商品详情
        .state('goodsDetail', {
            url: '/goodsDetail/:goodsId',
            //params:{"type":'',"actId":'', "firstGoodsTypeId":''},
            templateUrl: 'html/goodsDetail.html',
            controller: 'goodsDetailCtrl'
        })
        // footer描述
        .state('explainPage', {
            url: '/explainPage/:cmsId',
            templateUrl: 'html/explainpage.html',
            controller: 'footerExplainCtrl'
        })
        // 关于我们
        .state('about', {
            url: '/about/{pageParam}',
            templateUrl: 'html/about.html',
            controller: 'aboutCtrl'
        })
        // 杂志
        .state('magazine', {
            url: '/magazine',
            templateUrl: 'html/magazinepage.html',
            controller: 'magazineCtrl'
        })
        // 背景故事
        .state('theStory', {
            url: '/theStory/:cmsId',
            templateUrl: 'html/theStory.html',
            controller: 'theStoryCtrl'
        })
        // 商品检索
        .state('searchGoods', {
            url: '/searchGoods/:searchKey',
            templateUrl: 'html/goodListResults.html',
            controller: 'searchGoodsCtrl'
        })
        // 门店信息
        .state('storeList', {
            url: '/storeList',
            templateUrl: 'html/storeList.html',
            controller: 'storeListCtrl'
        })
        // 联系我们
        .state('contactUs', {
            url: '/contactUs',
            templateUrl: 'html/contactUs.html',
            controller: 'contactUsCtrl'
        })
        // 秒杀
        .state('secKill', {
            url: '/secKill',
            templateUrl: 'html/secKill.html',
            controller: 'secKillCtrl'
        })
        // 联系我们
        .state('msTest', {
            url: '/msTest',
            templateUrl: 'html/msTest.html',
            controller: 'msTestCtrl'
        })
        // 个人中心
        .state('personalCenter', {
            url: '/personalCenter',
            templateUrl: 'html/personalCenter.html',
            controller: 'personalCenterCtrl'
        })
        // personalHome
        .state('personalCenter.personalHome', {
            url: '/personalCenter/personalHome',
            templateUrl: 'html/personalHome.html',
            controller: 'personalHomeCtrl'
        })
        // 个人信息
        .state('personalCenter.personal', {
            url: '/personalCenter/personal',
            templateUrl: 'html/personal.html',
            controller: 'personalCtrl'
        })
        // 购物袋
        .state('personalCenter.bag', {
            url: '/personalCenter/bag',
            templateUrl: 'html/bag.html',
            controller: 'bagCtrl'
        })
        // 订单确认
        .state('personalCenter.confirmOrder', {
            url: '/personalCenter/confirmOrder/:shoppingBags/:singleGoodsInfo/:suitGoodsInfo',
            templateUrl: 'html/confirmOrder.html',
            controller: 'confirmOrderCtrl'
        })
        // 订单详情
        .state('personalCenter.orderDetail', {
            url: '/personalCenter/orderDetail/:orderId',
            templateUrl: 'html/orderDetail.html',
            controller: 'orderDetailCtrl'
        })
        // 收货地址
        .state('personalCenter.addressList', {
            url: '/personalCenter/addressList',
            templateUrl: 'html/addressList.html',
            controller: 'addressCenterCtrl'
        })
        // 心愿单
        .state('personalCenter.favorite', {
            url: '/personalCenter/favorite',
            templateUrl: 'html/favorite.html',
            controller: 'favoriteCtrl'
        })
        // 我的优惠券
        .state('personalCenter.couponList', {
            url: '/personalCenter/couponList',
            templateUrl: 'html/couponList.html',
            controller: 'couponCenterCtrl'
        })
        // 我的订单
        .state('personalCenter.orderList', {
            url: '/personalCenter/orderList',
            templateUrl: 'html/orderList.html',
            controller: 'orderListCtrl'
        })
        // 线下订单
        .state('personalCenter.orderListOffLine', {
            url: '/personalCenter/orderListOffLine',
            templateUrl: 'html/orderListOffLine.html',
            controller: 'orderListOffLineCtrl'
        })
        // 我的订单
        .state('personalCenter.pointExchange', {
            url: '/personalCenter/pointExchange',
            templateUrl: 'html/pointExchange.html',
            controller: 'pointExchangeCtrl'
        })
        //退货
        .state('personalCenter.applyReturnGoods', {
            url: '/personalCenter/applyReturnGoods',
            templateUrl: 'html/applyReturnGoods.html',
            controller: 'applyReturnGoodsCtrl'
        })
        //退款
        .state('personalCenter.applyRefund', {
            url: '/personalCenter/applyRefund/{orderId}',
            templateUrl: 'html/applyRefund.html',
            controller: 'applyRefundCtrl'
        })
    // 物流信息
    .state('personalCenter.logisticsList', {
        url: '/personalCenter/logisticsList/:orderId',
        templateUrl: 'html/logisticsList.html',
        controller: 'logisticsListCtrl'
    })

    // 积分兑换活动页
    .state('pointsExchangePage', {
        url: '/pointsExchangePage',
        templateUrl: 'html/pointsExchangePage.html',
        controller: 'pointsExchangePageCtrl'
    })

    // 积分兑换商品活动页
    .state('pointsExchangePageNew', {
        url: '/pointsExchangePageNew',
        templateUrl: 'html/pointsExchangePageNew.html',
        controller: 'pointsExchangePageNewCtrl'
    })

    // 积分兑换活动页
    .state('pointsCoupponPage', {
        url: '/pointsCoupponPage',
        templateUrl: 'html/pointsCoupponPage.html',
        controller: 'pointsCoupponPageCtrl'
    })

    // 抽奖
    .state('prizeDraw', {
        url: '/prizeDraw/:prizeDrawId',
        templateUrl: 'html/prizeDraw.html',
        controller: 'prizeDrawCtrl'
    });

    $urlRouterProvider.otherwise('/homepage');

}]);