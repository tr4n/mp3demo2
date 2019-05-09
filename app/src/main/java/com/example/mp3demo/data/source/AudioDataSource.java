package com.example.mp3demo.data.source;

import android.support.annotation.NonNull;

import com.example.mp3demo.data.model.Audio;
import com.example.mp3demo.data.model.OnDataLoadedCallback;

import java.util.List;

public interface AudioDataSource {
    void getAllAudios(@NonNull OnDataLoadedCallback<List<Audio>> callback);
}
