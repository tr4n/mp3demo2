package com.example.mp3demo.ui.audios;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;

import android.widget.Toast;

import com.example.mp3demo.data.model.Audio;
import com.example.mp3demo.data.model.OnDataLoadedCallback;
import com.example.mp3demo.data.repository.AudiosRepository;
import com.example.mp3demo.service.AudioPlayerService;
import com.example.mp3demo.util.Constant;
import com.example.mp3mvp.R;

import java.util.List;

import static android.content.Context.BIND_AUTO_CREATE;

public class AudiosPresenter implements AudiosContract.Presenter {

    private static final String[] PERMISSIONS = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int REQUEST_PERMISSION = 1;
    private final AudiosContract.View mAudioContractView;
    private final AudiosRepository mAudiosRepository;
    private final Context mContext;
    private static int sAudioStatus = Constant.AUDIO_EMPTY;
    private AudioPlayerService mAudioPlayerService;

    AudiosPresenter(AudiosContract.View mAudioContractView) {
        this.mAudioContractView = mAudioContractView;
        this.mContext = (Context) mAudioContractView;
        this.mAudiosRepository = new AudiosRepository(mContext);
        mAudioContractView.setPresenter(this);
    }

    @Override
    public boolean isPermissionGranted(String[] permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mContext.checkSelfPermission(permissions[0])
                    == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(
                        (Activity) mContext,
                        permissions,
                        REQUEST_PERMISSION
                );
                return false;
            }
        }
        return true;
    }

    @Override
    public void start() {
        if (isPermissionGranted(PERMISSIONS)) loadExternalAudios();
    }

    @Override
    public void loadExternalAudios() {
        mAudiosRepository.getAllAudios(new OnDataLoadedCallback<List<Audio>>() {
            @Override
            public void onDataLoaded(List<Audio> data) {
                mAudioContractView.showExternalAudios(data);
            }

            @Override
            public void onDataNotAvailable() {
                Toast.makeText(mContext, mContext.getString(R.string.message_data_not_available), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void bindService() {
        Intent intent = new Intent(mContext, AudioPlayerService.class);
        mContext.bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    public boolean updateCurrentAudioTab() {
        if (mAudioPlayerService == null) return false;
        sAudioStatus = mAudioPlayerService.getStatus();
        switch (sAudioStatus) {
            case Constant.AUDIO_EMPTY:
                mAudioContractView.hideCurrentAudioTab();
                break;
            case Constant.AUDIO_PLAYING:
                mAudioContractView.showCurrentAudioTab(mAudioPlayerService.getCurrent());
                mAudioContractView.updateCurrentAudioStatus(Constant.AUDIO_PLAYING);
                break;
            case Constant.AUDIO_PAUSE:
                mAudioContractView.showCurrentAudioTab(mAudioPlayerService.getCurrent());
                mAudioContractView.updateCurrentAudioStatus(Constant.AUDIO_PAUSE);
                break;
        }
        return true;
    }

    @Override
    public boolean startAudio(Audio audio) {
        if (audio == null) return false;
        sAudioStatus = Constant.AUDIO_PLAYING;
        Intent intent = new Intent(mContext, AudioPlayerService.class);
        intent.putExtra(Constant.EXTRA_AUDIO, audio);
        mContext.startService(intent);
        return true;
    }

    @Override
    public boolean controlPlayer() {
        switch (sAudioStatus) {
            case Constant.AUDIO_EMPTY:
                mAudioContractView.showCurrentAudioTab(mAudioPlayerService.getCurrent());
                return true;
            case Constant.AUDIO_PLAYING:
                sAudioStatus = Constant.AUDIO_PAUSE;
                mAudioContractView.updateCurrentAudioStatus(Constant.AUDIO_PAUSE);
                mAudioPlayerService.pause();
                return true;
            case Constant.AUDIO_PAUSE:
                sAudioStatus = Constant.AUDIO_PLAYING;
                mAudioContractView.updateCurrentAudioStatus(Constant.AUDIO_PLAYING);
                mAudioPlayerService.play();
                return true;
        }
        return false;
    }

    @Override
    public boolean stopAudio() {
        sAudioStatus = Constant.AUDIO_EMPTY;
        return mAudioPlayerService.stop();
    }

    @Override
    public void unbindService() {
        mContext.unbindService(mServiceConnection);
    }

    @Override
    public boolean destroyService() {
        if (sAudioStatus != Constant.AUDIO_EMPTY) return false;

        Intent intent = new Intent(mContext, AudioPlayerService.class);
        mContext.stopService(intent);
        return true;
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            AudioPlayerService.PlayAudioBinder binder = (AudioPlayerService.PlayAudioBinder) service;
            mAudioPlayerService = binder.getService();
            updateCurrentAudioTab();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
