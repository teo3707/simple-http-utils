package com.newt.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;


public class Http {

    private static Http instance;
    private Service service;
    private Http() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor())
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();

        this.service = retrofit.create(Service.class);
    }

    public static synchronized Http getInstance() {
        if (instance == null) {
            instance = new Http();
        }

        return instance;
    }

    public static <T> void post(String url, Callback<T> callback) {
        getInstance().service
                .post(url)
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<JsonElement>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        callback.before(d);
                    }

                    @Override
                    public void onNext(JsonElement value) {
                        Gson gson = new GsonBuilder().create();
                        Type[] interfaces = callback.getClass().getGenericInterfaces();
                        Type type = ((ParameterizedType)interfaces[0]).getActualTypeArguments()[0];
                        if (type.equals(String.class)) {
                            callback.success((T)value.toString());
                        } else {
                            callback.success(gson.fromJson(value, type));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.error(e);
                    }

                    @Override
                    public void onComplete() {
                        callback.after();
                    }
                });
    }

    // Test Bean
    class User {
        private String name;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static void main(String ...args) throws Exception {
        // Server: see app.py

        // test return bean
        post("user", new Callback<User>() {
            @Override
            public void success(User user) {
                System.out.println("user name: " + user.getName());
            }
        });

        // test return String
        post("user", new Callback<String>() {
            @Override
            public void success(String value) {
                System.out.println(value);
            }
        });

        post("users", new Callback<String>() {
            @Override
            public void success(String value) {
                System.out.println(value);
            }
        });

        // Test List
        post("users", new Callback<List<User>>() {
            @Override
            public void success(List<User> value) {
                for(User user : value)
                    System.out.println("[in list] user: " + user.getName());
            }
        });

        Thread.sleep(1000);
    }
}
