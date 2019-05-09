package com.example.mp3demo.data.source.local;

import android.os.AsyncTask;

import com.example.mp3demo.data.model.Audio;
import com.example.mp3demo.data.model.OnDataLoadedCallback;

import java.util.List;

public class ReadExternalAudiosTask extends AsyncTask<Void, Void, List<Audio>> {

    private ContentDAO mContentDAO;
    private OnDataLoadedCallback<List<Audio>> mCallback;

    public ReadExternalAudiosTask(ContentDAO mContentDAO, OnDataLoadedCallback<List<Audio>> mCallback) {
        this.mContentDAO = mContentDAO;
        this.mCallback = mCallback;
    }

    @Override
    protected List<Audio> doInBackground(Void... voids) {
        return mContentDAO.getExternalAudios();
    }

    @Override
    protected void onPostExecute(List<Audio> audios) {
        if (audios == null) {
            mCallback.onDataNotAvailable(new NullPointerException());
        } else {
            mCallback.onDataLoaded(audios);
        }
    }
}
