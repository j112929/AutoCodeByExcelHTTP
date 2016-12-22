package com.bluemobi.to.api.${packageName};

<#assign hasList = "" />
<#assign hasBigDecimal = "" />
<#assign hasDate = "" />
<#list propertyList as pro>
    <#if pro.type?contains("[]")>
        <#assign hasList = "true" />
    <#elseif pro.type == "BigDecimal">
        <#assign hasBigDecimal = "true" />
    <#elseif pro.type == "Date">
        <#assign hasDate = "true" />
    </#if>
</#list>
<#if hasList == "true">
import java.util.ArrayList;
import java.util.List;
</#if>

<#if hasBigDecimal == "true">
import java.math.BigDecimal;
</#if>
<#if hasDate == "true">
import java.util.Date;
</#if>

import com.appcore.model.AbstractObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
<#list propertyList as pro>
	<#if pro.type == "object" || pro.type == "object[]"  >
import com.bluemobi.to.api.${pro.javaType}TO;
	</#if>
</#list>

/**
 * 【${note}】${protocolType}
 * @author ${author} ${email}
 */
@JsonIgnoreProperties
public class ${nameFU}${protocolType}TO extends AbstractObject {

	private static final long serialVersionUID = 1L;

<#list propertyList as pro>
	//${pro.note}
	<#if pro.type == "object">
	private ${pro.javaType}TO ${pro.name} = new ${pro.javaType}TO();
	<#elseif pro.type == "object[]">
    private List<${pro.javaType}TO> ${pro.name} = new ArrayList<${pro.javaType}TO>();
    <#elseif pro.type?contains("[]")>
    private List<${pro.javaType?replace("[]","")}> ${pro.name} = new ArrayList<${pro.javaType?replace("[]","")}>();
    <#else>
	private ${pro.javaType} ${pro.name};
	</#if>
</#list>


<#list propertyList as pro>
	<#assign t = "" />
	<#if pro.type == "object">
		<#assign t = pro.javaType+"TO" />
	<#elseif pro.type == "object[]">
		<#assign t = "List<"+pro.javaType+"TO>" />
	<#elseif pro.type?contains("[]")>
        <#assign t = "List<"+pro.javaType?replace("[]","")+">" />
	<#else>
		<#assign t = pro.javaType />
	</#if>
    /**设置${pro.note}*/
	public void set${pro.nameFU}(${t} ${pro.name}){
		this.${pro.name}=${pro.name};
	}
	/**获取${pro.note}*/
	public ${t} get${pro.nameFU}(){
		return this.${pro.name};
	}
</#list>


}
