package com.newt.http;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;

public class Interceptor implements okhttp3.Interceptor{
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        String body = response.body().string();
        JsonObject jsonObject = new JsonParser().parse(body).getAsJsonObject();
        return response.newBuilder()
                .body(ResponseBody.create(MediaType.parse("UTF-8"), jsonObject.get("data").toString()))
                .build();
    }
}
