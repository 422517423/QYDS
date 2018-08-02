angular.module('dealuna.services', [])

    .factory('fileReader', ["$q", "$log", function($q, $log){
        var onLoad = function(reader, deferred, scope) {
            return function () {
                scope.$apply(function () {
                    deferred.resolve(reader.result);
                });
            };
        };
        var onError = function (reader, deferred, scope) {
            return function () {
                scope.$apply(function () {
                    deferred.reject(reader.result);
                });
            };
        };
        var getReader = function(deferred, scope) {
            var reader = new FileReader();
            reader.onload = onLoad(reader, deferred, scope);
            reader.onerror = onError(reader, deferred, scope);
            return reader;
        };
        var readAsDataURL = function (file, scope) {
            var deferred = $q.defer();
            var reader = getReader(deferred, scope);
            reader.readAsDataURL(file);
            var names = file.name.split(".");
            return deferred.promise;
        };
        return {
            readAsDataUrl: readAsDataURL
        };
    }])

    // 上传图片
    .factory("uploadImageService",["$q","$http", function($q, $http) {
        function getImgUrl(params) {
            var data = params.data;
            var defer = $q.defer();
            var json = {
                type : data.type,
                file : data.file,
                fileName : data.fileName,
                suffix : data.suffix
            };

            $.ajax({
                type: "post",
                data: json,
                url: UPLOAD_URI,
                dataType: "json",
                success: function (d) {
                    defer.resolve(d);
                },
                error: function (e) {
                    defer.reject();
                }
            })
            // $http.post(UPLOAD_URI, $.param({"form": json}))
            //     .then(
            //         function (response) {
            //             defer.resolve(response.data);
            //         }, function () {
            //             defer.reject();
            //         });
            return defer.promise;
        };
        return getImgUrl;
    }])
    .factory('LoadingIntercepter', ['$rootScope', '$q', function ($rootScope, $q) {
        /**
         * LOADING 拦截器
         */
        return {
            request: function (config) {
                var pattern = /.json$/;
                if (pattern.exec(config.url)) {
                    //判断是否显示加载条，noLoading为true时不显示。
                    if (config.showLoading) {
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
    .factory('alertService', function($rootScope,$sce) {
        var alertService = {};

        // 创建一个全局的 alert 数组
        $rootScope.alerts = [];

        alertService.add = function(type, msg,onclick) {
            $rootScope.alerts.push({
                'type': type,
                'msg': $sce.trustAsHtml(msg),
                'close': function(){
                    alertService.closeAlert(this);
                },
                'onClick': function(){
                    alertService.clickAlert(this,onclick);
                }
            });
        };

        alertService.clickAlert = function(alert,onclick) {
            alertService.closeAlertIdx($rootScope.alerts.indexOf(alert));
            onclick();
        };

        alertService.closeAlert = function(alert) {
            alertService.closeAlertIdx($rootScope.alerts.indexOf(alert));
        };

        alertService.closeAlertIdx = function(index) {
            $rootScope.alerts.splice(index, 1);
        };

        return alertService;
    })

    // 共通:省区市取得
    .factory("commonService",  ["$q","$http",function($q,$http) {
        function commonService(param){
            function getProvinces() {
                var defer = $q.defer();
                $http.post("/qyds-web-pc/com_discrict/getProvinces.json").then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function () {
                        defer.reject();
                    });
                return defer.promise;
            };

            function getSubAddresses() {
                var defer = $q.defer();
                $http.post("/qyds-web-pc/com_discrict/getSubAddresses.json", $.param({'parentId':param.parentId})).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function () {
                        defer.reject();
                    });
                return defer.promise;
            };


            function sendCaptcha() {
                var defer = $q.defer();
                $http.post("/qyds-web-pc/captcha/send.json", $.param({'data': JSON.stringify(param)})).then(
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
    }])

    // 登录注册
    .factory("authService", ["$q","$http",function($q,$http) {

        function authService(param){
            function login() {
                var defer = $q.defer();
                $http.post("/qyds-web-pc/auth/login.json", $.param({'data':JSON.stringify(param)})).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function (error) {
                        defer.reject('error');
                    });
                return defer.promise;
            };

            function register() {
                var defer = $q.defer();
                $http.post("/qyds-web-pc/auth/register.json", $.param({'data':JSON.stringify(param)})).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function (error) {
                        defer.reject('error');
                    });
                return defer.promise;
            };

            function changePassword() {
                var defer = $q.defer();
                $http.post("/qyds-web-pc/auth/changePassword.json", $.param({'data':JSON.stringify(param)})).then(
                    function (response) {
                        console.log(response);
                        defer.resolve(response.data);
                    }, function (error) {
                        defer.reject('error');
                    });
                return defer.promise;
            };

            function getPersonalInfo() {
                var defer = $q.defer();
                $http.post("/qyds-web-pc/mmb_master/getDetail.json", $.param({'data':JSON.stringify(param)})).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function (error) {
                        defer.reject('error');
                    });
                return defer.promise;
            };

            return {login:login,
                register:register,
                changePassword:changePassword,
                getPersonalInfo:getPersonalInfo}
        }

        return authService;
    }])

    .factory('storeService', ["$q","$http",function($q,$http) {
        function storeService(param){

            function getStoreList() {
                var defer = $q.defer();
                $http.post("/qyds-web-pc/store/getOrgList.json",$.param({'data':JSON.stringify(param)}))
                    .then(
                        function (response) {
                            defer.resolve(response.data);
                        }, function () {
                            defer.reject();
                        });
                return defer.promise;
            };

            function getOrgAddressList() {
                var defer = $q.defer();
                $http.post("/qyds-web-pc/store/getOrgAddressList.json", $.param({'data':JSON.stringify(param)}), {noLoading:true}).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function () {
                        defer.reject();
                    });
                return defer.promise;
            };

            return {
                getStoreList:getStoreList,
                getOrgAddressList:getOrgAddressList
            };

        }
        return storeService;

    }])

    //// 联系我们
    //.factory('contactUsService', ["$q","$http",function($q,$http) {
    //    function getContactUs() {
    //        var defer = $q.defer();
    //        var json = {};
    //        json.itemCode = "index_contact";
    //        $http.post("/qyds-web-pc/cms_items_api/getContentHtmlByItemCode.json",$.param({'data':JSON.stringify(json)}))
    //            .then(
    //                function (response) {
    //                    defer.resolve(response.data);
    //                }, function () {
    //                    defer.reject();
    //                });
    //        return defer.promise;
    //    };
    //    return getContactUs;
    //}])

    // 订单确认
    .factory("contactUsService", ["$q","$http",function($q,$http) {
        function contactUsService(param) {
            function upInfo() {
                var defer = $q.defer();
                $http.post("/qyds-web-pc/mmb_contact/add.json", $.param({'data': JSON.stringify(param)}))
                    .then(
                        function (response) {
                            defer.resolve(response.data);
                        }, function () {
                            defer.reject();
                        });
                return defer.promise;
            };

            return {
                upInfo: upInfo
            };

        }
        return contactUsService;
    }])


    .factory("displayUriService", function($http,$q) {
        function displayUriService(){
            var defer = $q.defer();
            //获取全局用的图片访问baseUri
            $http.get("/qyds-web-pc/common/getImageUrl.json").then(
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
            $http.get("/qyds-web-pc/common/getBnkLimit.json").then(
                function(response) {
                    defer.resolve(response.data);
                },function(){
                    defer.reject();
                });
            return defer.promise;
        }
        return getBnkLimitService
    })

    .factory("uploadUriService", function($http,$q) {
        function uploadUriService(){
            var defer = $q.defer();
            //获取全局用的图片访问baseUri
            $http.get("/qyds-web-pc/common/getUploadUrl.json").then(
                function(response) {
                    defer.resolve(response.data);
                },function(){
                    defer.reject();
                });
            return defer.promise;
        }
        return uploadUriService
    })

    // 获取商品分类的二级主页内容
    .factory("goodsTypeIndexService", function($http,$q) {
        function getGoodsTypeIndexService(params){
            var defer = $q.defer();
            var json = {};
            json.goodsTypeId = params.firstGoodsTypeId;
            json.memberId = params.memberId;
            $http.post("/qyds-web-pc/cms_items_api/getGoodsTypeIndex.json", $.param({'data':JSON.stringify(json)})).then(
                function(response) {
                    defer.resolve(response.data);
                },function(){
                    defer.reject();
                });
            return defer.promise;
        }
        return getGoodsTypeIndexService
    })

    .factory("activityListService", function($http,$q) {

        function getData(params) { //<———————需要给原来的方法体做一个内部方法名字尽量起的有意义（getxxx、savexxxx等）
            var defer = $q.defer();
            var json = {};
            var memberId = params.memberId;
            json.memberId = memberId;
            $http.post("/qyds-web-pc/points_exchange_api/activityListService.json",$.param({'data':JSON.stringify(json)})).then(
                function(response) {
                    defer.resolve(response.data);
                },function(){
                    defer.reject();
                });
            return defer.promise;
        };
        return getData;// 此处return上面的 方法
    })

    .factory("activityTypeListService", function($http,$q) {

        function getData(params) { //<———————需要给原来的方法体做一个内部方法名字尽量起的有意义（getxxx、savexxxx等）
            var defer = $q.defer();
            var json = {};
            var memberId = params.memberId;
            var firstGoodsTypeId = params.firstGoodsTypeId;
            json.memberId = memberId;
            json.firstGoodsTypeId=firstGoodsTypeId;
            $http.post("/qyds-web-pc/gds_master_pc_api/activityTypeListService.json",$.param({'data':JSON.stringify(json)})).then(
                function(response) {
                    defer.resolve(response.data);
                },function(){
                    defer.reject();
                });
            return defer.promise;
        };
        return getData;// 此处return上面的 方法
    })

    //用户下所有的活动信息
    .factory("activitysListService", function($http,$q) {

        function getData(params) { //<———————需要给原来的方法体做一个内部方法名字尽量起的有意义（getxxx、savexxxx等）
            var defer = $q.defer();
            var json = {};
            var memberId = params.memberId;
            json.memberId = memberId;
            $http.post("/qyds-web-pc/gds_master_pc_api/activitysListService.json",$.param({'data':JSON.stringify(json)})).then(
                function(response) {
                    defer.resolve(response.data);
                },function(){
                    defer.reject();
                });
            return defer.promise;
        };
        return getData;// 此处return上面的 方法
    })

    .factory("getNewDataService", function($http,$q) {

        function getData(params) { //<———————需要给原来的方法体做一个内部方法名字尽量起的有意义（getxxx、savexxxx等）
            var defer = $q.defer();
            var json = {};
            var firstGoodsTypeId = params.firstGoodsTypeId;
            json.goodsTypeId = firstGoodsTypeId;
            json.itemCode = "index_goods";
            $http.post("/qyds-web-pc/cms_items_api/getNewGoods.json",$.param({'data':JSON.stringify(json)})).then(
                function(response) {
                    defer.resolve(response.data);
                },function(){
                    defer.reject();
                });
            return defer.promise;
        };
        return getData;// 此处return上面的 方法
    })


    .factory("goodsListService", ["$q","$http",function($q,$http) {

        function getData(param) { //<———————需要给原来的方法体做一个内部方法名字尽量起的有意义（getxxx、savexxxx等）
            var defer = $q.defer();
            var goodsTypeId = param.goodsTypeId;
            var sizeCode = param.sizeCode;
            var activityId = param.actId;
            var firstGoodsTypeId = param.firstGoodsTypeId;
            var currentPage = param.currentPage;
            var totalPage = param.totalPage;
            var pageSize = param.pageSize;
            var from = param.from;
            var to = param.to;
            var updateTime = param.updateTime;
            var sortByPrice = param.sortByPrice;
            var sortByTime = param.sortByTime;
            var sortBySales = param.sortBySales;
            var memberId = param.memberId;
            var goodsIds = param.goodsIds;
            var cmsId = param.cmsId;
            var activityIds = param.actIds;

            var json = {};
            json.goodsTypeId = goodsTypeId;
            json.sizeCode = sizeCode;
            json.activityId = activityId;
            json.firstGoodsTypeId = firstGoodsTypeId;
            json.from = from;
            json.to = to;
            json.updateTime = updateTime;
            json.currentPage = currentPage;
            json.totalPage = totalPage;
            json.pageSize = pageSize;
            json.sortByPrice = sortByPrice;
            json.sortByTime = sortByTime;
            json.sortBySales = sortBySales;
            json.memberId = memberId;
            json.goodsIds = goodsIds;
            json.cmsId = cmsId;
            json.activityIds = activityIds;

            if(activityId != null && activityId.length > 0){
                $http.post("/qyds-web-pc/gds_master_pc_api/getProductListByActivityId.json", $.param({'data':JSON.stringify(json)})).then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
                return defer.promise;
            }else if(activityIds != null && activityIds.length > 0){
                $http.post("/qyds-web-pc/gds_master_pc_api/getProductListByActivityIds.json", $.param({'data':JSON.stringify(json)})).then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
                return defer.promise;
            }else {
                $http.post("/qyds-web-pc/gds_master_pc_api/getProductList.json", $.param({'data':JSON.stringify(json)})).then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
                return defer.promise;
            }

        };
        return getData;// 此处return上面的 方法
    }])

    .factory("goodsListDisplayService", function($http,$q) {

        function getData(param) { //<———————需要给原来的方法体做一个内部方法名字尽量起的有意义（getxxx、savexxxx等）
            var defer = $q.defer();
            var goodsTypeId = param.goodsTypeId;
            var memberId = param.memberId;
            goodsTypeId = goodsTypeId + '/' + memberId;
            //var json = {};
            //json.goodsTypeId = goodsTypeId;
            $http.post("/qyds-web-pc/gds_master_pc_api/getProductListBySequre.json", $.param({'data':goodsTypeId})).then(
                function(response) {
                    defer.resolve(response.data);
                },function(){
                    defer.reject();
                });
            return defer.promise;
        };
        return getData;// 此处return上面的 方法
    })

    .factory("hotSearchService", function($http,$q) {

        function getData(param) { //<———————需要给原来的方法体做一个内部方法名字尽量起的有意义（getxxx、savexxxx等）
            var defer = $q.defer();
            var searchKey = param.searchKey;
            var currentPage = param.currentPage;
            var totalPage = param.totalPage;
            var pageSize = param.pageSize;
            var memberId = param.memberId;

            var json = {};
            json.searchKey = searchKey;
            json.currentPage = currentPage;
            json.totalPage = totalPage;
            json.pageSize = pageSize;
            json.memberId = memberId;

            $http.post("/qyds-web-pc/gds_master_pc_api/getProductList.json", $.param({'data':JSON.stringify(json)})).then(
                function(response) {
                    defer.resolve(response.data);
                },function(){
                    defer.reject();
                });
            return defer.promise;
        };
        return getData;// 此处return上面的 方法
    })

    .factory("productDetailService", function($http,$q) {

        function getData(param) { //<———————需要给原来的方法体做一个内部方法名字尽量起的有意义（getxxx、savexxxx等）
            var defer = $q.defer();
            var goodsId = param.goodsId;
            var memberId = param.memberId;

            var json = {};
            json.goodsId = goodsId;
            json.memberId = memberId;
            $http.post("/qyds-web-pc/gds_master_pc_api/getProductDetail.json", $.param({'data':JSON.stringify(json)})).then(
                function(response) {
                    defer.resolve(response.data);
                },function(error){
                    defer.reject('error');
                });
            return defer.promise;
        };
        return getData;// 此处return上面的 方法
    })

    .factory("matingAndRecommendService", function($http,$q) {

        function getData(param) { //<———————需要给原来的方法体做一个内部方法名字尽量起的有意义（getxxx、savexxxx等）
            var defer = $q.defer();
            var goodsId = param.goodsId;
            var memberId = param.memberId;

            var json = {};
            json.goodsId = goodsId;
            json.memberId = memberId;
            $http.post("/qyds-web-pc/gds_master_pc_api/getMatingListAndRecommendList.json", $.param({'data':JSON.stringify(json)})).then(
                function(response) {
                    defer.resolve(response.data);
                },function(error){
                    defer.reject('error');
                });
            return defer.promise;
        };
        return getData;// 此处return上面的 方法
    })

    .service('popupService', ['$rootScope',  '$timeout',
        function ($rootScope, $timeout) {

            // A confirm dialog
            this.showConfirm = function ( message, okCb,okText,cancelCb,cancelText) {

                var cancelTxt = cancelText || '取消';
                var okText = okText || '确定';
                var modeless = new $.flavr({
                    content     : message,
                    dialog      : 'confirm',
                    buttons   : {
                        ok   : {
                            text:okText,
                            style:'info',
                            action: function () {
                                if(okCb) okCb();
                                modeless.close();
                                return false;
                            }
                        },
                        cancel   : {
                            text:cancelTxt,
                            action: function () {
                                if(cancelCb) cancelCb();
                                modeless.close();
                                return false;
                            }
                        }
                    }

                });
            };

            // An alert dialog
            this.showAlert = function ( message, callback) {
                var modeless = new $.flavr({
                                        content   : message,
                                        buttons   : {
                                            确定   : {
                                                action: function () {
                                                    if(callback) callback();
                                                    modeless.close();
                                                    return false;
                                                }
                                            }
                                        }

                                    });
            };

            //Toast message
            this.showToast = function(message,time){
                var defaultTime = time || 500;
                new $.flavr({
                    content     : message,
                    autoclose   : true,
                    buttons   : {},
                    timeout     : defaultTime
                });
            };

        }
    ])

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
// 头信息取得
.factory("headerService",["$q","$http", function($q, $http) {
    function headerService(param) {

        function getMetaData(){
            var defer = $q.defer();
            var json = {};
            // json.itemCode = 'index_gds_sorts,index_act';
            json.itemCode = 'seo';
            $http.post("/qyds-web-pc/cms_items_api/getMetaData.json", $.param({'data': JSON.stringify(json)}))
                .then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
            return defer.promise;
        }

        function getHeaderData(){
            var defer = $q.defer();
            var json = {};
            json.itemCode = 'index_gds_sorts';
            $http.post("/qyds-web-pc/cms_items_api/getMasterByItemArray.json", $.param({'data': JSON.stringify(json)}))
                .then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
            return defer.promise;
        }

        function getDeliverData(){
            var defer = $q.defer();
            var json = {};
            // json.itemCode = 'index_gds_sorts,index_act';
            json.itemCode = 'head_delivery';
            $http.post("/qyds-web-pc/cms_items_api/getDeliverData.json", $.param({'data': JSON.stringify(json)}))
                .then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
            return defer.promise;
        }

        return {
            getHeaderData : getHeaderData,
            getMetaData : getMetaData,
            getDeliverData : getDeliverData
        };

    };
    return headerService;

}])

// 品牌系列的获取接口
.factory("indexGdsBrandTypeService", function($http,$q) {
    function getIndexGdsBrandTypeData(param) {
        var defer = $q.defer();
        $http.post("/qyds-web-pc/gds_type_pc_api/getGdsBrandType.json", {})
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

// // 心愿单数量
// .factory("favoriteCountService",["$q","$http", function($q, $http) {
//     function getHeaderData(param) {
//         var defer = $q.defer();
//         var json = {};
//         json.memberId = param.memberId;
//         $http.post("/qyds-web-pc/mmb_collection/getCount.json", $.param({'data': JSON.stringify(json)}))
//             .then(
//                 function(response) {
//                     defer.resolve(response.data);
//                 },function(){
//                     defer.reject();
//                 });
//         return defer.promise;
//     };
//     return getHeaderData;
// }])
//
// // 购物袋数量
// .factory("mmbShopCountService",["$q","$http", function($q, $http) {
//     function getHeaderData(param) {
//         var defer = $q.defer();
//         var json = {};
//         json.memberId = param.memberId;
//         $http.post("/qyds-web-pc/mmb_shopping_bag/getCount.json", $.param({'data': JSON.stringify(json)}))
//             .then(
//                 function(response) {
//                     defer.resolve(response.data);
//                 },function(){
//                     defer.reject();
//                 });
//         return defer.promise;
//     };
//     return getHeaderData;
// }])

// 优惠券数量
.factory("couponCountService",["$q","$http", function($q, $http) {
    function getHeaderData(param) {
        var defer = $q.defer();
        var json = {};
        json.memberId = param.memberId;
        $http.post("/qyds-web-pc/mmb_coupon_pc_api/getCount.json", $.param({'data': JSON.stringify(json)}))
            .then(
                function(response) {
                    defer.resolve(response.data);
                },function(){
                    defer.reject();
                });
        return defer.promise;
    };
    return getHeaderData;
}])

// 轮播图
.factory("indexFigureService",["$q","$http", function($q, $http) {
    function getIndexFigureData(param) {
        var defer = $q.defer();
        var json = {};
        json.itemCode = 'index_figure';
        json.isChild = '0';
        $http.post("/qyds-web-pc/cms_items_api/getMasterByItemList.json", $.param({'data': JSON.stringify(json)}))
            .then(
                function(response) {
                    defer.resolve(response.data);
                },function(){
                    defer.reject();
                });
        return defer.promise;
    };
    return getIndexFigureData;
}])

// 新闻部分模版1
.factory("indexNewsService", ["$q","$http",function($q, $http) {
    function getIndexNewsData(param) {
        var defer = $q.defer();
        var json = {};
        json.itemCode = 'index_new';
        json.isChild = '0';
        $http.post("/qyds-web-pc/cms_items_api/getMasterByItemList.json", $.param({'data':JSON.stringify(json)}))
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
}])


.factory("indexNews1Service", ["$q","$http",function($q, $http) {
    function getIndexNews1Data(param) {
        var defer = $q.defer();
        var json = {};
        json.itemCode = 'index_new_1';
        json.isChild = '0';
        $http.post("/qyds-web-pc/cms_items_api/getMasterByItemList.json", $.param({'data':JSON.stringify(json)}))
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
        $http.post("/qyds-web-pc/cms_items_api/getMasterByItemList.json", $.param({'data':JSON.stringify(json)}))
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
        $http.post("/qyds-web-pc/cms_items_api/getMasterByItemList.json", $.param({'data':JSON.stringify(json)}))
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

    .factory("indexNews4Service", ["$q","$http",function($q, $http) {
        function getIndexNews4Data(param) {
            var defer = $q.defer();
            var json = {};
            json.itemCode = 'index_new_4';
            json.isChild = '0';
            $http.post("/qyds-web-pc/cms_items_api/getMasterByItemList.json", $.param({'data':JSON.stringify(json)}))
                .then(
                    function(response) {
                        defer.resolve(response.data);
                        // console.log(response.data.results);
                    },function(){
                        defer.reject();
                    });
            return defer.promise;
        };
        return getIndexNews4Data;
    }])

    .factory("indexNews5Service", ["$q","$http",function($q, $http) {
        function getIndexNews5Data(param) {
            var defer = $q.defer();
            var json = {};
            json.itemCode = 'index_new_5';
            json.isChild = '0';
            $http.post("/qyds-web-pc/cms_items_api/getMasterByItemList.json", $.param({'data':JSON.stringify(json)}))
                .then(
                    function(response) {
                        defer.resolve(response.data);
                        // console.log(response.data.results);
                    },function(){
                        defer.reject();
                    });
            return defer.promise;
        };
        return getIndexNews5Data;
    }])

    .factory("indexNews6Service", ["$q","$http",function($q, $http) {
        function getIndexNews6Data(param) {
            var defer = $q.defer();
            var json = {};
            json.itemCode = 'index_new_6';
            json.isChild = '0';
            $http.post("/qyds-web-pc/cms_items_api/getMasterByItemList.json", $.param({'data':JSON.stringify(json)}))
                .then(
                    function(response) {
                        defer.resolve(response.data);
                        // console.log(response.data.results);
                    },function(){
                        defer.reject();
                    });
            return defer.promise;
        };
        return getIndexNews6Data;
    }])

// 新闻部分模版2
.factory("indexNewsTwoService", ["$q","$http",function($q, $http) {
    function getIndexNewsTwoData(param) {
        var defer = $q.defer();
        var json = {};
        json.itemCode = 'index_new_two';
        json.isChild = '0';
        $http.post("/qyds-web-pc/cms_items_api/getListByItem.json", $.param({'data':JSON.stringify(json)}))
            .then(
                function(response) {
                    defer.resolve(response.data);
                    // console.log(response.data.results);
                },function(){
                    defer.reject();
                });
        return defer.promise;
    };
    return getIndexNewsTwoData;
}])

// 新品推荐
.factory("indexNewGoodsService", ["$q","$http",function($q, $http) {
    function getIndexNewGoodsData(param) {
        var defer = $q.defer();
        var json = {};
        json.itemCode = 'index_goods_main';
        json.isChild = '0';
        $http.post("/qyds-web-pc/cms_items_api/getMasterByItemList.json", $.param({'data': JSON.stringify(json)}))
            .then(
                function(response) {
                    defer.resolve(response.data);
                },function(){
                    defer.reject();
                });
        return defer.promise;
    };
    return getIndexNewGoodsData;
}])

// 区域2
    .factory("indexRegion2Service", ["$q","$http",function($q, $http) {
        function getIndexRegion2Data(param) {
            var defer = $q.defer();
            var json = {};
            json.itemCode = 'index_region_2';
            json.isChild = '0';
            $http.post("/qyds-web-pc/cms_items_api/getListByItem.json", $.param({'data':JSON.stringify(json)}))
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

    // 区域2-1
    .factory("indexRegion2_1Service", ["$q","$http",function($q, $http) {
        function getIndexRegion2_1Data(param) {
            var defer = $q.defer();
            var json = {};
            json.itemCode = 'index_region_2_1';
            json.isChild = '0';
            $http.post("/qyds-web-pc/cms_items_api/getListByItem.json", $.param({'data':JSON.stringify(json)}))
                .then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
            return defer.promise;
        };
        return getIndexRegion2_1Data;
    }])

    // 区域3
    .factory("indexRegion3Service", ["$q","$http",function($q, $http) {
        function getIndexRegion3Data(param) {
            var defer = $q.defer();
            var json = {};
            json.itemCode = 'index_region_3';
            json.isChild = '0';
            $http.post("/qyds-web-pc/cms_items_api/getListByItem.json", $.param({'data':JSON.stringify(json)}))
                .then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
            return defer.promise;
        };
        return getIndexRegion3Data;
    }])
    // 区域3手机
    .factory("indexRegion3PhoneService", ["$q","$http",function($q, $http) {
        function getIndexRegion3PhoneData(param) {
            var defer = $q.defer();
            var json = {};
            json.itemCode = 'index_region_3_phone';
            json.isChild = '0';
            $http.post("/qyds-web-pc/cms_items_api/getListByItem.json", $.param({'data':JSON.stringify(json)}))
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
            $http.post("/qyds-web-pc/cms_items_api/getListByItem.json", $.param({'data':JSON.stringify(json)}))
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
            $http.post("/qyds-web-pc/cms_items_api/getListByItem.json", $.param({'data':JSON.stringify(json)}))
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
            $http.post("/qyds-web-pc/cms_items_api/getListByItem.json", $.param({'data':JSON.stringify(json)}))
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
            $http.post("/qyds-web-pc/cms_items_api/getListByItem.json", $.param({'data':JSON.stringify(json)}))
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
            $http.post("/qyds-web-pc/cms_items_api/getListByItem.json", $.param({'data':JSON.stringify(json)}))
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

    // 秒杀海报
    .factory("secKillImageService", ["$q","$http",function($q, $http) {
        function secKillImageService(param) {
            var defer = $q.defer();
            var json = {};
            json.itemCode = 'sec_kill';
            json.isChild = '0';
            $http.post("/qyds-web-pc/cms_items_api/getListByItem.json", $.param({'data':JSON.stringify(json)}))
                .then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
            return defer.promise;
        };
        return secKillImageService;
    }])

    // 海报
    .factory("activityPointsService", ["$q","$http",function($q, $http) {
        function activityPointsService(param) {
            var defer = $q.defer();
            var json = {};
            json.itemCode = 'activity_points';
            json.isChild = '0';
            $http.post("/qyds-web-pc/cms_items_api/getListByItem.json", $.param({'data':JSON.stringify(json)}))
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

    // 海报
    .factory("couponPointsService", ["$q","$http",function($q, $http) {
        function couponPointsService(param) {
            var defer = $q.defer();
            var json = {};
            json.itemCode = 'points_couppon';
            json.isChild = '0';
            $http.post("/qyds-web-pc/cms_items_api/getListByItem.json", $.param({'data':JSON.stringify(json)}))
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
            $http.post("/qyds-web-pc/auth/getCurrentPoints.json", $.param({'data':JSON.stringify(json)}))
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

.factory("couponAllService", function($http,$q) {

   function couponAllService(param) {
       function getAllCoupons() {
           var defer = $q.defer();
           $http.post("/qyds-web-pc/coupon_master/getAllCoupons.json", $.param({'data': JSON.stringify(param)}))
               .then(
                   function (response) {
                       defer.resolve(response.data);
                   }, function () {
                       defer.reject();
                   });
           return defer.promise;
       };
       function getCoupon() {
           var defer = $q.defer();
           $http.post("/qyds-web-pc/coupon_master/addCouponsForUser.json", $.param({'data': JSON.stringify(param)}))
               .then(
                   function (response) {
                       defer.resolve(response.data);
                   }, function () {
                       defer.reject();
                   });
           return defer.promise;
       };
       return{
           getAllCoupons:getAllCoupons,
           getCoupon:getCoupon
       }
   }
    return couponAllService;
})

    // footer信息取得
.factory("footerService",["$q","$http", function($q, $http) {
    function getFooterData(param) {
        var defer = $q.defer();
        var json = {};
        json.itemCode = 'index_footer';
        json.isChild = '1';
        $http.post("/qyds-web-pc/cms_items_api/getMasterByItemList.json", $.param({'data': JSON.stringify(json)}))
            .then(
                function(response) {
                    defer.resolve(response.data);
                },function(){
                    defer.reject();
                });
        return defer.promise;
    };
    return getFooterData;
}])



    // 用户个人
    .factory("personalService", function($http,$q) {

        function personalService(param){
            function getPersonalInfo() {
                var defer = $q.defer();
                $http.post("/qyds-web-pc/mmb_master/getDetail.json", $.param({'data':JSON.stringify(param)}))
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
                $http.post("/qyds-web-pc/mmb_master/editPersonal.json", $.param({'data':JSON.stringify(param)}))
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
                $http.post("/qyds-web-pc/mmb_master/getAddressList.json", $.param({'data':JSON.stringify(param)})).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function () {
                        defer.reject();
                    });
                return defer.promise;
            };

            return {getPersonalInfo:getPersonalInfo,
                editPersonal:editPersonal,
                getERPAddressInfo:getERPAddressInfo}
        }

        return personalService;
    })

    .factory("shoppingBagService", ["$q","$http",function($q,$http) {

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
                $http.post("/qyds-web-pc/mmb_shopping_bag/getList.json", $.param({'data': JSON.stringify(param)})).then(
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
                $http.post("/qyds-web-pc/mmb_shopping_bag/add.json", $.param({'data': JSON.stringify(param)})).then(
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
                $http.post("/qyds-web-pc/mmb_shopping_bag/delete.json", $.param({'data': JSON.stringify(param)})).then(
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
                $http.post("/qyds-web-pc/mmb_shopping_bag/changeQuantity.json", $.param({'data': JSON.stringify(param)}),{noLoading:true}).then(
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
                $http.post("/qyds-web-pc/mmb_shopping_bag/changeActivity.json", $.param({'data': JSON.stringify(param)}),{noLoading:true}).then(
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
    }])

    // 心愿单
    .factory("secKillService",  ["$q","$http",function($q,$http) {

        function secKillService(param) {

            /**
             * 查询秒杀的时间段
             *
             * @param param memberId:会员ID
             * @return  {*}
             */
            function getTimes() {
                var defer = $q.defer();
                $http.post("/qyds-web-pc/gds_master_pc_api/getSecKillActivityTimes.json", $.param({'data':JSON.stringify(param)}),{noLoading:true}).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function () {
                        defer.reject();
                    });
                return defer.promise;
            };

            /**
             * 获取某一时间段内的秒杀活动商品
             *
             * @param param memberId:会员ID
             *             activityTime:时间段
             *
             * @returns {*}
             */
            function getProducts() {
                var defer = $q.defer();
                $http.post("/qyds-web-pc/gds_master_pc_api/getSecKillProductList.json", $.param({'data':JSON.stringify(param)})).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function () {
                        defer.reject();
                    });
                return defer.promise;
            };

            return {
                getTimes: getTimes,
                getProducts: getProducts
            };

        }

        return secKillService;
    }])

    // 心愿单
    .factory("favoriteService",  ["$q","$http",function($q,$http) {

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
                $http.post("/qyds-web-pc/mmb_collection/getList.json", $.param({'data':JSON.stringify(param)}),{noLoading:true}).then(
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
                $http.post("/qyds-web-pc/mmb_collection/add.json", $.param({'data':JSON.stringify(param)})).then(
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
                $http.post("/qyds-web-pc/mmb_collection/delete.json", $.param({'data':JSON.stringify(param)})).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function () {
                        defer.reject();
                    });
                return defer.promise;
            };

            /**
             * 收藏库存报警
             *
             * @param param memberId:会员ID
             *
             * @returns {*}
             */
            function getInventoryAlarming() {
                var defer = $q.defer();
                $http.post("/qyds-web-pc/mmb_collection/getInventoryAlarming.json", $.param({'data':JSON.stringify(param)})).then(
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
                deleteFavorite:deleteFavorite,
                getInventoryAlarming:getInventoryAlarming
            };

        }

        return favoriteService;
    }])

    .factory("favoritePhoneService",  ["$q","$http",function($q,$http) {

        function favoritePhoneService(param) {

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
                $http.post("/qyds-web-pc/mmb_collection/getPhoneList.json", $.param({'data':JSON.stringify(param)}),{noLoading:true}).then(
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
                $http.post("/qyds-web-pc/mmb_collection/add.json", $.param({'data':JSON.stringify(param)})).then(
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
                $http.post("/qyds-web-pc/mmb_collection/delete.json", $.param({'data':JSON.stringify(param)})).then(
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

        return favoritePhoneService;
    }])
    // 验证输入的电话号码是否存在
    .factory("checkInputTelService",  ["$q","$http",function($q,$http) {

        function checkInputTelService(param) {

            // 验证输入的电话号码是否存在
            function checkInputTel() {
                var defer = $q.defer();
                var json = {};
                json.tel = param.tel;
                $http.post("/qyds-web-pc/order_pc/checkInputTel.json", $.param({'data': JSON.stringify(json)}))
                    .then(
                        function(response) {
                            defer.resolve(response.data);
                        },function(){
                            defer.reject();
                        });
                return defer.promise;
            };
            return {checkInputTel:checkInputTel};
        }
        return checkInputTelService;
    }])

    // 收货地址
    .factory("addressService",  ["$q","$http",function($q,$http) {

        function addressService(param) {

            // 会员送货地址一览
            function getList() {
                var defer = $q.defer();
                $http.post("/qyds-web-pc/mmb_address/getList.json", $.param({'data':JSON.stringify(param)})).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function () {
                        defer.reject();
                    });
                return defer.promise;
            };

            function getDefaultAddress() {
                var defer = $q.defer();
                $http.post("/qyds-web-pc/mmb_address/getDefaultAddress.json", $.param({'data':JSON.stringify(param)})).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function () {
                        defer.reject();
                    });
                return defer.promise;
            };

            function add() {
                var defer = $q.defer();
                $http.post("/qyds-web-pc/mmb_address/add.json", $.param({'data':JSON.stringify(param)})).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function () {
                        defer.reject();
                    });
                return defer.promise;
            };

            function edit() {
                var defer = $q.defer();
                $http.post("/qyds-web-pc/mmb_address/edit.json", $.param({'data':JSON.stringify(param)})).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function () {
                        defer.reject();
                    });
                return defer.promise;
            };

            function deleteItem() {
                var defer = $q.defer();
                $http.post("/qyds-web-pc/mmb_address/delete.json", $.param({'data':JSON.stringify(param)})).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function () {
                        defer.reject();
                    });
                return defer.promise;
            };

            function getDetail() {
                var defer = $q.defer();
                $http.post("/qyds-web-pc/mmb_address/getDetail.json", $.param({'data':JSON.stringify(param)})).then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function () {
                        defer.reject();
                    });
                return defer.promise;
            };

            function changeDefault() {
                var defer = $q.defer();
                $http.post("/qyds-web-pc/mmb_address/changeDefault.json", $.param({'data':JSON.stringify(param)})).then(
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
    }])

    // 订单确认
    .factory("confirmOrderService", ["$q","$http",function($q,$http) {
        function confirmOrderService(param) {
            function getDataByBag() {
                var defer = $q.defer();
                $http.post("/qyds-web-pc/order_pc/confirmOrderFromBag.json", $.param({'data': JSON.stringify(param)}))
                    .then(
                        function (response) {
                            defer.resolve(response.data);
                        }, function () {
                            defer.reject();
                        });
                return defer.promise;
            };
            function checkDataByBag() {
                var defer = $q.defer();
                $http.post("/qyds-web-pc/order_pc/checkConfirmOrderFromBag.json", $.param({'data': JSON.stringify(param)}))
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
                $http.post("/qyds-web-pc/order_pc/confirmOrderFromGoods.json", $.param({'data': JSON.stringify(param)}))
                    .then(
                        function (response) {
                            defer.resolve(response.data);
                        }, function () {
                            defer.reject();
                        });
                return defer.promise;
            };
            function checkDataBySingleGoods() {
                var defer = $q.defer();
                $http.post("/qyds-web-pc/order_pc/checkConfirmOrderFromGoods.json", $.param({'data': JSON.stringify(param)}))
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
                $http.post("/qyds-web-pc/order_pc/confirmOrderFromSuitGoods.json", $.param({'data': JSON.stringify(param)}))
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
                $http.post("/qyds-web-pc/order_pc/submitOrder.json", $.param({'data': JSON.stringify(param)}),{showLoading:true})
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
                $http.post("/qyds-web-pc/coupon_master/getOrderCoupons.json", $.param({'data': JSON.stringify(param)}))
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
                $http.post("/qyds-web-pc/gds_master_pc_api/getGiftDetailByCode.json", $.param({'data': JSON.stringify(param)}))
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
                checkDataByBag: checkDataByBag,
                getDataBySingleGoods: getDataBySingleGoods,
                checkDataBySingleGoods: checkDataBySingleGoods,
                getDataBySuitGoods: getDataBySuitGoods,
                submitOrder: submitOrder,
                getOrderCoupons: getOrderCoupons,
                getGiftDetailByCode: getGiftDetailByCode
            };

        }
        return confirmOrderService;
    }])

// 我的订单
.factory("orderService",  ["$q","$http",function($q,$http) {

        function orderService(param) {
            function getList(){
                var defer = $q.defer();
                $http.post("/qyds-web-pc/order_pc/getOrderListByMemberId.json", $.param({'data': JSON.stringify(param)})).then(
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
                $http.post("/qyds-web-pc/order_pc/getOrderDetail.json", $.param({'data': JSON.stringify(param)})).then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
                return defer.promise;
            };
            // 取消订单
            function cancelOrder() {
                var defer = $q.defer();
                $http.post("/qyds-web-pc/order_pc/cancelOrder.json", $.param({'data': JSON.stringify(param)}),{showLoading:true}).then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
                return defer.promise;
            };
            // 确认收货（全单）
            function confirmReceiptInMaster() {
                var defer = $q.defer();
                $http.post("/qyds-web-pc/order_pc/confirmReceiptInMaster.json", $.param({'data': JSON.stringify(param)}),{showLoading:true}).then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
                return defer.promise;
            };
            // 确认收货（自提全单orderId或快递编号expressNo）
            function confirmReceived() {
                var defer = $q.defer();
                $http.post("/qyds-web-pc/order_pc/confirmReceived.json", $.param({'data': JSON.stringify(param)}),{showLoading:true}).then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
                return defer.promise;
            };
            // 删除订单（全单）
            function deleteOrder() {
                var defer = $q.defer();
                $http.post("/qyds-web-pc/order_pc/deleteOrder.json", $.param({'data': JSON.stringify(param)}),{showLoading:true}).then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
                return defer.promise;
            };
            // 全单退货（不可拆单退）
            function applyReturnGoods() {
                var defer = $q.defer();
                $http.post("/qyds-web-pc/order_pc/applyReturnGoods.json", $.param({'data': JSON.stringify(param)}),{showLoading:true}).then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
                return defer.promise;
            };
            // 子单退货（允许拆单退）
            function applyReturnSubGoods() {
                var defer = $q.defer();
                $http.post("/qyds-web-pc/order_pc/applyReturnSubGoods.json", $.param({'data': JSON.stringify(param)}),{showLoading:true}).then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
                return defer.promise;
            };
            // 获取门店信息
            function getOrgList() {
                var defer = $q.defer();
                $http.post("/qyds-web-pc/order_pc/getOrgList.json", $.param({'data': JSON.stringify(param)})).then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
                return defer.promise;
            };
            // 申请退款
            function applyRefund() {
                var defer = $q.defer();
                $http.post("/qyds-web-pc/order_pc/applyRefund.json", $.param({'data': JSON.stringify(param)}),{showLoading:true}).then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
                return defer.promise;
            };
            return {
                getList: getList,
                showOrderDetail: showOrderDetail,                       // 获取订单详情
                cancelOrder: cancelOrder,                               // 取消订单
                confirmReceiptInMaster: confirmReceiptInMaster,         // 确认收货（全单）
                applyReturnGoods: applyReturnGoods,                     // 全单退货（不可拆单退）
                deleteOrder: deleteOrder,                               // 删除订单（全单）
                applyReturnSubGoods: applyReturnSubGoods,               // 子单退货（允许拆单退）
                confirmReceived:confirmReceived,                        // 确认收货（物流信息）
                getOrgList: getOrgList,                                 // 获取门店信息
                applyRefund:applyRefund
            }
        }
        return orderService;
}])

// 线下订单
    .factory("orderOffLineService",  ["$q","$http",function($q,$http) {

        function orderOffLineService(param) {
            function getList(){
                var defer = $q.defer();
                $http.post("/qyds-web-pc/order_pc/getOrderListOffLineByMemberId.json", $.param({'data': JSON.stringify(param)})).then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
                return defer.promise;
            };

            return {
                getList: getList
            }
        }
        return orderOffLineService;
    }])
// 物流详情
.factory("logisticsService", ["$q","$http",function($q,$http) {

    function getLogistics(param) {
        var defer = $q.defer();
        $http.post("/qyds-web-pc/order_pc/queryLogisticsInfo.json", $.param({'data': JSON.stringify(param)}))
            .then(
                function (response) {
                    defer.resolve(response.data);
                }, function () {
                    defer.reject();
                });
        return defer.promise;
    };
    return getLogistics;

}])

// 获取商品分类
    .factory("typeService",["$q","$http", function($q, $http) {
        function getTypeData(param) {
            var defer = $q.defer();
            var json = {};
            json.goodsTypeId = param.firstGoodsTypeId;
            $http.post("/qyds-web-pc/gds_type_pc_api/getGdsTypeFloor.json", $.param({'data': JSON.stringify(json)}))
                .then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
            return defer.promise;
        };
        return getTypeData;
    }])

    //获取所有的type分类
    .factory("getAlltypesService",["$q","$http", function($q, $http) {
        function getTypeData(param) {
            var defer = $q.defer();
            $http.post("/qyds-web-pc/gds_type_pc_api/getAllTypes.json", $.param({}))
                .then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
            return defer.promise;
        };
        return getTypeData;
    }])


// 获取品牌系列
.factory("brandtypeService",["$q","$http", function($q, $http) {
    function getTypeData(param) {
        var defer = $q.defer();
        var json;
        $http.post("/qyds-web-pc/gds_type_pc_api/getGdsBrandTypeFloor.json")
            .then(
                function(response) {
                    console.log(response.data);
                    defer.resolve(response.data);
                },function(){
                    defer.reject();
                });
        return defer.promise;
    };
    return getTypeData;
}])

// 获取商品分类
.factory("typePhoneService",["$q","$http", function($q, $http) {
    function getTypeData(param) {
        var defer = $q.defer();
        var json = {};
        $http.post("/qyds-web-pc/gds_type_pc_api/getGdsTypeFloorByPhone.json")
            .then(
                function(response) {
                    console.log(response.data);
                    defer.resolve(response.data);
                },function(){
                    defer.reject();
                });
        return defer.promise;
    };
    return getTypeData;
}])

// 获取优惠券
.factory("couponService",["$q","$http", function($q, $http) {
    function getTypeData(param) {
        var defer = $q.defer();
        var json = {};
        if(param.data.currentPage){
            json.currentPage = parseInt(param.data.currentPage);
        }
        if(param.data.currentPagePhone){
            json.currentPagePhone = parseInt(param.data.currentPagePhone);
        }
        json.pageSize = parseInt(param.data.pageSize);
        json.memberId = param.data.memberId;
        json.status = param.data.status;
        $http.post("/qyds-web-pc/mmb_coupon_pc_api/getList.json", $.param({'data': JSON.stringify(json)}))
            .then(
                function(response) {
                    defer.resolve(response.data);
                },function(){
                    defer.reject();
                });
        return defer.promise;
    };
    return getTypeData;
}])
// 头信息取得
.factory("footerExplainService",["$q","$http", function($q, $http) {
    function getHeaderData(param) {
        var defer = $q.defer();
        $http.post("/qyds-web-pc/cms_items_api/getContentHtmlByCmsId.json", $.param({'data': JSON.stringify(param)}))
            .then(
                function(response) {
                    defer.resolve(response.data);
                },function(){
                    defer.reject();
                });
        return defer.promise;
    };
    return getHeaderData;
}])
//商品预约
.factory("goodsOrderService",["$q","$http", function($q, $http) {
    function goodsOrder(param) {
        var defer = $q.defer();
        $http.post("/qyds-web-pc/gds_master_pc_api/goodsOrder.json", $.param({'data': JSON.stringify(param)}))
            .then(
                function(response) {
                    defer.resolve(response.data);
                },function(){
                    defer.reject();
                });
        return defer.promise;
    };
    return goodsOrder;
}])

// 杂志信息取得
.factory("magazineListService",["$q","$http", function($q, $http) {
    function getHeaderData(param) {
        var defer = $q.defer();
        $http.post("/qyds-web-pc/cms_items_api/getMasterByItemList.json", $.param({'data': JSON.stringify(param)}))
            .then(
                function(response) {
                    defer.resolve(response.data);
                },function(){
                    defer.reject();
                });
        return defer.promise;
    };
    return getHeaderData;
}])

// 杂志背景故事
.factory("theStoryService",["$q","$http", function($q, $http) {
    function getHeaderData(param) {
        var defer = $q.defer();
        var json = {};
        json.cmsId = param.cmsId;
        $http.post("/qyds-web-pc/cms_items_api/getTheStory.json", $.param({'data': JSON.stringify(json)}))
            .then(
                function(response) {
                    defer.resolve(response.data);
                },function(){
                    defer.reject();
                });
        return defer.promise;
    };
    return getHeaderData;
}])
.factory("pointExchangeService", ["$q","$http", function($q, $http) {
    function pointExchangeService(param) {
        function getPointExchangeCoupons() {
            var defer = $q.defer();
            $http.post("/qyds-web-pc/coupon_master/getPointExchangeCoupons.json", $.param({'data': JSON.stringify(param)}))
                .then(
                    function (response) {
                        defer.resolve(response.data);
                    }, function () {
                        defer.reject();
                    });
            return defer.promise;
        };
        function getCoupon() {
            var defer = $q.defer();
            $http.post("/qyds-web-pc/coupon_master/addCouponsForUser.json", $.param({'data': JSON.stringify(param)}))
                .then(
                    function(response) {
                        defer.resolve(response.data);
                    },function(){
                        defer.reject();
                    });
            return defer.promise;
        };
        return {
            getPointExchangeCoupons: getPointExchangeCoupons,
            getCoupon:getCoupon
        };

    }
    return pointExchangeService;
}])
    .factory("WeiXinService", ["$q","$http", function($q, $http) {
        function WeiXinService(param) {
            function getWxPayInfo() {
                var defer = $q.defer();
                $http.post("/qyds-web-pc/wechat/getWxPayInfo.json", $.param({'data': JSON.stringify(param)}))
                    .then(
                        function (response) {
                            defer.resolve(response.data);
                        }, function () {
                            defer.reject();
                        });
                return defer.promise;
            };
            return {
                getWxPayInfo: getWxPayInfo
            };

        }
        return WeiXinService;
    }])
    .factory("prizeDrawService", ["$q","$http", function($q, $http) {
        function prizeDrawService(param) {
            function getPrizeDrawById() {
                var defer = $q.defer();
                $http.post("/qyds-web-pc/prize_draw/getPrizeDrawInfo.json", $.param({'data': JSON.stringify(param)}))
                    .then(
                        function (response) {
                            defer.resolve(response.data);
                        }, function () {
                            defer.reject();
                        });
                return defer.promise;
            };
            function exchangePrizeWithPoint() {
                var defer = $q.defer();
                $http.post("/qyds-web-pc/prize_draw/exchangePrizeWithPoint.json", $.param({'data': JSON.stringify(param)}))
                    .then(
                        function (response) {
                            defer.resolve(response.data);
                        }, function () {
                            defer.reject();
                        });
                return defer.promise;
            };

            function getImageService() {
                var defer = $q.defer();
                var json = {};
                json.itemCode = 'prize_draw';
                json.isChild = '0';
                $http.post("/qyds-web-pc/cms_items_api/getListByItem.json", $.param({'data': JSON.stringify(json)}))
                    .then(
                        function (response) {
                            defer.resolve(response.data);
                        }, function () {
                            defer.reject();
                        });
                return defer.promise;
            };

            function getWinningList() {
                var defer = $q.defer();
                $http.post("/qyds-web-pc/prize_draw/getWinningList.json", $.param({'data': JSON.stringify(param)}))
                    .then(
                        function (response) {
                            defer.resolve(response.data);
                        }, function () {
                            defer.reject();
                        });
                return defer.promise;
            };

            function getPrizeGoodsList() {
                var defer = $q.defer();
                $http.post("/qyds-web-pc/prize_draw/getPrizeGoodsList.json", $.param({'data': JSON.stringify(param)}))
                    .then(
                        function (response) {
                            defer.resolve(response.data);
                        }, function () {
                            defer.reject();
                        });
                return defer.promise;
            };
            function prizeDraw() {
                var defer = $q.defer();
                $http.post("/qyds-web-pc/prize_draw/prizeDraw.json", $.param({'data': JSON.stringify(param)}))
                    .then(
                        function (response) {
                            defer.resolve(response.data);
                        }, function () {
                            defer.reject();
                        });
                return defer.promise;
            };
            return {
                getPrizeDrawById: getPrizeDrawById,
                exchangePrizeWithPoint:exchangePrizeWithPoint,
                getImageService:getImageService,
                getWinningList:getWinningList,
                prizeDraw: prizeDraw,
                getPrizeGoodsList:getPrizeGoodsList
            };

        }
        return prizeDrawService;
    }]);