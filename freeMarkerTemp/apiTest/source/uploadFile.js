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
		/**var group_id = $('div.L').find('.active-gray').attr('group_id');**/
		$('.wrapper-left').show();
		$('.wrapper-right-content').show();

		/**$('#func_list').find('a:first').addClass('active-gray');**/
		var api_id = $('#func_list').find('.active-gray').attr('api_id');
		
		
	
	
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
/**function apitest_submit() {
	var data = $("#api_test_form").serializeArray();

	var jsonStr = '{';
	for(var i=0 ; i < data.length ; i++){
		if(data[i].name == 'json'){
			continue;
		}else{
			jsonStr += data[i].name + ':"' + data[i].value + '",'
		}
	}
	if(jsonStr.lastIndexOf(',') > 0){
		jsonStr = jsonStr.substring(0,jsonStr.lastIndexOf(','));
	}

	jsonStr += '}';
	$('#jsonStr').val(jsonStr);
	$('.apitest-response').showLoading();
	
	//$('input[name=request_method]').val($('#api_method').text());
	//$('input[name=project_id]').val($("select[name=project_id]").val());
	
	var saveCallBack = apitest_requested;
	var options = {
            dataType:'json',
            timeout:60000,
            success:saveCallBack,
            error:ajaxError
    };
    $("#api_test_form").ajaxSubmit(options);
    return false;
}**/

/**
 * 提交处理
 */
function apitest_submit() {
	

	$('.apitest-response').showLoading();
		
	var saveCallBack = apitest_requested;
	var options = {
		dataType : 'json',
		processData : false,
		contentType:"application/json",
		timeout : 60000,
		success : saveCallBack
	};
	$("#api_test_form").ajaxSubmit(options);
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

