(function ($) {
    HashMap = function () {
    	var index = 0;
    	var content = '';
    	var keyV = new Array();
    	var valueV = new Array();
    	//向map中添加key，value键值对
        this.put = function (key,value) {
            if(key == undefined || key.Trim == '') {
            	return;
            }
            if(value == undefined || value.Trim == '') {
            	return;
            }
            if(content.indexOf(key) == -1) {
            	keyV[index] = key;
            	valueV[index++] = value;
            	content += key + ';';
            } else {
            	var contents = content.split(';');
            	for(var i = 0; i < contents.length - 1; i++) {
            		if(key == contents[i]) {
            			valueV[i] = value;
            			break;
            		}
            	}
            }
        };
        //根据key获取value值
        this.get = function(key) {
        	if(key == undefined || key.Trim == '') {
            	return;
            }
        	var contents = content.split(';');
        	for(var i = 0; i < contents.length - 1; i++) {
        		if(key == contents[i]) {
        			return valueV[i];
        		}
        	}
        	return '';
        };
        //判断是否包含制定的key值
        this.containsKey = function() {
        	if(key == undefined || key.Trim == '') {
            	return false;
            }
        	var contents = content.split(';');
        	for(var i = 0; i < contents.length - 1; i++) {
        		if(key == contents[i]) {
        			return true;
        		}
        	}
        	return false;
        };
        //判断map是否为空
        this.isEmpty = function() {
        	if(keyV.length == 0) {
        		return true;
        	} else {
        		return false;
        	}
        };
        //获取该map的大小
        this.size = function() {
        	return keyV.length;
        };
    };
})(jQuery);
//取出串中的前后空格
String.prototype.Trim = function() {
	return this.replace(/^\s+/,'').replace(/\s+$/,'');
};