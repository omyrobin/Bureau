package com.administration.bureau.widget;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class ContainerView extends LinearLayout{

	private Context context;
	private ArrayList<GroupDescript> groupDescripts;
	private OnRowClickListener listener;

	public ContainerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public ContainerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public ContainerView(Context context) {
		super(context);
		initView(context);
	}
	
	public void initView(Context context){
		this.context = context;
		setOrientation(VERTICAL);
	}
	
	public void initData(ArrayList<GroupDescript> groupDescripts,OnRowClickListener listener){
		this.groupDescripts = groupDescripts;
		this.listener = listener;
	}

	public void notifyDataChange() {
		if(groupDescripts != null && groupDescripts.size()>0){
			GroupView group = null;
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);;
			params.topMargin = 10;
			for (int i = 0; i < groupDescripts.size(); i++) {
				group = new GroupView(context);
				group.initData(groupDescripts.get(i), listener);
				group.notifDataChange();
				addView(group,params);
			}
			setVisibility(View.VISIBLE);
		}else{
			setVisibility(View.GONE);
		}
	}
}
