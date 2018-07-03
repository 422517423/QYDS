angular.module('test.services', [])
    .factory('LoadingIntercepter', ['$rootScope', '$q', function ($rootScope, $q) {
        /**
         * LOADING 拦截器
         */
        return {
            request: function (config) {
                var pattern = /.json$/;
                if (pattern.exec(config.url)) {
                    //判断是否显示加载条，noLoading为true时不显示。
                    if (!config.noLoading) {
                        $rootScope.$broadcast('LOADING:SHOW');
                    }
                }
                return config;
            },
            response: function (response) {
                var pattern = /.json$/;
                if (!response ||(response&&pattern.exec(response.config.url))) {
                    $rootScope.$broadcast('LOADING:HIDE');//取消加载进度条
                }
                return response;
            },
            responseError: function (rejection) {
                //检测网络异常
                $rootScope.$broadcast('http-response:error', rejection);
                $rootScope.$broadcast('LOADING:HIDE');
                $q.reject(rejection);
            }
        }
    }])

    .factory("authService", function($http,$q) {

        function authService(param){
            function login() {
                var defer = $q.defer();
                $http.post("../auth/login.json", $jq.param({'data':JSON.stringify(param)})).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function (error) {
                        defer.reject('error');
                    });
                return defer.promise;
            };

            function register() {
                var defer = $q.defer();
                $http.post("../auth/register.json", $jq.param({'data':JSON.stringify(param)})).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function (error) {
                        defer.reject('error');
                    });
                return defer.promise;
            };

            function bingingWeiXin() {
                var defer = $q.defer();
                $http.post("../auth/bingingWeiXin.json", $jq.param({'data':JSON.stringify(param)})).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function (error) {
                        defer.reject('error');
                    });
                return defer.promise;
            };

            function getPersonalInfo() {
                var defer = $q.defer();
                $http.post("../mmb_master/getDetail.json", $jq.param({'data':JSON.stringify(param)})).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function (error) {
                        defer.reject('error');
                    });
                return defer.promise;
            };

            return {login:login,
                register:register,
                bingingWeiXin:bingingWeiXin,
                getPersonalInfo:getPersonalInfo}
        }

        return authService;
    })
    .factory("commonService", function($http,$q) {
        function commonService(param){
            function getProvinces() {
                var defer = $q.defer();
                $http.post("../com_discrict/getProvinces.json").then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function () {
                        defer.reject();
                    });
                return defer.promise;
            };

            function getSubAddresses() {
                var defer = $q.defer();
                $http.post("../com_discrict/getSubAddresses.json", $jq.param({'parentId':param.parentId})).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function () {
                        defer.reject();
                    });
                return defer.promise;
            };


            function sendCaptcha() {
                var defer = $q.defer();
                $http.post("../captcha/send.json", $jq.param({'data': JSON.stringify(param)})).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function () {
                        defer.reject();
                    });
                return defer.promise;
            };

            return {getProvinces:getProvinces,
                getSubAddresses:getSubAddresses,
                sendCaptcha:sendCaptcha}
        }

        return commonService;
    })
    .factory("orderService", function($http,$q) {
        function orderService(param){
            // 获取订单列表
            function getOrderList(param) {
                var defer = $q.defer();
                $http.post("../ord_wechat/getOrderListByMemberId.json", $jq.param({'data': JSON.stringify(param)})).then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
                return defer.promise;
            };
            // 获取订单详情
            function showOrderDetail(param) {
                var defer = $q.defer();
                $http.post("../ord_wechat/getOrderDetail.json", $jq.param({'data': JSON.stringify(param)})).then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
                return defer.promise;
            };
            // 取消订单
            function cancelOrder(param) {
                var defer = $q.defer();
                $http.post("../ord_wechat/cancelOrder.json", $jq.param({'data': JSON.stringify(param)})).then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
                return defer.promise;
            };
            // 确认收货（全单）
            function confirmReceiptInMaster(param) {
                var defer = $q.defer();
                $http.post("../ord_wechat/confirmReceiptInMaster.json", $jq.param({'data': JSON.stringify(param)})).then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
                return defer.promise;
            };
            // 确认收货（自提全单orderId或快递编号expressNo）
            function confirmReceived(param) {
                var defer = $q.defer();
                $http.post("../ord_wechat/confirmReceived.json", $jq.param({'data': JSON.stringify(param)})).then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
                return defer.promise;
            };
            // 删除订单（全单）
            function deleteOrder(param) {
                var defer = $q.defer();
                $http.post("../ord_wechat/deleteOrder.json", $jq.param({'data': JSON.stringify(param)})).then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
                return defer.promise;
            };
            // 全单退货（不可拆单退）
            function applyReturnGoods(param) {
                var defer = $q.defer();
                $http.post("../ord_wechat/applyReturnGoods.json", $jq.param({'data': JSON.stringify(param)})).then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
                return defer.promise;
            };
            // 子单退货（允许拆单退）
            function applyReturnSubGoods(param) {
                var defer = $q.defer();
                $http.post("../ord_wechat/applyReturnSubGoods.json", $jq.param({'data': JSON.stringify(param)})).then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
                return defer.promise;
            };
            // 申请退款
            function applyRefund(param) {
                var defer = $q.defer();
                $http.post("../ord_wechat/applyRefund.json", $jq.param({'data': JSON.stringify(param)})).then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
                return defer.promise;
            };

            // 获取门店信息
            function getOrgList(param) {
                var defer = $q.defer();
                $http.post("../ord_wechat/getOrgList.json", $jq.param({'data': JSON.stringify(param)})).then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
                return defer.promise;
            };
            function getOrgAddressList(param) {
                var defer = $q.defer();
                $http.post("../ord_wechat/getOrgAddressList.json", $jq.param({'data':JSON.stringify(param)}), {noLoading:true}).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function () {
                        defer.reject();
                    });
                return defer.promise;
            };
            function getOrderCountByMemberId(param) {
                var defer = $q.defer();
                $http.post("../ord_wechat/getOrderCountByMemberId.json", $jq.param({'data':JSON.stringify(param)}), {noLoading:true}).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function () {
                        defer.reject();
                    });
                return defer.promise;
            };
            function getOfflineOrders(param) {
                var defer = $q.defer();
                $http.post("../ord_wechat/getOrderListOffLineByMemberId.json", $jq.param({'data':JSON.stringify(param)})).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function () {
                        defer.reject();
                    });
                return defer.promise;
            };

            return {
                getOrderList: getOrderList,                             // 获取订单列表
                showOrderDetail: showOrderDetail,                       // 获取订单详情
                cancelOrder: cancelOrder,                               // 取消订单
                confirmReceiptInMaster: confirmReceiptInMaster,         // 确认收货（全单）
                applyReturnGoods: applyReturnGoods,                     // 全单退货（不可拆单退）
                deleteOrder: deleteOrder,                               // 删除订单（全单）
                applyReturnSubGoods: applyReturnSubGoods,               // 子单退货（允许拆单退）
                confirmReceived:confirmReceived,                        // 确认收货（物流信息）
                getOrgList: getOrgList,                                 // 获取门店信息
                getOrgAddressList:getOrgAddressList,
                getOrderCountByMemberId:getOrderCountByMemberId,
                applyRefund:applyRefund,
                getOfflineOrders:getOfflineOrders
            }
        }
        return orderService;
    })
    .factory("logisticsService", function($http,$q) {

        function getLogistics(param) {
            var defer = $q.defer();
            $http.post("../ord_wechat/queryLogisticsInfo.json", $jq.param({'data': JSON.stringify(param)}))
                .then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function () {
                        defer.reject();
                    });
            return defer.promise;
        };
        return getLogistics;

    })
    .factory("shoppingBagService", function($http,$q) {

        function shoppingBagService(param) {

            /**
             *
             * 购物袋商品一览(有分页)
             *
             * @param data memberId:会员ID
             *             lastUpdateTime:最后一条更新时间(分页用)
             * @returns {*}
             */
            function getList() {
                var defer = $q.defer();
                $http.post("../mmb_shopping_bag/getList.json", $jq.param({'data': JSON.stringify(param)})).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function () {
                        defer.reject();
                    });
                return defer.promise;
            };

            /**
             * 添加商品(单件商品)
             *
             * @param data 会员ID    memberId
             *             店铺ID    shopId
             *             商品类型    type
             *             商品代码    goodsCode
             *             商品名称    goodsName
             *             商品SKU    sku
             *             件数    quantity
             * @returns {*}
             */
            function add() {
                var defer = $q.defer();
                $http.post("../mmb_shopping_bag/add.json", $jq.param({'data': JSON.stringify(param)})).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function (error) {
                        defer.reject('error');
                    });
                return defer.promise;
            };

            /**
             * 删除商品
             *
             * @param data 购物袋编号 bagNo
             *             会员ID    memberId
             *             批量删除 delBags
             * @returns {*}
             */
            function remove() {
                var defer = $q.defer();
                $http.post("../mmb_shopping_bag/delete.json", $jq.param({'data': JSON.stringify(param)})).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function () {
                        defer.reject();
                    });
                return defer.promise;
            };

            /**
             * 改变单件商品数量
             *
             * @param data 购物袋编号 bagNo
             *             会员ID    memberId
             *             件数    quantity
             * @returns {*}
             */
            function changeQuantity() {
                var defer = $q.defer();
                $http.post("../mmb_shopping_bag/changeQuantity.json", $jq.param({'data': JSON.stringify(param)})).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function () {
                        defer.reject();
                    });
                return defer.promise;
            };

            /**
             * 改变单件商品活动信息
             *
             * @param data 购物袋编号 bagNo
             *             会员ID    memberId
             *             活动ID    actGoodsId
             * @return
             * @returns {*}
             */
            function changeActivity() {
                var defer = $q.defer();
                $http.post("../mmb_shopping_bag/changeActivity.json", $jq.param({'data': JSON.stringify(param)})).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function () {
                        defer.reject();
                    });
                return defer.promise;
            };


            return {
                getList: getList,
                add: add,
                remove: remove,
                changeQuantity: changeQuantity,
                changeActivity: changeActivity
            };

        }

        return shoppingBagService;
    })
    .factory("addressService", function ($http, $q) {

        function addressService(param) {

            // 会员送货地址一览
            function getList() {
                var defer = $q.defer();
                $http.post("../mmb_address/getList.json", $jq.param({'data':JSON.stringify(param)})).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function () {
                        defer.reject();
                    });
                return defer.promise;
            };

            function getDefaultAddress() {
                var defer = $q.defer();
                $http.post("../mmb_address/getDefaultAddress.json", $jq.param({'data':JSON.stringify(param)})).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function () {
                        defer.reject();
                    });
                return defer.promise;
            };

            function add() {
                var defer = $q.defer();
                $http.post("../mmb_address/add.json", $jq.param({'data':JSON.stringify(param)})).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function () {
                        defer.reject();
                    });
                return defer.promise;
            };

            function edit() {
                var defer = $q.defer();
                $http.post("../mmb_address/edit.json", $jq.param({'data':JSON.stringify(param)})).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function () {
                        defer.reject();
                    });
                return defer.promise;
            };

            function deleteItem() {
                var defer = $q.defer();
                $http.post("../mmb_address/delete.json", $jq.param({'data':JSON.stringify(param)})).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function () {
                        defer.reject();
                    });
                return defer.promise;
            };

            function getDetail() {
                var defer = $q.defer();
                $http.post("../mmb_address/getDetail.json", $jq.param({'data':JSON.stringify(param)})).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function () {
                        defer.reject();
                    });
                return defer.promise;
            };

            function changeDefault() {
                var defer = $q.defer();
                $http.post("../mmb_address/changeDefault.json", $jq.param({'data':JSON.stringify(param)})).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function () {
                        defer.reject();
                    });
                return defer.promise;
            };

            return {
                getList: getList,
                getDefaultAddress: getDefaultAddress,
                add: add,
                edit: edit,
                deleteItem: deleteItem,
                changeDefault: changeDefault,
                getDetail:getDetail
            };

        }

        return addressService;
    })

    .factory("favoriteService", function ($http, $q) {

        function favoriteService(param) {

            /**
             * 查询会员的收藏一览
             *
             * @param param memberId:会员ID
             *             type:收藏类型(10.商品，20.店铺)
             *             lastUpdateTime:分页标记(最后一条修改时间)
             * @return  {*}
             */
            function getList() {
                var defer = $q.defer();
                $http.post("../mmb_collection/getList.json", $jq.param({'data':JSON.stringify(param)})).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function () {
                        defer.reject();
                    });
                return defer.promise;
            };

            /**
             * 添加收藏
             *
             * @param param memberId:会员ID
             *             type:收藏类型(10.商品，20.店铺)
             *             objectId:收藏对象ID
             *             name:名称
             *             url:收藏URL
             *
             * @returns {*}
             */
            function addFavorite() {
                var defer = $q.defer();
                $http.post("../mmb_collection/add.json", $jq.param({'data':JSON.stringify(param)})).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function () {
                        defer.reject();
                    });
                return defer.promise;
            };

            /**
             * 删除收藏
             *
             * @param param memberId:会员ID
             *             collectNo:收藏编号
             *             collections:批量删除
             *
             * @returns {*}
             */
            function deleteFavorite() {
                var defer = $q.defer();
                $http.post("../mmb_collection/delete.json", $jq.param({'data':JSON.stringify(param)})).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function () {
                        defer.reject();
                    });
                return defer.promise;
            };

            return {
                getList: getList,
                addFavorite: addFavorite,
                deleteFavorite:deleteFavorite
            };

        }

        return favoriteService;
    })

    .factory("hotSearchService", function($http,$q) {

        function getData(param) { //<———————需要给原来的方法体做一个内部方法名字尽量起的有意义（getxxx、savexxxx等）
            var defer = $q.defer();
            var searchKey = param.searchKey;
            var json = {};
            json.searchKey = searchKey;
            $http.post("../gds_master_api/getProductList.json", $jq.param({'data':JSON.stringify(json)})).then(
                function(response) {
                    defer.resolve(response.data);
                },function(){
                    defer.reject();
                });
            return defer.promise;
        };
        return getData;// 此处return上面的 方法




        //var defer = $q.defer();
        //
        //$http.get("./data/hotSearch.json").then(
        //    function(response) {
        //        defer.resolve(response.data.results);
        //    },function(){
        //        defer.reject();
        //    });
        //return defer.promise;
    })

    // 海报
    .factory("couponPointsService", ["$q","$http",function($q, $http) {
        function couponPointsService(param) {
            var defer = $q.defer();
            var json = {};
            json.itemCode = 'points_couppon';
            json.isChild = '0';
            $http.post("../cms_items_api/getListByItem.json", $jq.param({'data':JSON.stringify(json)}))
                .then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
            return defer.promise;
        };
        return couponPointsService;
    }])

    // 当前积分灾区的
    .factory("currentPointsService", ["$q","$http",function($q, $http) {
        function currentPointsService(param) {
            var defer = $q.defer();
            var json = {};
            json.memberId = param.memberId;
            $http.post("../auth/getCurrentPoints.json", $jq.param({'data':JSON.stringify(json)}))
                .then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
            return defer.promise;
        };
        return currentPointsService;
    }])

    //.factory("goodsSearchService", function($http,$q) {
    //
    //    var defer = $q.defer();
    //
    //    $http.get("./data/goodsSearch.json").then(
    //        function(response) {
    //            defer.resolve(response.data.results);
    //        },function(){
    //            defer.reject();
    //        });
    //    return defer.promise;
    //})
    .factory("aboutService", function($http,$q) {

        var defer = $q.defer();

        $http.get("./data/about.json").then(
            function(response) {
                defer.resolve(response.data);
            },function(){
                defer.reject();
            });
        return defer.promise;
    })
    .factory("personalService", function($http,$q) {

        function personalService(param){
            function getPersonalInfo() {
                var defer = $q.defer();
                $http.post("../mmb_master/getDetail.json", $jq.param({'data':JSON.stringify(param)}))
                    .then(
                        function (response) {
                            defer.resolve(response.data);
                        }, function () {
                            defer.reject();
                        });
                return defer.promise;
            };

            function editPersonal() {
                var defer = $q.defer();
                $http.post("../mmb_master/editPersonal.json", $jq.param({'data':JSON.stringify(param)}))
                    .then(
                        function (response) {
                            defer.resolve(response.data);
                        }, function () {
                            defer.reject();
                        });
                return defer.promise;
            };

            function getERPAddressInfo() {
                var defer = $q.defer();
                $http.post("../mmb_master/getAddressList.json", $jq.param({'data':JSON.stringify(param)})).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function () {
                        defer.reject();
                    });
                return defer.promise;
            };
            function modifyMemberAvatarFromWX() {
                var defer = $q.defer();
                $http.post("../mmb_master/modifyMemberAvatarFromWX.json", $jq.param({'data':JSON.stringify(param)})).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function () {
                        defer.reject();
                    });
                return defer.promise;
            };
            function compressImage() {
                var defer = $q.defer();
                $http.post("../common/compressImage.json", $jq.param({'data':JSON.stringify(param)})).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function () {
                        defer.reject();
                    });
                return defer.promise;
            };

            return {getPersonalInfo:getPersonalInfo,
                editPersonal:editPersonal,
                getERPAddressInfo:getERPAddressInfo,
                modifyMemberAvatarFromWX:modifyMemberAvatarFromWX,
                compressImage:compressImage
            }
        }

        return personalService;
    })

    .factory("getNewDataService", function($http,$q) {

        function getData(params) { //<———————需要给原来的方法体做一个内部方法名字尽量起的有意义（getxxx、savexxxx等）
            var defer = $q.defer();
            var json = {};
            var firstGoodsTypeId = params.firstGoodsTypeId;
            json.goodsTypeId = firstGoodsTypeId;
            json.itemCode = "index_goods";
            $http.post("../cms_items_api/getNewGoods.json",$jq.param({'data':JSON.stringify(json)})).then(
                function(response) {
                    defer.resolve(response.data);
                },function(){
                    defer.reject();
                });
            return defer.promise;
        };
        return getData;// 此处return上面的 方法
    })

    .factory("goodsListService", function($http,$q) {

        function getData(param) { //<———————需要给原来的方法体做一个内部方法名字尽量起的有意义（getxxx、savexxxx等）
            var defer = $q.defer();
            var goodsTypeId = param.goodsTypeId;
            var activityId = param.actId;
            var firstGoodsTypeId = param.firstGoodsTypeId;
            var from = param.from;
            var to = param.to;
            var insertTime = param.insertTime;
            var memberId = param.memberId;
            var goodsIds = param.goodsIds;
            var cmsId = param.cmsId;
            var sortByTimeValue = param.sortByTimeValue;
            var insertTimeGds = param.insertTimeGds;
            var sort = param.sort;

            var json = {};
            json.goodsTypeId = goodsTypeId;
            json.activityId = activityId;
            json.firstGoodsTypeId = firstGoodsTypeId;
            json.from = from;
            json.to = to;
            json.insertTime = insertTime;
            json.memberId = memberId;
            json.goodsIds = goodsIds;
            json.cmsId = cmsId;
            json.sortByTime = sortByTimeValue;
            json.insertTimeGds = insertTimeGds;
            json.sort = sort;

            if(activityId != null && activityId.length > 0){
                $http.post("../gds_master_api/getProductListByActivityId.json", $jq.param({'data':JSON.stringify(json)})).then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
                return defer.promise;
            }else {
                $http.post("../gds_master_api/getProductList.json", $jq.param({'data':JSON.stringify(json)})).then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
                return defer.promise;
            }

        };
        return getData;// 此处return上面的 方法
    })

    .factory("goodsListDisplayService", function($http,$q) {

        function getData(param) { //<———————需要给原来的方法体做一个内部方法名字尽量起的有意义（getxxx、savexxxx等）
            var defer = $q.defer();
            var goodsTypeId = param.goodsTypeId;
            var memberId = param.memberId;
            goodsTypeId = goodsTypeId + '/' + memberId;
            //var json = {};
            //json.goodsTypeId = goodsTypeId;
            $http.post("../gds_master_api/getProductListBySequre.json", $jq.param({'data':goodsTypeId})).then(
                function(response) {
                    defer.resolve(response.data);
                },function(){
                    defer.reject();
                });
            return defer.promise;
        };
        return getData;// 此处return上面的 方法
    })

    .factory("couponListService", function($http,$q) {
        
        function getMyCoupons(param) {
            var defer = $q.defer();
            $http.post("../coupon_master/getMyCoupons.json", $jq.param({'data': JSON.stringify(param)}))
                .then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
            return defer.promise;
        };
        return getMyCoupons;
    })
    .factory("pointExchangeService", function($http,$q) {

        function pointExchangeService(param) {
            function getPointExchangeCoupons() {
                var defer = $q.defer();
                $http.post("../coupon_master/getPointExchangeCoupons.json", $jq.param({'data': JSON.stringify(param)}))
                    .then(
                        function (response) {
                            defer.resolve(response.data);
                        }, function () {
                            defer.reject();
                        });
                return defer.promise;
            };
            return {
                getPointExchangeCoupons: getPointExchangeCoupons
            };

        }
        return pointExchangeService;
    })
    //.factory("homePageService", function($http,$q) {
    //
    //    function getData(param) {
    //        var defer = $q.defer();
    //        $http.get("./data/homepage.json").then(
    //            function(response) {
    //                defer.resolve(response.data.results);
    //            },function(error){
    //                defer.reject('error');
    //            });
    //        return defer.promise;
    //    };
    //    return getData;
    //})

    // add by pcd start

    // 轮播图
    .factory("indexFigureService", function($http,$q) {
        function getIndexFigureData(param) {
            var defer = $q.defer();
            var json = {};
            json.itemCode = 'index_figure';
            json.isChild = '0';
            $http.post("../cms_items_api/getMasterByItemList.json", $jq.param({'data': JSON.stringify(json)}))
                .then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                });
            return defer.promise;
        };
        return getIndexFigureData;
    })

    // 新闻部分
    .factory("indexNewsService", function($http,$q) {
        function getIndexNewsData(param) {
            var defer = $q.defer();
            var json = {};
            json.itemCode = 'index_new';
            json.isChild = '0';
            $http.post("../cms_items_api/getMasterByItemList.json", $jq.param({'data':JSON.stringify(json)}))
                .then(
                    function(response) {
                        defer.resolve(response.data);
                        // console.log(response.data.results);
                    },function(){
                        defer.reject();
                    });
            return defer.promise;
        };
        return getIndexNewsData;
    })


    .factory("indexNews1Service", ["$q","$http",function($q, $http) {
        function getIndexNews1Data(param) {
            var defer = $q.defer();
            var json = {};
            json.itemCode = 'index_new_1';
            json.isChild = '0';
            $http.post("../cms_items_api/getMasterByItemList.json", $jq.param({'data':JSON.stringify(json)}))
                .then(
                    function(response) {
                        defer.resolve(response.data);
                        // console.log(response.data.results);
                    },function(){
                        defer.reject();
                    });
            return defer.promise;
        };
        return getIndexNews1Data;
    }])
    .factory("indexNews2Service", ["$q","$http",function($q, $http) {
        function getIndexNews2Data(param) {
            var defer = $q.defer();
            var json = {};
            json.itemCode = 'index_new_2';
            json.isChild = '0';
            $http.post("../cms_items_api/getMasterByItemList.json", $jq.param({'data':JSON.stringify(json)}))
                .then(
                    function(response) {
                        defer.resolve(response.data);
                        // console.log(response.data.results);
                    },function(){
                        defer.reject();
                    });
            return defer.promise;
        };
        return getIndexNews2Data;
    }])

    .factory("indexNews3Service", ["$q","$http",function($q, $http) {
        function getIndexNews3Data(param) {
            var defer = $q.defer();
            var json = {};
            json.itemCode = 'index_new_3';
            json.isChild = '0';
            $http.post("../cms_items_api/getMasterByItemList.json", $jq.param({'data':JSON.stringify(json)}))
                .then(
                    function(response) {
                        defer.resolve(response.data);
                        // console.log(response.data.results);
                    },function(){
                        defer.reject();
                    });
            return defer.promise;
        };
        return getIndexNews3Data;
    }])

    // 活动
    .factory("indexArrivalsService", function($http,$q) {
        function getIndexArrivalsData(param) {
            var defer = $q.defer();
            var json = {};
            json.itemCode = 'index_act';
            json.isChild = '0';
            $http.post("../cms_items_api/getMasterByItemList.json", $jq.param({'data': JSON.stringify(json)}))
                .then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
            return defer.promise;
        };
        return getIndexArrivalsData;
    })

    // 新品推荐
    .factory("indexNewGoodsService", function($http,$q) {
        function getIndexNewGoodsData(param) {
            var defer = $q.defer();
            var json = {};
            json.itemCode = 'index_goods_main';
            json.isChild = '0';
            $http.post("../cms_items_api/getMasterByItemList.json", $jq.param({'data': JSON.stringify(json)}))
                .then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
            return defer.promise;
        };
        return getIndexNewGoodsData;
    })


    // 区域2
    .factory("indexRegion2Service", ["$q","$http",function($q, $http) {
        function getIndexRegion2Data(param) {
            var defer = $q.defer();
            var json = {};
            json.itemCode = 'index_region_2';
            json.isChild = '0';
            $http.post("../cms_items_api/getListByItem.json", $jq.param({'data':JSON.stringify(json)}))
                .then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
            return defer.promise;
        };
        return getIndexRegion2Data;
    }])

    // 区域3手机
    .factory("indexRegion3PhoneService", ["$q","$http",function($q, $http) {
        function getIndexRegion3PhoneData(param) {
            var defer = $q.defer();
            var json = {};
            json.itemCode = 'index_region_3_phone';
            json.isChild = '0';
            $http.post("../cms_items_api/getListByItem.json", $jq.param({'data':JSON.stringify(json)}))
                .then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
            return defer.promise;
        };
        return getIndexRegion3PhoneData;
    }])
    // 区域4
    .factory("indexRegion4Service", ["$q","$http",function($q, $http) {
        function getIndexRegion4Data(param) {
            var defer = $q.defer();
            var json = {};
            json.itemCode = 'index_region_4';
            json.isChild = '0';
            $http.post("../cms_items_api/getListByItem.json", $jq.param({'data':JSON.stringify(json)}))
                .then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
            return defer.promise;
        };
        return getIndexRegion4Data;
    }])
    // 区域5
    .factory("indexRegion5Service", ["$q","$http",function($q, $http) {
        function getIndexRegion5Data(param) {
            var defer = $q.defer();
            var json = {};
            json.itemCode = 'index_region_5';
            json.isChild = '0';
            $http.post("../cms_items_api/getListByItem.json", $jq.param({'data':JSON.stringify(json)}))
                .then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
            return defer.promise;
        };
        return getIndexRegion5Data;
    }])
    // 区域6
    .factory("indexRegion6Service", ["$q","$http",function($q, $http) {
        function getIndexRegion6Data(param) {
            var defer = $q.defer();
            var json = {};
            json.itemCode = 'index_region_6';
            json.isChild = '0';
            $http.post("../cms_items_api/getListByItem.json", $jq.param({'data':JSON.stringify(json)}))
                .then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
            return defer.promise;
        };
        return getIndexRegion6Data;
    }])
    // 区域7
    .factory("indexRegion7Service", ["$q","$http",function($q, $http) {
        function getIndexRegion7Data(param) {
            var defer = $q.defer();
            var json = {};
            json.itemCode = 'index_region_7';
            json.isChild = '0';
            $http.post("../cms_items_api/getListByItem.json", $jq.param({'data':JSON.stringify(json)}))
                .then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
            return defer.promise;
        };
        return getIndexRegion7Data;
    }])
    // 区域8
    .factory("indexRegion8Service", ["$q","$http",function($q, $http) {
        function getIndexRegion8Data(param) {
            var defer = $q.defer();
            var json = {};
            json.itemCode = 'index_region_8';
            json.isChild = '0';
            $http.post("../cms_items_api/getListByItem.json", $jq.param({'data':JSON.stringify(json)}))
                .then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
            return defer.promise;
        };
        return getIndexRegion8Data;
    }])



    // 商品分类
    .factory("indexGdsSortsService", function($http,$q) {
        function getIndexGdsSortsData(param) {
            var defer = $q.defer();
            var json = {};
            json.itemCode = 'index_gds_sorts';
            json.isChild = '0';
            $http.post("../cms_items_api/getMasterByItemList.json", $jq.param({'data': JSON.stringify(json)}))
                .then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
            return defer.promise;
        };
        return getIndexGdsSortsData;
    })

    // 品牌系列的获取接口
    .factory("indexGdsBrandTypeService", function($http,$q) {
        function getIndexGdsBrandTypeData(param) {
            var defer = $q.defer();
            $http.post("../gds_type_api/getGdsBrandType.json", {})
                .then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
            return defer.promise;
        };
        return getIndexGdsBrandTypeData;
    })
    // add by pcd end

    //add by cky start
    .factory("gdsTypeService", function($http,$q) {
        var defer = $q.defer();
        //首页上获取第一层分类的数据(10.ERP分类，20.电商分类 不传递参数则全部展示)
        $http.get("../gds_type_api/getGdsTypeFirstFloor.json").then(
            function(response) {
                defer.resolve(response.data);
            },function(){
                defer.reject();
            });
        return defer.promise;
    })
    //add by cky end
    .factory("productDetailService", function($http,$q) {

        function getData(param) { //<———————需要给原来的方法体做一个内部方法名字尽量起的有意义（getxxx、savexxxx等）
            var defer = $q.defer();
            var goodsId = param.goodsId;
            var memberId = param.memberId;
            var json = {};
            json.goodsId = goodsId;
            json.memberId = memberId;
            $http.post("../gds_master_api/getProductDetail.json", $jq.param({'data':JSON.stringify(json)})).then(
                function(response) {
                    defer.resolve(response.data);
                },function(error){
                    defer.reject('error');
                });
            return defer.promise;
        };
        return getData;// 此处return上面的 方法

        //var defer = $q.defer();
        //
        //$http.get("./data/productDetail.json").then(
        //    function(response) {
        //        defer.resolve(response.data.results);
        //        console.log()
        //    },function(){
        //        defer.reject();
        //    });
        //return defer.promise;
    })
    //.factory("classificationService", function($http,$q) {
    //
    //    var defer = $q.defer();
    //
    //    $http.get("./data/classification.json").then(
    //        function(response) {
    //            defer.resolve(response.data.results);
    //            console.log()
    //        },function(){
    //            defer.reject();
    //        });
    //    return defer.promise;
    //})
    //add by cky start
    .factory("displayUriService", function($http,$q) {
        function displayUriService(){
            var defer = $q.defer();
            //获取全局用的图片访问baseUri
            $http.get("../common/getImageUrl.json").then(
                function(response) {
                    defer.resolve(response.data);
                },function(){
                    defer.reject();
                });
            return defer.promise;
        }
        return displayUriService
    })

    //获取码表中记录的库存限制 code为20 50
    .factory("getBnkLimitService", function($http,$q) {
        function getBnkLimitService(){
            var defer = $q.defer();
            //获取全局用的图片访问baseUri
            $http.get("../common/getBnkLimit.json").then(
                function(response) {
                    defer.resolve(response.data);
                },function(){
                    defer.reject();
                });
            return defer.promise;
        }
        return getBnkLimitService
    })

    .factory("secondGdsTypeService", function($http,$q) {
        function secondGdsTypeService(){
            var defer = $q.defer();
            //分类画面获取第二层分类的数据(10.ERP分类，20.电商分类 不传递参数则全部展示)
            $http.get("../gds_type_api/getGdsTypeFirstAndSecondFloor.json").then(
                function(response) {
                    defer.resolve(response.data);
                },function(){
                    defer.reject();
                });
            return defer.promise;
        }
        return secondGdsTypeService;
    })

    .factory("subGdsTypeService", function($http,$q) {
        function getData(param) {
            var defer = $q.defer();
            var goodsTypeId = param["goodsTypeId"] || '';

            //分类画面获取右侧显示区域数据(第三层数据+图片)
            $http.post("../gds_type_api/getGdsTypeSubFloor.json",$jq.param({'data':goodsTypeId})).then(
                function(response) {
                    defer.resolve(response.data);
                },function(){
                    defer.reject();
                });
            return defer.promise;
        };
        return getData;

    })

    .factory("secondGdsBrandTypeService", function($http,$q) {
        function secondGdsBrandTypeService(){
            var defer = $q.defer();
            //分类画面获取第二层分类的数据(10.ERP分类，20.电商分类 不传递参数则全部展示)
            $http.get("../gds_type_api/getGdsBrandTypeFirstAndSecondFloor.json").then(
                function(response) {
                    defer.resolve(response.data);
                },function(){
                    defer.reject();
                });
            return defer.promise;
        }
        return secondGdsBrandTypeService;
    })

    .factory("subGdsBrandTypeService", function($http,$q) {
        function getData(param) {
            var defer = $q.defer();
            var goodsTypeId = param["goodsTypeId"] || '';

            //分类画面获取右侧显示区域数据(第三层数据+图片)
            $http.post("../gds_type_api/getGdsBrandTypeSubFloor.json",$jq.param({'data':goodsTypeId})).then(
                function(response) {
                    defer.resolve(response.data);
                },function(){
                    defer.reject();
                });
            return defer.promise;
        };
        return getData;

    })
    //add by cky end
    .factory("couponAllService", function($http,$q) {

        function getAllCoupons(param) {
            var defer = $q.defer();
            $http.post("../coupon_master/getAllCoupons.json", $jq.param({'data': JSON.stringify(param)}))
                .then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){g
                        defer.reject();
                    });
            return defer.promise;
        };
        return getAllCoupons;
    })
    .factory("getCouponService", function($http,$q) {

        function getCoupon(param) {
            var defer = $q.defer();
            $http.post("../coupon_master/addCouponsForUser.json", $jq.param({'data': JSON.stringify(param)}))
                .then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
            return defer.promise;
        };
        return getCoupon;
    })
    .factory("confirmOrderService", function($http,$q) {
        function confirmOrderService(param) {
            function getDataByBag() {
                var defer = $q.defer();
                $http.post("../ord_wechat/confirmOrderFromBag.json", $jq.param({'data': JSON.stringify(param)}))
                    .then(
                        function (response) {
                            defer.resolve(response.data);
                        }, function () {
                            defer.reject();
                        });
                return defer.promise;
            };
            function getDataBySingleGoods() {
                var defer = $q.defer();
                $http.post("../ord_wechat/confirmOrderFromGoods.json", $jq.param({'data': JSON.stringify(param)}))
                    .then(
                        function (response) {
                            defer.resolve(response.data);
                        }, function () {
                            defer.reject();
                        });
                return defer.promise;
            };
            function getDataBySuitGoods() {
                var defer = $q.defer();
                $http.post("../ord_wechat/confirmOrderFromSuitGoods.json", $jq.param({'data': JSON.stringify(param)}))
                    .then(
                        function (response) {
                            defer.resolve(response.data);
                        }, function () {
                            defer.reject();
                        });
                return defer.promise;
            };
            function submitOrder() {
                var defer = $q.defer();
                $http.post("../ord_wechat/submitOrder.json", $jq.param({'data': JSON.stringify(param)}))
                    .then(
                        function (response) {
                            defer.resolve(response.data);
                        }, function () {
                            defer.reject();
                        });
                return defer.promise;
            };
            function getOrderCoupons() {
                var defer = $q.defer();
                $http.post("../coupon_master/getOrderCoupons.json", $jq.param({'data': JSON.stringify(param)}))
                    .then(
                        function (response) {
                            defer.resolve(response.data);
                        }, function () {
                            defer.reject();
                        });
                return defer.promise;
            };
            function getGiftDetailByCode() {
                var defer = $q.defer();
                $http.post("../gds_master_api/getGiftDetailByCode.json", $jq.param({'data': JSON.stringify(param)}))
                    .then(
                        function (response) {
                            defer.resolve(response.data);
                        }, function () {
                            defer.reject();
                        });
                return defer.promise;
            };
            return {
                getDataByBag: getDataByBag,
                getDataBySingleGoods: getDataBySingleGoods,
                getDataBySuitGoods: getDataBySuitGoods,
                submitOrder: submitOrder,
                getOrderCoupons: getOrderCoupons,
                getGiftDetailByCode: getGiftDetailByCode
            };

        }
        return confirmOrderService;
    })

    .factory("userService", function($http) {
        var users = [];
        return {
            getUsers: function() {
                return $http.get("https://randomuser.me/api/?results=10").then(function(response) {
                    users = response.data.results;
                    return response.data.results;
                });
            },
            getUser: function(index) {
                return users[index];
            }
        };
    })

    // 海报
    .factory("activityPointsService", ["$q","$http",function($q, $http) {
        function activityPointsService(param) {
            var defer = $q.defer();
            var json = {};
            json.itemCode = 'activity_points';
            json.isChild = '0';
            $http.post("../cms_items_api/getListByItem.json", $jq.param({'data':JSON.stringify(json)}))
                .then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
            return defer.promise;
        };
        return activityPointsService;
    }])
    .factory("activityListService", function($http,$q) {

        function getData(params) { //<———————需要给原来的方法体做一个内部方法名字尽量起的有意义（getxxx、savexxxx等）
            var defer = $q.defer();
            var json = {};
            var memberId = params.memberId;
            json.memberId = memberId;
            $http.post("../points_exchange_api/activityListService.json",$jq.param({'data':JSON.stringify(json)})).then(
                function(response) {
                    defer.resolve(response.data);
                },function(){
                    defer.reject();
                });
            return defer.promise;
        };
        return getData;// 此处return上面的 方法
    })




    .factory('localStorageService', [function() {
            return {
                get: function localStorageServiceGet(key, defaultValue) {
                    var stored = localStorage.getItem(key);
                    try {
                        stored = angular.fromJson(stored);
                    } catch (error) {
                        stored = null;
                    }
                    if (defaultValue && stored === null) {
                        stored = defaultValue;
                    }
                    return stored;
                },
                set: function localStorageServiceUpdate(key, value) {
                    if (value) {
                        localStorage.setItem(key, angular.toJson(value));
                    }
                },
                clear: function localStorageServiceClear(key) {
                    localStorage.removeItem(key);
                }
            };
        }])
    .factory('dateService', [function() {
        return {
            handleMessageDate: function(messages) {
                var i = 0,
                    length = 0,
                    messageDate = {},
                    nowDate = {},
                    weekArray = ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
                    diffWeekValue = 0;
                if (messages) {
                    nowDate = this.getNowDate();
                    length = messages.length;
                    for (i = 0; i < length; i++) {
                        messageDate = this.getMessageDate(messages[i]);
                        if(!messageDate){
                            return null;
                        }
                        if (nowDate.year - messageDate.year > 0) {
                            messages[i].lastMessage.time = messageDate.year + "";
                            continue;
                        }
                        if (nowDate.month - messageDate.month >= 0 ||
                            nowDate.day - messageDate.day > nowDate.week) {
                            messages[i].lastMessage.time = messageDate.month +
                                "月" + messageDate.day + "日";
                            continue;
                        }
                        if (nowDate.day - messageDate.day <= nowDate.week &&
                            nowDate.day - messageDate.day > 1) {
                            diffWeekValue = nowDate.week - (nowDate.day - messageDate.day);
                            messages[i].lastMessage.time = weekArray[diffWeekValue];
                            continue;
                        }
                        if (nowDate.day - messageDate.day === 1) {
                            messages[i].lastMessage.time = "昨天";
                            continue;
                        }
                        if (nowDate.day - messageDate.day === 0) {
                            messages[i].lastMessage.time = messageDate.hour + ":" + messageDate.minute;
                            continue;
                        }
                    }
                    // console.log(messages);
                    // return messages;
                } else {
                    console.log("messages is null");
                    return null;
                }

            },
            getNowDate: function() {
                var nowDate = {};
                var date = new Date();
                nowDate.year = date.getFullYear();
                nowDate.month = date.getMonth();
                nowDate.day = date.getDate();
                nowDate.week = date.getDay();
                nowDate.hour = date.getHours();
                nowDate.minute = date.getMinutes();
                nowDate.second = date.getSeconds();
                return nowDate;
            },
            getMessageDate: function(message) {
                var messageDate = {};
                var messageTime = "";
                //2015-10-12 15:34:55
                var reg = /(^\d{4})-(\d{1,2})-(\d{1,2})\s(\d{1,2}):(\d{1,2}):(\d{1,2})/g;
                var result = new Array();
                if (message) {
                    messageTime = message.lastMessage.originalTime;
                    result = reg.exec(messageTime);
                    if (!result) {
                        console.log("result is null");
                        return null;
                    }
                    messageDate.year = parseInt(result[1]);
                    messageDate.month = parseInt(result[2]);
                    messageDate.day = parseInt(result[3]);
                    messageDate.hour = parseInt(result[4]);
                    messageDate.minute = parseInt(result[5]);
                    messageDate.second = parseInt(result[6]);
                    // console.log(messageDate);
                    return messageDate;
                } else {
                    console.log("message is null");
                    return null;
                }
            }
        };
    }])
    .factory('messageService', ['localStorageService', 'dateService',
        function(localStorageService, dateService) {
            return {
                init: function(messages) {
                    var i = 0;
                    var length = 0;
                    var messageID = new Array();
                    var date = null;
                    var messageDate = null;
                    if (messages) {
                        length = messages.length;
                        for (; i < length; i++) {
                            messageDate = dateService.getMessageDate(messages[i]);
                            if(!messageDate){
                                return null;
                            }
                            date = new Date(messageDate.year, messageDate.month,
                                messageDate.day, messageDate.hour, messageDate.minute,
                                messageDate.second);
                            messages[i].lastMessage.timeFrome1970 = date.getTime();
                            messageID[i] = {
                                id: messages[i].id
                            };

                        }
                        localStorageService.update("messageID", messageID);
                        for (i = 0; i < length; i++) {
                            localStorageService.update("message_" + messages[i].id, messages[i]);
                        }
                    }
                },
                getAllMessages: function() {
                    var messages = new Array();
                    var i = 0;
                    var messageID = localStorageService.get("messageID");
                    var length = 0;
                    var message = null;
                    if (messageID) {
                        length = messageID.length;

                        for (; i < length; i++) {
                            message = localStorageService.get("message_" + messageID[i].id);
                            if(message){
                                messages.push(message);
                            }
                        }
                        dateService.handleMessageDate(messages);
                        return messages;
                    }
                    return null;

                },
                getMessageById: function(id){
                    return localStorageService.get("message_" + id);
                },
                getAmountMessageById: function(num, id){
                    var messages = [];
                    var message = localStorageService.get("message_" + id).message;
                    var length = 0;
                    if(num < 0 || !message) return;
                    length = message.length;
                    if(num < length){
                        messages = message.splice(length - num, length);
                        return messages;
                    }else{
                        return message;
                    }
                },
                updateMessage: function(message) {
                    var id = 0;
                    if (message) {
                        id = message.id;
                        localStorageService.update("message_" + id, message);
                    }
                },
                deleteMessageId: function(id){
                    var messageId = localStorageService.get("messageID");
                    var length = 0;
                    var i = 0;
                    if(!messageId){
                        return null;
                    }
                    length = messageId.length;
                    for(; i < length; i++){
                        if(messageId[i].id === id){
                            messageId.splice(i, 1);
                            break;
                        }
                    }
                    localStorageService.update("messageID", messageId);
                },
                clearMessage: function(message) {
                    var id = 0;
                    if (message) {
                        id = message.id;
                        localStorageService.clear("message_" + id);
                    }
                }
            };
        }
    ])
    .service('popupService', ['$rootScope', '$ionicPopup', '$timeout', '$ionicLoading',
    function ($rootScope, $ionicPopup, $timeout, $ionicLoading) {

        // A confirm dialog
        this.showConfirm = function (title, message, callback,okText) {
            var defaultTitle = '提示';
            var defaultMessage = '网络出错,请检查网络后重试.';
            var defaultOkText = '确定';
            var defaultCancelText = '取消';

            var confirmPopup = $ionicPopup.show({
                title: title ? title : defaultTitle,
                okText: okText ? okText : defaultOkText,
                cancelText: defaultCancelText,
                template: message ? message : defaultMessage,
                buttons: [
                    { text: '取消',
                        type: 'button-small',
                    },{ text: okText ? okText : defaultOkText,
                        type: 'button-dark button-small',
                        onTap: callback
                    }
                ]
            });
          //  confirmPopup.then(callback);
        };

        // An alert dialog
        this.showAlert = function (title, message, callback) {
            var defaultTitle = '提示';
            var defaultMessage = '网络出错,请检查网络后重试.';
            var defaultOkText = '确定';
            var alertPopup = $ionicPopup.alert({
                title: title ? title : defaultTitle,
                okText: defaultOkText,
                template: message ? message : defaultMessage,
                buttons: [
                    { text: '确定',
                        type: 'button-dark button-small',
                        onTap: callback
                    }
                ]
            });
        };

        //Toast message
        this.showToast = function(message){
            var defaultMessage = '网络出错,请检查网络后重试.';
            $ionicLoading.show({
                template: message?message:defaultMessage,
                duration: 2000,  //持续时间1000ms后调用hide()
                noBackdrop:true
            });
        };

        this.showLongTimeToast = function(message){
            var defaultMessage = '';
            $ionicLoading.show({
                template: message?message:defaultMessage,
                duration: 3000,  //持续时间1000ms后调用hide()
                noBackdrop:true
            });
        };

        //图片查看popup
        this.showImgViewer = function (imgSrc, hasImageFilter) {
            //默认图片过滤true
            var imageSrc = '';
            var hasImgFilter = arguments[1] ? arguments[1] : true;
            if (hasImgFilter) {
                var nsrc = 'default.png';
                if (imgSrc != null && imgSrc != '') {
                    nsrc = imgSrc;
                }
                imageSrc = nsrc;
            } else {
                imageSrc = imgSrc;
            }
            var imgViewerPopup = $ionicPopup.show({
                template: '<img ng-src="' + imageSrc + '">',
                title: '',
                subTitle: '',
                cssClass: 'jsh-img-viewer',
                buttons: [
                    {
                        text: '&#xe642;',
                        type: 'jsh-img-viewer-close dhcfont',
                        onTap: function (e) {
                            imgViewerPopup.close();
                        }
                    }
                ]
            });
        }

    }
]).service('WeiXinService', ['$http','$q','popupService', function ($http,$q, popupService) {
    this.getOpenid = function(param) {
        var defer = $q.defer();
        $http.post("../wechat/getOpenIdByCode.json", $jq.param({'data': JSON.stringify(param)}))
            .then(
                function(response) {
                    defer.resolve(response.data);
                },function(){
                    defer.reject();
                });
        return defer.promise;
    };
    this.getWxInfo = function(param) {
        var defer = $q.defer();
        $http.post("../wechat/getWxSignature.json", $jq.param({'data': JSON.stringify(param)}))
            .then(
                function(response) {
                    defer.resolve(response.data);
                },function(){
                    defer.reject();
                });
        return defer.promise;
    };

    this.getWxPayInfo = function(param) {
        var defer = $q.defer();
        $http.post("../wechat/getWxPayInfo.json", $jq.param({'data': JSON.stringify(param)}))
            .then(
                function(response) {
                    defer.resolve(response.data);
                },function(){
                    defer.reject();
                });
        return defer.promise;
    };

    this.configShare = function (title, link, imgUrl, desc) {
        var defaultOption = {
            title: title, // 分享标题
            desc: desc,
            link: link , // 分享链接
            imgUrl: imgUrl, // 分享图标
            success: function () {
            },
            fail: function (res) {
            }
        };
        wx.onMenuShareTimeline(defaultOption);
        wx.onMenuShareAppMessage(defaultOption);
        wx.onMenuShareQQ(defaultOption);
        wx.onMenuShareWeibo(defaultOption);
        wx.onMenuShareQZone(defaultOption);
    },
        this.initWeixinJS = function (jsApiList) {
            if (!jsApiList) {
                jsApiList = [
                    'onMenuShareTimeline',
                    'onMenuShareAppMessage',
                    'onMenuShareQQ',
                    'onMenuShareWeibo',
                    'onMenuShareQZone',
                    'startRecord',
                    'stopRecord',
                    'onVoiceRecordEnd',
                    'playVoice',
                    'pauseVoice',
                    'stopVoice',
                    'onVoicePlayEnd',
                    'uploadVoice',
                    'downloadVoice',
                    'chooseImage',
                    'previewImage',
                    'uploadImage',
                    'downloadImage',
                    'translateVoice',
                    'getNetworkType',
                    'openLocation',
                    'getLocation',
                    'hideOptionMenu',
                    'showOptionMenu',
                    'hideMenuItems',
                    'showMenuItems',
                    'hideAllNonBaseMenuItem',
                    'showAllNonBaseMenuItem',
                    'closeWindow',
                    'scanQRCode',
                    'chooseWXPay',
                    'openProductSpecificView',
                    'addCard',
                    'chooseCard',
                    'openCard'
                ];
            }
            // 从服务器获取微信签名等信息
            this.getWxInfo({url: location.href.split('#')[0]}).then(function (data) {
                if (data.resultCode == "00") {
                    var result = data.results;
                    wx.config({
                        debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                        appId: result.appid, // 必填，公众号的唯一标识
                        timestamp: result.timestamp, // 必填，生成签名的时间戳
                        nonceStr: result.noncestr, // 必填，生成签名的随机串
                        signature: result.signature,// 必填，签名，见附录1
                        jsApiList: jsApiList // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
                    });
                } else {
                    popupService.showToast('微信接口初始化失败。');
                }
            });

        }
}]);