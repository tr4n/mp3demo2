package com.example.mp3demo.data.source.local;

import android.support.annotation.NonNull;

import com.example.mp3demo.data.model.Audio;
import com.example.mp3demo.data.model.OnDataLoadedCallback;
import com.example.mp3demo.data.source.AudioDataSource;

import java.util.List;

public class AudioLocalDataSource implements AudioDataSource {

    private static AudioLocalDataSource sInstance;
    private ContentDAO mContentDao;

    private AudioLocalDataSource(ContentDAO contentDAO) {
        mContentDao = contentDAO;
    }

    public static AudioLocalDataSource getInstance(ContentDAO contentDAO) {
        if (sInstance == null) {
            sInstance = new AudioLocalDataSource(contentDAO);
        }
        return sInstance;
    }

    @Override
    public void getAllAudios(@NonNull OnDataLoadedCallback<List<Audio>> callback) {
        new ReadExternalAudiosTask(mContentDao, callback).execute();
    }
}
