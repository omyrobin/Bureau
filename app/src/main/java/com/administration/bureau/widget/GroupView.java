package com.administration.bureau.widget;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.administration.bureau.R;

public class GroupView extends LinearLayout{

	private ArrayList<RowDescript> descripts;
	
	private Context context;

	private OnRowClickListener listener;	

	public GroupView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initView();
	}

	public GroupView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
	}

	public GroupView(Context context) {
		super(context);
		this.context = context;
		initView();
	}
	
	public void initView(){
		setOrientation(VERTICAL);
		setBackgroundResource(android.R.color.white);
	}
	
	public void initData(GroupDescript descript, OnRowClickListener listener){
		this.descripts = descript.descripts;
		this.listener = listener;
	}
	
	public void notifDataChange(){
		if(descripts!=null && descripts.size()>0){
			RowView row = null;
			View line = null;
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,1);
			params.leftMargin = 10;
			for (int i = 0; i < descripts.size(); i++) {
				RowDescript descript = descripts.get(i);
				row = new RowView(context);
				row.initData(descript,listener);
				row.notifDataChange();
				addView(row);
				if(i != descripts.size()-1){
					line = new View(context);
					line.setBackgroundResource(R.color.colorDivider);
					addView(line,params);
				}
			}
		}else{
			setVisibility(View.GONE);
		}
	}
	
}
