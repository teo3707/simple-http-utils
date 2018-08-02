package com.newt.http;

import io.reactivex.disposables.Disposable;


public interface  Callback<T> {
    default void success(T value) {};

    default void error(Throwable e) {};

    default void before() {};

    default void before(Disposable disposable) {
        before();
    }

    default void after() {};
}
