// 图片拦截器
angular.module('test.filter', [])
    .filter('imgPath', ["$filter", function ($filter) {
        return function (oriPath, size) {

            var thumb = ORIGNAL;
            if (size == '120') {
                thumb = THUMB_120;
            } else if (size == '480') {
                thumb = THUMB_480;
            } else if (size == '800') {
                thumb = THUMB_800;
            } else if (size == '1200') {
                thumb = THUMB_1200;
            }
            if (oriPath != null && oriPath.lastIndexOf(',') != -1) {
                oriPath = oriPath.substring(0, oriPath.length - 1);
            }

            if(oriPath != null && oriPath.split(",").length > 0){
                oriPath = oriPath.split(",")[0];
            }

            return DISPLAY_URI + thumb + oriPath;
        };
    }])
    .filter('imgPathJson', ["$filter", function ($filter) {
        return function (oriPath, size) {
            var thumb = ORIGNAL;
            if (size == '120') {
                thumb = THUMB_120;
            } else if (size == '480') {
                thumb = THUMB_480;
            } else if (size == '800') {
                thumb = THUMB_800;
            } else if (size == '1200') {
                thumb = THUMB_1200;
            }
            return DISPLAY_URI + thumb + oriPath.split(",")[0];
        };
    }])
    .filter('sizeFilter', ["$filter", function ($filter) {
        return function (size) {
            return size.split(" (")[0];
        };
    }])
    .filter('sizeFilter2', ["$filter", function ($filter) {
        return function (size) {
            if(size.split(" (")[0] != null
             && size.split(" (")[0].split(" ")[1] == null){
                return size.split("(")[0];
            }
            return size.split(" (")[0].split(" ")[1];
        };
    }])
    .filter('ecSkuFilter', ["$filter", function ($filter) {
        return function (skuJsonStr, type) {
            // {"sku_display":"颜色_规格_尺寸_","sku_img":["/GDS_SKU/8c/8cf2980c-c63e-4dc9-b209-36a0d7e3f85a.PNG","/GDS_SKU/70/70748240-0295-4895-8d38-bfc990b0b93d.PNG"],"sku_key":"30333_22823_49_","sku_value":"白_大_100_"}
            var skuJson = JSON.parse(skuJsonStr);
            if (type == "image") {
                return skuJson.sku_img[0];
            } else if (type == "color") {
                return skuJson.sku_value.split("_")[0];
            } else if (type == "size") {
                return skuJson.sku_value.split("_")[1];
            }
            return size.split(" ")[0];
        };
    }])
    .filter('priceFilter', ["$filter", function ($filter) {
        return function (price) {
            return parseFloat(price).toFixed(2);
        };
    }])
    .filter('orderSkuFilter10', ["$filter", function ($filter) {
        return function (skuJsonStr, type) {
            // [{"color_code":"520","color_name":"橄榄绿","size_code":"10","size_name":"XS 165/88A"}]"
            var skuJson = JSON.parse(skuJsonStr)[0];
            if (type == "color") {
                return skuJson.color_name;
            } else if (type == "size") {
                return skuJson.size_name;
            }
            return "";
        };
    }])
    .filter('orderSkuFilter20', ["$filter", function ($filter) {
        return function (skuJsonStr, type) {
            // "[{"sku_display":"颜色_规格_尺寸_","sku_img":["/GDS_SKU/8c/8cf2980c-c63e-4dc9-b209-36a0d7e3f85a.PNG"],"sku_key":"40657_23567_49_","sku_value":"黑_小_100_"}]"
            var skuJson = JSON.parse(skuJsonStr)[0];
            if (type == "image") {
                return skuJson.sku_img[0];
            } else if (type == "color") {
                return skuJson.sku_value.split("_")[0];
            } else if (type == "size") {
                return skuJson.sku_value.split("_")[1];
            }
            return size.split(" ")[0];
        };
    }])
    .filter('storePhoneFilter', ["$filter", function ($filter) {
        return function (storeInfo) {
            var str1 = storeInfo.split("地址")[0];
            return str1.split(":")[1];
        };
    }])
    .filter('storeAddressFilter', ["$filter", function ($filter) {
        return function (storeInfo) {
            return storeInfo.split("地址:")[1];
        };
    }]);
