package com.example.mp3demo.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.mp3demo.data.model.Audio;
import com.example.mp3demo.data.model.OnDataLoadedCallback;

import java.util.List;

public class AudioLocalDataSource  {

    private final Context mContext;
    private static AudioLocalDataSource sInstance;

    private AudioLocalDataSource(Context context) {
        this.mContext = context;
    }

    public boolean getAllExternalAudios(@NonNull OnDataLoadedCallback callback) {
        List<Audio> externalAudios = LocalDataHandler.getInstance(mContext).getAllExternalAudios();
        if (externalAudios.isEmpty()) {
            callback.onDataNotAvailable();
            return false;
        }
        callback.onDataLoaded(externalAudios);
        return true;
    }

    public static AudioLocalDataSource getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new AudioLocalDataSource(context);
        }
        return sInstance;
    }
}
