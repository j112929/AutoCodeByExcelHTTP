/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package com.code.model;

import java.util.ArrayList;
import java.util.List;

public class Controller extends MyClass
{
  private List<TO> toList;

  public Controller()
  {
    this.toList = new ArrayList(); }

  public List<TO> getToList() {
    return this.toList;
  }

  public void setToList(List<TO> toList) {
    this.toList = toList;
  }
}