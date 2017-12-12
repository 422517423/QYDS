$(document).ready(function () {
    $("#prize_goods_edit_dialog").modal('show');
    $("textarea").css("resize", "none");
    if (selectedPrizeGoods) {
        initForm();
    }
    //表单验证的定义
    $("#prize_goods_edit_form").validate({
        errorElement: 'span', //default input error message container
        errorClass: 'help-block', // default input error message class
        focusInvalid: false, // do not focus the last invalid input
        errorPlacement: function (error, element) {
            if ($(element).attr('name') == 'start_time' || $(element).attr('name') == 'end_time') {
                $(element).parent().after(error)
            } else {
                $(element).after(error)
            }
        },
        rules: {
            prize_goods_name: {
                required: true,
                maxlength: 50
            },
            prize_goods_count: {
                required: true
            },
            win_percent: {
                required: true
            }
        },
        messages: {
            prize_goods_name: {
                required: "请输入奖品名称",
                maxlength: "不能超过50个字符"
            },
            prize_goods_count: {
                required: "请输入奖品件数"
            },
            win_percent: {
                required: "请输入中奖概率"
            }
        },

        highlight: function (element) { // hightlight error inputs
            $(element)
                .closest('.form-group').addClass('has-error'); // set error class to the control group
        },

        unhighlight: function (element) { // revert the change done by hightlight
            $(element)
                .closest('.form-group').removeClass('has-error'); // set error class to the control group
        },

        success: function (label) {
            label
                .closest('.form-group').removeClass('has-error'); // set success class to the control group
        }
    });

    $("#prize_goods_btn_confirm").click(function () {
        if (!$("#prize_goods_edit_form").valid()) {
            return;
        }
        savePrizeGoods();
    });

    $('#prize_goods_image').click(function () {
        $("#prize_goods_image_input").click();
    });

    $('#prize_goods_image_input').change(function (e) {
        processImage(this, $('#prize_goods_image'));
    });

});

function processImage(fileInput, image, count) {
    var filepath = $(fileInput).val();
    var extStart = filepath.lastIndexOf(".");
    var ext = filepath.substring(extStart + 1, filepath.length).toUpperCase();
    var fileName = getFileName(filepath);
    if (ext != "JPG" && ext != "JPEG" && ext != "PNG") {
        showAlert("请选择符合要求格式的图片上传！", "提示");
        return;
    }
    // Get a reference to the fileList
    var files = !!fileInput.files ? fileInput.files : [];
    // If no files were selected, or no FileReader support,
    // return
    if (!files.length || !window.FileReader) {
        showAlert("读取图片失败！", "提示");
        return;
    }
    // Only proceed if the selected file is an image
    if (/^image/.test(files[0].type)) {
        // Create a new instance of the FileReader
        var reader = new FileReader();
        // Read the local file as a DataURL
        reader.readAsDataURL(files[0]);
        reader.onloadend = function () {
            uploadImage(image, this.result, fileName, ext, count);
        }
    } else {
        showAlert("请选择图片文件！", "提示");
        return;
    }
}

//图片上传网络请求方法
function uploadImage(img, data, fileName, suffix, count) {
    var url = uploadUri;
    var param = {
        type: "PRIZE_DRAW",
        file: data,
        fileName: fileName,
        suffix: suffix
    };

    var success = function (data) {
        console.log(data);
        if (data.resultCode == '00') {
            img.attr("src", displayUri + orignal + data.data);
            $("#prize_goods_image_input").val(data.url)
        } else {
            showAlert('图片上传失败');
        }
    };
    var error = function () {
        showAlert('图片上传失败');
    };
    axseForImage(null, param, success, error);
}

function initForm() {
    $("#prize_goods_name").val(selectedPrizeGoods.prizeGoodsName);
    $("#prize_goods_count").val(selectedPrizeGoods.prizeGoodsCount);
    $("#win_percent").val(selectedPrizeGoods.winPercent);
    $("#prize_goods_image").attr("src", selectedPrizeGoods.prizeGoodsImage);
    $("#prize_goods_image_input").val(selectedPrizeGoods.prizeGoodsImage);
    $("#prize_goods_desc").val(selectedPrizeGoods.prizeGoodsDesc);
    $("#prize_goods_sort").val(selectedPrizeGoods.sort);
}

function savePrizeGoods() {
    var prizeGoodsId = null;
    var url = null;
    if (selectedPrizeGoods) {
        prizeGoodsId = selectedPrizeGoods.prizeGoodsId;
        url = "/prize_draw/editPrizeGoods.json";
    } else {
        prizeGoodsId = null;
        url = "/prize_draw/addPrizeGoods.json";
    }
    var param = {
        prizeDrawId:prizeDrawId,
        prizeGoodsId: prizeGoodsId,
        prizeGoodsName: $("#prize_goods_name").val(),
        prizeGoodsCount: $("#prize_goods_count").val(),
        winPercent: $("#win_percent").val(),
        prizeGoodsImage: $("#prize_goods_image_input").val(),
        prizeGoodsDesc: $("#prize_goods_desc").val(),
        sort: $("#prize_goods_sort").val()
    };

    var success = function (data) {
        if (data.resultCode == '00') {
            $("#prize_goods_edit_dialog").modal('hide');
            getPrizeGoodsList();
        } else {
            showAlert('提交失败');
        }
    };
    var error = function () {
        showAlert('网络异常');
    };
    axse(url, {"data": JSON.stringify(param)}, success, error);

}
