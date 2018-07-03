$(document).ready(function () {
    setListeners();
});

function setListeners() {
    $("#task_job_btn_send_birthday_coupon").click(function () {
        sendBirthdayCoupon();
        $(this).attr("disabled", "disabled");
    });
    $("#task_job_btn_confirm_receive").click(function () {
        confirmReceiveGoods();
        $(this).attr("disabled", "disabled");
    });
    $("#task_job_btn_finish_order").click(function () {
        finishOrder();
        $(this).attr("disabled", "disabled");
    });
    $("#task_job_btn_close_order").click(function () {
        closeOrder();
        $(this).attr("disabled", "disabled");
    });

    $("#task_job_btn_clear_log").click(function () {
        clearLog();
    });

}
// 同步客户数据
function sendBirthdayCoupon() {
    var url = '/task_job/distributeBirthdayCoupon.json';
    var param = {};
    var success = function (data) {
        if (data.resultCode == '00') {
            $("#task_job_progress").css("width", "100%");
            showAlert("提交成功", "提示", function () {
                resetProgress();
            });
        } else {
            resetProgress();
            showAlert("提交失败,原因：" + data.resultMessage, "提示");
        }
    };
    var error = function (data) {
        resetProgress();
        showAlert("提交失败,原因：网络异常或服务器异常", "提示");
    };
    axse(url, param, success, error);
    $("#task_job_progress").css("width", "10%");
}

function confirmReceiveGoods() {
    var url = '/task_job/receiveOrder15DaysAgo.json';
    var param = {};
    var success = function (data) {
        if (data.resultCode == '00') {
            $("#task_job_progress").css("width", "100%");
            showAlert("提交成功", "提示", function () {
                resetProgress();
            });
        } else {
            resetProgress();
            showAlert("提交失败,原因：" + data.resultMessage, "提示");
        }
    };
    var error = function (data) {
        resetProgress();
        showAlert("提交失败,原因：网络异常或服务器异常", "提示");
    };
    axse(url, param, success, error);
    $("#task_job_progress").css("width", "10%");
}

function finishOrder() {
    var url = '/task_job/finishOrder7DaysAgo.json';
    var param = {};
    var success = function (data) {
        if (data.resultCode == '00') {
            $("#task_job_progress").css("width", "100%");
            showAlert("提交成功", "提示", function () {
                resetProgress();
            });
        } else {
            resetProgress();
            showAlert("提交失败,原因：" + data.resultMessage, "提示");
        }
    };
    var error = function (data) {
        resetProgress();
        showAlert("提交失败,原因：网络异常或服务器异常", "提示");
    };
    axse(url, param, success, error);
    $("#task_job_progress").css("width", "10%");
}

function closeOrder() {
    var url = '/task_job/closeOrder30DaysAgo.json';
    var param = {};
    var success = function (data) {
        if (data.resultCode == '00') {
            $("#task_job_progress").css("width", "100%");
            showAlert("提交成功", "提示", function () {
                resetProgress();
            });
        } else {
            resetProgress();
            showAlert("提交失败,原因：" + data.resultMessage, "提示");
        }
    };
    var error = function (data) {
        resetProgress();
        showAlert("提交失败,原因：网络异常或服务器异常", "提示");
    };
    axse(url, param, success, error);
    $("#task_job_progress").css("width", "10%");
}


function resetProgress() {
    $(".task-job-btn").removeAttr("disabled");;
    $(".task-job-progress").css("width", "0%");
}

function appendLog(log, isNormal) {
    if (isNormal) {
        $("#task_job_log").append('<div>' + log + '</div>');
    } else {
        $("#task_job_log").append('<div class="red">' + log + '</div>');
    }
    $('#task_job_log').scrollTop($('#task_job_log')[0].scrollHeight);
}

function clearLog() {
    $("#task_job_log").text("");
}