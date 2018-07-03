/**
 * Created by C_Nagai on 2016/8/30.
 */
$(document).ready(function () {
    var json = {};
    json.itemCode = 'index_gds_sorts';
    json.isChild = '0';
    axse("/cms_items_api/getMasterByItemList.json", {'data': JSON.stringify(json)}, headerTypeSuccess, headerTypeError);
});

function headerTypeSuccess(data) {
    if (data.resultCode == '00') {
        var nData = data.results;
        if (nData != null && nData.length > 0) {
            var lastItem = $(".header-list").find("li:last");
            for (var i = 0; i < nData.length; i ++) {
                var header = '';
                // var listJson = JSON.parse(nData[i].listJson);
                // header += '<li class=" menu__item"><a class="menu__link" href="javascript:void(0);">' + listJson.text + '</a></li>';
                header += '<li class=" menu__item"><a class="menu__link" href="javascript:void(0);">' + nData[i].title + '</a></li>';
                $(header).insertBefore(lastItem);
            }
        }
    }

    var json = {};
    json.itemCode = 'index_act';
    json.isChild = '0';
    axse("/cms_items_api/getMasterByItemList.json", {'data': JSON.stringify(json)}, headerActSuccess, headerActError);
}

function headerTypeError(data) {

}

function headerActSuccess(data) {
    if (data.resultCode == '00') {
        console.log(data.results);
        var nData = data.results;
        if (nData != null && nData.length > 0) {
            var lastItem = $(".header-list").find("li:last");
            for (var i = 0; i < nData.length; i ++) {
                var header = '';
                header += '<li class=" menu__item"><a class="menu__link" href="javascript:void(0);">' + nData[i].title + '</a></li>';
                $(header).insertBefore(lastItem);
            }
        }
    }
}

function headerActError(data) {

}