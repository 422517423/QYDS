var commonMessage = new Object();
commonMessage.networkErrorMsg = "网络异常";
angular.module('dealnua.controllers', [])

// 登录 start
    .controller('loginCtrl', function ($scope, $state, authService,
                                       localStorageService, popupService, $rootScope) {

        $rootScope.setTitle("登录");
        var param = {
            "openId": $rootScope.openid
        };

        new authService(param).login()
            .then(function (res) {
                if (response.data.resultCode == '00'
                    || response.data.resultCode == '95') {
                    localStorageService.set(KEY_USERINFO, res);
                } else {
                    popupService.showToast(res.resultMessage);
                }
            }, function (error) {
                popupService.showToast(commonMessage.networkErrorMsg);
            });

        $scope.onRegister = function () {
            $state.go('register', {})
        }

    })
    // 登录 end
    // 注册 start
    .controller('registerCtrl', function ($scope, $state, $rootScope, $ionicHistory, authService,
                                          localStorageService,
                                          commonService, popupService, ionicDatePicker, $filter) {

        $rootScope.setTitle("注册");

        // 性别options
        $scope.genderList = {
            '--请选择性别--': "0",
            '男': "1",
            '女': "2"
        };

        $scope.registerInfo = {
            tel: '',
            captcha: '',
            password: '',
            rePassword: '',
            agree: false,
            sex: '0',
            birthdate: ''
        };

        // 日期选择器
        $scope.openDatePicker = function (val) {
            var ipObj = {
                callback: function (val) {  //Mandatory

                    $scope.registerInfo.birthdate = $filter('date')(new Date(val), 'yyyy-MM-dd');
                    console.log($scope.registerInfo.birthdate);
                },
                disabledDates: [],
                from: new Date(1950, 0, 1),
                to: new Date(),
                inputDate: $scope.registerInfo.birthdate ? new Date($scope.registerInfo.birthdate) : new Date("1990-01-01"),
                mondayFirst: true,
                closeOnSelect: true,
                templateType: 'popup',
                showTodayButton: false,
            };
            ionicDatePicker.openDatePicker(ipObj);
        };

        $scope.getVerifyCode = function (isValid) {

            $scope.submitted_verifyCode = true;
            $scope.submitted_register = false;

            if (!isValid) {
                //TODO 获取验证码check失败 弹窗
            } else {
                //发送验证码
                var param = {"mobile": $scope.registerInfo.tel, "orgId": "10"};
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
            }
        };
        // 注册
        $scope.register = function (isValid) {
            $scope.submitted_verifyCode = true;
            $scope.submitted_register = true;
            if (!isValid) {
                //TODO 获取验证码check失败 弹窗
            } else {
                //if($scope.registerInfo.password != $scope.registerInfo.rePassword){//check:密码一致性
                //    popupService.showAlert("错误","两次密码输入不一致,请重新输入",function(){
                //    });
                //    return;
                //}
                if (!$scope.registerInfo.agree) {//check:用户协议和隐私条款
                    popupService.showAlert("错误", "请阅读并同意用户协议和隐私条款", function () {
                    });
                    return;
                }
                var param = {
                    "openId": $rootScope.openid,
                    "telephone": $scope.registerInfo.tel,
                    "password": $jq.md5($scope.registerInfo.password),
                    "captcha": $scope.registerInfo.captcha,
                    "memberName": $scope.registerInfo.name,// TODO 获取微信信息:头像,名称,性别
                    "memberPic": "",
                    "sex": $scope.registerInfo.sex,
                    "birthdate": $scope.registerInfo.birthdate
                };

                new authService(param).register()
                    .then(function (res) {
                        if (res.resultCode == "00") {
                            localStorageService.set(KEY_USERINFO, res.data);
                            popupService.showAlert("提示", "注册成功,请妥善保管你的密码", function () {
                                //$ionicHistory.goBack(-2);
                                $state.go('homePageTab.homePage', {});
                            });
                        } else {
                            popupService.showToast(res.resultMessage);
                        }
                    }, function (error) {
                        popupService.showToast(commonMessage.networkErrorMsg);
                    });
            }

        }

        $scope.goPrivacy = function () {
            $state.go('privacy', {});
        }
    })
    // 注册 end
    // 隐私条款 start
    .controller('privacyCtrl', function ($scope, $rootScope) {
        $rootScope.setTitle("隐私条款");
        $scope.back = function () {
            history.go(-1);
        }
    })
    //隐私条款 end
    // 购物袋 start
    .controller('bagCtrl', function ($scope, $state, $rootScope,
                                     $ionicTabsDelegate, localStorageService, shoppingBagService, popupService, confirmOrderService) {

        $rootScope.setTitle("购物袋");
        var userInfo = localStorageService.get(KEY_USERINFO);

        $scope.$on('$ionicView.enter', function () {
            if (!userInfo) {
                $scope.goodsList = [];
                return;
            }
            $scope.totalPriceStr = 0;
            $scope.allCheck = {};
            $scope.allCheck.checked = false;
            var param = {
                memberId: userInfo.memberId,
                lastUpdateTime: null
            };
            $scope.getList(param);
        });
        $scope.getList = function (param) {
            new shoppingBagService(param).getList()
                .then(function (res) {
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
                        if (res.length != 0) {
                            ionic.DomUtil.ready(function () {
                                var doAfterChange = function (id, value) {
                                    var index = parseInt(id.split("quantity_")[1]);
                                    $scope.changeQuantity(index, value);
                                };
                                $jq('.bagNumSpinner').spinner({callback: doAfterChange, min: 1});
                            });
                        }
                        $scope.setTotalPrice();
                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function () {
                    popupService.showToast("网络错误.");
                });
        };

        //调用后台修改购物车件数
        $scope.changeQuantity = function (index) {
            var param = {
                bagNo: $scope.goodsList[index].bagNo,
                memberId: userInfo.memberId,
                quantity: $scope.goodsList[index].quantity
            };
            new shoppingBagService(param).changeQuantity()
                .then(function (res) {
                    if (res.resultCode == "00") {
                        $scope.setTotalPrice();
                    } else {
                        popupService.showToast("修改数量失败,原因:" + res.resultMessage);
                    }
                }, function (error) {
                    popupService.showToast("网络错误.");
                });
        };
        //调用后台修改购物车活动
        $scope.changeActivity = function (index) {
            var param = {
                bagNo: $scope.goodsList[index].bagNo,
                memberId: userInfo.memberId,
                actGoodsId: $scope.goodsList[index].actGoodsId
            };
            new shoppingBagService(param).changeActivity()
                .then(function (res) {
                    if (res.resultCode == "00") {
                        $scope.setTotalPrice();
                    } else {
                        popupService.showToast("修改活动失败,原因:" + res.resultMessage);
                    }
                }, function (error) {
                    popupService.showToast("网络错误.");
                });
        };

        $scope.goShopping = function () {
            $state.go('homePageTab.homePage');
        };

        $scope.setTotalPrice = function () {
            var totalPrice = 0;
            var checkCount = 0;
            angular.forEach($scope.goodsList, function (goods, index) {
                if (goods.isChecked) {
                    if (goods.activity == null) {
                        // 没有活动
                        var singlePrice = 0;
                        angular.forEach(goods.skuList, function (sku, index2) {
                            singlePrice += sku.price;
                        });
                        totalPrice += singlePrice * goods.quantity;
                    } else {
                        //有活动按照活动价
                        totalPrice += goods.activity.newPrice * goods.quantity;
                    }

                    checkCount++;
                }
            });
            $scope.totalPriceStr = totalPrice.toFixed(2);

            if (checkCount > 0 && checkCount == $scope.goodsList.length) {
                $scope.allCheck.checked = true;
            } else {
                $scope.allCheck.checked = false;
            }
        };

        $scope.goProdocutDetail = function (type) {
            $state.go("goodsDetail", {"type": type});
        };

        $scope.setSelectedActivity = function (index) {
            if ($scope.goodsList[index].actGoodsId == "") {
                $scope.goodsList[index].activity = null;
            } else {
                angular.forEach($scope.goodsList[index].activityList, function (activity, index2) {
                    if ($scope.goodsList[index].actGoodsId == activity.activityId) {
                        $scope.goodsList[index].activity = activity;
                    }
                });
            }
            $scope.changeActivity(index);
        };
        //$scope.allCheck={}
        //    .Checked = false;
        $scope.onAllCheckChange = function () {
            if ($scope.allCheck.checked) {
                angular.forEach($scope.goodsList, function (goods, index) {
                    goods.isChecked = true;
                });
            } else {
                angular.forEach($scope.goodsList, function (goods, index) {
                    goods.isChecked = false;
                });
            }
            $scope.setTotalPrice();
        };

        $scope.onCheckChange = function () {
            var isAllchecked = true;
            angular.forEach($scope.goodsList, function (goods, index) {
                if (!goods.isChecked) {
                    isAllchecked = false;
                }
            });
            $scope.allCheck.checked = isAllchecked;
            $scope.setTotalPrice();
        };


        $scope.removeFromBag = function (index) {
            popupService.showConfirm("提示", "确定要将此商品移出购物袋吗?", function () {
                var param = {
                    bagNo: $scope.goodsList[index].bagNo,
                    memberId: userInfo.memberId
                };
                new shoppingBagService(param).remove()
                    .then(function (res) {
                        if (res.resultCode == "00") {
                            $scope.goodsList.splice(index, 1);
                        } else {
                            popupService.showToast("删除活动失败,原因:" + res.resultMessage);
                        }
                    }, function (error) {
                        popupService.showToast("网络错误.");
                    });
            });
        };

        $scope.confirmOrder = function () {
            var submitGoodsList = [];
            angular.forEach($scope.goodsList, function (goods, index) {
                if (goods.isChecked) {
                    submitGoodsList.push(goods);
                }
            });
            if (submitGoodsList.length == 0) {
                popupService.showToast("请选择要结算的商品.");
                return;
            }

            $scope.checkOrderConfirmByShoppingBag(submitGoodsList);
        };

        $scope.checkOrderConfirmByShoppingBag = function (submitGoodsList) {
            var bagNoArray = [];
            angular.forEach(submitGoodsList, function (shopingBag, index) {
                bagNoArray.push(shopingBag.bagNo);
            });
            var param = {
                memberId: userInfo.memberId,
                bagNoArray: bagNoArray
            };
            new confirmOrderService(param).checkDataByBag().then(function (res) {
                if (res.resultCode == "00") {
                    $rootScope.shoppingBagsOfConfirmOrder = undefined;
                    $rootScope.singleGoodsInfoOfConfirmOrder = undefined;
                    $rootScope.suitGoodsInfoOfConfirmOrder = undefined;

                    $state.go("confirmOrder", {shoppingBags: submitGoodsList});
                } else {
                    popupService.showToast(res.resultMessage);
                }
            }, function (error) {
                popupService.showToast(commonMessage.networkErrorMsg);
            });
        };

    })
    // 购物袋 end
    // 首页tab start
    .controller('homePageTabCtrl', function ($scope, $rootScope, $state, $ionicTabsDelegate, localStorageService) {
        $rootScope.setTitle("首页");
    })
    // 首页tab end

    // 订单画面 start
    .controller('orderListCtrl', function ($scope, $rootScope, $state, $stateParams, $ionicTabsDelegate, $filter, $ionicHistory, localStorageService, orderService, popupService, WeiXinService, $ionicScrollDelegate) {
        $rootScope.setTitle("订单列表");
        $scope.tabFlg = $stateParams.tabFlg;
        $scope.pageSize = 5;
        $scope.currentPage = 1;
        $scope.isShowTop = 0;
        $scope.isLoading = false;
        if (!$scope.tabFlg) {
            $scope.tabFlg = 1;
        }
        $scope.hasMore = true;
        $scope.allOrderList = [];

        var userInfo = localStorageService.get(KEY_USERINFO);

        $scope.getSelectedTabStyle = function (tab) {
            if ($scope.tabFlg == tab) {
                return {"color": "#333333"};
            } else {
                return {"color": "#999999"};
            }
        };
        $scope.refreshByTab = function (tab) {
            $scope.tabFlg = tab;
            $scope.doRefresh();
        };
        $scope.doRefresh = function () {
            $scope.hasMore = true;
            $scope.allOrderList = [];
            $scope.currentPage = 1
            $scope.getData();
        };

        $scope.doGetMore = function () {
            $scope.getData();
        };
        $scope.getData = function () {
            if ($scope.isLoading) {
                return;
            }
            $scope.isLoading = true;
            var param = {
                "memberId": userInfo.memberId,
                "needColumns": $scope.pageSize,
                "startPoint": ($scope.currentPage - 1) * $scope.pageSize,
                "type": $scope.tabFlg
            };
            new orderService().getOrderList(param)
                .then(function (res) {
                        $scope.isLoading = false;
                        $scope.currentPage = $scope.currentPage + 1;
                        if (res.resultCode == "00") {
                            $scope.allOrderList = $scope.allOrderList.concat(res.results);
                            if (res.results.length < $scope.pageSize) {
                                $scope.hasMore = false;
                            } else {
                                $scope.hasMore = true;
                            }
                        } else {
                            popupService.showToast(res.resultMessage);
                        }
                    }, function (error) {
                        $scope.hasMore = false;
                        $scope.isLoading = false;
                        popupService.showToast(commonMessage.networkErrorMsg);
                    }
                );
        };

        $scope.doRefresh();

        $scope.goBack = function () {
            $ionicHistory.goBack();
        };

        // 跳转到商品详细
        $scope.goGoodsDetail = function (gdsId) {
            $state.go("goodsDetail", {"type": gdsId});
        };

        // 跳转到订单详情
        $scope.showOrderDetail = function (orderId) {
            var param = new Object();
            param.orderId = orderId;
            $state.go("orderDetail", param);
        };

        $scope.getWXPayInfo = function (out_trade_no, price) {
            var params = {
                "openid": $rootScope.openid,
                "body": "商品订单" + out_trade_no,
                "out_trade_no": out_trade_no,
                "total_fee": (parseFloat(price) * 100).toFixed(0),
                // "total_fee": "1",
                "trade_type": "JSAPI"
            };
            WeiXinService.getWxPayInfo(params).then(function (response) {
                if (response.resultCode == '00') {
                    $scope.chooseWXPay(response.results);
                } else {
                    popupService.showToast("支付失败,原因:" + response.resultMessage);
                }
            }, function (error) {
            });
        };

        $scope.chooseWXPay = function (result) {
            wx.chooseWXPay({
                timestamp: result.timeStamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
                nonceStr: result.nonceStr, // 支付签名随机串，不长于 32 位
                package: result.package, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
                signType: result.signType, // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
                paySign: result.paySign, // 支付签名
                success: function (res) {
                    $scope.clearRootScopeOfConfirmOrder();
                    // 支付成功后的回调函数
                    popupService.showToast("支付成功!");
                    $state.go("orderList", {"tabFlg": "3"});
                },
                cancel: function (res) {
                    $scope.clearRootScopeOfConfirmOrder();
                    // 支付成功后的回调函数
                    popupService.showToast("您取消了支付,可在订单列表中继续支付");
                    $state.go("orderList", {"tabFlg": "2"});
                },
                fail: function (res) {
                    $scope.clearRootScopeOfConfirmOrder();
                    // 支付成功后的回调函数
                    $state.go("orderList", {"tabFlg": "2"});
                }
            });

        };

        $scope.clearRootScopeOfConfirmOrder = function () {
            // 清空此次提交信息
            $rootScope.commentMessage = undefined;
            // 选择地址后画面信息更新
            $rootScope.selectedAddressInfo = undefined;
            $rootScope.selectedShopInfo = undefined;
            // 开票信息
            $rootScope.invoiceInfo = undefined;

            $rootScope.selectedDeliveryTab = undefined;
        };

        // 取消订单
        $scope.cancelOrder = function (orderId) {
            popupService.showConfirm("取消订单", "确定要取消此订单吗？", function () {
                var param = {// TODO 临时数据！！！！！
                    "memberId": userInfo.memberId,
                    "orderId": orderId
                };

                new orderService().cancelOrder(param)
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
            popupService.showConfirm("确认收货", "确定已收到此订单中所有的商品了吗？", function () {
                var param = {// TODO 临时数据！！！！！
                    "memberId": userInfo.memberId,
                    "orderId": orderId
                };

                new orderService().confirmReceiptInMaster(param)
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

        // 删除订单
        $scope.deleteOrder = function (orderId) {
            popupService.showConfirm("删除订单", "确定要删除此订单吗？", function () {
                var param = {// TODO 临时数据！！！！！
                    "memberId": userInfo.memberId,
                    "orderId": orderId
                };

                new orderService().deleteOrder(param)
                    .then(function (res) {
                            if (res.resultCode == "00") {
                                popupService.showToast("订单已删除。");
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

        // 退货（全单）
        $scope.refunds = function (orderItem) {
            var param = new Object();
            param.orderItem = orderItem;
            $state.go("refunds", param);
        };

        $scope.goHome = function () {
            $state.go("homePageTab.homePage");
        };

        $scope.scroll2Top = function () {
            $ionicScrollDelegate.$getByHandle("orderListScroll").scrollTop(true);
        };

        $scope.onScroll = function () {
            // var scrollPosition = $ionicScrollDelegate.$getByHandle("orderListScroll").getScrollPosition();
            // console.log(scrollPosition.top);
            // if(scrollPosition.top>50){
            //     if($scope.isShowTop != 1){
            //         $scope.isShowTop = 1;
            //     }
            // }else{
            //     if($scope.isShowTop != 0){
            //         $scope.isShowTop = 0;
            //     }
            //
            // }
        }

    })
    // 订单画面 end
    // 订单详情画面 start
    .controller('orderDetailCtrl', function ($scope, $rootScope, $state, $ionicTabsDelegate, localStorageService, orderService, logisticsService, popupService, WeiXinService, $stateParams) {
        $rootScope.setTitle("订单详细");
        var userInfo = localStorageService.get(KEY_USERINFO);

        if ($stateParams.orderId != null
            && $stateParams.orderId.length > 0) {
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
                        } else {
                            popupService.showToast(res.resultMessage);
                        }
                    }, function (error) {
                        $scope.hasMore = false;
                        popupService.showToast(commonMessage.networkErrorMsg);
                    }
                );
        };

        $scope.initData();

        // 跳转到商品详细
        $scope.goGoodsDetail = function (gdsId) {
            $state.go("goodsDetail", {"type": gdsId});
        };

        // 删除订单
        $scope.deleteOrder = function () {
            popupService.showConfirm("删除订单", "确定要删除此订单吗？", function () {
                var param = {// TODO 临时数据！！！！！
                    "memberId": userInfo.memberId,
                    "orderId": $scope.orderDetail.orderId
                };

                new orderService().deleteOrder(param)
                    .then(function (res) {
                            if (res.resultCode == "00") {
                                popupService.showToast("订单已删除。");
                                $state.go("orderList", {"tabFlg": "1"});
                                //$scope.initData();
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
        $scope.cancelOrder = function () {
            popupService.showConfirm("取消订单", "确定要取消此订单吗？", function () {
                var param = {// TODO 临时数据！！！！！
                    "memberId": userInfo.memberId,
                    "orderId": $scope.orderDetail.orderId
                };

                new orderService().cancelOrder(param)
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
        $scope.confirmReceiptInMaster = function () {
            popupService.showConfirm("确认收货", "确定已收到此订单中所有的商品了吗？", function () {
                var param = {// TODO 临时数据！！！！！
                    "memberId": userInfo.memberId,
                    "orderId": $scope.orderDetail.orderId
                };

                new orderService().confirmReceiptInMaster(param)
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
            var param = new Object();
            param.orderItem = orderItem;
            $state.go("refunds", param);
        };

        $scope.applyRefund = function () {
            $state.go("applyRefund", {orderId: $scope.orderDetail.orderId});
        };

        // 物流详情
        $scope.logisticsDetail = function () {
            $state.go("logisticsDetail", {"orderId": $state.params.orderId});
        };
        //支付
        $scope.getWXPayInfo = function () {
            var params = {
                "openid": $rootScope.openid,
                "body": "商品订单" + $scope.orderDetail.orderCode,
                "out_trade_no": $scope.orderDetail.orderCode,
                "total_fee": (parseFloat($scope.orderDetail.payInfact) * 100).toFixed(0),
                // "total_fee": "1",
                "trade_type": "JSAPI"
            };
            WeiXinService.getWxPayInfo(params).then(function (response) {
                if (response.resultCode == '00') {
                    $scope.chooseWXPay(response.results);
                } else {
                    popupService.showToast("支付失败,原因:" + response.resultMessage);
                }
            }, function (error) {
            });
        };

        $scope.chooseWXPay = function (result) {
            wx.chooseWXPay({
                timestamp: result.timeStamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
                nonceStr: result.nonceStr, // 支付签名随机串，不长于 32 位
                package: result.package, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
                signType: result.signType, // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
                paySign: result.paySign, // 支付签名
                success: function (res) {
                    $scope.clearRootScopeOfConfirmOrder();
                    // 支付成功后的回调函数
                    popupService.showToast("支付成功!");
                    $state.go("orderList", {"tabFlg": "3"});
                },
                cancel: function (res) {
                    $scope.clearRootScopeOfConfirmOrder();
                    // 支付成功后的回调函数
                    popupService.showToast("您取消了支付,可在订单列表中继续支付");
                    $state.go("orderList", {"tabFlg": "2"});
                },
                fail: function (res) {
                    $scope.clearRootScopeOfConfirmOrder();
                    // 支付成功后的回调函数
                    $state.go("orderList", {"tabFlg": "2"});
                }
            });

        };
    })
    // 订单详情画面 end
    // 线下订单画面 start
    .controller('orderListOfflineCtrl', function ($scope, $rootScope, $state, $stateParams, $ionicTabsDelegate, $filter, $ionicHistory, localStorageService, orderService, popupService, WeiXinService, $ionicScrollDelegate) {
        $rootScope.setTitle("线下订单列表");
        $scope.pageSize = 10;

        var userInfo = localStorageService.get(KEY_USERINFO);
        $scope.doRefresh = function () {
            $scope.hasMore = true;
            $scope.isLoading = false;
            $scope.allOrderList = [];
            $scope.currentPage = 1;
            $scope.getData();
        };

        $scope.doGetMore = function () {
            $scope.getData();
        };
        $scope.getData = function () {
            if ($scope.isLoading) {
                return;
            }
            $scope.isLoading = true;
            var param = {
                "memberId": userInfo.memberId,
                "needColumns": $scope.pageSize,
                "startPoint": ($scope.currentPage - 1) * $scope.pageSize
            };
            new orderService().getOfflineOrders(param)
                .then(function (res) {
                        $scope.isLoading = false;
                        $scope.currentPage = $scope.currentPage + 1;
                        if (res.resultCode == "00") {
                            $scope.allOrderList = $scope.allOrderList.concat(res.results);
                            if (res.results.length < $scope.pageSize) {
                                $scope.hasMore = false;
                            } else {
                                $scope.hasMore = true;
                            }
                        } else {
                            popupService.showToast(res.resultMessage);
                        }
                    }, function (error) {
                        $scope.hasMore = false;
                        $scope.isLoading = false;
                        popupService.showToast(commonMessage.networkErrorMsg);
                    }
                );
        };

        $scope.goBack = function () {
            $ionicHistory.goBack();
        };
        $scope.goHome = function () {
            $state.go("homePageTab.homePage");
        };
        $scope.scroll2Top = function () {
            $ionicScrollDelegate.$getByHandle("orderListScroll").scrollTop(true);
        };
        $scope.doRefresh();
    })
    // 线下订单画面 end
    // 物流详情画面 start
    .controller('logisticsDetailCtrl', function ($scope, $rootScope, $stateParams, $state, $ionicTabsDelegate, orderService, localStorageService, logisticsService, popupService) {
        $rootScope.setTitle("查看物流");

        var orderId = "";
        if ($stateParams.orderId != null
            && $stateParams.orderId.length > 0) {
            $rootScope.orderIdLogistics = $stateParams.orderId;
            orderId = $stateParams.orderId;
        } else {
            orderId = $rootScope.orderIdLogistics;
        }

        $scope.doRefresh = function () {
            $scope.logistics = [];
            $scope.getData();
        };

        $scope.getData = function () {

            var param = {
                orderId: orderId
            };

            new logisticsService(param).then(function (res) {

                if (res.resultCode == '00') {
                    $scope.logistics = res.results;
                    console.log($scope.logistics);
                } else {
                    $scope.logistics = [];
                    popupService.showToast("物流信息取得失败");
                }
            }, function (error) {
                $scope.logistics = [];
            });
        };

        $scope.getData();

        $scope.confirmReceived = function (orderId, expressNo) {

            popupService.showConfirm("确认收货", "确定已收到商品了吗？", function () {

                var userInfo = localStorageService.get(KEY_USERINFO);

                var param = {
                    memberId: userInfo.memberId,
                    orderId: orderId,
                    expressNo: expressNo
                };
                console.log(param);
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

    // 退货画面 start
    .controller('refundsCtrl', function ($scope, $rootScope, $state, $stateParams, $ionicHistory, $ionicTabsDelegate, localStorageService, popupService, orderService) {
        $rootScope.setTitle("退货");
        var userInfo = localStorageService.get(KEY_USERINFO);

        // 从前一页传过来的订单信息
        $scope.refundsInfo = $stateParams.orderItem;
        ionic.DomUtil.ready(function () {
            $jq('.refundsNumSpinner').spinner({});
            // 退货件数
            for (var i = 0; i < $scope.refundsInfo.ordList.length; i++) {
                var goods = $scope.refundsInfo.ordList[i];
                goods.newQuantity = goods.quantity;
            }
            // 退货原因
            $scope.refundsInfo.applyComment = "";
            // 退换货地点 (10.电商，20.门店)，默认电商退货。
            $scope.refundsInfo.rexPoint = "10";

            // 获取门店信息
            new orderService().getOrgList()
                .then(function (res) {
                        if (res.resultCode == '00') {
                            $scope.orgList = res.data;
                        } else {
                            popupService.showAlert("", "获取门店信息过程中出现问题，请联系？？？!");
                        }
                    }, function (error) {
                        $scope.hasMore = false;
                    }
                );

        });

        $scope.changeRexPoint = function (rexP) {
            $scope.refundsInfo.rexPoint = rexP;
        }

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
                        popupService.showAlert("", "此订单中的第" + (i + 1) + "件商品：【" + goods.goodsName + "】所输入的退货数量超过了您购买的该商品数量。请重新输入该商品的退货数量。");
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
                    popupService.showAlert("", "请输入想要退货的数量。");
                    return;
                }
            }

            if ($scope.refundsInfo.applyComment == '') {
                popupService.showToast("请输入您的退货理由。");
                return;
            }

            if ($scope.refundsInfo.rexPoint == '20') {
                if (!$scope.refundsInfo.rexStoreId || !$scope.refundsInfo.rexStoreId.store_code) {
                    popupService.showToast("请选择退货门店。");
                    return;
                }
            }

            popupService.showConfirm("申请退货", "确定要申请退货所选中的商品吗？", function () {

                var param = {
                    "memberId": userInfo.memberId,
                    "orderId": $scope.refundsInfo.orderId,
                    "applyComment": $scope.refundsInfo.applyComment,
                    "returnExchangeExt": "10",
                    "rexPoint": $scope.refundsInfo.rexPoint
                };

                if ($scope.refundsInfo.rexPoint == '20') {
                    param.rexStoreId = $scope.refundsInfo.rexStoreId.store_code;
                    param.rexStoreName = $scope.refundsInfo.rexStoreId.store_name_cn;
                }

                // 该订单允许拆单退货
                if ($scope.refundsInfo.canDivide == '1') {
                    param.detailId = detailId.substr(0, detailId.length - 1);
                    param.returnCount = returnCount.substr(0, returnCount.length - 1);
                    new orderService().applyReturnSubGoods(param)
                        .then(function (res) {
                                if (res.resultCode == "00") {
                                    popupService.showAlert("", "退货申请已提交，请耐心等待客服人员与您联系。");
                                    $scope.goBack();
                                } else {
                                    popupService.showToast(res.resultMessage);
                                }
                            }, function (error) {
                                popupService.showToast(commonMessage.networkErrorMsg);
                            }
                        );
                } else {
                    new orderService().applyReturnGoods(param)
                        .then(function (res) {
                                if (res.resultCode == "00") {
                                    popupService.showAlert("", "退货申请已提交，请耐心等待客服人员与您联系。");
                                    $scope.goBack();
                                } else {
                                    popupService.showToast(res.resultMessage);
                                }
                            }, function (error) {
                                popupService.showToast(commonMessage.networkErrorMsg);
                            }
                        );
                }
            });
        }

        $scope.goBack = function () {
            $ionicHistory.goBack();
        }
    })
    // 退货画面 end
    // 退款画面 start
    .controller('applyRefundCtrl', function ($scope, $rootScope, $state, $stateParams, $ionicHistory, localStorageService, popupService, orderService) {
        $rootScope.setTitle("退货");
        var userInfo = localStorageService.get(KEY_USERINFO);

        // 从前一页传过来的订单信息
        $scope.orderId = $stateParams.orderId;
        $scope.refundsInfo = {
            applyComment: ""
        }
        // 退货
        $scope.applyRefund = function () {
            if ($scope.refundsInfo.applyComment == "") {
                popupService.showToast("请输入退款原因.");
                return;
            }
            var param = {
                "memberId": userInfo.memberId,
                "orderId": $scope.orderId,
                "applyComment": $scope.refundsInfo.applyComment
            };
            new orderService().applyRefund(param)
                .then(function (res) {
                        if (res.resultCode == "00") {
                            popupService.showAlert("提示", "退款申请已提交，请耐心等待客服人员与您联系。", function () {
                                $scope.goBack();
                            });

                        } else {
                            popupService.showToast(res.resultMessage);
                        }
                    }, function (error) {
                        popupService.showToast(commonMessage.networkErrorMsg);
                    }
                );
        };

        $scope.goBack = function () {
            history.go(-1);
        }
    })
    // 退款画面 end

    // 确认订单 start
    .controller('confirmOrderCtrl', function ($scope, $state, $stateParams, $ionicHistory, $rootScope,
                                              localStorageService,
                                              confirmOrderService,
                                              popupService, shoppingBagService, WeiXinService, $ionicPopover) {
        $rootScope.setTitle("确认订单");
        if ($stateParams.shoppingBags) {
            $rootScope.shoppingBagsOfConfirmOrder = $stateParams.shoppingBags;
        }
        if ($stateParams.singleGoodsInfo) {
            $rootScope.singleGoodsInfoOfConfirmOrder = $stateParams.singleGoodsInfo;
        }
        if ($stateParams.suitGoodsInfo) {
            $rootScope.suitGoodsInfoOfConfirmOrder = $stateParams.suitGoodsInfo;
        }

        if ($rootScope.commentMessage) {
            $scope.comment = {
                message: $rootScope.commentMessage
            };
        } else {
            $scope.comment = {
                message: ''
            }
        }

        var userInfo = localStorageService.get(KEY_USERINFO);

        $scope.selectedOrderActivity = {};
        $scope.selectedCoupon = {};
        // 商品总价
        $scope.goodsTotalPrice = 0.00;
        // 订单最终价格
        $scope.orderFinalPrice = 0.00;

        $scope.goodsExchangePointCount = 0;

        $scope.exchangePointCount = 0;

        $scope.activityPointCount = 0;

        $scope.couponList = [];

        $scope.selectedDeliveryTab = "10";

        //进入页面后初始化页面
        $scope.$on('$ionicView.enter', function () {

            $scope.shoppingBags = $rootScope.shoppingBagsOfConfirmOrder;
            $scope.singleGoodsInfo = $rootScope.singleGoodsInfoOfConfirmOrder;
            $scope.suitGoodsInfo = $rootScope.suitGoodsInfoOfConfirmOrder;

            $scope.init();
        });

        //初始化函数
        $scope.init = function () {
            // 选择地址后画面信息更新
            $scope.addressInfo = $rootScope.selectedAddressInfo;

            $scope.shopInfo = $rootScope.selectedShopInfo;

            // 开票信息
            $scope.invoiceInfo = $rootScope.invoiceInfo;

            if ($rootScope.selectedDeliveryTab) {
                $scope.selectedDeliveryTab = $rootScope.selectedDeliveryTab;
            }

            if ($scope.shoppingBags) {
                $scope.getOrderConfirmByShoppingBag();
            } else if ($scope.singleGoodsInfo) {
                $scope.getOrderConfirmBySingle();
            } else if ($scope.suitGoodsInfo) {
                $scope.getOrderConfirmBySuit();
            } else {
                popupService.showToast("系统出错了,请返回首页刷新重试.");
                setTimeout(function () {
                    history.go(-1);
                }, 2000);
            }
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
                if (res.resultCode == "00") {
                    $scope.processConfirmData(res.results);
                } else {
                    popupService.showToast(res.resultMessage);
                    setTimeout(function () {
                        history.go(-1);
                    }, 2000);
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
                if (res.resultCode == "00") {
                    $scope.processConfirmData(res.results);
                } else {
                    popupService.showToast(res.resultMessage);
                    setTimeout(function () {
                        history.go(-1);
                    }, 2000);
                }
            }, function (error) {
                popupService.showToast(commonMessage.networkErrorMsg);
            });
        };

        $scope.getOrderConfirmBySuit = function () {
            var skuArray = [];
            angular.forEach($scope.suitGoodsInfo.skuList, function (sku, index) {
                skuArray.push({
                    "goodsId": sku.goodsId,
                    "goodsSkuId": sku.goodsSkuId
                });
            });
            var param = {
                memberId: userInfo.memberId,
                goodsId: $scope.suitGoodsInfo.goodsId,
                gdIdAndSkuIdJson: JSON.stringify(skuArray),
                quantity: $scope.suitGoodsInfo.quantity
            };
            new confirmOrderService(param).getDataBySuitGoods().then(function (res) {
                if (res.resultCode == "00") {
                    $scope.processConfirmData(res.results);
                } else {
                    popupService.showToast(res.resultMessage);
                    setTimeout(function () {
                        history.go(-1);
                    }, 2000);
                }
            }, function (error) {
                popupService.showToast(commonMessage.networkErrorMsg);
            });
        };

        $scope.processConfirmData = function (result) {
            console.log(result);
            $scope.confirmData = result;
            var selectAddressInfo = $rootScope.selectedAddressInfo;
            if (selectAddressInfo && selectAddressInfo.addressId) {
                $scope.addressInfo = selectAddressInfo;
            } else {
                $scope.addressInfo = $scope.confirmData.mmbAddressExt;
            }

            if ("1" == userInfo.isSaller) {
                $scope.confirmData.actMasterList = [];
            }

            if ($scope.confirmData.actMasterList != null && $scope.confirmData.actMasterList.length > 0) {
                $scope.selectedOrderActivity.id = $scope.confirmData.actMasterList[0].activityId;
            }
            $scope.setGoodsTotalPrice();
            $scope.setOrderFinalPrice();
            $scope.getCouponList();
        };

        $scope.getCouponList = function () {
            $scope.selectedCoupon.id = -1;
            $scope.couponList = [];

            var hasOrderActivity = "1";
            if (!$scope.selectedOrderActivity.id || $scope.selectedOrderActivity.id == -1) {
                hasOrderActivity = "0";
            }
            var param = {
                memberId: userInfo.memberId,
                goodsInfo: $scope.confirmData.goodsInfo,
                orderPrice: $scope.orderFinalPrice,
                hasOrderActivity: hasOrderActivity
            };
            new confirmOrderService(param).getOrderCoupons().then(function (res) {
                if (res.resultCode == "00") {
                    $scope.couponList = res.results;
                    if ("1" == userInfo.isSaller) {
                        $scope.couponList = [];
                    }
                    if ($scope.couponList && $scope.couponList.length > 0) {
                        $scope.selectedCoupon.id = $scope.couponList[0].couponMemberId;
                    }
                    $scope.setOrderFinalPrice();
                } else {
                    popupService.showToast(res.resultMessage);
                }
            }, function (error) {
                popupService.showToast(commonMessage.networkErrorMsg);
            });
        };

        $scope.setGoodsTotalPrice = function () {
            var goodsTotal = 0;
            var exchangePointCount = 0;
            var goodsCount = 0
            angular.forEach($scope.confirmData.goodsInfo, function (goods) {
                if (goods.type != "30") {
                    var goodsPrice = goods.ordConfirmOrderUnitExtList[0].price;
                    var goodsPoint = 0;
                    if (goods.activity) {
                        goodsPrice = goods.activity.newPrice;
                        goodsPoint = goods.activity.point == null ? 0 : goods.activity.point;
                    }
                    goodsTotal += parseFloat(goodsPrice) * goods.quantity;
                    exchangePointCount += goodsPoint * goods.quantity;
                    goodsCount += parseInt(goods.quantity);
                } else {
                    // 套装
                    var goodsPrice = 0;
                    var goodsPoint = 0;
                    angular.forEach(goods.ordConfirmOrderUnitExtList, function (sku, index2) {
                        goodsPrice += parseFloat(sku.price);
                    });
                    if (goods.activity) {
                        goodsPrice = goods.activity.newPrice;
                        goodsPoint = goods.activity.point == null ? 0 : goods.activity.point;
                    }
                    goodsTotal += parseFloat(goodsPrice) * goods.quantity;
                    exchangePointCount += goodsPoint * goods.quantity;
                    goodsCount += parseInt(goods.quantity);
                }
            });
            $scope.goodsCount = goodsCount;
            $scope.goodsTotalPrice = goodsTotal.toFixed(2);
            $scope.goodsExchangePointCount = exchangePointCount;
        };

        $scope.setOrderFinalPrice = function () {
            // 在商品总价的基础上减去订单优惠和优惠券抵值
            var orderDiscount = parseFloat($scope.goodsTotalPrice);
            $scope.discountPrice = 0;
            $scope.activityPointCount = 0;
            // 订单活动
            if ($scope.confirmData.actMasterList != null && $scope.confirmData.actMasterList.length > 0) {
                angular.forEach($scope.confirmData.actMasterList, function (activity) {
                    if ($scope.selectedOrderActivity.id == activity.activityId) {
                        // 选中的活动
                        orderDiscount = orderDiscount - activity.cutPrice;
                        // if(activity.needFee){
                        //     orderDiscount = orderDiscount + activity.needFee;
                        // }
                        $scope.discountPrice = activity.cutPrice;
                        $scope.activityPointCount = activity.needPoint;
                    }
                });
            }
            $scope.orderDiscountPrice = orderDiscount.toFixed(2);
            var orderFinal = orderDiscount;
            // 优惠券
            if ($scope.couponList != null && $scope.couponList.length > 0) {
                angular.forEach($scope.couponList, function (coupon) {
                    coupon.discountPrice = parseFloat(orderDiscount - orderDiscount * coupon.discount / 10).toFixed(2);
                    if ($scope.selectedCoupon.id == coupon.couponMemberId) {
                        // 选中的优惠券
                        if (coupon.couponStyle == "0") {
                            // 抵值
                            orderFinal = orderFinal - coupon.worth;
                        } else if (coupon.couponStyle == "1") {
                            // 打折
                            orderFinal = orderFinal - parseFloat(coupon.discountPrice);
                        }
                    }
                });
            }
            if (orderFinal < $scope.goodsCount) {
                orderFinal = parseFloat($scope.goodsCount);
            }
            $scope.orderFinalPrice = orderFinal.toFixed(2);
            $scope.exchangePointCount = $scope.activityPointCount + $scope.goodsExchangePointCount;
        };

        $scope.setSelectedActivity = function (id) {
            $scope.selectedOrderActivity.id = id;
            $scope.getCouponList();
        };
        $scope.setSelectedCoupon = function (id) {
            $scope.selectedCoupon.id = id;
            $scope.setOrderFinalPrice();
        };

        $scope.selectTab = function (index) {
            $scope.selectedDeliveryTab = index;
            $rootScope.selectedDeliveryTab = index;
        };

        $scope.conInfo = {cname: '', ctel: ''};
        $scope.submitOrder = function (isValid) {

            //表单验证
            $scope.submitted_confirm = true;
            if (!isValid) {
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
                if (goods.activity) {
                    goodsJson.actionId = goods.activity.activityId;
                    goodsJson.actionName = goods.activity.activityName;
                    goodsJson.priceDiscount = goods.activity.newPrice;
                    goodsJson.amount = (goods.activity.originPrice * goods.quantity).toFixed(2);
                    goodsJson.amountDiscount = (goods.activity.newPrice * goods.quantity).toFixed(2);
                } else {
                    goodsJson.actionId = "";
                    goodsJson.actionName = "";
                    goodsJson.priceDiscount = "";
                    goodsJson.amount = 0;

                    angular.forEach(goods.ordConfirmOrderUnitExtList, function (sku) {
                        goodsJson.amount += parseFloat(sku.price);
                    });
                    goodsJson.amount = (goodsJson.amount * goods.quantity).toFixed(2);
                    goodsJson.amountDiscount = "";
                }
                goodsJson.quantity = goods.quantity;

                orderListJson.push(goodsJson);
            });
            // 订单级别的活动
            var actionName = null;
            var actionId = null;
            if ($scope.selectedOrderActivity.id != -1) {
                var selectedActivity = null;
                angular.forEach($scope.confirmData.actMasterList, function (actMaster) {
                    if (actMaster.activityId == $scope.selectedOrderActivity.id) {
                        selectedActivity = actMaster;
                        actionName = actMaster.activityName;
                        actionId = actMaster.activityId;
                    }
                });
                if (selectedActivity != null && selectedActivity.activityType == "42") {
                    // 满送货品的活动必须要选择货品,判断有没有选择货品
                    var hasSelected = false;
                    angular.forEach($scope.confirmData.goodsInfo, function (goods, index) {
                        if (goods.isGift == "1" && goods.giftActivityId == selectedActivity.activityId) {
                            hasSelected = true;
                        }
                    });
                    if (!hasSelected) {
                        popupService.showToast("请选择活动的赠品.");
                        return;
                    }
                }
            }
            //优惠券
            var couponMemberId = null;
            var amountCoupon = null;
            if ($scope.selectedCoupon.id != -1) {
                angular.forEach($scope.couponList, function (coupon) {
                    if (coupon.couponMemberId == $scope.selectedCoupon.id) {
                        couponMemberId = coupon.couponMemberId;
                        if (coupon.couponStyle == "0") {
                            // 抵值
                            amountCoupon = coupon.worth;
                        } else if (coupon.couponStyle == "1") {
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
            if ($scope.selectedDeliveryTab == "10") {
                if (!$scope.addressInfo || $scope.addressInfo.addressId == null || $scope.addressInfo.addressId.length == 0) {
                    popupService.showToast("请选择收货地址");
                    return
                } else {
                    addressId = $scope.addressInfo.addressId;
                }
            } else if ($scope.selectedDeliveryTab == "20") {
                if (!$scope.shopInfo || $scope.shopInfo.store_code == null || $scope.shopInfo.store_code.length == 0) {
                    popupService.showToast("请选择门店");
                    return
                } else {
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
            if ($scope.selectedDeliveryTab == '20') {
                $scope.cname = $scope.conInfo.cname;
                $scope.ctel = $scope.conInfo.ctel;
            } else {
                $scope.cname = '';
                $scope.ctel = '';
            }
            $scope.memberIdForWx = sessionStorage.getItem('memberIdForWx');
            var param = {
                ordListJson: orderListJsonStr,
                memberId: userInfo.memberId,
                shopId: "00000000",
                amountTotle: $scope.goodsTotalPrice,
                actionId: actionId,
                actionName: actionName,
                amountDiscount: $scope.discountPrice,
                couponMemberId: couponMemberId,
                amountCoupon: amountCoupon,
                exchangePointCount: $scope.exchangePointCount,
                pointCount: 0,
                amountPoint: 0,
                //付款方式 10.支付宝，20.微信支付
                payType: "20",
                //订单类型 10.PC电商，11.手机电商，20.微信，30.APP
                orderType: "20",
                payInfact: $scope.orderFinalPrice,
                message: $scope.comment.message,
                wantinvoice: wantinvoice,
                invoiceTitle: invoiceTitle,
                invoiceAddress: ivainvoiceAddress,
                invoiceTel: invoiceTel,
                invoiceTaxno: invoiceTaxno,
                invoiceBankv: invoiceBankv,
                invoiceAccount: invoiceAccount,
                deliverType: $scope.selectedDeliveryTab,
                addressId: addressId,
                erpStoreId: erpStoreId,
                storeName: storeName,
                storePhone: storePhone,
                cname: $scope.cname,
                ctel: $scope.ctel,
                memberIdForWx: $scope.memberIdForWx
            };
            new confirmOrderService(param).submitOrder().then(function (res) {
                if (res.resultCode == "00") {
                    popupService.showAlert("提示", "提交成功!", function () {
                        $scope.getWXPayInfo(res.results);
                    });
                    $scope.removeShoppingbag();
                } else {
                    popupService.showToast(res.resultMessage);
                }
            }, function (error) {
                popupService.showToast(commonMessage.networkErrorMsg);
            });

        };
        $scope.removeShoppingbag = function () {
            if (!$scope.shoppingBags || $scope.shoppingBags.length == 0) {
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

        $scope.getWXPayInfo = function (out_trade_no) {
            var params = {
                "openid": $rootScope.openid,
                "body": "商品订单" + out_trade_no,
                "out_trade_no": out_trade_no,
                "total_fee": (parseFloat($scope.orderFinalPrice) * 100).toFixed(0),
                // "total_fee": "1",
                "trade_type": "JSAPI"
            };
            WeiXinService.getWxPayInfo(params).then(function (response) {
                console.log(response);
                if (response.resultCode == '00') {
                    $scope.chooseWXPay(response.results);
                } else {
                    popupService.showToast("支付失败,原因:" + response.resultMessage);
                    $state.go("orderList", {"tabFlg": "2"});
                }
            }, function (error) {
                popupService.showToast("支付失败,可在订单列表中继续支付");
                $state.go("orderList", {"tabFlg": "2"});
            });
        };

        $scope.chooseWXPay = function (result) {
            wx.chooseWXPay({
                timestamp: result.timeStamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
                nonceStr: result.nonceStr, // 支付签名随机串，不长于 32 位
                package: result.package, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
                signType: result.signType, // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
                paySign: result.paySign, // 支付签名
                success: function (res) {
                    $scope.clearRootScopeOfConfirmOrder();
                    // 支付成功后的回调函数
                    popupService.showToast("支付成功!");
                    $state.go("orderList", {"tabFlg": "3"});
                },
                cancel: function (res) {
                    $scope.clearRootScopeOfConfirmOrder();
                    // 支付成功后的回调函数
                    popupService.showToast("您取消了支付,可在订单列表中继续支付");
                    $state.go("orderList", {"tabFlg": "2"});
                },
                fail: function (res) {
                    $scope.clearRootScopeOfConfirmOrder();
                    // 支付成功后的回调函数
                    $state.go("orderList", {"tabFlg": "2"});
                }
            });

        };

        $scope.clearRootScopeOfConfirmOrder = function () {
            // 清空此次提交信息
            $rootScope.commentMessage = undefined;
            // 选择地址后画面信息更新
            $rootScope.selectedAddressInfo = undefined;
            $rootScope.selectedShopInfo = undefined;
            // 开票信息
            $rootScope.invoiceInfo = undefined;

            $rootScope.selectedDeliveryTab = undefined;
        };

        $scope.selectAddress = function () {
            $rootScope.commentMessage = $scope.comment.message;
            $state.go("addressSelect", {});
        };
        $scope.selectShop = function () {

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

            $rootScope.commentMessage = $scope.comment.message;
            $state.go("storeSelect", {orderList: orderListJson});
        };

        $scope.goCoupon = function () {
            $rootScope.commentMessage = $scope.comment.message;
            $state.go("couponList", {});
        };
        $scope.goInvoice = function () {
            $rootScope.commentMessage = $scope.comment.message;
            $state.go("invoice", {});
        };

        /**
         * 选择赠送的货品
         * @param goods
         * @param activity
         */
        $scope.selectGiftSku = function ($event, goods, activity) {
            var param = {
                "goodsCode": goods.goodsCode
            };
            // 获取商品详细
            new confirmOrderService(param).getGiftDetailByCode().then(function (vresponse) {
                if (vresponse.resultCode == "00") {
                    var res = vresponse.results;
                    console.log(res);
                    $scope.productDetailData = res;
                    // 过滤颜色
                    var validColorList = [];
                    angular.forEach($scope.productDetailData.colorList, function (color) {
                        var isValid = false;
                        angular.forEach(goods.activityColorCodeList, function (colorCode) {
                            if (colorCode == color.key) {
                                isValid = true;
                            }
                        });
                        if (isValid) {
                            validColorList.push(color);
                        }
                    });
                    $scope.productDetailData.colorList = validColorList;

                    $scope.skuList = $scope.productDetailData.skulist;
                    $scope.skuImage = $scope.productDetailData.skulist[0].imgs[0];
                    $scope.skuImages = $scope.productDetailData.imageUrlJsonPc;
                    $scope.skuPrice = $scope.productDetailData.skulist[0].price;
                    $scope.newCount = $scope.productDetailData.skulist[0].newCount;
                    $scope.activityPrice = $scope.productDetailData.skulist[0].activityPrice;
                    $scope.selectedColor = $scope.productDetailData.colorList[0].key;
                    $scope.selectedSize = $scope.productDetailData.sizeList[0].key;

                    $scope.skuImages = $scope.productDetailData.imageUrlJsonPc;

                    $scope.refreshSkuPrice = function () {
                        //如果某一个sku不存在在列表项目的时候
                        var isExist = false;

                        for (var i = 0; i < $scope.skuList.length; i++) {
                            var sku = $scope.skuList[i];
                            if (sku.nameKeyValues[0].key == $scope.selectedColor && sku.nameKeyValues[1].key == $scope.selectedSize) {
                                $scope.selectedSku = sku;
                                $scope.skuPrice = sku.price;
                                //大于20 显示无货 20-50显示可能购买 50显示有货
                                $scope.newCountDisplay = 0;
                                if (parseInt(sku.nameKeyValues[1].bnk_no_limit) > parseInt(sku.newCount)) {
                                    $scope.newCount = 0;
                                } else if (parseInt(sku.nameKeyValues[1].bnk_no_limit) < parseInt(sku.newCount)
                                    && parseInt(sku.nameKeyValues[1].bnk_less_limit) > parseInt(sku.newCount)) {
                                    $scope.newCount = sku.newCount;
                                    $scope.newCountDisplay = 1;
                                } else {
                                    $scope.newCount = sku.newCount;
                                }
                                //$scope.newCount = sku.newCount;
                                $scope.activityPrice = sku.activityPrice;
                                $scope.selectedSkuId = sku.skuid;
                                $scope.skuImage = sku.imgs[0];
                                isExist = true;
                                return false;
                            }
                        }

                        //angular.forEach($scope.skuList, function (sku) {
                        //    if (sku.nameKeyValues[0].key == $scope.selectedColor && sku.nameKeyValues[1].key == $scope.selectedSize) {
                        //        $scope.selectedSku = sku;
                        //        $scope.skuPrice = sku.price;
                        //        //大于20 显示无货 20-50显示可能购买 50显示有货
                        //        $scope.newCountDisplay = 0;
                        //        if(parseInt(sku.nameKeyValues[1].bnk_no_limit) >  parseInt(sku.newCount)){
                        //            $scope.newCount = 0;
                        //        }else if(parseInt(sku.nameKeyValues[1].bnk_no_limit) <  parseInt(sku.newCount)
                        //            && parseInt(sku.nameKeyValues[1].bnk_less_limit) >  parseInt(sku.newCount)){
                        //            $scope.newCount = sku.newCount;
                        //            $scope.newCountDisplay = 1;
                        //        }else{
                        //            $scope.newCount = sku.newCount;
                        //        }
                        //        //$scope.newCount = sku.newCount;
                        //        $scope.activityPrice = sku.activityPrice;
                        //        $scope.selectedSkuId = sku.skuid;
                        //        $scope.skuImage = sku.imgs[0];
                        //        isExist = true;
                        //    }
                        //});

                        if (!isExist) {
                            $scope.newCount = 0;
                        }
                    };

                    $scope.refreshSkuPrice();

                    $ionicPopover.fromTemplateUrl('templates/selectGiftPopover.html', {
                        scope: $scope
                    }).then(function (popover) {
                        $scope.popover = popover;
                        $scope.popover.show($jq("#submitOrder"));
                        var swiper = new Swiper('.swiper-container-phone', {
                            // pagination: '.swiper-pagination',
                            slidesPerView: 'auto',
                            centeredSlides: true,
                            initialSlide: 1,
                            paginationClickable: true,
                            spaceBetween: 10,
                            loop: true
                        });
                    });

                    $scope.$on('$ionicView.beforeLeave', function () {
                        $scope.popover.hide();
                    });

                    $scope.closePopover = function () {
                        $scope.popover.hide();

                    };

                    $scope.getColorClass = function (colorValue) {
                        if ($scope.selectedColor == colorValue) {
                            return "dark-bg light bordered-dark";
                        } else {
                            return "";
                        }
                    };
                    $scope.getSizeClass = function (sizeValue) {
                        if ($scope.selectedSize == sizeValue) {
                            return "dark-bg light bordered-dark";
                        } else {
                            return "";
                        }
                    };

                    $scope.setColor = function (colorValue, colorName) {
                        $scope.selectedColor = colorValue;
                        $scope.selectedColorName = colorName;
                        $scope.refreshSkuPrice();
                    };

                    $scope.setSize = function (sizeValue, sizeName) {
                        $scope.selectedSize = sizeValue;
                        $scope.selectedSizeName = sizeName;
                        $scope.refreshSkuPrice();
                    };
                    $scope.confirmPopover = function () {
                        $scope.popover.hide();
                        var goodsInfo = {};
                        goodsInfo.goodsId = $scope.productDetailData.goodsId;
                        goodsInfo.goodsName = $scope.productDetailData.goodsName;
                        goodsInfo.imageUrlJson = $scope.productDetailData.imageUrlJson;
                        goodsInfo.quantity = 1;
                        goodsInfo.type = $scope.productDetailData.type;
                        goodsInfo.isGift = "1";
                        goodsInfo.giftActivityId = activity.activityId;
                        activity.newPrice = activity.needFee;
                        activity.originPrice = $scope.selectedSku.price;
                        goodsInfo.activity = activity;
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

                        $scope.removeGiftGoods();
                        $scope.confirmData.goodsInfo.push(goodsInfo);
                        $scope.setGoodsTotalPrice();
                        $scope.setOrderFinalPrice();
                    };
                } else {
                    popupService.showToast("获取商品详细失败！");
                }
            }, function (err) {
                popupService.showToast(commonMessage.networkErrorMsg);
            });
        };

        $scope.removeGiftGoods = function () {
            angular.forEach($scope.confirmData.goodsInfo, function (goods, index) {
                if (goods.isGift == "1") {
                    $scope.confirmData.goodsInfo.splice(index, 1);
                }
            });
        };

    })
    // 确认订单 end
    // 发票信息 start
    .controller('invoiceCtrl', function ($scope, $ionicHistory, $rootScope, popupService) {
        $rootScope.setTitle("发票信息");
        $scope.box = {
            person: false,
            company: false
        };

        $scope.invoice = {
            type: '10',
            companyName: null,
            taxpayerCode: null,
            companyAddress: null,
            companyTel: null,
            bankName: null,
            bankAccount: null,
            invoiceTitle: null
        };

        var info = $rootScope.invoiceInfo;
        if (info) {

            $scope.invoice = info;
        }

        $scope.selectTab = function (index) {
            $scope.invoice.type = index;
        };

        $scope.submitInvoice = function () {

            if ($scope.invoice.type == '10') {
                if (!$scope.invoice.invoiceTitle || $scope.invoice.invoiceTitle.trim().length == 0) {
                    popupService.showToast("请填写开票信息");
                    return;
                }

            } else if ($scope.invoice.type == '20') {
                if (!$scope.invoice.companyName || $scope.invoice.companyName.trim().length == 0
                    || !$scope.invoice.taxpayerCode || $scope.invoice.taxpayerCode.trim().length == 0
                    || !$scope.invoice.companyAddress || $scope.invoice.companyAddress.trim().length == 0
                    || !$scope.invoice.companyTel || $scope.invoice.companyTel.trim().length == 0
                    || !$scope.invoice.bankName || $scope.invoice.bankName.trim().length == 0
                    || !$scope.invoice.bankAccount || $scope.invoice.bankAccount.trim().length == 0) {
                    popupService.showToast("请填写完整的开票信息");
                    return;
                }
            }

            $rootScope.invoiceInfo = $scope.invoice;

            $ionicHistory.goBack();
        };
    })
    // 发票信息 end
    // 地址管理 start
    .controller('addressManagerCtrl', function ($scope, $state, $ionicTabsDelegate,
                                                $rootScope, popupService,
                                                localStorageService, addressService) {
        $rootScope.setTitle("地址管理");
        var userInfo = localStorageService.get(KEY_USERINFO);

        //进入页面后初始化页面
        $scope.$on('$ionicView.enter', function () {
            $scope.init();
        });

        //初始化函数
        $scope.init = function () {
            var param = {"memberId": userInfo.memberId};
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

        $scope.modifyAddress = function (addressId) {
            $state.go("modifyAddress", {"type": 'modify', "addressId": addressId});
        };

        $scope.addAddress = function () {
            $state.go("modifyAddress", {"type": 'add'});
        };

        $scope.deleteAddress = function (addressId) {

            popupService.showConfirm("确认删除", "确定删除该收货地址信息？", function () {

                var param = {
                    addressId: addressId,
                    memberId: userInfo.memberId
                };

                new addressService(param).deleteItem()
                    .then(function (res) {
                        if (res.resultCode == "00") {
                            var selectInfo = $rootScope.selectedAddressInfo;
                            if (selectInfo && selectInfo.addressId == addressId) {
                                $rootScope.selectedAddressInfo = undefined;
                            }
                            $scope.init();
                            popupService.showToast("删除成功");
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
                memberId: userInfo.memberId
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
    })
    // 地址管理 end
    // 地址选择 start
    .controller('addressSelectCtrl', function ($scope, $state, $stateParams, $rootScope,
                                               $ionicHistory, $ionicTabsDelegate,
                                               localStorageService, addressService, popupService) {
        $rootScope.setTitle("地址选择");
        var selectInfo = $rootScope.selectedAddressInfo;

        var userInfo = localStorageService.get(KEY_USERINFO);

        //进入页面后初始化页面
        $scope.$on('$ionicView.enter', function () {
            $scope.init();
        });

        //初始化函数
        $scope.init = function () {
            var param = {"memberId": userInfo.memberId};

            new addressService(param).getList()
                .then(function (res) {
                    if (res.resultCode == "00") {
                        $scope.addressList = res.results;
                        angular.forEach($scope.addressList, function (address) {
                            if (selectInfo && address.addressId == selectInfo.addressId) {
                                // 选中
                                $rootScope.selectedAddressInfo = address;
                            }
                        });
                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function (error) {
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
        };

        $scope.setBgColor = function (addressId) {
            if (selectInfo && selectInfo.addressId == addressId) {
                return "bg-selectRow";
            }
        };

        $scope.selectAddress = function (addressInfo) {

            $rootScope.selectedAddressInfo = addressInfo;

            $ionicHistory.goBack();
        };

        $scope.manageAddress = function () {
            $state.go("addressManager", {});
        }
    })
    // 地址选择 end
    // 门店选择 start
    .controller('storeSelectCtrl', function ($scope, $state, $stateParams, $rootScope,
                                             $ionicHistory, $ionicTabsDelegate,
                                             popupService, orderService) {
        $rootScope.setTitle("门店选择");

        $scope.info = {provinceCd: '0', cityCd: '0', areaCd: '0'};//初始化联动下拉

        var selectedShop = $rootScope.selectedShopInfo;

        $scope.orderList = $stateParams.orderList;

        //进入页面后初始化页面
        $scope.$on('$ionicView.enter', function () {
            $scope.init();
        });

        //初始化函数
        $scope.init = function () {
            $scope.getProvinces();
            $scope.getStoreList();
        };

        $scope.getStoreList = function () {
            var param = {
                orderList: $scope.orderList,
                provinceCode: ($scope.info.provinceCd == '0' ? null : $scope.info.provinceCd),
                cityCode: ($scope.info.cityCd == '0' ? null : $scope.info.cityCd),
                districtCode: ($scope.info.areaCd == '0' ? null : $scope.info.areaCd)
            };

            // 获取门店信息
            new orderService().getOrgList(param)
                .then(function (res) {
                        if (res.resultCode == "00") {
                            $scope.storeList = res.data;
                        } else {
                            popupService.showToast(res.resultMessage);
                        }
                    }, function (error) {
                        popupService.showToast(commonMessage.networkErrorMsg);
                    }
                );
        };

        $scope.setBgColor = function (orgId) {
            if (selectedShop && selectedShop.store_code == orgId) {
                return "bg-selectRow";
            }
        };

        $scope.selectStore = function (store) {
            //if(store.new_count && store.new_count > 0){
            //    $rootScope.selectedShopInfo = store;
            //
            //    $ionicHistory.goBack();
            //} else {
            //    popupService.showToast("您选择的门店无货,请选择其他有库存的门店");
            //}
//门店限制取消
            $rootScope.selectedShopInfo = store;
            $ionicHistory.goBack();
        };

        $scope.getProvinces = function () {
            new orderService().getOrgAddressList(null)
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

            $scope.getStoreList();

            new orderService().getOrgAddressList(param)
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

    })
    // 门店选择 end
    // 搜索 start
    .controller('searchGoodsCtrl', function ($scope, $state, $stateParams, $ionicTabsDelegate, localStorageService, hotSearchService, popupService, $rootScope) {
        $rootScope.setTitle("搜索");
        $scope.searchGoods = {search: $stateParams.type};


        //hotSearchService.then(function (res) {
        //    $scope.hotSearchList = res;
        //});

        //$scope.searchGoods = function () {
        //    goodsSearchService.then(function (res) {
        //        $scope.goodsList = res;
        //    });
        //};

        var searchKey = "";
        if ($stateParams.type != null && $stateParams.type.length > 0) {
            searchKey = $stateParams.type;
            $rootScope.searchKey = $stateParams.type;
        } else {
            searchKey = $rootScope.searchKey;
        }
        $scope.searchGoods.search = searchKey;

        new hotSearchService({"searchKey": searchKey}).then(function (res) {
            if (res.resultCode == "00") {
                $scope.goodsList = res.results;
            } else {
                popupService.showToast(res.resultMessage);
            }
        }, function (err) {
            popupService.showToast(commonMessage.networkErrorMsg);
        });

        //$scope.goGoodsDetail = function (goodsId) {
        //    alert(goodsId);
        //    //$state.go("goodsList", {});
        //};

        $scope.goProdocutDetail = function (type) {
            $state.go("goodsDetail", {"type": type});
        };

        $scope.goGoodsSearch = function () {

            if ($scope.searchGoods.search != null && $scope.searchGoods.search.length > 0) {
                $rootScope.searchKey = $scope.searchGoods.search;
            } else {
                $scope.searchGoods.search = $rootScope.searchKey;
            }


            new hotSearchService({"searchKey": $scope.searchGoods.search}).then(function (res) {
                if (res.resultCode == "00") {
                    $scope.goodsList = res.results;
                } else {
                    popupService.showToast(res.resultMessage);
                }
            }, function () {
                popupService.showToast(commonMessage.networkErrorMsg);
            });
        };
    })
    // 搜索 end
    // 关于我们 start
    .controller('aboutCtrl', function ($scope, $rootScope, $state, $ionicTabsDelegate, localStorageService, aboutService, popupService) {
        $rootScope.setTitle("关于我们");
        aboutService.then(function (res) {
            if (res.resultCode == "00") {
                $scope.aboutInfo = res.results;
            } else {
                popupService.showToast(res.resultMessage);
            }
        }, function () {
            popupService.showToast(commonMessage.networkErrorMsg);
        });
    })
    // 关于我们 end
    // 个人信息 start
    .controller('personalCtrl', function ($scope, $rootScope, $state, $ionicHistory, $filter,
                                          localStorageService, personalService, popupService, ionicDatePicker) {
        $rootScope.setTitle("个人信息");
        $scope.info = {provinceCd: '0', cityCd: '0', areaCd: '0'};//初始化联动下拉
        //进入页面后初始化页面
        $scope.$on('$ionicView.enter', function () {
            $scope.init();
        });

        //初始化函数
        $scope.init = function () {

            var userInfo = localStorageService.get(KEY_USERINFO);
            $scope.personalInfo = userInfo;

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
                $scope.birthdateStatus = '10';
                $scope.personalInfo.birthdate = $filter('date')($scope.personalInfo.birthdate, 'yyyy-MM-dd');
            } else {
                $scope.birthdateStatus = '20';
            }
            //$scope.personalInfo.birthdate = $filter('date')($scope.personalInfo.birthdate, 'yyyy/MM/dd');
            // 日期选择器
            $scope.openDatePicker = function (val) {
                var ipObj = {
                    callback: function (val) {  //Mandatory

                        $scope.personalInfo.birthdate = $filter('date')(new Date(val), 'yyyy-MM-dd');
                    },
                    disabledDates: [],
                    from: new Date(1900, 1, 1),
                    to: new Date(),
                    inputDate: $scope.personalInfo.birthdate ? new Date($scope.personalInfo.birthdate) : new Date(),
                    mondayFirst: true,
                    closeOnSelect: false,
                    templateType: 'popup'
                };
                ionicDatePicker.openDatePicker(ipObj);
            };


            new personalService({}).getERPAddressInfo()
                .then(function (res) {
                    if (res.resultCode == '00') {
                        $scope.provincesList = res.data;

                        if (userInfo.provinceCode) {
                            $scope.info.provinceCd = userInfo.provinceCode;
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

            if (userInfo.birthdate > 0) {
                //$scope.birthdate.data = new Date(userInfo.birthdate);
                $scope.currentDate = new Date(userInfo.birthdate);
            }

            if (userInfo.provinceCode) {
                $scope.getDefaultCityList(userInfo.provinceCode, userInfo.cityCode, userInfo.districtCode);
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

            if (!isValid) {
                //TODO 获取验证码check失败 弹窗
            } else {

                var param = {
                    "memberId": $scope.personalInfo.memberId,
                    "memberName": $scope.personalInfo.memberName,
                    "sex": $scope.personalInfo.sex,
                    "birthdate": $scope.personalInfo.birthdate,
                    "email": $scope.personalInfo.email,
                    "profession": $scope.personalInfo.profession,
                    "income": $scope.personalInfo.income,
                    "postCode": $scope.personalInfo.postCode,
                    "provinceCode": $scope.info.provinceCd,
                    "cityCode": $scope.info.cityCd,
                    "districtCode": $scope.info.areaCd,
                    "address": $scope.personalInfo.address
                };

                new personalService(param).editPersonal()
                    .then(function (res) {
                        console.log("会员头像")
                        console.log(res)
                        if (res.resultCode == "00") {
                            $scope.personal = res.data;
                            localStorageService.set(KEY_USERINFO, res.data);

                            popupService.showToast('修改成功');
                            $ionicHistory.goBack();
                        } else {
                            popupService.showToast(res.resultMessage);
                        }
                    }, function () {
                        popupService.showToast(commonMessage.networkErrorMsg);
                    });
            }
        }
    })
    // 个人信息 end
    // 商品列表 start
    .controller('goodsListCtrl', function ($scope, $state, $stateParams, $ionicPopover, $ionicTabsDelegate, localStorageService, goodsListService, popupService, $rootScope) {
        //$rootScope.setTitle("商品列表");
        if ($stateParams.title) {
            $rootScope.setTitle($stateParams.title);
        } else {
            $rootScope.setTitle("商品列表");
        }

        var userInfo = localStorageService.get(KEY_USERINFO);
        var memberId = "";
        if (userInfo) {
            memberId = userInfo.memberId;
        }

        //数据集合
        $scope.goodsList = [];
        $scope.isLoading = false;

        var goodsTypeId = "";
        if ($stateParams.type != null
            && $stateParams.type.length > 0) {
            $rootScope.goodsTypeId = $stateParams.type;
            goodsTypeId = $stateParams.type;
        } else {
            goodsTypeId = $rootScope.goodsTypeId;
        }

        var actId = "";
        if ($stateParams.actId != null
            && $stateParams.actId.length > 0) {
            $rootScope.actId = $stateParams.actId;
            actId = $stateParams.actId;
        } else {
            actId = $rootScope.actId;
        }

        var firstGoodsTypeId = "";
        if ($stateParams.firstGoodsTypeId != null
            && $stateParams.firstGoodsTypeId.length > 0) {
            $rootScope.firstGoodsTypeId = $stateParams.firstGoodsTypeId;
            firstGoodsTypeId = $stateParams.firstGoodsTypeId;
        } else {
            firstGoodsTypeId = $rootScope.firstGoodsTypeId;
        }

        var pageType = "";
        var classifyId = "";
        var classifyName = "";

        if ($stateParams.pageType != null
            && $stateParams.pageType.length > 0) {
            $rootScope.pageType = $stateParams.pageType;
            $rootScope.classifyId = $stateParams.classifyId;
            $rootScope.classifyName = $stateParams.classifyName;

            pageType = $stateParams.pageType;
            classifyId = $stateParams.classifyId;
            classifyName = $stateParams.classifyName;
        } else {
            pageType = $rootScope.pageType;
            classifyId = $rootScope.classifyId;
            classifyName = $rootScope.classifyName;
        }

        //从首页直接跳转直接跳转
        if (pageType != null && pageType.length > 0 && pageType == '3') {
            //新品 3
            //$scope.getNewData();
        } else if (pageType != null && pageType.length > 0) {
            //其他
            firstGoodsTypeId = classifyId;
        }

        pageType = '';
        classifyId = '';
        classifyName = '';

        $scope.initData = function () {
            if ($scope.isLoading) {
                return;
            }
            $scope.isLoading = true;
            var params = {
                "goodsTypeId": goodsTypeId,
                "actId": actId,
                "firstGoodsTypeId": firstGoodsTypeId,
                "memberId": memberId
            };
            //初始化调用
            new goodsListService(params).then(function (res) {
                $scope.isLoading = false;
                if (res.resultCode == "00") {
                    $scope.goodsList = res.results;
                } else {
                    popupService.showToast(res.resultMessage);
                }
            }, function (err) {
                $scope.isLoading = false;
                popupService.showToast(commonMessage.networkErrorMsg);
            });
        };

        //跳转到详细
        $scope.goProdocutDetail = function (type) {
            $state.go("goodsDetail", {"type": type});
        };
        //价格输入后过滤
        $scope.goodsListInfo = {"from": "", "to": ""};
        $scope.resetData = function () {
            $scope.goodsListInfo.from = "";
            $scope.goodsListInfo.to = "";
        };
        //全部按钮点击
        $scope.doGetAll = function ($event) {

            var targetDom = $event.target;

            $jq(targetDom)
                .css("font-weight", "bold")
                .css("color", "#000000")
                .siblings()
                .css("font-weight", "normal")
                .css("color", "#CCCCCC");

            new goodsListService({
                "goodsTypeId": goodsTypeId,
                "actId": actId,
                "firstGoodsTypeId": firstGoodsTypeId,
                "memberId": memberId
            }).then(function (res) {
                if (res.resultCode == "00") {
                    $scope.goodsList = res.results;
                } else {
                    popupService.showToast(res.resultMessage);
                }
            }, function () {
                popupService.showToast(commonMessage.networkErrorMsg);
            });
        };
        //加载更多
        $scope.doGetMore = function () {
            if ($scope.isLoading) {
                return;
            }
            $scope.isLoading = true;
            var insertTime = null;
            if ($scope.goodsList[$scope.goodsList.length - 1] != null) {
                insertTime = $scope.goodsList[$scope.goodsList.length - 1].insertTime;
            }
            if (insertTime == null) {
                popupService.showToast("没有更多数据");
                return;
            }
            var sort = $scope.goodsList[$scope.goodsList.length - 1].sort;

            var from = 0;
            var to = 0;
            if ($scope.goodsListInfo.from != null) {
                from = $scope.goodsListInfo.from;
            }
            if ($scope.goodsListInfo.to != null) {
                to = $scope.goodsListInfo.to;
            }
            var params = {
                "goodsTypeId": goodsTypeId,
                "actId": actId,
                "firstGoodsTypeId": firstGoodsTypeId,
                "insertTime": insertTime,
                "from": from,
                "to": to,
                "sort": sort,
                "memberId": memberId
            }
            new goodsListService(params)
                .then(function (res) {
                    $scope.isLoading = false;
                    if (res.resultCode == "00") {
                        if (res.results.length != 0) {
                            $scope.goodsList = $scope.goodsList.concat(res.results);
                        } else {
                            popupService.showToast("没有更多数据");
                        }
                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function () {
                    $scope.isLoading = false;
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
        };
        //价格过滤
        $scope.doResearch = function (isvalid) {
            if ($scope.isLoading) {
                return;
            }
            $scope.isLoading = true;

            if (!isvalid) {
                popupService.showToast("输入的价格区间必须是数字");
                return;
            }

            var from = 0;
            var to = 0;
            if ($scope.goodsListInfo.from != null) {
                from = $scope.goodsListInfo.from;
            }
            if ($scope.goodsListInfo.to != null) {
                to = $scope.goodsListInfo.to;
            }

            new goodsListService({
                "goodsTypeId": goodsTypeId,
                "actId": actId,
                "firstGoodsTypeId": firstGoodsTypeId,
                "from": from,
                "to": to,
                "memberId": memberId
            }).then(function (res) {
                $scope.isLoading = false;
                if (res.resultCode == "00") {
                    $scope.goodsList = res.results;
                } else {
                    popupService.showToast(res.resultMessage);
                }
            }, function (err) {
                $scope.isLoading = false;
                popupService.showToast(commonMessage.networkErrorMsg);
            });
            $scope.popover.hide();
        };
        //点击筛选时候的样式
        $scope.filter = function ($event) {

            var targetDom = $event.target;

            $jq(targetDom)
                .css("font-weight", "bold")
                .css("color", "#000000")
                .siblings()
                .css("font-weight", "normal")
                .css("color", "#CCCCCC");

            $ionicPopover.fromTemplateUrl('templates/goodsListFilter.html', {
                scope: $scope
            }).then(function (popover) {
                $scope.popover = popover;
                $scope.popover.show($jq(targetDom).prev());
            });
            $scope.$on('$ionicView.beforeLeave', function () {
                $scope.popover.hide();
            });
        }
        $scope.initData();
    })

    // 打折列表 start
    .controller('saleListCtrl', function ($scope, $state, $stateParams, $ionicPopover, $ionicTabsDelegate, localStorageService, goodsListService, popupService, $rootScope) {

        $rootScope.setTitle("打折列表");

        var userInfo = localStorageService.get(KEY_USERINFO);
        var memberId = "";
        if (userInfo) {
            memberId = userInfo.memberId;
        }

        //数据集合
        $scope.goodsList = [];
        $scope.isLoading = false;


        var actId = "";
        if ($stateParams.type != null
            && $stateParams.type.length > 0) {
            $rootScope.actId = $stateParams.type;
            actId = $stateParams.type;
        } else {
            actId = $rootScope.actId;
        }

        $scope.initData = function () {
            if ($scope.isLoading) {
                return;
            }
            $scope.isLoading = true;
            var params = {
                "actId": actId,
                "memberId": memberId
            };
            //初始化调用
            new goodsListService(params).then(function (res) {
                $scope.isLoading = false;
                if (res.resultCode == "00") {
                    $scope.goodsList = res.results;
                } else {
                    popupService.showToast(res.resultMessage);
                }
            }, function (err) {
                $scope.isLoading = false;
                popupService.showToast(commonMessage.networkErrorMsg);
            });
        };

        //跳转到详细
        $scope.goProdocutDetail = function (type) {
            $state.go("goodsDetail", {"type": type});
        };
        //价格输入后过滤
        $scope.goodsListInfo = {"from": "", "to": ""};
        $scope.resetData = function () {
            $scope.goodsListInfo.from = "";
            $scope.goodsListInfo.to = "";
        };
        //全部按钮点击
        $scope.doGetAll = function ($event) {

            var targetDom = $event.target;

            $jq(targetDom)
                .css("font-weight", "bold")
                .css("color", "#000000")
                .siblings()
                .css("font-weight", "normal")
                .css("color", "#CCCCCC");

            new goodsListService({"actId": actId, "memberId": memberId}).then(function (res) {
                if (res.resultCode == "00") {
                    $scope.goodsList = res.results;
                } else {
                    popupService.showToast(res.resultMessage);
                }
            }, function () {
                popupService.showToast(commonMessage.networkErrorMsg);
            });
        };
        //加载更多
        $scope.doGetMore = function () {
            if ($scope.isLoading) {
                return;
            }
            $scope.isLoading = true;
            var insertTime = null;
            if ($scope.goodsList[$scope.goodsList.length - 1] != null) {
                insertTime = $scope.goodsList[$scope.goodsList.length - 1].insertTime;
            }
            if (insertTime == null) {
                popupService.showToast("没有更多数据");
                return;
            }
            var sort = $scope.goodsList[$scope.goodsList.length - 1].sort;

            var from = 0;
            var to = 0;
            if ($scope.goodsListInfo.from != null) {
                from = $scope.goodsListInfo.from;
            }
            if ($scope.goodsListInfo.to != null) {
                to = $scope.goodsListInfo.to;
            }
            var params = {
                "actId": actId,
                "insertTime": insertTime,
                "from": from,
                "to": to,
                "sort": sort,
                "memberId": memberId
            }
            new goodsListService(params)
                .then(function (res) {
                    $scope.isLoading = false;
                    if (res.resultCode == "00") {
                        if (res.results.length != 0) {
                            $scope.goodsList = $scope.goodsList.concat(res.results);
                        } else {
                            popupService.showToast("没有更多数据");
                        }
                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function () {
                    $scope.isLoading = false;
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
        };
        //价格过滤
        $scope.doResearch = function (isvalid) {
            if ($scope.isLoading) {
                return;
            }
            $scope.isLoading = true;

            if (!isvalid) {
                popupService.showToast("输入的价格区间必须是数字");
                return;
            }

            var from = 0;
            var to = 0;
            if ($scope.goodsListInfo.from != null) {
                from = $scope.goodsListInfo.from;
            }
            if ($scope.goodsListInfo.to != null) {
                to = $scope.goodsListInfo.to;
            }

            new goodsListService({"actId": actId, "from": from, "to": to, "memberId": memberId}).then(function (res) {
                $scope.isLoading = false;
                if (res.resultCode == "00") {
                    $scope.goodsList = res.results;
                } else {
                    popupService.showToast(res.resultMessage);
                }
            }, function (err) {
                $scope.isLoading = false;
                popupService.showToast(commonMessage.networkErrorMsg);
            });
            $scope.popover.hide();
        };
        //点击筛选时候的样式
        $scope.filter = function ($event) {

            var targetDom = $event.target;

            $jq(targetDom)
                .css("font-weight", "bold")
                .css("color", "#000000")
                .siblings()
                .css("font-weight", "normal")
                .css("color", "#CCCCCC");

            $ionicPopover.fromTemplateUrl('templates/goodsListFilter.html', {
                scope: $scope
            }).then(function (popover) {
                $scope.popover = popover;
                $scope.popover.show($jq(targetDom).prev());
            });
            $scope.$on('$ionicView.beforeLeave', function () {
                $scope.popover.hide();
            });
        }
        $scope.initData();
    })



    // 积分兑换画面
    .controller('pointsExchangePageCtrl', function ($scope, $state, $stateParams, $ionicPopover, $ionicTabsDelegate,
                                                    localStorageService, activityListService, goodsListService, activityPointsService, popupService, $rootScope) {

        $rootScope.setTitle("积分兑换商品");
        var userInfo = localStorageService.get(KEY_USERINFO);
        var memberId = "";
        if (userInfo) {
            memberId = userInfo.memberId;
        }

        //数据集合
        $scope.goodsList = [];
        $scope.isLoading = false;
        $scope.activityName = "";


        var actId = "";
        // 活动列表积分兑换的所有活动列表
        new activityListService({"memberId": memberId})
            .then(function (res) {
                if (res.resultCode == "00") {
                    $scope.activityList = res.data;

                    //默认选择第一个
                    if ($scope.activityList != null && $scope.activityList.length > 0) {
                        actId = $scope.activityList[0].activityId;
                        $scope.activityName = $scope.activityList[0].activityName;
                        $scope.initData();
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
                    if (res.results != null && res.results.length > 0) {
                        $scope.activityPoints = res.results[0];
                        if ($scope.activityPoints.cmList != null) {
                            $scope.playbill = $scope.activityPoints.cmList[0];
                        }
                    }
                } else {
                    // popupService.showToast(res.resultMessage);
                }
            }, function (error) {
                // popupService.showToast(commonMessage.networkErrorMsg);
            });


        $scope.initData = function () {
            if ($scope.isLoading) {
                return;
            }
            $scope.isLoading = true;
            var params = {
                "actId": actId,
                "memberId": memberId
            };
            //初始化调用
            new goodsListService(params).then(function (res) {
                $scope.isLoading = false;
                if (res.resultCode == "00") {
                    $scope.goodsList = res.results;
                } else {
                    popupService.showToast(res.resultMessage);
                }
            }, function (err) {
                $scope.isLoading = false;
                popupService.showToast(commonMessage.networkErrorMsg);
            });
        };

        //跳转到详细
        $scope.goProdocutDetail = function (type) {
            $state.go("goodsDetail", {"type": type});
        };

        //加载更多
        $scope.doGetMore = function () {
            if ($scope.isLoading) {
                return;
            }
            $scope.isLoading = true;
            var insertTime = null;
            if ($scope.goodsList[$scope.goodsList.length - 1] != null) {
                insertTime = $scope.goodsList[$scope.goodsList.length - 1].insertTime;
            }
            if (insertTime == null) {
                popupService.showToast("没有更多数据");
                return;
            }
            var sort = $scope.goodsList[$scope.goodsList.length - 1].sort;

            var params = {
                "actId": actId,
                "insertTime": insertTime,
                "sort": sort,
                "memberId": memberId
            }
            new goodsListService(params)
                .then(function (res) {
                    $scope.isLoading = false;
                    if (res.resultCode == "00") {
                        if (res.results.length != 0) {
                            $scope.goodsList = $scope.goodsList.concat(res.results);
                        } else {
                            popupService.showToast("没有更多数据");
                        }
                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function () {
                    $scope.isLoading = false;
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
        };

        //点击筛选时候的样式
        $scope.filter = function ($event) {

            var targetDom = $event.target;

            //$jq(targetDom)
            //    .css("font-weight", "bold")
            //    .css("color", "#000000")
            //    .siblings()
            //    .css("font-weight", "normal")
            //    .css("color", "#CCCCCC");

            $ionicPopover.fromTemplateUrl('templates/pointsExchangeFilter.html', {
                scope: $scope
            }).then(function (popover) {
                $scope.popover = popover;
                $scope.popover.show($jq(targetDom).prev());
            });
            $scope.$on('$ionicView.beforeLeave', function () {
                $scope.popover.hide();
            });
        };

        $scope.activitySelected = function (activityId, activityName) {
            actId = activityId;
            $scope.activityName = activityName;
            $scope.initData();
            $scope.popover.hide();
        }
    })

    // 积分兑换画面
    .controller('pointsExchangePageNewCtrl', function ($scope, $state, $stateParams, $ionicPopover, $ionicTabsDelegate,
                                                       localStorageService, activityListService, goodsListService, activityPointsService, popupService, $rootScope) {

        $rootScope.setTitle("积分兑换商品");
        var userInfo = localStorageService.get(KEY_USERINFO);
        var memberId = "";
        if (userInfo) {
            memberId = userInfo.memberId;
        }

        //数据集合
        $scope.goodsList = [];
        $scope.isLoading = false;
        $scope.activityName = "";
        $scope.activityIdList = [];


        var actId = "";
        // 活动列表积分兑换的所有活动列表
        new activityListService({"memberId": memberId})
            .then(function (res) {
                if (res.resultCode == "00") {
                    $scope.activityList = res.data;

                    //默认选择第一个
                    if ($scope.activityList != null && $scope.activityList.length > 0) {

                        for (var i = 0; i < $scope.activityList.length; i++) {
                            actId = $scope.activityList[i].activityId;
                            $scope.activityIdList.push($scope.activityList[i].activityName);
                            $scope.initData(actId);
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
                    if (res.results != null && res.results.length > 0) {
                        $scope.activityPoints = res.results[0];
                        if ($scope.activityPoints.cmList != null) {
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


        $scope.initData = function (actId) {
            //if($scope.isLoading){
            //    return;
            //}
            $scope.isLoading = true;
            var params = {
                "actId": actId,
                "memberId": memberId
            };
            //初始化调用
            new goodsListService(params).then(function (res) {
                $scope.isLoading = false;
                if (res.resultCode == "00") {
                    $scope.goodsList = $scope.goodsList.concat(res.results);

                    //重组前台显示的顺序
                    $scope.goodsListNew = [];
                    for (var i = 0; i < $scope.activityIdList.length; i++) {
                        var activityName = $scope.activityIdList[i];
                        for (var j = 0; j < $scope.goodsList.length; j++) {
                            if (activityName == $scope.goodsList[j].activityName) {
                                $scope.goodsListNew = $scope.goodsListNew.concat($scope.goodsList[j]);
                                break;
                            }
                        }
                    }
                } else {
                    popupService.showToast(res.resultMessage);
                }
            }, function (err) {
                $scope.isLoading = false;
                popupService.showToast(commonMessage.networkErrorMsg);
            });
        };

        //跳转到详细
        $scope.goProdocutDetail = function (type) {
            $state.go("goodsDetail", {"type": type});
        };

        ////加载更多
        //$scope.doGetMore = function () {
        //    if($scope.isLoading){
        //        return;
        //    }
        //    $scope.isLoading = true;
        //    var insertTime = null;
        //    if($scope.goodsList[$scope.goodsList.length - 1] != null){
        //        insertTime = $scope.goodsList[$scope.goodsList.length - 1].insertTime;
        //    }
        //    if(insertTime == null){
        //        popupService.showToast("没有更多数据");
        //        return;
        //    }
        //    var sort = $scope.goodsList[$scope.goodsList.length - 1].sort;
        //
        //    var params = {
        //        "actId":actId,
        //        "insertTime":insertTime,
        //        "sort":sort,
        //        "memberId":memberId
        //    }
        //    new goodsListService(params)
        //        .then(function (res) {
        //            $scope.isLoading = false;
        //            if (res.resultCode == "00") {
        //                if(res.results.length != 0){
        //                    $scope.goodsList = $scope.goodsList.concat(res.results);
        //                }else {
        //                    popupService.showToast("没有更多数据");
        //                }
        //            } else {
        //                popupService.showToast(res.resultMessage);
        //            }
        //        }, function () {
        //            $scope.isLoading = false;
        //            popupService.showToast(commonMessage.networkErrorMsg);
        //        });
        //};

        //点击筛选时候的样式
        //$scope.filter = function ($event) {
        //
        //    var targetDom = $event.target;
        //
        //    //$jq(targetDom)
        //    //    .css("font-weight", "bold")
        //    //    .css("color", "#000000")
        //    //    .siblings()
        //    //    .css("font-weight", "normal")
        //    //    .css("color", "#CCCCCC");
        //
        //    $ionicPopover.fromTemplateUrl('templates/pointsExchangeFilter.html', {
        //        scope: $scope
        //    }).then(function (popover) {
        //        $scope.popover = popover;
        //        $scope.popover.show($jq(targetDom).prev());
        //    });
        //    $scope.$on('$ionicView.beforeLeave', function () {
        //        $scope.popover.hide();
        //    });
        //};

        //$scope.activitySelected = function(activityId,activityName){
        //    actId = activityId;
        //    $scope.activityName = activityName;
        //    $scope.initData();
        //    $scope.popover.hide();
        //}
    })
    // 商品列表 end
    // 新品列表 start
    .controller('goodsListDisplayCtrl', function ($scope, $state, $stateParams, $ionicPopover, $ionicTabsDelegate, localStorageService, goodsListService, getNewDataService, goodsListDisplayService, popupService, $rootScope) {
        if ($stateParams.title) {
            $rootScope.setTitle($stateParams.title);
        } else {
            $rootScope.setTitle("新品列表");
        }
        //数据集合
        $scope.goodsList = [];
        $scope.sortByTimeValue = 3;
        var goodsTypeId = "";
        if ($stateParams.type != null
            && $stateParams.type.length > 0) {
            $rootScope.goodsTypeId = $stateParams.type;
            goodsTypeId = $stateParams.type;
        } else {
            goodsTypeId = $rootScope.goodsTypeId;
        }

        var cmsId = "";
        if ($stateParams.cmsId != null
            && $stateParams.cmsId.length > 0) {
            $rootScope.cmsId = $stateParams.cmsId;
            cmsId = $stateParams.cmsId;
        } else {
            cmsId = $rootScope.cmsId;
        }

        var userInfo = localStorageService.get(KEY_USERINFO);
        var memberId = "";
        if (userInfo) {
            memberId = userInfo.memberId;
        }


        if (!$stateParams.title) {
            $scope.getNewData = function () {

                if (goodsTypeId != null && goodsTypeId.split("_").length > 0) {
                    goodsTypeId = goodsTypeId.split("_")[0];
                }
                new getNewDataService({"firstGoodsTypeId": goodsTypeId})
                    .then(function (res) {
                        if (res.resultCode == "00") {
                            $scope.newDataList = res.results;
                            cmsId = res.cmsId;
                            console.log(res.results);
                            if ($scope.newDataList == null || $scope.newDataList.length == 0) {
                                $scope.newDataList = "####################################";
                            }

                            //初始化调用
                            new goodsListService({
                                "goodsIds": $scope.newDataList,
                                "memberId": memberId,
                                "cmsId": cmsId,
                                "sortByTimeValue": $scope.sortByTimeValue
                            }).then(function (res) {
                                if (res.resultCode == "00") {
                                    $scope.goodsList = res.results;
                                } else {
                                    popupService.showToast(res.resultMessage);
                                }
                            }, function (err) {
                                popupService.showToast(commonMessage.networkErrorMsg);
                            });
                        } else {
                            popupService.showToast(res.resultMessage);
                        }
                    }, function () {
                        popupService.showToast(commonMessage.networkErrorMsg);
                    });
            };
            $scope.getNewData();
        } else {
            //初始化调用
            new goodsListService({
                "goodsIds": goodsTypeId,
                "memberId": memberId,
                "cmsId": cmsId,
                "sortByTimeValue": $scope.sortByTimeValue
            }).then(function (res) {
                if (res.resultCode == "00") {
                    $scope.goodsList = res.results;
                } else {
                    popupService.showToast(res.resultMessage);
                }
            }, function (err) {
                popupService.showToast(commonMessage.networkErrorMsg);
            });
        }

        //
        ////初始化调用
        //new goodsListDisplayService({"goodsTypeId": goodsTypeId,"memberId":memberId}).then(function (res) {
        //    if (res.resultCode == "00") {
        //        $scope.goodsList = res.results;
        //    } else {
        //        popupService.showToast(res.resultMessage);
        //    }
        //}, function (err) {
        //    popupService.showToast(commonMessage.networkErrorMsg);
        //});
        //跳转到详细
        $scope.goProdocutDetail = function (type) {
            $state.go("goodsDetail", {"type": type});
        };

        //加载更多
        $scope.doGetMore = function () {
            var insertTime = null;
            if ($scope.goodsList[$scope.goodsList.length - 1] != null) {
                if ($scope.goodsList[$scope.goodsList.length - 1].insertTimeGds != null) {
                    insertTime = $scope.goodsList[$scope.goodsList.length - 1].insertTimeGds;
                } else {
                    insertTime = $scope.goodsList[$scope.goodsList.length - 1].insertTime;
                }

            }
            if (insertTime == null) {
                popupService.showToast("没有更多数据");
                return;
            }

            if (!$stateParams.title) {
                goodsTypeId = $scope.newDataList;
            }

            new goodsListService({
                "goodsIds": goodsTypeId,
                "memberId": memberId,
                "cmsId": cmsId,
                "sortByTimeValue": $scope.sortByTimeValue,
                "insertTimeGds": insertTime
            }).then(function (res) {
                if (res.resultCode == "00") {
                    if (res.results.length != 0) {
                        $scope.goodsList = $scope.goodsList.concat(res.results);
                    } else {
                        popupService.showToast("没有更多数据");
                    }
                } else {
                    popupService.showToast(res.resultMessage);
                }
            }, function () {
                popupService.showToast(commonMessage.networkErrorMsg);
            });
        };
    })

    .controller('goodsListCmsCtrl', function ($scope, $state, $stateParams, $ionicPopover, $ionicTabsDelegate, localStorageService, goodsListService, getNewDataService, goodsListDisplayService, popupService, $rootScope) {

        $rootScope.setTitle("新品列表");

        //数据集合
        $scope.goodsList = [];
        $scope.sortByTimeValue = 3;
        var goodsTypeId = "";
        if ($stateParams.type != null
            && $stateParams.type.length > 0) {
            $rootScope.goodsTypeId = $stateParams.type;
            goodsTypeId = $stateParams.type;
        } else {
            goodsTypeId = $rootScope.goodsTypeId;
        }

        var userInfo = localStorageService.get(KEY_USERINFO);
        var memberId = "";
        if (userInfo) {
            memberId = userInfo.memberId;
        }

        //初始化调用
        new goodsListDisplayService({"goodsTypeId": goodsTypeId, "memberId": memberId}).then(function (res) {
            if (res.resultCode == "00") {
                $scope.goodsList = res.results;
            } else {
                popupService.showToast(res.resultMessage);
            }
        }, function (err) {
            popupService.showToast(commonMessage.networkErrorMsg);
        });

        //跳转到详细
        $scope.goProdocutDetail = function (type) {
            $state.go("goodsDetail", {"type": type});
        };


    })
    // 商品列表 end
    // 我的优惠券 start
    .controller('couponListCtrl', function ($scope, $rootScope, $state, $ionicHistory, $ionicTabsDelegate, localStorageService, couponListService, popupService) {
        $rootScope.setTitle("我的优惠劵");
        $scope.hasMore = true;
        $scope.couponList = [];
        $scope.isLoading = false;
        var userInfo = localStorageService.get(KEY_USERINFO);

        $scope.doRefresh = function () {
            $scope.hasMore = true;
            $scope.couponList = [];
            $scope.getData(null);
        };

        $scope.doGetMore = function () {
            var lastUpdateTime = $scope.couponList[$scope.couponList.length - 1].updateTime;
            $scope.getData(lastUpdateTime);
        };
        $scope.getData = function (lastUpdateTime) {
            if ($scope.isLoading) {
                return;
            }
            $scope.isLoading = true;
            var param = {
                "memberId": userInfo.memberId,
                "updateTime": lastUpdateTime
            };
            new couponListService(param)
                .then(function (res) {
                    $scope.isLoading = false;
                    if (res.resultCode == '00') {
                        $scope.couponList = $scope.couponList.concat(res.results);
                        if ($scope.couponList && $scope.couponList.length >= 10) {
                            $scope.hasMore = true;
                        } else {
                            $scope.hasMore = false;
                        }
                    } else {
                        $scope.hasMore = false;
                        popupService.showToast(res.resultMessage);
                    }
                }, function (error) {
                    $scope.isLoading = false;
                    $scope.hasMore = false;
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
        };

        $scope.doRefresh();

        $scope.goBack = function () {
            $ionicHistory.goBack();
        }
    })
    // 我的优惠券 end
    // 收货地址 start
    .controller('modifyAddressCtrl', function ($scope, $state, $stateParams, $rootScope,
                                               $ionicHistory, popupService,
                                               $ionicTabsDelegate, localStorageService,
                                               addressService, commonService) {
        $rootScope.setTitle("收货地址");
        $scope.info = {provinceCd: '0', cityCd: '0', areaCd: '0', streetCd: '0'};//初始化联动下拉

        var userInfo = localStorageService.get(KEY_USERINFO);

        $scope.addressInfo = {
            name: null,
            tel: null,
            address: null
        };

        $scope.type = $stateParams.type;

        if ($stateParams.type == 'add') {
            $scope.title = "新增收货地址";
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
        } else if ($stateParams.type == 'modify') {
            $scope.title = "编辑收货地址";
            var param = {
                addressId: $stateParams.addressId,
                memberId: userInfo.memberId
            };
            new addressService(param).getDetail()
                .then(function (res) {

                    if (res.resultCode == "00") {
                        $scope.info.provinceCd = res.result.districtidProvince;
                        $scope.info.cityCd = res.result.districtidCity;
                        $scope.info.areaCd = res.result.districtidDistrict;
                        $scope.info.streetCd = '';//res.result.districtidStreet;
                        $scope.addressInfo.address = res.result.address;
                        $scope.addressInfo.name = res.result.contactor;
                        $scope.addressInfo.tel = res.result.phone;

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
            if (!isValid) {
                return;
            }

            if ($scope.info.provinceCd == '0' || $scope.info.cityCd == '0' || $scope.info.areaCd == '0') {
                return;
            }

            if ($stateParams.type == 'add') {

                var param = {
                    memberId: userInfo.memberId,
                    districtidProvince: $scope.info.provinceCd,
                    districtidCity: $scope.info.cityCd,
                    districtidDistrict: $scope.info.areaCd,
                    districtidStreet: '',//$scope.info.streetCd,<!-- 街道不显示 -->
                    address: $scope.addressInfo.address,
                    contactor: $scope.addressInfo.name,
                    phone: $scope.addressInfo.tel
                };

                new addressService(param).add()
                    .then(function (res) {
                        if (res.resultCode == "00") {
                            popupService.showToast('修改成功');
                            // $ionicHistory.goBack();
                            history.back(-1);
                        } else {
                            popupService.showToast(res.resultMessage);
                        }
                    }, function (error) {
                        popupService.showToast(commonMessage.networkErrorMsg);
                    });

            } else if ($stateParams.type == 'modify') {

                var param = {
                    addressId: $stateParams.addressId,
                    memberId: userInfo.memberId,
                    districtidProvince: $scope.info.provinceCd,
                    districtidCity: $scope.info.cityCd,
                    districtidDistrict: $scope.info.areaCd,
                    districtidStreet: '',//$scope.info.streetCd,<!-- 街道不显示 -->
                    address: $scope.addressInfo.address,
                    contactor: $scope.addressInfo.name,
                    phone: $scope.addressInfo.tel
                };

                new addressService(param).edit()
                    .then(function (res) {
                        if (res.resultCode == "00") {
                            popupService.showToast("修改成功");
                            //$ionicHistory.goBack();
                            history.back(-1);
                        } else {
                            popupService.showToast(res.resultMessage);
                        }
                    }, function (error) {
                        popupService.showToast(commonMessage.networkErrorMsg);
                    });
            }
        };


        //$scope.deleteAddress = function () {
        //
        //    popupService.showConfirm("确认删除", "确定删除该收货地址信息？", function () {
        //
        //        var param = {
        //            addressId: $stateParams.addressId,
        //            memberId: "cjk"
        //        };
        //
        //        new addressService(param).deleteItem()
        //            .then(function (res) {
        //
        //                var selectInfo = $rootScope.selectedAddressInfo;
        //                if(selectInfo && selectInfo.addressId == $stateParams.addressId){
        //                    $rootScope.selectedAddressInfo = undefined;
        //                }
        //                popupService.showToast("删除成功");
        //            }, function (error) {
        //                alert(error);
        //            });
        //
        //        $ionicHistory.goBack();
        //    });
        //};
    })
    // 收货地址 end
    // 我的心愿单 start
    .controller('favoriteCtrl', function ($scope, $rootScope, $state, $ionicTabsDelegate,
                                          localStorageService, favoriteService, popupService) {
        $rootScope.setTitle("我的心愿单");
        var userInfo = localStorageService.get(KEY_USERINFO);
        $scope.isLoading = false;

        $scope.doGetMore = function () {
            if ($scope.goodsList && $scope.goodsList.length > 0) {
                var lastUpdateTime = $scope.goodsList[$scope.goodsList.length - 1].updateTime;
                $scope.getData(lastUpdateTime);
            } else {
                $scope.getData(null);
            }

        };
        $scope.getData = function (lastUpdateTime) {
            if ($scope.isLoading) {
                return;
            }
            $scope.isLoading = true;

            var param = {
                memberId: userInfo.memberId,
                type: "10",
                lastUpdateTime: lastUpdateTime
            };
            new favoriteService(param).getList()
                .then(function (res) {
                    $scope.isLoading = false;
                    if (res.resultCode == "00") {
                        if (lastUpdateTime) {
                            $scope.goodsList = $scope.goodsList.concat(res.results);
                        } else {
                            $scope.goodsList = res.results;
                        }
                        if (res.results.length < 10) {
                            $scope.hasMore = false;
                        } else {
                            $scope.hasMore = true;
                        }
                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function (error) {
                    $scope.isLoading = false;
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
        };

        $scope.removeFavorite = function (index) {
            var collectNo = $scope.goodsList[index].collectNo;
            var param = {
                memberId: userInfo.memberId,
                collectNo: collectNo
            };
            new favoriteService(param).deleteFavorite()
                .then(function (res) {
                    if ('00' == res.resultCode) {
                        $scope.goodsList.splice(index, 1);
                        popupService.showToast("取消成功");
                    }
                }, function (error) {
                    alert(error);
                });

        };

        $scope.goProdocutDetail = function (type) {
            $state.go("goodsDetail", {"type": type});
        };

        $scope.getData(null);
    })
    // 我的心愿单 end
    // 个人中心 start
    .controller('personalCenterCtrl', function ($scope, $rootScope, $state, $ionicHistory,
                                                popupService,
                                                $ionicTabsDelegate, localStorageService, orderService, personalService, $timeout) {
        $rootScope.setTitle("个人中心");

        var userInfo = localStorageService.get(KEY_USERINFO);
        if (userInfo) {
            $scope.personal = userInfo;
        }

        //进入页面后初始化页面
        $scope.$on('$ionicView.enter', function () {
            $scope.init();
        });

        $scope.init = function () {

            $scope.receiveCount = 0;
            $scope.notDeliverCount = 0;
            $scope.notCount = 0;

            var info = localStorageService.get(KEY_USERINFO);
            if (info) {
                var param = {
                    memberId: info.memberId
                };
                new orderService().getOrderCountByMemberId(param)
                    .then(function (res) {
                        if (res.resultCode == '00') {
                            console.log(res);
                            $scope.waitPayCount = res.results.waitPayCount;
                            $scope.waitDeliveryCount = res.results.waitDeliveryCount;
                            $scope.waitPayCount = res.results.waitPayCount;
                            $scope.waitReceiveCount = res.results.waitReceiveCount;
                        }
                    }, function () {
                        popupService.showToast(commonMessage.networkErrorMsg);
                    });
            }
        };

        $scope.goPersonal = function () {

            if (!userInfo) {
                $state.go("binding", {});
                return;
            }
            $state.go("personal", {});

        };

        $scope.modifyHeaderImg = function () {
            if (!userInfo) {
                $state.go("binding", {});
                return;
            }
            wx.chooseImage({
                count: 1, // 默认9
                sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
                sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
                success: function (res) {
                    // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
                    $scope.personal.memberPic = res.localIds[0];
                    $timeout(function () {
                        $scope.uploadImage(res.localIds[0]);
                    }, 500);
                }, fail: function (data) {
                    popupService.showToast("fail" + data.errMsg);
                }
            });
        };

        $scope.uploadImage = function (localId) {
            wx.uploadImage({
                localId: localId, // 需要上传的图片的本地ID，由chooseImage接口获得
                isShowProgressTips: 1, // 默认为1，显示进度提示
                success: function (res) {
                    var serverId = res.serverId; // 返回图片的服务器端ID
                    $scope.modifyMemberAvatarFromWX(serverId);
                }, fail: function (data) {
                    PopupService.showToast("fail:" + data.errMsg);
                }
            });
        };

        $scope.modifyMemberAvatarFromWX = function (serverId) {
            var param = {
                memberId: userInfo.memberId,
                serverId: serverId
            }
            new personalService(param).modifyMemberAvatarFromWX()
                .then(function (res) {
                    if (res.resultCode == '00') {
                        $scope.personal.memberPic = res.url;
                        userInfo.memberPic = res.url;
                        localStorageService.set(KEY_USERINFO, userInfo);
                        $scope.compressImage(res.imageId);
                    } else {
                        popupService.showToast("上传失败！" + res.resultMessage);
                    }
                }, function () {
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
        }

        $scope.compressImage = function (id) {
            var param = {
                type: "CMS_MASTER",
                id: id,
                suffix: "jpg"
            }
            new personalService(param).compressImage()
                .then(function (res) {
                    if (res.resultCode == '00') {
                        popupService.showToast("修改成功！");
                    }
                }, function () {
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
        }

        $scope.goOrder = function (index) {

            if (!userInfo) {
                $state.go("binding", {});
                return;
            }

            switch (index) {
                case 1:
                    $state.go("orderList", {tabFlg: 2});
                    break;
                case 2:
                    $state.go("orderList", {tabFlg: 3});
                    break;
                case 3:
                    $state.go("orderList", {tabFlg: 4});
                    break;
                case 4:
                    $state.go("orderList", {tabFlg: 5});
                    break;
                default:
                    $state.go("orderList", {tabFlg: 1});
            }
        };

        $scope.goCoupon = function () {
            if (!userInfo) {
                $state.go("binding", {});
                return;
            }
            $state.go("couponList", {});
        };

        $scope.goFavorite = function () {
            if (!userInfo) {
                $state.go("binding", {});
                return;
            }
            $state.go("favorite", {});
        };

        $scope.goAddress = function () {
            if (!userInfo) {
                $state.go("binding", {});
                return;
            }
            $state.go("addressSelect", {});
        };

        $scope.goBinding = function () {
            $state.go("binding", {});
        };
        $scope.goPointExchange = function () {
            $state.go("pointExchange", {});
        };

        $scope.goOrderListOffline = function () {
            $state.go("orderListOffline", {});
        };

    })
    // 个人中心 end
    // 首页 start
    .controller('homePageCtrl', function ($scope, $location, $state, $ionicTabsDelegate, localStorageService,
                                          indexFigureService, indexNewsService, indexNews1Service
        , indexArrivalsService, indexNewGoodsService, indexGdsBrandTypeService,
                                          indexRegion2Service, indexRegion2_1Service, indexRegion3PhoneService, indexRegion4Service, indexRegion5Service, indexRegion6Service, indexRegion7Service, indexRegion8Service,
                                          indexGdsSortsService, gdsTypeService, $ionicSlideBoxDelegate, couponAllService, popupService, $rootScope, $ionicScrollDelegate) {
        $rootScope.setTitle("首页");
        var userInfo = localStorageService.get(KEY_USERINFO);
        console.log($location.absUrl());
        sessionStorage.setItem('memberIdForWx', $location.absUrl());
        $scope.homePageInfo = {search: ''};
        $rootScope.goodsTypeId = '';
        $rootScope.actId = '';
        $rootScope.firstGoodsTypeId = '';
        $rootScope.searchKey = '';
        $rootScope.goodsId = '';
        $rootScope.pageType = '';
        $rootScope.classifyId = '';
        $rootScope.classifyName = '';

        // add by pcd start

        // 轮播图
        new indexFigureService({})
            .then(function (res) {
                if (res.resultCode == "00") {
                    $scope.indexFigureData = res.results;
                    console.log($scope.indexFigureData);
                    $ionicSlideBoxDelegate.update();
                } else {
                    popupService.showToast(res.resultMessage);
                }
            }, function () {
                popupService.showToast(commonMessage.networkErrorMsg);
            });
        // indexFigureService.then(function(res){
        //     $scope.indexFigureData = res;
        // });

        // 新闻部分
        new indexNewsService({})
            .then(function (res) {
                if (res.resultCode == "00") {
                    $scope.indexNewsData = res.results;
                } else {
                    popupService.showToast(res.resultMessage);
                }
            }, function (error) {
                popupService.showToast(commonMessage.networkErrorMsg);
            });
        // indexNewsService.then(function(res){
        //     $scope.indexNewsData = res;
        // });

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

        // 模版2
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


        // 活动
        new indexArrivalsService({})
            .then(function (res) {
                if (res.resultCode == "00") {
                    $scope.indexArrivalsData = res.results;
                } else {
                    popupService.showToast(res.resultMessage);
                }
            }, function (error) {
                popupService.showToast(commonMessage.networkErrorMsg);
            });
        // indexArrivalsService.then(function(res){
        //     $scope.indexArrivalsData = res;
        // });

        // 新品推荐
        new indexNewGoodsService({})
            .then(function (res) {
                if (res.resultCode == "00") {
                    $scope.indexNewGoodsData = res.results;
                } else {
                    popupService.showToast(res.resultMessage);
                }
            }, function (error) {
                popupService.showToast(commonMessage.networkErrorMsg);
            });

        // 商品分类
        new indexGdsSortsService({})
            .then(function (res) {
                if (res.resultCode == "00") {
                    $scope.indexGdsSortsData = res.results;
                    new indexGdsBrandTypeService({})
                        .then(function (resp) {
                            if (resp.resultCode == "00") {
                                $scope.indexGdsBrandTypeData = resp.results;
                            } else {
                                popupService.showToast(resp.resultMessage);
                            }
                        }, function (error) {
                            popupService.showToast(commonMessage.networkErrorMsg);
                        });
                } else {
                    popupService.showToast(res.resultMessage);
                }
            }, function (error) {
                popupService.showToast(commonMessage.networkErrorMsg);
            });

        // add by pcd end


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
                if (res.resultCode == "00" && res.results != null && res.results != undefined) {
                    $scope.indexRegion2_1Data = res.results[0];
                } else {
                    // popupService.showToast(res.resultMessage);
                }
            }, function (error) {
                // popupService.showToast(commonMessage.networkErrorMsg);
            });

        //add by cky start
        gdsTypeService.then(function (res) {
            if (res.resultCode == "00") {
                $scope.gdsTypeData = res.results;
            } else {
                popupService.showToast(res.resultMessage);
            }
        }, function (error) {
            popupService.showToast(commonMessage.networkErrorMsg);
        });
        // add by cky end

        //add by cjk start
        var memberId = "";
        if (userInfo != null) {
            memberId = userInfo.memberId;
        }
        new couponAllService({"memberId": memberId})
            .then(function (res) {
                if (res.resultCode == "00") {
                    $scope.couponData = res.results;
                } else {
                    popupService.showToast(res.resultMessage);
                }
            }, function (error) {
                popupService.showToast(commonMessage.networkErrorMsg);
            });

        //add by cjk end
        $scope.goPersonalCenter = function () {
            $state.go("personalCenter", {});
        };
        $scope.goBag = function () {
            $state.go("bag", {});
        };
        // 点击首页轮播图
        $scope.goOtherPage = function (type, typeId, cmsId, title) {
            // 点击商品的时候
            if (type == 41) {
                var array = typeId.split(",");
                if (array != null && array.length > 1) {
                    $state.go("goodsListDisplay", {"type": typeId, "cmsId": cmsId, "title": title});
                } else if (array.length == 1) {
                    $state.go("goodsDetail", {"type": typeId});
                    //$state.go("pointExchange", {});
                }

                // 商品分类的时候
            } else if (type == 42) {
                $state.go("goodsList", {"firstGoodsTypeId": typeId});

                // 活动的时候
            } else if (type == 43) {
                $state.go("goodsList", {"actId": typeId, "title": title});
            } else if (type == 44) {
                //跳转新的活动画面
                $state.go("pointsExchangePageNew");
            } else if (type == 45) {
                //跳转积分兑换优惠劵画面
                $state.go("pointsCoupponPage");
            } else if (type == 46) {
                //跳转秒杀画面
                $state.go("secKill");
            } else if (type == 47) {
                //跳转抽奖画面
                $state.go("prizeDraw", {"prizeDrawId": typeId});
            }

        };

        // 首页点击到商品列表
        $scope.goGoodsList = function (type, typeId) {
            if (type == 41) {
                $state.go("goodsList", {});
            } else if (type == 42) {
                $state.go("goodsList", {"firstGoodsTypeId": typeId});
            } else if (type == 43) {
                $state.go("goodsList", {"actId": typeId});
            } else if (type == 44) {
                $state.go("classification", {"firstGoodsTypeId": typeId});
            }

        };
        // 跳转到商品详细
        $scope.goGoodsDetail = function (gdsId) {
            $state.go("goodsDetail", {"type": gdsId});
        };
        $scope.goCouponAll = function () {
            $state.go("couponAll", {});
        };
        $scope.goGoodsSearch = function () {
            $state.go("searchGoods", {"type": $scope.homePageInfo.search});
        };

        $scope.goTop = function () {
            $ionicScrollDelegate.$getByHandle("homePageScroll").scrollTop(true);
        };
        $scope.goActivityMore = function (activityName) {
            // $scope.homePageInfo.search = activityName;
            // $state.go("searchGoods", {"type": activityName});

            var type = $scope.indexFigureData[0].itemType;
            var typeId = $scope.indexFigureData[0].itemTypeVal;
            var cmsId = $scope.indexFigureData[0].cmsId;
            var title = $scope.indexFigureData[0].title;
            if (type == 41) {
                var array = typeId.split(",");
                if (array != null && array.length > 1) {
                    $state.go("goodsListDisplay", {"type": typeId, "cmsId": cmsId, "title": title});
                } else {
                    $state.go("goodsDetail", {"type": typeId});
                }

                // 商品分类的时候
            } else if (type == 42) {
                $state.go("goodsList", {"firstGoodsTypeId": typeId});

                // 活动的时候
            } else if (type == 43) {
                $state.go("goodsList", {"actId": typeId});
            }

        };
        // 分类区
        $scope.categoriesPhone = [
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

        //区域3手机
        new indexRegion3PhoneService({})
            .then(function (res) {
                if (res.resultCode == "00") {
                    $scope.indexRegion3PhoneData = res.results[0];
                    if ($scope.indexRegion3PhoneData.cmList != null) {
                        for (var i = 0; i < $scope.indexRegion3PhoneData.cmList.length; i++) {
                            var item = $scope.indexRegion3PhoneData.cmList[i];
                            var json = {};
                            json.typeId = item.itemTypeVal.split('_')[0];
                            json.pageType = item.comment;
                            json.imageUrl = item.imageUrl;
                            json.classifyId = item.itemTypeVal;
                            json.classifyName = item.textComment;
                            $scope.categoriesPhone.push(json);
                        }
                        $scope.categoriesDescribe = ['女士', '男士', '童装', '品牌'];

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
        //
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


        $scope.goGoodsList2 = function (typeId, pageType, classifyId, classifyName) {
            if (pageType != '3') {
                $state.go("goodsList", {
                    "firstGoodsTypeId": typeId,
                    "pageType": pageType,
                    "classifyId": classifyId,
                    "classifyName": classifyName
                });
            } else {
                $state.go("goodsListDisplay", {"type": typeId, "cmsId": '', "title": ''});
            }
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
        //$scope.suitPrice=[];

        // 区域8
        new indexRegion8Service({})
            .then(function (res) {
                if (res.resultCode == "00") {
                    $scope.indexRegion8Data = res.results[0];
                    if ($scope.indexRegion8Data.cmList != null) {

                        $scope.titleImg = $scope.indexRegion8Data.cmList[0].imageUrl;
                        for (var i = 1; i < $scope.indexRegion8Data.cmList.length; i++) {
                            var item = $scope.indexRegion8Data.cmList[i];
                            var json = {};
                            json.goodsId = item.itemTypeVal;
                            json.imageUrl = item.imageUrl;
                            json.tops = item.tops;
                            json.pants = item.pants;
                            $scope.matches.push(json);
                        }

                        // add by sunt for 8.首页搭配要加价格 start
                        //$scope.suitPrice = [
                        //    ['短袖T ￥111.30','针织短裤 ￥167.30'],
                        //    ['POLO短袖 ￥249.00','九分裤 ￥244.30'],
                        //    ['长袖衬衫 ￥244.30','短裤 ￥299.00'],
                        //    ['短袖T ￥139.30','短裤 ￥195.30'],
                        //    ['短袖衬衫 ￥299.00','短裤 ￥349.00'],
                        //    ['短袖T ￥199.00','慢跑裤 ￥244.30'],
                        //    ['POLO短袖 ￥139.30','慢跑裤 ￥195.30'],
                        //    ['POLO短袖 ￥209.30','针织短裤 ￥244.30'],
                        //    ['短袖T ￥199.00','短裤 ￥299.00']
                        //]
                        //
                        //$scope.suitPrice = [
                        //    ['短袖T ￥79.50','针织短裤 ￥119.50'],
                        //    ['POLO短袖 ￥174.30','九分裤 ￥244.30'],
                        //    ['长袖衬衫 ￥174.50','短裤 ￥209.30'],
                        //    ['短袖T ￥99.50','短裤 ￥139.50'],
                        //    ['短袖衬衫 ￥149.50','短裤 ￥244.30'],
                        //    ['短袖T ￥139.30','慢跑裤 ￥244.30'],
                        //    ['POLO短袖 ￥99.50','慢跑裤 ￥139.50'],
                        //    ['POLO短袖 ￥149.50','针织短裤 ￥174.50'],
                        //    ['短袖T ￥139.30','短裤 ￥209.30']
                        //]

                        $scope.suitPrice = [
                            ['上衣 ￥699.00', '长裤 ￥799.00'],
                            ['棉夹克 ￥399.00', '长裤 ￥299.00'],
                            ['上衣 ￥349.00', '长裤 ￥399.00'],
                            ['卫衣 ￥449.00', '慢跑裤 ￥499.00'],
                            ['上衣 ￥599.00', '长裤 ￥799.00'],
                            ['棉夹克 ￥749.00', '慢跑裤 ￥499.00'],
                            ['单西 ￥499', '长裤 ￥399.00'],
                            ['单夹克 ￥349.00', '慢跑裤 ￥299.00'],
                            ['风衣 ￥1299.00', '长裤 ￥799.00']
                        ]

                        // add by sunt for 8.首页搭配要加价格 end

                        angular.element(document).ready(function () {
                            var swipeBottomPhone = new Swiper('.swiper-bottom-phone', {
                                nextButton: '.swiper-button-next',
                                prevButton: '.swiper-button-prev',
                                slidesPerView: 3,
                                spaceBetween: 2,
                                freeMode: true,
                                loop: true
                            });

                            // md by sunt start
                            jQuery('.matchPop').off().click(function (e) {
                                var popW = jQuery(e.target).width();
                                var popH = jQuery(e.target).height();

                                var targetOffset = jQuery(e.target).offset();
                                var $cloneDom = jQuery(this).clone();
                                var popGoodsId = jQuery('.popHidden', $cloneDom).val();
                                jQuery('.popImg', $cloneDom).width(popW).height(popH).addClass('layerImg');
                                jQuery('.popFont', $cloneDom).show();

                                layer.open({
                                    type: 1,
                                    title: false, //不显示标题
                                    closeBtn: 0,
                                    anim: 5,
                                    shadeClose: true,
                                    area: [popW, popH], //宽高
                                    scrollbar: false,
                                    offset: [targetOffset.top - jQuery(document).scrollTop() + 'px', targetOffset.left + 'px'],

                                    content: $cloneDom.html(), //捕获的元素，注意：最好该指定的元素要存放在body最外层，否则可能被其它的相对元素所影响
                                });
                                jQuery('.layerImg').click(function () {
                                    var array = popGoodsId.split(",");
                                    if (array != null && array.length > 1) {
                                        $state.go("goodsListCms", {"type": popGoodsId});
                                    } else if (array.length == 1) {
                                        $scope.goGoodsDetail(popGoodsId);
                                    }
                                    layer.closeAll();
                                });


                                return false;
                            });
                            // md by sunt end
                        });
                    }
                } else {
                    // popupService.showToast(res.resultMessage);
                }
            }, function (error) {
                // popupService.showToast(commonMessage.networkErrorMsg);
            });

    })
    // 首页 end

    // 秒杀测试
    .controller('secKillCtrl', ["$scope", "$rootScope", "$state", 'popupService', 'secKillImageService', '$interval', 'secKillService', 'localStorageService',
        function ($scope, $rootScope, $state, popupService, secKillImageService, $interval, secKillService, localStorageService) {
            $scope.clickIndex = 0;
            $rootScope.setTitle('秒杀');
            $scope.times = [];

            $scope.goodsList = [];
            //当前时间
            $scope.nowTime;
            $scope.timer;
            $scope.currentPage = 0;
            $scope.totalPage = 0;
            $scope.pageSize = 4;

            $scope.switchTm = function (index, time) {
                $scope.currentPage = 0;
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
                        if (res.results != null && res.results.length > 0) {
                            $scope.activityPoints = res.results[0];
                            if ($scope.activityPoints.cmList != null) {
                                $scope.playbill = $scope.activityPoints.cmList[0];
                            }
                        }
                    } else {
                        // popupService.showToast(res.resultMessage);
                    }
                }, function (error) {
                    // popupService.showToast(commonMessage.networkErrorMsg);
                });

            $scope.getAllTimes = function () {

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
                            if (started == '0') {
                                // 结束时间减去nowTime + 1000/s
                                var diffTime = $scope.diffTime(endTime, currentTime);
                                console.log(diffTime);
                                $scope.activityTime = '抢购中 距结束:' + diffTime;
                                // 1s钟加1000
                                $scope.timer = $interval(function () {
                                    var rediffTime = $scope.reDiffTime(endTime, $scope.nowTime);
                                    $scope.activityTime = '抢购中 距结束:' + rediffTime;
                                    console.log(rediffTime);
                                }, 1000);
                            } else {
                                // 结束时间减去nowTime + 1000/s
                                var diffTime = $scope.diffTime(startTime, currentTime);
                                console.log(diffTime);
                                $scope.activityTime = '即将开始 距开始:' + diffTime;
                                // 1s钟加1000
                                $scope.timer = $interval(function () {
                                    var rediffTime = $scope.reDiffTime(startTime, $scope.nowTime);
                                    $scope.activityTime = '即将开始 距开始:' + rediffTime;
                                    console.log(rediffTime);
                                }, 1000);
                            }

                            if ($scope.times && $scope.times.length > 0) {
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

            $scope.diffTime = function (endTime, nowTime) {

                var diff = endTime - nowTime;//时间差的毫秒数

                //计算出相差天数
                var days = Math.floor(diff / (24 * 3600 * 1000));

                //计算出小时数
                var leave1 = diff % (24 * 3600 * 1000);    //计算天数后剩余的毫秒数
                var hours = Math.floor(leave1 / (3600 * 1000));
                //计算相差分钟数
                var leave2 = leave1 % (3600 * 1000);        //计算小时数后剩余的毫秒数
                var minutes = Math.floor(leave2 / (60 * 1000));

                //计算相差秒数
                var leave3 = leave2 % (60 * 1000);      //计算分钟数后剩余的毫秒数
                var seconds = Math.round(leave3 / 1000);

                var returnStr = seconds + "秒";
                if (minutes > 0) {
                    returnStr = minutes + "分" + returnStr;
                }
                if (hours > 0) {
                    returnStr = hours + "小时" + returnStr;
                }
                if (days > 0) {
                    returnStr = days + "天" + returnStr;
                }
                return returnStr;
            };

            $scope.reDiffTime = function (endTime, nowTime) {
                $scope.nowTime = nowTime + 1000;
                //如果当前时间和下一个的开始时间一致要刷新
                var startTime;
                if ($scope.times[1]) {
                    startTime = parseInt($scope.times[1].startTimeSec);
                    if ($scope.nowTime > startTime) {
                        $scope.getAllTimes();
                    }
                }

                var rediffTime = $scope.diffTime(endTime, nowTime);
                return rediffTime;
            };

            $scope.isLoading = false;
            $scope.getGoods = function (time) {
                var userInfo = localStorageService.get(KEY_USERINFO);
                var memberId = "";
                if (userInfo) {
                    memberId = userInfo.memberId;
                }

                var param = {
                    "memberId": memberId,
                    "activityTime": time.startTime,
                    "page": $scope.currentPage,
                    "displayLength": $scope.pageSize
                };

                if ($scope.isLoading) {
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
                                item.actInfo.sold = item.actInfo.quantity - item.actInfo.surplus;
                                if (item.actInfo.sold == 0) {
                                    item.actInfo.ratio = 0;
                                } else {
                                    item.actInfo.ratio = ((item.actInfo.quantity - item.actInfo.surplus) / item.actInfo.quantity) + 0.1;
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
                return {"width": radio * 100 + "%"};
            };

            //商品详细画面跳转
            $scope.goProdocutDetail = function (type) {
                $state.go("goodsDetail", {"type": type});
            };

            $scope.getAllTimes();

            //加载更多
            $scope.doGetMore = function () {
                if ($scope.currentPage >= $scope.totalPage) {
                    popupService.showToast('没有更多的数据了!');
                    return;
                }
                $scope.getGoods($scope.times[$scope.clickIndex]);
            }

        }])

    // 分类 start
    .controller('classificationCtrl', function ($scope, $rootScope, $state, $ionicTabsDelegate, localStorageService, activityTypeListService, getAlltypesService, brandtypeService, secondGdsTypeService, subGdsTypeService, secondGdsBrandTypeService, subGdsBrandTypeService, popupService) {
        //new classificationService({}).then(function(res){
        //    $scope.catalogs = res;
        //    $scope.setSelectedChildren($scope.catalogs[0],0);
        //},function(err){
        //    //TODO
        //});
        // $scope.selectedRow = 0;
        $rootScope.setTitle("分类");
        //add by cky start


        var userInfo = localStorageService.get(KEY_USERINFO);
        var memberId = userInfo == null ? "" : userInfo.memberId;
        $scope.levelRootList = [];
        $scope.activityTypeList = [];
        //memberId = '45d60b8e-ad7a-4227-8e82-aab5e267c67d';
        if (memberId && memberId.length > 0) {
            // 获取该用户下的所有活动
            new activityTypeListService({"memberId": memberId})
                .then(function (res) {
                    if (res.resultCode == "00") {
                        $scope.activityTypeList = res.data;
                        console.log($scope.activityTypeList);
                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function () {
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
        }

        // 所有的分类信息取得
        new getAlltypesService({})
            .then(function (res) {
                if (res.resultCode == "00") {
                    $scope.levelRootList = $scope.levelRootList.concat(res.results);
                    // 所有的品牌系列信息取得
                    new brandtypeService({})
                        .then(function (res) {
                            if (res.resultCode == "00") {
                                $scope.levelRootList = res.results.concat($scope.levelRootList);
                                console.log($scope.levelRootList);
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

        $jq('#actList').hide();
        //点击分类一级菜单跳转的方法
        $scope.displayNext = function (type) {
            for (var index = 0; index < $scope.levelRootList.length; index++) {
                var goodsTypeId = $scope.levelRootList[index].goodsTypeId;
                if (type == goodsTypeId) {
                    if ($scope.levelRootList[index].goodsTypeId=='6acd07b1-e0c7-4661-85dd-6044373f370e'){
                        if($scope.levelRootList[index].secondTypeList.length == 1){
                            $scope.levelRootList = $scope.levelRootList[index].secondTypeList[0].secondTypeList;
                            if ($scope.levelRootList == null || $scope.levelRootList.length == 0) {
                                $scope.goGoodsList(type);
                            }
                        }
                    }else{
                        $scope.goGoodsList(type);
                    }
                }
            }
            $jq('#actTitle').hide();
            $jq('#actList').hide();
        };

        $scope.displayNextAct = function () {
            $jq('#actTitle').hide();
            $jq('#actList').show();
            $jq('#typeList').hide();
        };

        //var curSubIndex = '0+0';
        //new secondGdsTypeService({}).then(function (res) {
        //    if (res.resultCode == "00") {
        //        $scope.categoryList = res.results;
        //
        //        new secondGdsBrandTypeService({}).then(function (res) {
        //            if (res.resultCode == "00") {
        //                $scope.categoryList = res.results.concat($scope.categoryList);
        //                $jq('#rightScrollArea').height($jq('#rightArea').height());
        //                ionic.DomUtil.ready(function () {
        //                    $jq(function() {
        //                        var Accordion = function(el, multiple) {
        //                            this.el = el || {};
        //                            this.multiple = multiple || false;
        //
        //                            // Variables privadas
        //                            var links = this.el.find('.link');
        //                            // Evento
        //                            links.on('click', {el: this.el, multiple: this.multiple}, this.dropdown)
        //                        }
        //
        //                        Accordion.prototype.dropdown = function(e) {
        //                            var $el = e.data.el;
        //                            $this = $jq(this),
        //                                $next = $this.next();
        //
        //                            $next.slideToggle();
        //                            $this.parent().toggleClass('open');
        //
        //                            if (!e.data.multiple) {
        //                                $el.find('.submenu').not($next).slideUp().parent().removeClass('open');
        //                            };
        //                        }
        //                        var accordion = new Accordion($jq('#accordion'), false);
        //
        //                        // init
        //                        var defaultParent = $jq('#accordion .link:eq(1)');
        //                        defaultParent.trigger('click');
        //                        $scope.setSubChildren($scope.categoryList[1].secondTypeList[0],0,0);
        //                    });
        //                });
        //            } else {
        //                popupService.showToast(res.resultMessage);
        //            }
        //        }, function (err) {
        //            popupService.showToast(commonMessage.networkErrorMsg);
        //        });
        //    } else {
        //        popupService.showToast(res.resultMessage);
        //    }
        //}, function (err) {
        //    popupService.showToast(commonMessage.networkErrorMsg);
        //});
        //$scope.setClass = function(index,parentIndex){
        //    if(curSubIndex == (parentIndex + '+' + index)){
        //        return 'selected';
        //    }else{
        //        return '';
        //    }
        //};
        //
        //$scope.initSub = function(catalog, index){
        //    if(index == 0){
        //        $scope.setSubBrandChildren($scope.categoryList[index].secondTypeList[0],0,0);
        //    }else{
        //        $scope.setSubChildren($scope.categoryList[index].secondTypeList[0],0,0);
        //    }
        //};
        //
        //$scope.setSubChildren = function (catalog, index,parentIndex) {
        //    curSubIndex = parentIndex + '+' + index;
        //    $scope.selectedRow = index;
        //    if(catalog != null){
        //        new subGdsTypeService({"goodsTypeId": catalog.goodsTypeId}).then(function (res) {
        //            if (res.resultCode == "00") {
        //                $scope.subGdsTypes = res.results;
        //            } else {
        //                popupService.showToast(res.resultMessage);
        //            }
        //        }, function (err) {
        //            popupService.showToast(commonMessage.networkErrorMsg);
        //        });
        //    }
        //};
        //$scope.setSubBrandChildren = function (catalog, index,parentIndex) {
        //    curSubIndex = parentIndex + '+' + index;
        //    $scope.selectedRow = index;
        //    if(catalog != null){
        //        new subGdsBrandTypeService({"goodsTypeId": catalog.goodsTypeId}).then(function (res) {
        //            if (res.resultCode == "00") {
        //                $scope.subGdsTypes = res.results;
        //            } else {
        //                popupService.showToast(res.resultMessage);
        //            }
        //        }, function (err) {
        //            popupService.showToast(commonMessage.networkErrorMsg);
        //        });
        //    }
        //};
        ////add by cky start
        //
        //var tabWidth = (document.body.clientWidth * 0.75 - 32) / 3;
        //$scope.tabStyle = {width: tabWidth + "px"};

        //$scope.setSelectedChildren = function (catalog, index) {
        //    $scope.children = catalog.child;
        //    $scope.selectedRow = index;
        //    //$ionicScrollDelegate.$getByHandle('rightScroll').resize();
        //};
        $scope.goGoodsList = function (type) {
            $state.go("goodsList", {"firstGoodsTypeId": type});
        };

        $scope.goSaleList = function (type) {
            $state.go("saleList", {"type": type});
        };
    })
    // 分类 end
    // 全部优惠券 start
    .controller('couponAllCtrl', function ($scope, $rootScope, $state, $ionicTabsDelegate, localStorageService, couponAllService, WeiXinService, getCouponService, popupService) {
        $rootScope.setTitle("全部优惠劵");
        $scope.couponList = [];

        var userInfo = localStorageService.get(KEY_USERINFO);

        $scope.doRefresh = function () {
            $scope.couponList = [];
            $scope.getData();
        };

        $scope.getData = function () {
            var param = {
                "memberId": userInfo == null ? "" : userInfo.memberId
            };
            new couponAllService(param)
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

        $scope.getCoupon = function (couponId, distributeType, price) {
            if (!userInfo || !userInfo.memberId) {
                $state.go("binding", {});
                return;
            }
            new getCouponService({"memberId": userInfo.memberId, "couponId": couponId})
                .then(function (res) {
                    if (res.resultCode == "00") {
                        if (distributeType == "60") {
                            // 现金购买
                            $scope.getWXPayInfo(res.result, price);
                        } else {
                            //popupService.showToast("领取成功!");
                            popupService.showAlert("提示", "领取成功!");
                        }
                        $scope.doRefresh();
                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function (error) {
                    popupService.showToast(commonMessage.networkErrorMsg);
                });

        };

        //支付
        $scope.getWXPayInfo = function (orderCode, price) {
            var params = {
                "openid": $rootScope.openid,
                "body": "商品订单" + orderCode,
                "out_trade_no": orderCode,
                "total_fee": (parseFloat(price) * 100).toFixed(0),
                // "total_fee": "1",
                "trade_type": "JSAPI"
            };
            console.log(params);
            WeiXinService.getWxPayInfo(params).then(function (response) {
                if (response.resultCode == '00') {
                    $scope.chooseWXPay(response.results);
                } else {
                    popupService.showToast("支付失败,原因:" + response.resultMessage);
                }
            }, function (error) {
            });
        };

        $scope.chooseWXPay = function (result) {
            wx.chooseWXPay({
                timestamp: result.timeStamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
                nonceStr: result.nonceStr, // 支付签名随机串，不长于 32 位
                package: result.package, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
                signType: result.signType, // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
                paySign: result.paySign, // 支付签名
                success: function (res) {
                    $scope.clearRootScopeOfConfirmOrder();
                    // 支付成功后的回调函数
                    popupService.showToast("支付成功!");
                    $state.go("orderList", {"tabFlg": "3"});
                },
                cancel: function (res) {
                    $scope.clearRootScopeOfConfirmOrder();
                    // 支付成功后的回调函数
                    popupService.showToast("您取消了支付,可在订单列表中继续支付");
                    $state.go("orderList", {"tabFlg": "2"});
                },
                fail: function (res) {
                    $scope.clearRootScopeOfConfirmOrder();
                    // 支付成功后的回调函数
                    $state.go("orderList", {"tabFlg": "2"});
                }
            });

        };

        $scope.doRefresh();
    })
    // 全部优惠券 end

    //积分兑换 start
    .controller('pointExchangeCtrl', function ($scope, $rootScope, $state, $ionicTabsDelegate, localStorageService, couponAllService, getCouponService, currentPointsService, popupService, pointExchangeService) {
        $rootScope.setTitle("积分兑换");
        $scope.hasMore = true;
        $scope.couponList = [];

        var userInfo = localStorageService.get(KEY_USERINFO);

        $scope.doRefresh = function () {
            $scope.hasMore = true;
            $scope.couponList = [];
            $scope.getData(null);
        };

        $scope.doGetMore = function () {
            var lastUpdateTime = $scope.couponList[$scope.couponList.length - 1].updateTime;
            $scope.getData(lastUpdateTime);
        };
        $scope.getData = function (lastUpdateTime) {
            var param = {
                "memberId": userInfo == null ? "" : userInfo.memberId,
                "updateTime": lastUpdateTime
            };
            new pointExchangeService(param)
                .getPointExchangeCoupons()
                .then(function (res) {
                    if (res.resultCode == "00") {
                        $scope.couponList = $scope.couponList.concat(res.results);
                        if ($scope.couponList && $scope.couponList.length >= 10) {
                            $scope.hasMore = true;
                        } else {
                            $scope.hasMore = false;
                        }
                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function (error) {
                    $scope.hasMore = false;
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
        };

        $scope.getCoupon = function (couponId) {
            new getCouponService({"memberId": userInfo.memberId, "couponId": couponId})
                .then(function (res) {
                    if (res.resultCode == "00") {
                        popupService.showToast("领取成功!");
                        $scope.doRefresh();
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
                        if (res.data != null) {
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
        $scope.getCurrentPoints();
        $scope.doRefresh();
    })
    // 积分兑换 end


    // 积分兑换优惠劵
    .controller('pointsCoupponPageCtrl', function ($scope, $rootScope, $state, $ionicTabsDelegate, localStorageService, couponAllService, couponPointsService, currentPointsService, getCouponService, popupService, pointExchangeService) {

        var userInfo = localStorageService.get(KEY_USERINFO);
        $scope.couponList = [];
        $scope.point = userInfo.point;

        // 海报区域
        new couponPointsService({})
            .then(function (res) {
                if (res.resultCode == "00") {
                    if (res.results != null && res.results.length > 0) {
                        $scope.activityPoints = res.results[0];
                        if ($scope.activityPoints.cmList != null) {
                            $scope.playbill = $scope.activityPoints.cmList[0];
                        }
                    }
                } else {
                    popupService.showToast(res.resultMessage);
                }
            }, function (error) {
                popupService.showToast(commonMessage.networkErrorMsg);
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

            new getCouponService({"memberId": userInfo.memberId, "couponId": couponId})
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

        $scope.getCurrentPoints = function () {
            var param = {
                "memberId": userInfo == null ? "" : userInfo.memberId
            };
            new currentPointsService(param)
                .then(function (res) {
                    if (res.resultCode == "00") {
                        if (res.data != null) {
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
        $scope.getCurrentPoints();
        $scope.doRefreshCoupon();
    })


    // 商品详情 start
    .controller('productDetailCtrl', function ($scope, $state, $stateParams, $ionicTabsDelegate, $interval,
                                               $ionicPopover, localStorageService, productDetailService,
                                               popupService, $sce, shoppingBagService, favoriteService, $rootScope, confirmOrderService) {
        $rootScope.setTitle("商品详情");
        var userInfo = localStorageService.get(KEY_USERINFO);
        var memberId = "";
        if (userInfo) {
            memberId = userInfo.memberId;
        }

        var goodsId = "";
        if ($stateParams.type != null
            && $stateParams.type.length > 0) {
            $rootScope.goodsId = $stateParams.type;
            goodsId = $stateParams.type;
        } else {
            goodsId = $rootScope.goodsId;
        }

        //当前时间
        $scope.nowTime;
        $scope.timer;

        new productDetailService({"goodsId": goodsId, "memberId": memberId}).then(function (vresponse) {
            var res = vresponse.results;
            if (vresponse.resultCode == "00") {
                $scope.productDetailData = res;
                $scope.selectedCount = {
                    value: 1
                };
                if (res.type != "30") {
                    $scope.skuList = res.skulist;
                    $scope.skuImage = res.skulist[0].imgs[0];
                    $scope.skuImages = res.imageUrlJsonPc;
                    angular.element(document).ready(function () {
                        var swiper = new Swiper('.swiper-container-phone', {
                            pagination: '.swiper-pagination',
                            slidesPerView: 'auto',
                            centeredSlides: true,
                            initialSlide: 1,
                            paginationClickable: true,
                            spaceBetween: 10,
                            loop: true
                        });
                    });
                    $scope.skuPrice = res.skulist[0].price;
                    $scope.newCount = res.skulist[0].newCount;
                    $scope.activityPrice = res.skulist[0].activityPrice;
                    $scope.selectedColor = res.colorList[0].key;
                    $scope.selectedSize = res.sizeList[0].key;
                    $scope.introduceHtml = $sce.trustAsHtml(res.introduceHtml);
                    // 售后服务维护内容
                    $scope.goodsServeHtml = $sce.trustAsHtml(res.goodsServeHtml);

                    if (res.activityInfo) {
                        var nowTime = res.nowTime;
                        var startTime = res.activityInfo.startTime;
                        var endTime = res.activityInfo.endTime;

                        $scope.nowTime = nowTime;
                        // 结束时间减去nowTime + 1000/s
                        var diffTime = $scope.diffTime(endTime, nowTime);
                        $scope.activityTime = diffTime;
                        // 1s钟加1000
                        $scope.timer = $interval(function () {
                            var rediffTime = $scope.reDiffTime(endTime, $scope.nowTime);
                            $scope.activityTime = rediffTime;
                        }, 1000);
                    }
                    $jq('#countdown').hide();
                    $scope.refreshSkuPrice();

                    $ionicPopover.fromTemplateUrl('templates/productDetailPopover.html', {
                        scope: $scope
                    }).then(function (popover) {
                        $scope.popover = popover;
                        ionic.DomUtil.ready(function () {
                            $jq(popover.$el[0]).find('.bagNumSpinner').spinner({min: 1});
                        });
                    });

                    $scope.openMorePopover = function ($event, popupType) {
                        $scope.popover.show($event);
                        $scope.popupType = popupType;
                        // ionic.DomUtil.ready(function () {
                        //     $jq('.bagNumSpinner').spinner();
                        // });

                    };
                    $scope.$on('$ionicView.beforeLeave', function () {
                        $scope.popover.hide();
                    });


                    ionic.DomUtil.ready(function () {
                        $jq('.binding').bind("click", function (obj) {
                            $state.go("goodsDetail", {"type": $jq(obj)[0].target.id});
                        });
                    });

                    $scope.closePopover = function () {
                        $scope.popover.hide();

                    };

                    $scope.getColorClass = function (colorValue) {
                        if ($scope.selectedColor == colorValue) {
                            return "dark-bg light bordered-dark";
                        } else {
                            return "";
                        }
                    };
                    $scope.getSizeClass = function (sizeValue) {
                        if ($scope.selectedSize == sizeValue) {
                            return "dark-bg light bordered-dark";
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

                    $scope.introduceHtml = $sce.trustAsHtml(res.introduceHtml);
                    // 售后服务维护内容
                    $scope.goodsServeHtml = $sce.trustAsHtml(res.goodsServeHtml);

                    $scope.goodsList = res.goodsList;
                    var imgArray = new Array();
                    var selecedColorArray = new Array();
                    var selecedSizeArray = new Array();
                    var skuPriceArray = new Array();
                    var skuActivityPriceArray = new Array();
                    var selectSkuIdArray = new Array();
                    var skuNewCountArray = new Array();
                    var isExist = false;
                    for (var i = 0; i < res.goodsList.length; i++) {
                        imgArray.push(res.goodsList[i].skulist[0].imgs[0]);
                        skuPriceArray.push(res.goodsList[i].skulist[0].price);
                        skuNewCountArray.push(res.goodsList[i].skulist[0].newCount);
                        if (res.goodsList[i].skulist[0].newCount == null
                            || res.goodsList[i].skulist[0].newCount == '0') {
                            isExist = true;
                        }
                        skuActivityPriceArray.push(res.goodsList[i].skulist[0].activityPrice);
                        selecedColorArray.push(res.goodsList[i].colorList[0].key);
                        selecedSizeArray.push(res.goodsList[i].sizeList[0].key);
                        selectSkuIdArray.push(res.goodsList[i].skulist[0].skuid);
                    }
                    $scope.skuImage = imgArray;
                    $scope.skuPrice = skuPriceArray;
                    $scope.skuPriceActivityPrice = skuActivityPriceArray;
                    $scope.selectedColor = selecedColorArray;
                    $scope.selectedSize = selecedSizeArray;
                    $scope.selectedSkuId = selectSkuIdArray;
                    $scope.newCount = skuNewCountArray;
                    $scope.refreshSkuPriceForSuit(0);
                    //判断是否存在库存为空的数据的标记
                    if (isExist) {
                        $scope.isHasStore = "0";
                    } else {
                        $scope.isHasStore = "1";
                    }


                    $ionicPopover.fromTemplateUrl('templates/productDetailPopoverSuit.html', {
                        scope: $scope
                    }).then(function (popover) {
                        $scope.popover = popover;
                        ionic.DomUtil.ready(function () {
                            $jq(popover.$el[0]).find('.bagNumSpinner').spinner({min: 1});
                        });
                    });

                    $scope.openMorePopover = function ($event, popupType) {
                        $scope.popover.show($event);
                        $scope.popupType = popupType;

                    };
                    $scope.closePopover = function () {
                        $scope.popover.hide();

                    };
                    $scope.$on('$ionicView.beforeLeave', function () {
                        $scope.popover.hide();
                    });

                    $scope.getColorClass = function (index, colorValue) {

                        if (colorValue == $scope.selectedColor[index]) {
                            return "dark-bg light bordered-dark";
                        } else {
                            return "";
                        }
                    };
                    $scope.getSizeClass = function (index, sizeValue) {

                        if (sizeValue == $scope.selectedSize[index]) {
                            return "dark-bg light bordered-dark";
                        } else {
                            return "";
                        }

                    };

                    $scope.setColor = function (index, colorValue) {
                        $scope.selectedColor[index] = colorValue;
                        $scope.refreshSkuPriceForSuit(index);
                    };

                    $scope.setSize = function (index, sizeValue) {

                        $scope.selectedSize[index] = sizeValue;
                        $scope.refreshSkuPriceForSuit(index);
                    };

                    ionic.DomUtil.ready(function () {
                        $jq('.binding').bind("click", function (obj) {
                            $state.go("goodsDetail", {"type": $jq(obj)[0].target.id});
                        });
                    });
                }

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

        $scope.diffTime = function (endTime, nowTime) {

            var diff = endTime - nowTime;//时间差的毫秒数

            //计算出相差天数
            var days = Math.floor(diff / (24 * 3600 * 1000));

            //计算出小时数
            var leave1 = diff % (24 * 3600 * 1000);    //计算天数后剩余的毫秒数
            var hours = Math.floor(leave1 / (3600 * 1000));
            //计算相差分钟数
            var leave2 = leave1 % (3600 * 1000);        //计算小时数后剩余的毫秒数
            var minutes = Math.floor(leave2 / (60 * 1000));

            //计算相差秒数
            var leave3 = leave2 % (60 * 1000);      //计算分钟数后剩余的毫秒数
            var seconds = Math.round(leave3 / 1000);

            var returnStr = seconds + "秒";
            if (minutes > 0) {
                returnStr = minutes + "分" + returnStr;
            }
            if (hours > 0) {
                returnStr = hours + "小时" + returnStr;
            }
            if (days > 0) {
                returnStr = days + "天" + returnStr;
            }
            return returnStr;
        };

        $scope.reDiffTime = function (endTime, nowTime) {
            $scope.nowTime = nowTime + 1000;
            console.log(nowTime);
            var rediffTime = $scope.diffTime(endTime, nowTime);
            return rediffTime;
        };
        $scope.$on('$destroy', function () {
            $interval.cancel($scope.timer);
        });

        $scope.goProdocutDetail = function (type) {
            alert(type);
            $state.go("goodsDetail", {"type": type});
        };

        $scope.confirmPopover = function () {
            $scope.popover.hide();

            if (!userInfo) {
                $state.go("binding", {});
                return;
            }

            if ($scope.popupType == "shopcart") {
                // 加入购物车,提示加入成功
                $scope.addToShopBag();
            } else if ($scope.popupType == "buy") {
                // 立即购买.跳转到订单列表
                if ($scope.productDetailData.type != "30") {
                    if (parseInt($scope.newCount) < parseInt($scope.selectedCount.value)) {
                        popupService.showToast("库存不足.");
                        return;
                    }
                    var goodsInfo = {};
                    goodsInfo.goodsId = $scope.productDetailData.goodsId;
                    goodsInfo.type = $scope.productDetailData.type;
                    goodsInfo.skuId = $scope.selectedSkuId;
                    goodsInfo.quantity = $scope.selectedCount.value;

                    $scope.checkOrderConfirmBySingle(goodsInfo);

                } else {
                    var isCountAvalible = true;
                    angular.forEach($scope.newCount, function (count) {
                        if (parseInt(count) < $scope.selectedCount.value) {
                            isCountAvalible = false;
                            return false;
                        }
                    });
                    if (!isCountAvalible) {
                        popupService.showToast("库存不足.");
                        return;
                    }

                    var goodsInfo = {};
                    goodsInfo.goodsId = $scope.productDetailData.goodsId;
                    goodsInfo.type = $scope.productDetailData.type;
                    goodsInfo.quantity = $scope.selectedCount.value;
                    var skuList = [];
                    angular.forEach($scope.productDetailData.goodsList, function (sku, index) {
                        skuList.push({
                            "goodsId": sku.goodsId,
                            "goodsSkuId": $scope.selectedSkuId[index]
                        });
                    });
                    goodsInfo.skuList = skuList;

                    $rootScope.shoppingBagsOfConfirmOrder = undefined;
                    $rootScope.singleGoodsInfoOfConfirmOrder = undefined;
                    $rootScope.suitGoodsInfoOfConfirmOrder = undefined;

                    $state.go("confirmOrder", {suitGoodsInfo: goodsInfo});
                }

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
                if (res.resultCode == "00") {

                    $rootScope.shoppingBagsOfConfirmOrder = undefined;
                    $rootScope.singleGoodsInfoOfConfirmOrder = undefined;
                    $rootScope.suitGoodsInfoOfConfirmOrder = undefined;

                    $state.go("confirmOrder", {singleGoodsInfo: goodsInfo});
                } else {
                    popupService.showToast(res.resultMessage);
                }
            }, function (error) {
                popupService.showToast(commonMessage.networkErrorMsg);
            });
        };

        $scope.addToShopBag = function () {

            if (!userInfo) {
                $state.go("binding", {});
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
            var skuList = [];
            if ($scope.productDetailData.type != "30") {
                skuList.push({skuId: $scope.selectedSkuId});
                if (parseInt($scope.newCount) < parseInt($scope.selectedCount.value)) {
                    popupService.showToast("库存不足.");
                    return;
                }
            } else {
                var isCountAvalible = true;
                angular.forEach($scope.newCount, function (count) {
                    if (parseInt(count) < parseInt($scope.selectedCount.value)) {
                        isCountAvalible = false;
                        return false;
                    }
                });
                if (!isCountAvalible) {
                    popupService.showToast("库存不足.");
                    return;
                }


                angular.forEach($scope.productDetailData.goodsList, function (sku, index) {
                    skuList.push({
                        "skuId": $scope.selectedSkuId[index]
                    });
                });
            }
            var param = {
                memberId: userInfo.memberId,
                type: $scope.productDetailData.type,
                goodsId: $scope.productDetailData.goodsId,
                goodsName: $scope.productDetailData.goodsName,
                quantity: $scope.selectedCount.value,
                skuList: skuList
            };
            new shoppingBagService(param).add()
                .then(function (res) {
                    if (res.resultCode == "00") {
                        popupService.showToast("加入成功");
                    } else {
                        popupService.showToast("加入失败,原因:" + res.resultMessage);
                    }

                }, function (error) {
                    alert(error);
                });
        };

        $scope.setWishlist = function (value) {

            if (!userInfo) {
                $state.go("binding", {});
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

            for (var i = 0; i < $scope.skuList.length; i++) {
                var sku = $scope.skuList[i];
                if (sku.nameKeyValues[0].key == $scope.selectedColor && sku.nameKeyValues[1].key == $scope.selectedSize) {
                    $scope.skuPrice = sku.price;
                    //大于20 显示无货 20-50显示可能购买 50显示有货
                    $scope.newCountDisplay = 0;
                    if (parseInt(sku.nameKeyValues[1].bnk_no_limit) > parseInt(sku.newCount)) {
                        $scope.newCount = 0;
                    } else if (parseInt(sku.nameKeyValues[1].bnk_no_limit) < parseInt(sku.newCount)
                        && parseInt(sku.nameKeyValues[1].bnk_less_limit) > parseInt(sku.newCount)) {
                        $scope.newCount = sku.newCount;
                        $scope.newCountDisplay = 1;
                    } else {
                        $scope.newCount = sku.newCount;
                    }
                    //$scope.newCount = sku.newCount;
                    $scope.activityPrice = sku.activityPrice;
                    $scope.selectedSkuId = sku.skuid;
                    $scope.skuImage = sku.imgs[0];

                    $scope.productDetailData.activityName = sku.activityName;
                    $scope.productDetailData.minAndMaxPriceActivity = sku.activityPrice;
                    $scope.productDetailData.minAndMaxPrice = sku.price;
                    if (sku.activityType != null && sku.activityType == "11") {
                        $jq('#countdown').show();
                    } else {
                        $jq('#countdown').hide();
                    }
                    isExist = true;
                    //return false;
                    break;
                }
            }
            //console.log($scope.skuList);
            //angular.forEach($scope.skuList, function (sku) {
            //    if (sku.nameKeyValues[0].key == $scope.selectedColor && sku.nameKeyValues[1].key == $scope.selectedSize) {
            //        $scope.skuPrice = sku.price;
            //        //大于20 显示无货 20-50显示可能购买 50显示有货
            //        $scope.newCountDisplay = 0;
            //        if(parseInt(sku.nameKeyValues[1].bnk_no_limit) >  parseInt(sku.newCount)){
            //            $scope.newCount = 0;
            //        }else if(parseInt(sku.nameKeyValues[1].bnk_no_limit) <  parseInt(sku.newCount)
            //            && parseInt(sku.nameKeyValues[1].bnk_less_limit) >  parseInt(sku.newCount)){
            //            $scope.newCount = sku.newCount;
            //            $scope.newCountDisplay = 1;
            //        }else{
            //            $scope.newCount = sku.newCount;
            //        }
            //        //$scope.newCount = sku.newCount;
            //        $scope.activityPrice = sku.activityPrice;
            //        $scope.selectedSkuId = sku.skuid;
            //        console.log(sku);
            //        $scope.skuImage = sku.imgs[0];
            //        isExist = true;
            //        return false;
            //    }
            //});

            if (!isExist) {
                $scope.newCount = 0;
            }
        };

        $scope.refreshSkuPriceForSuit = function (index) {

            angular.forEach($scope.goodsList[index].skulist, function (sku) {
                if (sku.nameKeyValues[0].key == $scope.selectedColor[index] && sku.nameKeyValues[1].key == $scope.selectedSize[index]) {
                    $scope.skuPrice[index] = sku.price;
                    $scope.newCount[index] = sku.newCount;
                    $scope.skuPriceActivityPrice[index] = sku.activityPrice;
                    $scope.selectedSkuId[index] = sku.skuid;
                    $scope.skuImage[index] = sku.imgs[0];
                }
            });

            var isExist = false;
            jQuery('.storeCount').each(function () {
                if (jQuery(this).text().indexOf('没有库存') != -1) {
                    isExist = true;
                }
            });
            if (isExist) {
                $scope.isHasStore = "0";
            } else {
                $scope.isHasStore = "1";
            }
        }


    })
    // 商品详情 end

    // 账号绑定 start
    .controller('bindingCtrl', function ($scope, $state, $ionicHistory, localStorageService, popupService, authService, commonService, $rootScope) {
        $rootScope.setTitle("登录");
        $scope.bindingInfo = {
            tel: "",
            captcha: ""
        };

        $scope.getVerifyCode = function (isValid) {

            $scope.submitted_verifyCode = true;
            $scope.submitted_binding = false;

            if (!isValid) {
                //TODO 获取验证码check失败
            } else {

                // 发送验证码
                var param = {"mobile": $scope.bindingInfo.tel, "orgId": "20"};
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
            }
        };

        $scope.submitBinding = function (isValid) {
            $scope.submitted_verifyCode = true;
            $scope.submitted_binding = true;
            if (!isValid) {
                //TODO check 失败
            } else {
                var param = {
                    "openId": $rootScope.openid,
                    "telephone": $scope.bindingInfo.tel,
                    "captcha": $scope.bindingInfo.captcha
                };

                new authService(param).bingingWeiXin()
                    .then(function (res) {
                        if (res.resultCode == "00") {
                            localStorageService.set(KEY_USERINFO, res.data);
                            popupService.showAlert("提示", "绑定成功", function () {
                                //$ionicHistory.goBack();
                                $state.go('homePageTab.homePage', {});
                            });
                        } else {
                            popupService.showToast(res.resultMessage);
                        }
                    }, function (error) {
                        popupService.showToast(commonMessage.networkErrorMsg);
                    });
            }
        }
        $scope.onRegister = function () {
            $state.go('register', {})
        }
    })
    // 账号绑定 end

    //抽奖
    .controller('prizeDrawCtrl', function ($rootScope, $scope, $state, $stateParams, popupService, prizeDrawService, localStorageService) {

        var userInfo = localStorageService.get(KEY_USERINFO);
        //抽奖机会次数
        $scope.oppCount = 0;
        // 活动ID
        var prizeDrawId = "";
        if ($stateParams.prizeDrawId != null
            && $stateParams.prizeDrawId.length > 0) {
            $rootScope.prizeDrawId = $stateParams.prizeDrawId;
            prizeDrawId = $stateParams.prizeDrawId;
        } else {
            prizeDrawId = $rootScope.prizeDrawId;
        }
        // 会员ID
        var memberId = userInfo.memberId;

        $scope.getImageService = function () {

            new prizeDrawService().getImageService()
                .then(function (res) {
                    if (res.results != null && res.results.length > 0) {
                        $scope.activityPoints = res.results[0];
                        if ($scope.activityPoints.cmList != null) {
                            for (var i = 0; i < $scope.activityPoints.cmList.length; i++) {
                                var item = $scope.activityPoints.cmList[i];
                                if (prizeDrawId == item.itemTypeVal) {
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

        $scope.getPrizeDraw = function () {
            var param = {
                prizeDrawId: prizeDrawId,
                memberId: memberId
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
                        if ($scope.oppCount == 0 ||
                            res.result.prizeDraw.currentTime > res.result.prizeDraw.endTime
                            || res.result.prizeDraw.currentTime < res.result.prizeDraw.startTime
                            || res.result.prizeDraw.isValid == '0') {
                            //未开始
                            $jq('.gb-turntable-btn').addClass("disabled");
                        } else {
                            //正在开始
                            $jq('.gb-turntable-btn').removeClass("disabled");
                        }

                        //积分兑换的信息
                        $scope.exchangeFlag = res.result.prizeDraw.exchangeFlag;
                        $scope.exchangePoint = res.result.prizeDraw.exchangePoint;

                        //订单的信息
                        $scope.isOrder = res.result.prizeDraw.isOrder;
                        $scope.orderAmount = res.result.prizeDraw.orderAmount;

                        //抽奖纪录
                        $scope.winList = res.result.winList;

                        if ($scope.phoneAndPC == 'none') {
                            angular.element(document).ready(function () {
                                $scope.scroll('list1', 2000, 1, 20);//停留时间，相对速度（越小越快）,每次滚动多少，最好和Li的Line-height一致
                            });
                        } else {
                            angular.element(document).ready(function () {
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

        $scope.exchangePrizeWithPoint = function () {
            var param = {
                prizeDrawId: prizeDrawId,
                memberId: memberId
            };

            new prizeDrawService(param).exchangePrizeWithPoint()
                .then(function (res) {
                    if (res.resultCode == "00") {
                        popupService.showAlert("提示", "兑换成功。", function () {
                            $scope.getPrizeDraw();
                        });

                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function () {
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
        };


        $scope.getWinningList = function () {
            var param = {
                prizeDrawId: prizeDrawId
            };

            new prizeDrawService(param).getWinningList()
                .then(function (res) {
                    if (res.resultCode == "00") {
                        $scope.allRecord = res.result;
                        angular.element(document).ready(function () {
                            $scope.scroll('list-phone', 2000, 1, 20);//停留时间，相对速度（越小越快）,每次滚动多少，最好和Li的Line-height一致
                        });

                    } else {
                        popupService.showToast(res.resultMessage);
                    }
                }, function () {
                    popupService.showToast(commonMessage.networkErrorMsg);
                });
        };

        $scope.getPrizeGoodsList = function () {
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

        $scope.initPanel = function (prizeGoodsList) {
            console.log(prizeGoodsList);
            var prizes = ['谢谢参与'];
            angular.forEach(prizeGoodsList, function (prizeGoods) {
                prizes.push(prizeGoods.prizeGoodsName);
            });
            angular.element(document).ready(function () {
                var container;
                var turntableId;
                container = document.getElementById("turntable-container-phone");
                turntableId = 'turntable-phone';
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
                            userId: userInfo.memberId
                        };
                        new prizeDrawService(param).prizeDraw()
                            .then(function (res) {
                                if (res.resultCode == "00") {
                                    if (res.result && res.result != "") {
                                        console.log(res.result);
                                        var index = $scope.getPrizeIndex(res.result, prizeGoodsList);
                                        callback && callback([index, 1]);
                                    }
                                } else {
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
                    gotBack: function (data, id) {
                        popupService.showAlert("提示", "恭喜抽中" + data, function () {
                            $scope.getPrizeDraw();
                        });
                    }
                });
            });
        }

        $scope.getPrizeIndex = function (prizeGoodsId, prizeGoodsList) {
            var index = 0;
            angular.forEach(prizeGoodsList, function (prizeGoods, i) {
                if (prizeGoods.prizeGoodsId == prizeGoodsId) {
                    index = i + 1;
                }
            });
            return index;
        };

        $scope.scroll = function (element, delay, speed, lineHeight) {
            var numpergroup = 1;
            var slideBox = (typeof element == 'string') ? document.getElementById(element) : element;
            if (slideBox == null) {
                return;
            }
            angular.element(document).ready(function () {
                var delay = delay || 1000;
                var speed = speed || 20;
                var lineHeight = lineHeight || 20;
                var tid = null, pause = false;
                var liLength = slideBox.getElementsByTagName('li').length;

                var heightPx = $jq(slideBox).css('height');
                var height = heightPx.substr(0, heightPx.length - 2);
                if (height >= liLength * 20) {
                    return;
                }

                var lack = numpergroup - liLength % numpergroup;
                for (i = 0; i < lack; i++) {
                    //slideBox.appendChild(document.createElement("li"));
                }
                var start = function () {
                    tid = setInterval(slide, speed);
                };
                var slide = function () {
                    if (pause) return;
                    slideBox.scrollTop += 2;
                    if (slideBox.scrollTop % lineHeight == 0) {
                        clearInterval(tid);
                        for (i = 0; i < numpergroup; i++) {
                            slideBox.appendChild(slideBox.getElementsByTagName('li')[0]);
                        }
                        slideBox.scrollTop = 0;
                        setTimeout(start, delay);
                    }
                };
                slideBox.onmouseover = function () {
                    pause = true;
                };
                slideBox.onmouseout = function () {
                    pause = false;
                };
                setTimeout(start, delay);
            });

        }

        $scope.getImageService();
        $scope.getPrizeDraw();
        $scope.getPrizeGoodsList();
    })
