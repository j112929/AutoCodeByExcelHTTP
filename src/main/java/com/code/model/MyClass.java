/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package com.code.model;

import com.code.model.abstractModel.AbstractField;
import java.util.ArrayList;
import java.util.List;

public class MyClass extends AbstractField
{
  public String packageName;
  public String moduleName;
  private List<Property> propertyList = new ArrayList();

  public String getPackageName()
  {
    return this.packageName; }

  public void setPackageName(String packageName) {
    this.packageName = packageName; }

  public String getModuleName() {
    return this.moduleName; }

  public void setModuleName(String moduleName) {
    this.moduleName = moduleName;
    this.packageName = moduleName.toLowerCase(); }

  public String getTime() {
    return this.time;
  }

  public String getTimeDay()
  {
    return this.timeDay;
  }

  public String getTimeMonth() {
    return this.timeMonth;
  }

  public String getTimeYear() {
    return this.timeYear; }

  public List<Property> getPropertyList() {
    return this.propertyList; }

  public void setPropertyList(List<Property> propertyList) {
    this.propertyList = propertyList;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAuthor() {
    return this.author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (obj instanceof MyClass) {
      MyClass myClass = (MyClass)obj;
      if (getName().equals(myClass.getName())) {
        return true;
      }
    }
    return false;
  }

  public int hashCode() {
    String pkStr = "" + getName();
    return pkStr.hashCode();
  }
}