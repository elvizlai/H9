package com.elvizlai.h9.activity.loading.entity;

public class TelPhoneInfo
{
  private String phoneDeviceId;
  private int phoneMacType;
  private String phoneModel;
  
  public String getPhoneDeviceId()
  {
    return this.phoneDeviceId;
  }
  
  public int getPhoneMacType()
  {
    return this.phoneMacType;
  }
  
  public String getPhoneModel()
  {
    return this.phoneModel;
  }
  
  public void setPhoneDeviceId(String paramString)
  {
    this.phoneDeviceId = paramString;
  }
  
  public void setPhoneMacType(int paramInt)
  {
    this.phoneMacType = paramInt;
  }
  
  public void setPhoneModel(String paramString)
  {
    this.phoneModel = paramString;
  }
}