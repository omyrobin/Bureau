package com.administration.bureau.model;

import com.administration.bureau.App;
import com.administration.bureau.entity.BaseResponse;
import com.administration.bureau.entity.UploadEntity;
import com.administration.bureau.entity.UserEntity;
import com.administration.bureau.entity.UserRegisterInfoEntity;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by omyrobin on 2017/4/16.
 */

public interface PostService {

    @POST
    @Multipart
    Observable<Response<BaseResponse<UploadEntity>>> uploadFile(@Url String url, @Part MultipartBody.Part file, @Header("Authorization") String token);

    @POST
    @FormUrlEncoded
    Observable<Response<BaseResponse<UserEntity>>> registerUser(@Url String url, @Field("phone") String page, @Field("verify_code") String verify_code);

    @POST("user/{user_id}/registrant")
    @FormUrlEncoded
    Observable<Response<BaseResponse<UserRegisterInfoEntity>>> registerInfo(@Path("user_id") int user_id, @FieldMap Map<String, Object> params, @Header("Authorization") String token);
}
