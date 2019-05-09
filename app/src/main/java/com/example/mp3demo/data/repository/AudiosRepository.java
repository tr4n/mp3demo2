package com.example.mp3demo.data.repository;

import android.support.annotation.NonNull;

import com.example.mp3demo.data.model.Audio;
import com.example.mp3demo.data.model.OnDataLoadedCallback;
import com.example.mp3demo.data.source.AudioDataSource;

import java.util.List;

public class AudiosRepository implements AudioDataSource {

    private AudioDataSource mAudioDataSource;

    public AudiosRepository(AudioDataSource audioDataSource) {
        mAudioDataSource = audioDataSource;
    }

    @Override
    public void getAllAudios(@NonNull OnDataLoadedCallback<List<Audio>> callback) {
        mAudioDataSource.getAllAudios(callback);
    }
}
