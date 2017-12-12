angular.module('test.directive', [])
    // 手动绑定ng-model
    .directive('ngUpdateInput', function () {
        return {
            restrict: 'AE', //attribute or element
            scope: {},
            replace: true,
            require: 'ngModel',
            link: function ($scope, elem, attr, ngModel) {
                $scope.$watch(ngModel, function (nv) {
                    elem.val(ngModel.$modelValue);
                });
                $jq(elem).change(function () { //bind the change event to hidden input
                    $scope.$apply(function () {
                        ngModel.$setViewValue(elem.val());
                    });
                });
            }
        };
    })
    // 密码一致性校验
    .directive('pwCheck', function () {
        return {
            require: "ngModel",
            link: function(scope, elem, attrs, ctrl) {
                var otherInput = elem.inheritedData("$formController")[attrs.pwCheck];

                ctrl.$parsers.push(function(value) {
                    if(value === otherInput.$viewValue) {
                        ctrl.$setValidity("repeat", true);
                        return value;
                    }
                    ctrl.$setValidity("repeat", false);
                });

                otherInput.$parsers.push(function(value) {
                    ctrl.$setValidity("repeat", value === ctrl.$viewValue);
                    return value;
                });
            }
        };
    })
    // 短信验证倒计时
    //.directive('timerbutton', function($timeout, $interval){
    //    return {
    //        restrict: 'AE',
    //        transclude: true,
    //        scope: {
    //            showTimer: '=',
    //            cb: '&',
    //            timeout: '='
    //        },
    //        link: function(scope, element, attrs){
    //            scope.timer = true;
    //            scope.timerCount = scope.timeout;
    //            var counter = $interval(function(){
    //                scope.timerCount = scope.timerCount - 1000;
    //            }, 1000);
    //
    //            scope.onClick = function(){
    //                scope.cb({message: '11111'});
    //            }
    //
    //            $timeout(function(){
    //                scope.timer = false;
    //                $interval.cancel(counter);
    //                scope.showTimer = false;
    //            }, scope.timeout);
    //        },
    //        template: '<button ng-click="onClick()" ng-disabled="timer"><span ng-transclude></span>&nbsp<span ng-if="showTimer">({{ timerCount / 1000 }}s)</span></button>'
    //    };
    //});
    .directive('timerbutton', function($timeout, $interval){
        return {
            restrict: 'AE',
            scope: {
                showTimer: '=',
                timeout: '=',
                cb: '&',
                isValid:'@isValid'
            },
            link: function(scope, element, attrs){
                scope.timer = false;
                scope.timeout = 60000;
                scope.timerCount = scope.timeout / 1000;
                scope.text = "获取验证码";
                //scope.isValid = false;

                scope.onClick = function(){
                    scope.cb();
                    if(!scope.isValid || scope.isValid == 'false'){
                        return false;
                    }

                    scope.showTimer = true;
                    scope.timer = true;
                    scope.text = "秒";
                    var counter = $interval(function(){
                        scope.timerCount = scope.timerCount - 1;
                    }, 1000);

                    $timeout(function(){
                        scope.text = "获取验证码";
                        scope.timer = false;
                        $interval.cancel(counter);
                        scope.showTimer = false;
                        scope.timerCount = scope.timeout / 1000;
                    }, scope.timeout);
                }
            },
            template: '<button style="font-size:15px;" ng-click="onClick()" class="button button-dark button-small" ng-disabled="timer"><span ng-if="showTimer">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;{{ timerCount }}</span>{{text}}<span ng-if="showTimer">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></button>'
        };
    })
    .directive('focusInput', ['$ionicScrollDelegate', '$window', '$timeout', '$ionicPosition', function ($ionicScrollDelegate, $window, $timeout, $ionicPosition) {
        return {
            restrict: 'A',
            scope: false,
            link: function ($scope, iElm, iAttrs, controller) {
                if (ionic.Platform.isIOS()) {
                    iElm.on('focus', function () {
                        var top = $ionicScrollDelegate.getScrollPosition().top;
                        var eleTop = ($ionicPosition.offset(iElm).top) / 2
                        var realTop = eleTop + top;
                        $timeout(function () {
                            if (!$scope.$last) {
                                $ionicScrollDelegate.scrollTo(0,realTop);
                            } else {
                                try {
                                    var aim = angular.element(document).find('.scroll')
                                    aim.css('transform', 'translate3d(0px,' + '-' + realTop + 'px, 0px) scale(1)');
                                    $timeout(function () {
                                        iElm[0].focus();
                                        console.log(2);
                                    }, 100)
                                } catch (e) {
                                }

                            }
                        }, 500)
                    })
                }

            }
        }
    }])
;