package com.administration.bureau.widget.pic;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.administration.bureau.R;


public class SelectPicDialog implements OnClickListener{

	private Context context;
	
	private Dialog mDialog;
	
	private DisplayMetrics display;
	
	private TextView mSelectTakePhotoTv, mSelectPhotoAlbumTv, mSelectCancelTv;
	
	private ISelectPic mISelectPic;

	private View mDialogContentView;
	
	public SelectPicDialog(Context context, ISelectPic mISelectPic) {
		super();
		this.context = context;
		this.mISelectPic = mISelectPic;
		display = context.getResources().getDisplayMetrics();
		initView();
		setListener();
	}

	private SelectPicDialog initView(){
		mDialog = new Dialog(context, R.style.BaseDiaLog_Select);
		mDialogContentView = LayoutInflater.from(context).inflate(R.layout.widget_select_pic, null);
		mDialogContentView.setMinimumWidth(display.widthPixels);
		mDialog.setContentView(mDialogContentView);
		
		Window dialogWindow = mDialog.getWindow();
		dialogWindow.setGravity(Gravity.START | Gravity.BOTTOM);
		return this;
	}
	
	private void setListener(){
		mSelectTakePhotoTv = (TextView) mDialogContentView.findViewById(R.id.mSelectTakePhotoTv);
		mSelectPhotoAlbumTv = (TextView) mDialogContentView.findViewById(R.id.mSelectPhotoAlbumTv);
		mSelectCancelTv = (TextView) mDialogContentView.findViewById(R.id.mSelectCancelTv);
		
		mSelectTakePhotoTv.setOnClickListener(this);
		mSelectPhotoAlbumTv.setOnClickListener(this);
		mSelectCancelTv.setOnClickListener(this);
	}
	
	
	public void show(){
		mDialog.show();
	}
	
	public void dismiss(){
		mDialog.dismiss();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mSelectTakePhotoTv:
			mISelectPic.selectOneItem();
			break;
			
		case R.id.mSelectPhotoAlbumTv:
			mISelectPic.selectTwoItem();;
			break;

		default:
			mDialog.dismiss();
			break;
		}
		
	}
}
