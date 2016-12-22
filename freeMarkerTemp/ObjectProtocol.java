package com.bluemobi.protocol;

import com.bluemobi.message.response.*;

/**
 * ${note} 消息
 * @author ${author} E-mail:${email}
 * @date ${timeObj.time}
 */
public class ${nameFU}Protocol {

<#list classList as pClass>
	
	/**
	 * 创建 ${pClass.note} 消息对象 【${pClass.protocolNum}】
	 * @author ${author} E-mail:${email}
	 * @date ${timeObj.time} 
	 * @param obj
	 * @return
	 * @return ${pClass.protocolType}${pClass.nameFU}Msg
	 */
	public static ${pClass.protocolType}${pClass.nameFU}Msg new${pClass.protocolType}${pClass.nameFU}Msg(){
		
		${pClass.protocolType}${pClass.nameFU}Msg msg = new ${pClass.protocolType}${pClass.nameFU}Msg();
		
	<#list pClass.propertyList as pro>
		//msg.set${pro.nameFU}(obj);//${pro.note}
	</#list>
		
		return msg;
		
	}

</#list>
	
}
