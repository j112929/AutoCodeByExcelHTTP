/**
 * eg){group_id:"0",api_id:"7"}
 * group_id 表示接口分组 ID
 * api_id 表示接口 ID
 */
var cookieName_apitest = 'ApiTestCookie';

$(document).ready(function() {
	
		/* 加载分组样式 */
		$('div.L').find('.nav-class:first').addClass('active-gray');
		
		/* 加载接口列表 */
		var group_id = $('div.L').find('.active-gray').attr('group_id');
		var is_api = loadApi(group_id);
		
		/* 加载导航栏 */
		loadNav(group_id, $("#func_list>a:first").attr('api_id'));
		
		/* 加载接口参数列表 form */
		if (is_api == true) {
			$('#func_list').find('a:first').addClass('active-gray');
			var api_id = $('#func_list').find('.active-gray').attr('api_id');
			loadParams(group_id, api_id);
		}
	
	
	/**
	 * 选择项目
	 */
	$('select[name=project_id]').change(function(){
		if ($(this).val()) {
			clearCookie();
			window.location = '/interface?project_id=' + $(this).val();
		}
	});
	
	/**
	 * 点击分组，切换接口列表，并默认载入第一个接口的参数列表 form，并记录 cookie
	 */
	$(".nav-class").click(function(){
		var group_id = $(this).attr('group_id');
		
		/* 分组样式切换 */
		$('div.L').find('.active-gray').removeClass('active-gray');
		$(this).addClass('active-gray');
		
		/* 重载接口列表 */
		var is_api = loadApi(group_id);
		
		/* 重载导航栏 */
		loadNav(group_id, $("#func_list>a:first").attr('api_id'));
		
		/* 重载接口参数列表 form */
		if (is_api == true) {
			$('#func_list').find('a:first').addClass('active-gray');
			loadParams($(this).attr('group_id'), $("#func_list>a:first").attr('api_id'));
		}
		
		/* 给隐藏分组字段赋值 */
		$("input[name=group_id]:last").val(group_id);
		
		/* 记录 cookie */
		setCookie(group_id, $("#func_list>a:first").attr('api_id'));
	});
	
	/**
	 * 点击接口加载参数列表 form
	 */
	$("#func_list").on('click', 'a', function(){
		/* 接口列表样式切换 */
		$('.wrapper-left').find('.active-gray').removeClass('active-gray');
		$(this).addClass('active-gray');
		
		/* 重载导航栏 */
		loadNav($(this).attr('group_id'), $(this).attr('api_id'));
		
		/* 重载接口参数列表 form */
		loadParams($(this).attr('group_id'), $(this).attr('api_id'));
		
		/* 记录 cookie */
		setCookie($(this).attr('group_id'), $(this).attr('api_id'));
	});
	
	/**
	 * 注解部分 - 展开与收缩功能
	 */
	$(".api-test-memo-stretch").click(function(){
		if ($(".memo-request").hasClass('height-auto')) {
			$(this).html('[+]');
			$(".memo-request").removeClass('height-auto');
		} else {
			$(this).html('[-]');
			$(".memo-request").addClass('height-auto');
		}
	});
	
	/**
	 * 提交方式下拉处理
	 */
	$('.api-method').click(function(){
		$('#api_method').text($(this).text());
	});
	
	/**
	 * 提交处理
	 */
	$("#apitest_submit").click(function(){
		apitest_submit();
	});
});


/**
 * 根据接口分组加载接口列表，若无接口，则加载分组描述
 */
function loadApi(group_id) {
	if (api_configs[group_id]) {
		$('.wrapper-right-infomation').empty();
		
		var apis = api_configs[group_id];
		var str_api = '';
		for (var k in apis) {
			str_api += '<a href="javascript:;" id="apitest_api_' + apis[k]['id'] + '" group_id="' + apis[k]['group_id'] + '" api_id="' + apis[k]['id'] + '" class="list-group-item">' + apis[k]['name'] + '</a>';
		}
		$("#func_list").html(str_api);
		
		$('.wrapper-left').show();
		$('.wrapper-right-content').show();
		
		return true;
	}
	
	$('.wrapper-left').hide();
	$('.wrapper-right-content').hide();
	$('.wrapper-right-infomation').html(group_configs[group_id]['content']);
	
	return false;
}

/**
 * 重载导航栏
 */
function loadNav(group_id, api_id) {
	var group_name = $('#apitest_group_' + group_id).parent().find('a:first').text();
	var group_name_son = $('#apitest_group_' + group_id).text();
	var api_name = '';
	if (api_configs[group_id]) {
		api_name = $('#apitest_api_' + api_id).text();
	}
	
	var api_test_nav = '<li class="active">' + group_name + '</li>' + 
			'<li class="active">' + group_name_son + '</li>';
	if (api_name) {
		api_test_nav += '<li class="active">' + api_name + '</li>';
	}
	$("#api_test_nav>li:first").nextAll().remove();
	$("#api_test_nav").append(api_test_nav);
}

/**
 * 根据接口加载具体的参数信息
 */
function loadParams(group_id, api_id) {
	if (param_configs[api_id]) {
		loadUrl(group_id, api_id);
		
		var method = api_configs[group_id][api_id]['request_method'];
		var j_para = param_configs[api_id];
		
		/* 处理参数 */
		var str_fixed = '';
		var str_shifty = '';
		for (var k in j_para) {
			var str_para = '';
			str_para += '<li m="' + method + '">';
			str_para += '<p class="p-param-name"><input type="text" class="form-control para-name" value="' + j_para[k]['name'] + '" disabled="disabled" /></p>';
			str_para += '<p class="p-param-symbol">&nbsp;=&nbsp;</p>';
			str_para += '<p class="p-param-value">';
			if (j_para[k]['val_inputtype'] == 'text') {
				str_para += '<input type="text" class="form-control para-value" name="' + j_para[k]['name'] + '" value="' + j_para[k]['val'] + '" />';
			} else if (j_para[k]['val_inputtype'] == 'select' && j_para[k]['val'].length > 0) {
				var dv = j_para[k]['val'];
				var dv_arr = dv.split(',');
				str_para += '<select class="form-control para-value" name="' + j_para[k]['name'] + '">';
				for (var i = 0; i < dv_arr.length; i++) {
					var dv_arr2 = dv_arr[i].split(':');
					str_para += '<option value="' + dv_arr2[1] + '">' + dv_arr2[0] + '</option>';
				}
				str_para += '</select>';
			} else if (j_para[k]['val_inputtype'] == 'file') {
				str_para += '<input type="file" class="form-control para-value" name="' + j_para[k]['name'] + '" value="' + j_para[k]['val'] + '" />';
			} else if (j_para[k]['val_inputtype'] == 'textarea') {
				str_para += '<textarea class="form-control para-value" name="' + j_para[k]['name'] + '">' + j_para[k]['val'] + '</textarea>';
			} else if (j_para[k]['val_inputtype'] == 'checkbox' && j_para[k]['val'].length > 0) {
				var dv = j_para[k]['val'];
				var dv_arr = dv.split(',');
				for (var i = 0; i < dv_arr.length; i++) {
					var dv_arr2 = dv_arr[i].split(':');
					str_para += '<input type="checkbox" class="form-control para-value" name="' + j_para[k]['name'] + '[]" value="' + dv_arr2[1] + '" />&nbsp;' + dv_arr2[0] + '<br />';
				}
			} else if (j_para[k]['val_inputtype'] == 'radio' && j_para[k]['val'].length > 0) {
				var dv = j_para[k]['val'];
				var dv_arr = dv.split(',');
				for (var i = 0; i < dv_arr.length; i++) {
					var dv_arr2 = dv_arr[i].split(':');
					str_para += '<input type="radio" class="form-control para-value" name="' + j_para[k]['name'] + '" value="' + dv_arr2[1] + '" />&nbsp;' + dv_arr2[0] + '<br />';
				}
			}
			str_para += '</p>';
			str_para += '<p class="p-para-type" title="' + j_para[k]['val_type'] + ':' + j_para[k]['note'] + '">(' + j_para[k]['val_type'] + ':<span class="para-memo">' + j_para[k]['note'] + '</span>)</p></li>';
			
			if (j_para[k]['is_fixed'] == 1) {
				str_fixed += str_para;
			} else {
				str_shifty += str_para;
			}
		}
		
		$("#para_list_fixed").html(str_fixed);
		$("#para_list_shifty").html(str_shifty);
		
		/* 根据当前请求方式下的动态参数数量，判断提交提示信息是否显示 */
		checkBtn();
		
		$(".apitest-response").hide().find(".apitest-result").each(function(){
			if ($(this).attr('id') == 'ar_params' || $(this).attr('id') == 'ar_result') {
				$(this).find('pre').empty();
			} else {
				$(this).empty();
			}
		});
	}
}

/**
 * 加载请求地址详情
 */
function loadUrl(group_id, api_id) {
	$("input[name=api_url]").val(api_configs[group_id][api_id]['request_url']);
	$(".memo-request").html(api_configs[group_id][api_id]['note'] ? api_configs[group_id][api_id]['note'] : '');
	$("#api_method").text(api_configs[group_id][api_id]['request_method']);
	
	$('input[name=request_url]').val(api_configs[group_id][api_id]['request_url']);
	$('input[name=request_method]').val(api_configs[group_id][api_id]['request_method']);
}

/**
 * 根据当前请求方式下的动态参数数量，判断提交提示信息是否显示
 */
function checkBtn() {
	if ($("#para_list_shifty").find(".default-hidden").size() == $("#para_list_shifty>li").size()) {
		$("#para_list_shifty").append('<li m="notice" class="notice-noparam">该接口下无需额外参数，可直接提交</li>');
	}
}

/**
 * 记录 cookie
 */
function setCookie(group_id, api_id) {
	var cookie_json_str = '{group_id:"' + group_id + '",api_id:"' + api_id + '"}';
    $.cookie(cookieName_apitest, cookie_json_str, {expires: 1, path: '/'});
}

/**
 * 清空 cookie
 */
function clearCookie() {
    $.cookie(cookieName_apitest, '');
}

/**
 * 提交处理
 */
function apitest_submit() {
	$('.apitest-response').showLoading();
	
	$('input[name=request_method]').val($('#api_method').text());
	$('input[name=project_id]').val($("select[name=project_id]").val());
	
	var saveCallBack = apitest_requested;
	var options = {
            dataType:'json',
            timeout:60000,
            success:saveCallBack,
            error:ajaxError
    };
    $("#api_test_form").ajaxSubmit(options);
    return false;
}

/**
 * 提交返回处理
 */
function apitest_requested(data, textStatus) {
    $(".apitest-response").show();
    
    var rmt = $("#api_method").text();
	var rurl = $('input[name=api_url]').val();
	
	// 获取传递参数
	var params = {};
	$('.para-value').each(function(){
		params[$(this).attr('name')] = $(this).val();
	});
	
    $("#ar_url").html(rurl);
    $("#ar_method").html(rmt);
    $("#ar_params>pre").html(JsonUti.convertToString(params));
    $("#ar_result>pre").html(JsonUti.convertToString(data));
    
    $('.apitest-response').hideLoading();
}

/**
 * ajaxError
 */
function ajaxError(XMLHttpRequest, textStatus, errorThrown) {
    alert('Ooops!Encountered error while connecting to the server.There might be something wrong with your network.Please check your network connection!');
    $('.apitest-response').hideLoading();
}

	 