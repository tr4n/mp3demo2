package com.example.mp3demo.ui.audios;

import com.example.mp3demo.data.model.Audio;
import com.example.mp3demo.data.model.OnDataLoadedCallback;
import com.example.mp3demo.data.repository.AudiosRepository;
import com.example.mp3demo.util.Constant;

import java.util.List;

public class AudiosPresenter implements AudiosContract.Presenter {

    private static int sCurrentAudioState = Constant.AUDIO_EMPTY;
    private AudiosContract.View mAudioContractView;
    private AudiosRepository mAudiosRepository;

    public AudiosPresenter(AudiosContract.View audioContractView, AudiosRepository audiosRepository) {
        mAudioContractView = audioContractView;
        mAudiosRepository = audiosRepository;
        mAudioContractView.setPresenter(this);
    }

    @Override
    public void start() {
        loadExternalAudios();
    }

    @Override
    public void loadExternalAudios() {
        mAudiosRepository.getAllAudios(new OnDataLoadedCallback<List<Audio>>() {
            @Override
            public void onDataLoaded(List<Audio> data) {
                mAudioContractView.showExternalAudios(data);
            }

            @Override
            public void onDataNotAvailable(Exception exception) {
                mAudioContractView.toast(exception.getMessage());
            }
        });
    }

    @Override
    public void setCurrentAudioState(int state) {
        sCurrentAudioState = state;
    }

    @Override
    public void updateCurrentAudio(int state, Audio audio) {
        switch (state) {
            case Constant.AUDIO_EMPTY:
                mAudioContractView.hideCurrentAudioTab();
                break;
            case Constant.AUDIO_PLAYING:
                mAudioContractView.showCurrentAudioTab(audio);
                mAudioContractView.setPlayingAudioState();
                break;
            case Constant.AUDIO_PAUSE:
                mAudioContractView.showCurrentAudioTab(audio);
                mAudioContractView.setPauseAudioState();
                break;
        }
    }

    @Override
    public void changeAudioState() {
        switch (sCurrentAudioState) {
            case Constant.AUDIO_PLAYING:
                mAudioContractView.setPauseAudioState();
                break;
            case Constant.AUDIO_PAUSE:
                mAudioContractView.setPlayingAudioState();
                break;
        }
    }
}
