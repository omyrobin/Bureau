package com.administration.bureau.model;

import com.administration.bureau.entity.BaseResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by omyrobin on 2017/4/19.
 */

public interface DeleteService {

    @DELETE("user/{user_id}/registrant/{id}")
    Observable<Response<BaseResponse<Boolean>>> deleteInfo(@Path("user_id") int user_id, @Path("id") int id, @QueryMap Map<String,Object> queryMap, @Header("Authorization") String token);
}
