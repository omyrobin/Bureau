package com.administration.bureau.model;

import com.administration.bureau.entity.ArticleEntity;
import com.administration.bureau.entity.BannerEntity;
import com.administration.bureau.entity.BaseResponse;
import com.administration.bureau.entity.MessageEntity;
import com.administration.bureau.entity.SpinnerData;
import com.administration.bureau.entity.UserRegisterInfoEntity;

import java.util.ArrayList;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

public interface GetService {
    @GET
    Observable<Response<BaseResponse<SpinnerData>>> getSpinnerData(@Url String config);

    @GET
    Observable<Response<BaseResponse<ArrayList<BannerEntity>>>> getBanner(@Url String banner);

    @GET
    Observable<Response<BaseResponse<ArticleEntity>>> getArticle(@Url String banner);

    @GET("user/{user_id}/registrant")
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<Response<BaseResponse<UserRegisterInfoEntity>>> getStatus(@Path("user_id") int user_id, @Header("Authorization") String token);

    @GET("user/{user_id}/notification")
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<Response<BaseResponse<MessageEntity>>> getMessage(@Path("user_id") int user_id, @Header("Authorization") String token);

    @GET
    Observable<Response<BaseResponse<ArticleEntity>>> getLawsArticle(@Url String banner, @Query("type") int type);
}