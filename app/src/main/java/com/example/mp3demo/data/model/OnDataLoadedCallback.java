package com.example.mp3demo.data.model;

public interface OnDataLoadedCallback<T> {

    void onDataLoaded(T data);

    void onDataNotAvailable();
}