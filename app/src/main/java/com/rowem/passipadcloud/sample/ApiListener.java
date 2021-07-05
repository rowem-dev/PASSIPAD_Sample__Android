package com.rowem.passipadcloud.sample;

public interface ApiListener<T extends ApiResponse>{
    void onResult(T res);
}
