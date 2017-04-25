package com.administration.bureau;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.administration.bureau.entity.UserRegisterInfoEntity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by omyrobin on 2017/4/4.
 */

public abstract class BaseFragment extends Fragment{

    protected View contentView;

    protected Unbinder unBinder;

    protected UserRegisterInfoEntity infoEntity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(getLayoutId(),null);
        unBinder = ButterKnife.bind(this, contentView);
        infoEntity = App.getInstance().getInfoEntity();
        initializeToolbar();
        initContent(savedInstanceState);
        return contentView;
    }

    protected abstract int getLayoutId();

    protected abstract void initializeToolbar();

    protected abstract void initContent(@Nullable Bundle savedInstanceState);

    @Override
    public void onDestroy() {
        super.onDestroy();
        unBinder.unbind();
    }
}
