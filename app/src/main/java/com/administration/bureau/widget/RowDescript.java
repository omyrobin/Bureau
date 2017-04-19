package com.administration.bureau.widget;

public class RowDescript {
	public int iconResId;
	public String lable;
	public RowActionEnum action;
	public int visable;
	
	public RowDescript(int iconResId, String lable, RowActionEnum action) {
		super();
		this.iconResId = iconResId;
		this.lable = lable;
		this.action = action;
	}

	public RowDescript(int iconResId, String lable, RowActionEnum action, int visable) {
		super();
		this.iconResId = iconResId;
		this.lable = lable;
		this.action = action;
		this.visable = visable;
	}
	
	
	
	
}
