/**
 * Created by C_Nagai on 2016/8/29.
 */
$(document).ready(function () {
    var json = {};
    json.itemCode = 'index_footer';
    json.isChild = '1';
    axse("/cms_items_api/getMasterByItemList.json", {'data': JSON.stringify(json)}, footerSuccess, footerError);
});

function footerSuccess(data) {
    if (data.resultCode == '00') {
        var nData = data.results;
        if (nData != null && nData.length > 0) {
            var footer;
            for (var i = 0; i < nData.length; i ++) {
                footer = ''
                footer += '<div class="col-xs-6 col-md-2 coupons-gd">';
                footer += '<h4>' + nData[i].itemName + '</h4>';
                var cmData = nData[i].cmList;
                if (cmData != null && cmData.length > 0) {
                    for (var y = 0; y < cmData.length; y ++) {
                        footer += '<p>' + cmData[y].title + '</p>';
                    }
                }
                footer += '</div>';
                $("#footer_area").append(footer);
            }
        }
        var QRCode = '';
        QRCode += '<div class="col-xs-6 col-md-2 coupons-gd">';
        QRCode += '<img alt=" " src="images/footer-phone.png" class="footer-phone">';
        QRCode += '</div>';
        $("#footer_area").append(QRCode);

    }

}

function footerError(data) {

}