//
//  ${nameFU}BaseClass.h
//  LeDemo
//
// 【${note}响应】
//  @author CCJ ${author} ${email}
//

#import <Foundation/Foundation.h>

<#list propertyList as pro>
	<#if pro.type == "object" || pro.type == "object[]"  >
#import "${pro.javaType}.h"
	</#if>
</#list>

@interface ${nameFU}BaseClass : NSObject

<#list propertyList as pro>
//${pro.note}
	<#if pro.type == "object">
@property (nonatomic, strong) ${pro.javaType} *${pro.name};
	<#elseif pro.type == "object[]"> 
@property (nonatomic, strong) NSArray *${pro.name};
    <#elseif pro.type?contains("[]")>
@property (nonatomic, strong) NSArray *${pro.name};  
    <#elseif pro.type == "Date">
@property (nonatomic, assign) double ${pro.name};
    <#elseif pro.type == "String">
@property (nonatomic, strong) NSString *<#if pro.name == "id">u</#if>${pro.name};
    <#elseif pro.type == "int">
@property (nonatomic, assign) int <#if pro.name == "id">u</#if>${pro.name};
	<#else>
@property (nonatomic, assign) ${pro.type} <#if pro.name == "id">u</#if>${pro.name};
	</#if>
</#list>

- (instancetype)initWithDictionary:(NSDictionary *)dic;

@end
