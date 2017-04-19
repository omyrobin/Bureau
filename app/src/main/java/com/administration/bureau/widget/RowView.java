package com.administration.bureau.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.administration.bureau.R;

public class RowView extends LinearLayout implements OnClickListener{
	
	private Context context;

	private ImageView rowIconImg;
	
	private TextView rowLableTv;
	
	private ImageButton rowActionBtn;

	private OnRowClickListener listener;

	private RowDescript descript;

	public RowView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initView();
	}

	public RowView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
	}

	public RowView(Context context) {
		super(context);
		this.context = context;
		initView();
	}
	
	public void initView(){
		LayoutInflater.from(context).inflate(R.layout.widget_rowview, this);
		rowIconImg = (ImageView) findViewById(R.id.row_icon_img);
		rowLableTv = (TextView) findViewById(R.id.row_lable_tv);
		rowActionBtn = (ImageButton) findViewById(R.id.row_action_btn);
	}
	
	@Override
	public void onClick(View v) {
		if(listener!=null){
			listener.onRowClick(descript.action);
		}
	}

	public void initData(RowDescript descript, OnRowClickListener listener) {
		this.descript = descript;
		this.listener = listener;
	}

	public void notifDataChange() {
		if(descript!=null){
			if(descript.iconResId==0){
				rowIconImg.setVisibility(View.GONE);
			}else{
				rowIconImg.setBackgroundResource(descript.iconResId);
			}
			rowLableTv.setText(descript.lable);
			if(descript.action !=null){
				setOnClickListener(this);
			}
			if(descript.visable==INVISIBLE){
				rowActionBtn.setVisibility(View.INVISIBLE);
			}else{
				rowActionBtn.setImageResource(R.drawable.more);
			}
		}
		
	}

}
