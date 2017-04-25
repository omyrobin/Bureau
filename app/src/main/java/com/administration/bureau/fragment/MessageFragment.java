package com.administration.bureau.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.administration.bureau.App;
import com.administration.bureau.BaseFragment;
import com.administration.bureau.R;
import com.administration.bureau.constant.Constant;
import com.administration.bureau.entity.BaseResponse;
import com.administration.bureau.entity.MessageEntity;
import com.administration.bureau.entity.eventbus.LanguageEvent;
import com.administration.bureau.http.ProgressSubscriber;
import com.administration.bureau.http.RetrofitClient;
import com.administration.bureau.http.RetrofitManager;
import com.administration.bureau.model.GetService;
import com.administration.bureau.widget.EmptyRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;
import rx.Observable;

/**
 * Created by omyrobin on 2017/4/4.
 */

public class MessageFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title_tv)
    TextView titleTv;
    @BindView(R.id.message_rv)
    EmptyRecyclerView messageRv;
    @BindView(R.id.message_re_layout)
    SwipeRefreshLayout messageRelayout;
    @BindView(R.id.empty_view_layout)
    View emptyViewLayout;
    @BindView(R.id.empty_view_tv)
    TextView emptyViewTv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    protected void initializeToolbar() {
        setLanguageText();
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public void setLanguageText(){
        titleTv.setText(R.string.message);
        emptyViewTv.setText(R.string.no_message);
    }

    @Subscribe
    public void onMessageEvent(LanguageEvent event){
        setLanguageText();
    }

    @Override
    protected void initContent(@Nullable Bundle savedInstanceState) {
        initRefreshLayout();
        initLayoutManager();
        autoRefresh();
    }

    private void initRefreshLayout(){
        messageRelayout.setColorSchemeResources(R.color.colorPrimary,R.color.colorPrimary,R.color.colorPrimary);
        messageRelayout.setOnRefreshListener(this);
    }

    private void initLayoutManager(){
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        messageRv.setLayoutManager(manager);
    }

    private void autoRefresh(){
        messageRelayout.post(new Runnable() {
            @Override
            public void run() {
                messageRelayout.setRefreshing(true);
                requestMessageData();
            }
        });
    }

    private void requestMessageData(){
        if(App.getInstance().getUserEntity() == null){
            messageRelayout.setRefreshing(false);
            emptyViewLayout.setVisibility(View.VISIBLE);
            return;
        }
        GetService getService = RetrofitManager.getRetrofit().create(GetService.class);
        int user_id = App.getInstance().getUserEntity().getUser().getId();
        String token = "Bearer "+ App.getInstance().getUserEntity().getToken();
        Observable<Response<BaseResponse<MessageEntity>>> observable = getService.getMessage(user_id, token, Constant.languages[App.locale]);
        RetrofitClient.client().request(observable, new ProgressSubscriber<MessageEntity>(getActivity()) {
            @Override
            protected void onSuccess(MessageEntity messageEntity) {
                if(messageEntity.getData() == null || messageEntity.getData().isEmpty()){
                    emptyViewLayout.setVisibility(View.VISIBLE);
                }else{
                    emptyViewLayout.setVisibility(View.GONE);
                }
                messageRv.setAdapter(new MessageAdapter(messageEntity));
                messageRelayout.setRefreshing(false);
            }

           @Override
            protected void onFailure(String message) {
                messageRelayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        messageRelayout.setRefreshing(true);
        requestMessageData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
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
