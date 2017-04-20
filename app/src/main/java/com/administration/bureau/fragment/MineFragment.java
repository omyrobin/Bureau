package com.administration.bureau.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.administration.bureau.App;
import com.administration.bureau.BaseFragment;
import com.administration.bureau.R;
import com.administration.bureau.activity.CertificateActivity;
import com.administration.bureau.activity.RegisterActivity;
import com.administration.bureau.activity.VerificationActivity;
import com.administration.bureau.constant.Constant;
import com.administration.bureau.entity.eventbus.LanguageEvent;
import com.administration.bureau.entity.eventbus.UserLoginEvent;
import com.administration.bureau.entity.eventbus.UserLogoutEvent;
import com.administration.bureau.utils.SharedPreferencesUtil;
import com.administration.bureau.utils.ToastUtil;
import com.administration.bureau.widget.ContainerView;
import com.administration.bureau.widget.GroupDescript;
import com.administration.bureau.widget.OnRowClickListener;
import com.administration.bureau.widget.RowActionEnum;
import com.administration.bureau.widget.RowDescript;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by omyrobin on 2017/4/4.
 */

public class MineFragment extends BaseFragment implements OnRowClickListener {

    @BindView(R.id.container_v)
    ContainerView containerView;
    @BindView(R.id.log_out_tv)
    TextView logOutTv;
    @BindView(R.id.user_face_img)
    ImageView userFaceImg;
    @BindView(R.id.user_name_tv)
    TextView userNameTv;
    @BindView(R.id.user_status_tv)
    TextView userStatusTv;
    RadioGroup radioGroup;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initializeToolbar() {
        initUserView();
    }

    @Override
    protected void initContent(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        initContainerView();
    }

    private void initContainerView() {
        ArrayList<GroupDescript> groupDescripts = new ArrayList<>();
        ArrayList<RowDescript> descript = new ArrayList<>();

        descript.add(new RowDescript(R.mipmap.ic_launcher, getString(R.string.registration_information), RowActionEnum.MINE_REGIEST));
        descript.add(new RowDescript(R.mipmap.ic_launcher, getString(R.string.write_off_info), RowActionEnum.MINE_INFO_VER));
        descript.add(new RowDescript(R.mipmap.ic_launcher, getString(R.string.electronic_certificate), RowActionEnum.MINE_CERTIFICATE));
        descript.add(new RowDescript(R.mipmap.ic_launcher, getString(R.string.language_settings), RowActionEnum.MINE_LANGUAGE));
        descript.add(new RowDescript(R.mipmap.ic_launcher, getString(R.string.user_feedback), RowActionEnum.MINE_FEEDBACK));
        descript.add(new RowDescript(R.mipmap.ic_launcher, getString(R.string.about_us), RowActionEnum.MINE_ABOUTUS));
        groupDescripts.add(new GroupDescript(descript));

        containerView.initData(groupDescripts, this);
        containerView.notifyDataChange();

        logOutTv.setText(R.string.log_out);
    }

    @Subscribe
    public void onMessageEvent(LanguageEvent event){
        containerView.removeAllViews();
        initContainerView();
        initUserView();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            initUserView();
        }
    }

    private void initUserView(){
        if(!TextUtils.isEmpty(App.getInstance().chinese_name)){
            userNameTv.setText(App.getInstance().chinese_name);
        }
        if(App.getInstance().status == 3){
            userStatusTv.setText(R.string.electronic_certificate_download);
        }else if(App.getInstance().status == 0){
            userStatusTv.setText(R.string.wait_for_review);
        }else if(App.getInstance().status == -1){
            userStatusTv.setText(R.string.not_submit_registration);
        }
    }

    @Override
    public void onRowClick(RowActionEnum action) {
        Intent intent = null;
        switch (action){
            case MINE_REGIEST:
                if(App.getInstance().status == 3){
                    intent = new Intent(getActivity(), CertificateActivity.class);
                    startActivity(intent);
                }else if(App.getInstance().status == 0){
                    ToastUtil.showShort("请耐心等待审核");
                }else{
                    intent = new Intent(getActivity(), RegisterActivity.class);
                    startActivity(intent);
                }
                break;

            case MINE_INFO_VER:
                if(App.getInstance().status == 3){
                    intent = new Intent(getActivity(), VerificationActivity.class);
                    startActivity(intent);
                }else{
                    ToastUtil.showShort("您尚未生成电子证书");
                }
                break;

            case MINE_CERTIFICATE:
                if(App.getInstance().status == 3){
                    intent = new Intent(getActivity(), CertificateActivity.class);
                    startActivity(intent);
                }else{
                    ToastUtil.showShort("电子证书不可用");
                }
                break;

            case MINE_LANGUAGE:
                if(App.locale == 0 ){
                    App.locale = 1;
                } else{
                    App.locale = 0;
                }
                App.getInstance().initLocale();
                SharedPreferencesUtil.setParam(getActivity(),Constant.LOCALE,App.locale);
                EventBus.getDefault().post(new LanguageEvent());
                break;

            case MINE_FEEDBACK:

                break;

            case MINE_ABOUTUS:

                break;
        }
    }

    @OnClick(R.id.log_out_tv)
    public void logOut(){
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.hint))
                .setMessage(getString(R.string.whether_out_of_the_current_account))
                .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //销毁用户相关信息
                        App.getInstance().setUserEntity(null);
                        SharedPreferencesUtil.setParam(getActivity(), Constant.USER_ID, -1);
                        SharedPreferencesUtil.setParam(getActivity(), Constant.USER_PHONE, "");
                        SharedPreferencesUtil.setParam(getActivity(), Constant.USER_TOKEN, "");
                        //销毁用户提交资料
                        App.getInstance().status = -1;
                        App.getInstance().certificate_image = "";
                        App.getInstance().chinese_name = "";
                        App.getInstance().reject_reason = "";
                        App.getInstance().id = -1;

                        changeUserInfo();

                        //TODO 通知刷新
                        EventBus.getDefault().post(new UserLogoutEvent());
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(getString(R.string.cancle),null).create();
        dialog.show();
    }

    private void changeUserInfo(){
        App.getInstance().status = -1;
        App.getInstance().chinese_name = "";
        App.getInstance().certificate_image = "";
        initUserView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
