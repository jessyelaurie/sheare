package com.jessyend.sheare.PlaylistView;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jessyend.sheare.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.models.Track;


/**
 * The Tracks Adapter for the Track view.
 * @author Jessye Nana Davies <nanadav2@illinois.edu>
 */
public class TracksAdapter extends RecyclerView.Adapter<TracksAdapter.MyViewHolder> {

    private final List<Track> mItems = new ArrayList<>();
    private final String mPlaylistId;
    private final Context mContext;
    List<Map<String, String>> mData;

    private Player mPlayer;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mPlayer = ((PlayerService.PlayerBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mPlayer = null;
        }
    };

    /**
     * Custom view holder class
     */
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView title;
        public final TextView subtitle;
        public final ImageView image;

        /**
         * constructor for each viewHolder
         * @param itemView the individual item
         */
        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.entity_title);
            subtitle = (TextView) itemView.findViewById(R.id.entity_subtitle);
            image = (ImageView) itemView.findViewById(R.id.entity_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int itemPosition = getAdapterPosition();
            String previewUrl = mData.get(itemPosition).get("uri");
            if (previewUrl == null) {
                Toast.makeText(mContext, "This track doesn't have a preview.", Toast.LENGTH_LONG)
                        .show();
                return;
            }

            if (mPlayer == null) return;

            String currentTrackUrl = mPlayer.getCurrentTrack();

            if (currentTrackUrl == null || !currentTrackUrl.equals(previewUrl)) {
                mPlayer.play(previewUrl);
            } else if (mPlayer.isPlaying()) {
                mPlayer.pause();
            } else {
                mPlayer.resume();
            }

            //TrackPlayer player = new TrackPlayer(mContext, track);
            //player.playTrack(track);
            //Toast.makeText(mContext, mData.get(itemPosition).get("title")+" clicked (sent to player)", Toast.LENGTH_LONG).show();
        }
    }

    public interface ItemSelectedListener {
        void onItemSelected(View itemView, Track item);
    }

    /**
     * Adapter's constructor
     * @param context the fragment's context
     * @param playlistId the Spotify playlist id
     * @param data data to display on the list
     */
    public TracksAdapter(Context context, String playlistId,
                         List<Map<String, String>> data)
    {
        mContext = context;
        mPlaylistId = playlistId;
        mData = data;
        mContext.bindService(PlayerService.getIntent(mContext), mServiceConnection, Activity.BIND_AUTO_CREATE);


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.title.setText(mData.get(position).get("title"));
        holder.subtitle.setText(mData.get(position).get("subtitle"));
        Picasso.with(mContext).load(mData.get(position).get("imageUrl")).into(holder.image);
    }

    /**
     * @return the recyclerView's size
     */
    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * Updates the list
     * @param data
     */
    public void swap(List<Map<String, String>> data)
    {
        mData = data;
        notifyDataSetChanged();
    }
}
