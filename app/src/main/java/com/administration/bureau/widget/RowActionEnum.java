package com.administration.bureau.widget;



public enum RowActionEnum {
	
	MINE_REGIEST(0,"注册登记资料"),
	MINE_INFO_VER(1,"信息核销"),
	MINE_CERTIFICATE(2,"电子证书"),
    MINE_LANGUAGE(3,"语言设置"),
	MINE_FEEDBACK(4,"用户反馈"),
	MINE_ABOUTUS(5,"关于我们");

	
	
	private final int value;  
	private final String lable; 
    public int getValue() {  
        return value;  
    }  
    public String getLable(){
    	 return lable;  
    }
    //构造器默认也只能是private, 从而保证构造函数只能在内部使用  
    RowActionEnum(int value,String lable) {  
        this.value = value;
        this.lable = lable;
    }  

}
