package com.example.mp3demo.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.example.mp3demo.data.model.Audio;
import com.example.mp3demo.ui.audios.AudiosActivity;
import com.example.mp3demo.util.Constant;
import com.example.mp3mvp.R;

import java.io.IOException;

public class AudioPlayerService extends Service implements AudioPlayerController {

    private static final String CHANNEL_ID = "com.example.demomp3player";
    private static final int NOTIFICATION_ID = 0;
    private final IBinder mBinder = new PlayAudioBinder();
    private static int sCurrentPosition = 0;
    private static Audio sCurrentAudio;
    private MediaPlayer mMediaPlayer;

    public AudioPlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final Audio request = intent.getExtras().getParcelable(Constant.EXTRA_AUDIO);
        if (null != request) {
            sCurrentAudio = request;
            create();
            start();
            pushNotification(sCurrentAudio);
        }
        return START_NOT_STICKY;
    }

    @Override
    public boolean create() {
        if (null != mMediaPlayer) mMediaPlayer.release();
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setLooping(true);
        try {
            mMediaPlayer.setDataSource(sCurrentAudio.getPath());
        } catch (IOException e) {
            mMediaPlayer = null;
            return false;
        }
        return true;
    }

    @Override
    public boolean start() {
        if (null == mMediaPlayer) return false;
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        mMediaPlayer.prepareAsync();
        return true;
    }

    @Override
    public boolean pause() {
        if (null == mMediaPlayer || mMediaPlayer.isPlaying()) return false;
        mMediaPlayer.pause();
        sCurrentPosition = mMediaPlayer.getCurrentPosition();
        return true;
    }

    @Override
    public boolean stop() {
        if (null == mMediaPlayer) return false;
        mMediaPlayer.release();
        return true;
    }

    @Override
    public boolean play() {
        if (null == mMediaPlayer) return false;
        mMediaPlayer.start();
        mMediaPlayer.seekTo(sCurrentPosition);
        return true;
    }

    @Override
    public Audio getCurrent() {
        return sCurrentAudio;
    }

    @Override
    public int getStatus() {
        return (null == sCurrentAudio || null == mMediaPlayer) ? Constant.AUDIO_EMPTY
                : mMediaPlayer.isPlaying() ? Constant.AUDIO_PLAYING
                : Constant.AUDIO_PAUSE;
    }

    private void pushNotification(Audio audio) {
        Intent notificationIntent = new Intent(this, AudiosActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, 0);

        onCreateNotificationChannel();

        Notification notification = getNotification(audio, pendingIntent);

        startForeground(NOTIFICATION_ID, notification);
    }

    private Notification getNotification(Audio audio, PendingIntent pendingIntent) {
        RemoteViews notificationLayout
                = new RemoteViews(getPackageName(), R.layout.notification_audio_player);
        notificationLayout.setTextViewText(R.id.text_title, audio.getTitle());
        notificationLayout.setTextViewText(R.id.text_artist, audio.getArtist());
        notificationLayout.setImageViewResource(R.id.image_control, R.drawable.ic_music_note_24dp);

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getText(R.string.notification_title))
                .setContentText(getText(R.string.notification_message))
                .setSmallIcon(R.drawable.ic_music_note_24dp)
                .setCustomContentView(notificationLayout)
                .setContentIntent(pendingIntent)
                .setTicker(getText(R.string.ticker_text))
                .setPriority(Notification.DEFAULT_ALL)
                .build();
    }

    private void onCreateNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationChannel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public class PlayAudioBinder extends Binder {
        public AudioPlayerService getService() {
            return AudioPlayerService.this;
        }
    }
}
