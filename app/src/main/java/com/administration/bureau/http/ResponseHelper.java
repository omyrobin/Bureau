package com.administration.bureau.http;

import android.util.Log;

import com.administration.bureau.entity.BaseResponse;
import com.administration.bureau.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ResponseHelper {
    /**
     * 对结果进行Transformer处理
     *
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<Response<BaseResponse<T>>, T> transformerResult() {
        return new Observable.Transformer<Response<BaseResponse<T>>, T>() {
            @Override
            public Observable<T> call(Observable<Response<BaseResponse<T>>> tObservable) {
                return tObservable.flatMap(new Func1<Response<BaseResponse<T>>, Observable<T>>() {
                    @Override
                    public Observable<T> call(Response<BaseResponse<T>> result) {
                        if (result.body().getCode() == 0) {
                            return createData(result.body().getData());
                        }else {
//                            Log.i("result", result.body().getMsg());
                            return Observable.error(new RuntimeException(result.body().getMsg()));
                        }
                    }
                }).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 创建成功的数据
     *
     * @param data
     * @param <T>
     * @return
     */
    private static <T> Observable<T> createData(final T data) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    subscriber.onNext(data);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }


    /**
            * 对结果进行Transformer处理
     *
             * @param <T>
     * @return
             */
    public static <T> Observable.Transformer<Response<T>, T> transformerResult2() {
        return new Observable.Transformer<Response<T>, T>() {
            @Override
            public Observable<T> call(Observable<Response<T>> tObservable) {
                return tObservable.flatMap(new Func1<Response<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(Response<T> result) {
                        JSONObject baseData = null;
                        try {
                            baseData = new JSONObject(result.body().toString());
                            if(baseData.getInt("code") == 0){
                                return createData(result.body());
                            }else{
                                return Observable.error(new RuntimeException(baseData.get("msg").toString()));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}