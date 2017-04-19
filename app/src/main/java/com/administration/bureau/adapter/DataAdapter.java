package com.administration.bureau.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.administration.bureau.R;
import com.administration.bureau.entity.DataEntity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by omyrobin on 2017/4/18.
 */

public class DataAdapter extends BaseAdapter {

    private Context context;

    private ArrayList<DataEntity> datas;

    public DataAdapter(Context context, ArrayList<DataEntity> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas != null ? datas.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return datas != null ? datas.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DataAdapter.DataViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_spinner_data, null);
            holder = new DataAdapter.DataViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (DataAdapter.DataViewHolder) convertView.getTag();
        }
        holder.spinnerDataTv.setText(datas.get(position).getValue());
        return convertView;
    }

    class DataViewHolder{
        @BindView(R.id.spinner_data_tv)
        TextView spinnerDataTv;

        public DataViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }
}
