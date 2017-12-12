angular.module('test.routes', [])

.config(function($stateProvider, $urlRouterProvider) {

    $stateProvider
        // .state('homePage', {
        //     url: '/homePage',
        //     templateUrl: 'templates/homePage.html',
        //     controller: 'homePageCtrl'
        // })
        .state('login', {
            url: '/login/:type',
            templateUrl: 'templates/login.html',
            controller: 'loginCtrl'
        })
        .state('purchase', {
            url: '/purchase',
            templateUrl: 'templates/purchase.html',
            controller: 'purchaseCtrl'
        })
        .state('myPurchase', {
            url: '/myPurchase',
            templateUrl: 'templates/myPurchase.html',
            controller: 'myPurchaseCtrl'
        })
        .state('myPurchaseDetail', {
            url: '/myPurchaseDetail',
            params:{"type":'',"status":''},
            templateUrl: 'templates/myPurchaseDetail.html',
            controller: 'myPurchaseDetailCtrl'
        })
        .state('mySupply', {
            url: '/mySupply',
            templateUrl: 'templates/mySupply.html',
            controller: 'mySupplyCtrl'
        })
        .state('mySupplyDetail', {
            url: '/mySupplyDetail',
            params:{"type":'',"status":''},
            templateUrl: 'templates/mySupplyDetail.html',
            controller: 'mySupplyDetailCtrl'
        })
        .state('purchaseInfo', {
            url: '/purchaseInfo',
            templateUrl: 'templates/purchaseInfo.html',
            controller: 'purchaseInfoCtrl'
        })
        .state('purchaseDetail', {
            url: '/purchaseDetail',
            params:{"type":''},
            templateUrl: 'templates/purchaseDetail.html',
            controller: 'purchaseDetailCtrl'
        })
        .state('register', {
            url: '/register',
            templateUrl: 'templates/register.html',
            controller: 'registerCtrl'
        })
        .state('offer', {
            url: '/offer',
            params:{"type":''},
            templateUrl: 'templates/offer.html',
            controller: 'offerCtrl'
    })
        // .state('bag', {
        //     url: '/bag',
        //     templateUrl: 'templates/bag.html',
        //     controller: 'bagCtrl'
        // })
        // .state('confirmOrder', {
        //     url: '/confirmOrder',
        //     params:{"shoppingBags":null,"singleGoodsInfo":null,"suitGoodsInfo":null},
        //     templateUrl: 'templates/confirmOrder.html',
        //     controller: 'confirmOrderCtrl'
        // })
        // .state('addressManager', {
        //     url: '/addressManager',
        //     templateUrl: 'templates/addressManager.html',
        //     controller: 'addressManagerCtrl'
        // })
        // .state('addressSelect', {
        //     url: '/addressSelect',
        //     templateUrl: 'templates/addressSelect.html',
        //     controller: 'addressSelectCtrl'
        // })
        // .state('searchGoods', {
        //     url: '/searchGoods',
        //     params:{"type":''},
        //     templateUrl: 'templates/searchGoods.html',
        //     controller: 'searchGoodsCtrl'
        // })
        // .state('about', {
        //     url: '/about',
        //     templateUrl: 'templates/about.html',
        //     controller: 'aboutCtrl',
        //     params:{args:{"testVal":''}}
        // })
        // .state('personal', {
        //     url: '/personal',
        //     templateUrl: 'templates/personal.html',
        //     controller: 'personalCtrl'
        // })
        // .state('goodsList', {
        //     url: '/goodsList/:type/:actId/:firstGoodsTypeId/:pageType/:classifyId/:classifyName/:title',
        //     templateUrl: 'templates/goodsList.html',
        //     controller: 'goodsListCtrl'
        // })
        // .state('goodsListDisplay', {
        //     url: '/goodsListDisplay/:type/:cmsId/:title',
        //     templateUrl: 'templates/goodsListDisplay.html',
        //     controller: 'goodsListDisplayCtrl'
        // })
        // .state('couponList', {
        //     url: '/couponList',
        //     templateUrl: 'templates/coupon.html',
        //     controller: 'couponListCtrl'
        // })
        // .state('couponAll', {
        //     url: '/couponAll',
        //     templateUrl: 'templates/couponAll.html',
        //     controller: 'couponAllCtrl'
        // })
        // .state('modifyAddress', {
        //     url: '/modifyAddress',
        //     params:{"type":'', "addressId":''},
        //     templateUrl: 'templates/modifyAddress.html',
        //     controller: 'modifyAddressCtrl'
        // })
        // .state('favorite', {
        //     url: '/favorite',
        //     templateUrl: 'templates/favorite.html',
        //     controller: 'favoriteCtrl'
        // })
        // .state('personalCenter', {
        //     url: '/personalCenter',
        //     templateUrl: 'templates/personalCenter.html',
        //     controller: 'personalCenterCtrl'
        // })
        // .state('goodsDetail', {
        //     url: '/goodsDetail',
        //     params:{"type":''},
        //     templateUrl: 'templates/productDetail.html',
        //     controller: 'productDetailCtrl'
        // })
        // .state('classification', {
        //     url: '/classification',
        //     templateUrl: 'templates/classification.html',
        //     controller: 'classificationCtrl'
        // })
        // .state('binding', {
        //     url: '/binding',
        //     templateUrl: 'templates/binding.html',
        //     controller: 'bindingCtrl'
        // })
        // .state('orderDetail', {
        //     url: '/orderDetail',
        //     params:{"orderId":''},
        //     templateUrl: 'templates/orderDetail.html',
        //     controller: 'orderDetailCtrl'
        // })
        // .state('refunds', {
        //     url: '/refunds',
        //     params:{"orderItem": null},
        //     templateUrl: 'templates/refunds.html',
        //     controller: 'refundsCtrl'
        // })
        // .state('applyRefund', {
        //     url: '/applyRefund',
        //     params:{"orderId": null},
        //     templateUrl: 'templates/applyRefund.html',
        //     controller: 'applyRefundCtrl'
        // })
        // .state('logisticsDetail', {
        //     url: '/logisticsDetail',
        //     params:{"orderId": null},
        //     templateUrl: 'templates/logisticsDetail.html',
        //     controller: 'logisticsDetailCtrl'
        // })
        // .state('invoice', {
        //     url: '/invoice',
        //     templateUrl: 'templates/invoice.html',
        //     controller: 'invoiceCtrl'
        // })
        // .state('orderList', {
        //     url: '/orderList',
        //     params: {"tabFlg": null},
        //     templateUrl: 'templates/orderList.html',
        //     controller: "orderListCtrl"
        // })
        //
        // // homePageTab
        // .state("homePageTab", {
        //     url: "/homePageTab",
        //     cache: false,
        //     abstract: true,
        //     templateUrl: "templates/homePageTab.html"
        // })
        // .state('homePageTab.homePage', {
        //     url: '/homePage',
        //     cache: false,
        //     views: {
        //         'tab-homePage': {
        //             templateUrl: 'templates/homePage.html',
        //             controller: "homePageCtrl"
        //         }
        //     }
        // })
        // .state('homePageTab.classification', {
        //     url: '/classification',
        //     views: {
        //         'tab-classification': {
        //             templateUrl: 'templates/classification.html',
        //             controller: "classificationCtrl"
        //         }
        //     }
        // })
        // .state('homePageTab.bag', {
        //     url: '/bag',
        //     views: {
        //         'tab-bag': {
        //             templateUrl: 'templates/bag.html',
        //             controller: "bagCtrl"
        //         }
        //     }
        // })
        // .state('homePageTab.personalCenter', {
        //     url: '/personalCenter',
        //     views: {
        //         'tab-personalCenter': {
        //             templateUrl: 'templates/personalCenter.html',
        //             controller: "personalCenterCtrl"
        //         }
        //     }
        // })
        //
        // .state('storeSelect', {
        //     url: '/storeSelect',
        //     params:{"orderList":null},
        //     templateUrl: 'templates/storeSelect.html',
        //     controller: 'storeSelectCtrl'
        // })
        //
        // .state('pointExchange', {
        //     url: '/pointExchange',
        //     templateUrl: 'templates/pointExchange.html',
        //     controller: 'pointExchangeCtrl'
        // })
        // .state('orderListOffline', {
        //     url: '/orderListOffline',
        //     templateUrl: 'templates/orderListOffline.html',
        //     controller: 'orderListOfflineCtrl'
        // })
        // .state('privacy', {
        //     url: '/privacy',
        //     templateUrl: 'templates/privacy.html',
        //     controller: 'privacyCtrl'
        // })
        //
        // // 积分兑换活动页
        // .state('pointsExchangePage', {
        //     url: '/pointsExchangePage',
        //     templateUrl: 'templates/pointsExchangePage.html',
        //     controller: 'pointsExchangePageCtrl'
        // })
        //
        // // 积分兑换活动页
        // .state('pointsCoupponPage', {
        //     url: '/pointsCoupponPage',
        //     templateUrl: 'templates/pointsCoupponPage.html',
        //     controller: 'pointsCoupponPageCtrl'
        // })
        //
        // // test 用 start
        // .state('testMenu', {
        //     url: '/testMenu',
        //     templateUrl: 'menu.html',
        //     controller: 'testMenuController'
        // });

        //$urlRouterProvider.otherwise("/orderTab/allOrder");
        // $urlRouterProvider.otherwise("/homePageTab/homePage");
        $urlRouterProvider.otherwise("/login");
        // test 用 end




});