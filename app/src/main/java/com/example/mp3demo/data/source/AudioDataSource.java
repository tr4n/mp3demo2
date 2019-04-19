package com.example.mp3demo.data.source;

import android.support.annotation.NonNull;

import com.example.mp3demo.data.model.OnDataLoadedCallback;

public interface AudioDataSource {
    boolean getAllAudios(@NonNull OnDataLoadedCallback callback);
}
