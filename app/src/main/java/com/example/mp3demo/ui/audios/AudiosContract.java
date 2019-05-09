package com.example.mp3demo.ui.audios;

import com.example.mp3demo.BasePresenter;
import com.example.mp3demo.BaseView;
import com.example.mp3demo.data.model.Audio;

import java.util.List;

interface AudiosContract {
    interface View extends BaseView<Presenter> {
        boolean isPermissionGranted(String[] permissions);

        void showExternalAudios(List<Audio> audios);

        void showCurrentAudioTab(Audio audio);

        void hideCurrentAudioTab();

        void setPauseAudioState();

        void setPlayingAudioState();

        void updateCurrentAudioTab();

        void startAudio(Audio audio);

        void stopAudio();

        void toast(String message);

        void destroyService();
    }

    interface Presenter extends BasePresenter {
        void loadExternalAudios();

        void setCurrentAudioState(int state);

        void updateCurrentAudio(int state, Audio audio);

        void changeAudioState();
    }
}
