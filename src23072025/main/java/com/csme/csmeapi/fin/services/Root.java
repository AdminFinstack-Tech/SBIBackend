package com.csme.csmeapi.fin.services;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"func"})
@XmlRootElement(name = "root")
public class Root {
  @XmlElement(required = true)
  public List<Func> func;
  
  public List<Func> getFunc() {
    if (this.func == null)
      this.func = new ArrayList<>(); 
    return this.func;
  }
  
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "", propOrder = {"value"})
  public static class Func {
    @XmlValue
    protected String value;
    
    @XmlAttribute(name = "desc", required = true)
    protected String desc;
    
    @XmlAttribute(name = "funcType", required = true)
    protected String funcType;
    
    @XmlAttribute(name = "id", required = true)
    protected String id;
    
    @XmlAttribute(name = "item", required = true)
    protected String item;
    
    @XmlAttribute(name = "module", required = true)
    protected String module;
    
    @XmlAttribute(name = "moduleDesc", required = true)
    protected String moduleDesc;
    
    @XmlAttribute(name = "product", required = true)
    protected String product;
    
    public String getValue() {
      return this.value;
    }
    
    public void setValue(String value) {
      this.value = value;
    }
    
    public String getDesc() {
      return this.desc;
    }
    
    public void setDesc(String value) {
      this.desc = value;
    }
    
    public String getFuncType() {
      return this.funcType;
    }
    
    public void setFuncType(String value) {
      this.funcType = value;
    }
    
    public String getId() {
      return this.id;
    }
    
    public void setId(String value) {
      this.id = value;
    }
    
    public String getItem() {
      return this.item;
    }
    
    public void setItem(String value) {
      this.item = value;
    }
    
    public String getModule() {
      return this.module;
    }
    
    public void setModule(String value) {
      this.module = value;
    }
    
    public String getModuleDesc() {
      return this.moduleDesc;
    }
    
    public void setModuleDesc(String value) {
      this.moduleDesc = value;
    }
    
    public String getProduct() {
      return this.product;
    }
    
    public void setProduct(String value) {
      this.product = value;
    }
    
    public String toString() {
      return "Func [value=" + this.value + ", desc=" + this.desc + ", funcType=" + this.funcType + ", id=" + this.id + ", item=" + this.item + ", module=" + this.module + ", moduleDesc=" + this.moduleDesc + ", product=" + this.product + "]";
    }
  }
}
