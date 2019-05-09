package com.example.mp3demo.ui.audios;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mp3demo.data.model.Audio;
import com.example.mp3demo.data.repository.AudiosRepository;
import com.example.mp3demo.data.source.AudioDataSource;
import com.example.mp3demo.data.source.local.AudioLocalDataSource;
import com.example.mp3demo.data.source.local.ContentDAO;
import com.example.mp3demo.service.AudioPlayerService;
import com.example.mp3demo.ui.BaseActivity;
import com.example.mp3demo.util.Constant;
import com.example.mp3mvp.R;

import java.util.List;

public class AudiosActivity extends BaseActivity
        implements AudiosContract.View, AudioAdapter.ItemClickListener, View.OnClickListener {

    private static final String[] PERMISSIONS = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final String EXTRA_AUDIO = "com.example.mp3demo.AUDIO";
    private static final int REQUEST_PERMISSION = 1;

    private AudioAdapter mAudioAdapter;
    private AudiosPresenter mAudiosPresenter;
    private AudioPlayerService mAudioPlayerService;

    private RecyclerView mRecyclerAudio;
    private ImageView mControlAudio, mStopAudio;
    private TextView mCurrentAudioTitle, mCurrentAudioArtist;
    private ConstraintLayout mCurrentAudioTab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_audios;
    }

    @Override
    protected void initComponent() {
        mRecyclerAudio = findViewById(R.id.recycler_audio);
        mControlAudio = findViewById(R.id.image_control_audio);
        mCurrentAudioTitle = findViewById(R.id.text_current_audio_title);
        mCurrentAudioArtist = findViewById(R.id.text_current_audio_artist);
        mCurrentAudioTab = findViewById(R.id.constraint_playing);
        mStopAudio = findViewById(R.id.image_stop_audio);
    }

    @Override
    protected void initData() {
        ContentDAO contentDAO = new ContentDAO(this);
        AudioDataSource dataSource = AudioLocalDataSource.getInstance(contentDAO);
        AudiosRepository repository = new AudiosRepository(dataSource);
        mAudiosPresenter = new AudiosPresenter(this, repository);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
        );
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                this,
                linearLayoutManager.getOrientation()
        );
        mAudioAdapter = new AudioAdapter(this);
        mRecyclerAudio.addItemDecoration(dividerItemDecoration);
        mRecyclerAudio.setLayoutManager(linearLayoutManager);
        mRecyclerAudio.setAdapter(mAudioAdapter);

        mControlAudio.setOnClickListener(this);
        mStopAudio.setOnClickListener(this);
    }

    @Override
    public boolean isPermissionGranted(String[] permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permissions[0])
                    == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSION);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) finish();
        }
    }

    @Override
    public void showExternalAudios(List<Audio> audios) {
        mAudioAdapter.updateData(audios);
    }

    @Override
    public void showCurrentAudioTab(Audio audio) {
        mCurrentAudioTab.setVisibility(View.VISIBLE);
        mCurrentAudioTitle.setText(audio.getTitle());
        mCurrentAudioArtist.setText(audio.getArtist());
    }

    @Override
    public void hideCurrentAudioTab() {
        mAudiosPresenter.setCurrentAudioState(Constant.AUDIO_EMPTY);
        mCurrentAudioTab.setVisibility(View.GONE);
        stopAudio();
    }

    @Override
    public void setPauseAudioState() {
        mAudiosPresenter.setCurrentAudioState(Constant.AUDIO_PAUSE);
        mCurrentAudioTab.setVisibility(View.VISIBLE);
        mControlAudio.setImageResource(R.drawable.ic_play_circle_filled_24dp);
        mAudioPlayerService.pause();
    }

    @Override
    public void setPlayingAudioState() {
        mAudiosPresenter.setCurrentAudioState(Constant.AUDIO_PLAYING);
        mCurrentAudioTab.setVisibility(View.VISIBLE);
        mControlAudio.setImageResource(R.drawable.ic_pause_circle_filled_24dp);
        mAudioPlayerService.play();
    }

    @Override
    public void setPresenter(AudiosContract.Presenter presenter) {
        mAudiosPresenter = (AudiosPresenter) presenter;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, AudioPlayerService.class);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPermissionGranted(PERMISSIONS)) mAudiosPresenter.start();
    }

    @Override
    public void startAudio(Audio audio) {
        if (audio != null) {
            mAudiosPresenter.setCurrentAudioState(Constant.AUDIO_PLAYING);
            Intent intent = new Intent(this, AudioPlayerService.class);
            intent.putExtra(EXTRA_AUDIO, audio);
            startService(intent);
        }
    }

    @Override
    public void stopAudio() {
        mAudiosPresenter.setCurrentAudioState(Constant.AUDIO_EMPTY);
        mAudioPlayerService.stop();
    }

    @Override
    public void updateCurrentAudioTab() {
        if (mAudioPlayerService != null) {
            mAudiosPresenter.updateCurrentAudio(
                    mAudioPlayerService.getState(),
                    mAudioPlayerService.getCurrent()
            );
        }
    }

    @Override
    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_control_audio:
                mAudiosPresenter.changeAudioState();
                break;
            case R.id.image_stop_audio:
                hideCurrentAudioTab();
                break;
        }
    }

    @Override
    public void onItemClicked(Audio audio) {
        final String message = new StringBuffer(audio.getTitle())
                .append("-")
                .append(audio.getArtist())
                .toString();
        toast(message);
        startAudio(audio);
        showCurrentAudioTab(audio);
        setPlayingAudioState();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(mServiceConnection);
    }

    @Override
    protected void onDestroy() {
        destroyService();
        super.onDestroy();
    }

    @Override
    public void destroyService() {
        if (mAudioPlayerService.getState() == Constant.AUDIO_EMPTY) {
            Intent intent = new Intent(this, AudioPlayerService.class);
            stopService(intent);
        }
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
