'use strict';

/* Directives */

angular.module('dealuna.directives', [])
    .directive('header', function(){
       return {
           restrict: 'EA',
           replace: true,
           templateUrl:"html/header.html"
       }
    })
    .directive('footer', function(){
        return {
           restrict: 'EA',
           replace: true,
           templateUrl:"html/footer.html"
        }
    })
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

                if($(elem).length > 0 && $(elem)[0].type && $(elem)[0].type == 'text'){
                    $(elem).change(function () {
                        var that = this;
                        $scope.$apply(function () {
                            ngModel.$setViewValue($(that).val());
                        });
                    });
                }else{
                    $(elem).find('input').change(function () {
                        var that = this;
                        $scope.$apply(function () {
                            ngModel.$setViewValue($(that).val());
                        });
                    });
                }
            }
        };
    })

    .directive('fileModel', ['$parse', function ($parse) {
        return {
            restrict: 'A',
            link: function(scope, element, attrs, ngModel) {
                var model = $parse(attrs.fileModel);
                var modelSetter = model.assign;
                element.bind('change', function(event){
                    scope.$apply(function(){
                        modelSetter(scope, element[0].files[0]);
                    });
                    //附件预览
                    scope.file = (event.srcElement || event.target).files[0];
                    scope.getFile();
                });
            }
        };
    }])
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
    });
 //.directive('footer', function(){
 //   return {
 //       restrict: 'EA',
 //       replace: true,
 //       templateUrl:"tpls/footer.html"
 //   }
 //})
 //.directive('datePicker', function(){
 //   return {
 //       restrict: 'EA',
 //       replace: true,
 //       templateUrl:"tpls/datePicker.html"
 //   }
 //})
 //.directive('paginations', function(){
 //   return {
 //       restrict: 'EA',
 //       replace: true,
 //       templateUrl:"tpls/paginations.html",
 //       controller:"PaginationDemoCtrl"
 //   }
 //})
 //.directive('pageGroup', function(){
 //   return {
 //       restrict: 'EA',
 //       replace: true,
 //       templateUrl:"tpls/pageGroup.html"
 //   }
 //})
 ;