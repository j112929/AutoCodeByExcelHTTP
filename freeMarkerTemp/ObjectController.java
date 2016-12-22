package com.bluemobi.controller.api.${packageName};

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.appcore.util.JsonUtil;
<#list toList as toClass>
import com.bluemobi.to.api.${packageName}.${toClass.nameFU}RequestTO;
import com.bluemobi.to.api.${packageName}.${toClass.nameFU}ResponseTO;
</#list>
import com.bluemobi.controller.AbstractAPIController;
import com.bluemobi.to.FailResponseTO;

/**
 * 【${note}】控制器
 * 
 * @author ${author} ${email}
 * @date ${timeMonth}
 * 
 */
@Controller(value = "${name}APIController")
@RequestMapping("api/${name}")
public class ${nameFU}APIController extends AbstractAPIController {

    private static final Logger LOGGER = LoggerFactory.getLogger(${nameFU}APIController.class);


<#list toList as toClass>
	/**
	 * ${toClass.note}
     * @param request
     * @param ${toClass.name}RequestTO
     * @return Object
	 * @author ${author} ${email}
     * @date ${timeMonth}
	 */
    @RequestMapping(value = "${toClass.name}")
    @ResponseBody
    public Object ${toClass.name}(HttpServletRequest request, ${toClass.nameFU}RequestTO requestTO) {
        
        LOGGER.debug("请求ip【{}】，请求信息【{}】", new Object[] { request.getRemoteHost(), requestTO });

        try{
            //处理业务
            
            ${toClass.nameFU}ResponseTO responseTO = new ${toClass.nameFU}ResponseTO();
            return responseTO;
        }catch(Exception e){
            e.printStackTrace();
            LOGGER.error("${toClass.note}出现异常【{}】，请求ip【{}】，请求信息【{}】", new Object[] { e.getMessage(), request.getRemoteAddr(), requestTO });
            return FailResponseTO.newFailResponseTO();
        }
		
    }

</#list>



}
