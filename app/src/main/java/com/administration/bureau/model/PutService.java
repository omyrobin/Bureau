package com.administration.bureau.model;

import com.administration.bureau.entity.BaseResponse;
import com.administration.bureau.entity.UserRegisterInfoEntity;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by omyrobin on 2017/4/30.
 */

public interface PutService {

    @PUT("user/{user_id}/registrant/{id}")
    @FormUrlEncoded
    Observable<Response<BaseResponse<Boolean>>> registerInfoAgain(@Path("user_id") int user_id, @Path("id") int id,@FieldMap Map<String, Object> params, @Header("Authorization") String token);
}

