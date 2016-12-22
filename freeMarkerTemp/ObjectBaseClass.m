//
//  ${nameFU}BaseClass.m
//  LeDemo
//
// 【${note}响应】
//  @author CCJ ${author} ${email}
//

#import "${nameFU}BaseClass.h"

@implementation ${nameFU}BaseClass

- (instancetype)initWithDictionary:(NSDictionary *)dic{
    
    self = [super init];
    
    if (self) {
<#list propertyList as pro>
	//${pro.note}
    <#if pro.type == "object"> 
	self.${pro.name} = [[${pro.javaType} alloc] initWithDictionary:dic[@"${pro.name}"]];
    <#elseif pro.type == "object[]"> 
	self.${pro.name} = [NSArray arrayWithObject:[dic objectForKey:@"${pro.name}"]];
    <#elseif pro.type?contains("[]")>
	self.${pro.name} = [NSArray arrayWithObject:[dic objectForKey:@"${pro.name}"]];
	<#elseif pro.type == "Date">
	self.${pro.name} = [NSString stringWithFormat:@"%@",[self objectOrNilForKey:@"${pro.name}" fromDictionary:dic]] doubleValue];
    <#elseif pro.type == "String">
	self.<#if pro.name == "id">u</#if>${pro.name} = [NSString stringWithFormat:@"%@",[self objectOrNilForKey:@"${pro.name}" fromDictionary:dic]];
    <#elseif pro.type == "int">
	self.<#if pro.name == "id">u</#if>${pro.name} = [[self objectOrNilForKey:@"${pro.name}" fromDictionary:dic] intValue];
    <#else>
	self.<#if pro.name == "id">u</#if>${pro.name} = [NSString stringWithFormat:@"%@",[self objectOrNilForKey:@"${pro.name}" fromDictionary:dic]];
    </#if>
</#list>
    }
    
    return self;
    
}

- (id)objectOrNilForKey:(id)aKey fromDictionary:(NSDictionary *)dict
{
    id object = [dict objectForKey:aKey];
    return [object isEqual:[NSNull null]] ? nil : object;
}


@end
