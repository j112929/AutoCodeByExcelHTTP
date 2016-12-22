package com.bluemobi.protocol;

import java.util.ArrayList;
import java.util.List;
import java.util.List;

import com.bluemobi.message.struct.${nameFU}Msg;

/**
 * ${note} 数据结构
 * @author ${author} E-mail:${email}
 * @date ${timeMonth} 
 */
public class ${nameFU}StructProtocol {

	/**
	 * 创建单个${note}消息对象
	 * @author ${author} E-mail:${email}
	 * @date ${timeMonth} 
	 * @param ${name}
	 * @return
	 * @return ${nameFU}Msg
	 */
	public static ${nameFU}Msg new${nameFU}Msg(){
		
		${nameFU}Msg msg = new ${nameFU}Msg();

	<#list propertyList as pro>
		//msg.set${pro.nameFU}();//${pro.note}
	</#list>

		return msg;
	}

	/**
	 * 创建多个${note}消息对象
	 * @author ${author} E-mail:${email}
	 * @date ${timeMonth} 
	 * @param ${name}Coll
	 * @return
	 * @return List<${nameFU}Msg>
	 */
	public static List<${nameFU}Msg> new${nameFU}Msgs(List ${name}Coll){
		
		List<${nameFU}Msg> msgList = new ArrayList<${nameFU}Msg>();
		
		/*for(${nameFU} ${name} : ${name}Coll){
			${nameFU}Msg msg = ${nameFU}StructProtocol.new${nameFU}Msg(${name});
			msgList.add(msg);
		}*/
		
		return msgList;
	}
	
	

}
