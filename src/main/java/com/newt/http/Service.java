package com.newt.http;

import com.google.gson.JsonElement;
import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

public interface Service {
    @POST("{url}")
    Observable<JsonElement> post(@Path("url") String url);
}
