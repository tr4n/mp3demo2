package com.example.mp3demo.ui.audios;

import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mp3demo.data.model.Audio;
import com.example.mp3demo.ui.BaseActivity;
import com.example.mp3demo.util.Constant;
import com.example.mp3mvp.R;

import java.util.List;

public class AudiosActivity extends BaseActivity implements AudiosContract.View, AudioAdapter.ItemClickListener, View.OnClickListener {

    private static final int REQUEST_PERMISSION = 1;

    private AudioAdapter mAudioAdapter;
    private AudiosPresenter mAudiosPresenter;

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
        mAudiosPresenter = new AudiosPresenter(this);
        mRecyclerAudio = findViewById(R.id.recycler_audio);
        mControlAudio = findViewById(R.id.image_control_audio);
        mCurrentAudioTitle = findViewById(R.id.text_current_audio_title);
        mCurrentAudioArtist = findViewById(R.id.text_current_audio_artist);
        mCurrentAudioTab = findViewById(R.id.constraint_playing);
        mStopAudio = findViewById(R.id.image_stop_audio);
    }

    @Override
    protected void initData() {
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                finish();
            }
        }
    }


    @Override
    public void showCurrentAudioTab(Audio audio) {
        mCurrentAudioTab.setVisibility(View.VISIBLE);
        mCurrentAudioTitle.setText(audio.getTitle());
        mCurrentAudioArtist.setText(audio.getArtist());
    }

    @Override
    public void showExternalAudios(List<Audio> audios) {
        mAudioAdapter.updateData(audios);
    }

    @Override
    public void hideCurrentAudioTab() {
        mCurrentAudioTab.setVisibility(View.GONE);
        mAudiosPresenter.stopAudio();
    }

    @Override
    public void updateCurrentAudioStatus(int status) {
        mCurrentAudioTab.setVisibility(View.VISIBLE);
        if (status == Constant.AUDIO_PLAYING)
            mControlAudio.setImageResource(R.drawable.ic_pause_circle_filled_24dp);
        else
            mControlAudio.setImageResource(R.drawable.ic_play_circle_filled_24dp);
    }

    @Override
    public void setPresenter(AudiosContract.Presenter presenter) {
        mAudiosPresenter = (AudiosPresenter) presenter;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAudiosPresenter.bindService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAudiosPresenter.start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_control_audio:
                mAudiosPresenter.controlPlayer();
                break;
            case R.id.image_stop_audio:
                hideCurrentAudioTab();
                break;
        }
    }

    @Override
    public void onItemClicked(Audio audio) {
        final String message = audio.getTitle() + "-" + audio.getArtist();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        mAudiosPresenter.startAudio(audio);
        showCurrentAudioTab(audio);
        updateCurrentAudioStatus(Constant.AUDIO_PLAYING);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAudiosPresenter.unbindService();
    }

    @Override
    protected void onDestroy() {
        mAudiosPresenter.destroyService();
        super.onDestroy();
    }
}
