package com.administration.bureau.activity;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.administration.bureau.BaseActivity;
import com.administration.bureau.R;
import com.administration.bureau.adapter.RejectAdapter;

import butterknife.BindView;

/**
 * Created by wubo on 2018/5/7.
 */

public class RejectActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title_tv)
    TextView titleTv;
    @BindView(R.id.reject_rv)
    RecyclerView rejectRv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_reject;
    }

    @Override
    protected void initializeToolbar() {
        titleTv.setText(R.string.reasons_for_rejection);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void initializeActivity(Bundle savedInstanceState) {
        initRecyclerView();
    }

    private void initRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rejectRv.setLayoutManager(layoutManager);
        RejectAdapter adapter = new RejectAdapter(infoEntity.getReject_fields());
        rejectRv.setAdapter(adapter);
    }
}
