$(document).ready(function(){
	$("#login_btn").on('click', function(){
		if (!$('.login-form').valid()) {
			return;
		}

		var json = {};
		json.loginId = $("#loginId").val();
		json.password = $.md5($("#password").val());
        json.loginIdMd = $.md5(json.loginId);
		axse("/auth/login.json", {"data": JSON.stringify(json)}, successFn, errorFn);
	})

	document.onkeydown = function (event) {
		var e = event || window.event || arguments.callee.caller.arguments[0];
		if (e && e.keyCode == 13) {
			if (!$('.login-form').valid()) {
				return;
			}
			$('#loginId').blur();
			$('#password').blur();
			var json = {};
			json.loginId = $("#loginId").val();
			json.password = $.md5($("#password").val());
			json.loginIdMd = $.md5(json.loginId);

			axse("/auth/login.json", {"data": JSON.stringify(json)}, successFn, errorFn);
		}
	};

});

var Validator;

var Login = function () {

	var handleLogin = function() {
		Validator = $('.login-form').validate({
	            errorElement: 'span', //default input error message container
	            errorClass: 'help-block', // default input error message class
	            focusInvalid: false, // do not focus the last invalid input
	            rules: {
					loginId: {
	                    required: true
	                },
	                password: {
	                    required: true,
					},
	                remember: {
	                    required: false
	                }
	            },

	            messages: {
					loginId: {
	                    required: "用户名必须填写."
	                },
	                password: {
	                    required: "密码必须填写."
						//,
						//remote:"用户名或者密码错误."
	                }
	            },

	            invalidHandler: function (event, validator) { //display error alert on form submit   
	                $('.alert-error', $('.login-form')).show();
	            },

	            highlight: function (element) { // hightlight error inputs
	                $(element)
	                    .closest('.form-group').addClass('has-error'); // set error class to the control group
	            },

	            success: function (label) {
	                label.closest('.form-group').removeClass('has-error');
	                label.remove();
	            },

	            errorPlacement: function (error, element) {
	                error.insertAfter(element.closest('.input-icon'));
	            },

	            submitHandler: function (form) {
	                form.submit();
	            }
	        });

	}
    
    return {
        //main function to initiate the module
        init: function () {
        	
            handleLogin();
	       
	       	$.backstretch([
		        "../assets/img/bg/1.jpg",
		        "../assets/img/bg/2.jpg",
		        "../assets/img/bg/3.jpg",
		        "../assets/img/bg/4.jpg"
		        ], {
		          fade: 1000,
		          duration: 8000
		    });
        }

    };

}();

function successFn(data) {
	if (data.resultCode == '00') {
		sessionStorage.setItem("userName", data.data.userName);
		sessionStorage.setItem("userId", data.data.loginId);
        sessionStorage.setItem("sameValue", data.sameValue);
		$.cookie('the_user_info', data.data.userId, { expires: 1 });
		window.location.href = 'index.html';
	}else{
		$('#password').parent().parent().addClass('has-error');
		$('#loginId').parent().parent().addClass('has-error');

		var len = $('#password').parent().parent().find('span').length;
		if(len == 0){
			$('#password').parent().parent().append('<span for="password" class="help-block">'+data.resultMessage+'</span>');
		}
	}
}

function errorFn(data) {

}
