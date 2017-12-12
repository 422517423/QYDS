/**
 * Created by C_Nagai on 2016/8/30.
 */

$(document).ready(function () {
    $(".logistics_list").on('click', function () {
        $('#personal_container').load('html/logisticslist.html', function () {

        });
    });

    $(".order_detail").on('click', function () {
        $('#personal_container').load('html/orderDetail.html', function () {

        });
    });
});