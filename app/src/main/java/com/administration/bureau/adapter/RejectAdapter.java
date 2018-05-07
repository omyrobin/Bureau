package com.administration.bureau.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.administration.bureau.R;
import com.administration.bureau.entity.RejectEntity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wubo on 2018/5/7.
 */

public class RejectAdapter extends RecyclerView.Adapter<RejectAdapter.RejectHolder> {

    private ArrayList<RejectEntity> rejectEntities;

    public RejectAdapter(ArrayList<RejectEntity> rejectEntities) {
        this.rejectEntities = rejectEntities;
    }

    @Override
    public RejectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View  itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reject_layout, parent, false);
        return new RejectHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RejectHolder holder, int position) {
        int num = position + 1;
        holder.labelTv.setText(num + ". " + rejectEntities.get(position).getLabel());
        holder.valueTv.setText(rejectEntities.get(position).getValue());
    }

    @Override
    public int getItemCount() {
        return rejectEntities!=null ? rejectEntities.size() : 0;
    }

    class RejectHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.label_tv)
        TextView labelTv;
        @BindView(R.id.value_tv)
        TextView valueTv;

        public RejectHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
