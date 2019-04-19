package com.example.mp3demo.data.repository;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.mp3demo.data.model.OnDataLoadedCallback;
import com.example.mp3demo.data.source.AudioDataSource;
import com.example.mp3demo.data.source.local.AudioLocalDataSource;

public class AudiosRepository implements AudioDataSource {

    private final Context mContext;

    public AudiosRepository(Context context) {
        mContext = context;
    }

    @Override
    public boolean getAllAudios(@NonNull OnDataLoadedCallback callback) {
        return AudioLocalDataSource.getInstance(mContext).getAllExternalAudios(callback);
    }
}
