package com.administration.bureau.http;

import com.administration.bureau.entity.BaseResponse;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action0;

/**
 * Created by omyrobin on 2017/4/13.
 */

public class RetrofitClient {

    private static volatile RetrofitClient client;

    public static RetrofitClient client(){
        if(client == null){
            synchronized (RetrofitClient.class){
                if(client == null){
                    client = new RetrofitClient();
                }
            }
        }
        return client;
    }

    public <T> void request(Observable<Response<BaseResponse<T>>> ob, final ProgressSubscriber<T> subscriber){
        //数据预处理
        Observable.Transformer<Response<BaseResponse<T>>, T> result = ResponseHelper.transformerResult();
        ob.compose(result)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        subscriber.showProgressDialog();
                    }
                })
                .subscribe(subscriber);
    }

    public <T> void requestString(Observable<Response<T>> ob, final ProgressSubscriber<T> subscriber){
        //数据预处理
        Observable.Transformer<Response<T>, T> result = ResponseHelper.transformerResult2();
        ob.compose(result).subscribe(subscriber);
    }

}
