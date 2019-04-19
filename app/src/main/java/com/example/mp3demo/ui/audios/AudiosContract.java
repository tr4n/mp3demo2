package com.example.mp3demo.ui.audios;

import com.example.mp3demo.BasePresenter;
import com.example.mp3demo.BaseView;
import com.example.mp3demo.data.model.Audio;

import java.util.List;

interface AudiosContract {
    interface View extends BaseView<Presenter> {
        void showExternalAudios(List<Audio> audios);

        void showCurrentAudioTab(Audio audio);

        void hideCurrentAudioTab();

        void updateCurrentAudioStatus(int status);

    }

    interface Presenter extends BasePresenter {
        boolean isPermissionGranted(String[] permissions);

        void loadExternalAudios();

        void bindService();

        boolean updateCurrentAudioTab();

        boolean startAudio(Audio audio);

        boolean controlPlayer();

        boolean stopAudio();

        void unbindService();

        boolean destroyService();

    }
}
