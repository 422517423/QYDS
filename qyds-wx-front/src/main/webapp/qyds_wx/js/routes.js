angular.module('dealnua.routes', [])

.config(function($stateProvider, $urlRouterProvider) {

    $stateProvider
        .state('homePage', {
            url: '/homePage',
            templateUrl: 'templates/homePage.html',
            controller: 'homePageCtrl'
        })
        .state('login', {
            url: '/login',
            templateUrl: 'templates/login.html',
            controller: 'loginCtrl'
        })
        .state('register', {
            url: '/register',
            templateUrl: 'templates/register.html',
            controller: 'registerCtrl'
        })
        .state('bag', {
            url: '/bag',
            templateUrl: 'templates/bag.html',
            controller: 'bagCtrl'
        })
        .state('confirmOrder', {
            url: '/confirmOrder',
            params:{"shoppingBags":null,"singleGoodsInfo":null,"suitGoodsInfo":null},
            templateUrl: 'templates/confirmOrder.html',
            controller: 'confirmOrderCtrl'
        })
        .state('addressManager', {
            url: '/addressManager',
            templateUrl: 'templates/addressManager.html',
            controller: 'addressManagerCtrl'
        })
        .state('addressSelect', {
            url: '/addressSelect',
            templateUrl: 'templates/addressSelect.html',
            controller: 'addressSelectCtrl'
        })
        .state('searchGoods', {
            url: '/searchGoods',
            params:{"type":''},
            templateUrl: 'templates/searchGoods.html',
            controller: 'searchGoodsCtrl'
        })
        .state('secKill', {
            url: '/secKill',
            templateUrl: 'templates/secKill.html',
            controller: 'secKillCtrl'
        })
        .state('about', {
            url: '/about',
            templateUrl: 'templates/about.html',
            controller: 'aboutCtrl'
        })
        .state('personal', {
            url: '/personal',
            templateUrl: 'templates/personal.html',
            controller: 'personalCtrl'
        })
        .state('goodsList', {
            url: '/goodsList/:type/:actId/:firstGoodsTypeId/:pageType/:classifyId/:classifyName/:title',
            templateUrl: 'templates/goodsList.html',
            controller: 'goodsListCtrl'
        })
        .state('saleList', {
            url: '/saleList/:type',
            templateUrl: 'templates/saleList.html',
            controller: 'saleListCtrl'
        })
        .state('goodsListDisplay', {
            url: '/goodsListDisplay/:type/:cmsId/:title',
            templateUrl: 'templates/goodsListDisplay.html',
            controller: 'goodsListDisplayCtrl'
        })
        .state('goodsListCms', {
            url: '/goodsListCms/:type',
            templateUrl: 'templates/goodsListCms.html',
            controller: 'goodsListCmsCtrl'
        })
        .state('couponList', {
            url: '/couponList',
            templateUrl: 'templates/coupon.html',
            controller: 'couponListCtrl'
        })
        .state('couponAll', {
            url: '/couponAll',
            templateUrl: 'templates/couponAll.html',
            controller: 'couponAllCtrl'
        })
        .state('modifyAddress', {
            url: '/modifyAddress',
            params:{"type":'', "addressId":''},
            templateUrl: 'templates/modifyAddress.html',
            controller: 'modifyAddressCtrl'
        })
        .state('favorite', {
            url: '/favorite',
            templateUrl: 'templates/favorite.html',
            controller: 'favoriteCtrl'
        })
        .state('personalCenter', {
            url: '/personalCenter',
            templateUrl: 'templates/personalCenter.html',
            controller: 'personalCenterCtrl'
        })
        .state('goodsDetail', {
            url: '/goodsDetail',
            params:{"type":''},
            templateUrl: 'templates/productDetail.html',
            controller: 'productDetailCtrl'
        })
        .state('classification', {
            url: '/classification',
            templateUrl: 'templates/classification.html',
            controller: 'classificationCtrl'
        })
        .state('binding', {
            url: '/binding',
            templateUrl: 'templates/binding.html',
            controller: 'bindingCtrl'
        })
        .state('orderDetail', {
            url: '/orderDetail',
            params:{"orderId":''},
            templateUrl: 'templates/orderDetail.html',
            controller: 'orderDetailCtrl'
        })
        .state('refunds', {
            url: '/refunds',
            params:{"orderItem": null},
            templateUrl: 'templates/refunds.html',
            controller: 'refundsCtrl'
        })
        .state('applyRefund', {
            url: '/applyRefund',
            params:{"orderId": null},
            templateUrl: 'templates/applyRefund.html',
            controller: 'applyRefundCtrl'
        })
        .state('logisticsDetail', {
            url: '/logisticsDetail',
            params:{"orderId": null},
            templateUrl: 'templates/logisticsDetail.html',
            controller: 'logisticsDetailCtrl'
        })
        .state('invoice', {
            url: '/invoice',
            templateUrl: 'templates/invoice.html',
            controller: 'invoiceCtrl'
        })
        .state('orderList', {
            url: '/orderList',
            params: {"tabFlg": null},
            templateUrl: 'templates/orderList.html',
            controller: "orderListCtrl"
        })

        // homePageTab
        .state("homePageTab", {
            url: "/homePageTab",
            cache: false,
            abstract: true,
            templateUrl: "templates/homePageTab.html"
        })
        .state('homePageTab.homePage', {
            url: '/homePage',
            cache: false,
            views: {
                'tab-homePage': {
                    templateUrl: 'templates/homePage.html',
                    controller: "homePageCtrl"
                }
            }
        })
        .state('homePageTab.classification', {
            url: '/classification',
            views: {
                'tab-classification': {
                    templateUrl: 'templates/classification.html',
                    controller: "classificationCtrl"
                }
            }
        })
        .state('homePageTab.bag', {
            url: '/bag',
            views: {
                'tab-bag': {
                    templateUrl: 'templates/bag.html',
                    controller: "bagCtrl"
                }
            }
        })
        .state('homePageTab.personalCenter', {
            url: '/personalCenter',
            views: {
                'tab-personalCenter': {
                    templateUrl: 'templates/personalCenter.html',
                    controller: "personalCenterCtrl"
                }
            }
        })

        .state('storeSelect', {
            url: '/storeSelect',
            params:{"orderList":null},
            templateUrl: 'templates/storeSelect.html',
            controller: 'storeSelectCtrl'
        })

        .state('pointExchange', {
            url: '/pointExchange',
            templateUrl: 'templates/pointExchange.html',
            controller: 'pointExchangeCtrl'
        })
        .state('orderListOffline', {
            url: '/orderListOffline',
            templateUrl: 'templates/orderListOffline.html',
            controller: 'orderListOfflineCtrl'
        })
        .state('privacy', {
            url: '/privacy',
            templateUrl: 'templates/privacy.html',
            controller: 'privacyCtrl'
        })

        // 积分兑换活动页
        .state('pointsExchangePage', {
            url: '/pointsExchangePage',
            templateUrl: 'templates/pointsExchangePage.html',
            controller: 'pointsExchangePageCtrl'
        })

        // 积分兑换活动页
        .state('pointsExchangePageNew', {
            url: '/pointsExchangePageNew',
            templateUrl: 'templates/pointsExchangePageNew.html',
            controller: 'pointsExchangePageNewCtrl'
        })

        // 积分兑换活动页
        .state('pointsCoupponPage', {
            url: '/pointsCoupponPage',
            templateUrl: 'templates/pointsCoupponPage.html',
            controller: 'pointsCoupponPageCtrl'
        })

        // 抽奖
        .state('prizeDraw', {
            url: '/prizeDraw/:prizeDrawId',
            templateUrl: 'templates/prizeDraw.html',
            controller: 'prizeDrawCtrl'
        })

        // test 用 start
        .state('testMenu', {
            url: '/testMenu',
            templateUrl: 'menu.html',
            controller: 'testMenuController'
        });

        //$urlRouterProvider.otherwise("/orderTab/allOrder");
        $urlRouterProvider.otherwise("/homePageTab/homePage");
        // test 用 end




});