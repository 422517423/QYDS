/**
 * Created by zlh on 17/2/9.
 */

var tempId = sessionStorage.getItem("tempId");
var tempName = sessionStorage.getItem("tempName");

$(document).ready(function () {
    $("#uploadSKUModal").modal('show');
    $("#tempName").val(tempName);

    initUpload();

    $("#btn_upload").click(function () {
        uploadFile();
    });
});


function initUpload(){

    $("#btn_upload").attr("disabled", true);
    $("#btn_upload").hide();

    $('.fileinput').on('change','#import',function(e){
        var files = e.target.files === undefined ? (e.target && e.target.value ? [{ name: e.target.value.replace(/^.+\\/, '')}] : []) : e.target.files;
        var file;
        if(files.length > 0){
            file = files[0];
        }else{
            showTip("无效文件,请重新选择");
            $('#import').replaceWith('<input type="file" name="import" id="import">');
            $("#btn_upload").attr("disabled", true);
            $("#btn_upload").hide();

            return;
        }

        var name = file.name;
        var ext = name.substring(name.lastIndexOf('.')+1);

        if(ext != 'xls'){
            showTip("请选择后缀名是xls的EXCEL文件");
            $('#import').replaceWith('<input type="file" name="import" id="import">');
            $("#btn_upload").attr("disabled", true);
            $("#btn_upload").hide();
            return;
        }

        $("#btn_upload").attr("disabled", false);
        $("#btn_upload").show();
    });
}

function uploadFile() {

    $.ajaxFileUpload({
        url: '/qyds-web-front/act_template/uploadSKU.json',
        fileElementId: 'import',
        dataType: 'json',
        data: {tempId: tempId},
        success: function (data) {
            if ($.type(data) == 'string' && data != '') {
                if (data.indexOf('{') != 0) {
                    data = data.substring(data.indexOf('{'))
                }
                data = $.parseJSON(data);
            }

            if (data.resultCode == '00') {
                $("#uploadSKUModal").modal('hide');
                showAlert('提交成功!', '提示', function () {
                    table.fnDraw();
                });
            } else {
                showTip("文件导入失败");
                $('#import').replaceWith('<input type="file" name="import" id="import">');
                $("#btn_upload").attr("disabled", true);
                $("#btn_upload").hide();
            }
        },
        error: function () {
            showTip("文件导入失败");
            $('#import').replaceWith('<input type="file" name="import" id="import">');
            $("#btn_upload").attr("disabled", true);
            $("#btn_upload").hide();
        }
    });
}


function showTip(message) {
    $("#upload_sku_tip").text(message);
    setTimeout(function () {
        $("#upload_sku_tip").text("");
    }, 3000);
}
