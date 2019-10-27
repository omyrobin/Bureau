package com.administration.bureau.model;

import com.administration.bureau.entity.BaseResponse;
import com.administration.bureau.entity.SpinnerData;
import com.administration.bureau.entity.TraceEntity;
import com.administration.bureau.entity.UserRegisterInfoEntity;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by omyrobin on 2017/4/30.
 */

public interface PutService {

    @PUT("user/{user_id}/registrant/{id}")
    @FormUrlEncoded
    Observable<Response<BaseResponse<Boolean>>> registerInfoAgain(@Path("user_id") int user_id, @Path("id") int id,@FieldMap Map<String, Object> params, @Header("Authorization") String token);

    @PUT("user/{id}")
    @FormUrlEncoded
    Observable<Response<BaseResponse<String>>> putUseUpdate(@Path ("id") int id, @FieldMap Map<String, Object> params, @Header("Authorization") String token);

    @PUT("user/location/{id}")
    @FormUrlEncoded
    Observable<Response<BaseResponse<TraceEntity>>> updateLocation(@Path ("id") int id, @Field("longitude") Double longitude, @Field("latitude") Double latitude, @Header("Authorization") String token);
}

