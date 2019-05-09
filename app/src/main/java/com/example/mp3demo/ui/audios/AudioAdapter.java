package com.example.mp3demo.ui.audios;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mp3demo.data.model.Audio;
import com.example.mp3demo.ui.BaseRecyclerViewAdapter;
import com.example.mp3mvp.R;

public class AudioAdapter extends BaseRecyclerViewAdapter<Audio, AudioAdapter.AudioViewHolder> {

    private final ItemClickListener mItemClickListener;

    public AudioAdapter(Context context) {
        mItemClickListener = (ItemClickListener) context;
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View convertView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_audio, viewGroup, false);
        return new AudioViewHolder(convertView, mItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder audioViewHolder, int i) {
        audioViewHolder.onBindData(getItem(i));
    }

    static class AudioViewHolder extends RecyclerView.ViewHolder {

        private Audio mAudio;
        private TextView mTitle;
        private TextView mArtist;

        AudioViewHolder(@NonNull View itemView, final ItemClickListener itemClickListener) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.text_title);
            mArtist = itemView.findViewById(R.id.text_artist);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mAudio) itemClickListener.onItemClicked(mAudio);
                }
            });
        }

        void onBindData(Audio audio) {
            mAudio = audio;
            mTitle.setText(audio.getTitle());
            mArtist.setText(audio.getArtist());
        }
    }

    interface ItemClickListener {
        void onItemClicked(Audio audio);
    }
}
