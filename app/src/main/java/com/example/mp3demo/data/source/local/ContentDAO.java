package com.example.mp3demo.data.source.local;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.mp3demo.data.model.Audio;

import java.util.ArrayList;
import java.util.List;

public class ContentDAO {
    private ContentResolver mContentResolver;

    public ContentDAO(Context context) {
        mContentResolver = context.getContentResolver();
    }

    public List<Audio> getExternalAudios() {
        List<Audio> audios = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = new StringBuilder(MediaStore.Audio.Media.IS_MUSIC)
                .append(" != 0")
                .toString();

        Cursor cursor = mContentResolver.query(uri, null, selection, null, null);

        if (null != cursor) {
            while (cursor.moveToNext()) {
                int titleIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                int artistIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                int dataIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);

                audios.add(new Audio(
                        cursor.getString(titleIndex),
                        cursor.getString(artistIndex),
                        cursor.getString(dataIndex)
                ));
            }
            cursor.close();
        }
        return audios;
    }
}
