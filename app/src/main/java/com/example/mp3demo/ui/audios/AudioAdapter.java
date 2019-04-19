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

import java.util.ArrayList;
import java.util.List;

public class AudioAdapter extends BaseRecyclerViewAdapter<AudioAdapter.AudioViewHolder> {
    private final List<Audio> mAudioModels = new ArrayList<>();
    private final ItemClickListener mItemClickListener;

    AudioAdapter(Context mContext) {
        super(mContext);
        mItemClickListener = (ItemClickListener) mContext;
    }


    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View convertView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_audio, viewGroup, false);
        return new AudioViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder audioViewHolder, int i) {
        audioViewHolder.onBindData(mAudioModels.get(i), mItemClickListener);
    }

    @Override
    public int getItemCount() {
        return mAudioModels.size();
    }

    void updateData(List<Audio> audio) {
        mAudioModels.clear();
        mAudioModels.addAll(audio);
        notifyDataSetChanged();
    }

    static class AudioViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTitle;
        private final TextView mArtist;

        AudioViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.text_title);
            mArtist = itemView.findViewById(R.id.text_artist);

        }

        void onBindData(final Audio audio, final ItemClickListener mItemClickListener) {
            mTitle.setText(audio.getTitle());
            mArtist.setText(audio.getArtist());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClicked(audio);
                }
            });
        }


    }

    public interface ItemClickListener {
        void onItemClicked(Audio audio);
    }
}
