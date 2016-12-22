/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package com.code.model;

import com.code.model.abstractModel.AbstractField;
import java.util.ArrayList;
import java.util.List;

public class Module extends AbstractField
{
  private List<Controller> controllerList;

  public Module()
  {
    this.controllerList = new ArrayList(); }

  public List<Controller> getControllerList() {
    return this.controllerList;
  }

  public void setControllerList(List<Controller> controllerList) {
    this.controllerList = controllerList;
  }

  public boolean containController(String controllerName)
  {
    for (Controller c : this.controllerList) {
      if (c.getName().equalsIgnoreCase(controllerName)) {
        return true;
      }
    }
    return false;
  }
}