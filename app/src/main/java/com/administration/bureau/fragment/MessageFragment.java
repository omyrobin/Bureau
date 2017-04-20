package com.administration.bureau.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.administration.bureau.App;
import com.administration.bureau.BaseFragment;
import com.administration.bureau.R;
import com.administration.bureau.constant.Constant;
import com.administration.bureau.entity.BannerEntity;
import com.administration.bureau.entity.BaseResponse;
import com.administration.bureau.entity.MessageEntity;
import com.administration.bureau.http.ProgressSubscriber;
import com.administration.bureau.http.RetrofitClient;
import com.administration.bureau.http.RetrofitManager;
import com.administration.bureau.model.GetService;
import com.administration.bureau.utils.ToastUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;
import rx.Observable;

/**
 * Created by omyrobin on 2017/4/4.
 */

public class MessageFragment extends BaseFragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title_tv)
    TextView titleTv;
    @BindView(R.id.message_rv)
    RecyclerView messageRv;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    protected void initializeToolbar() {
        titleTv.setText("消息");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    protected void initContent(@Nullable Bundle savedInstanceState) {
        initLayoutManager();
        requestMessageData();
    }

    private void initLayoutManager(){
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        messageRv.setLayoutManager(manager);
    }

    private void requestMessageData(){
        if(App.getInstance().getUserEntity() == null)
            return;
        GetService getService = RetrofitManager.getRetrofit().create(GetService.class);
        int user_id = App.getInstance().getUserEntity().getUser().getId();
        String token = "Bearer "+ App.getInstance().getUserEntity().getToken();
        Observable<Response<BaseResponse<MessageEntity>>> observable = getService.getMessage(user_id, token, Constant.languages[App.locale]);
        RetrofitClient.client().request(observable, new ProgressSubscriber<MessageEntity>(getActivity()) {
            @Override
            protected void onSuccess(MessageEntity messageEntity) {
                messageRv.setAdapter(new MessageAdapter(messageEntity));
            }

            @Override
            protected void onFailure(String message) {

            }
        });
    }

    class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder>{

        private MessageEntity messageEntity;

        public MessageAdapter(MessageEntity messageEntity) {
            this.messageEntity = messageEntity;
        }

        @Override
        public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_message, null);
            return new MessageViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MessageViewHolder holder, int position) {
            holder.messageTv.setText(messageEntity.getData().get(position).getData().getMessage());
            holder.createdDateTv.setText(messageEntity.getData().get(position).getData().getCreated_at());
        }


        @Override
        public int getItemCount() {
            return messageEntity != null ? messageEntity.getData().size() : 0;
        }
    }

    class MessageViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.message_tv)
        TextView messageTv;
        @BindView(R.id.created_date_tv)
        TextView createdDateTv;


        public MessageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
