package com.example.mp3demo.data.source.local;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.mp3demo.data.model.Audio;

import java.util.ArrayList;
import java.util.List;

public class LocalDataHandler {

    private static LocalDataHandler sInstance;
    private final Context mContext;

    private LocalDataHandler(Context context) {
        mContext = context;
    }

    public static LocalDataHandler getInstance(Context context) {
        if (null == sInstance) {
            sInstance = new LocalDataHandler(context);
        }
        return sInstance;
    }

    public List<Audio> getAllExternalAudios() {
        List<Audio> audioModels = new ArrayList<>();
        ContentResolver resolver = mContext.getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        Cursor cursor = resolver.query(uri, null, selection, null, null);

        if (null != cursor && cursor.moveToFirst()) {
            do {
                int titleIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                int artistIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                int dataIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);

                audioModels.add(new Audio(
                        cursor.getString(titleIndex),
                        cursor.getString(artistIndex),
                        cursor.getString(dataIndex)
                ));
            } while (cursor.moveToNext());

            cursor.close();
        }
        return audioModels;
    }
}
