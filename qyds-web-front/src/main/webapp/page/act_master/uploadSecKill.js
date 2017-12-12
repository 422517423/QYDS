/**
 * Created by zlh on 16/11/9.
 */

var activityId = sessionStorage.getItem("activityId");
var activityName = sessionStorage.getItem("activityName");

$(document).ready(function () {
    $("#uploadSecKillModal").modal('show');
    $("#activityName").val(activityName);

    initSecUpload();

    $("#btn_sec_upload").click(function () {
        uploadSecFile();
    });
});


function initSecUpload(){

    $("#btn_sec_upload").attr("disabled", true);
    $("#btn_sec_upload").hide();

    $('.fileinput').on('change','#import',function(e){
        var files = e.target.files === undefined ? (e.target && e.target.value ? [{ name: e.target.value.replace(/^.+\\/, '')}] : []) : e.target.files;
        var file;
        if(files.length > 0){
            file = files[0];
        }else{
            showTip("无效文件,请重新选择");
            $('#import').replaceWith('<input type="file" name="import" id="import">');
            $("#btn_sec_upload").attr("disabled", true);
            $("#btn_sec_upload").hide();

            return;
        }

        var name = file.name;
        var ext = name.substring(name.lastIndexOf('.')+1);

        if(ext != 'xls'){
            showTip("请选择后缀名是xls的EXCEL文件");
            $('#import').replaceWith('<input type="file" name="import" id="import">');
            $("#btn_sec_upload").attr("disabled", true);
            $("#btn_sec_upload").hide();
            return;
        }

        $("#btn_sec_upload").attr("disabled", false);
        $("#btn_sec_upload").show();
    });
}

function uploadSecFile() {

    $.ajaxFileUpload({
        url: '/qyds-web-front/act_master/uploadSecKill.json',
        fileElementId: 'import',
        dataType: 'json',
        data: {activityId: activityId},
        success: function (data) {
            if ($.type(data) == 'string' && data != '') {
                if (data.indexOf('{') != 0) {
                    data = data.substring(data.indexOf('{'))
                }
                data = $.parseJSON(data);
            }

            if (data.resultCode == '00') {
                $("#uploadSecKillModal").modal('hide');
                showAlert('提交成功!', '提示', function () {
                    table.fnDraw();
                });
            } else {
                showTip("文件导入失败");
                $('#import').replaceWith('<input type="file" name="import" id="import">');
                $("#btn_sec_upload").attr("disabled", true);
                $("#btn_sec_upload").hide();
            }
        },
        error: function () {
            showTip("文件导入失败");
            $('#import').replaceWith('<input type="file" name="import" id="import">');
            $("#btn_sec_upload").attr("disabled", true);
            $("#btn_sec_upload").hide();
        }
    });
}


function showTip(message) {
    $("#upload_sec_tip").text(message);
    setTimeout(function () {
        $("#upload_sec_tip").text("");
    }, 3000);
}
