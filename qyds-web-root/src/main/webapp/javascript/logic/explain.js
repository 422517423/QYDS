/**
 * Created by C_Nagai on 2016/9/3.
 */
$(document).ready(function () {
    var cmsId = getParameter("cmsId");
    var json = {};
    json.cmsId = cmsId;
    axse("/cms_items_api/getContentHtmlByCmsId.json", {'data': JSON.stringify(json)}, footerSuccess, footerError);
});

function getParameter(param) {
    var query = window.location.search;
    var iLen = param.length;
    var iStart = query.indexOf(param);
    if (iStart == -1)
        return "";
    iStart += iLen + 1;
    var iEnd = query.indexOf("&", iStart);
    if (iEnd == -1)
        return query.substring(iStart);
    return query.substring(iStart, iEnd);
}

function footerSuccess(data) {
    if (data.resultCode == '00') {
        var nData = data.results;
        if (nData != null) {
            $("#content_html").html(nData.contentHtml);
        }
    }
}

function footerError(data) {

}