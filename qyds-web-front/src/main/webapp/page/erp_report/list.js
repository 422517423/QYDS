/**
 * Created by zlh on 2016/12/15.
 */
$(document).ready(function() {
    if (jQuery().datepicker) {
        $('.date-picker').datepicker({
            rtl: App.isRTL(),
            autoclose: true,
        });
    }
});
